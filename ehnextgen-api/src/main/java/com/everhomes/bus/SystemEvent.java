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
    ACCOUNT_COMPLETE_INFO("account.complete_info"),

    // 论坛
    FORM_POST_CREATE("form.post_create"),
    FORM_POST_DELETE("form.post_delete"),
    FORM_POST_LIKE("form.post_like"),
    FORM_POST_LIKE_CANCEL("form.post_like_cancel"),
    FORM_POST_SHARE("form.post_share"),
    FORM_POST_VOTE("form.post_vote"),
    FORM_POST_REPORT("form.post_report"),

    FORM_COMMENT_CREATE("form.comment_create"),
    FORM_COMMENT_DELETE("form.comment_delete"),
    FORM_COMMENT_REPORT("form.comment_report"),

    // 活动
    ACTIVITY_ACTIVITY_CREATE("activity.activity_create"),
    ACTIVITY_ACTIVITY_ENTER("activity.activity_enter"),
    ACTIVITY_ACTIVITY_DELETE("activity.activity_delete"),
    ACTIVITY_ACTIVITY_ENTER_CANCEL("activity.activity_enter_cancel"),
    ACTIVITY_ACTIVITY_SHARE("activity.activity_share"),
    ACTIVITY_ACTIVITY_LIKE("activity.activity_like"),
    ACTIVITY_ACTIVITY_LIKE_CANCEL("activity.activity_like_cancel"),
    ACTIVITY_ACTIVITY_REPORT("activity.activity_report"),

    ACTIVITY_COMMENT_CREATE("activity.comment_create"),
    ACTIVITY_COMMENT_DELETE("activity.comment_delete"),
    ACTIVITY_COMMENT_REPORT("activity.comment_report"),

    // 俱乐部
    CLUB_CLUB_CREATE("club.club_create"),
    CLUB_CLUB_RELEASE("club.club_release"),
    CLUB_CLUB_JOIN("club.club_join"),
    CLUB_CLUB_LEAVE("club.club_leave"),

    // 资源预约
    RENTAL_RESOURCE_APPLY("rental.resource_apply"),
    RENTAL_RESOURCE_APPLY_CANCEL("rental.resource_apply_cancel"),

    ;

    private String code;

    SystemEvent(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String suffix(Object suffix) {
        if (suffix == null) {
            return dft();
        }
        return this.code + "." + suffix.toString();
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
