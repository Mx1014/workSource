// @formatter:off
package com.everhomes.visitorsys;

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
import com.everhomes.server.schema.tables.daos.EhVisitorSysOwnerCodeDao;
import com.everhomes.server.schema.tables.pojos.EhVisitorSysOwnerCode;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class VisitorSysOwnerCodeProviderImpl implements VisitorSysOwnerCodeProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createVisitorSysOwnerCode(VisitorSysOwnerCode visitorSysOwnerCode) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhVisitorSysOwnerCode.class));
		visitorSysOwnerCode.setId(id);
		visitorSysOwnerCode.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		visitorSysOwnerCode.setCreatorUid(UserContext.current().getUser().getId());
		visitorSysOwnerCode.setOperateTime(visitorSysOwnerCode.getCreateTime());
		visitorSysOwnerCode.setOperatorUid(visitorSysOwnerCode.getCreatorUid());
		getReadWriteDao().insert(visitorSysOwnerCode);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhVisitorSysOwnerCode.class, null);
	}

	@Override
	public void updateVisitorSysOwnerCode(VisitorSysOwnerCode visitorSysOwnerCode) {
		assert (visitorSysOwnerCode.getId() != null);
		visitorSysOwnerCode.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		visitorSysOwnerCode.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(visitorSysOwnerCode);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhVisitorSysOwnerCode.class, visitorSysOwnerCode.getId());
	}

	@Override
	public VisitorSysOwnerCode findVisitorSysOwnerCodeById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), VisitorSysOwnerCode.class);
	}
	
	@Override
	public List<VisitorSysOwnerCode> listVisitorSysOwnerCode() {
		return getReadOnlyContext().select().from(Tables.EH_VISITOR_SYS_OWNER_CODE)
				.orderBy(Tables.EH_VISITOR_SYS_OWNER_CODE.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, VisitorSysOwnerCode.class));
	}

	@Override
	public VisitorSysOwnerCode findVisitorSysCodeByOwner(Integer namespaceId, String ownerType, Long ownerId) {
		List<VisitorSysOwnerCode> list = getReadOnlyContext().select().from(Tables.EH_VISITOR_SYS_OWNER_CODE)
				.where(Tables.EH_VISITOR_SYS_OWNER_CODE.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_VISITOR_SYS_OWNER_CODE.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_VISITOR_SYS_OWNER_CODE.OWNER_ID.eq(ownerId))
				.fetch().map(r -> ConvertHelper.convert(r, VisitorSysOwnerCode.class));
		if(list==null||list.size()==0){
			return null;
		}
		return list.get(0);
	}

	@Override
	public VisitorSysOwnerCode findVisitorSysCodingByRandomCode(String randomCode) {
		List<VisitorSysOwnerCode> list = getReadOnlyContext().select().from(Tables.EH_VISITOR_SYS_OWNER_CODE)
				.where(Tables.EH_VISITOR_SYS_OWNER_CODE.RANDOM_CODE.eq(randomCode))
				.fetch().map(r -> ConvertHelper.convert(r, VisitorSysOwnerCode.class));
		if(list==null||list.size()==0){
			return null;
		}
		return list.get(0);
	}

	private EhVisitorSysOwnerCodeDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhVisitorSysOwnerCodeDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhVisitorSysOwnerCodeDao getDao(DSLContext context) {
		return new EhVisitorSysOwnerCodeDao(context.configuration());
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
