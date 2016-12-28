package com.everhomes.flow;

/**
 * 
 * @author janson
 *
 */
public class FlowStepErrorException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5687581749300356498L;

	public FlowStepErrorException(String message) {
        super(message);
    }
}
