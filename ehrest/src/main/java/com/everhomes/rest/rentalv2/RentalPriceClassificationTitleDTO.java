package com.everhomes.rest.rentalv2;

import java.util.List;

public class RentalPriceClassificationTitleDTO {
    private Byte userPriceType;
    private List<RentalPriceClassificationDTO> levels;

    public Byte getUserPriceType() {
        return userPriceType;
    }

    public void setUserPriceType(Byte userPriceType) {
        this.userPriceType = userPriceType;
    }

    public List<RentalPriceClassificationDTO> getLevels() {
        return levels;
    }

    public void setLevels(List<RentalPriceClassificationDTO> levels) {
        this.levels = levels;
    }
}
