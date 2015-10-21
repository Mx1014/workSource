package com.everhomes.techpark.rental;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
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
	RentalProvider rentalProvider;

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
		rentalRule.setPayEndTime(Timestamp.valueOf(cmd.getPayEndTime()));
		rentalRule.setPaymentRatio(cmd.getPayRatio());
		rentalRule.setPayStartTime(Timestamp.valueOf(cmd.getPayStartTime()));
		rentalRule.setRefundFlag(cmd.getRefundFlag());
		rentalRule.setRentalEndTime(Timestamp.valueOf(cmd.getRentalEndTime()));
		rentalRule.setRentalStartTime(Timestamp.valueOf(cmd
				.getRentalStartTime()));
		rentalRule.setOperateTime(new Timestamp(DateHelper.currentGMTTime()
				.getTime()));
		rentalRule.setOperatorUid(userId);
		rentalProvider.updateRentalRule(rentalRule);
		return null;
	}

	@Override
	public void addRentalSite(AddRentalSiteCommand cmd) {
		RentalSite rentalsite = new RentalSite();
		rentalsite.setAddress(cmd.getAddress());
		rentalsite.setBuildingName(cmd.getBuildingName());
		rentalsite.setCompanyId(cmd.getCompanyId());
		rentalsite.setContactPhonenum(cmd.getContactPhonenum());
		rentalsite.setEnterpriseCommunityId(cmd.getEnterpriseCommunityId());
		rentalsite.setOwnUid(cmd.getOwnId());
		rentalsite.setSiteType(cmd.getSiteType());
		rentalsite.setSpec(cmd.getSpec());
		rentalProvider.createRentalSite(rentalsite);

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
		JSONObject jsonObject = (JSONObject) JSONValue.parse(cmd
				.getChoosen());
		JSONArray choosenValue = (JSONArray) jsonObject.get("choosen");
		Gson gson = new Gson();
		List<Integer> choosenInts = gson.fromJson(choosenValue.toString(), new TypeToken<List<Integer>>(){}.getType());
//		 String[] arr = cmd.getChoosen().split(",");
//		 List<String> list = new ArrayList<String>(arr);
		while (start.before(end)) {
			Integer weekday = start.get(Calendar.DAY_OF_WEEK);
			if(choosenInts.contains(weekday))
			{
				for (int i = cmd.getBeginTime(); i < cmd.getEndTime(); i++) {
					rsr.setBeginTime(Timestamp.valueOf(dateSF.format(start.getTime())+" "+String.valueOf(i)+":00:00"));
					rsr.setEndTime(Timestamp.valueOf(dateSF.format(start.getTime())+" "+String.valueOf(i+1)+":00:00"));
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
		// 查 规则 关闭日期的rule ，如果有返回null
		// 查开放具体日期为当天的rule
		// 没有查到，再查每年的rule ，匹配
		// 没有查到，再查每月的rule ，匹配
		// 没有查到，再查每周的rule ，匹配
		// 没有查到，再查每天的rule ，匹配
		// 如果都没查到，返回null

		// 查订单，预定日为当天的
		// 匹配起止时间，总数量减去预定的数量
		// 如果总数量不为0，则商品数量减去预定数量

		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FindRentalSitesCommandResponse findRentalSites(
			FindRentalSitesCommand cmd) {

		Long userId = UserContext.current().getUser().getId();
		checkEnterpriseCommunityIdIsNull(cmd.getEnterpriseCommunityId());
		List<RentalSite> rentalSites = rentalProvider.findRentalSites(
				cmd.getEnterpriseCommunityId(), cmd.getSiteType());
		FindRentalSitesCommandResponse response = new FindRentalSitesCommandResponse();
		response.setRentalSites(new ArrayList<RentalSiteDTO>());
		for (RentalSite rentalSite : rentalSites) {
			RentalSiteDTO rSiteDTO = new RentalSiteDTO();
			rSiteDTO.setRentalSiteId(rentalSite.getId());
			rSiteDTO.setAddress(rentalSite.getAddress());
			rSiteDTO.setBuildingName(rentalSite.getBuildingName());
			rSiteDTO.setCompanyId(rentalSite.getCompanyId());
			rSiteDTO.setContactPhonenum(rentalSite.getContactPhonenum());
			rSiteDTO.setEnterpriseCommunityId(cmd.getEnterpriseCommunityId());
			rSiteDTO.setOwnId(rentalSite.getOwnUid());
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
		return response;
	}

	@Override
	public FindRentalSiteRulesCommandResponse findRentalSiteRules(
			FindRentalSiteRulesCommand cmd) {
		FindRentalSiteRulesCommandResponse response = new FindRentalSiteRulesCommandResponse();
		response.setRentalSiteRules(new ArrayList<RentalSiteRulesDTO>());
		List<RentalSiteRule> rsrs = rentalProvider.findRentalSiteRules(
				cmd.getRentalSiteId(), cmd.getRuleDate(), null);
//		for (RentalSiteRule rsr : rsrs) {
//			RentalSiteRulesDTO dto = new RentalSiteRulesDTO();
//			dto.setId(rsr.getId());
//			dto.setRentalSiteId(rsr.getRentalSiteId());
//			dto.setRentalType(rsr.getRentalType());
//			if (rsr.getRentalType().equals(RentalType.DAY.getCode()))
//				dto.setStepLength(String.valueOf(rsr.getStepLengthDay()));
//			if (rsr.getRentalType().equals(RentalType.DAY.getCode()))
//				dto.setStepLength(timeSF.format(rsr.getStepLengthTime()
//						.getTime()));
//			dto.setBeginTime(timeSF.format(rsr.getBeginTime().getTime()));
//			dto.setEndTime(timeSF.format(rsr.getEndTime().getTime()));
//			dto.setCounts(rsr.getCounts());
//			dto.setUnit(rsr.getUnit());
//			dto.setPrice(rsr.getPrice());
//			if (rsr.getLoopType().equals(LoopType.EVERYDAY.getCode())) {
//
//				dto.setRuleDate(dateSF.format(rsr.getRuleDate()));
//			} else if (rsr.getLoopType().equals(LoopType.EVERYMONTH.getCode())) {
//
//				dto.setRuleDate(dateSF.format(rsr.getRuleDate()));
//			} else if (rsr.getLoopType().equals(LoopType.EVERYWEEK.getCode())) {
//				Calendar calendar = Calendar.getInstance();
//				calendar.setTime(rsr.getRuleDate());
//				dto.setRuleDate(String.valueOf(calendar
//						.get(Calendar.DAY_OF_WEEK)));
//			} else if (rsr.getLoopType().equals(LoopType.EVERYYEAR.getCode())) {
//				dto.setRuleDate(dateSF.format(rsr.getRuleDate()));
//			} else if (rsr.getLoopType().equals(LoopType.ONLYTHEDAY.getCode())) {
//				dto.setRuleDate(dateSF.format(rsr.getRuleDate()));
//			}
//			dto.setLoopType(rsr.getLoopType());
//			dto.setStatus(rsr.getStatus());
//			response.getRentalSiteRules().add(dto);
//		}

		return response;
	}

	@Override
	public void addRentalBill(AddRentalBillCommand cmd) {
		// TODO: add bill
		JSONObject jsonObject = (JSONObject) JSONValue.parse(cmd
				.getRentalItems());
		JSONArray locationValue = (JSONArray) jsonObject.get("rentalItems");
		Gson gson = new Gson();
		// List<PunchGeopoint> geopoints =
		// gson.fromJson(locationValue.toString(),
		// new TypeToken<List<PunchGeopoint>>() {
		// }.getType());
	}

	@Override
	public FindRentalBillsCommandResponse findRentalBills(
			FindRentalBillsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
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
		response.setPayEndTime(datetimeSF.format(rentalRule.getPayEndTime()));
		response.setPayRatio(rentalRule.getPaymentRatio());
		response.setPayStartTime(datetimeSF.format(rentalRule.getPayStartTime()));
		response.setRefundFlag(rentalRule.getRefundFlag());
		response.setRentalEndTime(datetimeSF.format(rentalRule
				.getRentalEndTime()));
		response.setRentalStartTime(datetimeSF.format(rentalRule
				.getRentalStartTime()));
		response.setSiteType("meetingRoom");
		return response;
	}

}
