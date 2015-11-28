package com.everhomes.techpark.expansion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.enterprise.EnterpriseAttachment;
import com.everhomes.enterprise.EnterpriseAttachmentDTO;
import com.everhomes.enterprise.EnterpriseProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class EnterpriseApplyEntryServiceImpl implements EnterpriseApplyEntryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseApplyEntryServiceImpl.class);
    
	@Autowired
	private ConfigurationProvider configurationProvider;
	
	@Autowired
	private EnterpriseApplyEntryProvider enterpriseApplyEntryProvider;
	
	@Autowired
	private ContentServerService contentServerService;
	
	@Autowired
	private EnterpriseProvider enterpriseProvider;

	@Override
	public ListEnterpriseDetailResponse listEnterpriseDetails(
			ListEnterpriseDetailCommand cmd) {
		
		ListEnterpriseDetailResponse res = new ListEnterpriseDetailResponse();
		//TODO: 这里分页有问题，先注释掉，查全部
//		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
//		Integer offset = cmd.getPageAnchor() == null ? 0 : (cmd.getPageAnchor() - 1 ) * pageSize;
		List<EnterpriseDetail> enterpriseDetails = enterpriseApplyEntryProvider.listEnterpriseDetails(cmd.getCommunityId(),cmd.getBuildingName(), 0, Integer.MAX_VALUE);
		
//		if(enterpriseDetails != null && enterpriseDetails.size() > pageSize) {
//			enterpriseDetails.remove(enterpriseDetails.size() - 1);
//			res.setNextPageAnchor(cmd.getPageAnchor() + 1);
//		}
//		
		List<EnterpriseDetailDTO> dtos = enterpriseDetails.stream().map((c) ->{
			// return ConvertHelper.convert(c, EnterpriseDetailDTO.class);
		    return toEnterpriseDetailDTO(c);
		}).collect(Collectors.toList());
		
		res.setDetails(dtos);
		return res;
	}

	@Override
	public GetEnterpriseDetailByIdResponse getEnterpriseDetailById(
			GetEnterpriseDetailByIdCommand cmd) {
		GetEnterpriseDetailByIdResponse res = new GetEnterpriseDetailByIdResponse();
		EnterpriseDetail enterpriseDetail = enterpriseApplyEntryProvider.getEnterpriseDetailById(cmd.getId());
		// res.setDetail(ConvertHelper.convert(enterpriseDetail, EnterpriseDetailDTO.class));
		res.setDetail(toEnterpriseDetailDTO(enterpriseDetail));
		return res;
	}
	
	private EnterpriseDetailDTO toEnterpriseDetailDTO(EnterpriseDetail enterpriseDetail) {
	    User user = UserContext.current().getUser();
	    Long userId = (user == null) ? -1L : user.getId();
	    
	    EnterpriseDetailDTO dto = null;
	    if(enterpriseDetail != null) {
	        dto = ConvertHelper.convert(enterpriseDetail, EnterpriseDetailDTO.class);
	        
	        List<EnterpriseAttachment> attachments = enterpriseProvider.listEnterpriseAttachments(enterpriseDetail.getEnterpriseId());
	        if(attachments != null && attachments.size() > 0)
	        {
	            List<EnterpriseAttachmentDTO> attachmentDtoList = new ArrayList<EnterpriseAttachmentDTO>();
	            for(EnterpriseAttachment attachment : attachments) {
	                EnterpriseAttachmentDTO attachmentDto = ConvertHelper.convert(attachment, EnterpriseAttachmentDTO.class);
	                String uri = attachment.getContentUri();
	                if(uri != null && uri.length() > 0) {
	                    try{
	                        String url = contentServerService.parserUri(uri, EntityType.GROUP.getCode(), enterpriseDetail.getEnterpriseId());
	                        attachmentDto.setContentUrl(url);
	                    }catch(Exception e){
	                        LOGGER.error("Failed to parse content uri of enterprise attachments, userId=" + userId 
	                            + ", enterpriseId=" + enterpriseDetail.getEnterpriseId(), e);
	                    }
	                }
	                
	                attachmentDtoList.add(attachmentDto);
	            }
	            
	            dto.setAttachments(attachmentDtoList);
	        }
	    }
	    
	    return dto;
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
		EnterpriseOpRequest request = ConvertHelper.convert(cmd, EnterpriseOpRequest.class);
		request.setApplyUserId(UserContext.current().getUser().getId());
		if(null != cmd.getContactPhone())
			request.setApplyContact(cmd.getContactPhone());
		
		request.setOperatorUid(request.getApplyUserId());
		request.setStatus(ApplyEntryStatus.PROCESSING.getCode());
//		request.setSourceType(cmd.getSourceType());
//		request.setApplyType(cmd.getApplyType());
//		request.setEnterpriseName(cmd.getEnterpriseName());
//		request.setEnterpriseId(cmd.getEnterpriseId());
//		request.setApplyUserName(cmd.getApplyUserName());
//		request.setApplyContact(cmd.getContactPhone());
//		request.setApplyUserId(UserContext.current().getUser().getId());
//		request.setDescription(cmd.getDescription());
//		request.setSizeUnit(cmd.getSizeUnit());
//		request.setAreaSize(cmd.getAreaSize());
//		request.setOperatorUid(request.getApplyUserId());
//		request.setCommunityId(cmd.getCommunityId());
//		request.setNamespaceId(cmd.getNamespaceId());
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
