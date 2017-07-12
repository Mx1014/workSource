package com.everhomes.rest.sms;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/3/27.
 */
public class SmsLogDTO {
    private Long id;
    private Integer namespaceId;
    private String scope;
    private Integer code;
    private String locale;
    private String mobile;
    private String text;
    private Long templateId;
    private String variables;
    private String result;
    private Timestamp createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getVariables() {
        return variables;
    }

    public void setVariables(String variables) {
        this.variables = variables;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("namespaceId", namespaceId)
                .append("scope", scope)
                .append("code", code)
                .append("locale", locale)
                .append("mobile", mobile)
                .append("text", text)
                .append("templateId", templateId)
                .append("variables", variables)
                .append("result", result)
                .append("createTime", createTime)
                .toString();
    }
}
