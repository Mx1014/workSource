package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
* <ul>  
* <li>code：</li>
* </ul>
*/
public class GetPunchNewExceptionCommandResponse {
	private byte exceptionCode ; 
	
	

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }



	public byte getExceptionCode() {
		return exceptionCode;
	}



	public void setExceptionCode(byte exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

  
}
