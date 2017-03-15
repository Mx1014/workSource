package com.everhomes.techpark.expansion;

import com.everhomes.rest.techpark.expansion.ApplyEntryResponse;
import com.everhomes.rest.techpark.expansion.BuildingForRentDTO;
import com.everhomes.rest.techpark.expansion.CreateLeasePromotionCommand;
import com.everhomes.rest.techpark.expansion.DeleteApplyEntryCommand;
import com.everhomes.rest.techpark.expansion.DeleteLeasePromotionCommand;
import com.everhomes.rest.techpark.expansion.EnterpriseApplyEntryCommand;
import com.everhomes.rest.techpark.expansion.EnterpriseApplyRenewCommand;
import com.everhomes.rest.techpark.expansion.GetEnterpriseDetailByIdCommand;
import com.everhomes.rest.techpark.expansion.GetEnterpriseDetailByIdResponse;
import com.everhomes.rest.techpark.expansion.ListBuildingForRentCommand;
import com.everhomes.rest.techpark.expansion.ListBuildingForRentResponse;
import com.everhomes.rest.techpark.expansion.ListEnterpriseApplyEntryCommand;
import com.everhomes.rest.techpark.expansion.ListEnterpriseApplyEntryResponse;
import com.everhomes.rest.techpark.expansion.ListEnterpriseDetailCommand;
import com.everhomes.rest.techpark.expansion.ListEnterpriseDetailResponse;
import com.everhomes.rest.techpark.expansion.UpdateApplyEntryStatusCommand;
import com.everhomes.rest.techpark.expansion.UpdateLeasePromotionCommand;
import com.everhomes.rest.techpark.expansion.UpdateLeasePromotionStatusCommand;


public interface EnterpriseApplyEntryService {
	
	GetEnterpriseDetailByIdResponse getEnterpriseDetailById(GetEnterpriseDetailByIdCommand cmd);
	
	ListEnterpriseApplyEntryResponse listApplyEntrys(ListEnterpriseApplyEntryCommand cmd);
	
	ApplyEntryResponse applyEntry(EnterpriseApplyEntryCommand cmd);
	
	boolean applyRenew(EnterpriseApplyRenewCommand cmd);
	
	ListBuildingForRentResponse listLeasePromotions(ListBuildingForRentCommand cmd);
	
	boolean createLeasePromotion(CreateLeasePromotionCommand cmd);
	
	boolean updateLeasePromotion(UpdateLeasePromotionCommand cmd);
	
	BuildingForRentDTO findLeasePromotionById(Long id);
	
	boolean updateLeasePromotionStatus(UpdateLeasePromotionStatusCommand cmd);
	
	boolean updateApplyEntryStatus(UpdateApplyEntryStatusCommand cmd);
	
	boolean deleteApplyEntry(DeleteApplyEntryCommand cmd);
	
	boolean deleteLeasePromotion(DeleteLeasePromotionCommand cmd);
	
}
