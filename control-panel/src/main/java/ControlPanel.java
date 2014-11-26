import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.system.managment.common.socket.connector.Connector;
import ru.system.managment.common.socket.connector.ConnectorListener;
import ru.system.managment.common.socket.model.SocketData;

public class ControlPanel implements Runnable, ConnectorListener {

  private static final Logger logger = LoggerFactory.getLogger(ControlPanel.class);

  private Connector connector;

  @Override
  public void run() {
    try{
      connector.connectAndRead();
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
      ApplicationContext context = new ClassPathXmlApplicationContext("appContext.xml");
      ControlPanel panel = (ControlPanel) context.getBean("panel");
      panel.run();
    } catch (Exception e){
      logger.error("Application error", e);
    }

  }

}
