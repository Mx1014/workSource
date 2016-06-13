package com.everhomes.rest.business;


import java.util.List;



import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>requests: 商家列表，参考{@link com.everhomes.rest.business.BusinessDTO}</li>
 * <li>nextPageOffset: 下一页页码</li>
 * <li>favoriteCount: 收藏的店铺个数</li>
 * </ul>
 */

public class GetBusinessesByCategoryCommandResponse{
    @ItemType(BusinessDTO.class)
    private List<BusinessDTO> requests;
    private Integer nextPageOffset;
    private Integer favoriteCount;

    public List<BusinessDTO> getRequests() {
        return requests;
    }

    public void setRequests(List<BusinessDTO> requests) {
        this.requests = requests;
    }

    public Integer getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(Integer nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    public Integer getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(Integer favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
