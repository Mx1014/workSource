// @formatter:off
package com.everhomes.rest.socialSecurity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属类型 organization</li>
 * <li>ownerId: 所属id 公司id</li>
 * <li>socialSecurityCityId: 社保城市id</li>
 * <li>accumulationFundCityId: 公积金城市id</li>
 * <li>deptId: 部门id</li>
 * <li>keywords: 查询关键词-员工姓名</li>
 * <li>filterItems: 筛选项-多选</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListSocialSecurityPaymentsCommand {

	private String ownerType;

	private Long ownerId;

	private Long socialSecurityCityId;

	private Long accumulationFundCityId;

	private Long deptId;

	private String keywords;

	@ItemType(Byte.class)
	private List<Byte> filterItems;

	private Long pageAnchor;

	private Integer pageSize;

	public ListSocialSecurityPaymentsCommand() {

	}

	public ListSocialSecurityPaymentsCommand(String ownerType, Long ownerId, Long socialSecurityCityId, Long accumulationFundCityId, Long deptId, String keywords, List<Byte> filterItems, Long pageAnchor, Integer pageSize) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.socialSecurityCityId = socialSecurityCityId;
		this.accumulationFundCityId = accumulationFundCityId;
		this.deptId = deptId;
		this.keywords = keywords;
		this.filterItems = filterItems;
		this.pageAnchor = pageAnchor;
		this.pageSize = pageSize;
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

	public Long getSocialSecurityCityId() {
		return socialSecurityCityId;
	}

	public void setSocialSecurityCityId(Long socialSecurityCityId) {
		this.socialSecurityCityId = socialSecurityCityId;
	}

	public Long getAccumulationFundCityId() {
		return accumulationFundCityId;
	}

	public void setAccumulationFundCityId(Long accumulationFundCityId) {
		this.accumulationFundCityId = accumulationFundCityId;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public List<Byte> getFilterItems() {
		return filterItems;
	}

	public void setFilterItems(List<Byte> filterItems) {
		this.filterItems = filterItems;
	}

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
