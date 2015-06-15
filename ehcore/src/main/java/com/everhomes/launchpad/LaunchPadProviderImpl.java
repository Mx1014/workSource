// @formatter:off
package com.everhomes.launchpad;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhLaunchPadItemsDao;
import com.everhomes.server.schema.tables.daos.EhLaunchPadLayoutsDao;
import com.everhomes.server.schema.tables.pojos.EhLaunchPadItems;
import com.everhomes.server.schema.tables.pojos.EhLaunchPadLayouts;
import com.everhomes.util.ConvertHelper;

@Component
public class LaunchPadProviderImpl implements LaunchPadProvider {
    @Autowired
    private DbProvider dbProvider;

    @Override
    public void createLaunchPadItem(LaunchPadItem item) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        EhLaunchPadItemsDao dao = new EhLaunchPadItemsDao(context.configuration()); 
        dao.insert(item); 
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhLaunchPadItems.class, null); 
        
    }
    @Caching(evict = { @CacheEvict(value="LaunchPadItem", key="#item.id"), 
        @CacheEvict(value="LaunchPadItemList", key="{#item.scopeType, #item.scopeId}"),
        @CacheEvict(value="LaunchPadItemByItemGroupList", key="#item.itemGroup")})
    @Override
    public void updateLaunchPadItem(LaunchPadItem item) {
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        EhLaunchPadItemsDao dao = new EhLaunchPadItemsDao(context.configuration()); 
        dao.update(item);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhLaunchPadItems.class, null);
        
    }
    @Caching(evict = { @CacheEvict(value="LaunchPadItem", key="#item.id"), 
            @CacheEvict(value="LaunchPadItemList", key="{#item.scopeType, #item.scopeId}"),
            @CacheEvict(value="LaunchPadItemByItemGroupList", key="#item.itemGroup") })
    @Override
    public void deleteLaunchPadItem(LaunchPadItem item) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        EhLaunchPadItemsDao dao = new EhLaunchPadItemsDao(context.configuration()); 
        dao.delete(item);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhLaunchPadItems.class, null);
        
    }
    @Caching(evict = { @CacheEvict(value="LaunchPadItem", key="#id"), 
            @CacheEvict(value="LaunchPadItemList", key="{#item.scopeType, #item.scopeId}"),
            @CacheEvict(value="LaunchPadItemByItemGroupList", key="#item.itemGroup") })
    @Override
    public void deleteLaunchPadItem(long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        EhLaunchPadItemsDao dao = new EhLaunchPadItemsDao(context.configuration()); 
        dao.deleteById(id);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhLaunchPadItems.class, null);
        
    }
    @Cacheable(value="LaunchPadItem", key="#id", unless="#result == null")
    @Override
    public LaunchPadItem findLaunchPadItemById(long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(LaunchPadItem.class, id));
        EhLaunchPadItemsDao dao = new EhLaunchPadItemsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), LaunchPadItem.class);
    }

    @Cacheable(value="LaunchPadItemList", key="{#scopeType, #scopeId}", unless="#result.size() == 0")
    @Override
    public List<LaunchPadItem> listLaunchPadItemsByScopeTypeAndScopeId(String scopeType, long scopeId) {
        List<LaunchPadItem> items = new ArrayList<LaunchPadItem>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLaunchPadItems.class));
        SelectJoinStep<Record> step = context.select().from(Tables.EH_LAUNCH_PAD_ITEMS);
        Condition condition = Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_TYPE.eq(scopeType);
        condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_ID.eq(scopeId));
        
        step.where(condition).fetch().map((r) ->{
            items.add(ConvertHelper.convert(r, LaunchPadItem.class));
            return null;
        });
        
        return items;
    }

    @Cacheable(value="LaunchPadItemByItemGroupList", key="#itemGroup", unless="#result.size() == 0")
    @Override
    public List<LaunchPadItem> listLaunchPadItemsByItemGroup(String itemGroup) {
        List<LaunchPadItem> items = new ArrayList<LaunchPadItem>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLaunchPadItems.class));
        SelectJoinStep<Record> step = context.select().from(Tables.EH_LAUNCH_PAD_ITEMS);
        Condition condition = Tables.EH_LAUNCH_PAD_ITEMS.ITEM_GROUP.eq(itemGroup);
        
        step.where(condition).fetch().map((r) ->{
            items.add(ConvertHelper.convert(r, LaunchPadItem.class));
            return null;
        });
        
        return items;
    }
    
    @Override
    public List<LaunchPadItem> listLaunchPadItemsByAppId(long appId, String scopeType, long scopeId) {
        List<LaunchPadItem> items = new ArrayList<LaunchPadItem>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLaunchPadItems.class));
        SelectJoinStep<Record> step = context.select().from(Tables.EH_LAUNCH_PAD_ITEMS);
        Condition condition = Tables.EH_LAUNCH_PAD_ITEMS.APP_ID.eq(appId);
        condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_TYPE.eq(scopeType));
        condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_ID.eq(scopeId));
        
        step.where(condition).fetch().map((r) ->{
            items.add(ConvertHelper.convert(r, LaunchPadItem.class));
            return null;
        });
        
        return items;
    }
    
    @Override
    public List<LaunchPadItem> listLaunchPadItemsByScopeTypeAndItemNameItemGroup(
            String scopeType, String itemName, String itemGroup) {
        List<LaunchPadItem> items = new ArrayList<LaunchPadItem>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLaunchPadItems.class));
        SelectJoinStep<Record> step = context.select().from(Tables.EH_LAUNCH_PAD_ITEMS);
        Condition condition = Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_TYPE.eq(scopeType);
        condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.ITEM_NAME.eq(itemName));
        condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.ITEM_GROUP.eq(itemGroup));
        
        step.where(condition).fetch().map((r) ->{
            items.add(ConvertHelper.convert(r, LaunchPadItem.class));
            return null;
        });
        
        return items;
    }
    
    @Override
    public void createLaunchPadItems(List<LaunchPadItem> items) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        EhLaunchPadItemsDao dao = new EhLaunchPadItemsDao(context.configuration()); 
        dao.insert(items.stream().map(r->ConvertHelper.convert(r, EhLaunchPadItems.class)).collect(Collectors.toList()));
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhLaunchPadItems.class, null); 
        
    }
    
    @Override
    public void createLaunchPadLayout(LaunchPadLayout launchPadLayout){
        assert(launchPadLayout != null);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        EhLaunchPadLayoutsDao dao = new EhLaunchPadLayoutsDao(context.configuration()); 
        dao.insert(launchPadLayout);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhLaunchPadLayouts.class, null); 
    }
    
    @Override
    public List<LaunchPadLayout> findLaunchPadItemsByVersionCode(long versionCode) {
        List<LaunchPadLayout> layouts = new ArrayList<LaunchPadLayout>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLaunchPadLayouts.class));
        SelectJoinStep<Record> step = context.select().from(Tables.EH_LAUNCH_PAD_LAYOUTS);
        Condition condition = Tables.EH_LAUNCH_PAD_LAYOUTS.MIN_VERSION_CODE.lessOrEqual(versionCode);
        condition = condition.and(Tables.EH_LAUNCH_PAD_LAYOUTS.STATUS.eq(LaunchPadLayoutStatus.ACTIVE.getCode()));
        
        step.where(condition).orderBy(Tables.EH_LAUNCH_PAD_LAYOUTS.VERSION_CODE.desc()).fetch().map((r) ->{
            layouts.add(ConvertHelper.convert(r, LaunchPadLayout.class));
            return null;
        });
        
        return layouts;
    }
    @Override
    public List<LaunchPadItem> findLaunchPadItemsByServiceType(byte serviceType){
        List<LaunchPadItem> items = new ArrayList<LaunchPadItem>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLaunchPadItems.class));
        SelectJoinStep<Record> step = context.select().from(Tables.EH_LAUNCH_PAD_ITEMS);
        //TODO
        Condition condition = Tables.EH_LAUNCH_PAD_ITEMS.ITEM_GROUP.eq("");
        
        step.where(condition).fetch().map((r) ->{
            items.add(ConvertHelper.convert(r, LaunchPadItem.class));
            return null;
        });
        
        return items;
    }

}
