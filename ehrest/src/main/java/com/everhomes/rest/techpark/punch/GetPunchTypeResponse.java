package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;


/**
* <ul>  
* <li>punchType：0上班 1下班 2不打卡 参考 {@link PunchType}</li> 
* </ul>
*/
public class GetPunchTypeResponse {

	private Byte punchType;
	
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Byte getPunchType() {
		return punchType;
	}

	public void setPunchType(Byte punchType) {
		this.punchType = punchType;
	}
}
