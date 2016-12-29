package com.everhomes.general_approval;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhGeneralFormsDao;
import com.everhomes.server.schema.tables.pojos.EhGeneralForms;
import com.everhomes.server.schema.tables.records.EhGeneralFormsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class GeneralFormProviderImpl implements GeneralFormProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createGeneralForm(GeneralForm obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhGeneralForms.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGeneralForms.class));
        obj.setId(id);
        prepareObj(obj);
        EhGeneralFormsDao dao = new EhGeneralFormsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateGeneralForm(GeneralForm obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGeneralForms.class));
        EhGeneralFormsDao dao = new EhGeneralFormsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteGeneralForm(GeneralForm obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGeneralForms.class));
        EhGeneralFormsDao dao = new EhGeneralFormsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public GeneralForm getGeneralFormById(Long id) {
        try {
        GeneralForm[] result = new GeneralForm[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGeneralForms.class));

        result[0] = context.select().from(Tables.EH_GENERAL_FORMS)
            .where(Tables.EH_GENERAL_FORMS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, GeneralForm.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<GeneralForm> queryGeneralForms(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGeneralForms.class));

        SelectQuery<EhGeneralFormsRecord> query = context.selectQuery(Tables.EH_GENERAL_FORMS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_GENERAL_FORMS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<GeneralForm> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, GeneralForm.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(GeneralForm obj) {
    }
}
