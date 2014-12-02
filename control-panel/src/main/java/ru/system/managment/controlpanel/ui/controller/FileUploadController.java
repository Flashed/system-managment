package ru.system.managment.controlpanel.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * @author Mikhail Zaitsev
 */
public class FileUploadController {

  private FileChooser fileChooser;

  @FXML
  private Button chooseFileBtn;

  @FXML
  private Label fileNameLabel;

  public FileUploadController() {
    fileChooser = new FileChooser();
    fileChooser.setTitle("Выбор файла");
  }

  public void onChooseFile(){
    File file = fileChooser.showOpenDialog(chooseFileBtn.getScene().getWindow());
    if(file == null){
      return;
    }
    fileNameLabel.setText(file.getAbsolutePath() + file.getName());
  }

}
