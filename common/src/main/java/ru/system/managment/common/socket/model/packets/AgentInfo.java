package ru.system.managment.common.socket.model.packets;

import java.io.Serializable;

public class AgentInfo implements Serializable{

  private String agentId;

  private String host;

  private int allClients;

  public AgentInfo() {
  }

  public AgentInfo(String agentId, String host, int allClients) {
    this.agentId = agentId;
    this.host = host;
    this.allClients = allClients;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public int getAllClients() {
    return allClients;
  }

  public void setAllClients(int allClients) {
    this.allClients = allClients;
  }

  public String getAgentId() {
    return agentId;
  }

  public void setAgentId(String agentId) {
    this.agentId = agentId;
  }

  @Override
  public String toString() {
    return "AgentInfo{" +
            "agentId='" + agentId + '\'' +
            ", host='" + host + '\'' +
            ", allClients=" + allClients +
            '}';
  }
}
