// @formatter:off
package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul> 添加访客授权。
 * <li>phone: 电话</li>
 * <li>doorId: 门禁ID</li>
 * <li>namespaceId: 域空间ID</li>
 * <li>userName: 访客姓名</li>
 * <li>organization: 公司名称</li>
 * <li>description: 来访说明</li>
 * <li>authMethod: 授权方式{@link com.everhomes.aclink.DoorAuthMethodType}</li>
 * <li>authRuleType: 授权规则种类，0 时间，1 次数</li>
 * <li>validFromMs: 有效期开始时间</li>
 * <li>validEndMs: 有效期终止时间</li>
 * <li>totalAuthAmount: 有效开门次数</li>
 * </ul>
 */
public class CreateZLVisitorQRKeyCommand {
    private String phone;
    
    @NotNull
    private String MacAddress;
    
    private String userName;
    private String visitorEvent;
    private String organization;
    private String description;
    private Long doorNumber;
    private String authMethod;
	private Long validFromMs;
	private Long validEndMs;
	private Byte authRuleType;
	private Integer totalAuthAmount;
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMacAddress() {
		return MacAddress;
	}
	public void setMacAddress(String macAddress) {
		MacAddress = macAddress;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getVisitorEvent() {
		return visitorEvent;
	}
	public void setVisitorEvent(String visitorEvent) {
		this.visitorEvent = visitorEvent;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getDoorNumber() {
		return doorNumber;
	}
	public void setDoorNumber(Long doorNumber) {
		this.doorNumber = doorNumber;
	}
	public String getAuthMethod() {
		return authMethod;
	}
	public void setAuthMethod(String authMethod) {
		this.authMethod = authMethod;
	}
	public Long getValidFromMs() {
		return validFromMs;
	}
	public void setValidFromMs(Long validFromMs) {
		this.validFromMs = validFromMs;
	}
	public Long getValidEndMs() {
		return validEndMs;
	}
	public void setValidEndMs(Long validEndMs) {
		this.validEndMs = validEndMs;
	}
	public Byte getAuthRuleType() {
		return authRuleType;
	}
	public void setAuthRuleType(Byte authRuleType) {
		this.authRuleType = authRuleType;
	}
	public Integer getTotalAuthAmount() {
		return totalAuthAmount;
	}
	public void setTotalAuthAmount(Integer totalAuthAmount) {
		this.totalAuthAmount = totalAuthAmount;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
