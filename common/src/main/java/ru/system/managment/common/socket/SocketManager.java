package ru.system.managment.common.socket;

import ru.system.managment.common.model.Request;
import ru.system.managment.common.model.Response;

/**
 * A connector interface
 */
public interface SocketManager {

  void init(SocketManagerConfig config);

  void startAccept() throws Exception;

  void sendRequest(Request request)  throws Exception;

  void sendResponse(Response response)  throws Exception;


}
