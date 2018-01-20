// @formatter:off
package com.everhomes.socialSecurity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.SelectHavingStep;
import org.slf4j.Logger;
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
import com.everhomes.server.schema.tables.daos.EhSocialSecurityReportDao;
import com.everhomes.server.schema.tables.pojos.EhSocialSecurityReport;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SocialSecurityReportProviderImpl implements SocialSecurityReportProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocialSecurityReportProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createSocialSecurityReport(SocialSecurityReport socialSecurityReport) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSocialSecurityReport.class));
        socialSecurityReport.setId(id);
        socialSecurityReport.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		socialSecurityReport.setCreatorUid(UserContext.current().getUser().getId());
        socialSecurityReport.setUpdateTime(socialSecurityReport.getCreateTime());
        socialSecurityReport.setOperatorUid(socialSecurityReport.getCreatorUid());
        getReadWriteDao().insert(socialSecurityReport);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhSocialSecurityReport.class, null);
    }

    @Override
    public void updateSocialSecurityReport(SocialSecurityReport socialSecurityReport) {
        assert (socialSecurityReport.getId() != null);
        socialSecurityReport.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        socialSecurityReport.setOperatorUid(UserContext.current().getUser().getId());
        getReadWriteDao().update(socialSecurityReport);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSocialSecurityReport.class, socialSecurityReport.getId());
    }

    @Override
    public SocialSecurityReport findSocialSecurityReportById(Long id) {
        assert (id != null);
        return ConvertHelper.convert(getReadOnlyDao().findById(id), SocialSecurityReport.class);
    }

    @Override
    public List<SocialSecurityReport> listSocialSecurityReport() {
        return getReadOnlyContext().select().from(Tables.EH_SOCIAL_SECURITY_REPORT)
                .orderBy(Tables.EH_SOCIAL_SECURITY_REPORT.ID.asc())
                .fetch().map(r -> ConvertHelper.convert(r, SocialSecurityReport.class));
    }

    @Override
    public void deleteSocialSecurityReports(Long ownerId, String payMonth) {
        getReadWriteContext().delete(Tables.EH_SOCIAL_SECURITY_REPORT)
                .where(Tables.EH_SOCIAL_SECURITY_REPORT.ORGANIZATION_ID.eq(ownerId))
                .and(Tables.EH_SOCIAL_SECURITY_REPORT.PAY_MONTH.eq(payMonth)).execute();
    }

    @Override
    public SocialSecurityDepartmentSummary calculateSocialSecurityDepartmentSummary(List<Long> detailIds, String month) {
        SelectHavingStep<Record> step = getReadOnlyContext().select(
                Tables.EH_SOCIAL_SECURITY_REPORT.SOCIAL_SECURITY_SUM.sum(),
                Tables.EH_SOCIAL_SECURITY_REPORT.SOCIAL_SECURITY_COMPANY_SUM.sum(),
                Tables.EH_SOCIAL_SECURITY_REPORT.SOCIAL_SECURITY_EMPLOYEE_SUM.sum(),
                Tables.EH_SOCIAL_SECURITY_REPORT.PENSION_COMPANY_SUM.sum(), Tables.EH_SOCIAL_SECURITY_REPORT.PENSION_EMPLOYEE_SUM.sum(),
                Tables.EH_SOCIAL_SECURITY_REPORT.MEDICAL_COMPANY_SUM.sum(), Tables.EH_SOCIAL_SECURITY_REPORT.MEDICAL_EMPLOYEE_SUM.sum(),
                Tables.EH_SOCIAL_SECURITY_REPORT.INJURY_COMPANY_SUM.sum(), Tables.EH_SOCIAL_SECURITY_REPORT.INJURY_EMPLOYEE_SUM.sum(),
                Tables.EH_SOCIAL_SECURITY_REPORT.UNEMPLOYMENT_COMPANY_SUM.sum(), Tables.EH_SOCIAL_SECURITY_REPORT.UNEMPLOYMENT_EMPLOYEE_SUM.sum(),
                Tables.EH_SOCIAL_SECURITY_REPORT.BIRTH_COMPANY_SUM.sum(), Tables.EH_SOCIAL_SECURITY_REPORT.BIRTH_EMPLOYEE_SUM.sum(),
                Tables.EH_SOCIAL_SECURITY_REPORT.CRITICAL_ILLNESS_COMPANY_SUM.sum(), Tables.EH_SOCIAL_SECURITY_REPORT.CRITICAL_ILLNESS_EMPLOYEE_SUM.sum(),
                Tables.EH_SOCIAL_SECURITY_REPORT.AFTER_SOCIAL_SECURITY_COMPANY_SUM.sum(), Tables.EH_SOCIAL_SECURITY_REPORT.AFTER_SOCIAL_SECURITY_EMPLOYEE_SUM.sum(),
                Tables.EH_SOCIAL_SECURITY_REPORT.AFTER_PENSION_COMPANY_SUM.sum(), Tables.EH_SOCIAL_SECURITY_REPORT.AFTER_PENSION_EMPLOYEE_SUM.sum(),
                Tables.EH_SOCIAL_SECURITY_REPORT.AFTER_MEDICAL_COMPANY_SUM.sum(), Tables.EH_SOCIAL_SECURITY_REPORT.AFTER_MEDICAL_EMPLOYEE_SUM.sum(),
                Tables.EH_SOCIAL_SECURITY_REPORT.AFTER_INJURY_COMPANY_SUM.sum(), Tables.EH_SOCIAL_SECURITY_REPORT.AFTER_INJURY_EMPLOYEE_SUM.sum(),
                Tables.EH_SOCIAL_SECURITY_REPORT.AFTER_UNEMPLOYMENT_COMPANY_SUM.sum(), Tables.EH_SOCIAL_SECURITY_REPORT.AFTER_UNEMPLOYMENT_EMPLOYEE_SUM.sum(),
                Tables.EH_SOCIAL_SECURITY_REPORT.AFTER_BIRTH_COMPANY_SUM.sum(), Tables.EH_SOCIAL_SECURITY_REPORT.AFTER_BIRTH_EMPLOYEE_SUM.sum(),
                Tables.EH_SOCIAL_SECURITY_REPORT.AFTER_CRITICAL_ILLNESS_COMPANY_SUM.sum(), Tables.EH_SOCIAL_SECURITY_REPORT.AFTER_CRITICAL_ILLNESS_EMPLOYEE_SUM.sum(),
                Tables.EH_SOCIAL_SECURITY_REPORT.DISABILITY_SUM.sum(), Tables.EH_SOCIAL_SECURITY_REPORT.COMMERCIAL_INSURANCE.sum(),
                Tables.EH_SOCIAL_SECURITY_REPORT.ACCUMULATION_FUND_SUM.sum(), Tables.EH_SOCIAL_SECURITY_REPORT.ACCUMULATION_FUND_COMPANY_SUM.sum(),
                Tables.EH_SOCIAL_SECURITY_REPORT.ACCUMULATION_FUND_EMPLOYEE_SUM.sum(), Tables.EH_SOCIAL_SECURITY_REPORT.AFTER_ACCUMULATION_FUND_COMPANY_SUM.sum(),
                Tables.EH_SOCIAL_SECURITY_REPORT.AFTER_ACCUMULATION_FUND_EMPLOYEE_SUM.sum(), Tables.EH_SOCIAL_SECURITY_REPORT.CREATOR_UID.max(),
                Tables.EH_SOCIAL_SECURITY_REPORT.CREATE_TIME.max(), Tables.EH_SOCIAL_SECURITY_REPORT.FILE_UID.max(), Tables.EH_SOCIAL_SECURITY_REPORT.FILE_TIME.max(),
                Tables.EH_SOCIAL_SECURITY_REPORT.ORGANIZATION_ID,
                Tables.EH_SOCIAL_SECURITY_REPORT.DETAIL_ID.countDistinct()
        ).from(Tables.EH_SOCIAL_SECURITY_REPORT).where(Tables.EH_SOCIAL_SECURITY_REPORT.DETAIL_ID.in(detailIds))
                .and(Tables.EH_SOCIAL_SECURITY_REPORT.PAY_MONTH.eq(month)).groupBy(
//                        Tables.EH_SOCIAL_SECURITY_REPORT.CREATOR_UID,
//                        Tables.EH_SOCIAL_SECURITY_REPORT.CREATE_TIME,
//                        Tables.EH_SOCIAL_SECURITY_REPORT.FILE_UID,
//                        Tables.EH_SOCIAL_SECURITY_REPORT.FILE_TIME,
                        Tables.EH_SOCIAL_SECURITY_REPORT.ORGANIZATION_ID);
		LOGGER.debug("计算公司汇总表的 sql is " + step.toString());
        Record result = step.fetchAny();
        if (null == result) {
            return null;
        }
        return result.map(r -> {
            SocialSecurityDepartmentSummary summary = new SocialSecurityDepartmentSummary();
            summary.setPayMonth(month);
            summary.setSocialSecuritySum((BigDecimal) r.getValue(0));
            summary.setSocialSecurityCompanySum((BigDecimal) r.getValue(1));
            summary.setSocialSecurityEmployeeSum((BigDecimal) r.getValue(2));
            summary.setPensionCompanySum((BigDecimal) r.getValue(3));
            summary.setPensionEmployeeSum((BigDecimal) r.getValue(4));
            summary.setMedicalCompanySum((BigDecimal) r.getValue(5));
            summary.setMedicalEmployeeSum((BigDecimal) r.getValue(6));
            summary.setInjuryCompanySum((BigDecimal) r.getValue(7));
            summary.setInjuryEmployeeSum((BigDecimal) r.getValue(8));
            summary.setUnemploymentCompanySum((BigDecimal) r.getValue(9));
            summary.setUnemploymentEmployeeSum((BigDecimal) r.getValue(10));
            summary.setBirthCompanySum((BigDecimal) r.getValue(11));
            summary.setBirthEmployeeSum((BigDecimal) r.getValue(12));
            summary.setCriticalIllnessCompanySum((BigDecimal) r.getValue(13));
            summary.setCriticalIllnessEmployeeSum((BigDecimal) r.getValue(14));
            summary.setAfterSocialSecurityCompanySum((BigDecimal) r.getValue(15));
            summary.setAfterSocialSecurityEmployeeSum((BigDecimal) r.getValue(16));
            summary.setAfterPensionCompanySum((BigDecimal) r.getValue(17));
            summary.setAfterPensionEmployeeSum((BigDecimal) r.getValue(18));
            summary.setAfterMedicalCompanySum((BigDecimal) r.getValue(19));
            summary.setAfterMedicalEmployeeSum((BigDecimal) r.getValue(20));
            summary.setAfterInjuryCompanySum((BigDecimal) r.getValue(21));
            summary.setAfterInjuryEmployeeSum((BigDecimal) r.getValue(22));
            summary.setAfterUnemploymentCompanySum((BigDecimal) r.getValue(23));
            summary.setAfterUnemploymentEmployeeSum((BigDecimal) r.getValue(24));
            summary.setAfterBirthCompanySum((BigDecimal) r.getValue(25));
            summary.setAfterBirthEmployeeSum((BigDecimal) r.getValue(26));
            summary.setAfterCriticalIllnessCompanySum((BigDecimal) r.getValue(27));
            summary.setAfterCriticalIllnessEmployeeSum((BigDecimal) r.getValue(28));
            summary.setDisabilitySum((BigDecimal) r.getValue(29));
            summary.setCommercialInsurance((BigDecimal) r.getValue(30));
            summary.setAccumulationFundSum((BigDecimal) r.getValue(31));
            summary.setAccumulationFundCompanySum((BigDecimal) r.getValue(32));
            summary.setAccumulationFundEmployeeSum((BigDecimal) r.getValue(33));
            summary.setAfterAccumulationFundCompanySum((BigDecimal) r.getValue(34));
            summary.setAfterAccumulationFundEmployeeSum((BigDecimal) r.getValue(35));
            summary.setCreatorUid((Long) r.getValue(36));
            summary.setCreateTime((Timestamp) r.getValue(37));
            summary.setFileUid((Long) r.getValue(38));
            summary.setFileTime((Timestamp) r.getValue(39));
            summary.setOwnerId((Long) r.getValue(40));
            summary.setEmployeeCount((Integer) r.getValue(41));
            return summary;
        });
    }

    @Override
    public SocialSecurityReport findSocialSecurityReportByDetailId(Long id, String month) {
        Record record = getReadOnlyContext().select().from(Tables.EH_SOCIAL_SECURITY_REPORT)
                .where(Tables.EH_SOCIAL_SECURITY_REPORT.DETAIL_ID.eq(id))
                .and(Tables.EH_SOCIAL_SECURITY_REPORT.PAY_MONTH.eq(month))
                .orderBy(Tables.EH_SOCIAL_SECURITY_REPORT.ID.desc())
                .fetchAny();
        if (null == record) {
            return null;
        }

        return record.map(r -> ConvertHelper.convert(r, SocialSecurityReport.class));
    }

    @Override
    public List<SocialSecurityReport> listSocialSecurityReport(Long ownerId, String paymentMonth, CrossShardListingLocator locator, int pageSize) {
        SelectConditionStep<Record> step = getReadOnlyContext().select().from(Tables.EH_SOCIAL_SECURITY_REPORT)
                .where(Tables.EH_SOCIAL_SECURITY_REPORT.ORGANIZATION_ID.eq(ownerId));
        if (null != paymentMonth) {
            step = step.and(Tables.EH_SOCIAL_SECURITY_REPORT.PAY_MONTH.eq(paymentMonth));
        }
        if (null != locator && locator.getAnchor() != null) {
            step.and(Tables.EH_SOCIAL_SECURITY_REPORT.ID.gt(locator.getAnchor()));
        }
        step.limit(pageSize);
        return step.orderBy(Tables.EH_SOCIAL_SECURITY_REPORT.ID.asc())
                .fetch().map(r -> ConvertHelper.convert(r, SocialSecurityReport.class));
    }

    private EhSocialSecurityReportDao getReadWriteDao() {
        return getDao(getReadWriteContext());
    }

    private EhSocialSecurityReportDao getReadOnlyDao() {
        return getDao(getReadOnlyContext());
    }

    private EhSocialSecurityReportDao getDao(DSLContext context) {
        return new EhSocialSecurityReportDao(context.configuration());
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
