package com.everhomes.rest.yellowPage;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id：申请id</li>
 *  <li>createTime：申请时间</li>
 *  <li>creatorName：用户姓名</li>
 *  <li>creatorMobile：手机号</li>
 *  <li>creatorOrganization：机构名称</li>
 *  <li>serviceOrganization：服务机构名</li>
 * </ul>
 */
public class RequestInfoDTO {
	
	private Long id;
	
	private String creatorName;
	
	private String creatorMobile;
	
	private String creatorOrganization;
	
	private String serviceOrganization;
	
	private String createTime;

	private String templateType;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreatorOrganization() {
		return creatorOrganization;
	}

	public void setCreatorOrganization(String creatorOrganization) {
		this.creatorOrganization = creatorOrganization;
	}

	public String getServiceOrganization() {
		return serviceOrganization;
	}

	public void setServiceOrganization(String serviceOrganization) {
		this.serviceOrganization = serviceOrganization;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getCreatorMobile() {
		return creatorMobile;
	}

	public void setCreatorMobile(String creatorMobile) {
		this.creatorMobile = creatorMobile;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
