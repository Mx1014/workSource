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
import com.everhomes.server.schema.tables.daos.EhVisitorSysCodingDao;
import com.everhomes.server.schema.tables.pojos.EhVisitorSysCoding;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class VisitorSysCodingProviderImpl implements VisitorSysCodingProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createVisitorSysCoding(VisitorSysCoding visitorSysCoding) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhVisitorSysCoding.class));
		visitorSysCoding.setId(id);
		visitorSysCoding.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		visitorSysCoding.setCreatorUid(UserContext.current().getUser().getId());
		visitorSysCoding.setOperateTime(visitorSysCoding.getCreateTime());
		visitorSysCoding.setOperatorUid(visitorSysCoding.getCreatorUid());
		getReadWriteDao().insert(visitorSysCoding);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhVisitorSysCoding.class, null);
	}

	@Override
	public void updateVisitorSysCoding(VisitorSysCoding visitorSysCoding) {
		assert (visitorSysCoding.getId() != null);
		//更新时间需要在调用此方法时候设置
//		visitorSysCoding.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		visitorSysCoding.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(visitorSysCoding);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhVisitorSysCoding.class, visitorSysCoding.getId());
	}

	@Override
	public VisitorSysCoding findVisitorSysCodingById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), VisitorSysCoding.class);
	}
	
	@Override
	public List<VisitorSysCoding> listVisitorSysCoding() {
		return getReadOnlyContext().select().from(Tables.EH_VISITOR_SYS_CODING)
				.orderBy(Tables.EH_VISITOR_SYS_CODING.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, VisitorSysCoding.class));
	}

	@Override
	public VisitorSysCoding findVisitorSysCodingByOwner(Integer namespaceId, String ownerType, Long ownerId,String dayRemark) {
		List<VisitorSysCoding> list = getReadOnlyContext().select().from(Tables.EH_VISITOR_SYS_CODING)
				.where(Tables.EH_VISITOR_SYS_CODING.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_VISITOR_SYS_CODING.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_VISITOR_SYS_CODING.OWNER_ID.eq(ownerId))
				.and(Tables.EH_VISITOR_SYS_CODING.DAY_MARK.eq(dayRemark))
				.and(Tables.EH_VISITOR_SYS_CODING.STATUS.eq((byte)2))
				.fetch().map(r -> ConvertHelper.convert(r, VisitorSysCoding.class));
		if(list==null||list.size()==0){
			return null;
		}
		return list.get(0);
	}

//	@Override
//	public VisitorSysCoding findVisitorSysCodingByRandomCode(String randomCode) {
//		List<VisitorSysCoding> list = getReadOnlyContext().select().from(Tables.EH_VISITOR_SYS_CODING)
//				.where(Tables.EH_VISITOR_SYS_CODING.RANDOM_CODE.eq(randomCode))
//				.and(Tables.EH_VISITOR_SYS_CODING.STATUS.eq((byte)2))
//				.fetch().map(r -> ConvertHelper.convert(r, VisitorSysCoding.class));
//		if(list==null||list.size()==0){
//			return null;
//		}
//		return list.get(0);
//	}

	private EhVisitorSysCodingDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhVisitorSysCodingDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhVisitorSysCodingDao getDao(DSLContext context) {
		return new EhVisitorSysCodingDao(context.configuration());
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
