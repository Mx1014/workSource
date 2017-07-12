package com.everhomes.rest.parking;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */
public class GetParkingCardsResponse {

    @ItemType(ParkingCardDTO.class)
    private List<ParkingCardDTO> cards;

    private Byte toastType;

    public List<ParkingCardDTO> getCards() {
        return cards;
    }

    public void setCards(List<ParkingCardDTO> cards) {
        this.cards = cards;
    }

    public Byte getToastType() {
        return toastType;
    }

    public void setToastType(Byte toastType) {
        this.toastType = toastType;
    }
}
