import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Agent {

  private static final Logger logger = LoggerFactory.getLogger(Agent.class);

  public static void main(String... args){
    logger.info("{} start..." , Agent.class.getName());
  }
}
