package com.everhomes.techpark.expansion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.everhomes.address.AddressProvider;
import com.everhomes.address.ApartmentDTO;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.enterprise.EnterpriseAddress;
import com.everhomes.enterprise.EnterpriseAttachment;
import com.everhomes.enterprise.EnterpriseAttachmentDTO;
import com.everhomes.enterprise.EnterpriseCommunityMap;
import com.everhomes.enterprise.EnterpriseCommunityMapStatus;
import com.everhomes.enterprise.EnterpriseCommunityMapType;
import com.everhomes.enterprise.EnterpriseProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.group.Group;
import com.everhomes.group.GroupProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.server.schema.Tables;
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
	
	@Autowired
	private GroupProvider groupProvider;
	
	

	@Override
	public ListEnterpriseDetailResponse listEnterpriseDetails(
			ListEnterpriseDetailCommand cmd) {
		
		ListEnterpriseDetailResponse res = new ListEnterpriseDetailResponse();
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		
		List<Long> enterpriseIds = new ArrayList<Long>();
		ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
		//根据楼栋名称查询
		if(!StringUtils.isEmpty(cmd.getBuildingName())){
			List<EnterpriseAddress> enterpriseAddresses = enterpriseApplyEntryProvider.listBuildingEnterprisesByBuildingName(cmd.getBuildingName(), locator, pageSize+1);
			for (EnterpriseAddress enterpriseAddress : enterpriseAddresses) {
				enterpriseIds.add(enterpriseAddress.getEnterpriseId());
			}
		}else{
			List<EnterpriseCommunityMap> enterpriseMaps = this.enterpriseProvider.queryEnterpriseMapByCommunityId(locator
		                , cmd.getCommunityId(), pageSize+1, new ListingQueryBuilderCallback() {

		            @Override
		            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
		                    SelectQuery<? extends Record> query) {
		                query.addConditions(Tables.EH_ENTERPRISE_COMMUNITY_MAP.COMMUNITY_ID.eq(cmd.getCommunityId()));
		                query.addConditions(Tables.EH_ENTERPRISE_COMMUNITY_MAP.MEMBER_TYPE.eq(EnterpriseCommunityMapType.Enterprise.getCode()));
		                query.addConditions(Tables.EH_ENTERPRISE_COMMUNITY_MAP.MEMBER_STATUS.ne(EnterpriseCommunityMapStatus.Inactive.getCode()));
		                return query;
		            }
		      });    
			
			for (EnterpriseCommunityMap enterpriseCommunityMap : enterpriseMaps) {
				enterpriseIds.add(enterpriseCommunityMap.getMemberId());
			}
		}
		
		List<EnterpriseDetailDTO> dtos = new ArrayList<EnterpriseDetailDTO>();
		
		if(null != locator.getAnchor())
			enterpriseIds.remove(enterpriseIds.size()-1);
		
		for (Long enterpriseId : enterpriseIds) {
			EnterpriseDetail enterpriseDetail = this.getEnterpriseDetailByEnterpriseId(enterpriseId);
			if(null != enterpriseDetail)
				dtos.add(toEnterpriseDetailDTO(enterpriseDetail));
		}
		
		res.setNextPageAnchor(locator.getAnchor());
		res.setDetails(dtos);
		return res;
	}

	@Override
	public GetEnterpriseDetailByIdResponse getEnterpriseDetailById(
			GetEnterpriseDetailByIdCommand cmd) {
		GetEnterpriseDetailByIdResponse res = new GetEnterpriseDetailByIdResponse();
		EnterpriseDetail enterpriseDetail = this.getEnterpriseDetailByEnterpriseId(cmd.getId());
		res.setDetail(toEnterpriseDetailDTO(enterpriseDetail));
		return res;
	}
	
	private EnterpriseDetail getEnterpriseDetailByEnterpriseId(Long enterpriseId){
		Group group = groupProvider.findGroupById(enterpriseId);
		
		if(null == group){
			return null;
		}
		EnterpriseDetail enterpriseDetail = enterpriseApplyEntryProvider.getEnterpriseDetailById(enterpriseId);
		if(null == enterpriseDetail){
			enterpriseDetail = new EnterpriseDetail();
		}
		enterpriseDetail.setEnterpriseId(group.getId());
    	enterpriseDetail.setEnterpriseName(group.getName());
		String description =  enterpriseDetail.getDescription();
    	enterpriseDetail.setDescription(StringUtils.isEmpty(description) ? group.getDescription() : description);
    	String contact =  enterpriseDetail.getContact();
    	enterpriseDetail.setContact(StringUtils.isEmpty(contact) ? group.getEnterpriseContact() : contact);
    	String address = enterpriseDetail.getAddress();
    	enterpriseDetail.setDescription(StringUtils.isEmpty(address) ? group.getEnterpriseContact() : address);
    	return enterpriseDetail;
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
