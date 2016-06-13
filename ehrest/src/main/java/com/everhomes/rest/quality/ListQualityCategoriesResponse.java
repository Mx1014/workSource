package com.everhomes.rest.quality;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;


/**
 * <ul>
 *  <li>categories: 参考com.everhomes.rest.quality.QualityCategoriesDTO</li>
 *  <li>nextPageAnchor: 下一页的锚点，没有下一页则没有</li>
 * </ul>
 */
public class ListQualityCategoriesResponse {
	
	@ItemType(QualityCategoriesDTO.class)
	private List<QualityCategoriesDTO> categories;
	
	private Long nextPageAnchor;
	
	public ListQualityCategoriesResponse(Long nextPageAnchor, List<QualityCategoriesDTO> categories) {
        this.nextPageAnchor = nextPageAnchor;
        this.categories = categories;
    }

	public List<QualityCategoriesDTO> getCategories() {
		return categories;
	}

	public void setCategories(List<QualityCategoriesDTO> categories) {
		this.categories = categories;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
