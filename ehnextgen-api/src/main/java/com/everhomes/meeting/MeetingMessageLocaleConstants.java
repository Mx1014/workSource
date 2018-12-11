package com.everhomes.meeting;

public class MeetingMessageLocaleConstants {
    public static final String SCOPE = "meetingMessage";

    // 消息或邮件的标题
    public static final int MEETING_COMING_SOON_MESSAGE_TITLE = 100001; // 您的会议即将开始
    public static final int UPDATE_A_MEETING_MESSAGE_TITLE = 100002;    // 您的会议已调整
    public static final int CANCEL_A_MEETING_MESSAGE_TITLE = 100003;    // 您的会议已取消
    public static final int MEETING_INVITATION_REMOVE = 100008;    // 您已不是参会人
    public static final int MEETING_MANAGER_REMOVE = 100009;    // 您已不是会务人
    public static final int MEETING_MANAGER_ADD = 100010;    // 您已被指定为会务人
    public static final int BE_INVITED_A_MEETING_MESSAGE_TITLE = 1000000; // #发起人#邀请您参加会议 -> ${meetingSponsorName}邀请您参加会议
    public static final int RECEIVE_A_MEETING_RECORD_MESSAGE_TITLE = 1000001;    // #发起人#发布了会议纪要 -> ${meetingRecorderName}发布了会议纪要
    public static final int RECEIVE_A_MODIFY_MEETING_RECORD_MESSAGE_TITLE = 1000007;    // #发起人#修改了会议纪要 -> ${meetingRecorderName}修改了会议纪要


    // 消息或邮件的内容部分

    // 主题：#会议主题# -> 主题：${meetingSubject}/r/n  时间：#会议开始时间# -> 时间：${meetingBeginTime}/r/n  地点：#会议室名称# -> 地点：${meetingRoomName}/r/n
    public static final int MEETING_SYSTEM_MESSAGE_BODY = 1000002;
    // 主题：#会议主题# -> 主题：${meetingSubject}<br>时间：#会议开始时间# -> 时间：${meetingBeginTime}<br>地点：#会议室名称# -> 地点：${meetingRoomName}<br>
    // 发起人：#发起人姓名#  -> 发起人：${meetingSponsorName}<br>ATTENDEE：#参会人A#、#参会人B#、#参会人C# -> ATTENDEE：${meetingUserList}
    public static final int MEETING_MAIL_MESSAGE_BODY = 1000003;
    // 会议主题：#会议主题# -> 会议主题：${meetingSubject}
    public static final int MEETING_RECORD_MESSAGE_BODY = 1000004;
    // 主题：#会议主题# -> 主题：${meetingSubject}<br>时间：#会议开始时间# -> 时间：${meetingBeginTime}<br>地点：#会议室名称# -> 地点：${meetingRoomName}<br>
    // 发起人：#发起人姓名#  -> 发起人：${meetingSponsorName}<br>ATTENDEE：#参会人A#、#参会人B#、#参会人C# -> ATTENDEE：${meetingUserList}<br>详细信息请打开：#本企业APP名称# APP查看 -> 详细信息请打开：${appName} APP查看
    public static final int MEETING_MAIL_MESSAGE_WITH_APP_NAME_BODY = 1000005;
    // 管理员#姓名#删除了会议室  -> 管理员${adminContactName}删除了会议室
    public static final int MEETING_ROOM_DELETE_BY_ADMIN = 1000006;

}
