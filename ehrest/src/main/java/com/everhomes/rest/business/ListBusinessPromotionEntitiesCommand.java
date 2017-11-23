// @formatter:off
package com.everhomes.rest.business;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>pageSize: 显示数量</li>
 *     <li>pageAnchor: 下页锚点</li>
 *     <li>recommendType: 推荐类型, 配置在layout里的itemGroup里的instanceConfig</li>
 * </ul>
 */
public class ListBusinessPromotionEntitiesCommand {

    private Integer pageSize;
    private Long pageAnchor;
    private Byte recommendType;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Byte getRecommendType() {
        return recommendType;
    }

    public void setRecommendType(Byte recommendType) {
        this.recommendType = recommendType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
