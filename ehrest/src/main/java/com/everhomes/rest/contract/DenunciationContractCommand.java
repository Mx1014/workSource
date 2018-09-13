package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 合同id</li>
 *     <li>partyAId: 合同甲方id</li>
 *     <li>denunciationUid: 退约经办人</li>
 *     <li>denunciationTime: 退约时间</li>
 *     <li>denunciationReason: 退约原因</li>
 *     <li>categoryId: 合同类型categoryId，用于多入口</li>
 *     <li>costGenerationMethod: 费用收取方式，0：按计费周期，1：按实际天数</li>
 * </ul>
 * Created by ying.xiong on 2017/8/2.
 */
public class DenunciationContractCommand {

    private Long id;
    private Long partyAId;
    private Long denunciationUid;
    private Long denunciationTime;
    private String denunciationReason;
    private Long orgId;
    private Long communityId;
    private Integer namespaceId;
    private Byte paymentFlag = 0;
	private Long categoryId;
	private Byte costGenerationMethod;
	
	public Byte getCostGenerationMethod() {
		return costGenerationMethod;
	}

	public void setCostGenerationMethod(Byte costGenerationMethod) {
		this.costGenerationMethod = costGenerationMethod;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	
    public Long getDenunciationTime() {
        return denunciationTime;
    }

    public void setDenunciationTime(Long denunciationTime) {
        this.denunciationTime = denunciationTime;
    }

    public Byte getPaymentFlag() {
        return paymentFlag;
    }

    public void setPaymentFlag(Byte paymentFlag) {
        this.paymentFlag = paymentFlag;
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

    public Long getPartyAId() {
        return partyAId;
    }

    public void setPartyAId(Long partyAId) {
        this.partyAId = partyAId;
    }

    public Long getDenunciationUid() {
        return denunciationUid;
    }

    public void setDenunciationUid(Long denunciationUid) {
        this.denunciationUid = denunciationUid;
    }

    public String getDenunciationReason() {
        return denunciationReason;
    }

    public void setDenunciationReason(String denunciationReason) {
        this.denunciationReason = denunciationReason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
