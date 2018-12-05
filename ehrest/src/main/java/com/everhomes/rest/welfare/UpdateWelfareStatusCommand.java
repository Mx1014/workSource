// @formatter:off
package com.everhomes.rest.welfare;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>welfareId: 福利Id</li>
 * <li>status: 状态:0-草稿 1-发送中 2-已发送成功 3-发送失败 </li>
 * </ul>
 */
public class UpdateWelfareStatusCommand {

	private Long welfareId;
	
    private Byte status;
  
    

	public Long getWelfareId() {
		return welfareId;
	}

	public void setWelfareId(Long welfareId) {
		this.welfareId = welfareId;
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
