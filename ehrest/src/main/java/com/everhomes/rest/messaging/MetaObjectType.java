// @formatter:off
package com.everhomes.rest.messaging;

/**
 * <p>Meta对象类型</p>
 * <ul>
 * <li>GROUP_REQUEST_TO_JOIN: 用户主动请求加入group需要审核</li>
 * <li>GROUP_INVITE_TO_JOIN: 邀请用户加入gropu需要被邀请者同意/拒绝</li>
 * <li>GROUP_REQUEST_TO_BE_ADMIN: 用户主动申请成为group管理员需要审核</li>
 * <li>GROUP_INVITE_TO_BE_ADMIN: 邀请用户成为group管理员需要被邀请者同意/拒绝</li>
 * <li>ENTERPRISE_REQUEST_TO_JOIN: 主动申请加入企业需要审核</li>
 * <li>ENTERPRISE_INVITE_TO_JOIN: 邀请用户加入企业需要同意</li>
 * <li>ENTERPRISE_AGREE_TO_JOIN: 同意加入了公司</li>
 * <li>BIZ_NEW_ORDER: 有新的电商订单</li>
 * <li>MESSAGE_ROUTER: 路由跳转</li>
 * <li>FAMILY_AGREE_TO_JOIN: 同意加入了家庭</li>
 * <li>GROUP_MEMBER_DELETE: 用户被踢出</li>
 * <li>GROUP_INVITE_TO_JOIN_FREE: 被邀请进群，直接进去</li>
 * <li>GROUP_TALK_DISSOLVED: 解散群聊</li>
 * </ul>
 */
public enum MetaObjectType {
    GROUP_REQUEST_TO_JOIN("group.requestToJoin"), 
    GROUP_INVITE_TO_JOIN("group.inviteToJoin"), 
    GROUP_REQUEST_TO_BE_ADMIN("group.requestToBeAdmin"), 
    GROUP_INVITE_TO_BE_ADMIN("group.inviteToBeAdmin"),
    ENTERPRISE_REQUEST_TO_JOIN("enterprise.requestToJoin"), 
    ENTERPRISE_INVITE_TO_JOIN("enterprise.inviteToJoin"),
    ENTERPRISE_AGREE_TO_JOIN("enterprise.agreeToJoin"),
    ACLINK_AUTH_CHANGED("aclink.authChanged"),
    BIZ_NEW_ORDER("biz.new.order"),
    MESSAGE_ROUTER("message.router"),
    ENTERPRISE_LEAVE_THE_JOB("enterprise.leaveTheJob"),
    FAMILY_AGREE_TO_JOIN("family.agreeToJoin"),
    GROUP_MEMBER_DELETE("group.member.delete"),
    GROUP_INVITE_TO_JOIN_FREE("group.inviteToJoin.free"),
    GROUP_TALK_DISSOLVED("group.talk.dissolved");
    
    private String code;
    private MetaObjectType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static MetaObjectType fromCode(String code) {
        MetaObjectType[] values = MetaObjectType.values();
        for(MetaObjectType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}
