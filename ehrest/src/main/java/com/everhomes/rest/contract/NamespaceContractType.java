package com.everhomes.rest.contract;

/**
 * <ul>来自于第三方客户的类型，eh_contracts表namespace_contract_type字段
 * <li>EBEI("ebei"): 一碑</li>
 * <li>RUIAN_CM("ruian_cm"): 瑞安</li>
 * </ul>
 * Created by ying.xiong on 2017/11/23.
 */
public enum NamespaceContractType {
    EBEI("ebei"),RUIAN_CM("ruian_cm");

    private String code;
    private NamespaceContractType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static NamespaceContractType fromCode(String code) {
        if(code != null) {
            NamespaceContractType[] values = NamespaceContractType.values();
            for(NamespaceContractType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }

        return null;
    }
}
