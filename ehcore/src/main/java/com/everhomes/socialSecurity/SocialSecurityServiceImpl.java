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

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
	/**处理计算的线程池,预设最大值是5*/
	private static ExecutorService calculateExecutorPool = Executors.newFixedThreadPool(5);
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
		socialSecurityPaymentProvider.deleteSocialSecurityPayments(ownerId);
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
	public SocialSecurityPaymentDetailDTO getSocialSecurityRule(GetSocialSecurityRuleCommand cmd) {
		SocialSecurityPaymentDetailDTO response = new SocialSecurityPaymentDetailDTO();
		if (AccumOrSocail.fromCode(cmd.getAccumOrsocial()) == AccumOrSocail.SOCAIL && cmd.getHouseholdType() == null) {
			List<HouseholdTypesDTO> result = socialSecurityBaseProvider.listHouseholdTypesByCity(cmd.getCityId());
			cmd.setHouseholdType(result.get(0).getHouseholdTypeName());
		}
		List<SocialSecurityBase> bases = socialSecurityBaseProvider.listSocialSecurityBase(cmd.getCityId(), cmd.getHouseholdType(), cmd.getAccumOrsocial());
		if (null == bases) {
			return response;
		}
		response.setItems(bases.stream().map(r->{
			SocialSecurityItemDTO dto = new SocialSecurityItemDTO();
			copyRadixAndRatio(dto, r);
			return dto;
		}).collect(Collectors.toList()));
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
		List<SocialSecurityPayment> allPayments = socialSecurityPaymentProvider.listSocialSecurityPayment(cmd.getOwnerId(),cmd.getDetailId() );
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
		SocialSecurityItemDTO dto = ConvertHelper.convert(r, SocialSecurityItemDTO.class);
		SocialSecurityBase base = socialSecurityBaseProvider.findSocialSecurityBaseByCondition(r.getCityId(), r.getHouseholdType(), r.getAccumOrSocail(), r.getPayItem());
		copyRadixAndRatio(dto, base);
		return dto;
	}
	/**把基础规则数据赋值给itemDTO*/
	private void copyRadixAndRatio(SocialSecurityItemDTO dto, SocialSecurityBase base) {
		dto.setEditableFlag(base.getEditableFlag());
		dto.setPayItem(base.getPayItem());
		dto.setIsDefault(base.getIsDefault());
		dto.setCompanyRadixMax(base.getCompanyRadixMax());
		dto.setCompanyRadixMin(base.getCompanyRadixMin());
		dto.setCompanyRatioMax(base.getCompanyRatioMax());
		dto.setCompanyRatioMin(base.getCompanyRatioMin());
		dto.setEmployeeRadixMax(base.getEmployeeRadixMax());
		dto.setEmployeeRadixMin(base.getEmployeeRadixMin());
		dto.setEmployeeRatioMax(base.getEmployeeRatioMax());
		dto.setEmployeeRatioMin(base.getEmployeeRatioMin());
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
	public CalculateSocialSecurityReportsResponse calculateSocialSecurityReports(CalculateSocialSecurityReportsCommand cmd) {
		String seed = SocialSecurityConstants.SCOPE + cmd.getOwnerId() + DateHelper.currentGMTTime().getTime();
		String key = Base64.getEncoder().encodeToString(seed.getBytes());
		ValueOperations<String, String> valueOperations = getValueOperations(key);
		int timeout = 15 ;
		TimeUnit unit = TimeUnit.MINUTES;;
		// 先放一个和key一样的值,表示这个人key有效
		valueOperations.set(key, key, timeout, unit);
		//线程池中处理计算规则
		calculateExecutorPool.execute(new Runnable() {
			@Override
			public void run() {
				calculateReports(cmd.getOwnerId());
				//处理完成删除key,表示这个key已经完成了
				deleteValueOperations(key);
			}
		});
		return new CalculateSocialSecurityReportsResponse(key);
	}

	private void calculateReports(Long ownerId) {
		// TODO: 2017/12/21

		//社保报表
		calculateSocialSecurityReports(ownerId);

		//部门汇总表

		//人员增减表
	}

	private void calculateSocialSecurityReports(Long ownerId) {

		List<SocialSecurityPayment> payments = socialSecurityPaymentProvider.listSocialSecurityPayment(ownerId);
		if (null == payments) {
			throw RuntimeErrorException.errorWith(SocialSecurityConstants.SCOPE, SocialSecurityConstants.ERROR_NO_PAYMENTS,
					"没有找到缴费数据");
		}
		//删除这个公司这个月的数据
		socialSecurityReportProvider.deleteSocialSecurityReports(ownerId, payments.get(0).getPayMonth());
		List<OrganizationMemberDetails> details = organizationProvider.listOrganizationMemberDetails(ownerId);
		for (OrganizationMemberDetails detail : details) {
			SocialSecurityReport report = calculateUserSocialSecurityReport(detail, payments);
		}
	}
	/**计算社保报表*/
	private SocialSecurityReport calculateUserSocialSecurityReport(OrganizationMemberDetails detail, List<SocialSecurityPayment> payments) {
		SocialSecurityReport report = newSocialSecurityReport(detail);
		List<SocialSecurityPayment> userPayments = findSSpaymentsByDetail(detail.getId(), payments);
		for (SocialSecurityPayment userPayment : userPayments) {
			//统计公积金
			if (AccumOrSocail.ACCUM == AccumOrSocail.fromCode(userPayment.getAccumOrSocail())) {
				report.setAccumulationFundCompanyRadix(userPayment.getCompanyRadix());
				report.setAccumulationFundCompanyRatio(userPayment.getCompanyRatio());
				report.setAccumulationFundEmployeeRadix(userPayment.getEmployeeRadix());
				report.setAccumulationFundEmployeeRatio(userPayment.getEmployeeRatio());
				report.setAccumulationFundCompanySum(calculateAmount(userPayment.getCompanyRadix(), userPayment.getCompanyRatio(),
						report.getAccumulationFundCompanySum()));
				report.setAccumulationFundEmployeeSum(calculateAmount(userPayment.getEmployeeRadix(), userPayment.getEmployeeRatio(),
						report.getAccumulationFundEmployeeSum()));
				report.setAccumulationFundSum(report.getAccumulationFundCompanySum().add(report.getAccumulationFundEmployeeSum()));
			} else if (NormalFlag.YES == NormalFlag.fromCode(userPayment.getIsDefault())) {
				//统计社保
				report.setSocialSecurityCompanySum(calculateAmount(userPayment.getCompanyRadix(), userPayment.getCompanyRatio()));
				report.setSocialSecurityEmployeeSum(calculateAmount(userPayment.getEmployeeRadix(), userPayment.getEmployeeRatio()));
				report.setSocialSecuritySum(report.getSocialSecurityCompanySum().add(report.getSocialSecurityEmployeeSum()));
				switch (PayItem.fromCode(userPayment.getPayItem())) {
				case PENSION:
					if(NormalFlag.YES == NormalFlag.fromCode(userPayment.getAfterPayFlag())){

						report.setAfterPensionCompanySum(calculateAmount(userPayment.getCompanyRadix(), userPayment.getCompanyRatio(),
								report.getPensionCompanySum()));
						report.setAfterPensionEmployeeSum(calculateAmount(userPayment.getEmployeeRadix(), userPayment.getEmployeeRatio(),
								report.getPensionEmployeeSum()));
					}else{
						report.setPensionCompanyRadix(userPayment.getCompanyRadix());
						report.setPensionCompanyRatio(userPayment.getCompanyRatio());
						report.setPensionEmployeeRadix(userPayment.getEmployeeRadix());
						report.setPensionEmployeeRatio(userPayment.getEmployeeRatio());
						report.setPensionCompanySum(calculateAmount(userPayment.getCompanyRadix(), userPayment.getCompanyRatio(),
								report.getPensionCompanySum()));
						report.setPensionEmployeeSum(calculateAmount(userPayment.getEmployeeRadix(), userPayment.getEmployeeRatio(),
								report.getPensionEmployeeSum()));
					}
					break;
				case MEDICAL:
					if(NormalFlag.YES == NormalFlag.fromCode(userPayment.getAfterPayFlag())){

						report.setAfterMedicalCompanySum(calculateAmount(userPayment.getCompanyRadix(), userPayment.getCompanyRatio(),
								report.getMedicalCompanySum()));
						report.setAfterMedicalEmployeeSum(calculateAmount(userPayment.getEmployeeRadix(), userPayment.getEmployeeRatio(),
								report.getMedicalEmployeeSum()));
					}else{
						report.setMedicalCompanyRadix(userPayment.getCompanyRadix());
						report.setMedicalCompanyRatio(userPayment.getCompanyRatio());
						report.setMedicalEmployeeRadix(userPayment.getEmployeeRadix());
						report.setMedicalEmployeeRatio(userPayment.getEmployeeRatio());
						report.setMedicalCompanySum(calculateAmount(userPayment.getCompanyRadix(), userPayment.getCompanyRatio(),
								report.getMedicalCompanySum()));
						report.setMedicalEmployeeSum(calculateAmount(userPayment.getEmployeeRadix(), userPayment.getEmployeeRatio(),
								report.getMedicalEmployeeSum()));
					}
					break;
				case INJURY:
					if(NormalFlag.YES == NormalFlag.fromCode(userPayment.getAfterPayFlag())){

						report.setAfterInjuryCompanySum(calculateAmount(userPayment.getCompanyRadix(), userPayment.getCompanyRatio(),
								report.getInjuryCompanySum()));
						report.setAfterInjuryEmployeeSum(calculateAmount(userPayment.getEmployeeRadix(), userPayment.getEmployeeRatio(),
								report.getInjuryEmployeeSum()));
					}else{
						report.setInjuryCompanyRadix(userPayment.getCompanyRadix());
						report.setInjuryCompanyRatio(userPayment.getCompanyRatio());
						report.setInjuryEmployeeRadix(userPayment.getEmployeeRadix());
						report.setInjuryEmployeeRatio(userPayment.getEmployeeRatio());
						report.setInjuryCompanySum(calculateAmount(userPayment.getCompanyRadix(), userPayment.getCompanyRatio(),
								report.getInjuryCompanySum()));
						report.setInjuryEmployeeSum(calculateAmount(userPayment.getEmployeeRadix(), userPayment.getEmployeeRatio(),
								report.getInjuryEmployeeSum()));
					}
					break;
				case UNEMPLOYMENT:
					if(NormalFlag.YES == NormalFlag.fromCode(userPayment.getAfterPayFlag())){

						report.setAfterUnemploymentCompanySum(calculateAmount(userPayment.getCompanyRadix(), userPayment.getCompanyRatio(),
								report.getUnemploymentCompanySum()));
						report.setAfterUnemploymentEmployeeSum(calculateAmount(userPayment.getEmployeeRadix(), userPayment.getEmployeeRatio(),
								report.getUnemploymentEmployeeSum()));
					}else{
						report.setUnemploymentCompanyRadix(userPayment.getCompanyRadix());
						report.setUnemploymentCompanyRatio(userPayment.getCompanyRatio());
						report.setUnemploymentEmployeeRadix(userPayment.getEmployeeRadix());
						report.setUnemploymentEmployeeRatio(userPayment.getEmployeeRatio());
						report.setUnemploymentCompanySum(calculateAmount(userPayment.getCompanyRadix(), userPayment.getCompanyRatio(),
								report.getUnemploymentCompanySum()));
						report.setUnemploymentEmployeeSum(calculateAmount(userPayment.getEmployeeRadix(), userPayment.getEmployeeRatio(),
								report.getUnemploymentEmployeeSum()));
					}
					break;
				case BIRTH:
					if(NormalFlag.YES == NormalFlag.fromCode(userPayment.getAfterPayFlag())){

						report.setAfterBirthCompanySum(calculateAmount(userPayment.getCompanyRadix(), userPayment.getCompanyRatio(),
								report.getBirthCompanySum()));
						report.setAfterBirthEmployeeSum(calculateAmount(userPayment.getEmployeeRadix(), userPayment.getEmployeeRatio(),
								report.getBirthEmployeeSum()));
					}else{
						report.setBirthCompanyRadix(userPayment.getCompanyRadix());
						report.setBirthCompanyRatio(userPayment.getCompanyRatio());
						report.setBirthEmployeeRadix(userPayment.getEmployeeRadix());
						report.setBirthEmployeeRatio(userPayment.getEmployeeRatio());
						report.setBirthCompanySum(calculateAmount(userPayment.getCompanyRadix(), userPayment.getCompanyRatio(),
								report.getBirthCompanySum()));
						report.setBirthEmployeeSum(calculateAmount(userPayment.getEmployeeRadix(), userPayment.getEmployeeRatio(),
								report.getBirthEmployeeSum()));
					}
					break;
				}
			} else {
				//暂时不统计商业保险
			}
		}

		return report;
	}

	private BigDecimal calculateAmount(BigDecimal radix, Integer ratio) {
		return calculateAmount(radix, ratio, new BigDecimal(0));
	}

	private BigDecimal calculateAmount(BigDecimal radix, Integer ratio, BigDecimal addSum) {

		return radix.multiply(new BigDecimal(ratio)).divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_UP)
				.add(addSum == null ? new BigDecimal(0) : addSum);
	}

	private SocialSecurityReport newSocialSecurityReport(OrganizationMemberDetails detail) {
		SocialSecurityReport report = ConvertHelper.convert(detail, SocialSecurityReport.class);
		report.setDetailId(detail.getId());
		report.setUserId(detail.getTargetId());
		report.setUserName(detail.getContactName());
		report.setEntryDate(detail.getEndTime());
		report.setDeptName(detail.getDepartment());
		List<SocialSecuritySetting> settings = socialSecuritySettingProvider.listSocialSecuritySetting(detail.getId());
		if (null != settings) {
			report.setHouseholdType(settings.get(0).getHouseholdType());
			for (SocialSecuritySetting setting : settings) {
				if (AccumOrSocail.ACCUM == AccumOrSocail.fromCode(setting.getAccumOrSocail())) {
					report.setAccumulationFundCityId(setting.getCityId());
					Region city = regionProvider.findRegionById(setting.getCityId());
					if (null != city) {
						report.setAccumulationFundCityName(city.getName());
					}
					report.setAccumulationFundRadix(setting.getRadix());
				}else{
					report.setSocialSecurityCityId(setting.getCityId());
					Region city = regionProvider.findRegionById(setting.getCityId());
					if (null != city) {
						report.setSocialSecurityCityName(city.getName());
					}
					report.setSocialSecurityRadix(setting.getRadix());

				}
			}
		}
		return report;
	}

	private List<SocialSecurityPayment> findSSpaymentsByDetail(Long detailId, List<SocialSecurityPayment> payments) {
		List<SocialSecurityPayment> result = new ArrayList<>();
		for (SocialSecurityPayment payment : payments) {
			if (payment.getDetailId().equals(detailId)) {
				result.add(payment);
			}
		}
		return result;
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
		//归档-这个时候就计算归档表了
		List<SocialSecurityPayment> payments = socialSecurityPaymentProvider.listSocialSecurityPayment(cmd.getOwnerId());

		if (null == payments) {
			throw RuntimeErrorException.errorWith(SocialSecurityConstants.SCOPE, SocialSecurityConstants.ERROR_NO_PAYMENTS,
					"没有当月缴费数据!");
		}
		String paymonth = payments.get(0).getPayMonth();
		if (!paymonth.equals(cmd.getPaymentMonth())) {
			throw RuntimeErrorException.errorWith(SocialSecurityConstants.SCOPE, SocialSecurityConstants.ERROR_NO_PAYMENTS,
					"没有当月缴费数据!");
		}
		//删除之前当月的归档表
		socialSecurityPaymentLogProvider.deleteMonthLog(cmd.getOwnerId(), cmd.getPaymentMonth());
		for (SocialSecurityPayment payment : payments) {
			SocialSecurityPaymentLog paymentLog = ConvertHelper.convert(payment, SocialSecurityPaymentLog.class);
			socialSecurityPaymentLogProvider.createSocialSecurityPaymentLog(paymentLog);
		}
		//归档汇总表
		SocialSecuritySummary summary = socialSecurityPaymentProvider.calculateSocialSecuritySummary(cmd.getOwnerId(), cmd.getPaymentMonth());

		socialSecurityPaymentProvider.updateSocialSecurityPaymentFileStatus(cmd.getOwnerId());
	}

	@Override
	public ListSocialSecurityHistoryFilesResponse listSocialSecurityHistoryFiles(
			ListSocialSecurityHistoryFilesCommand cmd) {
		// TODO Auto-generated method stub

		return null;
	}
  

	@Override
	public ListUserInoutHistoryResponse listUserInoutHistory(ListUserInoutHistoryCommand cmd) {
		// TODO: 2017/12/21 人事档案提供
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
		Byte status = NormalFlag.NO.getCode();
		String key = cmd.getReportToken().trim();
		ValueOperations<String, String> valueOperations = getValueOperations(key);
		String value = valueOperations.get(key);
		if (null == value) {
			status = NormalFlag.YES.getCode();
		}
		return new GetSocialSecurityReportStatusResponse(status);
	}

	@Override
	public GetSocialSecurityReportsHeadResponse getSocialSecurityReportsHead(GetSocialSecurityReportsHeadCommand cmd) {
		return null;
	}

}