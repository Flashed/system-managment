package ru.system.managment.proxy.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.system.managment.common.socket.acceptor.Acceptor;
import ru.system.managment.common.socket.acceptor.AcceptorListener;
import ru.system.managment.common.socket.model.SocketData;
import ru.system.managment.common.socket.model.packets.RunPacket;

import java.nio.channels.SocketChannel;


public class RunManager implements AcceptorListener{


  private static final Logger logger = LoggerFactory.getLogger(RunManager.class);

  private IdentityManager identityManager;

  private Acceptor acceptor;



  public void setIdentityManager(IdentityManager identityManager) {
    this.identityManager = identityManager;
  }

  public void setAcceptor(Acceptor acceptor) {
    this.acceptor = acceptor;
  }

  @Override
  public void onAccept(SocketChannel channel) {
    //nope
  }

  @Override
  public void onRead(SocketData data) {
    for(Object o : data.getPackets()){
      if(o == null){
        continue;
      }
      if(o instanceof RunPacket){
        RunPacket packet = (RunPacket) o;

      }
    }
  }

  @Override
  public void onDisconnect(SocketChannel channel) {
    //nope
  }
}
