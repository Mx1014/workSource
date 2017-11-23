// @formatter:off
package com.everhomes.rest.barcode;

/**
 * <ul>
 * <li>BIZ: 电商</li>
 * </ul>
 */
public enum BarcodeType {

    BIZ((byte) 1);
    private byte code;

    private BarcodeType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static BarcodeType fromCode(Byte code) {
        if (code != null) {
            for (BarcodeType flag : BarcodeType.values()) {
                if (flag.getCode() == code.byteValue()) {
                    return flag;
                }
            }
        }
        return null;
    }
}
