package com.everhomes.yellowPage.stat;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;


public interface ClickStatProvider {

	List<ClickStat> listStats(Long type, Long ownerId, Long categoryId, Timestamp startTime, Timestamp endTime, Byte searchType);

	void createStat(ClickStat stat);

	void deleteStat(Date date);
}
