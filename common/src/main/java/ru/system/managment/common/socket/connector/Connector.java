package ru.system.managment.common.socket.connector;

import ru.system.managment.common.socket.model.SocketData;

/**
 * A Connector interface
 */
public interface Connector {

  void connectAndRead() throws Exception;

  void send(SocketData socketData) throws Exception;

  void disconnect() throws Exception;
}
