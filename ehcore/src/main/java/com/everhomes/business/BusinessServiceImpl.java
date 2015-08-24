// @formatter:off
package com.everhomes.business;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.lucene.spatial.DistanceUtils;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import ch.hsr.geohash.GeoHash;

import com.everhomes.business.admin.CreateBusinessAdminCommand;
import com.everhomes.business.admin.ListBusinessesByKeywordAdminCommand;
import com.everhomes.business.admin.ListBusinessesByKeywordAdminCommandResponse;
import com.everhomes.business.admin.RecommendBusinessesAdminCommand;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryDTO;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityGeoPoint;
import com.everhomes.community.CommunityProvider;
import com.everhomes.community.CommunityServiceErrorCode;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.core.AppConfig;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserActivityService;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserFavorite;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.RuntimeErrorException;





@Component
public class BusinessServiceImpl implements BusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessServiceImpl.class);
    private static final String BUSINESS_HOME_URL = "business.home.url";
    private static final String BUSINESS_DETAIL_URL = "business.detail.url";
    private static final String AUTHENTICATE_PREFIX_URL = "authenticate.prefix.url";
    //private static final String PREFIX_URL = "prefix.url";
    private static final String BUSINESS_IMAGE_URL = "business.image.url";
    @Autowired
    private BusinessProvider businessProvider;
    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private CategoryProvider categoryProvider;
    @Autowired
    private ContentServerService contentServerService;
    @Autowired
    private ConfigurationProvider configurationProvider;
    @Autowired
    private UserActivityProvider userActivityProvider;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private CommunityProvider communityProvider;
    @Autowired
    private UserActivityService userActivityService;
    @Autowired
    private RegionProvider regionProvider;
    
    @Override
    public void syncBusiness(SyncBusinessCommand cmd) {
        if(cmd.getUserId() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid paramter userId,userId is null");
        
        User user = userProvider.findUserById(cmd.getUserId());
        if(user == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid paramter userId,userId is not found");
        }
        if(cmd.getTargetId() == null || cmd.getTargetId().trim().equals("")){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid paramter targetId,targetId is null");
        }
        long userId = user.getId();
        this.dbProvider.execute((TransactionStatus status) -> {
            Business business = this.businessProvider.findBusinessByTargetId(cmd.getTargetId());
            
            if(business == null){
                business = createBusiness(cmd, userId);
            }else{
                updateBusiness(cmd, business);
            }
            
            if(cmd.getCategroies() != null && !cmd.getCategroies().isEmpty()){
                createBusinessCategories(business, cmd.getCategroies());
            }
            return true;
        });
        
    }
    
    private Business createBusiness(BusinessCommand cmd, long userId){
        if(cmd.getName() == null || cmd.getName().trim().equals(""))
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid paramter name,name is null");
        if(cmd.getCategroies() == null || cmd.getCategroies().isEmpty())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid paramter categories,categories is null");
        
        Business business = ConvertHelper.convert(cmd, Business.class);
        business.setCreatorUid(userId);
        business.setBizOwnerUid(cmd.getBizOwnerUid() == null ? userId : cmd.getBizOwnerUid());
        business.setTargetId(cmd.getTargetId() == null ? "" : cmd.getTargetId());
        business.setDescription(cmd.getDescription() == null ? "" : cmd.getDescription());
        business.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        if(cmd.getLatitude() != null && cmd.getLongitude() != null){
            String geohash = GeoHashUtils.encode(cmd.getLatitude(), cmd.getLongitude());
            business.setGeohash(geohash);
        }
        this.businessProvider.createBusiness(business);
        return business;
    }
    
    private void updateBusiness(BusinessCommand cmd, Business business){
        if(cmd.getAddress() != null && !cmd.getAddress().trim().equals(""))
            business.setAddress(cmd.getAddress());
        if(cmd.getContact() != null && !cmd.getContact().trim().equals(""))
            business.setContact(cmd.getContact());
        if(cmd.getDescription() != null && !cmd.getDescription().trim().equals(""))
            business.setDescription(cmd.getDescription());
        if(cmd.getDisplayName() != null && !cmd.getDisplayName().trim().equals(""))
            business.setDisplayName(cmd.getDisplayName());
        if(cmd.getLatitude() != null)
            business.setLatitude(cmd.getLatitude());
        if(cmd.getLongitude() != null)
            business.setLongitude(cmd.getLongitude());
        if(cmd.getLogoUri() != null && !cmd.getLogoUri().trim().equals(""))
            business.setLogoUri(cmd.getLogoUri());
        if(cmd.getName() != null && !cmd.getName().trim().equals(""))
            business.setName(cmd.getName());
        if(cmd.getPhone() != null && !cmd.getPhone().trim().equals(""))
            business.setPhone(cmd.getPhone());
        if(cmd.getTargetId() != null && !cmd.getTargetId().trim().equals(""))
            business.setTargetId(cmd.getTargetId());
        if(cmd.getTargetType() != null)
            business.setTargetType(cmd.getTargetType());
        if(cmd.getUrl() != null && cmd.getUrl().trim().equals(""))
            business.setUrl(cmd.getUrl());
        
        if(cmd.getLatitude() != null && cmd.getLongitude() != null){
            String geohash = GeoHashUtils.encode(cmd.getLatitude(), cmd.getLongitude());
            business.setGeohash(geohash);
        }
        business.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        
        this.businessProvider.updateBusiness(business);
    }
    
    private void createBusinessScopes(Business business, List<BusinessScope> scopes){
        this.businessProvider.deleteBusinessVisibleScopeByBusiness(business);
        scopes.forEach(r ->{
            BusinessVisibleScope scope = ConvertHelper.convert(r,BusinessVisibleScope.class);
            scope.setOwnerId(business.getId());
            this.businessProvider.createBusinessVisibleScope(scope);
        });
    }
    
    private void createBusinessCategories(Business business, List<Long> categories){
        this.businessProvider.deleteBusinessCategoryByBusiness(business);
        categories.forEach(r ->{
            BusinessCategory category = new BusinessCategory();
            category.setCategoryId(r.longValue());
            category.setOwnerId(business.getId());
            Category c = categoryProvider.findCategoryById(r.longValue());
            if(c != null)
                category.setCategoryPath(c.getPath());
            this.businessProvider.createBusinessCategory(category);
        });
    }

    @Override
    public GetBusinessesByCategoryCommandResponse getBusinessesByCategory(GetBusinessesByCategoryCommand cmd) {
        if(cmd.getCategoryId() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid paramter categoryId,categoryId is null");
        }
        if(cmd.getCommunityId() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid paramter communityId,communityId is null");
        }
        Community community = communityProvider.findCommunityById(cmd.getCommunityId());
        if(community == null){
            LOGGER.error("Community is not exists,communityId=" + cmd.getCommunityId());
            throw RuntimeErrorException.errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST, 
                    "Invalid paramter communityId,communityId is not exists.");
        }
            
        User user = UserContext.current().getUser();
        long userId = user.getId();
        long startTime = System.currentTimeMillis();
        int pageOffset = cmd.getPageOffset() == null ? 1 : cmd.getPageOffset();
        int pageSize = cmd.getPageSize() == null ? this.configurationProvider.getIntValue("pagination.page.size", 
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE) : cmd.getPageSize();
        int offset = (int) PaginationHelper.offsetFromPageOffset((long)pageOffset, pageSize);
        
        GetBusinessesByCategoryCommandResponse response = new GetBusinessesByCategoryCommandResponse();
        //只作校验用
        List<BusinessCategory> busineseCategories = this.businessProvider.findBusinessCategoriesByCategory(cmd.getCategoryId(),offset,pageSize);
        if(busineseCategories == null || busineseCategories.isEmpty())
            return response;

        List<CommunityGeoPoint> points = communityProvider.listCommunityGeoPoints(cmd.getCommunityId());
        if(points == null || points.isEmpty()){
            LOGGER.error("Community is not exists geo points,communityId=" + cmd.getCommunityId());
            return response;
        }
        CommunityGeoPoint point = points.get(0);
        final double lat = point != null ? point.getLatitude() : 0;
        final double lon = point != null ? point.getLongitude() : 0;

        List<String> geoHashList = getGeoHashCodeList(lat, lon);
        
        //获取指定类型的服务
        List<Business> businesses  = this.businessProvider.findBusinessByCategroy(cmd.getCategoryId(),geoHashList);
        
        Category category = categoryProvider.findCategoryById(cmd.getCategoryId());
        if(category == null){
            LOGGER.error("Category is not found.categoryId=" + cmd.getCategoryId());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid paramter categoryId,categoryId is null");
        }
        //user favorite
        List<Long> favoriteBizIds = userActivityProvider.findFavorite(userId).stream()
                .filter(r -> r.getTargetType().equalsIgnoreCase("biz")).map(r->r.getTargetId()).collect(Collectors.toList());
        //recommend to user
        List<Long> recommendBizIds = this.businessProvider.findBusinessAssignedScopeByScope(community.getCityId(),cmd.getCommunityId()).stream()
                .map(r->r.getOwnerId()).collect(Collectors.toList());
        
        final String businessHomeUrl = configurationProvider.getValue(BUSINESS_HOME_URL, "");
        final String businessDetailUrl = configurationProvider.getValue(BUSINESS_DETAIL_URL, "");
        final String authenticatePrefix = configurationProvider.getValue(AUTHENTICATE_PREFIX_URL, "");
        //final String prefixUrl = configurationProvider.getValue(PREFIX_URL, "");
        final String imageUrl = configurationProvider.getValue(BUSINESS_IMAGE_URL, "");
        List<BusinessDTO> dtos = new ArrayList<BusinessDTO>();
        businesses.forEach(r ->{
            
            BusinessDTO dto = ConvertHelper.convert(r, BusinessDTO.class);
            List<CategoryDTO> categories = new ArrayList<>();
            categories.add(ConvertHelper.convert(category, CategoryDTO.class));
            dto.setCategories(categories);
            dto.setLogoUrl(processLogoUrl(r, userId,imageUrl));
            dto.setUrl(processUrl(r,businessHomeUrl,businessDetailUrl,authenticatePrefix));
            if(favoriteBizIds != null && favoriteBizIds.contains(r.getId()))
                dto.setFavoriteStatus(BusinessFavoriteStatus.FAVORITE.getCode());
            else
                dto.setFavoriteStatus(BusinessFavoriteStatus.NONE.getCode());
            
            if(recommendBizIds != null && recommendBizIds.contains(r.getId())){
                dto.setRecommendStatus(BusinessRecommendStatus.RECOMMEND.getCode());
                //删除掉已在列表中的
                recommendBizIds.remove(r.getId());
            }
            else
                dto.setRecommendStatus(BusinessRecommendStatus.NONE.getCode());
            
            if(lat != 0 || lon != 0)
                dto.setDistance((int)calculateDistance(r.getLatitude(),r.getLongitude(),lat, lon));
            else
                dto.setDistance(0);
            
            dtos.add(dto);
        });
        
        processRecommendBusinesses(recommendBizIds,dtos,category,userId,favoriteBizIds);
        
        sortBusinesses(dtos);
        
        response.setRequests(dtos);
        long endTime = System.currentTimeMillis();
        LOGGER.info("GetBusinesses by category,categoryId=" + cmd.getCategoryId() 
                + ",communityId=" + cmd.getCommunityId() + ",elapse=" + (endTime - startTime));
        
        return response;
    }
    
    private String processUrl(Business business, String businessHomeUrl,String prefix,String authenticatePrefix){
        if(businessHomeUrl.trim().equals(""))
            LOGGER.error("Buiness home url is empty.");
        if(prefix.trim().equals(""))
            LOGGER.error("Buiness detail url is empty.");
        if(authenticatePrefix.trim().equals(""))
            LOGGER.error("Buiness authenticate prefix url is empty.");
        if(business.getTargetType() == BusinessTargetType.ZUOLIN.getCode()){
            String businessDetailUrl = null;
            try {
                businessDetailUrl = URLEncoder.encode(businessHomeUrl.trim() + prefix.trim() + business.getTargetId(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("unsported encoding.");
            }
            return authenticatePrefix.trim() + businessDetailUrl;
        }
            
        return business.getUrl();
    }
    
    private void processRecommendBusinesses( List<Long> recommendBizIds, List<BusinessDTO> dtos, 
            Category category, long userId,List<Long> favoriteBizIds){
        List<BusinessCategory> businessCategories = this.businessProvider.findBusinessCategoriesByIdAndOwnerIds(category.getId(),recommendBizIds);
        if(businessCategories == null || businessCategories.isEmpty())
            return;
        List<Long> bizIds = businessCategories.stream().map(r -> r.getOwnerId()).collect(Collectors.toList());
        List<Business> recomBusinesses = this.businessProvider.findBusinessByIds(bizIds);
        if(recomBusinesses != null && !recomBusinesses.isEmpty()){
            String imageUrl = configurationProvider.getValue(BUSINESS_IMAGE_URL, "");
            recomBusinesses.forEach(r ->{
                BusinessDTO dto = ConvertHelper.convert(r, BusinessDTO.class);
                List<CategoryDTO> categories = new ArrayList<>();
                categories.add(ConvertHelper.convert(category, CategoryDTO.class));
                dto.setCategories(categories);
                dto.setLogoUrl(processLogoUrl(r,userId,imageUrl));
                if(favoriteBizIds != null && favoriteBizIds.contains(r.getId()))
                    dto.setFavoriteStatus(BusinessFavoriteStatus.FAVORITE.getCode());
                else
                    dto.setFavoriteStatus(BusinessFavoriteStatus.NONE.getCode());
                 dto.setRecommendStatus(BusinessRecommendStatus.RECOMMEND.getCode());
                 dto.setDistance(0);
                
                dtos.add(dto);
            });
        }
    }
    
    private String processLogoUrl(Business business, long userId,String imageUrl){
        if(business.getLogoUri() == null || business.getLogoUri().trim().equals(""))
            return null;
        if(business.getTargetType() != BusinessTargetType.ZUOLIN.getCode())
            return parserUri(business.getLogoUri(),EntityType.USER.getCode(),userId);
        return imageUrl.trim() + business.getLogoUri();
    }
    
    private double calculateDistance(double latitude, double longitude, double lat, double lon){
      //getDistanceMi计算的是英里
        final double MILES_KILOMETRES_RATIO = 1.609344;
        return DistanceUtils.getDistanceMi(latitude,longitude,lat , lon) * MILES_KILOMETRES_RATIO * 1000;
    }
    private List<BusinessDTO> sortBusinesses(List<BusinessDTO> dtos){
        if(dtos == null || dtos.isEmpty())
            return dtos;
        
        //sort by distance
        dtos.sort(new Comparator<BusinessDTO>() {
            @Override
            public int compare(BusinessDTO o1, BusinessDTO o2) {
                
                return (int) (o1.getDistance() - o2.getDistance());
            }
        });
        
        //recommand business first
        dtos.sort(new Comparator<BusinessDTO>() {
            @Override
            public int compare(BusinessDTO o1, BusinessDTO o2) {
                return o2.getRecommendStatus() - o1.getRecommendStatus();
            }
        });
        
        //sort by targetType
        dtos.sort(new Comparator<BusinessDTO>() {
            @Override
            public int compare(BusinessDTO o1, BusinessDTO o2) {
                return o2.getTargetType() - o1.getTargetType();
            }
        });
        
        
        return null;
    }
    
    private String parserUri(String uri,String ownerType, long ownerId){
        try {
            if(!org.apache.commons.lang.StringUtils.isEmpty(uri))
                return contentServerService.parserUri(uri,ownerType,ownerId);
            
        } catch (Exception e) {
            LOGGER.error("Parser uri is error." + e.getMessage());
        }
        return null;

    }

    private List<String> getGeoHashCodeList(double latitude, double longitude){
        
        GeoHash geo = GeoHash.withCharacterPrecision(latitude, longitude, 6);
        GeoHash[] adjacents = geo.getAdjacent();
        List<String> geoHashCodes = new ArrayList<String>();
        geoHashCodes.add(geo.toBase32());
        for(GeoHash g : adjacents) {
            geoHashCodes.add(g.toBase32());
        }
        return geoHashCodes;
    }
    
    @Override
    public void updateBusiness(UpdateBusinessCommand cmd) {
        if(cmd.getId() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid paramter id null,categoryId is null");
        Business business = this.businessProvider.findBusinessById(cmd.getId());
        if(business == null){
            LOGGER.error("Business is not exists.id=" + cmd.getId());
            throw RuntimeErrorException.errorWith(BusinessServiceErrorCode.SCOPE, BusinessServiceErrorCode.ERROR_BUSINESS_NOT_EXIST, 
                    "Business is not exists.");
        }
        if(cmd.getAddress() != null && !cmd.getAddress().trim().equals(""))
            business.setAddress(cmd.getAddress());
        if(cmd.getContact() != null && !cmd.getContact().trim().equals(""))
            business.setContact(cmd.getContact());
        if(cmd.getDescription() != null && !cmd.getDescription().trim().equals(""))
            business.setDescription(cmd.getDescription());
        if(cmd.getDisplayName() != null && !cmd.getDisplayName().trim().equals(""))
            business.setDisplayName(cmd.getDisplayName());
        if(cmd.getLatitude() != null)
            business.setLatitude(cmd.getLatitude());
        if(cmd.getLongitude() != null)
            business.setLongitude(cmd.getLongitude());
        if(cmd.getLogoUri() != null && !cmd.getLogoUri().trim().equals(""))
            business.setLogoUri(cmd.getLogoUri());
        if(cmd.getName() != null && !cmd.getName().trim().equals(""))
            business.setName(cmd.getName());
        if(cmd.getPhone() != null && !cmd.getPhone().trim().equals(""))
            business.setPhone(cmd.getPhone());
        if(cmd.getTargetId() != null && !cmd.getTargetId().trim().equals(""))
            business.setTargetId(cmd.getTargetId());
        if(cmd.getTargetType() != null)
            business.setTargetType(cmd.getTargetType());
        if(cmd.getUrl() != null && cmd.getUrl().trim().equals(""))
            business.setUrl(cmd.getUrl());
        
        if(cmd.getLatitude() != null && cmd.getLongitude() != null){
            String geohash = GeoHashUtils.encode(cmd.getLatitude(), cmd.getLongitude());
            business.setGeohash(geohash);
            
        }
        this.dbProvider.execute((TransactionStatus status) -> {
            this.businessProvider.updateBusiness(business);
            if(cmd.getCategroies() != null && !cmd.getCategroies().isEmpty()){
                this.businessProvider.deleteBusinessCategoryByBusiness(business);
                cmd.getCategroies().forEach(r ->{
                    BusinessCategory category = new BusinessCategory();
                    category.setCategoryId(r.longValue());
                    category.setOwnerId(business.getId());
                    Category c = categoryProvider.findCategoryById(r.longValue());
                    if(c != null)
                        category.setCategoryPath(c.getPath());
                    this.businessProvider.createBusinessCategory(category);
                });
            }
           
            return null;
        });
    }

    @Override
    public void deleteBusiness(DeleteBusinessCommand cmd) {
        if(cmd.getId() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid paramter id null,categoryId is null");
        Business business = this.businessProvider.findBusinessById(cmd.getId());
        if(business == null){
            LOGGER.error("Business is not exists.id=" + cmd.getId());
            throw RuntimeErrorException.errorWith(BusinessServiceErrorCode.SCOPE, BusinessServiceErrorCode.ERROR_BUSINESS_NOT_EXIST, 
                    "Business is not exists.");
        }
        this.businessProvider.deleteBusiness(business);
        
    }

    @Override
    public BusinessDTO findBusinessById(Long id) {
        if(id == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid paramter id null,categoryId is null");
        Business business = this.businessProvider.findBusinessById(id);
        if(business != null)
            return ConvertHelper.convert(business,BusinessDTO.class);
        return null;
    }

    @Override
    public List<BusinessDTO> getBusinessesByScope(GetBusinessesByScopeCommand cmd) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ListBusinessesByKeywordAdminCommandResponse listBusinessesByKeyword(ListBusinessesByKeywordAdminCommand cmd) {
        int pageOffset = cmd.getPageOffset() == null ? 1 : cmd.getPageOffset();
        int pageSize = cmd.getPageSize() == null ? this.configurationProvider.getIntValue("pagination.page.size", 
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE) : cmd.getPageSize();
        User user = UserContext.current().getUser();
        int offset = (int) PaginationHelper.offsetFromPageOffset((long)pageOffset, pageSize);
        List<BusinessDTO> result = null;
        final String imageUrl = configurationProvider.getValue(BUSINESS_IMAGE_URL, "");
        List<Business> businesses = this.businessProvider.listBusinessesByKeyword(cmd.getKeyword() , offset , pageSize);
        if(businesses != null && !businesses.isEmpty())
            result = businesses.stream().map(r -> {
                    BusinessDTO dto = ConvertHelper.convert(r, BusinessDTO.class);
                    dto.setLogoUrl(processLogoUrl(r,user.getId(),imageUrl));
                    //set recommend status
                    processRecommendStatus(dto);
                    
                    return dto;
                    }
            ).collect(Collectors.toList());
        
        
        ListBusinessesByKeywordAdminCommandResponse response = new ListBusinessesByKeywordAdminCommandResponse();
        response.setRequests(result);
        if(result != null && result.size() == pageSize)
            response.setNextPageOffset(pageOffset + 1);
        return response;
    }

    private void processRecommendStatus(BusinessDTO dto) {
        List<BusinessAssignedScope> assignedScopes = this.businessProvider.findBusinessAssignedScopesByBusinessId(dto.getId());
        if(assignedScopes != null && !assignedScopes.isEmpty()){
            dto.setRecommendStatus(BusinessRecommendStatus.RECOMMEND.getCode());
            List<BusinessAssignedScopeDTO> scopes = new ArrayList<>();
            assignedScopes.forEach(a ->{
                
                BusinessAssignedScopeDTO scopeDTO = ConvertHelper.convert(a,BusinessAssignedScopeDTO.class);
                if(a.getScopeCode().byteValue() == BusinessScopeType.ALL.getCode())
                    scopeDTO.setRegionName("全国");
                else if(a.getScopeCode().byteValue() == BusinessScopeType.CITY.getCode()){
                    Region region = this.regionProvider.findRegionById(scopeDTO.getScopeId());
                    if(region != null)
                        scopeDTO.setRegionName(region.getName());
                }
                else{
                    Community community = this.communityProvider.findCommunityById(scopeDTO.getScopeId());
                    if(community != null)
                        scopeDTO.setRegionName(community.getName());
                }
                scopes.add(scopeDTO);
            });
            dto.setAssignedScopes(scopes);
        }
        
    }

    @Override
    public void recommendBusiness(RecommendBusinessesAdminCommand cmd) {
        if(cmd.getId() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid paramter id null,id is null");
        if(cmd.getScopes() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid paramter scopes null,scopes is null");
        }
        
        Business business = this.businessProvider.findBusinessById(cmd.getId());
        if(business == null){
            LOGGER.error("Business is not exits.id=" + cmd.getId());
            throw RuntimeErrorException.errorWith(BusinessServiceErrorCode.SCOPE, BusinessServiceErrorCode.ERROR_BUSINESS_NOT_EXIST, 
                    "Business is not exits.id=" + cmd.getId());
        }
        
        this.dbProvider.execute((TransactionStatus status) -> {
            this.businessProvider.deleteBusinessAssignedScopeByBusinessId(cmd.getId());
            if(cmd.getRecommendStatus().byteValue() == BusinessRecommendStatus.RECOMMEND.getCode()){
                cmd.getScopes().forEach(r ->{
                    BusinessAssignedScope scope = ConvertHelper.convert(r,BusinessAssignedScope.class);
                    scope.setOwnerId(cmd.getId());
                    this.businessProvider.createBusinessAssignedScope(scope);
                });
            }
            return true;
        });
        
    }

    @Override
    public void syncDeleteBusiness(SyncDeleteBusinessCommand cmd) {
        if(cmd.getId() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid paramter id null,categoryId is null");
        Business business = this.businessProvider.findBusinessByTargetId(cmd.getId());
        if(business == null){
            LOGGER.error("Business is not exists.id=" + cmd.getId());
            throw RuntimeErrorException.errorWith(BusinessServiceErrorCode.SCOPE, BusinessServiceErrorCode.ERROR_BUSINESS_NOT_EXIST, 
                    "Business is not exists.");
        }
        User user = userProvider.findUserById(cmd.getUserId());
        if(user == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid paramter userId,userId is not found");
        }
        this.businessProvider.deleteBusiness(business.getId());
        this.userActivityService.cancelShop(user.getId());
        
    }

    @Override
    public void createBusiness(CreateBusinessAdminCommand cmd) {
        if(cmd.getUrl() == null || cmd.getUrl().trim().equals("")){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid paramter url,url is null");
        }
        
        User user = UserContext.current().getUser();
        long userId = user.getId();
        this.dbProvider.execute((TransactionStatus status) -> {
            cmd.setLatitude(0D);
            cmd.setLongitude(0D);
            Business business = createBusiness(cmd, userId);
            
            if(cmd.getCategroies() != null && !cmd.getCategroies().isEmpty()){
                createBusinessCategories(business, cmd.getCategroies());
            }
            return true;
        });
    }

    @Override
    public void syncUserFavorite(SyncUserFavoriteCommand cmd) {
        isValiad(cmd);
        User user = userProvider.findUserById(cmd.getUserId());
        Business business = this.businessProvider.findBusinessByTargetId(cmd.getId());
        UserFavorite userFavorite = new UserFavorite();
        userFavorite.setOwnerUid(user.getId());
        userFavorite.setTargetType("biz");
        userFavorite.setTargetId(business.getId());
        userFavorite.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        userActivityProvider.addUserFavorite(userFavorite);
        
    }
    
    private boolean isValiad(SyncUserFavoriteCommand cmd){
        if(cmd.getUserId() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid paramter userId,userId is not found");
        }
        User user = userProvider.findUserById(cmd.getUserId());
        if(user == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid paramter userId,userId is not found");
        }
        if(cmd.getId() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid paramter id null,categoryId is null");
        Business business = this.businessProvider.findBusinessByTargetId(cmd.getId());
        if(business == null){
            LOGGER.error("Business is not exists.id=" + cmd.getId());
            throw RuntimeErrorException.errorWith(BusinessServiceErrorCode.SCOPE, BusinessServiceErrorCode.ERROR_BUSINESS_NOT_EXIST, 
                    "Business is not exists.");
        }
        return true;
    }

    @Override
    public void syncUserCancelFavorite(SyncUserFavoriteCommand cmd) {
        isValiad(cmd);
        User user = userProvider.findUserById(cmd.getUserId());
        Business business = this.businessProvider.findBusinessByTargetId(cmd.getId());
        userActivityProvider.deleteFavorite(user.getId(), business.getId(), "biz");
        
    }

    @Override
    public Byte findBusinessFavoriteStatus(SyncUserFavoriteCommand cmd) {
        isValiad(cmd);
        User user = userProvider.findUserById(cmd.getUserId());
        Business business = this.businessProvider.findBusinessByTargetId(cmd.getId());
        List<Long> ret = userActivityProvider.findFavorite(user.getId()).stream()
                .filter(r -> r.getTargetType().equalsIgnoreCase("biz")).map(r->r.getTargetId()).collect(Collectors.toList());
        if(ret != null && !ret.isEmpty()&& ret.contains(business.getId())){
            return BusinessFavoriteStatus.FAVORITE.getCode();
        }
        return BusinessFavoriteStatus.NONE.getCode();
    }
 
}
