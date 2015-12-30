package com.everhomes.rest.organization.pm;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>billDate : 指定的月份</li>
 * <li>nextPageOffset : 下一页码</li>
 * <li>requests : 账单和缴费记录列表,详见 {@link com.everhomes.rest.organization.pm.PmBillsDTO}</li>
 * 
 * <li>orgTelephone : 组织电话</li>
 * <li>orgName : 组织名称</li>
 * <li>zlTelephone : 左邻电话</li>
 * <li>zlName : 左邻名称</li>
 * <li>orgIsExist : 物业是否入驻小区,0-否,1-是</li>
 *</ul>
 *
 */

public class ListFamilyBillsAndPaysByFamilyIdCommandResponse {
	
	private String billDate;

	private Long nextPageOffset;
	
	private String orgTelephone;
	private String orgName;
	private String zlTelephone;
	private String zlName;
	private Byte orgIsExist;
	
	@ItemType(PmBillsDTO.class)
	private List<PmBillsDTO> requests;
	
	public Byte getOrgIsExist() {
		return orgIsExist;
	}

	public void setOrgIsExist(Byte orgIsExist) {
		this.orgIsExist = orgIsExist;
	}

	public String getBillDate() {
		return billDate;
	}
	
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public Long getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Long nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}
	
	public String getOrgTelephone() {
		return orgTelephone;
	}

	public void setOrgTelephone(String orgTelephone) {
		this.orgTelephone = orgTelephone;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getZlTelephone() {
		return zlTelephone;
	}

	public void setZlTelephone(String zlTelephone) {
		this.zlTelephone = zlTelephone;
	}

	public String getZlName() {
		return zlName;
	}

	public void setZlName(String zlName) {
		this.zlName = zlName;
	}

	public List<PmBillsDTO> getRequests() {
		return requests;
	}

	public void setRequests(List<PmBillsDTO> requests) {
		this.requests = requests;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	

}
