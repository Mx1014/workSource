//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/9/28.
 */

/**
 * @author change by yangcx
 * @date 2018年5月19日----下午8:18:48
 */
/**
 *<ul>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 显示数量</li>
 * <li>dateStrBegin: 账期开始时间</li>
 * <li>dateStrEnd: 账期结束时间</li>
 * <li>startPayTime: 缴费开始时间</li>
 * <li>endPayTime: 缴费结束时间</li>
 * <li>paymentStatus:订单状态：1：已完成，0：订单异常</li>
 * <li>paymentType:支付方式，0:微信，1：支付宝，2：对公转账</li>
 * <li>addressId: 楼栋门牌id</li>
 * <li>buildingName:楼栋名称</li>
 * <li>apartmentName:门牌名称</li>
 * <li>userType:用户类型，如：EhOrganizations</li>
 * <li>userId:用户ID</li>
 * <li>namespaceId:域空间</li>
 * <li>communityId:园区ID</li>
 * <li>organizationId:客户id，客户类型为企业时，organizationId为企业id</li>
 * <li>orderType:账单类型，如：wuyeCode</li>
 * <li>targetName:客户名称</li>
 * <li>paymentOrderNum:订单编号，如：954650447962984448，订单编号为缴费中交易明细与电商系统中交易明细串联起来的唯一标识。</li>
 * <li>billId:账单id</li>
 *</ul>
*/
public class ListPaymentBillCmd {
    private Long pageAnchor;
    private Long pageSize;

    private String startPayTime;
    private String endPayTime;
    private Integer namespaceId;
    private Long communityId;
    private Long organizationId;
    private Long categoryId;

    private Long moduleId;//用于下载中心

    private List<Long> orderIds;

    public List<Long> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<Long> orderIds) {
        this.orderIds = orderIds;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    //交易时间
    private String payTime;
    //流水号？
    private String orderNo;
    //订单编号
    private String paymentOrderNum;
    //支付类型
    private Integer paymentType;
    //交易类型，如：手续费/充值/提现/退款等
    private Integer transactionType;
    @ItemType(Integer.class)
    private List<Integer> transactionTypes;
    private Integer paymentStatus;
    //结算状态：已结算/待结算
    private Integer settlementStatus;
    private Boolean distributionRemarkIsNull;
    //账单类型
    private String orderType;

    private String userType;

    private Long userId;
    @ItemType(ReSortCmd.class)
    private List<ReSortCmd> sorts;
    
    //客户名称
    private String targetName;
    //账单id
    private Long billId;
    private String dateStrBegin;
    private String dateStrEnd;
    private Long addressId;
    private String buildingName;
    private String apartmentName;
    private Long targetId;
    private Long ownerId;

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public String getStartPayTime() {
        return startPayTime;
    }

    public void setStartPayTime(String startPayTime) {
        this.startPayTime = startPayTime;
    }

    public String getEndPayTime() {
        return endPayTime;
    }

    public void setEndPayTime(String endPayTime) {
        this.endPayTime = endPayTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public String getPaymentOrderNum() {
        return paymentOrderNum;
    }
    public void setPaymentOrderNum(String paymentOrderNum) {
        this.paymentOrderNum = paymentOrderNum;
    }
    public Integer getPaymentType() {
        return paymentType;
    }
    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }
    public Integer getTransactionType() {
        return transactionType;
    }
    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }
    public List<Integer> getTransactionTypes() {
        return transactionTypes;
    }
    public void setTransactionTypes(List<Integer> transactionTypes) {
        this.transactionTypes = transactionTypes;
    }
    public List<ReSortCmd> getSorts() {
        return sorts;
    }
    public void setSorts(List<ReSortCmd> sorts) {
        this.sorts = sorts;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Integer getPaymentStatus() {
        return paymentStatus;
    }
    public void setPaymentStatus(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    public Integer getSettlementStatus() {
        return settlementStatus;
    }
    public void setSettlementStatus(Integer settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Boolean getDistributionRemarkIsNull() {
        return distributionRemarkIsNull;
    }
    public void setDistributionRemarkIsNull(Boolean distributionRemarkIsNull) {
        this.distributionRemarkIsNull = distributionRemarkIsNull;
    }
    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
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

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getApartmentName() {
		return apartmentName;
	}

	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
