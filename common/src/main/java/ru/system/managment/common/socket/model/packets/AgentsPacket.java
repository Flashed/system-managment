package ru.system.managment.common.socket.model.packets;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class AgentsPacket implements Serializable {

  private Set<AgentInfo> agentsInfo;

  public Set<AgentInfo> getAgentsInfo() {
    if(agentsInfo == null){
      agentsInfo = new HashSet<AgentInfo>();
    }
    return agentsInfo;
  }

  public void setAgentsInfo(Set<AgentInfo> agentsInfo) {
    this.agentsInfo = agentsInfo;
  }

  @Override
  public String toString() {
    return "AgentsPacket{" +
            "agentsInfo=" + agentsInfo +
            '}';
  }
}
