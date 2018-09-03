package com.everhomes.filemanagement;

import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
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
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FileManagementServiceImpl implements  FileManagementService{

    @Autowired
    private CoordinationProvider coordinationProvider;
    @Autowired
    private FileManagementProvider fileManagementProvider;

    @Autowired
    private ServiceModuleService serviceModuleService;

    @Autowired
    private PortalService portalService;

    @Autowired
    private FileProvider fileProvider;

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
        //目录为空或者被删除报错
        if (catalog == null || FileManagementStatus.INVALID == FileManagementStatus.fromCode(catalog.getStatus()))
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
        dto.setOperatorName(catalog.getOperatorName());
        dto.setUpdateTime(catalog.getUpdateTime());
        dto.setScopes(listFileCatalogScopes(UserContext.getCurrentNamespaceId(), dto.getId()));
        dto.setIdentify("catgalog" + catalog.getId());
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
        FileContent searchContent = null;
        if (cmd.getContentId() != null) {
            searchContent = fileManagementProvider.findFileContentById(cmd.getContentId());
        }
        //如果找得到文件夹就查文件夹path下的
        String searchPath = searchContent == null ? null: searchContent.getPath();
        List<FileCatalog> catalogResults = new ArrayList<>();
        //只有在选择多个目录搜索的时候才搜目录,并且在选择contentId搜索的时候不搜索目录
        if(cmd.getCatalogIds() != null && cmd.getCatalogIds().size() > 1 && cmd.getContentId() == null){
	        catalogResults = fileManagementProvider.queryFileCatalogs(new ListingLocator(), namespaceId, cmd.getOwnerId(), (locator, query) -> {
	            if (searchPath != null) {
	                //在文件夹下搜索就不搜索目录
	                query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.ID.isNull());
	            }
	            if (cmd.getCatalogIds() != null && cmd.getCatalogIds().size() > 0)
	                query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.ID.in(cmd.getCatalogIds()));
	            if (cmd.getKeywords() != null)
	                query.addConditions(Tables.EH_FILE_MANAGEMENT_CATALOGS.NAME.like("%" + cmd.getKeywords() + "%"));
	            query.addOrderBy(Tables.EH_FILE_MANAGEMENT_CATALOGS.CREATE_TIME.desc());
	            return query;
	        });
        }
        List<FileContent> folderResults = fileManagementProvider.queryFileContents(new ListingLocator(), namespaceId, cmd.getOwnerId(), (locator, query) -> {
            if (searchPath != null) {
                //搜索文件夹下
                query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.PATH.like(searchPath + "/%"));
            }
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
            if (searchPath != null) {
                //搜索文件夹下
                query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.PATH.like(searchPath + "/%"));
            }
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
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        if (catalog == null)
            return dto;
        Map<String, Long> map = checkFilePath(cmd.getPath(), catalog.getOwnerId());
        Long parentId = map.get("parentId");
        Long catalogId = map.get("catalogId");
        FileContent parentContent = null;
        if (null != parentId) {
            parentContent = fileManagementProvider.findFileContentById(parentId);
        }

        //  1.whether the name has been used
        String contentName = setContentNameAutomatically(namespaceId, catalog.getOwnerId(), catalogId,
                parentId, cmd.getContentName(), cmd.getContentSuffix());

        //  2.create it & set the path
        FileContent content = new FileContent();
        content.setNamespaceId(namespaceId);
        content.setOwnerId(catalog.getOwnerId());
        content.setOwnerType(catalog.getOwnerType());
        content.setCatalogId(catalogId);
        content.setParentId(parentId);
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
        //更新上级目录/文件夹更新时间
        updateParentContent(content);
        //  4.return back the dto
        dto = ConvertHelper.convert(content, FileContentDTO.class);

        return dto;
    }

    @Override
    public void deleteFileContents(DeleteFileContentCommand cmd) {
        for (FileContentDTO dto : cmd.getContents()) {
        	Long contentId = dto.getId();
            FileContent content = fileManagementProvider.findFileContentById(contentId);
            Map<String, Long> map = checkFilePath(dto.getPath(), content.getOwnerId());
            Long parentId = map.get("parentId");
            Long catalogId = map.get("catalogId");
            if (checkContentMoved(content, parentId, catalogId)) {
                return;
            }else {
                //更新上级目录/文件夹更新时间
                updateParentContent(content);
                fileManagementProvider.updateFileContentStatusByIds(contentId, FileManagementStatus.INVALID.getCode());
                if (FileContentType.fromCode(content.getContentType()) == FileContentType.FOLDER) {
                    fileManagementProvider.deleteFileContentByContentPath(content.getPath());
                }

            }
        }
    }

    @Override
    public FileContentDTO updateFileContentName(UpdateFileContentNameCommand cmd) {

        FileContent content = fileManagementProvider.findFileContentById(cmd.getContentId());
        if (content == null)
            throw RuntimeErrorException.errorWith(FileManagementErrorCode.SCOPE, FileManagementErrorCode.ERROR_FILE_CONTENT_NOT_FOUND,
                    "the file content not found.");
        checkContentValid(content);
        Map<String, Long> map = checkFilePath(cmd.getPath(), content.getOwnerId());
        Long parentId = map.get("parentId");
        Long catalogId = map.get("catalogId");
        //  1.check the name
        // 改动:根据path找目录(因为可能有移动)
        checkFileContentName(content.getNamespaceId(), content.getOwnerId(), catalogId, parentId,
                cmd.getContentName(), cmd.getContentSuffix(), cmd.getContentId());
        //  2.update the name
        content.setContentName(cmd.getContentName());
        
        if (checkContentMoved(content, parentId, catalogId)) {
            //移动了新建
            content.setCatalogId(catalogId);
            content.setParentId(parentId);
            if (parentId != null){
                FileContent parentContent = fileManagementProvider.findFileContentById(parentId);
                content.setPath(parentContent.getPath());
            }
            else
                content.setPath("");
            fileManagementProvider.createFileContent(content);
        } else {
            //没移动就更新
            fileManagementProvider.updateFileContent(content);
            updateParentContent(content);

        }
        //  3.return back
        return ConvertHelper.convert(content, FileContentDTO.class);
    }
    private void checkContentValid(FileContent content) {
    	if (FileManagementStatus.INVALID == FileManagementStatus.fromCode(content.getStatus())){
        	//对于已删除的 文件夹报错,文件恢复
	        if (FileContentType.fromCode(content.getContentType()) == FileContentType.FOLDER) {
	        	throw RuntimeErrorException.errorWith(FileManagementErrorCode.SCOPE, FileManagementErrorCode.ERROR_FILE_CONTENT_NOT_FOUND,
	                    "the file content not found.");
	        }else{
	        	content.setStatus(FileManagementStatus.VALID.getCode());
	        } 
        }
	}

	/**逐级向上更新父级文件夹和目录的更新人/更新时间*/
    private void updateParentContent(FileContent content) {
        if(content.getParentId() != null && !content.getParentId().equals(content.getId())){
            FileContent parentContent = fileManagementProvider.findFileContentById(content.getParentId());
            fileManagementProvider.updateFileContent(parentContent);
            updateParentContent(parentContent);
        }else{
            FileCatalog catalog = fileManagementProvider.findFileCatalogById(content.getCatalogId());
            fileManagementProvider.updateFileCatalog(catalog);
        }
    }

    /**
     * content和新的parentId,catalogId比较是否移动了
     * 移动了返回true,没有返回false
     * */
    private boolean checkContentMoved(FileContent content, Long parentId, Long catalogId) {
        if (parentId == null) {

            if (content.getParentId() == null && content.getCatalogId().equals(catalogId)) {
                //直接放在目录下且目录相同就是没移动
                return false;
            }
            //否则移动了
            return true;
        }
        else if (parentId.equals(content.getParentId())) {
            //如果有上级id 就比对上级id 一样的就是没移动
            return false;

        }else{
            //否则移动了
            return true;

        }
    }

    private String setContentNameAutomatically(Integer namespaceId, Long ownerId, Long catalogId, Long parentId,
                                               String name, String suffix) {
    	return setContentNameAutomatically(namespaceId, ownerId, catalogId, parentId, name, suffix, null);
    }
    private String setContentNameAutomatically(Integer namespaceId, Long ownerId, Long catalogId, Long parentId,
            String name, String suffix, Long originContentId) {
        FileContent content = fileManagementProvider.findFileContentByNameNotEqId(namespaceId, ownerId, catalogId, parentId,
                name, suffix, originContentId);
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
        FileContent content = fileManagementProvider.findFileContentByNameNotEqId(namespaceId, ownerId, catalogId, parentId,
                name, suffix, contentId);
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
        if(null == parentContent){
        	response.setPath(processPathString(cmd.getCatalogId() + ""));
        }
        else{
        	response.setPath(processPathString(parentContent.getCatalogId() + "/" + parentContent.getPath()+ "/" + cmd.getContentId()));
        }
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
        dto.setUpdateTime(content.getUpdateTime());
        dto.setPath(processPathString(content.getCatalogId() + "/" + content.getPath()));
        dto.setIdentify("content" + content.getId());

        return dto;
    }
    /** id的path 转换成String 的path */
    private String processPathString(String path) {
        String[] pathArray = StringUtils.split(path, "/");
        StringBuilder sb = new StringBuilder();
        FileCatalog catalog = fileManagementProvider.findFileCatalogById(Long.valueOf(pathArray[0]));
        sb.append("/");
        sb.append(catalog.getName());
        //不要最后一个路径(那是文件自身)
        for (int i = 1; i < pathArray.length - 1; i++) {
            FileContent content = fileManagementProvider.findFileContentById(Long.valueOf(pathArray[i]));
            if (null != content) {
                sb.append("/");
                sb.append(content.getContentName());
            }
        }
        return sb.toString();
    }

    @Override
	public void moveFileContent(MoveFileContentCommand cmd) {
        this.dbProvider.execute((TransactionStatus status) -> {
            Map<String, Long> map = checkFilePath(cmd.getTargetPath(), cmd.getOwnerId());
            Long catalogId = map.get("catalogId");
            Long parentId = map.get("parentId");

            for (UpdateFileContentNameCommand contentCmd : cmd.getContents()) {
                Long contentId = contentCmd.getContentId();
                FileContent content = fileManagementProvider.findFileContentById(contentId);
                //更新移动前的文件/文件夹更新时间
                updateParentContent(content);
                if (null == content) {
                    throw RuntimeErrorException.errorWith(FileManagementErrorCode.SCOPE, FileManagementErrorCode.ERROR_FILE_CONTENT_NOT_FOUND,
                            "content can not be null.");
                }
                checkContentValid(content);
                if (content.getStatus().equals(FileManagementStatus.INVALID.getCode())) {
                    content.setStatus(FileManagementStatus.VALID.getCode());
                    fileManagementProvider.updateFileContent(content);
                }
                content.setCatalogId(catalogId);
                //  1.whether the name has been used
                String contentName = setContentNameAutomatically(UserContext.getCurrentNamespaceId(), content.getOwnerId(), catalogId,
                        parentId, contentCmd.getContentName(), contentCmd.getContentSuffix(), contentId);
                content.setContentName(contentName);

                content.setParentId(parentId);
                if (parentId != null) {
                    FileContent parentContent = fileManagementProvider.findFileContentById(parentId);
                    if(StringUtils.isNotBlank(parentContent.getPath()) && 
                    		(parentContent.getPath().contains("/" + contentId + "/") || parentContent.getPath().endsWith("/" + contentId))){ 
                        throw RuntimeErrorException.errorWith(FileManagementErrorCode.SCOPE, FileManagementErrorCode.ERROR_CANNOT_MOVE,
                                "cant move to own sub Content.");
                    }
                    content.setPath(parentContent.getPath() + "/" + contentId);
                } else {
                    content.setPath("/" + contentId);
                }
                //移动后修改子文件夹/子文件的catalog和path
                updateSubContentCatalogAndPath(content,1);
                //更新移动后的文件/文件夹更新时间
                fileManagementProvider.updateFileContent(content);
                updateParentContent(content);
            }
            return null;
        });
	}
    /**
     * 递归更新所有子文件/文件夹 的catalog和path
     * */
    private void updateSubContentCatalogAndPath(FileContent parentContent, int loopTime) {
		if(loopTime++ > 100){
			throw RuntimeErrorException.errorWith(FileManagementErrorCode.SCOPE, FileManagementErrorCode.ERROR_LOOP,
                    "死循环");
		}
		if(FileContentType.fromCode(parentContent.getContentType()) == FileContentType.FOLDER){
			List<FileContent> subContents = fileManagementProvider.queryFileContents(new ListingLocator(), parentContent.getNamespaceId()
	                , parentContent.getOwnerId(), (locator, query) -> {
	                    query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.PARENT_ID.eq(parentContent.getId()));
	                    query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.ID.ne(parentContent.getId()));
	                    return query;
	                });
			if(!CollectionUtils.isEmpty(subContents)){
				for(FileContent content : subContents){
					content.setCatalogId(parentContent.getCatalogId());
					content.setPath(parentContent.getPath() + "/" + content.getId());
					fileManagementProvider.updateFileContent(content);
					//再更新自己下级文件/文件夹的
					updateSubContentCatalogAndPath(content, loopTime);
				}
			}
			
		}
	}

	private Map<String, Long> checkFilePath(String targetPath, Long ownerId) {
        if (null == targetPath) {
            throw RuntimeErrorException.errorWith(FileManagementErrorCode.SCOPE, FileManagementErrorCode.ERROR_FILE_CATALOG_NOT_FOUND,
                    "target path can not be null.");
        }
        String[] pathArray = null;
        if (targetPath.contains("/")) {
        	pathArray = StringUtils.split(targetPath, "/");
        } else {
            pathArray = new String[]{targetPath};
        }
        return checkFilePath(pathArray, ownerId);

    }

    private Map<String, Long> checkFilePath(String[] pathArray, Long ownerId) {
        Map<String, Long> result = new HashMap<>();
        FileCatalog catalog = fileManagementProvider.findFileCatalogByName(UserContext.getCurrentNamespaceId(), ownerId, pathArray[0]);
        if(null == catalog){ 
            throw RuntimeErrorException.errorWith(FileManagementErrorCode.SCOPE, FileManagementErrorCode.ERROR_FILE_CATALOG_NOT_FOUND,
                    "the file catalog not found.");
        }else if (catalog.getStatus().equals(FileManagementStatus.INVALID.getCode())) {
            catalog.setStatus(FileManagementStatus.VALID.getCode());
            fileManagementProvider.updateFileCatalog(catalog);
        }
        result.put("catalogId", catalog.getId());
        if (pathArray.length == 1) {
            return result;
        }
        Long parentId = null;
        for(int i = 1;i< pathArray.length;i++) {
        	final Long parentId1 = parentId;
        	final int i1 = i;
        	parentId = this.coordinationProvider.getNamedLock(CoordinationLocks.FILE_CONTENT_CHECK.getCode() + getCheckLockKey(catalog.getId(),parentId,pathArray[i])).enter(() -> {
                Long first = null;
	            FileContent content = fileManagementProvider.findFileContentByName(UserContext.getCurrentNamespaceId(), ownerId, catalog.getId(), parentId1, pathArray[i1], null);
	            if (null != content) {
	            	first = content.getId();
	                if (content.getStatus().equals(FileManagementStatus.INVALID.getCode())) {
	                    content.setStatus(FileManagementStatus.VALID.getCode());
	                    fileManagementProvider.updateFileContent(content);
	                }
	            }else{
	            	content = new FileContent();
	                content.setNamespaceId(catalog.getNamespaceId());
	                content.setOwnerId(catalog.getOwnerId());
	                content.setOwnerType(catalog.getOwnerType());
	                content.setCatalogId(catalog.getId());
	                content.setParentId(parentId1);
	                if (parentId1 != null){
	                	FileContent parentContent = fileManagementProvider.findFileContentById(parentId1);
	                    content.setPath(parentContent.getPath());
	                    }
	                else
	                    content.setPath("");
	                content.setContentType(FileContentType.FOLDER.getCode());
	                content.setContentName(pathArray[i1]);
	                fileManagementProvider.createFileContent(content);
	                first = content.getId();
	            }
	            return first;
            }).first();
        }
        result.put("parentId", parentId);
        return result;
    }

    private String getCheckLockKey(Long catalogId, Long parentId, String contentName) {
		return catalogId + "-" + parentId + "-" + StringUtils.substring(contentName, 0, 8);
	}

	@Override
	public GetFileIconListResponse getFileIconList() {
        List<FileIcon> results = fileProvider.listFileIcons();
        return new GetFileIconListResponse(results.stream().map(r -> {
            FileIconDTO dto = ConvertHelper.convert(r, FileIconDTO.class);
            dto.setIconUri(contentServerService.parserUri(r.getIconUri()));
            return dto;}).collect(Collectors.toList()));
        
    }

    @Override
    public ListAllFlodersResponse listAllFloders(ListAllFlodersCommand cmd) {
        ListAllFlodersResponse response = new ListAllFlodersResponse();
        Map<String, String> fileIcons = fileService.getFileIconUrl();
        List<FileCatalog> results = fileManagementProvider.listFileCatalogs(UserContext.getCurrentNamespaceId(), cmd.getOwnerId(), null, Integer.MAX_VALUE - 1, null);
        List<FileCatalogDTO> catalogs = new ArrayList<>();
        if (results != null && results.size() > 0) {
            results.forEach(r -> {
                FileCatalogDTO dto = convertToCatalogDTO(r, fileIcons);
                dto.setDownloadPermission(FileDownloadPermissionStatus.ALLOW.getCode());
                dto.setContents(listCataLogAllContents(r.getId(), cmd.getOwnerId(), fileIcons));
                catalogs.add(dto);
            });
        }
        response.setCatalogs(catalogs);
        return response;
    }

    private List<FileContentDTO> listCataLogAllContents(Long catalogId, Long ownerId, Map<String, String> fileIcons) {
        return listSubContents(catalogId, ownerId, fileIcons, null);
    }

    private List<FileContentDTO> listSubContents(Long catalogId, Long ownerId, Map<String, String> fileIcons, Long contentId) {
        List<FileContentDTO> results = new ArrayList<>();
        List<FileContent> topFloders = fileManagementProvider.queryFileContents(new ListingLocator(), UserContext.getCurrentNamespaceId()
                , ownerId, (locator, query) -> {
                    query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.CONTENT_TYPE.eq(FileContentType.FOLDER.getCode()));
                    query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.CATALOG_ID.eq(catalogId));
                    if (contentId == null) {
                        query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.PARENT_ID.isNull());

                    } else {
                        query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.PARENT_ID.eq(contentId));
                        //防止parentid和id一样,死循环
                        query.addConditions(Tables.EH_FILE_MANAGEMENT_CONTENTS.ID.ne(contentId));
                    }
                    query.addOrderBy(Tables.EH_FILE_MANAGEMENT_CONTENTS.CREATE_TIME.desc());
                    return query;
                });
        if (topFloders != null && topFloders.size() > 0) {
            topFloders.forEach(r -> {
                FileContentDTO dto = convertToFileContentDTO(r, fileIcons);
                dto.setContents(listSubContents(catalogId, ownerId, fileIcons, dto.getId()));
                results.add(dto);
            });
        }
        return results;
    }

}