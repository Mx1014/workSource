// @formatter:off
package com.everhomes.approval;

import java.sql.Date;
import java.util.List;

public interface ApprovalCategoryProvider {

	void createApprovalCategory(ApprovalCategory approvalCategory);

	void updateApprovalCategory(ApprovalCategory approvalCategory);

	ApprovalCategory findApprovalCategoryById(Long id);

	List<ApprovalCategory> listApprovalCategory();

	List<ApprovalCategory> listApprovalCategory(Integer namespaceId, String ownerType, Long ownerId, Byte approvalType);

	ApprovalCategory findApprovalCategoryByName(Integer namespaceId, String ownerType, Long ownerId, Byte approvalType,
			String categoryName);

	List<ApprovalCategory> listApprovalCategoryForStatistics(Integer namespaceId, String ownerType,
			Long ownerId, Byte approvalType, Date fromDate);

}