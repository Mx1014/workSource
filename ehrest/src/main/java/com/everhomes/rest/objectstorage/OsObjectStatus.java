// @formatter:off
package com.everhomes.rest.objectstorage;

/**
 * <ul>
 *     <li>INACTIVE(0): 删除</li>
 *     <li>ACTIVE(2): 正常</li>
 *     <li>OBSOLETE(3): 报废</li>
 * </ul>
 */
public enum OsObjectStatus {

    INACTIVE((byte) 0), WAITING_FOR_APPROVAL((byte) 1), ACTIVE((byte) 2);

    private Byte code;

    OsObjectStatus(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static OsObjectStatus fromCode(Byte code) {
        if (code != null) {
            for (OsObjectStatus status : OsObjectStatus.values()) {
                if (status.getCode().equals(code)) {
                    return status;
                }
            }
        }
        return null;
    }
}