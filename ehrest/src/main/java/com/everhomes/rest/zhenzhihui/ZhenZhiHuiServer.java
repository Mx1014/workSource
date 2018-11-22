package com.everhomes.rest.zhenzhihui;

import com.everhomes.util.StringHelper;

/**
 * 圳智慧服务与code对应关系
 */
public enum ZhenZhiHuiServer {
    MEETING(1011,"会议室预定",40400L,"/resource-rental/build/index.html#/home?aliasName=%E5%B9%BF%E5%B7%9E%E6%80%A1%E5%9F%8E%E7%89%A9%E4%B8%9A%E7%AE%A1%E7%90%86%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8&status=3&itemLocation=/home/yuding&title=%E7%8B%AC%E7%AB%8B%E5%8A%9E%E5%85%AC%E5%AE%A4%E9%A2%84%E8%AE%A2&layoutName=HomeYudingLayout"),
    PARKING(1042,"停车缴费",40800L,"/pp/b/a.html#home"),
    HOTLINE(1013,"服务热线",40300L,""),
    SERVICE_ALLIANCE(1014,"服务联盟",40500L,"/service-alliance-web/build/index.html#/home/grid"),
    PARK_INTRODUCTION(1015,"园区介绍",40500L,"/service-alliance-web/build/index.html#/home/grid"),
    ASSET_MANAGEMENT(1016,"资产管理",38000L,""),
    ENTERPRISES(1017,"企业名录",40500L,""),
    PARK_INVESTMENT(1018,"园区招商",40100L,""),
    PROPERTY_WARRANTY(1019,"物业报修",20100L,"/property-repair-web/build/index.html?type=user&displayName=%E8%AE%BE%E5%A4%87%E6%8A%A5%E4%BF%AE&aliasName=%E8%B6%8A%E7%A9%BA%E9%97%B4&title=%E8%AE%BE%E5%A4%87%E6%8A%A5%E4%BF%AE#/home"),
    COMPLAINT_PROPOSAL(1020,"投诉建议",20100L,"/property-repair-web/build/index.html?type=user&displayName=%E8%AE%BE%E5%A4%87%E6%8A%A5%E4%BF%AE&aliasName=%E8%B6%8A%E7%A9%BA%E9%97%B4&title=%E8%AE%BE%E5%A4%87%E6%8A%A5%E4%BF%AE#/home"),
    ENTRANCE_GUARD(1021,"公共门禁",41010L,"/entrance-guard/build/index.html#/keys"),
    BANNER(1022,"广告banner",10400L,""),
    NOTICE_MANAGE(1023,"公告管理",10300L,""),
    ACTIVITY(1024,"官方活动",10600L,"/activity/build/index.html#/listPage"),
    NEWS(1025,"园区快讯",10800L,"/park-news-web/build/index.html#/newsList"),
    MAIL_LIST(1026,"通讯录",50100L,""),
    CARD_ATTENDANCE(1027,"打卡考勤",50600L,""),
    TASKS(1028,"任务管理",13000L,"/workflow/build/index.html#/list/management"),
    PARTY(1029,"园区党建",10110L,"/forum/build/index.html#/"),
    DYNAMIC(1030,"党建动态",40500L,"/service-alliance-web/build/index.html#/home/grid"),
    NOTICE(1031,"党建公告",40500L,"/service-alliance-web/build/index.html#/home/grid"),
    DONE(1032,"两学一做",40500L,"/service-alliance-web/build/index.html#/home/grid"),
    MERCHANT(1050,"商家名录",40500L,"/service-alliance-web/build/index.html#/home/list"),
    RESIDENCE(1052,"迁入办理",40500L,"/service-alliance-web/build/index.html#/home/list"),
    DECORATION(1053,"装修办理",40500L,"/service-alliance-web/build/index.html#/home/list"),
    BROADBAND(1054,"宽带申请",40500L,"/service-alliance-web/build/index.html#/home/list"),
    ENTREPRENEURIAL(1070,"创业入驻",40500L,"/service-alliance-web/build/index.html#/home/grid");

    private Integer code;
    private String name;
    private Long module;
    private String url;
    private ZhenZhiHuiServer(Integer code, String name, Long module, String url){
        this.code = code;
        this.name = name;
        this.module = module;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public static ZhenZhiHuiServer fromStatus(Integer code) {
        for(ZhenZhiHuiServer v : ZhenZhiHuiServer.values()) {
            if(v.getCode().equals(code))
                return v;
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
