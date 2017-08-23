//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;

import java.util.Date;
import java.util.List;

/**
 * Created by Wentian Wang on 2017/8/22.
 */

public class FeeRules {

    private Long chargingItemId;
    private Long chargingStandardId;
    private Date dateStrBegin;
    private Date dateStrEnd;
    @ItemType(String.class)
    private List<String> propertyName;
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

    public List<String> getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(List<String> propertyName) {
        this.propertyName = propertyName;
    }

    public List<VariableIdAndValue> getVariableIdAndValueList() {
        return variableIdAndValueList;
    }

    public void setVariableIdAndValueList(List<VariableIdAndValue> variableIdAndValueList) {
        this.variableIdAndValueList = variableIdAndValueList;
    }
}
