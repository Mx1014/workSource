// @formatter:off
package com.everhomes.rest.wx;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>code: 微信传过来的code</li>
 *     <li>namespaceId: 域空间id</li>
 * </ul>
 */
public class WxAuthCallBackCommand {

    @NotNull
    private String code;

    @NotNull
    private Integer namespaceId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
