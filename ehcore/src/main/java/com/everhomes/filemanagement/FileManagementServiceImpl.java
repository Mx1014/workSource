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
    public ListFileCatalogResponse listFileCatalogs(ListFileCatalogsCommand cmd) {
        return null;
    }

    @Override
    public ListFileCatalogResponse listAvailableFileContents(ListFileCatalogsCommand cmd) {
        return null;
    }

    @Override
    public ListFileContentResponse enterFileCatalog(FileCatalogIdCommand cmd) {
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
    public ListFileCatalogScopeResponse listFileCatalogScopes(ListFileCatalogScopeCommand cmd) {
        return null;
    }

    @Override
    public FileContentDTO addFileContent(AddFileContentCommand cmd) {
        return null;
    }

    @Override
    public void deleteFileContents(DeleteFileContentCommand cmd) {

    }

    @Override
    public FileContentDTO updateFileContentName(UpdateFileContentNameCommand cmd) {
        return null;
    }

    @Override
    public ListFileContentResponse listFileContents(ListFileContentCommand cmd) {
        return null;
    }
}
