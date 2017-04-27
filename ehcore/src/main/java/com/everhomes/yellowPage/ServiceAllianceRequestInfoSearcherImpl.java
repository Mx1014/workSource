package com.everhomes.yellowPage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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

import com.alibaba.fastjson.JSONObject;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.payment.util.DownloadUtil;
import com.everhomes.rest.flow.FlowCaseDetailDTO;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowCaseEntityType;
import com.everhomes.rest.flow.FlowCaseFileDTO;
import com.everhomes.rest.flow.FlowCaseFileValue;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.user.GetRequestInfoCommand;
import com.everhomes.rest.user.RequestTemplateDTO;
import com.everhomes.rest.wifi.WifiOwnerType;
import com.everhomes.rest.yellowPage.GetRequestInfoResponse;
import com.everhomes.rest.yellowPage.JumpType;
import com.everhomes.rest.yellowPage.RequestInfoDTO;
import com.everhomes.rest.yellowPage.SearchOneselfRequestInfoCommand;
import com.everhomes.rest.yellowPage.SearchOrgRequestInfoCommand;
import com.everhomes.rest.yellowPage.SearchRequestInfoCommand;
import com.everhomes.rest.yellowPage.SearchRequestInfoResponse;
import com.everhomes.rest.yellowPage.ServiceAllianceRequestNotificationTemplateCode;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.SearchUtils;
import com.everhomes.search.ServiceAllianceRequestInfoSearcher;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.CustomRequestConstants;
import com.everhomes.user.UserActivityService;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;

@Component
public class ServiceAllianceRequestInfoSearcherImpl extends AbstractElasticSearch
	implements ServiceAllianceRequestInfoSearcher {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceAllianceRequestInfoSearcherImpl.class);
	
	@Autowired
	private YellowPageProvider yellowPageProvider;
	@Autowired
	private OrganizationProvider organizationProvider;
	
	@Autowired
	private ConfigurationProvider configProvider;
	
	@Autowired
	private UserActivityService userActivityService;
	
	@Autowired
	private LocaleStringService localeStringService;
	
	@Autowired
	private FlowService flowService;
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	@Override
	public void deleteById(Long id) {
		deleteById(id.toString());
		
	}

	@Override
	public void syncFromDb() {
		int pageSize = 200;      
        this.deleteAll();

        syncFromServiceAllianceRequestsDb(pageSize);
        syncFromReservationRequestsDb(pageSize);
        syncFromSettleRequestInfoSearcherDb(pageSize);
        syncFromServiceAllianceApartmentRequestsDb(pageSize);
        syncFromServiceAllianceInvestRequestsDb(pageSize);

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
            XContentBuilder source = createDoc(requestInfo);
            if(null != source) {
                LOGGER.info("reserve request id:" + request.getId() + "-" + request.getTemplateType());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(request.getId().toString() + "-" + request.getTemplateType()).source(source));
            }

        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }

	@Override
	public void feedDoc(ServiceAllianceRequestInfo request) {
		XContentBuilder source = createDoc(request);
        feedDoc(request.getId().toString() + "-" + request.getTemplateType(), source);
	}

	@Override
	public SearchRequestInfoResponse searchRequestInfo(
			SearchRequestInfoCommand cmd) {

		SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        
        QueryBuilder qb = null;
        if(cmd.getKeyword() == null || cmd.getKeyword().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
                    .field("creatorName", 1.2f)
                    .field("creatorOrganization", 1.0f);
            
            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("creatorName").addHighlightedField("creatorOrganization");
            
        }
        
        FilterBuilder fb = FilterBuilders.termFilter("ownerType", WifiOwnerType.fromCode(cmd.getOwnerType()).getCode());
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerId", cmd.getOwnerId()));
        if(cmd.getCategoryId() != null)
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("type", cmd.getCategoryId()));
        
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
		SearchRequestInfoResponse response = searchRequestInfo(cmd);
		List<RequestInfoDTO> list = response.getDtos();
		GetRequestInfoCommand command = new GetRequestInfoCommand();
		//获取模板名称到map中
		List<RequestTemplateDTO> templateList =  userActivityService.getCustomRequestTemplateByNamespace();
		Map<String, String> templateMap = new HashMap<String,String>();
		for (RequestTemplateDTO requestTemplate : templateList) {
			templateMap.put(requestTemplate.getTemplateType(), requestTemplate.getName());
		}
		
		//key = templateName,Value = [requests List,templateInfos List]
		//map存 （模板名称，[模板下申请集合，模板下附加属性集合]）
		Map<String,List[]> requestsInfoMap = new HashMap<String, List[]>();
		for (RequestInfoDTO requestInfo : list) {
			command.setId(requestInfo.getId());
			command.setTemplateType(requestInfo.getTemplateType());
			Object getRequestInfoResponse = null;
			if("flowCase".equals(command.getTemplateType())){
				getRequestInfoResponse=flowService.getFlowCaseDetail(requestInfo.getFlowCaseId(), UserContext.current().getUser().getId(),
						FlowUserType.PROCESSOR, true);
				
			}else{
				getRequestInfoResponse = userActivityService.getCustomRequestInfo(command);
			}
			List[] lists = requestsInfoMap.get(requestInfo.getTemplateType());
			if(lists==null){
				lists = new List[2];
				lists[0] = new ArrayList<RequestInfoDTO>();
				lists[1] = new ArrayList<GetRequestInfoResponse>();
				requestsInfoMap.put(requestInfo.getTemplateType(), lists);
			}
			lists[0].add(requestInfo);
			lists[1].add(getRequestInfoResponse);
		}
		
		XSSFWorkbook wb = new XSSFWorkbook();
		for (Map.Entry<String, List[]> entry : requestsInfoMap.entrySet()) {
			createXSSFWorkbook(wb, templateMap.get(entry.getKey()), entry.getValue());
		}
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			wb.write(out);
		    DownloadUtil.download(out, httpResponse);
		} catch (Exception e) {
			LOGGER.error("export error, e = {}", e);
		} finally{
			try {
				wb.close();
				out.close();
			} catch (IOException e) {
				LOGGER.error("close error", e);
			}
		}
	}
	
	/**
	 * 
	 * by dengs 20170427
	 */
    private void createXSSFWorkbook(XSSFWorkbook wb, String templateName,List[] requestInfos){
		templateName = templateName==null?
				localeStringService.getLocalizedString(ServiceAllianceRequestNotificationTemplateCode.SCOPE, 
						ServiceAllianceRequestNotificationTemplateCode.APPLY_STRING, 
						UserContext.current().getUser().getLocale(),""):templateName;
		XSSFSheet sheet = wb.createSheet(templateName);
		XSSFCellStyle style = wb.createCellStyle();// 样式对象
        Font font = wb.createFont();
        font.setFontHeightInPoints((short)20);
        font.setFontName("Courier New");

        style.setFont(font);

        XSSFCellStyle titleStyle = wb.createCellStyle();// 样式对象
        titleStyle.setFont(font);
        titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        int rowNum = 0;

        XSSFRow row1 = sheet.createRow(rowNum ++);
        row1.setRowStyle(style);
        
        String order_string = localeStringService.getLocalizedString(ServiceAllianceRequestNotificationTemplateCode.SCOPE, 
				ServiceAllianceRequestNotificationTemplateCode.ORDER_STRING, 
				UserContext.current().getUser().getLocale(),"");
        String username_string = localeStringService.getLocalizedString(ServiceAllianceRequestNotificationTemplateCode.SCOPE, 
        		ServiceAllianceRequestNotificationTemplateCode.USERNAME_STRING, 
        		UserContext.current().getUser().getLocale(),"");
        String phone_string = localeStringService.getLocalizedString(ServiceAllianceRequestNotificationTemplateCode.SCOPE, 
        		ServiceAllianceRequestNotificationTemplateCode.PHONE_STRING, 
        		UserContext.current().getUser().getLocale(),"");
        String company_string = localeStringService.getLocalizedString(ServiceAllianceRequestNotificationTemplateCode.SCOPE, 
        		ServiceAllianceRequestNotificationTemplateCode.COMPANY_STRING, 
        		UserContext.current().getUser().getLocale(),"");
        String service_alliance_string = localeStringService.getLocalizedString(ServiceAllianceRequestNotificationTemplateCode.SCOPE, 
        		ServiceAllianceRequestNotificationTemplateCode.SERVICE_ALLIANCE_STRING, 
        		UserContext.current().getUser().getLocale(),"");
        String submit_time_string = localeStringService.getLocalizedString(ServiceAllianceRequestNotificationTemplateCode.SCOPE, 
        		ServiceAllianceRequestNotificationTemplateCode.SUBMIT_TIME_STRING, 
        		UserContext.current().getUser().getLocale(),"");

        row1.createCell(0).setCellValue(order_string);
        row1.createCell(1).setCellValue(username_string);
        row1.createCell(2).setCellValue(phone_string);
        row1.createCell(3).setCellValue(company_string);
        row1.createCell(4).setCellValue(service_alliance_string);
        row1.createCell(5).setCellValue(submit_time_string);
        
        if(requestInfos[1]!=null && requestInfos[1].size()>0){
        	//申请表单
        	if(requestInfos[1].get(0) instanceof GetRequestInfoResponse){
	        	GetRequestInfoResponse response = (GetRequestInfoResponse)requestInfos[1].get(0);
	        	for (int i = 0; response!=null && i < response.getDtos().size(); i++) {
	        		 row1.createCell(6+i).setCellValue(response.getDtos().get(i).getFieldName());
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
	        			row.createCell(6+j).setCellValue(response.getDtos().get(j).getFieldValue());
	        		}
				}
        	}
        	//审批申请
        	else if(requestInfos[1].get(0) instanceof FlowCaseDetailDTO){
        		//列名称-列序号 存在表单修改的情况，所以名称也有可能改，根据名称来设置值。
        		Map<String,Integer> columnname = new HashMap<String,Integer>();
        		int nextcol = 6;
        		//值
        		for (int i = 0; i < requestInfos[0].size(); i++) {
	        		RequestInfoDTO requestInfoDTO = (RequestInfoDTO)requestInfos[0].get(i);
	        		XSSFRow row = sheet.createRow(rowNum ++);
	        		row.setRowStyle(style);
	        		//固定一行的值
	        		setFixedColumnToRow(row,requestInfoDTO,i+1);
	        		//不定数量的值
	        		FlowCaseDetailDTO flowCaseDetailDTO = (FlowCaseDetailDTO)requestInfos[1].get(i);
	        		if(flowCaseDetailDTO!=null){
	        			Map<Integer,String> mapImage = new HashMap<Integer,String>();
		        		List<FlowCaseEntity> entityLists = flowCaseDetailDTO.getEntities();
		        		if(entityLists!=null)
			        		for (FlowCaseEntity flowCaseEntity : entityLists) {
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
            b.field("createTime", request.getCreateTime().getTime());
            b.field("creatorUid", request.getCreatorUid()); 
            b.field("flowCaseId", request.getFlowCaseId());
            String d = format.format(request.getCreateTime().getTime());  
            try {
				Date date=format.parse(d);
				b.field("createDate", date.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
            
			Organization org = organizationProvider.findOrganizationById(request.getCreatorOrganizationId());
          
			if(org != null) {
			    b.field("creatorOrganization", org.getName());
            } else {
                b.field("creatorOrganization", "");
            }
            
			ServiceAlliances sa = yellowPageProvider.findServiceAllianceById(request.getServiceAllianceId(), request.getOwnerType(), request.getOwnerId());
            if(sa != null) {
            	b.field("serviceOrganization", sa.getName());
            } else {
                b.field("serviceOrganization", "");
            }
			
            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create ServiceAllianceRequestInfo " + request.getId() + "-" + request.getTemplateType() + " error");
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
            	dto.setFlowCaseId(SearchUtils.getLongField(source.get("flowCaseId")));
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

	/**
	 * 固定几行的设置
	 */
	private void setFixedColumnToRow(XSSFRow row, RequestInfoDTO requestInfoDTO, int i){
		row.createCell(0).setCellValue(i+1);
		row.createCell(1).setCellValue(requestInfoDTO.getCreatorName());
		row.createCell(2).setCellValue(requestInfoDTO.getCreatorMobile());
		row.createCell(3).setCellValue(requestInfoDTO.getCreatorOrganization());
		row.createCell(4).setCellValue(requestInfoDTO.getServiceOrganization());
		row.createCell(5).setCellValue(requestInfoDTO.getCreateTime());
	}
}
