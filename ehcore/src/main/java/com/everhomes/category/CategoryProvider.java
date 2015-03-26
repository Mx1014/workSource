// @formatter:off
package com.everhomes.category;

import java.util.List;

import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;

@SuppressWarnings("unchecked")
public interface CategoryProvider {
    void createCategory(Category category);
    void updateCategory(Category category);
    void deleteCategory(Category category);
    void deleteCategoryById(long id);
    
    Category findCategoryById(long id);
    List<Category> listChildCategories(Long parentId, CategoryAdminStatus status, Tuple<String, SortOrder>... orderBy);
    List<Category> listDescendantCategories(Long ancestorId, CategoryAdminStatus status, Tuple<String, SortOrder>... orderBy);
}
