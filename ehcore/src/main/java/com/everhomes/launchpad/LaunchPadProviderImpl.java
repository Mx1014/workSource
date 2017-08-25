// @formatter:off
package com.everhomes.launchpad;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.launchpad.ItemServiceCategryStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhItemServiceCategriesDao;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.EhItemServiceCategriesRecord;
import com.everhomes.server.schema.tables.records.EhLaunchPadItemsRecord;
import org.jooq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.rest.common.ScopeType;
import com.everhomes.rest.launchpad.ItemServiceCategryStatus;
import com.everhomes.rest.launchpad.LaunchPadLayoutDTO;
import com.everhomes.rest.launchpad.LaunchPadLayoutStatus;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhItemServiceCategriesDao;
import com.everhomes.server.schema.tables.daos.EhLaunchPadItemsDao;
import com.everhomes.server.schema.tables.daos.EhLaunchPadLayoutsDao;
import com.everhomes.server.schema.tables.daos.EhUserLaunchPadItemsDao;
import com.everhomes.server.schema.tables.pojos.EhItemServiceCategries;
import com.everhomes.server.schema.tables.pojos.EhLaunchPadItems;
import com.everhomes.server.schema.tables.pojos.EhLaunchPadLayouts;
import com.everhomes.server.schema.tables.pojos.EhUserLaunchPadItems;
import com.everhomes.server.schema.tables.records.EhItemServiceCategriesRecord;
import com.everhomes.util.ConvertHelper;
import org.jooq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.everhomes.util.ConvertHelper;
import org.springframework.util.StringUtils;

@Component
public class LaunchPadProviderImpl implements LaunchPadProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(LaunchPadProvider.class);

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createLaunchPadItem(LaunchPadItem item) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhLaunchPadItems.class));
		item.setId(id);
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
	//    @Cacheable(value="LaunchPadItem", key="#id", unless="#result == null")
	@Override
	public LaunchPadItem findLaunchPadItemById(long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(LaunchPadItem.class, id));
		EhLaunchPadItemsDao dao = new EhLaunchPadItemsDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), LaunchPadItem.class);
	}

	//    @Cacheable(value="LaunchPadItemList", key="{#scopeType, #scopeId}", unless="#result.size() == 0")
	@Override
	public List<LaunchPadItem> listLaunchPadItemsByScopeTypeAndScopeId(Byte scopeCode, long scopeId) {
		List<LaunchPadItem> items = new ArrayList<LaunchPadItem>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLaunchPadItems.class));
		SelectJoinStep<Record> step = context.select().from(Tables.EH_LAUNCH_PAD_ITEMS);
		Condition condition = Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_CODE.eq(scopeCode.byteValue());
		condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_ID.eq(scopeId));

		step.where(condition).fetch().map((r) ->{
			items.add(ConvertHelper.convert(r, LaunchPadItem.class));
			return null;
		});

		return items;
	}

	@Override
	public void createLaunchPadItems(List<LaunchPadItem> items) {
		Long id = sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhLaunchPadItems.class), (long)items.size());
		List<EhLaunchPadItems> padItems = new ArrayList<>();
		for (LaunchPadItem item: items) {
			id ++;
			item.setId(id);
			padItems.add(ConvertHelper.convert(item, EhLaunchPadItems.class));
		}
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhLaunchPadItemsDao dao = new EhLaunchPadItemsDao(context.configuration());
		dao.insert(padItems);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhLaunchPadItems.class, null); 

	}

	@Override
	public void createLaunchPadLayout(LaunchPadLayout launchPadLayout){
		assert(launchPadLayout != null);

		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhLaunchPadLayouts.class));
		launchPadLayout.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhLaunchPadLayoutsDao dao = new EhLaunchPadLayoutsDao(context.configuration());
		dao.insert(launchPadLayout);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhLaunchPadLayouts.class, null); 
	}

	@Override
	public void updateLaunchPadLayout(LaunchPadLayout launchPadLayout){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		EhLaunchPadLayoutsDao dao = new EhLaunchPadLayoutsDao(context.configuration()); 
		dao.update(launchPadLayout);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhLaunchPadLayouts.class, null);
	}

	@Override
	public LaunchPadLayout findLaunchPadLayoutById(long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(LaunchPadLayout.class, id));
		EhLaunchPadLayoutsDao dao = new EhLaunchPadLayoutsDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), LaunchPadLayout.class);
	}

	@Override
	public List<LaunchPadLayout> findLaunchPadItemsByVersionCode(Integer namespaceId, String sceneType, String name,long versionCode, ScopeType scopeType, Long scopeId) {
		List<LaunchPadLayout> layouts = new ArrayList<LaunchPadLayout>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLaunchPadLayouts.class));
		SelectJoinStep<Record> step = context.select().from(Tables.EH_LAUNCH_PAD_LAYOUTS);
		Condition condition = Tables.EH_LAUNCH_PAD_LAYOUTS.NAME.eq(name);
		if(versionCode != 0){
			condition = condition.and(Tables.EH_LAUNCH_PAD_LAYOUTS.MIN_VERSION_CODE.lessOrEqual(versionCode));
		}

        condition = condition.and(Tables.EH_LAUNCH_PAD_LAYOUTS.NAMESPACE_ID.eq(namespaceId));
        condition = condition.and(Tables.EH_LAUNCH_PAD_LAYOUTS.SCENE_TYPE.eq(sceneType));
		condition = condition.and(Tables.EH_LAUNCH_PAD_LAYOUTS.STATUS.eq(LaunchPadLayoutStatus.ACTIVE.getCode()));
		condition = condition.and(Tables.EH_LAUNCH_PAD_LAYOUTS.SCOPE_CODE.eq(scopeType.getCode()));
		condition = condition.and(Tables.EH_LAUNCH_PAD_LAYOUTS.SCOPE_ID.eq(scopeId));
		step.where(condition).orderBy(Tables.EH_LAUNCH_PAD_LAYOUTS.VERSION_CODE.desc()).fetch().map((r) ->{
			layouts.add(ConvertHelper.convert(r, LaunchPadLayout.class));
			return null;
		});        
		
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query launch pad items by version code, sql=" + step.getSQL());
            LOGGER.debug("Query launch pad items by version code, bindValues=" + step.getBindValues());
        }

		return layouts;
	}

	@Override
	public List<LaunchPadItem> listLaunchPadItemsByScopeType(Integer namespaceId, String itemLocation,String itemGroup, Byte applyPolicy, ListingQueryBuilderCallback queryBuilderCallback){
		List<LaunchPadItem> items = new ArrayList<LaunchPadItem>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLaunchPadItems.class));
		SelectQuery<EhLaunchPadItemsRecord> query = context.selectQuery(Tables.EH_LAUNCH_PAD_ITEMS);
		Condition condition = Tables.EH_LAUNCH_PAD_ITEMS.ITEM_GROUP.eq(itemGroup);
		condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.ITEM_LOCATION.eq(itemLocation));
		condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.NAMESPACE_ID.eq(namespaceId));
		condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.APPLY_POLICY.eq(applyPolicy));
		if(null != queryBuilderCallback)
			queryBuilderCallback.buildCondition(null, query);
		query.addConditions(condition);
		query.fetch().map((r) ->{
			items.add(ConvertHelper.convert(r, LaunchPadItem.class));
			return null;
		});

		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("Query launch pad items by tag and scope, sql=" + query.getSQL());
			LOGGER.debug("Query launch pad items by tag and scope, bindValues=" + query.getBindValues());
		}
		return items;
	}

	@Override
	public List<LaunchPadItem> findLaunchPadItemsByTagAndScope(Integer namespaceId, String sceneType, String itemLocation,String itemGroup,Byte scopeCode,long scopeId,List<String> tags){
		return findLaunchPadItemsByTagAndScope(namespaceId, sceneType, itemLocation, itemGroup, scopeCode, scopeId, tags, null);
	}

	@Override
	public List<LaunchPadItem> findLaunchPadItemsByTagAndScope(Integer namespaceId, String sceneType, String itemLocation,String itemGroup,Byte scopeCode,long scopeId,List<String> tags, String categryName){
		List<LaunchPadItem> items = new ArrayList<LaunchPadItem>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLaunchPadItems.class));
		SelectJoinStep<Record> step = context.select().from(Tables.EH_LAUNCH_PAD_ITEMS);

		Condition condition = Tables.EH_LAUNCH_PAD_ITEMS.ITEM_GROUP.eq(itemGroup);
		condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.ITEM_LOCATION.eq(itemLocation));
		if(scopeCode != null){
			condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_CODE.eq(scopeCode.byteValue()));
			condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_ID.eq(scopeId));
		}
		if(tags != null && !tags.isEmpty()){
		    condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.TAG.in(tags));
		}else{
		    condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.TAG.isNull().or(Tables.EH_LAUNCH_PAD_ITEMS.TAG.eq("")));
		}
        condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.NAMESPACE_ID.eq(namespaceId));
        condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.SCENE_TYPE.eq(sceneType));
		if(!StringUtils.isEmpty(categryName))
			condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.CATEGRY_NAME.eq(categryName));
		step.where(condition).fetch().map((r) ->{
			items.add(ConvertHelper.convert(r, LaunchPadItem.class));
			return null;
		});       
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query launch pad items by tag and scope, sql=" + step.getSQL());
            LOGGER.debug("Query launch pad items by tag and scope, bindValues=" + step.getBindValues());
        }

		return items;
	}

	@Override
	public List<LaunchPadItem> listLaunchPadItemsByItemGroup(Integer namespaceId, String itemLocation,String itemGroup){
		List<LaunchPadItem> items = new ArrayList<LaunchPadItem>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLaunchPadItems.class));
		SelectJoinStep<Record> step = context.select().from(Tables.EH_LAUNCH_PAD_ITEMS);

		Condition condition = Tables.EH_LAUNCH_PAD_ITEMS.ITEM_GROUP.eq(itemGroup);
		condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.ITEM_LOCATION.eq(itemLocation));
		condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.NAMESPACE_ID.eq(namespaceId));
		step.where(condition).fetch().map((r) ->{
			items.add(ConvertHelper.convert(r, LaunchPadItem.class));
			return null;
		});

		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("Query launch pad items by tag and scope, sql=" + step.getSQL());
			LOGGER.debug("Query launch pad items by tag and scope, bindValues=" + step.getBindValues());
		}

		return items;
	}

	@Override
	public List<LaunchPadItem> getLaunchPadItemsByKeyword(String keyword, int offset, int pageSize) {

		List<LaunchPadItem> items = new ArrayList<LaunchPadItem>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLaunchPadItems.class));
		SelectJoinStep<Record> step = context.select().from(Tables.EH_LAUNCH_PAD_ITEMS);
		Condition condition = null;
		if(keyword != null && !keyword.trim().equals("")){
			condition = Tables.EH_LAUNCH_PAD_ITEMS.ITEM_NAME.like("%" + keyword + "%");
			condition = condition.or(Tables.EH_LAUNCH_PAD_ITEMS.ITEM_LABEL.like("%" + keyword + "%"));
		}
		if(condition != null)
			step.where(condition);

		step.limit(pageSize).offset(offset).fetch().map((r) ->{
			items.add(ConvertHelper.convert(r, LaunchPadItem.class));
			return null;
		});

		return items;
	}
	
	@Override
	public List<LaunchPadItem> searchLaunchPadItemsByKeyword(Integer namespaceId, String sceneType, Map<Byte, Long> scopeMap, String keyword, int offset, int pageSize) {
		
		List<LaunchPadItem> items = new ArrayList<LaunchPadItem>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLaunchPadItems.class));
		SelectJoinStep<Record> step = context.select().from(Tables.EH_LAUNCH_PAD_ITEMS);
		
//		Condition condition = Tables.EH_LAUNCH_PAD_ITEMS.ITEM_GROUP.eq(ItemGroup.BIZS.getCode());
		Condition condition = Tables.EH_LAUNCH_PAD_ITEMS.NAMESPACE_ID.eq(namespaceId);
        condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.SCENE_TYPE.eq(sceneType));
//		Condition condition = Tables.EH_LAUNCH_PAD_ITEMS.ITEM_LOCATION.eq("/home");
		
		Condition scopeConditionAll = null;
		for(Map.Entry<Byte, Long> entry: scopeMap.entrySet()){
			if(entry.getValue() == null){
				continue;
			}
			Condition scopeCondition = Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_CODE.eq(entry.getKey());
			scopeCondition = scopeCondition.and(Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_ID.eq(entry.getValue()));
			if(scopeConditionAll == null){
				scopeConditionAll = scopeCondition;
			}else{
				scopeConditionAll = scopeConditionAll.or(scopeCondition);
			}
		}
		
//		Condition scopeCondition
//		Condition scopeConditionAll = Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_CODE.eq(ScopeType.ALL.getCode());
//		scopeConditionAll = scopeConditionAll.and(Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_ID.eq(0L));
//		if(scopeCode != null && scopeId != null){
//			Condition scopeCondition = Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_CODE.eq(scopeCode);
//			scopeCondition = scopeCondition.and(Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_ID.eq(scopeId));
//			scopeConditionAll = scopeConditionAll.or(scopeCondition);
//		}
		
		
		condition = condition.and(scopeConditionAll);
		
        
        
		
		if(keyword != null && !keyword.trim().equals("")){
			Condition keyCondition = Tables.EH_LAUNCH_PAD_ITEMS.ITEM_NAME.like("%" + keyword + "%");
			keyCondition = keyCondition.or(Tables.EH_LAUNCH_PAD_ITEMS.ITEM_LABEL.like("%" + keyword + "%"));
			condition = condition.and(keyCondition);
		}
		if(condition != null)
			step.where(condition);

		step.limit(pageSize).offset(offset).fetch().map((r) ->{
			items.add(ConvertHelper.convert(r, LaunchPadItem.class));
			return null;
		});

		return items;
	}
	
	@Override
	public List<LaunchPadLayoutDTO> listLaunchPadLayoutByKeyword(int pageSize,long offset,String keyword) {

		List<LaunchPadLayoutDTO> list = new ArrayList<LaunchPadLayoutDTO>();

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLaunchPadLayouts.class));
		SelectJoinStep<Record> query = context.select().from(Tables.EH_LAUNCH_PAD_LAYOUTS);
		
		Condition condition = null;
		if(keyword != null && !keyword.equals("")){
			condition = Tables.EH_LAUNCH_PAD_LAYOUTS.NAME.like("%" + keyword + "%");
		}
		if(condition != null){
			query.where(condition);
		}
		
		query.limit(pageSize).offset((int)offset)
		.fetch().map(r -> {
			list.add(ConvertHelper.convert(r,LaunchPadLayoutDTO.class));
			return null;
		});

		return list;
	}

	@Override
	public List<LaunchPadLayout> getLaunchPadLayouts(String name, Integer namespaceId) {
		List<LaunchPadLayout> list = new ArrayList<>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLaunchPadLayouts.class));
		SelectJoinStep<Record> query = context.select().from(Tables.EH_LAUNCH_PAD_LAYOUTS);
		if(null != namespaceId)
			query.where(Tables.EH_LAUNCH_PAD_LAYOUTS.NAMESPACE_ID.eq(namespaceId));
		if(!StringUtils.isEmpty(name)){
			query.where(Tables.EH_LAUNCH_PAD_LAYOUTS.NAME.eq(name));
		}
		query.fetch().map(r -> {
			list.add(ConvertHelper.convert(r,LaunchPadLayout.class));
			return null;
		});
		return list;
	}

    @Override
    public List<LaunchPadItem> findLaunchPadItemByTargetAndScope(String targetType, long targetId,Byte scopeCode, long scopeId,Integer namesapceId) {
        List<LaunchPadItem> items = new ArrayList<LaunchPadItem>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLaunchPadItems.class));
        SelectJoinStep<Record> step = context.select().from(Tables.EH_LAUNCH_PAD_ITEMS);

        Condition condition = Tables.EH_LAUNCH_PAD_ITEMS.NAMESPACE_ID.eq(namesapceId);
        if(scopeCode != null){
            condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_CODE.eq(scopeCode));
            if(scopeId != 0)
                condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_ID.eq(scopeId));
        }
        
        if(targetType != null){
            if(condition != null){
                condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.TARGET_TYPE.eq(targetType));
                if(targetId != 0) 
                    condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.TARGET_ID.eq("" + targetId));
            }else{
                condition = Tables.EH_LAUNCH_PAD_ITEMS.TARGET_TYPE.eq(targetType);
                if(targetId != 0)
                    condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.TARGET_ID.eq("" + targetId));
            }
        }
       
        step.where(condition).fetch().map(r ->{
            items.add(ConvertHelper.convert(r, LaunchPadItem.class));
            return null;
        });
        
        return items;
    }
    
    @Override
    public LaunchPadItem findLaunchPadItemByTargetAndScopeAndSence(String targetType, long targetId,Byte scopeCode, long scopeId,Integer namesapceId, SceneType sceneType){
    	 List<LaunchPadItem> items = new ArrayList<LaunchPadItem>();
         DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLaunchPadItems.class));
         SelectJoinStep<Record> step = context.select().from(Tables.EH_LAUNCH_PAD_ITEMS);

         Condition condition = Tables.EH_LAUNCH_PAD_ITEMS.NAMESPACE_ID.eq(namesapceId);
         condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.SCENE_TYPE.eq(sceneType.getCode()));
         if(scopeCode != null){
             condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_CODE.eq(scopeCode));
             if(scopeId != 0)
                 condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_ID.eq(scopeId));
         }
         
         if(targetType != null){
             if(condition != null){
                 condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.TARGET_TYPE.eq(targetType));
                 if(targetId != 0) 
                     condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.TARGET_ID.eq("" + targetId));
             }else{
                 condition = Tables.EH_LAUNCH_PAD_ITEMS.TARGET_TYPE.eq(targetType);
                 if(targetId != 0)
                     condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.TARGET_ID.eq("" + targetId));
             }
         }
        
         step.where(condition).fetch().map(r ->{
             items.add(ConvertHelper.convert(r, LaunchPadItem.class));
             return null;
         });
         
         if(items.size() > 0){
        	 return items.get(0);
         }
         return null;
    }
    
    @Override
    public void deleteLaunchPadItemByTargetTypeAndTargetId(String targetType, long targetId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLaunchPadItems.class));
        Condition condition = Tables.EH_LAUNCH_PAD_ITEMS.TARGET_TYPE.eq(targetType);
        condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.TARGET_ID.eq("" + targetId));
        context.delete(Tables.EH_LAUNCH_PAD_ITEMS).where(condition).execute();
        
    }

    @Override
    public void deleteLaunchPadItemByScopeAndTargetId(Byte scopeCode,Long scopeId,String targetType, long targetId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLaunchPadItems.class));
        Condition condition = Tables.EH_LAUNCH_PAD_ITEMS.TARGET_TYPE.eq(targetType);
        condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.TARGET_ID.eq("" + targetId));
        if(scopeCode != null){
            condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_CODE.eq(scopeCode));
            if(scopeId != null)
                condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_ID.eq(scopeId));
        }
        context.delete(Tables.EH_LAUNCH_PAD_ITEMS).where(condition).execute();
        
    }
	@Override
	public List<LaunchPadItem> findLaunchPadItem(Integer nameSpaceId,String itemGroup,String itemName, Byte scopeCode, long scopeId) {
		List<LaunchPadItem> items = new ArrayList<LaunchPadItem>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLaunchPadItems.class));
        SelectJoinStep<Record> step = context.select().from(Tables.EH_LAUNCH_PAD_ITEMS);

        Condition condition = Tables.EH_LAUNCH_PAD_ITEMS.ITEM_GROUP.eq(itemGroup).and(Tables.EH_LAUNCH_PAD_ITEMS.ITEM_NAME.eq(itemName));
        if(nameSpaceId!=null)
        	condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.NAMESPACE_ID.eq(nameSpaceId));
        if(scopeCode != null){
            condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_CODE.eq(scopeCode));
            if(scopeId != 0)
                condition = condition.and(Tables.EH_LAUNCH_PAD_ITEMS.SCOPE_ID.eq(scopeId));
        }
       
        step.where(condition).fetch().map(r ->{
            items.add(ConvertHelper.convert(r, LaunchPadItem.class));
            return null;
        });
        
        return items;
	}
	
	@Override
	public List<UserLaunchPadItem> findUserLaunchPadItemByUserId(Long userId, String sceneType, String ownerType, Long ownerId) {
		List<UserLaunchPadItem> items = new ArrayList<UserLaunchPadItem>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUserLaunchPadItems.class));
        SelectJoinStep<Record> step = context.select().from(Tables.EH_USER_LAUNCH_PAD_ITEMS);

        Condition condition = Tables.EH_USER_LAUNCH_PAD_ITEMS.USER_ID.eq(userId);
        condition = condition.and(Tables.EH_USER_LAUNCH_PAD_ITEMS.OWNER_TYPE.eq(ownerType));
        condition = condition.and(Tables.EH_USER_LAUNCH_PAD_ITEMS.OWNER_ID.eq(ownerId));
        condition = condition.and(Tables.EH_USER_LAUNCH_PAD_ITEMS.SCENE_TYPE.eq(sceneType));
       
        step.where(condition).fetch().map(r ->{
            items.add(ConvertHelper.convert(r, UserLaunchPadItem.class));
            return null;
        });
        
        return items;
	}
	
	@Override
	public UserLaunchPadItem getUserLaunchPadItemByOwner(Long userId, String sceneType, String ownerType, Long ownerId, String itemName) {
		List<UserLaunchPadItem> items = new ArrayList<UserLaunchPadItem>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUserLaunchPadItems.class));
        SelectJoinStep<Record> step = context.select().from(Tables.EH_USER_LAUNCH_PAD_ITEMS);

        Condition condition = Tables.EH_USER_LAUNCH_PAD_ITEMS.USER_ID.eq(userId);
        condition = condition.and(Tables.EH_USER_LAUNCH_PAD_ITEMS.OWNER_TYPE.eq(ownerType));
        condition = condition.and(Tables.EH_USER_LAUNCH_PAD_ITEMS.OWNER_ID.eq(ownerId));
        condition = condition.and(Tables.EH_USER_LAUNCH_PAD_ITEMS.SCENE_TYPE.eq(sceneType));
        condition = condition.and(Tables.EH_USER_LAUNCH_PAD_ITEMS.ITEM_NAME.eq(itemName));
       
        step.where(condition).fetch().map(r ->{
            items.add(ConvertHelper.convert(r, UserLaunchPadItem.class));
            return null;
        });
        
        if(items.size() > 0){
        	return items.get(0);
        }
        
        return null;
	}
	
	@Override
	public void deleteUserLaunchPadItemById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhUserLaunchPadItemsDao dao = new EhUserLaunchPadItemsDao(context.configuration()); 
		dao.deleteById(id);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUserLaunchPadItems.class, null);
	}
	
	@Override
	public void createUserLaunchPadItem(UserLaunchPadItem userItem) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUserLaunchPadItems.class));
		userItem.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhUserLaunchPadItemsDao dao = new EhUserLaunchPadItemsDao(context.configuration()); 
		dao.insert(userItem); 
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhUserLaunchPadItems.class, null);
	}
	
	@Override
	public void updateUserLaunchPadItemById(UserLaunchPadItem userItem) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhUserLaunchPadItemsDao dao = new EhUserLaunchPadItemsDao(context.configuration()); 
		dao.update(userItem); 
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUserLaunchPadItems.class, null);
	}

	public List<ItemServiceCategry> listItemServiceCategries(Integer namespaceId){
		return listItemServiceCategries(namespaceId, null, null, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addGroupBy(Tables.EH_ITEM_SERVICE_CATEGRIES.NAME);
				return null;
			}
		});
	}

	@Override
	public List<ItemServiceCategry> listItemServiceCategries(Integer namespaceId, String itemLocation, String itemGroup, ListingQueryBuilderCallback callback){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		List<ItemServiceCategry> result = new ArrayList<>();
		SelectQuery<EhItemServiceCategriesRecord> query = context.selectQuery(Tables.EH_ITEM_SERVICE_CATEGRIES);
		query.addConditions(Tables.EH_ITEM_SERVICE_CATEGRIES.STATUS.eq(ItemServiceCategryStatus.ACTIVE.getCode()));
		query.addConditions(Tables.EH_ITEM_SERVICE_CATEGRIES.NAMESPACE_ID.eq(namespaceId));
		if(!StringUtils.isEmpty(itemLocation))
			query.addConditions(Tables.EH_ITEM_SERVICE_CATEGRIES.ITEM_LOCATION.eq(itemLocation));
		if(!StringUtils.isEmpty(itemGroup))
			query.addConditions(Tables.EH_ITEM_SERVICE_CATEGRIES.ITEM_GROUP.eq(itemGroup));
		if(null != callback){
			callback.buildCondition(null, query);
		}
		query.fetch().map(r -> {
			result.add(ConvertHelper.convert(r, ItemServiceCategry.class));
			return null;
		});
		return result;
	}

	@Override
	public void createItemServiceCategry(ItemServiceCategry itemServiceCategry) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhItemServiceCategries.class));
		itemServiceCategry.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhItemServiceCategriesDao dao = new EhItemServiceCategriesDao(context.configuration());
		dao.insert(itemServiceCategry);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhItemServiceCategries.class, null);
	}

    @Override
    public List<LaunchPadItem> listLaunchPadItemsByNamespaceId(Integer namespaceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_LAUNCH_PAD_ITEMS)
                .where(Tables.EH_LAUNCH_PAD_ITEMS.NAMESPACE_ID.eq(namespaceId))
                .fetchInto(LaunchPadItem.class);
    }

	@Override
	public void deleteItemServiceCategryById(Long id){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhItemServiceCategriesDao dao = new EhItemServiceCategriesDao(context.configuration());
		dao.deleteById(id);
	}
}
