package com.everhomes.rest.filedownload;

/**
 * <ul>
 *     <li>NOTALLREAD((byte) 0): 仍有未阅读</li>
 *     <li>ALLREAD((byte) 1): 全部已阅读</li>
 * </ul>
 */
public enum AllReadStatus {

    NOTALLREAD((byte) 0), ALLREAD((byte) 1);

    private Byte core;

    private AllReadStatus(Byte core) {
        this.core = core;
    }

    public Byte getCode() {
        return core;
    }

    public static AllReadStatus fromCode(Byte core) {
        if (core != null) {
            AllReadStatus[] values = AllReadStatus.values();
            for (AllReadStatus value : values) {
                if (value.getCode().equals(core)) {
                    return value;
                }
            }
        }

        return null;
    }
}
