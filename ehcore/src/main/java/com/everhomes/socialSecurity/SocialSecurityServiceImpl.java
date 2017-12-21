// @formatter:off
package com.everhomes.socialSecurity;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.*;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;

import com.everhomes.rest.socialSecurity.*;
import com.everhomes.techpark.punch.PunchConstants;
import com.everhomes.techpark.punch.PunchServiceImpl;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
	@Autowired
	private CommunityProvider communityProvider;
	@Autowired
	private BigCollectionProvider bigCollectionProvider;
	@Autowired
	private ConfigurationProvider configurationProvider;

	private static ThreadLocal<SimpleDateFormat> monthSF = new ThreadLocal<SimpleDateFormat>(){
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyyMM");
		}
	};
	private static final Logger LOGGER = LoggerFactory.getLogger(SocialSecurityServiceImpl.class);
	@Override
	public void addSocialSecurity(AddSocialSecurityCommand cmd) {
		// TODO Auto-generated method stub
		addSocialSecurity(cmd.getOwnerId());
	}
	/**添加某公司的新一期社保缴费*/
	private void addSocialSecurity(Long ownerId) {
		String paymentMonth = socialSecurityPaymentProvider.findPaymentMonthByOwnerId(ownerId);
		if (null == paymentMonth) {
			newSocialSecurityOrg(ownerId);
		}else{
			checkSocialSercurityFiled(ownerId);
			deleteOldMonthPayments(ownerId);
			try {
				Calendar month = Calendar.getInstance();
				month.setTime(monthSF.get().parse(paymentMonth));
				month.add(Calendar.MONTH, 1);
				paymentMonth = monthSF.get().format(month.getTime());
				addNewMonthPayments(ownerId,paymentMonth);
			} catch (ParseException e) {
				e.printStackTrace();
				LOGGER.error("payment month is wrong  "+paymentMonth,e);
			}

		}
	}

	private void deleteOldMonthPayments(Long ownerId) {
		List<SocialSecurityPayment> payments = socialSecurityPaymentProvider.listSocialSecurityPayment(ownerId);
		for (SocialSecurityPayment payment : payments) {
			SocialSecurityPaymentLog paymentLog = ConvertHelper.convert(payment, SocialSecurityPaymentLog.class);
			socialSecurityPaymentLogProvider.createSocialSecurityPaymentLog(paymentLog);
		}
	}

	private void newSocialSecurityOrg(Long ownerId) {
		Organization org = organizationProvider.findOrganizationById(ownerId);
		OrganizationCommunity organizationCommunity = organizationProvider.findOrganizationCommunityByOrgId(ownerId);
		Community community = communityProvider.findCommunityById(organizationCommunity.getCommunityId());
		Long cityId = getZuolinNamespaceCityId(community.getCityId());
		List<HouseholdTypesDTO> hTs = socialSecurityBaseProvider.listHouseholdTypesByCity(cityId);
		addNewSocialSecuritySettings(cityId, hTs.get(0).getHouseholdTypeName(), ownerId);
		String paymentMonth = monthSF.get().format(DateHelper.currentGMTTime());
		addNewMonthPayments(ownerId,paymentMonth);
	}

	private void addNewMonthPayments(Long ownerId, String paymentMonth) {
		//把属于该公司的所有要交社保的setting取出来
		List<Long> detailIds = null;

		List<SocialSecuritySetting> settings = socialSecuritySettingProvider.listSocialSecuritySetting(detailIds);
		for (SocialSecuritySetting setting : settings) {
			SocialSecurityPayment payment = processSocialSecurityPayment(setting, paymentMonth, NormalFlag.NO.getCode());
			socialSecurityPaymentProvider.createSocialSecurityPayment(payment);
		}
	}

	private Long getZuolinNamespaceCityId(Long cityId) {
		Region currentNSCity = regionProvider.findRegionById(cityId);
		Region zuolinCity = regionProvider.findRegionByName(0, currentNSCity.getName());
		if (null == zuolinCity) {
			return SocialSecurityConstants.DEFAULT_CITY;
		}
		return zuolinCity.getId();
	}

	private void addNewSocialSecuritySettings(Long cityId, String householdTypeName,Long ownerId) {

		List<SocialSecurityBase> ssBases = socialSecurityBaseProvider.listSocialSecurityBase(cityId,
				householdTypeName, AccumOrSocail.SOCAIL.getCode());
		List<SocialSecurityBase> afBases = socialSecurityBaseProvider.listSocialSecurityBase(cityId,
				null, AccumOrSocail.ACCUM.getCode());
		List<OrganizationMemberDetails> details = organizationProvider.listOrganizationMemberDetails(ownerId);
		for (OrganizationMemberDetails detail : details) {
			saveSocialSecuritySettings(ssBases,cityId,ownerId,detail.getTargetId(),detail.getId(),detail.getNamespaceId());
			saveSocialSecuritySettings(afBases,cityId,ownerId,detail.getTargetId(),detail.getId(),detail.getNamespaceId());
		}

	}

	private void saveSocialSecuritySettings(List<SocialSecurityBase> bases, Long cityId, Long orgId, Long userId,
											Long detailId, Integer namespaceId) {
		if (null != bases) {
			for (SocialSecurityBase base : bases) {
				SocialSecuritySetting setting = processSocialSecuritySetting(base, cityId, orgId, userId,
						detailId, namespaceId);
				socialSecuritySettingProvider.createSocialSecuritySetting(setting);
			}
		}
	}

	/**
	 * 检查归档
	 */
	private void checkSocialSercurityFiled(Long ownerId) {
		Integer unFiledNum = socialSecurityPaymentProvider.countUnFieldUsers(ownerId);
		if (unFiledNum >= 1) {
			throw RuntimeErrorException.errorWith(SocialSecurityConstants.SCOPE, SocialSecurityConstants.ERROR_HAS_UNFILED,
					"还有未归档!");
		}
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
		response.setPaymentMonth(socialSecurityPaymentProvider.findPaymentMonthByOwnerId(cmd.getOwnerId()));
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
		response.setPaymentMonth(socialSecurityPaymentProvider.findPaymentMonthByOwnerId(cmd.getOwnerId()));
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
		//社保设置
		if (NormalFlag.fromCode(cmd.getPayCurrentSocialSecurityFlag()) == NormalFlag.YES) {
			// 查询设置的城市户籍档次的数据规则
			List<SocialSecurityBase> bases = socialSecurityBaseProvider.listSocialSecurityBase(cmd.getOwnerId(), cmd.getHouseholdType());
			// 校验数据是否合法
			checkSocialSercurity(bases, cmd.getSocialSecurityPayment().getItems());
			// 保存setting表数据
			saveSocialSecuritySettings(cmd.getSocialSecurityPayment(),cmd.getDetailId(),AccumOrSocail.SOCAIL.getCode());
			// 保存当月payments数据
			saveSocialSecurityPayment(cmd.getSocialSecurityPayment(),cmd.getDetailId(), NormalFlag.NO.getCode(), AccumOrSocail.SOCAIL.getCode());
			if (NormalFlag.fromCode(cmd.getAfterPaySocialSecurityFlag()) == NormalFlag.YES) {
				saveSocialSecurityPayment(cmd.getAfterSocialSecurityPayment(), cmd.getDetailId(), NormalFlag.YES.getCode(), AccumOrSocail.SOCAIL.getCode());
			}
		}
		//公积金设置
		if (NormalFlag.fromCode(cmd.getPayCurrentAccumulationFundFlag()) == NormalFlag.YES) {
			// 查询设置的城市户籍档次的数据规则
			List<SocialSecurityBase> bases = socialSecurityBaseProvider.listSocialSecurityBase(cmd.getOwnerId(),AccumOrSocail.ACCUM.getCode());
			// 校验数据是否合法
			checkSocialSercurity(bases, cmd.getAccumulationFundPayment().getItems());
			// 保存setting表数据
			saveSocialSecuritySettings(cmd.getAccumulationFundPayment(), cmd.getDetailId(),AccumOrSocail.ACCUM.getCode());
			// 保存当月payments数据
			saveSocialSecurityPayment(cmd.getAccumulationFundPayment(), cmd.getDetailId(), NormalFlag.NO.getCode(), AccumOrSocail.ACCUM.getCode());
			if (NormalFlag.fromCode(cmd.getAfterPaySocialSecurityFlag()) == NormalFlag.YES) {
				saveSocialSecurityPayment(cmd.getAfterSocialSecurityPayment(), cmd.getDetailId(), NormalFlag.YES.getCode(), AccumOrSocail.ACCUM.getCode());
			}
		}
	}

	private void saveSocialSecurityPayment(SocialSecurityPaymentDetailDTO socialSecurityPayment, Long detailId, Byte afterPay, Byte accumOrSocial) {
		String paymentMonth = socialSecurityPaymentProvider.findPaymentMonth(detailId);
		socialSecurityPaymentProvider.setUserCityAndHTByAccumOrSocial(detailId, accumOrSocial, socialSecurityPayment.getCityId(), socialSecurityPayment.getHouseholdType());
		for (SocialSecurityItemDTO itemDTO : socialSecurityPayment.getItems()) {
			SocialSecurityPayment payment = socialSecurityPaymentProvider.findSocialSecurityPayment(detailId, itemDTO.getPayItem(), accumOrSocial);
			if (null == payment) {
				createSocialSecurityPayment(itemDTO, detailId, accumOrSocial, socialSecurityPayment,paymentMonth,afterPay);
			} else {
				copyRadixAndRatio(payment, socialSecurityPayment, itemDTO);
				socialSecurityPaymentProvider.updateSocialSecurityPayment(payment);
			}
		}
	}

	private void createSocialSecurityPayment(SocialSecurityItemDTO itemDTO, Long detailId, Byte accumOrSocial, SocialSecurityPaymentDetailDTO socialSecurityPayment, String paymentMonth, Byte afterPay) {
		SocialSecuritySetting setting = socialSecuritySettingProvider.findSocialSecuritySettingByDetailIdAndItem(detailId, itemDTO, accumOrSocial);
		SocialSecurityPayment payment = processSocialSecurityPayment(setting,paymentMonth,afterPay);
		copyRadixAndRatio(payment, socialSecurityPayment, itemDTO);
		socialSecurityPaymentProvider.createSocialSecurityPayment(payment);

	}

	private SocialSecurityPayment processSocialSecurityPayment(SocialSecuritySetting setting, String paymentMonth, Byte afterPay) {
		SocialSecurityPayment payment = ConvertHelper.convert(setting, SocialSecurityPayment.class);
		payment.setPayMonth(paymentMonth);
		payment.setAfterPayFlag(afterPay);
		return payment;
	}

	private void copyRadixAndRatio(SocialSecurityPayment target, SocialSecurityPaymentDetailDTO socialSecurityPayment, SocialSecurityItemDTO itemDTO) {
		target.setCompanyRadix(itemDTO.getCompanyRadix());
		target.setEmployeeRadix(itemDTO.getEmployeeRadix());
		target.setCompanyRatio(itemDTO.getCompanyRatio());
		target.setEmployeeRatio(itemDTO.getEmployeeRatio());
	}
	private void saveSocialSecuritySettings(SocialSecurityPaymentDetailDTO socialSecurityPayment, Long detailId, Byte accumOrSocial) {
		socialSecuritySettingProvider.setUserCityAndHTByAccumOrSocial(detailId, accumOrSocial, socialSecurityPayment.getCityId(), socialSecurityPayment.getHouseholdType());
		for (SocialSecurityItemDTO itemDTO : socialSecurityPayment.getItems()) {
			SocialSecuritySetting setting = socialSecuritySettingProvider.findSocialSecuritySettingByDetailIdAndItem(detailId, itemDTO, accumOrSocial);
			if (null == setting) {
				createSocialSecuritySetting(itemDTO, detailId, accumOrSocial,socialSecurityPayment);
			}else{
				copyRadixAndRatio(setting, socialSecurityPayment, itemDTO);
				socialSecuritySettingProvider.updateSocialSecuritySetting(setting);
			}
		}
	}

	private void copyRadixAndRatio(SocialSecuritySetting setting, SocialSecurityPaymentDetailDTO socialSecurityPayment, SocialSecurityItemDTO itemDTO) {
		setting.setRadix(socialSecurityPayment.getRadix());
		setting.setCompanyRadix(itemDTO.getCompanyRadix());
		setting.setEmployeeRadix(itemDTO.getEmployeeRadix());
		setting.setCompanyRatio(itemDTO.getCompanyRatio());
		setting.setEmployeeRatio(itemDTO.getEmployeeRatio());
	}

	private void createSocialSecuritySetting(SocialSecurityItemDTO itemDTO, Long detailId, Byte accumOrSocial, SocialSecurityPaymentDetailDTO socialSecurityPayment) {
		SocialSecurityBase base = socialSecurityBaseProvider.findSocialSecurityBaseByCondition(socialSecurityPayment.getCityId(), socialSecurityPayment.getHouseholdType(),
				accumOrSocial, itemDTO.getPayItem());
		OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
		SocialSecuritySetting setting = processSocialSecuritySetting(base,socialSecurityPayment.getCityId(),
				detail.getOrganizationId(),detail.getTargetId(),detail.getId(),detail.getNamespaceId());
		copyRadixAndRatio(setting, socialSecurityPayment, itemDTO);
		socialSecuritySettingProvider.createSocialSecuritySetting(setting);

	}

	private SocialSecuritySetting processSocialSecuritySetting(SocialSecurityBase base, Long cityId, Long orgId, Long userId,
															   Long detailId, Integer namespaceId) {
		SocialSecuritySetting setting = ConvertHelper.convert(base, SocialSecuritySetting.class);
		setting.setCityId(cityId);
		setting.setOrganizationId(orgId);
		setting.setUserId(userId);
		setting.setDetailId(detailId);
		setting.setNamespaceId(namespaceId);
		setting.setCompanyRadix(base.getCompanyRadixMin());
		setting.setEmployeeRadix(base.getEmployeeRadixMin());
		setting.setCompanyRatio(base.getCompanyRatioMin());
		setting.setEmployeeRatio(base.getEmployeeRatioMin());
		return setting;
	}

	private void checkSocialSercurity(List<SocialSecurityBase> bases, List<SocialSecurityItemDTO> items) {
		//没找到社保或者公积金规则是不对的
		if (null == bases) {
			throw RuntimeErrorException.errorWith(SocialSecurityConstants.SCOPE, SocialSecurityConstants.ERROR_CHECK_SETTING,
					"校验不通过 没有找到基础数据");
		}
		if (bases.size() == 1 && items.size() == 0) {
			//只有一条就是公积金了
			SocialSecurityBase base = bases.get(0);
			checkSocialSercurity(base, items.get(0));
		}else{
			for (SocialSecurityItemDTO itemDTO : items) {
				//对于补充保险不校验
				if (NormalFlag.fromCode(itemDTO.getIsDefault()) == NormalFlag.NO) {
					continue;
				}
				SocialSecurityBase base = findSSBase(bases, itemDTO);
				checkSocialSercurity(base, itemDTO);
			}
		}
	}

	private void checkSocialSercurity(SocialSecurityBase base, SocialSecurityItemDTO itemDTO) {
		//检测企业基数边界
		if (base.getCompanyRadixMax().compareTo(itemDTO.getCompanyRadix()) < 0 ||
				base.getCompanyRadixMin().compareTo(itemDTO.getCompanyRadix()) > 0) {
			throw RuntimeErrorException.errorWith(SocialSecurityConstants.SCOPE, SocialSecurityConstants.ERROR_CHECK_SETTING,
					String.format("校验不通过 [{}]的企业基数越界 最大值[{}] 最小值[{}] 实际[{}]",
							itemDTO.getPayItem()==null?"公积金":itemDTO.getPayItem(),base.getCompanyRadixMax(),
							base.getCompanyRadixMin(),itemDTO.getCompanyRadix()));
		}
		//检测企业比例边界
		if (base.getCompanyRatioMax()< itemDTO.getCompanyRatio() ||
				base.getCompanyRatioMin()>itemDTO.getCompanyRatio()) {
			throw RuntimeErrorException.errorWith(SocialSecurityConstants.SCOPE, SocialSecurityConstants.ERROR_CHECK_SETTING,
					String.format("校验不通过 [{}]的企业基数越界 最大值[{}] 最小值[{}] 实际[{}]",
							itemDTO.getPayItem()==null?"公积金":itemDTO.getPayItem(),base.getCompanyRatioMax(),
							base.getCompanyRatioMin(),itemDTO.getCompanyRatio()));
		}
		//检测个人基数边界
		if (base.getEmployeeRadixMax().compareTo(itemDTO.getEmployeeRadix()) < 0 ||
				base.getEmployeeRadixMin().compareTo(itemDTO.getEmployeeRadix()) > 0) {
			throw RuntimeErrorException.errorWith(SocialSecurityConstants.SCOPE, SocialSecurityConstants.ERROR_CHECK_SETTING,
					String.format("校验不通过 [{}]的企业基数越界 最大值[{}] 最小值[{}] 实际[{}]",
							itemDTO.getPayItem()==null?"公积金":itemDTO.getPayItem(),base.getEmployeeRadixMax(),
							base.getEmployeeRadixMin(),itemDTO.getEmployeeRadix()));
		}
		//检测个人比例边界
		if (base.getEmployeeRatioMax()< itemDTO.getEmployeeRatio() ||
				base.getEmployeeRatioMin()>itemDTO.getEmployeeRatio()) {
			throw RuntimeErrorException.errorWith(SocialSecurityConstants.SCOPE, SocialSecurityConstants.ERROR_CHECK_SETTING,
					String.format("校验不通过 [{}]的企业基数越界 最大值[{}] 最小值[{}] 实际[{}]",
							itemDTO.getPayItem()==null?"公积金":itemDTO.getPayItem(),base.getEmployeeRatioMax(),
							base.getEmployeeRatioMin(),itemDTO.getEmployeeRatio()));
		}
		//检测比例options
		if (StringUtils.isNotBlank(base.getRatioOptions())) {
			List<Integer> options =JSONObject.parseArray(base.getRatioOptions(), Integer.class);
			if (options.contains(itemDTO.getCompanyRatio())&&options.contains(itemDTO.getEmployeeRatio())) {
				//都在options就没问题
			}else{
				throw RuntimeErrorException.errorWith(SocialSecurityConstants.SCOPE, SocialSecurityConstants.ERROR_CHECK_SETTING,
						String.format("校验不通过 [{}]的比例可选项 [{}]   实际[{} , {}]",
								itemDTO.getPayItem()==null?"公积金":itemDTO.getPayItem(),base.getRatioOptions(),
								itemDTO.getEmployeeRatio(),itemDTO.getCompanyRatio()));
			}

		}
	}

	private SocialSecurityBase findSSBase(List<SocialSecurityBase> bases, SocialSecurityItemDTO itemDTO) {
		for (SocialSecurityBase base : bases) {
			if (base.getPayItem().equals(itemDTO.getPayItem())) {
				return base;
			}
		}
		throw RuntimeErrorException.errorWith(SocialSecurityConstants.SCOPE, SocialSecurityConstants.ERROR_CHECK_SETTING,
				"校验不通过 没有找到["+itemDTO.getPayItem()==null?"公积金":itemDTO.getPayItem()+"]的基础数据 "  );
	}

	@Override
	public void importSocialSecurityPayments(ImportSocialSecurityPaymentsCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void calculateSocialSecurityReports(CalculateSocialSecurityReportsCommand cmd) {
		// TODO Auto-generated method stub
		String seed = SocialSecurityConstants.SCOPE + DateHelper.currentGMTTime().getTime();
		String key = Base64.getEncoder().encodeToString(seed.getBytes());
		ValueOperations<String, String> valueOperations = getValueOperations(key);
		int timeout = configurationProvider.getIntValue(PunchConstants.PUNCH_QRCODE_TIMEOUT, 15);
		TimeUnit unit = TimeUnit.MINUTES;;
		// 先放一个和key一样的值,表示这个人key有效
		valueOperations.set(key, key, timeout, unit);
		//线程池
	}


	/**
	 * 获取key在redis操作的valueOperations
	 */
	private ValueOperations<String, String> getValueOperations(String key) {
		final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		Accessor acc = bigCollectionProvider.getMapAccessor(key, "");
		RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

		return valueOperations;
	}


	/**
	 * 清除redis中key的缓存
	 */
	private void deleteValueOperations(String key) {
		final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		Accessor acc = bigCollectionProvider.getMapAccessor(key, "");
		RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
		redisTemplate.delete(key);
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
		return new ListSocialSecurityHouseholdTypesResponse(socialSecurityBaseProvider.listHouseholdTypesByCity(cmd.getCityId()));
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