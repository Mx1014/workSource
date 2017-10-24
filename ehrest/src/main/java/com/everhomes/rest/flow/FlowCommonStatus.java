package com.everhomes.rest.flow;

/**
 * <ul>
 *     <li>INACTIVE((byte) 0): INACTIVE</li>
 *     <li>WAITING_FOR_APPROVAL((byte) 1): WAITING_FOR_APPROVAL</li>
 *     <li>ACTIVE((byte) 2): ACTIVE</li>
 * </ul>
 */
public enum FlowCommonStatus {

    INACTIVE((byte) 0), WAITING_FOR_APPROVAL((byte) 1), ACTIVE((byte) 2);

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
