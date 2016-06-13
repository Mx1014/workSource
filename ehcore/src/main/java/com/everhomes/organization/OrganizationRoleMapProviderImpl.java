// @formatter:off
package com.everhomes.organization;

import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.rest.organization.PrivateFlag;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhOrganizationRoleMapDao;
import com.everhomes.server.schema.tables.pojos.EhOrganizationRoleMap;
import com.everhomes.server.schema.tables.records.EhOrganizationRoleMapRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
@Component
public class OrganizationRoleMapProviderImpl implements OrganizationRoleMapProvider {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationRoleMapProviderImpl.class);

	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
    private ShardingProvider shardingProvider;

	@Override
	public List<OrganizationRoleMap> listOrganizationRoleMaps(Long ownerId, EntityType ownerType, PrivateFlag privateFlag) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhOrganizationRoleMapRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_ROLE_MAP);
		Condition cond = Tables.EH_ORGANIZATION_ROLE_MAP.OWNER_ID.eq(ownerId);
		cond = cond.and(Tables.EH_ORGANIZATION_ROLE_MAP.OWNER_TYPE.eq(ownerType.getCode()));
		cond = cond.or(Tables.EH_ORGANIZATION_ROLE_MAP.PRIVATE_FLAG.eq(privateFlag.getCode()));
		query.addConditions(cond);
		return query.fetch().map((r) -> {
			return ConvertHelper.convert(r, OrganizationRoleMap.class);
		});
	}
	
	@Override
	public List<OrganizationRoleMap> listOrganizationRoleMapsByOwnerIds(List<Long> ownerIds, EntityType ownerType, PrivateFlag privateFlag) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhOrganizationRoleMapRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_ROLE_MAP);
		Condition cond = Tables.EH_ORGANIZATION_ROLE_MAP.OWNER_TYPE.eq(ownerType.getCode());
		if(null != ownerIds && 0 != ownerIds.size()){
			Condition c = null;
			for (Long ownerId : ownerIds) {
				if(null == c){
					c = Tables.EH_ORGANIZATION_ROLE_MAP.OWNER_ID.eq(ownerId);
				}else{
					c = c.or(Tables.EH_ORGANIZATION_ROLE_MAP.OWNER_ID.eq(ownerId));
				}
			}
			c = c.or(Tables.EH_ORGANIZATION_ROLE_MAP.PRIVATE_FLAG.eq(privateFlag.getCode()));
			cond = cond.and(c);
		}
		query.addConditions(cond);
		return query.fetch().map((r) -> {
			return ConvertHelper.convert(r, OrganizationRoleMap.class);
		});
	}
	
	@Override
	public void createOrganizationRoleMap(OrganizationRoleMap organizationRoleMap) {
		long id = shardingProvider.allocShardableContentId(EhOrganizationRoleMap.class).second();
		organizationRoleMap.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhOrganizationRoleMap.class, id));
		EhOrganizationRoleMapDao dao = new EhOrganizationRoleMapDao(context.configuration());
		dao.insert(organizationRoleMap);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhOrganizationRoleMap.class, null); 
		
	}
	
	
	@Override
	public OrganizationRoleMap getOrganizationRoleMap(Long roleId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhOrganizationRoleMapRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_ROLE_MAP);
		Condition cond = Tables.EH_ORGANIZATION_ROLE_MAP.ROLE_ID.eq(roleId);
		query.addConditions(cond);
		return ConvertHelper.convert(query.fetchAny(), OrganizationRoleMap.class);
	}
	
	@Override
	public void updateOrganizationRoleMap(
			OrganizationRoleMap organizationRoleMap) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationRoleMapDao dao = new EhOrganizationRoleMapDao(context.configuration());
		dao.update(organizationRoleMap);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationRoleMap.class, organizationRoleMap.getId());
	}
}
