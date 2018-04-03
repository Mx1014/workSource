// @formatter:off
package com.everhomes.rest.flow;

/**
 * <ul>
 *     <li>INVALID((byte) 1): INVALID</li>
 *     <li>VALID((byte) 2): VALID</li>
 *     <li>UNKNOWN((byte) 3): UNKNOWN</li>
 * </ul>
 */
public enum FlowValidationStatus {

    INVALID((byte) 1), VALID((byte) 2), UNKNOWN((byte) 3);

    private Byte code;

    public Byte getCode() {
        return this.code;
    }

    FlowValidationStatus(Byte code) {
        this.code = code;
    }

    public static FlowValidationStatus fromCode(Byte code) {
        if (code != null) {
            for (FlowValidationStatus status : FlowValidationStatus.values()) {
                if (status.getCode().equals(code)) {
                    return status;
                }
            }
        }
        return null;
    }
}
