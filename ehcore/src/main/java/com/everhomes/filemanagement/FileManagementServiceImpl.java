package com.everhomes.filemanagement;

import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.portal.PortalService;
import com.everhomes.rest.filemanagement.*;
import com.everhomes.rest.module.CheckModuleManageCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.uniongroup.UniongroupTargetType;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.sql.Timestamp;
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

    @Autowired
    private OrganizationProvider organizationProvider;

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
        dbProvider.execute((TransactionStatus status) -> {
            Long catalogId = fileManagementProvider.createFileCatalog(catalog);
            //  3.create the scope
            updateFileCatalogScopes(namespaceId, catalogId, cmd.getScopes());
            return null;
        });
        //  4.return back the dto
        dto.setId(catalog.getId());
        dto.setName(catalogName);
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

    @Override
    public void deleteFileCatalog(FileCatalogIdCommand cmd) {
        FileCatalog catalog = fileManagementProvider.findFileCatalogById(cmd.getCatalogId());
        if (catalog != null) {
            catalog.setStatus(FileManagementStatus.INVALID.getCode());
            dbProvider.execute((TransactionStatus status) ->{
                fileManagementProvider.updateFileCatalog(catalog);
                fileManagementProvider.deleteFileContentByCatalogId(catalog.getId());
                return null;
            });
        }
    }

    @Override
    public FileCatalogDTO updateFileCatalog(UpdateFileCatalogCommand cmd) {
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
        dbProvider.execute((TransactionStatus status) -> {
            fileManagementProvider.updateFileCatalog(catalog);
            //  3.create the scope
            updateFileCatalogScopes(namespaceId, catalog.getId(), cmd.getScopes());
            return null;
        });
        //  4.return back the dto
        dto.setId(catalog.getId());
        dto.setName(cmd.getCatalogName());
        dto.setCreateTime(catalog.getCreateTime());
        return dto;
    }

    private void checkTheCatalogName(Integer namespaceId, Long ownerId, String name, Long catalogId) {
        FileCatalog catalog = fileManagementProvider.findFileCatalogByName(namespaceId, ownerId, name);
        if (catalog != null) {
            if (catalog.getId().longValue() != catalogId.longValue())
                throw RuntimeErrorException.errorWith(FileManagementErrorCode.SCOPE, FileManagementErrorCode.ERROR_NAME_ALREADY_EXISTS,
                        "the name has been used.");
        }
    }

    private void updateFileCatalogScopes(Integer namespaceId, Long catalogId, List<FileCatalogScopeDTO> scopes) {
        List<Long> detailIds = new ArrayList<>();
        List<Long> organizationIds = new ArrayList<>();

        if (scopes != null && scopes.size() > 0)
            for (FileCatalogScopeDTO dto : scopes) {
                //  in order to record those ids.
                if (UniongroupTargetType.fromCode(dto.getSourceType()).equals(UniongroupTargetType.ORGANIZATION))
                    organizationIds.add(dto.getSourceId());
                else if (UniongroupTargetType.fromCode(dto.getSourceType()).equals(UniongroupTargetType.MEMBERDETAIL))
                    detailIds.add(dto.getSourceId());

                FileCatalogScope scope = fileManagementProvider.findFileCatalogScope(catalogId, dto.getSourceId(), dto.getSourceType());
                if (scope != null) {
                    scope.setSourceDescription(dto.getSourceDescription());
                    scope.setDownloadPermission(dto.getDownloadPermission());
                    scope.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    fileManagementProvider.updateFileCatalogScope(scope);
                } else {
                    scope = new FileCatalogScope();
                    scope.setNamespaceId(namespaceId);
                    scope.setCatalogId(catalogId);
                    scope.setSourceId(dto.getSourceId());
                    scope.setSourceType(dto.getSourceType());
                    scope.setSourceDescription(dto.getSourceDescription());
                    scope.setDownloadPermission(dto.getDownloadPermission());
                    fileManagementProvider.createFileCatalogScope(scope);
                }
            }

        //  remove the extra scope.
        if (detailIds.size() == 0)
            detailIds.add(0L);
        fileManagementProvider.deleteOddFileCatalogScope(namespaceId, catalogId, UniongroupTargetType.MEMBERDETAIL.getCode(), detailIds);
        if (organizationIds.size() == 0)
            organizationIds.add(0L);
        fileManagementProvider.deleteOddFileCatalogScope(namespaceId, catalogId, UniongroupTargetType.ORGANIZATION.getCode(), organizationIds);
    }

    @Override
    public FileCatalogDTO getFileCatalog(FileCatalogIdCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        FileCatalog catalog = fileManagementProvider.findFileCatalogById(cmd.getCatalogId());
        if(catalog == null)
            return null;
        FileCatalogDTO dto = new FileCatalogDTO();
        dto.setId(catalog.getId());
        dto.setName(catalog.getName());
        dto.setCreateTime(catalog.getCreateTime());
        dto.setScopes(listFileCatalogScopes(namespaceId, dto.getId()));
        return dto;
    }

    private List<FileCatalogScopeDTO> listFileCatalogScopes(Integer namespaceId, Long catalogId) {
        List<FileCatalogScopeDTO> scopes = new ArrayList<>();
        String keywords = null;

        List<FileCatalogScope> results = fileManagementProvider.listFileCatalogScopes(namespaceId, catalogId, keywords);
        if (results != null && results.size() > 0) {
            results.forEach(r -> scopes.add(ConvertHelper.convert(r, FileCatalogScopeDTO.class)));
        }
        return scopes;
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
            results.forEach(r -> {
                FileCatalogDTO dto = convertToCatalogDTO(r, fileIcons);
                dto.setDownloadPermission(FileDownloadPermissionStatus.ALLOW.getCode());
                catalogs.add(dto);
            });
        }
        response.setCatalogs(catalogs);
        response.setNextPageAnchor(nextPageAnchor);
        return response;
    }

    private FileCatalogDTO convertToCatalogDTO(FileCatalog catalog, Map<String, String> fileIcons){
        FileCatalogDTO dto = new FileCatalogDTO();
        dto.setId(catalog.getId());
        dto.setName(catalog.getName());
        dto.setIconUrl(fileIcons.get(FileContentType.CATEGORY.getCode()));
        dto.setCreateTime(catalog.getCreateTime());
        return dto;
    }

    @Override
    public ListFileCatalogResponse listAvailableFileCatalogs(ListFileCatalogsCommand cmd) {
        ListFileCatalogResponse response = new ListFileCatalogResponse();
        List<FileCatalogDTO> catalogs = new ArrayList<>();
        User user = UserContext.current().getUser();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        //  1.the user is the administrator
        if (checkFileManagementAdmin(cmd.getOwnerId(), cmd.getOwnerType(), user.getId())) {
            cmd.setPageSize(Integer.MAX_VALUE - 1);
            response = listFileCatalogs(cmd);
        } else {
            //  2.normal user
            List<FileCatalogDTO> results = listFileCatalogs(cmd).getCatalogs();
            if(results == null || results.size() == 0)
                return response;
            OrganizationMember member = organizationProvider.findDepartmentMemberByTargetIdAndOrgId(user.getId(), cmd.getOwnerId());
            if(member == null)
                member = organizationProvider.findActiveOrganizationMemberByOrgIdAndUId(user.getId(), cmd.getOwnerId());
            for(FileCatalogDTO result: results)
                if(checkTheScope(namespaceId, result, member))
                    catalogs.add(result);
            response.setCatalogs(catalogs);
        }
        return response;
    }

    private boolean checkTheScope(Integer namespaceId, FileCatalogDTO catalog, OrganizationMember member) {
        //  whether the user is in the scope.
        //  set the catalog's permission
        if (member == null)
            return false;
        List<FileCatalogScopeDTO> scopes = listFileCatalogScopes(namespaceId, catalog.getId());
        Map<Long, Byte> userScope = scopes.stream()
                .filter(p1 -> p1.getSourceType().equals(UniongroupTargetType.MEMBERDETAIL.getCode()))
                .collect(Collectors.toMap(FileCatalogScopeDTO::getSourceId, FileCatalogScopeDTO::getDownloadPermission));
        Map<Long, Byte> depScope = scopes.stream()
                .filter(p1 -> p1.getSourceType().equals(UniongroupTargetType.ORGANIZATION.getCode()))
                .collect(Collectors.toMap(FileCatalogScopeDTO::getSourceId, FileCatalogScopeDTO::getDownloadPermission));
        if (userScope.containsKey(member.getDetailId())) {
            catalog.setDownloadPermission(userScope.get(member.getDetailId()));
            return true;
        }
        for (Long depId : depScope.keySet())
            if (member.getGroupPath().contains(String.valueOf(depId))) {
                catalog.setDownloadPermission(depScope.get(depId));
                return true;
            }
        return false;
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
            appId = apps.getServiceModuleApps().get(0).getOriginId();
        }
        CheckModuleManageCommand moduleCommand = new CheckModuleManageCommand();
        moduleCommand.setAppId(appId);
        moduleCommand.setModuleId(55000L);
        moduleCommand.setOwnerId(ownerId);
        moduleCommand.setOwnerType(ownerType);
        moduleCommand.setOrganizationId(ownerId);
        moduleCommand.setUserId(userId);
        Byte result = serviceModuleService.checkModuleManage(moduleCommand);
        return result == 1;
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
            catalogResults.forEach(r -> catalogs.add(convertToCatalogDTO(r, fileIcons)));

        if (folderResults != null && folderResults.size() > 0) {
            folderResults.forEach(r -> folders.add(convertToFileContentDTO(r, fileIcons)));
        }
        if (contentResults != null && contentResults.size() > 0) {
            contentResults.forEach(r -> files.add(convertToFileContentDTO(r, fileIcons)));
        }

        response.setCatalogs(catalogs);
        response.setFolders(folders);
        response.setFiles(files);
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
        return ConvertHelper.convert(content, FileContentDTO.class);
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
    
	@Override
	public void moveFileContent(MoveFileContentCommand cmd) {
	

	}

	@Override
	public GetFileIconResponse getFileIcon(GetFileIconCommand cmd) {
	
		return new GetFileIconResponse();
	}

}