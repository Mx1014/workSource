// @formatter:off
package com.everhomes.acl;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.module.*;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.module.ServiceModuleStatus;
import com.everhomes.schema.tables.pojos.EhAclRoles;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhServiceModules;
import com.everhomes.server.schema.tables.daos.EhServiceModuleAssignmentsDao;
import com.everhomes.server.schema.tables.daos.EhServiceModulesDao;
import com.everhomes.server.schema.tables.pojos.EhServiceModuleAssignments;
import com.everhomes.server.schema.tables.pojos.EhServiceModuleScopes;
import com.everhomes.server.schema.tables.records.EhServiceModuleAssignmentsRecord;
import com.everhomes.server.schema.tables.records.EhServiceModulePrivilegesRecord;
import com.everhomes.server.schema.tables.records.EhServiceModuleScopesRecord;
import com.everhomes.server.schema.tables.records.EhServiceModulesRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.*;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class PrivilegeProviderImpl implements PrivilegeProvider {

	@Autowired
	private DbProvider dbProvider;

	private static final Logger LOGGER = LoggerFactory.getLogger(PrivilegeProviderImpl.class);

	@Cacheable(
			value = {"AclRoles-ByOwnerAndKeywords"},
			key = "{#namespaceId, #appId, #ownerType, #ownerId, #keywords}",
			unless = "#result == null || #result.size() == 0"
	)
	@Override
	public List<Role> getRolesByOwnerAndKeywords(int namespaceId, long appId, String ownerType, Long ownerId, String keywords) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhAclRoles.class));
		List roles;
		Condition cond = null;
		if(!org.springframework.util.StringUtils.isEmpty(keywords)){
			cond = com.everhomes.schema.Tables.EH_ACL_ROLES.NAME.like(keywords + "%");
		}
		if(ownerId == null) {
			Condition c = com.everhomes.schema.Tables.EH_ACL_ROLES.NAMESPACE_ID.eq(Integer.valueOf(namespaceId)).and(com.everhomes.schema.Tables.EH_ACL_ROLES.APP_ID.eq(Long.valueOf(appId))).and(com.everhomes.schema.Tables.EH_ACL_ROLES.OWNER_TYPE.eq(ownerType)).and(com.everhomes.schema.Tables.EH_ACL_ROLES.OWNER_ID.isNull());
			if(null != cond){
				c.and(cond);
			}
			roles = context.select(new Field[0]).from(new TableLike[]{com.everhomes.schema.Tables.EH_ACL_ROLES})
					.where(new Condition[]{c}).fetch().map((arg) -> {
				return (Role)ConvertHelper.convert(arg, Role.class);
			});
			return roles;
		} else {
			Condition c = com.everhomes.schema.Tables.EH_ACL_ROLES.NAMESPACE_ID.eq(Integer.valueOf(namespaceId)).and(com.everhomes.schema.Tables.EH_ACL_ROLES.APP_ID.eq(Long.valueOf(appId))).and(com.everhomes.schema.Tables.EH_ACL_ROLES.OWNER_TYPE.eq(ownerType)).and(com.everhomes.schema.Tables.EH_ACL_ROLES.OWNER_ID.eq(ownerId));
			if(null != cond){
				c.and(cond);
			}
			roles = context.select(new Field[0]).from(new TableLike[]{com.everhomes.schema.Tables.EH_ACL_ROLES}).where(new Condition[]{c}).fetch().map((arg) -> {
				return (Role)ConvertHelper.convert(arg, Role.class);
			});
			return roles;
		}
	}

}
