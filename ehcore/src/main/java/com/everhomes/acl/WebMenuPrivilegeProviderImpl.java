// @formatter:off
package com.everhomes.acl;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.rest.acl.WebMenuPrivilegeShowFlag;
import com.everhomes.rest.acl.WebMenuStatus;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhWebMenuPrivilegesRecord;
import com.everhomes.server.schema.tables.records.EhWebMenuScopesRecord;
import com.everhomes.server.schema.tables.records.EhWebMenusRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class WebMenuPrivilegeProviderImpl implements WebMenuPrivilegeProvider {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebMenuPrivilegeProviderImpl.class);

	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
    private ShardingProvider shardingProvider;

	
	@Override
	//@Caching(evict={@CacheEvict(value="listWebMenuByType", key="'webMenu'")})
	public List<WebMenu> listWebMenuByType(String type) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhWebMenusRecord> query = context.selectQuery(Tables.EH_WEB_MENUS);
		Condition cond = Tables.EH_WEB_MENUS.STATUS.eq(WebMenuStatus.ACTIVE.getCode());
		cond = cond.and(Tables.EH_WEB_MENUS.TYPE.eq(type));
		query.addConditions(cond);
		query.addOrderBy(Tables.EH_WEB_MENUS.SORT_NUM);
		return query.fetch().map((r) -> {
			return ConvertHelper.convert(r, WebMenu.class);
		});
	}
	
	@Override
	//@Caching(evict={@CacheEvict(value="ListWebMenuByPrivilegeIds", key="webMenuByPrivilegeIds")})
	public List<WebMenuPrivilege> listWebMenuByPrivilegeIds(
			List<Long> privilegeIds, WebMenuPrivilegeShowFlag showFlag) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhWebMenuPrivilegesRecord> query = context.selectQuery(Tables.EH_WEB_MENU_PRIVILEGES);
		Condition cond = null;
		cond = Tables.EH_WEB_MENU_PRIVILEGES.PRIVILEGE_ID.in(privilegeIds);
		
		if(null != showFlag){
			cond = cond.and(Tables.EH_WEB_MENU_PRIVILEGES.SHOW_FLAG.eq(showFlag.getCode()));
		}
		query.addConditions(cond);
		query.addOrderBy(Tables.EH_WEB_MENU_PRIVILEGES.SORT_NUM);
		return query.fetch().map((r) -> {
			return ConvertHelper.convert(r, WebMenuPrivilege.class);
		});
	}
	
	@Override
	//@Caching(evict = {@CacheEvict(value="listWebMenuByType", key="'webMenuByMenuIds'")})
	public List<WebMenu> listWebMenuByMenuIds(
			List<Long> menuIds) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhWebMenusRecord> query = context.selectQuery(Tables.EH_WEB_MENUS);
		Condition cond = Tables.EH_WEB_MENUS.STATUS.eq(WebMenuStatus.ACTIVE.getCode());
		cond = cond.and(Tables.EH_WEB_MENUS.ID.in(menuIds));
		query.addConditions(cond);
		query.addOrderBy(Tables.EH_WEB_MENUS.SORT_NUM);
		return query.fetch().map((r) -> {
			return ConvertHelper.convert(r, WebMenu.class);
		});
	}
	
	@Override
	//@Caching(evict = {@CacheEvict(value="listWebMenuByType", key="'webMenuByMenuIds'")})
	public List<WebMenuScope> listWebMenuScopeByOwnerId(String ownerType, Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhWebMenuScopesRecord> query = context.selectQuery(Tables.EH_WEB_MENU_SCOPES);
		Condition cond = Tables.EH_WEB_MENU_SCOPES.OWNER_TYPE.eq(ownerType);
		cond = cond.and(Tables.EH_WEB_MENU_SCOPES.OWNER_ID.eq(ownerId));
		query.addConditions(cond);
//		query.addGroupBy(Tables.EH_WEB_MENU_SCOPES.MENU_ID);
		return query.fetch().map((r) -> {
			return ConvertHelper.convert(r, WebMenuScope.class);
		});
	}

	@Override
	//@Caching(evict={@CacheEvict(value="ListWebMenuByPrivilegeIds", key="webMenuByPrivilegeIds")})
	public List<WebMenuPrivilege> listWebMenuPrivilegeByMenuId(Long menuId) {
		List<WebMenuPrivilege> results = new ArrayList<>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhWebMenuPrivilegesRecord> query = context.selectQuery(Tables.EH_WEB_MENU_PRIVILEGES);
		Condition cond = Tables.EH_WEB_MENU_PRIVILEGES.MENU_ID.in(menuId);
		query.addConditions(cond);
	    query.fetch().map((r) -> {
			results.add(ConvertHelper.convert(r, WebMenuPrivilege.class));
			return null;
		});

		return results;
	}
}
