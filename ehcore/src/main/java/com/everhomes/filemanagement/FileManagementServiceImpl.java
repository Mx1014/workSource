package com.everhomes.filemanagement;

import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
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
import org.springframework.transaction.TransactionStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private FileService fileService;

    @Autowired
    private DbProvider dbProvider;

    @Override
    public FileCatalogDTO addFileCatalog(AddFileCatalogCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        FileCatalogDTO dto = new FileCatalogDTO();
        //  1.set the catalog name automatically
        String catalogName = setCatalogNameAutomatically(namespaceId, cmd.getOwnerId(), cmd.getCatalogName());
        //  2.create it
        FileCatalog catalog = new FileCatalog();
        catalog.setNamespaceId(namespaceId);
        catalog.setOwnerId(cmd.getOwnerId());
        catalog.setOwnerType(cmd.getOwnerType());
        catalog.setName(catalogName);
        fileManagementProvider.createFileCatalog(catalog);
        //  3.return back the dto
        dto.setId(catalog.getId());
        dto.setName(catalogName);
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
        if (catalog == null)
            throw RuntimeErrorException.errorWith(FileManagementErrorCode.SCOPE, FileManagementErrorCode.ERROR_FILE_CATALOG_NOT_FOUND,
                    "the file catalog not found.");

        //  1.whether the name has been used
        checkTheCatalogName(namespaceId, catalog.getOwnerId(), cmd.getCatalogName(), cmd.getCatalogId());
        //  2.update the name
        catalog.setName(cmd.getCatalogName());
        fileManagementProvider.updateFileCatalog(catalog);
        //  3.return back the dto
        dto.setId(catalog.getId());
        dto.setName(cmd.getCatalogName());
        dto.setCreateTime(catalog.getCreateTime());
        return dto;
    }

    private String setCatalogNameAutomatically(Integer namespaceId, Long ownerId, String name) {
        FileCatalog catalog = fileManagementProvider.findFileCatalogByName(namespaceId, ownerId, name);
        if (catalog != null) {
            List<String> allNames = fileManagementProvider.listFileCatalogNames(namespaceId, ownerId, name);
            for (int n = 1; n <= allNames.size() + 1; n++) {
                String newName = name + "(" + n + ")";
                if (!allNames.contains(newName))
                    return newName;
            }
            throw RuntimeErrorException.errorWith(FileManagementErrorCode.SCOPE, FileManagementErrorCode.ERROR_NAME_GENERATE_FAILED,
                    "automatically set the name failed.");
        } else {
            return name;
        }
    }

    private void checkTheCatalogName(Integer namespaceId, Long ownerId, String name, Long catalogId) {
        FileCatalog catalog = fileManagementProvider.findFileCatalogByName(namespaceId, ownerId, name);
        if (catalog != null) {
            if (catalog.getId().longValue() != catalogId.longValue())
                throw RuntimeErrorException.errorWith(FileManagementErrorCode.SCOPE, FileManagementErrorCode.ERROR_NAME_ALREADY_EXISTS,
                        "the name has been used.");
        }
    }

    @Override
    public ListFileCatalogResponse listFileCatalogs(ListFileCatalogsCommand cmd) {
        ListFileCatalogResponse response = new ListFileCatalogResponse();
        List<FileCatalogDTO> catalogs = new ArrayList<>();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Map<String, String> fileIcons = fileService.getFileIconUrl();
        Long nextPageAnchor = null;
        if (cmd.getPageSize() == null)
            cmd.setPageSize(55);

        List<FileCatalog> results = fileManagementProvider.listFileCatalogs(namespaceId, cmd.getOwnerId(), cmd.getPageAnchor(), cmd.getPageSize(), cmd.getKeywords());
        if (results != null && results.size() > 0) {
            if (results.size() > cmd.getPageSize()) {
                results.remove(results.size() - 1);
                nextPageAnchor = results.get(results.size() - 1).getId();
            }
            results.forEach(r ->{
                catalogs.add(convertToCatalogDTO(r, true, fileIcons));
            });
        }
        response.setCatalogs(catalogs);
        response.setNextPageAnchor(nextPageAnchor);
        return response;
    }

    private FileCatalogDTO convertToCatalogDTO(FileCatalog catalog, boolean isAdmin, Map<String, String> fileIcons){
            FileCatalogDTO dto = new FileCatalogDTO();
            dto.setId(catalog.getId());
            dto.setName(catalog.getName());
            if (isAdmin)
                dto.setDownloadPermission(FileDownloadPermissionStatus.ALLOW.getCode());
            else
                dto.setDownloadPermission(catalog.getDownloadPermission());
            dto.setIconUrl(fileIcons.get(FileContentType.CATEGORY.getCode()));
            dto.setCreateTime(catalog.getCreateTime());
        return dto;
    }

    @Override
    public ListFileCatalogResponse listAvailableFileCatalogs(ListFileCatalogsCommand cmd) {
        ListFileCatalogResponse response = new ListFileCatalogResponse();
        Map<String, String> fileIcons = fileService.getFileIconUrl();
        List<FileCatalogDTO> catalogs = new ArrayList<>();
        User user = UserContext.current().getUser();
        //  1.the user is the administrator
        if (checkFileManagementAdmin(cmd.getOwnerId(), cmd.getOwnerType(), user.getId())) {
            cmd.setPageSize(Integer.MAX_VALUE - 1);
            response = listFileCatalogs(cmd);
        } else {
            //  2.normal user
            List<FileCatalog> results = fileManagementProvider.listAvailableFileCatalogs(user.getNamespaceId(), cmd.getOwnerId(), user.getId());
            if (results != null && results.size() > 0) {
                results.forEach(r -> {
                    catalogs.add(convertToCatalogDTO(r, false, fileIcons));
                });
            }
            response.setCatalogs(catalogs);
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
        Map<String, String> fileIcons = fileService.getFileIconUrl();

        List<FileCatalog> catalogResults = fileManagementProvider.queryFileCatalogs(new ListingLocator(), namespaceId, cmd.getOwnerId(), (locator, query) -> {
            if (cmd.getCatalogIds() != null && cmd.getCatalogIds().size() > 0)
                query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.ID.in(cmd.getCatalogIds()));
            if (cmd.getKeywords() != null)
                query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.NAME.like("%" + cmd.getKeywords() + "%"));
            query.addOrderBy(Tables.EH_FILE_MANAGEMENT_CATALOGS.CREATE_TIME.desc());
            return query;
        });

        List<FileContent> folderResults = fileManagementProvider.queryFileContents(new ListingLocator(), namespaceId, cmd.getOwnerId(), (locator, query) -> {
            query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.CONTENT_TYPE.eq(FileContentType.FOLDER.getCode()));
            if (cmd.getCatalogIds() != null && cmd.getCatalogIds().size() > 0)
                query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.CATALOG_ID.in(cmd.getCatalogIds()));
            if (cmd.getKeywords() != null)
                query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.CONTENT_NAME.like("%" + cmd.getKeywords() + "%"));
            query.addOrderBy(Tables.EH_FILE_MANAGEMENT_CONTENTS.CREATE_TIME.desc());
            return query;
        });

        List<FileContent> contentResults = fileManagementProvider.queryFileContents(new ListingLocator(), namespaceId, cmd.getOwnerId(), (locator, query) -> {
            query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.CONTENT_TYPE.ne(FileContentType.FOLDER.getCode()));
            if (cmd.getCatalogIds() != null && cmd.getCatalogIds().size() > 0)
                query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.CATALOG_ID.in(cmd.getCatalogIds()));
            if (cmd.getKeywords() != null)
                query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.CONTENT_NAME.like("%" + cmd.getKeywords() + "%"));
            query.addOrderBy(Tables.EH_FILE_MANAGEMENT_CONTENTS.CREATE_TIME.desc());
            return query;
        });

        if (catalogResults != null && catalogResults.size() > 0)
            catalogResults.forEach(r ->{
                catalogs.add(convertToCatalogDTO(r, false, fileIcons));
            });

        if (folderResults != null && folderResults.size() > 0) {
            folderResults.forEach(r -> {
                folders.add(convertToFileContentDTO(r, fileIcons));
            });
        }
        if (contentResults != null && contentResults.size() > 0) {
            contentResults.forEach(r -> {
                files.add(convertToFileContentDTO(r, fileIcons));
            });
        }

        response.setCatalogs(catalogs);
        response.setFolders(folders);
        response.setFiles(files);
        return response;
    }

    @Override
    public List<FileCatalogScopeDTO> addFileCatalogScopes(AddFileCatalogScopesCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<FileCatalogScopeDTO> scopes = new ArrayList<>();

        if (cmd.getScopes() == null || cmd.getScopes().size() <= 0){
            //  delete all scopes by catalogId
            fileManagementProvider.deleteFileCatalogScopeNotInSourceIds(namespaceId, cmd.getCatalogId(), new ArrayList<>());
            return scopes;
        }

        dbProvider.execute((TransactionStatus status) -> {
            List<Long> sourceIds = new ArrayList<>();
            cmd.getScopes().forEach(r -> {
                //  1.save sourceIds
                sourceIds.add(r.getSourceId());
                //  2.create or update the scope
                FileCatalogScopeDTO dto = createFileCatalogScope(namespaceId, cmd.getCatalogId(), r);
                //  3.save the data which is returning back
                scopes.add(dto);
            });
            //  4.delete redundant data
            fileManagementProvider.deleteFileCatalogScopeNotInSourceIds(namespaceId, cmd.getCatalogId(), sourceIds);
            return null;
        });
        return scopes;
    }

    private FileCatalogScopeDTO createFileCatalogScope(Integer namespaceId, Long catalogId,  FileCatalogScopeDTO dto){
        FileCatalogScope scope = fileManagementProvider.findFileCatalogScopeBySourceId(catalogId, dto.getSourceId());
        FileCatalogScopeDTO result = new FileCatalogScopeDTO();

        if(scope != null){
            scope.setSourceDescription(dto.getSourceDescription());
            fileManagementProvider.updateFileCatalogScope(scope);
        }else{
            scope = new FileCatalogScope();
            scope.setNamespaceId(namespaceId);
            scope.setCatalogId(catalogId);
            scope.setSourceId(dto.getSourceId());
            scope.setSourceDescription(dto.getSourceDescription());
            fileManagementProvider.createFileCatalogScope(scope);
        }
        //  return the dto back
        result.setCatalogId(catalogId);
        result.setSourceId(scope.getSourceId());
        result.setSourceDescription(scope.getSourceDescription());
        result.setDownloadPermission(FileDownloadPermissionStatus.REFUSE.getCode());
        return result;
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
        //  set the max pageSize
        cmd.setPageSize(Integer.MAX_VALUE-1);

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
        FileContent parentContent = fileManagementProvider.findFileContentById(cmd.getParentId());
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        if (catalog == null)
            return dto;

        //  1.whether the name has been used
        String contentName = setContentNameAutomatically(namespaceId, catalog.getOwnerId(), catalog.getId(),
                cmd.getParentId(), cmd.getContentName(), cmd.getContentSuffix());

        //  2.create it & set the path
        FileContent content = new FileContent();
        content.setNamespaceId(catalog.getNamespaceId());
        content.setOwnerId(catalog.getOwnerId());
        content.setOwnerType(catalog.getOwnerType());
        content.setCatalogId(catalog.getId());
        content.setParentId(cmd.getParentId());
        if (parentContent != null)
            content.setPath(parentContent.getPath());
        else
            content.setPath("");
        content.setContentType(cmd.getContentType());
        content.setContentName(contentName);

        //  3.the content is file
        if (!content.getContentType().equals(FileContentType.FOLDER.getCode())) {
            if (cmd.getContentSuffix() == null)
                throw RuntimeErrorException.errorWith(FileManagementErrorCode.SCOPE, FileManagementErrorCode.ERROR_SUFFIX_NULL,
                        "the suffix can not be null.");
            content.setContentSuffix(cmd.getContentSuffix().toLowerCase());
            content.setSize(cmd.getContentSize());
            content.setContentUri(cmd.getContentUri());
        }
        fileManagementProvider.createFileContent(content);
        //  4.return back the dto
        dto = ConvertHelper.convert(content, FileContentDTO.class);

        return dto;
    }

    @Override
    public void deleteFileContents(DeleteFileContentCommand cmd) {
        fileManagementProvider.updateFileContentStatusByIds(cmd.getContendIds(), FileManagementStatus.INVALID.getCode());
    }

    @Override
    public FileContentDTO updateFileContentName(UpdateFileContentNameCommand cmd) {
        FileContent content = fileManagementProvider.findFileContentById(cmd.getContentId());
        if (content == null)
            throw RuntimeErrorException.errorWith(FileManagementErrorCode.SCOPE, FileManagementErrorCode.ERROR_FILE_CONTENT_NOT_FOUND,
                    "the file content not found.");

        //  1.check the name
        checkFileContentName(content.getNamespaceId(), content.getOwnerId(), content.getCatalogId(), content.getParentId(),
                cmd.getContentName(), cmd.getContentSuffix(), cmd.getContentId());
        //  2.update the name
        content.setContentName(cmd.getContentName());
        fileManagementProvider.updateFileContent(content);
        //  3.return back
        FileContentDTO dto = ConvertHelper.convert(content, FileContentDTO.class);
        return dto;
    }

    private String setContentNameAutomatically(Integer namespaceId, Long ownerId, Long catalogId, Long parentId,
                                               String name, String suffix) {
        FileContent content = fileManagementProvider.findFileContentByName(namespaceId, ownerId, catalogId, parentId,
                name, suffix);
        if (content != null) {
            List<String> allNames = fileManagementProvider.listFileContentNames(namespaceId, ownerId, catalogId, parentId,
                    name, suffix);
            for (int n = 1; n <= allNames.size() + 1; n++) {
                String newName = name + "(" + n + ")";
                if (!allNames.contains(newName))
                    return newName;
            }
            throw RuntimeErrorException.errorWith(FileManagementErrorCode.SCOPE, FileManagementErrorCode.ERROR_NAME_GENERATE_FAILED,
                    "automatically set the name failed.");
        } else
            return name;
    }

    private void checkFileContentName(Integer namespaceId, Long ownerId, Long catalogId, Long parentId, String name,
                                      String suffix, Long contentId) {
        FileContent content = fileManagementProvider.findFileContentByName(namespaceId, ownerId, catalogId, parentId,
                name, suffix);
        if (content != null) {
            if (content.getId().longValue() != contentId.longValue())
                throw RuntimeErrorException.errorWith(FileManagementErrorCode.SCOPE, FileManagementErrorCode.ERROR_NAME_ALREADY_EXISTS,
                        "the name has been used.");
        }
    }

    @Override
    public ListFileContentResponse listFileContents(ListFileContentCommand cmd) {
        ListFileContentResponse response = new ListFileContentResponse();
        List<FileContentDTO> folders = new ArrayList<>();
        List<FileContentDTO> files = new ArrayList<>();
        Map<String, String> fileIcons = fileService.getFileIconUrl();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        FileCatalog catalog = fileManagementProvider.findFileCatalogById(cmd.getCatalogId());
        //  find the parent content to use the path
        FileContent parentContent = fileManagementProvider.findFileContentById(cmd.getContentId());
        if (catalog == null)
            return response;

        List<FileContent> results = fileManagementProvider.queryFileContents(new ListingLocator(), namespaceId, catalog.getOwnerId(), (locator, query) -> {
            query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.CATALOG_ID.eq(catalog.getId()));
            //  1.get results by the keywords
            if(cmd.getKeywords() != null){
                query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.CONTENT_NAME.like("%" + cmd.getKeywords() + "%"));
                if (parentContent != null)
                    query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.PATH.like(parentContent.getPath() + "/" + "%"));
            }else{
                //  2.get results without keywords
                if (parentContent != null)
                    query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.PARENT_ID.eq(parentContent.getId()));
                else
                    query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.PARENT_ID.isNull());
            }
            query.addOrderBy(Tables.EH_FILE_MANAGEMENT_CONTENTS.CREATE_TIME.desc());
            return query;
        });

        //  3.process the result
        if (results != null && results.size() > 0) {
            results.stream().filter(r -> r.getContentType().equals(FileContentType.FOLDER.getCode())).map(r -> {
                FileContentDTO dto = convertToFileContentDTO(r, fileIcons);
                folders.add(dto);
                return null;
            }).collect(Collectors.toList());
            results.stream().filter(r -> !r.getContentType().equals(FileContentType.FOLDER.getCode())).map(r -> {
                FileContentDTO dto = convertToFileContentDTO(r, fileIcons);
                files.add(dto);
                return null;
            }).collect(Collectors.toList());
        }

        response.setFolders(folders);
        response.setFiles(files);
        return response;
    }

    private FileContentDTO convertToFileContentDTO(FileContent content, Map<String, String> fileIcons) {
        FileContentDTO dto = ConvertHelper.convert(content, FileContentDTO.class);

        if (content.getContentType().equals(FileContentType.FOLDER.getCode())){
            dto = ConvertHelper.convert(content, FileContentDTO.class);
            dto.setName(content.getContentName());
            dto.setIconUrl(fileIcons.get(FileContentType.FOLDER.getCode()));
        }
        else {
            dto.setName(content.getContentName() + "." + content.getContentSuffix());
            dto.setContentUrl(contentServerService.parserUri(dto.getContentUri()));
            dto.setIconUrl(fileIcons.get(content.getContentSuffix()));
            if (dto.getIconUrl() == null)
                dto.setIconUrl(fileIcons.get("other"));
        }
        return dto;
    }
}
