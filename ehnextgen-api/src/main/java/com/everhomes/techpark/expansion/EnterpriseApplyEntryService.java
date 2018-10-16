package com.everhomes.techpark.expansion;

import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.techpark.expansion.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;


public interface EnterpriseApplyEntryService {

	long OTHER_BUILDING_ID = 0L;

	GetEnterpriseDetailByIdResponse getEnterpriseDetailById(GetEnterpriseDetailByIdCommand cmd);
	
	ListEnterpriseApplyEntryResponse listApplyEntrys(ListEnterpriseApplyEntryCommand cmd);
	
	ApplyEntryResponse applyEntry(EnterpriseApplyEntryCommand cmd);

	ListBuildingForRentResponse listLeasePromotions(ListBuildingForRentCommand cmd);

	BuildingForRentDTO createLeasePromotion(CreateLeasePromotionCommand cmd, Byte adminFlag);

	BuildingForRentDTO updateLeasePromotion(UpdateLeasePromotionCommand cmd, Byte adminFlag);
	
	BuildingForRentDTO findLeasePromotionById(Long id);
	
	boolean updateLeasePromotionStatus(UpdateLeasePromotionStatusCommand cmd);
	
	boolean updateApplyEntryStatus(UpdateApplyEntryStatusCommand cmd);
	
	boolean deleteApplyEntry(DeleteApplyEntryCommand cmd);
	
	boolean deleteLeasePromotion(DeleteLeasePromotionCommand cmd);

	ListLeaseIssuersResponse listLeaseIssuers(ListLeaseIssuersCommand cmd);

	void deleteLeaseIssuer(DeleteLeaseIssuerCommand cmd);

	void addLeaseIssuer(AddLeaseIssuerCommand cmd);

	LeasePromotionConfigDTO getLeasePromotionConfig(GetLeasePromotionConfigCommand cmd);

	void setLeasePromotionConfig(SetLeasePromotionConfigCommand cmd);

	CheckIsLeaseIssuerDTO checkIsLeaseIssuer(CheckIsLeaseIssuerCommand cmd);

	ListLeaseIssuerBuildingsResponse listBuildings(ListLeaseIssuerBuildingsCommand cmd);

	List<AddressDTO>  listLeaseIssuerApartments(ListLeaseIssuerApartmentsCommand cmd);

	void updateLeasePromotionRequestForm(@Valid UpdateLeasePromotionRequestFormCommand cmd);

	LeaseFormRequestDTO getLeasePromotionRequestForm(@Valid GetLeasePromotionRequestFormCommand cmd);

	void updateLeasePromotionOrder(@Valid UpdateLeasePromotionOrderCommand cmd);

	String getSourceTypeName(String type);

	void exportApplyEntrys(ListEnterpriseApplyEntryCommand cmd, HttpServletResponse resp);

	List<Long> transformToCustomer(TransformToCustomerCommand cmd);
	
}
