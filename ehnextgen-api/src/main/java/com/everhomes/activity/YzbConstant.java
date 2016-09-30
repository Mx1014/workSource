package com.everhomes.activity;

public interface YzbConstant {
    //http://hzgate.boreyun.cn/hzapi/startlive?devid=98840940&version=1.0.1&devip=127.0.0.1
    //http://hzgate.boreyun.cn/hzapi/startlive?devid=98877787&version=1.0.1&devip=127.0.0.1
    //http://hzgate.boreyun.cn/hzapi/setcontinue?devid=98877787&continue=0
    //http://video.zuolin.com/evh/api/activity/devicechange?optcode=livestop&devid=98877787&yzb_user_id=98826894&vid=R0lKuxjprrsrgV&app_nonce=9998999989
    //http://video.zuolin.com/evh/api/activity/devicechange?optcode=livestart&devid=98877787&yzb_user_id=98826894&vid=R0lKuxjprrsrgV&app_nonce=1106876739
    public final static String YZB_SERVER_DEFAULT = "http://hzgate.boreyun.cn";
    public final static String YZB_SERVER = "yzb.video.server";
    public final static String START_LIVE = "hzapi/startlive";
    public final static String SET_CONTINUE = "hzapi/setcontinue";
    
    public final static String SCHEDULE_TARGET_NAME = "video-cron-";
    public final static String VIDEO_OFFICIAL_SUPPORT = "video.official.support";
    public final static String VIDEO_NONE_OFFICIAL_SUPPORT = "video.none_official.support";
    public final static String VIDEO_NORMAL_SUPPORT = "video.normal.support";
}
