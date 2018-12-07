// @formatter:off
package com.everhomes.rest.aclink;

import java.util.HashMap;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class GetUserKeyInfoResponse {
	private DoorAccessQRKeyDTO qrInfo;
	private Long authId;
	private Byte isSupportQR;
	private Byte isSupportRemote;
	private Byte isSupportTempAuth;
	private Byte isAuthByCount;
	private Byte isSupportFaceOpen;
	private Integer maxAuthDay;
	private Integer maxAuthCount;
	private Integer openRemainCount;
	private Integer openVailadTime;
	private String hardwareId;
	private String macCopy;
	private String blueToothSecret;
	@ItemType(AclinkKeyExtraActionsDTO.class)
	private List<AclinkKeyExtraActionsDTO> extraActions;

	private HashMap<String, String> authInfo;
	
	public Long getAuthId() {
		return authId;
	}
	public void setAuthId(Long authId) {
		this.authId = authId;
	}
	public DoorAccessQRKeyDTO getQrInfo() {
		return qrInfo;
	}
	public void setQrInfo(DoorAccessQRKeyDTO qrInfo) {
		this.qrInfo = qrInfo;
	}
	public Byte getIsSupportQR() {
		return isSupportQR;
	}
	public void setIsSupportQR(Byte isSupportQR) {
		this.isSupportQR = isSupportQR;
	}
	public Byte getIsSupportRemote() {
		return isSupportRemote;
	}
	public void setIsSupportRemote(Byte isSupportRemote) {
		this.isSupportRemote = isSupportRemote;
	}
	public Byte getIsSupportTempAuth() {
		return isSupportTempAuth;
	}
	public void setIsSupportTempAuth(Byte isSupportTempAuth) {
		this.isSupportTempAuth = isSupportTempAuth;
	}
	public Byte getIsAuthByCount() {
		return isAuthByCount;
	}
	public void setIsAuthByCount(Byte isAuthByCount) {
		this.isAuthByCount = isAuthByCount;
	}
	public Byte getIsSupportFaceOpen() {
		return isSupportFaceOpen;
	}
	public void setIsSupportFaceOpen(Byte isSupportFaceOpen) {
		this.isSupportFaceOpen = isSupportFaceOpen;
	}
	public Integer getMaxAuthDay() {
		return maxAuthDay;
	}
	public void setMaxAuthDay(Integer maxAuthDay) {
		this.maxAuthDay = maxAuthDay;
	}
	public Integer getMaxAuthCount() {
		return maxAuthCount;
	}
	public void setMaxAuthCount(Integer maxAuthCount) {
		this.maxAuthCount = maxAuthCount;
	}
	public Integer getOpenRemainCount() {
		return openRemainCount;
	}
	public void setOpenRemainCount(Integer openRemainCount) {
		this.openRemainCount = openRemainCount;
	}
	public Integer getOpenVailadTime() {
		return openVailadTime;
	}
	public void setOpenVailadTime(Integer openVailadTime) {
		this.openVailadTime = openVailadTime;
	}
	public String getHardwareId() {
		return hardwareId;
	}
	public void setHardwareId(String hardwareId) {
		this.hardwareId = hardwareId;
	}
	public String getMacCopy() {
		return macCopy;
	}
	public void setMacCopy(String macCopy) {
		this.macCopy = macCopy;
	}
	public String getBlueToothSecret() {
		return blueToothSecret;
	}
	public void setBlueToothSecret(String blueToothSecret) {
		this.blueToothSecret = blueToothSecret;
	}
	public List<AclinkKeyExtraActionsDTO> getExtraActions() {
		return extraActions;
	}
	public void setExtraActions(List<AclinkKeyExtraActionsDTO> extraActions) {
		this.extraActions = extraActions;
	}
	public HashMap<String, String> getAuthInfo() {
		return authInfo;
	}
	public void setAuthInfo(HashMap<String, String> authInfo) {
		this.authInfo = authInfo;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
