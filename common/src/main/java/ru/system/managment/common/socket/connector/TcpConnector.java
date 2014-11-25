package ru.system.managment.common.socket.connector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.system.managment.common.socket.model.SocketData;
import ru.system.managment.common.socket.reader.Reader;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * A implementation of config
 */
public class TcpConnector implements Connector{

  private static final Logger logger = LoggerFactory.getLogger(TcpConnector.class);

  private ConnectorListener listener;

  private ConnectorConfig config;

  private Reader reader;

  private boolean started;

  public TcpConnector(ConnectorConfig config) throws Exception {
    setConfig(config);
  }

  public TcpConnector() {
  }


  @Override
  public void connect()  throws Exception{
    if(started){
      logger.warn("Connector already started");
      return;
    }
    try{
      started = true;
      logger.info("Connect with: \n\t {}", config);

      SocketChannel socketChannel = SocketChannel.open(
              new InetSocketAddress(config.getHost(), config.getPort()));

      read(socketChannel);
      started = false;
    }catch (Exception e){
      started = false;
      throw new Exception("Failed to connect with: \n\t " + config, e);
    }
    logger.info("Disconnected with \n\t {}", config);
  }

  @Override
  public void disconnect()  throws Exception{

  }

  private void read(SocketChannel socketChannel) throws Exception {

    ByteBuffer buffer = ByteBuffer.allocate(config.getReadBufferSize());

    while(socketChannel.read(buffer) != -1){

      SocketData data = reader.read(buffer, socketChannel);
      if(data == null){
        continue;
      }
      if(listener != null){
        listener.onReadData(data);
      }
    }
  }

  public void setConfig(ConnectorConfig config) {
    this.config = config;
  }

  public void setListener(ConnectorListener listener) {
    this.listener = listener;
  }

  public void setReader(Reader reader) {
    this.reader = reader;
  }
}
