import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.system.managment.agent.logic.ConnectionManager;
import ru.system.managment.common.socket.connector.Connector;

public class Agent implements Runnable{

  private static final Logger logger = LoggerFactory.getLogger(Agent.class);

  private ConnectionManager connectionManager;

  public static void main(String... args){
    ApplicationContext context = new ClassPathXmlApplicationContext("appContext.xml");
    Agent agent = (Agent) context.getBean("agent");
    agent.run();
  }

  @Override
  public void run() {
      try{
          connectionManager.openConnection();
      } catch (Exception e){
        logger.error("Application error", e);
      }
  }

  public void setConnectionManager(ConnectionManager connectionManager) {
    this.connectionManager = connectionManager;
  }
}
