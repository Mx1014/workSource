package com.everhomes.rest.pmsy;

/**
 * <ul>
 * <li>id: ID</li>
 * <li>communityId: 小区ID</li>
 * <li>contact: 手机号</li>
 * <li>billTip: 提示信息</li>
 * </ul>
 */
public class PmsyCommunitiesDTO {
	
	private Long id;
	private Long communityId;
	private String contact;
	private String billTip;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getBillTip() {
		return billTip;
	}
	public void setBillTip(String billTip) {
		this.billTip = billTip;
	}
	
	
}
