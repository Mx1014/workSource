package com.everhomes.rest.common;

import com.everhomes.util.StringHelper;

import java.io.Serializable;

public class PrintOrderActionData implements Serializable{

	private static final long serialVersionUID = -7134989050392679006L;
	private Long orderId;
	private String url;
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
