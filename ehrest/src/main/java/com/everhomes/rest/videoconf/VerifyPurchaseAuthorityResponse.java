package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>purchaseAuthority: 是否具有购买权限 </li>
 *  <li>enterpriseActiveAccountCount: 企业有效账号数</li>
 *  <li>enterpriseAccountCount: 企业账号数</li>
 * </ul>
 *
 */
public class VerifyPurchaseAuthorityResponse {
	
	private boolean purchaseAuthority;
	
	private int enterpriseActiveAccountCount;
	
	private int enterpriseAccountCount;

	public boolean isPurchaseAuthority() {
		return purchaseAuthority;
	}

	public void setPurchaseAuthority(boolean purchaseAuthority) {
		this.purchaseAuthority = purchaseAuthority;
	}

	public int getEnterpriseActiveAccountCount() {
		return enterpriseActiveAccountCount;
	}

	public void setEnterpriseActiveAccountCount(int enterpriseActiveAccountCount) {
		this.enterpriseActiveAccountCount = enterpriseActiveAccountCount;
	}

	public int getEnterpriseAccountCount() {
		return enterpriseAccountCount;
	}

	public void setEnterpriseAccountCount(int enterpriseAccountCount) {
		this.enterpriseAccountCount = enterpriseAccountCount;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
