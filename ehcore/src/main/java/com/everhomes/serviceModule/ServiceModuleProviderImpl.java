// @formatter:off
package com.everhomes.serviceModule;

import com.everhomes.acl.WebMenu;
import com.everhomes.acl.WebMenuPrivilege;
import com.everhomes.acl.WebMenuPrivilegeProvider;
import com.everhomes.acl.WebMenuScope;
import com.everhomes.activity.ActivityVideo;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.acl.WebMenuPrivilegeShowFlag;
import com.everhomes.rest.acl.WebMenuStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhActivityVideoDao;
import com.everhomes.server.schema.tables.daos.EhServiceModuleAssignmentsDao;
import com.everhomes.server.schema.tables.pojos.EhActivityVideo;
import com.everhomes.server.schema.tables.pojos.EhOrganizations;
import com.everhomes.server.schema.tables.pojos.EhServiceModuleAssignments;
import com.everhomes.server.schema.tables.pojos.EhServiceModulePrivileges;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class ServiceModuleProviderImpl implements ServiceModuleProvider {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceModuleProviderImpl.class);

	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
    private ShardingProvider shardingProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public List<ServiceModulePrivilege> listServiceModulePrivileges(Long moduleId, ServiceModulePrivilegeType privilegeType) {
		List<ServiceModulePrivilege> results = new ArrayList<>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhServiceModulePrivilegesRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_PRIVILEGES);
		Condition cond = Tables.EH_SERVICE_MODULE_PRIVILEGES.MODULE_ID.eq(moduleId);
		if(null != privilegeType){
			cond = cond.and(Tables.EH_SERVICE_MODULE_PRIVILEGES.PRIVILEGE_TYPE.eq(privilegeType.getCode()));
		}
		query.addConditions(cond);
		query.addOrderBy(Tables.EH_SERVICE_MODULE_PRIVILEGES.DEFAULT_ORDER);

		query.fetch().map((r) -> {
			results.add(ConvertHelper.convert(r, ServiceModulePrivilege.class));
			return null;
		});
		return results;
	}

	@Override
	public Long createServiceModuleAssignment(ServiceModuleAssignment serviceModuleAssignment) {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceModuleAssignments.class));
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhServiceModuleAssignments.class));
		serviceModuleAssignment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		serviceModuleAssignment.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		serviceModuleAssignment.setId(id);
		EhServiceModuleAssignmentsDao dao = new EhServiceModuleAssignmentsDao(context.configuration());
		dao.insert(serviceModuleAssignment);
		return id;
	}

	@Override
	public void deleteServiceModuleAssignmentById(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhServiceModuleAssignmentsDao dao = new EhServiceModuleAssignmentsDao(context.configuration());
		dao.deleteById(id);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhServiceModuleAssignments.class, id);
	}

	@Override
	public List<ServiceModuleAssignment> listServiceModuleAssignments(Condition condition, Long organizationId) {
		List<ServiceModuleAssignment> results = new ArrayList<>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhServiceModuleAssignmentsRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_ASSIGNMENTS);
		Condition cond = Tables.EH_SERVICE_MODULE_ASSIGNMENTS.ORGANIZATION_ID.eq(organizationId);
		if(null != condition){
			cond = cond.and(condition);
		}
		query.addConditions(cond);
		query.addOrderBy(Tables.EH_SERVICE_MODULE_PRIVILEGES.DEFAULT_ORDER);
		query.fetch().map((r) -> {
			results.add(ConvertHelper.convert(r, ServiceModuleAssignment.class));
			return null;
		});
		return results;
	}


//	@Override
//	//@Caching(evict={@CacheEvict(value="ListWebMenuByPrivilegeIds", key="webMenuByPrivilegeIds")})
//	public List<WebMenuPrivilege> listWebMenuByPrivilegeIds(
//			List<Long> privilegeIds, WebMenuPrivilegeShowFlag showFlag) {
//		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
//		SelectQuery<EhWebMenuPrivilegesRecord> query = context.selectQuery(Tables.EH_WEB_MENU_PRIVILEGES);
//		Condition cond = null;
//		cond = Tables.EH_WEB_MENU_PRIVILEGES.PRIVILEGE_ID.in(privilegeIds);
//
//		if(null != showFlag){
//			cond = cond.and(Tables.EH_WEB_MENU_PRIVILEGES.SHOW_FLAG.eq(showFlag.getCode()));
//		}
//		query.addConditions(cond);
//		query.addOrderBy(Tables.EH_WEB_MENU_PRIVILEGES.SORT_NUM);
//		return query.fetch().map((r) -> {
//			return ConvertHelper.convert(r, WebMenuPrivilege.class);
//		});
//	}
//
//	@Override
//	//@Caching(evict = {@CacheEvict(value="listWebMenuByType", key="'webMenuByMenuIds'")})
//	public List<WebMenu> listWebMenuByMenuIds(
//			List<Long> menuIds) {
//		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
//		SelectQuery<EhWebMenusRecord> query = context.selectQuery(Tables.EH_WEB_MENUS);
//		Condition cond = Tables.EH_WEB_MENUS.STATUS.eq(WebMenuStatus.ACTIVE.getCode());
//		cond = cond.and(Tables.EH_WEB_MENUS.ID.in(menuIds));
//		query.addConditions(cond);
//		query.addOrderBy(Tables.EH_WEB_MENUS.SORT_NUM);
//		return query.fetch().map((r) -> {
//			return ConvertHelper.convert(r, WebMenu.class);
//		});
//	}
//
//	@Override
//	//@Caching(evict = {@CacheEvict(value="listWebMenuByType", key="'webMenuByMenuIds'")})
//	public List<WebMenuScope> listWebMenuScopeByOwnerId(String ownerType, Long ownerId) {
//		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
//		SelectQuery<EhWebMenuScopesRecord> query = context.selectQuery(Tables.EH_WEB_MENU_SCOPES);
//		Condition cond = Tables.EH_WEB_MENU_SCOPES.OWNER_TYPE.eq(ownerType);
//		cond = cond.and(Tables.EH_WEB_MENU_SCOPES.OWNER_ID.eq(ownerId));
//		query.addConditions(cond);
//		return query.fetch().map((r) -> {
//			return ConvertHelper.convert(r, WebMenuScope.class);
//		});
//	}
}
