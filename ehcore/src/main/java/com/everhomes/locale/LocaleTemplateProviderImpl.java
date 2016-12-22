package com.everhomes.locale;

import java.util.List;

import javax.annotation.PostConstruct;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import com.everhomes.cache.CacheAccessor;
import com.everhomes.cache.CacheProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhLocaleTemplatesDao;
import com.everhomes.server.schema.tables.pojos.EhGroups;
import com.everhomes.server.schema.tables.pojos.EhLocaleTemplates;
import com.everhomes.server.schema.tables.records.EhLocaleTemplatesRecord;
import com.everhomes.util.ConvertHelper;

@Component
public class LocaleTemplateProviderImpl implements LocaleTemplateProvider {
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private CacheProvider cacheProvice;
    
    @Autowired
    private SequenceProvider sequenceProvider;
    
    @PostConstruct
    public void setup() {
        CacheAccessor accessor = this.cacheProvice.getCacheAccessor("LocaleStringFind");
        accessor.clear();
    }
    
    @Cacheable(value="LocaleTemplateById", key="#id")
    @Override
    public LocaleTemplate findLocaleTemplateById(long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhLocaleTemplatesDao dao = new EhLocaleTemplatesDao(context.configuration());
        EhLocaleTemplates record = dao.findById(id);
            
        return ConvertHelper.convert(record, LocaleTemplate.class);
    }
    
    @Cacheable(value="LocaleTemplateByScope", key="{#namespaceId, #scope, #code, #locale}", unless="#result == null")
    @Override
    public LocaleTemplate findLocaleTemplateByScope(Integer namespaceId, String scope, int code, String locale) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhLocaleTemplatesRecord record = (EhLocaleTemplatesRecord)context.select().from(Tables.EH_LOCALE_TEMPLATES)
            .where(Tables.EH_LOCALE_TEMPLATES.SCOPE.like(scope))
            .and(Tables.EH_LOCALE_TEMPLATES.CODE.eq(code))
            .and(Tables.EH_LOCALE_TEMPLATES.LOCALE.like(locale))
            .and(Tables.EH_LOCALE_TEMPLATES.NAMESPACE_ID.eq(namespaceId))
            .fetchAny();
            
        return ConvertHelper.convert(record, LocaleTemplate.class);
    }
    
    @Caching(evict={ @CacheEvict(value="LocaleTemplateById", key="#template.id"),
        @CacheEvict(value="LocaleTemplateByScope", key="{#template.scope, #template.code, #template.locale}")})
    @Override
    public void updateLocaleTemplate(LocaleTemplate template) {
        assert(template.getId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class));
        EhLocaleTemplatesDao dao = new EhLocaleTemplatesDao(context.configuration());
        dao.update(template);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhLocaleTemplates.class, template.getId());
    }

    @Caching(evict={ @CacheEvict(value="LocaleTemplateById", key="#template.id"),
        @CacheEvict(value="LocaleTemplateByScope", key="{#template.scope, #template.code, #template.locale}")})
    @Override
    public void deleteLocaleTemplate(LocaleTemplate template) {
        assert(template.getId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class, template.getId()));
        EhLocaleTemplatesDao dao = new EhLocaleTemplatesDao(context.configuration());
        dao.delete(template);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhLocaleTemplates.class, template.getId());
    }
    
    @Override
    public void createLocaleTemplate(LocaleTemplate template){
    	Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhLocaleTemplates.class));
    	template.setId(id);
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhLocaleTemplatesDao dao = new EhLocaleTemplatesDao(context.configuration());
        dao.insert(template);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhLocaleTemplates.class, template.getId());
    }
    
    /**
     * added by Janson 20161227
     */
    @Override
    public List<LocaleTemplate> listLocaleTemplatesByScope(ListingLocator locator, Integer namespaceId, String scope, String locale, String keyword,  int count) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Condition cond = Tables.EH_LOCALE_TEMPLATES.SCOPE.like(scope)
        						.and(Tables.EH_LOCALE_TEMPLATES.LOCALE.like(locale))
        						.and(Tables.EH_LOCALE_TEMPLATES.NAMESPACE_ID.eq(namespaceId));
        
        if(locator.getAnchor() != null) {
        	cond = cond.and(Tables.EH_LOCALE_TEMPLATES.ID.lt(locator.getAnchor()));
        }
        
        if(keyword != null) {
        	cond = cond.and(Tables.EH_LOCALE_TEMPLATES.DESCRIPTION.like("%"+keyword+"%").or(Tables.EH_LOCALE_TEMPLATES.TEXT.like("%"+keyword+"%")));
        }
        
        List<LocaleTemplate> objs = context.select().from(Tables.EH_LOCALE_TEMPLATES)
            .where(cond).limit(count).fetch().map((r)->{
            	return ConvertHelper.convert(r, LocaleTemplate.class);
            });
        
        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }
        
        return objs;
    }
}
