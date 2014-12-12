package ru.system.managment.common.socket.model.packets;

import java.io.Serializable;

public class AgentInfo implements Serializable{

  private String agentId;

  private String host;

  private int activeClients;

  private int allClients;

  public AgentInfo() {
  }

  public AgentInfo(String agentId, String host, int activeClients, int allClients) {
    this.agentId = agentId;
    this.host = host;
    this.activeClients = activeClients;
    this.allClients = allClients;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public int getActiveClients() {
    return activeClients;
  }

  public void setActiveClients(int activeClients) {
    this.activeClients = activeClients;
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
            ", activeClients=" + activeClients +
            ", allClients=" + allClients +
            '}';
  }
}
