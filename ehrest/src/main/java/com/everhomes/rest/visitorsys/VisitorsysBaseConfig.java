// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

/**
  *<ul>
 *<li>selfRegistrationFlag : (必填)有效期是否必填，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
 *<li>selfDefineWelComeFlag : (必填)车牌号码是否必填，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
 *<li>signSecrecyAgreementsFlag : (必填)证件号码是否必填，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
 *<li>photosAllowedFlag : (必填)到访楼层是否必填，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
 *<li>AllowSkippingPhotosFlag : (必填)到访门牌是否必填，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
 *<li>followUpNumbersFlag : (必填)到访门牌是否必填，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
 *<li>doorGuardsFlag : (必填)到访门牌是否必填，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
 *<li>doorGuardsValidAfterConfirmedFlag : (选填)到访门牌是否必填，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
 *<li>doorGuardId : (选填)门禁id</li>
 *<li>doorGuardName : (选填)到访门牌是否必填，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
  *</ul>
  */
public class VisitorsysBaseConfig {
    private Byte selfRegistrationFlag=1;
    private Byte selfDefineWelComeFlag=1;
    private Byte signSecrecyAgreementsFlag=0;
    private Byte photosAllowedFlag=1;
    private Byte AllowSkippingPhotosFlag=1;
    private Byte followUpNumbersFlag=1;
    private Byte doorGuardsFlag=0;
    private Byte doorGuardsValidAfterConfirmedFlag;
    private String doorGuardId;
    private String doorGuardName;

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
