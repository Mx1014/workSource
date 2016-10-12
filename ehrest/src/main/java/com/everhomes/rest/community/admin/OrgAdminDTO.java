package com.everhomes.rest.community.admin;
/**
 * <ul>
 * <li>name: 名字</li>  
 * <li>contactToken：联系方式</li> 
 * </ul>
 */
public class OrgAdminDTO {
	private String name; 
	private Byte   contactType;
	private String contactToken;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Byte getContactType() {
		return contactType;
	}
	public void setContactType(Byte contactType) {
		this.contactType = contactType;
	}
	public String getContactToken() {
		return contactToken;
	}
	public void setContactToken(String contactToken) {
		this.contactToken = contactToken;
	}
	
	
}
