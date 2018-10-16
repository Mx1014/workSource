package com.everhomes.filemanagement;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface FileManagementProvider {

    Long createFileCatalog(FileCatalog catalog);

    void updateFileCatalog(FileCatalog catalog);

    FileCatalog findFileCatalogById(Long id);

    FileCatalog findFileCatalogByName(Integer namespaceId, Long ownerId, String name);

    List<String> listFileCatalogNames(Integer namespaceId, Long ownerId, String name);

    List<FileCatalog> listFileCatalogs(Integer namespaceId, Long ownerId, Long pageAnchor, Integer pageSize, String keywords);

    List<FileCatalog> queryFileCatalogs(ListingLocator locator, Integer namespaceId, Long ownerId, ListingQueryBuilderCallback queryBuilderCallback);

//    List<FileCatalog> listAvailableFileCatalogs(Integer namespaceId, Long ownerId, Long detailId);

    void createFileCatalogScope(FileCatalogScope scope);

    void deleteOddFileCatalogScope(Integer namespaceId, Long catalogId, String sourceType, List<Long> sourceIds);

    void updateFileCatalogScope(FileCatalogScope scope);

    FileCatalogScope findFileCatalogScope(Long catalogId, Long sourceId, String sourceType);

    List<FileCatalogScope> listFileCatalogScopes(Integer namespaceId, Long catalogId, String keywords);

    void createFileContent(FileContent content);

    void updateFileContentStatusByIds(Long id, Byte status);

    void deleteFileContentByCatalogId(Long catalogId);

    void updateFileContent(FileContent content);

    FileContent findFileContentById(Long id);

    FileContent findFileContentByName(Integer namespaceId, Long ownerId, Long catalogId, Long parentId, String name, String suffix);

//    FileContent findAllStatusFileContentByName(Integer namespaceId, Long ownerId, Long catalogId, Long parentId, String name, String suffix);

    List<String> listFileContentNames(Integer namespaceId, Long ownerId, Long catalogId, Long parentId, String name, String suffix);

    List<FileContent> queryFileContents(ListingLocator locator, Integer namespaceId, Long ownerId, ListingQueryBuilderCallback queryBuilderCallback);
 
	FileContent findFileContentByNameNotEqId(Integer namespaceId, Long ownerId, Long catalogId,
			Long parentId, String name, String suffix, Long originContentId);
 
    void deleteFileContentByContentPath(String path);
 

//    FileCatalog findAllStatusFileCatalogByName(Integer currentNamespaceId, Long ownerId, String name);
}
