// @formatter:off
package com.everhomes.rest.enterprisemoment;

import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>totalCount: 点赞总数</li>
 * <li>favourites: 点赞列表 {@link com.everhomes.rest.enterprisemoment.FavouriteDTO}</li>
 * </ul>
 */
public class ListMomentFavouritesResponse {
    private Long nextPageAnchor;
    private Long totalCount;
    private List<FavouriteDTO> favourites;

    public ListMomentFavouritesResponse() {
        this.favourites = new ArrayList<>();
    }

    public ListMomentFavouritesResponse(Long nextPageAnchor, List<FavouriteDTO> favourites) {
        this.nextPageAnchor = nextPageAnchor;
        this.favourites = favourites;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<FavouriteDTO> getFavourites() {
        return favourites;
    }

    public void setFavourites(List<FavouriteDTO> favourites) {
        this.favourites = favourites;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
