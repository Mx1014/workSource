//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 * <li>appShowPay: 2:全部缴，1：部分缴，0：单个缴</li>
 *</ul>
 */
public class JudgeAppShowPayResponse {
    private Byte appShowPay;

	public Byte getAppShowPay() {
		return appShowPay;
	}

	public void setAppShowPay(Byte appShowPay) {
		this.appShowPay = appShowPay;
	}
    
}
