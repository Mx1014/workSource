package com.everhomes.equipment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.techpark.punch.PunchTimeRule;
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
import com.everhomes.rest.equipment.EquipmentStandardsDTO;
import com.everhomes.rest.equipment.EquipmentStatus;
import com.everhomes.rest.equipment.EquipmentsDTO;
import com.everhomes.rest.equipment.ReviewResult;
import com.everhomes.rest.equipment.SearchEquipmentStandardRelationsCommand;
import com.everhomes.rest.equipment.SearchEquipmentStandardRelationsResponse;
import com.everhomes.rest.equipment.SearchEquipmentsCommand;
import com.everhomes.rest.equipment.SearchEquipmentsResponse;
import com.everhomes.rest.quality.OwnerType;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.EquipmentSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.util.ConvertHelper;
import com.everhomes.videoconf.ConfOrders;
import com.mysql.jdbc.StringUtils;

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
        Long privilegeId = configProvider.getLongValue(EquipmentConstant.EQUIPMENT_LIST, 0L);
        userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getOwnerId(), privilegeId);
		SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
		QueryBuilder qb = null;
        if(cmd.getKeyword() == null || cmd.getKeyword().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
            		.field("name", 1.2f)
                    .field("standardName", 1.0f);
            
            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("name").addHighlightedField("standardName");
        }

        FilterBuilder fb = null;
        FilterBuilder nfb = FilterBuilders.termFilter("status", EquipmentStatus.INACTIVE.getCode());
    	fb = FilterBuilders.notFilter(nfb);
        //分公司和总公司的问题，改为用namespaceId来弄 by xiongying20170328
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("namespaceId", UserContext.getCurrentNamespaceId()));
//        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerId", cmd.getOwnerId()));
//        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerType", OwnerType.fromCode(cmd.getOwnerType()).getCode()));
        if(cmd.getTargetId() != null)
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("targetId", cmd.getTargetId()));
        
        if(!StringUtils.isNullOrEmpty(cmd.getTargetType()))
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("targetType", OwnerType.fromCode(cmd.getTargetType()).getCode()));
        
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

	@Override
	public SearchEquipmentStandardRelationsResponse queryEquipmentStandardRelations(
			SearchEquipmentStandardRelationsCommand cmd) {
		SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
		QueryBuilder qb = null;
        if(cmd.getKeyword() == null || cmd.getKeyword().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
            		.field("name", 1.2f)
                    .field("standardName", 1.0f);
            
            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("name").addHighlightedField("standardName");
        }

        FilterBuilder fb = null;
        FilterBuilder nfb = FilterBuilders.termFilter("reviewStatus", EquipmentReviewStatus.DELETE.getCode());
    	fb = FilterBuilders.notFilter(nfb);
        //分公司和总公司的问题，改为用namespaceId来弄
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("namespaceId", UserContext.getCurrentNamespaceId()));
//    	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerId", cmd.getOwnerId()));
//        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerType", OwnerType.fromCode(cmd.getOwnerType()).getCode()));
        if(cmd.getTargetId() != null)
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("targetId", cmd.getTargetId()));
        
        if(!StringUtils.isNullOrEmpty(cmd.getTargetType()))
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("targetType", OwnerType.fromCode(cmd.getTargetType()).getCode()));
        
        if(cmd.getReviewStatus() != null)
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("reviewStatus", cmd.getReviewStatus()));
        
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
//            b.field("ownerId", equipment.getOwnerId());
//            b.field("ownerType", equipment.getOwnerType());
            b.field("namespaceId", equipment.getNamespaceId());
            b.field("targetId", equipment.getTargetId());
            b.field("targetType", equipment.getTargetType());
            b.field("status", equipment.getStatus());
            b.field("categoryId", equipment.getCategoryId());
            b.field("name", equipment.getName());
            b.field("inspectionCategoryId", equipment.getInspectionCategoryId());
//            b.field("reviewResult", equipment.getReviewResult());
//            b.field("reviewStatus", equipment.getReviewStatus());
//            
//            
//            EquipmentInspectionStandards standard = equipmentProvider.findStandardById(equipment.getStandardId(), equipment.getOwnerType(), equipment.getOwnerId());
//            if(null != standard) {
//                b.field("standardName", standard.getName());
//            } else {
//                b.field("standardName", "");
//            }
            
            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create order " + equipment.getId() + " error");
            return null;
        }
    }

}
