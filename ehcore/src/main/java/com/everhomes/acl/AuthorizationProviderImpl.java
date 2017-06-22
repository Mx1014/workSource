// @formatter:off
package com.everhomes.acl;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.menu.Target;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.acl.IdentityType;
import com.everhomes.rest.module.Project;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhAuthorizationRelationsDao;
import com.everhomes.server.schema.tables.daos.EhAuthorizationsDao;
import com.everhomes.server.schema.tables.pojos.EhAuthorizationRelations;
import com.everhomes.server.schema.tables.pojos.EhAuthorizations;
import com.everhomes.server.schema.tables.records.EhAuthorizationRelationsRecord;
import com.everhomes.server.schema.tables.records.EhAuthorizationsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.Condition;
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
import java.util.stream.Collectors;

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
	public List<Project> getAuthorizationProjectsByAuthIdAndTargets(String authType, Long authId, List<Target> targets){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		List<Project> result  = new ArrayList<>();
		Condition cond = Tables.EH_AUTHORIZATIONS.AUTH_TYPE.eq(authType);
		cond = cond.and(Tables.EH_AUTHORIZATIONS.AUTH_ID.eq(authId));
		Condition targetCond = null;
		for (Target target:targets) {
			if(null == targetCond){
				targetCond = Tables.EH_AUTHORIZATIONS.TARGET_TYPE.eq(target.getTargetType()).and(Tables.EH_AUTHORIZATIONS.TARGET_ID.eq(target.getTargetId()));
			}else{
				targetCond = targetCond.or(Tables.EH_AUTHORIZATIONS.TARGET_TYPE.eq(target.getTargetType()).and(Tables.EH_AUTHORIZATIONS.TARGET_ID.eq(target.getTargetId())));
			}
		}
		cond = cond.and(targetCond);
		context.select(Tables.EH_AUTHORIZATIONS.OWNER_TYPE, Tables.EH_AUTHORIZATIONS.OWNER_ID)
				.from(Tables.EH_AUTHORIZATIONS)
				.where(cond)
				.groupBy(Tables.EH_AUTHORIZATIONS.OWNER_TYPE, Tables.EH_AUTHORIZATIONS.OWNER_ID)
				.orderBy(Tables.EH_AUTHORIZATIONS.OWNER_ID)
				.fetch().map((r) -> {
					result.add(new Project(r.getValue(Tables.EH_AUTHORIZATIONS.OWNER_TYPE).toString(), Long.valueOf(r.getValue(Tables.EH_AUTHORIZATIONS.OWNER_ID).toString())));
					return null;
				});
		return result;
	}


	@Override
	public List<Long> getAuthorizationModuleIdsByTarget(List<Target> targets){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		List<Long> result  = new ArrayList<>();
		SelectQuery<EhAuthorizationsRecord> query = context.selectQuery(Tables.EH_AUTHORIZATIONS);
		Condition cond = Tables.EH_AUTHORIZATIONS.AUTH_TYPE.eq(EntityType.SERVICE_MODULE.getCode());
		Condition targetCond = null;
		for (Target target:targets) {
			if(null == targetCond){
				targetCond = Tables.EH_AUTHORIZATIONS.TARGET_TYPE.eq(target.getTargetType()).and(Tables.EH_AUTHORIZATIONS.TARGET_ID.eq(target.getTargetId()));
			}else{
				targetCond = targetCond.or(Tables.EH_AUTHORIZATIONS.TARGET_TYPE.eq(target.getTargetType()).and(Tables.EH_AUTHORIZATIONS.TARGET_ID.eq(target.getTargetId())));
			}
		}
		cond = cond.and(targetCond);
		query.addConditions(cond);
		query.fetch().map((r) -> {
			if(!result.contains(r.getAuthId()))
				result.add(r.getAuthId());
			return null;
		});
		return result;
	}

	@Override
	public List<AuthorizationRelation> listAuthorizationRelations(CrossShardListingLocator locator, Integer pageSize, ListingQueryBuilderCallback queryBuilderCallback){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		if(null != pageSize)
			pageSize = pageSize + 1;
		List<AuthorizationRelation> result  = new ArrayList<>();
		SelectQuery<EhAuthorizationRelationsRecord> query = context.selectQuery(Tables.EH_AUTHORIZATION_RELATIONS);

		if(null != queryBuilderCallback)
			queryBuilderCallback.buildCondition(locator, query);
		if(null != locator && null != locator.getAnchor())
			query.addConditions(Tables.EH_AUTHORIZATION_RELATIONS.ID.lt(locator.getAnchor()));

		query.addOrderBy(Tables.EH_AUTHORIZATION_RELATIONS.ID.desc());
		if(null != pageSize)
			query.addLimit(pageSize);
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, AuthorizationRelation.class));
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
	public List<AuthorizationRelation> listAuthorizationRelations(CrossShardListingLocator locator, Integer pageSize, String ownerType, Long ownerId, Long moduleId){
		return listAuthorizationRelations(locator, pageSize, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_AUTHORIZATION_RELATIONS.OWNER_TYPE.eq(ownerType));
				query.addConditions(Tables.EH_AUTHORIZATION_RELATIONS.OWNER_ID.eq(ownerId));
				query.addConditions(Tables.EH_AUTHORIZATION_RELATIONS.MODULE_ID.eq(moduleId));
				return query;
			}
		});
	}


	@Override
	public List<AuthorizationRelation> listAuthorizationRelations(String ownerType, Long ownerId, Long moduleId){
		return listAuthorizationRelations(null, null, ownerType, ownerId, moduleId);
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
	public List<Authorization> listAuthorizationsByScope(String scope){
		return listAuthorizations(null, null, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_AUTHORIZATIONS.SCOPE.eq(scope));
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
	public long createAuthorizations(List<Authorization> authorizations) {
		long id = this.sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(Authorization.class), (long)authorizations.size());
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(Authorization.class));
		List<EhAuthorizations> auths = new ArrayList<>();
		for (Authorization authorization: authorizations) {
			id ++;
			authorization.setId(id);
			if(null == authorization.getCreateTime())
				authorization.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			auths.add(ConvertHelper.convert(authorization, EhAuthorizations.class));
		}
		EhAuthorizationsDao dao = new EhAuthorizationsDao(context.configuration());
		dao.insert(auths);
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

	@Override
	public AuthorizationRelation findAuthorizationRelationById(Long id){
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		EhAuthorizationRelationsDao dao = new EhAuthorizationRelationsDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), AuthorizationRelation.class);
	}

	@Override
	public Long createAuthorizationRelation(AuthorizationRelation authorizationRelation) {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(AuthorizationRelation.class));
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(AuthorizationRelation.class));
		if(null == authorizationRelation.getCreateTime())
			authorizationRelation.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		authorizationRelation.setId(id);
		EhAuthorizationRelationsDao dao = new EhAuthorizationRelationsDao(context.configuration());
		dao.insert(authorizationRelation);
		return id;
	}

	@Override
	public void deleteAuthorizationRelationById(Long id){
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(AuthorizationRelation.class));
		EhAuthorizationRelationsDao dao = new EhAuthorizationRelationsDao(context.configuration());
		dao.deleteById(id);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAuthorizationRelations.class, id);
	}

	@Override
	public void updateAuthorizationRelation(AuthorizationRelation authorizationRelation) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(AuthorizationRelation.class));
		if(null == authorizationRelation.getUpdateTime())
			authorizationRelation.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		EhAuthorizationRelationsDao dao = new EhAuthorizationRelationsDao(context.configuration());
		dao.update(authorizationRelation);
	}
}
