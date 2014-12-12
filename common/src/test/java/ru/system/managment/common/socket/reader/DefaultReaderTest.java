package ru.system.managment.common.socket.reader;

import org.springframework.util.SerializationUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.system.managment.common.socket.FakeSocketChannel;
import ru.system.managment.common.socket.model.Header;
import ru.system.managment.common.socket.model.SocketData;
import ru.system.managment.common.socket.model.packets.AgentInfo;
import ru.system.managment.common.socket.model.packets.AgentsPacket;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.UUID;

/**
 * @author Mikhail Zaitsev
 */
public class DefaultReaderTest {

  private Reader reader = new DefaultReader();

  private String data1 = "data 1";
  private String data2 = "Данные 2";
  private String data3 = "йзуцкшгапймт кеий3-9ыва" +
          "пцкег0цщиьицзеapoeirg wsdfgsdfggrotibh" +
          "wrotijhybnwrme,\tywpoqmer[sdfggdasfgwn" +
          "iptmpqernbwtynwrnmeyokqwadfgadfgsdeirut" +
          "bnwpoetbnghbntnb\na;roetuinqerovnqoern";

  private SocketChannel socketChannel;

  @Test
  public void testReadOnePocket() throws Exception {
    socketChannel = new FakeSocketChannel(null);
    byte[] d = SerializationUtils.serialize(data1);
    ByteBuffer buffer = ByteBuffer.allocate(1212);
    buffer.putLong(Header.BEGIN);
    buffer.putInt(d.length);
    buffer.put(d);
    SocketData socketData = reader.read(buffer, socketChannel);

    Assert.assertNotNull(socketData);
    Assert.assertEquals(socketData.getSocketChannel(), socketChannel);
    Assert.assertTrue(socketData.getPackets().size() == 1);
    Assert.assertTrue(socketData.getPackets().contains(data1));
  }

  @Test
  public void testReadAnyPacketsFromOneBuffer() throws Exception {
    ByteBuffer buffer = ByteBuffer.allocate(1024);

    byte[] d = SerializationUtils.serialize(data1);
    buffer.putLong(Header.BEGIN);
    buffer.putInt(d.length);
    buffer.put(d);

    d = SerializationUtils.serialize(data2);
    buffer.putLong(Header.BEGIN);
    buffer.putInt(d.length);
    buffer.put(d);

    d = SerializationUtils.serialize(data3);
    buffer.putLong(Header.BEGIN);
    buffer.putInt(d.length);
    buffer.put(d);

    SocketData socketData = reader.read(buffer, socketChannel);
    Assert.assertNotNull(socketData);
    Assert.assertEquals(socketData.getSocketChannel(), socketChannel);
    Assert.assertTrue(socketData.getPackets().size() == 3);
    Assert.assertTrue(socketData.getPackets().contains(data1));
    Assert.assertTrue(socketData.getPackets().contains(data2));
    Assert.assertTrue(socketData.getPackets().contains(data3));

  }

  @Test
  public void testReadOnePacketFromAnyBuffer() throws Exception{
    socketChannel = new FakeSocketChannel(null);
    ByteBuffer buffer = ByteBuffer.allocate(15);

    byte[] d = SerializationUtils.serialize(data3);
    buffer.putLong(Header.BEGIN);
    buffer.putInt(d.length);
    for(int i=0; i<3; i++){
      buffer.put(d[i]);
    }
    SocketData socketData = reader.read(buffer, socketChannel);
    Assert.assertNull(socketData);

    buffer = ByteBuffer.allocate(5);
    for(int i=3; i<8; i++){
      buffer.put(d[i]);
    }
    socketData = reader.read(buffer, socketChannel);
    Assert.assertNull(socketData);

    buffer = ByteBuffer.allocate(1024);
    for(int i=8; i<d.length; i++){
      buffer.put(d[i]);
    }

    socketData = reader.read(buffer, socketChannel);
    Assert.assertNotNull(socketData);
    Assert.assertEquals(socketData.getSocketChannel(), socketChannel);
    Assert.assertTrue(socketData.getPackets().size() == 1);
    Assert.assertTrue(socketData.getPackets().contains(data3));
  }

  @Test
  public void testInvalidPacket() throws Exception {
    socketChannel = new FakeSocketChannel(null);
    ByteBuffer buffer = ByteBuffer.allocate(6 * 1024);
    buffer.putLong(Header.BEGIN);
    buffer.putInt((6 * 1024) - 12);
    buffer.put(new byte[(6 * 1024) - 12]);
    SocketData socketData = reader.read(buffer, socketChannel);
    Assert.assertNull(socketData);
  }

  @Test
  public void testRecoveryAfterInvalidPacket() throws Exception {
    testInvalidPacket();
    testReadOnePocket();
  }

  @Test
  public void testReadBigAgentsPacket() throws Exception{
    socketChannel = new FakeSocketChannel(null);
    int c = 20;
    AgentsPacket packet = createAgentsPacket(c);
    byte[] data = SerializationUtils.serialize(packet);
    ByteBuffer buffer = ByteBuffer.allocate(12 + data.length);
    buffer.clear();
    buffer.putLong(Header.BEGIN);
    buffer.putInt(data.length);
    buffer.put(data);
    buffer.flip();

    SocketData socketData = reader.read(buffer, socketChannel);
    Assert.assertNotNull(socketData);

    AgentsPacket readPacket = (AgentsPacket) socketData.getPackets().iterator().next();
    Assert.assertEquals(readPacket.getAgentsInfo().size(), c);

  }

  private static AgentsPacket createAgentsPacket(int c){
    AgentsPacket packet = new AgentsPacket();
    for(int i=0; i<c; i++){
      packet.getAgentsInfo().add(new AgentInfo(UUID.randomUUID().toString(), "127.0.0."+i, 10+i, 20+i));
    }
    return packet;
  }

}
