// @formatter:off
package com.everhomes.category;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.jooq.SelectQuery;
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
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.sequence.SequenceProvider;
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

    @Autowired
    private SequenceProvider sequenceProvider;
    
    @Caching(evict = { @CacheEvict(value="listChildCategory", allEntries=true),
            @CacheEvict(value="listDescendantCategory", allEntries=true),
            @CacheEvict(value="listAllCategory", allEntries=true),
            @CacheEvict(value="listBusinessSubCategories", allEntries=true)})
    @Override
    public void createCategory(Category category) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        
        long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhCategories.class));
        EhCategoriesDao dao = new EhCategoriesDao(context.configuration());
        category.setId(id);
//        EhCategoriesRecord record = ConvertHelper.convert(category, EhCategoriesRecord.class);
//        InsertQuery<EhCategoriesRecord> query = context.insertQuery(Tables.EH_CATEGORIES);
//        query.setRecord(record);
//        query.setReturning(Tables.EH_CATEGORIES.ID);
//        query.execute();
//        
//        category.setId(query.getReturnedRecord().getId());
        dao.insert(category);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhCategories.class, null);
    }

    @Caching(evict = { /*@CacheEvict(value="Category", key="#category.id"),*/
            @CacheEvict(value="listChildCategory", allEntries=true),
            @CacheEvict(value="listDescendantCategory", allEntries=true),
            @CacheEvict(value="listAllCategory", allEntries=true),
            @CacheEvict(value="listBusinessSubCategories", allEntries=true)})
    @Override
    public void updateCategory(Category category) {
        assert(category.getId() != null);
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhCategoriesDao dao = new EhCategoriesDao(context.configuration());
        dao.update(ConvertHelper.convert(category, EhCategories.class));
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCategories.class, category.getId());
    }

    @Caching(evict = { /*@CacheEvict(value="Category", key="#category.id"),*/
            @CacheEvict(value="listChildCategory",allEntries=true),
            @CacheEvict(value="listDescendantCategory", allEntries=true),
            @CacheEvict(value="listAllCategory", allEntries=true),
            @CacheEvict(value="listBusinessSubCategories", allEntries=true)})
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

//    @Cacheable(value="Category", key="#id", unless="#result == null")
    @Override
    public Category findCategoryById(long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhCategoriesDao dao = new EhCategoriesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), Category.class);
    }
    
    @Cacheable(value = "listAllCategory", unless="#result.size() == 0")
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

    @Cacheable(value = "listChildCategory" , unless="#result.size() == 0")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public List<Category> listChildCategories(Integer namespaceId, Long parentId, CategoryAdminStatus status,
            Tuple<String, SortOrder>... orderBy) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        //暂不向客户端开放排序字段指定 20150519
        SortField[] orderByFields = null;
        if(orderBy != null && orderBy.length != 0)
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

        condition = condition.and(Tables.EH_CATEGORIES.NAMESPACE_ID.eq(namespaceId));
//        if(parentId != null){
//            Category parentCategory = this.findCategoryById(parentId);
//            if(parentCategory == null){
//                LOGGER.error("Parent category is not found.parentId={}",parentId);
//                return null;
//            }
//            
//            condition = condition.or(Tables.EH_CATEGORIES.PATH.like(parentCategory.getName() + "/%"));
//        }
        if(condition != null) {
            selectStep.where(condition);
        }
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query child categories, namespaceId=" + namespaceId + ", parentId=" + parentId + ", status=" + status);
            LOGGER.debug("Query child categories, sql=" + selectStep.getSQL());
            LOGGER.debug("Query child categories, bindValues=" + selectStep.getBindValues());
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

    @Cacheable(value = "listDescendantCategory" , unless="#result.size() == 0")
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
    
    @Cacheable(value = "listContentCategories", unless="#result.size() == 0")
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

    @Cacheable(value = "listActionCategories", unless="#result.size() == 0")
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

    @Cacheable(value = "listBusinessSubCategories", unless="#result.size() == 0")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public List<Category> listBusinessSubCategories(long categoryId,
            CategoryAdminStatus status, Tuple<String, SortOrder>... orderBy) {
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        SortField[] orderByFields = null;
        if(orderBy != null && orderBy.length != 0)
                orderByFields = JooqHelper.toJooqFields(Tables.EH_CATEGORIES, orderBy);
        List<Category> result;
        
        List<Long> categoryIds = getBusinessSubCategories(categoryId);
        if(categoryIds == null || categoryIds.isEmpty())
            return null;
        
        SelectJoinStep<Record> selectStep = context.select().from(Tables.EH_CATEGORIES);
        Condition condition = Tables.EH_CATEGORIES.PARENT_ID.in(categoryIds);

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
        
        return result;
    }

    @Override
    public List<Long> getBusinessSubCategories(long categoryId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        SelectJoinStep<Record> selectStep = context.select().from(Tables.EH_CATEGORIES);
        
        Condition condition = Tables.EH_CATEGORIES.STATUS.eq(CategoryAdminStatus.ACTIVE.getCode());
        if(categoryId != 0)
            condition = condition.and(Tables.EH_CATEGORIES.PARENT_ID.eq(categoryId));
        
        List<Long> categoryIds = selectStep.where(condition).fetch().map(r -> r.getValue(Tables.EH_CATEGORIES.ID));
        return categoryIds;
    }
    
//    @Cacheable(value = "listTaskCategories", unless="#result.size() == 0")
	@Override
	public List<Category> listTaskCategories(Integer namespaceId, Long parentId, String keyword, 
			Long pageAnchor, Integer pageSize){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCategories.class));
        SelectQuery<EhCategoriesRecord> query = context.selectQuery(Tables.EH_CATEGORIES);
        if(null != namespaceId) 
        	query.addConditions(Tables.EH_CATEGORIES.NAMESPACE_ID.eq(namespaceId));
        if(null != parentId)
        	query.addConditions(Tables.EH_CATEGORIES.PARENT_ID.eq(parentId));
        if(StringUtils.isNotBlank(keyword))
        	query.addConditions(Tables.EH_CATEGORIES.PATH.like("%" + keyword + "%"));
        if(null != pageAnchor && pageAnchor != 0)
        	query.addConditions(Tables.EH_CATEGORIES.ID.gt(pageAnchor));
        query.addConditions(Tables.EH_CATEGORIES.STATUS.eq(CategoryAdminStatus.ACTIVE.getCode()));
        query.addOrderBy(Tables.EH_CATEGORIES.ID.asc());
        if(null != pageSize)
        	query.addLimit(pageSize);
        List<Category> result = query.fetch().stream().map(r -> ConvertHelper.convert(r, Category.class))
        		.collect(Collectors.toList());
        
        return result;
	}
    
	@Override
	public Category findCategoryByNamespaceAndName(Long parentId, Integer namespaceId, String categoryName) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCategories.class));
		
		Record record = context.select()
				.from(Tables.EH_CATEGORIES)
				.where(Tables.EH_CATEGORIES.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_CATEGORIES.NAME.eq(categoryName))
				.and(Tables.EH_CATEGORIES.PARENT_ID.eq(parentId))
				.and(Tables.EH_CATEGORIES.STATUS.eq(CategoryAdminStatus.ACTIVE.getCode()))
				.fetchAny();
		
		if (record != null) {
			return ConvertHelper.convert(record, Category.class);
		}
		
		return null;
	}
    
    
}
