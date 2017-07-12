package com.everhomes.rest.ui.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>sceneType: 场景类型，{@link com.everhomes.rest.ui.user.SceneType}</li>
 * <li>entityType: 实体类型，{@link com.everhomes.rest.user.UserCurrentEntityType}</li>
 * <li>entityId: 实体ID</li>
 * <li>contactId:联系人ID</li>
 * <li>contactPhone:联系人手机</li>
 * <li>contactName:联系人名字</li>
 * <li>contactAvatar:头像</li>
 * <li>userId:通讯录关联的用户ID</li>
 * <li>createTime:创建通讯录时间</li>
 * <li>statusLine:个性签名</li>
 * <li>occupation:职业</li>
 * <li>departmentName:部门名称</li>
 * <li>neighborhoodRelation: 邻居关系参考，{@link com.everhomes.rest.family.NeighborhoodRelation}</li>
 * <li>fullPinyin: 名字全拼</li>
 * <li>fullInitial: 名字全首字母</li>
 * <li>initial: 首字母</li>
 * <li>organizationPath: 机构path 路径，含层次关系，如/父亲id/第一层孩子id/第二层孩子id/...</li>
 * </ul>
 */
public class SceneContactDTO {
    private String sceneType;
    private String entityType;
    private Long entityId;
    private Long contactId;
    private String contactPhone;
    private String contactName;
    private String contactAvatar;
    private Long userId;
    private Long createTime;
    
    private String departmentName;
    
    private String statusLine;
    private String occupation; 
    private String initial;
    private String fullPinyin;
    private String fullInitial;
    
    private Byte neighborhoodRelation;

    public String getSceneType() {
        return sceneType;
    }

    public void setSceneType(String sceneType) {
        this.sceneType = sceneType;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getContactAvatar() {
        return contactAvatar;
    }

    public void setContactAvatar(String contactAvatar) {
        this.contactAvatar = contactAvatar;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    
    public String getStatusLine() {
		return statusLine;
	}

	public void setStatusLine(String statusLine) {
		this.statusLine = statusLine;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	
	public String getInitial() {
		return initial;
	}

	public void setInitial(String initial) {
		this.initial = initial;
	}
	
	

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

    public Byte getNeighborhoodRelation() {
		return neighborhoodRelation;
	}

	public void setNeighborhoodRelation(Byte neighborhoodRelation) {
		this.neighborhoodRelation = neighborhoodRelation;
	}

	
	public String getFullPinyin() {
		return fullPinyin;
	}

	public void setFullPinyin(String fullPinyin) {
		this.fullPinyin = fullPinyin;
	}

	public String getFullInitial() {
		return fullInitial;
	}

	public void setFullInitial(String fullInitial) {
		this.fullInitial = fullInitial;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
