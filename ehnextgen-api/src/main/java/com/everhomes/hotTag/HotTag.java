package com.everhomes.hotTag;

import com.everhomes.server.schema.tables.pojos.EhHotTags;
import com.everhomes.util.StringHelper;

public class HotTag extends EhHotTags {

	private static final long serialVersionUID = -5625173247962467395L;
	
	private Byte hotFlag;

	public Byte getHotFlag() {
		return hotFlag;
	}

	public void setHotFlag(Byte hotFlag) {
		this.hotFlag = hotFlag;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
