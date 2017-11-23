package com.everhomes.equipment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
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

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.equipment.EquipmentReviewStatus;
import com.everhomes.rest.equipment.EquipmentStandardRelationDTO;
import com.everhomes.rest.equipment.InspectionStandardMapTargetType;
import com.everhomes.rest.equipment.SearchEquipmentStandardRelationsCommand;
import com.everhomes.rest.equipment.SearchEquipmentStandardRelationsResponse;
import com.everhomes.rest.quality.OwnerType;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.EquipmentStandardMapSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;
import com.mysql.jdbc.StringUtils;

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
	public SearchEquipmentStandardRelationsResponse query(
			SearchEquipmentStandardRelationsCommand cmd) {
		Long privilegeId = configProvider.getLongValue(EquipmentConstant.EQUIPMENT_RELATION_LIST, 0L);
		if(cmd.getTargetId() != null && cmd.getTargetId() != 0L) {
			userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getTargetId(), cmd.getOwnerId(), privilegeId);
		} else {
			userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getOwnerId(), privilegeId);
		}

		SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
		QueryBuilder qb = null;
        if(cmd.getKeyword() == null || cmd.getKeyword().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
            		.field("equipmentName", 1.2f)
                    .field("standardNumber", 1.0f);
            
            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("equipmentName").addHighlightedField("standardNumber");
        }

        FilterBuilder fb = null;
        FilterBuilder nfb = FilterBuilders.termFilter("reviewStatus", EquipmentReviewStatus.DELETE.getCode());
    	fb = FilterBuilders.notFilter(nfb);
		//总公司分公司的原因改用namespaceId by xiongying20170328
		fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("namespaceId", UserContext.getCurrentNamespaceId()));
//    	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerId", cmd.getOwnerId()));
//        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerType", OwnerType.fromCode(cmd.getOwnerType()).getCode()));
        if(cmd.getTargetId() != null)
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("targetId", cmd.getTargetId()));
        
        if(!StringUtils.isNullOrEmpty(cmd.getTargetType()))
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("targetType", OwnerType.fromCode(cmd.getTargetType()).getCode()));
        
        if(cmd.getReviewStatus() != null)
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("reviewStatus", cmd.getReviewStatus()));

		if(cmd.getReviewResult() != null)
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("reviewResult", cmd.getReviewResult()));

        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0l;
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
         } 
        
        List<EquipmentStandardRelationDTO> dtos = new ArrayList<EquipmentStandardRelationDTO>();
        for(Long id : ids) {
        	EquipmentStandardMap map = equipmentProvider.findEquipmentStandardMapById(id);
        	if(map != null) {
	        	EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(map.getTargetId());
	        	EquipmentStandardRelationDTO dto = new EquipmentStandardRelationDTO();
	        	dto.setId(map.getId());
	        	dto.setEquipmentId(equipment.getId());
	        	dto.setTargetId(equipment.getTargetId());
//	        	Organization group = organizationProvider.findOrganizationById(dto.getTargetId());
				Community community = communityProvider.findCommunityById(dto.getTargetId());
	    		if(community != null)
	    			dto.setTargetName(community.getName());
	    		
	    		dto.setEquipmentName(equipment.getName());
	    		dto.setEquipmentModel(equipment.getEquipmentModel());
	    		dto.setStatus(equipment.getStatus());
	    		dto.setStandardId(map.getStandardId());
	    		EquipmentInspectionStandards standard = equipmentProvider.findStandardById(map.getStandardId());
	            if(standard != null) {
	            	dto.setStandardName(standard.getName());
	            }
	            
	            dto.setReviewResult(map.getReviewResult());
	            dto.setReviewStatus(map.getReviewStatus());
				dto.setReviewTime(map.getReviewTime());

				if(map.getReviewerUid() != null && map.getReviewerUid() != 0L) {
					OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(map.getReviewerUid(), equipment.getOwnerId());
					if(member != null) {
						dto.setReviewer(member.getContactName());
					}
				}
	    		dtos.add(dto);
        	}
        }
        
        response.setRelations(dtos);
        return response;
	}

	@Override
	public String getIndexType() {
		return SearchUtils.EQUIPMENTSTANDARDMAP;
	}
	
	private XContentBuilder createDoc(EquipmentStandardMap map){
		try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
           
            b.field("reviewStatus", map.getReviewStatus());
			b.field("reviewResult", map.getReviewResult());
            b.field("status", map.getStatus());
            
            EquipmentInspectionStandards standard = equipmentProvider.findStandardById(map.getStandardId());
            if(standard != null) {
            	b.field("standardNumber", standard.getStandardNumber());
				b.field("namespaceId", standard.getNamespaceId());
            } else {
            	b.field("standardNumber", "");
            }
            	
            EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(map.getTargetId());
            if(equipment != null) {
            	b.field("ownerId", equipment.getOwnerId());
            	b.field("ownerType", equipment.getOwnerType());
				b.field("namespaceId", equipment.getNamespaceId());
            	b.field("targetId", equipment.getTargetId());
            	b.field("targetType", equipment.getTargetType());
            	b.field("equipmentName", equipment.getName());
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
