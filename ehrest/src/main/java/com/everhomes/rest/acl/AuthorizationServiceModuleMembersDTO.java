package com.everhomes.rest.acl;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.io.Serializable;
import java.util.List;

/**
 * <ul>
 * <li>id：主键id</li>
 * <li>organizationId：组织id。</li>
 * <li>contactName：联系名称</li>
 * <li>contactType：联系类型。0-手机号，1-邮箱 {@link com.everhomes.rest.user.IdentifierType}</li>
 * <li>contactToken：联系方式:手机号/邮箱</li>
 * <li>targetType：类型</li>
 * <li>targetId：用户id</li>
 * <li>authorizationServiceModules：授权的业务模块，参考{@link com.everhomes.rest.acl.AuthorizationServiceModuleDTO}</li>
 * </ul>
 */
public class AuthorizationServiceModuleMembersDTO implements Serializable {

	private Long     id;
	private Long     organizationId;
	private String   contactName;
	private Byte     contactType;
	private String   contactToken;
	private String   targetType;
	private Long targetId;

	@ItemType(AuthorizationServiceModuleDTO.class)
	private List<AuthorizationServiceModuleDTO> authorizationServiceModules;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public Byte getContactType() {
		return contactType;
	}

	public void setContactType(Byte contactType) {
		this.contactType = contactType;
	}

	public String getContactToken() {
		return contactToken;
	}

	public void setContactToken(String contactToken) {
		this.contactToken = contactToken;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public List<AuthorizationServiceModuleDTO> getAuthorizationServiceModules() {
		return authorizationServiceModules;
	}

	public void setAuthorizationServiceModules(List<AuthorizationServiceModuleDTO> authorizationServiceModules) {
		this.authorizationServiceModules = authorizationServiceModules;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    
}
