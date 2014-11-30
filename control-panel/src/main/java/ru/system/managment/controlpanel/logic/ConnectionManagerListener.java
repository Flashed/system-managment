package ru.system.managment.controlpanel.logic;

/**
 * @author Mikhail Zaitsev
 */
public interface ConnectionManagerListener {

  void onConnected();

  void onDisconnected();

  void errorConnection(Throwable e);

}
