package ru.system.managment.common.socket.sender;


import ru.system.managment.common.socket.model.SocketData;

public interface Sender {

  void send(SocketData socketData) throws Exception;

}
