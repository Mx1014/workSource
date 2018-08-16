package com.everhomes.rest.customer;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>ownerId: organizationId</li>
 *     <li>oenerType: 类型</li>
 *     <li>keyword: 关键字：手机号或地址或客户名称</li>
 *     <li>trackingName:关键字：手跟进人名称 app用</li>
 *     <li>trackingUids: 跟进人id列表</li>
 *     <li>customerCategoryId: 客户类型id</li>
 *     <li>corpIndustryItemId: 行业类别id</li>
 *     <li>levelId: 客户级别id,如果多选用英文逗号分隔,eg: 1,2</li>
 *     <li>communityId: 园区id</li>
 *     <li>pageAnchor: 锚点</li>
 *     <li>pageSize: 页面大小</li>
 *     <li>sortType: 排序类型：0 升序， 1 降序</li>
 *     <li>sortField: 排序字段名</li>
 		<li>trackingUid: 跟进人uid</li>
 *     <li>type: 查询类型;1:全部客户  2:我的客户   3:公共客户 现在换成 1：无  2：有跟进人  3：无跟进人</li>
 *     <li>lastTrackingTime: 最近跟进时间（天）</li>
 *     <li>propertyType: 资产类型   String类型,如果多选用英文逗号分隔,eg: 1,2</li>
 *     <li>propertyUnitPrice: 资产单价区间  String类型,eg: 0,10  或者    @,10   或者   0,@ </li>
 *     <li>propertyArea: 资产面积区间  String类型,eg: 0,10 或者  @,10  或者   0,@ </li>
 *     <li>aptitudeFlag : 是否筛选资质客户</li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public class SearchEnterpriseCustomerCommand {

    private Long ownerId;

    private String ownerType;

    private String keyword;

    private String trackingName;

    private Long customerCategoryId;

    private String levelId;

    private Long corpIndustryItemId;

    private Long communityId;

    private Long pageAnchor;

    private Integer pageSize;
    
    private Long trackingUid;
    
    private Integer type;
    
    private Integer lastTrackingTime;
    
    private String propertyType;
    
    private String propertyUnitPrice;
    
    private String propertyArea;

    private Integer sortType;

    private String sortField;

    private Integer namespaceId;

    private Long orgId;

    @ItemType(Long.class)
    private List<Long> trackingUids;

    private Byte adminFlag;

    private Long buildingId;

    private Long addressId;

    private Long sourceItemId;

    private String sourceType;

    private Byte aptitudeFlagItemId;

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public Integer getSortType() {
        return sortType;
    }

    public void setSortType(Integer sortType) {
        this.sortType = sortType;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getCustomerCategoryId() {
        return customerCategoryId;
    }

    public void setCustomerCategoryId(Long customerCategoryId) {
        this.customerCategoryId = customerCategoryId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getCorpIndustryItemId() {
        return corpIndustryItemId;
    }

    public void setCorpIndustryItemId(Long corpIndustryItemId) {
        this.corpIndustryItemId = corpIndustryItemId;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
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
    
    
    public Long getTrackingUid() {
		return trackingUid;
	}

	public void setTrackingUid(Long trackingUid) {
		this.trackingUid = trackingUid;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	

	public Integer getLastTrackingTime() {
		return lastTrackingTime;
	}

	public void setLastTrackingTime(Integer lastTrackingTime) {
		this.lastTrackingTime = lastTrackingTime;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public String getPropertyUnitPrice() {
		return propertyUnitPrice;
	}

	public void setPropertyUnitPrice(String propertyUnitPrice) {
		this.propertyUnitPrice = propertyUnitPrice;
	}

	public String getPropertyArea() {
		return propertyArea;
	}

	public void setPropertyArea(String propertyArea) {
		this.propertyArea = propertyArea;
	}

    public List<Long> getTrackingUids() {
        return trackingUids;
    }

    public void setTrackingUids(List<Long> trackingUids) {
        this.trackingUids = trackingUids;
    }

    public String getTrackingName() {
        return trackingName;
    }

    public void setTrackingName(String trackingName) {
        this.trackingName = trackingName;
    }

    public Byte getAdminFlag() {
        return adminFlag;
    }

    public void setAdminFlag(Byte adminFlag) {
        this.adminFlag = adminFlag;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getSourceItemId() {
        return sourceItemId;
    }

    public void setSourceItemId(Long sourceItemId) {
        this.sourceItemId = sourceItemId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Byte getAptitudeFlagItemId() {
        return aptitudeFlagItemId;
    }

    public void setAptitudeFlagItemId(Byte aptitudeFlagItemId) {
        this.aptitudeFlagItemId = aptitudeFlagItemId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
