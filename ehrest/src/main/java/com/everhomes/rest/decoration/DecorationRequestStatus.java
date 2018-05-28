package com.everhomes.rest.decoration;

/**
 * <ul>
 * <li>1,"装修申请"</li>
 * <li>2,"资料审核"</li>
 * <li>3,"缴费"</li>
 * <li>4,"进场施工"</li>
 * <li>5,"竣工验收"</li>
 * <li>6,"押金退回"</li>
 * <li>7,"已完成"</li>
 * </ul>
 */
public enum DecorationRequestStatus {
    APPLY((byte)1,"装修申请","DECORATION_APPLY"),
    FILE_APPROVAL((byte)2,"资料审核","DECORATION_FILE_APPROVAL"),
    PAYMENT((byte)3,"缴费",""),
    CONSTRACT((byte)4,"进场施工","DECORATION_DURING"),
    CHECK((byte)5,"竣工验收","DECORATION_CHECK"),
    REFOUND((byte)6,"押金退回",""),
    COMPLETE((byte)7,"已完成","");

    private byte code;
    private String describe;
    private String flowOwnerType;
    private DecorationRequestStatus(byte code,String describe,String flowOwnerType) {
        this.code = code;
        this.describe = describe;
        this.flowOwnerType = flowOwnerType;
    }
    public byte getCode() {
        return this.code;
    }

    public static DecorationRequestStatus fromCode(byte code) {
        for(DecorationRequestStatus t : DecorationRequestStatus.values()) {
            if (t.code == code) {
                return t;
            }
        }

        return null;
    }

    public String getDescribe() {
        return describe;
    }

    public String getFlowOwnerType() {
        return flowOwnerType;
    }
}
