// @formatter:off
package com.everhomes.salary;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.server.schema.tables.records.EhSalaryDefaultEntitiesRecord;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhSalaryDefaultEntitiesDao;
import com.everhomes.server.schema.tables.pojos.EhSalaryDefaultEntities;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SalaryDefaultEntityProviderImpl implements SalaryDefaultEntityProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSalaryDefaultEntity(SalaryDefaultEntity salaryDefaultEntity) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSalaryDefaultEntities.class));
		salaryDefaultEntity.setId(id);
		salaryDefaultEntity.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		salaryDefaultEntity.setCreatorUid(UserContext.current().getUser().getId());
//		salaryDefaultEntity.setUpdateTime(salaryDefaultEntity.getCreateTime());
//		salaryDefaultEntity.setOperatorUid(salaryDefaultEntity.getCreatorUid());
		getReadWriteDao().insert(salaryDefaultEntity);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSalaryDefaultEntities.class, null);
	}

	@Override
	public void updateSalaryDefaultEntity(SalaryDefaultEntity salaryDefaultEntity) {
		assert (salaryDefaultEntity.getId() != null);
//		salaryDefaultEntity.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		salaryDefaultEntity.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(salaryDefaultEntity);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSalaryDefaultEntities.class, salaryDefaultEntity.getId());
	}

	@Override
	public SalaryDefaultEntity findSalaryDefaultEntityById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SalaryDefaultEntity.class);
	}
	
	@Override
	public List<SalaryDefaultEntity> listSalaryDefaultEntity() {
	    /*
	    * DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record> result = context.select().from(Tables.EH_ORGANIZATIONS)
			.where(Tables.EH_ORGANIZATIONS.NAMESPACE_ID.eq(namespaceId))
			.and(Tables.EH_ORGANIZATIONS.UPDATE_TIME.gt(new Timestamp(timestamp)))
			.orderBy(Tables.EH_ORGANIZATIONS.UPDATE_TIME.asc(), Tables.EH_ORGANIZATIONS.ID.asc())
			.limit(pageSize)
			.fetch();*/
	    DSLContext context = getReadOnlyContext();
	    List<SalaryDefaultEntity> entities = context.select().from(Tables.EH_SALARY_DEFAULT_ENTITIES)
                .orderBy(Tables.EH_SALARY_DEFAULT_ENTITIES.DEFAULT_ORDER.asc())
                .fetch().map(r -> ConvertHelper.convert(r, SalaryDefaultEntity.class));
		return entities;
	}
	
	private EhSalaryDefaultEntitiesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSalaryDefaultEntitiesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSalaryDefaultEntitiesDao getDao(DSLContext context) {
		return new EhSalaryDefaultEntitiesDao(context.configuration());
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
