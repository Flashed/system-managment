package ru.system.managment.controlpanel.logic;

import ru.system.managment.common.socket.connector.Connector;

public class ConnectionManager implements Thread.UncaughtExceptionHandler{

  private ConnectionManagerListener listener;

  private Connector connector;

  private volatile boolean connected;

  public void openConnection() throws Exception{
    if(connected){
      return;
    }
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          connector.connectAndRead();
          connected = false;
          if (listener != null) {
            listener.onDisconnected();
          }
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    });
    thread.setName("Connector thread");
    thread.setUncaughtExceptionHandler(this);
    thread.start();
    connected = true;
    if(listener != null){
      Thread.sleep(3000);    //ждём ощибок
      if(connected){
        listener.onConnected();
      }
    }

  }

  public void setConnector(Connector connector) {
    this.connector = connector;
  }

  @Override
  public void uncaughtException(Thread t, Throwable e) {
    connected = false;
    if(listener != null){
      listener.errorConnection(e);
    }
  }

  public void setListener(ConnectionManagerListener listener) {
    this.listener = listener;
  }
}

