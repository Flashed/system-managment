package ru.system.managment.controlpanel;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppContextUtil {

  private static ApplicationContext applicationContext = new ClassPathXmlApplicationContext("appContext.xml");

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }
}
