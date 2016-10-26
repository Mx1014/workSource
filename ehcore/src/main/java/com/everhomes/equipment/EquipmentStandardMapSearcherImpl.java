package com.everhomes.equipment;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.equipment.InspectionStandardMapTargetType;
import com.everhomes.rest.equipment.SearchEquipmentStandardRelationsCommand;
import com.everhomes.rest.equipment.SearchEquipmentStandardRelationsResponse;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.EquipmentStandardMapSearcher;
import com.everhomes.search.SearchUtils;

@Component
public class EquipmentStandardMapSearcherImpl extends AbstractElasticSearch implements
		EquipmentStandardMapSearcher {

	private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentStandardMapSearcherImpl.class);
	
	@Autowired
	private EquipmentProvider equipmentProvider;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIndexType() {
		return SearchUtils.EQUIPMENTSTANDARDMAP;
	}
	
	private XContentBuilder createDoc(EquipmentStandardMap map){
		try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
//            keyword: 查询关键字 
            
           
            b.field("reviewStatus", map.getReviewStatus());
            b.field("status", map.getStatus());
            
            EquipmentInspectionStandards standard = equipmentProvider.findStandardById(map.getStandardId());
            if(standard != null) {
            	b.field("standardNumber", standard.getStandardNumber());
            } else {
            	b.field("standardNumber", "");
            }
            	
            EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(map.getTargetId());
            if(equipment != null) {
            	b.field("ownerId", equipment.getOwnerId());
            	b.field("ownerType", equipment.getOwnerType());
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
