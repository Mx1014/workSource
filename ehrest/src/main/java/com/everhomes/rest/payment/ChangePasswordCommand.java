package com.everhomes.rest.payment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>oldPassword: 旧密码</li>
 * <li>newPassword: 新密码</li>
 * <li>newRePassword: 再一次新密码</li>
 * </ul>
 */
public class ChangePasswordCommand {
	private String ownerType;
    private Long ownerId;
    private String oldPassword;
    private String newPassword;
    private String newRePassword;
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
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getNewRePassword() {
		return newRePassword;
	}
	public void setNewRePassword(String newRePassword) {
		this.newRePassword = newRePassword;
	}
    
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
