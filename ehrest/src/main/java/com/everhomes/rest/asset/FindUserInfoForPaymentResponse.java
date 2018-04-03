//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/9/4.
 */

/**
 *<ul>
 * <li>contractList:合同列表</li>
 * <li>addressNames:指定合同下的地址名称集合</li>
 * <li>areaSizesSum:指定合同下的资产面积和</li>
 * <li>customerName:客户名称</li>
 *</ul>
 */
public class FindUserInfoForPaymentResponse {
    @ItemType(FindUserInfoForPaymentDTO.class)
    private List<FindUserInfoForPaymentDTO> contractList;
    @ItemType(String.class)
    private List<String> addressNames;
    private String areaSizesSum;
    private String customerName;

    public List<FindUserInfoForPaymentDTO> getContractList() {
        return contractList;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setContractList(List<FindUserInfoForPaymentDTO> contractList) {
        this.contractList = contractList;
    }

    public List<String> getAddressNames() {
        return addressNames;
    }

    public void setAddressNames(List<String> addressNames) {
        this.addressNames = addressNames;
    }

    public String getAreaSizesSum() {
        return areaSizesSum;
    }

    public void setAreaSizesSum(String areaSizesSum) {
        this.areaSizesSum = areaSizesSum;
    }
}
