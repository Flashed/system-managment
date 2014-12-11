package ru.system.managment.controlpanel.ui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class RunningTabController implements Initializable, AgentRecordListener {

  private static final Logger logger = LoggerFactory.getLogger(RunningTabController.class);

  @FXML
  private TextField nField;

  @FXML
  private TextField timeoutField;

  @FXML
  private Button runButton;

  @FXML
  private Button interruptButton;

  @FXML
  private Button stopButton;

  @FXML
  private TableView agentsTable;

  private ObservableList<AgentRecord> agentsRecords;


  public RunningTabController() {
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    agentsRecords = FXCollections.observableArrayList();
    AgentRecord agentRecord = new AgentRecord();
    agentRecord.setAgentId(UUID.randomUUID().toString());
    agentRecord.setHost("localhost");
    agentRecord.setRunClientsCount("1");
    agentRecord.setSelected(false);
    agentRecord.getListeners().add(this);

    agentsRecords.add(agentRecord);

    TableColumn checkCol = new TableColumn<AgentRecord, Boolean>();
    checkCol.setText("");
    checkCol.setMinWidth(50);
    checkCol.setMaxWidth(50);
    checkCol.setCellValueFactory(new PropertyValueFactory("selected"));
    checkCol.setCellFactory(new Callback<TableColumn, TableCell>() {
      @Override
      public TableCell call(TableColumn tableColumn) {
        CheckBoxTableCell cell = new CheckBoxTableCell();
        CheckBox checkBox = (CheckBox) cell.getGraphic();
        checkBox.setAlignment(Pos.CENTER);
        cell.setAlignment(Pos.CENTER);
        return cell;
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
    runClientsCountCol.setMinWidth(50);
    runClientsCountCol.setMaxWidth(50);
    runClientsCountCol.setCellValueFactory(new PropertyValueFactory("runClientsCount"));

    agentsTable.setEditable(true);
    agentsTable.getColumns().addAll(checkCol, idCol, hostCol, runClientsCountCol);
    agentsTable.setItems(agentsRecords);

  }

  @Override
  public void onChangeProperty(Object propertyValue) {
    Boolean b = (Boolean) propertyValue;
    logger.debug("Check to " + b);
  }
}
