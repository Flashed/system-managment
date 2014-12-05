package ru.system.managment.controlpanel.logic;

import ru.system.managment.common.socket.model.packets.AgentInfo;

import java.util.Set;

public interface AgentHostsManagerListener {

  void onGetHostsList(Set<AgentInfo> agentsInfo);

}
