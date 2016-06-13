package com.everhomes.wifi;

import java.util.List;

import javax.persistence.Table;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SelectQuery;
import org.jooq.SelectWhereStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.wifi.WifiSettingStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhWifiSettingsDao;
import com.everhomes.server.schema.tables.pojos.EhWifiSettings;
import com.everhomes.server.schema.tables.records.EhWifiSettingsRecord;
import com.everhomes.util.ConvertHelper;

@Component
public class WifiProviderImpl implements WifiProvider {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WifiProviderImpl.class);
	@Autowired
	private DbProvider dbProvider;
	
	@Autowired 
    private SequenceProvider sequenceProvider;
	
	
	@Override
	public List<WifiSetting> listWifiSetting(Long ownerId,String ownerType,Long pageAnchor,Integer pageSize){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhWifiSettings.class));
		SelectQuery<EhWifiSettingsRecord> query = context.selectQuery(Tables.EH_WIFI_SETTINGS);
		if(pageAnchor != null && pageAnchor != 0)
			query.addConditions(Tables.EH_WIFI_SETTINGS.ID.gt(pageAnchor));
		query.addConditions(Tables.EH_WIFI_SETTINGS.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_WIFI_SETTINGS.OWNER_TYPE.eq(ownerType));
		query.addConditions(Tables.EH_WIFI_SETTINGS.STATUS.eq(WifiSettingStatus.ACTIVE.getCode()));
		if(pageSize != null)
			query.addLimit(pageSize);

		query.addOrderBy(Tables.EH_WIFI_SETTINGS.ID.asc());
		Result<EhWifiSettingsRecord> result = query.fetch();
		
		return result.map(r -> ConvertHelper.convert(r, WifiSetting.class));
	}
	
	@Override
	public WifiSetting findWifiSettingById(Long id){
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhWifiSettingsDao dao = new EhWifiSettingsDao(context.configuration());
		
		return ConvertHelper.convert(dao.findById(id), WifiSetting.class);
	}
	
	@Override
	public WifiSetting findWifiSettingByCondition(String ssid,Long ownerId,String ownerType){
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		SelectWhereStep<EhWifiSettingsRecord> select = context.selectFrom(Tables.EH_WIFI_SETTINGS);
		EhWifiSettingsRecord record = select.where(Tables.EH_WIFI_SETTINGS.SSID.eq(ssid))
				.and(Tables.EH_WIFI_SETTINGS.OWNER_ID.eq(ownerId))
				.and(Tables.EH_WIFI_SETTINGS.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_WIFI_SETTINGS.STATUS.eq(WifiSettingStatus.ACTIVE.getCode()))
				.fetchOne();
		
		return ConvertHelper.convert(record, WifiSetting.class);
	}
	
	@Override
	public void createWifiSetting(WifiSetting wifiSetting){
		Long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhWifiSettings.class));
		wifiSetting.setId(id);
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhWifiSettingsDao dao = new EhWifiSettingsDao(context.configuration());
		
		dao.insert(wifiSetting);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhWifiSettings.class, null);
	}
	@Override
	public void updateWifiSetting(WifiSetting wifiSetting){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhWifiSettingsDao dao = new EhWifiSettingsDao(context.configuration());
		
		dao.update(wifiSetting);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhWifiSettings.class, null);
	}

}
