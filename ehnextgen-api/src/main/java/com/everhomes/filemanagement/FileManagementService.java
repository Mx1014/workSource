package com.everhomes.filemanagement;

import com.everhomes.rest.filemanagement.*;

import java.util.List;

public interface FileManagementService {

    FileCatalogDTO addFileCatalog(AddFileCatalogCommand cmd);

    void deleteFileCatalog(FileCatalogIdCommand cmd);

    FileCatalogDTO updateFileCatalogName(UpdateFileCatalogNameCommand cmd);

    FileCatalogDTO listFileCatalogs(ListFileCatalogsCommand cmd);

    List<FileCatalogDTO> addFileCatalogScopes(AddFileCatalogScopesCommand cmd);

    void deleteFileCatalogScopes(FileCatalogScopesIdCommand cmd);

    void enableFileCatalogScopeDownload(FileCatalogScopesIdCommand cmd);

    void disableFileCatalogScopeDownload(FileCatalogScopesIdCommand cmd);

    ListFielCatalogScopeResponse listFileCatalogScopes(FileCatalogIdCommand cmd);

    FileContentDTO addFileContent(AddFileContentCommand cmd);

    void deleteFileContents(DeleteFileContentCommand cmd);

}
