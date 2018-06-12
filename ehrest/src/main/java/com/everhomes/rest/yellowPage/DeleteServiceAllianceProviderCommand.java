package com.everhomes.rest.yellowPage;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;
/**
	* 
	* <ul>
	* <li>id : 服务商id</li>
	* <li>appId : 应用id 校验权限</li>
	* <li>currentPMId : 管理公司id校验权限</li>
	* </ul>
	*  @author
	*  huangmingbo 2018年5月17日
**/
public class DeleteServiceAllianceProviderCommand {
	
    @NotNull
    private Long id;
  
    @NotNull
	private Long appId;	
	
    @NotNull
	private Long currentPMId;
    

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
}
