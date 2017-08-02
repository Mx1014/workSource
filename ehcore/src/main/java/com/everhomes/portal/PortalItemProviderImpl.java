// @formatter:off
package com.everhomes.portal;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.portal.PortalItemActionType;
import com.everhomes.rest.portal.PortalItemStatus;
import com.everhomes.server.schema.tables.records.EhPortalItemsRecord;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPortalItemsDao;
import com.everhomes.server.schema.tables.pojos.EhPortalItems;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.springframework.util.StringUtils;

@Component
public class PortalItemProviderImpl implements PortalItemProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPortalItem(PortalItem portalItem) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPortalItems.class));
		portalItem.setId(id);
		portalItem.setName(EhPortalItems.class.getSimpleName() + id);
		portalItem.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		portalItem.setUpdateTime(portalItem.getCreateTime());
		getReadWriteDao().insert(portalItem);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPortalItems.class, null);
	}

	@Override
	public void updatePortalItem(PortalItem portalItem) {
		assert (portalItem.getId() != null);
		portalItem.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		getReadWriteDao().update(portalItem);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPortalItems.class, portalItem.getId());
	}

	@Override
	public PortalItem findPortalItemById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), PortalItem.class);
	}

	@Override
	public List<PortalItem> listPortalItems(Long itemCategoryId, Long itemGroupId){
		return listPortalItems(itemCategoryId, null, null, itemGroupId);
	}

	@Override
	public List<PortalItem> listPortalItemByCategoryId(Long itemCategoryId){
		return listPortalItems(itemCategoryId, null, null, null);
	}

	@Override
	public List<PortalItem> listPortalItemByGroupId(Long itemGroupId){
		return listPortalItems(null, null, null, itemGroupId);
	}

	@Override
	public List<PortalItem> listPortalItems(Long itemCategoryId, Integer namespaceId, String actionType, Long itemGroupId) {
		Condition cond = Tables.EH_PORTAL_ITEMS.STATUS.ne(PortalItemStatus.INACTIVE.getCode());
		if(null != itemCategoryId){
			if(0L == itemCategoryId)
				cond = cond.and(Tables.EH_PORTAL_ITEMS.ITEM_CATEGORY_ID.eq(itemCategoryId).or(Tables.EH_PORTAL_ITEMS.ITEM_CATEGORY_ID.isNull()));
			else
				cond = cond.and(Tables.EH_PORTAL_ITEMS.ITEM_CATEGORY_ID.eq(itemCategoryId));
		}
		if(null != namespaceId){
			cond = cond.and(Tables.EH_PORTAL_ITEMS.NAMESPACE_ID.eq(namespaceId));
		}

		if(null != PortalItemActionType.fromCode(actionType)){
			cond = cond.and(Tables.EH_PORTAL_ITEMS.ACTION_TYPE.eq(actionType));
		}

		if(null != itemGroupId){
			cond = cond.and(Tables.EH_PORTAL_ITEMS.ITEM_GROUP_ID.eq(itemGroupId));
		}

		return getReadOnlyContext().select().from(Tables.EH_PORTAL_ITEMS)
				.where(Tables.EH_PORTAL_ITEMS.STATUS.ne(PortalItemStatus.INACTIVE.getCode()))
				.and(cond)
				.orderBy(Tables.EH_PORTAL_ITEMS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, PortalItem.class));
	}

	@Override
	public PortalItem getPortalItemByGroupNameAndName(Integer namespaceId, String location, String groupName, String name, Long itemGroupId){
		List<PortalItem> portalItems = listPortalItems(namespaceId, location, groupName, name, itemGroupId);
		if(portalItems.size() > 0){
			return portalItems.get(0);
		}
		return null;
	}

	private List<PortalItem> listPortalItems(Integer  namespaceId, String location, String groupName, String name, Long itemGroupId) {
		Condition cond = Tables.EH_PORTAL_ITEMS.STATUS.ne(PortalItemStatus.INACTIVE.getCode());
		if(null != namespaceId){
			cond = cond.and(Tables.EH_PORTAL_ITEMS.NAMESPACE_ID.eq(namespaceId));
		}

		if(!StringUtils.isEmpty(location)){
			cond = cond.and(Tables.EH_PORTAL_ITEMS.ITEM_LOCATION.eq(location));
		}

		if(!StringUtils.isEmpty(groupName)){
			cond = cond.and(Tables.EH_PORTAL_ITEMS.GROUP_NAME.eq(groupName));
		}

		if(!StringUtils.isEmpty(name)){
			cond = cond.and(Tables.EH_PORTAL_ITEMS.NAME.eq(name));
		}

		if(null != itemGroupId){
			cond = cond.and(Tables.EH_PORTAL_ITEMS.ITEM_GROUP_ID.eq(itemGroupId));
		}

		return getReadOnlyContext().select().from(Tables.EH_PORTAL_ITEMS)
				.where(Tables.EH_PORTAL_ITEMS.STATUS.ne(PortalItemStatus.INACTIVE.getCode()))
				.and(cond)
				.orderBy(Tables.EH_PORTAL_ITEMS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, PortalItem.class));
	}


	@Override
	public List<PortalItem> listPortalItems(CrossShardListingLocator locator, Integer pageSize, ListingQueryBuilderCallback queryBuilderCallback) {
		List<PortalItem> results = new ArrayList<>();
		pageSize = pageSize + 1;
		SelectQuery<Record> query = getReadOnlyContext().selectQuery();
		query.addFrom(Tables.EH_PORTAL_CONTENT_SCOPES);
		query.addJoin(Tables.EH_PORTAL_ITEMS, JoinType.LEFT_OUTER_JOIN, Tables.EH_PORTAL_CONTENT_SCOPES.CONTENT_TYPE.eq(EntityType.PORTAL_ITEM.getCode()).and(Tables.EH_PORTAL_CONTENT_SCOPES.CONTENT_ID.eq(Tables.EH_PORTAL_ITEMS.ID)));
		if(null != queryBuilderCallback)
			queryBuilderCallback.buildCondition(locator, query);
		if(null != locator && null != locator.getAnchor())
			query.addConditions(Tables.EH_PORTAL_ITEMS.ID.lt(locator.getAnchor()));
		query.addConditions(Tables.EH_PORTAL_ITEMS.STATUS.ne(PortalItemStatus.INACTIVE.getCode()));
		query.addGroupBy(Tables.EH_PORTAL_ITEMS.ID);
		query.addOrderBy(Tables.EH_PORTAL_ITEMS.ID.desc());
		query.addLimit(pageSize);
		query.fetch().map((r) -> {
			PortalItem item = new PortalItem();
			item.setId(r.getValue(Tables.EH_PORTAL_ITEMS.ID));
			item.setLabel(r.getValue(Tables.EH_PORTAL_ITEMS.LABEL));
			item.setActionType(r.getValue(Tables.EH_PORTAL_ITEMS.ACTION_TYPE));
			item.setActionData(r.getValue(Tables.EH_PORTAL_ITEMS.ACTION_DATA));
			item.setStatus(r.getValue(Tables.EH_PORTAL_ITEMS.STATUS));
			item.setDisplayFlag(r.getValue(Tables.EH_PORTAL_ITEMS.DISPLAY_FLAG));
			item.setOperatorUid(r.getValue(Tables.EH_PORTAL_ITEMS.OPERATOR_UID));
			item.setCreatorUid(r.getValue(Tables.EH_PORTAL_ITEMS.CREATOR_UID));
			item.setCreateTime(r.getValue(Tables.EH_PORTAL_ITEMS.CREATE_TIME));
			item.setUpdateTime(r.getValue(Tables.EH_PORTAL_ITEMS.UPDATE_TIME));
			item.setIconUri(r.getValue(Tables.EH_PORTAL_ITEMS.ICON_URI));
			item.setSelectedIconUri(r.getValue(Tables.EH_PORTAL_ITEMS.SELECTED_ICON_URI));
			item.setDefaultOrder(r.getValue(Tables.EH_PORTAL_ITEMS.DEFAULT_ORDER));
			item.setMoreOrder(r.getValue(Tables.EH_PORTAL_ITEMS.MORE_ORDER));
			item.setItemCategoryId(r.getValue(Tables.EH_PORTAL_ITEMS.ITEM_CATEGORY_ID));
			item.setDescription(r.getValue(Tables.EH_PORTAL_ITEMS.DESCRIPTION));
			item.setItemHeight(r.getValue(Tables.EH_PORTAL_ITEMS.ITEM_HEIGHT));
			item.setItemWidth(r.getValue(Tables.EH_PORTAL_ITEMS.ITEM_WIDTH));
			results.add(item);
			return null;
		});
		if(null!= locator)
			locator.setAnchor(null);

		if(results.size() >= pageSize){
			results.remove(results.size() - 1);
			locator.setAnchor(results.get(results.size() - 1).getId());
		}
		return results;
	}
	
	private EhPortalItemsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhPortalItemsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhPortalItemsDao getDao(DSLContext context) {
		return new EhPortalItemsDao(context.configuration());
	}

	private DSLContext getReadWriteContext() {
		return getContext(AccessSpec.readWrite());
	}

	private DSLContext getReadOnlyContext() {
		return getContext(AccessSpec.readOnly());
	}

	private DSLContext getContext(AccessSpec accessSpec) {
		return dbProvider.getDslContext(accessSpec);
	}


}
