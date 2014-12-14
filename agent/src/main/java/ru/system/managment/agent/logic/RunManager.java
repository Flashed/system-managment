package ru.system.managment.agent.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.system.managment.common.socket.connector.Connector;
import ru.system.managment.common.socket.connector.ConnectorListener;
import ru.system.managment.common.socket.model.SocketData;
import ru.system.managment.common.socket.model.packets.RunPacket;
import ru.system.managment.common.socket.model.packets.StopPacket;
import ru.system.managment.common.socket.model.packets.UpdateCountPacket;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.SocketChannel;

public class RunManager implements ConnectorListener{

  private static final Logger logger = LoggerFactory.getLogger(RunManager.class);

  private String runCommand;

  private String stopCommand;

  private String runCountCommand;

  private volatile boolean started;

  private Connector connector;

  private ConnectionManager connectionManager;

  @Override
  public void onReadData(SocketData socketData) {
    for(Object o: socketData.getPackets()){
      if(o == null){
        continue;
      }
      if(o instanceof RunPacket){
        try{
          logger.info("Free memory: {}", Runtime.getRuntime().freeMemory());
          Runtime.getRuntime().exec(runCommand);
          logger.debug("run command: {}", runCommand);
        } catch (Exception e){
          logger.error("Failed to start", e);
        }
      } else if( o instanceof StopPacket){
        try{
          logger.info("Free memory: {}", Runtime.getRuntime().freeMemory());
          Runtime.getRuntime().exec(stopCommand);
          logger.debug("run command: {}", stopCommand);
        } catch (Exception e){
          logger.error("Failed to stop", e);
        }
      }
    }

  }

  @Override
  public void onConnected(SocketChannel socketChannel) {
    if(!started && runCountCommand != null && !runCountCommand.trim().isEmpty()){
      new Thread(new GetRunCountTask()).start();
    }
  }

  @Override
  public void onDisconnected(SocketChannel socketChannel) {
    started = false;
  }


  private class GetRunCountTask implements Runnable{

    @Override
    public void run() {
      started = true;

      while (started){
        try{
          logger.info("Free memory: {}", Runtime.getRuntime().freeMemory());
          Process p = Runtime.getRuntime().exec(runCountCommand);
          logger.debug("run command: {}", runCountCommand);

          BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
          String out = reader.readLine();
          logger.debug("Read from {} : {}", runCountCommand, out);

          try{
            reader.close();
            p.destroy();
          } catch (Exception e){
            logger.error("Failed to close process", e);
          }

          int count = 0;
          try{
            count = Integer.valueOf(out);
            SocketChannel proxyChannel = connectionManager.getProxyChannel();
            SocketData socketData = new SocketData();
            socketData.setSocketChannel(proxyChannel);
            socketData.getPackets().add(new UpdateCountPacket(count));
            connector.send(socketData);
          }catch (Exception e){
            logger.error("Failed parse to int out {}", out, e);
          }
        } catch (Exception e){
          logger.error("Failed to GetRunCount", e);
        }

        try {
          Thread.sleep(5000);
        } catch (InterruptedException ignored) {}
      }
      logger.info("GetRunCountTask end");
    }
  }


  public String getRunCommand() {
    return runCommand;
  }

  public void setRunCommand(String runCommand) {
    this.runCommand = runCommand;
  }

  public String getStopCommand() {
    return stopCommand;
  }

  public void setStopCommand(String stopCommand) {
    this.stopCommand = stopCommand;
  }

  public String getRunCountCommand() {
    return runCountCommand;
  }

  public void setRunCountCommand(String runCountCommand) {
    this.runCountCommand = runCountCommand;
  }

  public void setConnector(Connector connector) {
    this.connector = connector;
  }

  public void setConnectionManager(ConnectionManager connectionManager) {
    this.connectionManager = connectionManager;
  }
}
