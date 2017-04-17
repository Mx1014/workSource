package com.everhomes.preview;

import com.everhomes.server.schema.tables.pojos.EhPreviews;
import com.everhomes.util.StringHelper;

public class Preview extends EhPreviews{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8299301531354647521L;

	public Preview() {
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
