// @formatter:off
package com.everhomes.officecubicle;

import java.util.List;

public interface OfficeCubicleRangeProvider {

	void createOfficeCubicleRange(OfficeCubicleRange officeCubicleRange);

	void updateOfficeCubicleRange(OfficeCubicleRange officeCubicleRange);

	OfficeCubicleRange findOfficeCubicleRangeById(Long id);

	List<OfficeCubicleRange> listOfficeCubicleRange();

}