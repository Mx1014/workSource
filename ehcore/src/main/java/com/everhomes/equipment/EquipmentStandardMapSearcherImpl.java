package com.everhomes.equipment;

import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.portal.PortalService;
import com.everhomes.rest.equipment.EquipmentStandardRelationDTO;
import com.everhomes.rest.equipment.InspectionStandardMapTargetType;
import com.everhomes.rest.equipment.SearchEquipmentStandardRelationsCommand;
import com.everhomes.rest.equipment.SearchEquipmentStandardRelationsResponse;
import com.everhomes.rest.equipment.Status;
import com.everhomes.rest.quality.OwnerType;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.EquipmentStandardMapSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
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
public class EquipmentStandardMapSearcherImpl extends AbstractElasticSearch implements
		EquipmentStandardMapSearcher {

	private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentStandardMapSearcherImpl.class);
	
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
	public void bulkUpdate(List<EquipmentStandardMap> maps) {
		BulkRequestBuilder brb = getClient().prepareBulk();
        for (EquipmentStandardMap map : maps) {
        	if(InspectionStandardMapTargetType.EQUIPMENT.getCode().equals(map.getTargetType())) {
	            XContentBuilder source = createDoc(map);
	            if(null != source) {
	                LOGGER.info("equipment standard map id:" + map.getId());
	                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
	                        .id(map.getId().toString()).source(source));    
	                }
        	}
            
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }

	}

	@Override
	public void feedDoc(EquipmentStandardMap map) {
		if(InspectionStandardMapTargetType.EQUIPMENT.getCode().equals(map.getTargetType())) {
			XContentBuilder source = createDoc(map);
	        feedDoc(map.getId().toString(), source);
		}
		
	}

	@Override
	public void syncFromDb() {
		int pageSize = 200;      
        this.deleteAll();
        
        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<EquipmentStandardMap> maps = equipmentProvider.listEquipmentStandardMap(locator, pageSize);
            
            if(maps.size() > 0) {
                this.bulkUpdate(maps);
            }
            
            if(locator.getAnchor() == null) {
                break;
            }
        }

        this.optimize(1);
        this.refresh();
        
        LOGGER.info("sync for equipment standard map ok");
	}

	@Override
	public SearchEquipmentStandardRelationsResponse query(SearchEquipmentStandardRelationsCommand cmd) {
		/*Long privilegeId = configProvider.getLongValue(EquipmentConstant.EQUIPMENT_RELATION_LIST, 0L);
		if(cmd.getTargetId() != null && cmd.getTargetId() != 0L) {
			userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getTargetId(), cmd.getOwnerId(), privilegeId);
		} else {
			userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getOwnerId(), privilegeId);
		}*/
		//checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.EQUIPMENT_RELATION_LIST,cmd.getTargetId());

		SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
		QueryBuilder qb = null;
        if(cmd.getKeyword() == null || cmd.getKeyword().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
//            qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
//            		.field("equipmentName", 1.2f)
//                    .field("standardNumber", 1.0f)
//                    .field("standardName", 1.2f)
//                    .field("equipmentNumber", 1.2f);
			qb = QueryBuilders.queryString("*" + cmd.getKeyword() + "*")
					.field("equipmentName")
					.field("standardNumber")
					.field("standardName")
					.field("equipmentNumber");

            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("equipmentName").addHighlightedField("standardNumber");
        }

        FilterBuilder fb = null;
        //V3.0.2去掉设备和标准关联审批
       /* FilterBuilder nfb = FilterBuilders.termFilter("reviewStatus", EquipmentReviewStatus.DELETE.getCode());
    	fb = FilterBuilders.notFilter(nfb);*/
        FilterBuilder nfb = FilterBuilders.termFilter("status", Status.INACTIVE.getCode());
    	fb = FilterBuilders.notFilter(nfb);
		//总公司分公司的原因改用namespaceId by xiongying20170328
		fb = FilterBuilders.termFilter("namespaceId", cmd.getNamespaceId());
//    	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerId", cmd.getOwnerId()));
//       fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerType", OwnerType.fromCode(cmd.getOwnerType()).getCode()));
        if(cmd.getTargetId() != null)
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("targetId", cmd.getTargetId()));
        
        if(!StringUtils.isNullOrEmpty(cmd.getTargetType()))
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("targetType", OwnerType.fromCode(cmd.getTargetType()).getCode()));

		//增加  V3.0.2  inspectionCategoryId   repeatType 过滤
		if (cmd.getRepeatType() != null)
			fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("repeatType", cmd.getRepeatType()));

		if (cmd.getInspectionCategoryId() != null)
			fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("inspectionCategoryId", cmd.getInspectionCategoryId()));
		//只有巡检对象为设备的时候才根据categoryId过滤
		if (cmd.getCategoryId() != null)
			fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("categoryId", cmd.getCategoryId()));

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

		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("SearchEquipmentStandardRelations query : {}", builder);
			LOGGER.debug("SearchEquipmentStandardRelations rsp : {}", rsp);
		}
        List<Long> ids = getIds(rsp);
        
        SearchEquipmentStandardRelationsResponse response = new SearchEquipmentStandardRelationsResponse();
        if(ids.size() > pageSize) {
        	response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
         } else {
        	response.setNextPageAnchor(null);
		}
        
        List<EquipmentStandardRelationDTO> dtos = new ArrayList<EquipmentStandardRelationDTO>();
        for(Long id : ids) {
        	EquipmentStandardMap map = equipmentProvider.findEquipmentStandardMapById(id);
        	if(map != null) {
	        	EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(map.getTargetId());
	        	EquipmentStandardRelationDTO dto = new EquipmentStandardRelationDTO();
				dto.setId(map.getId());
				if (equipment != null) {
					dto.setEquipmentId(equipment.getId());
					dto.setTargetId(equipment.getTargetId());
//	        	Organization group = organizationProvider.findOrganizationById(dto.getTargetId());
				/*Community community = communityProvider.findCommunityById(dto.getTargetId());
				if(community != null)
	    			dto.setTargetName(community.getName());*/

					dto.setEquipmentName(equipment.getName());
					dto.setLocation(equipment.getLocation());
					dto.setEquipmentModel(equipment.getEquipmentModel());
					dto.setLocation(equipment.getLocation());
					dto.setStatus(equipment.getStatus());
					dto.setSequenceNo(equipment.getSequenceNo());
					dto.setCustomNumber(equipment.getCustomNumber());
				}
				EquipmentInspectionStandards standard = equipmentProvider.findStandardById(map.getStandardId());
				if (standard != null) {
					dto.setStandardName(standard.getName());
					dto.setRepeatType(standard.getRepeatType());
					dto.setStandardId(standard.getId());
				}
	            
	            /*dto.setReviewResult(map.getReviewResult());
	            dto.setReviewStatus(map.getReviewStatus());
				dto.setReviewTime(map.getReviewTime());*/

				/*if(map.getReviewerUid() != null && map.getReviewerUid() != 0L) {
					OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(map.getReviewerUid(), equipment.getOwnerId());
					if(member != null) {
						dto.setReviewer(member.getContactName());
					}
				}*/
	    		dtos.add(dto);
        	}
        }
        
        response.setRelations(dtos);
        return response;
	}
	private void checkUserPrivilege(Long orgId, Long privilegeId, Long communityId) {
		/*ListServiceModuleAppsCommand listServiceModuleAppsCommand = new ListServiceModuleAppsCommand();
		listServiceModuleAppsCommand.setNamespaceId(UserContext.getCurrentNamespaceId());
		listServiceModuleAppsCommand.setModuleId(EquipmentConstant.EQUIPMENT_MODULE);
		ListServiceModuleAppsResponse apps = portalService.listServiceModuleAppsWithConditon(listServiceModuleAppsCommand);
		boolean flag = false;
		if (null != apps && null != apps.getServiceModuleApps() && apps.getServiceModuleApps().size() > 0) {
			flag = userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), EntityType.ORGANIZATIONS.getCode(),
					orgId, orgId, privilegeId, apps.getServiceModuleApps().get(0).getId(), null, communityId);
			if (!flag) {
				LOGGER.error("Permission is denied, namespaceId={}, orgId={}, communityId={}," +
						" privilege={}", UserContext.getCurrentNamespaceId(), orgId, communityId, privilegeId);
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
						"Insufficient privilege");
			}
		}*/
		userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), orgId, privilegeId, EquipmentConstant.EQUIPMENT_MODULE, null, null, null,communityId);


	}

	@Override
	public String getIndexType() {
		return SearchUtils.EQUIPMENTSTANDARDMAP;
	}
	
	private XContentBuilder createDoc(EquipmentStandardMap map){
		try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            
            EquipmentInspectionStandards standard = equipmentProvider.findStandardById(map.getStandardId());
            if(standard != null) {
            	b.field("standardNumber", standard.getStandardNumber());
            	b.field("standardName", standard.getName());
            	//add repeatType
				b.field("repeatType", standard.getRepeatType());
				b.field("namespaceId", standard.getNamespaceId());
				b.field("status", standard.getStatus());
            } else {
            	b.field("standardNumber", "");
            }
            	
            EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(map.getTargetId());
            if(equipment != null) {
            	b.field("ownerId", equipment.getOwnerId());
            	b.field("equipmentName", equipment.getName());
            	b.field("equipmentNumber", equipment.getCustomNumber());
            	b.field("ownerType", equipment.getOwnerType());
				b.field("namespaceId", equipment.getNamespaceId());
            	b.field("targetId", equipment.getTargetId());
            	b.field("targetType", equipment.getTargetType());
//            	b.field("equipmentName", equipment.getName()).field("index","not_analyzed");
            	//add equipment   inspectionCategoryId and categoryId  V3.0.2
				b.field("inspectionCategoryId", equipment.getInspectionCategoryId());
				b.field("categoryId", equipment.getCategoryId());
			} else {
            	b.field("ownerId", "");
                b.field("ownerType", "");
                b.field("targetId", "");
                b.field("targetType", "");
                b.field("equipmentName", "");
            }

            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create EquipmentStandardMap " + map.getId() + " error");
            return null;
        }
    }

}
