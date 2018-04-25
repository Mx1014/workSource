package com.everhomes.rest.salary;

import java.util.List;
import java.util.Map;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul> 
 * <li>payslipDetailId: id</li>
 * <li>userId: 用户id</li>
 * <li>userDetailId: 用户detailId</li>
 * <li>name: 用户姓名</li>
 * <li>userContact: 用户手机</li>
 * <li>payslipContent: 导入内容key-value对的list</li>
 * <li>viewedFlag: 已查看0-否 1-是</li>
 * <li>status: 状态0-已发送 1-已撤回  2-已确认</li>
 * </ul>
 */
public class PayslipDetailDTO {
	private Long payslipDetailId;
    private Long userId;
    private Long userDetailId;
    private String name;
    private String userContact;
    private List<Map<String,String>> payslipContent;
    private Byte viewedFlag;
    private Byte status;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getUserDetailId() {
		return userDetailId;
	}
	public void setUserDetailId(Long userDetailId) {
		this.userDetailId = userDetailId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserContact() {
		return userContact;
	}
	public void setUserContact(String userContact) {
		this.userContact = userContact;
	}
	public List<Map<String, String>> getPayslipContent() {
		return payslipContent;
	}
	public void setPayslipContent(List<Map<String, String>> payslipContent) {
		this.payslipContent = payslipContent;
	}
	public Byte getViewedFlag() {
		return viewedFlag;
	}
	public void setViewedFlag(Byte viewedFlag) {
		this.viewedFlag = viewedFlag;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	public Long getPayslipDetailId() {
		return payslipDetailId;
	}
	public void setPayslipDetailId(Long payslipDetailId) {
		this.payslipDetailId = payslipDetailId;
	}
    
    
}
