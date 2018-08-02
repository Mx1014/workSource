package com.everhomes.rest.contract;

import java.security.Timestamp;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 *     <li>endTimeByDay: 退约时间</li>
 *     <li>contractId: 合同id</li>
 *     <li>communityId: 园区id</li>
 *     <li>categoryId: 合同类型categoryId，用于多入口</li>
 *     <li>namespaceId: namespace_id</li>
 * </ul>
 * Created by dingjianmin on 2017/8/2.
 */

public class GetDurationParamCommand {
	
	private Long endTimeByDay;
	private Long contractId;
	private Long categoryId;
	private Long communityId;
    private Integer namespaceId;
    
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
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public Long getEndTimeByDay() {
		return endTimeByDay;
	}
	public void setEndTimeByDay(Long endTimeByDay) {
		this.endTimeByDay = endTimeByDay;
	}
	
	public Long getContractId() {
		return contractId;
	}
	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
