package com.everhomes.organization.pmsy;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.pmsy.AddressDTO;
import com.everhomes.rest.pmsy.ListPmBillsCommand;
import com.everhomes.rest.pmsy.ListResourceCommand;
import com.everhomes.rest.pmsy.PmBillItemDTO;
import com.everhomes.rest.pmsy.PmsyBillsDTO;
import com.everhomes.util.RuntimeErrorException;
import com.google.gson.Gson;

@Component
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PmsyServiceImpl implements PmsyService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PmsyServiceImpl.class);
	
	
	public List<AddressDTO> listAddresses(ListResourceCommand cmd){
		if(cmd.getPayerId() != null){
			
		}
		List<AddressDTO> resultList = new ArrayList<>();
		String json = PmsyHttpUtil.post("UserRev_OwnerVerify", cmd.getUserName(), cmd.getUserContact(), "", "", "", "", "");
		Gson gson = new Gson();
		Map map = gson.fromJson(json, Map.class);
		System.out.println(map);
		List list = (List) map.get("UserRev_OwnerVerify");
		Map map2 = (Map) list.get(0);
		List list2 = (List) map2.get("Syswin");
		resultList = (List<AddressDTO>) list2.stream().map(r -> {
			Map map3 = (Map) r;
			AddressDTO dto = new AddressDTO();
			dto.setCustomerId((String)map3.get("CusID"));
			dto.setPayerId(cmd.getPayerId());
			dto.setProjectId((String)map3.get("ProjectID"));
			dto.setResourceId((String)map3.get("ResID"));
			StringBuilder resourceName = new StringBuilder();
			resourceName.append(map3.get("ProjectName"))
						.append(" ")
						.append(map3.get("BuildingName"))
						.append(" ")
						.append(map3.get("ResName"));
			dto.setResourceName(resourceName.toString());
			
			System.out.println(map3.get("ProjectID"));
			System.out.println(map3.get("ProjectName"));
			System.out.println(map3.get("CttID"));
			System.out.println(map3.get("CusID"));
			System.out.println(map3.get("ResID"));
			System.out.println(map3.get("ResCode"));
			System.out.println(map3.get("ResName"));
			System.out.println(map3.get("BuildingCode"));
			System.out.println(map3.get("BuildingName"));
			System.out.println(map3.get("IDCardName"));
			System.out.println(map3.get("IDCardNo"));
			System.out.println(map3.get("CusProperty"));
			return dto;
		}).collect(Collectors.toList());
		
		
		
		System.out.println(map);
		return resultList;
	}
	
	public PmsyBillsDTO listPmBillsByConditions(ListPmBillsCommand cmd) {
		if(cmd.getCustomerId() == null){
			LOGGER.error("the id of customer is not null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"the id of customer is not null.");
		}
		if(cmd.getProjectId() == null){
			LOGGER.error("the id of project is not null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"the id of project is not null.");
		}
		if(StringUtils.isBlank(cmd.getBillType())){
			LOGGER.error("the billType is not null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"the billType is not null.");
		}
		PmsyBillsDTO response = new PmsyBillsDTO();
		String startDate = "";
		String endDate = "";
		if(cmd.getStartDate() != null)
			startDate = TimeToString(cmd.getStartDate());
		if(cmd.getEndDate() != null)
			endDate = TimeToString(cmd.getEndDate());
		String json = PmsyHttpUtil.post("UserRev_GetFeeList", cmd.getCustomerId(), startDate,
				endDate, cmd.getProjectId(), cmd.getBillType(), "", "");
		Gson gson = new Gson();
		Map map = gson.fromJson(json, Map.class);
		List list = (List) map.get("UserRev_GetFeeList");
		Map map2 = (Map) list.get(0);
		List list2 = (List) map2.get("Syswin");
		HashSet<Long> calculateMonthSet = new HashSet<Long>();
		BigDecimal totalAmount = new BigDecimal(0);
		
		list2.stream().map(r -> {
			Map map3 = (Map) r;
			PmBillItemDTO dto = new PmBillItemDTO();
			dto.setCustomerId(cmd.getCustomerId());
			dto.setBillId((String)map3.get("ID"));
			Long billDateStr = StringToTime((String)map3.get("RepYears"));
			dto.setBillDateStr(billDateStr);
			dto.setItemName((String)map3.get("IpItemName"));
			BigDecimal priRev = new BigDecimal((String)map3.get("PriRev"));
			BigDecimal lFRev = new BigDecimal((String)map3.get("LFRev"));
			BigDecimal receivableAmount = priRev.add(lFRev);
			dto.setReceivableAmount(receivableAmount);
			BigDecimal priFailures = new BigDecimal((String)map3.get("PriFailures"));
			BigDecimal lFFailures = new BigDecimal((String)map3.get("LFFailures"));
			BigDecimal debtAmount = priFailures.add(lFFailures);
			dto.setDebtAmount(debtAmount);
			calculateMonthSet.add(billDateStr);
			totalAmount.add(debtAmount);
			
			return dto;
		}).collect(Collectors.toList());
		return response;
	}
	
	private String TimeToString(Long time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		return sdf.format(new Date(time));
	}
	
	private Long StringToTime(String s){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		try {
			return sdf.parse(s).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
