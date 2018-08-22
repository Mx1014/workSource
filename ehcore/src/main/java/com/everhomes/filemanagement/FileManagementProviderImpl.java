package com.everhomes.filemanagement;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.filemanagement.FileManagementStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhFileManagementCatalogScopesDao;
import com.everhomes.server.schema.tables.daos.EhFileManagementCatalogsDao;
import com.everhomes.server.schema.tables.daos.EhFileManagementContentsDao;
import com.everhomes.server.schema.tables.pojos.EhFileManagementCatalogScopes;
import com.everhomes.server.schema.tables.pojos.EhFileManagementCatalogs;
import com.everhomes.server.schema.tables.pojos.EhFileManagementContents;
import com.everhomes.server.schema.tables.records.EhFileManagementCatalogScopesRecord;
import com.everhomes.server.schema.tables.records.EhFileManagementCatalogsRecord;
import com.everhomes.server.schema.tables.records.EhFileManagementContentsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileManagementProviderImpl implements FileManagementProvider {

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createFileCatalog(FileCatalog catalog) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFileManagementCatalogs.class));
        catalog.setId(id);
        catalog.setCreatorUid(UserContext.currentUserId());
        catalog.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        catalog.setOperatorUid(catalog.getCreatorUid());
        catalog.setUpdateTime(catalog.getCreateTime());
        OrganizationMember member = organizationProvider.findActiveOrganizationMemberByOrgIdAndUId(
        		catalog.getOperatorUid(), catalog.getOwnerId());
        if (null != member) {
        	catalog.setOperatorName(member.getContactName());
        }else{
        	catalog.setOperatorName("-");
        }
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhFileManagementCatalogsDao dao = new EhFileManagementCatalogsDao(context.configuration());
        dao.insert(catalog);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhFileManagementCatalogs.class, null);
        return id;
    }

    @Override
    public void updateFileCatalog(FileCatalog catalog) {
        catalog.setOperatorUid(UserContext.currentUserId());
        catalog.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        OrganizationMember member = organizationProvider.findActiveOrganizationMemberByOrgIdAndUId(
        		catalog.getOperatorUid(), catalog.getOwnerId());
        if (null != member) {
        	catalog.setOperatorName(member.getContactName());
        }else{
        	catalog.setOperatorName("-");
        }
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
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.STATUS.eq(FileManagementStatus.VALID.getCode()));

        return query.fetchOneInto(FileCatalog.class);
    }

    @Override
    public List<String> listFileCatalogNames(Integer namespaceId, Long ownerId, String name){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(Tables.EH_FILE_MANAGEMENT_CATALOGS.NAME);
        query.addFrom(Tables.EH_FILE_MANAGEMENT_CATALOGS);
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.NAME.like(name + "%"));
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.STATUS.eq(FileManagementStatus.VALID.getCode()));

        return query.fetchInto(String.class);
    }

    @Override
    public List<FileCatalog> listFileCatalogs(Integer namespaceId, Long ownerId, Long pageAnchor, Integer pageSize, String keywords) {
        List<FileCatalog> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhFileManagementCatalogsRecord> query = context.selectQuery(Tables.EH_FILE_MANAGEMENT_CATALOGS);
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.STATUS.eq(FileManagementStatus.VALID.getCode()));
        if (keywords != null)
            query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.NAME.like("%" + keywords + "%"));
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
    public List<FileCatalog> queryFileCatalogs(ListingLocator locator, Integer namespaceId, Long ownerId, ListingQueryBuilderCallback queryBuilderCallback) {
        List<FileCatalog> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhFileManagementCatalogsRecord> query = context.selectQuery(Tables.EH_FILE_MANAGEMENT_CATALOGS);
        queryBuilderCallback.buildCondition(locator,query);
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.STATUS.eq(FileManagementStatus.VALID.getCode()));
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, FileCatalog.class));
            return null;
        });
        if (null != results && 0 != results.size()) {
            return results;
        }
        return null;
    }
/*
    @Override
    public List<FileCatalog> listAvailableFileCatalogs(Integer namespaceId, Long ownerId, Long detailId) {
        List<FileCatalog> results = new ArrayList<>();

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES);
        query.addJoin(Tables.EH_FILE_MANAGEMENT_CATALOGS, JoinType.JOIN,
                Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES.CATALOG_ID.eq(Tables.EH_FILE_MANAGEMENT_CATALOGS.ID));

        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES.SOURCE_ID.eq(detailId));
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.STATUS.eq(FileManagementStatus.VALID.getCode()));
        query.addOrderBy(Tables.EH_FILE_MANAGEMENT_CATALOGS.ID.desc());
        query.fetch().map(r -> {
            FileCatalog catalog = new FileCatalog();
            catalog.setId(r.getValue(Tables.EH_FILE_MANAGEMENT_CATALOGS.ID));
            catalog.setNamespaceId(r.getValue(Tables.EH_FILE_MANAGEMENT_CATALOGS.NAMESPACE_ID));
            catalog.setOwnerId(r.getValue(Tables.EH_FILE_MANAGEMENT_CATALOGS.OWNER_ID));
            catalog.setOwnerType(r.getValue(Tables.EH_FILE_MANAGEMENT_CATALOGS.OWNER_TYPE));
            catalog.setName(r.getValue(Tables.EH_FILE_MANAGEMENT_CATALOGS.NAME));
            catalog.setDownloadPermission(r.getValue(Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES.DOWNLOAD_PERMISSION));
            catalog.setCreatorUid(r.getValue(Tables.EH_FILE_MANAGEMENT_CATALOGS.CREATOR_UID));
            catalog.setCreateTime(r.getValue(Tables.EH_FILE_MANAGEMENT_CATALOGS.CREATE_TIME));
            catalog.setOperatorUid(r.getValue(Tables.EH_FILE_MANAGEMENT_CATALOGS.OPERATOR_UID));
            catalog.setUpdateTime(r.getValue(Tables.EH_FILE_MANAGEMENT_CATALOGS.UPDATE_TIME));
            results.add(catalog);
            return null;
        });
        if (null != results && 0 != results.size()) {
            return results;
        }
        return null;
    }*/

    @Caching(evict = {@CacheEvict(value = "FileCatalogScope", key = "#scope.catalogId")})
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

    @Caching(evict = {@CacheEvict(value = "FileCatalogScope", key = "#catalogId")})
    @Override
    public void deleteOddFileCatalogScope(Integer namespaceId, Long catalogId, String sourceType, List<Long> sourceIds){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteQuery<EhFileManagementCatalogScopesRecord> query = context.deleteQuery(Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES);
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES.CATALOG_ID.eq(catalogId));
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES.SOURCE_ID.notIn(sourceIds));
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES.SOURCE_TYPE.eq(sourceType));
        query.execute();
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhFileManagementCatalogScopes.class, null);
    }

    @Caching(evict = {@CacheEvict(value = "FileCatalogScope", key = "#scope.catalogId")})
    @Override
    public void updateFileCatalogScope(FileCatalogScope scope){
        scope.setOperatorUid(UserContext.currentUserId());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhFileManagementCatalogScopesDao dao = new EhFileManagementCatalogScopesDao(context.configuration());
        dao.update(scope);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhFileManagementCatalogScopes.class, scope.getId());
    }

    @Override
    public FileCatalogScope findFileCatalogScope(Long catalogId, Long sourceId, String sourceType){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhFileManagementCatalogScopesRecord> query = context.selectQuery(Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES);
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES.CATALOG_ID.eq(catalogId));
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES.SOURCE_ID.eq(sourceId));
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES.SOURCE_TYPE.eq(sourceType));

        return query.fetchAnyInto(FileCatalogScope.class);
    }

    @Cacheable(value = "FileCatalogScope", key = "#catalogId", unless = "#result == null")
    @Override
    public List<FileCatalogScope> listFileCatalogScopes(Integer namespaceId, Long catalogId, String keywords) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<FileCatalogScope> results = new ArrayList<>();

        SelectQuery<EhFileManagementCatalogScopesRecord> query = context.selectQuery(Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES);
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES.CATALOG_ID.eq(catalogId));
        if (keywords != null)
            query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOG_SCOPES.SOURCE_DESCRIPTION.like("%" + keywords + "%"));
//        query.addLimit(pageSize + 1);
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

    @Override
    public void createFileContent(FileContent content) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFileManagementContents.class));
        //  add the path
        String path = content.getPath() + "/" + String.valueOf(id);

        content.setId(id);
        content.setPath(path);
        content.setCreatorUid(UserContext.currentUserId());
        content.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        content.setOperatorUid(content.getCreatorUid());
        content.setUpdateTime(content.getCreateTime());
        OrganizationMember member = organizationProvider.findActiveOrganizationMemberByOrgIdAndUId(
        		content.getOperatorUid(), content.getOwnerId());
        if (null != member) {
        	content.setOperatorName(member.getContactName());
        }else{
        	content.setOperatorName("-");
        }
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhFileManagementContentsDao dao = new EhFileManagementContentsDao(context.configuration());
        dao.insert(content);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhFileManagementContents.class, null);
    }

    @Override
    public void updateFileContentStatusByIds(Long id, Byte status) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        UpdateQuery<EhFileManagementContentsRecord> query = context.updateQuery(Tables.EH_FILE_MANAGEMENT_CONTENTS);
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.ID.eq(id));
        query.addValue(Tables.EH_FILE_MANAGEMENT_CONTENTS.STATUS, status);
        query.execute();
    }

    @Override
    public void deleteFileContentByCatalogId(Long catalogId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        UpdateQuery<EhFileManagementContentsRecord> query = context.updateQuery(Tables.EH_FILE_MANAGEMENT_CONTENTS);
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.CATALOG_ID.eq(catalogId));
        query.addValue(Tables.EH_FILE_MANAGEMENT_CONTENTS.STATUS, FileManagementStatus.INVALID.getCode());
        query.execute();
    }

    @Override
    public void updateFileContent(FileContent content) {
        content.setOperatorUid(UserContext.currentUserId());
        content.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        OrganizationMember member = organizationProvider.findActiveOrganizationMemberByOrgIdAndUId(
                content.getOperatorUid(), content.getOwnerId());
        if (null != member) {
        	content.setOperatorName(member.getContactName());
        }else{
        	content.setOperatorName("-");
        }
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhFileManagementContentsDao dao = new EhFileManagementContentsDao(context.configuration());
        dao.update(content);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhFileManagementContents.class, content.getId());
    }

    @Override
    public FileContent findFileContentById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhFileManagementContentsDao dao = new EhFileManagementContentsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), FileContent.class);
    }

    @Override
    public FileContent findFileContentByName(Integer namespaceId, Long ownerId, Long catalogId, Long parentId, String name, String suffix) {
       return findFileContentByNameNotEqId(namespaceId, ownerId, catalogId, parentId, name, suffix, null);
    }
	@Override
	public FileContent findFileContentByNameNotEqId(Integer namespaceId, Long ownerId,
			Long catalogId, Long parentId, String name, String suffix, Long originContentId) {
		 DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

	        SelectQuery<EhFileManagementContentsRecord> query = context.selectQuery(Tables.EH_FILE_MANAGEMENT_CONTENTS);
	        query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.NAMESPACE_ID.eq(namespaceId));
	        query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.OWNER_ID.eq(ownerId));
	        query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.CATALOG_ID.eq(catalogId));
	        if (null != originContentId){
		        query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.ID.ne(originContentId));
	        }
	        if (parentId != null)
	            query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.PARENT_ID.eq(parentId));
	        else
	            query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.PARENT_ID.isNull());
	        query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.CONTENT_NAME.eq(name));
	        if (suffix != null)
	            query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.CONTENT_SUFFIX.eq(suffix));
	        else
	            query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.CONTENT_SUFFIX.isNull());

	        query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.STATUS.eq(FileManagementStatus.VALID.getCode()));

	        return query.fetchOneInto(FileContent.class);
	}
//    @Override
//    public FileContent findAllStatusFileContentByName(Integer namespaceId, Long ownerId, Long catalogId, Long parentId, String name, String suffix) {
//        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
//
//        SelectQuery<EhFileManagementContentsRecord> query = context.selectQuery(Tables.EH_FILE_MANAGEMENT_CONTENTS);
//        query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.NAMESPACE_ID.eq(namespaceId));
//        query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.OWNER_ID.eq(ownerId));
//        query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.CATALOG_ID.eq(catalogId));
//        if (parentId != null)
//            query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.PARENT_ID.eq(parentId));
//        else
//            query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.PARENT_ID.isNull());
//        query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.CONTENT_NAME.eq(name));
//        if (suffix != null)
//            query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.CONTENT_SUFFIX.eq(suffix));
//        else
//            query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.CONTENT_SUFFIX.isNull());
//
//        return query.fetchOneInto(FileContent.class);
//    }

    @Override
    public List<String> listFileContentNames(Integer namespaceId, Long ownerId, Long catalogId, Long parentId, String name, String suffix){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(Tables.EH_FILE_MANAGEMENT_CONTENTS.CONTENT_NAME);
        query.addFrom(Tables.EH_FILE_MANAGEMENT_CONTENTS);
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.CATALOG_ID.eq(catalogId));
        if (parentId != null)
            query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.PARENT_ID.eq(parentId));
        else
            query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.PARENT_ID.isNull());
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.CONTENT_NAME.like(name + "%"));
        if (suffix != null)
            query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.CONTENT_SUFFIX.eq(suffix));
        else
            query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.CONTENT_SUFFIX.isNull());
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.STATUS.eq(FileManagementStatus.VALID.getCode()));

        return query.fetchInto(String.class);
    }

    @Override
    public List<FileContent> queryFileContents(ListingLocator locator, Integer namespaceId, Long ownerId, ListingQueryBuilderCallback queryBuilderCallback){
        List<FileContent> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhFileManagementContentsRecord> query = context.selectQuery(Tables.EH_FILE_MANAGEMENT_CONTENTS);
        queryBuilderCallback.buildCondition(locator,query);
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.STATUS.eq(FileManagementStatus.VALID.getCode()));
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, FileContent.class));
            return null;
        });
        if (null != results && 0 != results.size()) {
            return results;
        }
        return null;
    }
 
    @Override
    public void deleteFileContentByContentPath(String path) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        UpdateQuery<EhFileManagementContentsRecord> query = context.updateQuery(Tables.EH_FILE_MANAGEMENT_CONTENTS);
        query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.PATH.like(path + "/%"));
        query.addValue(Tables.EH_FILE_MANAGEMENT_CONTENTS.STATUS, FileManagementStatus.INVALID.getCode());
        query.execute();
    }
 

//    @Override
//    public FileCatalog findAllStatusFileCatalogByName(Integer namespaceId, Long ownerId, String name) {
//        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
//
//        SelectQuery<EhFileManagementCatalogsRecord> query = context.selectQuery(Tables.EH_FILE_MANAGEMENT_CATALOGS);
//        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.NAMESPACE_ID.eq(namespaceId));
//        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.OWNER_ID.eq(ownerId));
//        query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.NAME.eq(name));
//
//        return query.fetchOneInto(FileCatalog.class);
//    }
}
