package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>communityId: communityId</li>
 *     <li>categoryId: 合同应用id</li>
 *     <li>name: 任务名称</li>
 *     <li>process: 处理进度</li>
 *     <li>processedNumber: 已处理记录</li>
 *     <li>totalNumber: 总记录</li>
 *     <li>errorDescription: 错误信息</li>
 * </ul>
 * Created by djm on 2018/8/30.
 */
public class SearchProgressDTO {

	private Integer namespaceId;
	private Long communityId;
	private String name;
	private Integer process;
	private Integer processedNumber;
	private Integer totalNumber;
	private String errorDescription;
	
	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public Integer getProcessedNumber() {
		return processedNumber;
	}

	public void setProcessedNumber(Integer processedNumber) {
		this.processedNumber = processedNumber;
	}

	public Integer getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(Integer totalNumber) {
		this.totalNumber = totalNumber;
	}

	public Integer getProcess() {
		return process;
	}

	public void setProcess(Integer process) {
		this.process = process;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
