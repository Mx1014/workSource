// @formatter:off
package com.everhomes.rest.visitorsys;

/**
 * <p>品牌形象字段</p>
 * <ul>
 * <li>NULL("null"),: 无</li>
 * <li>USER_PIC("user_pic"): 用户头像</li>
 * <li>VISITOR_QRCODE("visitor_qrcode"): 用户二维码</li>
 * </ul>
 */
public enum VisitorsysLeftImageType {
    NULL("null"),
    USER_PIC("user_pic"),
    VISITOR_QRCODE("visitor_qrcode");

    private String code;
    VisitorsysLeftImageType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static VisitorsysLeftImageType fromCode(String code) {
        if(code != null) {
            VisitorsysLeftImageType[] values = VisitorsysLeftImageType.values();
            for(VisitorsysLeftImageType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }

        return null;
    }
}
