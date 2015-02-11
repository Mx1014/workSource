// @formatter:off
package com.everhomes.border;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhBordersDao;
import com.everhomes.server.schema.tables.records.EhBordersRecord;
import com.everhomes.util.ConvertHelper;

/**
 * Border server administration implementation
 * 
 * @author Kelven Yang
 *
 */
@Component
public class BorderProviderImpl implements BorderProvider {

    @Autowired
    private DbProvider dbProvider;
    
    @Override
    @CacheEvict(value="Border", key = "'list'")
    public void createBorder(Border border) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        
        InsertQuery<EhBordersRecord> query = context.insertQuery(Tables.EH_BORDERS);
        query.setRecord(ConvertHelper.convert(border, EhBordersRecord.class));
        query.setReturning(Tables.EH_BORDERS.ID);
        if(query.execute() > 0) {
            border.setId(query.getReturnedRecord().getId());
        }
    }

    @Override
    @CacheEvict(value="Border", key = "{#border.id, 'list'}")
    public void updateBorder(Border border) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        EhBordersDao dao = new EhBordersDao(context.configuration());
        dao.update(border);
    }

    @Override
    @CacheEvict(value="Border", key = "{#id, 'list'}")
    public void deleteBorderById(int id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        EhBordersDao dao = new EhBordersDao(context.configuration());
        dao.deleteById(id);
    }

    @Override
    @Cacheable(value="Border", key="#id")
    public Border findBorderById(int id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        EhBordersDao dao = new EhBordersDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), Border.class);
    }

    @Override
    @Cacheable(value="Border", key="'list'")
    public synchronized List<Border> listAllBorders() {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select().from(Tables.EH_BORDERS).fetch().map((Record record) -> {
            return ConvertHelper.convert(record,  Border.class);
        });
    }
}
