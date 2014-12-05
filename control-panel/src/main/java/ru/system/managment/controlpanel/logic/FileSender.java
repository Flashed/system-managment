package ru.system.managment.controlpanel.logic;

import ru.system.managment.common.socket.connector.Connector;
import ru.system.managment.common.socket.connector.ConnectorListener;
import ru.system.managment.common.socket.model.SocketData;
import ru.system.managment.common.socket.model.packets.FileFragmentPacket;
import ru.system.managment.common.socket.model.packets.FileReceivedPacket;

import java.io.File;
import java.io.FileInputStream;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Set;

/**
 * Sends a file
 */
public class FileSender implements ConnectorListener{

  private Connector connector;

  private ConnectionManager connectionManager;

  private FileSenderListener listener;

  public void sendFile(File file, HashSet<String> receivers) throws Exception{
    try(FileInputStream fileInputStream = new FileInputStream(file)){
      int len;
      byte[] buffer = new byte[1024];
      long tSessionId = System.nanoTime();
      FileFragmentPacket packet = null;
      while((len = fileInputStream.read(buffer)) != -1) {
        packet = createPacket(buffer, len, receivers, file.getName(), tSessionId);
        createSocketDataAnSend(packet);
      }
      if(packet == null){
        return;
      }
      packet.setLast(true);
      createSocketDataAnSend(packet);

    } catch (Exception e){
      throw new Exception("Failed to send file "+ file, e);
    }
  }

  private SocketData createSocketDataAnSend(FileFragmentPacket fileFragmentPacket) throws Exception {
    if(connectionManager.getProxyChannel() == null){
      return null;
    }
    SocketData socketData = new SocketData();
    socketData.getPackets().add(fileFragmentPacket);
    socketData.setSocketChannel(connectionManager.getProxyChannel());
    connector.send(socketData);
    return socketData;

  }

  private FileFragmentPacket createPacket(byte[] buffer, int len, HashSet<String> receivers,
                                          String fileName, long transportSessionId){
    FileFragmentPacket packet = new FileFragmentPacket();
    packet.setReceivers(receivers);
    packet.setFileName(fileName);
    packet.setTransportSessionId(transportSessionId);
    byte[] bytes = new byte[len];
    System.arraycopy(buffer, 0, bytes, 0, len);
    packet.setBytes(bytes);
    return packet;
  }

  @Override
  public void onReadData(SocketData socketData) {
    Set<Object> packages = socketData.getPackets();
    for(Object o: packages){
      if(o == null){
        continue;
      }
      if(o instanceof FileReceivedPacket){
        if(listener != null){
          listener.onSent((FileReceivedPacket) o);
        }
      }
    }
  }

  @Override
  public void onConnected(SocketChannel socketChannel) {
    //nope
  }

  @Override
  public void onDisconnected(SocketChannel socketChannel) {
    //nope
  }

  public void setConnector(Connector connector) {
    this.connector = connector;
  }

  public void setConnectionManager(ConnectionManager connectionManager) {
    this.connectionManager = connectionManager;
  }

  public void setListener(FileSenderListener listener) {
    this.listener = listener;
  }
}
