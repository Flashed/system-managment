package ru.system.managment.controlpanel.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.system.managment.common.socket.connector.Connector;
import ru.system.managment.common.socket.connector.ConnectorListener;
import ru.system.managment.common.socket.model.SocketData;
import ru.system.managment.common.socket.model.packets.AgentsPacket;
import ru.system.managment.common.socket.model.packets.GetAgentsPacket;

import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Set;

public class AgentHostsManager implements ConnectorListener {

  private static final Logger logger = LoggerFactory.getLogger(AgentHostsManager.class);

  private Set<AgentHostsManagerListener> listeners = new HashSet<>();

  private Connector connector;

  private volatile boolean started;

  private SocketChannel socketChannel;

  public void setListeners(Set<AgentHostsManagerListener> listeners) {
    this.listeners = listeners;
  }

  public void setConnector(Connector connector) {
    this.connector = connector;
  }

  @Override
  public void onReadData(SocketData socketData) {
    try{
      if(listeners == null){
        return;
      }
      Set<Object> objects = socketData.getPackets();
      for(Object packet: objects){
        if(packet instanceof AgentsPacket){
          if(listeners != null){
            for(AgentHostsManagerListener listener: listeners){
              listener.onGetHostsList(((AgentsPacket) packet).getAgentsInfo());
            }
          }
        }
      }
    }catch (Exception e){
      logger.error("Failed to read data", e);
    }
  }

  @Override
  public void onConnected(SocketChannel socketChannel) {
    this.socketChannel = socketChannel;
    started = true;
    new Thread(new HostsInquirer()).start();
  }

  public Set<AgentHostsManagerListener> getListeners() {
    return listeners;
  }

  @Override
  public void onDisconnected(SocketChannel socketChannel) {
    started = false;
  }

  class HostsInquirer implements  Runnable{

    int timeout = 10000;

    @Override
    public void run() {
      try{
        while (started){
          SocketData socketData = new SocketData();
          socketData.setSocketChannel(socketChannel);
          GetAgentsPacket packet = new GetAgentsPacket();
          socketData.getPackets().add(packet);
          connector.send(socketData);
          if(logger.isDebugEnabled()){
            logger.debug("Send GetAgentsPacket");
          }
          Thread.sleep(timeout);
        }
      } catch (Exception e){
        started = false;
        logger.error("Failed to get list of agent hosts" ,e);
      }
    }
  }
}
