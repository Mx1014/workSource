// @formatter:off
package com.everhomes.rest.family;

public interface FamilyNotificationTemplateCode {
    static final String SCOPE = "family.notification";
    
    static final int FAMILY_JOIN_REQ_FOR_APPLICANT = 1; // 有人填写地址（地址匹配或不匹配），通知申请人等待审核
    static final int FAMILY_JOIN_REQ_FOR_OPERATOR = 2; // 有人填写地址（地址匹配），通知家庭成员审核
    static final int FAMILY_JOIN_ADMIN_APPROVE_FOR_APPLICANT = 3; // 填写的地址被管理员审核通过，通知申请人
    static final int FAMILY_JOIN_ADMIN_APPROVE_FOR_OTHER = 4; // 填写的地址被管理员审核通过，通知家庭其它成员
    static final int FAMILY_JOIN_ADMIN_CORRECT_FOR_APPLICANT = 5; // 填写的地址被管理员纠正通过，通知申请人
    static final int FAMILY_JOIN_ADMIN_CORRECT_FOR_OTHER = 6; // 填写的地址被管理员纠正通过，通知家庭其它成员
    static final int FAMILY_JOIN_MEMBER_APPROVE_FOR_APPLICANT = 7; // 填写的地址被家人审核通过，通知申请人
    static final int FAMILY_JOIN_MEMBER_APPROVE_FOR_OPERATOR = 8; // 填写的地址被家人审核通过，通知操作人
    static final int FAMILY_JOIN_MEMBER_APPROVE_FOR_OTHER = 9; // 填写的地址被家人审核通过，通知家庭其它成员
    static final int FAMILY_JOIN_ADMIN_REJECT_FOR_APPLICANT = 10; // 填写的地址被管理员拒绝，通知申请人
    static final int FAMILY_JOIN_ADMIN_REJECT_FOR_OTHER = 11; // 填写的地址被管理员拒绝，通知家庭其它成员
    static final int FAMILY_JOIN_MEMBER_REJECT_FOR_APPLICANT = 12; // 填写的地址被家人拒绝，通知申请人
    static final int FAMILY_JOIN_MEMBER_REJECT_FOR_OPERATOR = 13; // 填写的地址被家人拒绝，通知操作人
    static final int FAMILY_JOIN_MEMBER_REJECT_FOR_OTHER = 14; // 填写的地址被家人拒绝，通知家庭其它成员
    static final int FAMILY_MEMBER_LEAVE_FOR_APPLICANT = 15; // 某家庭成员主动退出家庭，通知操作人
    static final int FAMILY_MEMBER_LEAVE_FOR_OTHER = 16; // 某家庭成员主动退出家庭，通知家庭其它成员
    static final int FAMILY_MEMBER_REVOKE_FOR_APPLICANT = 17; // 某家庭成员被踢出家庭，通知被踢人
    static final int FAMILY_MEMBER_REVOKE_FOR_OPERATOR = 18; // 某家庭成员被踢出家庭，通知操作人
    static final int FAMILY_MEMBER_REVOKE_FOR_OTHER = 19; // 某家庭成员被踢出家庭，通知家庭其它成员
    static final int FAMILY_MEMBER_QUICK_APPLICANT = 20; // 地址加速审核，通知操作者
}
