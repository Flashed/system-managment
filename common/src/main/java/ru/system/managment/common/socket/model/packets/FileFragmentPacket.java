package ru.system.managment.common.socket.model.packets;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;

public class FileFragmentPacket implements Serializable{

  private String fileName;

  private long transportSessionId;

  private boolean last;

  private byte[] bytes;

  HashSet<String> receivers;


  public boolean isLast() {
    return last;
  }

  public void setLast(boolean last) {
    this.last = last;
  }

  public byte[] getBytes() {
    return bytes;
  }

  public void setBytes(byte[] bytes) {
    this.bytes = bytes;
  }

  public HashSet<String> getReceivers() {
    if(receivers == null){
      receivers = new HashSet<String>();
    }
    return receivers;
  }

  public void setReceivers(HashSet<String> receivers) {
    this.receivers = receivers;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public long getTransportSessionId() {
    return transportSessionId;
  }

  public void setTransportSessionId(long transportSessionId) {
    this.transportSessionId = transportSessionId;
  }

  @Override
  public String toString() {
    return "FileFragmentPacket{" +
            "fileName='" + fileName + '\'' +
            ", transportSessionId=" + transportSessionId +
            ", last=" + last +
            ", bytes=" + Arrays.toString(bytes) +
            ", receivers=" + receivers +
            '}';
  }
}
