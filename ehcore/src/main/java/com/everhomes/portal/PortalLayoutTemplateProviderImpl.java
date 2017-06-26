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
import com.everhomes.server.schema.tables.daos.EhPortalLayoutTemplatesDao;
import com.everhomes.server.schema.tables.pojos.EhPortalLayoutTemplates;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class PortalLayoutTemplateProviderImpl implements PortalLayoutTemplateProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPortalLayoutTemplate(PortalLayoutTemplate portalLayoutTemplate) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPortalLayoutTemplates.class));
		portalLayoutTemplate.setId(id);
		portalLayoutTemplate.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		portalLayoutTemplate.setCreatorUid(UserContext.current().getUser().getId());
		portalLayoutTemplate.setUpdateTime(portalLayoutTemplate.getCreateTime());
		portalLayoutTemplate.setOperatorUid(portalLayoutTemplate.getCreatorUid());
		getReadWriteDao().insert(portalLayoutTemplate);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPortalLayoutTemplates.class, null);
	}

	@Override
	public void updatePortalLayoutTemplate(PortalLayoutTemplate portalLayoutTemplate) {
		assert (portalLayoutTemplate.getId() != null);
		portalLayoutTemplate.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		portalLayoutTemplate.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(portalLayoutTemplate);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPortalLayoutTemplates.class, portalLayoutTemplate.getId());
	}

	@Override
	public PortalLayoutTemplate findPortalLayoutTemplateById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), PortalLayoutTemplate.class);
	}
	
	@Override
	public List<PortalLayoutTemplate> listPortalLayoutTemplate() {
		return getReadOnlyContext().select().from(Tables.EH_PORTAL_LAYOUT_TEMPLATES)
				.orderBy(Tables.EH_PORTAL_LAYOUT_TEMPLATES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, PortalLayoutTemplate.class));
	}
	
	private EhPortalLayoutTemplatesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhPortalLayoutTemplatesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhPortalLayoutTemplatesDao getDao(DSLContext context) {
		return new EhPortalLayoutTemplatesDao(context.configuration());
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
