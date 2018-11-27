package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 合同id</li>
 *     <li>partyAId: 合同甲方id</li>
 *     <li>categoryId: 合同应用id</li>
 * </ul>
 * Created by ying.xiong on 2017/8/2.
 */
public class EntryContractCommand {

    private Long id;

    private Long partyAId;

    private Long orgId;

    private Long communityId;

    private Integer namespaceId;
    
    private Long categoryId;
    
    private String user;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPartyAId() {
        return partyAId;
    }

    public void setPartyAId(Long partyAId) {
        this.partyAId = partyAId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
    
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
