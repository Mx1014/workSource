package com.everhomes.rest.namespace;

/**
 * <ul>左上角的名称显示方式
 * <li>ONLY_COMPANY_NAME((byte)0): 仅公司名称</li>
 * <li>ONLY_COMMUNITY_NAME((byte)1): 仅园区名称</li>
 * <li>COMMUNITY_COMPANY_NAME((byte)2): 园区名称+公司名称</li>
 * </ul>
 */
public enum NamespaceNameType {
    ONLY_COMPANY_NAME((byte)0), ONLY_COMMUNITY_NAME((byte)1), COMMUNITY_COMPANY_NAME((byte)2);

    private Byte code;

    private NamespaceNameType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return this.code;
    }

    public static NamespaceNameType fromCode(Byte code) {
        NamespaceNameType[] values = NamespaceNameType.values();
        for(NamespaceNameType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }

        return null;
    }
}
