package com.everhomes.techpark.park;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.techpark.onlinePay.OnlinePayProvider;
import com.everhomes.techpark.onlinePay.PayStatus;
import com.everhomes.techpark.onlinePay.RechargeStatus;
import com.everhomes.user.IdentifierType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.RuntimeErrorException;


@Component
public class ParkServiceImpl implements ParkService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ParkServiceImpl.class);

	@Autowired
	private ParkProvider parkProvider;
	
	@Autowired
	private ConfigurationProvider configurationProvider;
	
	@Autowired
	private UserProvider userProvider;

	@Autowired
	private OnlinePayProvider onlinePayProvider;
	
	@Autowired
    private ConfigurationProvider configProvider;
	
	@Override
	public void addCharge(CreateParkingChargeCommand cmd) {
		User user = UserContext.current().getUser();
//		Long parkId = user.getCommunityId();
		
		ParkCharge parkCharge = new ParkCharge();
		parkCharge.setMonths(cmd.getMonths());
		parkCharge.setAmount(cmd.getAmount());
		parkCharge.setEnterpriseCommunityId(233L);
		
		parkProvider.addCharge(parkCharge);
		
	}

	@Override
	public void deleteCharge(DeleteParkingChargeCommand cmd) {
		
		User user = UserContext.current().getUser();
//		Long parkId = user.getCommunityId();
		
		ParkCharge parkCharge = new ParkCharge();
		parkCharge.setId(cmd.getId());
		parkCharge.setMonths(cmd.getMonths());
		parkCharge.setAmount(cmd.getAmount());
		parkCharge.setEnterpriseCommunityId(233L);
		
		parkProvider.deleteCharge(parkCharge);
	}

	@Override
	public ParkResponseList listParkingCharge(ParkResponseListCommand cmd) {
		
		User user = UserContext.current().getUser();
//		Long parkId = user.getCommunityId();
		
		Integer pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		int pageOffset = cmd.getPageOffset() == null ? 1: cmd.getPageOffset();
        int offset = (int) PaginationHelper.offsetFromPageOffset((long) pageOffset, pageSize);
        
		ParkResponseList response = new ParkResponseList();
		List<ParkCharge> parkCharge = parkProvider.listParkingChargeByEnterpriseCommunityId(233L, offset, pageSize);
		
		if(parkCharge != null && parkCharge.size() == pageSize){
			response.setNextPageOffset(pageOffset + 1);
		}
		
		response.setParkingCharge(parkCharge.stream().map(r->{
			ParkingChargeDTO dto = ConvertHelper.convert(r, ParkingChargeDTO.class);
			return dto;
		}).collect(Collectors.toList()));
		
		return response;
	}

	@Override
	public RechargeOrderDTO createRechargeOrder(CreateRechargeOrderCommand cmd) {
		
		if(cmd.getAmount() == null || cmd.getPlateNumber() == null){
			LOGGER.error("amount or plateNumber is null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"amount or plateNumber is null.");
		}
		
		User user = UserContext.current().getUser();
		List<UserIdentifier> identifiers = this.userProvider.listUserIdentifiersOfUser(user.getId());
        List<String> phones = identifiers.stream().filter((r)-> { return IdentifierType.fromCode(r.getIdentifierType()) == IdentifierType.MOBILE; })
            .map((r) -> { return r.getIdentifierToken(); })
            .collect(Collectors.toList());
        
		RechargeInfo order = new RechargeInfo();
		if(phones != null)
			order.setRechargePhone(phones.get(0));
		order.setNumberType((byte) 0);
		order.setOwnerName(cmd.getOwnerName());
		order.setPlateNumber(cmd.getPlateNumber());
		order.setRechargeAmount(cmd.getAmount());
		order.setRechargeMonth(cmd.getMonths());
		order.setRechargeUserid(user.getId());
		order.setRechargeUsername(user.getNickName());
		Long time = System.currentTimeMillis();
		order.setRechargeTime(new Timestamp(time));
		String bill = String.valueOf(time)+String.valueOf(order.getRechargeAmount());
		order.setBillId(Long.valueOf(bill));
		order.setPaymentStatus(PayStatus.WAITING_FOR_PAY.getCode());
		order.setRechargeStatus(RechargeStatus.HANDING.getCode());
		order.setEnterpriseCommunityId(233L);
		order.setOldValidityperiod(strToTimestamp(cmd.getValidityPeriod()));
		order.setNewValidityperiod(addMonth(cmd.getValidityPeriod(), cmd.getMonths()));;
		
		this.parkProvider.createRechargeOrder(order);
		
		RechargeOrderDTO dto = new RechargeOrderDTO();
		dto.setBillId(order.getBillId());
		dto.setAmount(order.getRechargeAmount());
		return dto;
	}
	
	private Timestamp strToTimestamp(String str) {

		long l = Long.valueOf(str);
		Timestamp time = new Timestamp(l);
		
		return time;
	}
	
	private Timestamp addMonth(String oldPeriod, int month) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(strToTimestamp(oldPeriod));
		calendar.add(Calendar.MONTH, month);
		Timestamp newPeriod = new Timestamp(calendar.getTimeInMillis());
		
		return newPeriod;
	}

	@Override
	public RechargeSuccessResponse getRechargeStatus(RechargeResultSearchCommand cmd) {
		
		RechargeInfo rechargeInfo = onlinePayProvider.findRechargeInfoByOrderId(cmd.getBillId());
		
		RechargeSuccessResponse rechargeResponse = new RechargeSuccessResponse();
		rechargeResponse.setBillId(rechargeInfo.getBillId());
		rechargeResponse.setPayStatus(rechargeInfo.getPaymentStatus());
		rechargeResponse.setRechargeStatus(rechargeInfo.getRechargeStatus());
		if(rechargeInfo.getRechargeStatus().byteValue() == RechargeStatus.SUCCESS.getCode()) {
			rechargeResponse.setValidityPeriod(rechargeInfo.getNewValidityperiod());
		}
		else {
			rechargeResponse.setValidityPeriod(rechargeInfo.getOldValidityperiod());
		}
		return rechargeResponse;
	}

	@Override
	public RechargeRecordList listRechargeRecord(RechargeRecordListCommand cmd) {
		
		User user = UserContext.current().getUser();
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor() == null ? 0L : cmd.getPageAnchor());
		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		
		List<RechargeInfo> recordInfo = parkProvider.listRechargeRecord(233L, user.getId(), locator, pageSize + 1);
		List<RechargeRecordDTO> rechargeRecord = new ArrayList<RechargeRecordDTO>();
		
		recordInfo.forEach(record -> {
			RechargeRecordDTO r = ConvertHelper.convert(record, RechargeRecordDTO.class);
			if(r.getRechargeStatus().byteValue() == RechargeStatus.SUCCESS.getCode()) {
				r.setValidityperiod(record.getNewValidityperiod());
			} else {
				r.setValidityperiod(record.getOldValidityperiod());
			}
			rechargeRecord.add(r);
		});
		
		RechargeRecordList list = new RechargeRecordList();
		
		if(recordInfo.size() > pageSize) {
			list.setRechargeRecord(rechargeRecord.subList(0, rechargeRecord.size()-1));
			list.setNextPageAnchor(locator.getAnchor());
		} else {
			list.setRechargeRecord(rechargeRecord);
			list.setNextPageAnchor(null);
		}
		return list;
	}
}
