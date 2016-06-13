package com.everhomes.rest.pmsy;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>communityId : 小区ID（左邻）</li>
 * <li>projectId : 项目ID（来自第三方）</li>
 * <li>billTip : 提示信息</li>
 * <li>contact : 提示手机号</li>
 *</ul>
 *
 */
public class SetPmsyPropertyCommand {
	private Long communityId;
	@NotNull
	private String billTip;
	@NotNull
	private String contact;
	
	
	
	public String getBillTip() {
		return billTip;
	}
	public void setBillTip(String billTip) {
		this.billTip = billTip;
	}
	public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
