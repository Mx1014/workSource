//@formatter:off
package com.everhomes.asset;

import com.everhomes.address.AddressProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.oauth2client.handler.RestCallTemplate;
import com.everhomes.rest.asset.BillDetailDTO;
import com.everhomes.rest.asset.ShowBillDetailForClientDTO;
import com.everhomes.rest.asset.ShowBillDetailForClientResponse;
import com.everhomes.rest.asset.ShowBillForClientDTO;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Wentian Wang on 2017/8/10.
 */

@Component(AssetVendorHandler.ASSET_VENDOR_PREFIX+"ZJGK")
public class ZhangjianggaokeAssetVendor extends ZuolinAssetVendorHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(ZhangjianggaokeAssetVendor.class);

    @Autowired
    private AssetProvider assetProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private CommunityProvider communityProvider;

    @Override
    public ShowBillForClientDTO showBillForClient(Long ownerId, String ownerType, String targetType, Long targetId, Long billGroupId,Byte isOwedBill) {
        Map<String,String> map = new HashMap<>();
        Community communityById = new Community();
        if(targetType=="community"){
            communityById = communityProvider.findCommunityById(ownerId);
        } else {
            return null;
        }
        String communityName = communityById.getName();
        if (communityName == ""){
            return null;
        }
        if(targetType == "eh_user") {
            //个人用户，查询门牌
            List<String[]> list = userService.listBuildingAndApartmentById(UserContext.currentUserId());
//            RestCallTemplate.url(server.getToke).
            //调用getApartmentBills
            String body = RestCallTemplate.queryStringBuilder()
                    .var("community",communityName)
                    .var()
                    .build();
            RestCallTemplate.url()

        } else if(targetType == "eh_organization"){
            //拿到enterpriseName
            //调用getCompanyBills
        }
        return super.showBillForClient(ownerId,ownerType,targetType,targetId,billGroupId,isOwedBill);
    }

    @Override
    public ShowBillDetailForClientResponse getBillDetailForClient(Long billId) {
        return null;
    }

    @Override
    public ShowBillDetailForClientResponse listBillDetailOnDateChange(Long ownerId, String ownerType, String targetType, Long targetId, String dateStr) {
        return null;
    }
}
