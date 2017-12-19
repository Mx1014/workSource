package com.everhomes.videoconf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.enterprise.EnterpriseContact;
import com.everhomes.enterprise.EnterpriseContactEntry;
import com.everhomes.enterprise.EnterpriseContactGroup;
import com.everhomes.enterprise.EnterpriseContactGroupMember;
import com.everhomes.enterprise.EnterpriseContactProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationMemberStatus;
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
	private VideoConfProvider vcProvider;
	
	@Autowired
    private ConfigurationProvider configProvider;
	
	@Autowired
    private OrganizationProvider organizationProvider;
	
	@Autowired
    private OrganizationService organizationService;
	
	@Override
	public void deleteById(Long id) {
		deleteById(id.toString());
	}

	@Override
	public void bulkUpdate(List<OrganizationMember> members) {
		BulkRequestBuilder brb = getClient().prepareBulk();
        for (OrganizationMember member : members) {
        	
            XContentBuilder source = createDoc(member);
            if(null != source) {
                LOGGER.info("id:" + member.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(member.getId().toString()).source(source));    
                }
            
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }

	}

	@Override
	public void feedDoc(OrganizationMember member) {
		XContentBuilder source = createDoc(member);
        
        feedDoc("" + member.getOrganizationId() + "-" + member.getTargetId(), source);
	}

	@Override
	public void syncFromDb() {
		int pageSize = 200;      
        this.deleteAll();
        
        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<OrganizationMember> members = organizationProvider.listOrganizationMembersTargetIdExist();
            
            if(members.size() > 0) {
                this.bulkUpdate(members);
            }
            
            if(locator.getAnchor() == null) {
                break;
            }
        }
        
        this.optimize(1);
        this.refresh();
        
        LOGGER.info("sync for organization member ok");

	}

	@Override
	public ListUsersWithoutVideoConfPrivilegeResponse query(ListUsersWithoutVideoConfPrivilegeCommand cmd) {
		List<Long> userIds = vcProvider.findUsersByEnterpriseId(cmd.getEnterpriseId());
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

        FilterBuilder nfb = FilterBuilders.termsFilter("userId", userIds);
        
        FilterBuilder fb = FilterBuilders.notFilter(nfb);
        
        if(cmd.getEnterpriseId() != null)
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("enterpriseId", cmd.getEnterpriseId()));

//        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        int pageSize = cmd.getPageSize()==null?1000:cmd.getPageSize();
        Long anchor = 0l;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }
        
        qb = QueryBuilders.filteredQuery(qb, fb);
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        builder.setFrom(anchor.intValue() * pageSize).setSize(pageSize + 1);
        builder.setQuery(qb);
        
        SearchResponse rsp = builder.execute().actionGet();
        
        ListUsersWithoutVideoConfPrivilegeResponse listUsers = new ListUsersWithoutVideoConfPrivilegeResponse();
        //直接从搜索引擎里面获取信息 by sfyan 20160815
        List<EnterpriseUsersDTO> enterpriseUsers = new ArrayList<EnterpriseUsersDTO>();
        SearchHit[] docs = rsp.getHits().getHits();
        for (SearchHit sd : docs) {
            try {
            	EnterpriseUsersDTO user = new EnterpriseUsersDTO();
            	Map<String, Object> source = sd.getSource();
            	user.setUserId(SearchUtils.getLongField(source.get("userId")));
				user.setUserName(String.valueOf(source.get("userName")));
				user.setEnterpriseId(SearchUtils.getLongField(source.get("enterpriseId")));
				user.setMobile(String.valueOf(source.get("contact")));
				user.setDepartment(String.valueOf(source.get("department")));
				enterpriseUsers.add(user);
            }catch(Exception ex) {
            	LOGGER.error("query organization member error, cmd = {}", cmd);
            }
        }
        
       
//        for(Long id : ids) {
//        	
//        	
//        	EnterpriseUsersDTO user = new EnterpriseUsersDTO();
//        	OrganizationMember member = organizationProvider.findOrganizationMemberById(id);
//        	if(member != null) {
//	        	user.setUserId(member.getTargetId());
//				user.setUserName(member.getContactName());
//				user.setEnterpriseId(member.getOrganizationId());
//				user.setMobile(member.getContactToken());
//				
//        	EnterpriseContact contact = enterpriseContactProvider.getContactById(id);
//        	user.setUserId(contact.getUserId());
//			user.setUserName(contact.getName());
//			user.setContactId(contact.getId());
//			user.setEnterpriseId(contact.getEnterpriseId());
//			
//			EnterpriseContactGroupMember member = enterpriseContactProvider.getContactGroupMemberByContactId(contact.getEnterpriseId(), contact.getId());
//			if (member != null) {
//				EnterpriseContactGroup group = enterpriseContactProvider.getContactGroupById(member.getContactGroupId());
//				if (group != null) {
//					user.setDepartment(group.getName());
//				}
//			}
//			
//			List<EnterpriseContactEntry> entry = enterpriseContactProvider.queryContactEntryByContactId(contact);
//			if(entry != null && entry.size() >0)
//				user.setMobile(entry.get(0).getEntryValue());
			
//				enterpriseUsers.add(user);
//        	}
//        }
        listUsers.setEnterpriseUsers(enterpriseUsers);
        
        return listUsers;
	}

	@Override
	public String getIndexType() {
		return SearchUtils.ENTERPRISECONTACTINDEXTYPE;
	}
	
	private XContentBuilder createDoc(OrganizationMember member){
		try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            if(OrganizationMemberStatus.ACTIVE.getCode()==member.getStatus()) {
//	            b.field("id", member.getId());
	            b.field("userId", member.getTargetId());
	            b.field("enterpriseId", member.getOrganizationId());
	            b.field("userName", member.getContactName());
	            b.field("contact", member.getContactToken());
	            String department = "";
	            Organization organization = organizationProvider.findOrganizationById(member.getOrganizationId());
	            if(null != organization){
	            	List<OrganizationDTO> organizationDTOs = organizationService.getOrganizationMemberGroups(OrganizationGroupType.DEPARTMENT, member.getContactToken(), organization.getPath());
	            	for (OrganizationDTO organizationDTO : organizationDTOs) {
	            		department += "|" + organizationDTO.getName();
					}
	            	if(!StringUtils.isEmpty(department)){
	            		department = department.substring(1);
	            	}
	            }
	            b.field("department", department);
            }
            
            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create member " + member.getId() + " error");
            return null;
        }
    }

}
