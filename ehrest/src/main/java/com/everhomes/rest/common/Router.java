// @formatter:off
package com.everhomes.rest.common;

import com.everhomes.rest.announcement.AnnouncementDetailActionData;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.meeting.MeetingRecordDetailActionData;
import com.everhomes.rest.meeting.MeetingReservationDetailActionData;
import com.everhomes.rest.notice.EnterpriseNoticeDetailActionData;
import com.everhomes.rest.remind.SelfRemindDetailActionData;
import com.everhomes.rest.remind.TrackRemindDetailActionData;
import com.everhomes.rest.techpark.punch.PunchNotificationActionData;
import com.everhomes.rest.workReport.WorkReportDetailsActionData;
import com.everhomes.rest.workReport.WorkReportIndexActionData;

/**
 * <ul>
 *     <li>native</li>
 *     <ul>
 *         <li>NATIVE_SCAN: 二维码扫描</li>
 *     </ul>
 *     <li>action</li>
 *     <ul>
 *         <li>ACTION_NONE: 无跳转</li>
 *         <li>ACTION_DIAL: 拨号</li>
 *         <li>ACTION_DOWNLOAD: 下载App</li>
 *         <li>ACTION_LAUNCH_INTENT: ??</li>
 *     </ul>
 *     <li>launcher</li>
 *     <ul>
 *         <li>LAUNCHER_APP_STORE_MORE: 更多按钮</li>
 *         <li>LAUNCHER_NAVIGATION: 跳转下一级</li>
 *         <li>LAUNCHER_APP_STORE: ??</li>
 *     </ul>
 *     <li>family</li>
 *     <ul>
 *         <li>FAMILY_DETAIL: 家庭详情</li>
 *         <li>FAMILY_MEMBER_APPLY: 申请加入家庭审批页面</li>
 *     </ul>
 *     <li>group</li>
 *     <ul>
 *         <li>GROUP_DETAIL: 圈详情</li>
 *         <li>GROUP_MEMBER_APPLY: 申请加入圈审批页面</li>
 *         <li>GROUP_INVITE_APPLY: 被邀请加入圈审批页面</li>
 *         <li>GROUP_MANAGER_APPLY: 申请成为圈管理员审批页面</li>
 *         <li>GROUP_LIST: 用户圈列表</li>
 *         <li>GROUP_LIST_ALL_PUBLIC: 所有的公有圈</li>
 *     </ul>
 *     <li>post</li>
 *     <ul>
 *         <li>POST_DETAIL: 帖子详情</li>
 *         <li>POST_NEW: 最新帖子?</li>
 *         <li>POST_LIST_BY_CATEGORY: 根据分类获取帖子列表</li>
 *     </ul>
 *     <li>activity</li>
 *     <ul>
 *         <li>ACTIVITY_CHECK_IN: 跳转到活动帖子详情并自动签到</li>
 *         <li>ACTIVITY_LIST_NEARBY: 周边活动</li>
 *         <li>ACTIVITY_LIST_OFFICIAL: 官方活动</li>
 *         <li>ACTIVITY_LIST_BY_TAGS: 周边活动，tag</li>
 *         <li>ACTIVITY_DETAIL: 活动详情</li>
 *     </ul>
 *     <li>message</li>
 *     <ul>
 *         <li>MESSAGE_OPEN_SESSION: 打开对话session</li>
 *         <li>MESSAGE_SEND: 发消息</li>
 *     </ul>
 *     <li>browser</li>
 *     <ul>
 *         <li>BROWSER_I: 内部浏览器打开连接</li>
 *         <li>BROWSER_NAR: ??</li>
 *     </ul>
 *     ...
 * </ul>
 */
public enum Router {

    /**
     * native
     */
    NATIVE_SCAN(ActionType.QRCODE_SCAN,
            "zl://native/scan", NoParamActionData.class),

    /**
     * action
     */
    ACTION_NONE(ActionType.NONE,
            "zl://action/none", NoParamActionData.class),
    ACTION_DIAL(ActionType.PHONE_CALL,
            "zl://action/dial", PhoneCallActionData.class),
    ACTION_DOWNLOAD(ActionType.DOWNLOAD_APP,
            "zl://action/download", DownAppActionData.class),
    ACTION_LAUNCH_INTENT(ActionType.LAUNCH_APP,
            "zl://action/launch-intent", ApplaunchAppActionData.class),

    /**
     * launcher
     */
    LAUNCHER_APP_STORE_MORE(ActionType.MORE_BUTTON,
            "zl://launcher/app-store/more", MoreActionData.class),
    LAUNCHER_NAVIGATION(ActionType.NAVIGATION,
            "zl://launcher/navigation", NavigationActionData.class),
    LAUNCHER_APP_STORE(ActionType.ALL_BUTTON,
            "zl://launcher/app-store", MoreActionData.class),

    /**
     * family
     */
    FAMILY_DETAIL(ActionType.FAMILY_DETAILS,
            "zl://family/d", FamilyDetailActionData.class),
    FAMILY_MEMBER_APPLY(
            "zl://family/member-apply", QuestionMetaActionData.class),

    /**
     * group
     */
    GROUP_DETAIL(ActionType.GROUP_DETAILS,
            "zl://group/d", GroupDetailActionData.class),
    GROUP_MEMBER_APPLY(
            "zl://group/member-apply", QuestionMetaActionData.class),
    GROUP_INVITE_APPLY(
            "zl://group/invite-apply", QuestionMetaActionData.class),
    GROUP_MANAGER_APPLY(
            "zl://group/manager-apply", QuestionMetaActionData.class),
    GROUP_LIST(ActionType.USER_GROUPS,
            "zl://group/list", UserGroupActionData.class),
    GROUP_LIST_ALL_PUBLIC(ActionType.LIST_GROUPS,
            "zl://group/list-all-public", NoParamActionData.class),

    /**
     * post
     */
    POST_DETAIL(ActionType.POST_DETAILS,
            "zl://post/d", PostDetailActionData.class),
    POST_NEW(ActionType.POST_NEW,
            "zl://post/new", PostNewActionData.class),
    POST_LIST_BY_CATEGORY(ActionType.POST_BY_CATEGORY,
            "zl://post/list-by-category", PostByCategoryActionData.class),

    /**
     * activity
     */
    ACTIVITY_CHECK_IN(ActionType.CHECKIN_ACTIVITY,
            "zl://activity/check-in", CheckInActivityActionData.class),
    ACTIVITY_LIST_NEARBY(ActionType.NEARBY_ACTIVITIES,
            "zl://activity/list-nearby", ActivityActionData.class),
    ACTIVITY_LIST_OFFICIAL(ActionType.OFFICIAL_ACTIVITY,
            "zl://activity/list-official", OfficialActivityActionData.class),
    ACTIVITY_LIST_BY_TAGS(ActionType.ACTIVITY,
            "zl://activity/list-by-tags", ActivityActionData.class),
    ACTIVITY_DETAIL(ActionType.ACTIVITY_DETAIL,
            "zl://activity/d", ActivityDetailActionData.class),
    ACTIVITY_ENROLL_DETAIL(
    		"zl://activity/enroll-detail", ActivityEnrollDetailActionData.class),
    
    /**
     * message
     */
    MESSAGE_OPEN_SESSION(ActionType.OPEN_MSG_SESSION,
            "zl://message/open-session", OpenMsgSessionActionData.class),
    MESSAGE_SEND(ActionType.SEND_MSG,
            "zl://message/send", SendMsgActionData.class),

    /**
     * browser
     */
    BROWSER_I(ActionType.OFFICIAL_URL,
            "zl://browser/i", OfficialActionData.class),
    BROWSER_NAR(ActionType.OFFLINE_WEBAPP,
            "zl://browser/nar", OfflineWebAppActionData.class),


    /**
     * park-service
     */
    PARK_SERVICE_SETTLE(ActionType.ENTER_PARK,
            "zl://park-service/settle", SettleActionData.class),
    PARK_SERVICE_EXCHANGE_HALL(ActionType.EXCHANGE_HALL,
            "zl://park-service/exchange-hall", ExchangeHallActionData.class),
    PARK_SERVICE_MAKER_ZONE(ActionType.MAKERZONE,
            "zl://park-service/maker-zone", HackerStudioActionData.class),
    PARK_SERVICE_ENTERPRISES(ActionType.PARKENTERPRISE,
            "zl://park-service/enterprises", NoParamActionData.class),
    PARK_SERVICE_PARKING_RECHARGE(ActionType.PARKING_RECHARGE,
            "zl://park-service/parking-recharge", NoParamActionData.class),
    PARK_SERVICE_HOT_LINE(ActionType.SERVICE_HOT_LINE,
            "zl://park-service/hot-line", NoParamActionData.class),
    PARK_SERVICE_WIFI(ActionType.WIFI,
            "zl://park-service/wifi", NoParamActionData.class),
    PARK_SERVICE_NEWS_FEED(ActionType.NEWS,
            "zl://park-service/news-feed", NewsActionData.class),
    PARK_SERVICE_NEWSFLASH(ActionType.NEWS_FLASH,
            "zl://park-service/newsflash", NewsActionData.class),
    PARK_SERVICE_VEHICLE_RELEASE(ActionType.PARKING_CLEARANCE,
            "zl://park-service/vehicle-release", NoParamActionData.class),


    /**
     * enterprise
     */
    ENTERPRISE_CONTACT(ActionType.CONTACTS,
            "zl://enterprise/contact", NoParamActionData.class),
    ENTERPRISE_MEMBER_APPLY(
            "zl://enterprise/member-apply", QuestionMetaActionData.class),

    /**
     * access-control
     */
    ACCESS_CONTROL_REMOTE(ActionType.ACLINK_REMOTE_OPEN,
            "zl://access-control/remote", AccessControlActionData.class),
    ACCESS_CONTROL_MAIN(ActionType.ACLINK,
            "zl://access-control/main", AccessControlActionData.class),

    /**
     * workflow
     */
    WORKFLOW_TASKS(ActionType.FLOW_TASKS,
            "zl://workflow/tasks", NoParamActionData.class),
    WORKFLOW_DETAIL(
            "zl://workflow/detail", FlowCaseDetailActionData.class),

    /**
     * approval
     */
    APPROVAL_MINE(ActionType.MY_APPROVAL,
            "zl://approval/mine", NoParamActionData.class),

    /**
     * yellow-page
     */
    YELLOW_PAGE_MAIN(ActionType.SERVICEALLIANCE,
            "zl://yellow-page/main", ServiceAllianceActionData.class),

    /**
     * bulletin
     */
    BULLETIN_OWN_BY_ORG(ActionType.NOTICE_MANAGERMENT,
            "zl://bulletin/own-by-org", NoParamActionData.class),

    BULLETIN_DETAIL(ActionType.ANNOUNCEMENT_DETAIL,
            "zl://bulletin/detail", AnnouncementDetailActionData.class),

    /**
     * resource-reservation
     */
    RESOURCE_RESERVATION_LIST(ActionType.RENTAL,
            "zl://resource-reservation/list", RentalActionData.class),

    /**
     * access
     */
    ACCESS_AUTH(ActionType.AUTH,
            "zl://access/auth", NoParamActionData.class),

    /**
     * attendance
     */
    ATTENDANCE_PUNCH(ActionType.PUNCH,
            "zl://attendance/index", PunchNotificationActionData.class),

    ATTENDANCE_PUNCHCLOCK_RECORD(ActionType.PUNCH,
                    "zl://attendance/punchClockRecord", PunchClockRecordData.class),
            
    /**
     * video-conference
     */
    VIDEO_CONFERENCE_MAIN(ActionType.VIDEO_MEETING,
            "zl://video-conference/main", NoParamActionData.class),

    RENTAL_ORDER_DETAIL(ActionType.RENTAL,
            "zl://resource-reservation/detail", RentalOrderActionData.class),
    /**
     * work_report_notice
     */
    WORK_REPORT_DETAILS(
            "zl://work-report/details", WorkReportDetailsActionData.class),
    WORK_REPORT_INDEX(
            "zl://work-report/index", WorkReportIndexActionData.class),

    /**
     * enterprise-notice
     */
    ENTERPRISE_NOTICE_DETAIL(
            "zl://enterprise-bulletin/detail", EnterpriseNoticeDetailActionData.class),

    /**
     * calendar-remind
     */
    SELF_CALENDAR_REMIND_DETAIL(
            "zl://remind/create", SelfRemindDetailActionData.class),

    /**
     * calendar-remind
     */
    SHARED_CALENDAR_REMIND_DETAIL(
            "zl://remind/share-detail", TrackRemindDetailActionData.class),

    MEETING_RESERVATION_DETAIL(
            "zl://meeting-reservation/meeting-detail", MeetingReservationDetailActionData.class),

    MEETING_RECORD_DETAIL(
            "zl://meeting-reservation/meeting-recordDetail", MeetingRecordDetailActionData.class);

    // 此actionType不是必须的，
    // 有这个字段只是为了将之前的actionType形式的转换成router形式时的对照
    private ActionType actionType;
    private String router;
    private Class<?> clz;

    Router(String router, Class<?> clz) {
        this.router = router;
        this.clz = clz;
    }

    Router(ActionType actionType, String router, Class<?> clz) {
        this.actionType = actionType;
        this.router = router;
        this.clz = clz;
    }

    public static Router fromActionType(ActionType actionType) {
        if (actionType != null) {
            for (Router router : Router.values()) {
                if (router.actionType == actionType) {
                    return router;
                }
            }
        }
        return null;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public String getRouter() {
        return router;
    }

    public Class getClz() {
        return clz;
    }
}
