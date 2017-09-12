// @formatter:off
package com.everhomes.rest.sms;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>handler: handler</li>
 *     <li>mobile: mobile</li>
 *     <li>templateCode: templateCode</li>
 *     <li>variables: variables</li>
 * </ul>
 */
public class SendTestSmsCommand {

    private Integer namespaceId;
    private String handler;
    private String mobile;
    private Integer templateCode;
    private String variables;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(Integer templateCode) {
        this.templateCode = templateCode;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getVariables() {
        return variables;
    }

    public void setVariables(String variables) {
        this.variables = variables;
    }
}
