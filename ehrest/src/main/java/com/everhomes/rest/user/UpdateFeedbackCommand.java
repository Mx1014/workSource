package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * 
 * @author 
 * <ul> 
 * 	<li>id: id</li>
 *  <li>status: 处理状态  0-未处理， 1-已处理</li>
 *  <li>verifyType: 核实情况  0-不属实， 1-属实</li>
 *  <li>handleType: 处理方式 0-无， 1-删除</li>
 * </ul>
 */
public class UpdateFeedbackCommand {
	private Long id;
	private Byte status;
	private Byte verifyType;
    private Byte handleType;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Byte getVerifyType() {
		return verifyType;
	}

	public void setVerifyType(Byte verifyType) {
		this.verifyType = verifyType;
	}

	public Byte getHandleType() {
		return handleType;
	}

	public void setHandleType(Byte handleType) {
		this.handleType = handleType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
