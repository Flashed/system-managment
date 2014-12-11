package ru.system.managment.controlpanel.ui.controller;


import javafx.beans.value.ObservableListValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AgentsTableConfigurator {

  private TableView table;

  private ObservableListValue<AgentRecord> records;

  public AgentsTableConfigurator(TableView table, ObservableListValue<AgentRecord> records) {
    this.table = table;
    this.records = records;
  }

  public void init(){

    //Agent id column
    TableColumn agentIdCol = new TableColumn();
    agentIdCol.setText("ID");
    agentIdCol.setCellValueFactory(new PropertyValueFactory(""));

  }

}
