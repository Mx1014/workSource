// @formatter:off
package com.everhomes.rest.business;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 *     <li>id: 商品id</li>
 *     <li>posterUrl: 封面图url</li>
 *     <li>subject: 标题</li>
 *     <li>description: 内容</li>
 *     <li>commodityUrl: 商品跳转的链接</li>
 *     <li>price: 商品价格</li>
 * </ul>
 */
public class BusinessPromotionEntityDTO {

    private Long id;
    private String posterUrl;
    private String subject;
    private String description;
    private String commodityUrl;
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
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

    public String getCommodityUrl() {
        return commodityUrl;
    }

    public void setCommodityUrl(String commodityUrl) {
        this.commodityUrl = commodityUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
