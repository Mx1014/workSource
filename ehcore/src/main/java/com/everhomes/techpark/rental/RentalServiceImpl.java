package com.everhomes.techpark.rental;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
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
		checkEnterpriseCommunityIdIsNull(cmd.getEnterpriseCommunityId());
		RentalRule rentalRule = rentalProvider.getRentalRule(
				cmd.getEnterpriseCommunityId(), cmd.getSiteType());
		rentalRule.setContactNum(cmd.getContactNum());
		rentalRule.setPayEndTime(cmd.getPayEndTime());
		rentalRule.setPaymentRatio(cmd.getPayRatio());
		rentalRule.setPayStartTime(cmd.getPayStartTime());
		rentalRule.setRefundFlag(cmd.getRefundFlag());
		rentalRule.setRentalEndTime(cmd.getRentalEndTime());
		rentalRule.setRentalStartTime(cmd.getRentalStartTime());
		rentalRule.setOperateTime(new Timestamp(DateHelper.currentGMTTime()
				.getTime()));
		rentalRule.setOperatorUid(userId);
		rentalProvider.updateRentalRule(rentalRule);
		return null;
	}

	@Override
	public Long addRentalSite(AddRentalSiteCommand cmd) {
		RentalSite rentalsite = new RentalSite();
		rentalsite.setEnterpriseCommunityId(cmd.getEnterpriseCommunityId());
		rentalsite.setAddress(cmd.getAddress());
		rentalsite.setBuildingName(cmd.getBuildingName());
		rentalsite.setSiteName(cmd.getSiteName());
		rentalsite.setOwnCompanyName(cmd.getCompany());
		rentalsite.setContactName(cmd.getContactName());
		rentalsite.setContactPhonenum(cmd.getContactPhonenum());
		rentalsite.setSiteType(cmd.getSiteType());
		rentalsite.setSpec(cmd.getSpec());
		rentalsite.setStatus(RentalSiteStatus.NORMAL.getCode());
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
	public FindRentalSiteDayStatusCommandResponse findRentalSiteDayStatus(
			FindRentalSiteDayStatusCommand cmd) {

		java.util.Date reserveTime = new java.util.Date();
		FindRentalSiteDayStatusCommandResponse response = new FindRentalSiteDayStatusCommandResponse();
		response.setSites(new ArrayList<RentalSiteDTO>());
		RentalRule rentalRule = rentalProvider.getRentalRule(
				cmd.getEnterpriseCommunityId(), cmd.getSiteType());
		// 查sites
		List<RentalSite> rentalSites = rentalProvider.findRentalSites(
				cmd.getEnterpriseCommunityId(), cmd.getSiteType(), null, null,
				null);
		for (RentalSite rs : rentalSites) {
			RentalSiteDTO rsDTO = new RentalSiteDTO();
			rsDTO.setBuildingName(rs.getBuildingName());
			rsDTO.setSiteName(rs.getSiteName());
			rsDTO.setContactName(rs.getContactName());
			rsDTO.setCompanyName(rs.getOwnCompanyName());
			rsDTO.setSpec(rs.getSpec());
			rsDTO.setAddress(rs.getAddress());
			rsDTO.setContactPhonenum(rs.getContactPhonenum());
			rsDTO.setEnterpriseCommunityId(rs.getEnterpriseCommunityId());
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
							beginTime);
			// 查sitebills
			if (null != rentalSiteRules && rentalSiteRules.size() > 0) {
				for (RentalSiteRule rsr : rentalSiteRules) {
					RentalSiteRulesDTO dto = new RentalSiteRulesDTO();
					dto.setId(rsr.getId());
					dto.setRentalSiteId(rsr.getRentalSiteId());
					dto.setBeginTime(rsr.getBeginTime().getTime());
					dto.setEndTime(rsr.getEndTime().getTime());
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
					if (reserveTime.before(new java.util.Date(rsr.getBeginTime().getTime()
							- rentalRule.getRentalStartTime()))) {
						dto.setStatus(SiteRuleStatus.LATE.getCode());
					}
					if (reserveTime.after(new java.util.Date(rsr.getBeginTime().getTime()
							- rentalRule.getRentalEndTime()))) {
						dto.setStatus(SiteRuleStatus.EARLY.getCode());
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
		int totalCount = rentalProvider.countRentalSites(
				cmd.getEnterpriseCommunityId(), cmd.getSiteType(),
				cmd.getKeyword());
		if (totalCount == 0)
			return response;

		Integer pageSize = PaginationConfigHelper.getPageSize(
				configurationProvider, cmd.getPageSize());
		int pageCount = getPageCount(totalCount, pageSize);

		checkEnterpriseCommunityIdIsNull(cmd.getEnterpriseCommunityId());
		List<RentalSite> rentalSites = rentalProvider.findRentalSites(
				cmd.getEnterpriseCommunityId(), cmd.getSiteType(),
				cmd.getKeyword(), cmd.getPageOffset(), pageSize);

		response.setRentalSites(new ArrayList<RentalSiteDTO>());
		for (RentalSite rentalSite : rentalSites) {
			RentalSiteDTO rSiteDTO = new RentalSiteDTO();
			rSiteDTO.setSiteName(rentalSite.getSiteName());
			rSiteDTO.setRentalSiteId(rentalSite.getId());
			rSiteDTO.setAddress(rentalSite.getAddress());
			rSiteDTO.setBuildingName(rentalSite.getBuildingName());
			rSiteDTO.setCompanyName(rentalSite.getOwnCompanyName());
			rSiteDTO.setContactPhonenum(rentalSite.getContactPhonenum());
			rSiteDTO.setEnterpriseCommunityId(cmd.getEnterpriseCommunityId());
			rSiteDTO.setContactName(rentalSite.getContactName());
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
			rSiteDTO.setSiteType(rentalSite.getSiteType());
			rSiteDTO.setSiteType("meetingRoom");
			rSiteDTO.setSpec(rentalSite.getSpec());
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
		List<RentalSiteRule> rsrs = rentalProvider.findRentalSiteRules(
				cmd.getRentalSiteId(), cmd.getRuleDate(), null);
		// for (RentalSiteRule rsr : rsrs) {
		// RentalSiteRulesDTO dto = new RentalSiteRulesDTO();
		// dto.setId(rsr.getId());
		// dto.setRentalSiteId(rsr.getRentalSiteId());
		// dto.setRentalType(rsr.getRentalType());
		// if (rsr.getRentalType().equals(RentalType.DAY.getCode()))
		// dto.setStepLength(String.valueOf(rsr.getStepLengthDay()));
		// if (rsr.getRentalType().equals(RentalType.DAY.getCode()))
		// dto.setStepLength(timeSF.format(rsr.getStepLengthTime()
		// .getTime()));
		// dto.setBeginTime(timeSF.format(rsr.getBeginTime().getTime()));
		// dto.setEndTime(timeSF.format(rsr.getEndTime().getTime()));
		// dto.setCounts(rsr.getCounts());
		// dto.setUnit(rsr.getUnit());
		// dto.setPrice(rsr.getPrice());
		// if (rsr.getLoopType().equals(LoopType.EVERYDAY.getCode())) {
		//
		// dto.setRuleDate(dateSF.format(rsr.getRuleDate()));
		// } else if (rsr.getLoopType().equals(LoopType.EVERYMONTH.getCode())) {
		//
		// dto.setRuleDate(dateSF.format(rsr.getRuleDate()));
		// } else if (rsr.getLoopType().equals(LoopType.EVERYWEEK.getCode())) {
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTime(rsr.getRuleDate());
		// dto.setRuleDate(String.valueOf(calendar
		// .get(Calendar.DAY_OF_WEEK)));
		// } else if (rsr.getLoopType().equals(LoopType.EVERYYEAR.getCode())) {
		// dto.setRuleDate(dateSF.format(rsr.getRuleDate()));
		// } else if (rsr.getLoopType().equals(LoopType.ONLYTHEDAY.getCode())) {
		// dto.setRuleDate(dateSF.format(rsr.getRuleDate()));
		// }
		// dto.setLoopType(rsr.getLoopType());
		// dto.setStatus(rsr.getStatus());
		// response.getRentalSiteRules().add(dto);
		// }

		return response;
	}

	@Override
	public RentalBillDTO addRentalBill(AddRentalBillCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		RentalBillDTO response = new RentalBillDTO();
		java.util.Date reserveTime = new java.util.Date();
		List<RentalSiteRule> rentalSiteRules = new ArrayList<RentalSiteRule>();
		RentalRule rentalRule = rentalProvider.getRentalRule(
				cmd.getEnterpriseCommunityId(), cmd.getSiteType());
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
		rentalBill.setEnterpriseCommunityId(cmd.getEnterpriseCommunityId());
		rentalBill.setSiteType(cmd.getSiteType());
		rentalBill.setRentalSiteId(cmd.getRentalSiteId());
		rentalBill.setRentalUid(userId);
		rentalBill.setInvoiceFlag(InvoiceFlag.NONEED.getCode());
		rentalBill.setRentalDate(new Date(cmd.getRentalDate()));
		this.valiRentalBill(cmd.getRentalCount(), cmd.getRentalSiteRuleIds());
		rentalBill.setRentalCount(cmd.getRentalCount());
		Double siteTotalMoney = 0.0;
		for (Long siteRuleId : cmd.getRentalSiteRuleIds()) {
			if(null == siteRuleId)
				continue;
			RentalSiteRule rentalSiteRule = rentalProvider
					.findRentalSiteRuleById(siteRuleId);
			rentalSiteRules.add(rentalSiteRule);
			if (null == rentalBill.getStartTime()
					|| rentalBill.getStartTime().after(
							rentalSiteRule.getBeginTime())) {
				rentalBill.setStartTime(rentalSiteRule.getBeginTime());
			}
			if (null == rentalBill.getEndTime()
					|| rentalBill.getEndTime().before(
							rentalSiteRule.getEndTime())) {
				rentalBill.setEndTime(rentalSiteRule.getEndTime());
			}
			siteTotalMoney += rentalSiteRule.getPrice()
					* (cmd.getRentalCount() / rentalSiteRule.getUnit());
		}

		// for (SiteItemDTO siDto : cmd.getRentalItems()) {
		// totalMoney += siDto.getItemPrice() * siDto.getCounts();
		// }
		rentalBill.setSiteTotalMoney(siteTotalMoney);
		rentalBill.setPayTotalMoney(siteTotalMoney);
		rentalBill.setReserveMoney(siteTotalMoney
				* rentalRule.getPaymentRatio() / 100);
		rentalBill.setReserveTime(Timestamp.valueOf(datetimeSF
				.format(reserveTime)));
		rentalBill.setPayStartTime(new Timestamp(cmd.getStartTime()
				- rentalRule.getPayStartTime()));
		rentalBill.setPayEndTime(new Timestamp(cmd.getStartTime()
				- rentalRule.getPayEndTime()));
		rentalBill.setPaidMoney(0.0);
		if (reserveTime.before(new java.util.Date(cmd.getStartTime()
				- rentalRule.getPayStartTime()))) {
			// 在支付时间之前 为锁定待支付
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

		// 循环存site订单
		for (RentalSiteRule rsr : rentalSiteRules) {
			RentalSitesBill rsb = new RentalSitesBill();
			rsb.setEnterpriseCommunityId(cmd.getEnterpriseCommunityId());
			rsb.setSiteType(cmd.getSiteType());
			rsb.setRentalBillId(rentalBillId);
			rsb.setTotalMoney(rsr.getPrice()
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
			if(siteRuleId == null )
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

		Long userId = UserContext.current().getUser().getId();
		if (cmd.getPageAnchor() == null)
			cmd.setPageAnchor(9223372036854775807L);
		int pageSize = PaginationConfigHelper.getPageSize(
				configurationProvider, cmd.getPageSize());

		ListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<RentalBill> billList = this.rentalProvider.listRentalBills(userId,
				cmd.getEnterpriseCommunityId(), cmd.getSiteType(), locator,
				pageSize + 1, cmd.getBillStatus());
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
		dto.setContactPhonenum(rs.getContactPhonenum());
		dto.setRentalBillId(bill.getId());
		dto.setEnterpriseCommunityId(bill.getEnterpriseCommunityId());
		dto.setSiteType(bill.getSiteType());
		dto.setStartTime(bill.getStartTime().getTime());
		dto.setEndTime(bill.getEndTime().getTime());
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
	}

	@Override
	public GetRentalTypeRuleCommandResponse getRentalTypeRule(
			GetRentalTypeRuleCommand cmd) {
		checkEnterpriseCommunityIdIsNull(cmd.getEnterpriseCommunityId());
		GetRentalTypeRuleCommandResponse response = new GetRentalTypeRuleCommandResponse();
		RentalRule rentalRule = rentalProvider.getRentalRule(
				cmd.getEnterpriseCommunityId(), cmd.getSiteType());
		response.setContactNum(rentalRule.getContactNum());
		response.setEnterpriseCommunityId(cmd.getEnterpriseCommunityId());
		response.setPayEndTime(rentalRule.getPayEndTime());
		response.setPayRatio(rentalRule.getPaymentRatio());
		response.setPayStartTime(rentalRule.getPayStartTime());
		response.setRefundFlag(rentalRule.getRefundFlag());
		response.setRentalEndTime(rentalRule.getRentalEndTime());
		response.setRentalStartTime(rentalRule.getRentalStartTime());
		response.setSiteType(rentalRule.getSiteType());
		return response;
	}

	@Override
	public void addRentalSiteSimpleRules(AddRentalSiteSimpleRulesCommand cmd) {
		Integer billCount = rentalProvider.countRentalSiteBills(
				cmd.getRentalSiteId(), cmd.getBeginDate(), cmd.getEndDate());
		if (billCount > 0) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid price   parameter in the command");
		}
		Integer deleteCount = rentalProvider.deleteRentalSiteRules(
				cmd.getRentalSiteId(), cmd.getBeginDate(), cmd.getEndDate());
		LOGGER.debug("delete count = " + String.valueOf(deleteCount)
				+ "  from rental site rules  ");
		if (null == cmd.getWeekendPrice() || null == cmd.getWorkdayPrice())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid price   parameter in the command");
		RentalSiteRule rsr = new RentalSiteRule();
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.setTime(new Date(cmd.getBeginDate()));
		end.setTime(new Date(cmd.getEndDate()));
		JSONObject jsonObject = (JSONObject) JSONValue.parse(cmd.getChoosen());
		JSONArray choosenValue = (JSONArray) jsonObject.get("choosen");
		Gson gson = new Gson();
		// TODO: 按日预定
		List<Integer> choosenInts = gson.fromJson(choosenValue.toString(),
				new TypeToken<List<Integer>>() {
				}.getType());
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
					if (weekday == 1 || weekday == 7) {
						rsr.setPrice(cmd.getWeekendPrice());
					} else {
						rsr.setPrice(cmd.getWorkdayPrice());
					}
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
		rentalProvider.cancelRentalBillById(cmd.getRentalBillId());

	}

	@Override
	public void updateRentalSite(UpdateRentalSiteCommand cmd) {
		// 已有未取消的预定，不能修改
		Integer billCount = rentalProvider.countRentalSiteBills(
				cmd.getRentalSiteId(), null, null);
		if (billCount > 0) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid price   parameter in the command");
		}
		RentalSite rentalsite = new RentalSite();
		rentalsite.setEnterpriseCommunityId(cmd.getEnterpriseCommunityId());
		rentalsite.setAddress(cmd.getAddress());
		rentalsite.setId(cmd.getRentalSiteId());
		rentalsite.setBuildingName(cmd.getBuildingName());
		rentalsite.setSiteName(cmd.getSiteName());
		rentalsite.setOwnCompanyName(cmd.getCompany());
		rentalsite.setContactName(cmd.getContactName());
		rentalsite.setContactPhonenum(cmd.getContactPhonenum());
		rentalsite.setSiteType(cmd.getSiteType());
		rentalsite.setSpec(cmd.getSpec());
		rentalsite.setStatus(RentalSiteStatus.NORMAL.getCode());
		rentalProvider.updateRentalSite(rentalsite);
	}

	@Override
	public void deleteRentalSite(DeleteRentalSiteCommand cmd) {
		// 已有未取消的预定，不能删除
		Integer billCount = rentalProvider.countRentalSiteBills(
				cmd.getRentalSiteId(), null, null);
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
				RentalSiteStatus.DELETED.getCode());
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

		//循环存物品订单
		AddRentalBillItemCommandResponse response = new AddRentalBillItemCommandResponse();
		Long userId = UserContext.current().getUser().getId();
		if (cmd.getInvoiceFlag().equals(InvoiceFlag.NEED.getCode()))
			rentalProvider.updateBillInvoice(cmd.getRentalBillId(),
					cmd.getInvoiceFlag());
		if (cmd.getRentalItems() != null) {
			double itemMoney = 0.0;
			for (SiteItemDTO siDto : cmd.getRentalItems()) {
				RentalItemsBill rib = new RentalItemsBill();
				rib.setTotalMoney(siDto.getItemPrice() * siDto.getCounts());

				rib.setEnterpriseCommunityId(cmd.getEnterpriseCommunityId());
				rib.setSiteType(cmd.getSiteType());
				rib.setRentalSiteItemId(siDto.getId());
				rib.setRentalCount(siDto.getCounts());
				rib.setRentalBillId(cmd.getRentalBillId());
				rib.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
						.getTime()));
				rib.setCreatorUid(userId);
				itemMoney += rib.getTotalMoney();
				rentalProvider.createRentalItemBill(rib);
			}
			RentalBill bill = rentalProvider.findRentalBillById(cmd
					.getRentalBillId());
			if (itemMoney > 0) {
				bill.setPayTotalMoney(bill.getSiteTotalMoney() + itemMoney);
				bill.setReserveMoney(bill.getReserveMoney() + itemMoney);
			}
			rentalProvider.updateRentalBill(bill);
			if (bill.getStatus() == 0) {
				response.setAmount(bill.getReserveMoney());
			}
			if (bill.getStatus() == 3) {
				response.setAmount(bill.getPayTotalMoney()
						- bill.getPaidMoney());
			} 
			//TODO: 生成订单
		}
		return response;
	}

	@Override
	public ListRentalBillsCommandResponse listRentalBills(
			ListRentalBillsCommand cmd) {
		ListRentalBillsCommandResponse response = new ListRentalBillsCommandResponse();
		cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
		int totalCount = rentalProvider.countRentalBills(
				cmd.getEnterpriseCommunityId(), cmd.getSiteType(),
				cmd.getRentalSiteId(), cmd.getBillStatus(), cmd.getStartTime(),
				cmd.getEndTime(),cmd.getInvoiceFlag());
		if (totalCount == 0)
			return response;

		Integer pageSize = PaginationConfigHelper.getPageSize(
				configurationProvider, cmd.getPageSize());
		int pageCount = getPageCount(totalCount, pageSize);
		checkEnterpriseCommunityIdIsNull(cmd.getEnterpriseCommunityId());
		List<RentalBill> bills = rentalProvider.listRentalBills(
				cmd.getEnterpriseCommunityId(), cmd.getSiteType(),
				cmd.getRentalSiteId(), cmd.getBillStatus(),
				cmd.getPageOffset(), pageSize, cmd.getStartTime(),
				cmd.getEndTime(),cmd.getInvoiceFlag());
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
	
		rentalProvider.deleteRentalBillById(cmd.getRentalBillId());

	}

}
