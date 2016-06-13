package com.everhomes.rest.business;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>targetid:第三方店铺id</li>
 * 	<li>namespaceId:命名空间</li>
 * </ul>
 *
 */
public class BusinessAsignedNamespaceCommand {
	@NotNull
	private String targetId;
	@NotNull
	private Integer namespaceId;
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
