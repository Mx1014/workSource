// @formatter:off
package com.everhomes.express;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.express.ExpressOwner;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhExpressHotlinesDao;
import com.everhomes.server.schema.tables.pojos.EhExpressHotlines;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class ExpressHotlineProviderImpl implements ExpressHotlineProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createExpressHotline(ExpressHotline expressHotline) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhExpressHotlines.class));
		expressHotline.setId(id);
		expressHotline.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		expressHotline.setCreatorUid(UserContext.current().getUser().getId());
		expressHotline.setUpdateTime(expressHotline.getCreateTime());
		expressHotline.setOperatorUid(expressHotline.getCreatorUid());
		getReadWriteDao().insert(expressHotline);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhExpressHotlines.class, null);
	}

	@Override
	public void updateExpressHotline(ExpressHotline expressHotline) {
		assert (expressHotline.getId() != null);
		expressHotline.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		expressHotline.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(expressHotline);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhExpressHotlines.class, expressHotline.getId());
	}

	@Override
	public ExpressHotline findExpressHotlineById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ExpressHotline.class);
	}
	
	@Override
	public List<ExpressHotline> listExpressHotline() {
		return getReadOnlyContext().select().from(Tables.EH_EXPRESS_HOTLINES)
				.orderBy(Tables.EH_EXPRESS_HOTLINES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ExpressHotline.class));
	}
	
	private EhExpressHotlinesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhExpressHotlinesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhExpressHotlinesDao getDao(DSLContext context) {
		return new EhExpressHotlinesDao(context.configuration());
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
	public List<ExpressHotline> listHotLinesByOwner(ExpressOwner owner, int pageSize, Long pageAnchor) {
		SelectConditionStep<?> query = getReadOnlyContext().select().from(Tables.EH_EXPRESS_HOTLINES)
		.where(Tables.EH_EXPRESS_HOTLINES.NAMESPACE_ID.eq(owner.getNamespaceId()))
		.and(Tables.EH_EXPRESS_HOTLINES.OWNER_TYPE.eq(owner.getOwnerType().getCode()))
		.and(Tables.EH_EXPRESS_HOTLINES.OWNER_ID.eq(owner.getOwnerId()))
		.and(Tables.EH_EXPRESS_HOTLINES.STATUS.eq(CommonStatus.ACTIVE.getCode()));
		if(pageAnchor!=null){
			query.and(Tables.EH_EXPRESS_HOTLINES.ID.lt(pageAnchor));
		}
		return query.orderBy(Tables.EH_EXPRESS_HOTLINES.ID.desc()).limit(pageSize)
		.fetch().map(r -> ConvertHelper.convert(r, ExpressHotline.class));
	}

	@Override
	public void updateExpressHotlineStatus(ExpressOwner owner, Long id) {
		getReadWriteContext().update(Tables.EH_EXPRESS_HOTLINES)
		.set(Tables.EH_EXPRESS_HOTLINES.STATUS,CommonStatus.INACTIVE.getCode())
		.where(Tables.EH_EXPRESS_HOTLINES.ID.eq(id)).execute();
	}
}
