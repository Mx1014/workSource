package com.everhomes.equipment;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.portal.PortalService;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.equipment.*;
import com.everhomes.rest.quality.OwnerType;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.EquipmentSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.mysql.jdbc.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class EquipmentSearcherImpl extends AbstractElasticSearch implements EquipmentSearcher{

	private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentSearcherImpl.class);
			
	@Autowired
	private EquipmentProvider equipmentProvider;
	
	@Autowired
	private ConfigurationProvider configProvider;
	
	@Autowired
	private OrganizationProvider organizationProvider;

    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;

    @Autowired
    private PortalService portalService;
	
	@Override
	public void deleteById(Long id) {
		deleteById(id.toString());
	}

	@Override
	public void bulkUpdate(List<EquipmentInspectionEquipments> equipments) {
		BulkRequestBuilder brb = getClient().prepareBulk();
        for (EquipmentInspectionEquipments equipment : equipments) {
        	
            XContentBuilder source = createDoc(equipment);
            if(null != source) {
                LOGGER.info("equipment id:" + equipment.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(equipment.getId().toString()).source(source));    
                }
            
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
	}

	@Override
	public void feedDoc(EquipmentInspectionEquipments equipment) {
		XContentBuilder source = createDoc(equipment);
        
        feedDoc(equipment.getId().toString(), source);
	}

	@Override
	public void syncFromDb() {
		int pageSize = 200;      
        this.deleteAll();
        
        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<EquipmentInspectionEquipments> equipments = equipmentProvider.listEquipments(locator, pageSize);
            
            if(equipments.size() > 0) {
                this.bulkUpdate(equipments);
            }
            
            if(locator.getAnchor() == null) {
                break;
            }
        }

        this.optimize(1);
        this.refresh();
        
        LOGGER.info("sync for equipment ok");
	}

	@Override
	public SearchEquipmentsResponse queryEquipments(SearchEquipmentsCommand cmd) {
        /*Long privilegeId = configProvider.getLongValue(EquipmentConstant.EQUIPMENT_LIST, 0L);
        userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getTargetId(), cmd.getOwnerId(), privilegeId);*/
        checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.EQUIPMENT_LIST,cmd.getTargetId());
		SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
		QueryBuilder qb = null;
        if(cmd.getKeyword() == null || cmd.getKeyword().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
            		.field("name", 1.2f)
                    .field("standardName", 1.0f)
                    .field("customNumber",1.2f);
            
            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("name")
                    .addHighlightedField("standardName")
                    .addHighlightedField("customNumber");
        }

        FilterBuilder fb = null;
        FilterBuilder nfb = FilterBuilders.termFilter("status", EquipmentStatus.INACTIVE.getCode());
    	fb = FilterBuilders.notFilter(nfb);

        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("namespaceId",cmd.getNamespaceId()));

        if (cmd.getTargetId() != null && cmd.getTargetId() != 0L) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("targetId", cmd.getTargetId()));
            if (!StringUtils.isNullOrEmpty(cmd.getTargetType()))
                fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("targetType", OwnerType.fromCode(cmd.getTargetType()).getCode()));
        } else if (cmd.getTargetIds() != null && cmd.getTargetIds().size() > 0) {
            // only all scope field term with ownerId followed by this rule
            fb = FilterBuilders.termsFilter("targetId", cmd.getTargetIds());
        }

        if(cmd.getStatus() != null)
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("status", cmd.getStatus()));
        
        if(cmd.getCategoryId() != null)
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("categoryId", cmd.getCategoryId()));
        
        if(cmd.getInspectionCategoryId() != null)
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("inspectionCategoryId", cmd.getInspectionCategoryId()));
        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0l;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }
        
        qb = QueryBuilders.filteredQuery(qb, fb);
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        builder.setFrom(anchor.intValue() * pageSize).setSize(pageSize + 1);
        builder.setQuery(qb);
        
        if(LOGGER.isDebugEnabled())
			LOGGER.info("EquipmentSearcherImpl query builder ："+builder);
        
        SearchResponse rsp = builder.execute().actionGet();

        List<Long> ids = getIds(rsp);
        SearchEquipmentsResponse response = new SearchEquipmentsResponse();
        
        if(ids.size() > pageSize) {
        	response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
         } 
        
        List<EquipmentsDTO> dtos = new ArrayList<EquipmentsDTO>();
        for(Long id : ids) {
        	EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(id);
        	if(equipment != null) {
	        	EquipmentsDTO dto = ConvertHelper.convert(equipment, EquipmentsDTO.class);
//	        	Organization group = organizationProvider.findOrganizationById(dto.getTargetId());
//	    		if(group != null)
//	    			dto.setTargetName(group.getName());
                Community community = communityProvider.findCommunityById(dto.getTargetId());
                if(community != null)
                    dto.setTargetName(community.getName());

	    		dtos.add(dto);
        	}
        }
        LOGGER.info("query equipment: {}", dtos);
        response.setEquipment(dtos);
        return response;
	}
    private void checkUserPrivilege(Long orgId, Long privilegeId, Long communityId) {
//        ListServiceModuleAppsCommand listServiceModuleAppsCommand = new ListServiceModuleAppsCommand();
//        listServiceModuleAppsCommand.setNamespaceId(UserContext.getCurrentNamespaceId());
//        listServiceModuleAppsCommand.setModuleId(EquipmentConstant.EQUIPMENT_MODULE);
//        ListServiceModuleAppsResponse apps = portalService.listServiceModuleAppsWithConditon(listServiceModuleAppsCommand);
//        boolean flag = false;
//        if (null != apps && null != apps.getServiceModuleApps() && apps.getServiceModuleApps().size() > 0) {
//            flag = userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), EntityType.ORGANIZATIONS.getCode(),
//                    orgId, orgId, privilegeId, apps.getServiceModuleApps().get(0).getId(), null, communityId);
//            if (!flag) {
//                LOGGER.error("Permission is denied, namespaceId={}, orgId={}, communityId={}," +
//                        " privilege={}", UserContext.getCurrentNamespaceId(), orgId, communityId, privilegeId);
//                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
//                        "Insufficient privilege");
//            }
//        }
        userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), orgId, privilegeId, EquipmentConstant.EQUIPMENT_MODULE, null, null, null,communityId);


    }

	@Override
	public SearchEquipmentStandardRelationsResponse queryEquipmentStandardRelations(SearchEquipmentStandardRelationsCommand cmd) {
		SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
		QueryBuilder qb = null;
        if(cmd.getKeyword() == null || cmd.getKeyword().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
                    .field("name", 5.0f)
                    .field("customNumber", 5.2f)
                    .field("standardName", 5.0f);
           /*
            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);*/
            builder.addHighlightedField("name").addHighlightedField("customNumber")
                    .addHighlightedField("standardName");
        }

        FilterBuilder fb = null;
        FilterBuilder nfb = FilterBuilders.termFilter("status", EquipmentStatus.INACTIVE.getCode());
    	fb = FilterBuilders.notFilter(nfb);

        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("namespaceId", UserContext.getCurrentNamespaceId()));

        if(cmd.getTargetId() != null)
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("targetId", cmd.getTargetId()));
        
        if(!StringUtils.isNullOrEmpty(cmd.getTargetType()))
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("targetType", OwnerType.fromCode(cmd.getTargetType()).getCode()));
        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0L;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }
        
        qb = QueryBuilders.filteredQuery(qb, fb);
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        builder.setFrom(anchor.intValue() * pageSize).setSize(pageSize + 1);
        builder.setQuery(qb);
        
        SearchResponse rsp = builder.execute().actionGet();

        List<Long> ids = getIds(rsp);
        
        SearchEquipmentStandardRelationsResponse response = new SearchEquipmentStandardRelationsResponse();
        if(ids.size() > pageSize) {
        	response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
         } 
        
        List<EquipmentStandardRelationDTO> dtos = new ArrayList<EquipmentStandardRelationDTO>();
        for(Long id : ids) {
        	EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(id);
        	EquipmentStandardRelationDTO dto = new EquipmentStandardRelationDTO();
        	dto.setEquipmentId(equipment.getId());
        	dto.setTargetId(equipment.getTargetId());
        	Organization group = organizationProvider.findOrganizationById(dto.getTargetId());
    		if(group != null)
    			dto.setTargetName(group.getName());
    		
    		dto.setEquipmentName(equipment.getName());
    		dto.setEquipmentModel(equipment.getEquipmentModel());
    		dto.setQrCodeFlag(equipment.getQrCodeFlag());
    		dto.setStatus(equipment.getStatus());
//    		dto.setStandardId(equipment.getStandardId());
//    		EquipmentInspectionStandards standard = equipmentProvider.findStandardById(equipment.getStandardId(), equipment.getOwnerType(), equipment.getOwnerId());
//            if(standard != null) {
//            	dto.setStandardName(standard.getName());
//            }
//            
//            dto.setReviewResult(equipment.getReviewResult());
//            dto.setReviewStatus(equipment.getReviewStatus());

    		dtos.add(dto);
        }
        
        response.setRelations(dtos);
        return response;
	}

	@Override
	public String getIndexType() {
		return SearchUtils.EQUIPMENTINDEXTYPE;
	}
	
	private XContentBuilder createDoc(EquipmentInspectionEquipments equipment){
		try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("namespaceId", equipment.getNamespaceId());
            b.field("ownerId", equipment.getOwnerId());
            b.field("targetId", equipment.getTargetId());
            b.field("targetType", equipment.getTargetType());
            b.field("status", equipment.getStatus());
            b.field("categoryId", equipment.getCategoryId());
            b.field("name", equipment.getName());
            b.field("customNumber", equipment.getCustomNumber());
            b.field("inspectionCategoryId", equipment.getInspectionCategoryId());

            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create order " + equipment.getId() + " error");
            return null;
        }
    }

}
