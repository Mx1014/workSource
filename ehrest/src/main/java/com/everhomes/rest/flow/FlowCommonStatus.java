package com.everhomes.rest.flow;

/**
 * <ul>
 *     <li>INVALID((byte) 0): INVALID</li>
 *     <li>WAITING_FOR_APPROVAL((byte) 1): WAITING_FOR_APPROVAL</li>
 *     <li>VALID((byte) 2): VALID</li>
 * </ul>
 */
public enum FlowCommonStatus {

    INVALID((byte) 0), WAITING_FOR_APPROVAL((byte) 1), VALID((byte) 2);

    private Byte code;

    FlowCommonStatus(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static FlowCommonStatus fromCode(Byte code) {
        if (code != null) {
            for (FlowCommonStatus type : FlowCommonStatus.values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
        }
        return null;
    }
}
