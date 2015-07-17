// @formatter:off
package com.everhomes.business;

import java.util.ArrayList;
import java.util.List;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhBusinessVisibleScopes;
import com.everhomes.server.schema.tables.daos.EhBusinessesDao;
import com.everhomes.server.schema.tables.pojos.EhBusinesses;
import com.everhomes.util.ConvertHelper;


@Component
public class BusinessProviderImpl implements BusinessProvider {
	@Autowired
	private DbProvider dbProvider;

    @Override
    public void createBusiness(Business business) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        EhBusinessesDao dao = new EhBusinessesDao(context.configuration()); 
        dao.insert(business); 
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
    public List<BusinessCategory> findBusinessCategoriesByCategory(long category) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhBusinesses.class));
        List<BusinessCategory> businesses = new ArrayList<>();
        context.select().from(Tables.EH_BUSINESS_CATEGORIES)
                .where(Tables.EH_BUSINESS_CATEGORIES.CATEGORY_ID.eq(category))
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
    


}
