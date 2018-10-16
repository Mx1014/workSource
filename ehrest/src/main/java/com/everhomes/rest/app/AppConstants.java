// @formatter:off
package com.everhomes.rest.app;

public interface AppConstants {
    //TODO save in db
    public static final String APPKEY_BORDER = "b86ddb3b-ac77-4a65-ae03-7e8482a3db70";
    public static final String APPKEY_BIZ = "d80e06ca-3766-11e5-b18f-b083fe4e159f";
    public static final String APPKEY_APP = "bf925ea0-a5e0-11e4-a67c-00163e024631";

    public static final String APPKEY_SDK = "2f4cc25b-3d18-4485-ae10-fa954fbac829";

    public static final long APPID_DEFAULT = 0;
    public static final long APPID_MESSAGING = 1;
    public static final long APPID_FORUM = 2;
    public static final long APPID_ACTIVITY = 3;
    public static final long APPID_ADDRESS = 4;
    public static final long APPID_BULLETIN = 5;
    public static final long APPID_CATEGORY = 6;
    public static final long APPID_COUPON = 7;
    public static final long APPID_ECARD = 8;
    public static final long APPID_FAMILY = 9;
    public static final long APPID_FAVORITE = 10;
    public static final long APPID_FLEAMARKET = 11;
    public static final long APPID_GROUP = 12;
    public static final long APPID_PKG = 13;
    public static final long APPID_POLL = 14;
    public static final long APPID_REALESTATE = 15;
    public static final long APPID_REGION = 16;
    public static final long APPID_USER = 17;
    public static final long APPID_PUSH = 18;
    public static final long APPID_PM = 19;               // Property management
    public static final long APPID_THIRD_PART = 20;       //the third service
    public static final long APPID_LINK = 21;
    public static final long APPID_TOPIC_SUMMARY = 22;    // using in forwarding topic
    public static final long APPID_GROUP_CARD = 23;       // using in sharing group info
    public static final long APPID_GARC = 24;             // 业委，Government Agency - Resident Committee
    public static final long APPID_GANC = 25;             // 居委，Government Agency - Neighbor Committee
    public static final long APPID_GAPS = 26;             // 公安，Government Agency - Police Station
    public static final long APPID_ORGTASK = 27;          // Organization Task
    public static final long APPID_USED_AND_RENTAL = 28;  // 二手和租售
    public static final long APPID_FREE_STUFF = 29;       // 免费物品
    public static final long APPID_LOST_AND_FOUND = 30;   // 失物招领
    public static final long APPID_ENTERPRISE = 31;
    public static final long APPID_PARK_ADMIN = 32;
    public static final long APPID_ACLINK = 33;             // 门禁推送
    
    public static final int PAGINATION_DEFAULT_SIZE = 20;
    public static final int PAGINATION_MAX_SIZE = 2000;
    public static final String DEFAULT_ETAG_TIMEOUT_KEY = "etag.timeout";
    public static final int DEFAULT_ETAG_TIMEOUT_SECONDS = 5;
    
    public static final int DEFAULT_MAX_BANNER_CAN_ACTIVE = 8;// 默认用户可以激活的banne的数量
}
