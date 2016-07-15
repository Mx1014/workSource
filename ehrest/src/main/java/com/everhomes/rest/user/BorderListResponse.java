package com.everhomes.rest.user;

import java.util.List;

import com.everhomes.discover.ItemType;

public class BorderListResponse {
    @ItemType(String.class)
    private List<String> borders;

    public List<String> getBorders() {
        return borders;
    }

    public void setBorders(List<String> borders) {
        this.borders = borders;
    }
}
