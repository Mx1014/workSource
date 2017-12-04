// @formatter:off
package com.everhomes.rest.qrcode;

/**
 * <ul>
 *     <li>FLOW("FLOW"): FLOW</li>
 * </ul>
 */
public enum QRCodeHandler {

    FLOW("FLOW");

    private String code;

    QRCodeHandler(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static QRCodeHandler fromCode(String code) {
        if (code != null) {
            QRCodeHandler[] values = QRCodeHandler.values();
            for (QRCodeHandler value : values) {
                if (value.code.equals(code)) {
                    return value;
                }
            }
        }
        return null;
    }
}
