package ru.system.managment.controlpanel.logic;

import java.util.Set;

public interface AgentHostsManagerListener {

  void onGetHostsList(Set<String> hosts);

}
