// @formatter:off
package com.everhomes.socialSecurity;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;

import com.everhomes.rest.socialSecurity.*;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SocialSecurityServiceImpl implements SocialSecurityService {
	@Autowired
	private AccumulationFundBaseProvider accumulationFundBaseProvider;
	@Autowired
	private AccumulationFundPaymentProvider accumulationFundPaymentProvider;
	@Autowired
	private AccumulationFundSettingProvider accumulationFundSettingProvider;
	@Autowired
	private SocialSecurityBaseProvider socialSecurityBaseProvider;
	@Autowired
	private SocialSecurityPaymentProvider socialSecurityPaymentProvider;
	@Autowired
	private SocialSecurityReportProvider socialSecurityReportProvider;
	@Autowired
	private SocialSecuritySettingProvider socialSecuritySettingProvider;
	@Autowired
	private SocialSecuritySummaryProvider socialSecuritySummaryProvider;
	@Autowired
	private SocialSecurityDepartmentSummaryProvider socialSecurityDepartmentSummaryProvider;
	@Autowired
	private SocialSecurityInoutReportProvider socialSecurityInoutReportProvider;
	@Autowired
	private SocialSecurityPaymentLogProvider socialSecurityPaymentLogProvider;
	@Autowired
	private RegionProvider regionProvider;
	@Override
	public void addSocialSecurity(AddSocialSecurityCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ListSocialSecurityCitiesResponse listSocialSecurityCities(
			ListSocialSecurityCitiesCommand cmd) {
		ListSocialSecurityCitiesResponse resp = new ListSocialSecurityCitiesResponse();
		List<Long> cityIds = accumulationFundBaseProvider.listCities();
		resp.setSocialSecurityCitys(processCities(cityIds));
		return resp;
	}

	private List<SocialSecurityCityDTO> processCities(List<Long> cityIds) {
		List<SocialSecurityCityDTO> dtos = new ArrayList<>();
		if (null == cityIds) {
			return null;
		}
		for (Long cityId : cityIds) {
			Region city = regionProvider.findRegionById(cityId);
			if (null != city) {
				SocialSecurityCityDTO dto = new SocialSecurityCityDTO();
				dto.setCityId(cityId);
				dto.setCityName(city.getName());
				dtos.add(dto);
			}
		}
		return dtos;
	}

	@Override
	public ListAccumulationFundCitiesResponse listAccumulationFundCities(
			ListAccumulationFundCitiesCommand cmd) {
		ListAccumulationFundCitiesResponse resp = new ListAccumulationFundCitiesResponse();
		List<Long> cityIds = accumulationFundBaseProvider.listCities();
		resp.setAccumulationFundCitys(processCities(cityIds));
		return resp;
	}

	@Override
	public ListFilterItemsResponse listFilterItems(ListFilterItemsCommand cmd) {
		ListFilterItemsResponse response = new ListFilterItemsResponse();
		List<FilterItemDTO> items = new ArrayList<>();
		FilterItemDTO dto = new FilterItemDTO();
		dto.setId(SocialSecurityItem.SOCIALSECURITYPAY.getCode());
		dto.setItemName(SocialSecurityItem.SOCIALSECURITYPAY.getDescribe());
		items.add(dto);
		dto = new FilterItemDTO();
		dto.setId(SocialSecurityItem.ACCUMULATIONFUNDPAY.getCode());
		dto.setItemName(SocialSecurityItem.ACCUMULATIONFUNDPAY.getDescribe());
		items.add(dto);
		dto = new FilterItemDTO();
		dto.setId(SocialSecurityItem.INCREASE.getCode());
		dto.setItemName(SocialSecurityItem.INCREASE.getDescribe());
		items.add(dto);
		dto = new FilterItemDTO();
		dto.setId(SocialSecurityItem.DECREASE.getCode());
		dto.setItemName(SocialSecurityItem.DECREASE.getDescribe());
		items.add(dto);
		dto = new FilterItemDTO();
		dto.setId(SocialSecurityItem.INWORK.getCode());
		dto.setItemName(SocialSecurityItem.INWORK.getDescribe());
		items.add(dto);
		dto = new FilterItemDTO();
		dto.setId(SocialSecurityItem.OUTWORK.getCode());
		dto.setItemName(SocialSecurityItem.OUTWORK.getDescribe());
		items.add(dto);
		response.setFilterItems(items);
		return response;
	}

	@Override
	public ListSocialSecurityPaymentsResponse listSocialSecurityPayments(
			ListSocialSecurityPaymentsCommand cmd) {
		// TODO 通过组织架构拿到新增人员的detailIds
		List<Long> detailIds = null;
		SsorAfPay payFlag = null;

		if (cmd.getPageAnchor() == null)
			cmd.setPageAnchor(0L);
		int pageSize = cmd.getPageSize() == null ? 10 : cmd.getPageSize();
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());

		List<SocialSecurityPaymentDTO> results = socialSecuritySettingProvider.listSocialSecuritySetting(
				cmd.getSocialSecurityCityId(),cmd.getAccumulationFundCityId(),cmd.getDeptId(),cmd.getKeywords(),
				payFlag,detailIds,locator);
		ListSocialSecurityPaymentsResponse response = new ListSocialSecurityPaymentsResponse();
		if (null == results) {
			return null;
		}
		Long nextPageAnchor = null;
		if (results != null && results.size() > pageSize) {
			results.remove(results.size() - 1);
			nextPageAnchor = results.get(results.size() - 1).getId();
		}
		response.setNextPageAnchor(nextPageAnchor);
		response.setSocialSecurityPayments(results);
		response.setPaymentMonth(socialSecurityPaymentProvider.getPaymentMonth(cmd.getOwnerId()));
		return response;
	}

	@Override
	public ListSocialSecurityEmployeeStatusResponse listSocialSecurityEmployeeStatus(
			ListSocialSecurityEmployeeStatusCommand cmd) {
		// TODO 这个要人事档案提供一些接口
		ListSocialSecurityEmployeeStatusResponse response = new ListSocialSecurityEmployeeStatusResponse();
		return response;
	}

	@Override
	public GetSocialSecurityPaymentDetailsResponse getSocialSecurityPaymentDetails(
			GetSocialSecurityPaymentDetailsCommand cmd) {
		GetSocialSecurityPaymentDetailsResponse response = new GetSocialSecurityPaymentDetailsResponse();
		response.setPaymentMonth(socialSecurityPaymentProvider.getPaymentMonth(cmd.getOwnerId()));
		//社保本月缴费
		List<SocialSecurityPayment> ssPayments = socialSecurityPaymentProvider.listSocialSecurityPayment(
				cmd.getDetailId(), NormalFlag.NO.getCode());
		if (null == ssPayments) {
			response.setPayCurrentSocialSecurityFlag(NormalFlag.NO.getCode());
		}else{
			response.setPayCurrentSocialSecurityFlag(NormalFlag.YES.getCode());
			response.setSocialSecurityPayments(ssPayments.stream().map(r->{
				return processSocialSecurityPaymentDetailDTO(r);
			}).collect(Collectors.toList()));
		}
		//社保补缴
		List<SocialSecurityPayment> ssfaterPayments = socialSecurityPaymentProvider.listSocialSecurityPayment(
				cmd.getDetailId(), NormalFlag.YES.getCode());
		if (null == ssfaterPayments) {
			response.setAfterPaySocialSecurityFlag(NormalFlag.NO.getCode());
		}else{
			response.setAfterPaySocialSecurityFlag(NormalFlag.YES.getCode());
			response.setAfterSocialSecurityPayments(ssfaterPayments.stream().map(r->{
				return processSocialSecurityPaymentDetailDTO(r);
			}).collect(Collectors.toList()));
		}
		//公积金本月缴费
		List<AccumulationFundPayment> afPayments = accumulationFundPaymentProvider.listAccumulationFundPayment(
				cmd.getDetailId(), NormalFlag.NO.getCode());
		if (null == afPayments) {
			response.setPayCurrentAccumulationFundFlag(NormalFlag.NO.getCode());
		}else{
			response.setPayCurrentAccumulationFundFlag(NormalFlag.YES.getCode());
			response.setAccumulationFundPayments(afPayments.stream().map(r->{
				return processSocialSecurityPaymentDetailDTO(r);
			}).collect(Collectors.toList()));
		}
		//公积金补缴
		List<AccumulationFundPayment> afafterPayments = accumulationFundPaymentProvider.listAccumulationFundPayment(
				cmd.getDetailId(), NormalFlag.YES.getCode());
		if (null == afafterPayments) {
			response.setAfterPayAccumulationFundFlag(NormalFlag.NO.getCode());
		}else{
			response.setAfterPayAccumulationFundFlag(NormalFlag.YES.getCode());
			response.setAfterAccumulationFundPayments(afafterPayments.stream().map(r->{
				return processSocialSecurityPaymentDetailDTO(r);
			}).collect(Collectors.toList()));
		}
		return response;
	}

	private SocialSecurityPaymentDetailDTO processSocialSecurityPaymentDetailDTO(AccumulationFundPayment r) {
		SocialSecurityPaymentDetailDTO dto = ConvertHelper.convert(r,SocialSecurityPaymentDetailDTO.class);
		return dto;
	}

	private SocialSecurityPaymentDetailDTO processSocialSecurityPaymentDetailDTO(SocialSecurityPayment r) {
		SocialSecurityPaymentDetailDTO dto = ConvertHelper.convert(r,SocialSecurityPaymentDetailDTO.class);
		return dto;
	}

	@Override
	public void updateSocialSecurityPayment(UpdateSocialSecurityPaymentCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void importSocialSecurityPayments(ImportSocialSecurityPaymentsCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void calculateSocialSecurityReports(CalculateSocialSecurityReportsCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ListSocialSecurityReportsResponse listSocialSecurityReports(
			ListSocialSecurityReportsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void exportSocialSecurityReports(ExportSocialSecurityReportsCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ListSocialSecurityDepartmentSummarysResponse listSocialSecurityDepartmentSummarys(
			ListSocialSecurityDepartmentSummarysCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void exportSocialSecurityDepartmentSummarys(
			ExportSocialSecurityDepartmentSummarysCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ListSocialSecurityInoutReportsResponse listSocialSecurityInoutReports(
			ListSocialSecurityInoutReportsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void exportSocialSecurityInoutReports(ExportSocialSecurityInoutReportsCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fileSocialSecurity(FileSocialSecurityCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ListSocialSecurityHistoryFilesResponse listSocialSecurityHistoryFiles(
			ListSocialSecurityHistoryFilesCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}
  

}