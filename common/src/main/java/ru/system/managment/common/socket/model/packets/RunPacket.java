package ru.system.managment.common.socket.model.packets;

import java.io.Serializable;

public class RunPacket implements Serializable{

  private String host;

  public RunPacket(String host) {
    this.host = host;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  @Override
  public String toString() {
    return "RunPacket{" +
            "host='" + host + '\'' +
            '}';
  }
}
