package com.everhomes.rest.service_agreement;

import com.everhomes.util.StringHelper;

/** 
 * <ul> 
 * <li>namespaceId: 域空间</li>  
 * </ul>
 */
public class ServiceAgreementCommand { 
    private Integer namespaceId;


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
	 

    
}
