package com.everhomes.rest.yellowPage;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;


/**
* 
* <ul>
* <li>namespaceId : 域空间id</li>
* <li>ownerType: 所属类型</li>
* <li>ownerId: 所属项目ID</li>
* <li>appId: 应用id 校验权限用</li>
* <li>currentPMId: 管理公司id 校验权限用</li>
* <li>targetId: 服务id(service alliance)</li>
* </ul>
*  @author
*  huangmingbo 2018年6月05日
**/
public class ListOnlineServicesCommand {
	
	@NotNull
	private Integer namespaceId;
    @NotNull
    private String ownerType;
    @NotNull
    private Long ownerId;
    @NotNull
	private Long appId;	
    @NotNull
	private Long currentPMId;
    @NotNull
    private Long targetId; 
    
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public Long getCurrentPMId() {
		return currentPMId;
	}

	public void setCurrentPMId(Long currentPMId) {
		this.currentPMId = currentPMId;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}
    
    
}
