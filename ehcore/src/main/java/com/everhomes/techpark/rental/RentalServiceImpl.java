package com.everhomes.techpark.rental;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;

import net.greghaines.jesque.Job;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.queue.taskqueue.JesqueClientFactory;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.techpark.onlinePay.OnlinePayService;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Component
public class RentalServiceImpl implements RentalService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RentalServiceImpl.class);
	SimpleDateFormat timeSF = new SimpleDateFormat("HH:mm:ss");
	SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String queueName = "rentalService";
	

    @Autowired
    WorkerPoolFactory workerPoolFactory;
    
    @PostConstruct
    public void setup() {
        workerPoolFactory.getWorkerPool().addQueue(queueName);
    }
    
	// N分钟后取消
	private Long cancelTime = 5 * 60 * 1000L;
	@Autowired
	private OnlinePayService onlinePayService;
	@Autowired
	private OrganizationProvider organizationProvider;
	@Autowired
	JesqueClientFactory jesqueClientFactory;
	@Autowired
	private CoordinationProvider coordinationProvider;
	@Autowired
	private LocaleStringService localeStringService;
	@Autowired
	private ConfigurationProvider configurationProvider;
	@Autowired
	RentalProvider rentalProvider;

	private int getPageCount(int totalCount, int pageSize) {
		int pageCount = totalCount / pageSize;

		if (totalCount % pageSize != 0) {
			pageCount++;
		}
		return pageCount;
	}

	private void checkEnterpriseCommunityIdIsNull(Long enterpriseCommunityId) {
		if (null == enterpriseCommunityId || enterpriseCommunityId.equals(0L)) {
			LOGGER.error("Invalid enterpriseCommunityId   parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid enterpriseCommunityId   parameter in the command");
		}

	}

	@Override
	public UpdateRentalRuleCommandResponse updateRentalRule(
			UpdateRentalRuleCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		checkEnterpriseCommunityIdIsNull(cmd.getOwnerId());
//		RentalRule rentalRule = rentalProvider.getRentalRule(
//				cmd.getOwnerId(),cmd.getOwnerType(), cmd.getSiteType());
//		rentalRule.setOwnerId(cmd.getOwnerId());
//		rentalRule.setOwnerType(cmd.getOwnerType());
//		rentalRule.setContactNum(cmd.getContactNum());
//		rentalRule.setPayEndTime(cmd.getPayEndTime());
//		rentalRule.setPaymentRatio(cmd.getPayRatio());
//		rentalRule.setPayStartTime(cmd.getPayStartTime());
//		rentalRule.setRefundFlag(cmd.getRefundFlag());
//		rentalRule.setRentalType(cmd.getRentalType());
//		rentalRule.setOvertimeTime(cmd.getOvertimeTime());
//		rentalRule.setSiteType(cmd.getSiteType());
//		rentalRule.setRentalEndTime(cmd.getRentalEndTime());
//		rentalRule.setRentalStartTime(cmd.getRentalStartTime());
//		rentalRule.setOperateTime(new Timestamp(DateHelper.currentGMTTime()
//				.getTime()));
//		rentalRule.setContactAddress(cmd.getContactAddress());
//		rentalRule.setContactName(cmd.getContactName());
//		rentalRule.setOperatorUid(userId);
		RentalRule rentalRule =ConvertHelper.convert(cmd,RentalRule.class  );
		rentalRule.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		rentalRule.setOperatorUid(userId);
		rentalProvider.updateRentalRule(rentalRule);
		return null;
	}

	@Override
	public Long addRentalSite(AddRentalSiteCommand cmd) {
		RentalSite rentalsite = ConvertHelper.convert(cmd, RentalSite.class);
		rentalsite.setStatus(RentalSiteStatus.NORMAL.getCode());
		rentalsite.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
				.getTime()));
		rentalsite.setCreatorUid( UserContext.current().getUser().getId());
		Long siteId = rentalProvider.createRentalSite(rentalsite);
		if (null != cmd.getSiteItems()
				&& !StringUtils.isEmpty(cmd.getSiteItems())) {
			JSONObject jsonObject = (JSONObject) JSONValue.parse(cmd
					.getSiteItems());
			JSONArray itemValue = (JSONArray) jsonObject.get("siteItems");
			Gson gson = new Gson();
			List<SiteItemDTO> siteItemDTOs = gson.fromJson(
					itemValue.toString(), new TypeToken<List<SiteItemDTO>>() {
					}.getType());
			for (SiteItemDTO siteItemDTO : siteItemDTOs) {
				RentalSiteItem siteItem = new RentalSiteItem();
				siteItem.setRentalSiteId(siteId);
				siteItem.setCounts(siteItemDTO.getCounts());
				siteItem.setName(siteItemDTO.getItemName());
				siteItem.setPrice(siteItemDTO.getItemPrice());
				rentalProvider.createRentalSiteItem(siteItem);
			}
		}
		return siteId;
	}

	@Override
	public void addRentalSiteItems(AddRentalSiteItemsCommand cmd) {
		RentalSiteItem siteItem = new RentalSiteItem();
		siteItem.setRentalSiteId(cmd.getRentalSiteId());
		siteItem.setCounts(cmd.getCounts());
		siteItem.setName(cmd.getItemName());
		siteItem.setPrice(cmd.getItemPrice());
		rentalProvider.createRentalSiteItem(siteItem);
	}

	/**
	 * @param cmd
	 */
	@Override
	public void addRentalSiteRules(AddRentalSiteRulesCommand cmd) {
		RentalSiteRule rsr = new RentalSiteRule();
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.setTime(new Date(cmd.getBeginDate()));
		end.setTime(new Date(cmd.getEndDate()));
		JSONObject jsonObject = (JSONObject) JSONValue.parse(cmd.getChoosen());
		JSONArray choosenValue = (JSONArray) jsonObject.get("choosen");
		Gson gson = new Gson();
		List<Integer> choosenInts = gson.fromJson(choosenValue.toString(),
				new TypeToken<List<Integer>>() {
				}.getType());
		// String[] arr = cmd.getChoosen().split(",");
		// List<String> list = new ArrayList<String>(arr);
		while (start.before(end)) {
			Integer weekday = start.get(Calendar.DAY_OF_WEEK);
			if (choosenInts.contains(weekday)) {
				for (double i = cmd.getBeginTime(); i < cmd.getEndTime();) {
					rsr.setBeginTime(Timestamp.valueOf(dateSF.format(start
							.getTime())
							+ " "
							+ String.valueOf((int) i / 1)
							+ ":"
							+ String.valueOf((int) ((i % 1) * 60))
							+ ":00"));

					i = i + cmd.getTimeStep();
					rsr.setEndTime(Timestamp.valueOf(dateSF.format(start
							.getTime())
							+ " "
							+ String.valueOf((int) i / 1)
							+ ":"
							+ String.valueOf((int) ((i % 1) * 60))
							+ ":00"));
					rsr.setRentalSiteId(cmd.getRentalSiteId());
					rsr.setRentalType(cmd.getRentalType());
					rsr.setCounts(cmd.getCounts());
					rsr.setUnit(cmd.getUnit());
					rsr.setPrice(cmd.getPrice());
					rsr.setSiteRentalDate(Date.valueOf(dateSF.format(start
							.getTime())));
					rsr.setStatus(cmd.getStatus());
					rentalProvider.createRentalSiteRule(rsr);

				}
			}
			start.add(Calendar.DAY_OF_MONTH, 1);
		}
	}

	@Override
	public FindRentalSitesStatusCommandResponse findRentalSiteDayStatus(
			FindRentalSitesStatusCommand cmd) {

		if(null!=cmd.getCommunityId()){
			cmd.setOwnerId(cmd.getCommunityId());
			cmd.setOwnerType(RentalOwnerType.COMMUNITY.getCode());
		}
		java.util.Date reserveTime = new java.util.Date();
		FindRentalSitesStatusCommandResponse response = new FindRentalSitesStatusCommandResponse();
		response.setSites(new ArrayList<RentalSiteDTO>());
		RentalRule rentalRule = rentalProvider.getRentalRule(
				cmd.getOwnerId(),cmd.getOwnerType(), cmd.getSiteType());
		response.setContactNum(rentalRule.getContactNum());
		// 查sites
		List<RentalSite> rentalSites = rentalProvider.findRentalSites(
				cmd.getOwnerId(),cmd.getOwnerType(), cmd.getSiteType(), null, null, null);
		for (RentalSite rs : rentalSites) {
			RentalSiteDTO rsDTO = new RentalSiteDTO();
			rsDTO.setBuildingName(rs.getBuildingName());
			rsDTO.setSiteName(rs.getSiteName());
			rsDTO.setContactName(rs.getContactName());
			rsDTO.setCompanyName(rs.getOwnCompanyName());
			rsDTO.setSpec(rs.getSpec());
			rsDTO.setAddress(rs.getAddress());
			rsDTO.setContactPhonenum(rs.getContactPhonenum());
			rsDTO.setOwnerId(cmd.getOwnerId());
			rsDTO.setOwnerType(cmd.getOwnerType());
			rsDTO.setRentalSiteId(rs.getId());
			rsDTO.setSiteRules(new ArrayList<RentalSiteRulesDTO>());
			rsDTO.setSiteItems(new ArrayList<SiteItemDTO>());
			// 查rules

			java.util.Date nowTime = new java.util.Date();

			Timestamp beginTime = new Timestamp(nowTime.getTime()
					+ rentalRule.getRentalStartTime());
			List<RentalSiteRule> rentalSiteRules = rentalProvider
					.findRentalSiteRules(rsDTO.getRentalSiteId(), dateSF
							.format(new java.util.Date(cmd.getRuleDate())),
							beginTime, cmd.getRentalType() ,
							cmd.getRentalType().equals(RentalType.DAY)?DateLength.DAY.getCode():DateLength.MONTH.getCode());
			// 查sitebills
			if (null != rentalSiteRules && rentalSiteRules.size() > 0) {
				for (RentalSiteRule rsr : rentalSiteRules) {
					RentalSiteRulesDTO dto = new RentalSiteRulesDTO();
					dto.setId(rsr.getId());
					dto.setRentalSiteId(rsr.getRentalSiteId());
					dto.setRentalType(rsr.getRentalType());
					dto.setRentalStep(rsr.getRentalStep());
					if (dto.getRentalType().equals(RentalType.HOUR.getCode())) {
						dto.setTimeStep(rsr.getTimeStep());
						dto.setBeginTime(rsr.getBeginTime().getTime());
						dto.setEndTime(rsr.getEndTime().getTime());
					} else if (dto.getRentalType().equals(
							RentalType.HALFDAY.getCode())) {
						dto.setAmorpm(rsr.getAmorpm());
					}
					dto.setUnit(rsr.getUnit());
					dto.setPrice(rsr.getPrice());
					dto.setRuleDate(rsr.getSiteRentalDate().getTime());
					List<RentalSitesBill> rsbs = rentalProvider
							.findRentalSiteBillBySiteRuleId(rsr.getId());
					dto.setStatus(SiteRuleStatus.OPEN.getCode());
					dto.setCounts((double) rsr.getCounts());
					if (null != rsbs && rsbs.size() > 0) {
						for (RentalSitesBill rsb : rsbs) {
							dto.setCounts(dto.getCounts()
									- rsb.getRentalCount());
						}
					}
					if (dto.getCounts() == 0) {
						dto.setStatus(SiteRuleStatus.CLOSE.getCode());
					}
					if (dto.getRentalType().equals(RentalType.HOUR.getCode())) {
						if (reserveTime.before(new java.util.Date(rsr
								.getBeginTime().getTime()
								- rentalRule.getRentalStartTime()))) {
							dto.setStatus(SiteRuleStatus.EARLY.getCode());
						}
						if (reserveTime.after(new java.util.Date(rsr
								.getBeginTime().getTime()
								- rentalRule.getRentalEndTime()))) {
							dto.setStatus(SiteRuleStatus.LATE.getCode());
						}
					} else {
						if (reserveTime.before(new java.util.Date(rsr
								.getSiteRentalDate().getTime()
								- rentalRule.getRentalStartTime()))) {
							dto.setStatus(SiteRuleStatus.EARLY.getCode());
						}
						if (reserveTime.after(new java.util.Date(rsr
								.getSiteRentalDate().getTime()
								- rentalRule.getRentalEndTime()))) {
							dto.setStatus(SiteRuleStatus.LATE.getCode());
						}
					}
					rsDTO.getSiteRules().add(dto);

				}
			}
			response.getSites().add(rsDTO);
		}

		return response;
	}

	@Override
	public GetRentalSiteTypeResponse findRentalSiteTypes() {

		GetRentalSiteTypeResponse response = new GetRentalSiteTypeResponse();
		response.setSiteTypes(rentalProvider.findRentalSiteTypes());
		return response;
	}

	@Override
	public FindRentalSiteItemsCommandResponse findRentalSiteItems(
			FindRentalSiteItemsCommand cmd) {

		if(null!=cmd.getCommunityId()){
			cmd.setOwnerId(cmd.getCommunityId());
			cmd.setOwnerType(RentalOwnerType.COMMUNITY.getCode());
		}

		FindRentalSiteItemsCommandResponse response = new FindRentalSiteItemsCommandResponse();
		response.setSiteItems(new ArrayList<SiteItemDTO>());
		List<RentalSiteItem> rsiSiteItems = rentalProvider
				.findRentalSiteItems(cmd.getRentalSiteId());
		for (RentalSiteItem rsi : rsiSiteItems) {
			int maxOrder = 0;
			for (Long siteRuleId : cmd.getRentalSiteRuleIds()) {
				// 对于每一个物品，通过每一个siteRuleID找到它对应的BillIds
				int ruleOrderSum = 0;
				List<RentalSitesBill> rsbs = rentalProvider
						.findRentalSiteBillBySiteRuleId(siteRuleId);
				// 通过每一个billID找已预订的数量
				if (null == rsbs || rsbs.size() == 0) {
					continue;
				}
				for (RentalSitesBill rsb : rsbs) {
					RentalItemsBill rib = rentalProvider.findRentalItemBill(
							rsb.getRentalBillId(), rsi.getId());
					if (null == rib || null == rib.getRentalCount())
						continue;
					ruleOrderSum += rib.getRentalCount();
				}
				// 获取该物品的最大预订量
				if (ruleOrderSum > maxOrder)
					maxOrder = ruleOrderSum;
			}
			rsi.setCounts(rsi.getCounts() - maxOrder);
			SiteItemDTO dto = new SiteItemDTO();
			dto.setCounts(rsi.getCounts());
			dto.setId(rsi.getId());
			dto.setItemName(rsi.getName());
			dto.setItemPrice(rsi.getPrice());
			response.getSiteItems().add(dto);
		}
		return response;
	}

	@Override
	public FindRentalSitesCommandResponse findRentalSites(
			FindRentalSitesCommand cmd) {
		FindRentalSitesCommandResponse response = new FindRentalSitesCommandResponse();
		cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
		int totalCount = rentalProvider.countRentalSites(cmd.getOwnerId(),cmd.getOwnerType(),
				cmd.getSiteType(), cmd.getKeyword());
		if (totalCount == 0)
			return response;

		Integer pageSize = PaginationConfigHelper.getPageSize(
				configurationProvider, cmd.getPageSize());
		int pageCount = getPageCount(totalCount, pageSize);

		checkEnterpriseCommunityIdIsNull(cmd.getOwnerId());
		List<RentalSite> rentalSites = rentalProvider.findRentalSites(
				cmd.getOwnerId(),cmd.getOwnerType(), cmd.getSiteType(), cmd.getKeyword(),
				cmd.getPageOffset(), pageSize);

		response.setRentalSites(new ArrayList<RentalSiteDTO>());
		for (RentalSite rentalSite : rentalSites) {
			RentalSiteDTO rSiteDTO =ConvertHelper.convert(rentalSite, RentalSiteDTO.class);
			rSiteDTO.setRentalSiteId(rentalSite.getId());
			rSiteDTO.setCreateTime(rentalSite.getCreateTime().getTime());
			rSiteDTO.setSiteItems(new ArrayList<SiteItemDTO>());
			List<RentalSiteItem> items = rentalProvider
					.findRentalSiteItems(rentalSite.getId());
			for (RentalSiteItem item : items) {
				SiteItemDTO siteItemDTO = new SiteItemDTO();
				siteItemDTO.setCounts(item.getCounts());
				siteItemDTO.setItemName(item.getName());
				siteItemDTO.setItemPrice(item.getPrice());
				rSiteDTO.getSiteItems().add(siteItemDTO);
			}
			response.getRentalSites().add(rSiteDTO);
		}
		response.setNextPageOffset(cmd.getPageOffset() == pageCount ? null
				: cmd.getPageOffset() + 1);

		return response;
	}

	@Override
	public FindRentalSiteRulesCommandResponse findRentalSiteRules(
			FindRentalSiteRulesCommand cmd) {
		FindRentalSiteRulesCommandResponse response = new FindRentalSiteRulesCommandResponse();
		response.setRentalSiteRules(new ArrayList<RentalSiteRulesDTO>());

		List<RentalSiteRule> rentalSiteRules = rentalProvider
				.findRentalSiteRules(cmd.getRentalSiteId(), null, null,
						cmd.getRentalType() ,
						cmd.getRentalType().equals(RentalType.DAY)?DateLength.DAY.getCode():DateLength.MONTH.getCode());
		// 查sitebills
		if (null != rentalSiteRules && rentalSiteRules.size() > 0) {
			for (RentalSiteRule rsr : rentalSiteRules) {
				RentalSiteRulesDTO dto = new RentalSiteRulesDTO();
				dto.setId(rsr.getId());
				dto.setRentalSiteId(rsr.getRentalSiteId());
				dto.setRentalType(rsr.getRentalType());
				dto.setRentalStep(rsr.getRentalStep()); 
				if (dto.getRentalType().equals(RentalType.HOUR.getCode())) {
					dto.setTimeStep(rsr.getTimeStep());
					dto.setBeginTime(rsr.getBeginTime().getTime());
					dto.setEndTime(rsr.getEndTime().getTime());
				} else if (dto.getRentalType().equals(
						RentalType.HALFDAY.getCode())) {
					dto.setAmorpm(rsr.getAmorpm());
				}
				dto.setUnit(rsr.getUnit());
				dto.setPrice(rsr.getPrice());
				dto.setRuleDate(rsr.getSiteRentalDate().getTime());
				List<RentalSitesBill> rsbs = rentalProvider
						.findRentalSiteBillBySiteRuleId(rsr.getId());
				dto.setStatus(SiteRuleStatus.OPEN.getCode());
				dto.setCounts((double) rsr.getCounts());
				if (null != rsbs && rsbs.size() > 0) {
					for (RentalSitesBill rsb : rsbs) {
						dto.setCounts(dto.getCounts() - rsb.getRentalCount());
					}
				}
				if (dto.getCounts() == 0) {
					dto.setStatus(SiteRuleStatus.CLOSE.getCode());
				}

				response.getRentalSiteRules().add(dto);

			}
		}

		return response;
	}

	@Override
	public RentalBillDTO addRentalBill(AddRentalBillCommand cmd) {
		if(null!=cmd.getCommunityId()){
			cmd.setOwnerId(cmd.getCommunityId());
			cmd.setOwnerType(RentalOwnerType.COMMUNITY.getCode());
		}
		
		Long userId = UserContext.current().getUser().getId();
		int count = this.rentalProvider.countRentalBills(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getSiteType(), null, SiteBillStatus.SUCCESS.getCode(), cmd.getStartTime(), cmd.getEndTime(), null,userId);
		if(count > 0 ){
			throw RuntimeErrorException
			.errorWith(
					RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_ORDER_DUPLICATE,
					localeStringService.getLocalizedString(
							String.valueOf(RentalServiceErrorCode.SCOPE),
							String.valueOf(RentalServiceErrorCode.ERROR_ORDER_DUPLICATE),
							UserContext.current().getUser().getLocale(),
							"ORDER DUPLICATE IN THIS TIME "));
		}
		RentalBillDTO response = new RentalBillDTO();
		java.util.Date reserveTime = new java.util.Date();
		List<RentalSiteRule> rentalSiteRules = new ArrayList<RentalSiteRule>();
		RentalRule rentalRule = rentalProvider.getRentalRule(
				cmd.getOwnerId(),cmd.getOwnerType(), cmd.getSiteType());
		if (reserveTime.before(new java.util.Date(cmd.getStartTime()
				- rentalRule.getRentalStartTime()))) {
			LOGGER.error("reserve Time before reserve start time");
			throw RuntimeErrorException
					.errorWith(
							RentalServiceErrorCode.SCOPE,
							RentalServiceErrorCode.ERROR_RESERVE_TOO_EARLY,
							localeStringService.getLocalizedString(
									String.valueOf(RentalServiceErrorCode.SCOPE),
									String.valueOf(RentalServiceErrorCode.ERROR_RESERVE_TOO_EARLY),
									UserContext.current().getUser().getLocale(),
									"reserve Time before reserve start time"));
		}
		if (reserveTime.after(new java.util.Date(cmd.getStartTime()
				- rentalRule.getRentalEndTime()))) {
			LOGGER.error("reserve Time after reserve end time");
			throw RuntimeErrorException
					.errorWith(
							RentalServiceErrorCode.SCOPE,
							RentalServiceErrorCode.ERROR_RESERVE_TOO_LATE,
							localeStringService.getLocalizedString(
									String.valueOf(RentalServiceErrorCode.SCOPE),
									String.valueOf(RentalServiceErrorCode.ERROR_RESERVE_TOO_LATE),
									UserContext.current().getUser().getLocale(),
									"reserve Time after reserve end time"));
		}
		RentalBill rentalBill = new RentalBill();
		rentalBill.setOwnerId(cmd.getOwnerId());
		rentalBill.setOwnerType(cmd.getOwnerType());
		rentalBill.setSiteType(cmd.getSiteType());
		rentalBill.setRentalSiteId(cmd.getRentalSiteId());
		rentalBill.setRentalUid(userId);
		rentalBill.setInvoiceFlag(InvoiceFlag.NONEED.getCode());
		rentalBill.setRentalDate(new Date(cmd.getRentalDate()));
		this.valiRentalBill(cmd.getRentalCount(), cmd.getRentalSiteRuleIds());
		rentalBill.setRentalCount(cmd.getRentalCount());
		Double siteTotalMoney = 0.0;
		for (Long siteRuleId : cmd.getRentalSiteRuleIds()) {
			if (null == siteRuleId)
				continue;
			RentalSiteRule rentalSiteRule = rentalProvider
					.findRentalSiteRuleById(siteRuleId);
			rentalSiteRules.add(rentalSiteRule);
			if (null == rentalSiteRule.getBeginTime()) {
				if (null == rentalBill.getStartTime()
						|| rentalBill.getStartTime().after(
								rentalSiteRule.getSiteRentalDate()))
					rentalBill.setStartTime(new Timestamp(rentalSiteRule
							.getSiteRentalDate().getTime()));
			} else {
				if (null == rentalBill.getStartTime()
						|| rentalBill.getStartTime().after(
								rentalSiteRule.getBeginTime())) {

					rentalBill.setStartTime(rentalSiteRule.getBeginTime());
				}
			}

			if (null == rentalSiteRule.getEndTime()) {
				if (null == rentalBill.getEndTime()
						|| rentalBill.getEndTime().before(
								rentalSiteRule.getSiteRentalDate()))
					rentalBill.setEndTime(new Timestamp(rentalSiteRule
							.getSiteRentalDate().getTime()));
			} else {
				if (null == rentalBill.getEndTime()
						|| rentalBill.getEndTime().before(
								rentalSiteRule.getEndTime())) {

					rentalBill.setEndTime(rentalSiteRule.getEndTime());
				}
			}
			siteTotalMoney += (null == rentalSiteRule.getPrice()?0:rentalSiteRule.getPrice())
					* (cmd.getRentalCount() / rentalSiteRule.getUnit());
		}

		// for (SiteItemDTO siDto : cmd.getRentalItems()) {
		// totalMoney += siDto.getItemPrice() * siDto.getCounts();
		// }
		rentalBill.setSiteTotalMoney(siteTotalMoney);
		rentalBill.setPayTotalMoney(siteTotalMoney);
		rentalBill.setReserveMoney(siteTotalMoney
				* (rentalRule.getPaymentRatio()==null?0:rentalRule.getPaymentRatio())/ 100);
		rentalBill.setReserveTime(Timestamp.valueOf(datetimeSF
				.format(reserveTime)));
		if(rentalRule.getPayStartTime()!=null){
			rentalBill.setPayStartTime(new Timestamp(cmd.getStartTime()
					- rentalRule.getPayStartTime()));
		}
		if(rentalRule.getPayEndTime()!=null){
			rentalBill.setPayEndTime(new Timestamp(cmd.getStartTime()
					- rentalRule.getPayEndTime()));
		}
		rentalBill.setPaidMoney(0.0);
		//
		
		if (rentalRule.getPaymentRatio()!=null&&(rentalRule.getPaymentRatio()==null?0:rentalRule.getPaymentRatio()) <100 && reserveTime.before(new java.util.Date(cmd.getStartTime()
				- rentalRule.getPayStartTime()))) {
			//定金比例在100以内 在支付时间之前 为锁定待支付
			rentalBill.setStatus(SiteBillStatus.LOCKED.getCode());

		} else {
			// 在支付时间之后 为待支付全款
			rentalBill.setStatus(SiteBillStatus.PAYINGFINAL.getCode());
		}

		rentalBill.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
				.getTime()));
		rentalBill.setCreatorUid(userId);
		rentalBill.setVisibleFlag(VisibleFlag.VISIBLE.getCode());

		// Long rentalBillId = this.rentalProvider.createRentalBill(rentalBill);

		Tuple<Long, Boolean> tuple = (Tuple<Long, Boolean>) this.coordinationProvider
				.getNamedLock(CoordinationLocks.CREATE_RENTAL_BILL.getCode())
				.enter(() -> {
					// this.groupProvider.updateGroup(group);
					this.valiRentalBill(cmd.getRentalCount(),
							cmd.getRentalSiteRuleIds());
					return this.rentalProvider.createRentalBill(rentalBill);
				});
		Long rentalBillId = tuple.first();
		if (rentalBill.getStatus().equals(SiteBillStatus.LOCKED.getCode())) {
			// 20分钟后，取消状态为锁定的订单
			final Job job1 = new Job(
					CancelLockedRentalBillAction.class.getName(),
					new Object[] { String.valueOf(rentalBillId) });
			jesqueClientFactory.getClientPool().delayedEnqueue(queueName, job1,
					System.currentTimeMillis() + cancelTime);
			// 在支付时间开始时，把订单状态更新为待支付全款
			final Job job2 = new Job(
					UpdateRentalBillStatusToPayingFinalAction.class.getName(),
					new Object[] { String.valueOf(rentalBill.getId()) });
			// 20min cancel order if status still is locked or paying
			jesqueClientFactory.getClientPool().delayedEnqueue(queueName, job2,
					cmd.getStartTime() - rentalRule.getPayStartTime());
			

			// 在支付时间截止时，取消未成功的订单
			final Job job3 = new Job(
					CancelUnsuccessRentalBillAction.class.getName(),
					new Object[] { String.valueOf(rentalBill.getId()) });
			// 20min cancel order if status still is locked or paying
			jesqueClientFactory.getClientPool().delayedEnqueue(queueName, job3,
					cmd.getStartTime() - rentalRule.getPayEndTime());
			
			
		} else if (rentalBill.getStatus().equals(
				SiteBillStatus.PAYINGFINAL.getCode())) {
			// 20分钟后，取消未成功的订单
			final Job job1 = new Job(
					CancelUnsuccessRentalBillAction.class.getName(),
					new Object[] { String.valueOf(rentalBill.getId()) });

			jesqueClientFactory.getClientPool().delayedEnqueue(queueName, job1,
					System.currentTimeMillis() + cancelTime);

		}
		
		
		if(null!=rentalBill.getEndTime()&&null!=rentalRule.getOvertimeTime()){
			//超期未确认的置为超时
			final Job job1 = new Job(
					IncompleteUnsuccessRentalBillAction.class.getName(),
					new Object[] { String.valueOf(rentalBill.getId()) });

			jesqueClientFactory.getClientPool().delayedEnqueue(queueName, job1,
					rentalBill.getEndTime().getTime() + rentalRule.getOvertimeTime());
		}
		// 循环存site订单
		for (RentalSiteRule rsr : rentalSiteRules) {
			RentalSitesBill rsb = new RentalSitesBill();
			rsb.setOwnerId(cmd.getOwnerId());
			rsb.setOwnerType(cmd.getOwnerType());
			rsb.setSiteType(cmd.getSiteType());
			rsb.setRentalBillId(rentalBillId);
			rsb.setTotalMoney( (null ==rsr.getPrice()?0:rsr.getPrice()) 
					* (int) (cmd.getRentalCount() / rsr.getUnit()));
			rsb.setRentalCount(cmd.getRentalCount());
			rsb.setRentalSiteRuleId(rsr.getId());
			rsb.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			rsb.setCreatorUid(userId);

			rentalProvider.createRentalSiteBill(rsb);
		}
		mappingRentalBillDTO(response, rentalBill);
		return response;
	}

	private void valiRentalBill(Double rentalcount, List<Long> rentalSiteRuleIds) {
		// 如果有一个规则，剩余的数量少于预定的数量
		for (Long siteRuleId : rentalSiteRuleIds) {
			if (siteRuleId == null)
				continue;
			Double totalCount = Double.valueOf(this.rentalProvider
					.findRentalSiteRuleById(siteRuleId).getCounts());
			Double rentaledCount = this.rentalProvider
					.sumRentalRuleBillSumCounts(siteRuleId);
			if (null == rentaledCount)
				rentaledCount = 0.0;
			if ((totalCount - rentaledCount) < rentalcount) {
				throw RuntimeErrorException
						.errorWith(
								RentalServiceErrorCode.SCOPE,
								RentalServiceErrorCode.ERROR_NO_ENOUGH_SITES,
								localeStringService.getLocalizedString(
										String.valueOf(RentalServiceErrorCode.SCOPE),
										String.valueOf(RentalServiceErrorCode.ERROR_NO_ENOUGH_SITES),
										UserContext.current().getUser()
												.getLocale(),
										" has no enough sites to rental "));
			}
		}

	}

	@Override
	public FindRentalBillsCommandResponse findRentalBills(
			FindRentalBillsCommand cmd) {
		if(null!=cmd.getCommunityId()){
			cmd.setOwnerId(cmd.getCommunityId());
			cmd.setOwnerType(RentalOwnerType.COMMUNITY.getCode());
		}
		
		Long userId = UserContext.current().getUser().getId();
		if (cmd.getPageAnchor() == null)
			cmd.setPageAnchor(9223372036854775807L);
		int pageSize = PaginationConfigHelper.getPageSize(
				configurationProvider, cmd.getPageSize());

		ListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<RentalBill> billList = this.rentalProvider.listRentalBills(userId,
				cmd.getOwnerId(),cmd.getOwnerType(), cmd.getSiteType(), locator, pageSize + 1,
				cmd.getBillStatus());
		FindRentalBillsCommandResponse response = new FindRentalBillsCommandResponse();
		response.setRentalBills(new ArrayList<RentalBillDTO>());
		for (RentalBill bill : billList) {

			RentalBillDTO dto = new RentalBillDTO();
			mappingRentalBillDTO(dto, bill);

			dto.setSiteItems(new ArrayList<SiteItemDTO>());
			List<RentalItemsBill> rentalSiteItems = rentalProvider
					.findRentalItemsBillBySiteBillId(dto.getRentalBillId());
			for (RentalItemsBill rib : rentalSiteItems) {
				SiteItemDTO siDTO = new SiteItemDTO();
				siDTO.setCounts(rib.getRentalCount());
				RentalSiteItem rsItem = rentalProvider
						.findRentalSiteItemById(rib.getRentalSiteItemId());
				siDTO.setItemName(rsItem.getName());
				siDTO.setItemPrice(rib.getTotalMoney());
				dto.getSiteItems().add(siDTO);
			}
			response.getRentalBills().add(dto);
		}
		return response;
	}

	void mappingRentalBillDTO(RentalBillDTO dto, RentalBill bill) {
		RentalSite rs = rentalProvider
				.getRentalSiteById(bill.getRentalSiteId());
		dto.setSiteName(rs.getSiteName());
		dto.setBuildingName(rs.getBuildingName());
		dto.setAddress(rs.getAddress());
		dto.setSpec(rs.getSpec());
		dto.setCompanyName(rs.getOwnCompanyName());
		dto.setContactName(rs.getContactName());
		if(rs.getOwnerType().equals(RentalOwnerType.COMMUNITY.getCode())){
			dto.setCommunityId(rs.getOwnerId());
		}
		dto.setContactPhonenum(rs.getContactPhonenum());
		dto.setNotice(rs.getNotice());
		dto.setIntroduction(rs.getIntroduction());
		dto.setRentalBillId(bill.getId());
		dto.setOwnerId(bill.getOwnerId());
		dto.setOwnerType(bill.getOwnerType());
		dto.setSiteType(bill.getSiteType());
		dto.setRentalCount(bill.getRentalCount());
		if (null == bill.getStartTime()) {

		} else {
			dto.setStartTime(bill.getStartTime().getTime());
		}
		if (null == bill.getEndTime()) {

		} else {
			dto.setEndTime(bill.getEndTime().getTime());
		}
		dto.setReserveTime(bill.getReserveTime().getTime());
		if (null != bill.getPayStartTime()) {
			dto.setPayStartTime(bill.getPayStartTime().getTime());
		}
		if (null != bill.getPayTime()) {
			dto.setPayTime(bill.getPayTime().getTime());
		}
		if (null != bill.getPayEndTime()) {
			dto.setPayDeadLineTime(bill.getPayEndTime().getTime());
		}
		if (null != bill.getCancelTime()) {
			dto.setCancelTime(bill.getCancelTime().getTime());
		}
		// Integer sitePrice = rentalProvider.getSumSitePrice(dto
		// .getRentalBillId());
		// dto.setSitePrice(sitePrice);
		dto.setTotalPrice(bill.getPayTotalMoney());
		dto.setSitePrice(bill.getSiteTotalMoney());
		dto.setReservePrice(bill.getReserveMoney());
		dto.setPaidPrice(bill.getPaidMoney());
		dto.setUnPayPrice(bill.getPayTotalMoney() - bill.getPaidMoney());
		dto.setInvoiceFlag(bill.getInvoiceFlag());
		dto.setStatus(bill.getStatus());
		dto.setRentalSiteRules(new ArrayList<RentalSiteRulesDTO>());
		// 订单的rules
		List<RentalSitesBill> rsbs = rentalProvider
				.findRentalSitesBillByBillId(bill.getId());
		List<Long> siteRuleIds = new ArrayList<Long>();
		for (RentalSitesBill rsb : rsbs) {
			siteRuleIds.add(rsb.getRentalSiteRuleId());
		}
		List<RentalSiteRule> rsrs = rentalProvider
				.findRentalSiteRulesByRuleIds(siteRuleIds);

		for (RentalSiteRule rsr : rsrs) {
			RentalSiteRulesDTO ruleDto = new RentalSiteRulesDTO();
			ruleDto.setId(rsr.getId());
			ruleDto.setRentalSiteId(rsr.getRentalSiteId());
			ruleDto.setRentalType(rsr.getRentalType());
			ruleDto.setRentalStep(rsr.getRentalStep()); 
			if (ruleDto.getRentalType().equals(RentalType.HOUR.getCode())) {
				ruleDto.setTimeStep(rsr.getTimeStep());
				ruleDto.setBeginTime(rsr.getBeginTime().getTime());
				ruleDto.setEndTime(rsr.getEndTime().getTime());
			} else if (ruleDto.getRentalType().equals(
					RentalType.HALFDAY.getCode())) {
				ruleDto.setAmorpm(rsr.getAmorpm());
			}
			ruleDto.setUnit(rsr.getUnit());
			ruleDto.setPrice(rsr.getPrice());
			ruleDto.setRuleDate(rsr.getSiteRentalDate().getTime());
			dto.getRentalSiteRules().add(ruleDto);
		}
		// 订单的附件attachments
		dto.setBillAttachments(new ArrayList<BillAttachmentDTO>());
		List<RentalBillAttachment> attachments = rentalProvider
				.findRentalBillAttachmentByBillId(dto.getRentalBillId());
		for (RentalBillAttachment attachment : attachments) {
			BillAttachmentDTO attachmentDTO = new BillAttachmentDTO();
			attachmentDTO.setAttachmentType(attachment.getAttachmentType());
			attachmentDTO.setBillId(attachment.getRentalBillId());
			attachmentDTO.setContent(attachment.getContent());
			attachmentDTO.setId(attachment.getId());
			dto.getBillAttachments().add(attachmentDTO);
		}
	}

	@Override
	public GetRentalTypeRuleCommandResponse getRentalTypeRule(
			GetRentalTypeRuleCommand cmd) {
		checkEnterpriseCommunityIdIsNull(cmd.getOwnerId());
		RentalRule rentalRule = rentalProvider.getRentalRule(
				cmd.getOwnerId(),cmd.getOwnerType(), cmd.getSiteType());
		GetRentalTypeRuleCommandResponse response = ConvertHelper.convert(rentalRule, GetRentalTypeRuleCommandResponse.class);
		return response;
	}

	@Override
	public void addRentalSiteSimpleRules(AddRentalSiteSimpleRulesCommand cmd) {
		Integer billCount = rentalProvider.countRentalSiteBills(
				cmd.getRentalSiteId(), cmd.getBeginDate(), cmd.getEndDate(),null,null);
		if (billCount > 0) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_HAVE_BILL,
					localeStringService.getLocalizedString(String
							.valueOf(RentalServiceErrorCode.SCOPE), String
							.valueOf(RentalServiceErrorCode.ERROR_HAVE_BILL),
							UserContext.current().getUser().getLocale(),
							"HAS BILL IN YOUR DELETE STUFF"));
		}
		Integer deleteCount = rentalProvider.deleteRentalSiteRules(
				cmd.getRentalSiteId(), cmd.getBeginDate(), cmd.getEndDate());
		LOGGER.debug("delete count = " + String.valueOf(deleteCount)
				+ "  from rental site rules  ");
		for(TimeInterval timeInterval:cmd.getTimeInterval()){
			if(timeInterval.getBeginTime() == null || timeInterval.getEndTime()==null)
				continue;
			AddRentalSiteSingleSimpleRule signleCmd=ConvertHelper.convert(cmd, AddRentalSiteSingleSimpleRule.class );
			signleCmd.setBeginTime(timeInterval.getBeginTime());
			signleCmd.setEndTime(timeInterval.getEndTime());
			addRentalSiteSingleSimpleRule(signleCmd);
		}
		
	}
	
	
	public void addRentalSiteSingleSimpleRule(AddRentalSiteSingleSimpleRule cmd) {
		Long userId = UserContext.current().getUser().getId();
		
		// if (null == cmd.getWeekendPrice() || null == cmd.getWorkdayPrice())
		// throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
		// ErrorCodes.ERROR_INVALID_PARAMETER,
		// "Invalid price   parameter in the command");
		RentalSiteRule rsr = new RentalSiteRule();
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.setTime(new Date(cmd.getBeginDate()));
		end.setTime(new Date(cmd.getEndDate()));
		// JSONObject jsonObject = (JSONObject)
		// JSONValue.parse(cmd.getChoosen());
		// JSONArray choosenValue = (JSONArray) jsonObject.get("choosen");
		// Gson gson = new Gson();
		// List<Integer> choosenInts = gson.fromJson(choosenValue.toString(),
		// new TypeToken<List<Integer>>() {
		// }.getType());
		while (start.before(end)) {
			Integer weekday = start.get(Calendar.DAY_OF_WEEK);
			if (cmd.getChoosen().contains(weekday)) {
				if (cmd.getRentalType().equals(RentalType.HOUR.getCode())) {
					for (double i = cmd.getBeginTime(); i < cmd.getEndTime();) {
						rsr.setOwnerId(cmd.getOwnerId());
						rsr.setOwnerType(cmd.getOwnerType());
						rsr.setSiteType(cmd.getSiteType());
						rsr.setBeginTime(Timestamp.valueOf(dateSF.format(start
								.getTime())
								+ " "
								+ String.valueOf((int) i / 1)
								+ ":"
								+ String.valueOf((int) ((i % 1) * 60))
								+ ":00"));

						// i = i + cmd.getTimeStep();
						rsr.setRentalStep(cmd.getRentalStep()); 
						rsr.setTimeStep(cmd.getTimeStep());
//						i = i + 0.5;
						i = i + cmd.getTimeStep();
						rsr.setEndTime(Timestamp.valueOf(dateSF.format(start
								.getTime())
								+ " "
								+ String.valueOf((int) i / 1)
								+ ":"
								+ String.valueOf((int) ((i % 1) * 60))
								+ ":00"));
						rsr.setRentalSiteId(cmd.getRentalSiteId());
						rsr.setRentalType(cmd.getRentalType());
						rsr.setCounts(cmd.getCounts());
						rsr.setUnit(cmd.getUnit());
						if (weekday == 1 || weekday == 7) {
							rsr.setPrice(cmd.getWeekendPrice());
						} else {
							rsr.setPrice(cmd.getWorkdayPrice());
						}
						rsr.setSiteRentalDate(Date.valueOf(dateSF.format(start
								.getTime())));
						rsr.setStatus(cmd.getStatus());
						rsr.setCreateTime(new Timestamp(DateHelper
								.currentGMTTime().getTime()));
						rsr.setCreatorUid(userId);
						rentalProvider.createRentalSiteRule(rsr);

					}
				}
				// 按半日预定
				else if (cmd.getRentalType().equals(
						RentalType.HALFDAY.getCode())) {
					rsr.setOwnerId(cmd.getOwnerId());
					rsr.setOwnerType(cmd.getOwnerType());
					rsr.setSiteType(cmd.getSiteType());
					rsr.setRentalSiteId(cmd.getRentalSiteId());
					rsr.setRentalType(cmd.getRentalType());
					rsr.setCounts(cmd.getCounts());
					rsr.setUnit(cmd.getUnit());
					rsr.setSiteRentalDate(Date.valueOf(dateSF.format(start
							.getTime())));
					rsr.setStatus(cmd.getStatus());
					rsr.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
							.getTime()));
					rsr.setCreatorUid(userId);
					rsr.setRentalStep(cmd.getRentalStep());
					if (weekday == 1 || weekday == 7) {
						rsr.setPrice(cmd.getWeekendAMPrice());
						rsr.setAmorpm(AmorpmFlag.AM.getCode());
						rentalProvider.createRentalSiteRule(rsr);
						rsr.setPrice(cmd.getWeekendPMPrice());
						rsr.setAmorpm(AmorpmFlag.PM.getCode());
						rentalProvider.createRentalSiteRule(rsr);
					} else {
						rsr.setPrice(cmd.getWorkdayAMPrice());
						rsr.setAmorpm(AmorpmFlag.AM.getCode());
						rentalProvider.createRentalSiteRule(rsr);
						rsr.setPrice(cmd.getWorkdayPMPrice());
						rsr.setAmorpm(AmorpmFlag.PM.getCode());
						rentalProvider.createRentalSiteRule(rsr);
					}
				}
				// 按日预定
				else if (cmd.getRentalType().equals(RentalType.DAY.getCode())) {
					rsr.setOwnerId(cmd.getOwnerId());
					rsr.setOwnerType(cmd.getOwnerType());
					rsr.setSiteType(cmd.getSiteType());
					rsr.setRentalSiteId(cmd.getRentalSiteId());
					rsr.setRentalType(cmd.getRentalType());
					rsr.setCounts(cmd.getCounts());
					rsr.setRentalStep(cmd.getRentalStep());
					rsr.setUnit(cmd.getUnit());
					if (weekday == 1 || weekday == 7) {
						rsr.setPrice(cmd.getWeekendPrice());
					} else {
						rsr.setPrice(cmd.getWorkdayPrice());
					}
					rsr.setSiteRentalDate(Date.valueOf(dateSF.format(start
							.getTime())));
					rsr.setStatus(cmd.getStatus());
					rsr.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
							.getTime()));
					rsr.setCreatorUid(userId);
					rentalProvider.createRentalSiteRule(rsr);
				}

			}
			start.add(Calendar.DAY_OF_MONTH, 1);
		}
	}

	@Override
	public void deleteRentalSiteRules(DeleteRentalSiteRulesCommand cmd) {
		JSONObject jsonObject = (JSONObject) JSONValue
				.parse(cmd.getRuleDates());
		JSONArray choosenValue = (JSONArray) jsonObject.get("ruleDates");
		Gson gson = new Gson();
		List<Long> deleteDates = gson.fromJson(choosenValue.toString(),
				new TypeToken<List<Long>>() {
				}.getType());
		for (Long deleteDate : deleteDates) {
			rentalProvider
					.deleteRentalSiteRules(Long.valueOf(cmd.getRentalSiteId()),
							deleteDate, deleteDate);
		}
	}

	@Override
	public void cancelRentalBill(CancelRentalBillCommand cmd) {
		if(null!=cmd.getCommunityId()){
			cmd.setOwnerId(cmd.getCommunityId());
			cmd.setOwnerType(RentalOwnerType.COMMUNITY.getCode());
		}
		RentalRule rule = this.rentalProvider.getRentalRule(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getSiteType());
		java.util.Date cancelTime = new java.util.Date();
		RentalBill bill = this.rentalProvider.findRentalBillById(cmd.getRentalBillId());
		if (bill.getStatus().equals(SiteBillStatus.SUCCESS.getCode())&&cancelTime.after(new java.util.Date(bill.getStartTime().getTime()
				- rule.getCancelTime()))) {
			LOGGER.error("cancel over time");
			throw RuntimeErrorException
					.errorWith(
							RentalServiceErrorCode.SCOPE,
							RentalServiceErrorCode.ERROR_CANCEL_OVERTIME,
							localeStringService.getLocalizedString(
									String.valueOf(RentalServiceErrorCode.SCOPE),
									String.valueOf(RentalServiceErrorCode.ERROR_CANCEL_OVERTIME),
									UserContext.current().getUser().getLocale(),
									"cancel bill over time"));
		}else{
			rentalProvider.cancelRentalBillById(cmd.getRentalBillId());
		}
	}

	@Override
	public void updateRentalSite(UpdateRentalSiteCommand cmd) {
		// 已有未取消的预定，不能修改
		Integer billCount = rentalProvider.countRentalSiteBills(
				cmd.getRentalSiteId(), null, null, null, null);
		if (billCount > 0) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_HAVE_BILL,
					localeStringService.getLocalizedString(String
							.valueOf(RentalServiceErrorCode.SCOPE), String
							.valueOf(RentalServiceErrorCode.ERROR_HAVE_BILL),
							UserContext.current().getUser().getLocale(),
							"HAS BILL IN YOUR DELETE STUFF"));
		}
		RentalSite rentalsite = this.rentalProvider.getRentalSiteById(cmd.getRentalSiteId()); 
		rentalsite.setAddress(cmd.getAddress());
		rentalsite.setSiteName(cmd.getSiteName());
		rentalsite.setBuildingName(cmd.getBuildingName());
		rentalsite.setSpec(cmd.getSpec());
		rentalsite.setOwnCompanyName(cmd.getCompany());
		rentalsite.setContactName(cmd.getContactName());
		rentalsite.setContactPhonenum(cmd.getContactPhonenum());
		rentalsite.setIntroduction(cmd.getIntroduction());
		rentalsite.setNotice(cmd.getNotice());
		rentalProvider.updateRentalSite(rentalsite);
	}

	@Override
	public void deleteRentalSite(DeleteRentalSiteCommand cmd) {
		// 已有未取消的预定，不能删除
		Integer billCount = rentalProvider.countRentalSiteBills(
				cmd.getRentalSiteId(), null, null, null, null);
		if (billCount > 0) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_HAVE_BILL,
					localeStringService.getLocalizedString(String
							.valueOf(RentalServiceErrorCode.SCOPE), String
							.valueOf(RentalServiceErrorCode.ERROR_HAVE_BILL),
							UserContext.current().getUser().getLocale(),
							"HAS BILL IN YOUR DELETE STUFF"));
		}
		rentalProvider.deleteRentalSiteRules(cmd.getRentalSiteId(), null, null);
		rentalProvider.deleteRentalBillById(cmd.getRentalSiteId());
		rentalProvider.deleteRentalSite(cmd.getRentalSiteId());
	}
	@Override
	public void disableRentalSite(DisableRentalSiteCommand cmd) {
		// 已有未取消的预定，不能删除
		Integer billCount = rentalProvider.countRentalSiteBills(
				cmd.getRentalSiteId(), null, null, null, null);
		if (billCount > 0) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_HAVE_BILL,
					localeStringService.getLocalizedString(String
							.valueOf(RentalServiceErrorCode.SCOPE), String
							.valueOf(RentalServiceErrorCode.ERROR_HAVE_BILL),
							UserContext.current().getUser().getLocale(),
							"HAS BILL IN YOUR DELETE STUFF"));
		}
		rentalProvider.updateRentalSiteStatus(cmd.getRentalSiteId(),
				RentalSiteStatus.DISABLE.getCode());
	}
	

	@Override
	public void enableRentalSite(EnableRentalSiteCommand cmd) {
		// 已有未取消的预定，不能删除
		Integer billCount = rentalProvider.countRentalSiteBills(
				cmd.getRentalSiteId(), null, null, null, null);
		if (billCount > 0) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_HAVE_BILL,
					localeStringService.getLocalizedString(String
							.valueOf(RentalServiceErrorCode.SCOPE), String
							.valueOf(RentalServiceErrorCode.ERROR_HAVE_BILL),
							UserContext.current().getUser().getLocale(),
							"HAS BILL IN YOUR DELETE STUFF"));
		}
		rentalProvider.updateRentalSiteStatus(cmd.getRentalSiteId(),
				RentalSiteStatus.NORMAL.getCode());
	}
	
	
	@Override
	public void deleteRentalSiteItem(DeleteRentalSiteItemCommand cmd) {
		Integer billCount = rentalProvider.countRentalSiteItemBills(cmd
				.getRentalSiteItemId());
		if (billCount > 0) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_HAVE_BILL,
					localeStringService.getLocalizedString(String
							.valueOf(RentalServiceErrorCode.SCOPE), String
							.valueOf(RentalServiceErrorCode.ERROR_HAVE_BILL),
							UserContext.current().getUser().getLocale(),
							"HAS BILL IN YOUR DELETE STUFF"));

		}
		rentalProvider.deleteRentalSiteItemById(cmd.getRentalSiteItemId());
	}

	@Override
	public ListRentalSiteItemsCommandResponse listRentalSiteItems(
			ListRentalSiteItemsCommand cmd) {
		ListRentalSiteItemsCommandResponse response = new ListRentalSiteItemsCommandResponse();
		response.setSiteItems(new ArrayList<SiteItemDTO>());
		List<RentalSiteItem> rsiSiteItems = rentalProvider
				.findRentalSiteItems(cmd.getRentalSiteId());
		for (RentalSiteItem rsi : rsiSiteItems) {
			SiteItemDTO dto = new SiteItemDTO();
			dto.setCounts(rsi.getCounts());
			dto.setId(rsi.getId());
			dto.setItemName(rsi.getName());
			dto.setItemPrice(rsi.getPrice());
			response.getSiteItems().add(dto);
		}
		return response;
	}

	@Override
	public AddRentalBillItemCommandResponse addRentalItemBill(
			AddRentalBillItemCommand cmd) {

		if(null!=cmd.getCommunityId()){
			cmd.setOwnerId(cmd.getCommunityId());
			cmd.setOwnerType(RentalOwnerType.COMMUNITY.getCode());
		}
		// 循环存物品订单
		AddRentalBillItemCommandResponse response = new AddRentalBillItemCommandResponse();
		Long userId = UserContext.current().getUser().getId();

		RentalBill bill = rentalProvider.findRentalBillById(cmd
				.getRentalBillId());
		if (bill.getStatus().equals(SiteBillStatus.FAIL.getCode())) {
			throw RuntimeErrorException
					.errorWith(
							RentalServiceErrorCode.SCOPE,
							RentalServiceErrorCode.ERROR_BILL_OVERTIME,
							localeStringService.getLocalizedString(
									String.valueOf(RentalServiceErrorCode.SCOPE),
									String.valueOf(RentalServiceErrorCode.ERROR_BILL_OVERTIME),
									UserContext.current().getUser().getLocale(),
									"BILL OVERTIME"));
		}
		if (!cmd.getInvoiceFlag().equals(bill.getInvoiceFlag()))
			rentalProvider.updateBillInvoice(cmd.getRentalBillId(),
					cmd.getInvoiceFlag());
		if (null != cmd.getRentalItems()) {
			if (cmd.getRentalItems().get(0).getItemPrice() != null) {
				double itemMoney = 0.0;
				for (SiteItemDTO siDto : cmd.getRentalItems()) {
					if (cmd.getRentalItems().get(0).getItemPrice() == null)
						continue;
					RentalItemsBill rib = new RentalItemsBill();
					rib.setTotalMoney(siDto.getItemPrice() * siDto.getCounts());

					rib.setCommunityId(cmd.getOwnerId());
					rib.setRentalSiteItemId(siDto.getId());
					rib.setRentalCount(siDto.getCounts());
					rib.setRentalBillId(cmd.getRentalBillId());
					rib.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
							.getTime()));
					rib.setCreatorUid(userId);
					itemMoney += rib.getTotalMoney();
					rentalProvider.createRentalItemBill(rib);
				}
				if (itemMoney > 0) {
					bill.setPayTotalMoney(bill.getSiteTotalMoney() + itemMoney);
					bill.setReserveMoney(bill.getReserveMoney() + itemMoney);
				}
			

			}
		}
		if (bill.getPayTotalMoney().equals(0.0)) {
			// 总金额为0，直接预订成功状态
			bill.setStatus(SiteBillStatus.SUCCESS.getCode());
		} else if ( bill.getStatus().equals(
						SiteBillStatus.LOCKED.getCode())) {
			// 预付金额为0，且状态为locked，直接进入支付定金成功状态
			if(bill.getReserveMoney().equals(0.0))
				bill.setStatus(SiteBillStatus.RESERVED.getCode());
			else if (bill.getReserveMoney().equals(bill.getPayTotalMoney()-bill.getPaidMoney()))
				bill.setStatus(SiteBillStatus.PAYINGFINAL.getCode());

		}
		rentalProvider.updateRentalBill(bill);
		
		switch(cmd.getSiteType()){
		case("MEETINGROOM"): 
			response.setName("会议室预定订单");
			response.setDescription("会议室预定订单");
			response.setOrderType("huiyishiorder");
			break;
		case("VIPPARKING"):
			response.setName("VIP车位预定订单");
			response.setDescription("VIP车位预定订单");
			response.setOrderType("vipcheweiorder");
			break;
		case("ELECSCREEN"):
			response.setName("电子屏预定订单");
			response.setDescription("电子屏预定订单");
			response.setOrderType("dianzipingorder"); 
			break;
		}
		Long orderNo = null;
		if (bill.getStatus().equals(SiteBillStatus.LOCKED.getCode())) {
			orderNo = onlinePayService.createBillId(DateHelper
					.currentGMTTime().getTime());
			response.setAmount(bill.getReserveMoney());
			response.setOrderNo(String.valueOf(orderNo));
			
		} else if (bill.getStatus()
				.equals(SiteBillStatus.PAYINGFINAL.getCode())) {
			orderNo = onlinePayService.createBillId(DateHelper
					.currentGMTTime().getTime());
			response.setAmount(bill.getPayTotalMoney() - bill.getPaidMoney());
			response.setOrderNo(String.valueOf(orderNo));
		} else {
			response.setAmount(0.0);
		}
		// save bill and online pay bill
		RentalBillPaybillMap billmap = new RentalBillPaybillMap();

		billmap.setOwnerId(cmd.getOwnerId());
		billmap.setOwnerType(cmd.getOwnerType());
		billmap.setSiteType(cmd.getSiteType());
		billmap.setRentalBillId(cmd.getRentalBillId());
		billmap.setOnlinePayBillId(orderNo);
		billmap.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
				.getTime()));
		billmap.setCreatorUid(userId);
		rentalProvider.createRentalBillPaybillMap(billmap);

		if (null != cmd.getAttachmentType()) {
			RentalBillAttachment rba = new RentalBillAttachment();
			rba.setRentalBillId(cmd.getRentalBillId());
			rba.setOwnerId(cmd.getOwnerId());
			rba.setOwnerType(cmd.getOwnerType());
			rba.setSiteType(cmd.getSiteType());

			rba.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			rba.setCreatorUid(userId);
			rba.setAttachmentType(cmd.getAttachmentType());
			if (cmd.getAttachmentType().equals(AttachmentType.STRING.getCode())) {
				if (null != cmd.getRentalAttachments()
						&& cmd.getRentalAttachments().size() > 0) {
					for (String attachment : cmd.getRentalAttachments()) {

						rba.setContent(attachment);
						rentalProvider.createRentalBillAttachment(rba);
					}
				}
			} else if (cmd.getAttachmentType().equals(
					AttachmentType.EMAIL.getCode())) {
				rentalProvider.createRentalBillAttachment(rba);
			} else {
			}
		}
		// 客户端生成订单
		return response;
	}

	@Override
	public ListRentalBillsCommandResponse listRentalBills(
			ListRentalBillsCommand cmd) {
		ListRentalBillsCommandResponse response = new ListRentalBillsCommandResponse();
		cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
		int totalCount = rentalProvider.countRentalBills(cmd.getOwnerId(),cmd.getOwnerType(),
				cmd.getSiteType(), cmd.getRentalSiteId(), cmd.getBillStatus(),
				cmd.getStartTime(), cmd.getEndTime(), cmd.getInvoiceFlag(),null);
		if (totalCount == 0)
			return response;

		Integer pageSize = PaginationConfigHelper.getPageSize(
				configurationProvider, cmd.getPageSize());
		int pageCount = getPageCount(totalCount, pageSize);
		checkEnterpriseCommunityIdIsNull(cmd.getOwnerId());
		List<RentalBill> bills = rentalProvider.listRentalBills(
				cmd.getOwnerId(),cmd.getOwnerType(), cmd.getSiteType(), cmd.getRentalSiteId(),
				cmd.getBillStatus(), cmd.getPageOffset(), pageSize,
				cmd.getStartTime(), cmd.getEndTime(), cmd.getInvoiceFlag());
		response.setRentalBills(new ArrayList<RentalBillDTO>());
		for (RentalBill bill : bills) {
			RentalBillDTO dto = new RentalBillDTO();
			mappingRentalBillDTO(dto, bill);
			dto.setSiteItems(new ArrayList<SiteItemDTO>());
			List<RentalItemsBill> rentalSiteItems = rentalProvider
					.findRentalItemsBillBySiteBillId(dto.getRentalBillId());
			for (RentalItemsBill rib : rentalSiteItems) {
				SiteItemDTO siDTO = new SiteItemDTO();
				siDTO.setCounts(rib.getRentalCount());
				RentalSiteItem rsItem = rentalProvider
						.findRentalSiteItemById(rib.getRentalSiteItemId());
				siDTO.setItemName(rsItem.getName());
				siDTO.setItemPrice(rib.getTotalMoney());
				dto.getSiteItems().add(siDTO);
			}
			response.getRentalBills().add(dto);
		}

		response.setNextPageOffset(cmd.getPageOffset() == pageCount ? null
				: cmd.getPageOffset() + 1);
		return response;
	}

	@Override
	public void deleteRentalBill(DeleteRentalBillCommand cmd) {

		if(null!=cmd.getCommunityId()){
			cmd.setOwnerId(cmd.getCommunityId());
			cmd.setOwnerType(RentalOwnerType.COMMUNITY.getCode());
		}

		rentalProvider.deleteRentalBillById(cmd.getRentalBillId());

	}
 

	@Override
	public OnlinePayCallbackCommandResponse onlinePayCallback(
			OnlinePayCallbackCommand cmd) {
		// TODO Auto-generated method stub
		OnlinePayCallbackCommandResponse response = new OnlinePayCallbackCommandResponse();
		if(cmd.getPayStatus().toLowerCase().equals("fail")) {
			//TODO: 失败以后可能还是要做点什么
			LOGGER.info(" ----------------- - - - PAY FAIL ");
		}
			
		//success
		if(cmd.getPayStatus().toLowerCase().equals("success"))
		{
			RentalBillPaybillMap bpbMap= rentalProvider.findRentalBillPaybillMapByOrderNo(cmd.getOrderNo());
			RentalBill bill = rentalProvider.findRentalBillById(bpbMap.getRentalBillId());
			bill.setPaidMoney(bill.getPaidMoney()+Double.valueOf(cmd.getPayAmount()));
			if(bill.getStatus().equals(SiteBillStatus.LOCKED.getCode())){
				bill.setStatus(SiteBillStatus.RESERVED.getCode());
			}
			else if(bill.getStatus().equals(SiteBillStatus.PAYINGFINAL.getCode())){
				if(bill.getPayTotalMoney().equals(bill.getPaidMoney())){
					bill.setStatus(SiteBillStatus.SUCCESS.getCode());
				}
				else{
					
				}
			} 
			rentalProvider.updateRentalBill(bill);
		}
		return response;
	}

	@Override
	public FindRentalSiteWeekStatusCommandResponse findRentalSiteWeekStatus(
			FindRentalSiteWeekStatusCommand cmd) {
		 
		java.util.Date reserveTime = new java.util.Date(); 
		RentalSite rs = this.rentalProvider.getRentalSiteById(cmd.getSiteId());
		FindRentalSiteWeekStatusCommandResponse response = ConvertHelper.convert(rs, FindRentalSiteWeekStatusCommandResponse.class);

		if(cmd.getOwnerType().equals(RentalOwnerType.ORGANIZATION.getCode())){
			Organization org = this.organizationProvider.findOrganizationById(cmd.getOwnerId());
			response.setOwnerName(org.getName());
		}
		// 查rules
		RentalRule rentalRule = rentalProvider.getRentalRule(
				cmd.getOwnerId(),cmd.getOwnerType(), cmd.getSiteType());
		java.util.Date nowTime = new java.util.Date();
		
		Timestamp beginTime = new Timestamp(nowTime.getTime()
				+ rentalRule.getRentalStartTime());
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.setTime(new Date(cmd.getRuleDate()));
		end.setTime(new Date(cmd.getRuleDate()));
		start.set(Calendar.DAY_OF_WEEK,
				start.getActualMinimum(Calendar.DAY_OF_WEEK));
		end.set(Calendar.DAY_OF_WEEK,
				start.getActualMinimum(Calendar.DAY_OF_WEEK));
		end.add(Calendar.DAY_OF_YEAR, 7);
		response.setSiteDays(new ArrayList<RentalSiteDayRulesDTO>());
		for(;start.before(end);start.add(Calendar.DAY_OF_YEAR, 1)){
			RentalSiteDayRulesDTO dayDto = new RentalSiteDayRulesDTO();
			response.getSiteDays().add(dayDto);
			dayDto.setSiteRules(new ArrayList<RentalSiteRulesDTO>());
			dayDto.setRentalDate(start.getTimeInMillis());
			List<RentalSiteRule> rentalSiteRules = rentalProvider
					.findRentalSiteRules(cmd.getSiteId(), dateSF.format(new java.util.Date(start.getTimeInMillis())),
							beginTime, cmd.getRentalType()==null?RentalType.DAY.getCode():cmd.getRentalType(), DateLength.DAY.getCode());
			// 查sitebills
			if (null != rentalSiteRules && rentalSiteRules.size() > 0) {
				for (RentalSiteRule rsr : rentalSiteRules) {
					RentalSiteRulesDTO dto = new RentalSiteRulesDTO();
					dto.setId(rsr.getId());
					dto.setRentalSiteId(rsr.getRentalSiteId());
					dto.setRentalType(rsr.getRentalType());
					dto.setRentalStep(rsr.getRentalStep());
					if (dto.getRentalType().equals(RentalType.HOUR.getCode())) {
						dto.setTimeStep(rsr.getTimeStep());
						dto.setBeginTime(rsr.getBeginTime().getTime());
						dto.setEndTime(rsr.getEndTime().getTime());
					} else if (dto.getRentalType().equals(
							RentalType.HALFDAY.getCode())) {
						dto.setAmorpm(rsr.getAmorpm());
					}
					dto.setUnit(rsr.getUnit());
					dto.setPrice(rsr.getPrice());
					dto.setRuleDate(rsr.getSiteRentalDate().getTime());
					List<RentalSitesBill> rsbs = rentalProvider
							.findRentalSiteBillBySiteRuleId(rsr.getId());
					dto.setStatus(SiteRuleStatus.OPEN.getCode());
					dto.setCounts((double) rsr.getCounts());
					if (null != rsbs && rsbs.size() > 0) {
						for (RentalSitesBill rsb : rsbs) {
							dto.setCounts(dto.getCounts()
									- rsb.getRentalCount());
						}
					}
					if (dto.getCounts() == 0) {
						dto.setStatus(SiteRuleStatus.CLOSE.getCode());
					}
					if (dto.getRentalType().equals(RentalType.HOUR.getCode())) {
						if (reserveTime.before(new java.util.Date(rsr
								.getBeginTime().getTime()
								- rentalRule.getRentalStartTime()))) {
							dto.setStatus(SiteRuleStatus.EARLY.getCode());
						}
						if (reserveTime.after(new java.util.Date(rsr
								.getBeginTime().getTime()
								- rentalRule.getRentalEndTime()))) {
							dto.setStatus(SiteRuleStatus.LATE.getCode());
						}
					} else {
						if (reserveTime.before(new java.util.Date(rsr
								.getSiteRentalDate().getTime()
								- rentalRule.getRentalStartTime()))) {
							dto.setStatus(SiteRuleStatus.EARLY.getCode());
						}
						if (reserveTime.after(new java.util.Date(rsr
								.getSiteRentalDate().getTime()
								- rentalRule.getRentalEndTime()))) {
							dto.setStatus(SiteRuleStatus.LATE.getCode());
						}
					}
					dayDto.getSiteRules().add(dto);
	
				}
			}
		}
		return response;
	}

	@Override
	public RentalBillDTO confirmBill(ConfirmBillCommand cmd) {
		RentalBill bill = this.rentalProvider.findRentalBillById(cmd.getRentalBillId());
		if (bill.getStatus().equals(SiteBillStatus.FAIL.getCode())){
			throw RuntimeErrorException
			.errorWith(
					RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_RESERVE_TOO_LATE,
					localeStringService.getLocalizedString(
							String.valueOf(RentalServiceErrorCode.SCOPE),
							String.valueOf(RentalServiceErrorCode.ERROR_RESERVE_TOO_LATE),
							UserContext.current().getUser().getLocale(),
							"too late to order the service")); 
		}
		if (bill.getPayTotalMoney().equals(0.0)){
			bill.setStatus(SiteBillStatus.SUCCESS.getCode());
			rentalProvider.updateRentalBill(bill);
		}
		else {

			throw RuntimeErrorException
			.errorWith(
					RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_DID_NOT_PAY,
					localeStringService.getLocalizedString(
							String.valueOf(RentalServiceErrorCode.SCOPE),
							String.valueOf(RentalServiceErrorCode.ERROR_DID_NOT_PAY),
							UserContext.current().getUser().getLocale(),
							"did not pay for the bill ,can not confirm")); 
		}
		RentalBillDTO dto = new RentalBillDTO();
		mappingRentalBillDTO(dto, bill);
		return dto;
	}

	@Override
	public RentalBillDTO completeBill(CompleteBillCommand cmd) {
		RentalBill bill = this.rentalProvider.findRentalBillById(cmd.getRentalBillId());
		if (bill.getStatus().equals(SiteBillStatus.SUCCESS.getCode())){
			throw RuntimeErrorException
			.errorWith(
					RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_NOT_SUCCESS,
					localeStringService.getLocalizedString(
							String.valueOf(RentalServiceErrorCode.SCOPE),
							String.valueOf(RentalServiceErrorCode.ERROR_NOT_SUCCESS),
							UserContext.current().getUser().getLocale(),
							"order is not success order.")); 
		} 
		bill.setStatus(SiteBillStatus.COMPLETE.getCode());
		rentalProvider.updateRentalBill(bill);
	 
		RentalBillDTO dto = new RentalBillDTO();
		mappingRentalBillDTO(dto, bill);
		return dto;
	}

	@Override
	public RentalBillDTO incompleteBill(IncompleteBillCommand cmd) {
		RentalBill bill = this.rentalProvider.findRentalBillById(cmd.getRentalBillId());
		if (!bill.getStatus().equals(SiteBillStatus.COMPLETE.getCode())){
			throw RuntimeErrorException
			.errorWith(
					RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_NOT_COMPLETE,
					localeStringService.getLocalizedString(
							String.valueOf(RentalServiceErrorCode.SCOPE),
							String.valueOf(RentalServiceErrorCode.ERROR_NOT_COMPLETE),
							UserContext.current().getUser().getLocale(),
							"order is not complete order.")); 
		} 
		RentalRule rule = this.rentalProvider.getRentalRule(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getSiteType());
		java.util.Date cancelTime = new java.util.Date();
		if (cancelTime.before(new java.util.Date(bill.getEndTime().getTime()
				+ rule.getOvertimeTime()))) {
			bill.setStatus(SiteBillStatus.SUCCESS.getCode());
		}else{
			bill.setStatus(SiteBillStatus.OVERTIME.getCode());
		}
		
		rentalProvider.updateRentalBill(bill);
	 
		RentalBillDTO dto = new RentalBillDTO();
		mappingRentalBillDTO(dto, bill);
		return dto;
	}

	@Override
	public BatchCompleteBillCommandResponse batchIncompleteBill(BatchIncompleteBillCommand cmd) { 
		BatchCompleteBillCommandResponse response = new BatchCompleteBillCommandResponse();
		response.setBills(new ArrayList<RentalBillDTO>());
		for (Long billId : cmd.getRentalBillIds()){
			IncompleteBillCommand cmd2 = ConvertHelper.convert(cmd, IncompleteBillCommand.class);
			cmd2.setRentalBillId(billId);
			response.getBills().add(this.incompleteBill(cmd2));
		}
		return response;
	}

	@Override
	public BatchCompleteBillCommandResponse batchCompleteBill(BatchCompleteBillCommand cmd) {
		BatchCompleteBillCommandResponse response = new BatchCompleteBillCommandResponse();
		response.setBills(new ArrayList<RentalBillDTO>());
		for (Long billId : cmd.getRentalBillIds()){
			CompleteBillCommand cmd2 = ConvertHelper.convert(cmd, CompleteBillCommand.class);
			cmd2.setRentalBillId(billId);
			response.getBills().add(this.completeBill(cmd2));
		}
		return response;
	}
	
	@Override
	public ListRentalBillCountCommandResponse listRentalBillCount(ListRentalBillCountCommand cmd) {
		ListRentalBillCountCommandResponse response = new ListRentalBillCountCommandResponse(); 
		response.setRentalBillCounts(new ArrayList<RentalBillCountDTO>());
		if(cmd.getRentalSiteId() == null ){
			List<RentalSite>  sites = this.rentalProvider.findRentalSites(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getSiteType(),null , null, null);
			for(RentalSite site : sites){
				response.getRentalBillCounts().add(processRentalBillCountDTO(site,cmd.getBeginDate(),cmd.getEndDate()));
			}
		}
		else{
			RentalSite site = this.rentalProvider.getRentalSiteById(cmd.getRentalSiteId());
			response.getRentalBillCounts().add(processRentalBillCountDTO(site,cmd.getBeginDate(),cmd.getEndDate()));
			
		}
		
		return response;
	}
	
	private RentalBillCountDTO processRentalBillCountDTO(RentalSite site,
			Long beginDate, Long endDate) {
		RentalBillCountDTO dto = new RentalBillCountDTO();
		List<RentalBill> bills = this.rentalProvider.listRentalBills(site.getOwnerId(), site.getOwnerType(), site.getSiteType(), 
				site.getId(),beginDate,endDate);
		dto.setSiteName(site.getSiteName());
		processRentalBillCountDTO(dto, bills);
		return dto;
	} 

	private void processRentalBillCountDTO(RentalBillCountDTO dto, List<RentalBill> bills ){
		Integer sumCount =0;                    
		Integer completeCount =0;               
		Integer cancelCount=0;                      
		Integer overTimeCount=0;                 
		Integer successCount=0;                  
		for(RentalBill bill : bills){
			sumCount++;
			if(bill.getStatus().equals(SiteBillStatus.COMPLETE.getCode())){
				completeCount++;
			}
			else 	if(bill.getStatus().equals(SiteBillStatus.FAIL.getCode())){
				cancelCount ++;
			}
			else 	if(bill.getStatus().equals(SiteBillStatus.OVERTIME.getCode())){
				overTimeCount ++ ;
			}
			else 	if(bill.getStatus().equals(SiteBillStatus.SUCCESS.getCode())){
				successCount ++;
			} 
		}
		dto.setSumCount(sumCount);
		dto.setCancelCount(cancelCount);
		dto.setCompleteCount(completeCount);
		dto.setOverTimeCount(overTimeCount);
		dto.setSuccessCount(successCount);
	}
}
