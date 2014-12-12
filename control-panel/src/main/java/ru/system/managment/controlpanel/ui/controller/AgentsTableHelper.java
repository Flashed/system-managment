package ru.system.managment.controlpanel.ui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import ru.system.managment.common.socket.model.packets.AgentInfo;

import java.util.HashSet;
import java.util.Set;

public class AgentsTableHelper {

  private TableView table;

  private ObservableList<AgentRecord> records;

  public AgentsTableHelper(TableView table) {
    this.table = table;
  }

  public void init(){
    records = FXCollections.observableArrayList();

    TableColumn checkCol = new TableColumn<AgentRecord, Boolean>();
    checkCol.setText("");
    checkCol.setMinWidth(50);
    checkCol.setMaxWidth(50);
    checkCol.setCellValueFactory(new PropertyValueFactory("selected"));
    checkCol.setCellFactory(new Callback<TableColumn, TableCell>() {
      @Override
      public TableCell call(TableColumn tableColumn) {
        return new CheckBoxTableCell();
      }
    });

    TableColumn idCol = new TableColumn<AgentRecord, String>();
    idCol.setText("ID");
    idCol.setMinWidth(300);
    idCol.setMaxWidth(300);
    idCol.setCellValueFactory(new PropertyValueFactory("agentId"));

    TableColumn hostCol = new TableColumn<AgentRecord, String>();
    hostCol.setText("Хост");
    hostCol.setMinWidth(300);
    hostCol.setMaxWidth(300);
    hostCol.setCellValueFactory(new PropertyValueFactory("host"));

    TableColumn runClientsCountCol = new TableColumn<AgentRecord, String>();
    runClientsCountCol.setText("Клиенты");
    runClientsCountCol.setMinWidth(120);
    runClientsCountCol.setMaxWidth(120);
    runClientsCountCol.setCellValueFactory(new PropertyValueFactory("runClientsCount"));

    table.setEditable(true);
    table.getColumns().addAll(checkCol, idCol, hostCol, runClientsCountCol);
    table.setItems(records);

  }

  public Set<AgentInfo> getSelected(){
    Set<AgentInfo> result = new HashSet<>();
    synchronized (records) {
      for (AgentRecord rec : records) {
        if(rec.getSelected()){
          AgentInfo info = new AgentInfo();
          info.setHost(rec.getHost());
          info.setAgentId(rec.getAgentId());
          info.setAllClients(Integer.valueOf(rec.getRunClientsCount()));
          result.add(info);
        }
      }
    }
    return result;
  }

  public void update(Set<AgentInfo> agentsInfo){
    synchronized (records){
      Set<AgentRecord> newRec = searchForNew(agentsInfo);
      Set<AgentRecord> delRec = searchForDel(agentsInfo);
      records.removeAll(delRec);
      records.addAll(newRec);
      updateCountClients(agentsInfo);
    }
  }

  private void updateCountClients(Set<AgentInfo> infoSet){
    for(AgentRecord rec: records){
      for(AgentInfo info : infoSet){
        if(info.getAgentId().equals(rec.getAgentId())){
          rec.setRunClientsCount(String.valueOf(info.getAllClients()));
        }
      }
    }
  }

  private Set<AgentRecord> searchForDel(Set<AgentInfo> infoSet){
    Set<AgentRecord> result = new HashSet<>();
    boolean found;
    for(AgentRecord rec: records){
      found = false;
      for(AgentInfo info : infoSet){
        if(info.getAgentId().equals(rec.getAgentId())){
          found = true;
          break;
        }
      }
      if(!found){
        result.add(rec);
      }
    }
    return result;
  }

  private Set<AgentRecord> searchForNew(Set<AgentInfo> infoSet){
    Set<AgentRecord> result = new HashSet<>();
    boolean found;
    for(AgentInfo info : infoSet){
      found = false;
      for(AgentRecord rec: records){
        if(info.getAgentId().equals(rec.getAgentId())){
          found = true;
          break;
        }
      }
      if(!found){
        result.add(createRecord(info));
      }
    }
    return result;
  }

  private AgentRecord createRecord(AgentInfo agentInfo){
    AgentRecord rec = new AgentRecord();
    rec.setAgentId(agentInfo.getAgentId());
    rec.setRunClientsCount(String.valueOf(agentInfo.getAllClients()));
    rec.setHost(agentInfo.getHost());
    return rec;
  }

}
