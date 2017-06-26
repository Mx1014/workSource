// @formatter:off
package com.everhomes.portal;

import java.sql.Timestamp;
import java.util.List;

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
import com.everhomes.server.schema.tables.daos.EhPortalContentScopesDao;
import com.everhomes.server.schema.tables.pojos.EhPortalContentScopes;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class PortalContentScopeProviderImpl implements PortalContentScopeProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPortalContentScope(PortalContentScope portalContentScope) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPortalContentScopes.class));
		portalContentScope.setId(id);
		portalContentScope.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		portalContentScope.setCreatorUid(UserContext.current().getUser().getId());
		portalContentScope.setUpdateTime(portalContentScope.getCreateTime());
		portalContentScope.setOperatorUid(portalContentScope.getCreatorUid());
		getReadWriteDao().insert(portalContentScope);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPortalContentScopes.class, null);
	}

	@Override
	public void updatePortalContentScope(PortalContentScope portalContentScope) {
		assert (portalContentScope.getId() != null);
		portalContentScope.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		portalContentScope.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(portalContentScope);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPortalContentScopes.class, portalContentScope.getId());
	}

	@Override
	public PortalContentScope findPortalContentScopeById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), PortalContentScope.class);
	}
	
	@Override
	public List<PortalContentScope> listPortalContentScope() {
		return getReadOnlyContext().select().from(Tables.EH_PORTAL_CONTENT_SCOPES)
				.orderBy(Tables.EH_PORTAL_CONTENT_SCOPES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, PortalContentScope.class));
	}
	
	private EhPortalContentScopesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhPortalContentScopesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhPortalContentScopesDao getDao(DSLContext context) {
		return new EhPortalContentScopesDao(context.configuration());
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
