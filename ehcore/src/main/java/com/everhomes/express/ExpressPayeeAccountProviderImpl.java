// @formatter:off
package com.everhomes.express;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.paySDK.pojo.PayUserDTO;
import com.everhomes.rest.express.ExpressServiceErrorCode;
import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.order.OwnerType;
import com.everhomes.util.RuntimeErrorException;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhExpressPayeeAccountsDao;
import com.everhomes.server.schema.tables.pojos.EhExpressPayeeAccounts;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class ExpressPayeeAccountProviderImpl implements ExpressPayeeAccountProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExpressPayeeAccountProviderImpl.class);

	@Autowired
	public com.everhomes.paySDK.api.PayService sdkPayService;
	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createExpressPayeeAccount(ExpressPayeeAccount expressPayeeAccount) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhExpressPayeeAccounts.class));
		expressPayeeAccount.setId(id);
		expressPayeeAccount.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		expressPayeeAccount.setCreatorUid(UserContext.current().getUser().getId());
		expressPayeeAccount.setOperateTime(expressPayeeAccount.getCreateTime());
		expressPayeeAccount.setOperatorUid(expressPayeeAccount.getCreatorUid());
		getReadWriteDao().insert(expressPayeeAccount);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhExpressPayeeAccounts.class, null);
	}

	@Override
	public void updateExpressPayeeAccount(ExpressPayeeAccount expressPayeeAccount) {
		assert (expressPayeeAccount.getId() != null);
		expressPayeeAccount.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		expressPayeeAccount.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(expressPayeeAccount);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhExpressPayeeAccounts.class, expressPayeeAccount.getId());
	}

	@Override
	public List<ExpressPayeeAccount> findRepeatBusinessPayeeAccounts(Long id, Integer namespaceId, String ownerType, Long ownerId) {
		Condition condition = Tables.EH_EXPRESS_PAYEE_ACCOUNTS.NAMESPACE_ID.eq(namespaceId)
				.and(Tables.EH_EXPRESS_PAYEE_ACCOUNTS.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_EXPRESS_PAYEE_ACCOUNTS.OWNER_ID.eq(ownerId))
				.and(Tables.EH_EXPRESS_PAYEE_ACCOUNTS.STATUS.eq((byte)2));
		if(id!=null){
			condition=condition.and(Tables.EH_EXPRESS_PAYEE_ACCOUNTS.ID.notEqual(id));
		}
		return getReadOnlyContext().select().from(Tables.EH_EXPRESS_PAYEE_ACCOUNTS)
				.where(condition)
				.orderBy(Tables.EH_EXPRESS_PAYEE_ACCOUNTS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ExpressPayeeAccount.class));
	}

	@Override
	public ExpressPayeeAccount getPayeeAccountByOwner(Integer namespaceId, String ownerType, Long ownerId) {
		Condition condition = Tables.EH_EXPRESS_PAYEE_ACCOUNTS.NAMESPACE_ID.eq(namespaceId)
				.and(Tables.EH_EXPRESS_PAYEE_ACCOUNTS.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_EXPRESS_PAYEE_ACCOUNTS.OWNER_ID.eq(ownerId))
				.and(Tables.EH_EXPRESS_PAYEE_ACCOUNTS.STATUS.eq((byte)2));
		List<ExpressPayeeAccount> list = getReadOnlyContext().select().from(Tables.EH_EXPRESS_PAYEE_ACCOUNTS)
				.where(condition)
				.orderBy(Tables.EH_EXPRESS_PAYEE_ACCOUNTS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ExpressPayeeAccount.class));
		if(list == null || list.size()==0){
			return null;
		}
		return list.get(0);
	}

	@Override
	@Cacheable(value = "createPersonalPayUserIfAbsent", key="{#userId, #accountCode}", unless="#result == null")
	public ListBizPayeeAccountDTO createPersonalPayUserIfAbsent(String userId, String accountCode,String userIdenify, String tag1, String tag2, String tag3) {
		String payerid = OwnerType.USER.getCode()+userId;
		LOGGER.info("createPersonalPayUserIfAbsent payerid = {}, accountCode = {}, userIdenify={}",payerid,accountCode,userIdenify);
		PayUserDTO payUserList = sdkPayService.createPersonalPayUserIfAbsent(payerid, accountCode);
		if(payUserList==null){
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.ERROR_CREATE_USER_ACCOUNT,
					"创建个人付款方失败");
		}
		sdkPayService.bandPhone(payUserList.getId(), userIdenify);
		ListBizPayeeAccountDTO dto = new ListBizPayeeAccountDTO();
		dto.setAccountId(payUserList.getId());
		dto.setAccountType(payUserList.getUserType()==2? OwnerType.ORGANIZATION.getCode():OwnerType.USER.getCode());//帐号类型，1-个人帐号、2-企业帐号
		dto.setAccountName(payUserList.getUserName());
		dto.setAccountAliasName(payUserList.getUserAliasName());
		if(payUserList.getRegisterStatus()!=null) {
			dto.setAccountStatus(Byte.valueOf(payUserList.getRegisterStatus() + ""));
		}
		return dto;
	}

	@Override
	public ExpressPayeeAccount findExpressPayeeAccountById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ExpressPayeeAccount.class);
	}
	
	@Override
	public List<ExpressPayeeAccount> listExpressPayeeAccount() {
		return getReadOnlyContext().select().from(Tables.EH_EXPRESS_PAYEE_ACCOUNTS)
				.orderBy(Tables.EH_EXPRESS_PAYEE_ACCOUNTS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ExpressPayeeAccount.class));
	}
	
	private EhExpressPayeeAccountsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhExpressPayeeAccountsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhExpressPayeeAccountsDao getDao(DSLContext context) {
		return new EhExpressPayeeAccountsDao(context.configuration());
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
