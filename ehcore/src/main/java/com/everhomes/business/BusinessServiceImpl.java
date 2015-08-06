// @formatter:off
package com.everhomes.business;


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
import com.everhomes.user.User;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.RuntimeErrorException;





@Component
public class BusinessServiceImpl implements BusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessServiceImpl.class);
    
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
    
    @Override
    public void createBusiness(CreateBusinessCommand cmd) {
//        if(cmd.getTargetId() == null)
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
//                    "Invalid paramter targetId,targetId is null");
        if(cmd.getBizOwnerUid() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid paramter ownerUid,ownerUid is null");
        if(cmd.getName() == null || cmd.getName().trim().equals(""))
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid paramter name,name is null");
        if(cmd.getCategroies() == null || cmd.getCategroies().isEmpty())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid paramter categories,categories is null");
        if(cmd.getScopes() == null || cmd.getScopes().isEmpty())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid paramter scopes,scopes is null");
        User user = UserContext.current().getUser();
        long userId = user.getId();
        
        Business business = ConvertHelper.convert(cmd, Business.class);
        business.setCreatorUid(userId);
        business.setDescription(cmd.getDescription() == null ? "" : cmd.getDescription());
        business.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        if(cmd.getLatitude() != null && cmd.getLongitude() != null){
            String geohash = GeoHashUtils.encode(cmd.getLatitude(), cmd.getLongitude());
            business.setGeohash(geohash);
            
        }
        this.dbProvider.execute((TransactionStatus status) -> {
            this.businessProvider.createBusiness(business);
            cmd.getScopes().forEach(r ->{
                BusinessVisibleScope scope = ConvertHelper.convert(r,BusinessVisibleScope.class);
                scope.setOwnerId(business.getId());
                this.businessProvider.createBusinessVisibleScope(scope);
            });
            cmd.getCategroies().forEach(r ->{
                BusinessCategory category = new BusinessCategory();
                category.setCategoryId(r.longValue());
                category.setOwnerId(business.getId());
                Category c = categoryProvider.findCategoryById(r.longValue());
                if(c != null)
                    category.setCategoryPath(c.getPath());
                this.businessProvider.createBusinessCategory(category);
            });
            return null;
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
        
        int pageOffset = cmd.getPageOffset() == null ? 1 : cmd.getPageOffset();
        int pageSize = cmd.getPageSize() == null ? this.configurationProvider.getIntValue("pagination.page.size", 
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE) : cmd.getPageSize();
        int offset = (int) PaginationHelper.offsetFromPageOffset((long)pageOffset, pageSize);
        
        GetBusinessesByCategoryCommandResponse response = new GetBusinessesByCategoryCommandResponse();
        List<BusinessCategory> busineseCategories = this.businessProvider.findBusinessCategoriesByCategory(cmd.getCategoryId(),offset,pageSize);
        if(busineseCategories == null || busineseCategories.isEmpty())
            return response;
        List<Long> bizIds = busineseCategories.stream().map(r -> r.getOwnerId()).collect(Collectors.toList());
        List<Business> businesses = businessProvider.findBusinessByIds(bizIds);
        Category category = categoryProvider.findCategoryById(cmd.getCategoryId());
        if(category == null){
            LOGGER.error("Category is not found.categoryId=" + cmd.getCategoryId());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid paramter categoryId,categoryId is null");
        }
        //user favorite
        List<Long> favoriteBizIds = userActivityProvider.findFavorite(userId).stream()
                .filter(r -> r.getTargetType().equalsIgnoreCase("biz")).map(r->r.getTargetId()).collect(Collectors.toList());
        //recommand to user
        //TODO
        
        List<CommunityGeoPoint> points = communityProvider.listCommunityGeoPoints(cmd.getCommunityId());
        if(points == null || points.isEmpty()){
            LOGGER.error("Community is not exists geo points,communityId=" + cmd.getCommunityId());
            return response;
        }
        CommunityGeoPoint point = points.get(0);
        final double lat = point != null ? point.getLatitude() : 0;
        final double lon = point != null ? point.getLongitude() : 0;
        List<BusinessDTO> dtos = new ArrayList<BusinessDTO>();
        businesses.forEach(r ->{
            
            BusinessDTO dto = ConvertHelper.convert(r, BusinessDTO.class);
            List<CategoryDTO> categories = new ArrayList<>();
            categories.add(ConvertHelper.convert(category, CategoryDTO.class));
            dto.setCategories(categories);
            dto.setLogoUrl(parserUri(r.getLogoUri(),EntityType.USER.getCode(),userId));
            if(favoriteBizIds != null && favoriteBizIds.contains(r.getId())){
                dto.setFavoriteStatus(BusinessFavoriteStatus.FAVORITE.getCode());
            }
            
            dto.setRecommendStatus(BusinessRecommendStatus.NONE.getCode());
            if(lat != 0 || lon != 0)
                dto.setDistance((int)calculateDistance(r.getLatitude(),r.getLongitude(),lat, lon));
            else
                dto.setDistance(0);
            
            dtos.add(dto);
        });
        if(busineseCategories != null && busineseCategories.size() == pageSize){
            response.setNextPageOffset(pageOffset + 1);
        }
        
        sortBusinesses(dtos);
        
        response.setRequests(dtos);
        return response;
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
            if(cmd.getScopes() != null && !cmd.getScopes().isEmpty()){
                this.businessProvider.deleteBusinessVisibleScopeByBusinessId(business.getId());
                cmd.getScopes().forEach(r ->{
                    BusinessVisibleScope scope = ConvertHelper.convert(r,BusinessVisibleScope.class);
                    scope.setOwnerId(business.getId());
                    this.businessProvider.createBusinessVisibleScope(scope);
                });
            }
            if(cmd.getCategroies() != null && !cmd.getCategroies().isEmpty()){
                this.businessProvider.deleteBusinessCategoryByBusinessId(business.getId());
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
 
}
