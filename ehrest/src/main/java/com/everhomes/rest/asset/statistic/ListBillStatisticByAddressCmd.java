//@formatter:off
package com.everhomes.rest.asset.statistic;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>pageAnchor:锚点</li>
 * <li>pageSize:每页数量</li>
 * <li>namespaceId: 与空间</li>
 * <li>ownerType: 所属项目类型</li>
 * <li>ownerId: 所属项目ID</li>
 * <li>buildingName: 楼宇名称，如：1栋</li>
 * <li>apartmentNameList: 房源名称，如：101，102</li>
 * <li>chargingItemIdList: 收费项目ID列表</li>
 * <li>targetName: 客户名称</li>
 * <li>dateStrBegin: 账期范围</li>
 * <li>dateStrEnd: 账期范围</li>
 * <li>exportFileNamePrefix: 导出的文件名前缀</li>
 *</ul>
 */
public class ListBillStatisticByAddressCmd{
	private Integer pageSize;
    private Long pageAnchor;
	private Integer namespaceId;
	private String ownerType;
	private Long ownerId;
	private String buildingName;
	private List<String> apartmentNameList;
	private List<Long> chargingItemIdList;
	private String targetName;
	private String dateStrBegin;
	private String dateStrEnd;
	
	private String exportFileNamePrefix;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Long getPageAnchor() {
		return pageAnchor;
	}
	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
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
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public List<String> getApartmentNameList() {
		return apartmentNameList;
	}
	public void setApartmentNameList(List<String> apartmentNameList) {
		this.apartmentNameList = apartmentNameList;
	}
	public List<Long> getChargingItemIdList() {
		return chargingItemIdList;
	}
	public void setChargingItemIdList(List<Long> chargingItemIdList) {
		this.chargingItemIdList = chargingItemIdList;
	}
	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	public String getDateStrBegin() {
		return dateStrBegin;
	}
	public void setDateStrBegin(String dateStrBegin) {
		this.dateStrBegin = dateStrBegin;
	}
	public String getDateStrEnd() {
		return dateStrEnd;
	}
	public void setDateStrEnd(String dateStrEnd) {
		this.dateStrEnd = dateStrEnd;
	}
	public String getExportFileNamePrefix() {
		return exportFileNamePrefix;
	}
	public void setExportFileNamePrefix(String exportFileNamePrefix) {
		this.exportFileNamePrefix = exportFileNamePrefix;
	}
	
}
