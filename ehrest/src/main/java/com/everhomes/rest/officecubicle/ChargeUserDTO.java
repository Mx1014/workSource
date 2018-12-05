package com.everhomes.rest.officecubicle;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>chargeUid: 负责人ID</li>
 * <li>chargeName: 负责人姓名</li>
 * </ul>
 */
public class ChargeUserDTO {
    private Long chargeUid;
    private String chargeName;


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


	public Long getChargeUid() {
		return chargeUid;
	}





	public void setChargeUid(Long chargeUid) {
		this.chargeUid = chargeUid;
	}





	public String getChargeName() {
		return chargeName;
	}


	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}

    

}
