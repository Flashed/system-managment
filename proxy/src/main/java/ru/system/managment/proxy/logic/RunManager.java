package ru.system.managment.proxy.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.system.managment.common.socket.acceptor.Acceptor;
import ru.system.managment.common.socket.acceptor.AcceptorListener;
import ru.system.managment.common.socket.model.SocketData;
import ru.system.managment.common.socket.model.packets.AgentInfo;
import ru.system.managment.common.socket.model.packets.RunPacket;
import ru.system.managment.common.socket.model.packets.StopPacket;

import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Set;


public class RunManager implements AcceptorListener{


  private static final Logger logger = LoggerFactory.getLogger(RunManager.class);

  private IdentityManager identityManager;

  private Acceptor acceptor;

  @Override
  public void onAccept(SocketChannel channel) {
    //nope
  }

  @Override
  public void onRead(SocketData data) {
    try{
      for(Object o : data.getPackets()){
        if(o == null){
          continue;
        }
        if(o instanceof RunPacket){

          RunPacket packet = (RunPacket) o;
          Map<SocketChannel, AgentInfo> agents =  identityManager.getAgents();
          Set<SocketChannel> sockets = agents.keySet();

          for(SocketChannel channel: sockets){
            if(channel.getRemoteAddress().toString().equals(packet.getHost())){
              SocketData socketData = new SocketData();
              socketData.setSocketChannel(channel);
              socketData.getPackets().add(packet);
              try{
                acceptor.send(socketData);
              } catch (Exception e){
                logger.warn("Failed to send \n {}", socketData, e);
              }
              break;
            }
          }
        } else if(o instanceof StopPacket){

          StopPacket packet = (StopPacket) o;

          Map<SocketChannel, AgentInfo> agents =  identityManager.getAgents();
          Set<SocketChannel> sockets = agents.keySet();

          for(SocketChannel channel: sockets){
            if(channel.getRemoteAddress().toString().equals(packet.getHost())){
              SocketData socketData = new SocketData();
              socketData.setSocketChannel(channel);
              socketData.getPackets().add(packet);
              try{
                acceptor.send(socketData);
              } catch (Exception e){
                logger.warn("Failed to send \n {}", socketData, e);
              }
              break;
            }
          }
        }
      }
    } catch (Exception e){
      logger.error("Failed to send data", e);
    }
  }

  @Override
  public void onDisconnect(SocketChannel channel) {
    //nope
  }

  public void setIdentityManager(IdentityManager identityManager) {
    this.identityManager = identityManager;
  }

  public void setAcceptor(Acceptor acceptor) {
    this.acceptor = acceptor;
  }
}
