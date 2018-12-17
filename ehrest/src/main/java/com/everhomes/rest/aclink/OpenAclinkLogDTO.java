//@formatter: off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;
import java.sql.Timestamp;


/**
 * <ul>for openApi
 *     <li>userName:用户姓名</li>
 *     <li>userIdentifier:手机号码</li>
 *     <li>doorName:门禁名称</li>
 *     <li>authId:授权id</li>
 *     <li>eventType:开门方式 {@link com.everhomes.rest.aclink.AclinkLogEventType}</li>
 *     <li>logTime:开门时间 </li>
 *     <li>macAddress:mac地址 </li>
 *     <li>createTime:日志入库时间</li>
 *     <li>doorId:门禁id</li>
 *     <li>userId:用户id </li>
 * </ul>
 */
public class OpenAclinkLogDTO {
    private Long     eventType;
    private Long     logTime;
    private Long     id;
    private String     doorName;
    private String     macAddress;
    private Timestamp     createTime;
    private String     userName;
    private Long     authId;
    private String     userIdentifier;
    //TODO 确认是否要传:
    private Long     doorId;
    private Long     userId;
    
    public Long getEventType() {
		return eventType;
	}
	public void setEventType(Long eventType) {
		this.eventType = eventType;
	}
	public Long getLogTime() {
		return logTime;
	}
	public void setLogTime(Long logTime) {
		this.logTime = logTime;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDoorName() {
		return doorName;
	}
	public void setDoorName(String doorName) {
		this.doorName = doorName;
	}
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Long getDoorId() {
		return doorId;
	}
	public void setDoorId(Long doorId) {
		this.doorId = doorId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getAuthId() {
		return authId;
	}
	public void setAuthId(Long authId) {
		this.authId = authId;
	}
	public String getUserIdentifier() {
		return userIdentifier;
	}
	public void setUserIdentifier(String userIdentifier) {
		this.userIdentifier = userIdentifier;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
