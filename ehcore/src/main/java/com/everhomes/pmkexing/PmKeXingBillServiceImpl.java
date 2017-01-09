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
        params.put("projectName", currentOrganization().getCommunityName());
        params.put("companyName", organization.getName());
        if (cmd.getBillStatus() != null) {
            params.put("isPay", cmd.getBillStatus()+"");
        }
        params.put("pageCount", "1");
        params.put("pageSize", "1000");

        LocalDate date = LocalDate.now().plusMonths(20);
        // LocalDate date = LocalDate.now();

        int pageOffset = cmd.getPageOffset() != null ? cmd.getPageOffset() - 1 : 0;
        int monthOffset = pageOffset * pageSize + pageOffset;

        params.put("sdateFrom", date.minusMonths(monthOffset + pageSize).format(DateTimeFormatter.ofPattern("yyyy-MM")));
        params.put("sdateTo", date.minusMonths(monthOffset).format(DateTimeFormatter.ofPattern("yyyy-MM")));

        BillItemList itemList = post(api, params, BillItemList.class);
        return itemList != null ? itemList.toPmKeXingBillResponse(cmd.getPageOffset(), pageSize) : new ListPmKeXingBillsResponse();
    }

    private void putLatestSelectedOrganizationToCache(Long organizationId) {
        CacheAccessor accessor = cacheProvider.getCacheAccessor(null);
        accessor.put("pmbill:kexing:latest-selected-organization:" + currUserId(), organizationId);
    }

    /*@Override
    public ListPmKeXingBillsResponse listPmKeXingBillsByMultiRequest(ListPmKeXingBillsCommand cmd) {
        Organization organization = this.findOrganizationById(cmd.getOrganizationId());
        String api = getAPI(ConfigConstants.KEXING_PMBILL_API_BILLLIST);

        Map<String, String> params = new HashMap<>();
        params.put("projectName", currentOrganization().getCommunityName());
        params.put("companyName", organization.getName());
        params.put("pageCount", "1");
        params.put("pageSize", "1000");

        List<PmKeXingBillDTO> dtoList = new ArrayList<>();

        int monthOffset = (cmd.getPageOffset() != null ? cmd.getPageOffset() - 1 : 0) * cmd.getPageSize();
        LocalDate date = LocalDate.now().plusMonths(10);
        // LocalDate date = LocalDate.now();

        boolean breakFlag = false;

        // 发送pageSize此请求, 每次查询一个月的数据
        for (Integer i = 0; i < cmd.getPageSize(); i++) {
            params.put("sdate", date.minusMonths(i + monthOffset).format(DateTimeFormatter.ofPattern("yyyy-MM")));
            BillItemList itemList = post(api, params, BillItemList.class);

            if (itemList != null) {
                List<PmKeXingBillDTO> billDTOList = itemList.toPmKeXingBillDTOList();
                if (billDTOList.size() == 0) {
                    breakFlag = true;
                    break;
                }
                dtoList.addAll(billDTOList);
            }
        }

        dtoList.sort((o1, o2) -> o2.getBillDate().compareTo(o1.getBillDate()));

        ListPmKeXingBillsResponse response = new ListPmKeXingBillsResponse();
        response.setBills(dtoList);

        if (!breakFlag) {
            params.put("sdate", date.minusMonths(monthOffset + cmd.getPageSize() + 1).format(DateTimeFormatter.ofPattern("yyyy-MM")));
            BillItemList itemList = post(api, params, BillItemList.class);

            if (itemList != null) {
                if (itemList.toPmKeXingBillDTOList().size() > 0) {
                    response.setNextPageOffset(cmd.getPageOffset() != null ? cmd.getPageOffset() + 1 : 2);
                }
            }
        }
        return response;
    }*/

    @Override
    public PmKeXingBillDTO getPmKeXingBill(GetPmKeXingBillCommand cmd) {
        Organization organization = this.findOrganizationById(cmd.getOrganizationId());
        String api = getAPI(ConfigConstants.KEXING_PMBILL_API_BILLLIST);
        Map<String, String> params = new HashMap<>();
        params.put("projectName", currentOrganization().getCommunityName());
        params.put("companyName", organization.getName());
        params.put("pageCount", "1");
        params.put("pageSize", "1000");
        params.put("sdateFrom", cmd.getDateStr());
        params.put("sdateTo", cmd.getDateStr());

        BillItemList itemList = post(api, params, BillItemList.class);
        if (itemList != null && itemList.result.size() > 0) {
            return itemList.toPmKeXingBillDTO();
        }
        return new PmKeXingBillDTO();
    }

    @Override
    public PmKeXingBillStatDTO getPmKeXingBillStat(GetPmKeXingBillStatCommand cmd) {
        Organization organization = this.findOrganizationById(cmd.getOrganizationId());

        String api = getAPI(ConfigConstants.KEXING_PMBILL_API_BILLCOUNT);
        Map<String, String> params = new HashMap<>();
        params.put("projectName", currentOrganization().getCommunityName());
        params.put("companyName", organization.getName());
        // params.put("sdateFrom", "");// 不传查所有
        // params.put("sdateTo", "");// 不传查所有

        BillStat billStat = post(api, params, BillStat.class);
        return billStat != null ? billStat.toBillStatDTO() : new PmKeXingBillStatDTO();
    }

    private OrganizationDTO currentOrganization() {
        OrganizationDTO organization = organizationService.getUserCurrentOrganization();
        if (organization == null) {
            LOGGER.error("Current organization are not exist.");
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_ORG_NOT_EXIST,
                    "Organization are not exist");
        }
        if (organization.getCommunityId() == null || organization.getCommunityName() == null) {
            OrganizationCommunityRequest communityRequest = organizationProvider.getOrganizationCommunityRequestByOrganizationId(organization.getId());
            if (communityRequest != null) {
                Community community = communityProvider.findCommunityById(communityRequest.getCommunityId());
                if (community != null) {
                    organization.setCommunityId(community.getId());
                    organization.setCommunityName(community.getName());
                }
            }
        }
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
            return HttpUtils.post(api, params, 10, "utf-8");
        } catch (Throwable e) {
            LOGGER.error("Http post error for api: {}", api, e);
            throw RuntimeErrorException.errorWith(PmKeXingBillServiceErrorCode.SCOPE, PmKeXingBillServiceErrorCode.ERROR_HTTP_REQUEST,
                    "Http post error");
        }
    }

    private <T> T post(String api, Map<String, String> params, Class<T> clz) {
        return xmlToBean(post(api, params), clz);
    }

    @XmlRootElement(name = "list")
    private static class BillItemList extends ToStr {
        @XmlElement
        private List<BillItem> result = new ArrayList<>();

        ListPmKeXingBillsResponse toPmKeXingBillResponse(Integer pageOffset, Integer pageSize) {
            ListPmKeXingBillsResponse response = new ListPmKeXingBillsResponse();

            Map<String, List<BillItem>> dateBillItemListMap = result.parallelStream().collect(Collectors.groupingBy(BillItem::getBillDate));

            dateBillItemListMap.forEach((date, billItemList) -> {
                PmKeXingBillDTO dto = this.toPmKeXingBillDTO(date, billItemList);
                response.getBills().add(dto);
            });

            // 因为对方提供的分页机制有点问题, 所以我们这边只要取到数据就设置有下一页, 直到取不到数据为止
            if (response.getBills().size() >= pageSize) {
                response.setNextPageOffset(pageOffset != null ? pageOffset + 1 : 2);
            }
            response.getBills().sort((o1, o2) -> o2.getBillDate().compareTo(o1.getBillDate()));
            return response;
        }

        PmKeXingBillDTO toPmKeXingBillDTO(String date, List<BillItem> billItemList) {
            PmKeXingBillDTO dto = new PmKeXingBillDTO();
            dto.setBillDate(date);

            Optional<BigDecimal> receivableAmount = billItemList.parallelStream().map(BillItem::getReceivable).collect(Collectors.reducing(BigDecimal::add));
            Optional<BigDecimal> unpaidAmount = billItemList.parallelStream().map(BillItem::getActualmoney).collect(Collectors.reducing(BigDecimal::add));

            receivableAmount.ifPresent(dto::setReceivableAmount);
            unpaidAmount.ifPresent(dto::setUnpaidAmount);

            boolean haveUnpaidItem = billItemList.stream().map(BillItem::getIsPay).anyMatch(status -> Objects.equals(status, PmKeXingBillStatus.UNPAID.getCode()));
            Byte billStatusLocaleCode = haveUnpaidItem ? PmKeXingBillStatus.UNPAID.getCode() : PmKeXingBillStatus.PAID.getCode();
            String billStatus = localeStringService.getLocalizedString(PmKeXingBillLocalStringCode.SCOPE, String.valueOf(billStatusLocaleCode), currLocale(), "");
            dto.setBillStatus(billStatus);

            List<PmKeXingBillItemDTO> items = billItemList.parallelStream().map(BillItem::toBillItemDTO).collect(Collectors.toList());
            dto.setItems(items);
            return dto;
        }

        PmKeXingBillDTO toPmKeXingBillDTO() {
            Map<String, List<BillItem>> dateBillItemListMap = result.parallelStream().collect(Collectors.groupingBy(BillItem::getBillDate));
            PmKeXingBillDTO[] dtoArr = new PmKeXingBillDTO[1];
            dateBillItemListMap.forEach((date, itemList) -> dtoArr[0] = this.toPmKeXingBillDTO(date, itemList));
            return dtoArr[0];
        }
    }

    private static String currLocale() {
        return UserContext.current().getUser().getLocale();
    }

    /**
     * 账单
     */
    private static class BillItem extends ToStr {
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

        String getBillDate() {
            return billDate != null ? billDate : "";
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
        BillItemList billItemList = service.xmlToBean(xml, BillItemList.class);
        System.out.println(billItemList);
    }*/
}
