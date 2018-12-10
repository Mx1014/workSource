// @formatter:off
package com.everhomes.welfare;

import java.util.List;

public interface WelfarePointProvider {

	void createWelfarePoint(WelfarePoint welfarePoint);

	void updateWelfarePoint(WelfarePoint welfarePoint);

	WelfarePoint findWelfarePointById(Long id);

	List<WelfarePoint> listWelfarePoint();

}