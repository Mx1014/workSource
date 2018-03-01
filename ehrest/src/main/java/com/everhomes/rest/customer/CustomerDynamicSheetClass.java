package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2018/1/16.
 */
public enum CustomerDynamicSheetClass {
    CUSTOMER_TAX("com.everhomes.customer.CustomerTax"),
    CUSTOMER_ACCOUNT("com.everhomes.customer.CustomerAccount"),
    CUSTOMER_TALENT("com.everhomes.customer.CustomerTalent"),
    CUSTOMER_TRADEMARK("com.everhomes.customer.CustomerTrademark"),
    CUSTOMER_APPLY_PROJECT("com.everhomes.customer.CustomerApplyProject"),
    CUSTOMER_COMMERCIAL("com.everhomes.customer.CustomerCommercial"),
    CUSTOMER_INVESTMENT("com.everhomes.customer.CustomerInvestment"),
    CUSTOMER_ECONOMIC_INDICATOR("com.everhomes.customer.CustomerEconomicIndicator"),
    CUSTOMER_PATENT("com.everhomes.customer.CustomerPatent"),
    CUSTOMER_CERTIFICATE("com.everhomes.customer.CustomerCertificate"),
    CUSTOMER_ENTRY_INFO("com.everhomes.customer.CustomerEntryInfo"),
    CUSTOMER_DEPARTURE_INFO("com.everhomes.customer.CustomerDepartureInfo"),
    CUSTOMER_TRACKING("com.everhomes.customer.CustomerTracking"),
    CUSTOMER_TRACKING_PLAN("com.everhomes.customer.CustomerTrackingPlan");

    private String code;

    private CustomerDynamicSheetClass(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static CustomerDynamicSheetClass fromStatus(String code) {
        if(code != null) {
            for(CustomerDynamicSheetClass v : CustomerDynamicSheetClass.values()) {
                if(v.getCode().equals(code))
                    return v;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
