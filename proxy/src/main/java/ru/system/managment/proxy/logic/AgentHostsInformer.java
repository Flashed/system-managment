package ru.system.managment.proxy.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.system.managment.common.socket.acceptor.Acceptor;
import ru.system.managment.common.socket.acceptor.AcceptorListener;
import ru.system.managment.common.socket.model.SocketData;
import ru.system.managment.common.socket.model.packages.AgentInfo;
import ru.system.managment.common.socket.model.packages.AgentsPacket;
import ru.system.managment.common.socket.model.packages.GetAgentsPacket;

import java.nio.channels.SocketChannel;
import java.util.Set;

public class AgentHostsInformer implements AcceptorListener{

  private static final Logger logger = LoggerFactory.getLogger(AgentHostsInformer.class);

  private IdentityManager identityManager;

  private Acceptor acceptor;


  @Override
  public void onAccept(SocketChannel channel) {
    //nope
  }

  @Override
  public void onRead(SocketData data) {
    try{
      Set<Object> objects = data.getPackets();
      for(Object packet: objects){
        if(packet instanceof GetAgentsPacket){
          if(identityManager.getPanelChannel() == null){
            return;
          }
          Set<SocketChannel> agents = identityManager.getAgents();
          AgentsPacket answerPacket = new AgentsPacket();
          synchronized (agents){
            for(SocketChannel socketChannel :agents){
              if(socketChannel == null){
                continue;
              }
              if(!socketChannel.isOpen()){
                continue;
              }
              AgentInfo agentInfo = new AgentInfo();
              agentInfo.setHost(socketChannel.getRemoteAddress().toString());
              answerPacket.getAgentsInfo().add(agentInfo);
            }
          }
          SocketData answerData = new SocketData();
          answerData.setSocketChannel(identityManager.getPanelChannel());
          answerData.getPackets().add(answerPacket);
          acceptor.send(answerData);
          if(logger.isDebugEnabled()){
            logger.debug("Send information about agents {}", answerData);
          }
        }
      }
    }catch (Exception e){
      logger.error("Failed to read data", e);
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
