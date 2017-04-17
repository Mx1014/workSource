// @formatter:off
package com.everhomes.rest.launchpad;

import com.everhomes.rest.common.*;

/**
 *
 * <ul>参数类型
 * <li>NONE(0): 无</li>
 * <li>BIZ(1): 更多按钮</li>
 * <li>NAVIGATION(2): 跳下一层</li>
 * <li>THIRDPART_URL(14): 第三方url</li>
 * <li>OPEN_DOOR(21): 门禁</li>
 * <li>PUNCH(23): 打卡</li>
 * <li>MEETINGROOM(24): 会议室预定</li>
 * <li>VIPPARKING(25): vip停车位预定</li>
 * <li>ELECSCREEN(26): 电子屏预定</li>
 * <li>VIDEO_MEETING(27): 视频会议</li>
 * <li>ENTER_PARK(28): 园区入驻</li>
 * <li>EXCHANGE_HALL(29): 交流大厅</li>
 * <li>PARKING_RECHARGE(30): 停车充值</li>
 * <li>MAKERZONE(32): 创客空间  </li>
 * <li>SERVICEALLIANCE(33): 服务联盟 </li> 
 * <li>PARKENTERPRISE(34): 园区企业</li> 
 * <li>PARKENTERPRISE(36): 俱乐部</li> 
 * <li>ORG_TASK_MANAGERMENT(39): 任务管理</li> 
 * <li>NOTICE_MANAGERMENT(43): 公告管理</li> 
 * <li>MANAGER_TASK(39): 任务管理</li> 
 * <li>ACLINK(40): 门禁</li> 
 * <li>NEARBY_ACTIVITIES(41): 周边活动</li> 
 * <li>NEARBY_PUBLIC_CYCLE(42): 周边俱乐部</li> 
 * <li>OFFLINE_WEBAPP(44): 离线web应用</li>
 * <li>SERVICE_HOT_LINE(45): 园区服务热线</li>
 * <li>CONTACTS(46): 通讯录</li>
 * <li>WIFI(47): 储能wifi上网</li>
 * <li>NEWS(48): 新闻</li>
 * <li>RENTAL(49): 预订2.0</li>
 * <li>OFFICIAL_ACTIVITY(50): 官方活动</li>
 * <li>PM_TASK(51): 物业报修</li>
 * <li>MY_APPROVAL(54): 我的审批</li>
 * <li>NEWS_FLASH(55): 园区快讯新样式</li>
 * <li>FLOW_TASKS(56): 任务管理</li>
 * <li>PARKING_CLEARANCE(57): 车辆放行</li>
 * <li>PARKING_CLEARANCE_TASK(58): 车辆放行任务</li>
 * <li>CREATE_PMTASK(59): 物业报修（用户版）</li>
 * <li>ROUTER(60): 路由协议跳转</li>
 * <li>ACTIVITY(61): 活动通用配置</li>
 * <li>POST_LIST(62): 帖子列表（无参数）</li>
 * <li>ACLINK_REMOTE_OPEN(63): 门禁远程开门</li>
 * <li>ACTIVITY_DETAIL(64): 活动详情</li>
 * </ul>
 */
public enum ActionType {

    // 如果要使用url路由
    // 可以使用 {@link com.everhomes.util.Action2Router}
    // 工具从actionType+actionData转成url路由形式返回给客户端即可  add by xq.tian 2017/04/21

    NONE((byte) 0, "zl://action/none", NoParamActionData.class),
    MORE_BUTTON((byte) 1, "zl://launcher/app-store/more", MoreActionData.class),
    NAVIGATION((byte) 2, "zl://launcher/navigation", NavigationActionData.class),
    FAMILY_DETAILS((byte) 3, "zl://family/d", FamilyDetailActionData.class),
    GROUP_DETAILS((byte) 4, "zl://group/d", GroupDetailActionData.class),

    WIN_COUPON((byte) 5),
    USE_COUPON((byte) 6),
    BIZ_DETAILS((byte) 7, "zl://browser/i", OfficialActionData.class),
    DOWNLOAD_APP((byte) 8, "zl://action/download", DownAppActionData.class),
    POST_DETAILS((byte) 9, "zl://post/d", PostDetailActionData.class),

    CHECKIN_ACTIVITY((byte) 10, "zl://activity/check-in", CheckInActivityActionData.class),
    OPEN_MSG_SESSION((byte) 11, "zl://message/open-session", OpenMsgSessionActionData.class),
    SEND_MSG((byte) 12, "zl://message/send", SendMsgActionData.class),

    OFFICIAL_URL((byte) 13, "zl://browser/i", OfficialActionData.class),
    THIRDPART_URL((byte) 14, "zl://browser/i", ThirdPartActionData.class),

    POST_BY_CATEGORY((byte) 15, "zl://post/list-by-category", PostByCategoryActionData.class),
    QRCODE_SCAN((byte) 16, "zl://native/scan", NoParamActionData.class),
    PHONE_CALL((byte) 17, "zl://action/dial", PhoneCallActionData.class),
    LAUNCH_APP((byte) 18, "zl://action/launch-intent", ApplaunchAppActionData.class),

    POST_NEW((byte) 19, "zl://post/new", PostNewActionData.class),
    PM_DETAILS((byte) 20),
    OPEN_DOOR((byte) 21),
    PAY((byte) 22),

    PUNCH((byte) 23, "zl://attendance/punch", NoParamActionData.class),
    MEETINGROOM((byte) 24),
    VIPPARKING((byte) 25),

    ELECSCREEN((byte) 26),
    VIDEO_MEETING((byte) 27, "zl://video-conference/main", NoParamActionData.class),
    ENTER_PARK((byte) 28, "zl://park-service/settle", SettleActionData.class),
    EXCHANGE_HALL((byte) 29, "zl://park-service/exchange-hall", ExchangeHallActionData.class),
    PARKING_RECHARGE((byte) 30, "zl://park-service/parking-recharge", NoParamActionData.class),
    TOPIC_BY_FORUM((byte) 31),

    MAKERZONE((byte) 32, "zl://park-service/maker-zone", HackerStudioActionData.class),
    SERVICEALLIANCE((byte) 33, "zl://yellow-page/main", ServiceAllianceActionData.class),
    PARKENTERPRISE((byte) 34, "zl://park-service/enterprises", NoParamActionData.class),
    USER_GROUPS((byte) 35, "zl://group/list", UserGroupActionData.class),
    LIST_GROUPS((byte) 36, "zl://group/list-all-public", NoParamActionData.class),
    SERVICE_ORDER((byte) 37),

    ORG_TASK_MANAGERMENT((byte) 39),
    ACLINK((byte) 40, "zl://access-control/main", AccessControlActionData.class),
    NEARBY_ACTIVITIES((byte) 41, "zl://activity/list-nearby", ActivityActionData.class),
    NEARBY_PUBLIC_CYCLE((byte) 42),

    NOTICE_MANAGERMENT((byte) 43, "zl://bulletin/own-by-org", NoParamActionData.class),
    OFFLINE_WEBAPP((byte) 44, "zl://browser/nar", OfflineWebAppActionData.class),
    SERVICE_HOT_LINE((byte) 45, "zl://park-service/hot-line", NoParamActionData.class),
    CONTACTS((byte) 46, "zl://enterprise/contact", NoParamActionData.class),

    WIFI((byte) 47, "zl://park-service/wifi", NoParamActionData.class),
    NEWS((byte) 48, "zl://park-service/news-feed", NewsActionData.class),
    RENTAL((byte) 49, "zl://resource-reservation/list", RentalActionData.class),
    OFFICIAL_ACTIVITY((byte) 50, "zl://activity/list-official", OfficialActivityActionData.class),
    PM_TASK((byte) 51),
    AUTH((byte) 52, "zl://access/auth", NoParamActionData.class),

    ALL_BUTTON((byte) 53, "zl://launcher/app-store", MoreActionData.class),
    MY_APPROVAL((byte) 54, "zl://approval/mine", NoParamActionData.class),
    NEWS_FLASH((byte) 55, "zl://park-service/newsflash", NewsActionData.class),
    FLOW_TASKS((byte) 56, "zl://workflow/tasks", NoParamActionData.class),

    PARKING_CLEARANCE((byte) 57, "zl://park-service/vehicle-release", NoParamActionData.class),
    PARKING_CLEARANCE_TASK((byte) 58),

    CREATE_PMTASK((byte) 59),
    ROUTER((byte) 60),
    ACTIVITY((byte) 61, "zl://activity/list-by-tags", ActivityActionData.class),
    POST_LIST((byte) 62),
    ACLINK_REMOTE_OPEN((byte) 63, "zl://access-control/remote", AccessControlActionData.class),

    ACTIVITY_DETAIL((byte) 64, "zl://activity/d", ActivityDetailActionData.class),

    FLOW_CASE_DETAIL((byte) 65, "zl://workflow/detail", FlowCaseDetailActionData.class),

    ENTERPRISE_MEMBER_APPLY((byte) 66, "zl://enterprise/member-apply", MemberApplyActionData.class),

    GROUP_MEMBER_APPLY((byte) 67, "zl://group/member-apply", MemberApplyActionData.class),
    GROUP_MEMBER_INVITE((byte) 68, "zl://group/invite-apply", MemberApplyActionData.class),
    GROUP_MANAGER_APPLY((byte) 69, "zl://group/manager-apply", MemberApplyActionData.class),

    FAMILY_MEMBER_APPLY((byte) 70, "zl://family/member-apply", MemberApplyActionData.class),
    ;

    private byte code;
    private String router;
    private Class<?> clz;

    ActionType(byte code) {
        this.code = code;
    }

    // ActionType(byte code, String router) {
    //     this.code = code;
    //     this.router = router;
    // }

    ActionType(byte code, String router, Class<?> clz) {
        this.code = code;
        this.router = router;
        this.clz = clz;
    }

    public byte getCode() {
        return this.code;
    }

    public String getRouter() {
        return router;
    }

    public Class<?> getClz() {
        return clz;
    }

    public static ActionType fromCode(Byte code) {
        if (code == null) return null;
        ActionType[] values = ActionType.values();
        for (ActionType value : values) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }
}
