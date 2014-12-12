package ru.system.managment.common.socket.model.packets;

import java.io.Serializable;

public class FileReceivedPacket implements Serializable{

  public static final int STATUS_SUCCESS = 0;
  public static final int STATUS_ERROR = 1;

  private int status;

  private String receiver;

  public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "FileReceivedPacket{" +
            "status=" + status +
            ", receiver='" + receiver + '\'' +
            '}';
  }
}
