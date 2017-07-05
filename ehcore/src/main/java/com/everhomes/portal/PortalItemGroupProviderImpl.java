// @formatter:off
package com.everhomes.portal;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.rest.portal.PortalItemGroupStatus;
import org.jooq.DSLContext;
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

@Component
public class PortalItemGroupProviderImpl implements PortalItemGroupProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPortalItemGroup(PortalItemGroup portalItemGroup) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPortalItemGroups.class));
		portalItemGroup.setId(id);
		portalItemGroup.setName(EhPortalItemGroups.class.getName() + id);
		portalItemGroup.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		portalItemGroup.setUpdateTime(portalItemGroup.getCreateTime());
		getReadWriteDao().insert(portalItemGroup);
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
	public PortalItemGroup findPortalItemGroupById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), PortalItemGroup.class);
	}
	
	@Override
	public List<PortalItemGroup> listPortalItemGroup(Long layoutId) {
		return getReadOnlyContext().select().from(Tables.EH_PORTAL_ITEM_GROUPS)
				.where(Tables.EH_PORTAL_ITEM_GROUPS.STATUS.eq(PortalItemGroupStatus.ACTIVE.getCode()))
				.and(Tables.EH_PORTAL_ITEM_GROUPS.LAYOUT_ID.eq(layoutId))
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
