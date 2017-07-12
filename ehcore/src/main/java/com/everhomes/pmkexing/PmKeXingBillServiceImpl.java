// @formatter:off
package com.everhomes.pmkexing;

import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.cache.CacheAccessor;
import com.everhomes.cache.CacheProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.http.HttpUtils;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.organization.*;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationServiceErrorCode;
import com.everhomes.rest.pmkexing.*;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.StringReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by xq.tian on 2016/12/27.
 */
@Service
public class PmKeXingBillServiceImpl implements PmKeXingBillService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PmKeXingBillServiceImpl.class);

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private AddressProvider addressProvider;

    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private RolePrivilegeService rolePrivilegeService;

    @Autowired
    private CacheProvider cacheProvider;

    private static LocaleStringService localeStringService;

    @Override
    public List<ListOrganizationsByPmAdminDTO> listOrganizationsByPmAdmin() {
        List<ListOrganizationsByPmAdminDTO> dtoList = new ArrayList<>();

        final Long userId = currUserId();
        // 拿到用户所在的公司列表
        List<OrganizationDTO> organizationList = organizationService.listUserRelateOrganizations(currNamespaceId(),
                userId, OrganizationGroupType.ENTERPRISE);

        if (organizationList == null || organizationList.isEmpty()) {
            return dtoList;
        }

        ListServiceModuleAdministratorsCommand cmd = new ListServiceModuleAdministratorsCommand();
        for (OrganizationDTO organization : organizationList) {
            cmd.setOrganizationId(organization.getId());
            // 获取当前用户为超级管理员的公司
            List<OrganizationContactDTO> organizationAdmins = rolePrivilegeService.listOrganizationSuperAdministrators(cmd);
            if (organizationAdmins != null && organizationAdmins.size() > 0) {
                boolean isAdmin = organizationAdmins.stream().mapToLong(OrganizationContactDTO::getTargetId).anyMatch(adminId -> adminId == userId);
                if (isAdmin) {
                    dtoList.add(toOrganizationsByPmAdminDTO(organization));
                    continue;
                }
            }
            // 获取当前用户为企业管理员的公司
            List<OrganizationContactDTO> organizationPms = rolePrivilegeService.listOrganizationAdministrators(cmd);
            if (organizationPms != null && organizationPms.size() > 0) {
                boolean isPm = organizationPms.stream().mapToLong(OrganizationContactDTO::getTargetId).anyMatch(pmId -> pmId == userId);
                if (isPm) {
                    dtoList.add(toOrganizationsByPmAdminDTO(organization));
                }
            }
        }
        this.processLatestSelectedOrganization(dtoList);
        return dtoList;
    }

    private void processLatestSelectedOrganization(List<ListOrganizationsByPmAdminDTO> dtoList) {
        CacheAccessor accessor = cacheProvider.getCacheAccessor(null);
        Long latestSelectedOrganizationId = accessor.get("pmbill:kexing:latest-selected-organization:" + currUserId());
        if (latestSelectedOrganizationId != null) {
            dtoList.parallelStream()
                    .filter(r -> Objects.equals(r.getOrganizationId(), latestSelectedOrganizationId))
                    .forEach(r -> r.setLatestSelected(TrueOrFalseFlag.TRUE.getCode()));
        }
    }

    private ListOrganizationsByPmAdminDTO toOrganizationsByPmAdminDTO(OrganizationDTO organization) {
        ListOrganizationsByPmAdminDTO dto = new ListOrganizationsByPmAdminDTO();
        dto.setOrganizationId(organization.getId());
        dto.setOrganizationName(organization.getName());
        dto.setAddresses(this.getOrganizationAddresses(organization.getId()));
        return dto;
    }

    private List<AddressDTO> getOrganizationAddresses(Long organizationId) {
        List<OrganizationAddress> organizationAddresses = organizationProvider.findOrganizationAddressByOrganizationId(organizationId);
        if (organizationAddresses != null) {
            return organizationAddresses.stream().map(r -> {
                Address address = addressProvider.findAddressById(r.getAddressId());
                return ConvertHelper.convert(address, AddressDTO.class);
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public ListPmKeXingBillsResponse listPmKeXingBills(ListPmKeXingBillsCommand cmd) {
        Organization organization = this.findOrganizationById(cmd.getOrganizationId());
        this.putLatestSelectedOrganizationToCache(cmd.getOrganizationId());
        String api = getAPI(ConfigConstants.KEXING_PMBILL_API_BILLLIST);
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

        Map<String, String> params = new HashMap<>();
        params.put("projectName", currentOrganization(cmd.getOrganizationId()).getCommunityName());
        params.put("companyName", organization.getName());
        if (cmd.getBillStatus() != null) {
            params.put("isPay", String.valueOf(cmd.getBillStatus()));
        }
        int pageOffset = cmd.getPageOffset() != null ? cmd.getPageOffset() : 1;
        params.put("pageCount", String.valueOf(pageOffset));
        params.put("pageSize", String.valueOf(pageSize));

        String sdateTo = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        params.put("sdateTo", sdateTo);

        BillBeans itemList = post(api, params, BillBeans.class);
        return itemList != null ? itemList.toPmKeXingBillResponse(cmd.getPageOffset()) : new ListPmKeXingBillsResponse();
    }

    private void putLatestSelectedOrganizationToCache(Long organizationId) {
        CacheAccessor accessor = cacheProvider.getCacheAccessor(null);
        accessor.put("pmbill:kexing:latest-selected-organization:" + currUserId(), organizationId);
    }

    @Override
    public PmKeXingBillDTO getPmKeXingBill(GetPmKeXingBillCommand cmd) {
        Organization organization = this.findOrganizationById(cmd.getOrganizationId());
        String api = getAPI(ConfigConstants.KEXING_PMBILL_API_BILLLIST);
        Map<String, String> params = new HashMap<>();
        params.put("projectName", currentOrganization(cmd.getOrganizationId()).getCommunityName());
        params.put("companyName", organization.getName());
        params.put("pageCount", "1");
        params.put("pageSize", "1");
        params.put("sdateFrom", cmd.getDateStr());
        params.put("sdateTo", cmd.getDateStr());

        BillBeans billBeans = post(api, params, BillBeans.class);
        if (billBeans != null && billBeans.bill.size() > 0) {
            return billBeans.toPmKeXingBillDTO();
        }
        return new PmKeXingBillDTO();
    }

    @Override
    public PmKeXingBillStatDTO getPmKeXingBillStat(GetPmKeXingBillStatCommand cmd) {
        Organization organization = this.findOrganizationById(cmd.getOrganizationId());

        String api = getAPI(ConfigConstants.KEXING_PMBILL_API_BILLCOUNT);
        Map<String, String> params = new HashMap<>();
        params.put("projectName", currentOrganization(cmd.getOrganizationId()).getCommunityName());
        params.put("companyName", organization.getName());

        String sdateTo = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        params.put("sdateTo", sdateTo);

        BillStat billStat = post(api, params, BillStat.class);
        return billStat != null ? billStat.toBillStatDTO() : new PmKeXingBillStatDTO();
    }

    private OrganizationDTO currentOrganization(Long organizationId) {
        OrganizationDTO organization = organizationService.getUserCurrentOrganization();
        if (organization == null) {
            LOGGER.error("Current organization are not exist.");
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_ORG_NOT_EXIST,
                    "Organization are not exist");
        }
        if (organization.getCommunityId() == null || organization.getCommunityName() == null) {
            OrganizationCommunityRequest communityRequest = organizationProvider.getOrganizationCommunityRequestByOrganizationId(organizationId);

            Long communityId;
            if (communityRequest != null) {
                communityId = communityRequest.getCommunityId();
            } else {
                OrganizationCommunity orgComm = this.organizationProvider.findOrganizationCommunityByOrgId(organizationId);
                communityId = orgComm.getCommunityId();
            }

            Community community = communityProvider.findCommunityById(communityId);
            if (community != null) {
                organization.setCommunityId(community.getId());
                organization.setCommunityName(community.getName());
            } else {
                LOGGER.error("not found organization name for current organization or organization {}", organizationId);
            }
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Current user organization is {}", organization);
        return organization;
    }

    private Organization findOrganizationById(Long organizationId) {
        Organization organization = organizationProvider.findOrganizationById(organizationId);
        if (organization == null) {
            LOGGER.error("Organization are not exist for id = {}", organizationId);
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_ORG_NOT_EXIST,
                    "Organization are not exist");
        }
        return organization;
    }

    private Integer currNamespaceId() {
        return UserContext.getCurrentNamespaceId();
    }

    private Long currUserId() {
        return UserContext.current().getUser().getId();
    }

    private String getAPI(String configName) {
        String host = configurationProvider.getValue(currNamespaceId(), ConfigConstants.KEXING_PMBILL_API_HOST, "");
        String api = configurationProvider.getValue(currNamespaceId(), configName, "");
        return host + api;
    }

    private String post(String api, Map<String, String> params) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Http post params is :{}", params.toString());
            }
            return HttpUtils.post(api, params, 20, "utf-8");
        } catch (Throwable e) {
            LOGGER.error("Http post error for api: {}", api, e);
            throw RuntimeErrorException.errorWith(PmKeXingBillServiceErrorCode.SCOPE, PmKeXingBillServiceErrorCode.ERROR_HTTP_REQUEST,
                    "Http post error");
        }
    }

    private <T> T post(String api, Map<String, String> params, Class<T> clz) {
        return xmlToBean(post(api, params), clz);
    }

    @XmlRootElement(name = "billBeans")
    private static class BillBeans extends ToStr {
        @XmlElement
        private List<Bill> bill = new ArrayList<>();

        ListPmKeXingBillsResponse toPmKeXingBillResponse(Integer pageOffset) {
            ListPmKeXingBillsResponse response = new ListPmKeXingBillsResponse();
            for (Bill b : bill) {
                PmKeXingBillDTO billDTO = this.toPmKeXingBillDTO(b);
                response.getBills().add(billDTO);
                if (response.getNextPageOffset() == null && b.hasNextPag == TrueOrFalseFlag.TRUE.getCode()) {
                    response.setNextPageOffset(pageOffset != null ? ++pageOffset : 2);
                }
            }
            return response;
        }

        PmKeXingBillDTO toPmKeXingBillDTO(Bill bill) {
            PmKeXingBillDTO dto = new PmKeXingBillDTO();
            dto.setBillDate(bill.billDate);
            dto.setReceivableAmount(bill.totalShouldMoney);

            bill.billBeans.parallelStream()
                    .filter(bean -> bean.isPay == 0)
                    .map(BillBean::getActualmoney).collect(Collectors.reducing(BigDecimal::add))
                    .ifPresent(dto::setUnpaidAmount);

            dto.setBillStatus(processBillStatusLocaleString(bill.billBeans));

            List<PmKeXingBillItemDTO> items = bill.billBeans.parallelStream()
                    .map(BillBean::toBillItemDTO).collect(Collectors.toList());
            dto.setItems(items);
            return dto;
        }

        PmKeXingBillDTO toPmKeXingBillDTO() {
            if (bill != null && bill.size() > 0) {
                return this.toPmKeXingBillDTO(bill.get(0));
            }
            return new PmKeXingBillDTO();
        }

        private Byte processBillStatusLocaleString(List<BillBean> billBeans) {
            boolean haveUnpaidItem = billBeans.stream().map(BillBean::getIsPay).anyMatch(status -> Objects.equals(status, PmKeXingBillStatus.UNPAID.getCode()));
            return haveUnpaidItem ? PmKeXingBillStatus.UNPAID.getCode() : PmKeXingBillStatus.PAID.getCode();
        }
    }

    private static String currLocale() {
        return UserContext.current().getUser().getLocale();
    }

    private static class Bill extends ToStr {
        @XmlElement
        private List<BillBean> billBeans;
        @XmlElement
        private String billDate;
        @XmlElement
        private Byte hasNextPag;
        @XmlElement
        private BigDecimal totalShouldMoney;
    }

    /**
     * 账单
     */
    private static class BillBean extends ToStr {
        @XmlElement
        private Integer hasNextPag;
        @XmlElement
        private Byte isPay;
        @XmlElement
        private String billDate;
        @XmlElement
        private BigDecimal receivable;
        @XmlElement
        private BigDecimal actualmoney;
        @XmlElement
        private String fiName;
        // String contractNum;

        PmKeXingBillItemDTO toBillItemDTO() {
            PmKeXingBillItemDTO dto = new PmKeXingBillItemDTO();
            dto.setName(fiName);
            dto.setAmount(receivable);
            return dto;
        }

        BigDecimal getReceivable() {
            return receivable;
        }

        BigDecimal getActualmoney() {
            return actualmoney;
        }

        Byte getIsPay() {
            return isPay;
        }

        Integer getHasNextPag() {
            return hasNextPag;
        }
    }

    /**
     * 账单统计
     */
    @XmlRootElement(name = "list")
    private static class BillStat extends ToStr {
        @XmlElement
        private BigDecimal arrearageMonthCount;
        @XmlElement
        private BigDecimal arrearageSum;
        // BigDecimal alreadyMonthCount;
        // BigDecimal alreadySum;

        PmKeXingBillStatDTO toBillStatDTO() {
            PmKeXingBillStatDTO dto = new PmKeXingBillStatDTO();
            dto.setUnpaidAmount(arrearageSum);
            dto.setUnpaidMonth(arrearageMonthCount);
            return dto;
        }
    }

    private static class ToStr {
        public String toString() {
            return StringHelper.toJsonString(this);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T xmlToBean(String xml, Class<T> clz) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Response xml is:\n {}", xml);
        }
        T t = null;
        try {
            JAXBContext context = JAXBContext.newInstance(clz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            t = (T) unmarshaller.unmarshal(new StringReader(xml));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    @Autowired
    public void setLocaleStringService(LocaleStringService localeStringService) {
        PmKeXingBillServiceImpl.localeStringService = localeStringService;
    }

    /*public static void main(String[] args) {
        String xml = "<list>\n" +
                "  <result>\n" +
                "    <fiName>空调维护费2.4</fiName>\n" +
                "    <isPay>0</isPay>\n" +
                "    <actualmoney>48366.81</actualmoney>\n" +
                "    <billDate>2016-11</billDate>\n" +
                "    <receivable>48366.81</receivable>\n" +
                "    <contractNum>Cont000176</contractNum>\n" +
                "    <hasNextPag>1</hasNextPag>\n" +
                "  </result>\n" +
                "  <result>\n" +
                "    <fiName>空调维护费2.4</fiName>\n" +
                "    <isPay>0</isPay>\n" +
                "    <actualmoney>48366.81</actualmoney>\n" +
                "    <billDate>2016-12</billDate>\n" +
                "    <receivable>48366.81</receivable>\n" +
                "    <contractNum>Cont000176</contractNum>\n" +
                "    <hasNextPag>1</hasNextPag>\n" +
                "  </result>\n" +
                "  <result>\n" +
                "    <fiName>空调维护费2.4</fiName>\n" +
                "    <isPay>0</isPay>\n" +
                "    <actualmoney>48366.81</actualmoney>\n" +
                "    <billDate>2017-01</billDate>\n" +
                "    <receivable>48366.81</receivable>\n" +
                "    <contractNum>Cont000176</contractNum>\n" +
                "    <hasNextPag>1</hasNextPag>\n" +
                "  </result>\n" +
                "</list>";

        PmKeXingBillServiceImpl service = new PmKeXingBillServiceImpl();
        BillBeans billItemList = service.xmlToBean(xml, BillBeans.class);
        System.out.println(billItemList);

        System.out.println(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
    }*/
}
