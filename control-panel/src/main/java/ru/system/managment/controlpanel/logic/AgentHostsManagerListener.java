package ru.system.managment.controlpanel.logic;

import ru.system.managment.common.socket.model.packages.AgentInfo;

import java.util.Set;

public interface AgentHostsManagerListener {

  void onGetHostsList(Set<AgentInfo> agentsInfo);

}
