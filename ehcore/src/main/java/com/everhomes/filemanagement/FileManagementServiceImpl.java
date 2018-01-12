package com.everhomes.filemanagement;

import com.everhomes.rest.filemanagement.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FileManagementServiceImpl implements  FileManagementService{
    @Override
    public FileCatalogDTO addFileCatalog(AddFileCatalogCommand cmd) {
        return null;
    }

    @Override
    public void deleteFileCatalog(FileCatalogIdCommand cmd) {

    }

    @Override
    public FileCatalogDTO updateFileCatalogName(UpdateFileCatalogNameCommand cmd) {
        return null;
    }

    @Override
    public FileCatalogDTO listFileCatalogs(ListFileCatalogsCommand cmd) {
        return null;
    }

    @Override
    public List<FileCatalogDTO> addFileCatalogScopes(AddFileCatalogScopesCommand cmd) {
        return null;
    }

    @Override
    public void deleteFileCatalogScopes(FileCatalogScopesIdCommand cmd) {

    }

    @Override
    public void enableFileCatalogScopeDownload(FileCatalogScopesIdCommand cmd) {

    }

    @Override
    public void disableFileCatalogScopeDownload(FileCatalogScopesIdCommand cmd) {

    }

    @Override
    public ListFielCatalogScopeResponse listFileCatalogScopes(FileCatalogIdCommand cmd) {
        return null;
    }

    @Override
    public FileContentDTO addFileContent(AddFileContentCommand cmd) {
        return null;
    }

    @Override
    public void deleteFileContents(DeleteFileContentCommand cmd) {

    }
}
