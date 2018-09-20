package com.everhomes.rest.archives;

/**
 * <ul>
 * <li>PROMOTE((byte) 0): 晋升</li>
 * <li>TRANSFER((byte) 1): 调整、安排</li>
 * <li>OTHER((byte) 2): 其他</li>
 * </ul>
 */
public enum ArchivesTransferType {
    PROMOTE((byte) 0), TRANSFER((byte) 1), OTHER((byte) 2);
    private Byte code;

    private ArchivesTransferType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static ArchivesTransferType fromCode(Byte code) {
        if(code != null) {
            ArchivesTransferType[] values = ArchivesTransferType.values();
            for(ArchivesTransferType value : values) {
                if(code.byteValue() == value.code) {
                    return value;
                }
            }
        }
        return null;
    }
}
