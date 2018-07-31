// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：设备id</li>
 * <li>name：设备名称</li>
 * <li>doorAccessId：关联门禁id</li>
 * <li>enterStatus：进出标识0出1进</li>
 * </ul>
 */
public class UpdateLocalIpadCommand {
	private Long id;
	private String name;
	private Long doorAccessId;
	private Byte enterStatus;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getDoorAccessId() {
		return doorAccessId;
	}
	public void setDoorAccessId(Long doorAccessId) {
		this.doorAccessId = doorAccessId;
	}
	public Byte getEnterStatus() {
		return enterStatus;
	}
	public void setEnterStatus(Byte enterStatus) {
		this.enterStatus = enterStatus;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
