
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.system.managment.common.socket.acceptor.Acceptor;
import ru.system.managment.proxy.logic.IdentityManager;

import java.nio.channels.SocketChannel;

public class Proxy implements Runnable{

  private static final Logger logger = LoggerFactory.getLogger(Proxy.class);

  private Acceptor acceptor;

  private IdentityManager identityManager;


  @Override
  public void run() {
    try {
      identityManager.start();
      acceptor.acceptAndRead();
    } catch (Exception e) {
      try {
        identityManager.stop();
      } catch (Exception ignore){}
      throw new RuntimeException("Error running" ,e);
    }
  }

  public void setIdentityManager(IdentityManager identityManager) {
    this.identityManager = identityManager;
  }

  public void setAcceptor(Acceptor acceptor) {
    this.acceptor = acceptor;
  }

  public static void main(String... args){
    try{
      ApplicationContext context = new ClassPathXmlApplicationContext("appContext.xml");
      Proxy proxy = (Proxy) context.getBean("proxy");
      proxy.run();
    }catch (Exception e){
      logger.error("Application error", e);
    }
  }
}
