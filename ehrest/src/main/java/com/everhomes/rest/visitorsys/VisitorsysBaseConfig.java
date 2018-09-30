// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

/**
  *<ul>
 *<li>visitorQrcodeFlag : (必填)访客邀请函是否启用访客二维码，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
 *<li>visitorInfoFlag : (必填)访客邀请函是否启用访客信息，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
 *<li>trafficGuidanceFlag : (必填)访客邀请函是否启用交通指引，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
 *<li>selfRegistrationFlag : (必填)是否启用手机扫码自助登记，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
 *<li>selfDefineWelComeFlag : (必填)是否启用自定义欢迎页面信息，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
 *<li>signSecrecyAgreementsFlag : (必填)是否签署保密协议，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
 *<li>photosAllowedFlag : (必填)是否允许拍摄头像，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
 *<li>AllowSkippingPhotosFlag : (必填)是否允许跳过拍摄头像，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
 *<li>followUpNumbersFlag : (必填)是否允许输入随访人员数量，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
 *<li>doorGuardsFlag : (必填)是否对接门禁授权，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
 *<li>doorGuardsValidAfterConfirmedFlag : (选填)大堂门禁权限需要被确认到访后生效，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
 *<li>allowPassportQrcodeFlag : (选填)允许通行证显示基于访客二维码的专属信息，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
 *<li>doorGuardId : (选填)门禁id</li>
 *<li>doorGuardName : (选填)门禁名称</li>
 *
 * <li>visitorConfirmFlag : 到访确认</li>
 * <li>allowCardReader : 身份证读卡器</li>
  *</ul>
  */
public class VisitorsysBaseConfig {
    private Byte visitorQrcodeFlag;
    private Byte visitorInfoFlag;
    private Byte trafficGuidanceFlag;
    private Byte selfRegistrationFlag;
    private Byte selfDefineWelComeFlag;
    private Byte signSecrecyAgreementsFlag;
    private Byte photosAllowedFlag;
    private Byte AllowSkippingPhotosFlag;
    private Byte followUpNumbersFlag;
    private Byte doorGuardsFlag;
    private Byte doorGuardsValidAfterConfirmedFlag;
    private Byte allowPassportQrcodeFlag;
    private String doorGuardId;
    private String doorGuardName;

    private Byte visitorConfirmFlag;
    private Byte allowCardReader;

    public void generateDefaultValue(){
        visitorQrcodeFlag=1;
        visitorInfoFlag=1;
        trafficGuidanceFlag=1;
        selfRegistrationFlag=0;
        selfDefineWelComeFlag=0;
        signSecrecyAgreementsFlag=0;
        photosAllowedFlag=0;
        AllowSkippingPhotosFlag=0;
        followUpNumbersFlag=1;
        doorGuardsFlag=0;
        doorGuardsValidAfterConfirmedFlag=0;
        allowPassportQrcodeFlag = 1;
        visitorConfirmFlag = 1;
        allowCardReader = 0;
    }

    public Byte getVisitorQrcodeFlag() {
        return visitorQrcodeFlag;
    }

    public void setVisitorQrcodeFlag(Byte visitorQrcodeFlag) {
        this.visitorQrcodeFlag = visitorQrcodeFlag;
    }

    public Byte getVisitorInfoFlag() {
        return visitorInfoFlag;
    }

    public void setVisitorInfoFlag(Byte visitorInfoFlag) {
        this.visitorInfoFlag = visitorInfoFlag;
    }

    public Byte getTrafficGuidanceFlag() {
        return trafficGuidanceFlag;
    }

    public void setTrafficGuidanceFlag(Byte trafficGuidanceFlag) {
        this.trafficGuidanceFlag = trafficGuidanceFlag;
    }

    public Byte getSelfRegistrationFlag() {
        return selfRegistrationFlag;
    }

    public void setSelfRegistrationFlag(Byte selfRegistrationFlag) {
        this.selfRegistrationFlag = selfRegistrationFlag;
    }

    public Byte getSelfDefineWelComeFlag() {
        return selfDefineWelComeFlag;
    }

    public void setSelfDefineWelComeFlag(Byte selfDefineWelComeFlag) {
        this.selfDefineWelComeFlag = selfDefineWelComeFlag;
    }

    public Byte getSignSecrecyAgreementsFlag() {
        return signSecrecyAgreementsFlag;
    }

    public void setSignSecrecyAgreementsFlag(Byte signSecrecyAgreementsFlag) {
        this.signSecrecyAgreementsFlag = signSecrecyAgreementsFlag;
    }

    public Byte getPhotosAllowedFlag() {
        return photosAllowedFlag;
    }

    public void setPhotosAllowedFlag(Byte photosAllowedFlag) {
        this.photosAllowedFlag = photosAllowedFlag;
    }

    public Byte getAllowSkippingPhotosFlag() {
        return AllowSkippingPhotosFlag;
    }

    public void setAllowSkippingPhotosFlag(Byte allowSkippingPhotosFlag) {
        AllowSkippingPhotosFlag = allowSkippingPhotosFlag;
    }

    public Byte getFollowUpNumbersFlag() {
        return followUpNumbersFlag;
    }

    public void setFollowUpNumbersFlag(Byte followUpNumbersFlag) {
        this.followUpNumbersFlag = followUpNumbersFlag;
    }

    public Byte getDoorGuardsFlag() {
        return doorGuardsFlag;
    }

    public void setDoorGuardsFlag(Byte doorGuardsFlag) {
        this.doorGuardsFlag = doorGuardsFlag;
    }

    public Byte getDoorGuardsValidAfterConfirmedFlag() {
        return doorGuardsValidAfterConfirmedFlag;
    }

    public void setDoorGuardsValidAfterConfirmedFlag(Byte doorGuardsValidAfterConfirmedFlag) {
        this.doorGuardsValidAfterConfirmedFlag = doorGuardsValidAfterConfirmedFlag;
    }

    public String getDoorGuardId() {
        return doorGuardId;
    }

    public void setDoorGuardId(String doorGuardId) {
        this.doorGuardId = doorGuardId;
    }

    public String getDoorGuardName() {
        return doorGuardName;
    }

    public void setDoorGuardName(String doorGuardName) {
        this.doorGuardName = doorGuardName;
    }

    public Byte getAllowPassportQrcodeFlag() {
        return allowPassportQrcodeFlag;
    }

    public void setAllowPassportQrcodeFlag(Byte allowPassportQrcodeFlag) {
        this.allowPassportQrcodeFlag = allowPassportQrcodeFlag;
    }

    public Byte getVisitorConfirmFlag() {
        return visitorConfirmFlag;
    }

    public void setVisitorConfirmFlag(Byte visitorConfirmFlag) {
        this.visitorConfirmFlag = visitorConfirmFlag;
    }

    public Byte getAllowCardReader() {
        return allowCardReader;
    }

    public void setAllowCardReader(Byte allowCardReader) {
        this.allowCardReader = allowCardReader;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
