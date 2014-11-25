package ru.system.managment.common.connector.acceptor;

/**
 * Acceptor interface
 */
public interface Acceptor {

  void init(AcceptConfig config) throws Exception;

  void start() throws Exception;

}
