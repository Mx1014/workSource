// @formatter:off
package com.everhomes.yellowPage;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhServiceAllianceCommentsDao;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceComments;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class ServiceAllianceCommentProviderImpl implements ServiceAllianceCommentProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createServiceAllianceComment(ServiceAllianceComment serviceAllianceComment) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceAllianceComments.class));
		serviceAllianceComment.setId(id);
		serviceAllianceComment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		serviceAllianceComment.setCreatorUid(UserContext.current().getUser().getId());
//		serviceAllianceComment.setUpdATETIME(SERVICEALLIANCECOMMENT.GETCREATETIME());
//		SERVICEALLIANCECOMMENT.SETOPERATOrUid(serviceAllianceComment.getCreatorUid());
		getReadWriteDao().insert(serviceAllianceComment);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhServiceAllianceComments.class, null);
	}

	@Override
	public void updateServiceAllianceComment(ServiceAllianceComment serviceAllianceComment) {
		assert (serviceAllianceComment.getId() != null);
//		serviceAllianceComment.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		serviceAllianceComment.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(serviceAllianceComment);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhServiceAllianceComments.class, serviceAllianceComment.getId());
	}

	@Override
	public ServiceAllianceComment findServiceAllianceCommentById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ServiceAllianceComment.class);
	}
	
	@Override
	public List<ServiceAllianceComment> listServiceAllianceComment() {
		return getReadOnlyContext().select().from(Tables.EH_SERVICE_ALLIANCE_COMMENTS)
				.orderBy(Tables.EH_SERVICE_ALLIANCE_COMMENTS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ServiceAllianceComment.class));
	}
	
	private EhServiceAllianceCommentsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhServiceAllianceCommentsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhServiceAllianceCommentsDao getDao(DSLContext context) {
		return new EhServiceAllianceCommentsDao(context.configuration());
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

	@Override
	public List<ServiceAllianceComment> listServiceAllianceCommentByOwner(Integer namespaceId, String ownerType,
			Long ownerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Integer> listServiceAllianceCommentCountByOwner(Integer namespaceId, String ownerType,
			List<Long> ownerIds) {
		Result<Record2<Long, Integer>> result = getReadOnlyContext().select(Tables.EH_SERVICE_ALLIANCE_COMMENTS.OWNER_ID,Tables.EH_SERVICE_ALLIANCE_COMMENTS.OWNER_ID.count()).from(Tables.EH_SERVICE_ALLIANCE_COMMENTS)
			.where(Tables.EH_SERVICE_ALLIANCE_COMMENTS.NAMESPACE_ID.eq(namespaceId))
			.and(Tables.EH_SERVICE_ALLIANCE_COMMENTS.OWNER_TYPE.eq(ownerType))
			.and(Tables.EH_SERVICE_ALLIANCE_COMMENTS.OWNER_ID.in(ownerIds))
			.and(Tables.EH_SERVICE_ALLIANCE_COMMENTS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
			.groupBy(Tables.EH_SERVICE_ALLIANCE_COMMENTS.OWNER_ID).fetch();
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (Iterator<Record2<Long, Integer>> iterator = result.iterator(); iterator.hasNext();) {
			Record2<Long, Integer> record2 = (Record2<Long, Integer>) iterator.next();
			map.put(String.valueOf(record2.getValue(Tables.EH_SERVICE_ALLIANCE_COMMENTS.OWNER_ID)), record2.getValue(Tables.EH_SERVICE_ALLIANCE_COMMENTS.OWNER_ID.count()));
		}
		return map;
	}
}
