package com.everhomes.rest.enterprise;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>enterpriseId：企业id</li>
 * <li>userId: 被拒绝加入企业通讯录的用户ID</li>
 * <li>rejectText: 拒绝用户时填写的说明文本</li>
 * </ul>
 */
public class RejectContactCommand {
    private Long enterpriseId;
    
    private Long userId;
    
    private String rejectText;

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRejectText() {
        return rejectText;
    }

    public void setRejectText(String rejectText) {
        this.rejectText = rejectText;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
