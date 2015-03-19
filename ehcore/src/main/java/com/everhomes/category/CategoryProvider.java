// @formatter:off
package com.everhomes.category;

import java.util.List;

public interface CategoryProvider {
    void createCategory(Category category);
    void updateCategory(Category category);
    void deleteCategory(Category category);
    void deleteCategory(long id);
    
    Category findCategoryById(long id);
    List<Category> listChildCategories(long parentId);
    List<Category> listDescendantCategories(long ancestorId);
}
