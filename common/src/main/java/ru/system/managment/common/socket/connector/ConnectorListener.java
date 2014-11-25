package ru.system.managment.common.socket.connector;

import ru.system.managment.common.socket.model.SocketData;

/**
 * A Connector listener
 */
public interface ConnectorListener {

  void onReadData(SocketData socketData);

}
