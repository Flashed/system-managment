package ru.system.managment.common.socket;

import ru.system.managment.common.model.Request;
import ru.system.managment.common.model.Response;

/**
 * Listener of SocketManager's events
 */
public interface SocketManagerListener {

  void onGetRequest(Request request);

  void onGetResponse(Response response);

  void onSendRequest(Request request);

  void onSendResponse(Response response);
}
