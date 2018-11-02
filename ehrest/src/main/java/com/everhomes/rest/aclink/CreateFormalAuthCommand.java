// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: id,选楼层时,要拼接楼栋id,如1001_1</li>
 * <li>type: 门禁组类型 0单个门禁 1门禁组 2园区 3楼栋 4楼层 5门牌{@link com.everhomes.rest.aclink.AuthGroupType}</li>
 * <li>rightOpen: 开门权限 0无 1有 null默认有</li>
 * <li>rightRemote: 远程开门权限 0无 1有 null默认无</li>
 * <li>rightVisitor: 访客授权权限 0无 1有 null默认无</li>
 * </ul>
 */
public class CreateFormalAuthCommand {
	private String id;
	private Byte type;
	private Byte rightOpen;
	private Byte rightRemote;
	private Byte rightVisitor;
	
	public Byte getRightOpen() {
		return rightOpen;
	}
	public void setRightOpen(Byte rightOpen) {
		this.rightOpen = rightOpen;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
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
	
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
