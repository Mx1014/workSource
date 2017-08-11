//@formatter:off
package com.everhomes.asset;

import com.everhomes.address.AddressProvider;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.rest.asset.BillDetailDTO;
import com.everhomes.rest.asset.ShowBillDetailForClientDTO;
import com.everhomes.rest.asset.ShowBillDetailForClientResponse;
import com.everhomes.rest.asset.ShowBillForClientDTO;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
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

    @Override
    public ShowBillForClientDTO showBillForClient(Long ownerId, String ownerType, String targetType, Long targetId, Long billGroupId) {
        Map<String,String> map = new HashMap<>();
        if(targetType == "eh_user") {
            //个人用户，查询门牌,置入map中
        }
        return super.showBillForClient(ownerId,ownerType,targetType,targetId,billGroupId);
    }

    @Override
    public ShowBillDetailForClientResponse getBillDetailForClient(Long billId) {
        return null;
    }
}
