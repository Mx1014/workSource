// @formatter:off
package com.everhomes.salary;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.user.User;
import org.jooq.*;
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
import org.springframework.util.StringUtils;

@Component
public class SalaryGroupEntityProviderImpl implements SalaryGroupEntityProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSalaryGroupEntity(SalaryGroupEntity salaryGroupEntity) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSalaryGroupEntities.class));
		User user = UserContext.current().getUser();
		salaryGroupEntity.setId(id);
		salaryGroupEntity.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		salaryGroupEntity.setCreatorUid(user.getId());
		salaryGroupEntity.setNamespaceId(user.getNamespaceId());
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
        SelectQuery<Record> query = getReadOnlyContext().select(Tables.EH_SALARY_GROUP_ENTITIES.fields()).getQuery();
        query.addFrom(Tables.EH_SALARY_GROUP_ENTITIES,Tables.EH_SALARY_DEFAULT_ENTITIES);
        query.addSelect(Tables.EH_SALARY_DEFAULT_ENTITIES.DEFAULT_FLAG);
        query.addConditions(Tables.EH_SALARY_GROUP_ENTITIES.GROUP_ID.eq(salaryId));
        query.addConditions(Tables.EH_SALARY_GROUP_ENTITIES.ORIGIN_ENTITY_ID.eq(Tables.EH_SALARY_DEFAULT_ENTITIES.ID));
        query.addOrderBy(Tables.EH_SALARY_GROUP_ENTITIES.DEFAULT_ORDER.asc());

/*        return query.fetch().map(r ->{
            return  ConvertHelper.convert(r, SalaryGroupEntity.class);
        });*/

        return  query.fetchInto(SalaryGroupEntity.class);
    }


	@Override
	public List<SalaryGroupEntity> listSalaryGroupEntityByGroupId(Long organizationGroupId, Byte code) {
		SelectQuery<Record> query = getReadOnlyContext().select(Tables.EH_SALARY_GROUP_ENTITIES.fields()).getQuery();
		query.addFrom(Tables.EH_SALARY_GROUP_ENTITIES, Tables.EH_SALARY_DEFAULT_ENTITIES);
		query.addSelect(Tables.EH_SALARY_DEFAULT_ENTITIES.DEFAULT_FLAG);
		query.addConditions(Tables.EH_SALARY_GROUP_ENTITIES.GROUP_ID.eq(organizationGroupId));
		query.addConditions(Tables.EH_SALARY_GROUP_ENTITIES.VISIBLE_FLAG.eq(code));
		query.addConditions(Tables.EH_SALARY_GROUP_ENTITIES.ORIGIN_ENTITY_ID.eq(Tables.EH_SALARY_DEFAULT_ENTITIES.ID));
		query.addOrderBy(Tables.EH_SALARY_GROUP_ENTITIES.DEFAULT_ORDER.asc());

/*        return query.fetch().map(r ->{
			return  ConvertHelper.convert(r, SalaryGroupEntity.class);
        });*/

		return query.fetchInto(SalaryGroupEntity.class);
	}

	@Override
	public SalaryGroupEntity findSalaryGroupEntityByGroupAndOriginId(Long groupId, Long originEntityId) {
		  List<SalaryGroupEntity> results = getReadOnlyContext().select().from(Tables.EH_SALARY_GROUP_ENTITIES)
				.where(Tables.EH_SALARY_GROUP_ENTITIES.GROUP_ID.eq(groupId))
				.and(Tables.EH_SALARY_GROUP_ENTITIES.ORIGIN_ENTITY_ID.eq(originEntityId))
				.orderBy(Tables.EH_SALARY_GROUP_ENTITIES.DEFAULT_ORDER.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SalaryGroupEntity.class));
		  if(StringUtils.isEmpty(results) || results.size() ==0 )
		      return null;
		  else
		      return results.get(0);
	}

	@Override
	public void deleteSalaryGroupEntityByGroupIdNotInOriginIds(Long salaryGroupId, List<Long> entityIds) {
		getReadWriteContext().delete(Tables.EH_SALARY_GROUP_ENTITIES)
				.where(Tables.EH_SALARY_GROUP_ENTITIES.GROUP_ID.eq(salaryGroupId))
				.and(Tables.EH_SALARY_GROUP_ENTITIES.ORIGIN_ENTITY_ID.notIn(entityIds)).execute();

	}

	//	按员工批次表导出规则查询
    @Override
    public List<SalaryGroupEntity> listSalaryGroupWithExportRegular(Long salaryId) {
        return getReadOnlyContext().select().from(Tables.EH_SALARY_GROUP_ENTITIES)
                .where(Tables.EH_SALARY_GROUP_ENTITIES.GROUP_ID.eq(salaryId))
                .and(Tables.EH_SALARY_GROUP_ENTITIES.EDITABLE_FLAG.eq(Byte.valueOf("1")))
                .orderBy(Tables.EH_SALARY_GROUP_ENTITIES.DEFAULT_ORDER.asc())
                .fetch().map(r -> ConvertHelper.convert(r, SalaryGroupEntity.class));
    }

	@Override
	public List<SalaryGroupEntity> listSalaryGroupEntity() {
		return getReadOnlyContext().select().from(Tables.EH_SALARY_GROUP_ENTITIES)
				.orderBy(Tables.EH_SALARY_GROUP_ENTITIES.DEFAULT_ORDER.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SalaryGroupEntity.class));
	}

	@Override
	public void updateSalaryGroupEntityVisible(Long id, Byte visibleFlag) {
		getReadWriteContext().update(Tables.EH_SALARY_GROUP_ENTITIES).set(Tables.EH_SALARY_GROUP_ENTITIES.VISIBLE_FLAG, visibleFlag)
				.where(Tables.EH_SALARY_GROUP_ENTITIES.ID.eq(id)).execute();
	}

	//  删除记录
	@Override
	public void deleteSalaryGroupEntityByGroupId(Long groupId){
        DSLContext context = this.getContext(AccessSpec.readWrite());
        context.delete(Tables.EH_SALARY_GROUP_ENTITIES)
                .where(Tables.EH_SALARY_GROUP_ENTITIES.GROUP_ID.eq(groupId))
                .execute();
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
