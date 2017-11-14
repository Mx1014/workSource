//@formatter:off
package com.everhomes.asset;


import com.everhomes.rest.asset.ContractProperty;
import com.everhomes.server.schema.tables.pojos.EhPaymentChargingStandards;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Wentian Wang on 2017/10/17.
 */

public class BillItemsExpectancy {
    private BigDecimal amountReceivable;
    private BigDecimal amountOwed;
    private Date dateStrBegin;
    private Date dateStrEnd;
    private Byte status;
    private Long billGroupId;
    private String billDateStr;
    private String billDateDue;
    private String billDateDeadline;
    private String billDateGeneration;
    private String billCycleStart;
    private String billCycleEnd;

    private ContractProperty property;
    private PaymentBillGroupRule groupRule;
    private PaymentBillGroup group;
    private EhPaymentChargingStandards standard;
    private PaymentChargingItemScope itemScope;

    public PaymentChargingItemScope getItemScope() {
        return itemScope;
    }

    public EhPaymentChargingStandards getStandard() {
        return standard;
    }

    public void setStandard(EhPaymentChargingStandards standard) {
        this.standard = standard;
    }

    public PaymentBillGroupRule getGroupRule() {
        return groupRule;
    }

    public void setGroupRule(PaymentBillGroupRule groupRule) {
        this.groupRule = groupRule;
    }

    public PaymentBillGroup getGroup() {
        return group;
    }

    public void setGroup(PaymentBillGroup group) {
        this.group = group;
    }

    public ContractProperty getProperty() {
        return property;
    }

    public void setProperty(ContractProperty property) {
        this.property = property;
    }


    public BigDecimal getAmountReceivable() {
        return amountReceivable;
    }

    public String getBillDateGeneration() {
        return billDateGeneration;
    }

    public void setBillDateGeneration(String billDateGeneration) {
        this.billDateGeneration = billDateGeneration;
    }

    public String getBillCycleStart() {
        return billCycleStart;
    }

    public void setBillCycleStart(String billCycleStart) {
        this.billCycleStart = billCycleStart;
    }

    public String getBillCycleEnd() {
        return billCycleEnd;
    }

    public void setBillCycleEnd(String billCycleEnd) {
        this.billCycleEnd = billCycleEnd;
    }

    public Long getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }

    public String getBillDateStr() {
        return billDateStr;
    }

    public void setBillDateStr(String billDateStr) {
        this.billDateStr = billDateStr;
    }

    public void setAmountReceivable(BigDecimal amountReceivable) {
        this.amountReceivable = amountReceivable;
    }

    public BigDecimal getAmountOwed() {
        return amountOwed;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public void setAmountOwed(BigDecimal amountOwed) {
        this.amountOwed = amountOwed;
    }

    public Date getDateStrBegin() {
        return dateStrBegin;
    }

    public void setDateStrBegin(Date dateStrBegin) {
        this.dateStrBegin = dateStrBegin;
    }

    public Date getDateStrEnd() {
        return dateStrEnd;
    }

    public void setDateStrEnd(Date dateStrEnd) {
        this.dateStrEnd = dateStrEnd;
    }

    public String getBillDateDue() {
        return billDateDue;
    }

    public void setBillDateDue(String billDateDue) {
        this.billDateDue = billDateDue;
    }

    public String getBillDateDeadline() {
        return billDateDeadline;
    }

    public void setBillDateDeadline(String billDateDeadline) {
        this.billDateDeadline = billDateDeadline;
    }


    public void setItemScope(PaymentChargingItemScope itemScope) {
        this.itemScope = itemScope;
    }

}
