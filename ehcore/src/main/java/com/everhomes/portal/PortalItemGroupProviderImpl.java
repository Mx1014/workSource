// @formatter:off
package com.everhomes.portal;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.everhomes.rest.launchpad.Widget;
import com.everhomes.rest.portal.PortalItemGroupStatus;
import org.jooq.DSLContext;
import org.jooq.DeleteQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPortalItemGroupsDao;
import com.everhomes.server.schema.tables.pojos.EhPortalItemGroups;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.springframework.util.StringUtils;

@Component
public class PortalItemGroupProviderImpl implements PortalItemGroupProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(PortalItemGroupProviderImpl.class);

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPortalItemGroup(PortalItemGroup portalItemGroup) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPortalItemGroups.class));
		portalItemGroup.setId(id);
		if(StringUtils.isEmpty(portalItemGroup.getName()))
			portalItemGroup.setName(EhPortalItemGroups.class.getSimpleName() + id);
		portalItemGroup.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		portalItemGroup.setUpdateTime(portalItemGroup.getCreateTime());
		getReadWriteDao().insert(portalItemGroup);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPortalItemGroups.class, null);
	}

	@Override
	public void createPortalItemGroups(List<PortalItemGroup> portalItemGroups) {
		LOGGER.debug("create portal itemGroup size = {}", portalItemGroups.size());
		if(portalItemGroups.size() == 0){
			return;
		}

		/**
		 * 有id使用原来的id，没有则生成新的
		 */
		Long id = sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhPortalItemGroups.class), (long)portalItemGroups.size() + 1);
		List<EhPortalItemGroups> groups = new ArrayList<>();
		for (PortalItemGroup group: portalItemGroups) {
			if(group.getId() == null){
				id ++;
				group.setId(id);
			}

			if(group.getName() == null){
				group.setName(EhPortalItemGroups.class.getSimpleName() + group.getId());
			}

			group.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			group.setUpdateTime(group.getCreateTime());
			groups.add(ConvertHelper.convert(group, EhPortalItemGroups.class));
		}
		getReadWriteDao().insert(groups);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPortalItemGroups.class, null);
	}


	@Override
	public void updatePortalItemGroup(PortalItemGroup portalItemGroup) {
		assert (portalItemGroup.getId() != null);
		portalItemGroup.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		getReadWriteDao().update(portalItemGroup);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPortalItemGroups.class, portalItemGroup.getId());
	}

	@Override
	public void deleteByVersionId(Long versionId){
		DeleteQuery query = getReadWriteContext().deleteQuery(Tables.EH_PORTAL_ITEM_GROUPS);
		query.addConditions(Tables.EH_PORTAL_ITEM_GROUPS.VERSION_ID.eq(versionId));
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPortalItemGroups.class, null);
	}

	@Override
	public PortalItemGroup findPortalItemGroupById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), PortalItemGroup.class);
	}

	@Override
	public Integer findMaxDefaultOrder(Long layoutId) {
		Integer defaultOrder = getReadOnlyContext().select(Tables.EH_PORTAL_ITEM_GROUPS.DEFAULT_ORDER.max())
				.from(Tables.EH_PORTAL_ITEM_GROUPS)
				.where(Tables.EH_PORTAL_ITEM_GROUPS.LAYOUT_ID.eq(layoutId))
				.fetchOne().value1();

		return defaultOrder;
	}
	
	@Override
	public List<PortalItemGroup> listPortalItemGroup(Long layoutId) {
		return getReadOnlyContext().select().from(Tables.EH_PORTAL_ITEM_GROUPS)
				.where(Tables.EH_PORTAL_ITEM_GROUPS.STATUS.eq(PortalItemGroupStatus.ACTIVE.getCode()))
				.and(Tables.EH_PORTAL_ITEM_GROUPS.LAYOUT_ID.eq(layoutId))
				.orderBy(Tables.EH_PORTAL_ITEM_GROUPS.DEFAULT_ORDER.asc(), Tables.EH_PORTAL_ITEM_GROUPS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, PortalItemGroup.class));
	}

	@Override
	public List<PortalItemGroup> listPortalItemGroupByVersion(Integer namespaceId, Long versionId) {
		return getReadOnlyContext().select().from(Tables.EH_PORTAL_ITEM_GROUPS)
				.where(Tables.EH_PORTAL_ITEM_GROUPS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_PORTAL_ITEM_GROUPS.VERSION_ID.eq(versionId))
				.fetch().map(r -> ConvertHelper.convert(r, PortalItemGroup.class));
	}

	@Override
	public List<PortalItemGroup> listPortalItemGroupByWidgetAndStyle(Integer namespaceId, Long versionId, String widget, String style) {
		return getReadOnlyContext().select().from(Tables.EH_PORTAL_ITEM_GROUPS)
				.where(Tables.EH_PORTAL_ITEM_GROUPS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_PORTAL_ITEM_GROUPS.VERSION_ID.eq(versionId))
				.and(Tables.EH_PORTAL_ITEM_GROUPS.WIDGET.eq(widget))
				.and(Tables.EH_PORTAL_ITEM_GROUPS.STYLE.eq(style))
				.fetch().map(r -> ConvertHelper.convert(r, PortalItemGroup.class));
	}

	@Override
	public List<PortalItemGroup> listBannerItemGroupByAppId(String instanceConfig) {
		return getReadOnlyContext().select().from(Tables.EH_PORTAL_ITEM_GROUPS)
				.where(Tables.EH_PORTAL_ITEM_GROUPS.INSTANCE_CONFIG.like(instanceConfig))
				.and(Tables.EH_PORTAL_ITEM_GROUPS.WIDGET.eq(Widget.BANNERS.getCode()))
                .and(Tables.EH_PORTAL_ITEM_GROUPS.STATUS.eq(PortalItemGroupStatus.ACTIVE.getCode()))
				.orderBy(Tables.EH_PORTAL_ITEM_GROUPS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, PortalItemGroup.class));
	}

	private EhPortalItemGroupsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhPortalItemGroupsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhPortalItemGroupsDao getDao(DSLContext context) {
		return new EhPortalItemGroupsDao(context.configuration());
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
