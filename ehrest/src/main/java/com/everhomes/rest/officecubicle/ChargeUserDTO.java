package com.everhomes.rest.officecubicle;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>chargeUId: 负责人ID</li>
 * <li>chargeName: 负责人姓名</li>
 * </ul>
 */
public class ChargeUserDTO {
    private Long chargeUId;
    private String chargeName;


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


	public Long getChargeUId() {
		return chargeUId;
	}


	public void setChargeUId(Long chargeUId) {
		this.chargeUId = chargeUId;
	}


	public String getChargeName() {
		return chargeName;
	}


	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}

    

}
