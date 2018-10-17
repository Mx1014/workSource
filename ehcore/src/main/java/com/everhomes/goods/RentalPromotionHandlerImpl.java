package com.everhomes.goods;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rentalv2.RentalResource;
import com.everhomes.rentalv2.RentalResourceType;
import com.everhomes.rentalv2.Rentalv2Provider;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.goods.*;
import com.everhomes.rest.rentalv2.RentalV2ResourceType;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component(GoodsPromotionHandler.GOODS_PROMOTION_HANDLER_PREFIX + ServiceModuleConstants.RENTAL_MODULE)
public class RentalPromotionHandlerImpl extends DefaultGoodsPromotionHandlerImpl {

    @Autowired
    private Rentalv2Provider rentalv2Provider;
    @Autowired
    private CommunityProvider communityProvider;

    @Override
    public GetGoodListResponse getGoodList(GetGoodListCommand cmd) {
        List<GoodTagInfo> goods = new ArrayList<>();
        GetGoodListResponse response = new GetGoodListResponse();
        if (cmd.getGoodTagInfo() != null){
            Long resourceTypeId = Long.valueOf(cmd.getGoodTagInfo().getTag1Key());
            Long communityId = Long.valueOf(cmd.getGoodTagInfo().getTag2Key());
            ListingLocator locator = new ListingLocator();
            List<RentalResource> rentalSites = rentalv2Provider.findRentalSites(resourceTypeId, null, locator, Integer.MAX_VALUE, null, null, communityId);
            if (rentalSites != null && rentalSites.size() > 0){
                for (RentalResource rentalSite : rentalSites) {
                    GoodTagInfo good = ConvertHelper.convert(cmd.getGoodTagInfo(), GoodTagInfo.class);
                    good.setGoodsTag(rentalSite.getId().toString());
                    good.setGoodsName(rentalSite.getResourceName());
                    goods.add(good);
                }
            }

        }

        response.setGoods(goods);
        return response;
    }

    @Override
    public GetServiceGoodResponse getServiceGoodsScopes(GetServiceGoodCommand cmd) {
        GetServiceGoodResponse response = new GetServiceGoodResponse();
        GoodScopeDTO goodScopeDTO = new GoodScopeDTO();
        goodScopeDTO.setTitle("范围");
        List<RentalResourceType> resourceTypes = rentalv2Provider.findRentalResourceTypes(cmd.getNamespaceId(), null, null, null);
        if (resourceTypes != null && resourceTypes.size() > 0){
            //去除掉vip停车 属于停车模块
            resourceTypes = resourceTypes.stream().filter(r->!RentalV2ResourceType.VIP_PARKING.getCode().equals(r.getIdentify())).collect(Collectors.toList());
            List<TagDTO> resourceList = new ArrayList<>();
            //找到项目
            List<Community> communities = communityProvider.listNamespaceCommunities(cmd.getNamespaceId());
            List<TagDTO> communitiesList = new ArrayList<>();
            for (Community community : communities){
                TagDTO communitiy = new TagDTO();
                communitiy.setId(community.getId().toString());
                communitiy.setName(community.getName());
                communitiesList.add(communitiy);
            }
            for (RentalResourceType resourceType : resourceTypes){
                TagDTO dto = new TagDTO();
                dto.setId(resourceType.getId().toString());
                dto.setName(resourceType.getName());
                dto.setTags(communitiesList);
                resourceList.add(dto);
            }
            goodScopeDTO.setTagList(resourceList);
        }
        response.setGoodScopeDTO(goodScopeDTO);
        return response;
    }
}
