// @formatter:off
package com.everhomes.portal;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.Condition;
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
import com.everhomes.server.schema.tables.daos.EhPortalLaunchPadMappingsDao;
import com.everhomes.server.schema.tables.pojos.EhPortalLaunchPadMappings;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class PortalLaunchPadMappingProviderImpl implements PortalLaunchPadMappingProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPortalLaunchPadMapping(PortalLaunchPadMapping portalLaunchPadMapping) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPortalLaunchPadMappings.class));
		portalLaunchPadMapping.setId(id);
		portalLaunchPadMapping.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		portalLaunchPadMapping.setCreatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().insert(portalLaunchPadMapping);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPortalLaunchPadMappings.class, null);
	}

	@Override
	public void updatePortalLaunchPadMapping(PortalLaunchPadMapping portalLaunchPadMapping) {
		assert (portalLaunchPadMapping.getId() != null);
		getReadWriteDao().update(portalLaunchPadMapping);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPortalLaunchPadMappings.class, portalLaunchPadMapping.getId());
	}

	@Override
	public PortalLaunchPadMapping findPortalLaunchPadMappingById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), PortalLaunchPadMapping.class);
	}
	
	@Override
	public List<PortalLaunchPadMapping> listPortalLaunchPadMapping(String contentType, Long portalContentId, Long launchPadContentId) {

		Condition condition = Tables.EH_PORTAL_LAUNCH_PAD_MAPPINGS.ID.isNotNull();
		if(null != contentType)
			condition = condition.and(Tables.EH_PORTAL_LAUNCH_PAD_MAPPINGS.CONTENT_TYPE.eq(contentType));
		if(null != portalContentId)
			condition = condition.and(Tables.EH_PORTAL_LAUNCH_PAD_MAPPINGS.PORTAL_CONTENT_ID.eq(portalContentId));
		if(null != launchPadContentId)
			condition = condition.and(Tables.EH_PORTAL_LAUNCH_PAD_MAPPINGS.LAUNCH_PAD_CONTENT_ID.eq(launchPadContentId));

		return getReadOnlyContext().select().from(Tables.EH_PORTAL_LAUNCH_PAD_MAPPINGS)
				.where(condition)
				.orderBy(Tables.EH_PORTAL_LAUNCH_PAD_MAPPINGS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, PortalLaunchPadMapping.class));
	}
	
	private EhPortalLaunchPadMappingsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhPortalLaunchPadMappingsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhPortalLaunchPadMappingsDao getDao(DSLContext context) {
		return new EhPortalLaunchPadMappingsDao(context.configuration());
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
