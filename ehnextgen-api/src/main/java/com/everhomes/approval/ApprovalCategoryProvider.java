// @formatter:off
package com.everhomes.approval;

import java.util.List;

public interface ApprovalCategoryProvider {

	void createApprovalCategory(ApprovalCategory approvalCategory);

	void updateApprovalCategory(ApprovalCategory approvalCategory);

	Integer getNextApprovalCategoryDefaultOrder(Integer namespaceId, String ownerType, Long ownerId);

	ApprovalCategory findApprovalCategoryById(Long id);

	ApprovalCategory findApprovalCategoryByOriginId(Long id, Long ownerId);

	ApprovalCategory findApprovalCategoryById(Integer namespaceId, String ownerType, Long ownerId, Long id);

	List<ApprovalCategory> listBaseApprovalCategory();

	List<ApprovalCategory> listApprovalCategory(QueryApprovalCategoryCondition condition);

	void createApprovalCategoryInitLog(ApprovalCategoryInitLog approvalCategoryInitLog);

	int countApprovalCategoryInitLogByOwnerId(Integer namespaceId, String ownerType, Long ownerId);

	ApprovalCategory findApprovalCategoryByName(Integer namespaceId, String ownerType, Long ownerId, Byte approvalType,
			String categoryName);

}