//@formatter:off
package com.everhomes.rest.contract;

/**
 * Created by Wentian Wang on 2018/1/10.
 */

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>contractNumber:合同编号</li>
 * <li>name:合同名称</li>
 * <li>contractType: 合同属性 参考{@link com.everhomes.rest.contract.ContractType}</li>
 * <li>status: 合同状态 参考{@link com.everhomes.rest.contract.ContractStatus}</li>
 * <li>contractStartDate:开始日期</li>
 * <li>contractEndDate:结束日期</li>
 * <li>totalAmount:总金额</li>
 *</ul>
 */
public class ContractLogDTO {
    private String contractNumber;
    private String name;
    private Byte contractType;
    private String contractStartDate;
    private String contractEndDate;
    private String totalAmount;
    private Byte status;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getContractType() {
        return contractType;
    }

    public void setContractType(Byte contractType) {
        this.contractType = contractType;
    }

    public String getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(String contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public String getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(String contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
