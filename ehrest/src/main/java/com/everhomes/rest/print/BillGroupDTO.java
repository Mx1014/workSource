
package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>billGroupToken : 帐单组的token</li>
 * <li>billGroupName : 帐单组的名称</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年9月20日
 */
public class BillGroupDTO {
	private Long billGroupToken;
	private String billGroupName;
	
    @Override
    public String toString() {
    	return StringHelper.toJsonString(this);
    }

	public Long getBillGroupToken() {
		return billGroupToken;
	}

	public void setBillGroupToken(Long billGroupToken) {
		this.billGroupToken = billGroupToken;
	}

	public String getBillGroupName() {
		return billGroupName;
	}

	public void setBillGroupName(String billGroupName) {
		this.billGroupName = billGroupName;
	}

}

