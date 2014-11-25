package ru.system.managment.common.socket.connector;

/**
 * Configuration of connector
 */
public class ConnectorConfig {

  private int port;

  private String host;

  private int readBufferSize;

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public int getReadBufferSize() {
    return readBufferSize;
  }

  public void setReadBufferSize(int readBufferSize) {
    this.readBufferSize = readBufferSize;
  }
}
