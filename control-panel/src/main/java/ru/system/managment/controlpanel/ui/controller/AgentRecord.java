package ru.system.managment.controlpanel.ui.controller;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.HashSet;
import java.util.Set;

public class AgentRecord {

  private Set<AgentRecordListener> listeners;

  private SimpleStringProperty agentId = new SimpleStringProperty();

  private SimpleStringProperty host = new SimpleStringProperty();

  private SimpleStringProperty runClientsCount = new SimpleStringProperty();

  private SimpleBooleanProperty selected = new SimpleBooleanProperty();


  public AgentRecord() {
    selected.addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observableValue,
                          Boolean aBoolean, Boolean aBoolean2) {
        for(AgentRecordListener listener: getListeners()){
          listener.onChangeProperty(selected.getValue());
        }
      }
    });
  }

  public String getAgentId() {
    return agentId.get();
  }

  public SimpleStringProperty agentIdProperty() {
    return agentId;
  }

  public void setAgentId(String agentId) {
    this.agentId.set(agentId);
  }

  public String getHost() {
    return host.get();
  }

  public SimpleStringProperty hostProperty() {
    return host;
  }

  public void setHost(String host) {
    this.host.set(host);
  }

  public String getRunClientsCount() {
    return runClientsCount.get();
  }

  public SimpleStringProperty runClientsCountProperty() {
    return runClientsCount;
  }

  public void setRunClientsCount(String runClientsCount) {
    this.runClientsCount.set(runClientsCount);
  }

  public boolean getSelected() {
    return selected.get();
  }

  public SimpleBooleanProperty selectedProperty() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected.set(selected);
  }

  public Set<AgentRecordListener> getListeners() {
    if(listeners == null){
      listeners = new HashSet<>();
    }
    return listeners;
  }

  public void setListeners(Set<AgentRecordListener> listeners) {
    this.listeners = listeners;
  }
}
