// @formatter:off
package com.everhomes.category;

import java.util.List;

import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;

@SuppressWarnings("unchecked")
public interface CategoryProvider {
    void createCategory(Category category);
    void updateCategory(Category category);
    void deleteCategory(Category category);
    void deleteCategoryById(long id);
    
    Category findCategoryById(long id);
    List<Category> listAllCategories();
    List<Category> listChildCategories(Integer namespaceId, Long parentId, CategoryAdminStatus status, Tuple<String, SortOrder>... orderBy);
    List<Category> listDescendantCategories(Long ancestorId, CategoryAdminStatus status, Tuple<String, SortOrder>... orderBy);
    List<Category> listRootCategories();
    
    List<Category> listContentCategories();
    List<Category> listActionCategories(Long parentId);
    
    List<Category> listBusinessSubCategories(long categoryId, CategoryAdminStatus status, Tuple<String, SortOrder>... orderBy);
    List<Long> getBusinessSubCategories(long categoryId);
    
    List<Category> listTaskCategories(Integer namespaceId, Long parentId, String keyword, Long pageAnchor, Integer pageSize);
	Category findCategoryByNamespaceAndName(Long parentId, Integer namespaceId, String categoryName);
}
