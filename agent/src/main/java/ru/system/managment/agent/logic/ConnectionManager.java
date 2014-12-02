package ru.system.managment.agent.logic;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.system.managment.common.socket.connector.Connector;
import ru.system.managment.common.socket.connector.ConnectorListener;
import ru.system.managment.common.socket.model.SocketData;
import ru.system.managment.common.socket.model.packages.IdentifySuccessPacket;
import ru.system.managment.common.socket.model.packages.IdentityPacket;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class ConnectionManager implements ConnectorListener {

  private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

  private Connector connector;

  private volatile boolean connected;

  private SocketChannel proxyChannel;

  public void openConnection() throws Exception{
    if(connected){
      return;
    }
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        while (true) {
          try {
            connector.connectAndRead();
          } catch (Exception e) {
            connected = false;
            logger.error("Error open connection", e);
          }
          try {
            Thread.sleep(3000);
          } catch (InterruptedException ignored) {}
        }
      }
    });
    thread.setName("Connector thread");
    thread.start();
  }

  private void sendIdInfo() throws Exception {
    if(connected){
      SocketData socketData = new SocketData();
      socketData.setSocketChannel(proxyChannel);
      socketData.getPackets().add(new IdentityPacket(IdentityPacket.ID_AGENT));
      connector.send(socketData);

    }
  }

  @Override
  public void onReadData(SocketData socketData) {
    Set<Object> packets = socketData.getPackets();
    for(Object packet: packets){
      if(packet instanceof IdentifySuccessPacket){
        logger.info("Get IdentifySuccessPacket");
      }
    }

  }

  @Override
  public void onConnected(SocketChannel socketChannel) {
    proxyChannel = socketChannel;
    connected = true;
    try{
      sendIdInfo();
    } catch (Exception e){
      logger.error("Error to send " + IdentityPacket.class.getName(), e);
    }
  }

  @Override
  public void onDisconnected(SocketChannel socketChannel) {
    try {
      socketChannel.close();
    } catch (IOException e) {
      logger.error("Error closing socket", e);
    }
    connected = false;
  }


  public void setConnector(Connector connector) {
    this.connector = connector;
  }
}

