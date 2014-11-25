package ru.system.managment.common.socket.reader;

import ru.system.managment.common.socket.model.SocketData;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Interface of reading
 */
public interface Reader {

  SocketData read(ByteBuffer buffer, SocketChannel socketChannel) throws Exception;

}
