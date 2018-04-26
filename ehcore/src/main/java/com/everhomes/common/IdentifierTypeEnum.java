package com.everhomes.common;


/**
 * Created by yuanlei on 2018/4/2.
 */
public enum IdentifierTypeEnum {

    MOBILE((byte) 0,"mobile"),
    EMAIL((byte) 1,"email");

    private byte code;
    private String value;

    /**
     * 私有构造器，避免外界new对象
     * @param code
     * @param value
     */
    private IdentifierTypeEnum(byte code,String value){
        this.code = code;
        this.value = value;
    }

    /**
     * 获取code的方法
     * @return
     */
    public byte getCode() {
        return code;
    }

    /**
     * 获取value的方法
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     * 返回枚举类IdentifierTypeEnum对象的方法
     * @return
     */
    public static IdentifierTypeEnum codeOf(byte code){
        for(IdentifierTypeEnum identifierTypeEnum : values()){
            if(identifierTypeEnum.getCode() == code){
                return identifierTypeEnum;
            }
        }
        throw new RuntimeException("没有找到对应的枚举");
    }

}
