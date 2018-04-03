package com.everhomes.rest.news;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 * 参数
 * <li>pageAnchor: 锚点</li>
 * <li>tags: 标签</li>
 * </ul>
 */
public class GetNewsTagResponse {

    private Long pageAnchor;
    @ItemType(NewsTagDTO.class)
    private List<NewsTagDTO> tags;

    public List<NewsTagDTO> getTags() {
        return tags;
    }

    public void setTags(List<NewsTagDTO> tags) {
        this.tags = tags;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }
}
