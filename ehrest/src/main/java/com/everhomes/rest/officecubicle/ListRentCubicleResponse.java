package com.everhomes.rest.officecubicle;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>longRentFlag</li>
 * <li>shortRentFlag </li>
 * </ul>
 */
public class ListRentCubicleResponse { 
	private Byte longRentFlag;
	private Byte shortRentFlag;


	public Byte getLongRentFlag() {
		return longRentFlag;
	}

	public void setLongRentFlag(Byte longRentFlag) {
		this.longRentFlag = longRentFlag;
	}


	public Byte getShortRentFlag() {
		return shortRentFlag;
	}

	public void setShortRentFlag(Byte shortRentFlag) {
		this.shortRentFlag = shortRentFlag;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


}
