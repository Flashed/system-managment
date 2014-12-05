package ru.system.managment.proxy.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.system.managment.common.socket.acceptor.Acceptor;
import ru.system.managment.common.socket.acceptor.AcceptorListener;
import ru.system.managment.common.socket.model.SocketData;
import ru.system.managment.common.socket.model.packets.IdentifySuccessPacket;
import ru.system.managment.common.socket.model.packets.IdentityPacket;

import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class IdentityManager implements AcceptorListener{

  private static final Logger logger = LoggerFactory.getLogger(IdentityManager.class);

  private Acceptor acceptor;

  private SocketChannel panelChannel;

  private Set<SocketChannel> agents  = new HashSet<SocketChannel>();

  private Map<SocketChannel, Long> notIdentifySockets = new ConcurrentHashMap<SocketChannel, Long>();

  TimeoutDisconnectManager timeoutDisconnectManager = new TimeoutDisconnectManager();

  private volatile boolean started;

  public void start(){
    if(started){
      return;
    }
    started = true;
    new Thread(timeoutDisconnectManager).start();
  }

  public void stop(){
    started = false;
  }

  @Override
  public void onAccept(SocketChannel channel) {
    try {
      Long time = System.currentTimeMillis();
      notIdentifySockets.put(channel, time);
      if (logger.isInfoEnabled()) {
        logger.info("{} added as not identify on {} time", channel, time);
      }
    }catch (Exception e){
      logger.error("Failed to onAccept", e);
    }
  }

  @Override
  public void onRead(SocketData data) {
    try{
      Set<Object> packets = data.getPackets();
      for(Object packet: packets){
        if(packet == null){
          continue;
        }
        if(packet instanceof IdentityPacket){
          IdentityPacket identityPacket = (IdentityPacket) packet;
          if(IdentityPacket.ID_PANEL.equals(identityPacket.getId())){
            notIdentifySockets.remove(data.getSocketChannel());
            timeoutDisconnectManager.disconnect(panelChannel);
            panelChannel = data.getSocketChannel();
            sendSuccess(panelChannel);
            if(logger.isInfoEnabled()) {
              logger.info("{} identify as control-panel", data.getSocketChannel());
            }
          } else if(IdentityPacket.ID_AGENT.equals(identityPacket.getId())){
            notIdentifySockets.remove(data.getSocketChannel());
            synchronized (agents){
              agents.add(data.getSocketChannel());
            }
            sendSuccess(data.getSocketChannel());
            if(logger.isInfoEnabled()) {
              logger.info("{} identify as agent", data.getSocketChannel());
            }
          }
        }
      }
    } catch (Exception e){
      logger.error("Failed to read {}" ,data, e);
    }
  }

  @Override
  public void onDisconnect(SocketChannel channel) {
    try{
      if(panelChannel != null){
        if(panelChannel.equals(channel)){
          panelChannel = null;
        }
      }else {
        synchronized (agents){
          if(agents.contains(channel)){
            agents.remove(channel);
          }
        }
      }
      if(logger.isInfoEnabled()) {
        logger.info("Disconnected {}" , channel);
      }
    } catch (Exception e){
      logger.error("Failed to onDisconnect" , e);
    }
  }

  private void sendSuccess(SocketChannel socketChannel) throws Exception {
    SocketData success = new SocketData();
    success.getPackets().add(new IdentifySuccessPacket());
    success.setSocketChannel(socketChannel);
    acceptor.send(success);
  }

  public void setAcceptor(Acceptor acceptor) {
    this.acceptor = acceptor;
  }

  public Set<SocketChannel> getAgents() {
    if(agents == null){
      agents = new HashSet<SocketChannel>();
    }
    return agents;
  }

  public SocketChannel getPanelChannel() {
    return panelChannel;
  }

  private class TimeoutDisconnectManager implements Runnable{

    private int timeout = 10000;

    @Override
    public void run() {
      try{
        while (started) {
          Thread.sleep(timeout);
          Set<Map.Entry<SocketChannel, Long>> entries =  notIdentifySockets.entrySet();
          for(Map.Entry<SocketChannel, Long> entry : entries){
            if(System.currentTimeMillis()  - entry.getValue() > timeout){
              notIdentifySockets.remove(entry.getKey());
              disconnect(entry.getKey());
              onDisconnect(entry.getKey());
            }
          }
          if(logger.isDebugEnabled()){
            logger.debug("Identity manager tick");
          }
        }
      }catch (Exception e){
        logger.error("TimeoutDisconnectManager error", e);
      }
    }

    void disconnect(SocketChannel socketChannel){
      try{
        if(socketChannel == null){
          return;
        }
        synchronized (socketChannel){
          socketChannel.close();
        }
      } catch (Exception ignore){}
    }
  }


}
