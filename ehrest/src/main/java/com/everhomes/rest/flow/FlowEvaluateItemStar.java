// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>itemId: 评价项id</li>
 *     <li>stat: 分值</li>
 *     <li>content: 评价内容</li>
 * </ul>
 */
public class FlowEvaluateItemStar {

    private Long itemId;
    private Byte stat;
    private String content;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Byte getStat() {
        return stat;
    }

    public void setStat(Byte stat) {
        this.stat = stat;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
