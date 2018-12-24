package com.everhomes.rest.aclink;

public interface AclinkNotificationTemplateCode {
    public static final String SCOPE = "aclink.notification";
    
    public static final int ACLINK_NEW_AUTH = 1; // 有新的授权信息
    public static final int ACLINK_VISITOR_COMING = 2; // 临时授权访客来访
    public static final int ACLINK_PHOTO_SYNC_SUCCESS = 3; // 照片同步成功通知
    public static final int ACLINK_PHOTO_SYNC_REJECT = 4; // 照片同步失败通知
}
