package com.everhomes.rest.videoconf;
/**
* <ul>  
* <li>trialFlag：0-reject  2-OK {@link com.everhomes.rest.videoconf.TrialFlag}</li>
* </ul>
*/
public class CheckVideoConfTrialAccountResponse {

	private byte trialFlag;

	public byte getTrialFlag() {
		return trialFlag;
	}

	public void setTrialFlag(byte trialFlag) {
		this.trialFlag = trialFlag;
	}
 
	
}
