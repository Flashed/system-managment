package ru.system.managment.common.socket;

import ru.system.managment.common.model.Request;
import ru.system.managment.common.model.Response;
import ru.system.managment.common.socket.acceptor.*;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Default implementation of SocketManager
 */
public class DefaultSocketManager implements SocketManager, AcceptorListener{

  private SocketManagerListener socketManagerListener;

  private SocketManagerConfig config;

  private Acceptor acceptor;

  public DefaultSocketManager() {
  }

  public DefaultSocketManager(SocketManagerConfig config) {
    init(config);
  }


  @Override
  public void init(SocketManagerConfig config) {
    this.config = config;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void startAccept()  throws Exception{
    acceptor.start();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendRequest(Request request)  throws Exception{

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendResponse(Response response)  throws Exception{

  }

  @Override
  public void onAccept(SocketChannel channel) {

  }

  @Override
  public void onRead(ByteBuffer buffer) {

  }

  public void setSocketManagerListener(SocketManagerListener socketManagerListener) {
    this.socketManagerListener = socketManagerListener;
  }

  public void setAcceptor(Acceptor acceptor) {
    this.acceptor = acceptor;
    if(acceptor != null){
      acceptor.setListener(this);
    }
  }
}
