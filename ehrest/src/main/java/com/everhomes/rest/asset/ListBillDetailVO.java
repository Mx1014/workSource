//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Wentian Wang on 2017/8/18.
 */
/**
 *<ul>
 * <li>billId:账单id</li>
 * <li>dateStr:账期</li>
 * <li>dateStrBegin:账单开始时间，参与排序</li>
 * <li>dateStrEnd:账单结束时间，参与排序</li>
 * <li>contractNum:合同编号</li>
 * <li>buildingName: 楼栋名称</li>
 * <li>apartmentName: 门牌名称</li>
 * <li>noticeTel:催缴电话</li>
 * <li>targetName:客户名称</li>
 * <li>targetId:客户id</li>
 * <li>targetType:客户type</li>
 * <li>amountReceivable:应收(元)</li>
 * <li>amountReceived:实收(元)</li>
 * <li>billGroupDTO:账单组，包括减免项和收费项目的集合，参考{@link com.everhomes.rest.asset.BillGroupDTO}</li>
 *</ul>
 */
public class ListBillDetailVO {
    private Long billId;
    private Long billGroupId;
    private String dateStr;
    private String dateStrBegin;
    private String dateStrEnd;
    private String noticeTel;
    private String targetName;
    private String targetType;
    private String buildingName;
    private String apartmentName;
    private Long targetId;
    @ItemType(BillGroupDTO.class)
    private BillGroupDTO billGroupDTO;
    private String contractNum;
    private String invoiceNum;
    
    private BigDecimal amountReceivable;
    private BigDecimal amountReceived;
    
    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }


    public Long getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }


    public String getNoticeTel() {
        return noticeTel;
    }

    public void setNoticeTel(String noticeTel) {
        this.noticeTel = noticeTel;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public BillGroupDTO getBillGroupDTO() {
        return billGroupDTO;
    }

    public void setBillGroupDTO(BillGroupDTO billGroupDTO) {
        this.billGroupDTO = billGroupDTO;
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

	public BigDecimal getAmountReceivable() {
		return amountReceivable;
	}

	public void setAmountReceivable(BigDecimal amountReceivable) {
		this.amountReceivable = amountReceivable;
	}

	public BigDecimal getAmountReceived() {
		return amountReceived;
	}

	public void setAmountReceived(BigDecimal amountReceived) {
		this.amountReceived = amountReceived;
	}
}
