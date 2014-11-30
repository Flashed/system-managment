package ru.system.managment.common.socket.connector;

import ru.system.managment.common.socket.model.SocketData;

import java.nio.channels.SocketChannel;

/**
 * A Connector listener
 */
public interface ConnectorListener {

  void onReadData(SocketData socketData);

  void onConnected(SocketChannel socketChannel);

  void onDisconnected(SocketChannel socketChannel);

}
