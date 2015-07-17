// @formatter:off
package com.everhomes.business;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.category.Category;
import com.everhomes.category.CategoryDTO;
import com.everhomes.category.CategoryProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.util.ConvertHelper;
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

    @Override
    public void createBusiness(CreateBusinessCommand cmd) {
        if(cmd.getName() == null || cmd.getName().trim().equals(""))
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                    "Invalid paramter name,name is null");
        if(cmd.getCategroies() == null || cmd.getCategroies().isEmpty())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                    "Invalid paramter categories,categories is null");
        if(cmd.getScopes() == null || cmd.getScopes().isEmpty())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                    "Invalid paramter scopes,scopes is null");
    }

    @Override
    public List<BusinessDTO> getBusinessesByCategory(GetBusinessesByCategoryCommand cmd) {
        if(cmd.getCategoryId() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                    "Invalid paramter categoryId,categoryId is null");
        }
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
            dtos.add(dto);
        });
        return dtos;
    }

    @Override
    public void updateBusiness(UpdateBusinessCommand cmd) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteBusiness(DeleteBusinessCommand cmd) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteBusiness(long id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Business findBusinessById(long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<BusinessDTO> getBusinessesByScope(
            GetBusinessesByScopeCommand cmd) {
        // TODO Auto-generated method stub
        return null;
    }
 
}
