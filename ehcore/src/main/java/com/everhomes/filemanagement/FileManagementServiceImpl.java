package com.everhomes.filemanagement;

import com.everhomes.module.ServiceModuleService;
import com.everhomes.portal.PortalService;
import com.everhomes.rest.filemanagement.*;
import com.everhomes.rest.module.CheckModuleManageCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
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

    @Autowired
    private ServiceModuleService serviceModuleService;

    @Autowired
    private PortalService portalService;

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
            catalog.setStatus(FileManagementStatus.INVALID.getCode());
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
            catalogs = convertToCatalogDTO(results);
        }
        response.setCatalogs(catalogs);
        response.setNextPageAnchor(nextPageAnchor);
        return response;
    }

    private List<FileCatalogDTO> convertToCatalogDTO(List<FileCatalog> results){
        List<FileCatalogDTO> catalogs = new ArrayList<>();
        results.forEach(r -> {
            FileCatalogDTO dto = new FileCatalogDTO();
            dto.setId(r.getId());
            dto.setName(r.getName());
            dto.setCreateTime(r.getCreateTime());
            catalogs.add(dto);
        });
        return catalogs;
    }

    @Override
    public ListFileCatalogResponse listAvailableFileCatalogs(ListFileCatalogsCommand cmd) {
        ListFileCatalogResponse response = new ListFileCatalogResponse();
        List<FileCatalogDTO> catalogs = new ArrayList<>();
        User user = UserContext.current().getUser();
        //  1.the user is the administrator
        if(checkFileManagementAdmin(cmd.getOwnerId(), cmd.getOwnerType(), user.getId())){
            cmd.setPageSize(Integer.MAX_VALUE -1);
            response = listFileCatalogs(cmd);
        }else{
            //  2.normal user
            List<FileCatalog> results = fileManagementProvider.listAvailableFileCatalogs(user.getNamespaceId(), cmd.getOwnerId(), user.getId());
            if(results != null && results.size() > 0){
                catalogs = convertToCatalogDTO(results);
                response.setCatalogs(catalogs);
            }
        }
        return response;
    }

    private boolean checkFileManagementAdmin(Long ownerId, String ownerType, Long userId){
        ListServiceModuleAppsCommand appCommand = new ListServiceModuleAppsCommand();
        Byte actionType = 69;
        appCommand.setActionType(actionType);
        appCommand.setModuleId(55000L);
        appCommand.setNamespaceId(UserContext.getCurrentNamespaceId());
        ListServiceModuleAppsResponse apps = portalService.listServiceModuleAppsWithConditon(appCommand);
        Long appId = null;
        if(null != apps && apps.getServiceModuleApps().size() > 0){
            appId = apps.getServiceModuleApps().get(0).getId();
        }
        CheckModuleManageCommand moduleCommand = new CheckModuleManageCommand();
        moduleCommand.setAppId(appId);
        moduleCommand.setModuleId(55000L);
        moduleCommand.setOwnerId(ownerId);
        moduleCommand.setOwnerType(ownerType);
        moduleCommand.setOrganizationId(ownerId);
        moduleCommand.setUserId(userId);
        Byte result = serviceModuleService.checkModuleManage(moduleCommand);
        if(result == 1)
            return true;
        else
            return false;
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
        FileContentDTO dto = new FileContentDTO();
        FileCatalog catalog = fileManagementProvider.findFileCatalogById(cmd.getCatalogId());
        if(catalog == null)
            return dto;

        //  1.whether the name has been used
        FileContent content = fileManagementProvider.findFileContentByName(catalog.getNamespaceId(),catalog.getOwnerId(),cmd.getContentName());
        if(content != null){
            throw RuntimeErrorException.errorWith(FileManagementErrorCode.SCOPE, FileManagementErrorCode.ERROR_NAME_ALREADY_EXISTS,
                    "the name has been used.");
        }else{
            //  2.create it
            content = new FileContent();
            content.setNamespaceId(catalog.getNamespaceId());
            content.setOwnerId(catalog.getOwnerId());
            content.setOwnerType(catalog.getOwnerType());
            content.setCatalogId(catalog.getId());
            content.setName(cmd.getContentName());
            if(cmd.getContentSize() != null)
                content.setSize(cmd.getContentSize());
            if(cmd.getParentId() != null)
                content.setParentId(cmd.getParentId());
            content.setContentType(cmd.getContentType());
            if(cmd.getContentUri() != null)
                content.setContentUri(cmd.getContentUri());
            fileManagementProvider.createFileContent(content);
            //  3.return back the dto
            dto = ConvertHelper.convert(content, FileContentDTO.class);
        }
        return dto;
    }

    @Override
    public void deleteFileContents(DeleteFileContentCommand cmd) {
        fileManagementProvider.updateFileContentStatusByIds(cmd.getContendIds(), FileManagementStatus.INVALID.getCode());
    }

    @Override
    public FileContentDTO updateFileContentName(UpdateFileContentNameCommand cmd) {
        FileContentDTO dto = new FileContentDTO();
        FileContent content = fileManagementProvider.findFileContentById(cmd.getContentId());
        if (content != null) {
            //  update the name
            content.setName(cmd.getContentName());
            fileManagementProvider.updateFileContent(content);
            //  return back
            dto.setId(content.getId());
            dto.setName(content.getName());
        }
        return dto;
    }

    @Override
    public ListFileContentResponse listFileContents(ListFileContentCommand cmd) {
        return null;
    }
}
