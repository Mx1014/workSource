// @formatter:off
package com.everhomes.rest.announcement;

/**
 * <ul>是否对公告收藏标记
 * <li>NONE(0): 未收藏</li>
 * <li>FAVORITE(1): 已收藏</li>
 * </ul>
 */
public enum AnnouncementFavoriteFlag {
    NONE((byte)0), FAVORITE((byte)1);

    private byte code;

    private AnnouncementFavoriteFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static AnnouncementFavoriteFlag fromCode(Byte code) {
        if(code != null) {
            AnnouncementFavoriteFlag[] values = AnnouncementFavoriteFlag.values();
            for(AnnouncementFavoriteFlag value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
