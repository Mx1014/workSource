package com.everhomes.rest.decoration;

/**
 * <ul>
 * <li>"basic","装修申请"</li>
 * <li>"file","装修资料"</li>
 * <li>"fee","缴费"</li>
 * <li>"apply","施工申请"</li>
 * <li>"complete","竣工验收"</li>
 * <li>"refound","押金退回"</li>
 * </ul>
 */
public enum IllustrationType {
    BASIC("basic","装修申请"),
    FILE("file","装修资料"),
    FEE("fee","缴费"),
    APPLY("apply","施工申请"),
    COMPLETE("complete","竣工验收"),
    REFOUND("refound","押金退回");

    private String code;
    private String describe;

    private IllustrationType(String code,String describe) {
        this.code = code;
        this.describe = describe;
    }

    public String getCode() {
        return this.code;
    }

    public String getDescribe() {
        return describe;
    }

    public static IllustrationType fromCode(String code) {
        for(IllustrationType t : IllustrationType.values()) {
            if (t.code.equals(code)) {
                return t;
            }
        }

        return null;
    }
}
