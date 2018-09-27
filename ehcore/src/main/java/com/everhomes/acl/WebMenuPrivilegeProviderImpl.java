// @formatter:off
package com.everhomes.acl;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.acl.WebMenuPrivilegeShowFlag;
import com.everhomes.rest.acl.WebMenuStatus;
import com.everhomes.rest.acl.WebMenuType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhWebMenuScopesDao;
import com.everhomes.server.schema.tables.daos.EhWebMenusDao;
import com.everhomes.server.schema.tables.pojos.EhWebMenuScopes;
import com.everhomes.server.schema.tables.pojos.EhWebMenus;
import com.everhomes.server.schema.tables.records.EhWebMenuPrivilegesRecord;
import com.everhomes.server.schema.tables.records.EhWebMenuScopesRecord;
import com.everhomes.server.schema.tables.records.EhWebMenusRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;

import org.jooq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WebMenuPrivilegeProviderImpl implements WebMenuPrivilegeProvider {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebMenuPrivilegeProviderImpl.class);

	@Autowired
	private DbProvider dbProvider;
	
	 @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;
	
	@Override
	//@Caching(evict={@CacheEvict(value="listWebMenuByType", key="'webMenu'")})
	public List<WebMenu> listWebMenuByType(String type) {
		return listWebMenuByType(type, null, null, null);
	}

	@Override
	//@Caching(evict={@CacheEvict(value="listWebMenuByType", key="'webMenu'")})
	public List<WebMenu> listWebMenuByType(String type, List<String> categories, String path, List<Long> moduleIds) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhWebMenusRecord> query = context.selectQuery(Tables.EH_WEB_MENUS);
		Condition cond = Tables.EH_WEB_MENUS.STATUS.eq(WebMenuStatus.ACTIVE.getCode());
		if(null != WebMenuType.fromCode(type)){
			cond = cond.and(Tables.EH_WEB_MENUS.TYPE.eq(type));
		}

		if(null != categories && categories.size() > 0){
			cond = cond.and(Tables.EH_WEB_MENUS.CATEGORY.in(categories));
		}

		if(!StringUtils.isEmpty(path)){
			cond = cond.and(Tables.EH_WEB_MENUS.PATH.like(path));
		}

		if(moduleIds != null && moduleIds.size() > 0){
			cond = cond.and(Tables.EH_WEB_MENUS.MODULE_ID.in(moduleIds));
		}
		query.addConditions(cond);
		query.addOrderBy(Tables.EH_WEB_MENUS.SORT_NUM);
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("listWebMenuByType, sql=" + query.getSQL());
			LOGGER.debug("listWebMenuByType, bindValues=" + query.getBindValues());
		}
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
		return query.fetch().map((r) -> {
			return ConvertHelper.convert(r, WebMenuScope.class);
		});
	}

	@Override
	//@Caching(evict = {@CacheEvict(value="listWebMenuByType", key="'webMenuByMenuIds'")})
	public List<WebMenuScope> getWebMenuScopeMapByOwnerId(String ownerType, Long ownerId) {
		//Map<Long, WebMenuScope> map = new HashMap<>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhWebMenuScopesRecord> query = context.selectQuery(Tables.EH_WEB_MENU_SCOPES);
		Condition cond = Tables.EH_WEB_MENU_SCOPES.OWNER_TYPE.eq(ownerType);
		cond = cond.and(Tables.EH_WEB_MENU_SCOPES.OWNER_ID.eq(ownerId));
		query.addConditions(cond);

		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("getWebMenuScopeMapByOwnerId, sql=" + query.getSQL());
			LOGGER.debug("getWebMenuScopeMapByOwnerId, bindValues=" + query.getBindValues());
		}
		List<WebMenuScope> scopes = query.fetch().map((r) -> {
			//map.put(r.getMenuId(), ConvertHelper.convert(r, WebMenuScope.class));
			return ConvertHelper.convert(r, WebMenuScope.class);
		});
		return scopes;
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
	
	//add by Janson
	@Override
	public Long nextId() {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWebMenus.class));
		return id;
	}
	
    @Override
    public Long createWebMenu(WebMenu obj) {
        if(obj.getId() == null) {
        	obj.setId(this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWebMenus.class)));
        }
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhWebMenus.class));
        prepareObj(obj);
        EhWebMenusDao dao = new EhWebMenusDao(context.configuration());
        dao.insert(obj);
        return obj.getId();
    }
    
    @Override
    public void createWebMenus(List<WebMenu> objs) {
    	for(WebMenu obj : objs) {
    		if(obj.getId() == null) {
    			obj.setId(this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWebMenus.class)));
    		}
    		prepareObj(obj);
    	}
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhWebMenus.class));
    	EhWebMenusDao dao = new EhWebMenusDao(context.configuration());
    	dao.insert(objs.toArray(new WebMenu[objs.size()]));
    }
    
	//add by Janson
    @Override
    public void updateWebMenu(WebMenu obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhWebMenus.class));
        EhWebMenusDao dao = new EhWebMenusDao(context.configuration());
        dao.update(obj);
    }

	//add by Janson
    @Override
    public void deleteWebMenu(WebMenu obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhWebMenus.class));
        EhWebMenusDao dao = new EhWebMenusDao(context.configuration());
        dao.deleteById(obj.getId());
    }

	//add by Janson
    @Override
    public WebMenu getWebMenuById(Long id) {
        try {
        WebMenu[] result = new WebMenu[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhWebMenus.class));

        result[0] = context.select().from(Tables.EH_WEB_MENUS)
            .where(Tables.EH_WEB_MENUS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, WebMenu.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

	//add by Janson
    @Override
    public List<WebMenu> queryWebMenus(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhWebMenus.class));

        SelectQuery<EhWebMenusRecord> query = context.selectQuery(Tables.EH_WEB_MENUS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator != null && locator.getAnchor() != null) {
            query.addConditions(Tables.EH_WEB_MENUS.ID.gt(locator.getAnchor()));
		}

        query.addLimit(count);
        List<WebMenu> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, WebMenu.class);
        });

        if(objs.size() >= count && locator != null) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else if(locator != null && locator != null) {
            locator.setAnchor(null);
        }

        return objs;
    }

	//add by Janson
    private void prepareObj(WebMenu obj) {
    	obj.setStatus(WebMenuStatus.ACTIVE.getCode());
    }


	@Override
	public List<WebMenu> listWebMenus(Long parentId, String type, Byte sceneType) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhWebMenus.class));

		SelectQuery<EhWebMenusRecord> query = context.selectQuery(Tables.EH_WEB_MENUS);
		if(parentId != null){
			query.addConditions(Tables.EH_WEB_MENUS.PARENT_ID.eq(parentId));
		}

		if(sceneType != null){
			query.addConditions(Tables.EH_WEB_MENUS.SCENE_TYPE.eq(sceneType));
		}
		query.addConditions(Tables.EH_WEB_MENUS.TYPE.eq(type));
		query.addConditions(Tables.EH_WEB_MENUS.STATUS.eq(WebMenuStatus.ACTIVE.getCode()));
		query.addOrderBy(Tables.EH_WEB_MENUS.SORT_NUM.asc());

		List<WebMenu> objs = query.fetch().map((r) -> ConvertHelper.convert(r, WebMenu.class));

		return objs;
	}

	@Override
	public List<WebMenu> listWebMenusByPath(String path, List<String> types) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhWebMenus.class));

		SelectQuery<EhWebMenusRecord> query = context.selectQuery(Tables.EH_WEB_MENUS);
		query.addConditions(Tables.EH_WEB_MENUS.PATH.like(path+"%"));
		if(types != null)
			query.addConditions(Tables.EH_WEB_MENUS.TYPE.in(types));
		query.addConditions(Tables.EH_WEB_MENUS.STATUS.eq(WebMenuStatus.ACTIVE.getCode()));
		query.addOrderBy(Tables.EH_WEB_MENUS.SORT_NUM.asc());

		List<WebMenu> objs = query.fetch().map((r) -> ConvertHelper.convert(r, WebMenu.class));

		return objs;
	}

	@Override
	public void deleteWebMenuScopes(List<Long> ids) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		EhWebMenuScopesDao dao = new EhWebMenuScopesDao(context.configuration());
		dao.deleteById(ids);

	}

	@Override
	public void createWebMenuScopes(List<WebMenuScope> scopes) {
		for(WebMenuScope s: scopes){
			if(s.getId() == null){
				long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWebMenuScopes.class));
				s.setId(id);
			}
		}
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		EhWebMenuScopesDao dao = new EhWebMenuScopesDao(context.configuration());
		dao.insert(scopes.toArray(new WebMenuScope[scopes.size()]));
	}

	@Override
	public void deleteWebMenuScopesByMenuIdAndNamespace(List<Integer> socpeIds, Integer namespaceId) {
		dbProvider.getDslContext(AccessSpec.readWrite()).delete(Tables.EH_WEB_MENU_SCOPES)
			.where(Tables.EH_WEB_MENU_SCOPES.MENU_ID.in(socpeIds))
			.and(Tables.EH_WEB_MENU_SCOPES.OWNER_ID.eq(Long.valueOf(namespaceId)))
			.and(Tables.EH_WEB_MENU_SCOPES.OWNER_TYPE.eq("EhNamespaces"))
			.execute();
	}

	@Override
	public List<WebMenu> listMenuByModuleIdAndType(Long moduleId, String type) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhWebMenus.class));
		SelectQuery<EhWebMenusRecord> query = context.selectQuery(Tables.EH_WEB_MENUS);
		query.addConditions(Tables.EH_WEB_MENUS.MODULE_ID.eq(moduleId));
		query.addConditions(Tables.EH_WEB_MENUS.TYPE.eq(type));
		List<WebMenu> webMenus = query.fetch().map(r -> ConvertHelper.convert(r, WebMenu.class));
		return webMenus;
	}

	@Override
	public List<WebMenu> listMenuByTypeAndConfigType(String type, Byte configType) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhWebMenus.class));
		SelectQuery<EhWebMenusRecord> query = context.selectQuery(Tables.EH_WEB_MENUS);
		query.addConditions(Tables.EH_WEB_MENUS.CONFIG_TYPE.eq(configType));
		query.addConditions(Tables.EH_WEB_MENUS.TYPE.eq(type));
		List<WebMenu> webMenus = query.fetch().map(r -> ConvertHelper.convert(r, WebMenu.class));
		return webMenus;
	}

	@Override
	public void deleteMenuScopeByOwner(String ownerType, Long ownerId){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhWebMenuScopes.class));
		DeleteQuery query = context.deleteQuery(Tables.EH_WEB_MENU_SCOPES);
		query.addConditions(Tables.EH_WEB_MENU_SCOPES.OWNER_TYPE.eq(ownerType));
		query.addConditions(Tables.EH_WEB_MENU_SCOPES.OWNER_ID.eq(ownerId));
		query.execute();
	}
}
