package ru.system.managment.common.socket.model.packages;

import java.io.Serializable;

/**
 * @author Mikhail Zaitsev
 */
public class IdentifyPacket implements Serializable{

  public static final String ID_PANEL = "idPanel";

  public static final String ID_AGENT = "idAgent";

  private String id;

  public IdentifyPacket(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "IdentifyPacket{" +
            "id='" + id + '\'' +
            '}';
  }
}
