// @formatter:off
// generated at 2015-10-30 14:21:35
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
