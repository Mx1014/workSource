package com.everhomes.equipment;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.equipment.EquipmentAccessoriesDTO;
import com.everhomes.rest.equipment.SearchEquipmentAccessoriesCommand;
import com.everhomes.rest.equipment.SearchEquipmentAccessoriesResponse;
import com.everhomes.rest.quality.OwnerType;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.EquipmentAccessoriesSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;
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
public class EquipmentAccessoriesSearcherImpl extends AbstractElasticSearch implements
		EquipmentAccessoriesSearcher {

	private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentAccessoriesSearcherImpl.class);
			
	@Autowired
	private EquipmentProvider equipmentProvider;
	
	@Autowired
	private ConfigurationProvider configProvider;
	
	@Autowired
	private OrganizationProvider organizationProvider;

    @Autowired
    private CommunityProvider communityProvider;
	
	@Override
	public void deleteById(Long id) {
		deleteById(id.toString());
	}

	@Override
	public void bulkUpdate(List<EquipmentInspectionAccessories> accessories) {
		BulkRequestBuilder brb = getClient().prepareBulk();
        for (EquipmentInspectionAccessories accessory : accessories) {
        	
            XContentBuilder source = createDoc(accessory);
            if(null != source) {
                LOGGER.info("equipment accessory id:" + accessory.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(accessory.getId().toString()).source(source));    
                }
            
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
	}

	@Override
	public void feedDoc(EquipmentInspectionAccessories accessory) {
		XContentBuilder source = createDoc(accessory);
        
        feedDoc(accessory.getId().toString(), source);
	}

	@Override
	public void syncFromDb() {
		int pageSize = 200;      
        this.deleteAll();
        
        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<EquipmentInspectionAccessories> accessories = equipmentProvider.listEquipmentInspectionAccessories(locator, pageSize);
            
            if(accessories.size() > 0) {
                this.bulkUpdate(accessories);
            }
            
            if(locator.getAnchor() == null) {
                break;
            }
        }

        this.optimize(1);
        this.refresh();
        
        LOGGER.info("sync for equipment accessory ok");
	}

	@Override
	public SearchEquipmentAccessoriesResponse query(
			SearchEquipmentAccessoriesCommand cmd) {
		SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
		QueryBuilder qb = null;
        if(cmd.getKeyword() == null || cmd.getKeyword().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
            		.field("name", 5.0f)
                    .field("name.pinyin_prefix", 2.0f)
                    .field("name.pinyin_gram", 1.0f);
            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("name");

        }

        FilterBuilder fb = FilterBuilders.termFilter("namespaceId", cmd.getNamespaceId());

        if (cmd.getTargetId() != null && cmd.getTargetId() != 0L) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("targetId", cmd.getTargetId()));
            if (!StringUtils.isNullOrEmpty(cmd.getTargetType()))
                fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("targetType", OwnerType.fromCode(cmd.getTargetType()).getCode()));
        }else if(cmd.getTargetIds()!=null && cmd.getTargetIds().size()>0){
            // only all scope field term with ownerId followed by this rule
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termsFilter("targetId", cmd.getTargetIds()));
        }

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
        
        SearchEquipmentAccessoriesResponse response = new SearchEquipmentAccessoriesResponse();
        if(ids.size() > pageSize) {
        	response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
         } else {
        	 response.setNextPageAnchor(null);
            }
        
        List<EquipmentAccessoriesDTO> accessories = new ArrayList<EquipmentAccessoriesDTO>();
        for (Long id : ids) {
            EquipmentInspectionAccessories accessory = equipmentProvider.findAccessoryById(id);
            if (accessory != null) {
                EquipmentAccessoriesDTO dto = ConvertHelper.convert(accessory, EquipmentAccessoriesDTO.class);
                Community community = communityProvider.findCommunityById(dto.getTargetId());
                if (community != null)
                    dto.setTargetName(community.getName());
                accessories.add(dto);
            }
        }
        response.setAccessories(accessories);
        
        return response;
	}

	@Override
	public String getIndexType() {
		return SearchUtils.EQUIPMENTACCESSORYINDEXTYPE;
	}
	
	private XContentBuilder createDoc(EquipmentInspectionAccessories accessory){
		try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("namespaceId", accessory.getNamespaceId());
            b.field("ownerId", accessory.getOwnerId());
            b.field("ownerType", accessory.getOwnerType());
            b.field("targetId", accessory.getTargetId());
            b.field("targetType", accessory.getTargetType());
            b.field("name", accessory.getName());

            
            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create accessory " + accessory.getId() + " error");
            return null;
        }
    }

}
