package com.everhomes.border;

import java.sql.Timestamp;

import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.device.Device;
import com.everhomes.device.DeviceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhDevicesDao;
import com.everhomes.server.schema.tables.records.EhBordersRecord;
import com.everhomes.server.schema.tables.records.EhDevicesRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class DeviceProviderImpl implements DeviceProvider {

    @Autowired
    private DbProvider dbProvider;
    
    @Override
    @CacheEvict(value = "Device", key="#device.deviceId")
    public void registDevice(Device device) {
        device.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        InsertQuery<EhDevicesRecord> query = context.insertQuery(Tables.EH_DEVICES);
        query.setRecord(ConvertHelper.convert(device, EhDevicesRecord.class));
        query.setReturning(Tables.EH_BORDERS.ID);
        if(query.execute() > 0) {
            device.setId(query.getReturnedRecord().getId());
        }
        
    }

    @Override
    @Cacheable(value="Device", key="#deviceId", unless="#result == null")
    public Device findDeviceByDeviceId(String deviceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhDevicesDao dao = new EhDevicesDao(context.configuration());
        return ConvertHelper.convert(dao.fetchOneByDeviceId(deviceId), Device.class);
    }

}
