package ru.system.managment.common.socket.model.packets;


import java.io.Serializable;

public class UpdateCountPacket implements Serializable{

  private int count;

  public UpdateCountPacket() {
  }

  public UpdateCountPacket(int count) {
    this.count = count;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  @Override
  public String toString() {
    return "UpdateCountPacket{" +
            "count=" + count +
            '}';
  }
}
