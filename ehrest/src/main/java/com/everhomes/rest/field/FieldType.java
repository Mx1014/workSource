package com.everhomes.rest.field;

/**
 * <ul>
 *     <li>String:</li>
 *     <li>BigDecimal: </li>
 *     <li>Double: </li>
 *     <li>Integer: </li>
 *     <li>Byte: </li>
 *     <li>Long: </li>
 *     <li>Blob: </li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public enum FieldType {
    STRING("String"), BIGDECIMAL("BigDecimal"), DOUBLE("Double"), INTEGER("Integer"), BYTE("Byte"),
    LONG("Long"), BLOB("Blob");

    private String name;

    private FieldType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static FieldType fromName(String name) {
        if(name != null) {
            FieldType[] values = FieldType.values();
            for(FieldType value : values) {
                if(value.equals(name)) {
                    return value;
                }
            }
        }

        return null;
    }
}
