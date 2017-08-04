// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPortalItemGroupsDao;
import com.everhomes.server.schema.tables.pojos.EhPortalItemGroups;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PortalItemGroupProviderImpl implements PortalItemGroupProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPortalItemGroup(PortalItemGroup portalItemGroup) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPortalItemGroups.class));
		portalItemGroup.setId(id);
		portalItemGroup.setCreateTime(DateUtils.currentTimestamp());
		// portalItemGroup.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(portalItemGroup);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPortalItemGroups.class, id);
	}

	@Override
	public void updatePortalItemGroup(PortalItemGroup portalItemGroup) {
		// portalItemGroup.setUpdateTime(DateUtils.currentTimestamp());
		// portalItemGroup.setUpdateUid(UserContext.currentUserId());
        rwDao().update(portalItemGroup);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPortalItemGroups.class, portalItemGroup.getId());
	}

	@Override
	public PortalItemGroup findPortalItemGroupById(Long id) {
		return ConvertHelper.convert(dao().findById(id), PortalItemGroup.class);
	}

    @Override
    public List<PortalItemGroup> listPortalItemGroupByStatus(Long layoutId, byte status) {
        return context().selectFrom(Tables.EH_PORTAL_ITEM_GROUPS)
                .where(Tables.EH_PORTAL_ITEM_GROUPS.LAYOUT_ID.eq(layoutId))
                .and(Tables.EH_PORTAL_ITEM_GROUPS.STATUS.eq(status))
                .fetchInto(PortalItemGroup.class);
    }

    @Override
    public PortalItemGroup findPortalItemGroup(Long layoutId, String widgetName, String itemGroup) {
        return context().selectFrom(Tables.EH_PORTAL_ITEM_GROUPS)
                .where(Tables.EH_PORTAL_ITEM_GROUPS.LAYOUT_ID.eq(layoutId))
                .and(Tables.EH_PORTAL_ITEM_GROUPS.WIDGET.eq(widgetName))
                .and(Tables.EH_PORTAL_ITEM_GROUPS.NAME.eq(itemGroup))
                .orderBy(Tables.EH_PORTAL_ITEM_GROUPS.CREATE_TIME.desc())
                .fetchAnyInto(PortalItemGroup.class);

    }

    // @Override
	// public List<PortalItemGroup> listPortalItemGroup() {
	// 	return getReadOnlyContext().select().from(Tables.EH_PORTAL_ITEM_GROUPS)
	//			.orderBy(Tables.EH_PORTAL_ITEM_GROUPS.ID.asc())
	//			.fetch().map(r -> ConvertHelper.convert(r, PortalItemGroup.class));
	// }
	
	private EhPortalItemGroupsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhPortalItemGroupsDao(context.configuration());
	}

	private EhPortalItemGroupsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhPortalItemGroupsDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
