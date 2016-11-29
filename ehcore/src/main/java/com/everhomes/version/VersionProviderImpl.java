package com.everhomes.version;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectConditionStep;
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
import com.everhomes.rest.version.VersionDTO;
import com.everhomes.rest.version.VersionInfoDTO;
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
import com.everhomes.util.RecordHelper;
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
        @CacheEvict(value="VersionUpgrade-Match", allEntries=true),
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
        @CacheEvict(value="VersionUpgrade-Match", allEntries=true),
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
        @CacheEvict(value="VersionUpgrade-Match", allEntries=true),
        @CacheEvict(value="VersionUpgrade-List", key="#rule.realmId")
    })
    @Override
    public void deleteVersionUpgradeRule(VersionUpgradeRule rule) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        EhVersionUpgradeRulesDao dao = new EhVersionUpgradeRulesDao(context.configuration());
        dao.delete(rule);
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
//            .orderBy(Tables.EH_VERSION_UPGRADE_RULES.ORDER.asc())
            //取最新的版本, update by tt, 20161122
            .orderBy(Tables.EH_VERSION_UPGRADE_RULES.ID.desc())
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
//            .orderBy(Tables.EH_VERSION_UPGRADE_RULES.ORDER.asc())
            //取最新的版本, update by tt, 20161122
            .orderBy(Tables.EH_VERSION_UPGRADE_RULES.ID.desc())
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
        @CacheEvict(value="VersionedContent-Match", allEntries=true),
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
        @CacheEvict(value="VersionedContent-Match", allEntries=true),
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
//            .orderBy(Tables.EH_VERSIONED_CONTENT.ORDER.asc())
            //取最新的版本, update by tt, 20161122
            .orderBy(Tables.EH_VERSIONED_CONTENT.ID.desc())
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
//            .orderBy(Tables.EH_VERSIONED_CONTENT.ORDER.asc())
            //取最新的版本, update by tt, 20161122
            .orderBy(Tables.EH_VERSIONED_CONTENT.ID.desc())
            .fetch().map((record)-> {
                return ConvertHelper.convert(record, VersionedContent.class);
            });
        
        return contentPojos;
    }
    
    @Caching(evict = {
            @CacheEvict(value="VersionUrl-Version", allEntries=true)
        })
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
        @CacheEvict(value="VersionUrl-Version", allEntries=true)
    })
    @Override
    public void updateVersionUrl(VersionUrl versionUrl) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        
        EhVersionUrlsDao dao = new EhVersionUrlsDao(context.configuration());
        dao.update(versionUrl);
    }
    
    @Caching(evict = {
        @CacheEvict(value="VersionUrl-Id", key="#versionUrl.id"),
        @CacheEvict(value="VersionUrl-Version", allEntries=true)
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

	@Override
	public List<VersionInfoDTO> listVersionInfo(Long pageAnchor, int pageSize) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		com.everhomes.server.schema.tables.EhVersionRealm t1 = Tables.EH_VERSION_REALM.as("t1");
		com.everhomes.server.schema.tables.EhVersionUpgradeRules t2 = Tables.EH_VERSION_UPGRADE_RULES.as("t2");
		com.everhomes.server.schema.tables.EhVersionUrls t3 = Tables.EH_VERSION_URLS.as("t3");
		SelectConditionStep<?> step = context.select(t2.ID, t2.REALM_ID, t1.REALM, t1.DESCRIPTION, t2.MATCHING_LOWER_BOUND, t2.MATCHING_UPPER_BOUND, t2.TARGET_VERSION, t2.FORCE_UPGRADE, t3.ID.as("url_id"), t3.APP_NAME, t3.PUBLISH_TIME, t3.DOWNLOAD_URL, t3.UPGRADE_DESCRIPTION, t3.ICON_URL)
											.from(t2)
											.leftOuterJoin(t1).on(t2.REALM_ID.eq(t1.ID))
											.leftOuterJoin(t3).on(t2.REALM_ID.eq(t3.REALM_ID)).and(t2.TARGET_VERSION.eq(t3.TARGET_VERSION))
											.where("1=1");
		if (pageAnchor != null) {
			step = step.and(t2.ID.lt(pageAnchor));
		}
		
		Result<?> result = step.orderBy(t2.ID.desc())
								.limit(pageSize)
								.fetch();
		
		if (result != null && result.size() > 0) {
			return result.map(r->{
				VersionInfoDTO versionInfoDTO = RecordHelper.convert(r, VersionInfoDTO.class);
				versionInfoDTO.setMinVersion(Version.fromEncodedValue(Double.valueOf(r.getValue("matching_lower_bound", Double.class)+0.2).longValue()).toString());
				versionInfoDTO.setMaxVersion(Version.fromEncodedValue(Double.valueOf(r.getValue("matching_upper_bound", Double.class)+0.2).longValue()).toString());
				return versionInfoDTO;
			});
		}
			
		return new ArrayList<VersionInfoDTO>();
	}

	@Override
	public String findAppNameByRealm(Long realmId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Record record = context.select()
			.from(Tables.EH_VERSION_URLS)
			.where(Tables.EH_VERSION_URLS.REALM_ID.eq(realmId))
			.and(Tables.EH_VERSION_URLS.APP_NAME.isNotNull())
			.orderBy(Tables.EH_VERSION_URLS.ID.desc())
			.limit(1)
			.fetchOne();
			
		if (record != null) {
			VersionUrl versionUrl = ConvertHelper.convert(record, VersionUrl.class);
			return versionUrl.getAppName();
		}
		
		return "";
	}

	@Override
	public String findIconUrlByRealm(Long realmId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Record record = context.select()
			.from(Tables.EH_VERSION_URLS)
			.where(Tables.EH_VERSION_URLS.REALM_ID.eq(realmId))
			.and(Tables.EH_VERSION_URLS.ICON_URL.isNotNull())
			.orderBy(Tables.EH_VERSION_URLS.ID.desc())
			.limit(1)
			.fetchOne();
			
		if (record != null) {
			VersionUrl versionUrl = ConvertHelper.convert(record, VersionUrl.class);
			return versionUrl.getIconUrl();
		}
		
		return "";
	}

	@Override
	public String findDownloadUrlByRealm(Long realmId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Record record = context.select()
			.from(Tables.EH_VERSION_URLS)
			.where(Tables.EH_VERSION_URLS.REALM_ID.eq(realmId))
			.and(Tables.EH_VERSION_URLS.DOWNLOAD_URL.isNotNull())
			.orderBy(Tables.EH_VERSION_URLS.ID.desc())
			.limit(1)
			.fetchOne();
			
		if (record != null) {
			VersionUrl versionUrl = ConvertHelper.convert(record, VersionUrl.class);
			return versionUrl.getDownloadUrl();
		}
		
		return "";
	}
	
}
