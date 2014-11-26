package ru.system.managment.common.socket.model;

import java.io.Serializable;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Set;

/**
 * Data read from socket or write to socket
 */
public class SocketData{

  private SocketChannel socketChannel;

  private Set<Object> objects;

  public SocketChannel getSocketChannel() {
    return socketChannel;
  }

  public void setSocketChannel(SocketChannel socketChannel) {
    this.socketChannel = socketChannel;
  }

  public Set<Object> getObjects() {
    if(objects == null){
      objects = new HashSet<Object>();
    }
    return objects;
  }

}
