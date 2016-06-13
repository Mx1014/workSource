package com.everhomes.rest.enterprise;

/**
 * <ul>修改企业通讯录的基本信息</ul>
 * @author janson
 *
 */
public class UpdateContactCommand {
    private Long contactId; 
    private java.lang.String   name;
    private java.lang.String   nickName;
    private java.lang.String   avatar;
    private java.lang.Long     role;  
    private java.lang.Long     contactGroupId;  
    private String employeeNo;
    private String sex;
      
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
    public java.lang.Long getRole() {
        return role;
    }
    public void setRole(java.lang.Long role) {
        this.role = role;
    }
     
	public Long getContactId() {
		return contactId;
	}
	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}
	public java.lang.Long getContactGroupId() {
		return contactGroupId;
	}
	public void setContactGroupId(java.lang.Long contactGroupId) {
		this.contactGroupId = contactGroupId;
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
