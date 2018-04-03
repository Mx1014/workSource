package com.everhomes.rest.community.admin;

import com.everhomes.util.StringHelper;

/**
 * <p>
 * <ul>
 * <li>organizationId:  公司id</li> 
 * <li>organizationName:  公司名</li> 
 * <li>operationType:  0-退出企业 1-加入企业</li> 
 * <li>operateTime: 时间 </li> 
 * <li>operatorUid:  操作人id</li>  
 * <li>operatorNickName:  操作人昵称</li>  
 * </ul>
 */
public class OrganizationMemberLogDTO {
 
    private Long organizationId;
    private String organizationName;
    private String contactName; 
    private Byte operationType;
    private Byte requestType;
    private Long operateTime;
    private Long operatorUid;
    private String operatorNickName;
    

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public Byte getOperationType() {
		return operationType;
	}
	public void setOperationType(Byte operationType) {
		this.operationType = operationType;
	}
	public Byte getRequestType() {
		return requestType;
	}
	public void setRequestType(Byte requestType) {
		this.requestType = requestType;
	}
	public Long getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Long operateTime) {
		this.operateTime = operateTime;
	}
	public Long getOperatorUid() {
		return operatorUid;
	}
	public void setOperatorUid(Long operatorUid) {
		this.operatorUid = operatorUid;
	}

	public String getOperatorNickName() {
		return operatorNickName;
	}

	public void setOperatorNickName(String operatorNickName) {
		this.operatorNickName = operatorNickName;
	}
    
    
}
