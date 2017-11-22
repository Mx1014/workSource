// @formatter:off
package com.everhomes.point;

public interface PointEventLogProvider {

	void createPointEventLog(PointEventLog pointEventLog);

	void updatePointEventLog(PointEventLog pointEventLog);

	PointEventLog findById(Long id);

}