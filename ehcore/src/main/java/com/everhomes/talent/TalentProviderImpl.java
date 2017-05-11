// @formatter:off
package com.everhomes.talent;

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
import com.everhomes.server.schema.tables.daos.EhTalentsDao;
import com.everhomes.server.schema.tables.pojos.EhTalents;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class TalentProviderImpl implements TalentProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createTalent(Talent talent) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhTalents.class));
		talent.setId(id);
		talent.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		talent.setCreatorUid(UserContext.current().getUser().getId());
		talent.setUpdateTime(talent.getCreateTime());
		talent.setOperatorUid(talent.getCreatorUid());
		getReadWriteDao().insert(talent);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhTalents.class, null);
	}

	@Override
	public void updateTalent(Talent talent) {
		assert (talent.getId() != null);
		talent.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		talent.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(talent);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhTalents.class, talent.getId());
	}

	@Override
	public Talent findTalentById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), Talent.class);
	}
	
	@Override
	public List<Talent> listTalent() {
		return getReadOnlyContext().select().from(Tables.EH_TALENTS)
				.orderBy(Tables.EH_TALENTS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, Talent.class));
	}
	
	private EhTalentsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhTalentsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhTalentsDao getDao(DSLContext context) {
		return new EhTalentsDao(context.configuration());
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
