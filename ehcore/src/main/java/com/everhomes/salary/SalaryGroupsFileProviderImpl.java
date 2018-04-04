// @formatter:off
package com.everhomes.salary;

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
import com.everhomes.server.schema.tables.daos.EhSalaryGroupsFilesDao;
import com.everhomes.server.schema.tables.pojos.EhSalaryGroupsFiles;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SalaryGroupsFileProviderImpl implements SalaryGroupsFileProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createSalaryGroupsFile(SalaryGroupsFile salaryGroupsFile) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSalaryGroupsFiles.class));
        salaryGroupsFile.setId(id);
        salaryGroupsFile.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//        salaryGroupsFile.setCreatorUid(UserContext.currentUserId());
//		salaryGroupsFile.setUpdateTime(salaryGroupsFile.getCreateTime());
//		salaryGroupsFile.setOperatorUid(salaryGroupsFile.getCreatorUid());
        getReadWriteDao().insert(salaryGroupsFile);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhSalaryGroupsFiles.class, null);
    }

    @Override
    public void updateSalaryGroupsFile(SalaryGroupsFile salaryGroupsFile) {
        assert (salaryGroupsFile.getId() != null);
//		salaryGroupsFile.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		salaryGroupsFile.setOperatorUid(UserContext.current().getUser().getId());
        getReadWriteDao().update(salaryGroupsFile);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSalaryGroupsFiles.class, salaryGroupsFile.getId());
    }

    @Override
    public SalaryGroupsFile findSalaryGroupsFileById(Long id) {
        assert (id != null);
        return ConvertHelper.convert(getReadOnlyDao().findById(id), SalaryGroupsFile.class);
    }

    @Override
    public List<SalaryGroupsFile> listSalaryGroupsFile() {
        return getReadOnlyContext().select().from(Tables.EH_SALARY_GROUPS_FILES)
                .orderBy(Tables.EH_SALARY_GROUPS_FILES.ID.asc())
                .fetch().map(r -> ConvertHelper.convert(r, SalaryGroupsFile.class));
    }

    @Override
    public SalaryGroupsFile findSalaryGroupsFile(Long ownerId, String month) {
        Record r = getReadOnlyContext().select().from(Tables.EH_SALARY_GROUPS_FILES)
                .where(Tables.EH_SALARY_GROUPS_FILES.OWNER_ID.eq(ownerId))
                .and(Tables.EH_SALARY_GROUPS_FILES.SALARY_PERIOD.eq(month))
                .orderBy(Tables.EH_SALARY_GROUPS_FILES.ID.asc())
                .fetchAny();
        if (null == r) {
            return null;
        }
        return ConvertHelper.convert(r, SalaryGroupsFile.class);
    }

    @Override
    public void deleteGroupsFile(Long ownerId, String month) {
        getReadWriteContext().delete(Tables.EH_SALARY_GROUPS_FILES)
                .where(Tables.EH_SALARY_GROUPS_FILES.OWNER_ID.eq(ownerId))
                .and(Tables.EH_SALARY_GROUPS_FILES.SALARY_PERIOD.eq(month))
                .execute();
    }

    private EhSalaryGroupsFilesDao getReadWriteDao() {
        return getDao(getReadWriteContext());
    }

    private EhSalaryGroupsFilesDao getReadOnlyDao() {
        return getDao(getReadOnlyContext());
    }

    private EhSalaryGroupsFilesDao getDao(DSLContext context) {
        return new EhSalaryGroupsFilesDao(context.configuration());
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
