// @formatter:off
package com.everhomes.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.db.DbProvider;

public class CategoryProviderImpl implements CategoryProvider {
    
    @Autowired
    private DbProvider dbProvider;

    @Override
    public void createCategory(Category category) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateCategory(Category category) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteCategory(Category category) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteCategory(long id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Category findCategoryById(long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Category> listChildCategories(long parentId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Category> listDescendantCategories(long ancestorId) {
        // TODO Auto-generated method stub
        return null;
    }
}
