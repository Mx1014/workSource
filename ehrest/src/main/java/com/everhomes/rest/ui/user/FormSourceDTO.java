// @formatter:off
package com.everhomes.rest.ui.user;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>sourceType : 表单类型 {@link com.everhomes.rest.general_approval.GeneralFormSourceType}</li>
 * <li>sourceId : 表单id</li>
 * </ul>
 *
 *  @author:dengs 2017年7月6日
 */
public class FormSourceDTO {
	private String ownerType;
    private Long ownerId;
    private String sourceType;
    private Long sourceId;
    
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public Long getSourceId() {
		return sourceId;
	}
	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}
    
    @Override
    public String toString() {
    	return StringHelper.toJsonString(this);
    }
}
