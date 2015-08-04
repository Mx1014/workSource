// @formatter:off
package com.everhomes.business;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhBusinessVisibleScopes;
import com.everhomes.server.schema.tables.daos.EhBusinessCategoriesDao;
import com.everhomes.server.schema.tables.daos.EhBusinessVisibleScopesDao;
import com.everhomes.server.schema.tables.daos.EhBusinessesDao;
import com.everhomes.server.schema.tables.pojos.EhBusinessCategories;
import com.everhomes.server.schema.tables.pojos.EhBusinesses;
import com.everhomes.server.schema.tables.records.EhBusinessesRecord;
import com.everhomes.util.ConvertHelper;


@Component
public class BusinessProviderImpl implements BusinessProvider {
	@Autowired
	private DbProvider dbProvider;
	@Autowired
	private SequenceProvider sequenceProvider;
    @Override
    public void createBusiness(Business business) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhBusinessesRecord record = ConvertHelper.convert(business, EhBusinessesRecord.class);
        InsertQuery<EhBusinessesRecord> query = context.insertQuery(Tables.EH_BUSINESSES);
        query.setRecord(record);
        query.setReturning(Tables.EH_BUSINESSES.ID);
        query.execute();
        business.setId(query.getReturnedRecord().getId());
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhBusinesses.class, null); 
        
    }
    @Override
    public void updateBusiness(Business business) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        EhBusinessesDao dao = new EhBusinessesDao(context.configuration()); 
        dao.update(business);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBusinesses.class, null);
        
    }
    @Override
    public void deleteBusiness(Business business) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        EhBusinessesDao dao = new EhBusinessesDao(context.configuration()); 
        dao.delete(business);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBusinesses.class, null);
    }
    
    @Override
    public void deleteBusiness(long id) {
        BusinessProvider self = PlatformContext.getComponent(BusinessProvider.class);
        Business business = self.findBusinessById(id);
        if(business != null)
            self.deleteBusiness(business);
        
    }
    
    @Override
    public Business findBusinessById(long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        EhBusinessesDao dao = new EhBusinessesDao(context.configuration()); 
        return ConvertHelper.convert(dao.findById(id),Business.class);
    }

    @Override
    public List<Business> findBusinessByIds(List<Long> ids) {
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhBusinesses.class));
        List<Business> businesses = context.select().from(Tables.EH_BUSINESSES)
        .where(Tables.EH_BUSINESSES.ID.in(ids))
        .fetch().map(r ->{
            return ConvertHelper.convert(r, Business.class);
        });
        
        return businesses;
    }
    
    @Override
    public List<BusinessCategory> findBusinessCategoriesByCategory(long category, int offset, int pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhBusinesses.class));
        List<BusinessCategory> businesses = new ArrayList<>();
        context.select().from(Tables.EH_BUSINESS_CATEGORIES)
                .where(Tables.EH_BUSINESS_CATEGORIES.CATEGORY_ID.eq(category))
                .limit(pageSize).offset(offset)
                .fetch().map(r ->{
                    businesses.add(ConvertHelper.convert(r, BusinessCategory.class));
                    return null;
                });
        return businesses;
    }
    @Override
    public List<BusinessVisibleScope> findBusinessVisibleScopesByScope(long scopeId, byte scopeType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhBusinessVisibleScopes.class));
        List<BusinessVisibleScope> businessVisibleScopes = context.select().from(Tables.EH_BUSINESS_VISIBLE_SCOPES)
        .where(Tables.EH_BUSINESS_VISIBLE_SCOPES.SCOPE_CODE.eq(scopeType))
        .and(Tables.EH_BUSINESS_VISIBLE_SCOPES.SCOPE_ID.eq(scopeId))
        .fetch().map(r ->{
            return ConvertHelper.convert(r, BusinessVisibleScope.class);
        });
        return businessVisibleScopes;
    }
    
    @Override
    public void createBusinessVisibleScope(BusinessVisibleScope businessVisibleScope) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhBusinessVisibleScopes.class));
        businessVisibleScope.setId(id);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        EhBusinessVisibleScopesDao dao = new EhBusinessVisibleScopesDao(context.configuration()); 
        dao.insert(businessVisibleScope); 
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhBusinessVisibleScopes.class, null); 
    }
    
    @Override
    public void updateBusinessVisibleScope(BusinessVisibleScope businessVisibleScope) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        EhBusinessVisibleScopesDao dao = new EhBusinessVisibleScopesDao(context.configuration()); 
        dao.update(businessVisibleScope);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBusinessVisibleScopes.class, null);
        
    }
    
    @Override
    public void deleteBusinessVisibleScope(BusinessVisibleScope businessVisibleScope) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        EhBusinessVisibleScopesDao dao = new EhBusinessVisibleScopesDao(context.configuration()); 
        dao.delete(businessVisibleScope);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBusinessVisibleScopes.class, null);
        
    }
    
    @Override
    public void deleteBusinessVisibleScope(long id) {
        BusinessProvider self = PlatformContext.getComponent(BusinessProvider.class);
        BusinessVisibleScope businessVisibleScope = self.findBusinessVisibleScopeById(id);
        if(businessVisibleScope != null)
            self.deleteBusinessVisibleScope(businessVisibleScope);
        
    }
    
    @Override
    public BusinessVisibleScope findBusinessVisibleScopeById(long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        EhBusinessVisibleScopesDao dao = new EhBusinessVisibleScopesDao(context.configuration()); 
        return ConvertHelper.convert(dao.findById(id),BusinessVisibleScope.class);
    }
    
    @Override
    public void createBusinessCategory(BusinessCategory businessCategory) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhBusinessCategories.class));
        businessCategory.setId(id);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        EhBusinessCategoriesDao dao = new EhBusinessCategoriesDao(context.configuration()); 
        dao.insert(businessCategory); 
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhBusinessCategories.class, null); 
        
    }
    
    @Override
    public void updateBusinessCategory(BusinessCategory businessCategory) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        EhBusinessCategoriesDao dao = new EhBusinessCategoriesDao(context.configuration()); 
        dao.update(businessCategory);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBusinessCategories.class, null);
        
    }
    
    @Override
    public void deleteBusinessCategory(BusinessCategory businessCategory) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        EhBusinessCategoriesDao dao = new EhBusinessCategoriesDao(context.configuration()); 
        dao.delete(businessCategory);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBusinessVisibleScopes.class, null);
        
    }
    
    @Override
    public void deleteBusinessCategory(long id) {
        BusinessProvider self = PlatformContext.getComponent(BusinessProvider.class);
        BusinessCategory businessCategory = self.findBusinessCategoryById(id);
        if(businessCategory != null)
            self.deleteBusinessCategory(businessCategory);
        
    }
    
    @Override
    public BusinessCategory findBusinessCategoryById(long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        EhBusinessCategoriesDao dao = new EhBusinessCategoriesDao(context.configuration()); 
        return ConvertHelper.convert(dao.findById(id),BusinessCategory.class);
    }
    
    @Override
    public void deleteBusinessVisibleScopeByBusinessId(long businessId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        context.delete(Tables.EH_BUSINESS_VISIBLE_SCOPES).where(Tables.EH_BUSINESS_VISIBLE_SCOPES.OWNER_ID.eq(businessId)).execute();

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBusinessVisibleScopes.class, null);
    }
    
    @Override
    public void deleteBusinessCategoryByBusinessId(long businessId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        context.delete(Tables.EH_BUSINESS_CATEGORIES).where(Tables.EH_BUSINESS_CATEGORIES.OWNER_ID.eq(businessId)).execute();
      
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBusinessCategories.class, null);
        
    }


}
