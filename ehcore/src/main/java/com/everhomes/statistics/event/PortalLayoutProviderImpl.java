// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPortalLayoutsDao;
import com.everhomes.server.schema.tables.pojos.EhPortalLayouts;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PortalLayoutProviderImpl implements PortalLayoutProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPortalLayout(PortalLayout portalLayout) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPortalLayouts.class));
		portalLayout.setId(id);
		portalLayout.setCreateTime(DateUtils.currentTimestamp());
		// portalLayout.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(portalLayout);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPortalLayouts.class, id);
	}

	@Override
	public void updatePortalLayout(PortalLayout portalLayout) {
		// portalLayout.setUpdateTime(DateUtils.currentTimestamp());
		// portalLayout.setUpdateUid(UserContext.currentUserId());
        rwDao().update(portalLayout);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPortalLayouts.class, portalLayout.getId());
	}

	@Override
	public PortalLayout findPortalLayoutById(Long id) {
		return ConvertHelper.convert(dao().findById(id), PortalLayout.class);
	}

    @Override
    public List<PortalLayout> listPortalLayoutByStatus(byte status) {
        return context().selectFrom(Tables.EH_PORTAL_LAYOUTS)
                .where(Tables.EH_PORTAL_LAYOUTS.STATUS.eq(status))
                .fetchInto(PortalLayout.class);
    }

    // @Override
	// public List<PortalLayout> listPortalLayout() {
	// 	return getReadOnlyContext().select().from(Tables.EH_PORTAL_LAYOUTS)
	//			.orderBy(Tables.EH_PORTAL_LAYOUTS.ID.asc())
	//			.fetch().map(r -> ConvertHelper.convert(r, PortalLayout.class));
	// }
	
	private EhPortalLayoutsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhPortalLayoutsDao(context.configuration());
	}

	private EhPortalLayoutsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhPortalLayoutsDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
