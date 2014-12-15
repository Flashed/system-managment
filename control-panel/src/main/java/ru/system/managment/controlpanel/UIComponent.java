package ru.system.managment.controlpanel;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.system.managment.controlpanel.logic.ConnectionManager;

/**
 * @author Mikhail Zaitsev
 */
public class UIComponent extends Application {

  public UIComponent() {
  }

  @Override
  public void start(Stage stage) throws Exception {
    TabPane pane =  FXMLLoader.load(UIComponent.class.getResource("/main.fxml"));
    Scene scene = new Scene(pane);
    stage.setScene(scene);
    stage.setTitle("Control panel");
    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
      @Override
      public void handle(WindowEvent t) {
        getConnectionManager().disconnect();
        Platform.exit();
      }

    });
    stage.show();
  }

  private ConnectionManager getConnectionManager(){
    return (ConnectionManager) AppContextUtil.getApplicationContext().getBean("connectionManager");
  }
}
