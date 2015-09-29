package com.everhomes.organization;

import java.util.List;

import com.everhomes.address.Address;
import com.everhomes.organization.pm.CommunityPmContact;

public class OrganizationVo {
	
	private Organization org;
	private List<OrganizationCommunity> orgComms;
	private List<CommunityPmContact> orgContacts;
	private Address address;
	
	public Organization getOrg() {
		return org;
	}
	public void setOrg(Organization org) {
		this.org = org;
	}
	public List<OrganizationCommunity> getOrgComms() {
		return orgComms;
	}
	public void setOrgComms(List<OrganizationCommunity> orgComms) {
		this.orgComms = orgComms;
	}
	public List<CommunityPmContact> getOrgContacts() {
		return orgContacts;
	}
	public void setOrgContacts(List<CommunityPmContact> orgContacts) {
		this.orgContacts = orgContacts;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
	

}
