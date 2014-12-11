package ru.system.managment.agent.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.system.managment.common.socket.connector.ConnectorListener;
import ru.system.managment.common.socket.model.SocketData;
import ru.system.managment.common.socket.model.packets.RunPacket;

import java.nio.channels.SocketChannel;

public class RunManager implements ConnectorListener{

  private static final Logger logger = LoggerFactory.getLogger(RunManager.class);

  private String runCommand;

  @Override
  public void onReadData(SocketData socketData) {
    for(Object o: socketData.getPackets()){
      if(o == null){
        continue;
      }
      if(o instanceof RunPacket){
        try{
          Runtime.getRuntime().exec(runCommand);
          logger.debug("run command: {}", runCommand);
        } catch (Exception e){
          logger.error("Failed to start", e);
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

  public String getRunCommand() {
    return runCommand;
  }

  public void setRunCommand(String runCommand) {
    this.runCommand = runCommand;
  }
}
