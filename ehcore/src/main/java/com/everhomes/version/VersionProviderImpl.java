package com.everhomes.version;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhVersionRealmDao;
import com.everhomes.server.schema.tables.daos.EhVersionUpgradeRulesDao;
import com.everhomes.server.schema.tables.daos.EhVersionUrlsDao;
import com.everhomes.server.schema.tables.daos.EhVersionedContentDao;
import com.everhomes.server.schema.tables.pojos.EhVersionRealm;
import com.everhomes.server.schema.tables.pojos.EhVersionUpgradeRules;
import com.everhomes.server.schema.tables.pojos.EhVersionUrls;
import com.everhomes.server.schema.tables.pojos.EhVersionedContent;
import com.everhomes.server.schema.tables.records.EhUserLikesRecord;
import com.everhomes.server.schema.tables.records.EhVersionUrlsRecord;
import com.everhomes.user.UserLike;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.Version;
import com.everhomes.version.VersionProvider;
import com.everhomes.version.VersionRealm;
import com.everhomes.version.VersionUpgradeRule;
import com.everhomes.version.VersionedContent;

@Component
public class VersionProviderImpl implements VersionProvider {
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private SequenceProvider sequenceProvider;
    
    @Caching(evict = {
        @CacheEvict(value="VersionRealm-List")
    })
    @Override
    public void createVersionRealm(VersionRealm realm) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhVersionRealm.class));
        realm.setId(id);
        realm.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        EhVersionRealmDao dao = new EhVersionRealmDao(context.configuration());
        dao.insert(realm);
    }

    @Caching(evict = {
        @CacheEvict(value="VersionRealm-Id", key="#realm.id"),
        @CacheEvict(value="VersionRealm-Name", key="#realm.realm"),
        @CacheEvict(value="VersionRealm-List")
    })
    @Override
    public void updateVersionRealm(VersionRealm realm) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        
        EhVersionRealmDao dao = new EhVersionRealmDao(context.configuration());
        dao.update(realm);
    }

    @Caching(evict = {
        @CacheEvict(value="VersionRealm-Id", key="#realm.id"),
        @CacheEvict(value="VersionRealm-Name", key="#realm.realm"),
        @CacheEvict(value="VersionRealm-List"),
        @CacheEvict(value="VersionUpgrade-List", key="#realm.id"),
        @CacheEvict(value="VersionedContent-List", key="#realm.id")
    })
    @Override
    public void deleteVersionRealm(VersionRealm realm) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        
        EhVersionRealmDao dao = new EhVersionRealmDao(context.configuration());
        dao.delete(realm);
    }

    @Override
    public void deleteVersionRealmById(long id) {
        VersionProvider self = PlatformContext.getComponent(VersionProvider.class);
        
        VersionRealm realm = self.findVersionRealmById(id);
        if(realm != null)
            self.deleteVersionRealm(realm);
    }
    
    @Cacheable(value="VersionRealm-Id", key="#id", unless="#result == null")
    @Override
    public VersionRealm findVersionRealmById(long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<VersionRealm> realms = context.select().from(Tables.EH_VERSION_REALM)
            .where(Tables.EH_VERSION_REALM.ID.eq(id))
            .fetch().map((record)-> {
                return ConvertHelper.convert(record, VersionRealm.class);
            });
        if(realms.size() > 0)
            return realms.get(0);
        return null;
    }

    @Cacheable(value="VersionRealm-Name", key="{#namespaceId, #realmName}", unless="#result == null")
    @Override
    public VersionRealm findVersionRealmByName(String realmName) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<VersionRealm> realms = context.select().from(Tables.EH_VERSION_REALM)
            .where(Tables.EH_VERSION_REALM.REALM.eq(realmName))
            .fetch().map((record)-> {
                return ConvertHelper.convert(record, VersionRealm.class);
            });
        if(realms.size() > 0)
            return realms.get(0);
        return null;
    }

    @Cacheable(value="VersionRealm-List", unless="#result.size() == 0")
    @Override
    public List<VersionRealm> listVersionRealm() {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<VersionRealm> realms = context.select().from(Tables.EH_VERSION_REALM)
             .fetch().map((record)-> {
                return ConvertHelper.convert(record, VersionRealm.class);
            });
        
        return realms;
    }

    @Caching(evict = {
        @CacheEvict(value="VersionUpgrade-List", key="#rule.realmId")
    })
    @Override
    public void createVersionUpgradeRule(VersionUpgradeRule rule) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhVersionUpgradeRules.class));
        rule.setId(id);
        rule.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        EhVersionUpgradeRulesDao dao = new EhVersionUpgradeRulesDao(context.configuration());
        dao.insert(rule);
    }

    @Caching(evict = {
        @CacheEvict(value="VersionUpgrade-Id", key="#rule.id"),
        @CacheEvict(value="VersionUpgrade-Match"),
        @CacheEvict(value="VersionUpgrade-List", key="#rule.realmId")
    })
    @Override
    public void updateVersionUpgradeRule(VersionUpgradeRule rule) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        EhVersionUpgradeRulesDao dao = new EhVersionUpgradeRulesDao(context.configuration());
        dao.update(rule);
    }

    @Caching(evict = {
        @CacheEvict(value="VersionUpgrade-Id", key="#rule.id"),
        @CacheEvict(value="VersionUpgrade-Match"),
        @CacheEvict(value="VersionUpgrade-List", key="#rule.realmId")
    })
    @Override
    public void deleteVersionUpgradeRule(VersionUpgradeRule rule) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        EhVersionUpgradeRulesDao dao = new EhVersionUpgradeRulesDao(context.configuration());
        dao.update(rule);
    }

    @Override
    public void deleteVersionUpgradeRuleById(long id) {
        VersionProvider self = PlatformContext.getComponent(VersionProvider.class);
        
        VersionUpgradeRule rule = self.findVersionUpgradeRuleById(id);
        if(rule != null)
            self.deleteVersionUpgradeRule(rule);
    }

    @Cacheable(value="VersionUpgrade-Id", key="#id", unless="#result == null")
    @Override
    public VersionUpgradeRule findVersionUpgradeRuleById(long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<VersionUpgradeRule> rules = context.select().from(Tables.EH_VERSION_UPGRADE_RULES)
            .where(Tables.EH_VERSION_UPGRADE_RULES.ID.eq(id))
            .fetch().map((record)-> {
                return ConvertHelper.convert(record, VersionUpgradeRule.class);
            });
        if(rules.size() > 0)
            return rules.get(0);
        return null;
    }

    @Cacheable(value="VersionUpgrade-Match", key="{#realmId, #version}", unless="#result == null")
    @Override
    public VersionUpgradeRule matchVersionUpgradeRule(long realmId, Version version) {
        double encodedValue = (double)version.getEncodedValue();
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<VersionUpgradeRule> rules = context.select().from(Tables.EH_VERSION_UPGRADE_RULES)
            .where(Tables.EH_VERSION_UPGRADE_RULES.REALM_ID.eq(realmId))
            .and(Tables.EH_VERSION_UPGRADE_RULES.MATCHING_LOWER_BOUND.lessThan(encodedValue))
            .and(Tables.EH_VERSION_UPGRADE_RULES.MATCHING_UPPER_BOUND.greaterThan(encodedValue))
            .orderBy(Tables.EH_VERSION_UPGRADE_RULES.ORDER.asc())
            .fetch().map((record)-> {
                return ConvertHelper.convert(record, VersionUpgradeRule.class);
            });
 
        if(rules.size() > 0)
            return rules.get(0);
        return null;
    }

    @Cacheable(value="VersionUpgrade-List", key="#realmId", unless="#result.size() == 0")
    @Override
    public List<VersionUpgradeRule> listVersionUpgradeRules(long realmId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<VersionUpgradeRule> rules = context.select().from(Tables.EH_VERSION_UPGRADE_RULES)
            .where(Tables.EH_VERSION_UPGRADE_RULES.REALM_ID.eq(realmId))
            .orderBy(Tables.EH_VERSION_UPGRADE_RULES.ORDER.asc())
            .fetch().map((record)-> {
                return ConvertHelper.convert(record, VersionUpgradeRule.class);
            });
        
        return rules;
    }

    @Caching(evict = {
        @CacheEvict(value="VersionedContent-List", key="#content.realmId")
    })
    @Override
    public void createVersionedContent(VersionedContent content) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhVersionedContent.class));
        content.setId(id);
        content.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        EhVersionedContentDao dao = new EhVersionedContentDao(context.configuration());
        dao.insert(content);
    }

    @Caching(evict = {
        @CacheEvict(value="VersionedContent-Id", key="#content.id"),
        @CacheEvict(value="VersionedContent-Match"),
        @CacheEvict(value="VersionedContent-List", key="#content.realmId")
    })
    @Override
    public void updateVersionedContent(VersionedContent content) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        EhVersionedContentDao dao = new EhVersionedContentDao(context.configuration());
        dao.update(content);
    }

    @Caching(evict = {
        @CacheEvict(value="VersionedContent-Id", key="#content.id"),
        @CacheEvict(value="VersionedContent-Match"),
        @CacheEvict(value="VersionedContent-List", key="#content.realmId")
    })
    @Override
    public void deleteVersionedContent(VersionedContent content) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        EhVersionedContentDao dao = new EhVersionedContentDao(context.configuration());
        dao.delete(content);
    }

    @Override
    public void deleteVersionedContentById(long id) {
        VersionProvider self = PlatformContext.getComponent(VersionProvider.class);
        
        VersionedContent content = self.findVersionedContentById(id);
        if(content != null)
            self.deleteVersionedContent(content);
    }

    @Cacheable(value="VersionedContent-Id", key="#id", unless="#result == null")
    @Override
    public VersionedContent findVersionedContentById(long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<VersionedContent> contentPojos = context.select().from(Tables.EH_VERSIONED_CONTENT)
            .where(Tables.EH_VERSIONED_CONTENT.ID.eq(id))
            .fetch().map((record)-> {
                return ConvertHelper.convert(record, VersionedContent.class);
            });
        if(contentPojos.size() > 0)
            return contentPojos.get(0);
        return null;
    }

    @Cacheable(value="VersionedContent-Match", key="{#realmId, #version}", unless="#result == null")
    @Override
    public VersionedContent matchVersionedContent(long realmId, Version version) {
        double encodedValue = (double)version.getEncodedValue();
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<VersionedContent> contentPojos = context.select().from(Tables.EH_VERSIONED_CONTENT)
            .where(Tables.EH_VERSIONED_CONTENT.REALM_ID.eq(realmId))
            .and(Tables.EH_VERSIONED_CONTENT.MATCHING_LOWER_BOUND.lessThan(encodedValue))
            .and(Tables.EH_VERSIONED_CONTENT.MATCHING_UPPER_BOUND.greaterThan(encodedValue))
            .orderBy(Tables.EH_VERSIONED_CONTENT.ORDER.asc())
            .fetch().map((record)-> {
                return ConvertHelper.convert(record, VersionedContent.class);
            });
 
        if(contentPojos.size() > 0)
            return contentPojos.get(0);
        return null;
    }

    @Cacheable(value="VersionedContent-List", key="#realmId", unless="#result.size() == 0")
    @Override
    public List<VersionedContent> listVersionedContent(long realmId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<VersionedContent> contentPojos = context.select().from(Tables.EH_VERSIONED_CONTENT)
            .where(Tables.EH_VERSIONED_CONTENT.REALM_ID.eq(realmId))
            .orderBy(Tables.EH_VERSIONED_CONTENT.ORDER.asc())
            .fetch().map((record)-> {
                return ConvertHelper.convert(record, VersionedContent.class);
            });
        
        return contentPojos;
    }
    
    @Override
    public void createVersionUrl(VersionUrl versionUrl) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhVersionUrls.class));
        versionUrl.setId(id);

        EhVersionUrlsDao dao = new EhVersionUrlsDao(context.configuration());
        dao.insert(versionUrl);
    }
    
    @Caching(evict = {
        @CacheEvict(value="VersionUrl-Id", key="#versionUrl.id"),
        @CacheEvict(value="VersionUrl-Version")
    })
    @Override
    public void updateVersionUrl(VersionUrl versionUrl) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        
        EhVersionUrlsDao dao = new EhVersionUrlsDao(context.configuration());
        dao.update(versionUrl);
    }
    
    @Caching(evict = {
        @CacheEvict(value="VersionUrl-Id", key="#versionUrl.id"),
        @CacheEvict(value="VersionUrl-Version")
    })
    @Override
    public void deleteVersionUrl(VersionUrl versionUrl) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        
        EhVersionUrlsDao dao = new EhVersionUrlsDao(context.configuration());
        dao.delete(versionUrl);
    }
    
    @Override
    public void deleteVersionUrlById(long id) {
        VersionProvider self = PlatformContext.getComponent(VersionProvider.class);
        VersionUrl versionUrl = self.findVersionUrlById(id);
        if(versionUrl != null)
            self.deleteVersionUrl(versionUrl);
    }
    
    @Cacheable(value="VersionUrl-Id", key="#id", unless="#result == null")
    @Override
    public VersionUrl findVersionUrlById(long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<VersionUrl> versionUrls = context.select().from(Tables.EH_VERSION_URLS)
            .where(Tables.EH_VERSION_URLS.ID.eq(id))
            .fetch().map((record)-> {
                return ConvertHelper.convert(record, VersionUrl.class);
            });
        if(versionUrls.size() > 0)
            return versionUrls.get(0);
        return null;
    }
    
    @Cacheable(value="VersionUrl-Version", key="{#realmName, #targetVersion}", unless="#result == null")
    @Override
    public VersionUrl findVersionUrlByVersion(String realmName, String targetVersion) {
    	List<VersionUrl> versionUrls = new ArrayList<VersionUrl>();
        VersionProvider self = PlatformContext.getComponent(VersionProvider.class);
        VersionRealm realm = self.findVersionRealmByName(realmName);
        if(realm == null)
            return null;
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhVersionUrlsRecord> query = context.selectQuery(Tables.EH_VERSION_URLS);
        query.addConditions(Tables.EH_VERSION_URLS.REALM_ID.eq(realm.getId()));
        if(targetVersion != null)
            query.addConditions(Tables.EH_VERSION_URLS.TARGET_VERSION.eq(targetVersion));
        
        query.addOrderBy(Tables.EH_VERSION_URLS.TARGET_VERSION.desc());
        query.fetch().map((r)-> { 
        	versionUrls.add(ConvertHelper.convert(r, VersionUrl.class));
        	return null;
        });
        
        if(versionUrls.size() > 0)
            return versionUrls.get(0);
        
        return null;
    }
}
