package com.everhomes.rest.acl;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 所属实体类型,</li>
 * <li>ownerId:  所属实体id</li>
 * <li>level: 业务模块级别</li>
 * </ul>
 */
public class ListServiceModulesCommand {
	
	private String ownerType;

    private Long ownerId;

    private Integer level;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
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

}
