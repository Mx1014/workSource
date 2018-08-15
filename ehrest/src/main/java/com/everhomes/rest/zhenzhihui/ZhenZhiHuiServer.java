package com.everhomes.rest.zhenzhihui;

import com.everhomes.util.StringHelper;

/**
 * 圳智慧服务与code对应关系
 */
public enum ZhenZhiHuiServer {
    MEETING(1011,"会议室预定",40400L),PARKING(1012,"停车缴费",40800L),HOTLINE(1013,"服务热线",40300L),SERVICE_ALLIANCE(1014,"服务联盟",40500L),
    PARK_INTRODUCTION(1015,"园区介绍",10200L),ASSET_MANAGEMENT(1016,"资产管理",38000L),ENTERPRISES(1017,"企业名录",40500L), PARK_INVESTMENT(1018,"园区招商",40100L),
    PROPERTY_WARRANTY(1019,"物业报修",20100L),COMPLAINT_PROPOSAL(1020,"投诉建议",40500L),ENTRANCE_GUARD(1021,"大堂门禁",41010L), BANNER(1022,"广告banner",10400L),
    NOTICE_MANAGE(1023,"公告管理",10300L), ACTIVITY(1024,"官方活动",10600L), NEWS(1025,"园区快讯",10800L), MAIL_LIST(1026,"通讯录",50100L), CARD_ATTENDANCE(1027,"打卡考勤",50600L),
    TASKS(1028,"任务管理",13000L), PARTY(1029,"园区党建",10110L), DYNAMIC(1030,"党建动态",40500L), NOTICE(1031,"党建公告",40500L), DONE(1032,"两学一做",40500L);

    private Integer code;
    private String name;
    private Long module;

    private ZhenZhiHuiServer(Integer code, String name, Long module){
        this.code = code;
        this.name = name;
        this.module = module;
    }

    public Long getModule() {
        return module;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static ZhenZhiHuiServer fromStatus(Integer code) {
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
