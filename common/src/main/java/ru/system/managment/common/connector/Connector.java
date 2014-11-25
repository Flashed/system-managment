package ru.system.managment.common.connector;

import ru.system.managment.common.model.Request;
import ru.system.managment.common.model.Response;

/**
 * A connector interface
 */
public interface Connector {

  void startAccept();

  void sendRequest(Request request);

  void sendResponse(Response response);


}
