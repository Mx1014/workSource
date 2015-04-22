package com.everhomes.pkg;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.jooq.SortField;
import org.jooq.impl.DefaultRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.jooq.JooqHelper;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhClientPackagesDao;
import com.everhomes.server.schema.tables.pojos.EhClientPackages;
import com.everhomes.server.schema.tables.records.EhClientPackagesRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;

@Component
public class ClientPackageProviderImpl implements ClientPackageProvider {
    
    @Autowired
    private DbProvider dbProvider;

    @Cacheable(value = "listClientPackages")
    @SuppressWarnings({"unchecked", "rawtypes" })
    @Override
    public List<ClientPackage> listClientPackages(Tuple<String, SortOrder>... orderBy) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        SortField[] orderByFields = JooqHelper.toJooqFields(Tables.EH_CLIENT_PACKAGES, orderBy);
        List<ClientPackage> result;
        
        SelectJoinStep<Record> selectStep = context.select().from(Tables.EH_CLIENT_PACKAGES);
        
        if(orderByFields != null) {
            result = selectStep.orderBy(orderByFields).fetch().map(
                new DefaultRecordMapper(Tables.EH_CLIENT_PACKAGES.recordType(), ClientPackage.class)
            );
        } else {
            result = selectStep.fetch().map(
                new DefaultRecordMapper(Tables.EH_CLIENT_PACKAGES.recordType(), ClientPackage.class)
            );
        }
        
        return result;
    }
    
    @Cacheable(value="ClientPackage", key="#pkgId")
    @Override
    public ClientPackage findClientPackageById(long pkgId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhClientPackagesDao dao = new EhClientPackagesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(pkgId), ClientPackage.class);
    }
    
    @Caching(evict = { @CacheEvict(value="listClientPackages"),
            @CacheEvict(value="findRegionById") })
    @Override
    public void createPackage(ClientPackage pkg) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        
        EhClientPackagesRecord record = ConvertHelper.convert(pkg, EhClientPackagesRecord.class);
        InsertQuery<EhClientPackagesRecord> query = context.insertQuery(Tables.EH_CLIENT_PACKAGES);
        query.setRecord(record);
        query.setReturning(Tables.EH_CLIENT_PACKAGES.ID);
        query.execute();
        
        pkg.setId(query.getReturnedRecord().getId());
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhClientPackages.class, null);
    }

    @Caching(evict = { @CacheEvict(value="ClientPackage", key="pkg.id"),
            @CacheEvict(value="listClientPackages")})
    @Override
    public void updatePackage(ClientPackage pkg) {
    	 assert(pkg.getId() != null);
         
         DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
         EhClientPackagesDao dao = new EhClientPackagesDao(context.configuration());
         dao.update(ConvertHelper.convert(pkg, EhClientPackages.class));
         
         DaoHelper.publishDaoAction(DaoAction.MODIFY, EhClientPackages.class, pkg.getId());
    }

    @Caching(evict = { @CacheEvict(value="ClientPackage", key="pkg.id"),
            @CacheEvict(value="listClientPackages")})
    @Override
    public void deletePackage(ClientPackage pkg) {
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhClientPackagesDao dao = new EhClientPackagesDao(context.configuration());
        
        dao.deleteById(pkg.getId());
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhClientPackages.class, pkg.getId());
    }

    @Override
    public void deletePackageById(long id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<ClientPackageFile> listPackageFiles(long packageId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void createPackageFile(ClientPackageFile pkgFile) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updatePackageFile(ClientPackageFile pkgFile) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deletePackageFile(ClientPackageFile pkgFile) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deletePackageFileById(long id) {
        // TODO Auto-generated method stub
        
    }
}
