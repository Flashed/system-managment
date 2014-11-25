package ru.system.managment.common.socket.acceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

/**
 * Acceptor's implementation
 */
public class TcpNonblockingAcceptor implements Acceptor {

  private static final Logger logger = LoggerFactory.getLogger(TcpNonblockingAcceptor.class);

  private AcceptConfig config;

  private Selector selector;

  private boolean started;

  private AcceptorListener listener;

  public TcpNonblockingAcceptor() {
  }

  public TcpNonblockingAcceptor(AcceptConfig config) throws Exception {
    init(config);
  }

  @Override
  public void init(AcceptConfig config) throws Exception{
    try{
      if(config == null){
        throw new NullPointerException("config can not be null");
      }

      this.config = config;

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

  @Override
  public void start() throws Exception{
    try{
      if(started){
        logger.debug("Acceptor already started");
        return;
      }
      started = true;

      selector.select();

      Iterator selectedKeys = selector.selectedKeys().iterator();
      while(selectedKeys.hasNext()){
        SelectionKey key = (SelectionKey) selectedKeys.next();
        selectedKeys.remove();

        if(!key.isValid()){
          continue;
        }

        if(key.isAcceptable()){
          accept(key);
        } else if(key.isReadable()){
          read(key);
        } else {
          logger.warn("Got another key type: {}", key);
        }
      }
    }catch (Exception e){
      throw new Exception("Failed to start acceptor", e);
    }

    logger.info("Acceptor stop.");
  }


  private void accept(SelectionKey key) throws IOException {
    ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();

    SocketChannel clientChannel = serverChannel.accept();
    clientChannel.configureBlocking(false);

    clientChannel.register(selector, SelectionKey.OP_READ);

    logger.info("Accept \n {}", clientChannel);

    if(listener != null){
      listener.onAccept(clientChannel);
    }
  }

  private void read(SelectionKey key) throws IOException {
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
      return;
    }

    if(numRead == -1){
      clientChannel.close();
      key.cancel();
      logger.info("Closed connection with: \n {}", clientChannel);
      return;
    }

    if(listener != null){
      listener.onRead(readBuffer);
    }

  }

  @Override
  public void setListener(AcceptorListener listener) {
    this.listener = listener;
  }
}
