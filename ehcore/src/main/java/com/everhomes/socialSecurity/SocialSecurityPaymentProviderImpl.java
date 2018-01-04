// @formatter:off
package com.everhomes.socialSecurity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.everhomes.rest.socialSecurity.NormalFlag;
import com.everhomes.user.User;
import org.jooq.DSLContext;
import org.jooq.Record1;
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
import com.everhomes.server.schema.tables.daos.EhSocialSecurityPaymentsDao;
import com.everhomes.server.schema.tables.pojos.EhSocialSecurityPayments;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SocialSecurityPaymentProviderImpl implements SocialSecurityPaymentProvider {
    @Autowired
    private SocialSecurityService socialSecurityService;
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createSocialSecurityPayment(SocialSecurityPayment socialSecurityPayment) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSocialSecurityPayments.class));
        socialSecurityPayment.setId(id);
        socialSecurityPayment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        socialSecurityPayment.setCreatorUid(UserContext.current().getUser().getId());
        socialSecurityPayment.setUpdateTime(socialSecurityPayment.getCreateTime());
        socialSecurityPayment.setOperatorUid(socialSecurityPayment.getCreatorUid());
        getReadWriteDao().insert(socialSecurityPayment);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhSocialSecurityPayments.class, null);
    }

    @Override
    public void updateSocialSecurityPayment(SocialSecurityPayment socialSecurityPayment) {
        assert (socialSecurityPayment.getId() != null);
        socialSecurityPayment.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        socialSecurityPayment.setOperatorUid(UserContext.current().getUser().getId());
        getReadWriteDao().update(socialSecurityPayment);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSocialSecurityPayments.class, socialSecurityPayment.getId());
    }

    @Override
    public SocialSecurityPayment findSocialSecurityPaymentById(Long id) {
        assert (id != null);
        return ConvertHelper.convert(getReadOnlyDao().findById(id), SocialSecurityPayment.class);
    }

    @Override
    public List<SocialSecurityPayment> listSocialSecurityPayment(Long ownerId, Long detailId) {
        return getReadOnlyContext().select().from(Tables.EH_SOCIAL_SECURITY_PAYMENTS)
                .where(Tables.EH_SOCIAL_SECURITY_PAYMENTS.ORGANIZATION_ID.eq(ownerId))
                .and(Tables.EH_SOCIAL_SECURITY_PAYMENTS.DETAIL_ID.eq(detailId))
                .orderBy(Tables.EH_SOCIAL_SECURITY_PAYMENTS.ID.asc())
                .fetch().map(r -> ConvertHelper.convert(r, SocialSecurityPayment.class));
    }

    @Override
    public String findPaymentMonthByOwnerId(Long ownerId) {
        Record1<String> result = getReadOnlyContext().selectDistinct(Tables.EH_SOCIAL_SECURITY_PAYMENTS.PAY_MONTH)
                .from(Tables.EH_SOCIAL_SECURITY_PAYMENTS)
                .where(Tables.EH_SOCIAL_SECURITY_PAYMENTS.ORGANIZATION_ID.eq(ownerId))
                .fetchAny();
        if (null == result) {
            return null;
        }
        else return result.value1();
    }

    @Override
    public List<SocialSecurityPayment> listSocialSecurityPayment(Long ownerId) {
        return getReadOnlyContext().select().from(Tables.EH_SOCIAL_SECURITY_PAYMENTS)
                .where(Tables.EH_SOCIAL_SECURITY_PAYMENTS.ORGANIZATION_ID.eq(ownerId))
                .orderBy(Tables.EH_SOCIAL_SECURITY_PAYMENTS.ID.asc())
                .fetch().map(r -> ConvertHelper.convert(r, SocialSecurityPayment.class));

    }

    @Override
    public void setUserCityAndHTByAccumOrSocial(Long detailId, Byte accumOrSocial, Long cityId, String householdType) {
        getReadWriteContext().update(Tables.EH_SOCIAL_SECURITY_PAYMENTS)
                .set(Tables.EH_SOCIAL_SECURITY_PAYMENTS.CITY_ID, cityId)
                .set(Tables.EH_SOCIAL_SECURITY_PAYMENTS.HOUSEHOLD_TYPE, householdType)
                .where(Tables.EH_SOCIAL_SECURITY_PAYMENTS.DETAIL_ID.eq(detailId))
                .and(Tables.EH_SOCIAL_SECURITY_PAYMENTS.ACCUM_OR_SOCAIL.eq(accumOrSocial))
                .execute();
    }

    @Override
    public SocialSecurityPayment findSocialSecurityPayment(Long detailId, String payItem, Byte accumOrSocial) {
        return getReadOnlyContext().select().from(Tables.EH_SOCIAL_SECURITY_PAYMENTS)
                .where(Tables.EH_SOCIAL_SECURITY_PAYMENTS.DETAIL_ID.eq(detailId))
                .and(Tables.EH_SOCIAL_SECURITY_PAYMENTS.PAY_ITEM.eq(payItem))
                .and(Tables.EH_SOCIAL_SECURITY_PAYMENTS.ACCUM_OR_SOCAIL.eq(accumOrSocial))
                .orderBy(Tables.EH_SOCIAL_SECURITY_PAYMENTS.ID.asc())
                .fetchAny().map(r -> ConvertHelper.convert(r, SocialSecurityPayment.class));
    }


    @Override
    public String findPaymentMonthByDetail(Long detailId) {
        return getReadOnlyContext().selectDistinct(Tables.EH_SOCIAL_SECURITY_PAYMENTS.PAY_MONTH).from(Tables.EH_SOCIAL_SECURITY_PAYMENTS)
                .where(Tables.EH_SOCIAL_SECURITY_PAYMENTS.DETAIL_ID.eq(detailId))
                .fetchAny().value1();
    }

    @Override
    public Integer countUnFieldUsers(Long ownerId) {
        return getReadOnlyContext().selectDistinct(Tables.EH_SOCIAL_SECURITY_PAYMENTS.DETAIL_ID.countDistinct()).from(Tables.EH_SOCIAL_SECURITY_PAYMENTS)
                .where(Tables.EH_SOCIAL_SECURITY_PAYMENTS.ORGANIZATION_ID.eq(ownerId))
                .and(Tables.EH_SOCIAL_SECURITY_PAYMENTS.IS_FILED.eq(NormalFlag.NO.getCode()))
                .fetchAny().value1();
    }

    @Override
    public void deleteSocialSecurityPayments(Long ownerId) {
        getReadWriteContext().delete(Tables.EH_SOCIAL_SECURITY_PAYMENTS)
                .where(Tables.EH_SOCIAL_SECURITY_PAYMENTS.ORGANIZATION_ID.eq(ownerId)).execute();
    }

    @Override
    public void updateSocialSecurityPaymentFileStatus(Long ownerId) {
        getReadWriteContext().update(Tables.EH_SOCIAL_SECURITY_PAYMENTS).set(Tables.EH_SOCIAL_SECURITY_PAYMENTS.IS_FILED, NormalFlag.YES.getCode())
                .set(Tables.EH_SOCIAL_SECURITY_PAYMENTS.FILE_TIME, new Timestamp(DateHelper.currentGMTTime().getTime()))
                .set(Tables.EH_SOCIAL_SECURITY_PAYMENTS.FILE_UID, UserContext.currentUserId())
                .where(Tables.EH_SOCIAL_SECURITY_PAYMENTS.ORGANIZATION_ID.eq(ownerId)).execute();
    }

    @Override
    public SocialSecuritySummary calculateSocialSecuritySummary(Long ownerId, String paymentMonth) {
        return getReadOnlyContext().select(
                Tables.EH_SOCIAL_SECURITY_PAYMENTS.NAMESPACE_ID,
                Tables.EH_SOCIAL_SECURITY_PAYMENTS.ORGANIZATION_ID,
                Tables.EH_SOCIAL_SECURITY_PAYMENTS.PAY_MONTH,
                Tables.EH_SOCIAL_SECURITY_PAYMENTS.CREATOR_UID,
                Tables.EH_SOCIAL_SECURITY_PAYMENTS.CREATE_TIME,
                Tables.EH_SOCIAL_SECURITY_PAYMENTS.FILE_UID,
                Tables.EH_SOCIAL_SECURITY_PAYMENTS.FILE_TIME,
                Tables.EH_SOCIAL_SECURITY_PAYMENTS.COMPANY_RADIX.multiply(Tables.EH_SOCIAL_SECURITY_PAYMENTS.COMPANY_RADIX)
                        .divide(10000).round(2).sum(),
                Tables.EH_SOCIAL_SECURITY_PAYMENTS.EMPLOYEE_RADIX.multiply(Tables.EH_SOCIAL_SECURITY_PAYMENTS.EMPLOYEE_RATIO)
                        .divide(10000).round(2).sum()
        ).from(Tables.EH_SOCIAL_SECURITY_PAYMENTS)
                .where(Tables.EH_SOCIAL_SECURITY_PAYMENTS.ORGANIZATION_ID.eq(ownerId))
                .and(Tables.EH_SOCIAL_SECURITY_PAYMENTS.PAY_MONTH.eq(paymentMonth))
                .groupBy(
                        Tables.EH_SOCIAL_SECURITY_PAYMENTS.NAMESPACE_ID,
                        Tables.EH_SOCIAL_SECURITY_PAYMENTS.ORGANIZATION_ID,
                        Tables.EH_SOCIAL_SECURITY_PAYMENTS.PAY_MONTH,
                        Tables.EH_SOCIAL_SECURITY_PAYMENTS.CREATOR_UID,
                        Tables.EH_SOCIAL_SECURITY_PAYMENTS.CREATE_TIME,
                        Tables.EH_SOCIAL_SECURITY_PAYMENTS.FILE_UID,
                        Tables.EH_SOCIAL_SECURITY_PAYMENTS.FILE_TIME)
                .orderBy(Tables.EH_SOCIAL_SECURITY_PAYMENTS.ID.asc())
                .fetchAny().map(r -> {
                    SocialSecuritySummary summary = new SocialSecuritySummary();
                    summary.setNamespaceId((Integer) r.getValue(0));
                    summary.setOrganizationId((Long) r.getValue(1));
                    summary.setPayMonth((String) r.getValue(2));
                    summary.setCreatorUid((Long) r.getValue(3));
                    summary.setCreateTime((Timestamp) r.getValue(4));
                    summary.setFileUid((Long) r.getValue(5));
                    summary.setFileTime((Timestamp) r.getValue(6));
                    summary.setCompanyPayment((BigDecimal) r.getValue(7));
                    summary.setEmployeePayment((BigDecimal) r.getValue(8));
                    return summary;
                });

    }

    @Override
    public List<Long> listDetailsByPayFlag(List<Long> detailIds, Byte accOrSocial) {
        SelectConditionStep<Record1<Long>> step = getReadOnlyContext().selectDistinct(Tables.EH_SOCIAL_SECURITY_PAYMENTS.DETAIL_ID).from(Tables.EH_SOCIAL_SECURITY_PAYMENTS)
                .where(Tables.EH_SOCIAL_SECURITY_PAYMENTS.ACCUM_OR_SOCAIL.eq(accOrSocial));
        if (null != detailIds) {
            step = step.and(Tables.EH_SOCIAL_SECURITY_PAYMENTS.DETAIL_ID.in(detailIds));
        }
        return step.orderBy(Tables.EH_SOCIAL_SECURITY_PAYMENTS.DETAIL_ID.asc())
                .fetch().map(Record1::value1);
    }

    @Override
    public void batchCreateSocialSecurityPayment(List<EhSocialSecurityPayments> payments) {
        getReadWriteDao().insert(payments);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhSocialSecurityPayments.class, null);
    }


    private EhSocialSecurityPaymentsDao getReadWriteDao() {
        return getDao(getReadWriteContext());
    }

    private EhSocialSecurityPaymentsDao getReadOnlyDao() {
        return getDao(getReadOnlyContext());
    }

    private EhSocialSecurityPaymentsDao getDao(DSLContext context) {
        return new EhSocialSecurityPaymentsDao(context.configuration());
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
