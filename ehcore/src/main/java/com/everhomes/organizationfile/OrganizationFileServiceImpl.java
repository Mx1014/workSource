// @formatter:off
package com.everhomes.organizationfile;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.contentserver.ResourceType;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.objectstorage.ObjectStorageService;
import com.everhomes.objectstorage.OsObject;
import com.everhomes.objectstorage.OsObjectDownloadLog;
import com.everhomes.organization.OrganizationCommunityRequest;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.energy.util.ParamErrorCodes;
import com.everhomes.rest.objectstorage.OsObjectDownloadLogQuery;
import com.everhomes.rest.objectstorage.OsObjectQuery;
import com.everhomes.rest.objectstorage.OsObjectStatus;
import com.everhomes.rest.objectstorage.OsObjectType;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organizationfile.*;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.Tuple;
import org.apache.commons.lang.StringUtils;
import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Size;
import javax.validation.metadata.ConstraintDescriptor;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.everhomes.util.RuntimeErrorException.errorWith;

/**
 * Created by xq.tian on 2017/2/16.
 */
@Service
public class OrganizationFileServiceImpl implements OrganizationFileService {

    private final static Logger LOGGER = LoggerFactory.getLogger(OrganizationFileServiceImpl.class);
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    private ObjectStorageService objectStorageService;

    @Autowired
    private ContentServerService contentServerService;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private CommunityProvider communityProvider;

    @Override
    public OrganizationFileDTO createOrganizationFile(CreateOrganizationFileCommand cmd) {
        validate(cmd);
        this.checkCurrentUserNotInOrg(cmd.getOrganizationId());
        ContentServerResource resource = contentServerService.findResourceByUri(cmd.getContentUri());

        if (resource == null) {
            LOGGER.error("resource not exists, {}", cmd.getContentUri());
            throw errorWith(OrganizationFileServiceErrorCode.SCOPE, OrganizationFileServiceErrorCode.ERROR_RESOURCE_NOT_EXISTS,
                    "resource not exists");
        }

        OsObject obj = new OsObject();

        obj.setNamespaceId(currNamespaceId());
        obj.setContentUri(cmd.getContentUri());
        obj.setName(resource.getResourceName());
        obj.setServiceType(EntityType.ORGANIZATION_FILE.getCode());
        obj.setSize(resource.getResourceSize());
        obj.setObjectType(OsObjectType.FILE.getCode());
        obj.setContentType(ResourceType.file.name());
        obj.setDownloadCount(0);

        String md5 = base64Decode(resource.getResourceMd5());
        obj.setMd5(md5.substring(md5.indexOf(":") + 1));// 1:f5082a953f61ba8f74c204727fb990f8

        Community community = communityProvider.findCommunityById(cmd.getCommunityId());
        if (community != null) {
            obj.setOwnerType(EntityType.COMMUNITY.getCode());
            obj.setOwnerId(cmd.getCommunityId());
        } else {
            obj.setOwnerType(EntityType.NAMESPACE.getCode());
            obj.setOwnerId(Long.valueOf(currNamespaceId()));
        }

        obj = objectStorageService.createOsObject(obj);
        return toOrganizationFileDTO(obj);
    }

    private String base64Decode(String base64Str) {
        try {
            byte[] decode = Base64.getDecoder().decode(base64Str);
            return new String(decode, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return base64Str;
    }


    private OrganizationFileDTO toOrganizationFileDTO(OsObject obj) {
        OrganizationFileDTO dto = ConvertHelper.convert(obj, OrganizationFileDTO.class);
        dto.setContentUrl(this.parseUri(obj.getContentUri(), obj.getOwnerType(), obj.getOwnerId()));
        dto.setCreateTime(obj.getCreateTime().getTime());
        dto.setSize(this.parseFileSizeStr(obj.getSize()));
        User user = this.findUserById(obj.getCreatorUid());
        dto.setCreatorName(user.getNickName());
        return dto;
    }

    private String parseFileSizeStr(Integer objSize) {
        String sizeStr = "0";
        String unitStr = "KB";
        if (objSize != null) {
            double size = objSize / 1024.0;
            if (size >= 1024) {
                size = size / 1024.0;
                unitStr = "MB";
            }
            NumberFormat format = new DecimalFormat("#.##");
            sizeStr = format.format(size);
        }
        return (sizeStr + unitStr);
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        // double x = 91499 / 1024.0;
        // System.out.println(x);
        // NumberFormat format = new DecimalFormat("#.##");
        // String s = format.format(x);
        // System.out.println(s);

        String base64Str = "MTpmNTA4MmE5NTNmNjFiYThmNzRjMjA0NzI3ZmI5OTBmOA";
        byte[] decode = Base64.getDecoder().decode(base64Str);
        String x = new String(decode, "UTF-8");
        System.out.println(x.substring(x.indexOf(":")));
    }

    @Override
    public SearchOrganizationFileResponse searchOrganizationFileByCommunity(SearchOrganizationFileByCommunityCommand cmd) {
        validate(cmd);
        this.checkCurrentUserNotInOrg(cmd.getOrganizationId());

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

        OsObjectQuery query = new OsObjectQuery();
        query.setName(cmd.getKeyword());
        query.setPageSize(pageSize);
        query.setStatus(OsObjectStatus.ACTIVE.getCode());
        query.setServiceType(EntityType.ORGANIZATION_FILE.getCode());

        Community community = communityProvider.findCommunityById(cmd.getCommunityId());
        if (community != null) {
            query.setOwnerType(EntityType.COMMUNITY.getCode());
            query.setOwnerId(cmd.getCommunityId());
        } else {
            query.setOwnerType(EntityType.NAMESPACE.getCode());
            query.setOwnerId(Long.valueOf(currNamespaceId()));
        }

        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        List<OsObject> list = objectStorageService.listOsObject(query, locator);

        SearchOrganizationFileResponse response = new SearchOrganizationFileResponse();
        response.setNextPageAnchor(locator.getAnchor());
        response.setList(list.stream().map(this::toOrganizationFileDTO).collect(Collectors.toList()));
        return response;
    }

    @Override
    public SearchOrganizationFileResponse searchOrganizationFileByOrganization(SearchOrganizationFileByOrganizationCommand cmd) {
        validate(cmd);
        this.checkCurrentUserNotInOrg(cmd.getOrganizationId());
        OrganizationCommunityRequest ocr = organizationProvider.getOrganizationCommunityRequestByOrganizationId(cmd.getOrganizationId());

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        OsObjectQuery query = new OsObjectQuery();
        query.setPageSize(cmd.getPageSize());

        query.setName(cmd.getKeyword());
        query.setPageSize(pageSize);
        query.setStatus(OsObjectStatus.ACTIVE.getCode());
        query.setServiceType(EntityType.ORGANIZATION_FILE.getCode());

        ListingQueryBuilderCallback callback = (locator, selectQuery) -> {
            // 把域空间下的文件和小区下的文件一起查出来
            Condition namespaceCondition = Tables.EH_OS_OBJECTS.OWNER_TYPE.eq(EntityType.NAMESPACE.getCode())
                    .and(Tables.EH_OS_OBJECTS.OWNER_ID.eq(Long.valueOf(currNamespaceId())));
            Condition communityCondition = Tables.EH_OS_OBJECTS.OWNER_TYPE.eq(EntityType.COMMUNITY.getCode())
                    .and(Tables.EH_OS_OBJECTS.OWNER_ID.eq(ocr.getCommunityId()));
            selectQuery.addConditions(namespaceCondition.or(communityCondition));
            return selectQuery;
        };

        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        List<OsObject> list = objectStorageService.listOsObject(query, locator, callback);

        SearchOrganizationFileResponse response = new SearchOrganizationFileResponse();
        response.setNextPageAnchor(locator.getAnchor());
        response.setList(list.stream().map(this::toOrganizationFileDTO).collect(Collectors.toList()));
        return response;
    }

    @Override
    public OrganizationFileDownloadLogsDTO createOrganizationFileDownloadLog(CreateOrganizationFileDownloadLogCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        Tuple<OsObjectDownloadLog, Boolean> tuple = coordinationProvider.getNamedLock(CoordinationLocks.OS_OBJECT.getCode() + cmd.getFileId()).enter(() -> {
            OsObject obj = objectStorageService.findById(currNamespaceId(), cmd.getFileId());
            if (obj != null) {
                // 下载次数 +1
                obj.setDownloadCount(obj.getDownloadCount() + 1);
                objectStorageService.updateOsObject(obj);
                // 下载记录
                return objectStorageService.createOsObjectDownloadLog(cmd.getFileId(), currUserId());
            }
            return null;
        });

        if (tuple.first() != null) {
            return toOrganizationFileDownloadLogDTO(tuple.first());
        }
        return new OrganizationFileDownloadLogsDTO();
    }

    private OrganizationFileDownloadLogsDTO toOrganizationFileDownloadLogDTO(OsObjectDownloadLog log) {
        OrganizationFileDownloadLogsDTO dto = new OrganizationFileDownloadLogsDTO();
        dto.setId(log.getId());
        dto.setCreateTime(log.getCreateTime().getTime());
        User user = this.findUserById(log.getCreatorUid());
        dto.setCreatorName(user.getNickName());

        List<OrganizationDTO> orgs = organizationService.listUserRelateOrganizations(currNamespaceId(), log.getCreatorUid(), OrganizationGroupType.ENTERPRISE);
        if (orgs != null) {
            List<String> organizationNames = orgs.stream().map(OrganizationDTO::getName).collect(Collectors.toList());
            dto.setCreatorOrganizations(organizationNames);
        }
        return dto;
    }

    @Override
    public ListOrganizationFileDownloadLogsResponse listOrganizationFileDownloadLogs(ListOrganizationFileDownloadLogsCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

        OsObjectDownloadLogQuery query = new OsObjectDownloadLogQuery();
        query.setObjectId(cmd.getFileId());
        query.setPageSize(pageSize);

        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        List<OsObjectDownloadLog> logs = objectStorageService.listOsObjectDownloadLogs(query, locator);
        ListOrganizationFileDownloadLogsResponse response = new ListOrganizationFileDownloadLogsResponse();

        response.setList(logs.stream().map(this::toOrganizationFileDownloadLogDTO).collect(Collectors.toList()));
        response.setNextPageAnchor(locator.getAnchor());
        return response;
    }

    @Override
    public void deleteOrganizationFile(DeleteOrganizationFileCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        Tuple<Boolean, Boolean> tuple = coordinationProvider.getNamedLock(CoordinationLocks.OS_OBJECT.getCode() + cmd.getFileId()).enter(() -> {
            OsObject obj = objectStorageService.findById(currNamespaceId(), cmd.getFileId());
            if (obj != null) {
                objectStorageService.deleteOsObject(obj);
                return true;
            }
            return false;
        });

        if (!tuple.first()) {
            LOGGER.error("resource not exists, {}", cmd.getFileId());
            throw errorWith(OrganizationFileServiceErrorCode.SCOPE, OrganizationFileServiceErrorCode.ERROR_RESOURCE_NOT_EXISTS,
                    "resource not exists");
        }
    }

    private String parseUri(String uri, String ownerType, long ownerId) {
        try {
            if (StringUtils.isNotEmpty(uri))
                return contentServerService.parserUri(uri, ownerType, ownerId);
        } catch (Exception e) {
            LOGGER.error("Parse organization file uri error {}", e);
        }
        return null;
    }

    private Long currUserId() {
        return UserContext.current().getUser().getId();
    }

    private Integer currNamespaceId() {
        return UserContext.getCurrentNamespaceId();
    }

    private User findUserById(Long userId) {
        User user = userProvider.findUserById(userId);
        if (user != null) {
            return user;
        }
        LOGGER.error("User is not exist, userId = {}", userId);
        throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_USER_NOT_EXIST, "User is not exist userId = %s", userId);
    }

    private void checkCurrentUserNotInOrg(Long orgId) {
        /*if (orgId == null) {
            LOGGER.error("Invalid parameter organizationId [ null ]");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid parameter organizationId [ null ]");
        }
        Long userId = currUserId();
        OrganizationMember member = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(userId, orgId);
        if (member == null) {
            LOGGER.error("User is not in the organization.");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "User is not in the organization.");
        }*/
    }

    // 参数校验方法
    // 可以校验带bean validation 注解的对象
    // 校验失败, 抛出异常, 异常信息附带参数值信息
    private void validate(Object o) {
        Set<ConstraintViolation<Object>> result = validator.validate(o);

        for (ConstraintViolation<Object> v : result) {
            ConstraintDescriptor<?> constraintDescriptor = v.getConstraintDescriptor();
            String constraintAnnotationClassName = constraintDescriptor.getAnnotation().annotationType().getName();
            switch (constraintAnnotationClassName) {
                // 参数长度检查
                case "javax.validation.constraints.Size":
                    Size size = (Size) constraintDescriptor.getAnnotation();
                    int value = size.max();
                    if (value > 0) {
                        LOGGER.error("Parameter over length: [ {} ]", v.getPropertyPath());
                        throw errorWith(ParamErrorCodes.SCOPE, ParamErrorCodes.ERROR_OVER_LENGTH,
                                "Parameter over length: [ %s ]", v.getPropertyPath());
                    }
                    break;
                // 其他参数检查
                default:
                    LOGGER.error("Invalid parameter {} [ {} ]", v.getPropertyPath(), v.getInvalidValue());
                    throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                            "Invalid parameter %s [ %s ]", v.getPropertyPath(), v.getInvalidValue());
            }
        }
    }
}
