package com.everhomes.customer;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.equipment.EquipmentService;
import com.everhomes.investment.*;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.portal.PortalService;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.acl.PrivilegeServiceErrorCode;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.approval.CustomerAptitudeFlag;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.contract.ContractStatus;
import com.everhomes.rest.customer.CustomerEntryInfoDTO;
import com.everhomes.rest.customer.EasySearchEnterpriseCustomersCommand;
import com.everhomes.rest.customer.EasySearchEnterpriseCustomersDTO;
import com.everhomes.rest.customer.EnterpriseCustomerDTO;
import com.everhomes.rest.customer.ListCustomerEntryInfosCommand;
import com.everhomes.rest.customer.SearchEnterpriseCustomerCommand;
import com.everhomes.rest.customer.SearchEnterpriseCustomerResponse;
import com.everhomes.rest.equipment.findScopeFieldItemCommand;
import com.everhomes.rest.investment.*;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.varField.FieldItemDTO;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.EnterpriseCustomerSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.varField.Field;
import com.everhomes.varField.FieldProvider;
import com.everhomes.varField.FieldService;
import com.everhomes.varField.ScopeFieldItem;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.ExistsFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeFilterBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.jooq.tools.Convert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by ying.xiong on 2017/8/17.
 */
@Component
public class EnterpriseCustomerSearcherImpl extends AbstractElasticSearch implements EnterpriseCustomerSearcher {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ConfigurationProvider configProvider;

    @Autowired
    private EnterpriseCustomerProvider enterpriseCustomerProvider;

    @Autowired
    private FieldProvider fieldProvider;
    
	@Autowired
	private OrganizationProvider organizationProvider;

    @Autowired
    private FieldService fieldService;

    @Autowired
    private PortalService portalService;

    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AddressProvider addressProvider;

    @Autowired
    private ContractProvider contractProvider;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private InvitedCustomerProvider invitedCustomerProvider;

    @Autowired
    private InvitedCustomerService invitedCustomerService;

    @Override
    public String getIndexType() {
        return SearchUtils.ENTERPRISE_CUSTOMER;
    }

    @Override
    public void deleteById(Long id) {
        deleteById(id.toString());
    }

    @Override
    public void bulkUpdate(List<EnterpriseCustomer> customers) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (EnterpriseCustomer customer : customers) {
            XContentBuilder source = createDoc(customer);
            if(null != source) {
                LOGGER.info("id:" + customer.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType()).id(customer.getId().toString()).source(source));
            }
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }

    @Override
    public void feedDoc(EnterpriseCustomer customer) {
        XContentBuilder source = createDoc(customer);
        feedDoc(customer.getId().toString(), source);
    }

    private XContentBuilder createDoc(EnterpriseCustomer customer) {
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();

            builder.field("id", customer.getId());
            builder.field("ownerId", customer.getOwnerId());
            builder.field("communityId", customer.getCommunityId());
            builder.field("namespaceId", customer.getNamespaceId());
            builder.field("name", customer.getName());
            builder.field("contactPhone", customer.getContactPhone());
            builder.field("contactName", customer.getContactName());
            builder.field("contactAddress", customer.getContactAddress());
            builder.field("categoryItemId", customer.getCategoryItemId());
            builder.field("corpIndustryItemId", customer.getCorpIndustryItemId());
            // for statistics filter
            builder.field("levelItemId", customer.getLevelItemId() == null ? 0 : customer.getLevelItemId());
            builder.field("status", customer.getStatus());
            builder.field("trackingUid",customer.getTrackingUid());
            builder.field("trackingName",customer.getTrackingName() == null ? "" : customer.getTrackingName());
            builder.field("lastTrackingTime" , customer.getLastTrackingTime());
            builder.field("propertyType" , customer.getPropertyType());
            builder.field("propertyUnitPrice" , customer.getPropertyUnitPrice());
            builder.field("propertyArea" , customer.getPropertyArea());
            builder.field("adminFlag" , customer.getAdminFlag());
            builder.field("sourceItemId" , customer.getSourceItemId());
            builder.field("sourceId" , customer.getSourceId());
            builder.field("sourceType" , customer.getSourceType());
            builder.field("aptitudeFlagItemId" , customer.getAptitudeFlagItemId());
            builder.field("customerSource" , customer.getCustomerSource());
            builder.field("entryStatusItemId", customer.getEntryStatusItemId());
            List<CustomerTracker> tracker;
            tracker = invitedCustomerProvider.findTrackerByCustomerId(customer.getId());
            if(tracker != null && tracker.size() > 0){
                List<String> trackerNames = new ArrayList<>();
                List<Long> trackerIds = new ArrayList<>();

                tracker.forEach(r -> {
                    List<OrganizationMember> members = organizationProvider.listOrganizationMembersByUId(r.getTrackerUid());
                    if (members != null && members.size()>0) {
                        trackerNames.add(members.get(0).getContactName());
                        trackerIds.add(r.getTrackerUid());
                    }
                });
                builder.field("trackerName", StringUtils.join(trackerNames, "|"));
                builder.field("trackerUid", StringUtils.join(trackerIds, "|"));

            }
            CustomerRequirement requirement = invitedCustomerProvider.findNewestRequirementByCustoemrId(customer.getId());
            if(requirement != null){
                builder.field("requirementMinArea", requirement.getMinArea() == null ? "" : requirement.getMinArea());
                builder.field("requirementMaxArea", requirement.getMaxArea() == null ? "" : requirement.getMaxArea());

            }
            List<CustomerContact> contact = invitedCustomerProvider.findContactByCustomerIdAndType(customer.getId(), CustomerContactType.CUSTOMER_CONTACT.getCode());
            if(contact != null && contact.size() > 0){
                List<String> customerContactName = new ArrayList<>();
                List<Long> customerContactIds = new ArrayList<>();

                contact.forEach(r -> {
                    if(StringUtils.isNotBlank(r.getName())){
                        customerContactName.add(r.getName());
                    }
                });
                builder.field("customerContactName", StringUtils.join(customerContactName, "|"));
            }
            List<CustomerEntryInfo> entryInfos = enterpriseCustomerProvider.listCustomerEntryInfos(customer.getId());
            if (entryInfos != null && entryInfos.size() > 0) {
                List<String> buildings = new ArrayList<>();
                List<String> addressIds = new ArrayList<>();
                entryInfos.forEach((e) -> {
                    buildings.add(e.getBuildingId().toString());
                    if (e.getAddressId() != null){
                        addressIds.add(e.getAddressId().toString());
                    }
                });
                builder.field("buildingId", StringUtils.join(buildings, "|"));
                builder.field("addressId", StringUtils.join(addressIds, "|"));
//                builder.array("buildings", buildings);
//                builder.array("addressId", addressIds);
            }


            builder.endObject();
            return builder;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void syncFromDb() {
        int pageSize = 200;
        this.deleteAll();

        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<EnterpriseCustomer> customers = enterpriseCustomerProvider.listEnterpriseCustomers(locator, pageSize);

            if(customers.size() > 0) {
                this.bulkUpdate(customers);
            }

            if(locator.getAnchor() == null) {
                break;
            }
        }

        this.optimize(1);
        this.refresh();
        LOGGER.info("sync for customers ok");
    }

    private void checkCustomerAuth(Integer namespaceId, Long privilegeId, Long orgId, Long communityId) {
        ListServiceModuleAppsCommand cmd = new ListServiceModuleAppsCommand();
        cmd.setNamespaceId(namespaceId);
        cmd.setModuleId(ServiceModuleConstants.ENTERPRISE_CUSTOMER_MODULE);
        cmd.setActionType(ActionType.OFFICIAL_URL.getCode());
        ListServiceModuleAppsResponse apps = portalService.listServiceModuleAppsWithConditon(cmd);
        Long appId = apps.getServiceModuleApps().get(0).getOriginId();
        if(!userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), EntityType.ORGANIZATIONS.getCode(), orgId,
                orgId, privilegeId, appId, null, communityId)) {
            LOGGER.error("Permission is prohibited, namespaceId={}, orgId={}, ownerType={}, ownerId={}, privilegeId={}",
                    namespaceId, orgId, EntityType.COMMUNITY.getCode(), communityId, privilegeId);
            throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_CHECK_APP_PRIVILEGE,
                    "check user privilege error");
        }
    }

    @Override
    public SearchEnterpriseCustomerResponse queryEnterpriseCustomersById(SearchEnterpriseCustomerCommand cmd){
        SearchEnterpriseCustomerResponse response = new SearchEnterpriseCustomerResponse();

        List<Long> ids = cmd.getCustomerIds();
        List<EnterpriseCustomerDTO> dtos = new ArrayList<>();
        Map<Long, EnterpriseCustomer> customers = enterpriseCustomerProvider.listEnterpriseCustomersByIds(ids);
        if(customers != null && customers.size() > 0) {
            //一把取出来的列表顺序和搜索引擎中得到的ids的顺序不一定一样 以搜索引擎的为准 by xiongying 20170907
            ids.forEach(id -> {
                EnterpriseCustomer customer = customers.get(id);
                if(customer != null) {
                    EnterpriseCustomerDTO dto = convertToDTO(customer);
                    dtos.add(dto);
                }
            });
        }
//        Collections.sort(dtos);
        response.setDtos(dtos);
        return response;
    }

    @Override
    public SearchEnterpriseCustomerResponse queryEnterpriseCustomers(SearchEnterpriseCustomerCommand cmd,Boolean isAdmin) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        QueryBuilder qb = null;
        if((cmd.getKeyword() == null || cmd.getKeyword().isEmpty()) && (cmd.getCustomerName() == null || cmd.getCustomerName().isEmpty())) {
            //app端要只根据跟进人名称搜索
            if(StringUtils.isNotEmpty(cmd.getTrackingName())){
                qb = QueryBuilders.queryString("*" + cmd.getTrackingName() + "*")
                .field("trackingName", 4.0f);
            }else if(StringUtils.isNotEmpty(cmd.getTrackerName())){
                qb = QueryBuilders.queryString("*" + cmd.getTrackerName() + "*")
                        .field("trackerName", 4.0f);
            }else{
                qb = QueryBuilders.matchAllQuery();
            }
        } else {
            if(StringUtils.isNotBlank(cmd.getKeyword())) {
                qb = QueryBuilders.queryString("*" + cmd.getKeyword() + "*").field("trackerName",5.0f)
                            .field("contactName", 5.0f)
                        .field("customerContactName", 4.0f)
                        .field("contactAddress", 4.0f)
                        .field("contactPhone", 3.0f)
                        .field("name", 5.0f)
                        .field("trackingName", 3.0f);
            }

            if (StringUtils.isNotBlank(cmd.getCustomerName() )) {
                if(qb != null) {
                    qb = QueryBuilders.boolQuery().must(qb).must(QueryBuilders.queryString("*" + cmd.getCustomerName() + "*").field("name"));
                }else{
                    qb = QueryBuilders.queryString("*" + cmd.getCustomerName() + "*").field("name");
                }
            }

            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("name").addHighlightedField("contactName").addHighlightedField("contactAddress").addHighlightedField("contactPhone")
            		.addHighlightedField("trackingName");



        }

        FilterBuilder fb = null;
        FilterBuilder nfb = FilterBuilders.termFilter("status", CommonStatus.INACTIVE.getCode());
        fb = FilterBuilders.notFilter(nfb);
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("namespaceId", cmd.getNamespaceId()));
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("communityId", cmd.getCommunityId()));
        //是否为资质客户增加筛选

        if(cmd.getContractSearchCustomerFlag() != null && cmd.getContractSearchCustomerFlag() == 1){

            Byte aptitudeFlag = contractProvider.filterAptitudeCustomer(cmd.getCommunityId(),cmd.getNamespaceId());
            if(aptitudeFlag == 1) {
                fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("aptitudeFlagItemId", CustomerAptitudeFlag.APTITUDE.getCode()));
            }

        }

        if(cmd.getAptitudeFlagItemId() != null){
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("aptitudeFlagItemId", cmd.getAptitudeFlagItemId()));
        }

        if(cmd.getAbnormalFlag() != null && cmd.getAbnormalFlag() == 1){
           fb = FilterBuilders.andFilter(fb, FilterBuilders.missingFilter("addressId"));
           fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("status", ContractStatus.ACTIVE.getCode()));

        }else {
            if (cmd.getAddressId() != null) {
                MultiMatchQueryBuilder addressId = QueryBuilders.multiMatchQuery(cmd.getAddressId(), "addressId");
                qb = QueryBuilders.boolQuery().must(qb).must(addressId);
                //            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("addressId", cmd.getAddressId()));
            }
            if (cmd.getBuildingId() != null) {
                MultiMatchQueryBuilder buildingId = QueryBuilders.multiMatchQuery(cmd.getBuildingId(), "buildingId");
                qb = QueryBuilders.boolQuery().must(qb).must(buildingId);
                //            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("buildingId", cmd.getBuildingId()));
            }
        }

        if(cmd.getCustomerCategoryId() != null)
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("categoryItemId", cmd.getCustomerCategoryId()));

        if(cmd.getAdminFlag() != null)
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("adminFlag", cmd.getAdminFlag()));

        if(cmd.getCorpIndustryItemId() != null)
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("corpIndustryItemId", cmd.getCorpIndustryItemId()));

        if(cmd.getLevelId() != null )
            fb = FilterBuilders.andFilter(fb, FilterBuilders.inFilter("levelItemId", cmd.getLevelId().split(",")));

        /*//查询全部客户、我的客户、公共客户
        if(null != cmd.getType()){
        	if(2 == cmd.getType()){
        		fb = FilterBuilders.andFilter(fb ,FilterBuilders.termFilter("trackingUid", UserContext.currentUserId()));
        	}else if(3 == cmd.getType()){
        		fb = FilterBuilders.andFilter(fb ,FilterBuilders.termFilter("trackingUid", -1l));
        	}
        }*/
        //增加如果不是admin只能看自己为跟进人
        if (!isAdmin) {
            if(cmd.getCustomerSource() == null || cmd.getCustomerSource() == InvitedCustomerType.INVITED_CUSTOMER.getCode()){
                fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("trackerUid", UserContext.currentUserId()));
            }else{
                fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("trackingUid", UserContext.currentUserId()));
            }
        }
        //有无跟进人
        if(null != cmd.getType()){
            if(2 == cmd.getType()){
                fb = FilterBuilders.andFilter(fb ,FilterBuilders.existsFilter("trackingUid"));
            }else if(3 == cmd.getType()){
                ExistsFilterBuilder notFilter = FilterBuilders.existsFilter("trackingUid");
                fb = FilterBuilders.andFilter(fb ,FilterBuilders.notFilter(notFilter));
            }
        }






        //跟进时间、资产类型、资产面积、资产单价增加筛选
        if(null != cmd.getLastTrackingTime() && cmd.getLastTrackingTime() > 0){
        	RangeFilterBuilder rf = new RangeFilterBuilder("lastTrackingTime");
        	Long startTime = getTomorrowLastTimestamp(cmd.getLastTrackingTime());
        	rf.gte(startTime);
        	fb = FilterBuilders.andFilter(fb, rf); 
        }

        if(cmd.getTrackerUids() != null && cmd.getTrackerUids().size()>0){
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termsFilter("trackerUid", cmd.getTrackingUids()));
        }
        if(cmd.getTrackingUids() != null && cmd.getTrackingUids().size()>0){
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termsFilter("trackingUid", cmd.getTrackingUids()));
        }

        if(null != cmd.getPropertyType()){
        	fb = FilterBuilders.andFilter(fb ,FilterBuilders.inFilter("propertyType", cmd.getPropertyType().split(",")));
        }
        
        if(null != cmd.getPropertyArea()){
        	RangeFilterBuilder rf = new RangeFilterBuilder("propertyArea");
        	if(cmd.getPropertyArea().contains(",") && cmd.getPropertyArea().split(",").length == 2){
        		if(null != cmd.getPropertyArea().split(",")[0] && !"@".equals(cmd.getPropertyArea().split(",")[0])){
        			rf.gte(Double.parseDouble(cmd.getPropertyArea().split(",")[0]));
        		}
        		if(null != cmd.getPropertyArea().split(",")[1] && !"@".equals(cmd.getPropertyArea().split(",")[1])){
        			rf.lte(Double.parseDouble(cmd.getPropertyArea().split(",")[1]));
        		}
        		fb = FilterBuilders.andFilter(fb, rf); 
        	}
        	
        }

        if(null != cmd.getMinTrackingPeriod() || null != cmd.getMaxTrackingPeriod()){
            if(null != cmd.getMinTrackingPeriod() && null != cmd.getMaxTrackingPeriod()){
                RangeFilterBuilder rf = new RangeFilterBuilder("lastTrackingTime");
                Long startTime = cmd.getMinTrackingPeriod();
                Long endTime = cmd.getMaxTrackingPeriod();
                rf.gte(new Timestamp(startTime));
                rf.lte(new Timestamp(endTime));
                fb = FilterBuilders.andFilter(fb, rf);
            }else if(null != cmd.getMinTrackingPeriod() && null == cmd.getMaxTrackingPeriod()){
                RangeFilterBuilder rf = new RangeFilterBuilder("lastTrackingTime");
                Long startTime = cmd.getMinTrackingPeriod();
                rf.gte(startTime);
                fb = FilterBuilders.andFilter(fb, rf);
            }else{
                RangeFilterBuilder rf = new RangeFilterBuilder("lastTrackingTime");
                Long endTime = cmd.getMaxTrackingPeriod();
                rf.lte(endTime);
                fb = FilterBuilders.andFilter(fb, rf);
            }
        }

        if(null != cmd.getRequirementMinArea() || null != cmd.getRequirementMaxArea()){
            if(null != cmd.getRequirementMinArea() && null != cmd.getRequirementMaxArea()){
                RangeFilterBuilder rf1 = new RangeFilterBuilder("requirementMinArea");
                RangeFilterBuilder rf2 = new RangeFilterBuilder("requirementMaxArea");

                BigDecimal startArea = cmd.getRequirementMinArea();
                BigDecimal endArea = cmd.getRequirementMaxArea();
                rf1.gte(startArea);
                rf2.lte(endArea);
                fb = FilterBuilders.andFilter(fb, rf1);
                fb = FilterBuilders.andFilter(fb, rf2);
            }else if(null != cmd.getRequirementMinArea() && null == cmd.getRequirementMaxArea()){
                RangeFilterBuilder rf = new RangeFilterBuilder("requirementMinArea");
                BigDecimal startArea = cmd.getRequirementMinArea();
                rf.gte(startArea);
                fb = FilterBuilders.andFilter(fb, rf);
            }else{
                RangeFilterBuilder rf = new RangeFilterBuilder("requirementMaxArea");
                BigDecimal endArea = cmd.getRequirementMaxArea();
                rf.lte(endArea);
                fb = FilterBuilders.andFilter(fb, rf);
            }
        }

        if(null != cmd.getPropertyUnitPrice()){
        	RangeFilterBuilder rf = new RangeFilterBuilder("propertyUnitPrice");
        	if(cmd.getPropertyUnitPrice().contains(",") && cmd.getPropertyUnitPrice().split(",").length == 2){
        		if(null != cmd.getPropertyUnitPrice().split(",")[0] && !"@".equals(cmd.getPropertyUnitPrice().split(",")[0])){
        			rf.gte(Double.parseDouble(cmd.getPropertyUnitPrice().split(",")[0]));
        		}
        		if(null != cmd.getPropertyUnitPrice().split(",")[1] && !"@".equals(cmd.getPropertyUnitPrice().split(",")[1])){
        			rf.lte(Double.parseDouble(cmd.getPropertyUnitPrice().split(",")[1]));
        		}
        		fb = FilterBuilders.andFilter(fb, rf); 
        	}
        }

        if (cmd.getSourceItemId() != null && StringUtils.isNotBlank(cmd.getSourceType())) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("sourceItemId", cmd.getSourceItemId()));
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("sourceType", cmd.getSourceType()));
        }

        if(cmd.getSourceItemId()!=null && StringUtils.isBlank(cmd.getSourceType())){
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("sourceItemId", cmd.getSourceItemId()));
            fb = FilterBuilders.andFilter(fb, FilterBuilders.notFilter(FilterBuilders.existsFilter("sourceId")));
        }

        if(cmd.getEntryStatusItemId()!=null){
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("entryStatusItemId", cmd.getEntryStatusItemId()));
        }

        if(cmd.getCustomerSource() != null){
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("customerSource", cmd.getCustomerSource()));
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
        if(cmd.getSortField() != null && cmd.getSortType() != null) {
            if(cmd.getSortType() == 0) {
                builder.addSort(cmd.getSortField(), SortOrder.ASC);
            } else if(cmd.getSortType() == 1) {
                builder.addSort(cmd.getSortField(), SortOrder.DESC);
            }
        } else {
            builder.addSort("id", SortOrder.DESC);
        }
        SearchResponse rsp = builder.execute().actionGet();

        if(LOGGER.isDebugEnabled())
            LOGGER.info("EnterpriseCustomerSearcherImpl query builder: {}, rsp: {}", builder, rsp);

        List<Long> ids = getIds(rsp);
        SearchEnterpriseCustomerResponse response = new SearchEnterpriseCustomerResponse();

        if(ids.size() > pageSize) {
            response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
        }

        List<EnterpriseCustomerDTO> dtos = new ArrayList<>();
        Map<Long, EnterpriseCustomer> customers = enterpriseCustomerProvider.listEnterpriseCustomersByIds(ids);

        if(cmd.getConvertFlag() == null || cmd.getConvertFlag() == 0) {
            if (customers != null && customers.size() > 0) {
                //一把取出来的列表顺序和搜索引擎中得到的ids的顺序不一定一样 以搜索引擎的为准 by xiongying 20170907
                ids.forEach(id -> {
                    EnterpriseCustomer customer = customers.get(id);
                    if (customer != null) {
                        // zuolin base
                    customer.setOwnerId(cmd.getOrgId());
                    EnterpriseCustomerDTO dto = null;
                    if(cmd.getMobileFlag() == null || cmd.getMobileFlag() == 0){
                        dto = convertToDTO(customer);
                    }else{
                        dto = mobileConvertToDTO(customer);
                    }
                    dtos.add(dto);
                }
            });}
        }else{
            if (customers != null && customers.size() > 0) {
                ids.forEach(id -> {
                    EnterpriseCustomer customer = customers.get(id);
                    if (customer != null) {
                        EnterpriseCustomerDTO dto = ConvertHelper.convert(customer, EnterpriseCustomerDTO.class);
                        dtos.add(dto);
                    }
                });
            }
        }
//        Collections.sort(dtos);
        response.setDtos(dtos);
        return response;
    }

    @Override
    public List<EasySearchEnterpriseCustomersDTO> easyQueryEnterpriseCustomers(EasySearchEnterpriseCustomersCommand cmd) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        QueryBuilder qb = null;
        String keyWord = cmd.getKeyWord();
        if(org.jooq.tools.StringUtils.isBlank(keyWord)) {
            return new ArrayList<>();
        } else {
            qb = QueryBuilders.wildcardQuery("name.baidu", keyWord+"*");
        }
        FilterBuilder fb = null;
        FilterBuilder nfb = FilterBuilders.termFilter("status", CommonStatus.INACTIVE.getCode());
        fb = FilterBuilders.notFilter(nfb);
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("namespaceId", cmd.getNamespaceId()));
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("communityId", cmd.getCommunityId()));
        qb = QueryBuilders.filteredQuery(qb, fb);

        int pageSize = 10;
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        builder.setSize(pageSize);
        builder.setQuery(qb);

        SearchResponse rsp = builder.execute().actionGet();

        if(LOGGER.isDebugEnabled())
            LOGGER.info("EnterpriseCustomerSearcherImpl query builder: {}, rsp: {}", builder, rsp);

        List<Long> ids = getIds(rsp);
        if(ids.size() < 1) {
            return new ArrayList<>();
        }
        List<EasySearchEnterpriseCustomersDTO> list = new ArrayList<>();

        return  enterpriseCustomerProvider.listEnterpriseCustomerNameAndId(ids);
    }

    @Override
    public List<EasySearchEnterpriseCustomersDTO> listEnterpriseCustomers(EasySearchEnterpriseCustomersCommand cmd) {
         return enterpriseCustomerProvider.listCommunityEnterpriseCustomers(cmd.getCommunityId(), cmd.getNamespaceId());
    }

    private EnterpriseCustomerDTO convertToDTO(EnterpriseCustomer customer) {
        LOGGER.debug("convertToDTO start time :{}",System.currentTimeMillis());
        EnterpriseCustomerDTO dto = ConvertHelper.convert(customer, EnterpriseCustomerDTO.class);
        Integer result = 0;
//        ScopeFieldItem categoryItem = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getCategoryItemId());
        ScopeFieldItem categoryItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), customer.getCategoryItemId());
        if (categoryItem != null) {
            dto.setCategoryItemName(categoryItem.getItemDisplayName());
        } else {
            dto.setCategoryItemName(null);
        }
//        ScopeFieldItem levelItem = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getLevelItemId());
        ScopeFieldItem levelItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), customer.getLevelItemId());
        if (levelItem != null) {
            dto.setLevelItemName(levelItem.getItemDisplayName());
        } else {
            dto.setLevelItemName(null);
        }
        if (null != dto.getCorpIndustryItemId()) {
            ScopeFieldItem corpIndustryItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getCorpIndustryItemId());
            if (null != corpIndustryItem) {
                dto.setCorpIndustryItemName(corpIndustryItem.getItemDisplayName());
            } else {
                dto.setCorpIndustryItemName(null);
            }
        }
        if (null != dto.getSourceItemId()) {
            ScopeFieldItem item = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getSourceItemId());
            if (null != item) {
                dto.setSourceItemName(item.getItemDisplayName());
            } else {
                dto.setSourceItemName(null);
            }
        }
        if (null != dto.getCorpPurposeItemId()) {
            ScopeFieldItem item = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getCorpPurposeItemId());
            if (null != item) {
                dto.setCorpPurposeItemName(item.getItemDisplayName());
            } else {
                dto.setCorpPurposeItemName(null);
            }
        }
        if (null != dto.getCorpNatureItemId()) {
            ScopeFieldItem item = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getCorpNatureItemId());
            if (null != item) {
                dto.setCorpNatureItemName(item.getItemDisplayName());
            } else {
                dto.setCorpNatureItemName(null);
            }
        }
        if (null != dto.getContactGenderItemId()) {
            ScopeFieldItem contactGenderItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getContactGenderItemId());
            if (null != contactGenderItem) {
                dto.setContactGenderItemName(contactGenderItem.getItemDisplayName());
            } else {
                dto.setContactGenderItemName(null);
            }
        }
        if (dto.getTrackingUid() != null && dto.getTrackingUid() != 0) {
            dto.setTrackingName(dto.getTrackingName());
        }
        if (null != dto.getPropertyType()) {
            ScopeFieldItem propertyTypeItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getPropertyType());
            if (null != propertyTypeItem) {
                dto.setPropertyTypeName(propertyTypeItem.getItemDisplayName());
            } else {
                dto.setPropertyTypeName(null);
            }
        }
        if (null != dto.getCorpProductCategoryItemId()) {
            ScopeFieldItem item = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getOwnerId(),customer.getCommunityId(), dto.getCorpProductCategoryItemId());
            if (null != item) {
                dto.setCorpProductCategoryItemName(item.getItemDisplayName());
            } else {
                dto.setCorpProductCategoryItemName(null);
            }
        }
        if (null != dto.getCorpQualificationItemId()) {
            ScopeFieldItem item = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getOwnerId(),customer.getCommunityId(), dto.getCorpQualificationItemId());
            if (null != item) {
                dto.setCorpQualificationItemName(item.getItemDisplayName());
            } else {
                dto.setCorpQualificationItemName(null);
            }
        }

        if (null != dto.getRegistrationTypeId()) {
            ScopeFieldItem registrationTypeItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getRegistrationTypeId());
            if (null != registrationTypeItem) {
                dto.setRegistrationTypeName(registrationTypeItem.getItemDisplayName());
            } else {
                dto.setRegistrationTypeName(null);
            }
        }

        if (null != dto.getTechnicalFieldId()) {
            ScopeFieldItem technicalFieldItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getTechnicalFieldId());
            if (null != technicalFieldItem) {
                dto.setTechnicalFieldName(technicalFieldItem.getItemDisplayName());
            } else {
                dto.setTechnicalFieldName(null);
            }
        }

        if (null != dto.getTaxpayerTypeId()) {
            ScopeFieldItem taxpayerTypeItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getTaxpayerTypeId());
            if (null != taxpayerTypeItem) {
                dto.setTaxpayerTypeName(taxpayerTypeItem.getItemDisplayName());
            } else {
                dto.setTaxpayerTypeName(null);
            }
        }

        if (null != dto.getRelationWillingId()) {
            ScopeFieldItem relationWillingItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getRelationWillingId());
            if (null != relationWillingItem) {
                dto.setRelationWillingName(relationWillingItem.getItemDisplayName());
            } else {
                dto.setRelationWillingName(null);
            }
        }

        if (null != dto.getHighAndNewTechId()) {
            ScopeFieldItem highAndNewTechItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getHighAndNewTechId());
            if (null != highAndNewTechItem) {
                dto.setHighAndNewTechName(highAndNewTechItem.getItemDisplayName());
            } else {
                dto.setHighAndNewTechName(null);
            }
        }

        if (null != dto.getEntrepreneurialCharacteristicsId()) {
            ScopeFieldItem entrepreneurialCharacteristicsItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getEntrepreneurialCharacteristicsId());
            if (null != entrepreneurialCharacteristicsItem) {
                dto.setEntrepreneurialCharacteristicsName(entrepreneurialCharacteristicsItem.getItemDisplayName());
            } else {
                dto.setEntrepreneurialCharacteristicsName(null);
            }
        }

        if (null != dto.getSerialEntrepreneurId()) {
            ScopeFieldItem serialEntrepreneurItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getOwnerId(),customer.getCommunityId(), dto.getSerialEntrepreneurId());
            if (null != serialEntrepreneurItem) {
                dto.setSerialEntrepreneurName(serialEntrepreneurItem.getItemDisplayName());
            } else {
                dto.setSerialEntrepreneurName(null);
            }
        }

        if (null != dto.getBuyOrLeaseItemId()) {
            ScopeFieldItem buyOrLeaseItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getOwnerId(),customer.getCommunityId(), dto.getBuyOrLeaseItemId());
            if (null != buyOrLeaseItem) {
                dto.setBuyOrLeaseItemName(buyOrLeaseItem.getItemDisplayName());
            } else {
                dto.setBuyOrLeaseItemName(null);
            }
        }

        if (null != dto.getFinancingDemandItemId()) {
            ScopeFieldItem financingDemandItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getFinancingDemandItemId());
            if (null != financingDemandItem) {
                dto.setFinancingDemandItemName(financingDemandItem.getItemDisplayName());
            } else {
                dto.setSerialEntrepreneurName(null);
            }
        }

        if (null != dto.getDropBox1ItemId()) {
            ScopeFieldItem dropBoxItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getDropBox1ItemId());
            if (null != dropBoxItem) {
                dto.setDropBox1ItemName(dropBoxItem.getItemDisplayName());
            } else {
                dto.setDropBox1ItemName(null);
            }
        }

        if (null != dto.getDropBox2ItemId()) {
            ScopeFieldItem dropBoxItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getDropBox2ItemId());
            if (null != dropBoxItem) {
                dto.setDropBox2ItemName(dropBoxItem.getItemDisplayName());
            } else {
                dto.setDropBox2ItemName(null);
            }
        }

        if (null != dto.getDropBox3ItemId()) {
            ScopeFieldItem dropBoxItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getDropBox3ItemId());
            if (null != dropBoxItem) {
                dto.setDropBox3ItemName(dropBoxItem.getItemDisplayName());
            } else {
                dto.setDropBox3ItemName(null);
            }
        }

        if (null != dto.getDropBox4ItemId()) {
            ScopeFieldItem dropBoxItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getDropBox4ItemId());
            if (null != dropBoxItem) {
                dto.setDropBox4ItemName(dropBoxItem.getItemDisplayName());
            } else {
                dto.setDropBox4ItemName(null);
            }
        }

        if (null != dto.getDropBox5ItemId()) {
            ScopeFieldItem dropBoxItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getDropBox5ItemId());
            if (null != dropBoxItem) {
                dto.setDropBox5ItemName(dropBoxItem.getItemDisplayName());
            } else {
                dto.setDropBox5ItemName(null);
            }
        }

        if (null != dto.getDropBox6ItemId()) {
            ScopeFieldItem dropBoxItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getDropBox6ItemId());
            if (null != dropBoxItem) {
                dto.setDropBox6ItemName(dropBoxItem.getItemDisplayName());
            } else {
                dto.setDropBox6ItemName(null);
            }
        }

        if (null != dto.getDropBox7ItemId()) {
            ScopeFieldItem dropBoxItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(), dto.getDropBox7ItemId());
            if (null != dropBoxItem) {
                dto.setDropBox7ItemName(dropBoxItem.getItemDisplayName());
            } else {
                dto.setDropBox7ItemName(null);
            }
        }

        if (null != dto.getDropBox8ItemId()) {
            ScopeFieldItem dropBoxItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getOwnerId(),customer.getCommunityId(), dto.getDropBox8ItemId());
            if (null != dropBoxItem) {
                dto.setDropBox8ItemName(dropBoxItem.getItemDisplayName());
            } else {
                dto.setDropBox8ItemName(null);
            }
        }

        if (null != dto.getDropBox9ItemId()) {
            ScopeFieldItem dropBoxItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getOwnerId(),customer.getCommunityId(), dto.getDropBox9ItemId());
            if (null != dropBoxItem) {
                dto.setDropBox9ItemName(dropBoxItem.getItemDisplayName());
            } else {
                dto.setDropBox9ItemName(null);
            }
        }

        if (null != dto.getEntryStatusItemId()) {
            ScopeFieldItem entryStatusItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getOwnerId(),customer.getCommunityId(), dto.getEntryStatusItemId());
            if (null != entryStatusItem) {
                dto.setEntryStatusItemName(entryStatusItem.getItemDisplayName());
            } else {
                dto.setEntryStatusItemName(null);
            }
        }



        if (null != dto.getAptitudeFlagItemId()) {
            findScopeFieldItemCommand cmd = new findScopeFieldItemCommand();
            cmd.setNamespaceId(customer.getNamespaceId());
            cmd.setBusinessValue(Byte.valueOf(dto.getAptitudeFlagItemId().toString()));
            cmd.setModuleName("enterprise_customer");
            cmd.setCommunityId(customer.getCommunityId());
            Field field = fieldProvider.findField(10L, "aptitudeFlagItemId");
            if (field != null)
            cmd.setFieldId(field.getId());
            FieldItemDTO aptitudeFlag = equipmentService.findScopeFieldItemByFieldItemId(cmd);

            if (null != aptitudeFlag) {
                dto.setAptitudeFlagItemName(aptitudeFlag.getItemDisplayName());
            } else {
                dto.setAptitudeFlagItemName(null);
            }
        }






        LOGGER.debug("switch items name end time :{}",System.currentTimeMillis());

        //21002 企业管理1.4（来源于第三方数据，企业名称栏为灰色不可修改） add by xiongying20171219
        if(!StringUtils.isEmpty(customer.getNamespaceCustomerType())) {
            dto.setThirdPartFlag(true);
        }
        List<OrganizationMember> members = organizationProvider.listOrganizationMembersByUId(customer.getTrackingUid());
        if (members != null && members.size()>0) {
            dto.setTrackingPhone(members.get(0).getContactToken());
            dto.setTrackingName(dto.getTrackingName());
        }
        LOGGER.debug("search trackingName from organization  members end time  :{}",System.currentTimeMillis());
        if (customer.getLastTrackingTime() != null) {
            result = (int) ((System.currentTimeMillis() - customer.getLastTrackingTime().getTime()) / 86400000);
            dto.setTrackingPeriod(result);
        }
        ListServiceModuleAdministratorsCommand command = new ListServiceModuleAdministratorsCommand();
        command.setOrganizationId(customer.getOrganizationId());
        command.setCustomerId(customer.getId());
        command.setNamespaceId(UserContext.getCurrentNamespaceId());
        command.setCommunityId(customer.getCommunityId());
        List<OrganizationContactDTO> admins = customerService.listOrganizationAdmin(command);
        dto.setEnterpriseAdmins(admins);
        LOGGER.debug("list organization admins  end time  :{}",System.currentTimeMillis());
        //楼栋门牌
        ListCustomerEntryInfosCommand command1 = new ListCustomerEntryInfosCommand();
        command1.setCommunityId(customer.getCommunityId());
        command1.setCustomerId(customer.getId());
        List<CustomerEntryInfoDTO> entryInfos = customerService.listCustomerEntryInfosWithoutAuth(command1);
        entryInfos = removeDuplicatedEntryInfo(entryInfos);
        if (entryInfos != null && entryInfos.size() > 0) {
//            entryInfos = entryInfos.stream().peek((e) -> e.setAddressName(e.getAddressName().replace("-", "/"))).collect(Collectors.toList());
            entryInfos = entryInfos.stream().peek((e) -> e.setAddressName(e.getBuilding() + "/" + e.getApartment())).collect(Collectors.toList());
            dto.setEntryInfos(entryInfos);
        }



        CustomerRequirementDTO requirementDTO = invitedCustomerService.getCustomerRequirementDTOByCustomerId(dto.getId());
        if(requirementDTO != null){
            dto.setRequirement(requirementDTO);

        }



        List<CustomerContact> customerContacts = invitedCustomerProvider.findContactByCustomerId(dto.getId());
        List<CustomerContactDTO> contactDTOS = new ArrayList<>();
        if(customerContacts != null && customerContacts.size() > 0){
            customerContacts.forEach(r-> contactDTOS.add(ConvertHelper.convert(r, CustomerContactDTO.class)));
            dto.setContacts(contactDTOS);
        }



        List<CustomerTracker> trackers = invitedCustomerProvider.findTrackerByCustomerId(dto.getId());
        if(trackers != null && trackers.size() > 0){
            List<CustomerTrackerDTO> trackerDTOS = new ArrayList<>();
            trackers.forEach(r-> {
                CustomerTrackerDTO trackerDTO = ConvertHelper.convert(r, CustomerTrackerDTO.class);
                List<OrganizationMember> oMembers = organizationProvider.listOrganizationMembersByUId(trackerDTO.getTrackerUid());
                if (oMembers != null && oMembers.size()>0) {
                    trackerDTO.setTrackerPhone(oMembers.get(0).getContactToken());
                    trackerDTO.setTrackerName(oMembers.get(0).getContactName());
                }
                trackerDTOS.add(trackerDTO);
            });
            dto.setTrackers(trackerDTOS);
        }





        LOGGER.debug("customer entry info list end time  :{}",System.currentTimeMillis());
        return dto;
    }


    private EnterpriseCustomerDTO mobileConvertToDTO(EnterpriseCustomer customer) {
        LOGGER.debug("convertToDTO start time :{}",System.currentTimeMillis());
        EnterpriseCustomerDTO dto = ConvertHelper.convert(customer, EnterpriseCustomerDTO.class);
        Integer result = 0;
//        ScopeFieldItem categoryItem = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getCategoryItemId());

//        ScopeFieldItem levelItem = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getLevelItemId());


        if (dto.getTrackingUid() != null && dto.getTrackingUid() != 0) {
            dto.setTrackingName(dto.getTrackingName());
        }


        if (null != dto.getEntryStatusItemId()) {
            ScopeFieldItem entryStatusItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getOwnerId(),customer.getCommunityId(), dto.getEntryStatusItemId());
            if (null != entryStatusItem) {
                dto.setEntryStatusItemName(entryStatusItem.getItemDisplayName());
            } else {
                dto.setEntryStatusItemName(null);
            }
        }


        LOGGER.debug("switch items name end time :{}",System.currentTimeMillis());

        //21002 企业管理1.4（来源于第三方数据，企业名称栏为灰色不可修改） add by xiongying20171219
        if(!StringUtils.isEmpty(customer.getNamespaceCustomerType())) {
            dto.setThirdPartFlag(true);
        }

        if (customer.getLastTrackingTime() != null) {
            result = (int) ((System.currentTimeMillis() - customer.getLastTrackingTime().getTime()) / 86400000);
            dto.setTrackingPeriod(result);
        }
        ListServiceModuleAdministratorsCommand command = new ListServiceModuleAdministratorsCommand();
        command.setOrganizationId(customer.getOrganizationId());
        command.setCustomerId(customer.getId());
        command.setNamespaceId(UserContext.getCurrentNamespaceId());
        command.setCommunityId(customer.getCommunityId());
        /*
        List<OrganizationContactDTO> admins = customerService.listOrganizationAdmin(command);
        dto.setEnterpriseAdmins(admins);
        LOGGER.debug("list organization admins  end time  :{}",System.currentTimeMillis());
        */


        CustomerRequirementDTO requirementDTO = invitedCustomerService.getCustomerRequirementDTOByCustomerId(dto.getId());
        if(requirementDTO != null){
            dto.setRequirement(requirementDTO);

        }



        List<CustomerContact> customerContacts = invitedCustomerProvider.findContactByCustomerId(dto.getId());
        List<CustomerContactDTO> contactDTOS = new ArrayList<>();
        if(customerContacts != null && customerContacts.size() > 0){
            customerContacts.forEach(r-> contactDTOS.add(ConvertHelper.convert(r, CustomerContactDTO.class)));
            dto.setContacts(contactDTOS);
        }



        List<CustomerTracker> trackers = invitedCustomerProvider.findTrackerByCustomerId(dto.getId());
        if(trackers != null && trackers.size() > 0){
            List<CustomerTrackerDTO> trackerDTOS = new ArrayList<>();
            trackers.forEach(r-> {
                CustomerTrackerDTO trackerDTO = ConvertHelper.convert(r, CustomerTrackerDTO.class);
                List<OrganizationMember> oMembers = organizationProvider.listOrganizationMembersByUId(trackerDTO.getTrackerUid());
                if (oMembers != null && oMembers.size()>0) {
                    trackerDTO.setTrackerPhone(oMembers.get(0).getContactToken());
                    trackerDTO.setTrackerName(oMembers.get(0).getContactName());
                }
                trackerDTOS.add(trackerDTO);
            });
            dto.setTrackers(trackerDTOS);
        }





        LOGGER.debug("customer entry info list end time  :{}",System.currentTimeMillis());
        return dto;
    }

    private List<CustomerEntryInfoDTO> removeDuplicatedEntryInfo(List<CustomerEntryInfoDTO> entryInfos) {
        Map<Long, CustomerEntryInfoDTO> map = new HashMap<>();
        if (entryInfos != null && entryInfos.size() > 0) {
            entryInfos.forEach((e) -> {
                if (e.getAddressId() != null) {
                    Address address = addressProvider.findAddressById(e.getAddressId());
                    if (address != null) {
                        map.putIfAbsent(e.getAddressId(), e);
                    }
                }
            });
            return new ArrayList<>(map.values());
        }
        return null;
    }

    private Long getTomorrowLastTimestamp(Integer lastTrackingTime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, -(lastTrackingTime-1));
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime().getTime();
	}
}
