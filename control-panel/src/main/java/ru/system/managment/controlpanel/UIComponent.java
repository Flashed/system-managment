package ru.system.managment.controlpanel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * @author Mikhail Zaitsev
 */
public class UIComponent extends Application {

  public UIComponent() {
  }

  @Override
  public void start(Stage stage) throws Exception {
    TabPane pane =  FXMLLoader.load(UIComponent.class.getResource("connectionTab.fxml"));
    Scene scene = new Scene(pane);
    stage.setScene(scene);
    stage.setTitle("Control panel");
    stage.show();
  }
}
