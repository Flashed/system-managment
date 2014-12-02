package ru.system.managment.common.socket.model.packages;

import java.io.Serializable;

public class AgentInfo implements Serializable{

  private String host;

  private int activeClients;

  private int allClients;

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
}
