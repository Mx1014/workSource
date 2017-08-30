//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.Date;
import java.util.List;
import com.everhomes.discover.ItemType;

/**
 * Created by Wentian Wang on 2017/8/22.
 */

public class FeeRules {

    private Long chargingItemId;
    private Long chargingStandardId;
    private Date dateStrBegin;
    private Date dateStrEnd;
    @ItemType(String.class)
    private List<ContractProperty> properties;
    @ItemType(VariableIdAndValue.class)
    private List<VariableIdAndValue> variableIdAndValueList;


    public Long getChargingItemId() {
        return chargingItemId;
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

    public void setChargingItemId(Long chargingItemId) {
        this.chargingItemId = chargingItemId;
    }

    public Long getChargingStandardId() {
        return chargingStandardId;
    }

    public void setChargingStandardId(Long chargingStandardId) {
        this.chargingStandardId = chargingStandardId;
    }

    public List<ContractProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<ContractProperty> properties) {
        this.properties = properties;
    }

    public List<VariableIdAndValue> getVariableIdAndValueList() {
        return variableIdAndValueList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public void setVariableIdAndValueList(List<VariableIdAndValue> variableIdAndValueList) {
        this.variableIdAndValueList = variableIdAndValueList;
    }
}
