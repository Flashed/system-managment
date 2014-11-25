package ru.system.managment.common.socket.reader;

import ru.system.managment.common.socket.model.Header;
import ru.system.managment.common.socket.model.SocketData;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * A Default reader's implementation
 */
public class DefaultReader implements Reader{

  private Map<SocketChannel, DataBytes> dataBytesMap = new HashMap<SocketChannel, DataBytes>();

  @Override
  public SocketData read(ByteBuffer buffer, SocketChannel socketChannel) throws Exception {
    if (!dataBytesMap.containsKey(socketChannel)){
      dataBytesMap.put(socketChannel, new DataBytes());
    }
    buffer.flip();
    if (isBegin(buffer)){
      DataBytes dataBytes = dataBytesMap.get(socketChannel);
      dataBytes.length = buffer.getInt();
    }

    return null;
  }

  private boolean isBegin(ByteBuffer buffer){
    return buffer.getLong() == Header.BEGIN;
  }

  private static class DataBytes{

    byte[] bytes = new byte[0];

    int length;

  }

}
