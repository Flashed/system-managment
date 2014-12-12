package ru.system.managment.common.socket.connector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.system.managment.common.socket.model.SocketData;
import ru.system.managment.common.socket.reader.Reader;
import ru.system.managment.common.socket.sender.Sender;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * A implementation of config
 */
public class TcpConnector implements Connector{

  private static final Logger logger = LoggerFactory.getLogger(TcpConnector.class);

  private Set<ConnectorListener> listeners;

  private ConnectorConfig config;

  private Reader reader;

  private Sender sender;

  private boolean started;

  public TcpConnector(ConnectorConfig config) throws Exception {
    setConfig(config);
  }

  public TcpConnector() {
  }

  @Override
  public void connectAndRead()  throws Exception{
    if(started){
      logger.warn("Connector already started");
      return;
    }
    try{
      started = true;
      logger.info("Connect with: \n\t {}", config);

      SocketChannel socketChannel = SocketChannel.open(
              new InetSocketAddress(config.getHost(), config.getPort()));

      if(listeners != null){
        for(ConnectorListener listener: listeners){
          listener.onConnected(socketChannel);
        }
      }

      read(socketChannel);

      if(listeners != null){
        for(ConnectorListener listener: listeners){
          listener.onDisconnected(socketChannel);
        }
      }

      if(socketChannel != null
              && socketChannel.isOpen()){
        try{
          socketChannel.close();
        } catch (Exception ignored){}
      }

      started = false;
    }catch (Exception e){
      started = false;
      throw new Exception("Failed to connect with: \n\t " + config, e);
    }
    logger.info("Disconnected with \n\t {}", config);
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

  @Override
  public void disconnect()  throws Exception{

  }

  private void read(SocketChannel socketChannel) throws Exception {

    ByteBuffer buffer = ByteBuffer.allocate(config.getReadBufferSize());

    while(socketChannel.read(buffer) != -1){

      SocketData data = reader.read(buffer, socketChannel);
      buffer.clear();
      if(data == null){
        continue;
      }
      if(listeners != null){
        for(ConnectorListener listener: listeners){
          listener.onReadData(data);
        }
      }
    }
  }

  public void setConfig(ConnectorConfig config) {
    this.config = config;
  }

  public void setListeners(Set<ConnectorListener> listeners) {
    this.listeners = listeners;
  }

  public void setReader(Reader reader) {
    this.reader = reader;
  }

  public void setSender(Sender sender) {
    this.sender = sender;
  }
}
