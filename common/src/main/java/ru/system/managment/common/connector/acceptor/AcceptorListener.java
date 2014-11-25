package ru.system.managment.common.connector.acceptor;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Listener of Acceptor
 */
public interface AcceptorListener {

  void onAccept(SocketChannel channel);

  void onRead(ByteBuffer buffer);

}
