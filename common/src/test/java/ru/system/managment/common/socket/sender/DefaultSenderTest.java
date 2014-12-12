package ru.system.managment.common.socket.sender;

import org.springframework.util.SerializationUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.system.managment.common.socket.FakeSocketChannel;
import ru.system.managment.common.socket.model.Header;
import ru.system.managment.common.socket.model.SocketData;
import ru.system.managment.common.socket.model.packets.AgentInfo;
import ru.system.managment.common.socket.model.packets.AgentsPacket;
import ru.system.managment.common.socket.reader.DefaultReader;
import ru.system.managment.common.socket.reader.Reader;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.UUID;

/**
 * @author Mikhail Zaitsev
 */
public class DefaultSenderTest {

  private Sender sender = new DefaultSender();

  private Reader reader = new DefaultReader();

  private String data1 = "data 1";
  private String data2 = "Данные 2";
  private String data3 = "йзуцкшгапймт кеий3-9ыва" +
          "пцкег0цщиьицзеapoeirg wsdfgsdfggrotibh" +
          "wrotijhybnwrme,\tywpoqmer[sdfggdasfgwn" +
          "iptmpqernbwtynwrnmeyokqwadfgadfgsdeirut" +
          "bnwpoetbnghbntnb\na;roetuinqerovnqoern";

  @Test
  public void testSendOnePacket() throws Exception {
    SocketChannel socketChannel = new FakeSocketChannel(null);
    SocketData socketData = new SocketData();
    socketData.setSocketChannel(socketChannel);
    socketData.getPackets().add(data1);
    sender.send(socketData);

    ByteBuffer buffer = ((FakeSocketChannel)socketChannel).getCollectedData();
    socketData = reader.read(buffer, socketChannel);

    Assert.assertNotNull(socketData);
    Assert.assertEquals(socketData.getSocketChannel(), socketChannel);
    Assert.assertTrue(socketData.getPackets().size() == 1);
    Assert.assertTrue(socketData.getPackets().contains(data1));

  }

  @Test
  public void testSendAnyPackage() throws Exception {
    SocketChannel socketChannel = new FakeSocketChannel(null);
    SocketData socketData = new SocketData();
    socketData.setSocketChannel(socketChannel);
    socketData.getPackets().add(data1);
    socketData.getPackets().add(data2);
    socketData.getPackets().add(data3);
    sender.send(socketData);

    ByteBuffer buffer = ((FakeSocketChannel)socketChannel).getCollectedData();
    socketData = reader.read(buffer, socketChannel);
    Assert.assertNotNull(socketData);
    Assert.assertEquals(socketData.getSocketChannel(), socketChannel);
    Assert.assertTrue(socketData.getPackets().size() == 3);
    Assert.assertTrue(socketData.getPackets().contains(data1));
    Assert.assertTrue(socketData.getPackets().contains(data2));
    Assert.assertTrue(socketData.getPackets().contains(data3));
  }

  @Test
  public void testSendBigAgentsPacket() throws Exception{
    SocketChannel socketChannel = new FakeSocketChannel(null);
    int c = 20;
    AgentsPacket packet = createAgentsPacket(c);
    SocketData socketData = new SocketData();
    socketData.setSocketChannel(socketChannel);
    socketData.setSocketChannel(socketChannel);
    socketData.getPackets().add(packet);
    sender.send(socketData);


    ByteBuffer buffer = ((FakeSocketChannel)socketChannel).getCollectedData();
    SocketData readSocketData = reader.read(buffer, socketChannel);
    Assert.assertNotNull(readSocketData);

    AgentsPacket readPacket = (AgentsPacket) readSocketData.getPackets().iterator().next();
    Assert.assertEquals(readPacket.getAgentsInfo().size(), c);

  }

  private static AgentsPacket createAgentsPacket(int c){
    AgentsPacket packet = new AgentsPacket();
    for(int i=0; i<c; i++){
      packet.getAgentsInfo().add(new AgentInfo(UUID.randomUUID().toString(), "127.0.0."+i, 10+i));
    }
    return packet;
  }


}
