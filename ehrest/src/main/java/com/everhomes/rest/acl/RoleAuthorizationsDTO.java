// @formatter:off
package com.everhomes.rest.acl;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.acl.admin.RoleDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>targetId：对象id</li>
 * <li>targetType：对象类型，Eh_Users, Eh_Organizations,{@link com.everhomes.rest.common.EntityType}</li>
 * <li>identifierType：联系类型。0-手机号，1-邮箱 {@link com.everhomes.rest.user.IdentifierType}</li>
 * <li>targetName：对象名称</li>
 * <li>nikeName：别名</li>
 * <li>identifierToken：手机号码</li>
 * <li>roles：角色列表，{@link com.everhomes.rest.acl.admin.RoleDTO}</li>
 * </ul>
 */
public class RoleAuthorizationsDTO {

	private Long targetId;

	private String targetType;

	private String   targetName;

	private Byte     identifierType;

	private String   identifierToken;

	private String nikeName;


	@ItemType(RoleDTO.class)
	private List<RoleDTO> roles;

	public RoleAuthorizationsDTO() {
    }

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public Byte getIdentifierType() {
		return identifierType;
	}

	public void setIdentifierType(Byte identifierType) {
		this.identifierType = identifierType;
	}

	public String getIdentifierToken() {
		return identifierToken;
	}

	public void setIdentifierToken(String identifierToken) {
		this.identifierToken = identifierToken;
	}

	public String getNikeName() {
		return nikeName;
	}

	public void setNikeName(String nikeName) {
		this.nikeName = nikeName;
	}

	public List<RoleDTO> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleDTO> roles) {
		this.roles = roles;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
