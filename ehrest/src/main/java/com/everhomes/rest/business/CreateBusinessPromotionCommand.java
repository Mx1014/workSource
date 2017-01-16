// @formatter:off
package com.everhomes.rest.business;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>id: id</li>
 * <li>subject: 主题</li>
 * <li>description: 描述</li>
 * <li>posterUri: 封面图</li>
 * <li>price: 价格</li>
 * <li>commodityUrl: 跳转链接</li>
 * <li>defaultOrder: 排序</li>
 * </ul>
 */
public class CreateBusinessPromotionCommand {

    private Long id;
    private String subject;
    private String description;
    private String posterUri;
    private BigDecimal price;
    private String commodityUrl;
    private Integer defaultOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosterUri() {
        return posterUri;
    }

    public void setPosterUri(String posterUri) {
        this.posterUri = posterUri;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCommodityUrl() {
        return commodityUrl;
    }

    public void setCommodityUrl(String commodityUrl) {
        this.commodityUrl = commodityUrl;
    }

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
