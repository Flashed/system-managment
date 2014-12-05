package ru.system.managment.controlpanel.logic;


import ru.system.managment.common.socket.model.packets.FileReceivedPacket;

public interface FileSenderListener {

  void onSent(FileReceivedPacket packet);
}
