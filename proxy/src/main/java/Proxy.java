
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.system.managment.common.socket.acceptor.Acceptor;
import ru.system.managment.common.socket.acceptor.AcceptorListener;
import ru.system.managment.common.socket.model.SocketData;

import java.nio.channels.SocketChannel;

public class Proxy implements Runnable, AcceptorListener{

  private static final Logger logger = LoggerFactory.getLogger(Proxy.class);

  private Acceptor acceptor;


  @Override
  public void run() {
    try {
      acceptor.acceptAndRead();
    } catch (Exception e) {
      throw new RuntimeException();
    }
  }

  public void setAcceptor(Acceptor acceptor) {
    this.acceptor = acceptor;
  }

  @Override
  public void onAccept(SocketChannel channel) {

  }

  @Override
  public void onRead(SocketData data) {

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
