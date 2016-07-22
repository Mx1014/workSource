package com.everhomes.wanke;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.util.StringHelper;

public class MashenResponseEntity {
	private static final Logger LOGGER = LoggerFactory.getLogger(MashenResponseEntity.class);

	
	private Integer status;
	private Object data;
	private String message;
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Map<String, Object> getData() {
		if(data == null)
			LOGGER.error("MashenResponseEntity data is null {}", this);
		return (Map<String, Object>) data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean isSuccess() {
		if(status.intValue() == 0)
			return true;
		return false;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
