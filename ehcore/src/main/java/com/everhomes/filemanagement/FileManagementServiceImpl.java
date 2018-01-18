package com.everhomes.filemanagement;

import com.everhomes.contentserver.ContentServerService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.portal.PortalService;
import com.everhomes.rest.filemanagement.*;
import com.everhomes.rest.module.CheckModuleManageCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FileManagementServiceImpl implements  FileManagementService{

    @Autowired
    private FileManagementProvider fileManagementProvider;

    @Autowired
    private ServiceModuleService serviceModuleService;

    @Autowired
    private PortalService portalService;

    @Autowired
    private ContentServerService contentServerService;

    @Override
    public FileCatalogDTO addFileCatalog(AddFileCatalogCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        FileCatalogDTO dto = new FileCatalogDTO();
        //  1.whether the name has been used
        checkTheCatalogName(namespaceId, cmd.getOwnerId(), cmd.getCatalogName());
        //  2.create it
        FileCatalog catalog = new FileCatalog();
        catalog.setNamespaceId(namespaceId);
        catalog.setOwnerId(cmd.getOwnerId());
        catalog.setOwnerType(cmd.getOwnerType());
        catalog.setName(cmd.getCatalogName());
        fileManagementProvider.createFileCatalog(catalog);
        //  3.return back the dto
        dto.setId(catalog.getId());
        dto.setName(cmd.getCatalogName());
        dto.setCreateTime(catalog.getCreateTime());
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
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        FileCatalogDTO dto = new FileCatalogDTO();
        if(catalog!=null){
            //  1.whether the name has been used
            checkTheCatalogName(namespaceId, catalog.getOwnerId(), cmd.getCatalogName());
            //  2.update the name
            catalog.setName(cmd.getCatalogName());
            fileManagementProvider.updateFileCatalog(catalog);
            //  3.return back the dto
            dto.setId(catalog.getId());
            dto.setName(cmd.getCatalogName());
            dto.setCreateTime(catalog.getCreateTime());
        }
        return dto;
    }

    private void checkTheCatalogName(Integer namespaceId, Long ownerId, String name) {
        FileCatalog catalog = fileManagementProvider.findFileCatalogByName(namespaceId, ownerId, name);
        if (catalog != null)
            throw RuntimeErrorException.errorWith(FileManagementErrorCode.SCOPE, FileManagementErrorCode.ERROR_NAME_ALREADY_EXISTS,
                    "the name has been used.");
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
            if(r.getDownloadPermission() != null)
                dto.setDownloadPermission(r.getDownloadPermission());
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
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        SearchFileResponse response = new SearchFileResponse();
        List<FileCatalogDTO> catalogs = new ArrayList<>();
        List<FileContentDTO> folders = new ArrayList<>();
        List<FileContentDTO> files = new ArrayList<>();

        List<FileCatalog> catalogResults = fileManagementProvider.queryFileCatalogs(new ListingLocator(), namespaceId, cmd.getOwnerId(), ((locator, query) -> {
            if (cmd.getCatalogIds() != null && cmd.getCatalogIds().size() > 0)
                query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.ID.in(cmd.getCatalogIds()));
            if (cmd.getKeywords() != null)
                query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.NAME.like(cmd.getKeywords()));
            return query;
        }));

        return null;
    }

    @Override
    public List<FileCatalogScopeDTO> addFileCatalogScopes(AddFileCatalogScopesCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<FileCatalogScopeDTO> scopes = new ArrayList<>();

        //  todo:可能需要删除未勾选的
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
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        if (catalog == null)
            return dto;
        if (cmd.getParentId() == null)
            cmd.setParentId(cmd.getCatalogId());

        //todo：文件类型的判断
        //  1.whether the name has been used
        checkFileContentName(namespaceId, catalog.getOwnerId(), cmd.getParentId(), cmd.getContentName());
        //  2.create it
        FileContent content = new FileContent();
        content.setNamespaceId(catalog.getNamespaceId());
        content.setOwnerId(catalog.getOwnerId());
        content.setOwnerType(catalog.getOwnerType());
        content.setCatalogId(catalog.getId());
        content.setName(cmd.getContentName());
        content.setSize(cmd.getContentSize());
        content.setParentId(cmd.getParentId());
        content.setContentType(cmd.getContentType());
        content.setContentUri(cmd.getContentUri());
        fileManagementProvider.createFileContent(content);
        //  3.return back the dto
        dto = ConvertHelper.convert(content, FileContentDTO.class);

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
            //  1.check the name
            checkFileContentName(content.getNamespaceId(), content.getOwnerId(), content.getParentId(), cmd.getContentName());
            //  2.update the name
            content.setName(cmd.getContentName());
            fileManagementProvider.updateFileContent(content);
            //  3.return back
            dto.setId(content.getId());
            dto.setName(content.getName());
        }
        return dto;
    }

    private void checkFileContentName(Integer namespaceId, Long ownerId, Long parentId, String name){
        FileContent content = fileManagementProvider.findFileContentByName(namespaceId, ownerId, parentId, name);
        if (content != null)
            throw RuntimeErrorException.errorWith(FileManagementErrorCode.SCOPE, FileManagementErrorCode.ERROR_NAME_ALREADY_EXISTS,
                    "the name has been used.");
    }

    @Override
    public ListFileContentResponse listFileContents(ListFileContentCommand cmd) {
        ListFileContentResponse response = new ListFileContentResponse();
        List<FileContentDTO> folders = new ArrayList<>();
        List<FileContentDTO> files = new ArrayList<>();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        if(cmd.getContentId() == null)
            cmd.setContentId(cmd.getCatalogId());
        FileCatalog catalog = fileManagementProvider.findFileCatalogById(cmd.getCatalogId());
        if(catalog == null)
            return response;

        List<FileContent> results = fileManagementProvider.listFileContents(namespaceId,catalog.getOwnerId(),catalog.getId(),cmd.getContentId(),cmd.getKeywords());
        if(results !=null && results.size() > 0){
            results.stream().filter(r -> r.getContentType().equals(FileContentType.FOLDER.getCode())).map(r ->{
                FileContentDTO dto = ConvertHelper.convert(r, FileContentDTO.class);
                folders.add(dto);
                return null;
            }).collect(Collectors.toList());
            //todo:图标的处理
            results.stream().filter(r -> !r.getContentType().equals(FileContentType.FOLDER.getCode())).map(r ->{
                FileContentDTO dto = ConvertHelper.convert(r, FileContentDTO.class);
                if(dto.getContentUri() != null)
                    dto.setContentUrl(contentServerService.parserUri(dto.getContentUri()));
                files.add(dto);
                return null;
            }).collect(Collectors.toList());
        }

        response.setFolders(folders);
        response.setFiles(files);
        return response;
    }
}
