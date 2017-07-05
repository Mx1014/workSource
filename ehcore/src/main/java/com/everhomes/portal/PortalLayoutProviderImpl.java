// @formatter:off
package com.everhomes.portal;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.rest.portal.PortalLayoutStatus;
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
import com.everhomes.server.schema.tables.daos.EhPortalLayoutsDao;
import com.everhomes.server.schema.tables.pojos.EhPortalLayouts;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class PortalLayoutProviderImpl implements PortalLayoutProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPortalLayout(PortalLayout portalLayout) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPortalLayouts.class));
		portalLayout.setId(id);
		portalLayout.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		portalLayout.setUpdateTime(portalLayout.getCreateTime());
		getReadWriteDao().insert(portalLayout);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPortalLayouts.class, null);
	}

	@Override
	public void updatePortalLayout(PortalLayout portalLayout) {
		assert (portalLayout.getId() != null);
		portalLayout.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		portalLayout.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(portalLayout);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPortalLayouts.class, portalLayout.getId());
	}

	@Override
	public PortalLayout findPortalLayoutById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), PortalLayout.class);
	}
	
	@Override
	public List<PortalLayout> listPortalLayout() {
		return getReadOnlyContext().select().from(Tables.EH_PORTAL_LAYOUTS)
				.where(Tables.EH_PORTAL_LAYOUTS.STATUS.eq(PortalLayoutStatus.ACTIVE.getCode()))
				.orderBy(Tables.EH_PORTAL_LAYOUTS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, PortalLayout.class));
	}
	
	private EhPortalLayoutsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhPortalLayoutsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhPortalLayoutsDao getDao(DSLContext context) {
		return new EhPortalLayoutsDao(context.configuration());
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
