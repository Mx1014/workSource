package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * 	<li>id: id</li>
 *  <li>verifyType: 核实情况  0-不属实， 1-属实</li>
 *  <li>handleType: 处理方式 0-无， 1-删除</li>
 * </ul>
 */
public class UpdateFeedbackCommand {
	private Long id;
	private Byte verifyType;
    private Byte handleType;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
