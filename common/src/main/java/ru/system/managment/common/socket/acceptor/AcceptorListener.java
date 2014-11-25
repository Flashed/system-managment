package ru.system.managment.common.socket.acceptor;

import ru.system.managment.common.socket.model.SocketData;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Listener of Acceptor
 */
public interface AcceptorListener {

  void onAccept(SocketChannel channel);

  void onRead(SocketData data);

}
