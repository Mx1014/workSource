package com.everhomes.organization.pm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.rest.organization.OrganizationMemberStatus;
import com.everhomes.rest.organization.OwnerAddressDTO;
import com.everhomes.rest.organization.pm.OwnerDTO;
import com.everhomes.rest.organization.pm.SearchPMOwnerCommand;
import com.everhomes.rest.organization.pm.SearchPMOwnerResponse;
import com.everhomes.rest.videoconf.EnterpriseUsersDTO;
import com.everhomes.rest.videoconf.ListUsersWithoutVideoConfPrivilegeResponse;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.PMOwnerSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;

public class PMOwnerSearcherImpl extends AbstractElasticSearch implements PMOwnerSearcher {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PMOwnerSearcherImpl.class);
	
	@Autowired
	private PropertyMgrProvider propertyMgrProvider;
	
	@Autowired
	private ConfigurationProvider configProvider;
	
	@Autowired
	private AddressProvider addressProvider;

	@Override
	public void deleteById(Long id) {
		deleteById(id.toString());
	}

	@Override
	public void bulkUpdate(List<CommunityPmOwner> owners) {
		BulkRequestBuilder brb = getClient().prepareBulk();
        for (CommunityPmOwner owner : owners) {
        	
            XContentBuilder source = createDoc(owner);
            if(null != source) {
                LOGGER.info("id:" + owner.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(owner.getId().toString()).source(source));    
                }
            
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }

	}

	@Override
	public void feedDoc(CommunityPmOwner owner) {
		XContentBuilder source = createDoc(owner);
        
        feedDoc(owner.getId().toString(), source);
	}

	@Override
	public void syncFromDb() {
        this.deleteAll();
        
        List<CommunityPmOwner> owners = propertyMgrProvider.listCommunityPmOwners(null, null);
        
        if(owners.size() > 0) {
            this.bulkUpdate(owners);
        }
        
        this.optimize(1);
        this.refresh();
        
        LOGGER.info("sync for organization owner ok");

	}

	@Override
	public SearchPMOwnerResponse query(SearchPMOwnerCommand cmd) {
		SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
		QueryBuilder qb = null;
        if(cmd.getKeyword() == null || cmd.getKeyword().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
                    .field("contactToken", 5.0f)
                    .field("contactName", 2.0f);
            
            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("contactToken").addHighlightedField("contactName");
        }

        
        FilterBuilder fb = FilterBuilders.termFilter("communityId", cmd.getCommunityId());
        
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
        
        SearchPMOwnerResponse response = new SearchPMOwnerResponse();
        if(ids.size() > pageSize) {
        	response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
         } else {
        	 response.setNextPageAnchor(null);
            }
        
        Map<String, OwnerDTO> map = new HashMap<String, OwnerDTO>();
        List<CommunityPmOwner> pmOwners = propertyMgrProvider.listCommunityPmOwners(ids);
        for(CommunityPmOwner pmOwner : pmOwners) {
        	OwnerDTO dto = map.get(pmOwner.getContactToken());
        	if(null == dto) {
        		dto = new OwnerDTO();
        		List<OwnerAddressDTO> ownerAddresses = new ArrayList<OwnerAddressDTO>();
        		
        		Address address = addressProvider.findAddressById(pmOwner.getAddressId());
        		OwnerAddressDTO ownerAddress = new OwnerAddressDTO();
        		ownerAddress.setId(pmOwner.getId());
        		ownerAddress.setAddressId(address.getId());
        		ownerAddress.setApartmentName(address.getApartmentName());
        		ownerAddress.setBuildingName(address.getBuildingName());
        		ownerAddresses.add(ownerAddress);
        		
        		dto.setOwnerAddresses(ownerAddresses);
        		dto.setIdentifierToken(pmOwner.getContactToken());
        		dto.setOwnerName(pmOwner.getContactName());
        	} else {
        		List<OwnerAddressDTO> ownerAddresses = dto.getOwnerAddresses();
        		
        		Address address = addressProvider.findAddressById(pmOwner.getAddressId());
        		OwnerAddressDTO ownerAddress = new OwnerAddressDTO();
        		ownerAddress.setId(pmOwner.getId());
        		ownerAddress.setAddressId(address.getId());
        		ownerAddress.setApartmentName(address.getApartmentName());
        		ownerAddress.setBuildingName(address.getBuildingName());
        		ownerAddresses.add(ownerAddress);
        		
        		dto.setOwnerAddresses(ownerAddresses);
        	}
        	
        	map.put(pmOwner.getContactToken(), dto);
        }
        
        List<OwnerDTO> owners = map.values().stream().collect(Collectors.toList());
        response.setOwners(owners);
        
        return response;
	}

	@Override
	public String getIndexType() {
		return SearchUtils.PMOWNERINDEXTYPE;
	}
	
	private XContentBuilder createDoc(CommunityPmOwner owner){
		try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("contactToken", owner.getContactToken());
            b.field("contactName", owner.getContactName());
            b.field("communityId", owner.getCommunityId());
        
            b.endObject();
            return b;
		} catch (IOException ex) {
			LOGGER.error("Create owner " + owner.getId() + " error");
			return null;
		}
	}

}
