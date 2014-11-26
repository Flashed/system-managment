package ru.system.managment.common.socket.sender;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.SerializationUtils;
import ru.system.managment.common.socket.model.Header;
import ru.system.managment.common.socket.model.SocketData;

import java.nio.ByteBuffer;
import java.util.Set;

public class DefaultSender implements Sender {

  private static final Logger logger = LoggerFactory.getLogger(DefaultSender.class);

  /**
   * {@inheritDoc}
   */
  @Override
  public void send(SocketData socketData) throws Exception {
    try{
      Set<Object> packets = socketData.getPackets();
      for(Object p :packets){
        byte[] data = SerializationUtils.serialize(p);
        ByteBuffer buffer = ByteBuffer.allocate(12 + data.length);
        buffer.clear();
        buffer.putLong(Header.BEGIN);
        buffer.putInt(data.length);
        buffer.put(data);

        socketData.getSocketChannel().write(buffer);

        if(logger.isDebugEnabled()){
          logger.debug("Send packet: \n\t{} \n\t to: \n\t {}", p, socketData.getSocketChannel());
        }
      }
    } catch (Exception e){
      throw new Exception("Failed to send data: \n\t " + socketData);
    }
  }
}
