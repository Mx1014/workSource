// @formatter:off
package com.everhomes.rest.visitorsys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
  *<ul>返回码参考：
  *<li>ERROR_MUST_FILL (1001): 必填项为空</li>
  *<li>ERROR_PAIRING_TIMEOUT (408): 配对码验证超时</li>
  *<li>ERROR_DEVICE_NOT_FIND (1401): 设备没找到</li>
  *<li>ERROR_VISITOR_NOT_FIND (1402): 预约未找到</li>
  *<li>ERROR_ILLEGAL_VERIFICATIONCODE (1405): 非法验证码</li>
  *<li>ERROR_REPEAT_PHONE (1406): 重复的黑名单电话号码</li>
  *<li>ERROR_INBLACKLIST_PHONE_COMMUNITY (1407): 此手机号码已进入黑名单</li>
  *<li>ERROR_INBLACKLIST_PHONE_ENTERPRISE(1408): 此手机号码已进入黑名单</li>
  *<li>ERROR_REGISTED_IPAD(1409): 此ipad已被注册过</li>
  *</ul>
  */

public class VisitorsysConstant {
    //eh_configurations 表配置
    public final static String VISITORSYS_RANDOMCODE_LENGTH = "visitorsys.RandomCode.length";//项目标识码长度，默认4
    public final static String VISITORSYS_SERIALCODE_LENGTH = "visitorsys.serialcode.length";//流水码码长度，默认4
    public final static String VISITORSYS_PAIRINGCODE_LENGTH = "visitorsys.pairingcode.length";//配对码码长度，默认6
    public final static String VISITORSYS_INVITATION_LINK = "visitorsys.invitation.link";//访客管理邀请函地址模板 www.core.zuolin/visitorsys/invitation?id=dslfksdkfjdslfdsjlfasdjfl
    public final static String VISITORSYS_SELFREGISTER_LINK = "visitorsys.selfregister.link";//访客管理自助登记上下文 %s/selfregister?token=%s
    public static final String VISITORSYS_PAIRINGCODE_LIVE= "visitorsys.pairingcode.live";//配对码有效时长,默认60秒
    public static final String VISITORSYS_VERIFICATIONCODE_LIVE= "visitorsys.verificationcode.live";//验证码对码有效时长,默认900秒
    public final static String VISITORSYS_VERIFICATIONCODE_LENGTH = "visitorsys.verificationcode.length";//验证码码长度，默认6
    public static final String VISITORSYS_MODLUENAME= "visitorsys.modluename";//模块名称
    public static final String VISITORSYS_QRCODE_HEIGHT= "visitorsys.qrcode.height";//二维码高度 300
    public static final String VISITORSYS_QRCODE_WIDTH= "visitorsys.qrcode.width";//二维码宽度 300
    public static final String VISITORSYS_IPAD_CONFIG = "visitorsys.ipad.config";//ipad左邻logo,welcome,button,version

    //短信字段
    public static final String SMS_MODLUENAME_CN= "【左邻访客】";//短信发送模块名称
    public final static String SMS_MODLUENAME = "modlueName";
    public final static String SMS_VERIFICATIONCODE = "verificationCode";
    public final static String SMS_APPNAME = "appName";
    public final static String SMS_VISITENTERPRISENAME = "visitEnterpriseName";
    public final static String SMS_INVITATIONLINK = "invitationLink";

    //redis缓存 前缀
    public final static String VISITORSYS_PAIRINGCODE_ = "visitorsys_pairingcode_";//配对码前缀
    public final static String VISITORSYS_VERIFICATIONCODE_ = "visitorsys_verificationcode_";//访客验证码前缀
    public static final String VISITORSYS_SUBJECT= "visitorsys_subject";//访客ipad配对码验证监听的主题


    //异常码
    public final static String SCOPE = "visitorsys";
    public final static int ERROR_MUST_FILL = 1001;  //必填项
    public final static int ERROR_PAIRING_TIMEOUT = 408;  //配对码验证超时
    public final static int ERROR_DEVICE_NOT_FIND = 1401;  //设备没找到
    public final static int ERROR_VISITOR_NOT_FIND = 1402;  //预约未找到
    public final static int ERROR_ILLEGAL_VERIFICATIONCODE = 1405;  //非法验证码
    public final static int ERROR_REPEAT_PHONE = 1406;  //重复的黑名单电话号码
    public final static int ERROR_INBLACKLIST_PHONE_COMMUNITY = 1407;  //此手机号码已进入黑名单
    public final static int ERROR_INBLACKLIST_PHONE_ENTERPRISE= 1408;  //此手机号码已进入黑名单
    public final static int ERROR_REGISTED_IPAD= 1409;  //此ipad已被注册过

    //常规常量
    public static final long COMMUNITY_MODULE_ID = 41800 ;//园区访客模块Id

}
