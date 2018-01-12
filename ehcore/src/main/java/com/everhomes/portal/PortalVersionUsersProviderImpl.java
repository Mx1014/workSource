// @formatter:off
package com.everhomes.portal;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPortalVersionUsersDao;
import com.everhomes.server.schema.tables.pojos.EhPortalVersionUsers;
import com.everhomes.server.schema.tables.records.EhPortalVersionUsersRecord;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PortalVersionUsersProviderImpl implements PortalVersionUserProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;


	@Override
	public void createPortalVersionUser(PortalVersionUser portalVersionUser) {
		if(portalVersionUser.getId() == null){
			Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPortalVersionUsers.class));
			portalVersionUser.setId(id);
		}

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		EhPortalVersionUsersDao dao = new EhPortalVersionUsersDao(context.configuration());
		dao.insert(portalVersionUser);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPortalVersionUsers.class, null);
	}

	@Override
	public void deletePortalVersionUser(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		EhPortalVersionUsersDao dao = new EhPortalVersionUsersDao(context.configuration());
		dao.deleteById(id);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPortalVersionUsers.class, id);
	}

	@Override
	public PortalVersionUser findPortalVersionUserById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		EhPortalVersionUsersDao dao = new EhPortalVersionUsersDao(context.configuration());
		EhPortalVersionUsers portalVersionUser = dao.findById(id);
		if(portalVersionUser != null){
			return ConvertHelper.convert(portalVersionUser, PortalVersionUser.class);
		}
		return null;
	}

	@Override
	public List<PortalVersionUser> listPortalVersionUsers(Integer namespaceId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhPortalVersionUsersRecord> query = context.selectQuery(Tables.EH_PORTAL_VERSION_USERS);
		if(namespaceId != null){
			query.addConditions(Tables.EH_PORTAL_VERSION_USERS.NAMESPACE_ID.eq(namespaceId));
		}
		query.addOrderBy(Tables.EH_PORTAL_VERSION_USERS.ID.desc());

		List<PortalVersionUser> list = new ArrayList<>();
		query.fetch().map(r ->{
			list.add(ConvertHelper.convert(r, PortalVersionUser.class));
			return null;
		});

		return list;
	}

	@Override
	public PortalVersionUser findPortalVersionUserByUserId(Long userId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhPortalVersionUsersRecord> query = context.selectQuery(Tables.EH_PORTAL_VERSION_USERS);
		query.addConditions(Tables.EH_PORTAL_VERSION_USERS.USER_ID.eq(userId));
		return query.fetchAnyInto(PortalVersionUser.class);
	}
}
