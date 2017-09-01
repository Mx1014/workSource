//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/9/1.
 */

/**
 *<ul>
 * <li>targetName:客户名称</li>
 * <li>contractNum:合同编号</li>
 * <li>hasMoreContract:是否有更多合同，1：是；0：否</li>
 * <li>addressNames:选定合同的地址名称集合</li>
 * <li>areaSizesSum:选定合同下的资产的面积和</li>
 *</ul>
 */
public class FindUserInfoForPaymentDTO {
    private String targetName;
    private String contractNum;
    private Byte hasMoreContract;
    private List<String> addressNames;
    private Double areaSizesSum;

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public Byte getHasMoreContract() {
        return hasMoreContract;
    }

    public void setHasMoreContract(Byte hasMoreContract) {
        this.hasMoreContract = hasMoreContract;
    }

    public List<String> getAddressNames() {
        return addressNames;
    }

    public void setAddressNames(List<String> addressNames) {
        this.addressNames = addressNames;
    }

    public Double getAreaSizesSum() {
        return areaSizesSum;
    }

    public void setAreaSizesSum(Double areaSizesSum) {
        this.areaSizesSum = areaSizesSum;
    }
}
