package ru.system.managment.common.socket.reader;

import java.nio.ByteBuffer;

/**
 * Interface of reading
 */
public interface Reader {

  void setReaderListener(ReaderListener listener);

  void read(ByteBuffer buffer) throws Exception;

}
