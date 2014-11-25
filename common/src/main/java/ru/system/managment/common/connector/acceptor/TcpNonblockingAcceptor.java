package ru.system.managment.common.connector.acceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;

/**
 * Acceptor's implementation
 */
public class TcpNonblockingAcceptor implements Acceptor {

  private static final Logger logger = LoggerFactory.getLogger(TcpNonblockingAcceptor.class);

  private AcceptConfig config;

  private boolean started;

  @Override
  public void init(AcceptConfig config) throws Exception{
    try{
      if(config == null){
        throw new NullPointerException("config can not be null");
      }

      this.config = config;

      Selector selector = SelectorProvider.provider().openSelector();
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
      
    }catch (Exception e){
      throw new Exception("Failed to start acceptor", e);
    }
  }

}
