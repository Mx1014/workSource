// @formatter:off
package com.everhomes.rest.visitorsys;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/5/7 13:40
 */
public class VisitorsysErrorCode {
    //eh_configurations 表配置
    public final static String VISITORSYS_RANDOMCODE_LENGTH = "visitorsys.RandomCode.length";//项目标识码长度，默认4
    public final static String VISITORSYS_SERIALCODE_LENGTH = "visitorsys.serialcode.length";//流水码码长度，默认4
    public final static String VISITORSYS_INVITATION_LINK = "visitorsys.invitation.link";//访客管理邀请函地址模板 www.core.zuolin/visitorsys/invitation?id=dslfksdkfjdslfdsjlfasdjfl

    //短信字段
    public final static String SMS_MODLUENAME = "modlueName";
    public final static String SMS_VERIFICATIONCODE = "verificationCode";
    public final static String SMS_APPNAME = "appName";
    public final static String SMS_VISITENTERPRISENAME = "visitEnterpriseName";
    public final static String SMS_INVITATIONLINK = "invitationLink";

    //异常码
    public final static String SCOPE = "visitorsys";
    public final static int ERROR_MUST_FILL = 180507001;  //必填项


}
