package com.everhomes.filemanagement;

import java.util.List;

public interface FileManagementProvider {

    void createFileCatalog(FileCatalog catalog);

    void updateFileCatalog(FileCatalog catalog);

    FileCatalog findFileCatalogById(Long id);

    FileCatalog findFileCatalogByName(Integer namespaceId, Long ownerId, String name);

    List<FileCatalog> listFileCatalogs(Integer namespaceId, Long ownerId, Long pageAnchor, Integer pageSize, String keywords);

    List<FileCatalog> listAvailableFileCatalogs(Integer namespaceId, Long ownerId, Long userId);

    void createFileCatalogScope(FileCatalogScope scope);

    void deleteFileCatalogScopeByUserIds(Long catalogId, List<Long> sourceIds);

    void updateFileCatalogScopeDownload(Long catalogId, List<Long> sourceIds, Byte permission);

    List<FileCatalogScope> listFileCatalogScopes(Integer namespaceId, Long catalogId, Long pageAnchor, Integer pageSize, String keywords);

    void createFileContent(FileContent content);

    void updateFileContentStatusByIds(List<Long> ids, Byte status);

    void updateFileContent(FileContent content);

    FileContent findFileContentById(Long id);

    FileContent findFileContentByName(Integer namespaceId, Long ownerId, String name);
}
