package com.everhomes.filemanagement;

import com.everhomes.rest.filemanagement.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FileManagementServiceImpl implements  FileManagementService{

    @Autowired
    private FileManagementProvider fileManagementProvider;

    @Override
    public FileCatalogDTO addFileCatalog(AddFileCatalogCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        FileCatalogDTO dto = new FileCatalogDTO();
        //  1.whether the name has been used
        FileCatalog catalog = fileManagementProvider.findFileCatalogByName(namespaceId,cmd.getOwnerId(),cmd.getCatalogName());
        if(catalog != null){
            throw RuntimeErrorException.errorWith(FileManagementErrorCode.SCOPE, FileManagementErrorCode.ERROR_NAME_ALREADY_EXISTS,
                    "the name has been used.");
        }else{
        //  2.create it
            catalog = new FileCatalog();
            catalog.setNamespaceId(namespaceId);
            catalog.setOwnerId(cmd.getOwnerId());
            catalog.setOwnerType(cmd.getOwnerType());
            catalog.setName(cmd.getCatalogName());
            fileManagementProvider.createFileCatalog(catalog);
            //  3.return back the dto
            dto.setId(catalog.getId());
            dto.setName(cmd.getCatalogName());
            dto.setCreateTime(catalog.getCreateTime());
        }
        return dto;
    }

    @Override
    public void deleteFileCatalog(FileCatalogIdCommand cmd) {
        FileCatalog catalog = fileManagementProvider.findFileCatalogById(cmd.getCatalogId());
        if(catalog!=null){
            catalog.setStatus(FileCatalogStatus.INVALID.getCode());
            fileManagementProvider.updateFileCatalog(catalog);
        }
    }

    @Override
    public FileCatalogDTO updateFileCatalogName(UpdateFileCatalogNameCommand cmd) {
        FileCatalog catalog = fileManagementProvider.findFileCatalogById(cmd.getCatalogId());
        FileCatalogDTO dto = new FileCatalogDTO();
        if(catalog!=null){
            //  update the name
            catalog.setName(cmd.getCatalogName());
            fileManagementProvider.updateFileCatalog(catalog);
            //  return back the dto
            dto.setId(catalog.getId());
            dto.setName(cmd.getCatalogName());
            dto.setCreateTime(catalog.getCreateTime());
        }
        return dto;
    }

    @Override
    public ListFileCatalogResponse listFileCatalogs(ListFileCatalogsCommand cmd) {
        ListFileCatalogResponse response = new ListFileCatalogResponse();
        List<FileCatalogDTO> catalogs = new ArrayList<>();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Long nextPageAnchor = null;
        if (cmd.getPageSize() == null)
            cmd.setPageSize(55);

        List<FileCatalog> results = fileManagementProvider.listFileCatalogs(namespaceId, cmd.getOwnerId(), cmd.getPageAnchor(), cmd.getPageSize(), cmd.getKeywords());
        if (results != null && results.size() > 0) {
            if (results.size() > cmd.getPageSize()) {
                results.remove(results.size() - 1);
                nextPageAnchor = results.get(results.size() - 1).getId();
            }

            results.forEach(r -> {
                FileCatalogDTO dto = new FileCatalogDTO();
                dto.setId(r.getId());
                dto.setName(r.getName());
                dto.setCreateTime(r.getCreateTime());
            });
        }
        response.setCatalogs(catalogs);
        response.setNextPageAnchor(nextPageAnchor);
        return response;
    }

    @Override
    public ListFileCatalogResponse listAvailableFileContents(ListFileCatalogsCommand cmd) {
        return null;
    }

    @Override
    public SearchFileResponse searchFiles(SearchFileCommand cmd) {
        return null;
    }

    @Override
    public List<FileCatalogScopeDTO> addFileCatalogScopes(AddFileCatalogScopesCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<FileCatalogScopeDTO> scopes = new ArrayList<>();

        if (cmd.getScopes() != null && cmd.getScopes().size() > 0) {
            cmd.getScopes().forEach(r ->{
                FileCatalogScope scope = new FileCatalogScope();
                FileCatalogScopeDTO dto = new FileCatalogScopeDTO();
                scope.setNamespaceId(namespaceId);
                scope.setCatalogId(cmd.getCatalogId());
                scope.setSourceId(r.getSourceId());
                scope.setSourceDescription(r.getSourceDescription());
                fileManagementProvider.createFileCatalogScope(scope);
                //  return the dto back
                dto.setCatalogId(cmd.getCatalogId());
                dto.setSourceId(scope.getSourceId());
                dto.setSourceDescription(scope.getSourceDescription());
                dto.setDownloadPermission(FileDownloadPermissionStatus.REFUSE.getCode());
                scopes.add(dto);
            });
        }
        return scopes;
    }

    @Override
    public void deleteFileCatalogScopes(FileCatalogScopesIdCommand cmd) {
        if(cmd.getCatalogId() != null)
            fileManagementProvider.deleteFileCatalogScopeByUserIds(cmd.getCatalogId(), cmd.getSourceIds());
    }

    @Override
    public void enableFileCatalogScopeDownload(FileCatalogScopesIdCommand cmd) {
        if(cmd.getCatalogId() != null)
            fileManagementProvider.updateFileCatalogScopeDownload(cmd.getCatalogId(), cmd.getSourceIds(),FileDownloadPermissionStatus.ALLOW.getCode());
    }

    @Override
    public void disableFileCatalogScopeDownload(FileCatalogScopesIdCommand cmd) {
        if(cmd.getCatalogId() != null)
            fileManagementProvider.updateFileCatalogScopeDownload(cmd.getCatalogId(), cmd.getSourceIds(),FileDownloadPermissionStatus.REFUSE.getCode());
    }

    @Override
    public ListFileCatalogScopeResponse listFileCatalogScopes(ListFileCatalogScopeCommand cmd) {
        ListFileCatalogScopeResponse response = new ListFileCatalogScopeResponse();
        List<FileCatalogScopeDTO> scopes = new ArrayList<>();
        Long nextPageAnchor = null;
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        if (cmd.getPageSize() == null)
            cmd.setPageSize(20);

        List<FileCatalogScope> results = fileManagementProvider.listFileCatalogScopes(namespaceId, cmd.getCatalogId(), cmd.getPageAnchor(), cmd.getPageSize(), cmd.getKeywords());
        if (results != null && results.size() > 0) {
            if (results.size() > cmd.getPageSize()) {
                results.remove(results.size() - 1);
                nextPageAnchor = results.get(results.size() - 1).getId();
            }
            results.forEach(r -> {
                scopes.add(ConvertHelper.convert(r, FileCatalogScopeDTO.class));
            });
        }
        response.setScopes(scopes);
        response.setNextPageAnchor(nextPageAnchor);
        return response;
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
