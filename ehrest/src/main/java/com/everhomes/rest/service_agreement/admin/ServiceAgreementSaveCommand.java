package com.everhomes.rest.service_agreement.admin;

import com.everhomes.util.StringHelper;

/**
 * create by huanglm 20180709 
 * <ul> 
 * <li>namespaceId: 域空间</li>  
 * <li>agreementContent: 协议内容</li>  
 * </ul>
 */
public class ServiceAgreementSaveCommand { 
    private Integer namespaceId;
    private String agreementContent;


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
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
