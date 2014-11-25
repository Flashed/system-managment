package ru.system.managment.common.connector.acceptor;

/**
 * Configuration of accept
 */
public class AcceptConfig {

  private int port;

  private int readBufferSize;

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public int getReadBufferSize() {
    return readBufferSize;
  }

  public void setReadBufferSize(int readBufferSize) {
    this.readBufferSize = readBufferSize;
  }

  @Override
  public String toString() {
    return "AcceptConfig{" +
            "readBufferSize=" + readBufferSize +
            ", port=" + port +
            '}';
  }
}
