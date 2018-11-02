package com.everhomes.questionnaire;

import com.everhomes.util.StringHelper;

public class QuestionnaireScope {
    private String userId;
    private String orgId;

    public QuestionnaireScope() {
        this.userId = "";
        this.orgId = "";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
