package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>
 * 	<li>namespaceId : 链接</li>
 * 	<li>namespaceUserToken : 第三方用户标识</li>
 * 	</ul>
 *
 */
public class GetBizSignatureCommand {
	private Integer namespaceId;
	private String namespaceUserToken;
	
	public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getNamespaceUserToken() {
        return namespaceUserToken;
    }

    public void setNamespaceUserToken(String namespaceUserToken) {
        this.namespaceUserToken = namespaceUserToken;
    }

    @Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	

}
