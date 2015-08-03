// @formatter:off
package com.everhomes.business;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.jasper.tagplugins.jstl.core.If;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.category.Category;
import com.everhomes.category.CategoryDTO;
import com.everhomes.category.CategoryProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
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

    @Override
    public void createBusiness(CreateBusinessCommand cmd) {
//        if(cmd.getTargetId() == null)
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
//                    "Invalid paramter targetId,targetId is null");
        if(cmd.getBizOwnerUid() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                    "Invalid paramter ownerUid,ownerUid is null");
        if(cmd.getName() == null || cmd.getName().trim().equals(""))
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                    "Invalid paramter name,name is null");
        if(cmd.getCategroies() == null || cmd.getCategroies().isEmpty())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                    "Invalid paramter categories,categories is null");
        if(cmd.getScopes() == null || cmd.getScopes().isEmpty())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
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
    public List<BusinessDTO> getBusinessesByCategory(GetBusinessesByCategoryCommand cmd) {
        if(cmd.getCategoryId() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                    "Invalid paramter categoryId,categoryId is null");
        }
        User user = UserContext.current().getUser();
        long userId = user.getId();
        List<BusinessCategory> busineseCategories = this.businessProvider.findBusinessCategoriesByCategory(cmd.getCategoryId());
        if(busineseCategories == null || busineseCategories.isEmpty())
            return null;
        List<Long> bizIds = busineseCategories.stream().map(r -> r.getOwnerId()).collect(Collectors.toList());
        List<Business> businesses = businessProvider.findBusinessByIds(bizIds);
        Category category = categoryProvider.findCategoryById(cmd.getCategoryId());
        if(category == null){
            LOGGER.error("Category is not found.categoryId=" + cmd.getCategoryId());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                    "Invalid paramter categoryId,categoryId is null");
        }
        
        List<BusinessDTO> dtos = new ArrayList<BusinessDTO>();
        businesses.forEach(r ->{
            BusinessDTO dto = ConvertHelper.convert(r, BusinessDTO.class);
            List<CategoryDTO> categories = new ArrayList<>();
            categories.add(ConvertHelper.convert(category, CategoryDTO.class));
            dto.setCategories(categories);
            dto.setLogoUrl(parserUri(r.getLogoUri(),EntityType.USER.getCode(),userId));
            dtos.add(dto);
        });
        return dtos;
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
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
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
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
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
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
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
