package com.everhomes.contentserver;

/**
 * constant
 * 
 * @author elians
 *
 */
public interface WebSocketConstant {
	// standard request name
	String CONTENT_STORAGE_REQ = "contentstorage.request.";
	// standard response name
	String CONTENT_STORAGE_RSP = "contentstorage.response.";

	// config request item prefix
	String CONTENT_CONFIG_REQ = "contentstorage.request.config.";

	// config response item prefix
	String CONTENT_CONFIG_RSP = "contentstorage.response.config.";

	String CONTENT_SERVER_CAPACITY_REQ = "contentstorage.request.capacity";

	String CONTENT_SERVER_CAPACITY_RSP = "contentstorage.response.capacity";

	interface Scheme {
		// https scheme
		String HTTPS = "https";

		// http scheme
		String HTTP = "http";
	}

	// ok message code 0
	int OK_CODE = 0x00000000;

	// err message code -1
	int ERR_CODE = 0xffffffff;
}
