package com.everhomes.rest.rentalv2;

/**
 * <ul>
 * 预约需要提交的信息
 * <li>id: id</li>
 * <li>itemName: 物资名称</li>
 * </ul>
 */
public class RentalGoodItem {
    private Long id;
    private String itemName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
