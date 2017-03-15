package com.everhomes.rest.techpark.expansion;


import java.math.BigDecimal;

/**
 * <ul>
 * <li>pageAnchor：锚点</li>
 * <li>pageSize：每页数量</li>
 * <li>namespaceId：命名空间 </li>
 * <li>communityId：小区id</li>
 * <li>buildingId：建筑id</li>
 * <li>rentType：招租类型{@link com.everhomes.rest.techpark.expansion.LeasePromotionType}</li> 
 * <li>status：  参考{@link com.everhomes.rest.techpark.expansion.LeasePromotionStatus}}</li> 
 * </ul>
 */
public class ListBuildingForRentCommand {
    private Long pageAnchor;
    
    private Integer pageSize;
   
    private Integer namespaceId;
    
    private Long communityId;
    
    private Byte status;
    
    private Long buildingId;
    
    private String rentType;

	private BigDecimal startRentArea;
	private BigDecimal endRentArea;
	private BigDecimal startRentAmount;
	private BigDecimal endRentAmount;
	private Long userId;
	private String issuerType;

    public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public Long getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
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
	public String getRentType() {
		return rentType;
	}
	public void setRentType(String rentType) {
		this.rentType = rentType;
	}
    
}
