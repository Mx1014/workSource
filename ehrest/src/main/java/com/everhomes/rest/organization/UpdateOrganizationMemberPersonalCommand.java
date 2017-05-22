package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>memberId: 员工编号</li>
 * <li>enName：英文名</li>
 * <li>birthday: 出生日期</li>
 * <li>maritalFlag: 婚姻状况，0：保密 1：已婚 2：未婚</li>
 * <li>politicalStatus: 政治面貌</li>
 * <li>nativePlace: 籍贯</li>
 * <li>regResidence: 户口</li>
 * <li>idNumber: 身份证号</li>
 * </ul>
 */

public class UpdateOrganizationMemberPersonalCommand {

    private Long memberId;

    private String enName;

    private String birthday;

    private Byte maritalFlag;

    private String politicalStatus;

    private String nativePlace;

    private String regResidence;

    private String idNumber;

    public UpdateOrganizationMemberPersonalCommand() {
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Byte getMaritalFlag() {
        return maritalFlag;
    }

    public void setMaritalFlag(Byte maritalFlag) {
        this.maritalFlag = maritalFlag;
    }

    public String getPoliticalStatus() {
        return politicalStatus;
    }

    public void setPoliticalStatus(String politicalStatus) {
        this.politicalStatus = politicalStatus;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getRegResidence() {
        return regResidence;
    }

    public void setRegResidence(String regResidence) {
        this.regResidence = regResidence;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


}
