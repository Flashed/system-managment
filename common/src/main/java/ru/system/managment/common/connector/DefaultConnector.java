package ru.system.managment.common.connector;

import ru.system.managment.common.model.Request;
import ru.system.managment.common.model.Response;

/**
 * Default implementation of Connector
 */
public class DefaultConnector implements Connector{

  private ConnectorListener connectorListener;

  /**
   * {@inheritDoc}
   */
  @Override
  public void startAccept() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendRequest(Request request) {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendResponse(Response response) {

  }

  public void setConnectorListener(ConnectorListener connectorListener) {
    this.connectorListener = connectorListener;
  }
}
