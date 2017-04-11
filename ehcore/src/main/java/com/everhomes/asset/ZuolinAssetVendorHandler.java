package com.everhomes.asset;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.asset.AssetBillTemplateValueDTO;
import com.everhomes.rest.asset.ListSimpleAssetBillsResponse;
import com.everhomes.rest.asset.SimpleAssetBillDTO;
import com.everhomes.rest.asset.TenantType;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.group.GroupDiscriminator;
import com.everhomes.rest.organization.SearchOrganizationCommand;
import com.everhomes.rest.search.GroupQueryResult;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserGroup;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ying.xiong on 2017/4/11.
 */
@Component(AssetVendorHandler.ASSET_VENDOR_PREFIX + "ZUOLIN")
public class ZuolinAssetVendorHandler implements AssetVendorHandler {

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
    public ListSimpleAssetBillsResponse listSimpleAssetBills(Long ownerId, String ownerType, Long targetId, String targetType, Long addressId, String tenant, Byte status, Long startTime, Long endTime, Long pageAnchor, Integer pageSize) {
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
    public AssetBillTemplateValueDTO findAssetBill(Long id, Long ownerId, String ownerType, Long targetId, String targetType, Long templateVersion) {
        return null;
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
                    BigDecimal pastUnpaid = bill.getLateFee();
                    for(BigDecimal unpaid : unpaidAmounts) {
                        pastUnpaid = pastUnpaid.add(unpaid);
                    }

                    totalAmount.add(pastUnpaid);

                }
            }

            dto.setPeriodAccountAmount(totalAmount);
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
