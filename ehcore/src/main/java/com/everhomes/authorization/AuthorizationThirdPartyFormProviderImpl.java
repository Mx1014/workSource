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
import com.everhomes.server.schema.tables.daos.EhAuthorizationThirdPartyFormsDao;
import com.everhomes.server.schema.tables.pojos.EhAuthorizationThirdPartyForms;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class AuthorizationThirdPartyFormProviderImpl implements AuthorizationThirdPartyFormProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createAuthorizationThirdPartyForm(AuthorizationThirdPartyForm authorizationThirdPartyForm) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAuthorizationThirdPartyForms.class));
		authorizationThirdPartyForm.setId(id);
		authorizationThirdPartyForm.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		authorizationThirdPartyForm.setCreatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().insert(authorizationThirdPartyForm);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhAuthorizationThirdPartyForms.class, null);
	}

	@Override
	public void updateAuthorizationThirdPartyForm(AuthorizationThirdPartyForm authorizationThirdPartyForm) {
		assert (authorizationThirdPartyForm.getId() != null);
		getReadWriteDao().update(authorizationThirdPartyForm);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAuthorizationThirdPartyForms.class, authorizationThirdPartyForm.getId());
	}

	@Override
	public AuthorizationThirdPartyForm findAuthorizationThirdPartyFormById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), AuthorizationThirdPartyForm.class);
	}
	
	@Override
	public List<AuthorizationThirdPartyForm> listAuthorizationThirdPartyForm() {
		return getReadOnlyContext().select().from(Tables.EH_AUTHORIZATION_THIRD_PARTY_FORMS)
				.orderBy(Tables.EH_AUTHORIZATION_THIRD_PARTY_FORMS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, AuthorizationThirdPartyForm.class));
	}
	
	private EhAuthorizationThirdPartyFormsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhAuthorizationThirdPartyFormsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhAuthorizationThirdPartyFormsDao getDao(DSLContext context) {
		return new EhAuthorizationThirdPartyFormsDao(context.configuration());
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
	public List<AuthorizationThirdPartyForm> listFormSourceByOwner(String ownerType,Long ownerId) {
		return getReadOnlyContext().select().from(Tables.EH_AUTHORIZATION_THIRD_PARTY_FORMS)
		.where(Tables.EH_AUTHORIZATION_THIRD_PARTY_FORMS.OWNER_TYPE.eq(ownerType))
		.and(Tables.EH_AUTHORIZATION_THIRD_PARTY_FORMS.OWNER_ID.eq(ownerId))
		.fetch().map(r -> ConvertHelper.convert(r, AuthorizationThirdPartyForm.class));
	}
}
