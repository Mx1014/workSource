// @formatter:off
package com.everhomes.socialSecurity;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.archives.ArchivesService;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.filedownload.TaskService;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.*;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.filedownload.TaskRepeatFlag;
import com.everhomes.rest.filedownload.TaskType;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.socialSecurity.*;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhSocialSecurityPaymentLogs;
import com.everhomes.server.schema.tables.pojos.EhSocialSecurityPayments;
import com.everhomes.server.schema.tables.pojos.EhSocialSecuritySettings;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.*;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;

import org.apache.commons.collections.map.HashedMap;
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

import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
    private TaskService taskService;
    @Autowired
    private ImportFileService importFileService;
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
    private SocialSecurityInoutLogProvider socialSecurityInoutLogProvider;
    @Autowired
    private RegionProvider regionProvider;
    @Autowired
    private OrganizationProvider organizationProvider;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private CommunityProvider communityProvider;
    @Autowired
    private BigCollectionProvider bigCollectionProvider;
    @Autowired
    private SequenceProvider sequenceProvider;
    @Autowired
    private CoordinationProvider coordinationProvider;
    @Autowired
    private SocialSecurityGroupProvider socialSecurityGroupProvider;
    @Autowired
    private ArchivesService archivesService;
    /**
     * 处理计算的线程池,预设最大值是5
     */
    private static ExecutorService calculateExecutorPool = Executors.newFixedThreadPool(5);
    private static ThreadLocal<SimpleDateFormat> monthSF = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMM"));

    private static ThreadLocal<SimpleDateFormat> dateSF = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

    private static final Logger LOGGER = LoggerFactory.getLogger(SocialSecurityServiceImpl.class);

    @Override
    public void addSocialSecurity(AddSocialSecurityCommand cmd) {
        addSocialSecurity(cmd.getOwnerId());
    }

    /**
     * 添加某公司的新一期社保缴费
     */
    private void addSocialSecurity(Long ownerId) {
        this.coordinationProvider.getNamedLock(CoordinationLocks.SOCIAL_SECURITY_ADD.getCode() + ownerId).enter(() -> {

            final String paymentMonth = socialSecurityPaymentProvider.findPaymentMonthByOwnerId(ownerId);
            if (null == paymentMonth) {
                newSocialSecurityOrg(ownerId);
            } else {
                dbProvider.execute((TransactionStatus status) -> {

                    try {
                        Calendar month = Calendar.getInstance();
                        month.setTime(monthSF.get().parse(paymentMonth));
                        month.add(Calendar.MONTH, 1);
                        String newPayMonth = monthSF.get().format(month.getTime());
//                        checkSocialSercurityFiled(ownerId);
                        deleteOldMonthPayments(ownerId);
                        addNewMonthPayments(newPayMonth, ownerId);
                        newSocialSecurityGroup(ownerId, newPayMonth);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        LOGGER.error("payment month is wrong  " + paymentMonth, e);
                    }
                    return null;
                });
            }
            return null;
        });
    }

    private SocialSecurityGroup newSocialSecurityGroup(Long ownerId, String month) {
        socialSecurityGroupProvider.deleteGroup(ownerId, month);
        SocialSecurityGroup group = new SocialSecurityGroup();
        group.setOrganizationId(ownerId);
        group.setPayMonth(month);
        group.setIsFiled(NormalFlag.NO.getCode());
        socialSecurityGroupProvider.createSocialSecurityGroup(group);
        return group;
    }

    private void deleteOldMonthPayments(Long ownerId) {
        socialSecurityPaymentProvider.deleteSocialSecurityPayments(ownerId);
    }

    private void newSocialSecurityOrg(Long ownerId) {
//        Organization org = organizationProvider.findOrganizationById(ownerId);
        List<SocialSecuritySetting> settings = socialSecuritySettingProvider.listSocialSecuritySettingByOwner(ownerId);
        if (null == settings) {
            OrganizationCommunity organizationCommunity = organizationProvider.findOrganizationCommunityByOrgId(ownerId);
            Community community = communityProvider.findCommunityById(organizationCommunity.getCommunityId());
            Long cityId = getZuolinNamespaceCityId(community.getCityId());
            List<HouseholdTypesDTO> hTs = socialSecurityBaseProvider.listHouseholdTypesByCity(cityId);
            if (null == hTs) {
                cityId = SocialSecurityConstants.DEFAULT_CITY;
                hTs = socialSecurityBaseProvider.listHouseholdTypesByCity(cityId);
            }
            addNewSocialSecuritySettings(cityId, hTs.get(0).getHouseholdTypeName(), ownerId);
        }
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

        String paymentMonth = findPaymentMonth(detail.getOrganizationId());
        if (null == paymentMonth)
            paymentMonth = monthSF.get().format(DateHelper.currentGMTTime());
        if (Integer.valueOf(paymentMonth) >= Integer.valueOf(inMonth)) {
            addEmployeeNewMonthPayments(paymentMonth, detail);
        }
    }

    private void addEmployeeNewMonthPayments(String paymentMonth, OrganizationMemberDetails detail) {
        Set<Long> detailIds = new HashSet<>();
        detailIds.add(detail.getId());
        createPayments(detailIds, paymentMonth, AccumOrSocial.ACCUM);
        createPayments(detailIds, paymentMonth, AccumOrSocial.SOCAIL);
    }

    private void createPayments(Set<Long> detailIds, String paymentMonth, AccumOrSocial accumOrSocial) {
        List<SocialSecuritySetting> settings = socialSecuritySettingProvider.listSocialSecuritySetting(detailIds, accumOrSocial);
        List<EhSocialSecurityPayments> payments = new ArrayList<>();
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSocialSecurityPayments.class));
        if (null == settings) {
            return;
        }
        for (SocialSecuritySetting setting : settings) {
            EhSocialSecurityPayments payment = processSocialSecurityPayment(setting, paymentMonth, NormalFlag.NO.getCode());
            payment.setId(id++);
            payments.add(payment);
            payment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            payment.setCreatorUid(UserContext.current().getUser().getId());
            payment.setUpdateTime(payment.getCreateTime());
            payment.setOperatorUid(payment.getCreatorUid());
            payment.setIsFiled(NormalFlag.NO.getCode());
        }
        sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhSocialSecurityPayments.class), payments.size());
        socialSecurityPaymentProvider.batchCreateSocialSecurityPayment(payments);
    }

    private void addEmployeeNewSocialSecuritySettings(Long cityId, String householdTypeName, OrganizationMemberDetails detail) {
        List<SocialSecurityBase> ssBases = socialSecurityBaseProvider.listSocialSecurityBase(cityId,
                householdTypeName, AccumOrSocial.SOCAIL.getCode());
        if (null == ssBases) {
            ssBases = new ArrayList<>();
        }
        List<SocialSecurityBase> afBases = socialSecurityBaseProvider.listSocialSecurityBase(cityId,
                null, AccumOrSocial.ACCUM.getCode());
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
        Set<Long> detailIds = new HashSet<>();
        ListSocialSecurityPaymentsCommand cmd = new ListSocialSecurityPaymentsCommand();
        cmd.setOwnerId(ownerId);
        cmd.setPageSize(Integer.MAX_VALUE - 1);
        cmd.setAccumulationFundStatus(NormalFlag.YES.getCode());
        List<SocialSecurityEmployeeDTO> result = listSocialSecurityEmployees(cmd);
        if (null != result) {
            for (SocialSecurityEmployeeDTO dto : result) {
                detailIds.add(dto.getDetailId());
            }
            createPayments(detailIds, paymentMonth, AccumOrSocial.ACCUM);
        }

        cmd.setSocialSecurityStatus(NormalFlag.YES.getCode());
        cmd.setAccumulationFundStatus(null);
        result = listSocialSecurityEmployees(cmd);
        if (null != result) {
            for (SocialSecurityEmployeeDTO dto : result) {
                detailIds.add(dto.getDetailId());
            }

            createPayments(detailIds, paymentMonth, AccumOrSocial.SOCAIL);
        }
    }

    private Long getZuolinNamespaceCityId(Long cityId) {
        Region currentNSCity = regionProvider.findRegionById(cityId);
        Long id = getZuolinNamespaceCityId(currentNSCity.getName());
        if (null == id) {
            return SocialSecurityConstants.DEFAULT_CITY;
        }
        return id;
    }

    private Long getZuolinNamespaceCityId(String name) {

        Region zuolinCity = regionProvider.findRegionByName(0, name);
        if (null == zuolinCity) {
            return null;
        }
        return zuolinCity.getId();
    }

    private void addNewSocialSecuritySettings(Long cityId, String householdTypeName, Long ownerId) {

        List<SocialSecurityBase> ssBases = socialSecurityBaseProvider.listSocialSecurityBase(cityId,
                householdTypeName, AccumOrSocial.SOCAIL.getCode());
        if (null == ssBases) {
            ssBases = new ArrayList<>();
        }
        List<SocialSecurityBase> afBases = socialSecurityBaseProvider.listSocialSecurityBase(cityId,
                null, AccumOrSocial.ACCUM.getCode());
        if (null != afBases) {
            ssBases.addAll(afBases);
        }
        ListSocialSecurityPaymentsCommand command = new ListSocialSecurityPaymentsCommand(ownerId);
        List<SocialSecurityEmployeeDTO> members = listSocialSecurityEmployees(command);
//        List<Long> detailIds = archivesService.listSocialSecurityEmployees(ownerId, null, null, null);
//        List<OrganizationMemberDetails> details = organizationProvider.listOrganizationMemberDetails(ownerId);
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSocialSecuritySettings.class));
        List<EhSocialSecuritySettings> settings = new ArrayList<>();
        for (SocialSecurityEmployeeDTO dto : members) {
            id = saveSocialSecuritySettings(ssBases, cityId, ownerId, dto.getUserId(), dto.getDetailId(), dto.getNamespaceId(), id, settings);
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
                        detailId, namespaceId, null);
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
        dto.setId(SocialSecurityItem.SOCIALSECURITYUNPAY.getCode());
        dto.setItemName(SocialSecurityItem.SOCIALSECURITYUNPAY.getDescribe());
        items.add(dto);
        dto = new FilterItemDTO();
        dto.setId(SocialSecurityItem.ACCUMULATIONFUND_UNPAY.getCode());
        dto.setItemName(SocialSecurityItem.ACCUMULATIONFUND_UNPAY.getDescribe());
        items.add(dto);
//        dto = new FilterItemDTO();
//        dto.setId(SocialSecurityItem.INCREASE.getCode());
//        dto.setItemName(SocialSecurityItem.INCREASE.getDescribe());
//        items.add(dto);
//        dto = new FilterItemDTO();
//        dto.setId(SocialSecurityItem.DECREASE.getCode());
//        dto.setItemName(SocialSecurityItem.DECREASE.getDescribe());
//        items.add(dto);
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

        this.coordinationProvider.getNamedLock(CoordinationLocks.SOCIAL_SECURITY_LIST_PAYMENTS.getCode() + cmd.getOwnerId()).enter(() -> {
            String month = findPaymentMonth(cmd.getOwnerId());
            if (null == month) {
                //如果没有payments数据,增加一个
                addSocialSecurity(cmd.getOwnerId());
            }
            return null;

        });

//        SsorAfPay payFlag = null;
//        if (null != cmd.getFilterItems()) {
//            for (Byte filterItem : cmd.getFilterItems()) {
//                if (null == filterItem) {
//                    continue;
//                }
//                SocialSecurityItem item = SocialSecurityItem.fromCode(filterItem);
//                switch (item) {
//                    case SOCIALSECURITYUNPAY:
//                        if (payFlag != null && payFlag == SsorAfPay.ACCUMULATIONFUND_UNPAY) {
//                            payFlag = SsorAfPay.BOTHPAY;
//                        } else {
//                            payFlag = SsorAfPay.SOCIALSECURITYUNPAY;
//                        }
//                        break;
//
//                    case ACCUMULATIONFUND_UNPAY:
//                        if (payFlag != null && payFlag == SsorAfPay.SOCIALSECURITYUNPAY) {
//                            payFlag = SsorAfPay.BOTHPAY;
//                        } else {
//                            payFlag = SsorAfPay.ACCUMULATIONFUND_UNPAY;
//                        }
//                        break;
//                }
//            }
//        }
//        if (cmd.getPageAnchor() == null)
//            cmd.setPageAnchor(0L);
        ListSocialSecurityPaymentsResponse response = new ListSocialSecurityPaymentsResponse();
        int pageSize = cmd.getPageSize() == null ? 20 : cmd.getPageSize();
        if (cmd.getPageOffset() == null) cmd.setPageOffset(1);
        List<SocialSecurityEmployeeDTO> result = listSocialSecurityEmployees(cmd);
        LOGGER.debug("楠哥返回的" + result);
//        CrossShardListingLocator locator = new CrossShardListingLocator();
//        locator.setAnchor(cmd.getPageAnchor());
//        List<Long> detailIds = new ArrayList<>();
//        if (null != members) {
//            for (SocialSecurityEmployeeDTO member : members) {
//                detailIds.add(member.getDetailId());
//            }
//        }
//        if (null != cmd.getSocialSecurityCityId()) {
//            detailIds = socialSecuritySettingProvider.listDetailsByCityId(detailIds, cmd.getSocialSecurityCityId(), AccumOrSocial.SOCAIL.getCode());
//
//        }
//        if (null != cmd.getAccumulationFundCityId()) {
//            detailIds = socialSecuritySettingProvider.listDetailsByCityId(detailIds, cmd.getAccumulationFundCityId(), AccumOrSocial.ACCUM.getCode());
//
//        }
//        if (null != payFlag) {
//            if (SsorAfPay.BOTHPAY == payFlag || SsorAfPay.SOCIALSECURITYUNPAY == payFlag) {
//                detailIds = socialSecurityPaymentProvider.listDetailsByPayFlag(detailIds, AccumOrSocial.SOCAIL.getCode());
//            }
//
//            if (SsorAfPay.BOTHPAY == payFlag || SsorAfPay.ACCUMULATIONFUND_UNPAY == payFlag) {
//                detailIds = socialSecurityPaymentProvider.listDetailsByPayFlag(detailIds, AccumOrSocial.ACCUM.getCode());
//            }
//        }

//        List<SocialSecurityPaymentDTO> results = socialSecuritySettingProvider.listSocialSecuritySetting(
//                         cmd.getDeptId(), cmd.getKeywords(),
//                        payFlag, detailIds, locator);
//        List<SocialSecurityPaymentDTO> results = new ArrayList<>();
//        if (null == detailIds) {
//            return null;
//        }
//        Long nextPageAnchor = null;
//        int beginNum = 0;
//        if (null != cmd.getPageAnchor()) {
//            for (Long id : detailIds) {
//                if (id < cmd.getPageAnchor()) {
//                    beginNum++;
//                } else {
//                    break;
//                }
//            }
//        }
//        if (pageSize + beginNum < detailIds.size()) {
//            nextPageAnchor = detailIds.get(pageSize);
//        }
//        for (int i = beginNum; i < beginNum + pageSize && i < detailIds.size(); i++) {
//            SocialSecurityPaymentDTO dto = processSocialSecurityItemDTO(detailIds.get(i), members);
//            results.add(dto);
//        }
        Integer nextPageOffset = null;
        if (result != null && result.size() > pageSize) {
            result.remove(result.size() - 1);
            nextPageOffset = cmd.getPageOffset() + 1;
        }
        response.setNextPageOffset(nextPageOffset);
        response.setSocialSecurityPayments(result.stream().map(this::processSocialSecurityItemDTO).collect(Collectors.toList()));
        response.setPaymentMonth(findPaymentMonth(cmd.getOwnerId()));
        return response;
    }

    private SocialSecurityPaymentDTO processSocialSecurityItemDTO(SocialSecurityEmployeeDTO detail) {
        SocialSecurityPaymentDTO dto = new SocialSecurityPaymentDTO();
        if (null != detail.getCheckInTime()) {
            dto.setEntryDate(detail.getCheckInTime().getTime());
        }
        if (null != detail.getDismissTime()) {
            dto.setOutDate(detail.getDismissTime().getTime());
        }
        dto.setDetailId(detail.getDetailId());
//        OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(dto.getDetailId());
//        SocialSecurityEmployeeDTO detail = findSocialSecurityEmployeeDTO(members, detailId);
//        if (null != detail) {
        dto.setUserName(detail.getContactName());

        String depName = archivesService.convertToOrgNames(archivesService.getEmployeeDepartment(detail.getDetailId()));
        dto.setDeptName(depName);
        dto.setAccumulationFundStatus(detail.getAccumulationFundStatus());
        dto.setSocialSecurityStatus(detail.getSocialSecurityStatus());
        //// TODO: 2017/12/27  入职离职日期
//            dto.setEntryDate(detail.get);
        SocialSecuritySetting accSetting = socialSecuritySettingProvider.findSocialSecuritySettingByDetailIdAndAOS(detail.getDetailId(), AccumOrSocial.ACCUM);
        SocialSecuritySetting socSetting = socialSecuritySettingProvider.findSocialSecuritySettingByDetailIdAndAOS(detail.getDetailId(), AccumOrSocial.SOCAIL);
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
//        } else {
//            dto.setUserName("找不到这个人");
//        }
        return dto;
    }

//    private SocialSecurityEmployeeDTO findSocialSecurityEmployeeDTO(List<SocialSecurityEmployeeDTO> members, Long detailId) {
//        if (null != members) {
//            for (SocialSecurityEmployeeDTO member : members) {
//                if (member.getDetailId().equals(detailId)) {
//                    return member;
//                }
//
//            }
//        }
//        return null;
//    }

    @Override
    public ListSocialSecurityEmployeeStatusResponse listSocialSecurityEmployeeStatus(
            ListSocialSecurityEmployeeStatusCommand cmd) {
        ListSocialSecurityEmployeeStatusResponse response = new ListSocialSecurityEmployeeStatusResponse();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        Integer paySocialSecurityNumber = organizationProvider.queryOrganizationPersonnelCounts(new ListingLocator(), cmd.getOwnerId(), ((locator, query) -> {
            query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.SOCIAL_SECURITY_STATUS.eq(SocialSecurityStatus.PENDING.getCode()));
            return query;
        }));

        Integer accumulationFundNumber = organizationProvider.queryOrganizationPersonnelCounts(new ListingLocator(), cmd.getOwnerId(), ((locator, query) -> {
            query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ACCUMULATION_FUND_STATUS.eq(SocialSecurityStatus.PENDING.getCode()));
            return query;
        }));

        Integer inWorkNumber = 0;
        if (cmd.getSocialSecurityMonth() != null)
            inWorkNumber = organizationProvider.queryOrganizationMemberDetailCounts(new ListingLocator(), cmd.getOwnerId(), (locator, query) -> {
                query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.CHECK_IN_TIME.between(getTheFirstDate(cmd.getSocialSecurityMonth()), getTheLastDate(cmd.getSocialSecurityMonth())));
                return query;
            });

        Integer outWorkNumber = 0;
        if (cmd.getSocialSecurityMonth() != null){
            outWorkNumber = organizationProvider.queryOrganizationMemberDetailCounts(new ListingLocator(), cmd.getOwnerId(), (locator, query) -> {
//                query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.EMPLOYEE_STATUS.eq(EmployeeStatus.DISMISSAL.getCode()));
                query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.DISMISS_TIME.between(getTheFirstDate(cmd.getSocialSecurityMonth()), getTheLastDate(cmd.getSocialSecurityMonth())));
                return query;
            });
        }

        response.setPaySocialSecurityNumber(paySocialSecurityNumber);
        response.setPayAccumulationFundNumber(accumulationFundNumber);
        response.setInWorkNumber(inWorkNumber);
        response.setOutWorkNumber(outWorkNumber);
        return response;
    }

    @Override
    public SocialSecurityPaymentDetailDTO getSocialSecurityRule(GetSocialSecurityRuleCommand cmd) {
        SocialSecurityPaymentDetailDTO response = new SocialSecurityPaymentDetailDTO();
        if (AccumOrSocial.fromCode(cmd.getAccumOrsocial()) == AccumOrSocial.SOCAIL && cmd.getHouseholdType() == null) {
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
        if (null == finalSettings) {
            return null;
        }
        for (SocialSecuritySetting setting : finalSettings) {
            if (AccumOrSocial.ACCUM == AccumOrSocial.fromCode(accumOrSocail) &&
                    AccumOrSocial.ACCUM == AccumOrSocial.fromCode(setting.getAccumOrSocail())) {
                return setting;
            } else if (AccumOrSocial.SOCAIL == AccumOrSocial.fromCode(setting.getAccumOrSocail()) &&
                    setting.getPayItem().equals(payItem)) {
                return setting;
            }
        }
        return null;
    }


    @Override
    public GetSocialSecurityPaymentDetailsResponse getSocialSecurityPaymentDetails(
            GetSocialSecurityPaymentDetailsCommand cmd) {
        GetSocialSecurityPaymentDetailsResponse response = new GetSocialSecurityPaymentDetailsResponse();
        response.setPaymentMonth(findPaymentMonth(cmd.getOwnerId()));
        OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByDetailId(cmd.getDetailId());
        if (null == memberDetail) {
            return response;
        }
        response.setIdNumber(memberDetail.getIdNumber());
        response.setDetailId(memberDetail.getId());
        response.setIsWork(getIsWork(memberDetail.getId(), response.getPaymentMonth()));
        response.setUserName(memberDetail.getContactName());
        response.setSocialSecurityNo(memberDetail.getSocialSecurityNumber());
        //社保本月缴费
        List<SocialSecurityPayment> allPayments = socialSecurityPaymentProvider.listSocialSecurityPayment(cmd.getOwnerId(), cmd.getDetailId());
        List<SocialSecurityPayment> ssPayments = new ArrayList<>();
        List<SocialSecurityPayment> ssafterPayments = new ArrayList<>();
        List<SocialSecurityPayment> afPayments = new ArrayList<>();
        List<SocialSecurityPayment> afafterPayments = new ArrayList<>();
        for (SocialSecurityPayment payment : allPayments) {
            if (AccumOrSocial.fromCode(payment.getAccumOrSocail()) == AccumOrSocial.ACCUM) {
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
        Set<Long> detailIds = new HashSet<>();
        detailIds.add(cmd.getDetailId());
        List<SocialSecuritySetting> ssSettings = socialSecuritySettingProvider.listSocialSecuritySetting(detailIds, AccumOrSocial.SOCAIL);
        List<SocialSecuritySetting> afSettings = socialSecuritySettingProvider.listSocialSecuritySetting(detailIds, AccumOrSocial.ACCUM);
        if (NormalFlag.fromCode(memberDetail.getSocialSecurityStatus()) == NormalFlag.YES) {
            response.setPayCurrentSocialSecurityFlag(NormalFlag.YES.getCode());
        } else {
            response.setPayCurrentSocialSecurityFlag(NormalFlag.NO.getCode());
        }
        response.setSocialSecurityPayment(processSocialSecurityPaymentDetailDTO(ssSettings));
        //社保补缴
        if (ssafterPayments.size() == 0) {
            response.setAfterPaySocialSecurityFlag(NormalFlag.NO.getCode());
        } else {
            response.setAfterPaySocialSecurityFlag(NormalFlag.YES.getCode());
            response.setAfterSocialSecurityPayment(processSocialSecurityPaymentDetailDTO(ssSettings));
        }
        //公积金本月缴费
        if (NormalFlag.fromCode(memberDetail.getAccumulationFundStatus()) == NormalFlag.YES) {
            response.setPayCurrentAccumulationFundFlag(NormalFlag.YES.getCode());
        } else {
            response.setPayCurrentAccumulationFundFlag(NormalFlag.NO.getCode());
        }
        response.setAccumulationFundPayment(processSocialSecurityPaymentDetailDTO(afSettings));
        //公积金补缴
        if (afafterPayments.size() == 0) {
            response.setAfterPayAccumulationFundFlag(NormalFlag.NO.getCode());
        } else {
            response.setAfterPayAccumulationFundFlag(NormalFlag.YES.getCode());
            response.setAfterAccumulationFundPayment(processSocialSecurityPaymentDetailDTO(afSettings));
        }
        return response;
    }

    private String findPaymentMonth(Long ownerId) {
        String month = socialSecurityPaymentProvider.findPaymentMonthByOwnerId(ownerId);
        if (null == month) {
            month = socialSecurityPaymentLogProvider.findPayMonthByOwnerId(ownerId);
            if (null == month) {
                return null;
            }
            Calendar monthCalendar = Calendar.getInstance();
            try {
                monthCalendar.setTime(monthSF.get().parse(month));
            } catch (ParseException e) {
                LOGGER.error("parese month sf error", e);
            }
            monthCalendar.add(Calendar.MONTH, 1);
            month = monthSF.get().format(monthCalendar.getTime());

        }
        return month;
    }

//	private SocialSecurityPaymentDetailDTO processSocialSecurityPaymentDetailDTO(List<AccumulationFundPayment> r) {
//		SocialSecurityPaymentDetailDTO dto = ConvertHelper.convert(r,SocialSecurityPaymentDetailDTO.class);
//		return dto;
//	}

    private SocialSecurityPaymentDetailDTO processSocialSecurityPaymentDetailDTO(List<SocialSecuritySetting> settings) {
        if (null == settings || settings.size() == 0) {
            return null;
        }
        SocialSecurityPaymentDetailDTO dto = new SocialSecurityPaymentDetailDTO();
        dto.setCityId(settings.get(0).getCityId());
        Region city = regionProvider.findRegionById(dto.getCityId());
        if (null != city) dto.setCityName(city.getName());
        dto.setHouseholdType(settings.get(0).getHouseholdType());
        dto.setRadix(settings.get(0).getRadix());
        dto.setItems(settings.stream().map(this::processSocialSecurityItemDTO).collect(Collectors.toList()));
        return dto;
    }

    private SocialSecurityItemDTO processSocialSecurityItemDTO(SocialSecuritySetting setting) {
        SocialSecurityItemDTO dto = ConvertHelper.convert(setting, SocialSecurityItemDTO.class);
        SocialSecurityBase base = socialSecurityBaseProvider.findSocialSecurityBaseByCondition(setting.getCityId(), setting.getHouseholdType(),
                setting.getAccumOrSocail(), setting.getPayItem());
        if (null != base) {
            copyRadixAndRatio(dto, base);
        }
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
        dto.setRatioOptions(base.getRatioOptions());
    }

    @Override
    public void updateSocialSecurityPayment(UpdateSocialSecurityPaymentCommand cmd) {

        dbProvider.execute((TransactionStatus status) -> {
            OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(cmd.getDetailId());
            //社保设置
//            if (NormalFlag.fromCode(cmd.getPayCurrentSocialSecurityFlag()) == NormalFlag.YES) {
            if (null != cmd.getSocialSecurityPayment()) {


                // 查询设置的城市户籍档次的数据规则
                List<SocialSecurityBase> bases = socialSecurityBaseProvider.listSocialSecurityBase(cmd.getSocialSecurityPayment().getCityId(),
                        cmd.getSocialSecurityPayment().getHouseholdType());
                // 校验数据是否合法
                checkSocialSercurity(bases, cmd.getSocialSecurityPayment().getItems());
                // 保存setting表数据
                saveSocialSecuritySettings(cmd.getSocialSecurityPayment(), cmd.getDetailId(), AccumOrSocial.SOCAIL.getCode());
                // 保存当月payments数据
                if (NormalFlag.YES == NormalFlag.fromCode(detail.getSocialSecurityStatus())) {
                    saveSocialSecurityPayment(cmd.getSocialSecurityPayment(), cmd.getDetailId(), NormalFlag.NO.getCode(), AccumOrSocial.SOCAIL.getCode());
                    if (NormalFlag.fromCode(cmd.getAfterPaySocialSecurityFlag()) == NormalFlag.YES) {
                        saveSocialSecurityPayment(cmd.getAfterSocialSecurityPayment(), cmd.getDetailId(), NormalFlag.YES.getCode(), AccumOrSocial.SOCAIL.getCode());
                    }
                }
            }
            //公积金设置
//            if (NormalFlag.fromCode(cmd.getPayCurrentAccumulationFundFlag()) == NormalFlag.YES) {
            if (null != cmd.getAccumulationFundPayment()) {
                // 查询设置的城市户籍档次的数据规则
                List<SocialSecurityBase> bases = socialSecurityBaseProvider.listSocialSecurityBase(cmd.getAccumulationFundPayment().getCityId(), AccumOrSocial.ACCUM.getCode());
                // 校验数据是否合法
                checkSocialSercurity(bases, cmd.getAccumulationFundPayment().getItems());
                // 保存setting表数据
                saveSocialSecuritySettings(cmd.getAccumulationFundPayment(), cmd.getDetailId(), AccumOrSocial.ACCUM.getCode());
                if (NormalFlag.YES == NormalFlag.fromCode(detail.getAccumulationFundStatus())) {
                    // 保存当月payments数据
                    saveSocialSecurityPayment(cmd.getAccumulationFundPayment(), cmd.getDetailId(), NormalFlag.NO.getCode(), AccumOrSocial.ACCUM.getCode());
                    if (NormalFlag.fromCode(cmd.getAfterPaySocialSecurityFlag()) == NormalFlag.YES) {
                        saveSocialSecurityPayment(cmd.getAfterSocialSecurityPayment(), cmd.getDetailId(), NormalFlag.YES.getCode(), AccumOrSocial.ACCUM.getCode());
                    }
                }
            }
            return null;
        });
    }

    private String getPayMonthByOwner(Long ownerId) {

        String paymentMonth = socialSecurityPaymentProvider.findPaymentMonthByOwnerId(ownerId);
        if (null == paymentMonth) {
            paymentMonth = monthSF.get().format(DateHelper.currentGMTTime());
        }
        return paymentMonth;
    }

    private void saveSocialSecurityPayment(SocialSecurityPaymentDetailDTO socialSecurityPayment, Long detailId, Byte afterPay, Byte accumOrSocial) {
        OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
        String paymentMonth = getPayMonthByOwner(detail.getOrganizationId());
        List<SocialSecuritySetting> settings = socialSecuritySettingProvider.listSocialSecuritySetting(detailId);
        for (SocialSecuritySetting setting : settings) {
            SocialSecurityPayment payment = socialSecurityPaymentProvider.findSocialSecurityPayment(detailId, setting.getPayItem(), accumOrSocial);
            if (null == payment) {
                createSocialSecurityPayment(setting, detailId, accumOrSocial, paymentMonth, afterPay);
            } else {
                copyRadixAndRatio(payment, setting);
                socialSecurityPaymentProvider.updateSocialSecurityPayment(payment);
            }
        }
        socialSecurityPaymentProvider.setUserCityAndHTByAccumOrSocial(detailId, accumOrSocial, socialSecurityPayment.getCityId(), socialSecurityPayment.getHouseholdType());
    }

    private void createSocialSecurityPayment(SocialSecuritySetting setting, Long detailId, Byte accumOrSocial, String paymentMonth, Byte afterPay) {
//        SocialSecuritySetting setting = socialSecuritySettingProvider.findSocialSecuritySettingByDetailIdAndItem(detailId, itemDTO, accumOrSocial);
        SocialSecurityPayment payment = processSocialSecurityPayment(setting, paymentMonth, afterPay);
        payment.setDetailId(detailId);
        OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
        payment.setNamespaceId(detail.getNamespaceId());
        payment.setOrganizationId(detail.getOrganizationId());
        payment.setUserId(detail.getTargetId());
        payment.setAccumOrSocail(accumOrSocial);
//        copyRadixAndRatio(payment, itemDTO);
        payment.setIsFiled(NormalFlag.NO.getCode());
        socialSecurityPaymentProvider.createSocialSecurityPayment(payment);

    }

    private SocialSecurityPayment processSocialSecurityPayment(SocialSecurityItemDTO itemDTO, String paymentMonth, Byte afterPay) {
        SocialSecurityPayment payment = ConvertHelper.convert(itemDTO, SocialSecurityPayment.class);
        payment.setPayItem(itemDTO.getPayItem());
        payment.setAfterPayFlag(afterPay);
        payment.setPayMonth(paymentMonth);
        return payment;
    }

    private SocialSecurityPayment processSocialSecurityPayment(SocialSecuritySetting setting, String paymentMonth, Byte afterPay) {
        SocialSecurityPayment payment = ConvertHelper.convert(setting, SocialSecurityPayment.class);
        payment.setPayMonth(paymentMonth);
        payment.setAfterPayFlag(afterPay);
        return payment;
    }

    private void copyRadixAndRatio(SocialSecurityPayment target, SocialSecuritySetting itemDTO) {
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
                detail.getOrganizationId(), detail.getTargetId(), detail.getId(), detail.getNamespaceId(), itemDTO);
        copyRadixAndRatio(setting, socialSecurityPayment, itemDTO);
        socialSecuritySettingProvider.createSocialSecuritySetting(setting);

    }

    private SocialSecuritySetting processSocialSecuritySetting(SocialSecurityBase base, Long cityId, Long orgId, Long userId,
                                                               Long detailId, Integer namespaceId, SocialSecurityItemDTO itemDTO) {
        SocialSecuritySetting setting = ConvertHelper.convert(base, SocialSecuritySetting.class);
        if (null == setting) {
            setting = ConvertHelper.convert(itemDTO, SocialSecuritySetting.class);
            setting.setPayItem(itemDTO.getPayItem());
            setting.setAccumOrSocail(AccumOrSocial.SOCAIL.getCode());
        }
        setting.setCityId(cityId);
        setting.setOrganizationId(orgId);
        setting.setUserId(userId);
        setting.setDetailId(detailId);
        setting.setNamespaceId(namespaceId);
        if (null != base) {
            setting.setCompanyRadix(base.getCompanyRadixMin());
            setting.setEmployeeRadix(base.getEmployeeRadixMin());
            setting.setCompanyRatio(base.getCompanyRatioMin());
            setting.setEmployeeRatio(base.getEmployeeRatioMin());
        }
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
            LOGGER.error("校验不通过 [{}]的企业比例越界 最大值[{}] 最小值[{}] 实际[{}]",
                    itemDTO.getPayItem() == null ? "公积金" : itemDTO.getPayItem(), base.getCompanyRatioMax(),
                    base.getCompanyRatioMin(), itemDTO.getCompanyRatio());
            throw RuntimeErrorException.errorWith(SocialSecurityConstants.SCOPE, SocialSecurityConstants.ERROR_CHECK_SETTING,
                    "校验不通过");
        }
        //检测个人基数边界
        if (!itemDTO.getEmployeeRadix().equals(0) && (base.getEmployeeRadixMax().compareTo(itemDTO.getEmployeeRadix()) < 0 ||
                base.getEmployeeRadixMin().compareTo(itemDTO.getEmployeeRadix()) > 0)) {
            LOGGER.error("校验不通过 [{}]的个人基数越界 最大值[{}] 最小值[{}] 实际[{}]",
                    itemDTO.getPayItem() == null ? "公积金" : itemDTO.getPayItem(), base.getEmployeeRadixMax(),
                    base.getEmployeeRadixMin(), itemDTO.getEmployeeRadix());
            throw RuntimeErrorException.errorWith(SocialSecurityConstants.SCOPE, SocialSecurityConstants.ERROR_CHECK_SETTING,
                    "校验不通过");
        }
        //检测个人比例边界
        if (!itemDTO.getEmployeeRatio().equals(0) && (base.getEmployeeRatioMax() < itemDTO.getEmployeeRatio() ||
                base.getEmployeeRatioMin() > itemDTO.getEmployeeRatio())) {
            LOGGER.error("校验不通过 [{}]的个人比例越界 最大值[{}] 最小值[{}] 实际[{}]",
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

        SocialSecurityBase result = findSSBaseWithOutException(bases, itemDTO);
        if (null == result)
            throw RuntimeErrorException.errorWith(SocialSecurityConstants.SCOPE, SocialSecurityConstants.ERROR_CHECK_SETTING,
                    "校验不通过 没有找到[" + (itemDTO.getPayItem() == null ? "公积金" : itemDTO.getPayItem()) + "]的基础数据 ");
        return result;
    }

    private SocialSecurityBase findSSBaseWithOutException(List<SocialSecurityBase> bases, SocialSecurityItemDTO itemDTO) {
        for (SocialSecurityBase base : bases) {
            if (base.getAccumOrSocail().equals(AccumOrSocial.ACCUM.getCode())) {
                return base;
            }
            if (base.getPayItem().equals(itemDTO.getPayItem())) {
                return base;
            }
        }
        return null;
    }

    @Override
    public ImportFileResponse getImportSocialSecurityPaymentsResult(GetImportSocialSecurityPaymentsResultCommand cmd) {
        return importFileService.getImportFileResult(cmd.getTaskId());
    }

    @Override
    public void exportImportFileFailResults(GetImportFileResultCommand cmd, HttpServletResponse httpResponse) {
        importFileService.exportImportFileFailResultXls(httpResponse, cmd.getTaskId());
    }

    @Override
    public ImportFileTaskDTO importSocialSecurityPayments(ImportSocialSecurityPaymentsCommand cmd, MultipartFile file) {
        // TODO Auto-generated method stub
        ImportFileTask task = new ImportFileTask();
        List resultList = processorExcel(file);
        task.setOwnerType("SOCIAL_OWNER_TYPE");
        task.setOwnerId(cmd.getOwnerId());
        task.setType(ImportFileTaskType.SOCIAL_SERCURITY_PAYMENTS.getCode());
        task.setCreatorUid(UserContext.currentUserId());

        //  调用导入方法
        importFileService.executeTask(new ExecuteImportTaskCallback() {
            @Override
            public ImportFileResponse importFile() {
                ImportFileResponse response = new ImportFileResponse();
//                Map<String, String> title = new HashedMap();
//                title.put("导入社保设置", "导入社保设置");
//                response.setTitle(resultList.get(0));
                //  将 excel 的中的数据读取
                String fileLog = "";
                if (resultList.size() > 0) {
                    //  校验标题，若不合格直接返回错误

                    RowResult title = (RowResult) resultList.get(0);
                    Map<String, String> titleMap = title.getCells();
                    fileLog = checkImportSocialSecurityPaymentsTitle(titleMap);
                    if (!StringUtils.isEmpty(fileLog)) {
                        response.setFileLog(fileLog);
                        return response;
                    }
                }
                batchUpdateSSSettingAndPayments(resultList, cmd.getOwnerId(), fileLog, response);
                return response;
            }

        }, task);
        return ConvertHelper.convert(task, ImportFileTaskDTO.class);
    }

    private List processorExcel(MultipartFile file) {
        try {
            List resultList = PropMrgOwnerHandler.processorExcel(file.getInputStream());
            if (resultList.isEmpty()) {
                LOGGER.error("File content is empty");
                throw RuntimeErrorException.errorWith(SocialSecurityConstants.SCOPE, SocialSecurityConstants.ERROR_FILE_IS_EMPTY,
                        "File content is empty");
            }
            return resultList;
        } catch (IOException e) {
            LOGGER.error("file process excel error ", e);
            e.printStackTrace();
        }

        return null;
    }

    private String checkImportSocialSecurityPaymentsTitle(Map<String, String> map) {

        //  TODO:是否从数据库读取模板
        List<String> module = new ArrayList<>(Arrays.asList("手机", "姓名", "参保城市", "公积金城市", "户籍类型", "社保基数", "公积金基数",
                "公积金企业缴纳比例", "公积金个人缴纳比例", "养老企业比例", "医疗企业比例", "生育企业比例", "工伤企业比例", "失业企业比例", "残障金", "商业保险"));
        //  存储字段来进行校验
        List<String> temp = new ArrayList<String>(map.values());

        for (int i = 0; i < module.size(); i++) {
            if (module.get(i).equals(temp.get(i)))
                continue;
            else {
                return ImportFileErrorType.TITLE_ERROR.getCode();
            }
        }
        return null;
    }

    private void batchUpdateSSSettingAndPayments(List list, Long ownerId, String fileLog, ImportFileResponse response) {
        //
        Set<Long> count = new HashSet<>();
        Set<Long> cover = new HashSet<>();
        RowResult title = (RowResult) list.get(0);
        Map<String, String> titleMap = title.getCells();
        response.setTitle(titleMap);
        response.setLogs(new ArrayList<>());
        for (int i = 1; i < list.size(); i++) {
            RowResult r = (RowResult) list.get(i);
            ImportFileResultLog<Map<String, String>> log = new ImportFileResultLog<>(SocialSecurityConstants.SCOPE);
//            response.getLogs().add(log);
            Map<String, String> data = new HashMap();
            for (Map.Entry<String, String> entry : titleMap.entrySet()) {
                data.put(entry.getKey(), (r.getCells().get(entry.getKey()) == null) ? "" : r.getCells().get(entry.getKey()));
            }
            log.setData(data);
            String userContact = r.getA().trim();
            OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByOrganizationIdAndContactToken(ownerId, userContact);
            if (null == detail) {
//                response.setFileLog("找不到用户: 手机号" + userContact);
                LOGGER.error("can not find organization member ,contact token is " + userContact);
                log.setErrorLog("找不到用户: 手机号" + userContact);
                log.setCode(SocialSecurityConstants.ERROR_CHECK_CONTACT);
                log.setErrorDescription(log.getErrorLog());
                response.getLogs().add(log);
                continue;
            } else {
                count.add(detail.getId());
                String ssCityName = r.getC();
                Long ssCityId = getZuolinNamespaceCityId(ssCityName);
                if(null == ssCityId){
                    LOGGER.error("没有这个城市 " + r.getC() );
                    log.setErrorLog("没有这个户籍城市");
                    log.setCode(SocialSecurityConstants.ERROR_CHECK_SOCIAL_CITY);
                    log.setErrorDescription(log.getErrorLog());
                    response.getLogs().add(log);
                    continue;
                }
                String afCityName = r.getD();
                Long afCItyId = getZuolinNamespaceCityId(afCityName);
                String houseType = r.getE();
                if (null == houseType) {
                    LOGGER.error("没有这个城市 " + r.getC() );
                    log.setErrorLog("没有这个户籍类型");
                    log.setCode(SocialSecurityConstants.ERROR_CHECK_SOCIAL_CITY);
                    log.setErrorDescription(log.getErrorLog());
                    response.getLogs().add(log);
                    continue;
                }
                List<SocialSecurityBase> ssBases = socialSecurityBaseProvider.listSocialSecurityBase(ssCityId, houseType, AccumOrSocial.SOCAIL.getCode());
                if (null == ssBases) {
                    LOGGER.error("没有这个城市或者户籍档次 " + ssCityName + houseType);
                    log.setErrorLog("没有这个户籍城市或者户籍档次");
                    log.setCode(SocialSecurityConstants.ERROR_CHECK_SOCIAL_CITY);
                    log.setErrorDescription(log.getErrorLog());
                    response.getLogs().add(log);
                    continue;
                }
                List<SocialSecurityBase> afBases = socialSecurityBaseProvider.listSocialSecurityBase(afCItyId,
                        null, AccumOrSocial.ACCUM.getCode());
                if (null == afBases) {
                    LOGGER.error("没有这个公积金城市" + afCityName);
                    log.setErrorLog("没有这个公积金城市");
                    log.setCode(SocialSecurityConstants.ERROR_CHECK_ACCUM_CITY);
                    log.setErrorDescription(log.getErrorLog());
                    response.getLogs().add(log);
                    continue;
                }
                importUpdateSetting(detail, ssBases, afBases, r, log, ssCityId, afCItyId, response, houseType, cover);
            }
        }
        response.setTotalCount((long) count.size());
        response.setCoverCount((long) cover.size());
        response.setFailCount((long) response.getLogs().size());
        //设置完后同步一下
        socialSecuritySettingProvider.syncRadixAndRatioToPayments(ownerId);

    }

    private void importUpdateSetting(OrganizationMemberDetails detail, List<SocialSecurityBase> ssBases,
                                     List<SocialSecurityBase> afBases, RowResult r, ImportFileResultLog<Map<String, String>> log, Long ssCityId, Long afCItyId, ImportFileResponse response, String houseType, Set<Long> cover) {
        List<SocialSecuritySetting> settings = socialSecuritySettingProvider.listSocialSecuritySetting(detail.getId());

        // 社保
        String ssRadixString = r.getF();
        BigDecimal ssRadix = null;
        if (StringUtils.isNotBlank(ssRadixString)) {
            ssRadix = new BigDecimal(ssRadixString);
            if (ssRadix.compareTo(new BigDecimal(0)) <= 0) {
                String errorString = "社保基数必须大于0 现在值: " + ssRadixString;
                LOGGER.error(errorString);
                log.setErrorLog(errorString);
                log.setCode(SocialSecurityConstants.ERROR_CHECK_SSRADIX);
                log.setErrorDescription(log.getErrorLog());
                response.getLogs().add(log);
                return;
            }
//            socialSecuritySettingProvider.updateSocialSecuritySettingRadix(detail.getId(), ssRadix);
        }
        List<SocialSecurityItemDTO> dtos = new ArrayList();
        // 公积金
        String afRadixString = r.getG();
        BigDecimal afRadix = null;
        if (StringUtils.isNotBlank(afRadixString)) {
            afRadix = new BigDecimal(afRadixString);
            if (afRadix.compareTo(new BigDecimal(0)) <= 0) {
                String errorString = "公积金基数必须大于0 现在值: " + ssRadixString;
                LOGGER.error(errorString);
                log.setErrorLog(errorString);
                log.setCode(SocialSecurityConstants.ERROR_CHECK_AFRADIX);
                log.setErrorDescription(log.getErrorLog());
                response.getLogs().add(log);
                return;
            }
        }
        String companyString = r.getH();
        String emloyeeString = r.getI();
        addImportItemDTO(dtos, ssRadix, companyString, emloyeeString, AccumOrSocial.ACCUM, "");
        // 养老

        companyString = r.getJ();
        emloyeeString = "";
        addImportItemDTO(dtos, ssRadix, companyString, emloyeeString, AccumOrSocial.SOCAIL, "养老");
        // 医疗
        companyString = r.getK();
        emloyeeString = "";
        addImportItemDTO(dtos, ssRadix, companyString, emloyeeString, AccumOrSocial.SOCAIL, "医疗");
        // 生育
        companyString = r.getL();
        emloyeeString = "";
        addImportItemDTO(dtos, ssRadix, companyString, emloyeeString, AccumOrSocial.SOCAIL, "生育");
        // 工伤
        companyString = r.getM();
        emloyeeString = "";
        addImportItemDTO(dtos, ssRadix, companyString, emloyeeString, AccumOrSocial.SOCAIL, "工伤");
        // 失业
        companyString = r.getN();
        emloyeeString = "";
        addImportItemDTO(dtos, ssRadix, companyString, emloyeeString, AccumOrSocial.SOCAIL, "失业");
        // 残障
        String czRadixString = r.getO();
        BigDecimal czRadix = null;
        if (StringUtils.isNotBlank(czRadixString)) {
            czRadix = new BigDecimal(czRadixString);
            if (czRadix.compareTo(new BigDecimal(0)) < 0) {
                String errorString = "残障金基数必须大于0 现在值: " + czRadixString;
                LOGGER.error(errorString);
                log.setErrorLog(errorString);
                log.setCode(SocialSecurityConstants.ERROR_CHECK_SSRADIX);
                log.setErrorDescription(log.getErrorLog());
                response.getLogs().add(log);
                return;
            }
        }
        addImportItemDTO(dtos, czRadix, "100%", "0%", AccumOrSocial.SOCAIL, "残障金");
        // 商业保险
        String syRadixString = r.getP();
        BigDecimal syRadix = null;
        if (StringUtils.isNotBlank(syRadixString)) {
            syRadix = new BigDecimal(syRadixString);
            if (syRadix.compareTo(new BigDecimal(0)) < 0) {
                String errorString = "商业保险基数必须大于0 现在值: " + syRadixString;
                LOGGER.error(errorString);
                log.setErrorLog(errorString);
                log.setCode(SocialSecurityConstants.ERROR_CHECK_SSRADIX);
                log.setErrorDescription(log.getErrorLog());
                response.getLogs().add(log);
                return;
            }
        }
        addImportItemDTO(dtos, syRadix, "100%", "0%", AccumOrSocial.SOCAIL, "商业保险");

        for (SocialSecurityItemDTO item : dtos) {
            SocialSecuritySetting setting = findSetting(item.getAccumOrSocial(), item.getPayItem(), settings);
            List<SocialSecurityBase> bases = (item.getAccumOrSocial().equals(AccumOrSocial.ACCUM.getCode())) ? afBases : ssBases;
            Long cityId = (item.getAccumOrSocial().equals(AccumOrSocial.ACCUM.getCode())) ? afCItyId : ssCityId;
            BigDecimal radix = (item.getAccumOrSocial().equals(AccumOrSocial.ACCUM.getCode())) ? afRadix : ssRadix;
            SocialSecurityBase base = findSSBaseWithOutException(bases, item);
            if (null == setting) {
                if (item.getCompanyRadix() == null || item.getCompanyRatio() == null) {
                    if (!item.getPayItem().equals("商业保险") && !item.getPayItem().equals("残障金")) {
                        String errorString = "增员的人员基数和比例不能为空";
                        LOGGER.error(errorString);
                        log.setErrorLog(errorString);
                        log.setCode(SocialSecurityConstants.ERROR_CHECK_SSRADIX);
                        log.setErrorDescription(log.getErrorLog());
                        response.getLogs().add(log);
                        return;
                    }
                }
                if (null == base) {
                    setting = ConvertHelper.convert(item, SocialSecuritySetting.class);
                    setting.setCityId(cityId);
                    setting.setOrganizationId(detail.getOrganizationId());
                    setting.setUserId(detail.getTargetId());
                    setting.setDetailId(detail.getId());
                    setting.setAccumOrSocail(item.getAccumOrSocial());
                    setting.setNamespaceId(detail.getNamespaceId());
                } else {
                    setting = processSocialSecuritySetting(base, cityId, detail.getOrganizationId(),
                            detail.getTargetId(), detail.getId(), detail.getNamespaceId(), null);

                }
                setting.setId(null);
            }
            if (null != radix) {
                setting.setRadix(radix);
            }
            //城市和户籍
            if (!item.getAccumOrSocial().equals(AccumOrSocial.ACCUM.getCode())) {
                setting.setHouseholdType(houseType);
            }
            importCalculateRadix(radix, base, item, setting);
            String errorString = null;
            if (null == item.getCompanyRatio()) {
                item.setCompanyRatio(setting.getCompanyRatio());
            }
            errorString = importCalculateCompanyRatio(item.getCompanyRatio(), base, item, setting);

            if (null == item.getEmployeeRatio()) {
                item.setEmployeeRatio(setting.getEmployeeRatio());
            }
            errorString = importCalculateEmployeeRatio(item.getEmployeeRatio(), base, item, setting);

            if (null != errorString) {
                LOGGER.error(errorString);
                log.setErrorLog(errorString);
                log.setCode(SocialSecurityConstants.ERROR_CHECK_AFRADIX);
                response.getLogs().add(log);
                return;
            }
            LOGGER.debug("dto是{} 要更新的setting是{}", item, StringHelper.toJsonString(setting));

//            //增员 目前不改变状态只填充数据
//            if (item.getAccumOrSocial().equals(AccumOrSocial.ACCUM.getCode())
//                    && NormalFlag.YES != NormalFlag.fromCode(detail.getAccumulationFundStatus())) {
//                increseMemberDetail(detail, AccumOrSocial.ACCUM);
//            } else if (item.getAccumOrSocial().equals(AccumOrSocial.SOCAIL.getCode())
//                    && NormalFlag.YES != NormalFlag.fromCode(detail.getSocialSecurityStatus())) {
//                increseMemberDetail(detail, AccumOrSocial.SOCAIL);
//            }
            if (setting.getId() == null) {
                //如果没有id ,说明是新建的setting,同时创建一个payment
                socialSecuritySettingProvider.createSocialSecuritySetting(setting);
                String payMonth = findPaymentMonth(detail.getOrganizationId());
                SocialSecurityPayment payment = processSocialSecurityPayment(setting, payMonth, NormalFlag.NO.getCode());
                socialSecurityPaymentProvider.createSocialSecurityPayment(payment);
            } else {
                cover.add(detail.getId());
                socialSecuritySettingProvider.updateSocialSecuritySetting(setting);
            }
        }

    }

    private String importCalculateEmployeeRatio(Integer employeeRatio, SocialSecurityBase base, SocialSecurityItemDTO itemDTO, SocialSecuritySetting setting) {
        if (null == employeeRatio) {
            employeeRatio = 0;
        }
        if (null != base) {
            if (employeeRatio.compareTo(base.getEmployeeRatioMin()) < 0) {
                setting.setEmployeeRatio(base.getEmployeeRatioMin());
            } else if (employeeRatio.compareTo(base.getEmployeeRatioMax()) > 0) {
                setting.setEmployeeRatio(base.getEmployeeRatioMax());
            } else {
                setting.setEmployeeRatio(employeeRatio);
            }
            if (StringUtils.isNotBlank(base.getRatioOptions())) {
                List<Integer> options = JSONObject.parseArray(base.getRatioOptions(), Integer.class);
                if (options.contains(employeeRatio) || employeeRatio.equals(0)) {
                    //都在options就没问题
                } else {
                    return itemDTO.getPayItem() == null ? "公积金" : itemDTO.getPayItem() + "的比例可选项 万分之: [{" + base.getRatioOptions() + "}]   ";
                }

            }
        } else {
            setting.setEmployeeRatio(employeeRatio);
        }


        return null;
    }

    private String importCalculateCompanyRatio(Integer companyRatio, SocialSecurityBase base, SocialSecurityItemDTO itemDTO, SocialSecuritySetting setting) {
        if (null == companyRatio) {
            companyRatio = 0;
        }
        LOGGER.debug("base : " + StringHelper.toJsonString(base) + " ratio " + companyRatio);
        if (null != base) {
            if (companyRatio.compareTo(base.getCompanyRatioMin()) < 0) {
                setting.setCompanyRatio(base.getCompanyRatioMin());
            } else if (companyRatio.compareTo(base.getCompanyRatioMax()) > 0) {
                setting.setCompanyRatio(base.getCompanyRatioMax());
            } else {
                setting.setCompanyRatio(companyRatio);
            }
            if (StringUtils.isNotBlank(base.getRatioOptions())) {
                List<Integer> options = JSONObject.parseArray(base.getRatioOptions(), Integer.class);
                if (options.contains(companyRatio) || companyRatio.equals(0)) {
                    //都在options就没问题
                } else {
                    return itemDTO.getPayItem() == null ? "公积金" : itemDTO.getPayItem() + "的比例可选项 万分之: [{" + base.getRatioOptions() + "}]   ";
                }

            }
        } else {
            setting.setCompanyRatio(companyRatio);
        }

        return null;
    }

    private void importCalculateRadix(BigDecimal afRadix, SocialSecurityBase base, SocialSecurityItemDTO item, SocialSecuritySetting setting) {
        LOGGER.debug("base : " + StringHelper.toJsonString(base) + " item" + item);
        BigDecimal compayRadix = item.getCompanyRadix();
        BigDecimal employeeRadix = item.getEmployeeRadix();
        if (null != base && compayRadix.intValue() != 0) {
            if (compayRadix.compareTo(base.getCompanyRadixMin()) < 0) {
                setting.setCompanyRadix(base.getCompanyRadixMin());
                setting.setRadix(setting.getCompanyRadix());
            } else if (compayRadix.compareTo(base.getCompanyRadixMax()) > 0) {
                setting.setCompanyRadix(base.getCompanyRadixMax());
                setting.setRadix(setting.getCompanyRadix());
            } else {
                setting.setCompanyRadix(compayRadix);
            }
        } else {
            setting.setCompanyRadix(compayRadix);
        }
        if (null != base && employeeRadix.intValue() != 0) {
            if (employeeRadix.compareTo(base.getEmployeeRadixMin()) < 0) {
                setting.setEmployeeRadix(base.getEmployeeRadixMin());
            } else if (employeeRadix.compareTo(base.getEmployeeRadixMax()) > 0) {
                setting.setEmployeeRadix(base.getEmployeeRadixMax());
            } else {
                setting.setEmployeeRadix(employeeRadix);
            }
        } else {
            setting.setEmployeeRadix(employeeRadix);
        }
    }

    private void addImportItemDTO(List<SocialSecurityItemDTO> dtos, BigDecimal radix, String companyString, String emloyeeString, AccumOrSocial accumOrSocail, String payItem) {
        if (StringUtils.isNotBlank(companyString) || StringUtils.isNotBlank(emloyeeString) || null != radix) {
            SocialSecurityItemDTO dto = new SocialSecurityItemDTO();
            dto.setAccumOrSocial(accumOrSocail.getCode());
            dto.setPayItem(payItem);
            Integer companyRatio = null;
            Integer employeeRatio = null;
            if (StringUtils.isNotBlank(companyString)) {
//                LOGGER.debug("companyString is " + companyString);
                companyRatio = new BigDecimal(companyString.replace("%", "")).multiply(new BigDecimal(100)).intValue();
            }
            if (StringUtils.isNotBlank(emloyeeString)) {
//                LOGGER.debug("companyString is " + emloyeeString);
                employeeRatio = new BigDecimal(emloyeeString.replace("%", "")).multiply(new BigDecimal(100)).intValue();
            }
            dto.setCompanyRatio(companyRatio);
            dto.setEmployeeRatio(employeeRatio);
            dto.setCompanyRadix(radix);
            dto.setEmployeeRadix(radix);
            dtos.add(dto);
        }
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
        String month = findPaymentMonth(ownerId);
        socialSecurityInoutReportProvider.deleteSocialSecurityInoutReportByMonth(ownerId, month, NormalFlag.NO.getCode());
        List<Byte> socialInouts = new ArrayList<>();
        socialInouts.add(InOutLogType.ACCUMULATION_FUND_IN.getCode());
        socialInouts.add(InOutLogType.ACCUMULATION_FUND_OUT.getCode());
        socialInouts.add(InOutLogType.SOCIAL_SECURITY_IN.getCode());
        socialInouts.add(InOutLogType.SOCIAL_SECURITY_OUT.getCode());
        List<Long> detailIds = socialSecurityInoutLogProvider.listSocialSecurityInoutLogDetailIds(ownerId, month, socialInouts);
        if (detailIds != null) {
            for (Long detailId : detailIds) {
                OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
                if(null != detail){
                	SocialSecurityInoutReport report = createInoutReport(detail, month);
                }
            }
        }
//        if (outDetails != null) {
//            for (Long detailId : outDetails) {
//                OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
//                SocialSecurityInoutReport report = createInoutReport(detail, month, InOutFlag.DECRE, AccumOrSocial.SOCAIL);
//            }
//        }
//
//        inDetails = socialSecurityInoutLogProvider.listSocialSecurityInoutLogDetailIds(ownerId, month, InOutLogType.ACCUMULATION_FUND_IN);
//        outDetails = socialSecurityInoutLogProvider.listSocialSecurityInoutLogDetailIds(ownerId, month, InOutLogType.ACCUMULATION_FUND_OUT);
//        if (inDetails != null) {
//            for (Long detailId : inDetails) {
//                OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
//                SocialSecurityInoutReport report = createInoutReport(detail, month, InOutFlag.INCRE, AccumOrSocial.ACCUM);
//            }
//        }
//        if (outDetails != null) {
//            for (Long detailId : outDetails) {
//                OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
//                SocialSecurityInoutReport report = createInoutReport(detail, month, InOutFlag.DECRE, AccumOrSocial.ACCUM);
//            }
//        }
    }

    private SocialSecurityInoutReport createInoutReport(OrganizationMemberDetails detail, String month) {
        SocialSecurityReport ssReport = socialSecurityReportProvider.findSocialSecurityReportByDetailId(detail.getId(), month);
        SocialSecurityInoutReport report = new SocialSecurityInoutReport();

        if (null == ssReport) {
//            LOGGER.error("can not find report : detail:{}{},month:{}", detail.getId(), detail.getContactName(), month);
//            return null;
            ssReport = newSocialSecurityReport(detail);
            ssReport.setPayMonth(month);
        }
        report = ConvertHelper.convert(ssReport, SocialSecurityInoutReport.class);
        if (null != ssReport.getAfterSocialSecurityCompanySum()) {
            report.setSocialSecurityAfter(ssReport.getAfterSocialSecurityCompanySum().add(ssReport.getAfterSocialSecurityEmployeeSum()));
        }
        if (null != ssReport.getAfterAccumulationFundCompanySum()) {
            report.setAccumulationFundAfter(ssReport.getAfterAccumulationFundCompanySum().add(ssReport.getAfterAccumulationFundEmployeeSum()));
        }
        List<SocialSecurityInoutLog> inoutLogs = socialSecurityInoutLogProvider.listSocialSecurityInoutLogs(detail.getOrganizationId(),
                detail.getId(), month);

        for (SocialSecurityInoutLog log : inoutLogs) {
            //通过INOUTLOG的type 和当前用户社保公积金状态比较,确定这个月计算他的增员还是减员
            LOGGER.debug("log type :{} ,detail status ss {}  af {} ", log.getType(), detail.getSocialSecurityStatus(), detail.getAccumulationFundStatus());
            if (log.getType().equals(InOutLogType.ACCUMULATION_FUND_IN.getCode())
                   // && (NormalFlag.YES == NormalFlag.fromCode(detail.getAccumulationFundStatus()))
            ) {
                report.setAccumulationFundIncrease(month);
            } else if (log.getType().equals(InOutLogType.SOCIAL_SECURITY_IN.getCode())
//                    && (NormalFlag.YES == NormalFlag.fromCode(detail.getSocialSecurityStatus()))
                    ) {
                report.setSocialSecurityIncrease(month);
            } else if (log.getType().equals(InOutLogType.SOCIAL_SECURITY_OUT.getCode())
//                    && (NormalFlag.NO == NormalFlag.fromCode(detail.getSocialSecurityStatus()))
                    ) {
                report.setSocialSecurityDecrease(month);
            } else if (log.getType().equals(InOutLogType.ACCUMULATION_FUND_OUT.getCode())
//                    && (NormalFlag.NO == NormalFlag.fromCode(detail.getAccumulationFundStatus()))
                    ) {
                report.setAccumulationFundDecrease(month);
            }
        }
//        if (inOutFlag == InOutFlag.INCRE) {
//            if (AccumOrSocial.SOCAIL == accumOrSocial) {
//            } else {
//            }
//        } else if (inOutFlag == InOutFlag.DECRE) {
//
////            if (AccumOrSocial.SOCAIL == accumOrSocial) {
////                report.setSocialSecurityDecrease(ssReport.getSocialSecuritySum());
////            } else {
////                report.setAccumulationFundDecrease(ssReport.getAccumulationFundSum());
////            }
//        }
        report.setIsFiled(NormalFlag.NO.getCode());
        socialSecurityInoutReportProvider.createSocialSecurityInoutReport(report);
        return report;
    }


    private void calculateSocialSecurityDptReports(Long ownerId) {
        String month = findPaymentMonth(ownerId);
        socialSecurityDepartmentSummaryProvider.deleteSocialSecurityDptReports(ownerId, month, NormalFlag.NO.getCode());
        List<Organization> orgs = findOrganizationDpts(ownerId);
        for (Organization dpt : orgs) {
            if (null == dpt) {
                continue;
            }
            calculateSocialSecurityDptReports(ownerId, dpt, month);
        }

    }

    private void calculateSocialSecurityDptReports(Long ownerId, Organization dpt, String month) {
//        List<Organization> dptOrgs = findOrganizationDpts(dpt);
//        CrossShardListingLocator locator = new CrossShardListingLocator();
//        List<Long> orgIds = new ArrayList<Long>();
//        orgIds.add(dpt.getId());
//        for (Organization o : dptOrgs) {
//            orgIds.add(o.getId());
//        }
//        List<OrganizationMember> organizationMembers = this.organizationProvider.listOrganizationPersonnels(null, orgIds,
//                OrganizationMemberStatus.ACTIVE.getCode(), null, locator, Integer.MAX_VALUE - 1);
        ListSocialSecurityPaymentsCommand cmd = new ListSocialSecurityPaymentsCommand();
        Long dptId = null;
        if(!dpt.getId().equals(ownerId)){
        	dptId = dpt.getId();	
        }
        cmd.setPageSize(Integer.MAX_VALUE - 1);
        List<OrganizationMemberDetails> records = archivesService.queryArchivesEmployees(new ListingLocator(), ownerId, dptId, (locator, query) -> {
            query.addOrderBy(Tables.EH_ORGANIZATION_MEMBER_DETAILS.CHECK_IN_TIME.desc()); 
            return query;
        });
        List<Long> detailIds = new ArrayList<>();
        if (null != records) {
            for (OrganizationMemberDetails member : records) {
                if (null != member.getId()) {
                    detailIds.add(member.getId());
                }
            }
        }
//        LOGGER.error("开始计算部门-{} 的数据,\n下辖部门id {},\n人员id{}", dpt.getName(), orgIds, detailIds);
        SocialSecurityDepartmentSummary summary = processSocialSecurityDepartmentSummary(dpt, detailIds, month);
        socialSecurityDepartmentSummaryProvider.createSocialSecurityDepartmentSummary(summary);
    }

    private SocialSecurityDepartmentSummary processSocialSecurityDepartmentSummary(Organization dpt, List<Long> detailIds, String month) {
        SocialSecurityDepartmentSummary summary = new SocialSecurityDepartmentSummary();
        if (detailIds != null && detailIds.size() > 0) {
            summary = socialSecurityReportProvider.calculateSocialSecurityDepartmentSummary(detailIds, month);
        }
        if (null == summary) {
            summary = new SocialSecurityDepartmentSummary();
        }
        summary.setIsFiled(NormalFlag.NO.getCode());
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
        socialSecurityReportProvider.deleteSocialSecurityReports(ownerId, payments.get(0).getPayMonth(), NormalFlag.NO.getCode());
        List<OrganizationMemberDetails> details = organizationProvider.listOrganizationMemberDetails(ownerId);
        for (OrganizationMemberDetails detail : details) {
            if (NormalFlag.YES != NormalFlag.fromCode(detail.getAccumulationFundStatus()) &&
                    NormalFlag.YES != NormalFlag.fromCode(detail.getSocialSecurityStatus())) {
                continue;
            }
            SocialSecurityReport report = calculateUserSocialSecurityReport(detail, payments);
            if (null != report) {
                socialSecurityReportProvider.createSocialSecurityReport(report);
            }
        }
    }

    private Byte getIsWork(Long detailId, String payMonth) {
        SocialSecurityEmployeeDTO dto = getSocialSecurityEmployeeInfo(detailId);
        //TODO: 这里以后也要改
        if (dto.getDismissTime() != null && Integer.valueOf(payMonth) >=
                Integer.valueOf(monthSF.get().format(dto.getDismissTime()))) {
            return IsWork.IS_OUT.getCode();
        } else {
            if (null != dto.getCheckInTime() &&
                    Integer.valueOf(monthSF.get().format(dto.getCheckInTime())).equals(payMonth)) {
                return IsWork.IS_NEW.getCode();
            } else {
                return IsWork.NORMAL.getCode();
            }
        }
    }

    /**
     * 计算社保报表
     */
    private SocialSecurityReport calculateUserSocialSecurityReport(OrganizationMemberDetails detail, List<SocialSecurityPayment> payments) {
        SocialSecurityReport report = newSocialSecurityReport(detail);
        List<SocialSecurityPayment> userPayments = findSSpaymentsByDetail(detail.getId(), payments);
        if (null == userPayments || userPayments.size() == 0) {
            return null;
        }
        report.setIsFiled(NormalFlag.NO.getCode());
        report.setPayMonth(userPayments.get(0).getPayMonth());
        report.setCreatorUid(userPayments.get(0).getCreatorUid());
        report.setCreateTime(userPayments.get(0).getCreateTime());
        report.setFileUid(userPayments.get(0).getFileUid());
        report.setFileTime(userPayments.get(0).getFileTime());
        report.setIsWork(getIsWork(detail.getId(), report.getPayMonth()));
        for (SocialSecurityPayment userPayment : userPayments) {
            report.setCreatorUid(userPayment.getCreatorUid());
            report.setCreateTime(userPayment.getCreateTime());
            report.setFileUid(userPayment.getFileUid());
            report.setFileTime(userPayment.getFileTime());
            //统计公积金
            if (AccumOrSocial.ACCUM == AccumOrSocial.fromCode(userPayment.getAccumOrSocail())) {
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
                if (userPayment.getPayItem() == null) {
                    LOGGER.error("脏数据, payitem = null" + userPayment);
                    continue;
                }
                report.setSocialSecurityCompanySum(calculateAmount(userPayment.getCompanyRadix(), userPayment.getCompanyRatio()
                        , report.getSocialSecurityCompanySum()));
                report.setSocialSecurityEmployeeSum(calculateAmount(userPayment.getEmployeeRadix(), userPayment.getEmployeeRatio()
                        , report.getSocialSecurityEmployeeSum()));
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
                switch (PayItem.fromCode(userPayment.getPayItem())) {
                    case DISABLE:
                        report.setDisabilitySum(userPayment.getCompanyRadix());
//                        report.setDisabilitySum(calculateAmount(userPayment.getCompanyRadix(), userPayment.getCompanyRatio())
//                                .add(calculateAmount(userPayment.getEmployeeRadix(), userPayment.getEmployeeRatio(), report.getDisabilitySum())));
                        break;
                    case BUSINESS:
                        report.setCommercialInsurance(userPayment.getCompanyRadix());
//                        report.setCommercialInsurance(calculateAmount(userPayment.getCompanyRadix(), userPayment.getCompanyRatio())
//                                .add(calculateAmount(userPayment.getEmployeeRadix(), userPayment.getEmployeeRatio(), report.getCommercialInsurance())));
                        break;
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
        ratio = (ratio == null) ? 0 : ratio;
        radix = (radix == null) ? new BigDecimal(0) : radix;
        return radix.multiply(new BigDecimal(ratio)).divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_UP)
                .add(addSum == null ? new BigDecimal(0) : addSum);
    }

    private SocialSecurityReport newSocialSecurityReport(OrganizationMemberDetails detail) {
        SocialSecurityReport report = ConvertHelper.convert(detail, SocialSecurityReport.class);
        report.setDetailId(detail.getId());
        report.setUserId(detail.getTargetId());
        report.setUserName(detail.getContactName());
        report.setEntryDate(detail.getCheckInTime());
        String depName = archivesService.convertToOrgNames(archivesService.getEmployeeDepartment(detail.getId()));
        report.setDeptName(depName);
        List<SocialSecuritySetting> settings = socialSecuritySettingProvider.listSocialSecuritySetting(detail.getId());
        if (null != settings) {
            for (SocialSecuritySetting setting : settings) {
                if (AccumOrSocial.ACCUM == AccumOrSocial.fromCode(setting.getAccumOrSocail())) {
                    report.setAccumulationFundCityId(setting.getCityId());
                    Region city = regionProvider.findRegionById(setting.getCityId());
                    if (null != city) {
                        report.setAccumulationFundCityName(city.getName());
                    }
                    report.setAccumulationFundRadix(setting.getRadix());
                } else {
                    report.setHouseholdType(setting.getHouseholdType());
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
        List<SocialSecurityReport> result = socialSecurityReportProvider.listFiledSocialSecurityReport(cmd.getOwnerId(),
                cmd.getPayMonth(), locator, pageSize + 1);
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
        if (null != r.getEntryDate()) {
            dto.setEntryDate(r.getEntryDate().getTime());
        }
        if (null != r.getOutWorkDate()) {
            dto.setOutWorkDate(r.getOutWorkDate().getTime());
        }
        return dto;
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

    private String checkNullInteger(Integer a) {
        if (a == null) {
            return "";
        }
        return a.toString();
    }

    private XSSFSheet setNewSocialSecurityReportRow(XSSFSheet sheet, SocialSecurityReport r) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        int i = -1;
        row.createCell(++i).setCellValue(r.getUserName());
        row.createCell(++i).setCellValue(r.getEntryDate() == null ? "" : dateSF.get().format(r.getEntryDate()));
        row.createCell(++i).setCellValue(r.getContactToken());
        row.createCell(++i).setCellValue(r.getIdNumber());
        row.createCell(++i).setCellValue(r.getDegree());
        row.createCell(++i).setCellValue(r.getSalaryCardBank());
        row.createCell(++i).setCellValue(r.getSalaryCardNumber());
        row.createCell(++i).setCellValue(r.getDeptName());
        row.createCell(++i).setCellValue(r.getSocialSecurityNumber());
        row.createCell(++i).setCellValue(r.getProvidentFundNumber());
        row.createCell(++i).setCellValue(r.getOutWorkDate() == null ? "" : dateSF.get().format(r.getOutWorkDate()));
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
        row.createCell(++i).setCellValue(checkNullRatio(r.getAccumulationFundCompanyRatio()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getAccumulationFundEmployeeRadix()));
        row.createCell(++i).setCellValue(checkNullRatio(r.getAccumulationFundEmployeeRatio()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getAccumulationFundSum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getAccumulationFundCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getAccumulationFundEmployeeSum()));
//        row.createCell(++i).setCellValue("");
        //养老
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getPensionCompanyRadix()));
        row.createCell(++i).setCellValue(checkNullRatio(r.getPensionCompanyRatio()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getPensionCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getPensionEmployeeRadix()));
        row.createCell(++i).setCellValue(checkNullRatio(r.getPensionEmployeeRatio()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getPensionEmployeeSum()));
        //失业
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getUnemploymentCompanyRadix()));
        row.createCell(++i).setCellValue(checkNullRatio(r.getUnemploymentCompanyRatio()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getUnemploymentCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getUnemploymentEmployeeRadix()));
        row.createCell(++i).setCellValue(checkNullRatio(r.getUnemploymentEmployeeRatio()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getUnemploymentEmployeeSum()));
        //医疗
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getMedicalCompanyRadix()));
        row.createCell(++i).setCellValue(checkNullRatio(r.getMedicalCompanyRatio()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getMedicalCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getMedicalEmployeeRadix()));
        row.createCell(++i).setCellValue(checkNullRatio(r.getMedicalEmployeeRatio()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getMedicalEmployeeSum()));
        //工伤
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getInjuryCompanyRadix()));
        row.createCell(++i).setCellValue(checkNullRatio(r.getInjuryCompanyRatio()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getInjuryCompanySum()));
        //生育
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getBirthCompanyRadix()));
        row.createCell(++i).setCellValue(checkNullRatio(r.getBirthCompanyRatio()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getBirthCompanySum()));
        //大病
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getCriticalIllnessCompanyRadix()));
        row.createCell(++i).setCellValue(checkNullRatio(r.getCriticalIllnessCompanyRatio()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getCriticalIllnessCompanySum()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getCriticalIllnessEmployeeRadix()));
        row.createCell(++i).setCellValue(checkNullRatio(r.getCriticalIllnessEmployeeRatio()));
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

    private String checkNullRatio(Integer ratio) {
        if (null == ratio) {
            return "";
        }

        return new BigDecimal(ratio).divide(new BigDecimal(100.00),2, BigDecimal.ROUND_HALF_UP).toString()+"%";
    }

    private String checkNull(Object o) {
        if (o == null) {
            return "";
        }
        return o.toString();
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
//        row.createCell(++i).setCellValue("公积金需纳税额");
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
        List<SocialSecurityDepartmentSummary> result = socialSecurityDepartmentSummaryProvider.listFiledSocialSecurityDepartmentSummary(cmd.getOwnerId(),
                cmd.getPayMonth(), locator, pageSize + 1);
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
    public void exportSocialSecurityReports(ExportSocialSecurityReportsCommand cmd) {

        // TODO 导出
        Map<String, Object> params = new HashMap();

        //如果是null的话会被传成“null”
        params.put("ownerId", cmd.getOwnerId());
        params.put("payMonth", cmd.getPayMonth());
        params.put("reportType", "exportSocialSecurityReports");
        String fileName = String.format("%s社保报表.xlsx", cmd.getPayMonth());

        taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), SocialSecurityReportsTaskHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new Date());

    }

    @Override
    public OutputStream getSocialSecurityReportsOutputStream(Long ownerId, String payMonth) {
        List<SocialSecurityReport> result = socialSecurityReportProvider.listFiledSocialSecurityReport(ownerId,
                payMonth, null, Integer.MAX_VALUE - 1);
        XSSFWorkbook workbook = createSocialSecurityReportWorkBook(result);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            workbook.write(out);
        } catch (IOException e) {
            LOGGER.error("something woring with build output stream");
            e.printStackTrace();
        }
        return out;

    }

    @Override
    public OutputStream getSocialSecurityDepartmentSummarysOutputStream(Long ownerId, String payMonth) {
        List<SocialSecurityDepartmentSummary> result = socialSecurityDepartmentSummaryProvider.listFiledSocialSecurityDepartmentSummary(
                ownerId, payMonth, null, Integer.MAX_VALUE - 1);
        Workbook workbook = createSocialSecurityDepartmentSummarysWorkBook(result);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            workbook.write(out);
        } catch (IOException e) {
            LOGGER.error("something woring with build output stream");
            e.printStackTrace();
        }
        return out;

    }

    @Override
    public OutputStream getSocialSecurityInoutReportsOutputStream(Long ownerId, String payMonth) {

        List<SocialSecurityInoutReport> result = socialSecurityInoutReportProvider.listFiledSocialSecurityInoutReport(
                ownerId, payMonth, null, Integer.MAX_VALUE - 1);
        Workbook workbook = createSocialSecurityInoutReportsWorkBook(result);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            workbook.write(out);
        } catch (IOException e) {
            LOGGER.error("something woring with build output stream");
            e.printStackTrace();
        }
        return out;

    }

    @Override
    public void exportSocialSecurityInoutReports(ExportSocialSecurityInoutReportsCommand cmd) {
// TODO: 2018/1/2 导出
        // TODO 导出
        Map<String, Object> params = new HashMap();

        //如果是null的话会被传成“null”
        params.put("ownerId", cmd.getOwnerId());
        params.put("payMonth", cmd.getPayMonth());
        params.put("reportType", "exportSocialSecurityInoutReports");
        String fileName = String.format("%s社保增减表.xlsx", cmd.getPayMonth());

        taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), SocialSecurityReportsTaskHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new Date());

    }

    @Override
    public void exportSocialSecurityDepartmentSummarys(
            ExportSocialSecurityDepartmentSummarysCommand cmd) {
        // TODO 导出
        Map<String, Object> params = new HashMap();

        //如果是null的话会被传成“null”
        params.put("ownerId", cmd.getOwnerId());
        params.put("payMonth", cmd.getPayMonth());
        params.put("reportType", "exportSocialSecurityDepartmentSummarys");
        String fileName = String.format("%s社保部门汇总报表.xlsx", cmd.getPayMonth());

        taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), SocialSecurityReportsTaskHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new Date());

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
        List<SocialSecurityInoutReport> result = socialSecurityInoutReportProvider.listFiledSocialSecurityInoutReport(cmd.getOwnerId(),
                cmd.getPayMonth(), locator, pageSize + 1);
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
        if (null != r.getEntryDate()) {
            dto.setEntryDate(r.getEntryDate().getTime());
        }
        if (null != r.getOutWorkDate()) {
            dto.setOutWorkDate(r.getOutWorkDate().getTime());
        }
        return dto;
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
        row.createCell(++i).setCellValue(r.getEntryDate() == null ? "" : dateSF.get().format(r.getEntryDate()));
        row.createCell(++i).setCellValue(r.getOutWorkDate() == null ? "" : dateSF.get().format(r.getOutWorkDate()));
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
        row.createCell(++i).setCellValue(r.getSocialSecurityIncrease());
        row.createCell(++i).setCellValue(r.getSocialSecurityDecrease());
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getSocialSecurityAfter()));
        row.createCell(++i).setCellValue(checkNullBigDecimal(r.getAccumulationFundRadix()));
        row.createCell(++i).setCellValue(r.getAccumulationFundCityName());
        row.createCell(++i).setCellValue(r.getAccumulationFundIncrease());
        row.createCell(++i).setCellValue(r.getAccumulationFundDecrease());
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
    public FileSocialSecurityResponse fileSocialSecurity(FileSocialSecurityCommand cmd) {

        //归档-这个时候就计算归档表了
        List<SocialSecurityPayment> payments = socialSecurityPaymentProvider.listSocialSecurityPayment(cmd.getOwnerId());

        if (null == payments) {
            throw RuntimeErrorException.errorWith(SocialSecurityConstants.SCOPE, SocialSecurityConstants.ERROR_NO_PAYMENTS,
                    "没有当月缴费数据!");
        }
        String paymonth = payments.get(0).getPayMonth();
        if (!paymonth.equals(cmd.getPayMonth())) {
            throw RuntimeErrorException.errorWith(SocialSecurityConstants.SCOPE, SocialSecurityConstants.ERROR_NO_PAYMENTS,
                    "归档月份错误! 月份[" + paymonth + "],参数[" + cmd + "]");
        }
        String seed = SocialSecurityConstants.SCOPE + "fileSocialSecurity" + cmd.getOwnerId() + DateHelper.currentGMTTime().getTime();
        String key = Base64.getEncoder().encodeToString(seed.getBytes());
        ValueOperations<String, String> valueOperations = getValueOperations(key);
        int timeout = 15;
        TimeUnit unit = TimeUnit.MINUTES;
        Long userId = UserContext.currentUserId();
        // 先放一个和key一样的值,表示这个人key有效
        valueOperations.set(key, key, timeout, unit);
        //线程池中处理计算规则
        calculateExecutorPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    calculateReports(cmd.getOwnerId());
                    fileSocialSecurity(cmd.getOwnerId(), cmd.getPayMonth(), payments, userId);
                } catch (Exception e) {
                    LOGGER.error("calculate reports error!! cmd is  :" + cmd, e);
                } finally {
                    //处理完成删除key,表示这个key已经完成了
                    deleteValueOperations(key);
                }
            }
        });
        return new FileSocialSecurityResponse(key);

    }

    private void fileSocialSecurity(Long ownerId, String payMonth, List<SocialSecurityPayment> payments, Long userId) {
        //删除之前当月的归档表
        LOGGER.debug("开始归档报表");
        Timestamp fileTime = new Timestamp(DateHelper.currentGMTTime().getTime());
        socialSecurityPaymentLogProvider.deleteMonthLog(ownerId, payMonth);
        Long id = sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhSocialSecurityPaymentLogs.class), payments.size() + 1);
        List<EhSocialSecurityPaymentLogs> logs = new ArrayList<>();
        for (SocialSecurityPayment payment : payments) {
            EhSocialSecurityPaymentLogs paymentLog = ConvertHelper.convert(payment, EhSocialSecurityPaymentLogs.class);
            paymentLog.setId(id++);
            logs.add(paymentLog);
//            socialSecurityPaymentLogProvider.createSocialSecurityPaymentLog(paymentLog);
        }
        socialSecurityPaymentLogProvider.batchCreateSocialSecurityPaymentLog(logs);
        LOGGER.debug("开始计算汇总表");

        //归档汇总表
        socialSecuritySummaryProvider.deleteSocialSecuritySummary(ownerId, payMonth);
        socialSecurityPaymentProvider.updateSocialSecurityPaymentFileStatus(ownerId, userId);
        SocialSecuritySummary summary = socialSecurityPaymentProvider.calculateSocialSecuritySummary(ownerId, payMonth);
        LOGGER.debug("sumary = " + StringHelper.toJsonString(summary));
        summary.setCreatorUid(userId);
        socialSecuritySummaryProvider.createSocialSecuritySummary(summary);
        //更新归档状态
        //三个报表删除归档状态的表,再把未归档状态的表复制一份归档状态的
        socialSecurityReportProvider.deleteSocialSecurityReports(ownerId, payMonth, NormalFlag.YES.getCode());
        socialSecurityDepartmentSummaryProvider.deleteSocialSecurityDptReports(ownerId, payMonth, NormalFlag.YES.getCode());
        socialSecurityInoutReportProvider.deleteSocialSecurityInoutReportByMonth(ownerId, payMonth, NormalFlag.YES.getCode());

        List<SocialSecurityDepartmentSummary> dptSums = socialSecurityDepartmentSummaryProvider.listSocialSecurityDepartmentSummary(ownerId, payMonth, NormalFlag.NO.getCode());
        if (null != dptSums) {
            for (SocialSecurityDepartmentSummary summary1 : dptSums) {
                summary1.setIsFiled(NormalFlag.YES.getCode());
                summary1.setFileTime(fileTime);
                summary1.setFileUid(userId);
                socialSecurityDepartmentSummaryProvider.createSocialSecurityDepartmentSummary(summary1);
            }
        }
        List<SocialSecurityReport> reports = socialSecurityReportProvider.listSocialSecurityReport(ownerId, payMonth, NormalFlag.NO.getCode());
        if (reports != null) {
            for (SocialSecurityReport report1 : reports) {
                report1.setIsFiled(NormalFlag.YES.getCode());
                report1.setFileTime(fileTime);
                report1.setFileUid(userId);
                socialSecurityReportProvider.createSocialSecurityReport(report1);
            }
        }
        List<SocialSecurityInoutReport> inouts = socialSecurityInoutReportProvider.listSocialSecurityInoutReport(ownerId, payMonth, NormalFlag.NO.getCode());
        if (inouts != null) {
            for (SocialSecurityInoutReport inout : inouts) {
                inout.setIsFiled(NormalFlag.YES.getCode());
                inout.setFileTime(fileTime);
                inout.setFileUid(userId);
                socialSecurityInoutReportProvider.createSocialSecurityInoutReport(inout);
            }
        }
        SocialSecurityGroup group = socialSecurityGroupProvider.findSocialSecurityGroupByOrg(ownerId, payMonth);
        if (null == group) {
            group = newSocialSecurityGroup(ownerId, payMonth);
        }
        group.setFileUid(userId);
        group.setFileTime(fileTime);
        group.setIsFiled(NormalFlag.YES.getCode());
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
                cmd.getPayMonth(), locator, pageSize + 1);
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
        if (null != r.getCreateTime()) {
            dto.setCreateTime(r.getCreateTime().getTime());
        }
        if (null != r.getFileTime()) {
            dto.setFileTime(r.getFileTime().getTime());
        }
        OrganizationMember detail = organizationProvider.findOrganizationMemberByUIdAndOrgId(r.getCreatorUid(), r.getOrganizationId());
        if (null != detail) {
            dto.setCreatorName(detail.getContactName());
        }
        detail = organizationProvider.findOrganizationMemberByUIdAndOrgId(r.getFileUid(), r.getOrganizationId());
        if (null != detail) {
            dto.setFileName(detail.getContactName());
        }
        return dto;
    }


    @Override
    public ListUserInoutHistoryResponse
    listUserInoutHistory(ListUserInoutHistoryCommand cmd) {
        List<SocialSecurityInoutLog> logs = socialSecurityInoutLogProvider.listSocialSecurityInoutLogs(cmd.getOwnerId(), cmd.getDetailId(), null);

        List<UserInoutHistoryDTO> history = new ArrayList<>();

        Map<String, List<Byte>> historyMap = new HashedMap();
        if (null != logs) {

            for (SocialSecurityInoutLog log : logs) {
                if (null == historyMap.get(log.getLogMonth())) {
                    List<Byte> list = new ArrayList<>();
                    list.add(log.getType());
                    historyMap.put(log.getLogMonth(), list);
                } else {
                    historyMap.get(log.getLogMonth()).add(log.getType());
                }
            }
            for (String month : historyMap.keySet()) {
                UserInoutHistoryDTO dto = new UserInoutHistoryDTO();
                dto.setMonth(month);
                StringBuilder sb = null;
                for (Byte logType : historyMap.get(month)) {
                    if (null == sb) {
                        sb = new StringBuilder();
                    } else {
                        sb.append("、");
                    }
                    sb.append(InOutLogType.fromCode(logType).getDescribe());
                }
                dto.setLogs(sb.toString());
                history.add(dto);
            }
        }
        return new ListUserInoutHistoryResponse(history);
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
        response.setPayMonth(cmd.getPayMonth());
        List<SocialSecurityPayment> result = socialSecurityPaymentProvider.listSocialSecurityPayment(cmd.getOwnerId());
        if (null == result) {
            return null;
        }
        if (result.get(0).getPayMonth().equals(cmd.getPayMonth())) {
            response.setCreateTime(result.get(0).getCreateTime().getTime());
            response.setCreatorUid(result.get(0).getCreatorUid());

            response.setFileUid(result.get(0).getFileUid());
            if (null != result.get(0).getFileTime())
                response.setFileTime(result.get(0).getFileTime().getTime());
            processCreatorName(response, cmd.getOwnerId());
            return response;
        }
        SocialSecurityPaymentLog log = socialSecurityPaymentLogProvider.findAnyOneSocialSecurityPaymentLog(cmd.getOwnerId(), cmd.getPayMonth());
        if (null == log) {
            return null;
        }
        SocialSecurityGroup group = socialSecurityGroupProvider.findSocialSecurityGroupByOrg(cmd.getOwnerId(), cmd.getPayMonth());
        if (null != group) {
            response.setCreateTime(group.getCreateTime().getTime());
            response.setCreatorUid(group.getCreatorUid());
            response.setFileUid(group.getFileUid());
            response.setFileTime(group.getFileTime().getTime());
        }
        processCreatorName(response, cmd.getOwnerId());
        return response;
    }

    private void processCreatorName(GetSocialSecurityReportsHeadResponse response, Long ownerId) {

        response.setCreatorName(findNameByOwnerAndUser(ownerId, response.getCreatorUid()));

        response.setFileName(findNameByOwnerAndUser(ownerId, response.getFileUid()));
    }

    @Override
    public String findNameByOwnerAndUser(Long ownerId, Long uid) {
        if (null == uid) {
            return null;
        }
        OrganizationMember detail = organizationProvider.findOrganizationMemberByUIdAndOrgId(uid, ownerId);
        if (null != detail) {
            return detail.getContactName();
        }
        User user = userProvider.findUserById(uid);
        if (null != user) {
            return user.getNickName();
        }
        return null;
    }

    @Override
    public List<SocialSecurityEmployeeDTO> listSocialSecurityEmployees(ListSocialSecurityPaymentsCommand cmd) {
        List<SocialSecurityEmployeeDTO> results = new ArrayList<>();
        Integer pageSize;
        if (cmd.getPageSize() != null)
            pageSize = cmd.getPageSize();
        else
            pageSize = 20;

        List<OrganizationMemberDetails> records = archivesService.queryArchivesEmployees(new ListingLocator(), cmd.getOwnerId(), cmd.getDeptId(), (locator, query) -> {
            if (cmd.getSocialSecurityStatus() != null)
                query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.SOCIAL_SECURITY_STATUS.eq(cmd.getSocialSecurityStatus()));
            if (cmd.getAccumulationFundStatus() != null)
                query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ACCUMULATION_FUND_STATUS.eq(cmd.getAccumulationFundStatus()));
            if (cmd.getCheckInMonth() != null) {
                query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.CHECK_IN_TIME.between(getTheFirstDate(cmd.getCheckInMonth()), getTheLastDate(cmd.getCheckInMonth())));
            }
            if (cmd.getDismissMonth() != null) {
                query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.DISMISS_TIME.between(getTheFirstDate(cmd.getDismissMonth()), getTheLastDate(cmd.getDismissMonth())));
                //query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.EMPLOYEE_STATUS.eq(EmployeeStatus.DISMISSAL.getCode()));
            }
            if (cmd.getKeywords() != null) {
                query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.CONTACT_TOKEN.eq(cmd.getKeywords()).or(Tables.EH_ORGANIZATION_MEMBER_DETAILS.CONTACT_NAME.like(cmd.getKeywords())));
            }
            if (cmd.getAccumulationFundCityId() != null) {
                query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID.in(dbProvider.getDslContext(AccessSpec.readOnly()).selectDistinct(Tables.EH_SOCIAL_SECURITY_SETTINGS.DETAIL_ID)
                        .from(Tables.EH_SOCIAL_SECURITY_SETTINGS).where(Tables.EH_SOCIAL_SECURITY_SETTINGS.ORGANIZATION_ID.eq(cmd.getOwnerId()))
                        .and(Tables.EH_SOCIAL_SECURITY_SETTINGS.CITY_ID.eq(cmd.getAccumulationFundCityId()))
                        .and(Tables.EH_SOCIAL_SECURITY_SETTINGS.ACCUM_OR_SOCAIL.eq(AccumOrSocial.ACCUM.getCode()))));
            }
            if (cmd.getSocialSecurityCityId() != null) {
                query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID.in(dbProvider.getDslContext(AccessSpec.readOnly()).selectDistinct(Tables.EH_SOCIAL_SECURITY_SETTINGS.DETAIL_ID)
                        .from(Tables.EH_SOCIAL_SECURITY_SETTINGS).where(Tables.EH_SOCIAL_SECURITY_SETTINGS.ORGANIZATION_ID.eq(cmd.getOwnerId()))
                        .and(Tables.EH_SOCIAL_SECURITY_SETTINGS.CITY_ID.eq(cmd.getSocialSecurityCityId()))
                        .and(Tables.EH_SOCIAL_SECURITY_SETTINGS.ACCUM_OR_SOCAIL.eq(AccumOrSocial.SOCAIL.getCode()))));
            }
            int offset = ((cmd.getPageOffset() == null ? 1 : cmd.getPageOffset()) - 1) * (pageSize);
            query.addOrderBy(Tables.EH_ORGANIZATION_MEMBER_DETAILS.CHECK_IN_TIME.desc());
            query.addLimit(offset, pageSize + 1);
            return query;
        });

        records.forEach(r -> {
            SocialSecurityEmployeeDTO dto = new SocialSecurityEmployeeDTO();
            dto.setContactName(r.getContactName());
            dto.setUserId(r.getTargetId());
            dto.setDetailId(r.getId());
            dto.setNamespaceId(r.getNamespaceId());
            dto.setDepartmentName(r.getDepartmentName());
            dto.setCheckInTime(r.getCheckInTime());
            dto.setSocialSecurityStatus(r.getSocialSecurityStatus());
            dto.setAccumulationFundStatus(r.getAccumulationFundStatus());
            dto.setDismissTime(r.getDismissTime());
            results.add(dto);
        });
        return results;

    }

    @Override
    public java.sql.Date getTheFirstDate(String m) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
        try {
            Date date = df.parse(m);
            c.setTime(date);
            c.add(Calendar.MONTH, 0);
            c.set(Calendar.DAY_OF_MONTH, 1); //  设置为1号,当前日期既为本月第一天
            date = c.getTime();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            return sqlDate;
        } catch (ParseException e) {
            LOGGER.error("transfer format error");
            return null;
        }
    }
    @Override
    public java.sql.Date getTheLastDate(String m) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
        try {
            Date date = df.parse(m);
            c.setTime(date);
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH)); //  获取当前月最后一天
            date = c.getTime();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            return sqlDate;
        } catch (ParseException e) {
            LOGGER.error("transfer format error");
            return null;
        }
    }

    @Override
    public SocialSecurityEmployeeDTO getSocialSecurityEmployeeInfo(Long detailId) {
        SocialSecurityEmployeeDTO dto = new SocialSecurityEmployeeDTO();

        OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
        if (memberDetail != null) {
            dto.setDetailId(memberDetail.getId());
            dto.setUserId(memberDetail.getTargetId());
            dto.setCheckInTime(memberDetail.getCheckInTime());
            dto.setDismissTime(memberDetail.getDismissTime());

            SocialSecurityInoutTime social = socialSecurityInoutTimeProvider.getSocialSecurityInoutTimeByDetailId(InOutTimeType.SOCIAL_SECURITY.getCode(), detailId);
            if (social != null) {
                dto.setSocialSecurityStartMonth(social.getStartMonth());
                dto.setSocialSecurityEndMonth(social.getEndMonth());
            }
        }
        return dto;
    }

/*    @Override
    public List<Long> listSocialSecurityEmployeeDetailIdsByPayMonth(Long ownerId, String payMonth) {
        List<Long> detailIds = socialSecurityInoutTimeProvider.listSocialSecurityEmployeeDetailIdsByPayMonth(ownerId, payMonth, InOutTimeType.SOCIAL_SECURITY.getCode());
        return detailIds;
    }*/
/*@Override
    public SocialSecurityInoutTimeDTO addSocialSecurityInOutTime(AddSocialSecurityInOutTimeCommand cmd) {
        OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByDetailId(cmd.getDetailId());

        if (memberDetail != null) {
            //  1.create inOut time.
            SocialSecurityInoutTime time = createSocialSecurityInoutTime(cmd, memberDetail);
            //  2.create the log.
            SocialSecurityInoutLog log = convertToSocialSecurityInOutLog(time);
            socialSecurityInoutLogProvider.createSocialSecurityInoutLog(log);
            //  todo:3.social...
//            newSocialSecurityEmployee(cmd.getDetailId(), cmd.getStartMonth());

            //  return back.
            SocialSecurityInoutTimeDTO dto = ConvertHelper.convert(time, SocialSecurityInoutTimeDTO.class);
            dto.setInOutType(time.getType());
            return dto;
        }
        return null;
    }

    private SocialSecurityInoutTime createSocialSecurityInoutTime(AddSocialSecurityInOutTimeCommand cmd, OrganizationMemberDetails memberDetail) {
        SocialSecurityInoutTime time = socialSecurityInoutTimeProvider.getSocialSecurityInoutTimeByDetailId(cmd.getInOutType(), cmd.getDetailId());
        if (time != null) {
            if (cmd.getStartMonth() != null)
                time.setStartMonth(cmd.getStartMonth());
            if (cmd.getEndMonth() != null)
                time.setEndMonth(cmd.getEndMonth());
            socialSecurityInoutTimeProvider.updateSocialSecurityInoutTime(time);
        } else {
            time = new SocialSecurityInoutTime();
            time.setNamespaceId(memberDetail.getNamespaceId());
            time.setOrganizationId(cmd.getOrganizationId());
            time.setUserId(memberDetail.getTargetId());
            time.setDetailId(memberDetail.getId());
            time.setType(cmd.getInOutType());
            if (cmd.getStartMonth() != null)
                time.setStartMonth(cmd.getStartMonth());
            if (cmd.getEndMonth() != null)
                time.setEndMonth(cmd.getEndMonth());
            socialSecurityInoutTimeProvider.createSocialSecurityInoutTime(time);
        }
        return time;
    }

    private SocialSecurityInoutLog convertToSocialSecurityInOutLog(SocialSecurityInoutTime time) {
        SocialSecurityInoutLog log = new SocialSecurityInoutLog();
        log.setNamespaceId(time.getNamespaceId());
        log.setOrganizationId(time.getOrganizationId());
        log.setUserId(time.getUserId());
        log.setDetailId(time.getDetailId());
        log.setLogDate(ArchivesUtil.currentDate());

        //  check the time type and then set the log type.
        //  1) social security & startTime
        //  2) social security & endTime
        //  3) accumulation fund & startTime
        //  4) accumulation fund & endTime

        if (time.getType().equals(InOutTimeType.SOCIAL_SECURITY.getCode())) {
            if (time.getStartMonth() != null) {
                log.setType(InOutLogType.SOCIAL_SECURITY_IN.getCode());
                log.setLogMonth(time.getStartMonth());
            } else if (time.getEndMonth() != null) {
                log.setType(InOutLogType.SOCIAL_SECURITY_OUT.getCode());
                log.setLogMonth(time.getEndMonth());
            }
        } else if (time.getType().equals(InOutTimeType.ACCUMULATION_FUND.getCode())) {
            if (time.getStartMonth() != null) {
                log.setType(InOutLogType.ACCUMULATION_FUND_IN.getCode());
                log.setLogMonth(time.getStartMonth());
            } else if (time.getEndMonth() != null) {
                log.setType(InOutLogType.ACCUMULATION_FUND_OUT.getCode());
                log.setLogMonth(time.getEndMonth());
            }
        }

        //  return it.
        return log;
    }*/

    @Override
    public void increseSocialSecurity(IncreseSocialSecurityCommand cmd) {
        //先校验
        if (null != cmd.getAccumulationFundPayment()) {
            // 查询设置的城市户籍档次的数据规则
            List<SocialSecurityBase> bases = socialSecurityBaseProvider.listSocialSecurityBase(cmd.getAccumulationFundPayment().getCityId(), AccumOrSocial.ACCUM.getCode());
            // 校验数据是否合法
            checkSocialSercurity(bases, cmd.getAccumulationFundPayment().getItems());
        }
        if (null != cmd.getSocialSecurityPayment()) {
            // 查询设置的城市户籍档次的数据规则
            List<SocialSecurityBase> bases = socialSecurityBaseProvider.listSocialSecurityBase(cmd.getSocialSecurityPayment().getCityId(),
                    cmd.getSocialSecurityPayment().getHouseholdType());
            // 校验数据是否合法
            checkSocialSercurity(bases, cmd.getSocialSecurityPayment().getItems());
        }
        //在存储
        if (null != cmd.getDetailIds()) {
            for (Long detailId : cmd.getDetailIds()) {
                this.coordinationProvider.getNamedLock(CoordinationLocks.SOCIAL_SECURITY_INCRESE.getCode() + detailId).enter(() -> {

                    OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
                    if (null != cmd.getAccumulationFundPayment()) {
                        if (NormalFlag.YES != NormalFlag.fromCode(detail.getAccumulationFundStatus())) {
                            increseMemberDetail(detail, AccumOrSocial.ACCUM);
                        }
                        // 保存setting表数据
                        saveSocialSecuritySettings(cmd.getAccumulationFundPayment(), detailId, AccumOrSocial.ACCUM.getCode());
                        // 保存当月payments数据
                        saveSocialSecurityPayment(cmd.getAccumulationFundPayment(), detailId, NormalFlag.NO.getCode(), AccumOrSocial.ACCUM.getCode());
                    }
                    if (null != cmd.getSocialSecurityPayment()) {
                        if (NormalFlag.YES != NormalFlag.fromCode(detail.getSocialSecurityStatus())) {
                            increseMemberDetail(detail, AccumOrSocial.SOCAIL);
                        }
                        // 保存setting表数据
                        saveSocialSecuritySettings(cmd.getSocialSecurityPayment(), detailId, AccumOrSocial.SOCAIL.getCode());
                        // 保存当月payments数据
                        saveSocialSecurityPayment(cmd.getSocialSecurityPayment(), detailId, NormalFlag.NO.getCode(), AccumOrSocial.SOCAIL.getCode());
                    }
                    return null;
                });
            }
        }
    }


    void increseMemberDetail(OrganizationMemberDetails detail, AccumOrSocial accum) {
        if (AccumOrSocial.ACCUM == accum) {
            detail.setAccumulationFundStatus(NormalFlag.YES.getCode());
        } else {
            detail.setSocialSecurityStatus(NormalFlag.YES.getCode());
        }
        increseLog(detail.getNamespaceId(), detail.getOrganizationId(), detail.getTargetId(), detail.getId(), accum);
        organizationProvider.updateOrganizationMemberDetails(detail, detail.getId());
    }

    void increseLog(Integer namepsaceId, Long orgId, Long userId, Long detailId, AccumOrSocial accumOrSocial) {
        SocialSecurityInoutLog log = new SocialSecurityInoutLog();
        log.setNamespaceId(namepsaceId);
        log.setOrganizationId(orgId);
        log.setUserId(userId);
        log.setDetailId(detailId);
        if (AccumOrSocial.ACCUM == accumOrSocial) {
            log.setType(InOutLogType.ACCUMULATION_FUND_IN.getCode());
        } else {
            log.setType(InOutLogType.SOCIAL_SECURITY_IN.getCode());
        }
        Date date = DateHelper.currentGMTTime();
        log.setLogDate(new java.sql.Date(date.getTime()));
        String paymentMonth = getPayMonthByOwner(orgId);
        log.setLogMonth(paymentMonth);
        socialSecurityInoutLogProvider.createSocialSecurityInoutLog(log);
    }

    void decreseMemberDetail(OrganizationMemberDetails detail, AccumOrSocial accum) {
        if (AccumOrSocial.ACCUM == accum) {
            detail.setAccumulationFundStatus(NormalFlag.NO.getCode());
        } else {
            detail.setSocialSecurityStatus(NormalFlag.NO.getCode());
        }
        decreseLog(detail.getNamespaceId(), detail.getOrganizationId(), detail.getTargetId(), detail.getId(), accum);
        organizationProvider.updateOrganizationMemberDetails(detail, detail.getId());
    }

    void decreseLog(Integer namepsaceId, Long orgId, Long userId, Long detailId, AccumOrSocial accumOrSocial) {
        SocialSecurityInoutLog log = new SocialSecurityInoutLog();
        log.setNamespaceId(namepsaceId);
        log.setOrganizationId(orgId);
        log.setUserId(userId);
        log.setDetailId(detailId);
        if (AccumOrSocial.ACCUM == accumOrSocial) {
            log.setType(InOutLogType.ACCUMULATION_FUND_OUT.getCode());
        } else {
            log.setType(InOutLogType.SOCIAL_SECURITY_OUT.getCode());
        }
        Date date = DateHelper.currentGMTTime();
        log.setLogDate(new java.sql.Date(date.getTime()));
        String paymentMonth = getPayMonthByOwner(orgId);
        log.setLogMonth(paymentMonth);
        socialSecurityInoutLogProvider.createSocialSecurityInoutLog(log);
    }

    @Override
    public void decreseSocialSecurity(DecreseSocialSecurityCommand cmd) {
        dbProvider.execute((TransactionStatus status) -> {
            for (Long detailId : cmd.getDetailIds()) {
                OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
                if (NormalFlag.YES == NormalFlag.fromCode(detail.getAccumulationFundStatus())) {
                    decreseMemberDetail(detail, AccumOrSocial.ACCUM);
                }
                if (NormalFlag.YES == NormalFlag.fromCode(detail.getSocialSecurityStatus())) {
                    decreseMemberDetail(detail, AccumOrSocial.SOCAIL);
                }

            }
            socialSecurityPaymentProvider.deleteSocialSecurityPaymentsByDetailIds(cmd.getDetailIds());
            return null;
        });
    }

}