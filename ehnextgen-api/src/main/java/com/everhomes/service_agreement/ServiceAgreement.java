package com.everhomes.service_agreement;

public class ServiceAgreement {

	private Long id;
	private Long namespaceId;
    private String agreementContent;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Long namespaceId) {
		this.namespaceId = namespaceId;
	}
	public String getAgreementContent() {
		return agreementContent;
	}
	public void setAgreementContent(String agreementContent) {
		this.agreementContent = agreementContent;
	}
    
}
