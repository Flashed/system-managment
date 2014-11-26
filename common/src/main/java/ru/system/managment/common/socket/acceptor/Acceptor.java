package ru.system.managment.common.socket.acceptor;

import ru.system.managment.common.socket.model.SocketData;

/**
 * Acceptor interface
 */
public interface Acceptor {

  void acceptAndRead() throws Exception;

  void send(SocketData socketData) throws Exception;


}
