package com.everhomes.filemanagement;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhFileManagementCatalogScopesDao;
import com.everhomes.server.schema.tables.daos.EhFileManagementCatalogsDao;
import com.everhomes.server.schema.tables.pojos.EhFileManagementCatalogScopes;
import com.everhomes.server.schema.tables.pojos.EhFileManagementCatalogs;
import com.everhomes.server.schema.tables.records.EhFileManagementCatalogScopesRecord;
import com.everhomes.server.schema.tables.records.EhFileManagementCatalogsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.DeleteQuery;
import org.jooq.SelectQuery;
import org.jooq.UpdateQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileManagementProviderImpl implements FileManagementProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createFileCatalog(FileCatalog catalog) {
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
    public void updateFileCatalog(FileCatalog catalog) {
        catalog.setOperatorUid(UserContext.currentUserId());
        catalog.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhFileManagementCatalogsDao dao = new EhFileManagementCatalogsDao(context.configuration());
        dao.update(catalog);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhFileManagementCatalogs.class, catalog.getId());
    }

    @Override
    public FileCatalog findFileCatalogById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhFileManagementCatalogsDao dao = new EhFileManagementCatalogsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), FileCatalog.class);
    }

    @Override
    public FileCatalog findFileCatalogByName(Integer namespaceId, Long ownerId, String name) {
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
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, FileCatalog.class));
            return null;
        });
        if (null != results && 0 != results.size()) {
            return results;
        }
        return null;
    }

    @Override
    public void createFileCatalogScope(FileCatalogScope scope) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFileManagementCatalogScopes.class));
        scope.setId(id);
        scope.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        scope.setOperatorUid(UserContext.currentUserId());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhFileManagementCatalogScopesDao dao = new EhFileManagementCatalogScopesDao(context.configuration());
        dao.insert(scope);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhFileManagementCatalogScopes.class, null);
    }

    @Override
    public void deleteFileCatalogScopeByUserIds(Long catalogId, List<Long> sourceIds){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        DeleteQuery<EhFileManagementCatalogScopesRecord> query = context.deleteQuery(Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES);
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES.CATALOG_ID.eq(catalogId));
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES.SOURCE_ID.in(sourceIds));
        query.execute();
    }

    @Override
    public void updateFileCatalogScopeDownload(Long catalogId, List<Long> sourceIds, Byte permission){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        UpdateQuery<EhFileManagementCatalogScopesRecord> query = context.updateQuery(Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES);
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES.CATALOG_ID.eq(catalogId));
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES.SOURCE_ID.in(sourceIds));
        query.addValue(Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES.DOWNLOAD_PERMISSION, permission);
        query.execute();
    }

    @Override
    public List<FileCatalogScope> listFileCatalogScopes(Integer namespaceId, Long catalogId, Long pageAnchor, Integer pageSize, String keywords) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<FileCatalogScope> results = new ArrayList<>();

        SelectQuery<EhFileManagementCatalogScopesRecord> query = context.selectQuery(Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES);
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES.CATALOG_ID.eq(catalogId));
        if (keywords != null)
            query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES.SOURCE_DESCRIPTION.like(keywords));
        if (pageAnchor != null)
            query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES.ID.lt(pageAnchor));
        query.addLimit(pageSize + 1);
        query.addOrderBy(Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES.ID.desc());
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, FileCatalogScope.class));
            return null;
        });
        if (null != results && 0 != results.size()) {
            return results;
        }
        return null;
    }
}
