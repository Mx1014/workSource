// @formatter:off
package com.everhomes.print;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SelectConditionStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhSiyinPrintSettingsDao;
import com.everhomes.server.schema.tables.pojos.EhSiyinPrintSettings;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SiyinPrintSettingProviderImpl implements SiyinPrintSettingProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(SiyinPrintSettingProviderImpl.class);
	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	
	@Override
	public List<SiyinPrintSetting> listSiyinPrintSettingByOwner(String ownerType, Long ownerId) {
		 SelectConditionStep<?> query = getReadOnlyContext().select().from(Tables.EH_SIYIN_PRINT_SETTINGS)
				.where(Tables.EH_SIYIN_PRINT_SETTINGS.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_SIYIN_PRINT_SETTINGS.OWNER_ID.eq(ownerId))
				.and(Tables.EH_SIYIN_PRINT_SETTINGS.STATUS.eq(CommonStatus.ACTIVE.getCode()));
		 LOGGER.info("listSiyinPrintSettingByOwner sql = {},param = {}",query.getSQL(),query.getBindValues());
		 return	query.fetch().map(r -> ConvertHelper.convert(r, SiyinPrintSetting.class));
	}
	
//	@Override
//	public void createSiyinPrintSetting(SiyinPrintSetting siyinPrintSetting) {
//		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSiyinPrintSettings.class));
//		siyinPrintSetting.setId(id);
//		siyinPrintSetting.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		siyinPrintSetting.setCreatorUid(UserContext.current().getUser().getId());
////		siyinPrintSetting.setUpdateTime(siyinPrintSetting.getCreateTime());
//		siyinPrintSetting.setOperatorUid(siyinPrintSetting.getCreatorUid());
//		getReadWriteDao().insert(siyinPrintSetting);
//		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSiyinPrintSettings.class, null);
//	}

//	@Override
//	public void updateSiyinPrintSettings(List<SiyinPrintSetting> siyinPrintSettings) {
//		assert (siyinPrintSetting.getId() != null);
////		siyinPrintSetting.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		siyinPrintSetting.setOperatorUid(UserContext.current().getUser().getId());
//		getReadWriteDao().update(siyinPrintSetting);
//		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSiyinPrintSettings.class, siyinPrintSetting.getId());
//	}

	@Override
	public SiyinPrintSetting findSiyinPrintSettingById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SiyinPrintSetting.class);
	}
	
	@Override
	public List<SiyinPrintSetting> listSiyinPrintSetting() {
		return  getReadOnlyContext().select().from(Tables.EH_SIYIN_PRINT_SETTINGS)
				.orderBy(Tables.EH_SIYIN_PRINT_SETTINGS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SiyinPrintSetting.class));
	}
	
	private EhSiyinPrintSettingsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSiyinPrintSettingsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSiyinPrintSettingsDao getDao(DSLContext context) {
		return new EhSiyinPrintSettingsDao(context.configuration());
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
	public void createSiyinPrintSettings(List<SiyinPrintSetting> list,String ownerType, Long ownerId) {
	    dbProvider.execute(r -> {
	    	//删除原来的设置
	    	getReadWriteContext().delete(Tables.EH_SIYIN_PRINT_SETTINGS)
			.where(Tables.EH_SIYIN_PRINT_SETTINGS.OWNER_TYPE.eq(ownerType))
			.and(Tables.EH_SIYIN_PRINT_SETTINGS.OWNER_ID.eq(ownerId)).execute();
	    	//更新原来的设置
		    for (SiyinPrintSetting siyinPrintSetting : list) {
		    	Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSiyinPrintSettings.class));
				siyinPrintSetting.setId(id);
				siyinPrintSetting.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				siyinPrintSetting.setCreatorUid(UserContext.current().getUser().getId());
				siyinPrintSetting.setOperateTime(siyinPrintSetting.getCreateTime());
				siyinPrintSetting.setOperatorUid(siyinPrintSetting.getCreatorUid());
				getReadWriteDao().insert(siyinPrintSetting);
				DaoHelper.publishDaoAction(DaoAction.CREATE, EhSiyinPrintSettings.class, null);
		    }
		    return null;
	    });
	}
}
