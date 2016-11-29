package com.everhomes.quality;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.repeat.RepeatSettings;
import com.everhomes.rest.quality.StandardGroupDTO;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionStandards;
import com.everhomes.util.StringHelper;

public class QualityInspectionStandards extends EhQualityInspectionStandards {

	private static final long serialVersionUID = 8570986794007947976L;
	
	@ItemType(RepeatSettings.class)
	private RepeatSettings repeat;
	
	@ItemType(QualityInspectionStandardGroupMap.class)
	private List<QualityInspectionStandardGroupMap> executiveGroup;

	@ItemType(QualityInspectionStandardGroupMap.class)
	private List<QualityInspectionStandardGroupMap> reviewGroup;
	
	@ItemType(QualityInspectionSpecifications.class)
	private List<QualityInspectionSpecifications> specifications;
	
	public RepeatSettings getRepeat() {
		return repeat;
	}

	public void setRepeat(RepeatSettings repeat) {
		this.repeat = repeat;
	}

	public List<QualityInspectionStandardGroupMap> getExecutiveGroup() {
		return executiveGroup;
	}

	public void setExecutiveGroup(List<QualityInspectionStandardGroupMap> executiveGroup) {
		this.executiveGroup = executiveGroup;
	}

	public List<QualityInspectionStandardGroupMap> getReviewGroup() {
		return reviewGroup;
	}

	public void setReviewGroup(List<QualityInspectionStandardGroupMap> reviewGroup) {
		this.reviewGroup = reviewGroup;
	}

	public List<QualityInspectionSpecifications> getSpecifications() {
		return specifications;
	}

	public void setSpecifications(
			List<QualityInspectionSpecifications> specifications) {
		this.specifications = specifications;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
