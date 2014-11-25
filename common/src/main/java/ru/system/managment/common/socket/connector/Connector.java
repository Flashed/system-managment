package ru.system.managment.common.socket.connector;

/**
 * A Connector interface
 */
public interface Connector {

  void connect() throws Exception;

  void disconnect() throws Exception;
}
