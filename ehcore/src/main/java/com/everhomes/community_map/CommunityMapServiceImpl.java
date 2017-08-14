// @formatter:off
package com.everhomes.community_map;

import com.everhomes.business.BusinessService;
import com.everhomes.community.Building;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.business.ShopDTO;
import com.everhomes.rest.community.BuildingDTO;
import com.everhomes.rest.community_map.*;
import com.everhomes.rest.community_map.SearchCommunityMapContentsCommand;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.ui.user.*;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.*;
import com.everhomes.util.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CommunityMapServiceImpl implements CommunityMapService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommunityMapServiceImpl.class);

	@Autowired
	private CommunityMapProvider communityMapProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private BusinessService businessService;
    @Autowired
    private CommunityProvider communityProvider;
    @Autowired
    private ConfigurationProvider configurationProvider;
    @Autowired
    private CommunityMapSearcherImpl communityMapSearcherImpl;

    @Override
    public ListCommunityMapSearchTypesResponse listCommunityMapSearchTypesByScene(ListCommunityMapSearchTypesCommand cmd) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        SceneTokenDTO sceneToken = userService.checkSceneToken(userId, cmd.getSceneToken());

        //先按域空间查，ownerid和ownertype暂时不用
        ListCommunityMapSearchTypesResponse response = new ListCommunityMapSearchTypesResponse();

        List<CommunityMapSearchType> searchTypes = communityMapProvider.listCommunityMapSearchTypesByNamespaceId(namespaceId);

        if(searchTypes.size() > 0) {
            response.setSearchTypes(searchTypes.stream().map(r -> {
                CommunityMapSearchTypeDTO dto = ConvertHelper.convert(r, CommunityMapSearchTypeDTO.class);
                return dto;
            }).collect(Collectors.toList()));
        }
        return response;
    }

    @Override
    public SearchCommunityMapContentsResponse searchContentsByScene(SearchCommunityMapContentsCommand cmd) {
        long startTime = System.currentTimeMillis();
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        //	    SceneTokenDTO sceneToken = checkSceneToken(userId, cmd.getSceneToken());

        if(StringUtils.isEmpty(cmd.getContentType())) {
            cmd.setContentType(CommunityMapSearchContentType.ALL.getCode());
        }
        CommunityMapSearchContentType contentType = CommunityMapSearchContentType.fromCode(cmd.getContentType());

        SearchCommunityMapContentsResponse response = new SearchCommunityMapContentsResponse();
        switch(contentType) {

            case ORGANIZATION:
//                response = organizationService.searchEnterprise(cmd);
                break;

            case BUILDING:
//                int pageSize2 = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
//                CrossShardListingLocator locator = new CrossShardListingLocator();
//                locator.setAnchor(cmd.getPageAnchor());
//                List<Building> buildings = communityProvider.ListBuildingsByCommunityId(locator, pageSize2 + 1,null,
//                        namespaceId, cmd.getKeyword());

//                response
                break;
            case SHOP:
//                SearchContentsBySceneCommand cmd2 = ConvertHelper.convert(cmd, SearchContentsBySceneCommand.class);
//                SearchContentsBySceneReponse resp = businessService.searchShops(cmd2);
                break;
            case ALL:
//                int pageSize = (int)configurationProvider.getIntValue("search.content.size", 3);
//                cmd.setPageSize(pageSize);
//
//                List<OrganizationDTO> organizationDTOs = new ArrayList<OrganizationDTO>();
//                List<BuildingDTO> buildingDTOs = new ArrayList<BuildingDTO>();
//                List<ShopDTO> shopDTOs = new ArrayList<ShopDTO>();
//                response.setBuildingDTOs(buildingDTOs);
//                response.setShopDTOs(shopDTOs);
//                response.setOrganizationDTOs(organizationDTOs);
//
//                response.getOrganizationDTOs().addAll(organizationService.searchEnterprise(cmd).getOrganizationDTOs());
//
//                SearchContentsBySceneCommand cmd3 = ConvertHelper.convert(cmd, SearchContentsBySceneCommand.class);
//                SearchContentsBySceneReponse tempResp = businessService.searchShops(cmd3);
//                if(tempResp != null
//                        && tempResp.getShopDTOs() != null) {
//                    response.getShopDTOs().addAll(tempResp.getShopDTOs());
//                }

                break;

            default:
                LOGGER.error("Unsupported content type for search, contentType=" + cmd.getContentType());
                break;
        }

        if(LOGGER.isDebugEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.debug("search contents by scene, userId={}, namespaceId={}, elapse={}, cmd={}",
                    userId, namespaceId, (endTime - startTime), cmd);
        }
        return response;
    }

}
