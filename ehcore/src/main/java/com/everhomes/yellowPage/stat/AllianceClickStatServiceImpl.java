package com.everhomes.yellowPage.stat;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.rest.common.PrivilegeType;
import com.everhomes.rest.statistics.event.StatEventCommonStatus;
import com.everhomes.rest.statistics.event.StatEventLogDTO;
import com.everhomes.rest.yellowPage.GetSelfDefinedStateCommand;
import com.everhomes.rest.yellowPage.IdNameDTO;
import com.everhomes.rest.yellowPage.ListServiceNamesCommand;
import com.everhomes.rest.yellowPage.ServiceAllianceBelongType;
import com.everhomes.rest.yellowPage.YellowPageServiceErrorCode;
import com.everhomes.rest.yellowPage.stat.ClickStatDTO;
import com.everhomes.rest.yellowPage.stat.ClickStatDetailDTO;
import com.everhomes.rest.yellowPage.stat.ClickTypeDTO;
import com.everhomes.rest.yellowPage.stat.InterestStatDTO;
import com.everhomes.rest.yellowPage.stat.ListClickStatCommand;
import com.everhomes.rest.yellowPage.stat.ListClickStatDetailCommand;
import com.everhomes.rest.yellowPage.stat.ListClickStatDetailResponse;
import com.everhomes.rest.yellowPage.stat.ListClickStatResponse;
import com.everhomes.rest.yellowPage.stat.ListInterestStatResponse;
import com.everhomes.rest.yellowPage.stat.ListServiceTypeNamesCommand;
import com.everhomes.rest.yellowPage.stat.ListStatCommonCommand;
import com.everhomes.rest.yellowPage.stat.ServiceAndTypeNameDTO;
import com.everhomes.rest.yellowPage.stat.StatClickOrSortType;
import com.everhomes.rest.yellowPage.stat.TestClickStatCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhAllianceStat;
import com.everhomes.sms.DateUtil;
import com.everhomes.statistics.event.StatEvent;
import com.everhomes.statistics.event.StatEventContentLogProvider;
import com.everhomes.statistics.event.StatEventDeviceLog;
import com.everhomes.statistics.event.StatEventDeviceLogProvider;
import com.everhomes.statistics.event.StatEventHandler;
import com.everhomes.statistics.event.StatEventLog;
import com.everhomes.statistics.event.StatEventLogContent;
import com.everhomes.statistics.event.StatEventLogProvider;
import com.everhomes.statistics.event.StatEventParamLog;
import com.everhomes.statistics.event.StatEventParamLogProvider;
import com.everhomes.statistics.event.StatEventProvider;
import com.everhomes.statistics.event.StatEventStepExecution;
import com.everhomes.statistics.event.handler.StatEventHandlerManager;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;
import com.everhomes.util.excel.ExcelUtils;
import com.everhomes.yellowPage.AllianceConfigState;
import com.everhomes.yellowPage.AllianceConfigStateProvider;
import com.everhomes.yellowPage.AllianceStandardService;
import com.everhomes.yellowPage.ServiceAllianceCategories;
import com.everhomes.yellowPage.ServiceAlliances;
import com.everhomes.yellowPage.YellowPageProvider;
import com.everhomes.yellowPage.YellowPageUtils;

@Component
public class AllianceClickStatServiceImpl implements AllianceClickStatService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AllianceClickStatServiceImpl.class);
	
	private final ThreadLocal<ClickStatTool> ThreadLocalClickStatTool = new ThreadLocal<ClickStatTool>();

	@Autowired
	YellowPageProvider yellowPageProvider;
	
	@Autowired
	ClickStatProvider clickStatProvider;
	
	@Autowired
	ClickStatDetailProvider clickStatDetailProvider;	
	
	@Autowired
	UserProvider userProvider;
	
	@Autowired
	ConfigurationProvider configProvider;
	
	@Autowired
	private UserPrivilegeMgr userPrivilegeMgr;
	
	@Autowired
	private CommunityProvider communityProvider;
	
	@Autowired
	private StatEventContentLogProvider statEventContentLogProvider;
	
	@Autowired
	private StatEventDeviceLogProvider statEventDeviceLogProvider;
	
	@Autowired
	private StatEventProvider statEventProvider;
	
	@Autowired
	private StatEventLogProvider statEventLogProvider;
	
	@Autowired
	private StatEventParamLogProvider statEventParamLogProvider;
	
	@Autowired
	private NamespaceProvider namespaceProvider;
	
	@Autowired
	AllianceStandardService allianceStandardService;
	@Autowired
	private AllianceConfigStateProvider allianceConfigStateProvider;
	
	

	@Override
	public ListInterestStatResponse listInterestStat(ListStatCommonCommand cmd) {

		// 校验权限
		checkPrivilege(PrivilegeType.USER_BEHAVIOUR_STAT, cmd.getCurrentPMId(), cmd.getAppId(), cmd.getCurrentProjectId());

		List<InterestStatDTO> dtos = new ArrayList<>();
		CrossShardListingLocator locator = new CrossShardListingLocator();

		// 进行查询
		locator.setAnchor(cmd.getPageAnchor());
		dtos = listInterestStat(locator, cmd);

		// 组织返回
		ListInterestStatResponse resp = new ListInterestStatResponse();
		resp.setNextPageAnchor(locator.getAnchor());
		resp.setDtos(dtos);
		return resp;
	}
	

	@Override
	public ListClickStatResponse listClickStat(ListClickStatCommand cmd) {
		
		// 校验权限
		checkPrivilege(PrivilegeType.USER_BEHAVIOUR_STAT, cmd.getCurrentPMId(), cmd.getAppId(), cmd.getCurrentProjectId());

		//获取数据
		ListingLocator locator = new ListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<ClickStatDTO> dtos = listClickStat(locator, cmd.getPageSize(), cmd.getType(), cmd.getOwnerId(),
				cmd.getCategoryId(), cmd.getStartDate(), cmd.getEndDate(), cmd.getSearchType(), cmd.getSortOrder(),
				cmd.getSortType());

		// 组装返回数据
		ListClickStatResponse resp = new ListClickStatResponse();
		resp.setDtos(dtos);
		resp.setNextPageAnchor(locator.getAnchor());
		return resp;
	}

	@Override
	public ListClickStatDetailResponse listClickStatDetail(ListClickStatDetailCommand cmd) {
		
		// 校验权限
		checkPrivilege(PrivilegeType.USER_BEHAVIOUR_STAT, cmd.getCurrentPMId(), cmd.getAppId(), cmd.getCurrentProjectId());
		
		ListingLocator locator = new ListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		locator.setEntityId(cmd.getPageAnchorId() == null ? 0L : cmd.getPageAnchorId());
		List<ClickStatDetailDTO> dtos = listClickStatDetail(locator, cmd, null);
		if (!StringUtils.isEmpty(cmd.getContactPhone()) && CollectionUtils.isEmpty(dtos)) {
			//如果通过手机号没有查到，可能是数据库存的是旧手机号，故通过id来查
			UserIdentifier userInfo = userProvider.findClaimedIdentifierByTokenAndNamespaceId(cmd.getContactPhone(),
					UserContext.getCurrentNamespaceId());
			if (null != userInfo) {
				cmd.setContactPhone(null);
				dtos = listClickStatDetail(locator, cmd, userInfo.getOwnerUid());
			}
		}

		// 构建返回参数
		ListClickStatDetailResponse resp = new ListClickStatDetailResponse();
		resp.setDtos(dtos);
		resp.setNextPageAnchor(locator.getAnchor());
		resp.setNextPageAnchorId(locator.getEntityId() > 0 ? locator.getEntityId() : null);
		return resp;
	}
	

	@Override
	public void exportClickStatDetail(ListClickStatDetailCommand cmd, HttpServletRequest request, HttpServletResponse resp) {
		// 校验权限
		checkPrivilege(PrivilegeType.USER_BEHAVIOUR_STAT, cmd.getCurrentPMId(), cmd.getAppId(), cmd.getCurrentProjectId());
		// TODO Auto-generated method stub
		
		List<ClickStatDetailDTO> dtos = listClickStatDetail(null, cmd, null);
		if (!StringUtils.isEmpty(cmd.getContactPhone()) && CollectionUtils.isEmpty(dtos)) {
			//如果通过手机号没有查到，可能是数据库存的是旧手机号，故通过id来查
			UserIdentifier userInfo = userProvider.findClaimedIdentifierByTokenAndNamespaceId(cmd.getContactPhone(),
					UserContext.getCurrentNamespaceId());
			if (null != userInfo) {
				cmd.setContactPhone(null);
				dtos = listClickStatDetail(null, cmd, userInfo.getOwnerUid());
			}
		}
		
		// 获取联盟名称/项目名称
		String fileName = buildCommonFileName(cmd.getType(), cmd.getOwnerId());
		String sheetName = "明细查询";
		String[] propertyNames = { "userName", "userPhone", "clickTypeName", "serviceName", "serviceTypeName", "clickShowTime"};
		String[] titleNames = { "用户姓名", "联系电话" , "用户行为", "服务名称", "服务类型", "操作时间"};
		int[] columnSizes = { 40, 20, 20, 40, 40, 20 };
		ExcelUtils excelUtils = new ExcelUtils(request, resp, fileName + "-" + sheetName, sheetName);
		excelUtils.writeExcel(propertyNames, titleNames, columnSizes, dtos);
	}

	@Override
	public void exportClickStat(ListClickStatCommand cmd, HttpServletRequest request, HttpServletResponse resp) {

		// 校验权限
		checkPrivilege(PrivilegeType.USER_BEHAVIOUR_STAT, cmd.getCurrentPMId(), cmd.getAppId(),
				cmd.getCurrentProjectId());

		List<ClickStatDTO> dtos = listClickStat(null, null, cmd.getType(), cmd.getOwnerId(), cmd.getCategoryId(),
				cmd.getStartDate(), cmd.getEndDate(), cmd.getSearchType(), cmd.getSortOrder(), cmd.getSortType());

		// 获取联盟名称/项目名称
		String fileName = buildCommonFileName(cmd.getType(), cmd.getOwnerId());
		String sheetName = "服务类型相关统计";

		// 生成excel
		if (TYPE_STAT_SEARCH_BY_SERVICE.equals(cmd.getSearchType())) {
			String[] propertyNames = { "serviceName", "serviceTypeName", "serviceClickCount", "supportClickCount",
					"shareClickCount", "commitClickCount", "commitTimes", "conversionShowPercent" };
			String[] titleNames = { "服务名称", "服务类型", "查看详情", "咨询", "分享", "点击申请按钮", "提交申请", "转化率" };
			int[] columnSizes = { 40, 40, 20, 20, 20, 20, 20, 20 };
			sheetName = "服务相关统计";
			ExcelUtils excelUtils = new ExcelUtils(request, resp, fileName + "-" + sheetName, sheetName);
			excelUtils.writeExcel(propertyNames, titleNames, columnSizes, dtos);
			return;
		}

		String[] propertyNames = { "serviceTypeName", "serviceTypeCount" };
		String[] titleNames = { "服务类型", "点击次数" };
		int[] columnSizes = { 40, 20 };
		ExcelUtils excelUtils = new ExcelUtils(request, resp, fileName + "-" + sheetName, sheetName);
		excelUtils.writeExcel(propertyNames, titleNames, columnSizes, dtos);
		return;

	}

	@Override
	public void exportInterestStat(ListStatCommonCommand cmd, HttpServletRequest request, HttpServletResponse resp) {
		// 校验权限
		checkPrivilege(PrivilegeType.USER_BEHAVIOUR_STAT, cmd.getCurrentPMId(), cmd.getAppId(), cmd.getCurrentProjectId());
		
		List<InterestStatDTO>  dtos = listInterestStat(null, cmd);
		
		// 获取联盟名称/项目名称
		String fileName = buildCommonFileName(cmd.getType(), cmd.getOwnerId());
		String sheetName = "服务类型排名";

		// 生成excel
		if (TYPE_STAT_SEARCH_BY_SERVICE.equals(cmd.getSearchType())) {
			String[] propertyNames = { "serviceName", "serviceTypeName", "interestStat"};
			String[] titleNames = { "服务名称", "服务类型", "兴趣指数"};
			int[] columnSizes = { 40, 40, 20 };
			sheetName = "服务排名";
			ExcelUtils excelUtils = new ExcelUtils(request, resp, fileName + "-" + sheetName, sheetName);
			excelUtils.writeExcel(propertyNames, titleNames, columnSizes, dtos);
			return;
		}

		String[] propertyNames = { "serviceTypeName", "interestStat" };
		String[] titleNames = { "服务类型", "兴趣指数" };
		int[] columnSizes = { 40, 20 };
		ExcelUtils excelUtils = new ExcelUtils(request, resp, fileName + "-" + sheetName, sheetName);
		excelUtils.writeExcel(propertyNames, titleNames, columnSizes, dtos);
		return;
	}

	@Override
	public List<ClickTypeDTO> listClickTypes() {

		List<ClickTypeDTO> typeDtos = new ArrayList<>();
		ClickTypeDTO dto = null;

		for (StatClickOrSortType value : StatClickOrSortType.values()) {
			if (!StatClickOrSortType.CLICK_TYPE.equals(value.getType())) {
				continue;
			}

			dto = new ClickTypeDTO();
			dto.setType(value.getCode());
			dto.setName(value.getInfo());
			typeDtos.add(dto);
		}

		if (0 == typeDtos.size()) {
			return null;
		}

		return typeDtos;
	}

	@Override
	public List<IdNameDTO> listServiceNames(ListServiceNamesCommand cmd) {
		// 校验权限
		checkPrivilege(PrivilegeType.USER_BEHAVIOUR_STAT, cmd.getCurrentPMId(), cmd.getAppId(), cmd.getCurrentProjectId());
		
		List<ServiceAndTypeNameDTO> dtos = yellowPageProvider.listServiceNames(cmd.getType(), cmd.getOwnerId(),
				cmd.getCategoryId());
		if (CollectionUtils.isEmpty(dtos)) {
			return null;
		}

		return dtos.stream().map(r -> {
			return ConvertHelper.convert(r, IdNameDTO.class);
		}).collect(Collectors.toList());
	}
	
	@Override
	public List<IdNameDTO> listServiceTypeNames(ListServiceTypeNamesCommand cmd) {
		// 校验权限
		checkPrivilege(PrivilegeType.USER_BEHAVIOUR_STAT, cmd.getCurrentPMId(), cmd.getAppId(),
				cmd.getCurrentProjectId());
		
		String ownerType = cmd.getOwnerType();
		Long ownerId = cmd.getOwnerId();
		if (ServiceAllianceBelongType.COMMUNITY.getCode().equals(cmd.getOwnerType())) {
			AllianceConfigState state = allianceConfigStateProvider.findConfigState(cmd.getType(),ownerId);
			if (allianceStandardService.isDisableSelfConfig(state)) {
				ownerId = allianceStandardService.getOrgIdByTypeAndProjectId(cmd.getType(), ownerId);
				ownerType = ServiceAllianceBelongType.ORGANAIZATION.getCode();
			}
		}

		List<IdNameDTO> dtos = yellowPageProvider.listServiceTypeNames(ownerType, ownerId,  cmd.getType());
		if (null != dtos && dtos.size() > 1) {
			return dtos.stream().filter(r -> !r.getParentId().equals(0L)).collect(Collectors.toList());
		}
		
		
		

		return dtos;
	}
	
	private String buildCommonFileName(Long type, Long ownerId) {
		StringBuilder fileName = new StringBuilder();

		String allianceName = null;
		if (null != type) {
			ServiceAllianceCategories sc = allianceStandardService.queryHomePageCategoryByScene(type, ownerId);
			if (null != sc) {
				allianceName = sc.getName();
			}
		}

		String communityName = null;
		if (null != ownerId) {
			Community community = communityProvider.findCommunityById(ownerId);
			if (null != community) {
				communityName = community.getName();
			}
		}

		fileName.append(allianceName + "-");
		if (null != communityName) {
			fileName.append(communityName + "-");
		}

		fileName.append(DateUtil.dateToStr(new java.util.Date(), DateUtil.NO_SLASH));
		return fileName.toString();
	}

	private List<InterestStatDTO> listInterestStat(CrossShardListingLocator locator, ListStatCommonCommand cmd) {

		// 获取相关的服务统计。
		List<ClickStatDTO> clickStats = listClickStatForInterestStat(locator, cmd);
		if (CollectionUtils.isEmpty(clickStats)) {
			return null;
		}

		// 根据统计计算分数
		List<InterestStatDTO> interestDtos = listInterestStat(locator, cmd.getPageSize(), cmd.getSortOrder(),
				cmd.getSearchType(), clickStats);
		if (CollectionUtils.isEmpty(interestDtos)) {
			return null;
		}

		return interestDtos;
	}

	private List<ClickStatDTO> listClickStatForInterestStat(CrossShardListingLocator locator,
			ListStatCommonCommand cmd) {
		return listClickStat(null, null, cmd.getType(), cmd.getOwnerId(), cmd.getCategoryId(), cmd.getStartDate(), cmd.getEndDate(),
				cmd.getSearchType(), cmd.getSortOrder(), null);
	}

	/**
	 * 获取每日统计的实现
	 * 
	 * @param type
	 * @param ownerId
	 * @param categoryId
	 * @param startDate
	 * @param endDate
	 * @param sortOrder
	 * @param sortType
	 * @return
	 */
	private List<ClickStatDTO> listClickStat(ListingLocator locator, Integer pageSize, Long type,
			Long ownerId, Long categoryId, String startDate, String endDate, Byte searchType, Byte sortOrder,
			Byte sortType) {

		// 根据条件获取所有记录
		Timestamp startTime = getTrueClickStatStartTime(startDate);
		Timestamp endTime = getTrueClickStatEndTime(endDate);
		List<ClickStat> stats = clickStatProvider.listStats(type, ownerId, categoryId, startTime, endTime, searchType);
		if (CollectionUtils.isEmpty(stats)) {
			return null;
		}

		// 转化成dto
		List<ClickStatDTO> statDtos = convertClickStatsToDtos(stats, searchType);

		// 做排序
		statDtos = reSortClickStatDtos(statDtos, searchType, sortType, sortOrder);

		// 做分页
		return buildSubStatByPageAnchor(locator, pageSize, statDtos);
	}


	private String calConversionPercent(Long commitTimes, Long clickCount) {
		if (null == clickCount || null == commitTimes || 0 == clickCount) {
			return "0.00";
		}

		BigDecimal commitTimesBig = new BigDecimal(commitTimes*100);
		BigDecimal clickCountBig = new BigDecimal(clickCount);
		return commitTimesBig.divide(clickCountBig, CONVERSION_PERCENT_RETAIN_DECIMAL, CONVERSION_PERCENT_ROUNDING_MODE)
				.toPlainString();
	}

	private List<ClickStatDTO> reSortClickStatDtos(List<ClickStatDTO> statDtos, Byte searchType, Byte sortType,
			Byte sortOrder) {
		if (TYPE_STAT_SEARCH_BY_SERVICE.equals(searchType)) {
			return reSortClickStatByServiceDtos(statDtos, sortType, sortOrder);
		}

		return reSortClickStatByServiceTypeDtos(statDtos, sortOrder);
	}

	private List<ClickStatDTO> reSortClickStatByServiceTypeDtos(List<ClickStatDTO> statDtos, final Byte sortOrder) {

		final int sortFactor = TYPE_SORT_INTEREST_STAT_ASC.equals(sortOrder) ? -1 : 1;

		return statDtos.stream().sorted((x, y) -> {
			return sortClickCountInnerCompare(x.getServiceTypeCount(), y.getServiceTypeCount(), sortFactor);
		}).collect(Collectors.toList());
	}

	private List<ClickStatDTO> reSortClickStatByServiceDtos(List<ClickStatDTO> statDtos, Byte sortType,
			Byte sortOrder) {

		final int sortFactor = TYPE_SORT_INTEREST_STAT_ASC.equals(sortOrder) ? -1 : 1;

		return statDtos.stream().sorted((x, y) -> {

			Long xCnt = getStatClickCountBySortType(x, sortType);
			Long yCnt = getStatClickCountBySortType(y, sortType);

			return sortClickCountInnerCompare(xCnt, yCnt, sortFactor);

		}).collect(Collectors.toList());
	}

	private Long getStatClickCountBySortType(ClickStatDTO dto, Byte sortType) {

		if (null == sortType || StatClickOrSortType.SORT_TYPE_CONVERSION_PERCENT.getCode().equals(sortType)) {
			if (StringUtils.isEmpty(dto.getConversionPercent())) {
				return null;
			}
			
			BigDecimal bigDecimal = new BigDecimal(dto.getConversionPercent());
			bigDecimal = bigDecimal.multiply(new BigDecimal(Math.pow(10, CONVERSION_PERCENT_RETAIN_DECIMAL)));
			return bigDecimal.longValue();
		}

		if (StatClickOrSortType.CLICK_TYPE_SERVICE.getCode().equals(sortType)) {
			return dto.getServiceClickCount();
		}

		if (StatClickOrSortType.CLICK_TYPE_COMMIT_BUTTON.getCode().equals(sortType)) {
			return dto.getCommitClickCount();
		}

		if (StatClickOrSortType.CLICK_TYPE_SUPPORT.getCode().equals(sortType)) {
			return dto.getSupportClickCount();
		}

		if (StatClickOrSortType.CLICK_TYPE_SHARE.getCode().equals(sortType)) {
			return dto.getShareClickCount();
		}

		if (StatClickOrSortType.CLICK_TYPE_COMMIT_TIMES.getCode().equals(sortType)) {
			return null == dto.getCommitTimes() ? null : dto.getCommitTimes().longValue();
		}

		LOGGER.error("no such sortType:" + sortType);
		return null;
	}

	private int sortClickCountInnerCompare(Long xCnt, Long yCnt, int sortFactor) {

		if (null == xCnt && null == yCnt) {
			return 0;
		}

		if (null == xCnt) {
			return 1;
		}

		if (null == yCnt) {
			return -1;
		}

		long ret = (yCnt - xCnt) * sortFactor;
		if (ret > 0) {
			return 1;
		} else if (ret < 0) {
			return -1;
		}

		return 0;
	}

	private List<ClickStatDTO> convertClickStatsToDtos(List<ClickStat> stats, Byte searchType) {
		
		if (TYPE_STAT_SEARCH_BY_SERVICE.equals(searchType)) {
			return convertClickStatsToServiceDtos(stats);
		} 
		
		return convertClickStatsToServiceTypeDtos(stats);
	}
	
	private List<ClickStatDTO> convertClickStatsToServiceDtos(List<ClickStat> stats) {

		Map<Long, ClickStatDTO> map = new HashMap<>();
		ClickStatDTO dto = null;
		String serviceName = null;
		Long serviceId = null;
		for (ClickStat stat : stats) {
			
			serviceId = stat.getServiceId();
			serviceName = getStatTool(stat.getType()).getServiceName(serviceId);
			if (null == serviceName) {
				// 如果当前数据没有改记录，则跳过
				continue;
			}

			dto = map.get(serviceId);
			if (null == dto) {
				dto = new ClickStatDTO();
				dto.setServiceId(serviceId);
				dto.setServiceName(serviceName);
				IdNameDTO idName = getStatTool(stat.getType()).getTypeByServiceId(serviceId);
				if (null != idName) {
					dto.setServiceTypeId(idName.getId());
					dto.setServiceTypeName(idName.getName());
				}
				map.put(serviceId, dto);
			}

			setClickStatAttribute(dto, stat);
		}
		
		// 进行转化率计算
		List<ClickStatDTO> dtos = new ArrayList<ClickStatDTO>(map.values());
		for (ClickStatDTO statDto : dtos) {
			computeStatConversionPercent(statDto);
		}

		return dtos;
	}
	
	private void computeStatConversionPercent(ClickStatDTO statDto) {

		if (null == statDto.getCommitTimes() || 0 == statDto.getCommitTimes()) {
			statDto.setConversionPercent("0.00");
			return;
		}

		Long totalClickCount = 0L;
		totalClickCount += statDto.getServiceClickCount() == null ? 0L : statDto.getServiceClickCount();// 查看详情
		totalClickCount += statDto.getSupportClickCount() == null ? 0L : statDto.getSupportClickCount();// 咨询
		totalClickCount += statDto.getShareClickCount() == null ? 0L : statDto.getShareClickCount();// 分享
		totalClickCount += statDto.getCommitClickCount() == null ? 0L : statDto.getCommitClickCount();// 点击申请按钮

		// 转化率 = 提交申请 / (查看详情 + 咨询 + 分享 + 点击申请按钮)
		statDto.setConversionPercent(calConversionPercent(statDto.getCommitTimes().longValue(), totalClickCount));
	}


	private List<ClickStatDTO> convertClickStatsToServiceTypeDtos(List<ClickStat> stats) {

		Map<Long, ClickStatDTO> map = new HashMap<>();
		ClickStatDTO dto = null;
		String serviceTypeName = null;
		Long serviceTypeId = null;
		for (ClickStat stat : stats) {
			
			serviceTypeId = stat.getCategoryId();
			serviceTypeName = getStatTool(stat.getType()).getServiceTypeName(serviceTypeId);
			if (null == serviceTypeName) {
				// 如果当前数据没有该类型，则跳过
				continue;
			}

			dto = map.get(serviceTypeId);
			if (null == dto) {
				dto = new ClickStatDTO();
				dto.setServiceTypeId(serviceTypeId);
				dto.setServiceTypeName(serviceTypeName);
				map.put(serviceTypeId, dto);
			}

			setClickStatAttribute(dto, stat);
		}

		return new ArrayList<ClickStatDTO>(map.values());
	}

	private void setClickStatAttribute(ClickStatDTO dto, ClickStat stat) {

		Byte clickType = stat.getClickType();
		Long count = stat.getClickCount();

		if (StatClickOrSortType.CLICK_TYPE_SERVICE_TYPE.getCode().equals(clickType)) {
			dto.setServiceTypeCount(count);

		} else if (StatClickOrSortType.CLICK_TYPE_SERVICE.getCode().equals(clickType)) {
			dto.setServiceClickCount(count);

		} else if (StatClickOrSortType.CLICK_TYPE_COMMIT_BUTTON.getCode().equals(clickType)) {
			dto.setCommitClickCount(count);

		} else if (StatClickOrSortType.CLICK_TYPE_SUPPORT.getCode().equals(clickType)) {
			dto.setSupportClickCount(count);

		} else if (StatClickOrSortType.CLICK_TYPE_SHARE.getCode().equals(clickType)) {
			dto.setShareClickCount(count);

		} else if (StatClickOrSortType.CLICK_TYPE_COMMIT_TIMES.getCode().equals(clickType)) {
			dto.setCommitTimes(count.intValue());

		} else {
			LOGGER.error("no such clickType:" + clickType);
		}
	}

	private List<InterestStatDTO> listInterestStat(CrossShardListingLocator locator, Integer pageSize, Byte sortOrder,
			Byte searchType, List<ClickStatDTO> clickStats) {

		// 计算兴趣指数
		List<InterestStatDTO> interestDtos = computeInterestStat(clickStats, searchType);

		// 重新排序
		interestDtos = reSortInterest(interestDtos, sortOrder);

		// 截取所需区间
		return buildSubStatByPageAnchor(locator, pageSize, interestDtos);
	}

	private List<InterestStatDTO> computeInterestStat(List<ClickStatDTO> clickStats, Byte searchType) {

		if (TYPE_STAT_SEARCH_BY_SERVICE.equals(searchType)) {
			return computeInterestStatByService(clickStats);
		}

		return computeInterestStatByServiceType(clickStats);
	}

	private List<InterestStatDTO> computeInterestStatByServiceType(List<ClickStatDTO> clickStats) {
		// 建立服务类型id与兴趣dto的对应关系。
		Map<Long, InterestStatDTO> map = new HashMap<>();

		InterestStatDTO interestdto = null;
		for (ClickStatDTO clicks : clickStats) {

			// 如果没有服务类型id视为无效数据，直接过滤
			Long serviceTypeId = clicks.getServiceTypeId();
			if (null == serviceTypeId) {
				continue;
			}

			// 获取当前类型已储存的兴趣dto，如果没有则新增。
			interestdto = map.get(serviceTypeId);
			if (null == interestdto) {
				interestdto = new InterestStatDTO();
				interestdto.setServiceName(clicks.getServiceName());
				interestdto.setServiceTypeName(clicks.getServiceTypeName());
				interestdto.setInterestStat("0");
				map.put(serviceTypeId, interestdto);
			}

			// 对兴趣指数进行更新
			long originInterestScore = Long.parseLong(interestdto.getInterestStat());
			long currentCnt = getInterestScoreByServiceType(clicks);
			interestdto.setInterestStat((originInterestScore + currentCnt) + "");
		}

		return new ArrayList<InterestStatDTO>(map.values());
	}

	private long getInterestScoreByServiceType(ClickStatDTO cnts) {
		return getInterestScoreByClickItems(cnts, TYPE_STAT_SEARCH_BY_CATEGORY);
	}

	private long getInterestScoreByService(ClickStatDTO cnts) {
		return getInterestScoreByClickItems(cnts, TYPE_STAT_SEARCH_BY_SERVICE);
	}

	private long getInterestScoreByClickItems(ClickStatDTO cnts, Byte searchType) {

		long serviceTypeClickTimes = cnts.getServiceTypeCount();
		long commitTimes = cnts.getCommitTimes();
		long otherClickTimes = cnts.getServiceClickCount() + cnts.getCommitClickCount() + cnts.getShareClickCount()
				+ cnts.getSupportClickCount();

		if (TYPE_STAT_SEARCH_BY_SERVICE.equals(searchType)) {

			/**
			 * 某个服务兴趣指数计算方法： 1. 根据该服务在明细统计中“查看详情”、“点击申请按钮”、“咨询”、“分享”的统计数据相加：每点击1次得1分，假设点击x次
			 * 2. 该服务被用户实际提交申请的次数：每成功提交1次得20分，假设申请了y次 总分=1*x+20*y，总分最高表示用户最感兴趣，排名第1。
			 */
			return 1 * otherClickTimes + 20 * commitTimes;

		}

		/**
		 * 服务类型兴趣指数计算方法： 1. 点击某个服务类型的次数：每点击1次得10分，假设点击x次 2.
		 * 该服务类型下所有服务被点击的次数（包括查看详情/申请按钮/咨询/分享，其中咨询包括在线客服和电话咨询）：每点击1次得1分，假设点击y次 3.
		 * 该服务类型下所有服务被用户实际提交申请的次数：每成功提交1次得50分，假设申请了z次
		 * 总分=10*x+1*y+50*z，总分最高表示用户最感兴趣，排名第1。
		 */
		return 10 * serviceTypeClickTimes + 1 * otherClickTimes + 50 * commitTimes;
	}

	private List<InterestStatDTO> computeInterestStatByService(List<ClickStatDTO> clickStats) {

		// 建立服务与服务总览的对应map
		List<InterestStatDTO> interestDtos = new ArrayList<>(100);
		InterestStatDTO dto = null;
		for (ClickStatDTO clicks : clickStats) {
			// 添加返回参数
			dto = new InterestStatDTO();
			dto.setServiceName(clicks.getServiceName());
			dto.setServiceTypeName(clicks.getServiceTypeName());
			dto.setInterestStat("" + getInterestScoreByService(clicks));
			interestDtos.add(dto);
		}

		return interestDtos;
	}

	private <T> List<T> buildSubStatByPageAnchor(ListingLocator locator, Integer pageSize,
			List<T> dtos) {

		if (null == locator || null == pageSize) {
			return dtos;
		}

		int currentAnchor = locator.getAnchor() == null ? 0 : locator.getAnchor().intValue();
		int startIndex = currentAnchor * pageSize;
		if (startIndex >= dtos.size()) {
			locator.setAnchor(null);
			return null;
		}

		int endIndex = startIndex + pageSize;
		if (endIndex >= dtos.size()) {
			endIndex = dtos.size();
			locator.setAnchor(null);
		} else {
			locator.setAnchor(currentAnchor + 1L);
		}

		return dtos.subList(startIndex, endIndex);
	}

	/**
	 * 注：当前默认排序为降序
	 * 
	 * @param interestDtos
	 * @param sortOrder
	 * @return
	 */
	private List<InterestStatDTO> reSortInterest(List<InterestStatDTO> interestDtos, Byte sortOrder) {

		int sortFactor = TYPE_SORT_INTEREST_STAT_ASC.equals(sortOrder) ? -1 : 1; // 排序因子 1表示遵循默认排序 -1表示与默认排序相反
		
		return interestDtos.stream().sorted((x, y) -> {

			String xInterest = x.getInterestStat();
			String yInterest = y.getInterestStat();

			// 都为空时，认为是相等
			if (null == xInterest && null == yInterest) {
				return 0;
			}

			// 只有x是空时，x放到下面
			if (null == xInterest) {
				return 1;
			}

			// 只有y是空时，y位置不变
			if (null == yInterest) {
				return -1;
			}

			int xNum = Integer.parseInt(xInterest);
			int yNum = Integer.parseInt(yInterest);

			// 默认为降序
			return (yNum - xNum) * sortFactor;

		}).collect(Collectors.toList());
	}

	public List<ClickStatDetailDTO> listClickStatDetail(ListingLocator locator, ListClickStatDetailCommand cmd,
			Long userId) {

		// 获取startTime， endTime
		Timestamp startTime = getTrueClickStatStartTime(cmd.getStartDate());
		Timestamp endTime = getTrueClickStatEndTime(cmd.getEndDate());
		
		//设置锚点
		List<ClickStatDetail> details = clickStatDetailProvider.listStatDetails(locator, cmd.getPageSize(),
				cmd.getType(), cmd.getOwnerId(), cmd.getCategoryId(), cmd.getServiceId(), cmd.getClickType(), userId,
				cmd.getContactPhone(), startTime, endTime);
		if (CollectionUtils.isEmpty(details)) {
			return null;
		}

		// 进行返回值转化
		return details.stream().map(r -> {
			return convertClickDetailToDTO(r);
		}).collect(Collectors.toList());
	}
	
	private ClickStatDetailDTO convertClickDetailToDTO(ClickStatDetail detail) {

		ClickStatDetailDTO dto = new ClickStatDetailDTO();
		dto.setUserName(detail.getUserName());
		dto.setUserPhone(detail.getUserPhone());
		dto.setClickTime(detail.getClickTime());
		dto.setClickShowTime(DateUtil.dateToStr(new Date(detail.getClickTime()), "yyyy-MM-dd HH:mm:ss"));
		
		dto.setServiceName(getStatTool(detail.getType()).getServiceName(detail.getServiceId()));
		
		StatClickOrSortType clickType = StatClickOrSortType.fromCode(detail.getClickType());
		if (null != clickType) {
			dto.setClickTypeName(clickType.getInfo());
		}
		
		if (clickType == StatClickOrSortType.CLICK_TYPE_SERVICE_TYPE) {
			dto.setServiceTypeName(getStatTool(detail.getType()).getServiceTypeName(detail.getCategoryId()));
		} else {
			dto.setServiceTypeName(getStatTool(detail.getType()).getTypeNameByServiceId(detail.getServiceId()));
		}
		return dto;
	}
	
	private Timestamp getTrueClickStatStartTime(String startDate) {
		if (!StringUtils.isEmpty(startDate)) {
			return DateUtil.parseTimestamp(startDate);
		}
		
		return null;
	}
	
	private Timestamp getTrueClickStatEndTime(String endDate) {
		if (StringUtils.isEmpty(endDate)) {
			return null;
		} 
		
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date parsedDate;
		try {
			parsedDate = dateFormat.parse(endDate+" 23:59:59");
		} catch (ParseException e) {
			return null;
		}
		
		return new Timestamp(parsedDate.getTime());
	}

	private ClickStatTool getStatTool(Long type) {
		if (null == type) {
			YellowPageUtils.throwError(YellowPageServiceErrorCode.ERROR_ALLIANCE_GET_STAT_TOOL_TYPE_IS_NULL,
					"type is null when getting stat tool");
		}

		ClickStatTool tool = ThreadLocalClickStatTool.get();
		if (null != tool && type.equals(tool.getCurrentType())) {
			return tool;
		}

		// 删除旧数据
		ThreadLocalClickStatTool.remove();

		// 新增
		tool = new ClickStatTool();
		tool.build(type);
		ThreadLocalClickStatTool.set(tool);
		return tool;
	}
	
	/**
	 * 校验当前请求是否符合权限
	 * 
	 * @param privilegeType
	 * @param currentPMId
	 * @param appId
	 * @param checkOrgId
	 * @param checkCommunityId
	 */
	public void checkPrivilege(PrivilegeType privilegeType, Long currentPMId, Long appId, Long checkCommunityId) {
		if (configProvider.getBooleanValue("privilege.community.checkflag", true)) {
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), currentPMId,
					privilegeType.getCode(), appId, null, checkCommunityId); 
		}
	}


	private void deleteStat(LocalDate statDate) {
		Timestamp minTime = Timestamp.valueOf(statDate.atTime(LocalTime.MIN));
		Date date = new Date(minTime.getTime());

		EhAllianceStat STAT_TABLE = Tables.EH_ALLIANCE_STAT;

		DbProvider dbProvider = PlatformContext.getComponent(DbProvider.class);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		context.delete(STAT_TABLE).where(STAT_TABLE.CLICK_DATE.eq(date)).execute();
	}

	
	@Override
	public String testClickStat(TestClickStatCommand cmd) {

		DbProvider dbProvider = PlatformContext.getComponent(DbProvider.class);

		dbProvider.execute(r -> {
			detailSave(cmd.getStartDate(), cmd.getEndDate());
			return null;
		});

		return cmd.toString();
	}

	private void detailStat(List<Namespace> namespaces, LocalDate statDate) {
		deleteStat(statDate); 
        StatEventHandler handler = StatEventHandlerManager.getHandler("service_alliance_click");
		for (Namespace namespace : namespaces) {
			handler.processStat(namespace, null, statDate, null);
		}
	}
	
	private void detailSave(String startDateStr, String endDateStr) {
		LocalDate startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern("yyyyMMdd"));
		LocalDate endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern("yyyyMMdd"));

		if (startDate.isAfter(endDate)) {
			LocalDate tmp = startDate;
			startDate = endDate;
			endDate = tmp;
		}
        List<Namespace> namespaces = namespaceProvider.listNamespaces();

		do {
			doExecute(startDate);
			detailStat(namespaces, startDate);
			startDate = startDate.plusDays(1);
		} while (!startDate.isAfter(endDate));
	}
	
	private void updateEventLogContent(List<Long> toUpdateIds) {
		DbProvider dbProvider = PlatformContext.getComponent(DbProvider.class);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		com.everhomes.server.schema.tables.EhStatEventContentLogs LOGS = Tables.EH_STAT_EVENT_CONTENT_LOGS;
		context.update(LOGS).set(LOGS.STATUS, StatEventCommonStatus.WAITING_FOR_CONFIRMATION.getCode())
				.where(LOGS.ID.in(toUpdateIds)).execute();
	}

	private List<StatEventLogContent> listEventLogContent(Timestamp minTime, Timestamp maxTime) {
		DbProvider dbProvider = PlatformContext.getComponent(DbProvider.class);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		return context.selectFrom(Tables.EH_STAT_EVENT_CONTENT_LOGS)
				.where(Tables.EH_STAT_EVENT_CONTENT_LOGS.STATUS.eq(StatEventCommonStatus.ACTIVE.getCode()))
				.and(Tables.EH_STAT_EVENT_CONTENT_LOGS.CREATE_TIME.between(minTime, maxTime))
				.and(Tables.EH_STAT_EVENT_CONTENT_LOGS.CONTENT.like("%\"service_alliance_click\"%"))
				.fetchInto(StatEventLogContent.class);
	}
	
	 public void doExecute(LocalDate statDate) {

	        Timestamp minTime = Timestamp.valueOf(LocalDateTime.of(statDate, LocalTime.MIN));
	        Timestamp maxTime = Timestamp.valueOf(LocalDateTime.of(statDate, LocalTime.MAX));

	        List<StatEventLogContent> logContents = listEventLogContent(minTime, maxTime);

	        Map<String, StatEventDeviceLog> statDeviceLogMap = new HashMap<>();

	        // 有可能第一次什么也没有，客户端会传个0的sessionId
	        // StatEventDeviceLog zeroDevice = new StatEventDeviceLog();
	        // zeroDevice.setNamespaceId(Namespace.DEFAULT_NAMESPACE);
	        // zeroDevice.setUid(0L);
	        // zeroDevice.setId(0L);
	        // statDeviceLogMap.put("0", zeroDevice);

	        Map<String, StatEvent> statEventMap = new HashMap<>();
	        // Map<String, StatEventParam> statEventParamMap = new HashMap<>();
	        Map<String, List<Long>> sessionIdToDeviceGenIdMap = new HashMap<>();
			DbProvider dbProvider = PlatformContext.getComponent(DbProvider.class);
	        for (StatEventLogContent logContent : logContents) {
	            StatEventLogDTO[] logs = (StatEventLogDTO[]) StringHelper.fromJsonString(logContent.getContent(), StatEventLogDTO[].class);
	            dbProvider.execute(status -> {
	                for (StatEventLogDTO logDTO : logs) {
	                    if (logDTO == null) continue;

	                    StatEventDeviceLog deviceLog = statDeviceLogMap.get(logDTO.getSessionId());
	                    if (deviceLog == null) {
	                        deviceLog = statEventDeviceLogProvider.findStatEventDeviceLogById(Long.valueOf(logDTO.getSessionId()));
	                        // 找不到这个deviceLog就跳过
	                        if (deviceLog == null) {
	                            continue;
	                        }
	                        statDeviceLogMap.put(String.valueOf(deviceLog.getId()), deviceLog);
	                    }
	                    StatEvent statEvent = statEventMap.get(logDTO.getEventName());
	                    if (statEvent == null) {
	                        statEvent = statEventProvider.findStatEventByName(logDTO.getEventName());
	                        statEventMap.put(statEvent.getEventName(), statEvent);
	                    }

	                    // 根据deviceGenId去重
	                    List<Long> deviceGenIdList = sessionIdToDeviceGenIdMap.get(logDTO.getSessionId());
	                    if (deviceGenIdList == null) {
	                        deviceGenIdList = statEventLogProvider.listDeviceGenIdBySessionId(logDTO.getSessionId());
	                        deviceGenIdList.add(logDTO.getLogId());
	                        sessionIdToDeviceGenIdMap.put(logDTO.getSessionId(), deviceGenIdList);
	                    } else {
	                        if (deviceGenIdList.contains(logDTO.getLogId())) {
	                            continue;
	                        }
	                        deviceGenIdList.add(logDTO.getLogId());
	                    }

	                    StatEventLog log = new StatEventLog();
	                    log.setNamespaceId(logContent.getNamespaceId());
	                    log.setUid(deviceLog.getUid());
	                    log.setDeviceGenId(logDTO.getLogId());
	                    log.setAcc(logDTO.getAcc());
	                    log.setDeviceGenId(logDTO.getLogId());
	                    log.setEventName(logDTO.getEventName());
	                    log.setEventVersion(logDTO.getVersion());
	                    log.setEventType(statEvent.getEventType());
	                    log.setSessionId(logDTO.getSessionId());
	                    log.setStatus(StatEventCommonStatus.ACTIVE.getCode());
	                    log.setDeviceTime(logDTO.getDeviceTime());
	                    log.setAcc(1);
	                    log.setUploadTime(logContent.getCreateTime());
	                    statEventLogProvider.createStatEventLog(log);

	                    Map<String, String> param = logDTO.getParam();
	                    if (param == null) {
	                        continue;
	                    }

	                    StatEventHandler handler = StatEventHandlerManager.getHandler(log.getEventName());
	                    List<StatEventParamLog> paramLogs = handler.processEventParamLogs(log, param);
	                    statEventParamLogProvider.createStatEventParamLogs(paramLogs);

	                    /*for (Map.Entry<String, String> entry : param.entrySet()) {
	                        StatEventParam statEventParam = statEventParamMap.get(log.getEventName() + entry.getKey());
	                        if (statEventParam == null) {
	                            statEventParam = statEventParamProvider.findStatEventParam(log.getEventName(), entry.getKey());
	                            statEventParamMap.put(log.getEventName() + entry.getKey(), statEventParam);
	                        }
	                        if (statEventParam != null) {
	                            StatEventParamLog paramLog = new StatEventParamLog();
	                            paramLog.setStatus(StatEventCommonStatus.ACTIVE.getCode());
	                            paramLog.setSessionId(log.getSessionId());
	                            paramLog.setNamespaceId(log.getNamespaceId());
	                            paramLog.setEventType(log.getEventType());
	                            paramLog.setEventName(log.getEventName());
	                            paramLog.setUid(log.getUid());
	                            paramLog.setEventLogId(log.getId());
	                            paramLog.setParamKey(entry.getKey());
	                            paramLog.setEventVersion(log.getEventVersion());
	                            paramLog.setUploadTime(logContent.getCreateTime());
	                            if (statEventParam.getParamType() == StatEventParamType.NUMBER.getCode()) {
	                                paramLog.setNumberValue(Integer.valueOf(entry.getValue()));
	                            } else {
	                                paramLog.setStringValue(entry.getValue());
	                            }
	                            statEventParamLogProvider.createStatEventParamLog(paramLog);
	                        }
	                    }*/
	                }
	                return true;
	            });
	            // 把这条记录状态置为已load
	            logContent.setStatus(StatEventCommonStatus.WAITING_FOR_CONFIRMATION.getCode());
	            statEventContentLogProvider.updateStatEventLogContent(logContent);
	        }
	    }
	
}
