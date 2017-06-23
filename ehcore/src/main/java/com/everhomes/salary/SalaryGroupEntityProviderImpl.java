// @formatter:off
package com.everhomes.salary;

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
import com.everhomes.server.schema.tables.daos.EhSalaryGroupEntitiesDao;
import com.everhomes.server.schema.tables.pojos.EhSalaryGroupEntities;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SalaryGroupEntityProviderImpl implements SalaryGroupEntityProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSalaryGroupEntity(SalaryGroupEntity salaryGroupEntity) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSalaryGroupEntities.class));
		salaryGroupEntity.setId(id);
		salaryGroupEntity.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		salaryGroupEntity.setCreatorUid(UserContext.current().getUser().getId());
//		salaryGroupEntity.setUpdateTime(salaryGroupEntity.getCreateTime());
//		salaryGroupEntity.setOperatorUid(salaryGroupEntity.getCreatorUid());
		getReadWriteDao().insert(salaryGroupEntity);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSalaryGroupEntities.class, null);
	}

	@Override
	public void updateSalaryGroupEntity(SalaryGroupEntity salaryGroupEntity) {
		assert (salaryGroupEntity.getId() != null);
//		salaryGroupEntity.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		salaryGroupEntity.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(salaryGroupEntity);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSalaryGroupEntities.class, salaryGroupEntity.getId());
	}

	@Override
	public SalaryGroupEntity findSalaryGroupEntityById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SalaryGroupEntity.class);
	}
	
	@Override
	public List<SalaryGroupEntity> listSalaryGroupEntityByGroupId(Long salaryId) {
		return getReadOnlyContext().select().from(Tables.EH_SALARY_GROUP_ENTITIES)
				.where(Tables.EH_SALARY_GROUP_ENTITIES.GROUP_ID.eq(salaryId))
				.orderBy(Tables.EH_SALARY_GROUP_ENTITIES.DEFAULT_ORDER.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SalaryGroupEntity.class));
	}
	
	private EhSalaryGroupEntitiesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSalaryGroupEntitiesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSalaryGroupEntitiesDao getDao(DSLContext context) {
		return new EhSalaryGroupEntitiesDao(context.configuration());
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
