package com.everhomes.locale;

import javax.annotation.PostConstruct;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.everhomes.cache.CacheAccessor;
import com.everhomes.cache.CacheProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhLocaleStringsRecord;
import com.everhomes.util.ConvertHelper;

@Component
public class LocaleStringProviderImpl implements LocaleStringProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocaleStringProviderImpl.class);
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private CacheProvider cacheProvice;
    
    @PostConstruct
    public void setup() {
        CacheAccessor accessor = this.cacheProvice.getCacheAccessor("LocaleStringFind");
        accessor.clear();
    }
    
    @Cacheable(value="LocaleStringFind", key="{#scope, #code, #locale}")
    @Override
    public LocaleString find(String scope, String code, String locale) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        
//        EhLocaleStringsRecord record = (EhLocaleStringsRecord)context.select().from(Tables.EH_LOCALE_STRINGS)
//            .where(Tables.EH_LOCALE_STRINGS.SCOPE.like(scope))
//            .and(Tables.EH_LOCALE_STRINGS.CODE.eq(code))
//            .and(Tables.EH_LOCALE_STRINGS.LOCALE.like(locale))
//            .fetchOne();
        
        SelectConditionStep<Record> query = context.select().from(Tables.EH_LOCALE_STRINGS)
            .where(Tables.EH_LOCALE_STRINGS.SCOPE.like(scope))
            .and(Tables.EH_LOCALE_STRINGS.CODE.eq(code))
            .and(Tables.EH_LOCALE_STRINGS.LOCALE.like(locale));
        
        EhLocaleStringsRecord record = (EhLocaleStringsRecord)query.fetchOne();
        
//        if(LOGGER.isDebugEnabled()) {
//            LOGGER.debug("Query locale string, sql=" + query.getSQL());
//            LOGGER.debug("Query locale string, bindValues=" + query.getBindValues());
//        }
            
        return ConvertHelper.convert(record, LocaleString.class);
    }
}
