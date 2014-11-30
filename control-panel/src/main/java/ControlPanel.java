import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.system.managment.common.socket.connector.Connector;
import ru.system.managment.common.socket.connector.ConnectorListener;
import ru.system.managment.common.socket.model.SocketData;
import ru.system.managment.controlpanel.AppContextUtil;
import ru.system.managment.controlpanel.UIComponent;

public class ControlPanel implements Runnable, ConnectorListener {

  private static final Logger logger = LoggerFactory.getLogger(ControlPanel.class);

  private Connector connector;

  @Override
  public void run() {
    try{
      Application.launch(UIComponent.class);
    }catch (Exception e){
      throw new RuntimeException("Failed to run ControlPanel",e);
    }
  }

  @Override
  public void onReadData(SocketData socketData) {

  }

  public void setConnector(Connector connector) {
    this.connector = connector;
  }

  public static void main(String... args){
    try{
      ApplicationContext context = AppContextUtil.getApplicationContext();
      ControlPanel panel = (ControlPanel) context.getBean("panel");
      panel.run();
    } catch (Exception e){
      logger.error("Application error", e);
    }

  }

}
