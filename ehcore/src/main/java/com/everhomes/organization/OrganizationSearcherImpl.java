package com.everhomes.organization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.everhomes.rest.organization.*;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.*;
import org.elasticsearch.common.xcontent.ToXContent.Params;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceProvider;
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
    private OrganizationProvider organizationProvider;
    
    @Autowired
    private ConfigurationProvider  configProvider;
	
	@Autowired
    private NamespaceProvider nsProvider;
	
	@Autowired
	private LocaleStringService localeStringService;
	
	@Autowired
    private RolePrivilegeService rolePrivilegeService;
	
	@Autowired
	private AddressProvider addressProvider;

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
            b.field("id", organization.getId());
            b.field("namespaceId", organization.getNamespaceId());
            Long communityId = organizationService.getOrganizationActiveCommunityId(organization.getId());
            b.field("communityId", communityId);
            b.field("name", organization.getName());
            //保证后面只能筛选出普通企业（ 注：不包含物业等机构），且一般不会改动，所以加入搜索引擎， by sfyan 20160523
            b.field("organizationType", organization.getOrganizationType());
            b.field("description", organization.getDescription());
            b.field("createTime", organization.getCreateTime());
            b.field("setAdminFlag", organization.getSetAdminFlag());
            List<OrganizationAddress> organizationAddresses = organizationProvider.listOrganizationAddressByOrganizationId(organization.getId());
            List<String> addresses = new ArrayList<>();
            if (organizationAddresses != null && !organizationAddresses.isEmpty()) {
                List<String> buildings = new ArrayList<>();
            	for (OrganizationAddress organizationAddress : organizationAddresses) {
            		Address address = addressProvider.findAddressById(organizationAddress.getAddressId());
            		if (address != null) {
            			addresses.add(getAddress(address));
                        buildings.add(address.getBuildingName());
					}
            	}
            	if (!addresses.isEmpty()) {
            		b.field("addresses", String.join(",", addresses));
            		b.array("buildings", buildings);
				}
			}
            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create group " + organization.getId() + " error");
            return null;
        }
    }
    
    private String getAddress(Address address) {
    	if (address.getApartmentName() != null && address.getBuildingName() != null) {
			if (address.getApartmentName().contains(address.getBuildingName())) {
				return address.getApartmentName();
			}else {
				return address.getBuildingName()+"-"+address.getApartmentName();
			}
		}
		return address.getAddress();
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
//        GroupQueryFilter filter = new GroupQueryFilter();
        int pageNum = 0;
        if(cmd.getPageAnchor() != null) {
            pageNum = cmd.getPageAnchor().intValue();
        }
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());

        QueryBuilder qb = null;

        List<QueryBuilder> qbs = new ArrayList<>();
        BoolQueryBuilder bqb = null;
        //长度大于8的时候 截取掉一段查询
//        if(null != cmd.getBuildingName() && cmd.getBuildingName().length() > 8){
//            QueryBuilder qb1 = QueryBuilders.queryString("*"+cmd.getBuildingName().substring(0, 8)+"*").field("addresses");
//            qbs.add(qb1);
//            if(cmd.getBuildingName().length() > 16){
//                QueryBuilder qb2 = QueryBuilders.queryString("*"+cmd.getBuildingName().substring(8, 16)+"*").field("addresses");
//                qbs.add(qb2);
//                if(cmd.getBuildingName().length() > 24){
//                    QueryBuilder qb3 = QueryBuilders.queryString("*"+cmd.getBuildingName().substring(16, 24)+"*").field("addresses");
//                    qbs.add(qb3);
//                }else{
//                    qbs.add(QueryBuilders.queryString("*"+cmd.getBuildingName().substring(16)+"*").field("addresses"));
//                }
//            }else{
//                qbs.add(QueryBuilders.queryString("*"+cmd.getBuildingName().substring(8)+"*").field("addresses"));
//            }
//
//            if(qbs.size() > 1){
//                bqb = QueryBuilders.boolQuery();
//                for (QueryBuilder queryBuilder: qbs){
//                    bqb.must(queryBuilder);
//                }
//            }
//        }else if(null != cmd.getBuildingName()){
//            qbs.add(QueryBuilders.queryString("*"+cmd.getBuildingName()+"*").field("addresses"));
//        }


//        if(cmd.getBuildingName() != null) {
//            //fix bug for #15397
//            qbs.add(QueryBuilders.queryString(cmd.getBuildingName()).field("buildings"));
////            FilterBuilder buildFilter = FilterBuilders.termFilter("buildings", cmd.getBuildingName());
////            fbList.add(buildFilter);
//        }

        if(StringUtils.isEmpty(cmd.getKeyword())) {
            if (StringUtils.isEmpty(cmd.getBuildingName())) {
                qb = QueryBuilders.matchAllQuery();
            }else {
                if(null != bqb){
                    qb = bqb;
                }else{
                    if(qbs.size() > 0)
                        qb = qbs.get(0);
                }
//				qb = QueryBuilders.multiMatchQuery(cmd.getBuildingName())
//	                    .field("addresses", 5.0f);
            }
//        }else if(isContainChinese(cmd.getKeyword())){//增加中文名称的权重 by xiongying20170524 中文就字符匹配，英文就加上拼音匹配， by dengs 20170524
        }else {
            // es中超过10个字无法搜索出来结果，这里把关键词截断处理
            if (cmd.getKeyword().length() > 10) {
                cmd.setKeyword(cmd.getKeyword().substring(cmd.getKeyword().length() - 10));
            }
            if (qbs.size() == 0) {
//        		qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
//                        .field("name", 5.0f)
//                        .field("name.pinyin_prefix", 2.0f)
//                        .field("name.pinyin_gram", 1.0f)
//                        .field("addresses", 5.0f);
                qb = QueryBuilders.queryString("*"+cmd.getKeyword()+"*").field("addresses").field("name");
            }else {
//				qb = QueryBuilders.multiMatchQuery(cmd.getKeyword() + " " + cmd.getBuildingName())
//                        .field("name", 5.0f)
//                        .field("name.pinyin_prefix", 2.0f)
//                        .field("name.pinyin_gram", 1.0f)
//                        .field("addresses", 5.0f);
                if(null != bqb){
                    qb = bqb.must(QueryBuilders.queryString("*"+cmd.getKeyword()+"*").field("addresses").field("name"));
                }else{
                    qb = QueryBuilders.boolQuery()
                            .must(qbs.get(0))
                            .must(QueryBuilders.queryString("*"+cmd.getKeyword()+"*").field("addresses").field("name"));
                }

            }

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
        List<FilterBuilder> fbList = new ArrayList<>();
        FilterBuilder fb = FilterBuilders.termFilter("namespaceId", namespaceId);
        fbList.add(fb);

        if(!StringUtils.isEmpty(cmd.getBuildingName())) {
            //fix bug for #15397
//            qbs.add(QueryBuilders.queryString(cmd.getBuildingName()).field("buildings"));
            FilterBuilder buildFilter = FilterBuilders.termFilter("buildings", cmd.getBuildingName());
            fbList.add(buildFilter);
        }

        // 每个企业（含物业管理公司）都有可能在某个园区内，当客户端提供园区作为过滤条件时，则在园区范围内挑选园区 by lqs 20160512
        if(cmd.getCommunityId() != null) {
            FilterBuilder cmntyFilter = FilterBuilders.termFilter("communityId", cmd.getCommunityId());
//            fb = FilterBuilders.andFilter(fb, cmntyFilter);
            fbList.add(cmntyFilter);
        }

        // 用于一些场景下只能搜索出普通公司 by sfyan 20160523
        //empty判断 by xiongying 20160613
        if(!StringUtils.isEmpty(cmd.getOrganizationType())) {
            //转小写查 by xiongying 20160524
            FilterBuilder orgTypeFilter = FilterBuilders.termFilter("organizationType", cmd.getOrganizationType().toLowerCase());
//            fb = FilterBuilders.andFilter(fb, orgTypeFilter);
            fbList.add(orgTypeFilter);
        }

        if (cmd.getSetAdminFlag() != null) {
            FilterBuilder adminFlagFilter = FilterBuilders.termFilter("setAdminFlag", cmd.getSetAdminFlag());
//            fb = FilterBuilders.andFilter(fb, adminFlagFilter);
            fbList.add(adminFlagFilter);
        }

//        if (cmd.getExistAddressFlag() != null && cmd.getExistAddressFlag() == ExistAddressFlag.EXIST.getCode()) {
//            FilterBuilder addressFilter = FilterBuilders.existsFilter("addresses");
//            fbList.add(addressFilter);
//        }

        fb = FilterBuilders.andFilter(fbList.toArray(new FilterBuilder[fbList.size()]));

        qb = QueryBuilders.filteredQuery(qb, fb);

        builder.setSearchType(SearchType.QUERY_THEN_FETCH);

        builder.setFrom(pageNum * pageSize).setSize(pageSize + 1);

        builder.setQuery(qb);


        builder.addSort("id", SortOrder.DESC);

        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("Query organization, cmd={}, builder={}", cmd, builder);
        }

        SearchResponse rsp = builder.execute().actionGet();

        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("result from elasticsearch {}", rsp);
        }

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
    public OrganizationQueryResult fuzzyQueryOrganizationByName(SearchOrganizationCommand cmd) {
        GroupQueryFilter filter = new GroupQueryFilter();
        int pageNum = 0;
        if(cmd.getPageAnchor() != null) {
            pageNum = cmd.getPageAnchor().intValue();
        }
        int pageSize = PaginationConfigHelper.getMaxPageSize(configProvider, cmd.getPageSize());

        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());

        QueryBuilder qb;

        if(StringUtils.isEmpty(cmd.getKeyword())) {
            qb = QueryBuilders.matchAllQuery();
        }else {
            // es中超过10个字无法搜索出来结果，这里把关键词截断处理
            if (cmd.getKeyword().length() > 10) {
                cmd.setKeyword(cmd.getKeyword().substring(cmd.getKeyword().length() - 10));
            }
            qb = QueryBuilders.boolQuery().must(QueryBuilders.queryString("*" + cmd.getKeyword() + "*").field("name"));
        }

        FilterBuilder fb = null;
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

        qb = QueryBuilders.filteredQuery(qb, fb);

        builder.setSearchType(SearchType.QUERY_THEN_FETCH);

        builder.setFrom(pageNum * pageSize).setSize(pageSize+1);

        builder.setQuery(qb);
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("Query organization, cmd={}, builder={}", cmd, builder);
        }

        SearchResponse rsp = builder.execute().actionGet();

        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("result from elasticsearch {}", rsp);
        }
        OrganizationQueryResult result = new OrganizationQueryResult();

        List<OrganizationDTO> dtos = this.getDTOs(rsp, cmd.getSimplifyFlag());

        if(dtos.size() > pageSize){
            result.setPageAnchor((long)(pageNum+1));
            dtos.remove(dtos.size() - 1);
        }

        result.setDtos(dtos);

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

            // simplifyFlag = 1 简化版的搜索 add by yanjun 20171012
            if(cmd.getSimplifyFlag() != null && cmd.getSimplifyFlag().byteValue() == 1){
                qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
                        .field("name", 5.0f);
            }else {
                qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
                        .field("name", 5.0f)
                        .field("name.pinyin_prefix", 2.0f)
                        .field("name.pinyin_gram", 1.0f);
            }

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
        
        List<OrganizationDTO> dtos = this.getDTOs(rsp, cmd.getSimplifyFlag());
        
        if(dtos.size() > pageSize){
//        	result.setPageAnchor(dtos.get(pageSize - 1).getId());
        	//用的是offset不是锚点 by xiongying20160902
        	result.setPageAnchor((long)(pageNum+1));
        	//移除多查的那个 by xiongying20160902
        	dtos.remove(dtos.size() - 1);
        }
        
        result.setDtos(this.getDTOs(rsp, cmd.getSimplifyFlag()));
        
        return result;
    }
    
    private List<OrganizationDTO> getDTOs(SearchResponse rsp, Byte simplifyFlag) {
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

    			// add if by yanjun for no search detail  20171012
    			if(simplifyFlag == null || simplifyFlag.byteValue() == 0){
                    ListOrganizationAdministratorCommand orgAdminCmd = new ListOrganizationAdministratorCommand();
                    orgAdminCmd.setOrganizationId(dto.getId());
                    ListOrganizationMemberCommandResponse res = rolePrivilegeService.listOrganizationAdministrators(orgAdminCmd);
                    if(res != null && res.getMembers() != null && res.getMembers().size() > 0) {
                        OrganizationMemberDTO member = res.getMembers().get(0);
                        dto.setEnterpriseContactor(member.getContactName());
                        dto.setMobile(member.getContactToken());
                    }
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
