package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class FlowEvaluateItemStar {
	private Long itemId;
	private Byte stat;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Byte getStat() {
		return stat;
	}

	public void setStat(Byte stat) {
		this.stat = stat;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
