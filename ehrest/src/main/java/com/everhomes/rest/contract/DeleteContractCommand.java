package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 合同id</li>
 *     <li>partyAId: 合同甲方id</li>
 *     <li>categoryId: 合同类型categoryId，用于多入口</li>
 *     <li>contractApplicationScene: 合同适用场景contractApplicationScene，0 租赁合同场景 1 物业合同场景 2 综合合同场景</li>
 * </ul>
 * Created by ying.xiong on 2017/8/21.
 */
public class DeleteContractCommand {
    private Long id;

    private Long partyAId;

    private Long orgId;

    private Long communityId;

    private Integer namespaceId;

    private Byte paymentFlag = 0;
    
    private Byte contractApplicationScene = 0;

    public Byte getContractApplicationScene() {
		return contractApplicationScene;
	}

	public void setContractApplicationScene(Byte contractApplicationScene) {
		this.contractApplicationScene = contractApplicationScene;
	}

	public Byte getPaymentFlag() {
        return paymentFlag;
    }

    public void setPaymentFlag(Byte paymentFlag) {
        this.paymentFlag = paymentFlag;
    }
    
	private Long categoryId;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
    private Boolean checkAuth;

    public Boolean getCheckAuth() {
        return checkAuth;
    }

    public void setCheckAuth(Boolean checkAuth) {
        this.checkAuth = checkAuth;
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
