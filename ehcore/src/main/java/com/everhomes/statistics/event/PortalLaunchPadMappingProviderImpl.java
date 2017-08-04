// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.server.schema.Tables;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhPortalLaunchPadMappingsDao;
import com.everhomes.server.schema.tables.pojos.EhPortalLaunchPadMappings;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;

@Repository
public class PortalLaunchPadMappingProviderImpl implements PortalLaunchPadMappingProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPortalLaunchPadMapping(PortalLaunchPadMapping portalLaunchPadMapping) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPortalLaunchPadMappings.class));
		portalLaunchPadMapping.setId(id);
		portalLaunchPadMapping.setCreateTime(DateUtils.currentTimestamp());
		// portalLaunchPadMapping.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(portalLaunchPadMapping);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPortalLaunchPadMappings.class, id);
	}

	@Override
	public void updatePortalLaunchPadMapping(PortalLaunchPadMapping portalLaunchPadMapping) {
		// portalLaunchPadMapping.setUpdateTime(DateUtils.currentTimestamp());
		// portalLaunchPadMapping.setUpdateUid(UserContext.currentUserId());
        rwDao().update(portalLaunchPadMapping);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPortalLaunchPadMappings.class, portalLaunchPadMapping.getId());
	}

	@Override
	public PortalLaunchPadMapping findById(Long id) {
		return ConvertHelper.convert(dao().findById(id), PortalLaunchPadMapping.class);
	}

    @Override
    public PortalLaunchPadMapping findPortalLaunchPadMapping(String contentType, Long launchPadContentId) {
        return context().selectFrom(Tables.EH_PORTAL_LAUNCH_PAD_MAPPINGS)
                .where(Tables.EH_PORTAL_LAUNCH_PAD_MAPPINGS.CONTENT_TYPE.eq(contentType))
                .and(Tables.EH_PORTAL_LAUNCH_PAD_MAPPINGS.LAUNCH_PAD_CONTENT_ID.eq(launchPadContentId))
                .orderBy(Tables.EH_PORTAL_LAUNCH_PAD_MAPPINGS.CREATE_TIME.desc())
                .fetchAnyInto(PortalLaunchPadMapping.class);
    }

    private EhPortalLaunchPadMappingsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhPortalLaunchPadMappingsDao(context.configuration());
	}

	private EhPortalLaunchPadMappingsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhPortalLaunchPadMappingsDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
