package com.everhomes.organization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.rest.organization.ListOrganizationAdministratorCommand;
import com.everhomes.rest.organization.ListOrganizationMemberCommandResponse;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.organization.SearchOrganizationCommand;
import com.everhomes.rest.search.GroupQueryResult;
import com.everhomes.rest.search.OrganizationQueryResult;
import com.everhomes.rest.videoconf.ConfServiceErrorCode;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.GroupQueryFilter;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;

@Service
public class OrganizationSearcherImpl extends AbstractElasticSearch implements OrganizationSearcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationSearcherImpl.class);
    
    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
    private ConfigurationProvider  configProvider;
	
	@Autowired
    private NamespaceProvider nsProvider;
	
	@Autowired
	private LocaleStringService localeStringService;
	
	@Autowired
    private RolePrivilegeService rolePrivilegeService;

    @Override
    public String getIndexType() {
        return SearchUtils.ENTERPRISEINDEXTYPE;
    }
    
    private static Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
    
    public static boolean isContainChinese(String str) {
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
    
    private XContentBuilder createDoc(Organization organization){
        try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("namespaceId", organization.getNamespaceId());
            Long communityId = organizationService.getOrganizationActiveCommunityId(organization.getId());
            b.field("communityId", communityId);
            b.field("name", organization.getName());
            //保证后面只能筛选出普通企业（ 注：不包含物业等机构），且一般不会改动，所以加入搜索引擎， by sfyan 20160523
            b.field("organizationType", organization.getOrganizationType());
            b.field("description", organization.getDescription());
            b.field("createTime", organization.getCreateTime());
            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create group " + organization.getId() + " error");
            return null;
        }
    }
    
    @Override
    public void deleteById(Long id) {
        deleteById(id.toString());
        
    }
    
    @Override
    public void bulkUpdate(List<Organization> organizations) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (Organization organization : organizations) {
        	if(null == organization){
        		continue;
        	}
            XContentBuilder source = createDoc(organization);
            if(null != source) {
                LOGGER.info("id:" + organization.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(organization.getId().toString()).source(source));    
                }
            
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }
    
    @Override
    public void feedDoc(Organization organization) {
        XContentBuilder source = createDoc(organization);
        
        feedDoc(organization.getId().toString(), source);
        
    }
    
    @Override
    public void syncFromDb() {
        this.deleteAll();
        
        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            
        	List<Organization> organizations = organizationService.getSyncDatas(locator);
        	
            if(organizations.size() > 0) {
                this.bulkUpdate(organizations);
            }
            
            if(locator.getAnchor() == null) {
                break;
            }
        }
        
        this.optimize(1);
        this.refresh();
        
        LOGGER.info("sync for company ok");
    }

    @Override
    public GroupQueryResult query(SearchOrganizationCommand cmd) {
        GroupQueryFilter filter = new GroupQueryFilter();
        int pageNum = 0;
        if(cmd.getPageAnchor() != null) {
            pageNum = cmd.getPageAnchor().intValue();
        }
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

       SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        
        QueryBuilder qb;
        
        if(StringUtils.isEmpty(cmd.getKeyword())) {
            qb = QueryBuilders.matchAllQuery();
        }else if(isContainChinese(cmd.getKeyword())){//增加中文名称的权重 by xiongying20170524 中文就字符匹配，英文就加上拼音匹配， by dengs 20170524
        	qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
        		.field("name", 9.0f);
        }else{
        	qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
                .field("name", 9.0f)
                .field("name.pinyin_prefix", 2.0f)
                .field("name.pinyin_gram", 1.0f);    
        }
        
//        FilterBuilder fb = null;
//
//        if(null == fb) {
//            fb = FilterBuilders.termFilter("communityType", t.getCode());
//        } else {
//            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("communityType", EnterpriseCommunityType.Normal.getCode()));
//        }
        
//        if(null != fb) {
//            qb = QueryBuilders.filteredQuery(qb, fb);
//        }
        
      //namespaceId by xiongying 20160613
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        FilterBuilder fb = FilterBuilders.termFilter("namespaceId", namespaceId);
        
        // 每个企业（含物业管理公司）都有可能在某个园区内，当客户端提供园区作为过滤条件时，则在园区范围内挑选园区 by lqs 20160512
        if(cmd.getCommunityId() != null) {
            FilterBuilder cmntyFilter = FilterBuilders.termFilter("communityId", cmd.getCommunityId());
            fb = FilterBuilders.andFilter(fb, cmntyFilter);
        }
        
        // 用于一些场景下只能搜索出普通公司 by sfyan 20160523
        //empty判断 by xiongying 20160613
        if(!StringUtils.isEmpty(cmd.getOrganizationType())) {
        	//转小写查 by xiongying 20160524
            FilterBuilder cmntyFilter = FilterBuilders.termFilter("organizationType", cmd.getOrganizationType().toLowerCase());
            fb = FilterBuilders.andFilter(fb, cmntyFilter);
        }
        qb = QueryBuilders.filteredQuery(qb, fb);
       
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        
        builder.setFrom(pageNum * pageSize).setSize(pageSize + 1);
        
        builder.setQuery(qb);
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("Query organization, cmd={}, builder={}", cmd, builder);
        }
        
        SearchResponse rsp = builder.execute().actionGet();
        
        List<Long> ids = getIds(rsp);
        GroupQueryResult result = new GroupQueryResult();
//        if(ids.size() > filter.getPageSize()) {
//            result.setPageAnchor(new Long(filter.getPageNumber() + 1));
//            ids.remove(ids.size() - 1);
//         } else {
//            result.setPageAnchor(null);
//            }

        if(ids.size() > pageSize){
            result.setPageAnchor(Long.valueOf(pageNum + 1));
            ids.remove(ids.size() - 1);
        }
        result.setIds(ids);
        
        return result;
    }

    @Override
    public OrganizationQueryResult queryOrganization(SearchOrganizationCommand cmd) {
        GroupQueryFilter filter = new GroupQueryFilter();
        int pageNum = 0;
        if(cmd.getPageAnchor() != null) {
            pageNum = cmd.getPageAnchor().intValue();
        }
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

       SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        
        QueryBuilder qb;
        
        if(StringUtils.isEmpty(cmd.getKeyword())) {
            qb = QueryBuilders.matchAllQuery();
        } else {
        	qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
                    .field("name", 5.0f)
                    .field("name.pinyin_prefix", 2.0f)
                    .field("name.pinyin_gram", 1.0f);      
        }
        
        FilterBuilder fb = null;
//
//        if(null == fb) {
//            fb = FilterBuilders.termFilter("communityType", t.getCode());
//        } else {
//            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("communityType", EnterpriseCommunityType.Normal.getCode()));
//        }
        
//        if(null != fb) {
//            qb = QueryBuilders.filteredQuery(qb, fb);
//        }
        
//        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        if(null != cmd.getNamespaceId())
        	fb = FilterBuilders.termFilter("namespaceId", cmd.getNamespaceId());
        
        // 每个企业（含物业管理公司）都有可能在某个园区内，当客户端提供园区作为过滤条件时，则在园区范围内挑选园区 by lqs 20160512
        if(cmd.getCommunityId() != null) {
        	if(null == fb)
        		fb = FilterBuilders.termFilter("communityId", cmd.getCommunityId());
        	else {
        		FilterBuilder cmntyFilter = FilterBuilders.termFilter("communityId", cmd.getCommunityId());
        		fb = FilterBuilders.andFilter(fb, cmntyFilter);
        	}
        }
        
        // 用于一些场景下只能搜索出普通公司 by sfyan 20160523
        if(!StringUtils.isEmpty(cmd.getOrganizationType())) {
        	if(null == fb)
        		fb = FilterBuilders.termFilter("organizationType", cmd.getOrganizationType().toLowerCase());
        	else {
	        	//转小写查 by xiongying 20160524
	            FilterBuilder cmntyFilter = FilterBuilders.termFilter("organizationType", cmd.getOrganizationType().toLowerCase());
	            fb = FilterBuilders.andFilter(fb, cmntyFilter);
        	}
        }
        qb = QueryBuilders.filteredQuery(qb, fb);
       
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        
        builder.setFrom(pageNum * pageSize).setSize(pageSize+1);
        
        builder.setQuery(qb);
        
        SearchResponse rsp = builder.execute().actionGet();
        
        OrganizationQueryResult result = new OrganizationQueryResult();
        
        List<OrganizationDTO> dtos = this.getDTOs(rsp);
        
        if(dtos.size() > pageSize){
//        	result.setPageAnchor(dtos.get(pageSize - 1).getId());
        	//用的是offset不是锚点 by xiongying20160902
        	result.setPageAnchor((long)(pageNum+1));
        	//移除多查的那个 by xiongying20160902
        	dtos.remove(dtos.size() - 1);
        }
        
        result.setDtos(this.getDTOs(rsp));
        
        return result;
    }
    
    private List<OrganizationDTO> getDTOs(SearchResponse rsp) {
        List<OrganizationDTO> dtos = new ArrayList<OrganizationDTO>();
        SearchHit[] docs = rsp.getHits().getHits();
        for (SearchHit sd : docs) {
            try {
            	OrganizationDTO dto = new OrganizationDTO();
            	dto.setId(Long.parseLong(sd.getId()));
            	Map<String, Object> source = sd.getSource();
            	
            	dto.setName(String.valueOf(source.get("name")));
            	dto.setCommunityId(SearchUtils.getLongField(source.get("communityId")));
            	dto.setDescription(String.valueOf(source.get("description")));
            	dto.setNamespaceId(SearchUtils.getLongField(source.get("namespaceId")).intValue());
            	
    	    	if(dto.getNamespaceId() == 0) {
    	    		dto.setNamespaceName(localeStringService.getLocalizedString(String.valueOf(ConfServiceErrorCode.SCOPE), 
    						String.valueOf(ConfServiceErrorCode.ZUOLIN_NAMESPACE_NAME),
    						UserContext.current().getUser().getLocale(),"ZUOLIN"));
    			} else {
    		    	Namespace ns = nsProvider.findNamespaceById(dto.getNamespaceId());
    				if(ns != null)
    					dto.setNamespaceName(ns.getName());
    			}
    	    	
    	    	ListOrganizationAdministratorCommand orgAdminCmd = new ListOrganizationAdministratorCommand();
		    	orgAdminCmd.setOrganizationId(dto.getId());
		    	ListOrganizationMemberCommandResponse res = rolePrivilegeService.listOrganizationAdministrators(orgAdminCmd);
		    	if(res != null && res.getMembers() != null && res.getMembers().size() > 0) {
		    		OrganizationMemberDTO member = res.getMembers().get(0);
		    		dto.setEnterpriseContactor(member.getContactName());
			    	dto.setMobile(member.getContactToken());
		    	}
    			
            	dtos.add(dto);
            	
            }
            catch(Exception ex) {
                LOGGER.info("getTopicIds error " + ex.getMessage());
            }
        }
        
        return dtos;
    }
}
