package com.everhomes.videoconf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import com.everhomes.enterprise.EnterpriseContact;
import com.everhomes.enterprise.EnterpriseContactEntry;
import com.everhomes.enterprise.EnterpriseContactProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.videoconf.EnterpriseUsersDTO;
import com.everhomes.rest.videoconf.ListUsersWithoutVideoConfPrivilegeCommand;
import com.everhomes.rest.videoconf.ListUsersWithoutVideoConfPrivilegeResponse;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.SearchUtils;
import com.everhomes.search.UserWithoutConfAccountSearcher;
import com.everhomes.settings.PaginationConfigHelper;

@Component
public class UserWithoutConfAccountSearcherImpl extends AbstractElasticSearch
		implements UserWithoutConfAccountSearcher {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserWithoutConfAccountSearcherImpl.class);
	
	@Autowired
	private EnterpriseContactProvider enterpriseContactProvider;
	
	@Autowired
    private ConfigurationProvider configProvider;
	
	@Override
	public void deleteById(Long id) {
		deleteById(id.toString());
	}

	@Override
	public void bulkUpdate(List<EnterpriseContact> contacts) {
		BulkRequestBuilder brb = getClient().prepareBulk();
        for (EnterpriseContact contact : contacts) {
        	
            XContentBuilder source = createDoc(contact);
            if(null != source) {
                LOGGER.info("id:" + contact.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(contact.getId().toString()).source(source));    
                }
            
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }

	}

	@Override
	public void feedDoc(EnterpriseContact contact) {
		XContentBuilder source = createDoc(contact);
        
        feedDoc(contact.getId().toString(), source);
	}

	@Override
	public void syncFromDb() {
		int pageSize = 200;      
        this.deleteAll();
        
        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<EnterpriseContact> contacts = enterpriseContactProvider.queryContact(locator, pageSize);
            
            if(contacts.size() > 0) {
                this.bulkUpdate(contacts);
            }
            
            if(locator.getAnchor() == null) {
                break;
            }
        }
        
        this.optimize(1);
        this.refresh();
        
        LOGGER.info("sync for enterprise contact ok");

	}

	@Override
	public ListUsersWithoutVideoConfPrivilegeResponse query(ListUsersWithoutVideoConfPrivilegeCommand cmd) {
		SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
		QueryBuilder qb = null;
        if(cmd.getKeyword() == null || cmd.getKeyword().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
                    .field("userName", 5.0f)
                    .field("department", 2.0f)
                    .field("contact", 1.0f);
            
            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("userName").addHighlightedField("department").addHighlightedField("contact");
        }

        FilterBuilder fb = null;
        if(cmd.getEnterpriseId() != null)
        	fb = FilterBuilders.termFilter("enterpriseId", cmd.getEnterpriseId());
        
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
        
        ListUsersWithoutVideoConfPrivilegeResponse listUsers = new ListUsersWithoutVideoConfPrivilegeResponse();
        if(ids.size() > pageSize) {
        	listUsers.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
         } else {
        	 listUsers.setNextPageAnchor(null);
            }
        
        List<EnterpriseUsersDTO> enterpriseUsers = new ArrayList<EnterpriseUsersDTO>();
        for(Long id : ids) {
        	EnterpriseUsersDTO user = new EnterpriseUsersDTO();
        	EnterpriseContact contact = enterpriseContactProvider.getContactById(id);
        	user.setUserId(contact.getUserId());
			user.setUserName(contact.getName());
			user.setDepartment(contact.getStringTag1());
			user.setContactId(contact.getId());
			user.setEnterpriseId(contact.getEnterpriseId());
			
			List<EnterpriseContactEntry> entry = enterpriseContactProvider.queryContactEntryByContactId(contact);
			if(entry != null && entry.size() >0)
				user.setMobile(entry.get(0).getEntryValue());
			
			enterpriseUsers.add(user);
        }
        listUsers.setEnterpriseUsers(enterpriseUsers);
        
        return listUsers;
	}

	@Override
	public String getIndexType() {
		return SearchUtils.ENTERPRISECONTACTINDEXTYPE;
	}
	
	private XContentBuilder createDoc(EnterpriseContact contact){
		try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("contactId", contact.getId());
            b.field("userId", contact.getUserId());
            b.field("enterpriseId", contact.getEnterpriseId());
            b.field("department", contact.getStringTag1());
            b.field("userName", contact.getName());
            
            List<EnterpriseContactEntry> entry = enterpriseContactProvider.queryContactEntryByContactId(contact);
			if(entry != null && entry.size() >0) {
                b.field("contact", entry.get(0).getEntryValue());
            } else {
                b.field("contact", "");
            }
          
            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create contact " + contact.getId() + " error");
            return null;
        }
    }

}
