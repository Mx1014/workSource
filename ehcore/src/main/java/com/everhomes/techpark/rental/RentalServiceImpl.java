package com.everhomes.techpark.rental;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.naming.java.javaURLContextFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
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
		FindRentalSiteDayStatusCommandResponse response = new FindRentalSiteDayStatusCommandResponse();
		response.setSites(new ArrayList<RentalSiteDTO>());
		RentalRule rentalRule = rentalProvider.getRentalRule(
				cmd.getEnterpriseCommunityId(), cmd.getSiteType());
		// 查sites
		List<RentalSite> rentalSites = rentalProvider.findRentalSites(
				cmd.getEnterpriseCommunityId(), cmd.getSiteType(),null);
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
		int totalCount = rentalProvider.countRentalSites(cmd.getEnterpriseCommunityId(),cmd.getSiteType(),cmd.getKeyword());
		if (totalCount == 0)
			return response;

		Integer pageSize = PaginationConfigHelper.getPageSize(
				configurationProvider, cmd.getPageSize());
		int pageCount = getPageCount(totalCount, pageSize);
		
		checkEnterpriseCommunityIdIsNull(cmd.getEnterpriseCommunityId());
		List<RentalSite> rentalSites = rentalProvider.findRentalSites(
				cmd.getEnterpriseCommunityId(), cmd.getSiteType(),cmd.getKeyword());
		
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
	public void addRentalBill(AddRentalBillCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		java.util.Date reserveTime = new java.util.Date(); 
		List<RentalSiteRule> rentalSiteRules = new ArrayList<RentalSiteRule>();
		RentalRule rentalRule = rentalProvider.getRentalRule(
				cmd.getEnterpriseCommunityId(), cmd.getSiteType());
		if (reserveTime.before(new java.util.Date(cmd.getStartTime()
				- rentalRule.getRentalStartTime()))) {
			LOGGER.error("reserve Time before reserve start time");
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_RESERVE_TOO_EARLY,
					"reserve Time before reserve start time");
		}
		if (reserveTime.after(new java.util.Date(cmd.getStartTime()
				- rentalRule.getRentalEndTime()))) {
			LOGGER.error("reserve Time after reserve end time");
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_RESERVE_TOO_LATE,
					"reserve Time after reserve end time");
		}
		RentalBill rentalBill = new RentalBill();
		rentalBill.setEnterpriseCommunityId(cmd.getEnterpriseCommunityId());
		rentalBill.setSiteType(cmd.getSiteType());
		rentalBill.setRentalSiteId(cmd.getRentalSiteId());
		rentalBill.setRentalUid(userId);
		rentalBill.setInvoiceFlag(cmd.getInvoiceFlag());
		rentalBill.setRentalDate(new Date(cmd.getRentalDate()));
		rentalBill.setStartTime(new Timestamp(cmd.getStartTime()));
		rentalBill.setEndTime(new Timestamp(cmd.getEndTime()));
		rentalBill.setRentalCount(cmd.getRentalcount());
		int totalMoney = 0;
		for (Long siteRuleId : cmd.getRentalSiteRuleIds()) {
			RentalSiteRule rentalSiteRule = rentalProvider
					.findRentalSiteRuleById(siteRuleId);
			rentalSiteRules.add(rentalSiteRule);
			totalMoney += rentalSiteRule.getPrice()
					* (int) (cmd.getRentalcount() / rentalSiteRule.getUnit());
		}

		for (SiteItemDTO siDto : cmd.getRentalItems()) {
			totalMoney += siDto.getItemPrice() * siDto.getCounts();
		}
		rentalBill.setPayTatolMoney(totalMoney);
		rentalBill.setReserveMoney(totalMoney * rentalRule.getPaymentRatio()
				/ 100);
		rentalBill.setReserveTime(Timestamp.valueOf(datetimeSF
				.format(reserveTime)));
		rentalBill.setPayStartTime(new Timestamp(cmd.getStartTime()
				- rentalRule.getPayStartTime()));
		rentalBill.setPayEndTime(new Timestamp(cmd.getStartTime()
				- rentalRule.getPayEndTime()));
		rentalBill.setPaidMoney(0);
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
		Long rentalBillId = rentalProvider.createRentalBill(rentalBill);
		// 循环存物品订单
		for (SiteItemDTO siDto : cmd.getRentalItems()) {
			RentalItemsBill rib = new RentalItemsBill();
			rib.setTotalMoney(siDto.getItemPrice() * siDto.getCounts());

			rib.setEnterpriseCommunityId(cmd.getEnterpriseCommunityId());
			rib.setSiteType(cmd.getSiteType());
			rib.setRentalSiteItemId(siDto.getId());
			rib.setRentalCount(siDto.getCounts());
			rib.setRentalBillId(rentalBillId);
			rib.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			rib.setCreatorUid(userId);
			rentalProvider.createRentalItemBill(rib);
		}
		// 循环存site订单
		for (RentalSiteRule rsr : rentalSiteRules) {
			RentalSitesBill rsb = new RentalSitesBill();
			rsb.setEnterpriseCommunityId(cmd.getEnterpriseCommunityId());
			rsb.setSiteType(cmd.getSiteType());
			rsb.setRentalBillId(rentalBillId);
			rsb.setTotalMoney(rsr.getPrice()
					* (int) (cmd.getRentalcount() / rsr.getUnit()));
			rsb.setRentalCount(cmd.getRentalcount());
			rsb.setRentalSiteRuleId(rsr.getId());
			rsb.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			rsb.setCreatorUid(userId);
			rentalProvider.createRentalSiteBill(rsb);
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
		List<RentalBill> billList = this.rentalProvider.listRentalBills(userId,cmd.getEnterpriseCommunityId(),cmd.getSiteType(),
				locator, pageSize + 1, cmd.getBillStatus());
		FindRentalBillsCommandResponse response = new FindRentalBillsCommandResponse();
		response.setRentalBills(new ArrayList<RentalBillDTO>());
		for (RentalBill bill : billList) {
			RentalSite rs = rentalProvider.getRentalSiteById(bill
					.getRentalSiteId());
			RentalBillDTO dto = new RentalBillDTO();
			dto.setRentalBillId(bill.getId());
			dto.setEnterpriseCommunityId(rs.getEnterpriseCommunityId());
			dto.setSiteType(rs.getSiteType());
			dto.setSiteName(rs.getSiteName());
			dto.setBuildingName(rs.getBuildingName());
			dto.setAddress(rs.getAddress());
			dto.setSpec(rs.getSpec());
			dto.setCompanyName(rs.getOwnCompanyName());
			dto.setContactName(rs.getContactName());
			dto.setContactPhonenum(rs.getContactPhonenum());
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
			Integer sitePrice = rentalProvider.getSumSitePrice(dto
					.getRentalBillId());
			dto.setSitePrice(sitePrice);
			dto.setTotalPrice(bill.getPayTatolMoney());
			dto.setReservePrice(bill.getReserveMoney());
			dto.setPaidPrice(bill.getPaidMoney());
			dto.setUnPayPrice(bill.getPayTatolMoney() - bill.getPaidMoney());
			dto.setInvoiceFlag(bill.getInvoiceFlag());
			dto.setStatus(bill.getStatus());
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
					"HAS BILL IN YOUR DELETE STUFF");
		}
		rentalProvider.updateRentalSiteStatus(cmd.getRentalSiteId(),RentalSiteStatus.DELETED.getCode());
	}
	@Override
	public void deleteRentalSiteItem(DeleteRentalSiteItemCommand cmd) { 
		Integer billCount = rentalProvider.countRentalSiteItemBills(cmd.getRentalSiteItemId());
		if (billCount > 0) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_HAVE_BILL,
					"HAS BILL IN YOUR DELETE STUFF");
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

}
