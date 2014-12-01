package ru.system.managment.common.socket.acceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.system.managment.common.socket.model.SocketData;
import ru.system.managment.common.socket.reader.Reader;
import ru.system.managment.common.socket.sender.Sender;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;

/**
 * Acceptor's implementation
 */
public class TcpAcceptor implements Acceptor {

  private static final Logger logger = LoggerFactory.getLogger(TcpAcceptor.class);

  private AcceptConfig config;

  private Selector selector;

  private boolean started;

  private Set<AcceptorListener> listeners;

  private Reader reader;

  private Sender sender;

  public TcpAcceptor() {
  }

  public TcpAcceptor(AcceptConfig config) throws Exception {
    setConfig(config);
  }


  @Override
  public void acceptAndRead() throws Exception{
    try{
      if(started){
        logger.debug("Acceptor already started");
        return;
      }
      started = true;

      init();

      while(started){

        selector.select();

        Iterator selectedKeys = selector.selectedKeys().iterator();
        while(selectedKeys.hasNext()) {
          SelectionKey key = (SelectionKey) selectedKeys.next();
          selectedKeys.remove();

          if (!key.isValid()) {
            continue;
          }

          if (key.isAcceptable()) {
            accept(key);
          } else if (key.isReadable()) {
            read(key);
          } else {
            logger.warn("Got another key type: {}", key);
          }
        }
      }
    }catch (Exception e){
      started = false;
      throw new Exception("Failed to start acceptor", e);
    }
    started = false;
    logger.info("Acceptor stop.");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void send(SocketData socketData)  throws Exception{
    try {
      sender.send(socketData);
    } catch (Exception e) {
      throw new Exception("Failed to send data: \n\t " + socketData, e);
    }
  }


  private void accept(SelectionKey key) throws IOException {
    ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();

    SocketChannel clientChannel = serverChannel.accept();
    clientChannel.configureBlocking(false);

    clientChannel.register(selector, SelectionKey.OP_READ);

    logger.info("Accept \n {}", clientChannel);

    if(listeners != null){
      for(AcceptorListener listener: listeners){
        listener.onAccept(clientChannel);
      }
    }
  }

  private void read(SelectionKey key) throws Exception {
    SocketChannel clientChannel = (SocketChannel) key.channel();

    ByteBuffer readBuffer = ByteBuffer.allocate(config.getReadBufferSize());
    readBuffer.clear();

    int numRead;
    try{
      numRead = clientChannel.read(readBuffer);
    }catch (IOException e) {
      clientChannel.close();
      key.cancel();
      logger.error("Error read to buffer", e);
      if(listeners != null){
        for(AcceptorListener listener: listeners){
          listener.onDisconnect(clientChannel);
        }
      }
      return;
    }

    if(numRead == -1){
      clientChannel.close();
      key.cancel();
      logger.info("Closed connection with: \n {}", clientChannel);
      if(listeners != null){
        for(AcceptorListener listener: listeners){
          listener.onDisconnect(clientChannel);
        }
      }
      return;
    }

    SocketData data = reader.read(readBuffer, clientChannel);
    if(data ==null){
      return;
    }

    if(listeners != null){
      for(AcceptorListener listener: listeners){
        listener.onRead(data);
      }
    }

  }

  private void init() throws Exception{
    try{
      if(config == null){
        throw new NullPointerException("config can not be null");
      }

      selector = SelectorProvider.provider().openSelector();
      ServerSocketChannel socketChannel = ServerSocketChannel.open();
      socketChannel.configureBlocking(false);

      InetSocketAddress address = new InetSocketAddress(config.getPort());
      socketChannel.socket().bind(address);

      socketChannel.register(selector, SelectionKey.OP_ACCEPT);

      logger.info("Acceptor initialized with config \n {}", config);

    } catch (Exception e){
      throw new Exception("Failed to init acceptor", e);
    }


  }

  public void setConfig(AcceptConfig config) {
    this.config = config;
  }

  public void setListeners(Set<AcceptorListener> listeners) {
    this.listeners = listeners;
  }

  public void setReader(Reader reader) {
    this.reader = reader;
  }

  public void setSender(Sender sender) {
    this.sender = sender;
  }
}
