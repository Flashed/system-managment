package ru.system.managment.common.socket.model;

import java.io.Serializable;
import java.nio.channels.SocketChannel;

/**
 * Data read from socket or write to socket
 */
public class SocketData{

  private SocketChannel socketChannel;

  private Serializable serializable;

  public SocketChannel getSocketChannel() {
    return socketChannel;
  }

  public void setSocketChannel(SocketChannel socketChannel) {
    this.socketChannel = socketChannel;
  }

  public Serializable getSerializable() {
    return serializable;
  }

  public void setSerializable(Serializable serializable) {
    this.serializable = serializable;
  }
}
