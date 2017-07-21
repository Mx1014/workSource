package com.everhomes.equipment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.everhomes.entity.EntityType;
import com.everhomes.rest.equipment.*;
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
import com.everhomes.repeat.RepeatService;
import com.everhomes.repeat.RepeatSettings;
import com.everhomes.rest.repeat.RepeatSettingsDTO;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.EquipmentStandardSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.util.ConvertHelper;

@Component
public class EquipmentStandardSearcherImpl extends AbstractElasticSearch implements EquipmentStandardSearcher{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentStandardSearcherImpl.class);

	@Autowired
	private EquipmentProvider equipmentProvider;
	
	@Autowired
	private ConfigurationProvider configProvider;
	
	@Autowired
	private RepeatService repeatService;

    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;
	
	@Override
	public void deleteById(Long id) {
		deleteById(id.toString());
	}

	@Override
	public void bulkUpdate(List<EquipmentInspectionStandards> standards) {
		BulkRequestBuilder brb = getClient().prepareBulk();
        for (EquipmentInspectionStandards standard : standards) {
        	
            XContentBuilder source = createDoc(standard);
            if(null != source) {
                LOGGER.info("equipment inspection standard id:" + standard.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(standard.getId().toString()).source(source));    
                }
            
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
	}

	@Override
	public void feedDoc(EquipmentInspectionStandards standard) {
		XContentBuilder source = createDoc(standard);
        
        feedDoc(standard.getId().toString(), source);
	}

	@Override
	public void syncFromDb() {
		int pageSize = 200;      
        this.deleteAll();
        
        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<EquipmentInspectionStandards> standards = equipmentProvider.listEquipmentInspectionStandards(locator, pageSize);
            
            if(standards.size() > 0) {
                this.bulkUpdate(standards);
            }
            
            if(locator.getAnchor() == null) {
                break;
            }
        }

        this.optimize(1);
        this.refresh();
        
        LOGGER.info("sync for equipment inspection standard ok");
	}

	@Override
	public SearchEquipmentStandardsResponse query(
			SearchEquipmentStandardsCommand cmd) {
        Long privilegeId = configProvider.getLongValue(EquipmentConstant.EQUIPMENT_STANDARD_LIST, 0L);
        if(cmd.getTargetId() != null) {
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
            		.field("standardName", 1.2f)
                    .field("standardNumber", 1.0f);
            
            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("standardName").addHighlightedField("standardNumber");
        }

        FilterBuilder fb = null;
        FilterBuilder nfb = FilterBuilders.termFilter("status", EquipmentStandardStatus.INACTIVE.getCode());
    	fb = FilterBuilders.notFilter(nfb);

        // 改用namespaceId by xiongying20170328
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("namespaceId", UserContext.getCurrentNamespaceId()));
//    	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerId", cmd.getOwnerId()));
//        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerType", OwnerType.fromCode(cmd.getOwnerType()).getCode()));
        if(cmd.getTargetId() != null) {
            FilterBuilder tfb = FilterBuilders.termFilter("targetId", cmd.getTargetId());
            if(TargetIdFlag.YES.equals(TargetIdFlag.fromStatus(cmd.getTargetIdFlag()))) {
                tfb = FilterBuilders.orFilter(tfb, FilterBuilders.termFilter("targetId", 0));
            }
            fb = FilterBuilders.andFilter(fb, tfb);
        }

        if(cmd.getStandardType() != null)
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("standardType", cmd.getStandardType()));
        
        if(cmd.getStatus() != null)
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("status", cmd.getStatus()));
        
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
            LOGGER.info("EquipmentStandardSearcherImpl query builder ："+builder);

        SearchResponse rsp = builder.execute().actionGet();

        if(LOGGER.isDebugEnabled())
            LOGGER.info("EquipmentStandardSearcherImpl query rsp ："+rsp);

        List<Long> ids = getIds(rsp);
        
        Long nextPageAnchor = null;
        if(ids.size() > pageSize) {
        	nextPageAnchor = anchor + 1;
            ids.remove(ids.size() - 1);
         } 
        
        List<EquipmentStandardsDTO> eqStandards = new ArrayList<EquipmentStandardsDTO>();
        for(Long id : ids) {
        	EquipmentInspectionStandards standard = equipmentProvider.findStandardById(id);
        	if(standard != null) {
        		processRepeatSetting(standard);
        		EquipmentStandardsDTO dto = ConvertHelper.convert(standard, EquipmentStandardsDTO.class);
        		if(null != standard.getRepeat()) {
    	    		RepeatSettingsDTO rs = ConvertHelper.convert(standard.getRepeat(), RepeatSettingsDTO.class);
    	    		dto.setRepeat(rs);
        		}
        		eqStandards.add(dto);
        	}
        	
        }
        
        return new SearchEquipmentStandardsResponse(nextPageAnchor, eqStandards);
	}

	private void processRepeatSetting(EquipmentInspectionStandards standard) {
		if(null != standard.getRepeatSettingId() && standard.getRepeatSettingId() != 0) {
			RepeatSettings repeat = repeatService.findRepeatSettingById(standard.getRepeatSettingId());
			standard.setRepeat(repeat);
		}
	}
	
	@Override
	public String getIndexType() {
		return SearchUtils.EQUIPMENTSTANDARDINDEXTYPE;
	}
	
	private XContentBuilder createDoc(EquipmentInspectionStandards standard){
		try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("standardName", standard.getName());
            b.field("standardNumber", standard.getStandardNumber());
            b.field("ownerId", standard.getOwnerId());
            b.field("ownerType", standard.getOwnerType());
            b.field("standardType", standard.getStandardType());
            b.field("inspectionCategoryId", standard.getInspectionCategoryId());
            b.field("status", standard.getStatus());
            b.field("namespaceId", standard.getNamespaceId());

            if(standard.getTargetId() != null) {
                b.field("targetId", standard.getTargetId());
            } else {
                b.field("targetId", 0);
            }

            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create standard " + standard.getId() + " error");
            return null;
        }
    }

}
