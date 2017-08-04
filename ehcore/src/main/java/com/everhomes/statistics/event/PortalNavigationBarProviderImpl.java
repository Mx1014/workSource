// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPortalNavigationBarsDao;
import com.everhomes.server.schema.tables.pojos.EhPortalNavigationBars;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PortalNavigationBarProviderImpl implements PortalNavigationBarProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPortalNavigationBar(PortalNavigationBar portalNavigationBar) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPortalNavigationBars.class));
		portalNavigationBar.setId(id);
		portalNavigationBar.setCreateTime(DateUtils.currentTimestamp());
		// portalNavigationBar.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(portalNavigationBar);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPortalNavigationBars.class, id);
	}

	@Override
	public void updatePortalNavigationBar(PortalNavigationBar portalNavigationBar) {
		// portalNavigationBar.setUpdateTime(DateUtils.currentTimestamp());
		// portalNavigationBar.setUpdateUid(UserContext.currentUserId());
        rwDao().update(portalNavigationBar);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPortalNavigationBars.class, portalNavigationBar.getId());
	}

	@Override
	public PortalNavigationBar findPortalNavigationBarById(Long id) {
		return ConvertHelper.convert(dao().findById(id), PortalNavigationBar.class);
	}
	
	@Override
	public List<PortalNavigationBar> listPortalNavigationBar() {
        return context().select().from(Tables.EH_PORTAL_NAVIGATION_BARS)
                .orderBy(Tables.EH_PORTAL_NAVIGATION_BARS.ID.asc())
                .fetchInto(PortalNavigationBar.class);
    }

    @Override
    public List<PortalNavigationBar> listPortalNavigationBarByStatus(byte status) {
        return context().select().from(Tables.EH_PORTAL_NAVIGATION_BARS)
                .where(Tables.EH_PORTAL_NAVIGATION_BARS.STATUS.eq(status)) // 已发布状态
                .orderBy(Tables.EH_PORTAL_NAVIGATION_BARS.ID.asc())
                .fetchInto(PortalNavigationBar.class);
    }

    private EhPortalNavigationBarsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhPortalNavigationBarsDao(context.configuration());
	}

	private EhPortalNavigationBarsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhPortalNavigationBarsDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
