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
import ru.system.managment.common.socket.model.packets.AgentInfo;
import ru.system.managment.controlpanel.AppContextUtil;
import ru.system.managment.controlpanel.logic.AgentHostsManager;
import ru.system.managment.controlpanel.logic.AgentHostsManagerListener;
import ru.system.managment.controlpanel.logic.RunManager;
import ru.system.managment.controlpanel.logic.RunManagerListener;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;

public class RunningTabController implements Initializable, AgentHostsManagerListener, RunManagerListener {

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

  private AgentsTableHelper tableHelper;

  public RunningTabController() {
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    try{
      btnMode(true);

      tableHelper = new AgentsTableHelper(agentsTable);
      tableHelper.init();
      getAgentHostsManager().getListeners().add(this);
      getRunManager().getListeners().add(this);
    } catch (Exception e){
      logger.error("Failed to initialize UI");
    }
  }


  public void onRun(){
    try{
      btnMode(false);
      int count = 0;
      int timeout = 0;
      try{count = Integer.valueOf(nField.getText());} catch (Exception ignored){}
      try{timeout = Integer.valueOf(timeoutField.getText());} catch (Exception ignored){}
      getRunManager().run(tableHelper.getSelected(),
                        count, timeout);
    } catch (Exception e){
      logger.error("Failed onRun", e);
      btnMode(true);
    }

  }

  public void onInterrupt(){
    try{
      getRunManager().stop();
      btnMode(true);
    } catch (Exception e){
      logger.error("Failed onInterrupt");
    }
  }

  private void btnMode(boolean v){
    interruptButton.setDisable(v);
    stopButton.setDisable(v);
    runButton.setDisable(!v);
  }

  @Override
  public void onCommandedRun(Set<AgentInfo> agents) {
    btnMode(true);
  }

  @Override
  public void onGetHostsList(Set<AgentInfo> agentsInfo) {
    if(agentsInfo == null){
      logger.debug("Got null agentsInfo");
      return;
    }
    tableHelper.update(agentsInfo);
    logger.debug("Updated content of table");
  }

  private AgentHostsManager getAgentHostsManager(){
    return (AgentHostsManager) AppContextUtil.getApplicationContext().getBean("agentHostsManager");
  }

  private RunManager getRunManager(){
    return (RunManager) AppContextUtil.getApplicationContext().getBean("runManager");
  }
}
