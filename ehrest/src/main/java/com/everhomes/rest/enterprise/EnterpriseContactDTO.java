package com.everhomes.rest.enterprise;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 * <li>userId: 通讯录绑定的用户ID</li>
 * <li>role: TODO 用户在公司的角色</li>
 * <li>enterpriseId: 用户所在企业的ID</li>
 * </ul>
 * @author janson
 *
 */
public class EnterpriseContactDTO {
    private java.lang.Long     id;
    private java.lang.Long     enterpriseId;
    private java.lang.String   name;
    private java.lang.String   nickName;
    private java.lang.String   avatar;
    private String employeeNo;
    private String groupName;
    private String sex;
    private String phone;
    private java.lang.Long     userId;
    private java.lang.Long     role;
    private java.lang.Byte     status;
    private java.lang.Long     creatorUid;
    private java.sql.Timestamp createTime;
    
    @ItemType(EnterpriseContactEntryDTO.class)
    private List<EnterpriseContactEntryDTO> entries;

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(java.lang.Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getNickName() {
        return nickName;
    }

    public void setNickName(java.lang.String nickName) {
        this.nickName = nickName;
    }

    public java.lang.String getAvatar() {
        return avatar;
    }

    public void setAvatar(java.lang.String avatar) {
        this.avatar = avatar;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public java.lang.Long getUserId() {
        return userId;
    }

    public void setUserId(java.lang.Long userId) {
        this.userId = userId;
    }

    public java.lang.Long getRole() {
        return role;
    }

    public void setRole(java.lang.Long role) {
        this.role = role;
    }

    public java.lang.Byte getStatus() {
        return status;
    }

    public void setStatus(java.lang.Byte status) {
        this.status = status;
    }

    public java.lang.Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(java.lang.Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public java.sql.Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(java.sql.Timestamp createTime) {
        this.createTime = createTime;
    }

    public List<EnterpriseContactEntryDTO> getEntries() {
        return entries;
    }

    public void setEntries(List<EnterpriseContactEntryDTO> entries) {
        this.entries = entries;
    }

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
}
