//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/9/4.
 */
/**
 *<ul>
 * <li>addressNames:指定合同下的地址名称集合</li>
 * <li>areaSizesSum:指定合同下的资产面积和</li>
 *</ul>
 */
public class GetAreaAndAddressByContractDTO {
    @ItemType(String.class)
    private List<String> addressNames;
    private String areaSizesSum;

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
