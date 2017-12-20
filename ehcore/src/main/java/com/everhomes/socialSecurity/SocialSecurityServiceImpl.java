// @formatter:off
package com.everhomes.socialSecurity;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
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
	@Autowired
	private OrganizationProvider organizationProvider;
	@Override
	public void addSocialSecurity(AddSocialSecurityCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ListSocialSecurityCitiesResponse listSocialSecurityCities(
			ListSocialSecurityCitiesCommand cmd) {
		ListSocialSecurityCitiesResponse resp = new ListSocialSecurityCitiesResponse();
		List<Long> cityIds = socialSecurityBaseProvider.listCities();
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
		List<Long> cityIds = socialSecurityBaseProvider.listCities();
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
		OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByDetailId(cmd.getDetailId());
		if (null == memberDetail) {
			return response;
		}
		response.setDetailId(memberDetail.getId());
		response.setUserName(memberDetail.getContactName());
		response.setSocialSecurityNo(memberDetail.getSocialSecurityNumber());
		//社保本月缴费
		List<SocialSecurityPayment> allPayments = socialSecurityPaymentProvider.listSocialSecurityPayment(cmd.getDetailId() );
		List<SocialSecurityPayment> ssPayments = new ArrayList<>();
		List<SocialSecurityPayment> ssafterPayments = new ArrayList<>();
		List<SocialSecurityPayment> afPayments = new ArrayList<>();
		List<SocialSecurityPayment> afafterPayments = new ArrayList<>();
		for (SocialSecurityPayment payment : allPayments) {
			if (AccumOrSocail.fromCode(payment.getAccumOrSocail()) == AccumOrSocail.ACCUM) {
				if (NormalFlag.fromCode(payment.getAfterPayFlag()) == NormalFlag.NO) {
					afPayments.add(payment);
				}else{
					afafterPayments.add(payment);
				}
			}else{
				if (NormalFlag.fromCode(payment.getAfterPayFlag()) == NormalFlag.NO) {
					ssPayments.add(payment);
				}else{
					ssafterPayments.add(payment);
				}
			}
		}
		if ( ssPayments.size()==0) {
			response.setPayCurrentSocialSecurityFlag(NormalFlag.NO.getCode());
		}else{
			response.setPayCurrentSocialSecurityFlag(NormalFlag.YES.getCode());
			response.setSocialSecurityPayment(processSocialSecurityPaymentDetailDTO(ssPayments));
		}
		//社保补缴
		if (ssafterPayments.size()==0) {
			response.setAfterPaySocialSecurityFlag(NormalFlag.NO.getCode());
		}else{
			response.setAfterPaySocialSecurityFlag(NormalFlag.YES.getCode());
			response.setAfterSocialSecurityPayment(processSocialSecurityPaymentDetailDTO(ssafterPayments));
		}
		//公积金本月缴费
		if (afPayments.size()==0) {
			response.setPayCurrentAccumulationFundFlag(NormalFlag.NO.getCode());
		}else{
			response.setPayCurrentAccumulationFundFlag(NormalFlag.YES.getCode());
			response.setAccumulationFundPayment(processSocialSecurityPaymentDetailDTO(afPayments));
		}
		//公积金补缴
		if (afafterPayments.size()==0) {
			response.setAfterPayAccumulationFundFlag(NormalFlag.NO.getCode());
		}else{
			response.setAfterPayAccumulationFundFlag(NormalFlag.YES.getCode());
			response.setAfterAccumulationFundPayment(processSocialSecurityPaymentDetailDTO(afafterPayments));
		}
		return response;
	}

//	private SocialSecurityPaymentDetailDTO processSocialSecurityPaymentDetailDTO(List<AccumulationFundPayment> r) {
//		SocialSecurityPaymentDetailDTO dto = ConvertHelper.convert(r,SocialSecurityPaymentDetailDTO.class);
//		return dto;
//	}

	private SocialSecurityPaymentDetailDTO processSocialSecurityPaymentDetailDTO(List<SocialSecurityPayment> payments) {
		SocialSecurityPaymentDetailDTO dto = new SocialSecurityPaymentDetailDTO();
		dto.setCityId(dto.getCityId());
		Region city = regionProvider.findRegionById(dto.getCityId());
		if (null != city) dto.setCityName(city.getName());
		dto.setHouseholdType(payments.get(0).getHouseholdType());
		dto.setItems(payments.stream().map(r->{
			return processSocialSecurityItemDTO(r);
		}).collect(Collectors.toList()));
		return dto;
	}

	private SocialSecurityItemDTO processSocialSecurityItemDTO(SocialSecurityPayment r) {
		return ConvertHelper.convert(r, SocialSecurityItemDTO.class);
	}

	@Override
	public void updateSocialSecurityPayment(UpdateSocialSecurityPaymentCommand cmd) {
		// 查询设置的城市户籍档次的数据规则
		// 校验数据是否合法
		// 保存setting表数据
		// 保存当月payments数据

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
  

	@Override
	public ListUserInoutHistoryResponse listUserInoutHistory(ListUserInoutHistoryCommand cmd) {
	
		return new ListUserInoutHistoryResponse();
	}

	@Override
	public ListSocialSecurityHouseholdTypesResponse listSocialSecurityHouseholdTypes(ListSocialSecurityHouseholdTypesCommand cmd) {
	
		return new ListSocialSecurityHouseholdTypesResponse();
	}

	@Override
	public ListAccumulationFundHouseholdTypesResponse listAccumulationFundHouseholdTypes(ListAccumulationFundHouseholdTypesCommand cmd) {
	
		return new ListAccumulationFundHouseholdTypesResponse();
	}

	@Override
	public GetSocialSecurityReportStatusResponse getSocialSecurityReportStatus(GetSocialSecurityReportStatusCommand cmd) {
	
		return new GetSocialSecurityReportStatusResponse();
	}

}