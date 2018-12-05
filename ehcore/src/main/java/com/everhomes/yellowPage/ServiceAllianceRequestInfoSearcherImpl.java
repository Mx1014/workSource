package com.everhomes.yellowPage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.lang3.StringUtils;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeFilterBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseDetail;
import com.everhomes.flow.FlowCaseProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.general_approval.GeneralApprovalProvider;
import com.everhomes.general_approval.GeneralApprovalVal;
import com.everhomes.general_approval.GeneralApprovalValProvider;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.general_form.GeneralFormVal;
import com.everhomes.general_form.GeneralFormValProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationCommunityRequest;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.payment.util.DownloadUtil;
import com.everhomes.rest.common.EntityType;
import com.everhomes.rest.common.PrivilegeType;
import com.everhomes.rest.flow.FlowCaseDetailDTO;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowCaseEntityType;
import com.everhomes.rest.flow.FlowCaseFileDTO;
import com.everhomes.rest.flow.FlowCaseFileValue;
import com.everhomes.rest.flow.FlowCaseSearchType;
import com.everhomes.rest.flow.FlowCaseType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.flow.SearchFlowCaseCommand;
import com.everhomes.rest.general_approval.GeneralFormDataSourceType;
import com.everhomes.rest.general_approval.GeneralFormDataVisibleType;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_approval.PostApprovalFormTextValue;
import com.everhomes.rest.user.FieldContentType;
import com.everhomes.rest.user.GetRequestInfoCommand;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.RequestTemplateDTO;
import com.everhomes.rest.yellowPage.GetRequestInfoResponse;
import com.everhomes.rest.yellowPage.JumpType;
import com.everhomes.rest.yellowPage.RequestInfoDTO;
import com.everhomes.rest.yellowPage.SearchOneselfRequestInfoCommand;
import com.everhomes.rest.yellowPage.SearchOrgRequestInfoCommand;
import com.everhomes.rest.yellowPage.SearchRequestInfoCommand;
import com.everhomes.rest.yellowPage.SearchRequestInfoResponse;
import com.everhomes.rest.yellowPage.ServiceAllianceBelongType;
import com.everhomes.rest.yellowPage.ServiceAllianceRequestNotificationTemplateCode;
import com.everhomes.rest.yellowPage.ServiceAllianceWorkFlowStatus;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.SearchUtils;
import com.everhomes.search.ServiceAllianceRequestInfoSearcher;
import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.CustomRequestConstants;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityService;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class ServiceAllianceRequestInfoSearcherImpl extends AbstractElasticSearch
	implements ServiceAllianceRequestInfoSearcher {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceAllianceRequestInfoSearcherImpl.class);
	
	@Autowired
	private YellowPageProvider yellowPageProvider;
	
	@Autowired
	private YellowPageService yellowPageService;
	
	@Autowired
	private OrganizationProvider organizationProvider;
	
	@Autowired
	private UserPrivilegeMgr userPrivilegeMgr;
	@Autowired
	private ConfigurationProvider configProvider;
	
	@Autowired
	private UserActivityService userActivityService;
	
	@Autowired
	private LocaleStringService localeStringService;
	
	@Autowired
	private FlowService flowService;

    @Autowired
    private FlowCaseProvider flowCaseProvider;
	
	@Autowired
	private GeneralApprovalValProvider generalApprovalValProvider;
	
	@Autowired
	private GeneralFormProvider generalFormProvider;

    @Autowired
    private UserProvider userProvider;
    
    @Autowired
	private ServiceAllianceApplicationRecordProvider saapplicationRecordProvider;
    
    @Autowired
	private GeneralFormValProvider generalFormValProvider;
    

	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	@Override
	public void deleteById(Long id) {
		deleteById(id.toString());
		
	}

	@Override
	public void syncFromDb() {
		int pageSize = 200;      
		long pageAnchor = 0;
        this.deleteAll();
        
        while(true){
        	List<ServiceAllianceApplicationRecord> list = saapplicationRecordProvider.listServiceAllianceApplicationRecord(pageAnchor,pageSize);
        	for (ServiceAllianceApplicationRecord record : list) {
        		feedDoc(ConvertHelper.convert(record, ServiceAllianceRequestInfo.class));
			}
        	if(list.size()<pageSize){
        		break;
        	}
        	pageAnchor = list.get(list.size()-1).getId();
        }
        	
//        syncFromServiceAllianceRequestsDb(pageSize);
//        syncFromReservationRequestsDb(pageSize);
//        syncFromSettleRequestInfoSearcherDb(pageSize);
//        syncFromServiceAllianceApartmentRequestsDb(pageSize);
//        syncFromServiceAllianceInvestRequestsDb(pageSize);
//        syncFromServiceAllianceFlowCasesDb(pageSize);//同步通过工作流审批的申请记录。

	}

    private void syncFromServiceAllianceFlowCasesDb(int pageSize) {

        CrossShardListingLocator locator = new CrossShardListingLocator();
        SearchFlowCaseCommand cmd = new SearchFlowCaseCommand();
        cmd.setFlowCaseSearchType(FlowCaseSearchType.APPLIER.getCode());
        cmd.setModuleId(ServiceAllianceFlowModuleListener.MODULE_ID);
        for(;;) {
            List<FlowCaseDetail> details = flowCaseProvider.listFlowCasesByModuleId(locator, pageSize, cmd);

            if(details.size() > 0) {
                this.bulkUpdateServiceAllianceFlowCaseRequests(details);
            }

            if(locator.getAnchor() == null) {
                break;
            }
        }

        LOGGER.info("sync for service alliance flowcase request ok");

    }

    protected PostApprovalFormItem getFormFieldDTO(String string, List<PostApprovalFormItem> values) {
        for (PostApprovalFormItem val : values) {
            if (val.getFieldName().equals(string))
                return val;
        }
        return null;
    }
    
    public List<ServiceAllianceRequestInfo> generateUpdateServiceAllianceFlowCaseRequests(List<FlowCaseDetail> details){
    	List<ServiceAllianceRequestInfo> list = new ArrayList<>();
        for (FlowCaseDetail flowCase : details) {
            // 服务联盟的审批拼接工作流 content字符串
            ServiceAllianceRequestInfo request = new ServiceAllianceRequestInfo();
            List<GeneralApprovalVal> lists = generalApprovalValProvider.queryGeneralApprovalValsByFlowCaseId(flowCase.getId());
            List<PostApprovalFormItem> values = lists.stream().map(r -> {
                PostApprovalFormItem value = new PostApprovalFormItem();
//                value.setFieldDisplayName(r.get);
                value.setFieldName(r.getFieldName());
                value.setFieldType(r.getFieldType());
                value.setFieldValue(r.getFieldStr3());
                return value;
            }).collect(Collectors.toList());
            PostApprovalFormItem sourceVal = getFormFieldDTO(GeneralFormDataSourceType.SOURCE_ID.getCode(),values);
            Long yellowPageId = 0l;
            if(null != sourceVal){
                yellowPageId = Long.valueOf(JSON.parseObject(sourceVal.getFieldValue(), PostApprovalFormTextValue.class).getText());
                ServiceAlliances  yellowPage = yellowPageProvider.findServiceAllianceById(yellowPageId,null,null);
                ServiceAllianceCategories  parentPage = yellowPageProvider.findCategoryById(yellowPage.getParentId());
                request.setServiceAllianceId(yellowPageId);
                request.setType(yellowPage.getParentId());
                
                request.setSecondCategoryId(yellowPage.getCategoryId());
                request.setSecondCategoryName(yellowPage.getServiceType());

            }
            //服务联盟加一个申请
            PostApprovalFormItem organizationVal = getFormFieldDTO(GeneralFormDataSourceType.ORGANIZATION_ID.getCode(),values);

            User user = this.userProvider.findUserById(flowCase.getApplyUserId());
            if(user!=null) {
                UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
                if (identifier != null) {
                    request.setCreatorMobile(identifier.getIdentifierToken());
                }
            }
            request.setJumpType(2L);
            request.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            if(lists!=null && lists.size()>0) {
                request.setCreateTime(lists.get(0).getCreateTime());
            }
            request.setCreatorName(user!=null?user.getNickName():"");
            if(organizationVal!= null && organizationVal.getFieldValue() != null){
                PostApprovalFormTextValue organizationvalue = JSON.parseObject(organizationVal.getFieldValue(), PostApprovalFormTextValue.class);
                if(organizationvalue != null) {
                    request.setCreatorOrganizationId(Long.valueOf(organizationvalue.getText()));
                }
            }

            if (EntityType.COMMUNITY.getCode().equals(flowCase.getProjectType()) || "community".equals(flowCase.getProjectType())
                    || EhCommunities.class.getName().equals(flowCase.getProjectType())){
            	// bydengs,修改owner
            	 request.setOwnerType(ServiceAllianceBelongType.COMMUNITY.getCode());
            	 request.setOwnerId(flowCase.getProjectId());
//                request.setOwnerType(EntityType.ORGANIZATIONS.getCode());
//                List<Organization> communityList = organizationProvider.findOrganizationByCommunityId(flowCase.getProjectId());
//                request.setOwnerId(communityList.get(0).getId());
            }else{
            	OrganizationCommunityRequest ocr =organizationProvider.getOrganizationCommunityRequestByOrganizationId(flowCase.getProjectId());
            	if(ocr != null){
            		request.setOwnerType(ServiceAllianceBelongType.COMMUNITY.getCode());
                	request.setOwnerId(ocr.getCommunityId());
            	}else{
            		request.setOwnerType(flowCase.getProjectType());
                	request.setOwnerId(flowCase.getProjectId());
            	}
            }
            request.setFlowCaseId(flowCase.getId());
            request.setId(flowCase.getId());
            request.setCreatorUid(user.getId());
            request.setTemplateType("flowCase");
           
            
            Byte status = flowCase.getStatus();
    		if(FlowCaseType.fromCode(flowCase.getCaseType()) == FlowCaseType.DUMB){
    			status = ServiceAllianceWorkFlowStatus.NONE.getCode();
    		}
    		request.setWorkflowStatus(status);
            list.add(request);
        }
        return list;
    }

    private void bulkUpdateServiceAllianceFlowCaseRequests(List<FlowCaseDetail> details){
    	 List<ServiceAllianceRequestInfo> list = generateUpdateServiceAllianceFlowCaseRequests(details);
    	 for (ServiceAllianceRequestInfo request : list) {
    		feedDoc(request);
            LOGGER.debug("request = "+request);
        }
    }

    private void syncFromServiceAllianceInvestRequestsDb(int pageSize) {

        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<ServiceAllianceInvestRequests> requests = yellowPageProvider.listInvestRequests(locator, pageSize);

            if(requests.size() > 0) {
                this.bulkUpdateServiceAllianceInvestRequests(requests);
            }

            if(locator.getAnchor() == null) {
                break;
            }
        }

        LOGGER.info("sync for service alliance apartment request ok");

    }

    private void syncFromServiceAllianceApartmentRequestsDb(int pageSize) {

        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<ServiceAllianceApartmentRequests> requests = yellowPageProvider.listApartmentRequests(locator, pageSize);

            if(requests.size() > 0) {
                this.bulkUpdateServiceAllianceApartmentRequests(requests);
            }

            if(locator.getAnchor() == null) {
                break;
            }
        }

        LOGGER.info("sync for service alliance apartment request ok");

    }

    private void syncFromServiceAllianceRequestsDb( int pageSize) {
        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<ServiceAllianceRequests> requests = yellowPageProvider.listServiceAllianceRequests(locator, pageSize);

            if(requests.size() > 0) {
                this.bulkUpdateServiceAllianceRequests(requests);
            }

            if(locator.getAnchor() == null) {
                break;
            }
        }

        LOGGER.info("sync for service alliance request ok");
    }

    private void syncFromReservationRequestsDb( int pageSize) {
        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<ReservationRequests> requests = yellowPageProvider.listReservationRequests(locator, pageSize);

            if(requests.size() > 0) {
                this.bulkUpdateReservationRequests(requests);
            }

            if(locator.getAnchor() == null) {
                break;
            }
        }

        LOGGER.info("sync for reserve request ok");
    }

    private void syncFromSettleRequestInfoSearcherDb( int pageSize) {
        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<SettleRequests> requests = yellowPageProvider.listSettleRequests(locator, pageSize);

            if(requests.size() > 0) {
                this.bulkUpdateSettleRequests(requests);
            }

            if(locator.getAnchor() == null) {
                break;
            }
        }

        LOGGER.info("sync for settle request ok");
    }

	@Override
	public String getIndexType() {
		return SearchUtils.SAREQUEST;
	}

	@Override
	public void bulkUpdateServiceAllianceRequests(List<ServiceAllianceRequests> requests) {
		BulkRequestBuilder brb = getClient().prepareBulk();
        for (ServiceAllianceRequests request : requests) {
            ServiceAllianceRequestInfo requestInfo = ConvertHelper.convert(request, ServiceAllianceRequestInfo.class);
            requestInfo.setTemplateType(CustomRequestConstants.SERVICE_ALLIANCE_REQUEST_CUSTOM);
            requestInfo.setJumpType(JumpType.TEMPLATE.getCode());
            requestInfo.setSecondCategoryId(request.getCategoryId());
            ServiceAllianceCategories categories = yellowPageProvider.findCategoryById(request.getCategoryId());
            requestInfo.setSecondCategoryName(categories==null?null:categories.getName());
            requestInfo.setWorkflowStatus(ServiceAllianceWorkFlowStatus.NONE.getCode());
            XContentBuilder source = createDoc(requestInfo);
            if(null != source) {
                LOGGER.info("service alliance request id:" + request.getId()+"-EhServiceAllianceRequests");
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(request.getId()+"-EhServiceAllianceRequests").source(source));
            }
            
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
		
	}

    @Override
    public void bulkUpdateServiceAllianceInvestRequests(List<ServiceAllianceInvestRequests> requests) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (ServiceAllianceInvestRequests request : requests) {
            ServiceAllianceRequestInfo requestInfo = ConvertHelper.convert(request, ServiceAllianceRequestInfo.class);
            requestInfo.setTemplateType(CustomRequestConstants.INVEST_REQUEST_CUSTOM);
            requestInfo.setJumpType(JumpType.TEMPLATE.getCode());
            requestInfo.setSecondCategoryId(request.getCategoryId());
            ServiceAllianceCategories categories = yellowPageProvider.findCategoryById(request.getCategoryId());
            requestInfo.setSecondCategoryName(categories==null?null:categories.getName());
            requestInfo.setWorkflowStatus(ServiceAllianceWorkFlowStatus.NONE.getCode());
            XContentBuilder source = createDoc(requestInfo);
            if(null != source) {
                LOGGER.info("service alliance invest request id:" + request.getId()+"-EhServiceAllianceInvestRequests");
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(request.getId()+"-EhServiceAllianceInvestRequests").source(source));
            }

        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }

    }

    @Override
    public void bulkUpdateServiceAllianceApartmentRequests(List<ServiceAllianceApartmentRequests> requests) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (ServiceAllianceApartmentRequests request : requests) {
            ServiceAllianceRequestInfo requestInfo = ConvertHelper.convert(request, ServiceAllianceRequestInfo.class);
            requestInfo.setTemplateType(CustomRequestConstants.APARTMENT_REQUEST_CUSTOM);
            requestInfo.setJumpType(JumpType.TEMPLATE.getCode());
            requestInfo.setSecondCategoryId(request.getCategoryId());
            ServiceAllianceCategories categories = yellowPageProvider.findCategoryById(request.getCategoryId());
            requestInfo.setSecondCategoryName(categories==null?null:categories.getName());
            requestInfo.setWorkflowStatus(ServiceAllianceWorkFlowStatus.NONE.getCode());
            XContentBuilder source = createDoc(requestInfo);
            if(null != source) {
                LOGGER.info("service alliance apartment request id:" + request.getId() + "-EhServiceAllianceApartmentRequests");
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(request.getId().toString() + "-EhServiceAllianceApartmentRequests").source(source));
            }

        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }

    @Override
    public void bulkUpdateSettleRequests(List<SettleRequests> requests) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (SettleRequests request : requests) {
            ServiceAllianceRequestInfo requestInfo = ConvertHelper.convert(request, ServiceAllianceRequestInfo.class);
            requestInfo.setTemplateType(CustomRequestConstants.SETTLE_REQUEST_CUSTOM);
            requestInfo.setJumpType(JumpType.TEMPLATE.getCode());
            requestInfo.setSecondCategoryId(request.getCategoryId());
            ServiceAllianceCategories categories = yellowPageProvider.findCategoryById(request.getCategoryId());
            requestInfo.setSecondCategoryName(categories==null?null:categories.getName());
            requestInfo.setWorkflowStatus(ServiceAllianceWorkFlowStatus.NONE.getCode());
            XContentBuilder source = createDoc(requestInfo);
            if(null != source) {
                LOGGER.info("settle request id:" + request.getId() + "-EhSettleRequests");
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(request.getId().toString() + "-EhSettleRequests").source(source));
            }

        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }

    }

    @Override
    public void bulkUpdateReservationRequests(List<ReservationRequests> requests) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (ReservationRequests request : requests) {
            ServiceAllianceRequestInfo requestInfo = ConvertHelper.convert(request, ServiceAllianceRequestInfo.class);
            requestInfo.setTemplateType(CustomRequestConstants.RESERVE_REQUEST_CUSTOM);
            requestInfo.setJumpType(JumpType.TEMPLATE.getCode());
            requestInfo.setSecondCategoryId(request.getCategoryId());
            ServiceAllianceCategories categories = yellowPageProvider.findCategoryById(request.getCategoryId());
            requestInfo.setSecondCategoryName(categories==null?null:categories.getName());
            requestInfo.setWorkflowStatus(ServiceAllianceWorkFlowStatus.NONE.getCode());
            XContentBuilder source = createDoc(requestInfo);
            if(null != source) {
                LOGGER.info("reserve request id:" + getDocIdByTemplateType(request.getId(), request.getTemplateType()));
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(getDocIdByTemplateType(request.getId(), request.getTemplateType())).source(source));
            }

        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }

	@Override
	public void feedDoc(ServiceAllianceRequestInfo request) {
		XContentBuilder source = createDoc(request);
        feedDoc(getDocIdByTemplateType(request.getFlowCaseId(), request.getTemplateType()), source);
	}

	@Override
	public SearchRequestInfoResponse searchRequestInfo(
			SearchRequestInfoCommand cmd) {
		if (cmd.getAppId()!=null) {
			yellowPageService.checkPrivilege(PrivilegeType.APPLY_RECORD, cmd.getCurrentPMId(), cmd.getAppId(), cmd.getOwnerId());
		}

		SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        
        QueryBuilder qb = null;
        if(cmd.getKeyword() == null || cmd.getKeyword().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
            		.field("creatorMobile", 1.3f)
                    .field("creatorName", 1.2f)
                    .field("serviceOrganization", 1.1f)
                    .field("creatorOrganization", 1.0f);
            
            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("creatorMobile").addHighlightedField("creatorName")
            .addHighlightedField("serviceOrganization").addHighlightedField("creatorOrganization");
            
        }
        FilterBuilder fb = null;
        if(ServiceAllianceBelongType.ORGANAIZATION == ServiceAllianceBelongType.fromCode(cmd.getOwnerType())){
        	//
        	List<OrganizationCommunity> result = organizationProvider.listOrganizationCommunities(cmd.getOwnerId());
        	
        	for (OrganizationCommunity orgcommunity : result) {
        		FilterBuilder orfb = FilterBuilders.termFilter("ownerType", ServiceAllianceBelongType.COMMUNITY.getCode());
        		orfb = FilterBuilders.andFilter(orfb, FilterBuilders.termFilter("ownerId", orgcommunity.getCommunityId()));
        		if(fb == null){
        			fb = orfb;
        		}else{
        			fb = FilterBuilders.orFilter(fb,orfb);
        		}
			}
        }else{
	        fb = FilterBuilders.termFilter("ownerType", cmd.getOwnerType());
	        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerId", cmd.getOwnerId()));
        }

        if(cmd.getCategoryId() != null)
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("type", cmd.getCategoryId()));
        
        if(cmd.getSecondCategoryId() != null)
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("secondCategoryId", cmd.getSecondCategoryId()));
        
        if(cmd.getWorkflowStatus() != null)
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("workflowStatus", cmd.getWorkflowStatus()));
        
        RangeFilterBuilder rf = new RangeFilterBuilder("createDate");
        if(cmd.getStartDay() != null) {
        	rf.gte(cmd.getStartDay());
        	fb = FilterBuilders.andFilter(fb, rf); 
        }
        
        if(cmd.getEndDay() != null) {
        	rf.lte(cmd.getEndDay());
        	fb = FilterBuilders.andFilter(fb, rf); 
        }
        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0l;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }
        
        qb = QueryBuilders.filteredQuery(qb, fb);
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        builder.setFrom(anchor.intValue() * pageSize).setSize(pageSize + 1);
        builder.setQuery(qb);
        
        if(cmd.getKeyword() == null || cmd.getKeyword().isEmpty()) {
            builder.addSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC).ignoreUnmapped(true));
        }
        
        if(LOGGER.isDebugEnabled())
			LOGGER.info("ServiceAllianceRequestInfoSearcherImpl query builder ："+builder);
        
        SearchResponse rsp = builder.execute().actionGet();
        SearchRequestInfoResponse response = new SearchRequestInfoResponse();
        List<RequestInfoDTO> dtos = getDTOs(rsp);
        
        if(dtos.size() > pageSize){
        	response.setNextPageAnchor(anchor+1);
        	dtos.remove(dtos.size() - 1);
        }
        
        response.setDtos(dtos);
        
		return response;
	}
	
	@Override
	public void exportRequestInfo(SearchRequestInfoCommand cmd, HttpServletResponse httpResponse) {
		//申请记录
		if(cmd.getPageSize()==null){
			cmd.setPageSize(configProvider.getIntValue("service.alliance.export.max.num", 150));
			cmd.setPageAnchor(0L);
		}
		SearchRequestInfoResponse response = searchRequestInfo(cmd);
		if(response.getDtos()!=null && response.getDtos().size()>0){
			//获取模板名称到map中
			Map<String, String> templateMap = getTemplateMap();
			
			//key = templateName,Value = [requests List,templateInfos List]
			//map存 （模板名称，[模板下申请集合，模板下附加属性集合]）
			//如('审批',[['序号':1,'用户姓名':'邓爽'],['附加属性1':'值1','附加属性2':'值2']])
			Integer formId = null;
			Map<String,List[]> requestsInfoMap = new HashMap<String, List[]>();
			GetRequestInfoCommand command = new GetRequestInfoCommand();
			forLoop:
			for (RequestInfoDTO requestInfo : response.getDtos()) {
				command.setId(requestInfo.getId());
				command.setTemplateType(requestInfo.getTemplateType());
				Object extrasInfo = null;
				//审批另外调用接口，返回的object，通过类型判断在生成sheet时做对应处理，参考createXSSFWorkbook(XSSFWorkbook wb, String templateName,List[] requestInfos)
				if ("flowCase".equals(command.getTemplateType())) {
					try {
						extrasInfo = flowService.getFlowCaseDetail(requestInfo.getFlowCaseId(),
								UserContext.current().getUser().getId(), FlowUserType.PROCESSOR, true);
					} catch (RuntimeErrorException e) {
						LOGGER.error("{}", e);
						continue forLoop;
					}
					GeneralFormVal val = generalFormValProvider.getGeneralFormValBySourceIdAndName(
							requestInfo.getFlowCaseId(), ServiceAllianceFlowModuleListener.FORM_VAL_SOURCE_TYPE_NAME,
							GeneralFormDataSourceType.USER_NAME.getCode());
					if (val != null) {
						GeneralForm form = this.generalFormProvider
								.getActiveGeneralFormByOriginIdAndVersion(val.getFormOriginId(), val.getFormVersion());
						if (form == null)
							continue;
						// 使用表单名称+表单id作为sheet的名称 by dengs 20170510
						requestInfo.setTemplateType("flowCase" + form.getId());
						templateMap.put("flowCase" + form.getId(), form.getFormName() + form.getId());
					}
					
				}else{
					extrasInfo = userActivityService.getCustomRequestInfo(command);
				}
				putValuesToMap(requestsInfoMap,requestInfo,extrasInfo);
			}
			
			//生成excel并，输出到httpResponse
			ServiceAllianceCategories category = yellowPageProvider.findCategoryById(cmd.getCategoryId());
			try(
				XSSFWorkbook wb = new XSSFWorkbook();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
			){
				for (Map.Entry<String, List[]> entry : requestsInfoMap.entrySet()) {
					createXSSFWorkbook(wb, templateMap.get(entry.getKey()), entry.getValue());
				}
				wb.write(out);
				if(category!=null){
					DownloadUtil.download(out, httpResponse,category.getName()+"apply records");
				}else{
					DownloadUtil.download(out, httpResponse);
				}
			} catch (Exception e) {
				LOGGER.error("export error, e = {}", e);
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
	                    e.getMessage());
			} 
		}
	}
	
	/**
	 * by dengs,将标准属性值 附加属性值存放到map中
	 */
	private void putValuesToMap(Map<String, List[]> requestsInfoMap, RequestInfoDTO requestInfo, Object extrasInfo) {
		// TODO Auto-generated method stub
		List[] lists = requestsInfoMap.get(requestInfo.getTemplateType());
		if(lists==null){
			lists = new List[2];
			//lists[0]存放标准属性
			lists[0] = new ArrayList<RequestInfoDTO>();
			//lists[1]存放附加属性
			lists[1] = new ArrayList<GetRequestInfoResponse>();
			requestsInfoMap.put(requestInfo.getTemplateType(), lists);
		}
		lists[0].add(requestInfo);
		lists[1].add(extrasInfo);
	}

	/**
	 * by dengs 20170427 获取申请模板
	 */
	private Map<String, String> getTemplateMap() {
		List<RequestTemplateDTO> templateList =  userActivityService.getCustomRequestTemplateByNamespace();
		Map<String, String> templateMap = new HashMap<String,String>();
		for (RequestTemplateDTO requestTemplate : templateList) {
			templateMap.put(requestTemplate.getTemplateType(), requestTemplate.getName());
		}
		return templateMap;
	}

	/**
	 * 
	 * by dengs 20170427
	 */
    private void createXSSFWorkbook(XSSFWorkbook wb, String templateName,List[] requestInfos){
    	//创建style
    	XSSFCellStyle style = createStyle(wb);
    	//创建sheet
        XSSFSheet sheet = createSheet(wb,templateName,style);
        
        int rowNum = 0;

        //填充数据到sheet
        if(requestInfos[1]!=null && requestInfos[1].size()>0){
        	//申请表单
        	if(requestInfos[1].get(0) instanceof GetRequestInfoResponse){
        		 //创建第一行
                XSSFRow row1 = createRow1(sheet,style,rowNum++);
	        	GetRequestInfoResponse response = (GetRequestInfoResponse)requestInfos[1].get(0);
	        	for (int i = 0; response!=null && i < response.getDtos().size(); i++) {
	        		FieldContentType fieldContentType = FieldContentType.fromCode(response.getDtos().get(i).getFieldContentType());
	        		//只导出文本数据 by dengs,2017 05 09
	        		if(fieldContentType == FieldContentType.TEXT){
	        			row1.createCell(3+i).setCellValue(response.getDtos().get(i).getFieldName());
	        		}
	        	}
	        	
	        	for (int i = 0; i < requestInfos[0].size(); i++) {
	        		RequestInfoDTO requestInfoDTO = (RequestInfoDTO)requestInfos[0].get(i);
	        		GetRequestInfoResponse getRequestInfoResponse = (GetRequestInfoResponse)requestInfos[1].get(i);
	        		XSSFRow row = sheet.createRow(rowNum ++);
	        		row.setRowStyle(style);
	        		//固定一行的值
	        		setFixedColumnToRow(row,requestInfoDTO,i+1);
	        		response = (GetRequestInfoResponse)requestInfos[1].get(i);
	        		//不定数量的值
	        		for (int j = 0; response!=null && j < response.getDtos().size(); j++) {
	        			FieldContentType fieldContentType = FieldContentType.fromCode(response.getDtos().get(j).getFieldContentType());
		        		//只导出文本数据 by dengs,2017 05 09
	        			if(fieldContentType == FieldContentType.TEXT){
		        			row.createCell(3+j).setCellValue(response.getDtos().get(j).getFieldValue());
		        		}
	        		}
				}
        	}
        	//审批申请
        	else if(requestInfos[1].get(0) instanceof FlowCaseDetailDTO){
        		
        		Long formId = null;
        		if (requestInfos[0].size() > 0) { 
        			int templateTypePrefixLen = "flowCase".length();
        			String templateType = ((RequestInfoDTO)requestInfos[0].get(0)).getTemplateType();
        			formId = templateType.length() >  templateTypePrefixLen ? Long.parseLong(templateType.substring(templateTypePrefixLen)) : null;
        		}
        		
        		// 判断是否要导出固定区域中姓名，手机号，企业 这三个前置字段
        		boolean needExportUserFixedInfoField = isNeedExportUserFixedInfoField(formId);
        		
        		//列名称-列序号 存在表单修改的情况，所以名称也有可能改，根据名称来设置值。
        		 //创建第一行
                XSSFRow row1 = createRow1forFlowCase(sheet,style,rowNum++, needExportUserFixedInfoField);
        		Map<String,Integer> columnname = new HashMap<String,Integer>();
        		int nextcol = row1.getLastCellNum();
        		//值
        		for (int i = 0; i < requestInfos[0].size(); i++) {
	        		RequestInfoDTO requestInfoDTO = (RequestInfoDTO)requestInfos[0].get(i);
	        		XSSFRow row = sheet.createRow(rowNum ++);
	        		row.setRowStyle(style);
	        		//固定一行的值
	        		setFixedColumnToRow(row,requestInfoDTO,i+1, needExportUserFixedInfoField);
	        		//不定数量的值
	        		FlowCaseDetailDTO flowCaseDetailDTO = (FlowCaseDetailDTO)requestInfos[1].get(i);
	        		if(flowCaseDetailDTO!=null){
	        			Map<Integer,String> mapImage = new HashMap<Integer,String>();
		        		List<FlowCaseEntity> entityLists = flowCaseDetailDTO.getEntities();
		        		if(entityLists!=null)
			        		for (FlowCaseEntity flowCaseEntity : entityLists) {
			        			//不导出图片和文件
			        			if(isExportColumn(flowCaseEntity)){
				        			Integer col = columnname.get(flowCaseEntity.getKey());
				        			if(col==null){
				        				col = createColumnHeads(columnname, nextcol, flowCaseEntity, row1);
				        				nextcol++;
				        			}
				        			row.createCell(col)
				        				.setCellValue(getFileFieldValue(flowCaseEntity,col,mapImage));
			        			}
							}
	        		}
				}
        	}
        }
    }
    
    /**
     *  by dengs,20170510 部分申请字段不导出
     */
    private boolean isExportColumn(FlowCaseEntity flowCaseEntity){
    	FlowCaseEntityType flowCaseEntityType =	FlowCaseEntityType.fromCode(flowCaseEntity.getEntityType());
    	String notExportColumns = localeStringService.getLocalizedString(ServiceAllianceRequestNotificationTemplateCode.SCOPE, 
  				ServiceAllianceRequestNotificationTemplateCode.EXCEL_NOTEXPORT_COLUMN_STRING, 
  				UserContext.current().getUser().getLocale(),"1,2,3");
    	String[] notExportColumnArray = notExportColumns.split(",");
    	return 	flowCaseEntityType!=FlowCaseEntityType.FILE 
				&& flowCaseEntityType!=FlowCaseEntityType.IMAGE
				&& !notExportColumnArray[0].equals(flowCaseEntity.getKey())
				&& !notExportColumnArray[1].equals(flowCaseEntity.getKey())
				&& !notExportColumnArray[2].equals(flowCaseEntity.getKey());
    }

	/**
     * by dengs,创建第一行，20170502
     */
    private XSSFRow createRow1(XSSFSheet sheet,XSSFCellStyle style,int rowNum) {
		// TODO Auto-generated method stub
    	  XSSFRow row1 = sheet.createRow(rowNum);
          row1.setRowStyle(style);
          
          String excelHeads = localeStringService.getLocalizedString(ServiceAllianceRequestNotificationTemplateCode.SCOPE, 
  				ServiceAllianceRequestNotificationTemplateCode.EXCEL_HEAD_STRING, 
  				UserContext.current().getUser().getLocale(),"row1");
          String[] excelHeadArray = excelHeads.split(",");
          String order_string = excelHeadArray[0];
//          String username_string =excelHeadArray.length>1?excelHeadArray[1]:"row2";
//          String phone_string = excelHeadArray.length>2?excelHeadArray[2]:"row3";
//          String company_string = excelHeadArray.length>3?excelHeadArray[3]:"row4";
          String service_alliance_string = excelHeadArray.length>4?excelHeadArray[4]:"row5";
          String submit_time_string = excelHeadArray.length>5?excelHeadArray[5]:"row6";

          row1.createCell(0).setCellValue(order_string);
//          row1.createCell(1).setCellValue(username_string);
//          row1.createCell(2).setCellValue(phone_string);
//          row1.createCell(3).setCellValue(company_string);
          row1.createCell(1).setCellValue(service_alliance_string);
          row1.createCell(2).setCellValue(submit_time_string);
          return row1;
	}
    /**
     * by dengs,创建第一行，20170502
     * @param flowCaseDetailDTO 
     */
    private XSSFRow createRow1forFlowCase(XSSFSheet sheet,XSSFCellStyle style,int rowNum, boolean needExportUserFixedInfo) {
    	// TODO Auto-generated method stub
    	XSSFRow row1 = sheet.createRow(rowNum);
    	row1.setRowStyle(style);
    	
    	String excelHeads = localeStringService.getLocalizedString(ServiceAllianceRequestNotificationTemplateCode.SCOPE, 
    			ServiceAllianceRequestNotificationTemplateCode.EXCEL_HEAD_STRING, 
    			UserContext.current().getUser().getLocale(),"row1");
    	String[] excelHeadArray = excelHeads.split(",");
    	String order_string = excelHeadArray[0];
    	String username_string =excelHeadArray.length>1?excelHeadArray[1]:"row2";
    	String phone_string = excelHeadArray.length>2?excelHeadArray[2]:"row3";
    	String company_string = excelHeadArray.length>3?excelHeadArray[3]:"row4";
    	
    	int nextColumnNum = 0;
    	row1.createCell(nextColumnNum++).setCellValue(order_string);
    	
    	if(needExportUserFixedInfo){
    		row1.createCell(nextColumnNum++).setCellValue(username_string);
    		row1.createCell(nextColumnNum++).setCellValue(phone_string);
    		row1.createCell(nextColumnNum++).setCellValue(company_string);
    	}
    	
    	return row1;
    }
    

	/**
     * by dengs,创建cell style,20170502
     */
    private XSSFCellStyle createStyle(XSSFWorkbook wb){
		// TODO Auto-generated method stub
		XSSFCellStyle style = wb.createCellStyle();// 样式对象
        Font font = wb.createFont();
        font.setFontHeightInPoints((short)20);
        font.setFontName("Courier New");

        style.setFont(font);

        return style;
	}
    
    /**
     * by dengs,创建sheet，20170502
     */
    private XSSFSheet createSheet(XSSFWorkbook wb, String templateName,XSSFCellStyle style) {
		// TODO Auto-generated method stub
    	templateName = templateName==null?
				localeStringService.getLocalizedString(ServiceAllianceRequestNotificationTemplateCode.SCOPE, 
						ServiceAllianceRequestNotificationTemplateCode.APPLY_STRING, 
						UserContext.current().getUser().getLocale(),""):templateName;
		XSSFSheet sheet = wb.getSheet(templateName);
		if(sheet==null){
			sheet = wb.createSheet(templateName);
		}
        return sheet;
	}

	/**
     * 数据的名称找不到对应的列，就需要创建一个，调用此方法 
     */
    private int createColumnHeads(Map<String, Integer> columnname, int nextcol, FlowCaseEntity flowCaseEntity,XSSFRow row1) {
    	//头
		Integer col = columnname.get(flowCaseEntity.getKey());
		if(col==null){
			col = nextcol;
			columnname.put(flowCaseEntity.getKey(), col);
			row1.createCell(col).setCellValue(flowCaseEntity.getKey());
		}
		return col;
	}
    
    /**
	 * 文件json，转URL,URL,URL....
	 * <b>URL:/</b>
	 * <p></p>
     * @param col 
     * @param row 
     * @param mapImage 
	 */
	private String getFileFieldValue(FlowCaseEntity flowCaseEntity, Integer col, Map<Integer, String> mapImage){
		FlowCaseEntityType flowCaseEntityType = FlowCaseEntityType.fromCode(flowCaseEntity.getEntityType());
		StringBuffer buffer = new StringBuffer();
		if(FlowCaseEntityType.FILE == flowCaseEntityType){
			FlowCaseFileValue files = JSONObject.parseObject(flowCaseEntity.getValue(), FlowCaseFileValue.class);
			for (FlowCaseFileDTO flowCaseFileDTO : files.getFiles()) {
				buffer.append(flowCaseFileDTO.getUrl());
				buffer.append(",");
			}
			return buffer.toString().substring(0,  buffer.toString().length()-1);
		}else if(FlowCaseEntityType.IMAGE == flowCaseEntityType){
			String originalValue = mapImage.get(col);
			originalValue = originalValue==null?"":originalValue+",";
			mapImage.put(col, buffer.append(originalValue).append(flowCaseEntity.getValue()).toString());
			return mapImage.get(col);
		}
		return flowCaseEntity.getValue();
	}

	@Override
    public SearchRequestInfoResponse searchOneselfRequestInfo(SearchOneselfRequestInfoCommand cmd) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());

        QueryBuilder qb = null;

        FilterBuilder fb = FilterBuilders.termFilter("type", cmd.getType());
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("creatorUid", UserContext.current().getUser().getId()));

        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0l;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }

        qb = QueryBuilders.filteredQuery(qb, fb);
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        builder.setFrom(anchor.intValue() * pageSize).setSize(pageSize + 1);
        builder.setQuery(qb);
        builder.addSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC).ignoreUnmapped(true));
        
        if(LOGGER.isDebugEnabled())
            LOGGER.info("ServiceAllianceRequestInfoSearcherImpl query builder ："+builder);

        SearchResponse rsp = builder.execute().actionGet();
        SearchRequestInfoResponse response = new SearchRequestInfoResponse();
        List<RequestInfoDTO> dtos = getDTOs(rsp);

        if(dtos.size() > pageSize){
            response.setNextPageAnchor(anchor+1);
            dtos.remove(dtos.size() - 1);
        }

        response.setDtos(dtos);

        return response;
    }

    @Override
    public SearchRequestInfoResponse searchOrgRequestInfo(SearchOrgRequestInfoCommand cmd) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());

        QueryBuilder qb = null;

        FilterBuilder fb = FilterBuilders.termFilter("creatorOrganizationId", cmd.getOrgId());

        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0l;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }

        qb = QueryBuilders.filteredQuery(qb, fb);
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        builder.setFrom(anchor.intValue() * pageSize).setSize(pageSize + 1);
        builder.setQuery(qb);
        builder.addSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC).ignoreUnmapped(true));

        if(LOGGER.isDebugEnabled())
            LOGGER.info("ServiceAllianceRequestInfoSearcherImpl query builder ："+builder);

        SearchResponse rsp = builder.execute().actionGet();
        SearchRequestInfoResponse response = new SearchRequestInfoResponse();
        List<RequestInfoDTO> dtos = getDTOs(rsp);

        if(dtos.size() > pageSize){
            response.setNextPageAnchor(anchor+1);
            dtos.remove(dtos.size() - 1);
        }

        response.setDtos(dtos);

        return response;
    }

    private XContentBuilder createDoc(ServiceAllianceRequestInfo request){
		try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("jumpType", request.getJumpType());
            b.field("templateType", request.getTemplateType());
            b.field("type", request.getType());
            b.field("ownerType", request.getOwnerType());
            b.field("ownerId", request.getOwnerId());
            b.field("creatorName", request.getCreatorName());
            b.field("creatorOrganizationId", request.getCreatorOrganizationId());
            b.field("creatorMobile", request.getCreatorMobile());
            if(request.getCreateTime()!=null){
            	b.field("createTime", request.getCreateTime().getTime());
            	 String d = format.format(request.getCreateTime().getTime());  
                 try {
     				Date date=format.parse(d);
     				b.field("createDate", date.getTime());
     			} catch (Exception e) {
     				e.printStackTrace();
     			}
            }
            b.field("creatorUid", request.getCreatorUid()); 
            b.field("flowCaseId", request.getFlowCaseId());
            b.field("secondCategoryId", request.getSecondCategoryId());
            b.field("secondCategoryName", request.getSecondCategoryName());
            b.field("workflowStatus", request.getWorkflowStatus());
            b.field("creatorOrganization", request.getCreatorOrganization()==null?"":request.getCreatorOrganization());
            
			ServiceAlliances sa = yellowPageProvider.findServiceAllianceById(request.getServiceAllianceId(), request.getOwnerType(), request.getOwnerId());
            if(sa != null) {
            	b.field("serviceOrganization", sa.getName());
            } else {
                b.field("serviceOrganization", request.getServiceOrganization()==null?"":request.getServiceOrganization());
            }
			
            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create ServiceAllianceRequestInfo " + getDocIdByTemplateType(request.getId(), request.getTemplateType()) + " error");
            return null;
        }
    }
	
	private List<RequestInfoDTO> getDTOs(SearchResponse rsp) {
        List<RequestInfoDTO> dtos = new ArrayList<RequestInfoDTO>();
        SearchHit[] docs = rsp.getHits().getHits();
        for (SearchHit sd : docs) {
            try {
            	RequestInfoDTO dto = new RequestInfoDTO();
            	String[] ids = sd.getId().split("-");
            	dto.setId(Long.parseLong(ids[0]));
            	Map<String, Object> source = sd.getSource();
                dto.setTemplateType(String.valueOf(source.get("templateType")));
            	dto.setCreatorName(String.valueOf(source.get("creatorName")));
            	dto.setCreatorMobile(String.valueOf(source.get("creatorMobile")));
            	dto.setCreatorOrganization(String.valueOf(source.get("creatorOrganization")));
            	dto.setServiceOrganization(String.valueOf(source.get("serviceOrganization")));
            	dto.setSecondCategoryName(String.valueOf(source.get("secondCategoryName")));
            	dto.setFlowCaseId(SearchUtils.getLongField(source.get("flowCaseId")));
            	dto.setSecondCategoryId(SearchUtils.getLongField(source.get("secondCategoryId")));
            	dto.setWorkflowStatus(Byte.valueOf(String.valueOf(source.get("workflowStatus"))));
            	Long time = SearchUtils.getLongField(source.get("createDate"));  
                String day = format.format(time);
            	dto.setCreateTime(day);
            	
            	dto.setJumpType(SearchUtils.getLongField(source.get("jumpType")));
            	dtos.add(dto);
            }
            catch(Exception ex) {
                LOGGER.info("getRequestInfoDTOs error " + ex.getMessage());
            }
        }
        
        return dtos;
    }

	private void setFixedColumnToRow(XSSFRow row, RequestInfoDTO requestInfoDTO, int i){
		setFixedColumnToRow(row, requestInfoDTO, i,null);
	}
	/**
	 * 固定几行的设置
	 */
	private void setFixedColumnToRow(XSSFRow row, RequestInfoDTO requestInfoDTO, int i, Boolean needExportUserFixedInfoField){
		if(needExportUserFixedInfoField !=null){
			int nextColumnNum = 0;
			row.createCell(nextColumnNum++).setCellValue(i);
			if(needExportUserFixedInfoField){
				row.createCell(nextColumnNum++).setCellValue(requestInfoDTO.getCreatorName());
				row.createCell(nextColumnNum++).setCellValue(requestInfoDTO.getCreatorMobile());
				row.createCell(nextColumnNum++).setCellValue(requestInfoDTO.getCreatorOrganization());
			}
			
//			row.createCell(nextColumnNum++).setCellValue(requestInfoDTO.getServiceOrganization());
//			row.createCell(nextColumnNum++).setCellValue(requestInfoDTO.getCreateTime());
			
		}else{
			row.createCell(0).setCellValue(i);
//			row.createCell(1).setCellValue(requestInfoDTO.getCreatorName());
//			row.createCell(2).setCellValue(requestInfoDTO.getCreatorMobile());
//			row.createCell(3).setCellValue(requestInfoDTO.getCreatorOrganization());
			row.createCell(1).setCellValue(requestInfoDTO.getServiceOrganization());
			row.createCell(2).setCellValue(requestInfoDTO.getCreateTime());
		}
	}
	
    
	/**
	 * 更新单个字段的值
	 * 
	 * @param id 记录id
	 * @param field  字段名称 
	 * @param reNewValue 字段值
	 */
	public void updateDocByField(Long flowCaseId, String templateType, String field, Object reNewValue) {
		getClient().prepareUpdate().setIndex(getIndexName()).setType(getIndexType())
				.setId(getDocIdByTemplateType(flowCaseId, templateType)).setDoc(field, reNewValue).execute()
				.actionGet();
	}
	
	private String getDocIdByTemplateType(Long Id, String templateType) {
		return Id + "-" +templateType;
	}
	
	
	private List<GeneralFormFieldDTO> getFormFieldDtoList(Long formId) {
		if (null == formId) {
			return null;
		}
		
		GeneralForm form = generalFormProvider.getGeneralFormById(formId);
		if (null == form) {
			return null;
		}

		return JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
	}
	
	/**   
	* @Function: ServiceAllianceRequestInfoSearcherImpl.java
	* @Description: 根据表单id判断是否要导出固定的用户姓名，手机号码，企业 这三个字段
	*
	* @version: v1.0.0
	* @author:	 黄明波
	* @date: 2018年6月30日 下午3:53:49 
	*
	*/
	private boolean isNeedExportUserFixedInfoField(Long formId) {
		
		List<GeneralFormFieldDTO> formFieldDtos = getFormFieldDtoList(formId);
		if (CollectionUtils.isEmpty(formFieldDtos)) { // 如果没有表单，需要显示
			return true;
		}

		String notExportColumns = localeStringService.getLocalizedString(
				ServiceAllianceRequestNotificationTemplateCode.SCOPE,
				ServiceAllianceRequestNotificationTemplateCode.EXCEL_NOTEXPORT_FORM_FIELD_TYPE_STRING,
				UserContext.current().getUser().getLocale(), null);
		if (StringUtils.isBlank(notExportColumns)) {
			return true;
		}

		String[] notExportColumnArray = notExportColumns.split(",");

		for (GeneralFormFieldDTO dto : formFieldDtos) {

			for (String colum : notExportColumnArray) {
				if (colum.equals(dto.getDataSourceType())
						&& !GeneralFormDataVisibleType.HIDDEN.getCode().equals(dto.getVisibleType())) {
					return false;
				}
			}
		}

		return true;
	}
}
