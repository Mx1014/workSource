// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.favorite;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.favorite.FavoriteDTO;

public class ListFavoritesRestResponse extends RestResponseBase {

    private List<FavoriteDTO> response;

    public ListFavoritesRestResponse () {
    }

    public List<FavoriteDTO> getResponse() {
        return response;
    }

    public void setResponse(List<FavoriteDTO> response) {
        this.response = response;
    }
}
