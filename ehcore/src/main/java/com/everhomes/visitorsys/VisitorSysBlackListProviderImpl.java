// @formatter:off
package com.everhomes.visitorsys;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.rest.approval.CommonStatus;
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
import com.everhomes.server.schema.tables.daos.EhVisitorSysBlackListDao;
import com.everhomes.server.schema.tables.pojos.EhVisitorSysBlackList;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class VisitorSysBlackListProviderImpl implements VisitorSysBlackListProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createVisitorSysBlackList(VisitorSysBlackList visitorSysBlackList) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhVisitorSysBlackList.class));
		visitorSysBlackList.setId(id);
		visitorSysBlackList.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		visitorSysBlackList.setCreatorUid(UserContext.current().getUser().getId());
		visitorSysBlackList.setOperateTime(visitorSysBlackList.getCreateTime());
		visitorSysBlackList.setOperatorUid(visitorSysBlackList.getCreatorUid());
		visitorSysBlackList.setStatus(CommonStatus.ACTIVE.getCode());
		getReadWriteDao().insert(visitorSysBlackList);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhVisitorSysBlackList.class, null);
	}

	@Override
	public void updateVisitorSysBlackList(VisitorSysBlackList visitorSysBlackList) {
		assert (visitorSysBlackList.getId() != null);
		visitorSysBlackList.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		visitorSysBlackList.setOperatorUid(UserContext.current().getUser().getId());
		visitorSysBlackList.setStatus(CommonStatus.ACTIVE.getCode());
		getReadWriteDao().update(visitorSysBlackList);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhVisitorSysBlackList.class, visitorSysBlackList.getId());
	}

	@Override
	public VisitorSysBlackList findVisitorSysBlackListById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), VisitorSysBlackList.class);
	}
	
	@Override
	public List<VisitorSysBlackList> listVisitorSysBlackList() {
		return getReadOnlyContext().select().from(Tables.EH_VISITOR_SYS_BLACK_LIST)
				.orderBy(Tables.EH_VISITOR_SYS_BLACK_LIST.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, VisitorSysBlackList.class));
	}

	@Override
	public List<VisitorSysBlackList> listVisitorSysBlackList(Integer namespaceId, String ownerType, Long ownerId,
															 String keyWords, Integer pageSize, Long pageAnchor) {
		Condition condition = Tables.EH_VISITOR_SYS_BLACK_LIST.NAMESPACE_ID.eq(namespaceId);
		condition = condition.and( Tables.EH_VISITOR_SYS_BLACK_LIST.OWNER_TYPE.eq(ownerType));
		condition = condition.and( Tables.EH_VISITOR_SYS_BLACK_LIST.STATUS.eq(CommonStatus.ACTIVE.getCode()));
		condition = condition.and( Tables.EH_VISITOR_SYS_BLACK_LIST.OWNER_ID.eq(ownerId));
		if(keyWords!=null){
			Condition or = Tables.EH_VISITOR_SYS_BLACK_LIST.VISITOR_NAME.like("%" + keyWords + "%")
					.or(Tables.EH_VISITOR_SYS_BLACK_LIST.VISITOR_PHONE.like("%" + keyWords + "%"));
			condition = condition.and(or);
		}

		return getReadOnlyContext().select().from(Tables.EH_VISITOR_SYS_BLACK_LIST)
				.where(condition)
				.orderBy(Tables.EH_VISITOR_SYS_BLACK_LIST.ID.desc())
				.fetch().map(r -> ConvertHelper.convert(r, VisitorSysBlackList.class));
	}

	@Override
	public void deleteBlackListById(Integer namespaceId, Long id) {
		getReadWriteContext().update(Tables.EH_VISITOR_SYS_BLACK_LIST)
				.set(Tables.EH_VISITOR_SYS_BLACK_LIST.STATUS, CommonStatus.INACTIVE.getCode())
				.where(Tables.EH_VISITOR_SYS_BLACK_LIST.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_VISITOR_SYS_BLACK_LIST.ID.eq(id)).execute();
	}

	@Override
	public VisitorSysBlackList findVisitorSysBlackListByPhone(Integer namespaceId, String ownerType, Long ownerId, String visitorPhone) {
		Condition condition = Tables.EH_VISITOR_SYS_BLACK_LIST.NAMESPACE_ID.eq(namespaceId);
		condition = condition.and( Tables.EH_VISITOR_SYS_BLACK_LIST.OWNER_TYPE.eq(ownerType));
		condition = condition.and( Tables.EH_VISITOR_SYS_BLACK_LIST.STATUS.eq(CommonStatus.ACTIVE.getCode()));
		condition = condition.and( Tables.EH_VISITOR_SYS_BLACK_LIST.OWNER_ID.eq(ownerId));
		condition = condition.and( Tables.EH_VISITOR_SYS_BLACK_LIST.VISITOR_PHONE.eq(visitorPhone));

		List<VisitorSysBlackList> list = getReadOnlyContext().select().from(Tables.EH_VISITOR_SYS_BLACK_LIST)
				.where(condition)
				.orderBy(Tables.EH_VISITOR_SYS_BLACK_LIST.ID.desc())
				.fetch().map(r -> ConvertHelper.convert(r, VisitorSysBlackList.class));
		if(list==null || list.size()==0){
			return null;
		}
		return list.get(0);
	}

	private EhVisitorSysBlackListDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhVisitorSysBlackListDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhVisitorSysBlackListDao getDao(DSLContext context) {
		return new EhVisitorSysBlackListDao(context.configuration());
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
