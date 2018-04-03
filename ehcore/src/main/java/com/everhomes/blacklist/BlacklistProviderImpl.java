// @formatter:off
package com.everhomes.blacklist;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.blacklist.UserBlacklistStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhUserBlacklistsDao;
import com.everhomes.server.schema.tables.pojos.EhUserBlacklists;
import com.everhomes.server.schema.tables.records.EhUserBlacklistsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class BlacklistProviderImpl implements BlacklistProvider {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BlacklistProviderImpl.class);

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public List<UserBlacklist> listUserBlacklists(Integer namespaceId, String scopeType, Long scopeId, Timestamp startTime, Timestamp endTime, String keywords) {
		List<UserBlacklist> results = new ArrayList<>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhUserBlacklistsRecord> query = context.selectQuery(Tables.EH_USER_BLACKLISTS);
		query.addConditions(Tables.EH_USER_BLACKLISTS.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_USER_BLACKLISTS.STATUS.eq(UserBlacklistStatus.ACTIVE.getCode()));
		if(!StringUtils.isEmpty(scopeType)){
			query.addConditions(Tables.EH_USER_BLACKLISTS.SCOPE_TYPE.eq(scopeType));
		}

		if(null != scopeId){
			query.addConditions(Tables.EH_USER_BLACKLISTS.SCOPE_ID.eq(scopeId));
		}

		if(null != startTime){
			query.addConditions(Tables.EH_USER_BLACKLISTS.CREATE_TIME.ge(startTime));
		}

		if(null != endTime){
			query.addConditions(Tables.EH_USER_BLACKLISTS.CREATE_TIME.le(endTime));
		}

		if(!StringUtils.isEmpty(keywords)){
			query.addConditions(Tables.EH_USER_BLACKLISTS.CONTACT_NAME.like(keywords + "%").or(Tables.EH_USER_BLACKLISTS.CONTACT_TOKEN.eq(keywords)));
		}

		query.fetch().map((r) -> {
			results.add(ConvertHelper.convert(r, UserBlacklist.class));
			return null;
		});
		return results;
	}

	@Override
	public List<UserBlacklist> listUserBlacklists(Integer namespaceId, ListingLocator locator, Integer pageSize,ListingQueryBuilderCallback queryBuilderCallback) {
		List<UserBlacklist> results = new ArrayList<>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhUserBlacklistsRecord> query = context.selectQuery(Tables.EH_USER_BLACKLISTS);
		query.addConditions(Tables.EH_USER_BLACKLISTS.NAMESPACE_ID.eq(namespaceId));

		if(null != locator.getAnchor()){
			query.addConditions(Tables.EH_USER_BLACKLISTS.ID.lt(locator.getAnchor()));
		}
		queryBuilderCallback.buildCondition(locator, query);
		query.addOrderBy(Tables.EH_USER_BLACKLISTS.ID.desc());
		query.addLimit(pageSize + 1);
		query.fetch().map((r) -> {
			results.add(ConvertHelper.convert(r, UserBlacklist.class));
			return null;
		});

		locator.setAnchor(null);
		if(results.size() > pageSize){
			results.remove(results.size() - 1);
			locator.setAnchor(results.get(results.size() - 1).getId());
		}
		return results;
	}

	@Override
	public Long createUserBlacklist(UserBlacklist userBlacklist) {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUserBlacklists.class));
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhUserBlacklists.class));
		userBlacklist.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		userBlacklist.setId(id);
		EhUserBlacklistsDao dao = new EhUserBlacklistsDao(context.configuration());
		dao.insert(userBlacklist);
		return id;
	}

	@Override
	public void updateUserBlacklist(UserBlacklist userBlacklist) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhUserBlacklists.class));
		EhUserBlacklistsDao dao = new EhUserBlacklistsDao(context.configuration());
		dao.update(userBlacklist);
	}

	@Override
	public UserBlacklist findUserBlacklistById(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		EhUserBlacklistsDao dao = new EhUserBlacklistsDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), UserBlacklist.class);
	}

	@Override
	public UserBlacklist findUserBlacklistByContactToken(Integer namespaceId, String scopeType, Long scopeId, String contactToken) {
		List<UserBlacklist> results = new ArrayList<>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhUserBlacklistsRecord> query = context.selectQuery(Tables.EH_USER_BLACKLISTS);
		query.addConditions(Tables.EH_USER_BLACKLISTS.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_USER_BLACKLISTS.CONTACT_TOKEN.eq(contactToken));
		query.addConditions(Tables.EH_USER_BLACKLISTS.STATUS.eq(UserBlacklistStatus.ACTIVE.getCode()));
		if(!StringUtils.isEmpty(scopeType)){
			query.addConditions(Tables.EH_USER_BLACKLISTS.SCOPE_TYPE.eq(scopeType));
		}

		if(null != scopeId){
			query.addConditions(Tables.EH_USER_BLACKLISTS.SCOPE_ID.eq(scopeId));
		}

		query.fetch().map((r) -> {
			results.add(ConvertHelper.convert(r, UserBlacklist.class));
			return null;
		});

		if(results.size() > 0){
			return results.get(0);
		}

		return null;
	}
}
