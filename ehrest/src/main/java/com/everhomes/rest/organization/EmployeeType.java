package com.everhomes.rest.organization;

/**
 * <p>员工类型</p>
 * <ul>
 * <li>FULLTIME: 全职</li>
 * <li>PARTTIME: 兼职</li>
 * <li>INTERSHIP: 实习</li>
 * <li>LABORDISPATCH: 劳动派遣</li>
 * </ul>
 */
public enum EmployeeType {
    FULLTIME((byte)0,"FULLTIME"), PARTTIME((byte)1,"PARTTIME"),INTERSHIP((byte)2,"INTERSHIP"),LABORDISPATCH((byte)3,"LABORDISPATCH");

    private byte code;
    private String text;

    private EmployeeType(byte code, String text) {
        this.code = code;
        this.text = text;
    }

    public byte getCode() {
        return this.code;
    }

    public String getText(){
        return this.text;
    }

    public static EmployeeType fromCode(Byte code) {
        if(code != null) {
            EmployeeType[] values = EmployeeType.values();
            for(EmployeeType value : values) {
                if(code.byteValue() == value.code) {
                    return value;
                }
            }
        }
        return null;
    }
}