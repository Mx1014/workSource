// @formatter:off
package com.everhomes.launchpad;

import java.util.ArrayList;
import java.util.List;

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
import com.everhomes.server.schema.tables.pojos.EhLaunchPadItems;
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
//    @Caching(evict = { @CacheEvict(value="LaunchPadItem", key="#item.id"), 
//        @CacheEvict(value="LaunchPadItemList", key="{#item.scopeType, #item.scopeId}"),
//        @CacheEvict(value="LaunchPadItemByItemGroupList", key="#item.itemGroup")})
    @Override
    public void updateLaunchPadItem(LaunchPadItem item) {
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        EhLaunchPadItemsDao dao = new EhLaunchPadItemsDao(context.configuration()); 
        dao.update(item);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhLaunchPadItems.class, null);
        
    }
//    @Caching(evict = { @CacheEvict(value="LaunchPadItem", key="#item.id"), 
//            @CacheEvict(value="LaunchPadItemList", key="{#item.scopeType, #item.scopeId}"),
//            @CacheEvict(value="LaunchPadItemByItemGroupList", key="#item.itemGroup") })
    @Override
    public void deleteLaunchPadItem(LaunchPadItem item) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        EhLaunchPadItemsDao dao = new EhLaunchPadItemsDao(context.configuration()); 
        dao.delete(item);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhLaunchPadItems.class, null);
        
    }
//    @Caching(evict = { @CacheEvict(value="LaunchPadItem", key="#id"), 
//            @CacheEvict(value="LaunchPadItemList", key="{#item.scopeType, #item.scopeId}"),
//            @CacheEvict(value="LaunchPadItemByItemGroupList", key="#item.itemGroup") })
    @Override
    public void deleteLaunchPadItem(long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        EhLaunchPadItemsDao dao = new EhLaunchPadItemsDao(context.configuration()); 
        dao.deleteById(id);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhLaunchPadItems.class, null);
        
    }
    //@Cacheable(value="LaunchPadItem", key="#id")
    @Override
    public LaunchPadItem findLaunchPadItemById(long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(LaunchPadItem.class, id));
        EhLaunchPadItemsDao dao = new EhLaunchPadItemsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), LaunchPadItem.class);
    }

    //@Cacheable(value="LaunchPadItemList", key="{#scopeType, #scopeId}")
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

    //@Cacheable(value="LaunchPadItemByItemGroupList", key="#itemGroup")
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
//        if(scopeType != null && !scopeType.trim().equals(""))
//            condition.and(Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_TYPE.eq(scopeType));
//        if(scopeId != 0)
//            condition.and(Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_ID.eq(scopeId));
        
        step.where(condition).fetch().map((r) ->{
            items.add(ConvertHelper.convert(r, LaunchPadItem.class));
            return null;
        });
        
        return items;
    }

}
