package com.everhomes.rest.namespace;

/**
 * <ul>左上角的名称显示方式
 * <li>ONLY_COMPANY_NAME(0): 仅公司名称</li>
 * <li>ONLY_COMMUNITY_NAME(1): 仅园区名称</li>
 * <li>COMMUNITY_COMPANY_NAME(2): 园区名称+公司名称</li>
 * </ul>
 */
public enum NamespaceNameType {
    ONLY_COMPANY_NAME(0), ONLY_COMMUNITY_NAME(1), COMMUNITY_COMPANY_NAME(2);

    private Integer code;

    private NamespaceNameType(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }

    public static NamespaceNameType fromCode(Integer code) {
        NamespaceNameType[] values = NamespaceNameType.values();
        for(NamespaceNameType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }

        return null;
    }
}
