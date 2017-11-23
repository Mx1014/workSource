// @formatter:off
package com.everhomes.acl;

import com.everhomes.db.*;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.schema.Tables;
import com.everhomes.schema.tables.EhAclRoles;
import com.everhomes.schema.tables.records.EhAclRolesRecord;
import com.everhomes.util.ConvertHelper;
import org.jooq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PrivilegeProviderImpl implements PrivilegeProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private AclProvider aclProvider;

	private static final Logger LOGGER = LoggerFactory.getLogger(PrivilegeProviderImpl.class);

	/* @Cacheable(
			value = {"AclRoles-ByOwnerAndKeywords"},
			key = "{#namespaceId, #appId, #ownerType, #ownerId, #keywords}",
			unless = "#result == null || #result.size() == 0"
	) */
	@Override
	public List<Role> getRolesByOwnerAndKeywords(int namespaceId, long appId, String ownerType, Long ownerId, String keywords) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhAclRoles.class));
		List roles;
		Condition cond = null;
		if(!org.springframework.util.StringUtils.isEmpty(keywords)){
			cond = Tables.EH_ACL_ROLES.NAME.like("%" + keywords + "%");
		}
		if(ownerId == null) {
			Condition c = Tables.EH_ACL_ROLES.NAMESPACE_ID.eq(Integer.valueOf(namespaceId)).and(Tables.EH_ACL_ROLES.APP_ID.eq(Long.valueOf(appId))).and(Tables.EH_ACL_ROLES.OWNER_TYPE.eq(ownerType)).and(Tables.EH_ACL_ROLES.OWNER_ID.isNull());
			if(null != cond){
				c = c.and(cond);
			}
			roles = context.select(new Field[0]).from(new TableLike[]{Tables.EH_ACL_ROLES})
					.where(new Condition[]{c}).fetch().map((arg) -> {
				return (Role)ConvertHelper.convert(arg, Role.class);
			});
			return roles;
		} else {
			Condition c = Tables.EH_ACL_ROLES.NAMESPACE_ID.eq(Integer.valueOf(namespaceId)).and(Tables.EH_ACL_ROLES.APP_ID.eq(Long.valueOf(appId))).and(Tables.EH_ACL_ROLES.OWNER_TYPE.eq(ownerType)).and(Tables.EH_ACL_ROLES.OWNER_ID.eq(ownerId));
			if(null != cond){
				c = c.and(cond);
			}
			roles = context.select(new Field[0]).from(new TableLike[]{Tables.EH_ACL_ROLES}).where(new Condition[]{c}).fetch().map((arg) -> {
				return (Role)ConvertHelper.convert(arg, Role.class);
			});
			return roles;
		}
	}

	@Override
	public List<Role> listRolesByOwnerAndKeywords(CrossShardListingLocator locator, Integer pageSize, int namespaceId, long appId, String ownerType, Long ownerId, String keywords) {
		return listRoles(locator, pageSize, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
				Condition cond = null;
				Condition c = null;
				if(!org.springframework.util.StringUtils.isEmpty(keywords)){
					cond = Tables.EH_ACL_ROLES.NAME.like("%" + keywords + "%");
				}
				if(ownerId == null) {
					c = Tables.EH_ACL_ROLES.NAMESPACE_ID.eq(Integer.valueOf(namespaceId)).and(Tables.EH_ACL_ROLES.APP_ID.eq(Long.valueOf(appId))).and(Tables.EH_ACL_ROLES.OWNER_TYPE.eq(ownerType)).and(Tables.EH_ACL_ROLES.OWNER_ID.isNull());
					if(null != cond){
						c = c.and(cond);
					}
				} else {
					c = Tables.EH_ACL_ROLES.NAMESPACE_ID.eq(Integer.valueOf(namespaceId)).and(Tables.EH_ACL_ROLES.APP_ID.eq(Long.valueOf(appId))).and(Tables.EH_ACL_ROLES.OWNER_TYPE.eq(ownerType)).and(Tables.EH_ACL_ROLES.OWNER_ID.eq(ownerId));
					if(null != cond){
						c = c.and(cond);
					}
				}
				query.addConditions(c);
				return null;
			}
		});
	}

	@Override
	public List<Role> listRoles(CrossShardListingLocator locator, Integer pageSize, ListingQueryBuilderCallback queryBuilderCallback) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhAclRoles.class));
		List<Role> results = new ArrayList<>();
		pageSize = pageSize + 1;
		SelectQuery<EhAclRolesRecord> query = context.selectQuery(Tables.EH_ACL_ROLES);
		if(null != queryBuilderCallback)
			queryBuilderCallback.buildCondition(locator, query);
		if(null != locator && null != locator.getAnchor())
			query.addConditions(Tables.EH_ACL_ROLES.ID.lt(locator.getAnchor()));

		query.addOrderBy(Tables.EH_ACL_ROLES.ID.desc());
		query.addLimit(pageSize);
		query.fetch().map((arg) -> {
			results.add(ConvertHelper.convert(arg, Role.class));
			return null;
		});

		if(null != locator)
			locator.setAnchor(null);

		if(results.size() >= pageSize){
			results.remove(results.size() - 1);
			locator.setAnchor(results.get(results.size() - 1).getId());
		}
		return results;
	}


	@Override
	public List<Acl> listAcls(String ownerType, Long ownerId, String targetType, Long targetId){
		AclRoleDescriptor aclRoleDescriptor = new AclRoleDescriptor(targetType, targetId);
		return aclProvider.getResourceAclByRole(ownerType, ownerId, aclRoleDescriptor);
	}

	@Override
	public List<Acl> listAclsByScope(String ownerType, Long ownerId, String targetType, Long targetId, String scope){
		return aclProvider.getAcl(new QueryBuilder() {
			@Override
			public SelectQuery<? extends Record> buildCondition(SelectQuery<? extends Record> selectQuery) {
				Condition cond = null;
				if(ownerId == null) {
					cond = Tables.EH_ACLS.OWNER_TYPE.eq(ownerType).and(Tables.EH_ACLS.OWNER_ID.isNull());
				}else{
					cond = Tables.EH_ACLS.OWNER_TYPE.eq(ownerType).and(Tables.EH_ACLS.OWNER_ID.eq(ownerId));
				}
				cond = cond.and(Tables.EH_ACLS.ROLE_TYPE.eq(targetType));
				cond = cond.and(Tables.EH_ACLS.ROLE_ID.eq(targetId));
				cond = cond.and(Tables.EH_ACLS.SCOPE.eq(scope));
				selectQuery.addConditions(cond);
				return selectQuery;
			}
		});
	}

	@Override
	public List<Acl> listAclsByTag(String tag){
		return aclProvider.getAcl(new QueryBuilder() {
			@Override
			public SelectQuery<? extends Record> buildCondition(SelectQuery<? extends Record> selectQuery) {
				Condition cond = Tables.EH_ACLS.COMMENT_TAG1.eq(tag);
				selectQuery.addConditions(cond);
				return selectQuery;
			}
		});
	}

	@Override
	public List<Acl> listAclsByModuleId(String ownerType, Long ownerId, String targetType, Long targetId, Long moduleId){
		String scope = EntityType.SERVICE_MODULE.getCode() + moduleId;
		return listAclsByScope(ownerType, ownerId, targetType, targetId, scope);
	}

	@Override
	public void deleteAclsByTag(String tag){
		List<Acl> acls = listAclsByTag(tag);
		for (Acl acl: acls) {
			aclProvider.deleteAcl(acl.getId());
		}
	}



}
