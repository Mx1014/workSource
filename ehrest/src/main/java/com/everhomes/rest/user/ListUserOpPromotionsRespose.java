package com.everhomes.rest.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.promotion.OpPromotionDTO;
/**
 * <ul>
 * <li>promotions : 运营推广信息列表，{@link com.everhomes.rest.promotion.OpPromotionDTO}</li>
 * <li>nextPageAnchor : 下一页页码</li>
 * </ul>
 */
public class ListUserOpPromotionsRespose {
    @ItemType(OpPromotionDTO.class)
    private List<OpPromotionDTO> promotions;

    private Long nextPageAnchor;

    public ListUserOpPromotionsRespose() {
    }

    public ListUserOpPromotionsRespose(List<OpPromotionDTO> promotions, Long nextPageAnchor) {
        super();
        this.promotions = promotions;
        this.nextPageAnchor = nextPageAnchor;
    }

	public List<OpPromotionDTO> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<OpPromotionDTO> promotions) {
        this.promotions = promotions;
    }

    public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
}
