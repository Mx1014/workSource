// @formatter:off
package com.everhomes.rest.user;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

public class ListBorderAndContentResponse {
    @ItemType(String.class)
    private List<String> borders;

    private String contentUrl;

    public List<String> getBorders() {
        return borders;
    }

    public void setBorders(List<String> borders) {
        this.borders = borders;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
