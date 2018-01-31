package com.everhomes.rest.notice;

/**
 * <ul>
 * <li>FILE("file"): 文件</li>
 * </ul>
 */
public enum EnterpriseNoticeAttachmentContentType {
    FILE("file");

    private String code;

    private EnterpriseNoticeAttachmentContentType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static EnterpriseNoticeAttachmentContentType fromCode(String code) {
        if (code != null) {
            EnterpriseNoticeAttachmentContentType[] values = EnterpriseNoticeAttachmentContentType.values();
            for (EnterpriseNoticeAttachmentContentType value : values) {
                if (code.equals(value.code)) {
                    return value;
                }
            }
        }

        return null;
    }
}
