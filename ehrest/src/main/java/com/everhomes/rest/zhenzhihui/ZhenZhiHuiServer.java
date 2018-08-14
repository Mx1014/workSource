package com.everhomes.rest.zhenzhihui;

import com.everhomes.util.StringHelper;

/**
 * 圳智慧服务与code对应关系
 */
public enum ZhenZhiHuiServer {
    MEETING(1011,"会议室预定"),PARKING(1012,"停车缴费"),HOTLINE(1013,"服务热线"),SERVICE_ALLIANCE(1014,"服务联盟"),
    PARK_INTRODUCTION(1015,"园区介绍"),ASSET_MANAGEMENT(1016,"资产管理"),ENTERPRISES(1017,"企业名录"), PARK_INVESTMENT(1018,"园区招商"),
    PROPERTY_WARRANTY(1019,"物业保修"),COMPLAINT_PROPOSAL(1020,"投诉建议"),ENTRANCE_GUARD(1021,"大堂门禁"), BANNER(1022,"广告banner"),
    NOTICE_MANAGE(1023,"公告管理"), ACTIVITY(1024,"官方活动"), NEWS(1025,"园区快讯"), MAIL_LIST(1026,"通讯录"), CARD_ATTENDANCE(1027,"打卡考勤"),
    TASKS(1028,"任务管理"), PARTY(1029,"园区党建"), DYNAMIC(1030,"党建动态"), NOTICE(1031,"党建公告"), DONE(1032,"两学一做");

    private Integer code;
    private String name;

    private ZhenZhiHuiServer(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static ZhenZhiHuiServer fromStatus(byte code) {
        for(ZhenZhiHuiServer v : ZhenZhiHuiServer.values()) {
            if(v.getCode() == code)
                return v;
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
