// @formatter:off
package com.everhomes.express;

public interface ExpressHandler {
	
	String EXPRESS_HANDLER_PREFIX = "express_handler_";

	String handle(ExpressOrder expressOrder);
	
}
