// @formatter:off
package com.everhomes.point;

public interface PointRuleProvider {

	void createPointRule(PointRule pointRule);

	void updatePointRule(PointRule pointRule);

	PointRule findById(Long id);

}