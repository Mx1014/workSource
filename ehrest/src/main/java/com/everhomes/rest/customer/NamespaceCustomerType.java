package com.everhomes.rest.customer;

/**
 * <ul>来自于第三方客户的类型，eh_enterprise_customers表的 namespace_customer_type字段
 * <li>SHENZHOU("shenzhou"): 神州数码</li>
 * <li>EBEI("ebei"): 一碑</li>
 * </ul>
 * Created by ying.xiong on 2017/8/11.
 */
public enum NamespaceCustomerType {
    SHENZHOU("shenzhou"), EBEI("ebei"), CM("cm");

    private String code;
    private NamespaceCustomerType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static NamespaceCustomerType fromCode(String code) {
        if(code != null) {
            NamespaceCustomerType[] values = NamespaceCustomerType.values();
            for(NamespaceCustomerType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }

        return null;
    }
}
