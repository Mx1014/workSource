// @formatter:off
package com.everhomes.category;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.jooq.SortField;
import org.jooq.impl.DefaultRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.jooq.JooqHelper;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhCategoriesDao;
import com.everhomes.server.schema.tables.pojos.EhCategories;
import com.everhomes.server.schema.tables.records.EhCategoriesRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;

@Component
public class CategoryProviderImpl implements CategoryProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryProviderImpl.class);
    
    @Autowired
    private DbProvider dbProvider;

    @Caching(evict = { @CacheEvict(value="listChildCategory"),
            @CacheEvict(value="listDescendantCategory"),
            @CacheEvict(value="listAllCategory") })
    @Override
    public void createCategory(Category category) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        
        EhCategoriesRecord record = ConvertHelper.convert(category, EhCategoriesRecord.class);
        InsertQuery<EhCategoriesRecord> query = context.insertQuery(Tables.EH_CATEGORIES);
        query.setRecord(record);
        query.setReturning(Tables.EH_CATEGORIES.ID);
        query.execute();
        
        category.setId(query.getReturnedRecord().getId());
        
//        EhCategoriesDao dao = new EhCategoriesDao(context.configuration());
//        dao.insert(category);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhCategories.class, null);
    }

    @Caching(evict = { @CacheEvict(value="Category", key="#category.id"),
            @CacheEvict(value="listChildCategory"),
            @CacheEvict(value="listDescendantCategory"),
            @CacheEvict(value="listAllCategory") })
    @Override
    public void updateCategory(Category category) {
        assert(category.getId() != null);
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhCategoriesDao dao = new EhCategoriesDao(context.configuration());
        dao.update(ConvertHelper.convert(category, EhCategories.class));
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCategories.class, category.getId());
    }

    @Caching(evict = { @CacheEvict(value="Category", key="#category.id"),
            @CacheEvict(value="listChildCategory"),
            @CacheEvict(value="listDescendantCategory"),
            @CacheEvict(value="listAllCategory") })
    @Override
    public void deleteCategory(Category category) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhCategoriesDao dao = new EhCategoriesDao(context.configuration());
        
        dao.deleteById(category.getId());
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCategories.class, category.getId());
    }

    @Override
    public void deleteCategoryById(long id) {
        CategoryProvider self = PlatformContext.getComponent(CategoryProvider.class);
        Category category = self.findCategoryById(id);
        if(category != null) {
            deleteCategory(category);
        }
    }

    @Cacheable(value="Category", key="#id")
    @Override
    public Category findCategoryById(long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhCategoriesDao dao = new EhCategoriesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), Category.class);
    }
    
    @Cacheable(value = "listAllCategory")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public List<Category> listAllCategories() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        
        SelectJoinStep<Record> selectStep = context.select().from(Tables.EH_CATEGORIES);
        selectStep.where(Tables.EH_CATEGORIES.STATUS.eq(CategoryAdminStatus.ACTIVE.getCode()));
        List<Category> result = selectStep.fetch().map(
                new DefaultRecordMapper(Tables.EH_CATEGORIES.recordType(), Category.class)
            );
        
        return result;
    }

    @Cacheable(value = "listChildCategory")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public List<Category> listChildCategories(Long parentId, CategoryAdminStatus status,
            Tuple<String, SortOrder>... orderBy) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        //暂不向客户端开放排序字段指定 20150519
        SortField[] orderByFields = null;
        if(orderBy != null && orderBy.length != 0 && orderBy[0] != null)
        		orderByFields = JooqHelper.toJooqFields(Tables.EH_CATEGORIES, orderBy);
        List<Category> result;
        
        SelectJoinStep<Record> selectStep = context.select().from(Tables.EH_CATEGORIES);
        Condition condition = null;
        
        if(parentId != null)
            condition = Tables.EH_CATEGORIES.PARENT_ID.eq(parentId.longValue());
        else
            condition = Tables.EH_CATEGORIES.PARENT_ID.isNull().or(Tables.EH_CATEGORIES.PARENT_ID.eq(0L));
            
        if(status != null)
            condition = condition.and(Tables.EH_CATEGORIES.STATUS.eq(status.getCode()));
        
        if(condition != null) {
            selectStep.where(condition);
        }
        
        if(orderByFields != null) {
            result = selectStep.orderBy(orderByFields).fetch().map(
                new DefaultRecordMapper(Tables.EH_CATEGORIES.recordType(), Category.class)
            );
        } else {
            result = selectStep.fetch().map(
                new DefaultRecordMapper(Tables.EH_CATEGORIES.recordType(), Category.class)
            );
        }
//        result = selectStep.fetch().map(
//                new DefaultRecordMapper(Tables.EH_CATEGORIES.recordType(), Category.class)
//            );
        
        return result;
    }

    @Cacheable(value = "listDescendantCategory")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public List<Category> listDescendantCategories(Long ancestorId, CategoryAdminStatus status,
            Tuple<String, SortOrder>... orderBy) {
        List<Category> result = new ArrayList<>();
        
        String pathLike = "%";
        if(ancestorId != null) {
            Category parentRegion = this.findCategoryById(ancestorId);
            if(parentRegion == null) {
                LOGGER.error("Could not find parent region " + ancestorId);
                return result;
            }
            
            if(parentRegion.getPath() == null || parentRegion.getPath().isEmpty()) {
                LOGGER.error("Parent category " + ancestorId + " does not have valid path info" );
                return result;
            }
            
            pathLike = parentRegion.getPath() + "/%"; 
        }
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        //暂不向客户端开放排序字段指定 20150519
        //SortField[] orderByFields = JooqHelper.toJooqFields(Tables.EH_CATEGORIES, orderBy);
        
        SelectJoinStep<Record> selectStep = context.select().from(Tables.EH_CATEGORIES);
        Condition condition = Tables.EH_CATEGORIES.PATH.like(pathLike);
        
        if(ancestorId != null)
            condition = condition.and(Tables.EH_CATEGORIES.PARENT_ID.eq(ancestorId.longValue()));
        else
            condition = condition.and(Tables.EH_CATEGORIES.PARENT_ID.isNull());
            
        if(status != null)
            condition = condition.and(Tables.EH_CATEGORIES.STATUS.eq(status.getCode()));
        
        if(condition != null) {
            selectStep.where(condition);
        }
        
//        if(orderByFields != null) {
//            result = selectStep.orderBy(orderByFields).fetch().map(
//                new DefaultRecordMapper(Tables.EH_CATEGORIES.recordType(), Category.class)
//            );
//        } else {
//            result = selectStep.fetch().map(
//                new DefaultRecordMapper(Tables.EH_CATEGORIES.recordType(), Category.class)
//            );
//        }
        result = selectStep.fetch().map(
                new DefaultRecordMapper(Tables.EH_CATEGORIES.recordType(), Category.class)
            );
        
        return result;
    }

    @Override
    public List<Category> listRootCategories() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCategories.class));
        List<Category> categories=new ArrayList<Category>();
        context.select().from(Tables.EH_CATEGORIES).where(Tables.EH_CATEGORIES.PARENT_ID.isNull()).fetch().forEach(categroy->{
            categories.add(ConvertHelper.convert(categroy, Category.class));
        });;
        return categories;
    }
    
    @Cacheable(value = "listContentCategories")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public List<Category> listContentCategories() {
        List<Category> result = new ArrayList<>();
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        
        SelectJoinStep<Record> selectStep = context.select().from(Tables.EH_CATEGORIES);
        Condition condition = Tables.EH_CATEGORIES.PARENT_ID.eq(CategoryConstants.CATEGORY_ID_TOPIC);

        condition = condition.and(Tables.EH_CATEGORIES.STATUS.eq(CategoryAdminStatus.ACTIVE.getCode()));
        
        if(condition != null) {
            selectStep.where(condition);
        }
        
        result = selectStep.fetch().map(
                new DefaultRecordMapper(Tables.EH_CATEGORIES.recordType(), Category.class)
            );
        
        return result;
    }

    @Cacheable(value = "listActionCategories")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public List<Category> listActionCategories(Long parentId) {
        
        List<Category> result = new ArrayList<>();
        
        if(parentId == null) {
            LOGGER.error("Could not find parent region, parentId=" + parentId);
            return null;
        }
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        
        SelectJoinStep<Record> selectStep = context.select().from(Tables.EH_CATEGORIES);
        Condition condition = Tables.EH_CATEGORIES.PARENT_ID.eq(parentId);
           
        if(condition != null) {
            selectStep.where(condition);
        }
        
        result = selectStep.fetch().map(
                new DefaultRecordMapper(Tables.EH_CATEGORIES.recordType(), Category.class)
            );
        
        return result;
    }
}
