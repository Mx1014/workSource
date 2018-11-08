package com.everhomes.questionnaire;

import com.everhomes.util.StringHelper;

public class QuestionnaireScope {
    private String userId;
    private String orgId;
    private String namespaceId;

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

    public String getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(String namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
