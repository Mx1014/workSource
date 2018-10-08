//@formatter:off
package com.everhomes.rest.asset;

import java.util.List;

/**
 * @author created by ycx
 * @date 下午9:31:37
 */

/**
 *<ul>
 * <li>billIdList:账单id列表</li>
 * <li>categoryId:多应用入口区分标识</li>
 * <li>ownerType:所属者类型</li>
 * <li>ownerId:所属者ID</li>
 * <li>billGroupId:账单组id</li>
 * <li>organizationId：当前登陆的企业ID</li>
 *</ul>
 */
public class BatchUpdateBillsToPaidCmd {
	private List<Long> billIdList;
	private Long categoryId;
    private String ownerType;
    private Long ownerId;
    private Long billGroupId;
    //物业缴费V6.0 新增权限
    private Long organizationId;
    
	public List<Long> getBillIdList() {
		return billIdList;
	}
	public void setBillIdList(List<Long> billIdList) {
		this.billIdList = billIdList;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public Long getBillGroupId() {
		return billGroupId;
	}
	public void setBillGroupId(Long billGroupId) {
		this.billGroupId = billGroupId;
	}
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
    
}
