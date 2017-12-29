// @formatter:off
package com.everhomes.socialSecurity;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import com.everhomes.rest.socialSecurity.AccumOrSocail;
import com.everhomes.rest.socialSecurity.SocialSecurityItemDTO;
import com.everhomes.rest.socialSecurity.SsorAfPay;
import org.jooq.DSLContext;
import org.jooq.Record;
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
import com.everhomes.server.schema.tables.daos.EhSocialSecuritySettingsDao;
import com.everhomes.server.schema.tables.pojos.EhSocialSecuritySettings;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SocialSecuritySettingProviderImpl implements SocialSecuritySettingProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createSocialSecuritySetting(SocialSecuritySetting socialSecuritySetting) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSocialSecuritySettings.class));
        socialSecuritySetting.setId(id);
        socialSecuritySetting.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        socialSecuritySetting.setCreatorUid(UserContext.current().getUser().getId());
        socialSecuritySetting.setUpdateTime(socialSecuritySetting.getCreateTime());
        socialSecuritySetting.setOperatorUid(socialSecuritySetting.getCreatorUid());
        getReadWriteDao().insert(socialSecuritySetting);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhSocialSecuritySettings.class, null);
    }

    @Override
    public void updateSocialSecuritySetting(SocialSecuritySetting socialSecuritySetting) {
        assert (socialSecuritySetting.getId() != null);
        socialSecuritySetting.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        socialSecuritySetting.setOperatorUid(UserContext.current().getUser().getId());
        getReadWriteDao().update(socialSecuritySetting);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSocialSecuritySettings.class, socialSecuritySetting.getId());
    }

    @Override
    public SocialSecuritySetting findSocialSecuritySettingById(Long id) {
        assert (id != null);
        return ConvertHelper.convert(getReadOnlyDao().findById(id), SocialSecuritySetting.class);
    }

//	@Override
//	public List<SocialSecurityPaymentDTO> listSocialSecuritySetting(Long socialSecurityCityId, Long accumulationFundCityId,
//																	Long deptId, String keywords, SsorAfPay payFlag, List<Long> detailIds, CrossShardListingLocator locator) {
//		SelectConditionStep<Record> step = getReadOnlyContext().select().from(Tables.EH_SOCIAL_SECURITY_SETTINGS)
//				.where(Tables.EH_SOCIAL_SECURITY_SETTINGS.ID.greaterOrEqual(0L));
//		if (null != socialSecurityCityId) {
//			step.andExists(getReadOnlyContext().select().from(Tables.EH_SOCIAL_SECURITY_SETTINGS.as("t2")).where(Tables.EH_SOCIAL_SECURITY_SETTINGS.CITY_ID.eq()))
//		}
//		return null;
//	}

    @Override
    public List<SocialSecuritySetting> listSocialSecuritySetting(Long socialSecurityCityId, Long accumulationFundCityId, Long deptId, String keywords, SsorAfPay payFlag, List<Long> detailIds) {
        return null;
    }

    @Override
    public List<SocialSecuritySetting> listSocialSecuritySetting() {
        return getReadOnlyContext().select().from(Tables.EH_SOCIAL_SECURITY_SETTINGS)
                .orderBy(Tables.EH_SOCIAL_SECURITY_SETTINGS.ID.asc())
                .fetch().map(r -> ConvertHelper.convert(r, SocialSecuritySetting.class));
    }

    @Override
    public int setUserCityAndHTByAccumOrSocial(Long detailId, Byte accumOrSocial, Long cityId, String householdType) {
        return getReadWriteContext().update(Tables.EH_SOCIAL_SECURITY_SETTINGS).set(Tables.EH_SOCIAL_SECURITY_SETTINGS.CITY_ID, cityId)
                .set(Tables.EH_SOCIAL_SECURITY_SETTINGS.HOUSEHOLD_TYPE, householdType)
                .where(Tables.EH_SOCIAL_SECURITY_SETTINGS.DETAIL_ID.eq(detailId))
                .and(Tables.EH_SOCIAL_SECURITY_SETTINGS.ACCUM_OR_SOCAIL.eq(accumOrSocial)).execute();
    }

    @Override
    public SocialSecuritySetting findSocialSecuritySettingByDetailIdAndItem(Long detailId, SocialSecurityItemDTO itemDTO, Byte accumOrSocial) {
        SelectConditionStep<Record> step = getReadOnlyContext().select().from(Tables.EH_SOCIAL_SECURITY_SETTINGS)
                .where(Tables.EH_SOCIAL_SECURITY_SETTINGS.DETAIL_ID.eq(detailId));
        if (null != accumOrSocial) {
            step = step.and(Tables.EH_SOCIAL_SECURITY_SETTINGS.ACCUM_OR_SOCAIL.eq(accumOrSocial));
        }
        if (null != itemDTO) {
            step = step.and(Tables.EH_SOCIAL_SECURITY_SETTINGS.PAY_ITEM.eq(itemDTO.getPayItem()));
        }
        return step.orderBy(Tables.EH_SOCIAL_SECURITY_SETTINGS.ID.asc())
                .fetchAny().map(r -> ConvertHelper.convert(r, SocialSecuritySetting.class));
    }

    @Override
    public List<SocialSecuritySetting> listSocialSecuritySetting(Long detailId) {
        return getReadOnlyContext().select().from(Tables.EH_SOCIAL_SECURITY_SETTINGS)
                .where(Tables.EH_SOCIAL_SECURITY_SETTINGS.DETAIL_ID.eq(detailId))
                .orderBy(Tables.EH_SOCIAL_SECURITY_SETTINGS.ID.asc())
                .fetch().map(r -> ConvertHelper.convert(r, SocialSecuritySetting.class));
    }

    @Override
    public List<SocialSecuritySetting> listSocialSecuritySetting(Set<Long> detailIds) {
        return getReadOnlyContext().select().from(Tables.EH_SOCIAL_SECURITY_SETTINGS)
                .where(Tables.EH_SOCIAL_SECURITY_SETTINGS.DETAIL_ID.in(detailIds))
                .orderBy(Tables.EH_SOCIAL_SECURITY_SETTINGS.ID.asc())
                .fetch().map(r -> ConvertHelper.convert(r, SocialSecuritySetting.class));
    }

    @Override
    public List<Long> listDetailsByCityId(List<Long> detailIds, Long cityId, byte accOrsoc) {
        SelectConditionStep<Record1<Long>> step = getReadOnlyContext().selectDistinct(Tables.EH_SOCIAL_SECURITY_SETTINGS.DETAIL_ID).from(Tables.EH_SOCIAL_SECURITY_SETTINGS)
                .where(Tables.EH_SOCIAL_SECURITY_SETTINGS.CITY_ID.eq(cityId))
                .and(Tables.EH_SOCIAL_SECURITY_SETTINGS.ACCUM_OR_SOCAIL.eq(accOrsoc));
        if (null != detailIds) {
            step = step.and(Tables.EH_SOCIAL_SECURITY_SETTINGS.DETAIL_ID.in(detailIds));
        }
        return step.orderBy(Tables.EH_SOCIAL_SECURITY_SETTINGS.DETAIL_ID.asc())
                .fetch().map(Record1::value1);
    }

    @Override
    public SocialSecuritySetting findSocialSecuritySettingByDetailIdAndAOS(Long detailId, AccumOrSocail socail) {
        List<SocialSecuritySetting> results = getReadOnlyContext().select().from(Tables.EH_SOCIAL_SECURITY_SETTINGS)
                .where(Tables.EH_SOCIAL_SECURITY_SETTINGS.DETAIL_ID.eq(detailId))
                .and(Tables.EH_SOCIAL_SECURITY_SETTINGS.ACCUM_OR_SOCAIL.eq(socail.getCode()))
                .orderBy(Tables.EH_SOCIAL_SECURITY_SETTINGS.ID.asc())
                .fetch().map(r -> ConvertHelper.convert(r, SocialSecuritySetting.class));
        if (null == results || results.size() == 0) {
            return null;
        }
        return results.get(0);
    }

    @Override
    public void batchCreateSocialSecuritySetting(List<EhSocialSecuritySettings> settings) {
        getReadWriteDao().insert(settings);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhSocialSecuritySettings.class, null);
    }

    private EhSocialSecuritySettingsDao getReadWriteDao() {
        return getDao(getReadWriteContext());
    }

    private EhSocialSecuritySettingsDao getReadOnlyDao() {
        return getDao(getReadOnlyContext());
    }

    private EhSocialSecuritySettingsDao getDao(DSLContext context) {
        return new EhSocialSecuritySettingsDao(context.configuration());
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
