package com.everhomes.rest.point;

/**
 * <ul>
 *     <li>INACTIVE((byte) 0): INACTIVE</li>
 *     <li>WAITING_FOR_CONFIRMATION((byte) 1): 待处理</li>
 *     <li>ACTIVE((byte) 2): 已处理</li>
 * </ul>
 */
public enum PointEventLogStatus {

    WAITING_FOR_PROCESS((byte) 1), PROCESSING((byte) 2), PROCESSED((byte) 3);

    private Byte code;

    PointEventLogStatus(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static PointEventLogStatus fromCode(Byte code) {
        if (code != null) {
            for (PointEventLogStatus type : PointEventLogStatus.values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
        }
        return null;
    }
}
