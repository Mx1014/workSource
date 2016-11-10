package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *   <li>cardType:类型</li>
 *   <li>categoryName:显示名称</li>
 * </ul>
 */
public class ParkingCardCategoryDTO {
    private Byte   cardType;
    private String categoryName;

    public Byte getCardType() {
        return cardType;
    }

    public void setCardType(Byte cardType) {
        this.cardType = cardType;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
