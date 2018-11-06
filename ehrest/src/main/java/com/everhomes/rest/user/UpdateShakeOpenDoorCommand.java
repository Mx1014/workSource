package com.everhomes.rest.user;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * <li></li>
 * <li>shakeOpenDoor: 开启参数0或1</li>
 * </ul>
 */
public class UpdateShakeOpenDoorCommand {
	
	@NotNull
	private Byte shakeOpenDoor;
	
	private String hardwareId;
 
	

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }



	public String getHardwareId() {
		return hardwareId;
	}



	public void setHardwareId(String hardwareId) {
		this.hardwareId = hardwareId;
	}



	public Byte getShakeOpenDoor() {
		return shakeOpenDoor;
	}



	public void setShakeOpenDoor(Byte shakeOpenDoor) {
		this.shakeOpenDoor = shakeOpenDoor;
	}

}
