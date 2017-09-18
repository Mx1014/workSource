package com.everhomes.techpark.punch;

import java.util.Date;
import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface PunchSchedulingProvider {

	Long createPunchScheduling(PunchScheduling obj);

	void updatePunchScheduling(PunchScheduling obj);

	void deletePunchScheduling(PunchScheduling obj);

	PunchScheduling getPunchSchedulingById(Long id);

	List<PunchScheduling> queryPunchSchedulings(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);
 

	PunchScheduling getPunchSchedulingByRuleDateAndTarget(Long ruleId, Date time);

	PunchScheduling getPunchSchedulingByRuleDateAndTarget(Long ruleId, java.sql.Date time);

	void deletePunchSchedulingByOwnerAndTarget(String ownerType, Long ownerId, String targetType,
			Long targetId);

	void deletePunchSchedulingByPunchRuleId(Long id);

	Integer countSchedulingUser(Long ruleId, java.sql.Date start, java.sql.Date end);

	PunchScheduling getPunchSchedulingByRuleDateAndTarget(Long punchOrganizationId, Long userId,
			Date date,Long puchruleId);

	void deletePunchSchedulingByPunchRuleId(Long id, Date ruleDate, Long ownerId, Long targetId);

	void deletePunchSchedulingByPunchRuleIdAndTarget(Long id, Long detailId);

	void deletePunchSchedulingByPunchRuleIdAndNotInTarget(Long id, List<Long> detailIds);

	void deletePunchSchedulingByOwnerIdAndTarget(Long ownerId, Long detailId);
}
