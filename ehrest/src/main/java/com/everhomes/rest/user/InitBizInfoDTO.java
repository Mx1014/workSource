package com.everhomes.rest.user;



/**
 * <ul>
 * 	<li>label：第三方用户唯一标识</li>
 * 	<li>detail：用户名</li>
 * 	<li>mark：手机号</li>
 * 	<li>description：用户头像</li>
 * 	<li>namespaceId：命名空间</li>
 * 	<li>subNonce：随机数</li>
 * 	<li>subTimestamp：时间戳</li>
 * 	<li>subKey：公钥</li>
 * 	<li>subSign：签名</li>
 * </ul>
 */
public class InitBizInfoDTO {
	private String label;
	private String detail;
	private String mark;
	private String description;
	private Integer namespaceId;
	private Integer subNonce;
	private Long subTimestamp;
	private String subSign;
	private String subKey;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public Integer getSubNonce() {
		return subNonce;
	}
	public void setSubNonce(Integer subNonce) {
		this.subNonce = subNonce;
	}
	public Long getSubTimestamp() {
		return subTimestamp;
	}
	public void setSubTimestamp(Long subTimestamp) {
		this.subTimestamp = subTimestamp;
	}
	public String getSubSign() {
		return subSign;
	}
	public void setSubSign(String subSign) {
		this.subSign = subSign;
	}
	public String getSubKey() {
		return subKey;
	}
	public void setSubKey(String subKey) {
		this.subKey = subKey;
	}

}
