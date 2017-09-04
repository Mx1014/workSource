package com.everhomes.rest.techpark.expansion;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
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
 * <li>startRentArea：搜索开始面积</li>
 * <li>endRentArea：搜索结束面积</li>
 * <li>startRentAmount：开始金额</li>
 * <li>endRentAmount：结束金额</li>
 * <li>userId：查询业主发布的招租</li>
 * <li>organizationId：公司id</li>
 * <li>issuerType：发布人类型  {@link com.everhomes.rest.techpark.expansion.LeaseIssuerType  NORMAL_USER：普通用户或公司，ORGANIZATION：物业公司}</li>
 * </ul>
 */
public class ListBuildingForRentCommand {
    private Long pageAnchor;
    
    private Integer pageSize;
   
    private Integer namespaceId;
    
    private Long communityId;
    
    private Byte status;
	@NotNull
    private Long buildingId;

    private String rentType;

	private BigDecimal startRentArea;
	private BigDecimal endRentArea;
	private BigDecimal startRentAmount;
	private BigDecimal endRentAmount;
	private Long userId;
	private Long organizationId;
	private String issuerType;

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public BigDecimal getStartRentArea() {
		return startRentArea;
	}

	public void setStartRentArea(BigDecimal startRentArea) {
		this.startRentArea = startRentArea;
	}

	public BigDecimal getEndRentArea() {
		return endRentArea;
	}

	public void setEndRentArea(BigDecimal endRentArea) {
		this.endRentArea = endRentArea;
	}

	public BigDecimal getStartRentAmount() {
		return startRentAmount;
	}

	public void setStartRentAmount(BigDecimal startRentAmount) {
		this.startRentAmount = startRentAmount;
	}

	public BigDecimal getEndRentAmount() {
		return endRentAmount;
	}

	public void setEndRentAmount(BigDecimal endRentAmount) {
		this.endRentAmount = endRentAmount;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getIssuerType() {
		return issuerType;
	}

	public void setIssuerType(String issuerType) {
		this.issuerType = issuerType;
	}

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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
