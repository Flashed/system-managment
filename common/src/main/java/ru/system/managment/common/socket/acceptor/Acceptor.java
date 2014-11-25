package ru.system.managment.common.socket.acceptor;

/**
 * Acceptor interface
 */
public interface Acceptor {

  void init(AcceptConfig config) throws Exception;

  void start() throws Exception;

  void setListener(AcceptorListener listener);

}
