// @formatter:off
package com.everhomes.module;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhAppCategoriesDao;
import com.everhomes.server.schema.tables.daos.EhServiceModuleEntriesDao;
import com.everhomes.server.schema.tables.pojos.EhAppCategories;
import com.everhomes.server.schema.tables.pojos.EhServiceModuleEntries;
import com.everhomes.server.schema.tables.records.EhAppCategoriesRecord;
import com.everhomes.server.schema.tables.records.EhServiceModuleEntriesRecord;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AppCategoryProviderImpl implements AppCategoryProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public List<AppCategory> listAppCategories(Byte locationType, Long parentId){
        List<AppCategory> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhAppCategoriesRecord> query = context.selectQuery(Tables.EH_APP_CATEGORIES);
        if(locationType != null){
            query.addConditions(Tables.EH_APP_CATEGORIES.LOCATION_TYPE.eq(locationType));
        }

        if(parentId != null){
            query.addConditions(Tables.EH_APP_CATEGORIES.PARENT_ID.eq(parentId));
        }

        query.addOrderBy(Tables.EH_APP_CATEGORIES.DEFAULT_ORDER.asc());
        query.fetch().map((r) -> {
            results.add(ConvertHelper.convert(r, AppCategory.class));
            return null;
        });
        return results;
    }


    @Override
    public List<AppCategory> listLeafAppCategories(Byte locationType){
        List<AppCategory> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhAppCategoriesRecord> query = context.selectQuery(Tables.EH_APP_CATEGORIES);
        query.addConditions(Tables.EH_APP_CATEGORIES.LOCATION_TYPE.eq(locationType));
        query.addConditions(Tables.EH_APP_CATEGORIES.LEAF_FLAG.eq(TrueOrFalseFlag.TRUE.getCode()));
        query.addOrderBy(Tables.EH_APP_CATEGORIES.ID.asc());
        query.fetch().map((r) -> {
            results.add(ConvertHelper.convert(r, AppCategory.class));
            return null;
        });
        return results;
    }


    @Override
    public Long findMaxDefaultOrder(Byte locationType, Long parentId){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Long defaultOrder = context.select(Tables.EH_APP_CATEGORIES.DEFAULT_ORDER.max())
                .from(Tables.EH_APP_CATEGORIES)
                .where(Tables.EH_APP_CATEGORIES.LOCATION_TYPE.eq(locationType))
                .and(Tables.EH_APP_CATEGORIES.PARENT_ID.eq(parentId))
                .fetchOne().value1();

        return defaultOrder;
    }




    @Override
    public void delete(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAppCategories.class, id));
        EhAppCategoriesDao dao = new EhAppCategoriesDao(context.configuration());
        dao.deleteById(id);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAppCategories.class, id);
    }

    @Override
    public void create(AppCategory appCategory) {

        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAppCategories.class));
        appCategory.setId(id);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAppCategories.class, id));
        EhAppCategoriesDao dao = new EhAppCategoriesDao(context.configuration());
        dao.insert(appCategory);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhAppCategories.class, id);
    }

    @Override
    public AppCategory findById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhAppCategoriesDao dao = new EhAppCategoriesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), AppCategory.class);
    }


    @Override
    public void udpate(AppCategory appCategory) {

        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAppCategories.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAppCategories.class, id));
        EhAppCategoriesDao dao = new EhAppCategoriesDao(context.configuration());
        dao.update(appCategory);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhAppCategories.class, id);
    }

}
