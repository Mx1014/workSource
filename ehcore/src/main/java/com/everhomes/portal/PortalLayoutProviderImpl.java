// @formatter:off
package com.everhomes.portal;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.portal.PortalLayoutStatus;
import com.everhomes.rest.portal.PortalLayoutType;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.DeleteQuery;
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
import org.springframework.util.StringUtils;

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
		if(StringUtils.isEmpty(portalLayout.getName()))
			portalLayout.setName(EhPortalLayouts.class.getSimpleName() + id);
		if(StringUtils.isEmpty(portalLayout.getLocation()))
			portalLayout.setLocation("/" + portalLayout.getName());
		getReadWriteDao().insert(portalLayout);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPortalLayouts.class, null);
	}


	@Override
	public void createPortalLayouts(List<PortalLayout> portalLayouts) {
		if(portalLayouts.size() == 0){
			return;
		}

		/**
		 * 有id使用原来的id，没有则生成新的
		 */
		Long id = sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhPortalLayouts.class), (long)portalLayouts.size() + 1);
		List<EhPortalLayouts> mappings = new ArrayList<>();
		for (PortalLayout layout: portalLayouts) {
			if(layout.getId() == null){
				id ++;
				layout.setId(id);
			}
			layout.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			mappings.add(ConvertHelper.convert(layout, EhPortalLayouts.class));
		}
		getReadWriteDao().insert(mappings);
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
	public PortalLayout getPortalLayout(Integer namespaceId, String name, Long versionId) {
		List<PortalLayout> layouts = listPortalLayout(namespaceId, name, versionId);
		if(layouts.size() > 0)
			return layouts.get(0);
		return null;
	}

	@Override
	public List<PortalLayout> listPortalLayout(Integer namespaceId, String name, Long versionId) {
		Condition cond = Tables.EH_PORTAL_LAYOUTS.NAMESPACE_ID.eq(namespaceId);
		if(!StringUtils.isEmpty(name)){
			cond = cond.and(Tables.EH_PORTAL_LAYOUTS.NAME.eq(name));
		}
		if(versionId != null){
			cond = cond.and(Tables.EH_PORTAL_LAYOUTS.VERSION_ID.eq(versionId));
		}
		return getReadOnlyContext().select().from(Tables.EH_PORTAL_LAYOUTS)
				.where(Tables.EH_PORTAL_LAYOUTS.STATUS.eq(PortalLayoutStatus.ACTIVE.getCode()))
				.and(cond)
				.orderBy(Tables.EH_PORTAL_LAYOUTS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, PortalLayout.class));
	}

	@Override
	public List<PortalLayout> listPortalLayoutByVersion(Integer namespaceId, Long versionId) {

		return getReadOnlyContext().select().from(Tables.EH_PORTAL_LAYOUTS)
				.where(Tables.EH_PORTAL_LAYOUTS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_PORTAL_LAYOUTS.VERSION_ID.eq(versionId))
				.fetch().map(r -> ConvertHelper.convert(r, PortalLayout.class));
	}

	@Override
	public PortalLayout findIndexPortalLayout(Long versionId, Byte type) {

		return getReadOnlyContext().select().from(Tables.EH_PORTAL_LAYOUTS)
				.where(Tables.EH_PORTAL_LAYOUTS.VERSION_ID.eq(versionId))
				.and(Tables.EH_PORTAL_LAYOUTS.TYPE.eq(type))
				.and(Tables.EH_PORTAL_LAYOUTS.INDEX_FLAG.eq(TrueOrFalseFlag.TRUE.getCode()))
				.and(Tables.EH_PORTAL_LAYOUTS.STATUS.eq(PortalLayoutStatus.ACTIVE.getCode()))
				.fetchAnyInto(PortalLayout.class);
	}

	@Override
	public void deleteByVersionId(Long versionId){
		DeleteQuery query = getReadWriteContext().deleteQuery(Tables.EH_PORTAL_LAYOUTS);
		query.addConditions(Tables.EH_PORTAL_LAYOUTS.VERSION_ID.eq(versionId));
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.MODIFY, PortalLayout.class, null);
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
