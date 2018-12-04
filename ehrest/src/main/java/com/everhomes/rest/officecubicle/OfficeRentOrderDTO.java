package com.everhomes.rest.officecubicle;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *空间订单
 *<li> id: 订单id	</li>
 *<li>orderNo</li>
 *<li> namespaceId: namespace id	</li>
 *<li> spaceId: spaceId	</li>
 *<li> spaceName: 工位空间名称	</li>
 *<li> description : 详情	</li> 
 * <li>rentType: 1-长租,0-短租</li>  
 * <li>reserveTime: 预定时间 </li> 
 * <li>reservePerson: 预订人姓名 </li> 
 * <li>reserveContact:  预订人联系方式</li> 
 * <li>reserveEnterpriseId: 预订人公司</li> 
 * <li>reserveEnterpriseName: 预订人公司</li>
 * <li>userDetail:预定使用时间</li> 
 * <li>createTime:申请时间</li>
 * <li>price:价格</li>
 * <li>requestType：requestType:订单来源 {@link com.everhomes.rest.officecubicle.OfficeCubicleRequestType}</li>
 * <li>orderStatus：订单状态{@link com.everhomes.rest.officecubicle.OfficeCubiceOrderStatus}</li>
 * <li>paidType:支付方式,10001-支付宝，10002-微信 {@link com.everhomes.rest.organization.VendorType}</li>
 * <li>paidMode:支付类型 {@link com.everhomes.rest.general.order.GorderPayType}</li>
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
    private String reserverContactToken;
    private Long reserverEnterpriseId;
    private String reserverEnterpriseName;
    private String userDetail;
    private Long createTime;
    private BigDecimal price;
    private Byte paidMode;
    private Byte paidType;
    private Byte requestType;
    private Byte orderStatus;
    private Long orderNo;
    private String accountName;
	private String openTime;


	public String getOpenTime() {
		return openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}

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

	public String getReserverContactToken() {
		return reserverContactToken;
	}

	public void setReserverContactToken(String reserverContactToken) {
		this.reserverContactToken = reserverContactToken;
	}

	public Long getReserverEnterpriseId() {
		return reserverEnterpriseId;
	}

	public void setReserverEnterpriseId(Long reserverEnterpriseId) {
		this.reserverEnterpriseId = reserverEnterpriseId;
	}

	public String getReserverEnterpriseName() {
		return reserverEnterpriseName;
	}

	public void setReserverEnterpriseName(String reserverEnterpriseName) {
		this.reserverEnterpriseName = reserverEnterpriseName;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
}
