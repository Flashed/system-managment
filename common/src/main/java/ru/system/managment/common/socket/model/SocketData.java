package ru.system.managment.common.socket.model;

import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Set;

/**
 * Data read from socket or write to socket
 */
public class SocketData{

  private SocketChannel socketChannel;

  private Set<Object> packets;

  public SocketChannel getSocketChannel() {
    return socketChannel;
  }

  public void setSocketChannel(SocketChannel socketChannel) {
    this.socketChannel = socketChannel;
  }

  public Set<Object> getPackets() {
    if(packets == null){
      packets = new HashSet<Object>();
    }
    return packets;
  }

  @Override
  public String toString() {
    return "SocketData{" +
            "socketChannel=" + socketChannel +
            ", packets=" + packets +
            '}';
  }
}
