package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userName: 授权用户名字</li>
 * <li>doorName: 门禁名称 </li>
 * <li>phone: 手机号</li>
 * <li>createTime: 授权时间</li>
 * <li>description: 描述 </li>
 * <li>organization: 公司 </li>
 * <li>qr: 二维码数据 </li>
 * <li>isValid: 当前是否有效 </li>
 * <li>approveName: 授权人</li>
 * <li>validFromMs: 有效期开始时间</li>
 * <li>validEndMs: 有效期终止时间</li>
 * <li>validAuthAmount: 剩余有效开门次数</li>
 * <li>hotline: 服务热线</li>
 * </ul>
 * @author janson
 *
 */
public class GetVisitorResponse {
	Long id;
	Long doorId;
    String userName;
    String doorName;
    String phone;
    Long createTime;
    String qr;
    Long validDay;
    Byte isValid;
    String organization;
    String description;
    String approveName;
    Byte permissionDeny;
    Long validEndMs;
    String namespaceName;
    String qrIntro;
    private Long validFromMs;
    private Integer validAuthAmount;
    private String hotline;

    public String getHotline() {
        return hotline;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    public Long getValidFromMs() {
		return validFromMs;
	}
	public void setValidFromMs(Long validFromMs) {
		this.validFromMs = validFromMs;
	}
	public Integer getValidAuthAmount() {
		return validAuthAmount;
	}
	public void setValidAuthAmount(Integer validAuthAmount) {
		this.validAuthAmount = validAuthAmount;
	}
	public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getDoorName() {
        return doorName;
    }
    public void setDoorName(String doorName) {
        this.doorName = doorName;
    }
    public Long getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
    public String getQr() {
        return qr;
    }
    public void setQr(String qr) {
        this.qr = qr;
    }
    public Long getValidDay() {
        return validDay;
    }
    public void setValidDay(Long validDay) {
        this.validDay = validDay;
    }
    public Byte getIsValid() {
        return isValid;
    }
    public void setIsValid(Byte isValid) {
        this.isValid = isValid;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
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
    public String getApproveName() {
        return approveName;
    }
    public void setApproveName(String approveName) {
        this.approveName = approveName;
    }

    public Byte getPermissionDeny() {
        return permissionDeny;
    }
    public void setPermissionDeny(Byte permissionDeny) {
        this.permissionDeny = permissionDeny;
    }

    public Long getValidEndMs() {
		return validEndMs;
	}
	public void setValidEndMs(Long validEndMs) {
		this.validEndMs = validEndMs;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getDoorId() {
		return doorId;
	}
	public void setDoorId(Long doorId) {
		this.doorId = doorId;
	}
	public String getNamespaceName() {
        return namespaceName;
    }
    public void setNamespaceName(String namespaceName) {
        this.namespaceName = namespaceName;
    }

    public String getQrIntro() {
        return qrIntro;
    }
    public void setQrIntro(String qrIntro) {
        this.qrIntro = qrIntro;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
