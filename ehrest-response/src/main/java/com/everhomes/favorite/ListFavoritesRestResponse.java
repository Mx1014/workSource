// @formatter:off
// generated at 2015-08-20 18:05:54
package com.everhomes.favorite;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.favorite.FavoriteDTO;

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
