package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>keyword: 关键字：手机号或地址或客户名称</li>
 *     <li>customerCategoryId: 客户类型id</li>
 *     <li>levelId: 客户级别id</li>
 *     <li>communityId: 园区id</li>
 *     <li>pageAnchor: 锚点</li>
 *     <li>pageSize: 页面大小</li>
 *     <li>trackingUid: 跟进人uid</li>
 *     <li>type: 查询类型;1:全部客户  2:我的客户   3:公共客户</li>
 *     
 *     <li>lastTrackingTime: 最近跟进时间（天）</li>
 *     <li>propertyType: 资产类型   String类型,如果多选用英文逗号分隔,eg: 1,2</li>
 *     <li>propertyUnitPrice: 资产单价区间  String类型,eg: 0~10 </li>
 *     <li>propertyArea: 资产面积区间  String类型,eg: 0~10 </li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public class SearchEnterpriseCustomerCommand {

    private String keyword;

    private Long customerCategoryId;

    private Long levelId;

    private Long communityId;

    private Long pageAnchor;

    private Integer pageSize;
    
    private Long trackingUid;
    
    private Integer type;
    
    private Integer lastTrackingTime;
    
    private String propertyType;
    
    private String propertyUnitPrice;
    
    private String propertyArea;

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

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
