package com.everhomes.rest.user;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * <li>namespaceId: 域空间</li>
 * <li>statDate: 统计日</li>
 * </ul>
 */
public class UpdateShakeOpenDoorCommand {
	
	@NotNull
	private Byte shakeOpenDoor;
 
	 

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


	public Byte getShakeOpenDoor() {
		return shakeOpenDoor;
	}



	public void setShakeOpenDoor(Byte shakeOpenDoor) {
		this.shakeOpenDoor = shakeOpenDoor;
	}

}
