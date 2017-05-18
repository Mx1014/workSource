// @formatter:off
package com.everhomes.acl;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.acl.IdentityType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhAuthorizationsDao;
import com.everhomes.server.schema.tables.pojos.EhAuthorizations;
import com.everhomes.server.schema.tables.records.EhAuthorizationsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.Record;
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
public class AuthorizationProviderImpl implements AuthorizationProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationProviderImpl.class);

	@Override
	public List<Authorization> listAuthorizations(CrossShardListingLocator locator, Integer pageSize, ListingQueryBuilderCallback queryBuilderCallback){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		if(null != pageSize)
			pageSize = pageSize + 1;
		List<Authorization> result  = new ArrayList<>();
		SelectQuery<EhAuthorizationsRecord> query = context.selectQuery(Tables.EH_AUTHORIZATIONS);

		if(null != queryBuilderCallback)
			queryBuilderCallback.buildCondition(locator, query);
		if(null != locator && null != locator.getAnchor())
			query.addConditions(Tables.EH_AUTHORIZATIONS.ID.lt(locator.getAnchor()));

		query.addOrderBy(Tables.EH_AUTHORIZATIONS.ID.desc());
		if(null != pageSize)
			query.addLimit(pageSize);
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, Authorization.class));
			return null;
		});
		if(null != locator)
			locator.setAnchor(null);

		if(null != pageSize && result.size() >= pageSize){
			result.remove(result.size() - 1);
			locator.setAnchor(result.get(result.size() - 1).getId());
		}
		return result;
	}

	@Override
	public List<Authorization> listAuthorizations(String ownerType, Long ownerId, String targetType, Long targetId, String authType, Long authId, String identityType, Boolean targetFlag){
		return listAuthorizations(null, null, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
				if(!StringUtils.isEmpty(ownerType) && null != ownerId){
					query.addConditions(Tables.EH_AUTHORIZATIONS.OWNER_TYPE.eq(ownerType));
					query.addConditions(Tables.EH_AUTHORIZATIONS.OWNER_ID.eq(ownerId));
				}

				if(!StringUtils.isEmpty(targetType) && null != targetId){
					query.addConditions(Tables.EH_AUTHORIZATIONS.TARGET_TYPE.eq(targetType));
					query.addConditions(Tables.EH_AUTHORIZATIONS.TARGET_ID.eq(targetId));
				}

				if(!StringUtils.isEmpty(authType)){
					query.addConditions(Tables.EH_AUTHORIZATIONS.AUTH_TYPE.eq(authType));
				}

				if(!StringUtils.isEmpty(authType) && null != authId){
					query.addConditions(Tables.EH_AUTHORIZATIONS.AUTH_ID.eq(authId));
				}

				if(!StringUtils.isEmpty(identityType)){
					query.addConditions(Tables.EH_AUTHORIZATIONS.IDENTITY_TYPE.eq(identityType));
				}

				if(targetFlag){
					query.addGroupBy(Tables.EH_AUTHORIZATIONS.TARGET_TYPE);
					query.addGroupBy(Tables.EH_AUTHORIZATIONS.TARGET_ID);
				}
				return query;
			}
		});
	}

	@Override
	public List<Authorization> listAuthorizations(String ownerType, Long ownerId, String targetType, Long targetId, String authType, Long authId, String identityType){
		return listAuthorizations(ownerType, ownerId, targetType, targetId, authType, authId, identityType, false);
	}

	@Override
	public List<Authorization> listManageAuthorizationsByTarget(String ownerType, Long ownerId, String targetType, Long targetId, String authType, Long authId){
		return listAuthorizations(ownerType, ownerId, targetType, targetId, authType, null, IdentityType.MANAGE.getCode(), false);
	}

	@Override
	public List<Authorization> listManageAuthorizations(String ownerType, Long ownerId, String authType, Long authId){
		return listTargetAuthorizations(ownerType, ownerId, authType, authId, IdentityType.MANAGE.getCode());
	}

	@Override
	public List<Authorization> listOrdinaryAuthorizations(String ownerType, Long ownerId, String authType, Long authId) {
		return listTargetAuthorizations(ownerType, ownerId, authType, authId, IdentityType.ORDINARY.getCode());
	}

	@Override
	public List<Authorization> listOrdinaryAuthorizationsByTarget(String ownerType, Long ownerId, String targetType, Long targetId, String authType, Long authId) {
		return listAuthorizations(ownerType, ownerId, targetType, targetId, authType, null, IdentityType.ORDINARY.getCode(), false);
	}

	@Override
	public List<Authorization> listTargetAuthorizations(String ownerType, Long ownerId, String authType, Long authId, String identityType) {
		return listAuthorizations(ownerType, ownerId, null, null, authType, authId, identityType, true);
	}

	@Override
	public Long createAuthorization(Authorization authorization) {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Authorization.class));
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(Authorization.class));
		if(null == authorization.getCreateTime())
			authorization.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		if(null == authorization.getUpdateTime())
			authorization.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		authorization.setId(id);
		EhAuthorizationsDao dao = new EhAuthorizationsDao(context.configuration());
		dao.insert(authorization);
		return id;
	}

	@Override
	public void updateAuthorization(Authorization authorization) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(Authorization.class));
		if(null == authorization.getUpdateTime())
			authorization.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		EhAuthorizationsDao dao = new EhAuthorizationsDao(context.configuration());
		dao.update(authorization);
	}



	@Override
	public List<Authorization> deleteAuthorization(String ownerType, Long ownerId, String targetType, Long targetId, String authType, Long authId, String identityType) {
		List<Authorization> authorizations = listAuthorizations(ownerType, ownerId, targetType, targetId, authType, authId, identityType);
		for (Authorization authorization: authorizations) {
			deleteAuthorizationById(authorization.getId());
		}
		return authorizations;
	}

	@Override
	public void deleteAuthorizationById(Long id){
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(Authorization.class));
		EhAuthorizationsDao dao = new EhAuthorizationsDao(context.configuration());
		dao.deleteById(id);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAuthorizations.class, id);
	}
}
