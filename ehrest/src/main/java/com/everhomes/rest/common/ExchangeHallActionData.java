package com.everhomes.rest.common;

import java.io.Serializable;




import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为MAKERZONE(32)，创客空间
 * <li>categoryId: 活动对应的帖子类型</li>
 * <li>tag：活动标签</li>
 * </ul>
 */
public class ExchangeHallActionData implements Serializable{
    private static final long serialVersionUID = 5179532585528826878L;
    private Long categoryId;
    private String tag;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
