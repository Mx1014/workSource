// @formatter:off
package com.everhomes.salary;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.salary.PayslipDetailStatus;
import com.everhomes.rest.techpark.punch.NormalFlag;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SelectConditionStep;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhSalaryPayslipDetailsDao;
import com.everhomes.server.schema.tables.pojos.EhSalaryPayslipDetails;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SalaryPayslipDetailProviderImpl implements SalaryPayslipDetailProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SalaryPayslipDetailProviderImpl.class);

    @Override
    public void createSalaryPayslipDetail(SalaryPayslipDetail salaryPayslipDetail) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSalaryPayslipDetails.class));
        salaryPayslipDetail.setId(id);
        salaryPayslipDetail.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        salaryPayslipDetail.setCreatorUid(UserContext.current().getUser().getId());
        salaryPayslipDetail.setUpdateTime(salaryPayslipDetail.getCreateTime());
        salaryPayslipDetail.setOperatorUid(salaryPayslipDetail.getCreatorUid());
        getReadWriteDao().insert(salaryPayslipDetail);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhSalaryPayslipDetails.class, null);
    }

    @Override
    public void updateSalaryPayslipDetail(SalaryPayslipDetail salaryPayslipDetail) {
        assert (salaryPayslipDetail.getId() != null);
        salaryPayslipDetail.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        salaryPayslipDetail.setOperatorUid(UserContext.current().getUser().getId());
        getReadWriteDao().update(salaryPayslipDetail);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSalaryPayslipDetails.class, salaryPayslipDetail.getId());
    }

    @Override
    public SalaryPayslipDetail findSalaryPayslipDetailById(Long id) {
        assert (id != null);
        return ConvertHelper.convert(getReadOnlyDao().findById(id), SalaryPayslipDetail.class);
    }

    @Override
    public List<SalaryPayslipDetail> listSalaryPayslipDetail() {
        return getReadOnlyContext().select().from(Tables.EH_SALARY_PAYSLIP_DETAILS)
                .orderBy(Tables.EH_SALARY_PAYSLIP_DETAILS.ID.asc())
                .fetch().map(r -> ConvertHelper.convert(r, SalaryPayslipDetail.class));
    }

    private EhSalaryPayslipDetailsDao getReadWriteDao() {
        return getDao(getReadWriteContext());
    }

    private EhSalaryPayslipDetailsDao getReadOnlyDao() {
        return getDao(getReadOnlyContext());
    }

    private EhSalaryPayslipDetailsDao getDao(DSLContext context) {
        return new EhSalaryPayslipDetailsDao(context.configuration());
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
    public Integer countSend(Long payslipId) {
        Record1<Integer> record = getReadOnlyContext().select(Tables.EH_SALARY_PAYSLIP_DETAILS.ID.count())
                .from(Tables.EH_SALARY_PAYSLIP_DETAILS)
                .where(Tables.EH_SALARY_PAYSLIP_DETAILS.PAYSLIP_ID.eq(payslipId))
//                .and(Tables.EH_SALARY_PAYSLIP_DETAILS.STATUS.ne(PayslipDetailStatus.REVOKED.getCode()))
                .fetchAny();

        if (null == record) {
            return 0;
        }
        return record.value1();
    }

    @Override
    public Integer countConfirm(Long payslipId) {
        Record1<Integer> record = getReadOnlyContext().select(Tables.EH_SALARY_PAYSLIP_DETAILS.ID.count())
                .from(Tables.EH_SALARY_PAYSLIP_DETAILS)
                .where(Tables.EH_SALARY_PAYSLIP_DETAILS.PAYSLIP_ID.eq(payslipId))
                .and(Tables.EH_SALARY_PAYSLIP_DETAILS.STATUS.eq(PayslipDetailStatus.CONFIRMED.getCode()))
                .fetchAny();

        if (null == record) {
            return 0;
        }
        return record.value1();
    }

    @Override
    public Integer countView(Long payslipId) {
        Record1<Integer> record = getReadOnlyContext().select(Tables.EH_SALARY_PAYSLIP_DETAILS.ID.count())
                .from(Tables.EH_SALARY_PAYSLIP_DETAILS)
                .where(Tables.EH_SALARY_PAYSLIP_DETAILS.PAYSLIP_ID.eq(payslipId))
                .and(Tables.EH_SALARY_PAYSLIP_DETAILS.VIEWED_FLAG.eq(NormalFlag.YES.getCode()))
                .fetchAny();

        if (null == record) {
            return 0;
        }
        return record.value1();
    }

    @Override
    public Integer countRevoke(Long payslipId) {
        Record1<Integer> record = getReadOnlyContext().select(Tables.EH_SALARY_PAYSLIP_DETAILS.ID.count())
                .from(Tables.EH_SALARY_PAYSLIP_DETAILS)
                .where(Tables.EH_SALARY_PAYSLIP_DETAILS.PAYSLIP_ID.eq(payslipId))
                .and(Tables.EH_SALARY_PAYSLIP_DETAILS.STATUS.eq(PayslipDetailStatus.REVOKED.getCode()))
                .fetchAny();

        if (null == record) {
            return 0;
        }
        return record.value1();
    }

    @Override
    public List<SalaryPayslipDetail> listSalaryPayslipDetail(Long organizationId, Long ownerId,Long payslipId, String name,
                                                             Byte status, CrossShardListingLocator locator, Integer pageSize) {
        SelectConditionStep<Record> step = getReadOnlyContext().select().from(Tables.EH_SALARY_PAYSLIP_DETAILS)
                .where(Tables.EH_SALARY_PAYSLIP_DETAILS.OWNER_ID.eq(ownerId));
        if (null != payslipId) {
            step = step.and(Tables.EH_SALARY_PAYSLIP_DETAILS.PAYSLIP_ID.eq(payslipId));
        }
        if (null != name) {
            step = step.and(Tables.EH_SALARY_PAYSLIP_DETAILS.NAME.like("%" + name + "%"));
        }

        if (null != status) {
            step = step.and(Tables.EH_SALARY_PAYSLIP_DETAILS.STATUS.eq(status));
        }
        if(null != locator && locator.getAnchor()!= null){
            step = step.and(Tables.EH_SALARY_PAYSLIP_DETAILS.ID.gt(locator.getAnchor()));
        }
        return step.orderBy(Tables.EH_SALARY_PAYSLIP_DETAILS.ID.asc()).limit(pageSize).fetch().map(r -> ConvertHelper.convert(r, SalaryPayslipDetail.class));
    }

    @Override
    public List<SalaryPayslipDetail> listSalaryPayslipDetailBypayslipId(Long payslipId) {
        SelectConditionStep<Record> step = getReadOnlyContext().select().from(Tables.EH_SALARY_PAYSLIP_DETAILS)
                .where(Tables.EH_SALARY_PAYSLIP_DETAILS.PAYSLIP_ID.in(payslipId));
        return step.orderBy(Tables.EH_SALARY_PAYSLIP_DETAILS.ID.asc()).fetch().map(r -> ConvertHelper.convert(r, SalaryPayslipDetail.class));

    }

    @Override
    public void deleteSalaryPayslipDetail(SalaryPayslipDetail spd) {
        getReadWriteDao().deleteById(spd.getId());
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSalaryPayslipDetails.class, spd.getId());
    }

    @Override
    public List<SalaryPayslipDetail> listUserSalaryPayslipDetail(Long userId, Long organizationId) {
        SelectConditionStep<Record> step = getReadOnlyContext().select().from(Tables.EH_SALARY_PAYSLIP_DETAILS)
                .where(Tables.EH_SALARY_PAYSLIP_DETAILS.USER_ID.in(userId))
                .and(Tables.EH_SALARY_PAYSLIP_DETAILS.ORGANIZATION_ID.eq(organizationId))
                .and(Tables.EH_SALARY_PAYSLIP_DETAILS.STATUS.ne(PayslipDetailStatus.REVOKED.getCode()));
        return step.orderBy(Tables.EH_SALARY_PAYSLIP_DETAILS.ID.asc()).fetch().map(r -> ConvertHelper.convert(r, SalaryPayslipDetail.class));

    }

    @Override
    public void confirmSalaryPayslipDetailBeforeDate(Timestamp timestamp) {
        getReadWriteContext().update(Tables.EH_SALARY_PAYSLIP_DETAILS)
                .set(Tables.EH_SALARY_PAYSLIP_DETAILS.STATUS, PayslipDetailStatus.CONFIRMED.getCode())
                .where(Tables.EH_SALARY_PAYSLIP_DETAILS.CREATE_TIME.lessOrEqual(timestamp))
                .and(Tables.EH_SALARY_PAYSLIP_DETAILS.STATUS.ne(PayslipDetailStatus.REVOKED.getCode()))
                .execute();

    }
}
