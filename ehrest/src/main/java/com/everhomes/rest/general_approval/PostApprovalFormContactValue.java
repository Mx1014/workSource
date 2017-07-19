package com.everhomes.rest.general_approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>enterpriseName: 公司名 </li>
 * <li>enterpriseId: 公司id </li>
 * <li>address: 地址-楼栋门牌 </li>
 * <li>addressIds: 地址表ids</li>
 * <li>contactName: 联系人 </li>
 * <li>contactNumber: 联系方式 </li> 
 * </ul>
 * @author janson
 *
 */
public class PostApprovalFormContactValue {
	private String enterpriseName;
	private Long enterpriseId;
	private String address;
	@ItemType(Long.class)
	private List<Long> addressIds;
	private String contactName;
	private String contactNumber;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

 

	public String getEnterpriseName() {
		return enterpriseName;
	}


	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
 

	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public List<Long> getAddressIds() {
		return addressIds;
	}


	public void setAddressIds(List<Long> addressIds) {
		this.addressIds = addressIds;
	}


	public String getContactName() {
		return contactName;
	}


	public void setContactName(String contactName) {
		this.contactName = contactName;
	}


	public String getContactNumber() {
		return contactNumber;
	}


	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}


	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	} 
}
