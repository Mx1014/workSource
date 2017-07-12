package com.everhomes.rest.quality;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>specifications: 参考 {@link com.everhomes.rest.quality.QualityInspectionSpecificationDTO}</li>
 * </ul>
 */
public class ListQualitySpecificationsResponse {

	@ItemType(QualityInspectionSpecificationDTO.class)
	private List<QualityInspectionSpecificationDTO> specifications;
	
	public List<QualityInspectionSpecificationDTO> getSpecifications() {
		return specifications;
	}

	public void setSpecifications(
			List<QualityInspectionSpecificationDTO> specifications) {
		this.specifications = specifications;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
