package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

public class UserInfoFroBiz {
	
	private Long id;
	private String nickName;
	private Integer namespaceId;
	private String namespaceUserToken;
	private String telePhone;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public String getNamespaceUserToken() {
		return namespaceUserToken;
	}
	public void setNamespaceUserToken(String namespaceUserToken) {
		this.namespaceUserToken = namespaceUserToken;
	}
	public String getTelePhone() {
		return telePhone;
	}
	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	
	

}
