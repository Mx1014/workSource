package com.everhomes.rest.questionnaire;

import com.everhomes.util.StringHelper;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * 
 *  <ul>
 *  <li>identifierToken:电话号码</li>
 *  <li>id:用户id</li>
 *  <li>accountName:用户名/结果描述</li>
 *  <li>verifyStatus:检测状态,参考 {@link com.everhomes.rest.questionnaire.QuestionnaireCommonStatus}</li>
 *  </ul>
 **/
public class QuestionnaireUserDTO {

    private String identifierToken;
    private Long id;
    private String accountName;
    private String nickName;
    private String name;
    private String avatarUrl;
    private Byte gender;
    private String birthday;
    private Byte identifierType;
    private Byte verifyStatus;

    public QuestionnaireUserDTO() {
    }

    public Byte getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Byte verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }


    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Byte getIdentifierType() {
        return identifierType;
    }

    public void setIdentifierType(Byte identifierType) {
        this.identifierType = identifierType;
    }

    public String getIdentifierToken() {
        return identifierToken;
    }

    public void setIdentifierToken(String identifierToken) {
        this.identifierToken = identifierToken;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
