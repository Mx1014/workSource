package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
* <ul>  
* <li>code：</li>
* </ul>
*/
public class PunchClockResponse {
	private byte punchCode ;
	private String punchTime;
	
	

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


 


	public String getPunchTime() {
		return punchTime;
	}



	public void setPunchTime(String punchTime) {
		this.punchTime = punchTime;
	}





	public byte getPunchCode() {
		return punchCode;
	}





	public void setPunchCode(byte punchCode) {
		this.punchCode = punchCode;
	}
}
