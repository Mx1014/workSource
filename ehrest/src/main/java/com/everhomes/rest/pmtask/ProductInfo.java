package com.everhomes.rest.pmtask;


/**
 * <ul>
 *  <li>productName: 产品名称</li>
 *  <li>productAmount: 数量</li>
 *  <li>productPrice: 单价</li>
 * </ul>
 */
public class ProductInfo {

    private String productName;
    private Integer productAmount;
    private Long productPrice;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(Integer productAmount) {
        this.productAmount = productAmount;
    }

    public Long getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Long productPrice) {
        this.productPrice = productPrice;
    }
}
