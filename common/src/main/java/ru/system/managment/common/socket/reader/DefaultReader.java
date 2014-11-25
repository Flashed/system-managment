package ru.system.managment.common.socket.reader;

import ru.system.managment.common.socket.model.SocketData;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * A Default reader's implementation
 */
public class DefaultReader implements Reader{

  @Override
  public SocketData read(ByteBuffer buffer, SocketChannel socketChannel) throws Exception {
    return null;
  }
}
