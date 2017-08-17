// @formatter:off
package com.everhomes.rest.express;

import java.util.Map;

import com.everhomes.util.StringHelper;

/**
 * <ul>参数:
 * <li>ownerType: 所属者类型，参考{@link com.everhomes.rest.express.ExpressOwnerType}</li>
 * <li>ownerId: 所属者id</li>
 * <li>id: 快递id</li>
 * </ul>
 */
public class PrePayExpressOrderResponse {
	private Integer errorCode;
	private Byte success;
	private String content;
	private Map<String,String> data;

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public Byte getSuccess() {
		return success;
	}

	public void setSuccess(Byte success) {
		this.success = success;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
