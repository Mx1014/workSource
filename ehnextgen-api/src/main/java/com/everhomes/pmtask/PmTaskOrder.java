package com.everhomes.pmtask;

import com.everhomes.server.schema.tables.pojos.EhPmTaskOrders;
import com.everhomes.util.StringHelper;

public class PmTaskOrder extends EhPmTaskOrders {
    private static final long serialVersionUID = 1L;

    private String clientAppName;

    private String payerType;
    private String payerId;

    private String payeeType;
    private String payeeId;

    public PmTaskOrder() {
        this.setProductFee(0L);
        this.setServiceFee(0L);
        this.setAmount(0L);
    }

    public String getClientAppName() {
        return clientAppName;
    }

    public void setClientAppName(String clientAppName) {
        this.clientAppName = clientAppName;
    }

    public String getPayerType() {
        return payerType;
    }

    public void setPayerType(String payerType) {
        this.payerType = payerType;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public String getPayeeType() {
        return payeeType;
    }

    public void setPayeeType(String payeeType) {
        this.payeeType = payeeType;
    }

    public String getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
