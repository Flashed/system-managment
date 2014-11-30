package ru.system.managment.controlpanel.ui.controller;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import ru.system.managment.controlpanel.AppContextUtil;
import ru.system.managment.controlpanel.logic.ConnectionManager;
import ru.system.managment.controlpanel.logic.ConnectionManagerListener;

public class ConnectionTabController implements ConnectionManagerListener{

  private static final Logger logger = LoggerFactory.getLogger(ConnectionTabController.class);

  @FXML
  private Label statusLabel;

  @FXML
  private Button connectionBtn;

  public void onClickConnection(){
    try{
      final ConnectionManager manager = getConnectionManager();
      manager.setListener(this);
      connectionBtn.setDisable(true);
      manager.openConnection();
    } catch (Exception e){
      logger.error("Failed to handle open connection click", e);
      statusLabel.setTextFill(Color.RED);
      statusLabel.setText("Не удалось установить соединение");
      connectionBtn.setDisable(false);
    }
  }

  private ConnectionManager getConnectionManager(){
    ApplicationContext context = AppContextUtil.getApplicationContext();
    return (ConnectionManager) context.getBean("connectionManager");
  }

  @Override
  public void onConnected() {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        if(logger.isDebugEnabled()){
          logger.debug("Connection opened successfully");
        }
        statusLabel.setTextFill(Color.GREEN);
        statusLabel.setText("Соединение успешно установлено");
      }
    });
  }

  @Override
  public void onDisconnected() {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        if(logger.isDebugEnabled()){
          logger.debug("Disconnected");
        }
        connectionBtn.setDisable(false);
        statusLabel.setTextFill(Color.RED);
        statusLabel.setText("Соединение разорвано");
      }
    });

  }

  @Override
  public void errorConnection(final Throwable e) {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        logger.error("Failed to open connection", e);
        connectionBtn.setDisable(false);
        statusLabel.setTextFill(Color.RED);
        statusLabel.setText("Не удалось установить соединение");
      }
    });
  }
}
