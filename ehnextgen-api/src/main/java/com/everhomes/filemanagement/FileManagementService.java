package com.everhomes.filemanagement;

import com.everhomes.rest.filemanagement.*;

import java.util.List;

public interface FileManagementService {

    FileCatalogDTO addFileCatalog(AddFileCatalogCommand cmd);

    void deleteFileCatalog(FileCatalogIdCommand cmd);

    FileCatalogDTO updateFileCatalogName(UpdateFileCatalogNameCommand cmd);

    ListFileCatalogResponse listFileCatalogs(ListFileCatalogsCommand cmd);

    ListFileCatalogResponse listAvailableFileCatalogs(ListFileCatalogsCommand cmd);

    SearchFileResponse searchFiles(SearchFileCommand cmd);

    List<FileCatalogScopeDTO> addFileCatalogScopes(AddFileCatalogScopesCommand cmd);

    void deleteFileCatalogScopes(FileCatalogScopesIdCommand cmd);

    void enableFileCatalogScopeDownload(FileCatalogScopesIdCommand cmd);

    void disableFileCatalogScopeDownload(FileCatalogScopesIdCommand cmd);

    ListFileCatalogScopeResponse listFileCatalogScopes(ListFileCatalogScopeCommand cmd);

    FileContentDTO addFileContent(AddFileContentCommand cmd);

    void deleteFileContents(DeleteFileContentCommand cmd);

    FileContentDTO updateFileContentName(UpdateFileContentNameCommand cmd);

    ListFileContentResponse listFileContents(ListFileContentCommand cmd);
}
