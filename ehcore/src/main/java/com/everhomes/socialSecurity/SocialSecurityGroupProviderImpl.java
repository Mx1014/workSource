// @formatter:off
package com.everhomes.socialSecurity;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhSocialSecurityGroupsDao;
import com.everhomes.server.schema.tables.pojos.EhSocialSecurityGroups;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SocialSecurityGroupProviderImpl implements SocialSecurityGroupProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSocialSecurityGroup(SocialSecurityGroup socialSecurityGroup) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSocialSecurityGroups.class));
		socialSecurityGroup.setId(id);
		socialSecurityGroup.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		socialSecurityGroup.setCreatorUid(UserContext.currentUserId());
		socialSecurityGroup.setUpdateTime(socialSecurityGroup.getCreateTime());
		socialSecurityGroup.setOperatorUid(socialSecurityGroup.getCreatorUid());
		getReadWriteDao().insert(socialSecurityGroup);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSocialSecurityGroups.class, null);
	}

	@Override
	public void updateSocialSecurityGroup(SocialSecurityGroup socialSecurityGroup) {
		assert (socialSecurityGroup.getId() != null);
		socialSecurityGroup.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		socialSecurityGroup.setOperatorUid(UserContext.currentUserId());
		getReadWriteDao().update(socialSecurityGroup);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSocialSecurityGroups.class, socialSecurityGroup.getId());
	}

	@Override
	public SocialSecurityGroup findSocialSecurityGroupById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SocialSecurityGroup.class);
	}
	
	@Override
	public List<SocialSecurityGroup> listSocialSecurityGroup() {
		return getReadOnlyContext().select().from(Tables.EH_SOCIAL_SECURITY_GROUPS)
				.orderBy(Tables.EH_SOCIAL_SECURITY_GROUPS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SocialSecurityGroup.class));
	}

	@Override
	public void deleteGroup(Long ownerId, String month) {
		getReadWriteContext().delete(Tables.EH_SOCIAL_SECURITY_GROUPS)
				.where(Tables.EH_SOCIAL_SECURITY_GROUPS.ORGANIZATION_ID.eq(ownerId))
				.and(Tables.EH_SOCIAL_SECURITY_GROUPS.PAY_MONTH.eq(month)).execute();
	}

	@Override
	public SocialSecurityGroup findSocialSecurityGroupByOrg(Long ownerId, String payMonth) {
		Record r = getReadOnlyContext().select().from(Tables.EH_SOCIAL_SECURITY_GROUPS)
				.where(Tables.EH_SOCIAL_SECURITY_GROUPS.ORGANIZATION_ID.eq(ownerId))
				.and(Tables.EH_SOCIAL_SECURITY_GROUPS.PAY_MONTH.eq(payMonth))
				.orderBy(Tables.EH_SOCIAL_SECURITY_GROUPS.ID.asc())
				.fetchAny();
		if (null == r) {
			return null;
		}
		return ConvertHelper.convert(r, SocialSecurityGroup.class);
	}

	private EhSocialSecurityGroupsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSocialSecurityGroupsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSocialSecurityGroupsDao getDao(DSLContext context) {
		return new EhSocialSecurityGroupsDao(context.configuration());
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
