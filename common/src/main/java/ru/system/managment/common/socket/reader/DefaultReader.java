package ru.system.managment.common.socket.reader;

import org.springframework.util.SerializationUtils;
import ru.system.managment.common.socket.model.Header;
import ru.system.managment.common.socket.model.SocketData;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * A Default reader's implementation
 */
public class DefaultReader implements Reader{

  private Map<SocketChannel, DataBytes> dataBytesMap = new HashMap<SocketChannel, DataBytes>();

  @Override
  public SocketData read(ByteBuffer buffer, SocketChannel socketChannel) throws Exception {
    SocketData result = null;

    if (!dataBytesMap.containsKey(socketChannel)){
      dataBytesMap.put(socketChannel, new DataBytes());
    }

    DataBytes dataBytes = dataBytesMap.get(socketChannel);
    buffer.rewind();
    while(buffer.hasRemaining()){
      if(dataBytes.getMode() == DataBytes.MODE_READ_HEADER){

        if (tryReadBegin(buffer)){
          dataBytes.setLength(buffer.getInt());
        }

      } else if(dataBytes.getMode() == DataBytes.MODE_READ_DATA){
        dataBytes.getBaos().write(buffer.get());
        if(dataBytes.isFull()){
          Object o = deserialize(dataBytes);
          if(o != null){
            if(result == null){
              result = new SocketData();
              result.setSocketChannel(socketChannel);
            }
            result.getObjects().add(o);
          }
          dataBytes.reset();
        }
      }
    }
    return result;
  }

  private Object deserialize(DataBytes dataBytes){
    try {
      return SerializationUtils.deserialize(dataBytes.getBaos().toByteArray());
    }catch (Exception e){
      return null;
    }
  }

  private boolean tryReadBegin(ByteBuffer buffer){
    if(buffer.remaining() < 8){
      buffer.position(buffer.limit());
      return false;
    }
    boolean res = buffer.getLong() == Header.BEGIN;
    if(!res){
      buffer.position(buffer.position()-7);
    }
    return res;
  }

  private static class DataBytes{

    private static final int MAX_SIZE = 1024*5;
    public  static final int MODE_READ_HEADER = 0;
    public  static final int MODE_READ_DATA = 1;

    private int mode = MODE_READ_HEADER;

    private ByteArrayOutputStream baos = new ByteArrayOutputStream();

    private int length;

    public ByteArrayOutputStream getBaos() {
      return baos;
    }

    public int getLength() {
      return length;
    }

    public void setLength(int length) {
      this.length = length;
      if(this.length > (MAX_SIZE)){
        this.length = MAX_SIZE;
      }
      if(this.length > 0){
        mode = MODE_READ_DATA;
        baos.reset();
      }

    }

    public int getMode() {
      return mode;
    }

    public boolean isFull(){
      return baos.size() == length;
    }

    public void reset(){
      mode = MODE_READ_HEADER;
      length = 0;
      baos.reset();
    }

  }

}
