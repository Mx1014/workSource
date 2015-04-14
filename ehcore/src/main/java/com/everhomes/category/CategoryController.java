package com.everhomes.category;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.category.Category;
import com.everhomes.category.CategoryAdminStatus;
import com.everhomes.category.CategoryDTO;
import com.everhomes.category.CategoryProvider;
import com.everhomes.category.ListCategoryCommand;
import com.everhomes.controller.ControllerBase;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;


/**
 * Category REST API controller
 * 
 * @author Kelven Yang
 *
 */
@RestController
@RequestMapping("/category")
public class CategoryController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryProvider categoryProvider;
    
    @RequireAuthentication(false)
    @RequestMapping("listChildren")
    @RestReturn(value=CategoryDTO.class, collection=true)
    public RestResponse listChildren(@Valid ListCategoryCommand cmd) {
        Tuple<String, SortOrder> orderBy = null;
        if(cmd.getSortBy() != null)
            orderBy = new Tuple<String, SortOrder>(cmd.getSortBy(), SortOrder.fromCode(cmd.getSortOrder()));
        
        @SuppressWarnings("unchecked")
        List<Category> entityResultList = this.categoryProvider.listChildCategories(cmd.getParentId(), 
            CategoryAdminStatus.fromCode(cmd.getStatus()), orderBy);
        
        List<CategoryDTO> dtoResultList = entityResultList.stream()
                .map(r->{ return ConvertHelper.convert(r, CategoryDTO.class); })
                .collect(Collectors.toList());
        return new RestResponse(dtoResultList);
    }
    
    @RequireAuthentication(false)
    @RequestMapping("listDescendants")
    @RestReturn(value=CategoryDTO.class, collection=true)
    public RestResponse listDescendants(@Valid ListCategoryCommand cmd) {
        Tuple<String, SortOrder> orderBy = null;
        if(cmd.getSortBy() != null)
            orderBy = new Tuple<String, SortOrder>(cmd.getSortBy(), SortOrder.fromCode(cmd.getSortOrder()));
        
        @SuppressWarnings("unchecked")
        List<Category> entityResultList = this.categoryProvider.listDescendantCategories(cmd.getParentId(), 
            CategoryAdminStatus.fromCode(cmd.getStatus()), orderBy);
        
        List<CategoryDTO> dtoResultList = entityResultList.stream()
                .map(r->{ return ConvertHelper.convert(r, CategoryDTO.class); })
                .collect(Collectors.toList());
        return new RestResponse(dtoResultList);
    }
}
