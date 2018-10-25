// @formatter:off
package com.everhomes.rest.aclink;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id:门禁id</li>
 * <li>createTime: 创建时间</li>
 * <li>doorType: 门禁类型{@link com.everhomes.rest.aclink.DoorAccessType}</li>
 * <li>name: 门禁名字</li>
 * <li>displayName: 用户端显示的门禁名字</li>
 * <li>farewareVersion[String]:固件版本</li>
 * </ul>
 * @author janson
 *
 */
public class DoorAccessLiteDTO {
	private Long id;
	private Timestamp createTime;
	private Byte doorType;
	private String name;
	private String displayName;
	private Integer defualtInvalidDuration;
	private String farewareVersion;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Byte getDoorType() {
		return doorType;
	}
	public void setDoorType(Byte doorType) {
		this.doorType = doorType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public Integer getDefualtInvalidDuration() {
		return defualtInvalidDuration;
	}
	public void setDefualtInvalidDuration(Integer defualtInvalidDuration) {
		this.defualtInvalidDuration = defualtInvalidDuration;
	}
	public String getFarewareVersion() {
		return farewareVersion;
	}
	public void setFarewareVersion(String farewareVersion) {
		this.farewareVersion = farewareVersion;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
