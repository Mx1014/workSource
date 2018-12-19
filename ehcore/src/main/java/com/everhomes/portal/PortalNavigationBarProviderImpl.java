// @formatter:off
package com.everhomes.portal;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.rest.portal.PortalNavigationBarStatus;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPortalNavigationBarsDao;
import com.everhomes.server.schema.tables.pojos.EhPortalNavigationBars;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class PortalNavigationBarProviderImpl implements PortalNavigationBarProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPortalNavigationBar(PortalNavigationBar portalNavigationBar) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPortalNavigationBars.class));
		portalNavigationBar.setId(id);
		portalNavigationBar.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		portalNavigationBar.setUpdateTime(portalNavigationBar.getCreateTime());
		getReadWriteDao().insert(portalNavigationBar);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPortalNavigationBars.class, null);
	}

	@Override
	public void updatePortalNavigationBar(PortalNavigationBar portalNavigationBar) {
		assert (portalNavigationBar.getId() != null);
		portalNavigationBar.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		getReadWriteDao().update(portalNavigationBar);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPortalNavigationBars.class, portalNavigationBar.getId());
	}

	@Override
	public void deletePortalNavigationBar(Long id) {
		assert (id != null);
		getReadWriteDao().deleteById(id);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPortalNavigationBars.class, id);
	}

	@Override
	public PortalNavigationBar findPortalNavigationBarById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), PortalNavigationBar.class);
	}
	
	@Override
	public List<PortalNavigationBar> listPortalNavigationBar(Long versionId) {
		Condition cond = Tables.EH_PORTAL_NAVIGATION_BARS.STATUS.ne(PortalNavigationBarStatus.INACTIVE.getCode());
		if(null != versionId){
			cond = cond.and(Tables.EH_PORTAL_NAVIGATION_BARS.VERSION_ID.eq(versionId));
		}

		return getReadOnlyContext().select().from(Tables.EH_PORTAL_NAVIGATION_BARS)
				.where(cond)
				.orderBy(Tables.EH_PORTAL_NAVIGATION_BARS.DEFAULT_ORDER.asc())
				.fetch().map(r -> ConvertHelper.convert(r, PortalNavigationBar.class));
	}

	@Override
	public Integer maxOrder(Integer namespaceId, Long versionId) {
		Condition cond = Tables.EH_PORTAL_NAVIGATION_BARS.STATUS.ne(PortalNavigationBarStatus.INACTIVE.getCode());
		if(null != versionId){
			cond = cond.and(Tables.EH_PORTAL_NAVIGATION_BARS.VERSION_ID.eq(versionId));
		}

		if (namespaceId != null) {
            cond = cond.and(Tables.EH_PORTAL_NAVIGATION_BARS.NAMESPACE_ID.eq(namespaceId));
        }

		return getReadOnlyContext().select(DSL.max(Tables.EH_PORTAL_NAVIGATION_BARS.DEFAULT_ORDER)).from(Tables.EH_PORTAL_NAVIGATION_BARS)
				.where(cond)
				.fetchOneInto(Integer.class);
	}

	private EhPortalNavigationBarsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhPortalNavigationBarsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhPortalNavigationBarsDao getDao(DSLContext context) {
		return new EhPortalNavigationBarsDao(context.configuration());
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
