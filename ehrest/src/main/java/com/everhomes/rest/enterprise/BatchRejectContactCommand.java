package com.everhomes.rest.enterprise;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>enterpriseId：企业id</li>
 * <li>userIds: 被拒绝加入企业通讯录的用户ID list</li>
 * <li>rejectText: 拒绝用户时填写的说明文本</li>
 * </ul>
 */
public class BatchRejectContactCommand {
    private Long enterpriseId;

    @ItemType(Long.class)
    private List<Long> userIds;
    
    private String rejectText;

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
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

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}
}
