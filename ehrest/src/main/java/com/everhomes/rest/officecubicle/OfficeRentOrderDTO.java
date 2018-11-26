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
*<li> provinceName : 省份名称	</li> 
*<li> cityName : 城市名称	</li>
*<li> address : 地址	</li>
*<li> longitude : 经度	</li>
*<li>latitude  : 纬度	</li>
*<li>contactPhone  : 咨询电话	</li>  
*<li> description : 详情-html片	</li> 
*<li> coverUrl : 封面图片url</li>   
 * <li>rentType: 租赁类别:1-开放式（默认space_type 1）,2-办公室</li>  
 * <li>spaceType: 空间类别:1-工位,2-面积 </li>  
 * <li>spaceSize: 场所大小 - 对于工位是个数，对于面积是平米</li> 
 * <li>status: 状态 2-客户端可见  0-客户端不可见</li> 
 * <li>orderType: 预定类别：1-参观 2-预定 </li> 
 * <li>reserveTime: 预定时间 </li> 
 * <li>reservePerson: 预订人姓名 </li> 
 * <li>reserveContact:  预订人联系方式</li> 
 * <li>reservceCompany: 预订人公司</li> 
 * <li>flowCaseId: 工作流id</li> 
 * <li>workFlowStatus: 工作流状态， {@link com.everhomes.rest.officecubicle.OfficeOrderWorkFlowStatus}</li>
 * </ul>
 */
public class OfficeRentOrderDTO {
    private Long id;
    private Long namespaceId;
    private Long spaceId;
    private String spaceName; 
    private String description;
    private Byte rentType;
    private Byte spaceType;
    private String spaceSize;
    private Byte status;
    private Byte orderType; 
    private Long reserveTime;
    private String reserverName;
    private String reserveContactToken;
    private String reserveEnterprise;


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

	public Byte getSpaceType() {
		return spaceType;
	}

	public void setSpaceType(Byte spaceType) {
		this.spaceType = spaceType;
	}

	public String getSpaceSize() {
		return spaceSize;
	}

	public void setSpaceSize(String spaceSize) {
		this.spaceSize = spaceSize;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Byte getOrderType() {
		return orderType;
	}

	public void setOrderType(Byte orderType) {
		this.orderType = orderType;
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

	public String getReserveEnterprise() {
		return reserveEnterprise;
	}

	public void setReserveEnterprise(String reserveEnterprise) {
		this.reserveEnterprise = reserveEnterprise;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
}
