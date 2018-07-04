package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>communityId: 园区id</li>
 * <li>payorreceiveContractType: 合同类型 0 收款合同 1付款合同</li>
 * <li>generateContractNumberRule: 上送的合同生成规则</li>
 * <li>categoryId: 合同类型categoryId，用于多入口</li>
 * </ul>
 * Created by ying.xiong on 2017/8/2.
 */
public class GenerateContractNumberCommand {

	private Integer namespaceId;
	private Long communityId;
	private Long orgId;
	private Byte payorreceiveContractType;
	private String generateContractNumberRule;
	private Long categoryId;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getGenerateContractNumberRule() {
		return generateContractNumberRule;
	}

	public void setGenerateContractNumberRule(String generateContractNumberRule) {
		this.generateContractNumberRule = generateContractNumberRule;
	}

	public Byte getPayorreceiveContractType() {
		return payorreceiveContractType;
	}

	public void setPayorreceiveContractType(Byte payorreceiveContractType) {
		this.payorreceiveContractType = payorreceiveContractType;
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
