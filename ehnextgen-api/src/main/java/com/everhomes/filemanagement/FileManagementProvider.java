package com.everhomes.filemanagement;

import java.util.List;

public interface FileManagementProvider {

    void createFileCatalog(FileCatalog catalog);

    void updateFileCatalog(FileCatalog catalog);

    FileCatalog findFileCatalogById(Long id);

    FileCatalog findFileCatalogByName(Integer namespaceId, Long ownerId, String name);

    List<FileCatalog> listFileCatalogs(Integer namespaceId, Long ownerId, Long pageAnchor, Integer pageSize, String keywords);
}
