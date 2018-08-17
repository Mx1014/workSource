// @formatter:off
package com.everhomes.techpark.punch;

import java.util.List;

public interface PunchStatisticFileProvider {

	void createPunchStatisticFile(PunchStatisticFile punchStatisticFile);

	void updatePunchStatisticFile(PunchStatisticFile punchStatisticFile);

	PunchStatisticFile findPunchStatisticFileById(Long id);

	List<PunchStatisticFile> listPunchStatisticFile();

}