// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>authIds: 授权id列表</li>
 * <li>rightRemote: 0 远程访问未授权， 1 授权</li>
 * <li>rightVisitor: 0 访客未授权， 1 授权</li>
 * <li>status: 0 删除</li>
 * </ul>
 */
public class UpdateAuthBatchCommand {
	private List<Long> authIds;
	private Byte rightRemote;
	private Byte rightVisitor;
	private Byte status;
	
	public List<Long> getAuthIds() {
		return authIds;
	}
	public void setAuthIds(List<Long> authIds) {
		this.authIds = authIds;
	}
	public Byte getRightRemote() {
		return rightRemote;
	}
	public void setRightRemote(Byte rightRemote) {
		this.rightRemote = rightRemote;
	}
	public Byte getRightVisitor() {
		return rightVisitor;
	}
	public void setRightVisitor(Byte rightVisitor) {
		this.rightVisitor = rightVisitor;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
