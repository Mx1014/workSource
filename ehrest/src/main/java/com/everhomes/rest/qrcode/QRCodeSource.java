// @formatter:off
package com.everhomes.rest.qrcode;

/**
 * <ul>
 *     <li>INDEX("INDEX"): 来自首页的扫一扫</li>
 *     <li>OTHER("OTHER"): 来自其他地方的扫一扫</li>
 *     <li>FLOW("FLOW"): 来自工作流的扫一扫</li>
 * </ul>
 */
public enum QRCodeSource {

    INDEX("INDEX"),
    OTHER("OTHER"),
    FLOW("FLOW"),
    ;

    private String code;

    QRCodeSource(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static QRCodeSource fromCode(String code) {
        if (code != null) {
            QRCodeSource[] values = QRCodeSource.values();
            for (QRCodeSource value : values) {
                if (value.code.equals(code)) {
                    return value;
                }
            }
        }
        return null;
    }
}
