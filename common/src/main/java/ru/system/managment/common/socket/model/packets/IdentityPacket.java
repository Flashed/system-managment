package ru.system.managment.common.socket.model.packets;

import java.io.Serializable;

/**
 * @author Mikhail Zaitsev
 */
public class IdentityPacket implements Serializable{

  public static final String ID_PANEL = "idPanel";

  public static final String ID_AGENT = "idAgent";

  private String id;

  private String agentId;

  public IdentityPacket(String id, String agentId) {
    this.id = id;
    this.agentId = agentId;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAgentId() {
    return agentId;
  }

  public void setAgentId(String agentId) {
    this.agentId = agentId;
  }

  @Override
  public String toString() {
    return "IdentityPacket{" +
            "id='" + id + '\'' +
            ", agentId='" + agentId + '\'' +
            '}';
  }
}
