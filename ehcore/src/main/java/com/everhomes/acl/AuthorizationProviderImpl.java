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
import com.everhomes.rest.module.ControlTarget;
import com.everhomes.rest.module.Project;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhAuthorizationControlConfigsDao;
import com.everhomes.server.schema.tables.daos.EhAuthorizationRelationsDao;
import com.everhomes.server.schema.tables.daos.EhAuthorizationsDao;
import com.everhomes.server.schema.tables.pojos.EhAuthorizationControlConfigs;
import com.everhomes.server.schema.tables.pojos.EhAuthorizationRelations;
import com.everhomes.server.schema.tables.pojos.EhAuthorizations;
import com.everhomes.server.schema.tables.records.EhAuthorizationRelationsRecord;
import com.everhomes.server.schema.tables.records.EhAuthorizationsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.Tuple;
import org.jooq.*;
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
	public List<Project> getAuthorizationProjectsByAuthIdAndTargets(String authType, Long authId, List<Target> targets){
		return this.getAuthorizationProjectsByAuthIdAndTargets(null, authType, authId, targets);
	}

	@Override
	public List<Project> getManageAuthorizationProjectsByAuthAndTargets(String authType, Long authId, List<Target> targets){
		return this.getAuthorizationProjectsByAuthIdAndTargets(IdentityType.MANAGE.getCode(), authType, authId, targets);
	}

    @Override
    public Long getMaxControlIdInAuthorizations() {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		return context.select(Tables.EH_AUTHORIZATIONS.CONTROL_ID.max()).from(Tables.EH_AUTHORIZATIONS).fetchOne().value1();
    }

	@Override
	public Long createAuthorizationControlConfig(AuthorizationControlConfig authorizationControlConfig) {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAuthorizationControlConfigs.class));
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAuthorizationControlConfigs.class));
		authorizationControlConfig.setId(id);
		EhAuthorizationControlConfigsDao dao = new EhAuthorizationControlConfigsDao(context.configuration());
		dao.insert(authorizationControlConfig);
		return id;
	}

	@Override
	public Long createAuthorizationControlConfigs(List<AuthorizationControlConfig> authorizationControlConfigs) {
		long id = this.sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhAuthorizationControlConfigs.class), (long)authorizationControlConfigs.size());
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAuthorizationControlConfigs.class));
		List<EhAuthorizationControlConfigs> auths = new ArrayList<>();
		for (AuthorizationControlConfig authorizationControlConfig: authorizationControlConfigs) {
			id ++;
			authorizationControlConfig.setId(id);
			auths.add(ConvertHelper.convert(authorizationControlConfig, EhAuthorizationControlConfigs.class));
		}
		EhAuthorizationControlConfigsDao dao = new EhAuthorizationControlConfigsDao(context.configuration());
		dao.insert(auths);
		return id;
	}

	@Override
	public void delteAuthorizationControlConfigsWithCondition(Integer namespaceId, Long userId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAuthorizationControlConfigs.class));
		DeleteQuery query = context.deleteQuery(Tables.EH_AUTHORIZATION_CONTROL_CONFIGS);
		query.addConditions(Tables.EH_AUTHORIZATION_CONTROL_CONFIGS.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_AUTHORIZATION_CONTROL_CONFIGS.USER_ID.eq(userId));
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAuthorizationControlConfigs.class, null);
	}

	@Override
	public List<ControlTarget> listAuthorizationControlConfigs(Long userId, Long controlId) {
		List<ControlTarget> result = new ArrayList<>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		context.select()
				.from(Tables.EH_AUTHORIZATION_CONTROL_CONFIGS)
				.where(Tables.EH_AUTHORIZATION_CONTROL_CONFIGS.CONTROL_ID.eq(controlId))
				.and(Tables.EH_AUTHORIZATION_CONTROL_CONFIGS.USER_ID.eq(userId)).fetch()
				.map(r -> {
					result.add(new ControlTarget(r.getValue(Tables.EH_AUTHORIZATION_CONTROL_CONFIGS.TARGET_ID), r.getValue(Tables.EH_AUTHORIZATION_CONTROL_CONFIGS.INCLUDE_CHILD_FLAG)));
					return null;
				});
		return result;
	}

	@Override
	public List<Project> getAuthorizationProjectsByAuthIdAndTargets(String identityType, String authType, Long authId, List<Target> targets){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		List<Project> result  = new ArrayList<>();
		Condition cond = Tables.EH_AUTHORIZATIONS.AUTH_TYPE.eq(authType);
		if(null != authId){
			cond = cond.and(Tables.EH_AUTHORIZATIONS.AUTH_ID.eq(authId));
		}

		if(null != identityType){
			cond = cond.and(Tables.EH_AUTHORIZATIONS.IDENTITY_TYPE.eq(identityType));
		}
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
	public List<Project> getAuthorizationProjectsByAppIdAndTargets(String identityType, String authType, Long authId, Long appId, List<Target> targets){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		List<Project> result  = new ArrayList<>();
		Condition cond = Tables.EH_AUTHORIZATIONS.AUTH_TYPE.eq(authType);
		if(null != authId){
			cond = cond.and(Tables.EH_AUTHORIZATIONS.AUTH_ID.eq(authId));
		}
		if(null != appId){
			cond = cond.and(Tables.EH_AUTHORIZATIONS.MODULE_APP_ID.eq(appId));
		}
		if(null != identityType){
			cond = cond.and(Tables.EH_AUTHORIZATIONS.IDENTITY_TYPE.eq(identityType));
		}
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
	public List<String> getAuthorizationScopesByAuthAndTargets(String authType, Long authId, List<Target> targets){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		List<String> result  = new ArrayList<>();
		Condition cond = Tables.EH_AUTHORIZATIONS.AUTH_TYPE.eq(authType);
		if(null != authId){
			cond = cond.and(Tables.EH_AUTHORIZATIONS.AUTH_ID.eq(authId));
		}
		Condition targetCond = null;
		for (Target target:targets) {
			if(null == targetCond){
				targetCond = Tables.EH_AUTHORIZATIONS.TARGET_TYPE.eq(target.getTargetType()).and(Tables.EH_AUTHORIZATIONS.TARGET_ID.eq(target.getTargetId()));
			}else{
				targetCond = targetCond.or(Tables.EH_AUTHORIZATIONS.TARGET_TYPE.eq(target.getTargetType()).and(Tables.EH_AUTHORIZATIONS.TARGET_ID.eq(target.getTargetId())));
			}
		}
		cond = cond.and(targetCond);
		cond = cond.and(Tables.EH_AUTHORIZATIONS.SCOPE.isNotNull());
		context.select(Tables.EH_AUTHORIZATIONS.SCOPE)
				.from(Tables.EH_AUTHORIZATIONS)
				.where(cond)
				.groupBy(Tables.EH_AUTHORIZATIONS.SCOPE)
				.fetch().map((r) -> {
			result.add(r.getValue(Tables.EH_AUTHORIZATIONS.SCOPE));
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
	public List<Tuple<Long,String>> getAuthorizationAppModuleIdsByTarget(List<Target> targets) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		List<Tuple<Long,String>> result = new ArrayList<>();
		SelectQuery<EhAuthorizationsRecord> query = context.selectQuery(Tables.EH_AUTHORIZATIONS);
		Condition cond = Tables.EH_AUTHORIZATIONS.AUTH_TYPE.eq(EntityType.SERVICE_MODULE_APP.getCode());
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
			result.add(new Tuple<Long,String>(r.getModuleAppId(), r.getModuleControlType()));
			return null;
		});
		return result;
	}

	@Override
	public List<Tuple<Long, String>> getAuthorizationAppModuleIdsByTargetWithTypes(List<Target> targets, List<String> types) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		List<Tuple<Long,String>> result = new ArrayList<>();
		SelectQuery<EhAuthorizationsRecord> query = context.selectQuery(Tables.EH_AUTHORIZATIONS);
		Condition cond = Tables.EH_AUTHORIZATIONS.AUTH_TYPE.eq(EntityType.SERVICE_MODULE_APP.getCode());
		cond = cond.and(Tables.EH_AUTHORIZATIONS.MODULE_CONTROL_TYPE.in(types));
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
			result.add(new Tuple<Long,String>(r.getModuleAppId(), r.getModuleControlType()));
			return null;
		});
		return result;
	}

	@Override
	public List<Tuple<Long, String>> getAuthorizationAppModuleIdsByTargetWithTypesAndConfigIds(List<Target> targets, List<String> types, List<Long> configIds) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		TableLike t1 = Tables.EH_AUTHORIZATIONS.as("t1");
		TableLike t2 = Tables.EH_AUTHORIZATION_CONTROL_CONFIGS.as("t2");
		List<Tuple<Long,String>> result = new ArrayList<>();
		SelectJoinStep step = context.select().from(t1).leftOuterJoin(t2).on(t1.field("control_id").eq(t2.field("control_id")));
		Condition cond = t1.field("auth_type").eq(EntityType.SERVICE_MODULE_APP.getCode());
		cond = cond.and(t1.field("module_control_type").in(types)
				.or(t1.field("module_control_type").isNull()));//这一行必须加，权限细化的 module_control_type 为空值
		Condition targetCond = null;
		for (Target target:targets) {
			if(null == targetCond){
				targetCond = t1.field("target_type").eq(target.getTargetType()).and(t1.field("target_id").eq(target.getTargetId()));
			}else{
				targetCond = targetCond.or(t1.field("target_type").eq(target.getTargetType()).and(t1.field("target_id").eq(target.getTargetId())));
			}
		}
		cond = cond.and(targetCond);
		Condition t2_condition = (t2.field("target_type").in(types).and(t2.field("target_id").in(configIds)).or(t2.field("control_id").eq(0)));
		cond = cond.and(
				t1.field("control_id").isNull() //这一行必须加，因为权限细化的 control_id 为空值
				.or(t2_condition));
		step.where(cond).fetch().map((r) -> {
			result.add(new Tuple<Long,String>((Long)r.getValue(t1.field("module_app_id")), (String)r.getValue(t1.field("module_control_type"))));
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
	public List<AuthorizationRelation> listAuthorizationRelations(CrossShardListingLocator locator, Integer pageSize, String ownerType, Long ownerId, Long moduleId, Long appId){
		return listAuthorizationRelations(locator, pageSize, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_AUTHORIZATION_RELATIONS.OWNER_TYPE.eq(ownerType));
				query.addConditions(Tables.EH_AUTHORIZATION_RELATIONS.OWNER_ID.eq(ownerId));
				query.addConditions(Tables.EH_AUTHORIZATION_RELATIONS.MODULE_ID.eq(moduleId));
				if(appId != null){
					query.addConditions(Tables.EH_AUTHORIZATION_RELATIONS.APP_ID.eq(appId));
				}
				return query;
			}
		});
	}


	@Override
	public List<AuthorizationRelation> listAuthorizationRelations(String ownerType, Long ownerId, Long moduleId){
		return listAuthorizationRelations(null, null, ownerType, ownerId, moduleId,  null);
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
	public List<Authorization> listAuthorizations(String ownerType, Long ownerId, String targetType, Long targetId, String authType, Long authId, String identityType, Boolean targetFlag, CrossShardListingLocator locator, Integer pageSize){
		return listAuthorizations(locator, pageSize, new ListingQueryBuilderCallback() {
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
	public List<Authorization> listAuthorizations(String ownerType, Long ownerId, String targetType, Long targetId, String authType, Long authId, String identityType, Long appId, String moduleControlType, Byte all_control_flag, Boolean targetFlag){
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

				if(null != appId){
				    //如果是指定应用搜索，则寻找所有应用或者指定此应用的条目。
					query.addConditions(Tables.EH_AUTHORIZATIONS.MODULE_APP_ID.eq(appId).or(Tables.EH_AUTHORIZATIONS.MODULE_APP_ID.eq(0l)));
				}

				if(!StringUtils.isEmpty(moduleControlType)){
					query.addConditions(Tables.EH_AUTHORIZATIONS.MODULE_CONTROL_TYPE.eq(moduleControlType));
				}

				if(null != all_control_flag){
					query.addConditions(Tables.EH_AUTHORIZATIONS.ALL_CONTROL_FLAG.eq(all_control_flag));
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
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAuthorizations.class));
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAuthorizations.class));
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
		long id = this.sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhAuthorizations.class), (long)authorizations.size());
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAuthorizations.class));
		List<EhAuthorizations> auths = new ArrayList<>();
		for (Authorization authorization: authorizations) {
			id++;
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
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAuthorizations.class));
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
	public void deleteAuthorizationWithConditon(Integer namespaceId, String ownerType, Long ownerId, String targetType, Long targetId, String authType, Long authId, String identityType, String moduleControlType, Long appId, Long controlId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAuthorizations.class));
		DeleteQuery query = context.deleteQuery(Tables.EH_AUTHORIZATIONS);
		if (namespaceId != null)
			query.addConditions(Tables.EH_AUTHORIZATIONS.NAMESPACE_ID.eq(namespaceId));
		if (ownerId != null)
			query.addConditions(Tables.EH_AUTHORIZATIONS.OWNER_ID.eq(ownerId));
		if (!StringUtils.isEmpty(ownerType))
			query.addConditions(Tables.EH_AUTHORIZATIONS.OWNER_TYPE.eq(ownerType));
		if (!StringUtils.isEmpty(targetType))
			query.addConditions(Tables.EH_AUTHORIZATIONS.TARGET_TYPE.eq(targetType));
		if (targetId != null)
			query.addConditions(Tables.EH_AUTHORIZATIONS.TARGET_ID.eq(targetId));
		if (!StringUtils.isEmpty(authType))
			query.addConditions(Tables.EH_AUTHORIZATIONS.AUTH_TYPE.eq(authType));
		if (authId != null)
			query.addConditions(Tables.EH_AUTHORIZATIONS.AUTH_ID.eq(authId));
		if (!StringUtils.isEmpty(identityType))
			query.addConditions(Tables.EH_AUTHORIZATIONS.IDENTITY_TYPE.eq(identityType));
		if (!StringUtils.isEmpty(moduleControlType))
			query.addConditions(Tables.EH_AUTHORIZATIONS.MODULE_CONTROL_TYPE.eq(moduleControlType));
		if (appId != null)
			query.addConditions(Tables.EH_AUTHORIZATIONS.MODULE_APP_ID.eq(appId));
		if (controlId != null)
			query.addConditions(Tables.EH_AUTHORIZATIONS.CONTROL_ID.eq(controlId));

		query.execute();

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAuthorizations.class, null);
	}


	@Override
	public void deleteAuthorizationById(Long id){
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAuthorizations.class));
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
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAuthorizationRelations.class));
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAuthorizationRelations.class));
		if(null == authorizationRelation.getCreateTime())
			authorizationRelation.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		authorizationRelation.setId(id);
		EhAuthorizationRelationsDao dao = new EhAuthorizationRelationsDao(context.configuration());
		dao.insert(authorizationRelation);
		return id;
	}

	@Override
	public void deleteAuthorizationRelationById(Long id){
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAuthorizationRelations.class));
		EhAuthorizationRelationsDao dao = new EhAuthorizationRelationsDao(context.configuration());
		dao.deleteById(id);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAuthorizationRelations.class, id);
	}

	@Override
	public void updateAuthorizationRelation(AuthorizationRelation authorizationRelation) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAuthorizationRelations.class));
		if(null == authorizationRelation.getUpdateTime())
			authorizationRelation.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		EhAuthorizationRelationsDao dao = new EhAuthorizationRelationsDao(context.configuration());
		dao.update(authorizationRelation);
	}
}
