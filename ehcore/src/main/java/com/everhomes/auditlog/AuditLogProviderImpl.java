package com.everhomes.auditlog;

import javax.annotation.PostConstruct;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.cache.CacheAccessor;
import com.everhomes.cache.CacheProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.server.schema.tables.daos.EhAuditLogsDao;
import com.everhomes.server.schema.tables.pojos.EhAuditLogs;

@Component
public class AuditLogProviderImpl implements AuditLogProvider {
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private CacheProvider cacheProvice;
    
//    @PostConstruct
//    public void setup() {
//        CacheAccessor accessor = this.cacheProvice.getCacheAccessor("LocaleStringFind");
//        accessor.clear();
//    }
    
    @Override
    public void createAuditLog(AuditLog auditLog) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        
        EhAuditLogsDao dao = new EhAuditLogsDao(context.configuration());
        dao.insert(auditLog);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhAuditLogs.class, null);
    }
}
