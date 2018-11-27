package com.everhomes.rest.officecubicle;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *空间订单
 *<li> id: 订单id	</li>
 *<li> namespaceId: namespace id	</li>
 *<li> spaceId: spaceId	</li>
 *<li> spaceName: 工位空间名称	</li>
*<li>contactPhone  : 咨询电话	</li>  
*<li> description : 详情-html片	</li> 
 * <li>rentType: 1-长租,2-短租</li>  
 * <li>status: 状态 2-客户端可见  0-客户端不可见</li> 
 * <li>orderType: 预定类别：1-参观 2-预定 </li> 
 * <li>reserveTime: 预定时间 </li> 
 * <li>reservePerson: 预订人姓名 </li> 
 * <li>reserveContact:  预订人联系方式</li> 
 * <li>reserveEnterpriseId: 预订人公司</li> 
 * <li>reserveEnterpriseName: 预订人公司</li> 
 * </ul>
 */
public class OfficeRentOrderDTO {
    private Long id;
    private Long namespaceId;
    private Long spaceId;
    private String spaceName; 
    private String description;
    private Byte rentType;
    private Long reserveTime;
    private String reserverName;
    private String reserveContactToken;
    private Long reserveEnterpriseId;
    private String reserveEnterpriseName;
    private String userDetail;
    private Long createTime;
    private BigDecimal price;
    private Byte paidMode;
    private Byte paidType;
    private Byte requestType;
    private Byte orderStatus;


	public Byte getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Byte orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Byte getRequestType() {
		return requestType;
	}

	public void setRequestType(Byte requestType) {
		this.requestType = requestType;
	}

	public Byte getPaidMode() {
		return paidMode;
	}

	public void setPaidMode(Byte paidMode) {
		this.paidMode = paidMode;
	}

	public Byte getPaidType() {
		return paidType;
	}

	public void setPaidType(Byte paidType) {
		this.paidType = paidType;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(String userDetail) {
		this.userDetail = userDetail;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
	public Long getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Long namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}

	public String getSpaceName() {
		return spaceName;
	}

	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Byte getRentType() {
		return rentType;
	}

	public void setRentType(Byte rentType) {
		this.rentType = rentType;
	}

	public Long getReserveTime() {
		return reserveTime;
	}

	public void setReserveTime(Long reserveTime) {
		this.reserveTime = reserveTime;
	}

	public String getReserverName() {
		return reserverName;
	}

	public void setReserverName(String reserverName) {
		this.reserverName = reserverName;
	}

	public String getReserveContactToken() {
		return reserveContactToken;
	}

	public void setReserveContactToken(String reserveContactToken) {
		this.reserveContactToken = reserveContactToken;
	}


	public Long getReserveEnterpriseId() {
		return reserveEnterpriseId;
	}

	public void setReserveEnterpriseId(Long reserveEnterpriseId) {
		this.reserveEnterpriseId = reserveEnterpriseId;
	}

	public String getReserveEnterpriseName() {
		return reserveEnterpriseName;
	}

	public void setReserveEnterpriseName(String reserveEnterpriseName) {
		this.reserveEnterpriseName = reserveEnterpriseName;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
}
