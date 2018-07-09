package com.everhomes.rest.service_agreement;


/**
 * <ul>
 * <li>id: 主键</li>
 * <li>namespaceId: 域空间ID</li>
 * <li>agreementContent: 协议内容</li>
 * </ul>
 */
public class ServiceAgreementDTO {
	private Long id;
	private Integer namespaceId;
	private String agreementContent;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public String getAgreementContent() {
		return agreementContent;
	}
	public void setAgreementContent(String agreementContent) {
		this.agreementContent = agreementContent;
	}
	

}
