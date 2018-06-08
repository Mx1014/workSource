// @formatter:off
package com.everhomes.salary;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhSalaryPayslipsDao;
import com.everhomes.server.schema.tables.pojos.EhSalaryPayslips;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SalaryPayslipProviderImpl implements SalaryPayslipProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createSalaryPayslip(SalaryPayslip salaryPayslip) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSalaryPayslips.class));
        salaryPayslip.setId(id);
        salaryPayslip.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        salaryPayslip.setCreatorUid(UserContext.current().getUser().getId());
        salaryPayslip.setUpdateTime(salaryPayslip.getCreateTime());
        salaryPayslip.setOperatorUid(salaryPayslip.getCreatorUid());
        getReadWriteDao().insert(salaryPayslip);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhSalaryPayslips.class, null);
    }

    @Override
    public void updateSalaryPayslip(SalaryPayslip salaryPayslip) {
        assert (salaryPayslip.getId() != null);
//		salaryPayslip.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		salaryPayslip.setOperatorUid(UserContext.current().getUser().getId());
        getReadWriteDao().update(salaryPayslip);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSalaryPayslips.class, salaryPayslip.getId());
    }

    @Override
    public SalaryPayslip findSalaryPayslipById(Long id) {
        assert (id != null);
        return ConvertHelper.convert(getReadOnlyDao().findById(id), SalaryPayslip.class);
    }

    @Override
    public List<SalaryPayslip> listSalaryPayslip() {
        return getReadOnlyContext().select().from(Tables.EH_SALARY_PAYSLIPS)
                .orderBy(Tables.EH_SALARY_PAYSLIPS.ID.asc())
                .fetch().map(r -> ConvertHelper.convert(r, SalaryPayslip.class));
    }

    private EhSalaryPayslipsDao getReadWriteDao() {
        return getDao(getReadWriteContext());
    }

    private EhSalaryPayslipsDao getReadOnlyDao() {
        return getDao(getReadOnlyContext());
    }

    private EhSalaryPayslipsDao getDao(DSLContext context) {
        return new EhSalaryPayslipsDao(context.configuration());
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
    public List<SalaryPayslip> listSalaryPayslip(Long ownerId, Long organizationId,
                                                 String payslipYear, String name, CrossShardListingLocator locator, Integer pageSize) {
        SelectConditionStep<Record> step = getReadOnlyContext().select().from(Tables.EH_SALARY_PAYSLIPS)
                .where(Tables.EH_SALARY_PAYSLIPS.OWNER_ID.eq(ownerId));
        if (null != organizationId) {
            step = step.and(Tables.EH_SALARY_PAYSLIPS.ORGANIZATION_ID.in(organizationId));
        }
        if (null != payslipYear) {
            step = step.and(Tables.EH_SALARY_PAYSLIPS.SALARY_PERIOD.like(payslipYear + "%"));
        }

        if (null != name) {
            step = step.and(Tables.EH_SALARY_PAYSLIPS.NAME.like("%" + name + "%"));
        }

        if (null != locator && locator.getAnchor() != null) {
            step = step.and(Tables.EH_SALARY_PAYSLIP_DETAILS.ID.gt(locator.getAnchor()));
        }
        return step.orderBy(Tables.EH_SALARY_PAYSLIPS.ID.asc()).limit(pageSize).fetch().map(r -> ConvertHelper.convert(r, SalaryPayslip.class));

    }

    @Override
    public void deleteSalaryPayslip(Long payslipId) {
        getReadWriteDao().deleteById(payslipId);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSalaryPayslips.class, payslipId);
    }
}
