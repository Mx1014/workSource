package com.everhomes.launchpad;

import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为postAction时点击item需要的参数
 * <li>forumId: 论坛id</li>
 * <li>actionCategory: 发帖动作的大类，如物业，家政等</li>
 * <li>contentCategory: 发帖动作的小类，如投诉，建议等</li>
 * </ul>
 */
public class LaunchPadPostActionActionData {
    //{"contentCategory":3092,"actionCategory":9,"forumId":1} 
    private Long forumId;
    private Long actionCategory;
    private Long contentCategory;

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public Long getActionCategory() {
        return actionCategory;
    }

    public void setActionCategory(Long actionCategory) {
        this.actionCategory = actionCategory;
    }

    public Long getContentCategory() {
        return contentCategory;
    }

    public void setContentCategory(Long contentCategory) {
        this.contentCategory = contentCategory;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
