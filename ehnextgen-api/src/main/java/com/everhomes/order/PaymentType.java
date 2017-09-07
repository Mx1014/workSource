//@formatter:off
package com.everhomes.order;

/**
 * <ul>
 *     <li>WECHAT_APP(1): WECHAT_APP</li>
 *     <li>ALI_PAY(8): ALI_PAY</li>
 *     <li>WECHAT_JS(9): WECHAT_JS</li>
 * </ul>
 */
public enum PaymentType {
    WECHAT_APP(1), ALI_PAY(8), WECHAT_JS(9);
    private Integer code;

    PaymentType(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static PaymentType fromCode(Integer code) {
        for (PaymentType type : PaymentType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }

}
