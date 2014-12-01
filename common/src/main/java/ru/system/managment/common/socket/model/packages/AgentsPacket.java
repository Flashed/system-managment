package ru.system.managment.common.socket.model.packages;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class AgentsPacket implements Serializable {

  private Set<String> hosts;

  public Set<String> getHosts() {
    if(hosts == null){
      hosts = new HashSet<String>();
    }
    return hosts;
  }

  public void setHosts(Set<String> hosts) {
    this.hosts = hosts;
  }
}
