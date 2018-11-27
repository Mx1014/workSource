package com.everhomes.locale;

import com.everhomes.cache.CacheAccessor;
import com.everhomes.cache.CacheProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.rest.locale.LocaleTemplateDTO;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhLocaleTemplates;
import com.everhomes.server.schema.tables.daos.EhLocaleTemplatesDao;
import com.everhomes.server.schema.tables.records.EhLocaleStringsRecord;
import com.everhomes.util.ConvertHelper;
import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectConditionStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LocaleStringProviderImpl implements LocaleStringProvider, ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocaleStringProviderImpl.class);
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private CacheProvider cacheProvice;
    
    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180516   
    //@PostConstruct
    public void setup() {
        CacheAccessor accessor = this.cacheProvice.getCacheAccessor("LocaleStringFind");
        accessor.clear();
    }
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            setup();
        }
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

	@Override
	public List<LocaleTemplateDTO> listLocaleTemplate(int from, int pageSize, Integer namespaceId, String scope,
			Integer code, String keyword) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		
		EhLocaleTemplates t1 = Tables.EH_LOCALE_TEMPLATES.as("t1");
		
		SelectConditionStep<Record> step = context.select()
												.from(t1)
												.where("1=1");
		
		if (namespaceId != null) {
			step = step.and(t1.NAMESPACE_ID.eq(namespaceId));
		}
		
		if (scope != null) {
			step = step.and(t1.SCOPE.eq(scope));
		}
		
		if (code != null) {
			step = step.and(t1.CODE.eq(code));
		}
		
		if (StringUtils.isNotBlank(keyword)) {
			step = step.and(t1.TEXT.like("%"+keyword+"%").or(t1.DESCRIPTION.like("%"+keyword+"%")));
		}
		
		Result<Record> result = step.orderBy(t1.NAMESPACE_ID.asc())
									.limit(from, pageSize)
									.fetch();
		
		if (result != null && result.size() > 0) {
			return result.map(r->ConvertHelper.convert(r, LocaleTemplateDTO.class));
		}
		
		return new ArrayList<LocaleTemplateDTO>();
	}

	@Override
	public LocaleTemplate findTemplateById(Long id){
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		EhLocaleTemplatesDao dao = new EhLocaleTemplatesDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), LocaleTemplate.class);
	}

	@Override
	public void updateLocaleTemplate(LocaleTemplate template) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhLocaleTemplatesDao dao = new EhLocaleTemplatesDao(context.configuration());
		dao.update(template);
		
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhLocaleTemplates.class, template.getId());
	}
}
