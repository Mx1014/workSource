package com.everhomes.asset;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.asset.*;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.group.GroupDiscriminator;
import com.everhomes.rest.organization.SearchOrganizationCommand;
import com.everhomes.rest.search.GroupQueryResult;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.server.schema.tables.pojos.EhAssetBills;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserGroup;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ying.xiong on 2017/4/11.
 */
@Component(AssetVendorHandler.ASSET_VENDOR_PREFIX + "ZUOLIN")
public class ZuolinAssetVendorHandler implements AssetVendorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZuolinAssetVendorHandler.class);
    @Autowired
    private AssetProvider assetProvider;

    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private OrganizationSearcher organizationSearcher;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private AddressProvider addressProvider;

    @Override
    public ListSimpleAssetBillsResponse listSimpleAssetBills(Long ownerId, String ownerType, Long targetId, String targetType, Long organizationId, Long addressId, String tenant, Byte status, Long startTime, Long endTime, Long pageAnchor, Integer pageSize) {
        List<Long> tenantIds = new ArrayList<>();
        String tenantType = null;
        if(tenant != null) {
            Community community = communityProvider.findCommunityById(targetId);
            if(community != null) {
                //园区 查公司表
                if(CommunityType.COMMERCIAL.equals(CommunityType.fromCode(community.getCommunityType()))) {
                    tenantType = TenantType.ENTERPRISE.getCode();
                    SearchOrganizationCommand command = new SearchOrganizationCommand();
                    command.setKeyword(tenant);
                    command.setCommunityId(targetId);
                    GroupQueryResult result = organizationSearcher.query(command);

                    if(result != null) {
                        tenantIds.addAll(result.getIds());
                    }
                }

                //小区 查用户所属家庭
                else if(CommunityType.RESIDENTIAL.equals(CommunityType.fromCode(community.getCommunityType()))) {
                    tenantType = TenantType.FAMILY.getCode();
                    List<User> users = userProvider.listUserByNickName(tenant);

                    if(users != null && users.size() > 0) {
                        for(User user : users) {
                            List<UserGroup> list = userProvider.listUserActiveGroups(user.getId(), GroupDiscriminator.FAMILY.getCode());
                            if(list != null && list.size() > 0) {
                                for(UserGroup userGroup : list) {
                                    tenantIds.add(userGroup.getGroupId());
                                }
                            }
                        }
                    }

                }

            }
        }

        CrossShardListingLocator locator=new CrossShardListingLocator();
        if(pageAnchor!=null){
            locator.setAnchor(pageAnchor);
        }

        pageSize = PaginationConfigHelper.getPageSize(configurationProvider, pageSize);
        if(organizationId != null) {
            tenantIds.add(organizationId);
        }

        List<AssetBill> bills  = assetProvider.listAssetBill(ownerId, ownerType, targetId, targetType,
                tenantIds, tenantType, addressId, status, startTime,endTime, locator, pageSize + 1);


        ListSimpleAssetBillsResponse response = new ListSimpleAssetBillsResponse();
        if (bills.size() > pageSize) {
            response.setNextPageAnchor(locator.getAnchor());
            bills = bills.subList(0, pageSize);
        }

        List<SimpleAssetBillDTO> dtos = convertAssetBillToSimpleDTO(bills);
        response.setBills(dtos);
        return response;
    }

    @Override
    public AssetBillTemplateValueDTO findAssetBill(Long id, Long ownerId, String ownerType, Long targetId, String targetType,
                    Long templateVersion, Long organizationId, String dateStr, Long tenantId, String tenantType, Long addressId) {
        AssetBillTemplateValueDTO dto = new AssetBillTemplateValueDTO();
        AssetBill bill = null;
        if(id != null) {
            bill = assetProvider.findAssetBill(id, ownerId, ownerType, targetId, targetType);
        } else {
            bill = assetProvider.findAssetBill(ownerId, ownerType, targetId, targetType, dateStr, tenantId, tenantType, addressId);
        }


        if (bill == null) {
            LOGGER.error("cannot find asset bill. bill: id = " + id + ", ownerId = " + ownerId
                    + ", ownerType = " + ownerType + ", targetId = " + targetId + ", targetType = " + targetType);
            throw RuntimeErrorException.errorWith(AssetServiceErrorCode.SCOPE,
                    AssetServiceErrorCode.ASSET_BILL_NOT_EXIST,
                    "账单不存在");
        }

        dto.setId(bill.getId());
        dto.setNamespaceId(bill.getNamespaceId());
        dto.setOwnerType(bill.getOwnerType());
        dto.setOwnerId(bill.getOwnerId());
        dto.setTargetType(bill.getTargetType());
        dto.setTargetId(bill.getTargetId());
        dto.setTemplateVersion(bill.getTemplateVersion());
        dto.setTenantId(bill.getTenantId());
        dto.setTenantType(bill.getTenantType());
        dto.setAddressId(bill.getAddressId());
        List<AssetBillTemplateFieldDTO> templateFields = null;
        if(bill.getTemplateVersion() == 0L) {
            templateFields = assetProvider.findTemplateFieldByTemplateVersion(0L, ownerType, 0L, targetType, 0L);
        } else {
            templateFields = assetProvider.findTemplateFieldByTemplateVersion(ownerId, ownerType, targetId, targetType, bill.getTemplateVersion());
        }

        if(templateFields != null && templateFields.size() > 0) {
            List<FieldValueDTO> valueDTOs = new ArrayList<>();
            Field[] fields = EhAssetBills.class.getDeclaredFields();
            for(AssetBillTemplateFieldDTO fieldDTO : templateFields) {
                if(AssetBillTemplateSelectedFlag.SELECTED.equals(AssetBillTemplateSelectedFlag.fromCode(fieldDTO.getSelectedFlag()))) {
                    FieldValueDTO valueDTO = new FieldValueDTO();
                    if(fieldDTO.getFieldCustomName() != null) {
                        valueDTO.setFieldDisplayName(fieldDTO.getFieldCustomName());
                    } else {
                        valueDTO.setFieldDisplayName(fieldDTO.getFieldDisplayName());
                    }
                    valueDTO.setFieldName(fieldDTO.getFieldName());
                    valueDTO.setFieldType(fieldDTO.getFieldType());

                    for (Field requestField : fields) {
                        requestField.setAccessible(true);
                        // private类型
                        if (requestField.getModifiers() == 2) {
                            if(requestField.getName().equals(fieldDTO.getFieldName())){
                                // 字段值
                                try {
                                    if(requestField.get(bill) != null)
                                        valueDTO.setFieldValue(requestField.get(bill).toString());

                                    break;
                                } catch (IllegalArgumentException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    valueDTOs.add(valueDTO);
                }
            }

            BigDecimal totalAmounts = bill.getPeriodAccountAmount();
            // 当月的账单要把滞纳金和往期未付的账单一起计入总计应收
            if(compareMonth(bill.getAccountPeriod()) == 0) {
                BigDecimal pastUnpaid = new BigDecimal(0);
                List<BigDecimal> unpaidAmounts = assetProvider.listPeriodUnpaidAccountAmount(bill.getOwnerId(), bill.getOwnerType(),
                        bill.getTargetId(), bill.getTargetType(), bill.getAddressId(), bill.getTenantType(), bill.getTenantId(),bill.getAccountPeriod());

                if(unpaidAmounts != null && unpaidAmounts.size() > 0) {

                    for(BigDecimal unpaid : unpaidAmounts) {
                        pastUnpaid = pastUnpaid.add(unpaid);
                    }

                    FieldValueDTO unpaidAmountsDTO = new FieldValueDTO();
                    unpaidAmountsDTO.setFieldDisplayName("往期欠费");
                    unpaidAmountsDTO.setFieldType("BigDecimal");
                    unpaidAmountsDTO.setFieldValue(pastUnpaid.toString());

                    valueDTOs.add(unpaidAmountsDTO);

                }
                totalAmounts = totalAmounts.add(pastUnpaid);
                totalAmounts = totalAmounts.add(bill.getLateFee());
            }

            FieldValueDTO totalAmountsDTO = new FieldValueDTO();
            totalAmountsDTO.setFieldDisplayName("总计应收");
            totalAmountsDTO.setFieldType("BigDecimal");
            totalAmountsDTO.setFieldValue(totalAmounts.toString());

            valueDTOs.add(totalAmountsDTO);

            dto.setDtos(valueDTOs);
            dto.setPeriodAccountAmount(totalAmounts);
            if(AssetBillStatus.PAID.equals(AssetBillStatus.fromStatus(bill.getStatus()))) {
                dto.setUnpaidPeriodAccountAmount(BigDecimal.ZERO);
            }

            if(AssetBillStatus.UNPAID.equals(AssetBillStatus.fromStatus(bill.getStatus()))) {
                dto.setUnpaidPeriodAccountAmount(totalAmounts);
            }

        }
        return dto;
    }

    @Override
    public AssetBillStatDTO getAssetBillStat(String tenantType, Long tenantId, Long addressId) {
        List<AssetBill> bills = assetProvider.listUnpaidBills(tenantType, tenantId, addressId);
        AssetBillStatDTO dto = new AssetBillStatDTO();
        dto.setUnpaidAmount(BigDecimal.ZERO);
        Set<Timestamp> accountPeriod = new HashSet<>();
        bills.forEach(bill -> {
        	dto.setUnpaidAmount(dto.getUnpaidAmount().add(bill.getPeriodAccountAmount()));
            accountPeriod.add(bill.getAccountPeriod());
        });
        BigDecimal unpaidMonth = new BigDecimal(accountPeriod.size());
        dto.setUnpaidMonth(unpaidMonth);
        return dto;
    }

    private List<SimpleAssetBillDTO> convertAssetBillToSimpleDTO(List<AssetBill> bills) {
        List<SimpleAssetBillDTO> dtos =  bills.stream().map(bill -> {
            SimpleAssetBillDTO dto = ConvertHelper.convert(bill, SimpleAssetBillDTO.class);
            Address address = addressProvider.findAddressById(bill.getAddressId());
            if(address != null) {
                dto.setBuildingName(address.getBuildingName());
                dto.setApartmentName(address.getApartmentName());
            }

            BigDecimal totalAmount = bill.getPeriodAccountAmount();
            // 当月的账单要把滞纳金和往期未付的账单一起计入总计应收
            if(compareMonth(bill.getAccountPeriod()) == 0) {

                List<BigDecimal> unpaidAmounts = assetProvider.listPeriodUnpaidAccountAmount(bill.getOwnerId(), bill.getOwnerType(),
                        bill.getTargetId(), bill.getTargetType(), bill.getAddressId(), bill.getTenantType(), bill.getTenantId(),bill.getAccountPeriod());

                if(unpaidAmounts != null && unpaidAmounts.size() > 0) {
                    BigDecimal pastUnpaid = BigDecimal.ZERO;
                    if(bill.getLateFee() != null) {
                        pastUnpaid = bill.getLateFee();
                    }
                    for(BigDecimal unpaid : unpaidAmounts) {
                        if(unpaid != null) {
                            pastUnpaid = pastUnpaid.add(unpaid);
                        }
                    }

                    totalAmount.add(pastUnpaid);

                }
            }

            dto.setPeriodAccountAmount(totalAmount);
            if(AssetBillStatus.PAID.equals(AssetBillStatus.fromStatus(dto.getStatus()))) {
                dto.setUnpaidPeriodAccountAmount(BigDecimal.ZERO);
            }

            if(AssetBillStatus.UNPAID.equals(AssetBillStatus.fromStatus(dto.getStatus()))) {
                dto.setUnpaidPeriodAccountAmount(totalAmount);
            }

            return dto;
        }).collect(Collectors.toList());

        return dtos;
    }

    private int compareMonth(Timestamp compareValue) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Timestamp(System.currentTimeMillis()));
        int monthNow = c.get(Calendar.MONTH);

        c.setTime(compareValue);
        int monthCompare = c.get(Calendar.MONTH);

        return (monthNow-monthCompare);
    }
}
