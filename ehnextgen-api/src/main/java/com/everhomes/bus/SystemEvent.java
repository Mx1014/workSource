package com.everhomes.bus;

/**
 * <ul>
 *     <li>BIZ_ORDER_CREATE_DEFAULT("biz.order_create.default"): 创建订单</li>
 *     <li>BIZ_ORDER_CANCEL_DEFAULT("biz.order_cancel.default"): 取消订单</li>
 *     <li>BIZ_ORDER_PAY_COMPLETE_DEFAULT("biz.order_pay_complete.default"): 支付完成</li>
 *     <li>BIZ_ORDER_RETURN_DEFAULT("biz.order_return.default"): 退货</li>
 * </ul>
 */
public enum SystemEvent {

    // 电商
    BIZ_ORDER_CREATE("biz.order_create"),
    BIZ_ORDER_CANCEL("biz.order_cancel"),
    BIZ_ORDER_PAY_COMPLETE("biz.order_pay_complete"),
    BIZ_ORDER_RETURN("biz.order_return"),

    // 账户
    ACCOUNT_REGISTER_SUCCESS("account.register_success"),
    ACCOUNT_AUTH_SUCCESS("account.auth_success"),
    ACCOUNT__FAMILY_AUTH_SUCCESS("account.family.auth_success"),
    ACCOUNT_COMPLETE_INFO("account.complete_info"),
    ACCOUNT_OPEN_APP("account.open_app"),
    ACCOUNT_LEAVE_ENTERPRISE("account.leave.enterprise"),//离开企业
    ACCOUNT_LEAVE_FAMILY("account.leave.family"),//离开地址

    // 论坛
    FORUM_POST_CREATE("forum.post_create"),
    FORUM_POST_DELETE("forum.post_delete"),
    FORUM_POST_LIKE("forum.post_like"),
    FORUM_POST_LIKE_CANCEL("forum.post_like_cancel"),
    FORUM_POST_SHARE("forum.post_share"),
    FORUM_POST_VOTE("forum.post_vote"),
    FORUM_POST_REPORT("forum.post_report"),

    FORUM_COMMENT_CREATE("forum.comment_create"),
    FORUM_COMMENT_DELETE("forum.comment_delete"),
    // FORUM_COMMENT_REPORT("forum.comment_report"),

    // 活动
    ACTIVITY_ACTIVITY_CREATE("activity.activity_create"),
    ACTIVITY_ACTIVITY_ROSTER_CREATE("activity.activity.roster_create"),
    ACTIVITY_ACTIVITY_ENTER("activity.activity_enter"),
    ACTIVITY_ACTIVITY_DELETE("activity.activity_delete"),
    ACTIVITY_ACTIVITY_ENTER_CANCEL("activity.activity_enter_cancel"),
    ACTIVITY_ACTIVITY_SHARE("activity.activity_share"),
    ACTIVITY_ACTIVITY_LIKE("activity.activity_like"),
    ACTIVITY_ACTIVITY_LIKE_CANCEL("activity.activity_like_cancel"),
    ACTIVITY_ACTIVITY_REPORT("activity.activity_report"),

    ACTIVITY_COMMENT_CREATE("activity.comment_create"),
    ACTIVITY_COMMENT_DELETE("activity.comment_delete"),
    // ACTIVITY_COMMENT_REPORT("activity.comment_report"),

    // 意见反馈
    FEEDBACK_FEEDBACK_CREATE("feedback.feedback_create"),
    FEEDBACK_FEEDBACK_DELETE("feedback.feedback_delete"),
    FEEDBACK_FEEDBACK_SHARE("feedback.feedback_share"),
    FEEDBACK_FEEDBACK_LIKE("feedback.feedback_like"),
    FEEDBACK_FEEDBACK_LIKE_CANCEL("feedback.feedback_like_cancel"),
    FEEDBACK_FEEDBACK_REPORT("feedback.feedback_report"),

    FEEDBACK_COMMENT_CREATE("feedback.comment_create"),
    FEEDBACK_COMMENT_DELETE("feedback.comment_delete"),
    FEEDBACK_COMMENT_REPORT("feedback.comment_report"),

    // group
    GROUP_GROUP_CREATE("group.group_create"),
    GROUP_GROUP_APPROVAL("group.group_approval"),
    GROUP_GROUP_DELETE("group.group_delete"),
    GROUP_GROUP_JOIN("group.group_join"),
    GROUP_GROUP_JOIN_APPROVAL("group.group_join_approval"),
    GROUP_GROUP_LEAVE("group.group_leave"),

    // 俱乐部
    CLUB_CLUB_CREATE("club.club_create"),
    CLUB_CLUB_RELEASE("club.club_release"),
    CLUB_CLUB_JOIN("club.club_join"),
    CLUB_CLUB_LEAVE("club.club_leave"),

    // 资源预约
    RENTAL_RESOURCE_APPLY("rental.resource_apply"),
    RENTAL_RESOURCE_APPLY_CANCEL("rental.resource_apply_cancel"),

   //服务联盟
    SERVICE_ALLIANCE_CREATE("service.alliance_create");

    private String code;

    SystemEvent(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String suffix(Object... suffix) {
        if (suffix == null) {
            return dft();
        }
        StringBuilder eventName = new StringBuilder(this.code);
        for (Object o : suffix) {
            if (o == null) {
                continue;
            }
            eventName.append(".").append(o.toString());
        }
        return eventName.toString();
    }

    public String dft() {
        return this.code + ".default";
    }

    public static SystemEvent fromCode(String code) {
        if (code != null) {
            for (SystemEvent type : SystemEvent.values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
        }
        return null;
    }
}
