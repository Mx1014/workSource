// @formatter:off
package com.everhomes.rest.launchpad;

/**
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
 * <li>GUILD(38): 行业协会</li>
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
 * 如果要使用路由uri的形式，请移步{@link com.everhomes.rest.common.Router}
 * <li>GENERAL_APPROVAL(65): 审批(企业申请审批)</li>
 * <li>SWITCH_SCENE(66): 切换场景</li>
 * <li>COMMUNITY_MAP((byte)67): 园区地图</li>
 * <li>WORK_REPORT((byte)68): 工作汇报</li>
 * <li>FILE_MANAGEMENT((byte)69): 文档管理</li>
 * <li>,NOTICE((byte)70): 公告管理</li>
 * <li>INCUBATORAPPLY((byte)71): 入孵申请</li>
 * <li>FIXED_ASSET((byte) 72): 固定资产管理</li>
 * <li>CALENDAR_REMIND((byte) 73):  日程</li>
 * <li>PAYSLIP((byte) 74):  薪酬-工资条</li>
 * <li>MEETING_OA((byte)75): 企业会议室管理</li>
 * <li>PUBLIC_ACLINK((byte)78):公共门禁</li>
 * <li>ENTERPRISE_ACLINK((byte)79):企业门禁</li>
 * </ul>
 */
public enum ActionType {

      // 如果要使用路由uri的形式，请移步{@link com.everhomes.rest.common.Router}

      NONE((byte)0),MORE_BUTTON((byte)1),NAVIGATION((byte)2),FAMILY_DETAILS((byte)3),GROUP_DETAILS((byte)4),
      WIN_COUPON((byte)5),USE_COUPON((byte)6),BIZ_DETAILS((byte)7),DOWNLOAD_APP((byte)8),POST_DETAILS((byte)9),
      CHECKIN_ACTIVITY((byte)10),OPEN_MSG_SESSION((byte)11),SEND_MSG((byte)12),OFFICIAL_URL((byte)13),
      THIRDPART_URL((byte)14),POST_BY_CATEGORY((byte)15),QRCODE_SCAN((byte)16),PHONE_CALL((byte)17),LAUNCH_APP((byte)18),
      POST_NEW((byte)19),PM_DETAILS((byte)20),OPEN_DOOR((byte)21),PAY((byte)22),PUNCH((byte)23),MEETINGROOM((byte)24),VIPPARKING((byte)25),
      ELECSCREEN((byte)26) ,VIDEO_MEETING((byte)27),ENTER_PARK((byte)28),EXCHANGE_HALL((byte)29),PARKING_RECHARGE((byte)30),TOPIC_BY_FORUM((byte)31),
      MAKERZONE((byte)32),SERVICEALLIANCE((byte)33),PARKENTERPRISE((byte)34), USER_GROUPS((byte)35), LIST_GROUPS((byte)36), SERVICE_ORDER((byte)37),
      GUILD((byte)38),ORG_TASK_MANAGERMENT((byte)39),ACLINK((byte)40),NEARBY_ACTIVITIES((byte)41),NEARBY_PUBLIC_CYCLE((byte)42),
      NOTICE_MANAGERMENT((byte)43),OFFLINE_WEBAPP((byte)44),SERVICE_HOT_LINE((byte)45),CONTACTS((byte)46),
      WIFI((byte)47),NEWS((byte)48),RENTAL((byte)49),OFFICIAL_ACTIVITY((byte)50),PM_TASK((byte)51),AUTH((byte)52),
      ALL_BUTTON((byte)53),MY_APPROVAL((byte) 54),NEWS_FLASH((byte) 55), FLOW_TASKS( (byte)56 ), PARKING_CLEARANCE((byte) 57), PARKING_CLEARANCE_TASK((byte) 58),
      CREATE_PMTASK((byte) 59),ROUTER((byte) 60), ACTIVITY((byte)61), POST_LIST((byte)62), ACLINK_REMOTE_OPEN((byte)63),
      ACTIVITY_DETAIL((byte)64),GENERAL_APPROVAL((byte)65), ACTIVITY_ENROLL_DETAIL((byte)65),SWITCH_SCENE((byte)66),
    COMMUNITY_MAP((byte)67),WORK_REPORT((byte)68),FILE_MANAGEMENT((byte)69),NOTICE((byte)70), INCUBATORAPPLY((byte) 71), FIXED_ASSET((byte) 72), CALENDAR_REMIND((byte) 73),
    PAYSLIP((byte) 74), MEETING_OA((byte) 75), PARK_BUS((byte) 77),ANNOUNCEMENT_DETAIL((byte)78);


    private byte code;
    private String url;

    private ActionType(byte code) {
        this.code = code;
    }

    private ActionType(byte code, String url) {
    	this.code = code;
    	this.url = url;
    }

    public byte getCode() {
        return this.code;
    }

    public String getUrl() {
		return this.url;
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
