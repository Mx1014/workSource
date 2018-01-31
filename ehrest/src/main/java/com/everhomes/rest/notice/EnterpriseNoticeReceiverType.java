package com.everhomes.rest.notice;

import org.apache.commons.lang.StringUtils;

/**
 * <p>公告接收范围类型</p>
 * <ul>
 * <li>ORGANIZATIONS("EhOrganizations") : 公告部门可见</li>
 * <li>USER("EhUsers"): 公告某员工可见</li>
 * </ul>
 */
public enum EnterpriseNoticeReceiverType {

    ORGANIZATIONS("EhOrganizations"), USER("EhUsers");

    private String code;

    private EnterpriseNoticeReceiverType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static EnterpriseNoticeReceiverType fromCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        EnterpriseNoticeReceiverType[] values = EnterpriseNoticeReceiverType.values();
        for (EnterpriseNoticeReceiverType value : values) {
            if (value.code.equals(code)) {
                return value;
            }
        }

        return null;
    }
}
