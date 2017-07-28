package com.everhomes.general_form;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhGeneralFormValsDao;
import com.everhomes.server.schema.tables.pojos.EhGeneralFormVals;
import com.everhomes.server.schema.tables.records.EhGeneralFormValsRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.DeleteQuery;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class GeneralFormValProviderImpl implements GeneralFormValProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createGeneralFormVal(GeneralFormVal obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhGeneralFormVals.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGeneralFormVals.class));
        obj.setId(id);
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
        EhGeneralFormValsDao dao = new EhGeneralFormValsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateGeneralFormVal(GeneralFormVal obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGeneralFormVals.class));
        EhGeneralFormValsDao dao = new EhGeneralFormValsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteGeneralFormVal(GeneralFormVal obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGeneralFormVals.class));
        EhGeneralFormValsDao dao = new EhGeneralFormValsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public GeneralFormVal getGeneralFormValById(Long id) {
        try {
        GeneralFormVal[] result = new GeneralFormVal[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGeneralFormVals.class));

        result[0] = context.select().from(Tables.EH_GENERAL_FORM_VALS)
            .where(Tables.EH_GENERAL_FORM_VALS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, GeneralFormVal.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<GeneralFormVal> queryGeneralFormVals(String sourceType, Long sourceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGeneralFormVals.class));

        SelectQuery<EhGeneralFormValsRecord> query = context.selectQuery(Tables.EH_GENERAL_FORM_VALS);
        query.addConditions(Tables.EH_GENERAL_FORM_VALS.SOURCE_ID.eq(sourceId));
        query.addConditions(Tables.EH_GENERAL_FORM_VALS.SOURCE_TYPE.eq(sourceType));

        List<GeneralFormVal> objs = query.fetch().map((r) -> ConvertHelper.convert(r, GeneralFormVal.class));

        return objs;
    }

    @Override
    public void deleteGeneralFormVals(String sourceType, Long sourceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        DeleteQuery<EhGeneralFormValsRecord> query = context.deleteQuery(Tables.EH_GENERAL_FORM_VALS);
        query.addConditions(Tables.EH_GENERAL_FORM_VALS.SOURCE_ID.eq(sourceId));
        query.addConditions(Tables.EH_GENERAL_FORM_VALS.SOURCE_TYPE.eq(sourceType));

        query.execute();
    }

}
