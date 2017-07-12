package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
* <ul>  
* <li>isAdminFlag：0-not admin 1-is admin {@link com.everhomes.rest.techpark.punch.NormalFlag}</li>
* </ul>
*/
public class CheckPunchAdminResponse {
	private byte isAdminFlag; 
	
	

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }



	public byte getIsAdminFlag() {
		return isAdminFlag;
	}



	public void setIsAdminFlag(byte isAdminFlag) {
		this.isAdminFlag = isAdminFlag;
	}
 

  
}
