package com.everhomes.filemanagement;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface FileManagementProvider {

    void createFileCatalog(FileCatalog catalog);

    void updateFileCatalog(FileCatalog catalog);

    FileCatalog findFileCatalogById(Long id);

    FileCatalog findFileCatalogByName(Integer namespaceId, Long ownerId, String name);

    List<String> listFileCatalogNames(Integer namespaceId, Long ownerId, String name);

    List<FileCatalog> listFileCatalogs(Integer namespaceId, Long ownerId, Long pageAnchor, Integer pageSize, String keywords);

    List<FileCatalog> queryFileCatalogs(ListingLocator locator, Integer namespaceId, Long ownerId, ListingQueryBuilderCallback queryBuilderCallback);

    List<FileCatalog> listAvailableFileCatalogs(Integer namespaceId, Long ownerId, Long userId);

    void createFileCatalogScope(FileCatalogScope scope);

    void deleteFileCatalogScopeByCatalogId(Integer namespaceId, Long catalogId);

    void deleteFileCatalogScopeNotInSourceIds(Integer namespaceId, Long catalogId, List<Long> sourceIds);

    void deleteFileCatalogScopeByUserIds(Long catalogId, List<Long> sourceIds);

    void updateFileCatalogScope(FileCatalogScope scope);

    void updateFileCatalogScopeDownload(Long catalogId, List<Long> sourceIds, Byte permission);

    FileCatalogScope findFileCatalogScopeBySourceId(Long catalogId, Long sourceId);

    List<FileCatalogScope> listFileCatalogScopes(Integer namespaceId, Long catalogId, Long pageAnchor, Integer pageSize, String keywords);

    void createFileContent(FileContent content);

    void updateFileContentStatusByIds(List<Long> ids, Byte status);

    void updateFileContent(FileContent content);

    FileContent findFileContentById(Long id);

    FileContent findFileContentByName(Integer namespaceId, Long ownerId, Long catalogId, Long parentId, String name, String suffix);

    List<String> listFileContentNames(Integer namespaceId, Long ownerId, Long catalogId, Long parentId, String name, String suffix);

//    List<FileContent> listFileContents(Integer namespaceId, Long ownerId, Long catalogId, String path, String keywords);

    List<FileContent> queryFileContents(ListingLocator locator, Integer namespaceId, Long ownerId, ListingQueryBuilderCallback queryBuilderCallback);
}
