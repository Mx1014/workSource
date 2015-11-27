package com.everhomes.techpark.expansion;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class EnterpriseApplyEntryServiceImpl implements
		EnterpriseApplyEntryService {
	
	@Autowired
	private ConfigurationProvider configurationProvider;
	
	@Autowired
	private EnterpriseApplyEntryProvider enterpriseApplyEntryProvider;
	
	@Autowired
	private ContentServerService contentServerService;

	@Override
	public ListEnterpriseDetailResponse listEnterpriseDetails(
			ListEnterpriseDetailCommand cmd) {
		
		ListEnterpriseDetailResponse res = new ListEnterpriseDetailResponse();
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		Integer offset = cmd.getPageAnchor() == null ? 0 : (cmd.getPageAnchor() - 1 ) * pageSize;
		List<EnterpriseDetail> enterpriseDetails = enterpriseApplyEntryProvider.listEnterpriseDetails(cmd.getCommunityId(),cmd.getBuildingName(), offset, pageSize + 1);
		
		if(enterpriseDetails != null && enterpriseDetails.size() > pageSize) {
			enterpriseDetails.remove(enterpriseDetails.size() - 1);
			res.setNextPageAnchor(cmd.getPageAnchor() + 1);
		}
		
		List<EnterpriseDetailDTO> dtos = enterpriseDetails.stream().map((c) ->{
			return ConvertHelper.convert(c, EnterpriseDetailDTO.class);
		}).collect(Collectors.toList());
		
		res.setDetails(dtos);
		return res;
	}

	@Override
	public GetEnterpriseDetailByIdResponse getEnterpriseDetailById(
			GetEnterpriseDetailByIdCommand cmd) {
		GetEnterpriseDetailByIdResponse res = new GetEnterpriseDetailByIdResponse();
		EnterpriseDetail enterpriseDetail = enterpriseApplyEntryProvider.getEnterpriseDetailById(cmd.getId());
		res.setDetail(ConvertHelper.convert(enterpriseDetail, EnterpriseDetailDTO.class));
		return res;
	}

	@Override
	public ListEnterpriseApplyEntryResponse listApplyEntrys(
			ListEnterpriseApplyEntryCommand cmd) {
		
		ListEnterpriseApplyEntryResponse res = new ListEnterpriseApplyEntryResponse();
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		Integer offset = cmd.getPageAnchor() == null ? 0 : (cmd.getPageAnchor() - 1 ) * pageSize;
		List<EnterpriseOpRequest> enterpriseOpRequests = enterpriseApplyEntryProvider.listApplyEntrys(cmd.getCommunityId(), offset, pageSize + 1);
		
		if(enterpriseOpRequests != null && enterpriseOpRequests.size() > pageSize) {
			enterpriseOpRequests.remove(enterpriseOpRequests.size() - 1);
			res.setNextPageAnchor(cmd.getPageAnchor() + 1);
		}
		
		List<EnterpriseApplyEntryDTO> dtos = enterpriseOpRequests.stream().map((c) ->{
			return ConvertHelper.convert(c, EnterpriseApplyEntryDTO.class);
		}).collect(Collectors.toList());
		
		res.setEntrys(dtos);
		return res;
	}

	@Override
	public boolean applyEntry(EnterpriseApplyEntryCommand cmd) {
		EnterpriseOpRequest request = new EnterpriseOpRequest();
		request.setSourceType(cmd.getSourceType());
		request.setApplyType(cmd.getApplyType());
		request.setEnterpriseName(cmd.getEnterpriseName());
		request.setEnterpriseId(cmd.getEnterpriseId());
		request.setApplyUserName(cmd.getApplyUserName());
		request.setApplyContact(cmd.getContactPhone());
		request.setApplyUserId(UserContext.current().getUser().getId());
		request.setDescription(cmd.getDescription());
		request.setSizeUnit(cmd.getSizeUnit());
		request.setStatus(ApplyEntryStatus.PROCESSING.getCode());
		request.setAreaSize(cmd.getAreaSize());
		request.setOperatorUid(request.getApplyUserId());
		request.setCommunityId(cmd.getCommunityId());
		request.setNamespaceId(cmd.getNamespaceId());
		enterpriseApplyEntryProvider.createApplyEntry(request);
		return true;
	}

	@Override
	public boolean applyRenew(EnterpriseApplyRenewCommand cmd) {
		EnterpriseOpRequest request = enterpriseApplyEntryProvider.getEnterpriseOpRequestByBuildIdAndUserId(cmd.getId(), UserContext.current().getUser().getId());
		
		if(null == request){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"You have not applied for admission");
		}
		
		request.setApplyType(ApplyEntryApplyType.RENEW.getCode());
		request.setStatus(ApplyEntryStatus.PROCESSING.getCode());
		enterpriseApplyEntryProvider.createApplyEntry(request);
		return true;
	}
	
	@Override
	public ListBuildingForRentResponse listLeasePromotions(
			ListBuildingForRentCommand cmd) {
		ListBuildingForRentResponse res = new ListBuildingForRentResponse();
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		Integer offset = cmd.getPageAnchor() == null ? 0 : (cmd.getPageAnchor() - 1 ) * pageSize;
		List<LeasePromotion> leasePromotions = enterpriseApplyEntryProvider.listLeasePromotions(cmd.getCommunityId(), offset, pageSize + 1);
		
		if(leasePromotions != null && leasePromotions.size() > pageSize) {
			leasePromotions.remove(leasePromotions.size() - 1);
			res.setNextPageAnchor(cmd.getPageAnchor() + 1);
		}
		for (LeasePromotion leasePromotion : leasePromotions) {
			leasePromotion.setPosterUrl(contentServerService.parserUri(leasePromotion.getPosterUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId()));
			List<LeasePromotionAttachment> attachments = leasePromotion.getAttachments();
			if(null != attachments){
				for (LeasePromotionAttachment leasePromotionAttachment : attachments) {
					leasePromotionAttachment.setContentUrl(contentServerService.parserUri(leasePromotionAttachment.getContentUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId()));
				}
			}
				
		}
		
		List<BuildingForRentDTO> dtos = leasePromotions.stream().map((c) ->{
			return ConvertHelper.convert(c, BuildingForRentDTO.class);
		}).collect(Collectors.toList());
		
		res.setDtos(dtos);
		return res;
	}

}
