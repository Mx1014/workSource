package com.everhomes.yellowPage.stat;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.everhomes.listing.ListingLocator;

public interface ClickStatDetailProvider {

	List<ClickStatDetail> listStatDetails(ListingLocator locator, Integer pageSize, Long type, Long ownerId,
			Long categoryId, Long serviceId, Byte clickType, Long userId, String userPhone, Timestamp startTime,
			Timestamp endTime);

	void createClickStatDetail(ClickStatDetail detail);

	List<Map<String, Object>> countClickDetailsByDate(Integer namespaceId, Timestamp minTime, Timestamp maxTime);

	void deleteStatDetail(Date date);

}
