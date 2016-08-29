package com.everhomes.videoconf;

import java.io.IOException;
import java.sql.Timestamp;
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

import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.enterprise.Enterprise;
import com.everhomes.enterprise.EnterpriseProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.organization.ListOrganizationAdministratorCommand;
import com.everhomes.rest.organization.ListOrganizationMemberCommandResponse;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.videoconf.ConfAccountDTO;
import com.everhomes.rest.videoconf.ConfServiceErrorCode;
import com.everhomes.rest.videoconf.EnterpriseConfAccountDTO;
import com.everhomes.rest.videoconf.ListEnterpriseVideoConfAccountResponse;
import com.everhomes.rest.videoconf.ListEnterpriseWithVideoConfAccountCommand;
import com.everhomes.rest.videoconf.ListEnterpriseWithVideoConfAccountResponse;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.ConfEnterpriseSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import com.mysql.jdbc.StringUtils;

@Component
public class ConfEnterpriseSearcherImpl extends AbstractElasticSearch implements
		ConfEnterpriseSearcher {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfEnterpriseSearcherImpl.class);
	
	@Autowired
	private VideoConfProvider vcProvider;
	
	@Autowired
    private ConfigurationProvider configProvider;
	
//	@Autowired
//	private EnterpriseProvider enterpriseProvider;
	
	@Autowired
	private OrganizationProvider organizationProvider;
	
	@Autowired
    private RolePrivilegeService rolePrivilegeService;
	
	@Autowired
    private NamespaceProvider nsProvider;
	
	@Autowired
	private LocaleStringService localeStringService;
	
	@Override
	public void deleteById(Long id) {
		deleteById(id.toString());
	}

	@Override
	public void bulkUpdate(List<ConfEnterprises> enterprises) {
		BulkRequestBuilder brb = getClient().prepareBulk();
        for (ConfEnterprises enterprise : enterprises) {
        	
            XContentBuilder source = createDoc(enterprise);
            if(null != source) {
                LOGGER.info("conf enterprise id:" + enterprise.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(enterprise.getId().toString()).source(source));    
                }
            
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
	}

	@Override
	public void feedDoc(ConfEnterprises enterprise) {

		XContentBuilder source = createDoc(enterprise);
        
        feedDoc(enterprise.getId().toString(), source);

	}

	@Override
	public void syncFromDb() {
		int pageSize = 200;      
        this.deleteAll();
        
        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
        	List<ConfEnterprises> enterprises = vcProvider.listEnterpriseWithVideoConfAccount(null, null, locator, pageSize);
            
            if(enterprises.size() > 0) {
                this.bulkUpdate(enterprises);
            }
            
            if(locator.getAnchor() == null) {
                break;
            }
        }

        this.optimize(1);
        this.refresh();
        
        LOGGER.info("sync for conference enterprise ok");
	}

	@Override
	public ListEnterpriseWithVideoConfAccountResponse query(
			ListEnterpriseWithVideoConfAccountCommand cmd) {
		SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
		QueryBuilder qb = null;
        if(cmd.getKeyword() == null || cmd.getKeyword().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else if(org.apache.commons.lang.StringUtils.isNumeric(cmd.getKeyword())) {
        	qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
                    .field("enterpriseId", 5.0f);
            
            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("enterpriseId");
        }else {
            qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
                    .field("enterpriseName", 2.0f)
                    .field("enterpriseDisplayName", 1.0f);
            
            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("enterpriseId").addHighlightedField("enterpriseName").addHighlightedField("enterpriseDisplayName");
        }

        FilterBuilder fb = FilterBuilders.termFilter("deleteStatus", 1);
        
        if(cmd.getStatus() != null) {
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("status", cmd.getStatus()));
        }
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0l;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }
        
        if(null != fb) {
            qb = QueryBuilders.filteredQuery(qb, fb);
        }
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        builder.setFrom(anchor.intValue() * pageSize).setSize(pageSize + 1);
        builder.setQuery(qb);
        
        SearchResponse rsp = builder.execute().actionGet();

        List<Long> ids = getIds(rsp);
        
        ListEnterpriseWithVideoConfAccountResponse response = new ListEnterpriseWithVideoConfAccountResponse();
        if(ids.size() > pageSize) {
        	response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
         } else {
        	 response.setNextPageAnchor(null);
            }
        
        List<EnterpriseConfAccountDTO> enterpriseConfAccounts = new ArrayList<EnterpriseConfAccountDTO>();
        for(Long id : ids) {
        	EnterpriseConfAccountDTO dto = new EnterpriseConfAccountDTO();
        	ConfEnterprises confEnterprise = vcProvider.findConfEnterpriseById(id);
        	dto.setId(confEnterprise.getId());
	    	dto.setEnterpriseId(confEnterprise.getEnterpriseId());
	    	dto.setNamespaceId(confEnterprise.getNamespaceId());
	    	if(dto.getNamespaceId() == 0) {
	    		dto.setNamespaceName(localeStringService.getLocalizedString(String.valueOf(ConfServiceErrorCode.SCOPE), 
						String.valueOf(ConfServiceErrorCode.ZUOLIN_NAMESPACE_NAME),
						UserContext.current().getUser().getLocale(),"ZUOLIN"));
			} else {
		    	Namespace ns = nsProvider.findNamespaceById(confEnterprise.getNamespaceId());
				if(ns != null)
					dto.setNamespaceName(ns.getName());
			}
			
//	    	Enterprise enterprise = enterpriseProvider.findEnterpriseById(confEnterprise.getEnterpriseId());
	    	Organization org = organizationProvider.findOrganizationById(confEnterprise.getEnterpriseId());
	    	
	    	if(org != null) {
	    		dto.setEnterpriseName(org.getName());
		    	dto.setEnterpriseDisplayName(org.getName());
		    	
		    	ListOrganizationAdministratorCommand orgAdminCmd = new ListOrganizationAdministratorCommand();
		    	orgAdminCmd.setOrganizationId(org.getId());
		    	ListOrganizationMemberCommandResponse res = rolePrivilegeService.listOrganizationAdministrators(orgAdminCmd);
		    	if(res != null && res.getMembers() != null && res.getMembers().size() > 0) {
		    		OrganizationMemberDTO member = res.getMembers().get(0);
		    		dto.setEnterpriseContactor(member.getContactName());
			    	dto.setMobile(member.getContactToken());
		    	}
		    	
	    	}
	    		
//	    	Organization org = organizationProvider.findOrganizationById(confEnterprise.getEnterpriseId());
//	    	
//	    	if(org != null) {
//	    		dto.setEnterpriseName(org.getName());
//		    	dto.setEnterpriseDisplayName(org.getName());
//	    	}
//	    	dto.setEnterpriseContactor(confEnterprise.getContactName());
//	    	dto.setMobile(confEnterprise.getContact());

	    	if(confEnterprise.getActiveAccountAmount() != 0 && confEnterprise.getActiveAccountAmount() > confEnterprise.getTrialAccountAmount())
	    		dto.setUseStatus((byte) 0);
	    	if(confEnterprise.getActiveAccountAmount() != 0 && confEnterprise.getActiveAccountAmount() == confEnterprise.getTrialAccountAmount()) {
	    		dto.setUseStatus((byte) 1);
	    	}
	    	if(confEnterprise.getActiveAccountAmount() == 0) {
	    		dto.setUseStatus((byte) 2);
	    	}
	    	
	    	dto.setStatus(confEnterprise.getStatus());
	    	dto.setTotalAccount(confEnterprise.getAccountAmount());
	    	dto.setValidAccount(confEnterprise.getActiveAccountAmount());
	    	dto.setBuyChannel(confEnterprise.getBuyChannel());
			
	    	enterpriseConfAccounts.add(dto);
        }
        response.setEnterpriseConfAccounts(enterpriseConfAccounts);
        
        return response;
	}

	@Override
	public String getIndexType() {
		return SearchUtils.CONFENTERPRISEINDEXTYPE;
	}
	
	private XContentBuilder createDoc(ConfEnterprises enterprise){
		try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("enterpriseId", enterprise.getEnterpriseId());
            b.field("deleteStatus", enterprise.getStatus());
            
            //status: 状态 0-formally use 1-on trial 2-overdue
            if(enterprise.getActiveAccountAmount() == null)
            	enterprise.setActiveAccountAmount(0);
            if(enterprise.getTrialAccountAmount() == null)
            	enterprise.setTrialAccountAmount(0);
            
        	if(enterprise.getActiveAccountAmount() != 0 && enterprise.getActiveAccountAmount() > enterprise.getTrialAccountAmount()) {
        		b.field("status", 0);
        	}
			if(enterprise.getActiveAccountAmount() != 0 && enterprise.getActiveAccountAmount() == enterprise.getTrialAccountAmount()) {
				b.field("status", 1);
			}
			if(enterprise.getActiveAccountAmount() == 0) {
				b.field("status", 2);
			}
			
			Organization org = organizationProvider.findOrganizationById(enterprise.getEnterpriseId());
//          Enterprise enter = enterpriseProvider.findEnterpriseById(enterprise.getEnterpriseId());
          
			if(null != org) {
			    b.field("enterpriseName", org.getName());
			    b.field("enterpriseDisplayName", org.getName());
            } else {
                b.field("enterpriseName", "");
                b.field("enterpriseDisplayName", "");
            }
            
            
            
            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create ConfEnterprises " + enterprise.getId() + " error");
            return null;
        }
    }

}
