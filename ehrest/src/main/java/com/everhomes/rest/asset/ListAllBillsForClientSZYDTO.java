//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by ChongXin Yang on 2018/04/08.
 */

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 * <li>number:合同编号</li>
 * <li>tenancyName:合同名称</li>
 * <li>tenCustomerDes:客户名称</li>
 * <li>tenancyType:合同类型</li>
 * <li>oldTenancyBill:原合同</li>
 * <li>startDate:开始日期</li>
 * <li>endDate:结束日期</li>
 * <li>tenancyDate:签约日期</li>
 * <li>leaseCount:租赁周期</li>
 * <li>roomTotalRent:租赁总面积</li>
 * <li>depositAmount:押金</li>
 * <li>freeDays:免租期</li>
 * <li>firstPayRent:首付总额</li>
 * <li>firstLeaseEndDate:首付截止日期</li>
 * <li>rentCountType:付款方式</li>
 * <li>tenancyState:合同状态</li>
 * <li>tenancyAdviser:经办人</li>
 * <li>description:备注</li>
 * <li>dealTotalRent:合同总额</li>
 * <li>quitRoomDate:退约日期</li>
 * <li>bills:账单列表，参考{@link BillForClientSZY}</li>
 *</ul>
 */
public class ListAllBillsForClientSZYDTO {
    private String number;
    private String tenancyName;
    private String tenCustomerDes;
    private String tenancyType;
    private String oldTenancyBill;
    private String startDate;
    private String endDate;
    private String tenancyDate;
    private String leaseCount;
    private String roomTotalRent;
    private String depositAmount;
    private String freeDays;
    private String firstPayRent;
    private String firstLeaseEndDate;
    private String rentCountType;
    private String tenancyState;
    private String tenancyAdviser;
    private String description;
    private String dealTotalRent;
    private String quitRoomDate;
    @ItemType(BillForClientSZY.class)
    List<BillForClientSZY> bills;

    public ListAllBillsForClientSZYDTO() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getTenancyName() {
		return tenancyName;
	}

	public void setTenancyName(String tenancyName) {
		this.tenancyName = tenancyName;
	}

	public String getTenCustomerDes() {
		return tenCustomerDes;
	}

	public void setTenCustomerDes(String tenCustomerDes) {
		this.tenCustomerDes = tenCustomerDes;
	}

	public String getTenancyType() {
		return tenancyType;
	}

	public void setTenancyType(String tenancyType) {
		this.tenancyType = tenancyType;
	}

	public String getOldTenancyBill() {
		return oldTenancyBill;
	}

	public void setOldTenancyBill(String oldTenancyBill) {
		this.oldTenancyBill = oldTenancyBill;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getTenancyDate() {
		return tenancyDate;
	}

	public void setTenancyDate(String tenancyDate) {
		this.tenancyDate = tenancyDate;
	}

	public String getLeaseCount() {
		return leaseCount;
	}

	public void setLeaseCount(String leaseCount) {
		this.leaseCount = leaseCount;
	}

	public String getRoomTotalRent() {
		return roomTotalRent;
	}

	public void setRoomTotalRent(String roomTotalRent) {
		this.roomTotalRent = roomTotalRent;
	}

	public String getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(String depositAmount) {
		this.depositAmount = depositAmount;
	}

	public String getFreeDays() {
		return freeDays;
	}

	public void setFreeDays(String freeDays) {
		this.freeDays = freeDays;
	}

	public String getFirstPayRent() {
		return firstPayRent;
	}

	public void setFirstPayRent(String firstPayRent) {
		this.firstPayRent = firstPayRent;
	}

	public String getFirstLeaseEndDate() {
		return firstLeaseEndDate;
	}

	public void setFirstLeaseEndDate(String firstLeaseEndDate) {
		this.firstLeaseEndDate = firstLeaseEndDate;
	}

	public String getRentCountType() {
		return rentCountType;
	}

	public void setRentCountType(String rentCountType) {
		this.rentCountType = rentCountType;
	}

	public String getTenancyState() {
		return tenancyState;
	}

	public void setTenancyState(String tenancyState) {
		this.tenancyState = tenancyState;
	}

	public String getTenancyAdviser() {
		return tenancyAdviser;
	}

	public void setTenancyAdviser(String tenancyAdviser) {
		this.tenancyAdviser = tenancyAdviser;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDealTotalRent() {
		return dealTotalRent;
	}

	public void setDealTotalRent(String dealTotalRent) {
		this.dealTotalRent = dealTotalRent;
	}

	public String getQuitRoomDate() {
		return quitRoomDate;
	}

	public void setQuitRoomDate(String quitRoomDate) {
		this.quitRoomDate = quitRoomDate;
	}

	public List<BillForClientSZY> getBills() {
		return bills;
	}

	public void setBills(List<BillForClientSZY> bills) {
		this.bills = bills;
	}
    
}
