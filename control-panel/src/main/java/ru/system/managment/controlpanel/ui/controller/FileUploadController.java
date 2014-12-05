package ru.system.managment.controlpanel.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import ru.system.managment.common.socket.model.packets.AgentInfo;
import ru.system.managment.common.socket.model.packets.FileReceivedPacket;
import ru.system.managment.controlpanel.AppContextUtil;
import ru.system.managment.controlpanel.logic.*;

import java.io.File;
import java.util.Set;

/**
 * @author Mikhail Zaitsev
 */
public class FileUploadController implements FileSenderListener, AgentHostsManagerListener {

  private FileChooser fileChooser;

  @FXML
  private Button chooseFileBtn;

  @FXML
  private Label fileNameLabel;

  @FXML
  private Label statusSentLabel;

  public FileUploadController() {
    fileChooser = new FileChooser();
    fileChooser.setTitle("Выбор файла");
    initAgentHostManager();

  }

  public void onChooseFile(){
    File file = fileChooser.showOpenDialog(chooseFileBtn.getScene().getWindow());
    if(file == null){
      return;
    }


    fileNameLabel.setText(file.getAbsolutePath() + file.getName());
    getFileSender().sendFile(file, );
  }

  @Override
  public void onSent(FileReceivedPacket packet) {
    if(packet.getStatus() == FileReceivedPacket.STATUS_SUCCESS){
      statusSentLabel.setText("Файл успешно отправлен");
    } else if(packet.getStatus() == FileReceivedPacket.STATUS_ERROR){
      statusSentLabel.setText("Не удалось отправить файл");
    }
  }

  private FileSender getFileSender(){
    FileSender sender = (FileSender) AppContextUtil.getApplicationContext().getBean("fileSender");
    sender.setListener(this);
    return sender;
  }

  private AgentHostsManager initAgentHostManager(){
    AgentHostsManager agentHostsManager = (AgentHostsManager) AppContextUtil.getApplicationContext().getBean("agentHostManager");
    if (!agentHostsManager.getListeners().contains(this)){
      agentHostsManager.getListeners().add(this);
    }
    return agentHostsManager;
  }

  @Override
  public void onGetHostsList(Set<AgentInfo> agentsInfo) {

  }
}
