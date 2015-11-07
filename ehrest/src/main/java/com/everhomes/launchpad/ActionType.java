// @formatter:off
package com.everhomes.launchpad;

/**
 * <ul>参数类型
 * <li>NONE(0): 无</li>
 * <li>BIZ(1): 更多按钮</li>
 * <li>NAVIGATION(2): 跳下一层</li>
 * <li>OPEN_DOOR(21): 门禁</li>
 * <li>PUNCH(23): 打卡</li>
 * <li>MEETINGROOM(24): 会议室预定</li>
 * <li>VIPPARKING(25): vip停车位预定</li>
 * <li>ELECSCREEN(26): 电子屏预定</li>
 * </ul>
 */
public enum ActionType {
      NONE((byte)0),MORE_BUTTON((byte)1),NAVIGATION((byte)2),FAMILY_DETAILS((byte)3),GROUP_DETAILS((byte)4),
      WIN_COUPON((byte)5),USE_COUPON((byte)6),BIZ_DETAILS((byte)7),DOWNLOAD_APP((byte)8),POST_DETAILS((byte)9),
      CHECKIN_ACTIVITY((byte)10),OPEN_MSG_SESSION((byte)11),SEND_MSG((byte)12),OFFICIAL_URL((byte)13),
      THIRDPART_URL((byte)14),POST_BY_CATEGORY((byte)15),QRCODE_SCAN((byte)16),PHONE_CALL((byte)17),LAUNCH_APP((byte)18),
      POST_NEW((byte)19),PM_DETAILS((byte)20),OPEN_DOOR((byte)21),PAY((byte)22),PUNCH((byte)23),MEETINGROOM((byte)24),VIPPARKING((byte)25),
      ELECSCREEN((byte)26) ,VEDIO_MEETING((byte)27);
    
    private byte code;
    
    private ActionType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ActionType fromCode(Byte code) {
        if(code == null)
            return null;
        
        ActionType[] values = ActionType.values();
        for(ActionType value : values) {
            if(value.code == code.byteValue()) {
                return value;
            }
        }
        
        return null;
    }
}
