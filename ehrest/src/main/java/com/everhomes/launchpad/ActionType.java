// @formatter:off
package com.everhomes.launchpad;

/**
 * <ul>参数类型
 * <li>NNONE(0): 无，不跳转</li>
 * <li>MORE_BUTTON(1): 更多按钮</li>
 * <li>NAVIGATION(2): 跳下一层</li>
 * <li>FAMILY_DETAILS(3): 家庭详情</li>
 * <li>GROUP_DETAILS(4): 圈详情</li>
 * <li>WIN_COUPON(5): 领取优惠券</li>
 * <li>USE_COUPON(6): 使用优惠券</li>
 * <li>BIZ_DETAILS(7): 商家详情（含黄页）</li>
 * <li>DOWNLOAD_APP(8): 下载APP</li>
 * <li>TOPIC_DETAILS(9): 帖子详情</li>
 * <li>CHECKIN_ACTIVITY(10): 活动签到</li>
 * <li>OPEN_MSG_SESSION(11): 打开消息会话</li>
 * <li>SEND_MSG(12): 打开消息对话框发消息</li>
 * <li>ZUOLIN_URL(13): 左邻官方url</li>
 * <li>THIRDPART_URL(14): 第三方url</li>
 * <li>TOPIC_BY_CATEGORY(15): 按类型查帖列表</li>
 * <li>QRCODE_SCAN(16): 二维码扫描</li>
 * <li>PHONE_CALL(17): 拨打电话</li>
 * <li>OPEN_APP(18): 打开应用</li>
 * </ul>
 */
public enum ActionType {
//    NONE((byte)0), MORE_BUTTON((byte)1), APP((byte)2),ZUOLIN_URL((byte)3), THIRDPART_URL((byte)4), YELLOW_PAGE((byte)5),
//    POST((byte)6), MESSAGE((byte)7), ACTIVITY((byte)8), HOTLINE((byte)9), QRCODE_SCAN((byte)10) ,POST_ACTION((byte)11),COUPON((byte)12)
      NONE((byte)0),MORE_BUTTON((byte)1),NAVIGATION((byte)2),FAMILY_DETAILS((byte)3),GROUP_DETAILS((byte)4),
      WIN_COUPON((byte)5),USE_COUPON((byte)6),BIZ_DETAILS((byte)7),DOWNLOAD_APP((byte)8),TOPIC_DETAILS((byte)9),
      CHECKIN_ACTIVITY((byte)10),OPEN_MSG_SESSION((byte)11),SEND_MSG((byte)12),ZUOLIN_URL((byte)13),
      THIRDPART_URL((byte)14),TOPIC_BY_CATEGORY((byte)15),QRCODE_SCAN((byte)16),PHONE_CALL((byte)17),OPEN_APP((byte)18);
    
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
