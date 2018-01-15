package com.everhomes.filemanagement;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhFileManagementCatalogsDao;
import com.everhomes.server.schema.tables.pojos.EhFileManagementCatalogs;
import com.everhomes.server.schema.tables.records.EhFileManagementCatalogsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileManagementProviderImpl implements FileManagementProvider{

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createFileCatalog(FileCatalog catalog){
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFileManagementCatalogs.class));
        catalog.setId(id);
        catalog.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        catalog.setOperatorUid(UserContext.currentUserId());
        catalog.setUpdateTime(catalog.getCreateTime());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhFileManagementCatalogsDao dao = new EhFileManagementCatalogsDao(context.configuration());
        dao.insert(catalog);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhFileManagementCatalogs.class, null);
    }

    @Override
    public void updateFileCatalog(FileCatalog catalog){
        catalog.setOperatorUid(UserContext.currentUserId());
        catalog.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhFileManagementCatalogsDao dao = new EhFileManagementCatalogsDao(context.configuration());
        dao.update(catalog);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhFileManagementCatalogs.class, catalog.getId());
    }

    @Override
    public FileCatalog findFileCatalogById(Long id){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhFileManagementCatalogsDao dao = new EhFileManagementCatalogsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), FileCatalog.class);
    }

    @Override
    public FileCatalog findFileCatalogByName(Integer namespaceId, Long ownerId, String name){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhFileManagementCatalogsRecord> query = context.selectQuery(Tables.EH_FILE_MANAGEMENT_CATALOGS);
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.NAME.eq(name));

        return query.fetchAnyInto(FileCatalog.class);
    }

    @Override
    public List<FileCatalog> listFileCatalogs(Integer namespaceId, Long ownerId, Long pageAnchor, Integer pageSize, String keywords) {
        List<FileCatalog> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhFileManagementCatalogsRecord> query = context.selectQuery(Tables.EH_FILE_MANAGEMENT_CATALOGS);
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.OWNER_ID.eq(ownerId));
        if (keywords != null)
            query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.NAME.like(keywords));
        if (pageAnchor != null)
            query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.ID.lt(pageAnchor));
        query.addLimit(pageSize + 1);
        query.addOrderBy(Tables.EH_FILE_MANAGEMENT_CATALOGS.ID.desc());
        query.fetch().map(r ->{
            results.add(ConvertHelper.convert(r, FileCatalog.class));
            return null;
        });
        if (null != results && 0 != results.size()) {
            return results;
        }
        return null;
    }
}
