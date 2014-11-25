package ru.system.managment.common.connector;

import ru.system.managment.common.model.Request;
import ru.system.managment.common.model.Response;

/**
 * Listener of Connector's events
 */
public interface ConnectorListener {

  void onGetRequest(Request request);

  void onGetResponse(Response response);

  void onSendRequest(Request request);

  void onSendResponse(Response response);
}
