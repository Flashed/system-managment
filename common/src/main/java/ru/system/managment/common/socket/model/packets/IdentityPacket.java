package ru.system.managment.common.socket.model.packets;

import java.io.Serializable;

/**
 * @author Mikhail Zaitsev
 */
public class IdentityPacket implements Serializable{

  public static final String ID_PANEL = "idPanel";

  public static final String ID_AGENT = "idAgent";

  private String id;

  private AgentInfo agentInfo;

  public IdentityPacket(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public AgentInfo getAgentInfo() {
    if(agentInfo == null){
      agentInfo = new AgentInfo();
    }
    return agentInfo;
  }

  public void setAgentInfo(AgentInfo agentInfo) {
    this.agentInfo = agentInfo;
  }

  @Override
  public String toString() {
    return "IdentityPacket{" +
            "id='" + id + '\'' +
            ", agentInfo=" + agentInfo +
            '}';
  }
}
