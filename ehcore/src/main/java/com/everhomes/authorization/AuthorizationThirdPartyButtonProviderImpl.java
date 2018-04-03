// @formatter:off
package com.everhomes.authorization;

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
import com.everhomes.server.schema.tables.daos.EhAuthorizationThirdPartyButtonsDao;
import com.everhomes.server.schema.tables.pojos.EhAuthorizationThirdPartyButtons;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class AuthorizationThirdPartyButtonProviderImpl implements AuthorizationThirdPartyButtonProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createAuthorizationThirdPartyButton(AuthorizationThirdPartyButton authorizationThirdPartyButton) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAuthorizationThirdPartyButtons.class));
		authorizationThirdPartyButton.setId(id);
		authorizationThirdPartyButton.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		authorizationThirdPartyButton.setCreatorUid(UserContext.current().getUser().getId());
		authorizationThirdPartyButton.setUpdateTime(authorizationThirdPartyButton.getCreateTime());
		authorizationThirdPartyButton.setOperatorUid(authorizationThirdPartyButton.getCreatorUid());
		getReadWriteDao().insert(authorizationThirdPartyButton);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhAuthorizationThirdPartyButtons.class, null);
	}

	@Override
	public void updateAuthorizationThirdPartyButton(AuthorizationThirdPartyButton authorizationThirdPartyButton) {
		assert (authorizationThirdPartyButton.getId() != null);
		authorizationThirdPartyButton.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		authorizationThirdPartyButton.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(authorizationThirdPartyButton);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAuthorizationThirdPartyButtons.class, authorizationThirdPartyButton.getId());
	}

	@Override
	public AuthorizationThirdPartyButton findAuthorizationThirdPartyButtonById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), AuthorizationThirdPartyButton.class);
	}
	
	@Override
	public List<AuthorizationThirdPartyButton> listAuthorizationThirdPartyButton() {
		return getReadOnlyContext().select().from(Tables.EH_AUTHORIZATION_THIRD_PARTY_BUTTONS)
				.orderBy(Tables.EH_AUTHORIZATION_THIRD_PARTY_BUTTONS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, AuthorizationThirdPartyButton.class));
	}
	
	private EhAuthorizationThirdPartyButtonsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhAuthorizationThirdPartyButtonsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhAuthorizationThirdPartyButtonsDao getDao(DSLContext context) {
		return new EhAuthorizationThirdPartyButtonsDao(context.configuration());
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
	public AuthorizationThirdPartyButton getButtonStatusByOwner(String ownerType, Long ownerId) {
		List<AuthorizationThirdPartyButton> list = getReadOnlyContext().select().from(Tables.EH_AUTHORIZATION_THIRD_PARTY_BUTTONS)
				.where(Tables.EH_AUTHORIZATION_THIRD_PARTY_BUTTONS.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_AUTHORIZATION_THIRD_PARTY_BUTTONS.OWNER_ID.eq(ownerId))
				.fetch().map(r -> ConvertHelper.convert(r, AuthorizationThirdPartyButton.class));
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
}
