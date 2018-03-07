// @formatter:off
package com.everhomes.rest.banner;

/**
 * <ul>
 *     <li>DELETE((byte) 0): DELETE</li>
 *     <li>WAITINGCONFIRM((byte) 1): WAITINGCONFIRM</li>
 *     <li>ACTIVE((byte) 2): 开启</li>
 *     <li>CLOSE((byte) 3): 关闭</li>
 * </ul>
 */
public enum BannerStatus {

    DELETE((byte) 0), WAITINGCONFIRM((byte) 1), ACTIVE((byte) 2), CLOSE((byte) 3);

    private byte code;

    private BannerStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static BannerStatus fromCode(Byte code) {
        if (code == null)
            return null;
        switch (code.byteValue()) {
            case 0:
                return DELETE;
            case 1:
                return WAITINGCONFIRM;
            case 2:
                return ACTIVE;
            case 3:
                return CLOSE;
            default:
                assert (false);
                break;
        }
        return null;
    }
}
