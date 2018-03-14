package com.everhomes.rest.quality;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *  <li>specifications: 参考 {@link com.everhomes.rest.quality.QualityInspectionSpecificationDTO}</li>
 *  <li>nextPageAnchor: 下一页锚点</li>
 * </ul>
 */
public class ListQualitySpecificationsResponse {

	@ItemType(QualityInspectionSpecificationDTO.class)
	private List<QualityInspectionSpecificationDTO> specifications;

	private Long nextPageAnchor;
	
	public List<QualityInspectionSpecificationDTO> getSpecifications() {
		return specifications;
	}

	public void setSpecifications(
			List<QualityInspectionSpecificationDTO> specifications) {
		this.specifications = specifications;
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
