package com.everhomes.locale;

import com.everhomes.cache.CacheAccessor;
import com.everhomes.cache.CacheProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhLocaleStringsRecord;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class LocaleStringProviderImpl implements LocaleStringProvider {
    
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
        EhLocaleStringsRecord record = (EhLocaleStringsRecord)context.select().from(Tables.EH_LOCALE_STRINGS)
            .where(Tables.EH_LOCALE_STRINGS.SCOPE.like(scope))
            .and(Tables.EH_LOCALE_STRINGS.CODE.eq(code))
            .and(Tables.EH_LOCALE_STRINGS.LOCALE.like(locale))
            .fetchOne();
            
        return ConvertHelper.convert(record, LocaleString.class);
    }

    @Cacheable(value="LocaleStringFind", key="{#scope, #text, #locale}")
    @Override
    public LocaleString findByText(String scope, String text, String locale) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhLocaleStringsRecord record = (EhLocaleStringsRecord)context.select().from(Tables.EH_LOCALE_STRINGS)
                .where(Tables.EH_LOCALE_STRINGS.SCOPE.like(scope))
                .and(Tables.EH_LOCALE_STRINGS.TEXT.eq(text))
                .and(Tables.EH_LOCALE_STRINGS.LOCALE.like(locale))
                .fetchOne();

        return ConvertHelper.convert(record, LocaleString.class);
    }
}
