package com.everhomes.approval;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface ApprovalRangeStatisticProvider {

	Long createApprovalRangeStatistic(ApprovalRangeStatistic obj);

	void updateApprovalRangeStatistic(ApprovalRangeStatistic obj);

	void deleteApprovalRangeStatistic(ApprovalRangeStatistic obj);

	ApprovalRangeStatistic getApprovalRangeStatisticById(Long id);

	List<ApprovalRangeStatistic> queryApprovalRangeStatistics(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

	ApprovalRangeStatistic getApprovalRangeStatisticByParams(String punchMonth, String ownerType,
			Long ownerId, Long userId, Byte approvalType, Long categoryId);

}
