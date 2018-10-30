package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;


/**

 *<ul>
 *<li>infoUrl：商户跳转URL</li>
 * </ul>
 */
public class GetPrintMerchantUrlResponse {
	private String infoUrl;

	public String getInfoUrl() {
		return infoUrl;
	}


	public void setInfoUrl(String infoUrl) {
		this.infoUrl = infoUrl;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
