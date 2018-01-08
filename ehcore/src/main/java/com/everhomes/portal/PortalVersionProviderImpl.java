// @formatter:off
package com.everhomes.portal;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPortalVersionsDao;
import com.everhomes.server.schema.tables.pojos.EhPortalItems;
import com.everhomes.server.schema.tables.pojos.EhPortalVersions;
import com.everhomes.server.schema.tables.records.EhPortalVersionsRecord;
import com.everhomes.util.ConvertHelper;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PortalVersionProviderImpl implements PortalVersionProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;


	@Override
	public void createPortalVersion(PortalVersion portalVersion) {
		if(portalVersion.getId() == null){
			Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPortalVersions.class));
			portalVersion.setId(id);
		}

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		EhPortalVersionsDao dao = new EhPortalVersionsDao(context.configuration());
		dao.insert(portalVersion);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPortalItems.class, null);
	}

	@Override
	public void updatePortalVersion(PortalVersion portalVersion) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		EhPortalVersionsDao dao = new EhPortalVersionsDao(context.configuration());
		dao.update(portalVersion);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPortalItems.class, null);
	}

	@Override
	public PortalVersion findPortalVersionById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		EhPortalVersionsDao dao = new EhPortalVersionsDao(context.configuration());
		EhPortalVersions versions = dao.findById(id);
		if(versions != null){
			return ConvertHelper.convert(versions, PortalVersion.class);
		}
		return null;
	}

	@Override
	public PortalVersion findMaxVersion(Integer namespaceId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhPortalVersionsRecord> query = context.selectQuery(Tables.EH_PORTAL_VERSIONS);
		query.addConditions(Tables.EH_PORTAL_VERSIONS.NAMESPACE_ID.eq(namespaceId));
		query.addOrderBy(Tables.EH_PORTAL_VERSIONS.ID.desc());
		query.addLimit(1);
		return query.fetchAnyInto(PortalVersion.class);
	}

	@Override
	public List<PortalVersion> listPortalVersion(Integer namespaceId, Byte status) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhPortalVersionsRecord> query = context.selectQuery(Tables.EH_PORTAL_VERSIONS);
		if(namespaceId != null){
			query.addConditions(Tables.EH_PORTAL_VERSIONS.NAMESPACE_ID.eq(namespaceId));
		}
		if(status != null){
			query.addConditions(Tables.EH_PORTAL_VERSIONS.STATUS.eq(status));
		}
		query.addOrderBy(Tables.EH_PORTAL_VERSIONS.ID.desc());

		List<PortalVersion> list = new ArrayList<>();
		query.fetch().map(r ->{
			list.add(ConvertHelper.convert(r, PortalVersion.class));
			return null;
		});

		return list;
	}
}
