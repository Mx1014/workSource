// @formatter:off
package com.everhomes.salary;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.rest.salary.SalaryEditableFlag;
import com.everhomes.rest.salary.SalaryEntityType;
import com.everhomes.rest.salary.SalaryNeedCheckType;
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
        Long uid = UserContext.currentUserId() == null ? 0 : UserContext.currentUserId();
        salaryGroupEntity.setId(id);
        salaryGroupEntity.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        salaryGroupEntity.setCreatorUid(uid);
        salaryGroupEntity.setUpdateTime(salaryGroupEntity.getCreateTime());
        salaryGroupEntity.setOperatorUid(salaryGroupEntity.getCreatorUid());
        getReadWriteDao().insert(salaryGroupEntity);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhSalaryGroupEntities.class, null);
    }

    @Override
    public void updateSalaryGroupEntity(SalaryGroupEntity salaryGroupEntity) {
        assert (salaryGroupEntity.getId() != null);
        Long uid = UserContext.currentUserId() == null ? 0 : UserContext.currentUserId();
        salaryGroupEntity.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        salaryGroupEntity.setOperatorUid(uid);
        getReadWriteDao().update(salaryGroupEntity);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSalaryGroupEntities.class, salaryGroupEntity.getId());
    }

    @Override
    public SalaryGroupEntity findSalaryGroupEntityById(Long id) {
        assert (id != null);
        return ConvertHelper.convert(getReadOnlyDao().findById(id), SalaryGroupEntity.class);
    }

    @Override
    public List<SalaryGroupEntity> listSalaryGroupEntityByOrgId(Long organizationId) {
        return getReadOnlyContext().select().from(Tables.EH_SALARY_GROUP_ENTITIES)
                .where(Tables.EH_SALARY_GROUP_ENTITIES.ORGANIZATION_ID.eq(organizationId))
                .fetch().map(r -> {
                    return ConvertHelper.convert(r, SalaryGroupEntity.class);
                });
    }

    @Override
    public void deleteSalaryGroupEntity(SalaryGroupEntity entity) {
        getReadWriteDao().delete(entity);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSalaryGroupEntities.class, null);

    }

//	@Override
//	public List<SalaryGroupEntity> listSalaryGroupEntityByGroupId(Long salaryId) {
//        SelectQuery<Record> query = getReadOnlyContext().select(Tables.EH_SALARY_GROUP_ENTITIES.fields()).getQuery();
//        query.addFrom(Tables.EH_SALARY_GROUP_ENTITIES,Tables.EH_SALARY_DEFAULT_ENTITIES);
//        query.addConditions(Tables.EH_SALARY_GROUP_ENTITIES.ORIGIN_ENTITY_ID.eq(Tables.EH_SALARY_DEFAULT_ENTITIES.ID));
//
///*        return query.fetch().map(r ->{
//            return  ConvertHelper.convert(r, SalaryGroupEntity.class);
//        });*/
//
//        return  query.fetchInto(SalaryGroupEntity.class);
//    }
//
//
//	@Override
//	public List<SalaryGroupEntity> listSalaryGroupEntityByGroupId(Long organizationGroupId, Byte code) {
//		SelectQuery<Record> query = getReadOnlyContext().select(Tables.EH_SALARY_GROUP_ENTITIES.fields()).getQuery();
//		query.addFrom(Tables.EH_SALARY_GROUP_ENTITIES, Tables.EH_SALARY_DEFAULT_ENTITIES);
//		query.addConditions(Tables.EH_SALARY_GROUP_ENTITIES.ORIGIN_ENTITY_ID.eq(Tables.EH_SALARY_DEFAULT_ENTITIES.ID));
//
///*        return query.fetch().map(r ->{
//			return  ConvertHelper.convert(r, SalaryGroupEntity.class);
//        });*/
//
//		return query.fetchInto(SalaryGroupEntity.class);
//	}
//
//	@Override
//	public SalaryGroupEntity findSalaryGroupEntityByGroupAndOriginId(Long groupId, Long originEntityId) {
//		  List<SalaryGroupEntity> results = getReadOnlyContext().select().from(Tables.EH_SALARY_GROUP_ENTITIES)
//				.fetch().map(r -> ConvertHelper.convert(r, SalaryGroupEntity.class));
//		  if(StringUtils.isEmpty(results) || results.size() ==0 )
//		      return null;
//		  else
//		      return results.get(0);
//	}
//
//	@Override
//	public void deleteSalaryGroupEntityByGroupIdNotInOriginIds(Long salaryGroupId, List<Long> entityIds) {
//
//	}
//
//	//	按员工批次表导出规则查询
//    @Override
//    public List<SalaryGroupEntity> listSalaryGroupWithExportRegular(Long salaryGroupId) {
//        return getReadOnlyContext().select().from(Tables.EH_SALARY_GROUP_ENTITIES)
//                .where(Tables.EH_SALARY_GROUP_ENTITIES.EDITABLE_FLAG.eq(SalaryEditableFlag.EDITABLE.getCode()))
//                .fetch().map(r -> ConvertHelper.convert(r, SalaryGroupEntity.class));
//    }
//
//    //	按员工核算规则查询
//    @Override
//	public List<SalaryGroupEntity> listPeriodSalaryWithExportRegular(Long salaryGroupId){
//
//		SelectQuery<Record> query = getReadOnlyContext().select().getQuery();
///*		query.addConditions(Tables.EH_SALARY_GROUP_ENTITIES.GROUP_ID.eq(salaryGroupId));
//		query.addConditions(Tables.EH_SALARY_GROUP_ENTITIES.TYPE.eq(SalaryEntityType.TEXT.getCode())
//				.and(Tables.EH_SALARY_GROUP_ENTITIES.EDITABLE_FLAG.eq(SalaryEditableFlag.EDITABLE.getCode())));
//		query.addConditions(Tables.EH_SALARY_GROUP_ENTITIES.TYPE.eq(SalaryEntityType.NUMBER.getCode())
//				.and(Tables.EH_SALARY_GROUP_ENTITIES.NEED_CHECK.eq(SalaryNeedCheckType.WANT.getCode())));*/
//		return query.fetchInto(SalaryGroupEntity.class);
//	}
//
//	@Override
//	public List<SalaryGroupEntity> listSalaryGroupEntity() {
//		return getReadOnlyContext().select().from(Tables.EH_SALARY_GROUP_ENTITIES)
//				.fetch().map(r -> ConvertHelper.convert(r, SalaryGroupEntity.class));
//	}
//
//	@Override
//	public void updateSalaryGroupEntityVisible(Long id, Byte visibleFlag) {
//	}
//
//	//  删除记录
//	@Override
//	public void deleteSalaryGroupEntityByGroupId(Long groupId){
//    }


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
