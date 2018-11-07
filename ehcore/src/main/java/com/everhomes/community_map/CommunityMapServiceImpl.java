// @formatter:off
package com.everhomes.community_map;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.app.App;
import com.everhomes.business.BusinessService;
import com.everhomes.community.Building;
import com.everhomes.community.CommunityProvider;
import com.everhomes.community.CommunityService;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.address.ApartmentDTO;
import com.everhomes.rest.community.BuildingDTO;
import com.everhomes.rest.community.GetBuildingCommand;
import com.everhomes.rest.community_map.*;
import com.everhomes.rest.community_map.SearchCommunityMapContentsCommand;
import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.ui.user.*;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.WebTokenGenerator;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
	private UserPrivilegeMgr userPrivilegeMgr;
    @Autowired
    private CommunityService communityService;
    @Autowired
    private ContentServerService contentServerService;
    @Autowired
    private AddressProvider addressProvider;

    @Override
    public ListCommunityMapSearchTypesResponse listCommunityMapSearchTypesByScene(ListCommunityMapSearchTypesCommand cmd) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();

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

        if(StringUtils.isEmpty(cmd.getContentType())) {
            cmd.setContentType(CommunityMapSearchContentType.ALL.getCode());
        }
        CommunityMapSearchContentType contentType = CommunityMapSearchContentType.fromCode(cmd.getContentType());

        SearchCommunityMapContentsResponse response = new SearchCommunityMapContentsResponse();
        switch(contentType) {

            case ORGANIZATION:
                response = this.searchEnterprise(cmd);
                break;

            case BUILDING:
                response = this.searchBuildings(cmd);

                break;
            case SHOP:
                response = this.searchShops(cmd);

                break;
            case ALL:
                int pageSize = configurationProvider.getIntValue("search.content.size", 3);
                cmd.setPageSize(pageSize);

                response.setOrganizations(this.searchEnterprise(cmd).getOrganizations());
                response.setBuildings(this.searchBuildings(cmd).getBuildings());
                response.setShops(this.searchShops(cmd).getShops());

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

    private SearchCommunityMapContentsResponse searchShops(SearchCommunityMapContentsCommand cmd) {

        Integer pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

        SearchCommunityMapContentsResponse response = new SearchCommunityMapContentsResponse();
        List<CommunityMapShopDTO> result = new ArrayList<>();

        SearchContentsBySceneCommand cmd2 = ConvertHelper.convert(cmd, SearchContentsBySceneCommand.class);
        cmd2.setPageSize(pageSize);

        Long pageAnchor = cmd.getPageAnchor();
        if (null == pageAnchor || pageAnchor < Integer.MAX_VALUE) {
            SearchContentsBySceneReponse resp = businessService.searchShops(cmd2);

            if (null != resp) {
                response.setNextPageAnchor(resp.getNextPageAnchor());
                result.addAll(resp.getShopDTOs().stream().map(r -> {
                    CommunityMapShopDTO shop = ConvertHelper.convert(r, CommunityMapShopDTO.class);
                    Long buildingId = null;
                    Long apartmentId = null;
                    if (StringUtils.isNotBlank(r.getBuildingId())) {
                        buildingId = Long.valueOf(r.getBuildingId());
                    }
                    if (StringUtils.isNotBlank(r.getApartmentId())) {
                        apartmentId = Long.valueOf(r.getApartmentId());
                    }
                    populateShopAddressInfo(shop, buildingId, apartmentId);
                    shop.setShopFlag(CommunityMapShopFlag.OPEN_IN_BIZ.getCode());
                    return shop;
                }).collect(Collectors.toList()));
            }
        }

        Long userId = UserContext.currentUserId();

        if (result.size() < pageSize) {
            //TODO:超过pageSize时，做一个截取，因为是从电商 和core server同时查询店铺数据
            Integer tempPageSize = pageSize - result.size();

            Integer namespaceId = UserContext.getCurrentNamespaceId();

            List<CommunityMapShopDetail> shops = communityMapProvider.searchCommunityMapShops(namespaceId, null,
                    null, cmd.getBuildingId(), cmd.getKeyword(), cmd.getPageAnchor(), tempPageSize);
            result.addAll(shops.stream().map(r -> {
                CommunityMapShopDTO shop = new CommunityMapShopDTO();
                shop.setShopNo(String.valueOf(r.getId()));
                shop.setShopName(r.getShopName());

                String url = contentServerService.parserUri(r.getShopAvatarUri(), EntityType.USER.getCode(), userId);
                shop.setShopLogo(url);

                populateShopAddressInfo(shop, r.getBuildingId(), r.getAddressId());
                shop.setShopFlag(CommunityMapShopFlag.NOT_OPEN_IN_BIZ.getCode());
                return shop;
            }).collect(Collectors.toList()));

            if(result.size() != pageSize){
                response.setNextPageAnchor(null);
            }else{
                response.setNextPageAnchor(shops.get(shops.size() - 1).getCreateTime().getTime());
            }
        }

        response.setShops(result);
        return response;
    }

    private void populateShopAddressInfo(CommunityMapShopDTO shop, Long buildingId, Long apartmentId) {
        if (null != buildingId) {
            List<CommunityMapBuildingDTO> buildings = new ArrayList<>();

            Building building = communityProvider.findBuildingById(buildingId);
            if (null != building) {
                CommunityMapBuildingDTO dto = ConvertHelper.convert(building, CommunityMapBuildingDTO.class);

                List<CommunityBuildingGeo> geos = communityMapProvider.listCommunityBuildingGeos(building.getId());

                if (!geos.isEmpty()) {
                    CommunityBuildingGeo centerGeo = geos.get(geos.size() - 1);
                    geos.remove(centerGeo);
                    dto.setCenterLatitude(centerGeo.getLatitude());
                    dto.setCenterLongitude(centerGeo.getLongitude());
                    dto.setGeos(geos.stream().map(g -> ConvertHelper.convert(g, CommunityMapBuildingGeoDTO.class))
                            .collect(Collectors.toList()));
                }

                if (null != apartmentId) {
                    List<ApartmentDTO> apartmentDTOS = new ArrayList<ApartmentDTO>();
                    Address address = addressProvider.findAddressById(apartmentId);
                    ApartmentDTO apartmentDTO = new ApartmentDTO();
                    apartmentDTO.setApartmentName(address.getApartmentName());
                    apartmentDTO.setAddressId(address.getId());
                    apartmentDTOS.add(apartmentDTO);
                    dto.setApartments(apartmentDTOS);
                }

                buildings.add(dto);
                shop.setBuildings(buildings);
            }
        }
    }

    private SearchCommunityMapContentsResponse searchBuildings(SearchCommunityMapContentsCommand cmd) {
        SearchCommunityMapContentsResponse response = new SearchCommunityMapContentsResponse();

        User user = UserContext.current().getUser();

        Long userId = user.getId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        int pageSize2 = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

//      标准版兼容
        AppContext appContext = AppContextGenerator.fromWebToken(cmd.getSceneToken());
        Long communityId = appContext.getCommunityId();

        List<Building> buildings = communityProvider.ListBuildingsByCommunityId(locator, pageSize2 + 1, communityId,
                namespaceId, cmd.getKeyword());

        Long nextPageAnchor = null;
        if(buildings.size() > pageSize2) {
            buildings.remove(buildings.size() - 1);
            nextPageAnchor = buildings.get(buildings.size() - 1).getId();
        }

        List<CommunityMapBuildingDTO> dtos = buildings.stream().map(r -> {
            CommunityMapBuildingDTO dto = ConvertHelper.convert(r, CommunityMapBuildingDTO.class);

            List<CommunityBuildingGeo> geos = communityMapProvider.listCommunityBuildingGeos(r.getId());

            if (!geos.isEmpty()) {
                CommunityBuildingGeo centerGeo = geos.get(geos.size() - 1);
                geos.remove(centerGeo);
                dto.setCenterLatitude(centerGeo.getLatitude());
                dto.setCenterLongitude(centerGeo.getLongitude());
                dto.setGeos(geos.stream().map(g -> ConvertHelper.convert(g, CommunityMapBuildingGeoDTO.class))
                        .collect(Collectors.toList()));
            }

            return  dto;
        }).collect(Collectors.toList());
        response.setBuildings(dtos);
        response.setNextPageAnchor(nextPageAnchor);

        return response;
    }

    private SearchCommunityMapContentsResponse searchEnterprise(SearchCommunityMapContentsCommand cmd) {

        int namespaceId = UserContext.getCurrentNamespaceId();
        Long userId = UserContext.currentUserId();
        //      标准版兼容
        AppContext appContext = AppContextGenerator.fromWebToken(cmd.getSceneToken());
        Long communityId = appContext.getCommunityId();

        SearchOrganizationCommand searchCmd = ConvertHelper.convert(cmd, SearchOrganizationCommand.class);
        searchCmd.setNamespaceId(namespaceId);
        searchCmd.setCommunityId(communityId);
        if (null != cmd.getBuildingId()) {
            Building building = communityProvider.findBuildingById(cmd.getBuildingId());
            if (null != building) {
                searchCmd.setBuildingName(building.getName());
            }
        }
//        searchCmd.setOrganizationType(OrganizationType.ENTERPRISE.getCode());
        searchCmd.setExistAddressFlag(ExistAddressFlag.EXIST.getCode());

        ListEnterprisesCommandResponse orgResponse = organizationService.searchEnterprise(searchCmd);

        List<CommunityMapOrganizationDTO> organizations = orgResponse.getDtos().stream().map(r -> {

            CommunityMapOrganizationDTO org = new CommunityMapOrganizationDTO();
            org.setId(r.getOrganizationId());
            org.setName(r.getName());
            org.setLogo(r.getAvatarUrl());

            List<AddressDTO> addresses = r.getAddresses();

            org.setBuildings(processAddresses(addresses, communityId));
            return org;
        }).collect(Collectors.toList());

        SearchCommunityMapContentsResponse response = new SearchCommunityMapContentsResponse();
        response.setNextPageAnchor(orgResponse.getNextPageAnchor());
        response.setOrganizations(organizations);

        return response;
    }

    //处理同一个园区的楼栋门牌
    private List<CommunityMapBuildingDTO> processAddresses(List<AddressDTO> addresses, Long communityId) {
        List<CommunityMapBuildingDTO> buildings = new ArrayList<>();
        Map<String, List<ApartmentDTO>> temp = new HashMap<>();
        for (AddressDTO ad: addresses) {
            if (temp.containsKey(ad.getBuildingName())) {
                ApartmentDTO apartmentDTO = new ApartmentDTO();
                apartmentDTO.setApartmentName(ad.getApartmentName());
                apartmentDTO.setAddressId(ad.getId());
                List<ApartmentDTO> apartmentDTOS = temp.get(ad.getBuildingName());
                apartmentDTOS.add(apartmentDTO);
            }else {

                ApartmentDTO apartmentDTO = new ApartmentDTO();
                apartmentDTO.setApartmentName(ad.getApartmentName());
                apartmentDTO.setAddressId(ad.getId());
                List<ApartmentDTO> apartmentDTOS = new ArrayList<ApartmentDTO>();
                apartmentDTOS.add(apartmentDTO);
                temp.put(ad.getBuildingName(), apartmentDTOS);
            }
        }

        temp.keySet().forEach(r -> {
            Building building = communityProvider.findBuildingByCommunityIdAndName(communityId, r);
            if (null != building) {
                CommunityMapBuildingDTO buildingDTO = ConvertHelper.convert(building, CommunityMapBuildingDTO.class);
                buildingDTO.setApartments(temp.get(r));

                List<CommunityBuildingGeo> geos = communityMapProvider.listCommunityBuildingGeos(building.getId());

                if (!geos.isEmpty()) {
                    CommunityBuildingGeo centerGeo = geos.get(geos.size() - 1);
                    geos.remove(centerGeo);
                    buildingDTO.setCenterLatitude(centerGeo.getLatitude());
                    buildingDTO.setCenterLongitude(centerGeo.getLongitude());
                    buildingDTO.setGeos(geos.stream().map(g -> ConvertHelper.convert(g, CommunityMapBuildingGeoDTO.class))
                            .collect(Collectors.toList()));
                }

                buildings.add(buildingDTO);
            }
        });

        return buildings;
    }

    @Override
    public CommunityMapBuildingDetailDTO getCommunityMapBuildingDetailById(GetCommunityMapBuildingDetailByIdCommand cmd) {
        GetBuildingCommand cmd2 = ConvertHelper.convert(cmd, GetBuildingCommand.class);
        BuildingDTO buildingDTO = communityService.getBuilding(cmd2);

        CommunityMapBuildingDetailDTO dto = ConvertHelper.convert(buildingDTO, CommunityMapBuildingDetailDTO.class);
        SearchCommunityMapContentsCommand cmd3 = new SearchCommunityMapContentsCommand();
        cmd3.setBuildingId(cmd.getBuildingId());
        cmd3.setPageSize(5);
        cmd3.setSceneToken(cmd.getSceneToken());

        SearchCommunityMapContentsResponse orgResponse = this.searchEnterprise(cmd3);
        SearchCommunityMapContentsResponse shopResponse = this.searchShops(cmd3);

        dto.setOrganizations(orgResponse.getOrganizations());
        dto.setShops(shopResponse.getShops());

        List<CommunityBuildingGeo> geos = communityMapProvider.listCommunityBuildingGeos(cmd.getBuildingId());

        if (!geos.isEmpty()) {

            CommunityBuildingGeo centerGeo = geos.get(geos.size() - 1);
            dto.setCenterLatitude(centerGeo.getLatitude());
            dto.setCenterLongitude(centerGeo.getLongitude());

        }
        return dto;
    }

    @Override
    public CommunityMapInitDataDTO getCommunityMapInitData(GetCommunityMapInitDataCommand cmd) {

        User user = UserContext.current().getUser();
        Long userId = user.getId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();

//        SceneTokenDTO sceneToken = userService.checkSceneToken(userId, cmd.getSceneToken());
//        Long communityId = userService.getCommunityIdBySceneToken(sceneToken);
//        标准版兼容
        AppContext appContext = AppContextGenerator.fromWebToken(cmd.getSceneToken());
        Long communityId = appContext.getCommunityId();

        CommunityMapInfo communityMapInfo = communityMapProvider.findCommunityMapInfo(namespaceId);

        CommunityMapInitDataDTO dto = ConvertHelper.convert(communityMapInfo, CommunityMapInitDataDTO.class);

        String url = contentServerService.parserUri(communityMapInfo.getMapUri(), EntityType.USER.getCode(), userId);
        dto.setMapUrl(url);

        CrossShardListingLocator locator = new CrossShardListingLocator();

        List<Building> buildings = communityProvider.ListBuildingsByCommunityId(locator, Integer.MAX_VALUE, communityId,
                namespaceId, null);

        List<CommunityMapBuildingDTO> tempBuildings = buildings.stream().map(r -> {
            CommunityMapBuildingDTO d = ConvertHelper.convert(r, CommunityMapBuildingDTO.class);
            List<CommunityBuildingGeo> geos = communityMapProvider.listCommunityBuildingGeos(r.getId());

            if (!geos.isEmpty()) {

                CommunityBuildingGeo centerGeo = geos.get(geos.size() - 1);
                geos.remove(centerGeo);
                d.setCenterLatitude(centerGeo.getLatitude());
                d.setCenterLongitude(centerGeo.getLongitude());
                d.setGeos(geos.stream().map(g -> ConvertHelper.convert(g, CommunityMapBuildingGeoDTO.class))
                        .collect(Collectors.toList()));
            }
            return d;
        }).collect(Collectors.toList());
        dto.setBuildings(tempBuildings);

        return dto;
    }

    @Override
    public void deleteCommunityMapShop(DeleteCommunityMapShopCommand cmd) {
        CommunityMapShopDetail shop = communityMapProvider.getCommunityMapShopDetailById(cmd.getShopId());
        if (null == shop) {
            LOGGER.error("Shop not found, cmd={}", cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Shop not found.");
        }
        shop.setStatus(CommunityMapShopStatus.INACTIVE.getCode());
        communityMapProvider.updateCommunityMapShop(shop);
    }

    @Override
    public CommunityMapShopDetailDTO updateCommunityMapShop(UpdateCommunityMapShopCommand cmd) {
    	if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4007040071L, cmd.getAppId(), null,cmd.getCurrentProjectId());//商户管理权限
		}
        CommunityMapShopDetail shop = communityMapProvider.getCommunityMapShopDetailById(cmd.getShopId());
        if (null == shop) {
            LOGGER.error("Shop not found, cmd={}", cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Shop not found.");
        }
        BeanUtils.copyProperties(cmd, shop);
        communityMapProvider.updateCommunityMapShop(shop);
        return ConvertHelper.convert(shop, CommunityMapShopDetailDTO.class);
    }

    @Override
    public CommunityMapShopDetailDTO createCommunityMapShop(CreateCommunityMapShopCommand cmd) {
    	if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4007040071L, cmd.getAppId(), null,cmd.getCurrentProjectId());//商户管理权限
		}
        CommunityMapShopDetail shop = ConvertHelper.convert(cmd, CommunityMapShopDetail.class);
        shop.setNamespaceId(UserContext.getCurrentNamespaceId());
        communityMapProvider.createCommunityMapShop(shop);

        return ConvertHelper.convert(shop, CommunityMapShopDetailDTO.class);
    }

    @Override
    public CommunityMapShopDetailDTO getCommunityMapShopDetailById(GetCommunityMapShopDetailByIdCommand cmd) {

        CommunityMapShopDetail shop = communityMapProvider.getCommunityMapShopDetailById(cmd.getShopId());

        if (null == shop) {
            LOGGER.error("Shop not found, cmd={}", cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Shop not found.");
        }
        CommunityMapShopDetailDTO dto = ConvertHelper.convert(shop, CommunityMapShopDetailDTO.class);
        populateShop(dto, shop);
        return dto;
    }

    private void populateShop(CommunityMapShopDetailDTO dto, CommunityMapShopDetail shop) {
        Building building = communityProvider.findBuildingById(shop.getBuildingId());

        if (null != building) {
            dto.setBuildingName(building.getName());
        }

        Address address = addressProvider.findAddressById(shop.getAddressId());

        if (null != address) {
            dto.setApartmentName(address.getApartmentName());
        }
        if (null != shop.getShopAvatarUri()) {
            String url = contentServerService.parserUri(shop.getShopAvatarUri(), EntityType.USER.getCode(), UserContext.currentUserId());
            dto.setShopAvatarUrl(url);
        }
    }

    @Override
    public SearchCommunityMapShopsResponse searchCommunityMapShops(SearchCommunityMapShopsCommand cmd) {
    	if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4007040071L, cmd.getAppId(), null,cmd.getCurrentProjectId());//商户管理权限
		}
        SearchCommunityMapShopsResponse response = new SearchCommunityMapShopsResponse();
        Integer pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

        Integer namespaceId = UserContext.getCurrentNamespaceId();

        List<CommunityMapShopDetail> shops = communityMapProvider.searchCommunityMapShops(namespaceId, cmd.getOwnerType(),
                cmd.getOwnerId(), cmd.getBuildingId(), cmd.getKeyword(), cmd.getPageAnchor(), pageSize);

        int size = shops.size();
        if(size > 0){
            response.setShops(shops.stream().map(r -> {
                CommunityMapShopDetailDTO dto = ConvertHelper.convert(r, CommunityMapShopDetailDTO.class);
                populateShop(dto, r);
                return dto;
            }).collect(Collectors.toList()));

            if(size != pageSize){
                response.setNextPageAnchor(null);
            }else{
                response.setNextPageAnchor(shops.get(size-1).getCreateTime().getTime());
            }
        }

        return response;
    }
}
