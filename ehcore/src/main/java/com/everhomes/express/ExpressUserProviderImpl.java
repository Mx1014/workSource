// @formatter:off
package com.everhomes.express;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.express.ExpressOwnerType;
import com.everhomes.rest.express.ListExpressUserCondition;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhExpressUsersDao;
import com.everhomes.server.schema.tables.pojos.EhExpressUsers;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class ExpressUserProviderImpl implements ExpressUserProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createExpressUser(ExpressUser expressUser) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhExpressUsers.class));
		expressUser.setId(id);
		expressUser.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		expressUser.setCreatorUid(UserContext.current().getUser().getId());
		expressUser.setUpdateTime(expressUser.getCreateTime());
		expressUser.setOperatorUid(expressUser.getCreatorUid());
		getReadWriteDao().insert(expressUser);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhExpressUsers.class, null);
	}

	@Override
	public void updateExpressUser(ExpressUser expressUser) {
		assert (expressUser.getId() != null);
		expressUser.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		expressUser.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(expressUser);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhExpressUsers.class, expressUser.getId());
	}

	@Override
	public ExpressUser findExpressUserById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ExpressUser.class);
	}
	
	@Override
	public ExpressUser findExpressUserByOrganizationMember(Integer namespaceId, String ownerType, Long ownerId,
			Long organizationId, Long organizationMemberId) {
		Record record = getReadOnlyContext().select().from(Tables.EH_EXPRESS_USERS)
			.where(Tables.EH_EXPRESS_USERS.NAMESPACE_ID.eq(namespaceId))
			.and(Tables.EH_EXPRESS_USERS.OWNER_TYPE.eq(ownerType))
			.and(Tables.EH_EXPRESS_USERS.OWNER_ID.eq(ownerId))
			.and(Tables.EH_EXPRESS_USERS.ORGANIZATION_ID.eq(organizationId))
			.and(Tables.EH_EXPRESS_USERS.ORGANIZATION_MEMBER_ID.eq(organizationMemberId))
			.fetchOne();
			
		return record == null ? null : ConvertHelper.convert(record, ExpressUser.class);
	}

	@Override
	public ExpressUser findExpressUserByUserId(Integer namespaceId, ExpressOwnerType ownerType, Long ownerId,
			Long userId, Long serviceAddressId, Long expressCompanyId) {
		Record record = getReadOnlyContext().select().from(Tables.EH_EXPRESS_USERS)
				.where(Tables.EH_EXPRESS_USERS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_EXPRESS_USERS.OWNER_TYPE.eq(ownerType.getCode()))
				.and(Tables.EH_EXPRESS_USERS.OWNER_ID.eq(ownerId))
				.and(Tables.EH_EXPRESS_USERS.USER_ID.eq(userId))
				.and(Tables.EH_EXPRESS_USERS.SERVICE_ADDRESS_ID.eq(serviceAddressId))
				.and(Tables.EH_EXPRESS_USERS.EXPRESS_COMPANY_ID.eq(expressCompanyId))
				.fetchOne();
				
		return record == null ? null : ConvertHelper.convert(record, ExpressUser.class);
	}

	@Override
	public List<ExpressUser> listExpressUser() {
		return getReadOnlyContext().select().from(Tables.EH_EXPRESS_USERS)
				.orderBy(Tables.EH_EXPRESS_USERS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ExpressUser.class));
	}
	
	@Override
	public List<ExpressUser> listExpressUserByCondition(ListExpressUserCondition condition) {
		SelectConditionStep<Record> step = getReadOnlyContext().select().from(Tables.EH_EXPRESS_USERS)
				.where(Tables.EH_EXPRESS_USERS.NAMESPACE_ID.eq(condition.getNamespaceId()))
				.and(Tables.EH_EXPRESS_USERS.OWNER_TYPE.eq(condition.getOwnerType()))
				.and(Tables.EH_EXPRESS_USERS.OWNER_ID.eq(condition.getOwnerId()))
				.and(Tables.EH_EXPRESS_USERS.STATUS.eq(CommonStatus.ACTIVE.getCode()));
		
		if (condition.getExpressCompanyId() != null) {
			step.and(Tables.EH_EXPRESS_USERS.EXPRESS_COMPANY_ID.eq(condition.getExpressCompanyId()));
		}
		
		if (condition.getServiceAddressId() != null) {
			step.and(Tables.EH_EXPRESS_USERS.SERVICE_ADDRESS_ID.eq(condition.getServiceAddressId()));
		}
		
		 return	step.and(condition.getPageAnchor()==null?DSL.trueCondition():Tables.EH_EXPRESS_USERS.ID.lt(condition.getPageAnchor()))
					.orderBy(Tables.EH_EXPRESS_USERS.ID.desc())
					.limit(condition.getPageSize()+1)
					.fetch().map(r -> ConvertHelper.convert(r, ExpressUser.class));
	}

	private EhExpressUsersDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhExpressUsersDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhExpressUsersDao getDao(DSLContext context) {
		return new EhExpressUsersDao(context.configuration());
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
