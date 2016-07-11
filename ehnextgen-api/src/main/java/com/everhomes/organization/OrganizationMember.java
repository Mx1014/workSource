// @formatter:off
package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhOrganizationMembers;
import com.everhomes.util.StringHelper;

public class OrganizationMember extends EhOrganizationMembers implements Comparable<OrganizationMember> {
	
	private java.lang.String   nickName;
	private java.lang.String   avatar;
	
	private String initial;
	
    private String fullPinyin;
    private String fullInitial;
	
	private java.lang.Long creatorUid;
	
	private static final long serialVersionUID = 2994038655987093227L;

	public OrganizationMember() {
    }
    
	
	
    public java.lang.String getNickName() {
		return nickName;
	}



	public void setNickName(java.lang.String nickName) {
		this.nickName = nickName;
	}



	public java.lang.String getAvatar() {
		return avatar;
	}



	public void setAvatar(java.lang.String avatar) {
		this.avatar = avatar;
	}



	public java.lang.Long getCreatorUid() {
		return creatorUid;
	}



	public void setCreatorUid(java.lang.Long creatorUid) {
		this.creatorUid = creatorUid;
	}



	public String getInitial() {
		return initial;
	}



	public void setInitial(String initial) {
		this.initial = initial;
	}

	
	public String getFullPinyin() {
		return fullPinyin;
	}



	public void setFullPinyin(String fullPinyin) {
		this.fullPinyin = fullPinyin;
	}



	public String getFullInitial() {
		return fullInitial;
	}



	public void setFullInitial(String fullInitial) {
		this.fullInitial = fullInitial;
	}



	public int compareTo(OrganizationMember organizationMember) {
	    return this.initial.compareTo(organizationMember.getInitial());
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
}
