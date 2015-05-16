package com.everhomes.contentserver;

import org.springframework.web.socket.WebSocketSession;

/**
 * call back when connection closed
 * @author elians
 *
 */
public interface WebSocketCallback {
	//connection closed
	void onClose(WebSocketSession seesion);
}
