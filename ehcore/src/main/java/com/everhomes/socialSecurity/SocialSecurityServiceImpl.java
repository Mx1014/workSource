// @formatter:off
package com.everhomes.socialSecurity;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.archives.ArchivesService;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.*;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;

import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationMemberStatus;
import com.everhomes.rest.socialSecurity.*;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.pojos.EhSocialSecurityPayments;
import com.everhomes.server.schema.tables.pojos.EhSocialSecuritySettings;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IntegerUtil;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
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
    private DbProvider dbProvider;
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
    private SocialSecurityInoutTimeProvider socialSecurityInoutTimeProvider;
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
    @Autowired
    private ArchivesService archivesService;
    @Autowired
    private SequenceProvider sequenceProvider;
    @Autowired
    private CoordinationProvider coordinationProvider;
    /**
     * 处理计算的线程池,预设最大值是5
     */
    private static ExecutorService calculateExecutorPool = Executors.newFixedThreadPool(5);
    private static ThreadLocal<SimpleDateFormat> monthSF = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMM");
        }
    };

    private static ThreadLocal<SimpleDateFormat> dateSF = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    private static final Logger LOGGER = LoggerFactory.getLogger(SocialSecurityServiceImpl.class);

    @Override
    public void addSocialSecurity(AddSocialSecurityCommand cmd) {
        addSocialSecurity(cmd.getOwnerId());
    }

    /**
     * 添加某公司的新一期社保缴费
     */
    private void addSocialSecurity(Long ownerId) {
        String paymentMonth = socialSecurityPaymentProvider.findPaymentMonthByOwnerId(ownerId);
        if (null == paymentMonth) {
            newSocialSecurityOrg(ownerId);
        } else {
            checkSocialSercurityFiled(ownerId);
            deleteOldMonthPayments(ownerId);
            try {
                Calendar month = Calendar.getInstance();
                month.setTime(monthSF.get().parse(paymentMonth));
                month.add(Calendar.MONTH, 1);
                paymentMonth = monthSF.get().format(month.getTime());
                addNewMonthPayments(paymentMonth, ownerId);
            } catch (ParseException e) {
                e.printStackTrace();
                LOGGER.error("payment month is wrong  " + paymentMonth, e);
            }

        }
    }

    private void deleteOldMonthPayments(Long ownerId) {
        socialSecurityPaymentProvider.deleteSocialSecurityPayments(ownerId);
    }

    private void newSocialSecurityOrg(Long ownerId) {
//        Organization org = organizationProvider.findOrganizationById(ownerId);
        OrganizationCommunity organizationCommunity = organizationProvider.findOrganizationCommunityByOrgId(ownerId);
        Community community = communityProvider.findCommunityById(organizationCommunity.getCommunityId());
        Long cityId = getZuolinNamespaceCityId(community.getCityId());
        List<HouseholdTypesDTO> hTs = socialSecurityBaseProvider.listHouseholdTypesByCity(cityId);
        if (null == hTs) {
            cityId = SocialSecurityConstants.DEFAULT_CITY;
            hTs = socialSecurityBaseProvider.listHouseholdTypesByCity(cityId);
        }
        addNewSocialSecuritySettings(cityId, hTs.get(0).getHouseholdTypeName(), ownerId);
        String paymentMonth = monthSF.get().format(DateHelper.currentGMTTime());
        addNewMonthPayments(paymentMonth, ownerId);
    }

    @Override
    public void newSocialSecurityEmployee(Long detailId, String inMonth) {
        OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
        OrganizationCommunity organizationCommunity = organizationProvider.findOrganizationCommunityByOrgId(detail.getOrganizationId());
        Community community = communityProvider.findCommunityById(organizationCommunity.getCommunityId());
        Long cityId = getZuolinNamespaceCityId(community.getCityId());
        List<HouseholdTypesDTO> hTs = socialSecurityBaseProvider.listHouseholdTypesByCity(cityId);
        if (null == hTs) {
            cityId = SocialSecurityConstants.DEFAULT_CITY;
            hTs = socialSecurityBaseProvider.listHouseholdTypesByCity(cityId);
        }
        addEmployeeNewSocialSecuritySettings(cityId, hTs.get(0).getHouseholdTypeName(), detail);

        String paymentMonth = socialSecurityPaymentProvider.findPaymentMonthByOwnerId(detail.getOrganizationId());
        if (null == paymentMonth)
            paymentMonth = monthSF.get().format(DateHelper.currentGMTTime());
        if (Integer.valueOf(paymentMonth) >= Integer.valueOf(inMonth)) {
            addEmployeeNewMonthPayments(paymentMonth, detail);
        }
    }

    private void addEmployeeNewMonthPayments(String paymentMonth, OrganizationMemberDetails detail) {
        Set<Long> detailIds = new HashSet<>();
        detailIds.add(detail.getId());
        createPayments(detailIds, paymentMonth);
    }

    private void createPayments(Set<Long> detailIds, String paymentMonth) {
        List<SocialSecuritySetting> settings = socialSecuritySettingProvider.listSocialSecuritySetting(detailIds);
        List<EhSocialSecurityPayments> payments = new ArrayList<>();
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSocialSecurityPayments.class));
        for (SocialSecuritySetting setting : settings) {
            EhSocialSecurityPayments payment = processSocialSecurityPayment(setting, paymentMonth, NormalFlag.NO.getCode());
            payment.setId(id++);
            payments.add(payment);
        }
        sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhSocialSecurityPayments.class), payments.size());
        socialSecurityPaymentProvider.batchCreateSocialSecurityPayment(payments);
    }

    private void addEmployeeNewSocialSecuritySettings(Long cityId, String householdTypeName, OrganizationMemberDetails detail) {
        List<SocialSecurityBase> ssBases = socialSecurityBaseProvider.listSocialSecurityBase(cityId,
                householdTypeName, AccumOrSocail.SOCAIL.getCode());
        if (null == ssBases) {
            ssBases = new ArrayList<>();
        }
        List<SocialSecurityBase> afBases = socialSecurityBaseProvider.listSocialSecurityBase(cityId,
                null, AccumOrSocail.ACCUM.getCode());
        if (null != afBases) {
            ssBases.addAll(afBases);
        }
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSocialSecuritySettings.class));
        List<EhSocialSecuritySettings> settings = new ArrayList<>();
        sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhSocialSecuritySettings.class), settings.size());
        socialSecuritySettingProvider.batchCreateSocialSecuritySetting(settings);
    }

    private void addNewMonthPayments(String paymentMonth, Long ownerId) {
        //把属于该公司的所有要交社保的setting取出来
        //todo : 本月要交社保的人
        Set<Long> detailIds;
        List<OrganizationMemberDetails> details = organizationProvider.listOrganizationMemberDetails(ownerId);
        if (null == details) {
            return;
        }
        detailIds = details.stream().map(r -> r.getId()).collect(Collectors.toSet());
        createPayments(detailIds, paymentMonth);
    }

    private Long getZuolinNamespaceCityId(Long cityId) {
        Region currentNSCity = regionProvider.findRegionById(cityId);
        return getZuolinNamespaceCityId(currentNSCity.getName());
    }

    private Long getZuolinNamespaceCityId(String name) {

        Region zuolinCity = regionProvider.findRegionByName(0, name);
        if (null == zuolinCity) {
            return SocialSecurityConstants.DEFAULT_CITY;
        }
        return zuolinCity.getId();
    }

    private void addNewSocialSecuritySettings(Long cityId, String householdTypeName, Long ownerId) {

        List<SocialSecurityBase> ssBases = socialSecurityBaseProvider.listSocialSecurityBase(cityId,
                householdTypeName, AccumOrSocail.SOCAIL.getCode());
        if (null == ssBases) {
            ssBases = new ArrayList<>();
        }
        List<SocialSecurityBase> afBases = socialSecurityBaseProvider.listSocialSecurityBase(cityId,
                null, AccumOrSocail.ACCUM.getCode());
        if (null != afBases) {
            ssBases.addAll(afBases);
        }
        List<OrganizationMemberDetails> details = organizationProvider.listOrganizationMemberDetails(ownerId);
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSocialSecuritySettings.class));
        List<EhSocialSecuritySettings> settings = new ArrayList<>();
        for (OrganizationMemberDetails detail : details) {
            id = saveSocialSecuritySettings(ssBases, cityId, ownerId, detail.getTargetId(), detail.getId(), detail.getNamespaceId(), id, settings);
//            id = saveSocialSecuritySettings(afBases, cityId, ownerId, detail.getTargetId(), detail.getId(), detail.getNamespaceId(), id, settings);
        }
        sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhSocialSecuritySettings.class), settings.size());
        socialSecuritySettingProvider.batchCreateSocialSecuritySetting(settings);

    }

    private Long saveSocialSecuritySettings(List<SocialSecurityBase> bases, Long cityId, Long orgId, Long userId,
                                            Long detailId, Integer namespaceId, Long id, List<EhSocialSecuritySettings> settings) {
        if (null != bases) {
            for (SocialSecurityBase base : bases) {
                EhSocialSecuritySettings setting = processSocialSecuritySetting(base, cityId, orgId, userId,
                        detailId, namespaceId);
//                socialSecuritySettingProvider.createSocialSecuritySetting(setting);
                setting.setId(id++);
                setting.setRadix(setting.getCompanyRadix());
                settings.add(setting);
            }
        }
        return id;
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
        this.coordinationProvider.getNamedLock(CoordinationLocks.SOCIAL_SECURITY_LIST_PAYMENTS.getCode() + cmd.getOwnerId()).enter(() -> {
            String month = socialSecurityPaymentProvider.findPaymentMonthByOwnerId(cmd.getOwnerId());
            if (null == month) {
                //如果没有payments数据,增加一个
                addSocialSecurity(cmd.getOwnerId());
            }
            return null;

        });
        List<Long> detailIds = archivesService.listSocialSecurityEmployees(cmd.getOwnerId(), cmd.getDeptId(), cmd.getKeywords(), cmd.getFilterItems());

//        SsorAfPay payFlag = null;
//        if (null != cmd.getFilterItems()) {
//            for (Byte filterItem : cmd.getFilterItems()) {
//                if (null == filterItem) {
//                    continue;
//                }
//                SocialSecurityItem item = SocialSecurityItem.fromCode(filterItem);
//                switch (item) {
//                    case SOCIALSECURITYPAY:
//                        if (payFlag != null && payFlag == SsorAfPay.ACCUMULATIONFUNDPAY) {
//                            payFlag = SsorAfPay.BOTHPAY;
//                        } else {
//                            payFlag = SsorAfPay.SOCIALSECURITYPAY;
//                        }
//                        break;
//
//                    case ACCUMULATIONFUNDPAY:
//                        if (payFlag != null && payFlag == SsorAfPay.SOCIALSECURITYPAY) {
//                            payFlag = SsorAfPay.BOTHPAY;
//                        } else {
//                            payFlag = SsorAfPay.ACCUMULATIONFUNDPAY;
//                        }
//                        break;
//                    //todo : 等楠哥接口出来看这里怎么写
//                }
//            }
//        }
        if (cmd.getPageAnchor() == null)
            cmd.setPageAnchor(0L);
        int pageSize = cmd.getPageSize() == null ? 10 : cmd.getPageSize();
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        if (null != cmd.getSocialSecurityCityId()) {
            detailIds = socialSecuritySettingProvider.listDetailsByCityId(detailIds, cmd.getSocialSecurityCityId(), AccumOrSocail.SOCAIL.getCode());

        }
        if (null != cmd.getAccumulationFundCityId()) {
            detailIds = socialSecuritySettingProvider.listDetailsByCityId(detailIds, cmd.getAccumulationFundCityId(), AccumOrSocail.ACCUM.getCode());

        }
//        if (null != payFlag) {
//            if (SsorAfPay.BOTHPAY == payFlag || SsorAfPay.SOCIALSECURITYPAY == payFlag) {
//                detailIds = socialSecurityPaymentProvider.listDetailsByPayFlag(detailIds, AccumOrSocail.SOCAIL.getCode());
//            }
//
//            if (SsorAfPay.BOTHPAY == payFlag || SsorAfPay.ACCUMULATIONFUNDPAY == payFlag) {
//                detailIds = socialSecurityPaymentProvider.listDetailsByPayFlag(detailIds, AccumOrSocail.ACCUM.getCode());
//            }
//        }

//        List<SocialSecurityPaymentDTO> results = socialSecuritySettingProvider.listSocialSecuritySetting(
//                         cmd.getDeptId(), cmd.getKeywords(),
//                        payFlag, detailIds, locator);
        ListSocialSecurityPaymentsResponse response = new ListSocialSecurityPaymentsResponse();
        List<SocialSecurityPaymentDTO> results = new ArrayList<>();
        if (null == detailIds) {
            return null;
        }
        Long nextPageAnchor = null;
        int beginNum = 0;
        if (null != cmd.getPageAnchor()) {
            for (Long id : detailIds) {
                if (id < cmd.getPageAnchor()) {
                    beginNum++;
                } else {
                    break;
                }
            }
        }
        if (pageSize < beginNum + detailIds.size()) {
            nextPageAnchor = detailIds.get(pageSize);
        }
        for (int i = beginNum; i < beginNum + pageSize; i++) {
            SocialSecurityPaymentDTO dto = processSocialSecurityItemDTO(detailIds.get(i));
            results.add(dto);
        }
        response.setNextPageAnchor(nextPageAnchor);
        response.setSocialSecurityPayments(results);
        response.setPaymentMonth(socialSecurityPaymentProvider.findPaymentMonthByOwnerId(cmd.getOwnerId()));
        return response;
    }

    private SocialSecurityPaymentDTO processSocialSecurityItemDTO(Long detailId) {
        SocialSecurityPaymentDTO dto = new SocialSecurityPaymentDTO();
        dto.setDetailId(detailId);
        OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(dto.getDetailId());
        if (null != detail) {
            dto.setUserName(detail.getContactName());
            dto.setDeptName(detail.getDepartment());
            //// TODO: 2017/12/27  入职离职日期
//            dto.setEntryDate(detail.get);
            SocialSecuritySetting accSetting = socialSecuritySettingProvider.findSocialSecuritySettingByDetailIdAndAOS(detailId, AccumOrSocail.ACCUM);
            SocialSecuritySetting socSetting = socialSecuritySettingProvider.findSocialSecuritySettingByDetailIdAndAOS(detailId, AccumOrSocail.SOCAIL);
            if (null != socSetting) {
                dto.setSocialSecurityRadix(socSetting.getRadix());
                Region city = regionProvider.findRegionById(socSetting.getCityId());
                if (null != city) dto.setSocialSecurityCity(city.getName());
            }
            if (null != accSetting) {
                dto.setAccumulationFundRadix(accSetting.getRadix());
                Region city = regionProvider.findRegionById(accSetting.getCityId());
                if (null != city) dto.setAccumulationFundCity(city.getName());
            }
        } else {
            dto.setUserName("找不到这个人");
        }
        return dto;
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
        List<SocialSecuritySetting> settings = null;
        if (null != cmd.getDetailId()) {
            settings = socialSecuritySettingProvider.listSocialSecuritySetting(cmd.getDetailId());
        }

        List<SocialSecuritySetting> finalSettings = settings;
        response.setItems(bases.stream().map(r -> {
            SocialSecurityItemDTO dto = new SocialSecurityItemDTO();
            copyRadixAndRatio(dto, r);
            if (null != finalSettings) {
                SocialSecuritySetting setting = findSetting(r.getAccumOrSocail(), r.getPayItem(), finalSettings);
                if (null != setting) {
                    dto.setCompanyRatio(setting.getCompanyRatio());
                    dto.setCompanyRadix(setting.getCompanyRadix());
                    dto.setEmployeeRadix(setting.getEmployeeRadix());
                    dto.setEmployeeRatio(setting.getEmployeeRatio());
                }
            }
            return dto;
        }).collect(Collectors.toList()));
        return response;
    }

    private SocialSecuritySetting findSetting(Byte accumOrSocail, String payItem, List<SocialSecuritySetting> finalSettings) {
        for (SocialSecuritySetting setting : finalSettings) {
            if (AccumOrSocail.ACCUM == AccumOrSocail.fromCode(accumOrSocail) &&
                    AccumOrSocail.ACCUM == AccumOrSocail.fromCode(setting.getAccumOrSocail())) {
                return setting;
            } else if (setting.getPayItem().equals(payItem)) {
                return setting;
            }
        }
        return null;
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
        List<SocialSecurityPayment> allPayments = socialSecurityPaymentProvider.listSocialSecurityPayment(cmd.getOwnerId(), cmd.getDetailId());
        List<SocialSecurityPayment> ssPayments = new ArrayList<>();
        List<SocialSecurityPayment> ssafterPayments = new ArrayList<>();
        List<SocialSecurityPayment> afPayments = new ArrayList<>();
        List<SocialSecurityPayment> afafterPayments = new ArrayList<>();
        for (SocialSecurityPayment payment : allPayments) {
            if (AccumOrSocail.fromCode(payment.getAccumOrSocail()) == AccumOrSocail.ACCUM) {
                if (NormalFlag.fromCode(payment.getAfterPayFlag()) == NormalFlag.NO) {
                    afPayments.add(payment);
                } else {
                    afafterPayments.add(payment);
                }
            } else {
                if (NormalFlag.fromCode(payment.getAfterPayFlag()) == NormalFlag.NO) {
                    ssPayments.add(payment);
                } else {
                    ssafterPayments.add(payment);
                }
            }
        }
        if (ssPayments.size() == 0) {
            response.setPayCurrentSocialSecurityFlag(NormalFlag.NO.getCode());
        } else {
            response.setPayCurrentSocialSecurityFlag(NormalFlag.YES.getCode());
            response.setSocialSecurityPayment(processSocialSecurityPaymentDetailDTO(ssPayments));
        }
        //社保补缴
        if (ssafterPayments.size() == 0) {
            response.setAfterPaySocialSecurityFlag(NormalFlag.NO.getCode());
        } else {
            response.setAfterPaySocialSecurityFlag(NormalFlag.YES.getCode());
            response.setAfterSocialSecurityPayment(processSocialSecurityPaymentDetailDTO(ssafterPayments));
        }
        //公积金本月缴费
        if (afPayments.size() == 0) {
            response.setPayCurrentAccumulationFundFlag(NormalFlag.NO.getCode());
        } else {
            response.setPayCurrentAccumulationFundFlag(NormalFlag.YES.getCode());
            response.setAccumulationFundPayment(processSocialSecurityPaymentDetailDTO(afPayments));
        }
        //公积金补缴
        if (afafterPayments.size() == 0) {
            response.setAfterPayAccumulationFundFlag(NormalFlag.NO.getCode());
        } else {
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
        dto.setCityId(payments.get(0).getCityId());
        Region city = regionProvider.findRegionById(dto.getCityId());
        if (null != city) dto.setCityName(city.getName());
        dto.setHouseholdType(payments.get(0).getHouseholdType());
        dto.setItems(payments.stream().map(this::processSocialSecurityItemDTO).collect(Collectors.toList()));
        return dto;
    }

    private SocialSecurityItemDTO processSocialSecurityItemDTO(SocialSecurityPayment r) {
        SocialSecurityItemDTO dto = ConvertHelper.convert(r, SocialSecurityItemDTO.class);
        SocialSecurityBase base = socialSecurityBaseProvider.findSocialSecurityBaseByCondition(r.getCityId(), r.getHouseholdType(), r.getAccumOrSocail(), r.getPayItem());
        copyRadixAndRatio(dto, base);
        return dto;
    }

    /**
     * 把基础规则数据赋值给itemDTO
     */
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

        dbProvider.execute((TransactionStatus status) -> {
            //社保设置
            if (NormalFlag.fromCode(cmd.getPayCurrentSocialSecurityFlag()) == NormalFlag.YES) {

                // 查询设置的城市户籍档次的数据规则
                List<SocialSecurityBase> bases = socialSecurityBaseProvider.listSocialSecurityBase(cmd.getSocialSecurityPayment().getCityId(),
                        cmd.getSocialSecurityPayment().getHouseholdType());
                // 校验数据是否合法
                checkSocialSercurity(bases, cmd.getSocialSecurityPayment().getItems());
                // 保存setting表数据
                saveSocialSecuritySettings(cmd.getSocialSecurityPayment(), cmd.getDetailId(), AccumOrSocail.SOCAIL.getCode());
                // 保存当月payments数据
                saveSocialSecurityPayment(cmd.getSocialSecurityPayment(), cmd.getDetailId(), NormalFlag.NO.getCode(), AccumOrSocail.SOCAIL.getCode());
                if (NormalFlag.fromCode(cmd.getAfterPaySocialSecurityFlag()) == NormalFlag.YES) {
                    saveSocialSecurityPayment(cmd.getAfterSocialSecurityPayment(), cmd.getDetailId(), NormalFlag.YES.getCode(), AccumOrSocail.SOCAIL.getCode());
                }
            }
            //公积金设置
            if (NormalFlag.fromCode(cmd.getPayCurrentAccumulationFundFlag()) == NormalFlag.YES) {
                // 查询设置的城市户籍档次的数据规则
                List<SocialSecurityBase> bases = socialSecurityBaseProvider.listSocialSecurityBase(cmd.getAccumulationFundPayment().getCityId(), AccumOrSocail.ACCUM.getCode());
                // 校验数据是否合法
                checkSocialSercurity(bases, cmd.getAccumulationFundPayment().getItems());
                // 保存setting表数据
                saveSocialSecuritySettings(cmd.getAccumulationFundPayment(), cmd.getDetailId(), AccumOrSocail.ACCUM.getCode());
                // 保存当月payments数据
                saveSocialSecurityPayment(cmd.getAccumulationFundPayment(), cmd.getDetailId(), NormalFlag.NO.getCode(), AccumOrSocail.ACCUM.getCode());
                if (NormalFlag.fromCode(cmd.getAfterPaySocialSecurityFlag()) == NormalFlag.YES) {
                    saveSocialSecurityPayment(cmd.getAfterSocialSecurityPayment(), cmd.getDetailId(), NormalFlag.YES.getCode(), AccumOrSocail.ACCUM.getCode());
                }
            }
            return null;
        });
    }

    private void saveSocialSecurityPayment(SocialSecurityPaymentDetailDTO socialSecurityPayment, Long detailId, Byte afterPay, Byte accumOrSocial) {
        String paymentMonth = socialSecurityPaymentProvider.findPaymentMonthByDetail(detailId);
        socialSecurityPaymentProvider.setUserCityAndHTByAccumOrSocial(detailId, accumOrSocial, socialSecurityPayment.getCityId(), socialSecurityPayment.getHouseholdType());
        for (SocialSecurityItemDTO itemDTO : socialSecurityPayment.getItems()) {
            SocialSecurityPayment payment = socialSecurityPaymentProvider.findSocialSecurityPayment(detailId, itemDTO.getPayItem(), accumOrSocial);
            if (null == payment) {
                createSocialSecurityPayment(itemDTO, detailId, accumOrSocial, paymentMonth, afterPay);
            } else {
                copyRadixAndRatio(payment, itemDTO);
                socialSecurityPaymentProvider.updateSocialSecurityPayment(payment);
            }
        }
    }

    private void createSocialSecurityPayment(SocialSecurityItemDTO itemDTO, Long detailId, Byte accumOrSocial, String paymentMonth, Byte afterPay) {
        SocialSecuritySetting setting = socialSecuritySettingProvider.findSocialSecuritySettingByDetailIdAndItem(detailId, itemDTO, accumOrSocial);
        SocialSecurityPayment payment = processSocialSecurityPayment(setting, paymentMonth, afterPay);
        copyRadixAndRatio(payment, itemDTO);
        socialSecurityPaymentProvider.createSocialSecurityPayment(payment);

    }

    private SocialSecurityPayment processSocialSecurityPayment(SocialSecuritySetting setting, String paymentMonth, Byte afterPay) {
        SocialSecurityPayment payment = ConvertHelper.convert(setting, SocialSecurityPayment.class);
        payment.setPayMonth(paymentMonth);
        payment.setAfterPayFlag(afterPay);
        return payment;
    }

    private void copyRadixAndRatio(SocialSecurityPayment target, SocialSecurityItemDTO itemDTO) {
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
                createSocialSecuritySetting(itemDTO, detailId, accumOrSocial, socialSecurityPayment);
            } else {
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
        SocialSecuritySetting setting = processSocialSecuritySetting(base, socialSecurityPayment.getCityId(),
                detail.getOrganizationId(), detail.getTargetId(), detail.getId(), detail.getNamespaceId());
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
        } else {
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
        if (!itemDTO.getCompanyRadix().equals(0) && (base.getCompanyRadixMax().compareTo(itemDTO.getCompanyRadix()) < 0 ||
                base.getCompanyRadixMin().compareTo(itemDTO.getCompanyRadix()) > 0)) {

            LOGGER.error("校验不通过 [{}]的企业基数越界 最大值[{}] 最小值[{}] 实际[{}]",
                    itemDTO.getPayItem() != null ? itemDTO.getPayItem() : "公积金", base.getCompanyRadixMax(),
                    base.getCompanyRadixMin(), itemDTO.getCompanyRadix());
            throw RuntimeErrorException.errorWith(SocialSecurityConstants.SCOPE, SocialSecurityConstants.ERROR_CHECK_SETTING,
                    "校验不通过");
        }
        //检测企业比例边界
        if (!itemDTO.getCompanyRatio().equals(0) && (base.getCompanyRatioMax() < itemDTO.getCompanyRatio() ||
                base.getCompanyRatioMin() > itemDTO.getCompanyRatio())) {
            LOGGER.error("校验不通过 [{}]的企业基数越界 最大值[{}] 最小值[{}] 实际[{}]",
                    itemDTO.getPayItem() == null ? "公积金" : itemDTO.getPayItem(), base.getCompanyRatioMax(),
                    base.getCompanyRatioMin(), itemDTO.getCompanyRatio());
            throw RuntimeErrorException.errorWith(SocialSecurityConstants.SCOPE, SocialSecurityConstants.ERROR_CHECK_SETTING,
                    "校验不通过");
        }
        //检测个人基数边界
        if (!itemDTO.getEmployeeRadix().equals(0) && (base.getEmployeeRadixMax().compareTo(itemDTO.getEmployeeRadix()) < 0 ||
                base.getEmployeeRadixMin().compareTo(itemDTO.getEmployeeRadix()) > 0)) {
            LOGGER.error("校验不通过 [{}]的企业基数越界 最大值[{}] 最小值[{}] 实际[{}]",
                    itemDTO.getPayItem() == null ? "公积金" : itemDTO.getPayItem(), base.getEmployeeRadixMax(),
                    base.getEmployeeRadixMin(), itemDTO.getEmployeeRadix());
            throw RuntimeErrorException.errorWith(SocialSecurityConstants.SCOPE, SocialSecurityConstants.ERROR_CHECK_SETTING,
                    "校验不通过");
        }
        //检测个人比例边界
        if (!itemDTO.getEmployeeRatio().equals(0) && (base.getEmployeeRatioMax() < itemDTO.getEmployeeRatio() ||
                base.getEmployeeRatioMin() > itemDTO.getEmployeeRatio())) {
            LOGGER.error("校验不通过 [{}]的企业基数越界 最大值[{}] 最小值[{}] 实际[{}]",
                    itemDTO.getPayItem() == null ? "公积金" : itemDTO.getPayItem(), base.getEmployeeRatioMax(),
                    base.getEmployeeRatioMin(), itemDTO.getEmployeeRatio());
            throw RuntimeErrorException.errorWith(SocialSecurityConstants.SCOPE, SocialSecurityConstants.ERROR_CHECK_SETTING,
                    "校验不通过");
        }
        //检测比例options
        if (StringUtils.isNotBlank(base.getRatioOptions())) {
            List<Integer> options = JSONObject.parseArray(base.getRatioOptions(), Integer.class);
            if ((options.contains(itemDTO.getCompanyRatio()) || itemDTO.getCompanyRatio().equals(0))
                    && (options.contains(itemDTO.getEmployeeRatio()) || itemDTO.getEmployeeRatio().equals(0))) {
                //都在options就没问题
            } else {
                LOGGER.error("校验不通过 [{}]的比例可选项 [{}]   实际[{} , {}]",
                        itemDTO.getPayItem() == null ? "公积金" : itemDTO.getPayItem(), base.getRatioOptions(),
                        itemDTO.getEmployeeRatio(), itemDTO.getCompanyRatio());
                throw RuntimeErrorException.errorWith(SocialSecurityConstants.SCOPE, SocialSecurityConstants.ERROR_CHECK_SETTING,
                        "校验不通过");
            }

        }
    }

    private SocialSecurityBase findSSBase(List<SocialSecurityBase> bases, SocialSecurityItemDTO itemDTO) {
        for (SocialSecurityBase base : bases) {
            if (base.getAccumOrSocail().equals(AccumOrSocail.ACCUM.getCode())) {
                return base;
            }
            if (base.getPayItem().equals(itemDTO.getPayItem())) {
                return base;
            }
        }
        throw RuntimeErrorException.errorWith(SocialSecurityConstants.SCOPE, SocialSecurityConstants.ERROR_CHECK_SETTING,
                "校验不通过 没有找到[" + (itemDTO.getPayItem() == null ? "公积金" : itemDTO.getPayItem()) + "]的基础数据 ");
    }

    @Override
    public void importSocialSecurityPayments(ImportSocialSecurityPaymentsCommand cmd, MultipartFile file) {
        // TODO Auto-generated method stub
        try {
            List resultList = PropMrgOwnerHandler.processorExcel(file.getInputStream());
            if (resultList.isEmpty()) {
                LOGGER.error("File content is empty");
                throw RuntimeErrorException.errorWith(SocialSecurityConstants.SCOPE, SocialSecurityConstants.ERROR_FILE_IS_EMPTY,
                        "File content is empty");
            }
            batchUpdateSSSettingAndPayments(resultList, cmd.getOwnerId());
        } catch (IOException e) {
            LOGGER.error("file process excel error ", e);
            e.printStackTrace();
        }
    }

    private void batchUpdateSSSettingAndPayments(List list, Long ownerId) {
        //
        for (int i = 1; i < list.size(); i++) {
            RowResult r = (RowResult) list.get(i);
            String userContact = r.getA();
            OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByOrganizationIdAndContactToken(ownerId, userContact);
            if (null == detail) {
                LOGGER.error("can not find organization member ,contact token is " + userContact);
            }else{
                String ssCityName = r.getB();
                Long ssCityId = getZuolinNamespaceCityId(ssCityName);
                String afCityName = r.getC();
                Long afCItyId = getZuolinNamespaceCityId(afCityName);
                String houseType = r.getD();
                List<SocialSecurityBase> ssBases = socialSecurityBaseProvider.listSocialSecurityBase(ssCityId, houseType, AccumOrSocail.SOCAIL.getCode());
                if (null == ssBases) {

                }
                List<SocialSecurityBase> afBases = socialSecurityBaseProvider.listSocialSecurityBase(afCItyId,
                        null, AccumOrSocail.ACCUM.getCode());
                if (null == afBases) {

                }

            }

        }
        //设置完后同步一下
        socialSecuritySettingProvider.syncRadixAndRatioToPayments(ownerId);

    }

    @Override
    public CalculateSocialSecurityReportsResponse calculateSocialSecurityReports(CalculateSocialSecurityReportsCommand cmd) {
        String seed = SocialSecurityConstants.SCOPE + cmd.getOwnerId() + DateHelper.currentGMTTime().getTime();
        String key = Base64.getEncoder().encodeToString(seed.getBytes());
        ValueOperations<String, String> valueOperations = getValueOperations(key);
        int timeout = 15;
        TimeUnit unit = TimeUnit.MINUTES;
        // 先放一个和key一样的值,表示这个人key有效
        valueOperations.set(key, key, timeout, unit);
        //线程池中处理计算规则
        calculateExecutorPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    calculateReports(cmd.getOwnerId());
                } catch (Exception e) {
                    LOGGER.error("calculate reports error!! cmd is  :" + cmd, e);
                } finally {
                    //处理完成删除key,表示这个key已经完成了
                    deleteValueOperations(key);
                }
            }
        });
        return new CalculateSocialSecurityReportsResponse(key);
    }

    private void calculateReports(Long ownerId) {

        //同步setting表的值到payment表
        socialSecuritySettingProvider.syncRadixAndRatioToPayments(ownerId);
        //社保报表
        calculateSocialSecurityReports(ownerId);

        //部门汇总表
        calculateSocialSecurityDptReports(ownerId);
        //人员增减表

        calculateSocialSecurityInoutReports(ownerId);
    }

    private void calculateSocialSecurityInoutReports(Long ownerId) {
        //todo 组织架构获取本月增员本月减员的人
        String month = socialSecurityPaymentProvider.findPaymentMonthByDetail(ownerId);
        List<OrganizationMemberDetails> inDetails = null;
        List<OrganizationMemberDetails> outDetails = null;
        socialSecurityInoutReportProvider.deleteSocialSecurityInoutReportByMonth(ownerId, month);
        if (inDetails != null) {
            for (OrganizationMemberDetails detail : inDetails) {
                SocialSecurityInoutReport report = crateInoutReport(detail, month, InOutFlag.INCRE);
            }
        }
        if (outDetails != null) {
            for (OrganizationMemberDetails detail : outDetails) {
                SocialSecurityInoutReport report = crateInoutReport(detail, month, InOutFlag.DECRE);
            }
        }
    }

    private SocialSecurityInoutReport crateInoutReport(OrganizationMemberDetails detail, String month, InOutFlag inOutFlag) {
        SocialSecurityReport ssReport = socialSecurityReportProvider.findSocialSecurityReportByDetailId(detail.getId(), month);
        SocialSecurityInoutReport report = ConvertHelper.convert(ssReport, SocialSecurityInoutReport.class);
        report.setSocialSecurityAfter(ssReport.getAfterSocialSecurityCompanySum().add(ssReport.getAfterSocialSecurityEmployeeSum()));
        report.setAccumulationFundAfter(ssReport.getAfterAccumulationFundCompanySum().add(ssReport.getAfterAccumulationFundEmployeeSum()));
        if (inOutFlag == InOutFlag.INCRE) {
            report.setSocialSecurityIncrease(ssReport.getSocialSecuritySum());
            report.setAccumulationFundIncrease(ssReport.getAccumulationFundSum());
        } else if (inOutFlag == InOutFlag.DECRE) {
            report.setSocialSecurityDecrease(ssReport.getSocialSecuritySum());
            report.setAccumulationFundDecrease(ssReport.getAccumulationFundSum());
        }
        socialSecurityInoutReportProvider.createSocialSecurityInoutReport(report);
        return report;
    }


    private void calculateSocialSecurityDptReports(Long ownerId) {
        String month = socialSecurityPaymentProvider.findPaymentMonthByOwnerId(ownerId);
        socialSecurityDepartmentSummaryProvider.deleteSocialSecurityDptReports(ownerId, month);
        List<Organization> orgs = findOrganizationDpts(ownerId);
        for (Organization dpt : orgs) {
            calculateSocialSecurityDptReports(dpt, month);
        }

    }

    private void calculateSocialSecurityDptReports(Organization dpt, String month) {
        List<Organization> dptOrgs = findOrganizationDpts(dpt);
        CrossShardListingLocator locator = new CrossShardListingLocator();
        List<Long> orgIds = new ArrayList<Long>();
        orgIds.add(dpt.getId());
        for (Organization o : dptOrgs) {
            orgIds.add(o.getId());
        }
        List<OrganizationMember> organizationMembers = this.organizationProvider.listOrganizationPersonnels(null, orgIds,
                OrganizationMemberStatus.ACTIVE.getCode(), null, locator, Integer.MAX_VALUE - 1);
        List<Long> detailIds = new ArrayList<>();
        if (null != organizationMembers) {
            for (OrganizationMember member : organizationMembers) {
                if (null != member.getDetailId()) {
                    detailIds.add(member.getDetailId());
                }
            }
        }
        LOGGER.debug("校验不通过");
        LOGGER.error("开始计算部门-{} 的数据,\n下辖部门id {},\n人员id{}", dpt.getName(), orgIds, detailIds);
        SocialSecurityDepartmentSummary summary = processSocialSecurityDepartmentSummary(dpt, detailIds, month);
        socialSecurityDepartmentSummaryProvider.createSocialSecurityDepartmentSummary(summary);
    }

    private SocialSecurityDepartmentSummary processSocialSecurityDepartmentSummary(Organization dpt, List<Long> detailIds, String month) {
        SocialSecurityDepartmentSummary summary = new SocialSecurityDepartmentSummary();
        if (detailIds != null && detailIds.size() > 0) {
            summary = socialSecurityReportProvider.calculateSocialSecurityDepartmentSummary(detailIds, month);
        }
        summary.setNamespaceId(dpt.getNamespaceId());
        summary.setDeptId(dpt.getId());
        summary.setDeptName(processOrgPathName(dpt.getPath()));
        summary.setPayMonth(month);
        return summary;
    }

    private String processOrgPathName(String path) {
        StringBuilder sb = null;
        String[] orgArray = path.split("/");
        for (String orgId : orgArray) {
            if (StringUtils.isNotEmpty(orgId)) {
                Organization org = organizationProvider.findOrganizationById(Long.valueOf(orgId));
                if (null != org) {
                    if (null == sb) {
                        sb = new StringBuilder();
                    } else {
                        sb.append("/");
                    }
                    sb.append(org.getName());
                }
            }
        }

        return sb.toString();
    }

    private List<Organization> findOrganizationDpts(Organization org) {

        List<String> groupTypeList = new ArrayList<String>();
        groupTypeList.add(OrganizationGroupType.ENTERPRISE.getCode());
        groupTypeList.add(OrganizationGroupType.DEPARTMENT.getCode());
        List<Organization> orgs = organizationProvider.listOrganizationByGroupTypes(org.getPath() + "%", groupTypeList);
        return orgs;
    }

    private List<Organization> findOrganizationDpts(Long orgId) {
        Organization org = organizationProvider.findOrganizationById(orgId);
        return findOrganizationDpts(org);
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
            socialSecurityReportProvider.createSocialSecurityReport(report);
        }
    }

    /**
     * 计算社保报表
     */
    private SocialSecurityReport calculateUserSocialSecurityReport(OrganizationMemberDetails detail, List<SocialSecurityPayment> payments) {
        SocialSecurityReport report = newSocialSecurityReport(detail);
        List<SocialSecurityPayment> userPayments = findSSpaymentsByDetail(detail.getId(), payments);
        report.setPayMonth(userPayments.get(0).getPayMonth());
        report.setCreatorUid(userPayments.get(0).getCreatorUid());
        report.setCreateTime(userPayments.get(0).getCreateTime());
        report.setFileUid(userPayments.get(0).getFileUid());
        report.setFileTime(userPayments.get(0).getFileTime());
        SocialSecurityEmployeeDTO dto = getSocialSecurityEmployeeInfo(detail.getOrganizationId());
        if (dto.getDismissTime() != null && Integer.valueOf(report.getPayMonth()) >=
                Integer.valueOf(dateSF.get().format(dto.getDismissTime()))) {
            report.setIsWork(IsWork.IS_OUT.getCode());
        }else{
            if (Integer.valueOf(dateSF.get().format(dto.getCheckInTime())).equals(report.getPayMonth())) {
                report.setIsWork(IsWork.IS_NEW.getCode());
            } else {
                report.setIsWork(IsWork.NORMAL.getCode());
            }
        }
        for (SocialSecurityPayment userPayment : userPayments) {
            report.setCreatorUid(userPayment.getCreatorUid());
            report.setCreateTime(userPayment.getCreateTime());
            report.setFileUid(userPayment.getFileUid());
            report.setFileTime(userPayment.getFileTime());
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
                        if (NormalFlag.YES == NormalFlag.fromCode(userPayment.getAfterPayFlag())) {

                            report.setAfterPensionCompanySum(calculateAmount(userPayment.getCompanyRadix(), userPayment.getCompanyRatio(),
                                    report.getPensionCompanySum()));
                            report.setAfterPensionEmployeeSum(calculateAmount(userPayment.getEmployeeRadix(), userPayment.getEmployeeRatio(),
                                    report.getPensionEmployeeSum()));
                        } else {
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
                        if (NormalFlag.YES == NormalFlag.fromCode(userPayment.getAfterPayFlag())) {

                            report.setAfterMedicalCompanySum(calculateAmount(userPayment.getCompanyRadix(), userPayment.getCompanyRatio(),
                                    report.getMedicalCompanySum()));
                            report.setAfterMedicalEmployeeSum(calculateAmount(userPayment.getEmployeeRadix(), userPayment.getEmployeeRatio(),
                                    report.getMedicalEmployeeSum()));
                        } else {
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
                        if (NormalFlag.YES == NormalFlag.fromCode(userPayment.getAfterPayFlag())) {

                            report.setAfterInjuryCompanySum(calculateAmount(userPayment.getCompanyRadix(), userPayment.getCompanyRatio(),
                                    report.getInjuryCompanySum()));
                            report.setAfterInjuryEmployeeSum(calculateAmount(userPayment.getEmployeeRadix(), userPayment.getEmployeeRatio(),
                                    report.getInjuryEmployeeSum()));
                        } else {
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
                        if (NormalFlag.YES == NormalFlag.fromCode(userPayment.getAfterPayFlag())) {

                            report.setAfterUnemploymentCompanySum(calculateAmount(userPayment.getCompanyRadix(), userPayment.getCompanyRatio(),
                                    report.getUnemploymentCompanySum()));
                            report.setAfterUnemploymentEmployeeSum(calculateAmount(userPayment.getEmployeeRadix(), userPayment.getEmployeeRatio(),
                                    report.getUnemploymentEmployeeSum()));
                        } else {
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
                        if (NormalFlag.YES == NormalFlag.fromCode(userPayment.getAfterPayFlag())) {

                            report.setAfterBirthCompanySum(calculateAmount(userPayment.getCompanyRadix(), userPayment.getCompanyRatio(),
                                    report.getBirthCompanySum()));
                            report.setAfterBirthEmployeeSum(calculateAmount(userPayment.getEmployeeRadix(), userPayment.getEmployeeRatio(),
                                    report.getBirthEmployeeSum()));
                        } else {
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
                switch (PayItem.fromCode(userPayment.getPayItem())) {
                    case DISABLE:
                        report.setDisabilitySum(calculateAmount(userPayment.getCompanyRadix(), userPayment.getCompanyRatio())
                                .add(calculateAmount(userPayment.getEmployeeRadix(), userPayment.getEmployeeRatio(), report.getDisabilitySum())));
                    case BUSINESS:
                        report.setCommercialInsurance(calculateAmount(userPayment.getCompanyRadix(), userPayment.getCompanyRatio())
                                .add(calculateAmount(userPayment.getEmployeeRadix(), userPayment.getEmployeeRatio(), report.getCommercialInsurance())));

                }
            }
        }

        return report;
    }

    @Override
    public BigDecimal calculateAmount(BigDecimal radix, Integer ratio) {
        return calculateAmount(radix, ratio, new BigDecimal(0));
    }

    public BigDecimal calculateAmount(BigDecimal radix, Integer ratio, BigDecimal addSum) {

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
                } else {
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
        ListSocialSecurityReportsResponse response = new ListSocialSecurityReportsResponse();
        if (cmd.getPageAnchor() == null)
            cmd.setPageAnchor(0L);
        int pageSize = cmd.getPageSize() == null ? 10 : cmd.getPageSize();
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        List<SocialSecurityReport> result = socialSecurityReportProvider.listSocialSecurityReport(cmd.getOwnerId(),
                cmd.getPaymentMonth(), locator, pageSize + 1);
        if (null == result)
            return response;
        Long nextPageAnchor = null;
        if (result != null && result.size() > pageSize) {
            result.remove(result.size() - 1);
            nextPageAnchor = result.get(result.size() - 1).getId();
        }
        response.setNextPageAnchor(nextPageAnchor);
        response.setSocialSecurityReports(result.stream().map(this::processSocialSecurityReportDTO).collect(Collectors.toList()));
        return response;
    }

    private SocialSecurityReportDTO processSocialSecurityReportDTO(SocialSecurityReport r) {
        SocialSecurityReportDTO dto = ConvertHelper.convert(r, SocialSecurityReportDTO.class);
        return dto;
    }

    @Override
    public void exportSocialSecurityReports(ExportSocialSecurityReportsCommand cmd) {
        List<SocialSecurityReport> result = socialSecurityReportProvider.listSocialSecurityReport(cmd.getOwnerId(),
                cmd.getPaymentMonth(), null, Integer.MAX_VALUE - 1);
        XSSFWorkbook wb = createSocialSecurityReportWorkBook(result);
        // TODO 导出

    }

    private XSSFWorkbook createSocialSecurityReportWorkBook(List<SocialSecurityReport> result) {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("InoutReport");
        if (null == result || result.size() == 0) {
            return wb;
        }
        sheet = createSocialSecurityReportWBHead(sheet);
        for (SocialSecurityReport summary : result) {
            sheet = setNewSocialSecurityReportRow(sheet, summary);
        }
        return wb;

    }

    private XSSFSheet setNewSocialSecurityReportRow(XSSFSheet sheet, SocialSecurityReport r) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        int i = -1;
        row.createCell(++i).setCellValue(r.getUserName());
        row.createCell(++i).setCellValue(dateSF.get().format(r.getEntryDate()));
        row.createCell(++i).setCellValue(r.getContactToken());
        row.createCell(++i).setCellValue(r.getIdNumber());
        row.createCell(++i).setCellValue(r.getDegree());
        row.createCell(++i).setCellValue(r.getSalaryCardBank());
        row.createCell(++i).setCellValue(r.getSalaryCardNumber());
        row.createCell(++i).setCellValue(r.getDeptName());
        row.createCell(++i).setCellValue(r.getSocialSecurityNumber());
        row.createCell(++i).setCellValue(r.getProvidentFundNumber());
        row.createCell(++i).setCellValue(dateSF.get().format(r.getOutWorkDate()));
        row.createCell(++i).setCellValue(r.getHouseholdType());
        row.createCell(++i).setCellValue(r.getSocialSecurityCityName());
        row.createCell(++i).setCellValue(r.getPayMonth());
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getSocialSecurityRadix()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getSocialSecuritySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getSocialSecurityCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getSocialSecurityEmployeeSum()));
        row.createCell(++i).setCellValue(r.getAccumulationFundCityName());
        row.createCell(++i).setCellValue(r.getPayMonth());
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getAccumulationFundRadix()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getAccumulationFundCompanyRadix()));
        row.createCell(++i).setCellValue(r.getAccumulationFundCompanyRatio());
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getAccumulationFundEmployeeRadix()));
        row.createCell(++i).setCellValue(r.getAccumulationFundEmployeeRatio());
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getAccumulationFundSum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getAccumulationFundCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getAccumulationFundEmployeeSum()));
        row.createCell(++i).setCellValue("");
        //养老
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getPensionCompanyRadix()));
        row.createCell(++i).setCellValue(r.getPensionCompanyRatio());
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getPensionCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getPensionEmployeeRadix()));
        row.createCell(++i).setCellValue(r.getPensionEmployeeRatio());
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getPensionEmployeeSum()));
        //失业
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getUnemploymentCompanyRadix()));
        row.createCell(++i).setCellValue(r.getUnemploymentCompanyRatio());
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getUnemploymentCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getUnemploymentEmployeeRadix()));
        row.createCell(++i).setCellValue(r.getUnemploymentEmployeeRatio());
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getUnemploymentEmployeeSum()));
        //医疗
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getMedicalCompanyRadix()));
        row.createCell(++i).setCellValue(r.getMedicalCompanyRatio());
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getMedicalCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getMedicalEmployeeRadix()));
        row.createCell(++i).setCellValue(r.getMedicalEmployeeRatio());
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getMedicalEmployeeSum()));
        //工伤
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getInjuryCompanyRadix()));
        row.createCell(++i).setCellValue(r.getInjuryCompanyRatio());
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getInjuryCompanySum()));
        //生育
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getBirthCompanyRadix()));
        row.createCell(++i).setCellValue(r.getBirthCompanyRatio());
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getBirthCompanySum()));
        //大病
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getCriticalIllnessCompanyRadix()));
        row.createCell(++i).setCellValue(r.getCriticalIllnessCompanyRatio());
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getCriticalIllnessCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getCriticalIllnessEmployeeRadix()));
        row.createCell(++i).setCellValue(r.getCriticalIllnessEmployeeRatio());
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getCriticalIllnessEmployeeSum()));

        //补缴
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getAfterSocialSecurityCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getAfterSocialSecurityEmployeeSum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getAfterAccumulationFundCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getAfterAccumulationFundEmployeeSum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getAfterPensionCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getAfterPensionEmployeeSum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getAfterUnemploymentCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getAfterUnemploymentEmployeeSum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getAfterMedicalCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getAfterMedicalEmployeeSum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getAfterInjuryCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getAfterBirthCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getAfterCriticalIllnessCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getAfterCriticalIllnessEmployeeSum()));
        //商业保险残障金
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getDisabilitySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getCommercialInsurance()));
        return sheet;
    }

    private XSSFSheet createSocialSecurityReportWBHead(XSSFSheet sheet) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        int i = -1;
        row.createCell(++i).setCellValue("姓名");
        row.createCell(++i).setCellValue("入职日期");
        row.createCell(++i).setCellValue("手机号");
        row.createCell(++i).setCellValue("身份证号");
        row.createCell(++i).setCellValue("学历");
        row.createCell(++i).setCellValue("开户行");
        row.createCell(++i).setCellValue("银行卡号");
        row.createCell(++i).setCellValue("部门");
        row.createCell(++i).setCellValue("社保电脑号");
        row.createCell(++i).setCellValue("公积金账号");
        row.createCell(++i).setCellValue("离职日期");
        row.createCell(++i).setCellValue("户籍类型");
        row.createCell(++i).setCellValue("参保城市");
        row.createCell(++i).setCellValue("社保月份");
        row.createCell(++i).setCellValue("社保基数");
        row.createCell(++i).setCellValue("社保合计");
        row.createCell(++i).setCellValue("社保企业");
        row.createCell(++i).setCellValue("社保个人");
        row.createCell(++i).setCellValue("公积金城市");
        row.createCell(++i).setCellValue("公积金月份");
        row.createCell(++i).setCellValue("公积金基数");
        row.createCell(++i).setCellValue("公积金企业基数");
        row.createCell(++i).setCellValue("公积金企业比例");
        row.createCell(++i).setCellValue("公积金个人基数");
        row.createCell(++i).setCellValue("公积金个人比例");
        row.createCell(++i).setCellValue("公积金合计");
        row.createCell(++i).setCellValue("公积金企业");
        row.createCell(++i).setCellValue("公积金个人");
        row.createCell(++i).setCellValue("公积金需纳税额");
        row.createCell(++i).setCellValue("养老企业基数");
        row.createCell(++i).setCellValue("养老企业比例");
        row.createCell(++i).setCellValue("养老企业");
        row.createCell(++i).setCellValue("养老个人基数");
        row.createCell(++i).setCellValue("养老个人比例");
        row.createCell(++i).setCellValue("养老个人");
        row.createCell(++i).setCellValue("失业企业基数");
        row.createCell(++i).setCellValue("失业企业比例");
        row.createCell(++i).setCellValue("失业企业");
        row.createCell(++i).setCellValue("失业个人基数");
        row.createCell(++i).setCellValue("失业个人比例");
        row.createCell(++i).setCellValue("失业个人");
        row.createCell(++i).setCellValue("医疗企业基数");
        row.createCell(++i).setCellValue("医疗企业比例");
        row.createCell(++i).setCellValue("医疗企业");
        row.createCell(++i).setCellValue("医疗个人基数");
        row.createCell(++i).setCellValue("医疗个人比例");
        row.createCell(++i).setCellValue("医疗个人");
        row.createCell(++i).setCellValue("工伤企业基数");
        row.createCell(++i).setCellValue("工伤企业比例");
        row.createCell(++i).setCellValue("工伤企业");
        row.createCell(++i).setCellValue("生育企业基数");
        row.createCell(++i).setCellValue("生育企业比例");
        row.createCell(++i).setCellValue("生育企业");
        row.createCell(++i).setCellValue("大病企业基数");
        row.createCell(++i).setCellValue("大病企业比例");
        row.createCell(++i).setCellValue("大病企业");
        row.createCell(++i).setCellValue("大病个人基数");
        row.createCell(++i).setCellValue("大病个人比例");
        row.createCell(++i).setCellValue("大病个人");
        row.createCell(++i).setCellValue("社保补缴企业");
        row.createCell(++i).setCellValue("社保补缴个人");
        row.createCell(++i).setCellValue("公积金补缴企业");
        row.createCell(++i).setCellValue("公积金补缴个人");
        row.createCell(++i).setCellValue("养老补缴企业");
        row.createCell(++i).setCellValue("养老补缴个人");
        row.createCell(++i).setCellValue("失业补缴企业");
        row.createCell(++i).setCellValue("失业补缴个人");
        row.createCell(++i).setCellValue("医疗补缴企业");
        row.createCell(++i).setCellValue("医疗补缴个人");
        row.createCell(++i).setCellValue("工伤补缴企业");
        row.createCell(++i).setCellValue("生育补缴企业");
        row.createCell(++i).setCellValue("大病补缴企业");
        row.createCell(++i).setCellValue("大病补缴个人");
        row.createCell(++i).setCellValue("残障金");
        row.createCell(++i).setCellValue("商业保险");
        return sheet;
    }


    @Override
    public ListSocialSecurityDepartmentSummarysResponse listSocialSecurityDepartmentSummarys(
            ListSocialSecurityDepartmentSummarysCommand cmd) {
        ListSocialSecurityDepartmentSummarysResponse response = new ListSocialSecurityDepartmentSummarysResponse();
        if (cmd.getPageAnchor() == null)
            cmd.setPageAnchor(0L);
        int pageSize = cmd.getPageSize() == null ? 10 : cmd.getPageSize();
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        List<SocialSecurityDepartmentSummary> result = socialSecurityDepartmentSummaryProvider.listSocialSecurityDepartmentSummary(cmd.getOwnerId(),
                cmd.getPaymentMonth(), locator, pageSize + 1);
        if (null == result)
            return response;
        Long nextPageAnchor = null;
        if (result != null && result.size() > pageSize) {
            result.remove(result.size() - 1);
            nextPageAnchor = result.get(result.size() - 1).getId();
        }
        response.setNextPageAnchor(nextPageAnchor);
        response.setSocialSecurityDepartmentSummarys(result.stream().map(this::processSocialSecurityDepartmentSummaryDTO).collect(Collectors.toList()));
        return response;
    }

    private SocialSecurityDepartmentSummaryDTO processSocialSecurityDepartmentSummaryDTO(SocialSecurityDepartmentSummary r) {
        SocialSecurityDepartmentSummaryDTO dto = ConvertHelper.convert(r, SocialSecurityDepartmentSummaryDTO.class);
        return dto;
    }

    @Override
    public void exportSocialSecurityDepartmentSummarys(
            ExportSocialSecurityDepartmentSummarysCommand cmd) {
        List<SocialSecurityDepartmentSummary> result = socialSecurityDepartmentSummaryProvider.listSocialSecurityDepartmentSummary(cmd.getOwnerId(),
                cmd.getPaymentMonth(), null, Integer.MAX_VALUE - 1);
        Workbook workBook = createSocialSecurityDepartmentSummarysWorkBook(result);
        // TODO: 2018/1/2 导出
    }

    private Workbook createSocialSecurityDepartmentSummarysWorkBook(List<SocialSecurityDepartmentSummary> result) {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("DepartmentSummary");
        if (null == result || result.size() == 0) {
            return wb;
        }
        sheet = createSocialSecurityDepartmentSummarysWBHead(sheet);
        for (SocialSecurityDepartmentSummary summary : result) {
            sheet = setNewSocialSecurityDepartmentSummarysRow(sheet, summary);
        }
        return wb;
    }

    private XSSFSheet setNewSocialSecurityDepartmentSummarysRow(XSSFSheet sheet, SocialSecurityDepartmentSummary summary) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        int i = -1;
        row.createCell(++i).setCellValue(summary.getDeptName());
        row.createCell(++i).setCellValue(summary.getEmployeeCount());
        row.createCell(++i).setCellValue(checkNullBigDecimal(summary.getSocialSecuritySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(summary.getSocialSecurityCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(summary.getSocialSecurityEmployeeSum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(summary.getPensionCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(summary.getPensionEmployeeSum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(summary.getMedicalCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(summary.getMedicalEmployeeSum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(summary.getInjuryCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(summary.getUnemploymentCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(summary.getUnemploymentEmployeeSum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(summary.getBirthCompanySum()));
        row.createCell(++i).setCellValue("");
        row.createCell(++i).setCellValue("");
        row.createCell(++i).setCellValue(checkNullBigDecimal(summary.getAfterSocialSecurityCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(summary.getAfterSocialSecurityEmployeeSum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(summary.getAfterPensionCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(summary.getAfterPensionEmployeeSum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(summary.getAfterUnemploymentCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(summary.getAfterUnemploymentEmployeeSum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(summary.getAfterMedicalCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(summary.getAfterMedicalEmployeeSum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(summary.getAfterInjuryCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(summary.getAfterBirthCompanySum()));
        row.createCell(++i).setCellValue("");
        row.createCell(++i).setCellValue("");
        row.createCell(++i).setCellValue(checkNullBigDecimal(summary.getDisabilitySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(summary.getCommercialInsurance()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(summary.getAccumulationFundSum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(summary.getAccumulationFundCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(summary.getAccumulationFundEmployeeSum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(summary.getAfterAccumulationFundCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(summary.getAfterAccumulationFundEmployeeSum()));
        return sheet;
    }

    private String checkNullBigDecimal(BigDecimal bd) {
        return bd == null ? "" : bd.toString();
    }

    private XSSFSheet createSocialSecurityDepartmentSummarysWBHead(XSSFSheet sheet) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        int i = -1;
        row.createCell(++i).setCellValue("部门");
        row.createCell(++i).setCellValue("人数");
        row.createCell(++i).setCellValue("社保合计");
        row.createCell(++i).setCellValue("社保企业合计");
        row.createCell(++i).setCellValue("社保个人合计");
        row.createCell(++i).setCellValue("养老企业");
        row.createCell(++i).setCellValue("养老个人");
        row.createCell(++i).setCellValue("医疗企业");
        row.createCell(++i).setCellValue("医疗个人");
        row.createCell(++i).setCellValue("工伤企业");
        row.createCell(++i).setCellValue("失业企业");
        row.createCell(++i).setCellValue("失业个人");
        row.createCell(++i).setCellValue("生育企业");
        row.createCell(++i).setCellValue("大病企业");
        row.createCell(++i).setCellValue("大病个人");
        row.createCell(++i).setCellValue("社保补缴企业");
        row.createCell(++i).setCellValue("社保补缴个人");
        row.createCell(++i).setCellValue("养老补缴企业");
        row.createCell(++i).setCellValue("养老补缴个人");
        row.createCell(++i).setCellValue("失业补缴企业");
        row.createCell(++i).setCellValue("失业补缴个人");
        row.createCell(++i).setCellValue("医疗补缴企业");
        row.createCell(++i).setCellValue("医疗补缴个人");
        row.createCell(++i).setCellValue("工伤补缴企业");
        row.createCell(++i).setCellValue("生育补缴企业");
        row.createCell(++i).setCellValue("大病补缴企业");
        row.createCell(++i).setCellValue("大病补缴个人");
        row.createCell(++i).setCellValue("残障金");
        row.createCell(++i).setCellValue("商业保险");
        row.createCell(++i).setCellValue("公积金合计");
        row.createCell(++i).setCellValue("公积金企业");
        row.createCell(++i).setCellValue("公积金个人");
        row.createCell(++i).setCellValue("公积金补缴企业");
        row.createCell(++i).setCellValue("公积金补缴个人");
        return sheet;
    }

    @Override
    public ListSocialSecurityInoutReportsResponse listSocialSecurityInoutReports(
            ListSocialSecurityInoutReportsCommand cmd) {
        ListSocialSecurityInoutReportsResponse response = new ListSocialSecurityInoutReportsResponse();
        if (cmd.getPageAnchor() == null)
            cmd.setPageAnchor(0L);
        int pageSize = cmd.getPageSize() == null ? 10 : cmd.getPageSize();
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        List<SocialSecurityInoutReport> result = socialSecurityInoutReportProvider.listSocialSecurityInoutReport(cmd.getOwnerId(),
                cmd.getPaymentMonth(), locator, pageSize + 1);
        if (null == result)
            return response;
        Long nextPageAnchor = null;
        if (result != null && result.size() > pageSize) {
            result.remove(result.size() - 1);
            nextPageAnchor = result.get(result.size() - 1).getId();
        }
        response.setNextPageAnchor(nextPageAnchor);
        response.setSocialSecurityInoutReports(result.stream().map(this::processSocialSecurityInoutReportDTO).collect(Collectors.toList()));
        return response;
    }

    private SocialSecurityInoutReportDTO processSocialSecurityInoutReportDTO(SocialSecurityInoutReport r) {
        SocialSecurityInoutReportDTO dto = ConvertHelper.convert(r, SocialSecurityInoutReportDTO.class);
        return dto;
    }

    @Override
    public void exportSocialSecurityInoutReports(ExportSocialSecurityInoutReportsCommand cmd) {
        List<SocialSecurityInoutReport> result = socialSecurityInoutReportProvider.listSocialSecurityInoutReport(cmd.getOwnerId(),
                cmd.getPaymentMonth(), null, Integer.MAX_VALUE - 1);
        Workbook workBook = createSocialSecurityInoutReportsWorkBook(result);
// TODO: 2018/1/2 导出 
    }

    private Workbook createSocialSecurityInoutReportsWorkBook(List<SocialSecurityInoutReport> result) {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("InoutReport");
        if (null == result || result.size() == 0) {
            return wb;
        }
        sheet = createSocialSecurityInoutReportWBHead(sheet);
        for (SocialSecurityInoutReport summary : result) {
            sheet = setNewSocialSecurityInoutReportRow(sheet, summary);
        }
        return wb;
    }

    private XSSFSheet setNewSocialSecurityInoutReportRow(XSSFSheet sheet, SocialSecurityInoutReport r) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        int i = -1;
        row.createCell(++i).setCellValue(r.getUserName());
        row.createCell(++i).setCellValue(dateSF.get().format(r.getEntryDate()));
        row.createCell(++i).setCellValue(dateSF.get().format(r.getOutWorkDate()));
        row.createCell(++i).setCellValue(r.getContactToken());
        row.createCell(++i).setCellValue(r.getIdNumber());
        row.createCell(++i).setCellValue(r.getDegree());
        row.createCell(++i).setCellValue(r.getSalaryCardBank());
        row.createCell(++i).setCellValue(r.getSalaryCardNumber());
        row.createCell(++i).setCellValue(r.getDeptName());
        row.createCell(++i).setCellValue(r.getSocialSecurityNumber());
        row.createCell(++i).setCellValue(r.getProvidentFundNumber());
        row.createCell(++i).setCellValue(r.getHouseholdType());
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getSocialSecurityRadix()));
        row.createCell(++i).setCellValue(r.getSocialSecurityCityName());
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getSocialSecurityIncrease()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getSocialSecurityDecrease()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getSocialSecurityAfter()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getAccumulationFundRadix()));
        row.createCell(++i).setCellValue(r.getAccumulationFundCityName());
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getAccumulationFundIncrease()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getAccumulationFundDecrease()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getAccumulationFundAfter()));
        return sheet;
    }

    private XSSFSheet createSocialSecurityInoutReportWBHead(XSSFSheet sheet) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        int i = -1;
        row.createCell(++i).setCellValue("姓名");
        row.createCell(++i).setCellValue("入职日期");
        row.createCell(++i).setCellValue("离职日期");
        row.createCell(++i).setCellValue("手机号");
        row.createCell(++i).setCellValue("身份证号");
        row.createCell(++i).setCellValue("学历");
        row.createCell(++i).setCellValue("开户行");
        row.createCell(++i).setCellValue("银行卡号");
        row.createCell(++i).setCellValue("部门");
        row.createCell(++i).setCellValue("社保电脑号");
        row.createCell(++i).setCellValue("公积金账号");
        row.createCell(++i).setCellValue("户籍类型");
        row.createCell(++i).setCellValue("社保基数");
        row.createCell(++i).setCellValue("参保城市");
        row.createCell(++i).setCellValue("社保增");
        row.createCell(++i).setCellValue("社保减");
        row.createCell(++i).setCellValue("社保补缴");
        row.createCell(++i).setCellValue("公积金基数");
        row.createCell(++i).setCellValue("公积金城市");
        row.createCell(++i).setCellValue("公积金增");
        row.createCell(++i).setCellValue("公积金减");
        row.createCell(++i).setCellValue("公积金补缴");
        return sheet;
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
        socialSecuritySummaryProvider.deleteSocialSecuritySummary(cmd.getOwnerId(), cmd.getPaymentMonth());
        SocialSecuritySummary summary = socialSecurityPaymentProvider.calculateSocialSecuritySummary(cmd.getOwnerId(), cmd.getPaymentMonth());
        socialSecuritySummaryProvider.createSocialSecuritySummary(summary);
        //更新归档状态
        socialSecurityPaymentProvider.updateSocialSecurityPaymentFileStatus(cmd.getOwnerId());
    }

    @Override
    public ListSocialSecurityHistoryFilesResponse listSocialSecurityHistoryFiles(
            ListSocialSecurityHistoryFilesCommand cmd) {
        ListSocialSecurityHistoryFilesResponse response = new ListSocialSecurityHistoryFilesResponse();
        if (cmd.getPageAnchor() == null)
            cmd.setPageAnchor(0L);
        int pageSize = cmd.getPageSize() == null ? 10 : cmd.getPageSize();
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        List<SocialSecuritySummary> result = socialSecuritySummaryProvider.listSocialSecuritySummary(cmd.getOwnerId(),
                cmd.getPaymentMonth(), locator, pageSize + 1);
        if (null == result)
            return response;
        Long nextPageAnchor = null;
        if (result != null && result.size() > pageSize) {
            result.remove(result.size() - 1);
            nextPageAnchor = result.get(result.size() - 1).getId();
        }
        response.setNextPageAnchor(nextPageAnchor);
        response.setSocialSecuritySummarys(result.stream().map(this::processSocialSecuritySummaryDTO).collect(Collectors.toList()));
        return response;
    }

    private SocialSecuritySummaryDTO processSocialSecuritySummaryDTO(SocialSecuritySummary r) {
        SocialSecuritySummaryDTO dto = ConvertHelper.convert(r, SocialSecuritySummaryDTO.class);
        return dto;
    }


    @Override
    public ListUserInoutHistoryResponse listUserInoutHistory(ListUserInoutHistoryCommand cmd) {
        // TODO: 2017/12/21 人事档案提供
        SocialSecurityEmployeeDTO result = getSocialSecurityEmployeeInfo(cmd.getDetailId());

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
        GetSocialSecurityReportsHeadResponse response = new GetSocialSecurityReportsHeadResponse();
        response.setPaymentMonth(cmd.getPaymentMonth());
        List<SocialSecurityPayment> result = socialSecurityPaymentProvider.listSocialSecurityPayment(cmd.getOwnerId());
        if (null == result) {
            return null;
        }
        if (result.get(0).getPayMonth().equals(cmd.getPaymentMonth())) {
            response.setCreateTime(result.get(0).getCreateTime().getTime());
            response.setCreatorUid(result.get(0).getCreatorUid());
            response.setFileUid(result.get(0).getFileUid());
            response.setFileTime(result.get(0).getFileTime().getTime());
            return response;
        }
        SocialSecurityPaymentLog log = socialSecurityPaymentLogProvider.findAnyOneSocialSecurityPaymentLog(cmd.getOwnerId(), cmd.getPaymentMonth());
        if (null == log) {
            return null;
        }
        response.setCreateTime(log.getCreateTime().getTime());
        response.setCreatorUid(log.getCreatorUid());
        response.setFileUid(log.getFileUid());
        response.setFileTime(log.getFileTime().getTime());
        return response;
    }

    public SocialSecurityEmployeesCountResponse getSocialSecurityEmployeesCount(Long organizationId, Long month) {
        SocialSecurityEmployeesCountResponse response = new SocialSecurityEmployeesCountResponse();





        response.setSocialSecurity(2);
        response.setAccumulationFund(3);
        response.setGrowth(0);
        response.setReduce(0);
        response.setCheckIn(9);
        response.setDismiss(7);
        return response;
    }

    public SocialSecurityInoutTimeDTO addSocialSecurityInOutTime(AddSocialSecurityInOutTimeCommand cmd) {
        SocialSecurityInoutTime inoutTime = new SocialSecurityInoutTime();
        OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByDetailId(cmd.getDetailId());

        inoutTime.setNamespaceId(memberDetail.getNamespaceId());
        inoutTime.setUserId(memberDetail.getTargetId());
        inoutTime.setDetailId(memberDetail.getId());
        inoutTime.setType(cmd.getInOutType());
        if(cmd.getStartMonth() != null)
            inoutTime.setStartMonth(cmd.getStartMonth());
        if(cmd.getEndMonth() != null)
            inoutTime.setEndMonth(cmd.getEndMonth());

        socialSecurityInoutTimeProvider.createSocialSecurityInoutTime(inoutTime);
        newSocialSecurityEmployee(cmd.getDetailId(), cmd.getStartMonth());

        //  return back.
        SocialSecurityInoutTimeDTO dto = ConvertHelper.convert(inoutTime, SocialSecurityInoutTimeDTO.class);
        dto.setInOutType(inoutTime.getType());
        return dto;
    }

    public SocialSecurityEmployeeDTO getSocialSecurityEmployeeInfo(Long detailId){
        SocialSecurityEmployeeDTO dto = new SocialSecurityEmployeeDTO();

        OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
        if(memberDetail != null){
            dto.setDetailId(memberDetail.getId());
            dto.setUserId(memberDetail.getTargetId());
            dto.setCheckInTime(memberDetail.getCheckInTime());
            dto.setDismissTime(memberDetail.getDismissTime());

            SocialSecurityInoutTime social = socialSecurityInoutTimeProvider.getSocialSecurityInoutTimeByDetailId(InOutType.SOCIAL_SECURITY.getCode(), detailId);
            if(social != null){
                dto.setSocialSecurityStartMonth(social.getStartMonth());
                dto.setSocialSecurityEndMonth(social.getEndMonth());
            }
        }
        return dto;
    }
}