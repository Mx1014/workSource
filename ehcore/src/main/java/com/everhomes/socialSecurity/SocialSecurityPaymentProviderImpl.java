// @formatter:off
package com.everhomes.socialSecurity;

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
import com.everhomes.server.schema.tables.daos.EhSocialSecurityPaymentsDao;
import com.everhomes.server.schema.tables.pojos.EhSocialSecurityPayments;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SocialSecurityPaymentProviderImpl implements SocialSecurityPaymentProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSocialSecurityPayment(SocialSecurityPayment socialSecurityPayment) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSocialSecurityPayments.class));
		socialSecurityPayment.setId(id);
		socialSecurityPayment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		socialSecurityPayment.setCreatorUid(UserContext.current().getUser().getId());
		socialSecurityPayment.setUpdateTime(socialSecurityPayment.getCreateTime());
		socialSecurityPayment.setOperatorUid(socialSecurityPayment.getCreatorUid());
		getReadWriteDao().insert(socialSecurityPayment);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSocialSecurityPayments.class, null);
	}

	@Override
	public void updateSocialSecurityPayment(SocialSecurityPayment socialSecurityPayment) {
		assert (socialSecurityPayment.getId() != null);
		socialSecurityPayment.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		socialSecurityPayment.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(socialSecurityPayment);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSocialSecurityPayments.class, socialSecurityPayment.getId());
	}

	@Override
	public SocialSecurityPayment findSocialSecurityPaymentById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SocialSecurityPayment.class);
	}
	
	@Override
	public List<SocialSecurityPayment> listSocialSecurityPayment() {
		return getReadOnlyContext().select().from(Tables.EH_SOCIAL_SECURITY_PAYMENTS)
				.orderBy(Tables.EH_SOCIAL_SECURITY_PAYMENTS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SocialSecurityPayment.class));
	}
	
	private EhSocialSecurityPaymentsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSocialSecurityPaymentsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSocialSecurityPaymentsDao getDao(DSLContext context) {
		return new EhSocialSecurityPaymentsDao(context.configuration());
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
