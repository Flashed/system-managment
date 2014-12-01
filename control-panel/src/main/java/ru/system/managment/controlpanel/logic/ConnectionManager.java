package ru.system.managment.controlpanel.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.system.managment.common.socket.connector.Connector;
import ru.system.managment.common.socket.connector.ConnectorListener;
import ru.system.managment.common.socket.model.SocketData;
import ru.system.managment.common.socket.model.packages.IdentityPacket;
import ru.system.managment.common.socket.model.packages.IdentifySuccessPacket;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class ConnectionManager implements Thread.UncaughtExceptionHandler, ConnectorListener{

  private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

  private ConnectionManagerListener listener;

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
        try {
          connector.connectAndRead();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    });
    thread.setName("Connector thread");
    thread.setUncaughtExceptionHandler(this);
    thread.start();
  }

  @Override
  public void uncaughtException(Thread t, Throwable e) {
    connected = false;
    if(listener != null){
      listener.errorConnection(e);
    }
  }

  private void sendIdInfo() throws Exception {
    if(connected){
      SocketData socketData = new SocketData();
      socketData.setSocketChannel(proxyChannel);
      socketData.getPackets().add(new IdentityPacket(IdentityPacket.ID_PANEL));
      connector.send(socketData);

    }
  }

  @Override
  public void onReadData(SocketData socketData) {
    Set<Object> packets = socketData.getPackets();
    for(Object packet: packets){
      if(packet instanceof IdentifySuccessPacket){
        if(listener != null){
          listener.onConnected();
        }
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
      logger.error("Error to send " + IdentityPacket.class.getName());
      if(listener != null){
        listener.errorConnection(e);
      }
    }
  }

  @Override
  public void onDisconnected(SocketChannel socketChannel) {
    try {
      socketChannel.close();
    } catch (IOException e) {
      logger.error("Error closing socket");
    }
    connected = false;
    if (listener != null) {
      listener.onDisconnected();
    }
  }


  public void setConnector(Connector connector) {
    this.connector = connector;
  }

  public void setListener(ConnectionManagerListener listener) {
    this.listener = listener;
  }
}

