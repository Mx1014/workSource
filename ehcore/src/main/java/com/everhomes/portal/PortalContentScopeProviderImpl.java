// @formatter:off
package com.everhomes.portal;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
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
import com.everhomes.server.schema.tables.daos.EhPortalContentScopesDao;
import com.everhomes.server.schema.tables.pojos.EhPortalContentScopes;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class PortalContentScopeProviderImpl implements PortalContentScopeProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(PortalContentScopeProviderImpl.class);


	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPortalContentScope(PortalContentScope portalContentScope) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPortalContentScopes.class));
		portalContentScope.setId(id);
		portalContentScope.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		portalContentScope.setUpdateTime(portalContentScope.getCreateTime());
		getReadWriteDao().insert(portalContentScope);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPortalContentScopes.class, null);
	}

	@Override
	public void createPortalContentScopes(List<PortalContentScope> portalContentScopes) {
		LOGGER.debug("create portal itemGroup size = {}", portalContentScopes.size());
		if(portalContentScopes.size() == 0){
			return;
		}
		Long id = sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhPortalContentScopes.class), (long)portalContentScopes.size());
		List<EhPortalContentScopes> scopes = new ArrayList<>();
		for (PortalContentScope scope: portalContentScopes) {
			id ++;
			scope.setId(id);
			scope.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			scope.setUpdateTime(scope.getCreateTime());
			scopes.add(ConvertHelper.convert(scope, EhPortalContentScopes.class));
		}
		getReadWriteDao().insert(scopes);
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
	public void deletePortalContentScopeById(Long id) {
		assert (id != null);
		getReadWriteDao().deleteById(id);
	}

	@Override
	public void deletePortalContentScopeByIds(List<Long> ids) {
		assert (ids != null && ids.size() != 0);
		getReadWriteDao().deleteById(ids);
	}

	@Override
	public PortalContentScope findPortalContentScopeById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), PortalContentScope.class);
	}
	
	@Override
	public List<PortalContentScope> listPortalContentScope(String contentType, Long contentId) {
		return getReadOnlyContext().select().from(Tables.EH_PORTAL_CONTENT_SCOPES)
				.where(Tables.EH_PORTAL_CONTENT_SCOPES.CONTENT_TYPE.eq(contentType))
				.and(Tables.EH_PORTAL_CONTENT_SCOPES.CONTENT_ID.eq(contentId))
				.orderBy(Tables.EH_PORTAL_CONTENT_SCOPES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, PortalContentScope.class));
	}

	@Override
	public void deletePortalContentScopes(String contentType, Long contentId) {
		List<PortalContentScope> portalContentScopes = listPortalContentScope(contentType, contentId);
		List<Long> ids = new ArrayList<>();
		for (PortalContentScope portalContentScope: portalContentScopes) {
			ids.add(portalContentScope.getId());
		}
		deletePortalContentScopeByIds(ids);
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
