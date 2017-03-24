package com.everhomes.techpark.expansion;

import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.techpark.expansion.*;

import java.util.List;


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

	ListLeaseIssuersResponse listLeaseIssuers(ListLeaseIssuersCommand cmd);

	void deleteLeaseIssuer(DeleteLeaseIssuerCommand cmd);

	void addLeaseIssuer(AddLeaseIssuerCommand cmd);

	LeasePromotionConfigDTO getLeasePromotionConfig(GetLeasePromotionConfigCommand cmd);

	CheckIsLeaseIssuerDTO checkIsLeaseIssuer(CheckIsLeaseIssuerCommand cmd);

	ListLeaseIssuerBuildingsResponse listBuildings(ListLeaseIssuerBuildingsCommand cmd);

	List<AddressDTO>  listLeaseIssuerApartments(ListLeaseIssuerApartmentsCommand cmd);
	
}
