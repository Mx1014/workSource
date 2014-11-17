package com.everhomes.border;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.cache.DirtyTrackingProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhBordersDao;
import com.everhomes.server.schema.tables.pojos.EhBorders;
import com.everhomes.server.schema.tables.records.EhBordersRecord;
import com.everhomes.util.ConvertHelper;

@Component
public class BorderProviderImpl implements BorderProvider {

    @Autowired
    private DirtyTrackingProvider trackingProvider;
    
    @Autowired
    private DbProvider dbProvider;
    
    private List<Border> borders = new ArrayList<Border>();
    private long lastLoadingTimestamp;
    
    @Override
    public void createBorder(Border border) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        
        InsertQuery<EhBordersRecord> query = context.insertQuery(Tables.EH_BORDERS);
        query.setRecord(ConvertHelper.convert(border, EhBordersRecord.class));
        query.setReturning(Tables.EH_BORDERS.ID);
        if(query.execute() > 0) {
            border.setId(query.getReturnedRecord().getId());
        }
        
        this.trackingProvider.updateTrackingTimestamp(NameMapper.getDirtyTrackingNameFromTablePojo(EhBorders.class), false);
    }

    @Override
    public void updateBorder(Border border) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        EhBordersDao dao = new EhBordersDao(context.configuration());
        dao.update(border);
        
        this.trackingProvider.updateTrackingTimestamp(NameMapper.getDirtyTrackingNameFromTablePojo(EhBorders.class), false);
    }

    @Override
    public void deleteBorderById(int id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        EhBordersDao dao = new EhBordersDao(context.configuration());
        dao.deleteById(id);
        
        this.trackingProvider.updateTrackingTimestamp(NameMapper.getDirtyTrackingNameFromTablePojo(EhBorders.class), false);
    }

    @Override
    public Border findBorderById(int id) {
        List<Border> l = listAllBorders();
        for(Border border : l) {
            if(border.getId() == id)
                return border;
        }
        
        return null;
    }

    @Override
    public synchronized List<Border> listAllBorders() {
        long dirtyTimestamp = this.trackingProvider.getTrackingTimestamp(NameMapper.getDirtyTrackingNameFromTablePojo(EhBorders.class), false);
        if(dirtyTimestamp == 0 || this.lastLoadingTimestamp != dirtyTimestamp) {
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
            
            borders = context.select().from(Tables.EH_BORDERS).fetch().map(new RecordMapper<Record, Border>() {

                @Override
                public Border map(Record record) {
                    return ConvertHelper.convert(record,  Border.class);
                }
            });
            this.lastLoadingTimestamp = dirtyTimestamp;
        }
        
        return borders;
    }
}
