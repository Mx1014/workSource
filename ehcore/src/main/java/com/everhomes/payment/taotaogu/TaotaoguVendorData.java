package com.everhomes.payment.taotaogu;

import com.everhomes.util.StringHelper;
/**
 * 淘淘谷厂商初始数据
 */
public class TaotaoguVendorData {
	private String branchCode;
	private String appName;
	private String version;
	private String dstId;
	private String cardPatternid;
	private String chnlType;
	private String chnlId;
	private String merchId;
	private String termnlId;
	private String initPassword;
	private String token;
	//渠道流水号,保证递增 且唯一
	private Long chnlSn;
	
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDstId() {
		return dstId;
	}
	public void setDstId(String dstId) {
		this.dstId = dstId;
	}
	public String getCardPatternid() {
		return cardPatternid;
	}
	public void setCardPatternid(String cardPatternid) {
		this.cardPatternid = cardPatternid;
	}
	public String getChnlType() {
		return chnlType;
	}
	public void setChnlType(String chnlType) {
		this.chnlType = chnlType;
	}
	public String getChnlId() {
		return chnlId;
	}
	public void setChnlId(String chnlId) {
		this.chnlId = chnlId;
	}
	public String getMerchId() {
		return merchId;
	}
	public void setMerchId(String merchId) {
		this.merchId = merchId;
	}
	public String getTermnlId() {
		return termnlId;
	}
	public void setTermnlId(String termnlId) {
		this.termnlId = termnlId;
	}
	public String getInitPassword() {
		return initPassword;
	}
	public void setInitPassword(String initPassword) {
		this.initPassword = initPassword;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Long getChnlSn() {
		return chnlSn;
	}
	public void setChnlSn(Long chnlSn) {
		this.chnlSn = chnlSn;
	}
}
