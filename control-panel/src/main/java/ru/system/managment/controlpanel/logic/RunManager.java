package ru.system.managment.controlpanel.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.system.managment.common.socket.connector.Connector;
import ru.system.managment.common.socket.model.SocketData;
import ru.system.managment.common.socket.model.packets.AgentInfo;
import ru.system.managment.common.socket.model.packets.RunPacket;

import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Set;

public class RunManager {

  private static final Logger logger = LoggerFactory.getLogger(RunManager.class);

  private ConnectionManager connectionManager;

  private Set<RunManagerListener> listeners;

  private Connector connector;

  private SocketChannel proxyChannel;

  private volatile boolean started;

  public void run(Set<AgentInfo> agents, int count,  int timeout){
    if(started){
      logger.debug("Already started");
      return;
    }
    if(agents == null || agents.isEmpty()){
      logger.debug("agents is null or empty");
      return;
    }
    started = true;
    proxyChannel = connectionManager.getProxyChannel();
    new Thread(new RunTask(count, timeout, agents)).start();
  }

  public void stop(){
    started = false;
  }

  private class RunTask implements Runnable{

    private int count;

    private int timeout;

    private Set<AgentInfo> agents;

    private RunTask(int count, int timeout, Set<AgentInfo> agents) {
      this.count = count;
      this.timeout = timeout;
      this.agents = agents;
    }

    @Override
    public void run() {
      try{
        if(count < 1){
          count = 1;
        }
        if(timeout < 100){
          timeout =100;
        }

        while(count > 0){
          if(!started){
            break;
          }

          SocketData socketData = new SocketData();
          socketData.setSocketChannel(proxyChannel);
          for(AgentInfo agent : agents){
            socketData.getPackets().add(new RunPacket(agent.getAgentId()));
          }
          connector.send(socketData);
          logger.debug("Sent run packets");
          count --;
          Thread.sleep(timeout);

        }
        started = false;

        for (RunManagerListener listener :getListeners()){
          listener.onCommandedRun(agents);
        }

      } catch (Exception e){
        logger.error("Failed to run" ,e);
      }
    }
  }

  public void setConnector(Connector connector) {
    this.connector = connector;
  }

  public void setConnectionManager(ConnectionManager connectionManager) {
    this.connectionManager = connectionManager;
  }

  public Set<RunManagerListener> getListeners() {
    if(listeners == null){
      listeners = new HashSet<>();
    }
    return listeners;
  }

  public void setListeners(Set<RunManagerListener> listeners) {
    this.listeners = listeners;
  }
}
