package ru.system.managment.common.socket.reader;

import java.nio.ByteBuffer;

/**
 * A Default reader's implementation
 */
public class DefaultReader implements Reader{

  private ReaderListener listener;

  @Override
  public void setReaderListener(ReaderListener listener) {
    this.listener = listener;
  }

  @Override
  public void read(ByteBuffer buffer) throws Exception {
    
  }
}
