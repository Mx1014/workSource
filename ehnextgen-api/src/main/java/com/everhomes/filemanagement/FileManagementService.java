package com.everhomes.filemanagement;

import com.everhomes.rest.filemanagement.*;

import java.util.List;

public interface FileManagementService {

    FileCatalogDTO addFileCatalog(AddFileCatalogCommand cmd);

    void deleteFileCatalog(FileCatalogIdCommand cmd);

    FileCatalogDTO updateFileCatalogName(UpdateFileCatalogNameCommand cmd);

    FileCatalogDTO listFileCatalogs(ListFileCatalogsCommand cmd);

    List<FileCatalogDTO> addFileCatalogScopes(AddFileCatalogScopesCommand cmd);
}
