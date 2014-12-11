package ru.system.managment.common.socket.model.packets;

import java.io.Serializable;

public class RunPacket implements Serializable{

  private String agentId;

  public RunPacket(String id) {
    this.agentId = id;
  }

  public String getId() {
    return agentId;
  }

  public void setAgentId(String id) {
    this.agentId = agentId;
  }

  @Override
  public String toString() {
    return "RunPacket{" +
            "agentId='" + agentId + '\'' +
            '}';
  }
}
