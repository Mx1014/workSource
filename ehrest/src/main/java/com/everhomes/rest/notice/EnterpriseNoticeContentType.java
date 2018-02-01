// @formatter:off
package com.everhomes.rest.notice;

/**
 * <p>企业公告内容类型</p>
 * <ul>
 * <li>TEXT("text"): 文本</li>
 * </ul>
 */
public enum EnterpriseNoticeContentType {
    TEXT("text");

    private String code;

    EnterpriseNoticeContentType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static EnterpriseNoticeContentType fromCode(String code) {
        if (code == null)
            return null;
        EnterpriseNoticeContentType[] contentTypes = EnterpriseNoticeContentType.values();
        for (EnterpriseNoticeContentType contentType : contentTypes) {
            if (contentType.getCode().equals(code)) {
                return contentType;
            }
        }
        return null;
    }
}
