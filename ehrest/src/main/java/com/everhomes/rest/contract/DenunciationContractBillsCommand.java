package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

public class DenunciationContractBillsCommand {
	
	private Integer namespaceId;
	private Byte costGenerationMethod;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	
	public Byte getCostGenerationMethod() {
		return costGenerationMethod;
	}

	public void setCostGenerationMethod(Byte costGenerationMethod) {
		this.costGenerationMethod = costGenerationMethod;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
