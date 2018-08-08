package com.everhomes.fixedasset;

import com.alibaba.fastjson.JSON;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.filedownload.TaskService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.organization.ExecuteImportTaskCallback;
import com.everhomes.organization.ImportFileService;
import com.everhomes.organization.ImportFileTask;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.filedownload.TaskRepeatFlag;
import com.everhomes.rest.filedownload.TaskType;
import com.everhomes.rest.fixedasset.BatchDeleteFixedAssetCommand;
import com.everhomes.rest.fixedasset.BatchUpdateFixedAssetCommand;
import com.everhomes.rest.fixedasset.ByteStringMapDTO;
import com.everhomes.rest.fixedasset.CreateFixedAssetCategoryCommand;
import com.everhomes.rest.fixedasset.CreateOrUpdateFixedAssetCommand;
import com.everhomes.rest.fixedasset.DeleteFixedAssetCategoryCommand;
import com.everhomes.rest.fixedasset.DeleteFixedAssetCommand;
import com.everhomes.rest.fixedasset.FixedAssetAddFrom;
import com.everhomes.rest.fixedasset.FixedAssetCategoryDTO;
import com.everhomes.rest.fixedasset.FixedAssetCategoryStatus;
import com.everhomes.rest.fixedasset.FixedAssetDTO;
import com.everhomes.rest.fixedasset.FixedAssetErrorCode;
import com.everhomes.rest.fixedasset.FixedAssetOperationDTO;
import com.everhomes.rest.fixedasset.FixedAssetOperationItemDTO;
import com.everhomes.rest.fixedasset.FixedAssetStatisticsDTO;
import com.everhomes.rest.fixedasset.FixedAssetStatus;
import com.everhomes.rest.fixedasset.GetFixedAssetCommand;
import com.everhomes.rest.fixedasset.GetFixedAssetDictionaryResponse;
import com.everhomes.rest.fixedasset.GetFixedAssetOperationLogsCommand;
import com.everhomes.rest.fixedasset.GetFixedAssetOperationLogsResponse;
import com.everhomes.rest.fixedasset.GetImportFixedAssetResultCommand;
import com.everhomes.rest.fixedasset.ImportFixedAssetsCommand;
import com.everhomes.rest.fixedasset.ListFixedAssetCategoryCommand;
import com.everhomes.rest.fixedasset.ListFixedAssetCommand;
import com.everhomes.rest.fixedasset.ListFixedAssetResponse;
import com.everhomes.rest.fixedasset.UpdateFixedAssetCategoryCommand;
import com.everhomes.rest.organization.FilterOrganizationContactScopeType;
import com.everhomes.rest.organization.ImportFileErrorType;
import com.everhomes.rest.organization.ImportFileResultLog;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.organization.ImportFileTaskType;
import com.everhomes.rest.organization.ListAllTreeOrganizationsCommand;
import com.everhomes.rest.organization.ListOrganizationContactCommand;
import com.everhomes.rest.organization.ListOrganizationMemberCommandResponse;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.organization.OrganizationTreeDTO;
import com.everhomes.rest.organization.VisibleFlag;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.DateUtil;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FixedAssetServiceImpl implements FixedAssetService {
    private final static Logger LOGGER = LoggerFactory.getLogger(FixedAssetServiceImpl.class);

    // 限制分类类型树结构的最大层级，防止因为错误的父级关联形成完整闭环造成死循环
    private final static int FIXED_ASSET_CATEGORY_LEVEL_MAX_SIZE = 6;

    @Autowired
    private FixedAssetProvider fixedAssetProvider;
    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private LocaleStringService localeStringService;
    @Autowired
    private OrganizationProvider organizationProvider;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private ContentServerService contentServerService;
    @Autowired
    private ConfigurationProvider configurationProvider;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ImportFileService importFileService;
    @Autowired
    private FixedAssetUtils fixedAssetUtils;
    @Autowired
    private CoordinationProvider coordinationProvider;

    @Override
    public GetFixedAssetDictionaryResponse listFixedAssetDictionaries() {
        GetFixedAssetDictionaryResponse response = new GetFixedAssetDictionaryResponse();

        FixedAssetAddFrom[] addFromValues = FixedAssetAddFrom.values();
        List<ByteStringMapDTO> addFromList = new ArrayList<>(addFromValues.length);
        for (FixedAssetAddFrom addFrom : addFromValues) {
            if (FixedAssetAddFrom.OTHERS == addFrom) {
                // 把"其它"选项放在最后
                continue;
            }
            addFromList.add(new ByteStringMapDTO(addFrom.getCode(), addFrom.getName()));
        }
        // 把"其它"选项放在最后
        addFromList.add(new ByteStringMapDTO(FixedAssetAddFrom.OTHERS.getCode(), FixedAssetAddFrom.OTHERS.getName()));
        response.setAddFromList(addFromList);

        FixedAssetStatus[] statusValues = FixedAssetStatus.values();
        List<ByteStringMapDTO> statusList = new ArrayList<>(statusValues.length);
        for (FixedAssetStatus status : statusValues) {
            statusList.add(new ByteStringMapDTO(status.getCode(), status.getName()));
        }
        response.setStatusList(statusList);

        return response;
    }

    @Override
    public Integer createFixedAssetCategory(CreateFixedAssetCategoryCommand cmd) {
        Integer parentId = cmd.getParentId() == null ? 0 : cmd.getParentId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        String ownerType = StringUtils.hasText(cmd.getOwnerType()) ? cmd.getOwnerType() : EntityType.ORGANIZATIONS.getCode();
        CheckFixedAssetCategoryNameExistRequest checkRequest = new CheckFixedAssetCategoryNameExistRequest(namespaceId, ownerType, cmd.getOwnerId(), parentId, cmd.getCategoryName());
        checkCategoryNameIsDuplicated(checkRequest);

        FixedAssetCategory parent = checkCategoryParentId(parentId);

        Integer defaultOrder = this.fixedAssetProvider.getMaxDefaultOrderCurrentLevel(namespaceId, ownerType, cmd.getOwnerId(), parentId);
        FixedAssetCategory fixedAssetCategory = new FixedAssetCategory();
        fixedAssetCategory.setNamespaceId(namespaceId);
        fixedAssetCategory.setOwnerType(ownerType);
        fixedAssetCategory.setOwnerId(cmd.getOwnerId());
        fixedAssetCategory.setName(cmd.getCategoryName());
        fixedAssetCategory.setParentId(cmd.getParentId());
        fixedAssetCategory.setDefaultOrder(defaultOrder + 1);
        fixedAssetCategory.setStatus(FixedAssetCategoryStatus.VALID.getCode());
        fixedAssetCategory.setPath(parent == null ? "" : parent.getPath());
        return this.fixedAssetProvider.createFixedAssetCategory(fixedAssetCategory);
    }

    @Override
    public Integer updateFixedAssetCategory(UpdateFixedAssetCategoryCommand cmd) {
        FixedAssetCategory existFixedAssetCategory = this.fixedAssetProvider.getFixedAssetCategoryById(cmd.getCategoryId());
        if (existFixedAssetCategory == null) {
            return 0;
        }
        if (existFixedAssetCategory.getName().equals(cmd.getCategoryName())) {
            // 信息没有修改就不用操作数据库了
            return 0;
        }
        String ownerType = StringUtils.hasText(cmd.getOwnerType()) ? cmd.getOwnerType() : EntityType.ORGANIZATIONS.getCode();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        CheckFixedAssetCategoryNameExistRequest request = new CheckFixedAssetCategoryNameExistRequest(namespaceId, ownerType, cmd.getOwnerId(), existFixedAssetCategory.getParentId(), cmd.getCategoryName());
        request.setSelfCategoryId(existFixedAssetCategory.getId());
        checkCategoryNameIsDuplicated(request);

        existFixedAssetCategory.setName(cmd.getCategoryName());
        return this.fixedAssetProvider.updateFixedAssetCategory(existFixedAssetCategory);
    }

    private FixedAssetCategory checkCategoryParentId(Integer parentId) {
        if (parentId == null || parentId.equals(0)) {
            return null;
        }
        FixedAssetCategory category = this.fixedAssetProvider.getFixedAssetCategoryById(parentId);
        if (category == null) {
            throw RuntimeErrorException
                    .errorWith(
                            FixedAssetErrorCode.SCOPE,
                            FixedAssetErrorCode.FIXED_ASSET_PARENT_CATEGORY_NOT_EXIST_ERROR,
                            localeStringService.getLocalizedString(
                                    String.valueOf(FixedAssetErrorCode.SCOPE),
                                    String.valueOf(FixedAssetErrorCode.FIXED_ASSET_PARENT_CATEGORY_NOT_EXIST_ERROR),
                                    UserContext.current().getUser().getLocale(),
                                    "The parent classification does not exist"));
        }
        return category;
    }

    private void checkCategoryNameIsDuplicated(CheckFixedAssetCategoryNameExistRequest request) {
        boolean isCategoryNameExistInSameLevel = this.fixedAssetProvider.isCategoryNameExistInSameLevel(request);
        if (isCategoryNameExistInSameLevel) {
            throw RuntimeErrorException
                    .errorWith(
                            FixedAssetErrorCode.SCOPE,
                            FixedAssetErrorCode.FIXED_ASSET_CATEGORY_NAME_DUPLICATE_ERROR,
                            localeStringService.getLocalizedString(
                                    String.valueOf(FixedAssetErrorCode.SCOPE),
                                    String.valueOf(FixedAssetErrorCode.FIXED_ASSET_CATEGORY_NAME_DUPLICATE_ERROR),
                                    UserContext.current().getUser().getLocale(),
                                    "Category names can not be repeated"));
        }
    }

    @Override
    public void deleteFixedAssetCategory(DeleteFixedAssetCategoryCommand cmd) {
        FixedAssetCategory existFixedAssetCategory = this.fixedAssetProvider.getFixedAssetCategoryById(cmd.getCategoryId());
        if (existFixedAssetCategory == null) {
            return;
        }
        List<FixedAssetCategory> waitDeleteList = new ArrayList<>();
        waitDeleteList.add(existFixedAssetCategory);
        waitDeleteList.addAll(this.fixedAssetProvider.getAllSubFixedAssetCategories(existFixedAssetCategory.getPath()));

        List<Integer> includeSubIds = new ArrayList<>(waitDeleteList.size());
        for (FixedAssetCategory fixedAssetCategory : waitDeleteList) {
            includeSubIds.add(fixedAssetCategory.getId());
        }

        String ownerType = StringUtils.hasText(cmd.getOwnerType()) ? cmd.getOwnerType() : EntityType.ORGANIZATIONS.getCode();
        checkNoAssetsInTheseCategoires(ownerType, cmd.getOwnerId(), includeSubIds);

        this.dbProvider.execute((transactionStatus -> {
            for (FixedAssetCategory fixedAssetCategory : waitDeleteList) {
                fixedAssetCategory.setStatus(FixedAssetCategoryStatus.INVALID.getCode());
                this.fixedAssetProvider.updateFixedAssetCategory(fixedAssetCategory);
            }
            return null;
        }));
    }

    private void checkNoAssetsInTheseCategoires(String ownerType, Long ownerId, List<Integer> categoryIds) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<FixedAsset> results = this.fixedAssetProvider.findFixedAssets(new ListingLocator(), 1, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_FIXED_ASSETS.NAMESPACE_ID.eq(namespaceId));
                query.addConditions(Tables.EH_FIXED_ASSETS.OWNER_TYPE.eq(ownerType));
                query.addConditions(Tables.EH_FIXED_ASSETS.OWNER_ID.eq(ownerId));
                query.addConditions(Tables.EH_FIXED_ASSETS.FIXED_ASSET_CATEGORY_ID.in(categoryIds));
                return query;
            }
        });

        if (!CollectionUtils.isEmpty(results)) {
            throw RuntimeErrorException
                    .errorWith(
                            FixedAssetErrorCode.SCOPE,
                            FixedAssetErrorCode.FIXED_ASSET_CATEGORY_HAVE_HAS_ITEMS_ERROR,
                            localeStringService.getLocalizedString(
                                    String.valueOf(FixedAssetErrorCode.SCOPE),
                                    String.valueOf(FixedAssetErrorCode.FIXED_ASSET_CATEGORY_HAVE_HAS_ITEMS_ERROR),
                                    UserContext.current().getUser().getLocale(),
                                    "There are assets in this category"));
        }
    }

    @Override
    public List<FixedAssetCategoryDTO> findFixedAssetCategories(ListFixedAssetCategoryCommand cmd) {
        String ownerType = StringUtils.hasText(cmd.getOwnerType()) ? cmd.getOwnerType() : EntityType.ORGANIZATIONS.getCode();
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        boolean shouldCopyFromDefault = this.fixedAssetProvider.countFixedAssetCategoriesIgnoreStatus(namespaceId, ownerType, cmd.getOwnerId()) == 0;
        if (shouldCopyFromDefault) {
            this.coordinationProvider.getNamedLock(CoordinationLocks.FIXED_ASSET_CATEGORY_COPY_DEFAULT.getCode() + namespaceId).enter(() -> {
                // 获得锁以后再次判断是否需要复制默认分类
                boolean shouldCopyFromDefault2 = this.fixedAssetProvider.countFixedAssetCategoriesIgnoreStatus(namespaceId, ownerType, cmd.getOwnerId()) == 0;
                if (shouldCopyFromDefault2) {
                    this.copyFromDefaultCategories(ownerType, cmd.getOwnerId());
                }
                return null;
            });
        }

        List<FixedAssetCategory> list = this.fixedAssetProvider.findFixedAssetCategories(namespaceId, ownerType, cmd.getOwnerId());
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        return buildFixedAssetCategoryTreeData(list);
    }

    private List<FixedAssetCategoryDTO> buildFixedAssetCategoryTreeData(List<FixedAssetCategory> list) {
        Map<Integer, List<FixedAssetCategory>> parentIdAndCategoryMaps = this.fixedAssetCategoriesGroupByParentId(list);
        List<FixedAssetCategory> topCategories = parentIdAndCategoryMaps.get(Integer.valueOf(0));
        List<FixedAssetCategoryDTO> results = new ArrayList<>(topCategories.size());
        for (FixedAssetCategory top : topCategories) {
            int level = 0;
            FixedAssetCategoryDTO topDTO = new FixedAssetCategoryDTO();
            topDTO.setId(top.getId());
            topDTO.setName(top.getName());
            topDTO.setSubCategories(new ArrayList<>());
            results.add(topDTO);

            if (parentIdAndCategoryMaps.containsKey(top.getId())) {
                topDTO.setSubCategories(buildFixedAssetCategoryTreeData(top.getId(), parentIdAndCategoryMaps, ++level));
            }
        }
        return results;
    }

    private List<FixedAssetCategoryDTO> buildFixedAssetCategoryTreeData(Integer parentId, Map<Integer, List<FixedAssetCategory>> parentIdAndCategoryMaps, int level) {
        if (level > FIXED_ASSET_CATEGORY_LEVEL_MAX_SIZE) {
            LOGGER.info("Fixed Asset Category Level exceeding limit, parentId = {}", parentId);
            return Collections.emptyList();
        }
        List<FixedAssetCategory> parents = parentIdAndCategoryMaps.get(parentId);
        List<FixedAssetCategoryDTO> results = new ArrayList<>(parents.size());
        for (FixedAssetCategory parent : parents) {
            FixedAssetCategoryDTO parentDTO = new FixedAssetCategoryDTO();
            parentDTO.setId(parent.getId());
            parentDTO.setName(parent.getName());
            parentDTO.setSubCategories(new ArrayList<>());
            results.add(parentDTO);

            if (parentIdAndCategoryMaps.containsKey(parent.getId())) {
                parentDTO.setSubCategories(buildFixedAssetCategoryTreeData(parent.getId(), parentIdAndCategoryMaps, ++level));
            }
        }
        return results;
    }

    private Map<Integer, List<FixedAssetCategory>> fixedAssetCategoriesGroupByParentId(List<FixedAssetCategory> list) {
        Map<Integer, List<FixedAssetCategory>> parentIdAndCategoryMaps = new HashMap<>();
        for (FixedAssetCategory fixedAssetCategory : list) {
            if (parentIdAndCategoryMaps.containsKey(fixedAssetCategory.getParentId())) {
                List<FixedAssetCategory> categories = parentIdAndCategoryMaps.get(fixedAssetCategory.getParentId());
                categories.add(fixedAssetCategory);
            } else {
                List<FixedAssetCategory> categories = new ArrayList<>();
                categories.add(fixedAssetCategory);
                parentIdAndCategoryMaps.put(fixedAssetCategory.getParentId(), categories);
            }
        }
        return parentIdAndCategoryMaps;
    }

    private void copyFromDefaultCategories(String ownerType, Long ownerId) {
        List<FixedAssetDefaultCategory> defaultCategories = this.fixedAssetProvider.listAllFixedAssetDefaultCategories();
        if (CollectionUtils.isEmpty(defaultCategories)) {
            return;
        }
        Map<Integer, List<FixedAssetDefaultCategory>> parentIdAndCategoryMaps = fixedAssetDefaultCategoriesGroupByParentId(defaultCategories);
        copyFromDefaultCategories(ownerType, ownerId, 0, 0, parentIdAndCategoryMaps, 0);
    }

    private void copyFromDefaultCategories(String ownerType, Long ownerId, Integer parentId, Integer newParentId, Map<Integer, List<FixedAssetDefaultCategory>> parentIdAndCategoryMaps, int loopIndex) {
        if (loopIndex > FIXED_ASSET_CATEGORY_LEVEL_MAX_SIZE) {
            LOGGER.info("Fixed Asset Default Category Level exceeding limit, parentId = {}", parentId);
            return;
        }
        List<FixedAssetDefaultCategory> children = parentIdAndCategoryMaps.get(parentId);
        CreateFixedAssetCategoryCommand createFixedAssetCategoryCommand = new CreateFixedAssetCategoryCommand();
        createFixedAssetCategoryCommand.setOwnerType(ownerType);
        createFixedAssetCategoryCommand.setOwnerId(ownerId);
        createFixedAssetCategoryCommand.setParentId(newParentId);
        for (FixedAssetDefaultCategory child : children) {
            createFixedAssetCategoryCommand.setCategoryName(child.getName());
            try {
                Integer id = this.createFixedAssetCategory(createFixedAssetCategoryCommand);
                if (parentIdAndCategoryMaps.containsKey(child.getId())) {
                    copyFromDefaultCategories(ownerType, ownerId, child.getId(), id, parentIdAndCategoryMaps, ++loopIndex);
                }
            } catch (Exception e) {
                LOGGER.error("copyFromDefaultCategories error,name = {}", child.getName());
            }
        }
    }

    private Map<Integer, List<FixedAssetDefaultCategory>> fixedAssetDefaultCategoriesGroupByParentId(List<FixedAssetDefaultCategory> list) {
        Map<Integer, List<FixedAssetDefaultCategory>> parentIdAndCategoryMaps = new HashMap<>();
        for (FixedAssetDefaultCategory fixedAssetCategory : list) {
            if (parentIdAndCategoryMaps.containsKey(fixedAssetCategory.getParentId())) {
                List<FixedAssetDefaultCategory> categories = parentIdAndCategoryMaps.get(fixedAssetCategory.getParentId());
                categories.add(fixedAssetCategory);
            } else {
                List<FixedAssetDefaultCategory> categories = new ArrayList<>();
                categories.add(fixedAssetCategory);
                parentIdAndCategoryMaps.put(fixedAssetCategory.getParentId(), categories);
            }
        }
        return parentIdAndCategoryMaps;
    }

    @Override
    public Long createOrUpdateFixedAsset(CreateOrUpdateFixedAssetCommand cmd) {
        this.fixedAssetUtils.checkCreateOrUpdateFixedAssetCommand(cmd);
        String ownerType = StringUtils.hasText(cmd.getOwnerType()) ? cmd.getOwnerType() : EntityType.ORGANIZATIONS.getCode();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        String operatorName = getUserContactNameByUserId(UserContext.currentUserId());
        Date buyDate = null;
        if (StringUtils.hasText(cmd.getBuyDate())) {
            buyDate = Date.valueOf(cmd.getBuyDate().trim());
        }
        Date occupyDate = null;
        if (StringUtils.hasText(cmd.getOccupiedDate())) {
            occupyDate = Date.valueOf(cmd.getOccupiedDate().trim());
        }
        Long occupyMemberDetailId = null;
        String occupyMemberName = null;
        Long occupyDeptId = null;
        if (cmd.getOccupiedMemberDetailId() != null) {
            Organization organization = this.getCurrentOrganizationByDetailId(cmd.getOccupiedMemberDetailId());
            occupyMemberDetailId = cmd.getOccupiedMemberDetailId();
            occupyDeptId = organization == null ? null : organization.getId();
            occupyMemberName = this.getUserContactNameByMemberDetailId(cmd.getOccupiedMemberDetailId());
        } else if (cmd.getOccupiedDepartmentId() != null) {
            Organization organization = organizationProvider.findOrganizationById(cmd.getOccupiedDepartmentId());
            occupyDeptId = organization == null ? null : organization.getId();
        }

        CheckFixedAssetItemNoExistRequest checkFixedAssetItemNoExistRequest = new CheckFixedAssetItemNoExistRequest(namespaceId, ownerType, cmd.getOwnerId(), cmd.getId(), cmd.getItemNo().trim());
        checkFixedAssetItemNumIsDuplicated(checkFixedAssetItemNoExistRequest);

        if (cmd.getId() == null) {
            FixedAsset createFixedAsset = ConvertHelper.convert(cmd, FixedAsset.class);
            createFixedAsset.setOwnerType(ownerType);
            createFixedAsset.setNamespaceId(namespaceId);
            createFixedAsset.setBuyDate(buyDate);
            createFixedAsset.setOccupiedDate(occupyDate);
            createFixedAsset.setOccupiedMemberDetailId(occupyMemberDetailId);
            createFixedAsset.setOccupiedMemberName(occupyMemberName);
            createFixedAsset.setOccupiedDepartmentId(occupyDeptId);
            createFixedAsset.setPrice(cmd.getPrice() != null ? cmd.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP) : null);
            createFixedAsset.setOperatorName(operatorName);
            return this.dbProvider.execute(transactionStatus -> {
                this.fixedAssetProvider.createFixedAsset(createFixedAsset);
                this.fixedAssetProvider.createFixedAssetOperationLog(this.buildOperationLog(createFixedAsset.getId(), null, createFixedAsset, FixedAssetOperationType.CREATE));
                return createFixedAsset.getId();
            });

        }

        FixedAsset existFixedAsset = this.fixedAssetProvider.getFixedAssetDetail(cmd.getId(), namespaceId, ownerType, cmd.getOwnerId());
        if (existFixedAsset == null) {
            return 0L;
        }
        existFixedAsset.setName(cmd.getName());
        existFixedAsset.setItemNo(cmd.getItemNo());
        existFixedAsset.setSpecification(cmd.getSpecification());
        existFixedAsset.setStatus(cmd.getStatus());
        existFixedAsset.setAddFrom(cmd.getAddFrom());
        existFixedAsset.setBarcodeUri(cmd.getBarcodeUri());
        existFixedAsset.setImageUri(cmd.getImageUri());
        existFixedAsset.setBuyDate(buyDate);
        existFixedAsset.setPrice(cmd.getPrice() != null ? cmd.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP) : null);
        existFixedAsset.setVendor(cmd.getVendor());
        existFixedAsset.setOtherInfo(cmd.getOtherInfo());
        existFixedAsset.setRemark(cmd.getRemark());
        existFixedAsset.setFixedAssetCategoryId(cmd.getFixedAssetCategoryId());
        existFixedAsset.setLocation(cmd.getLocation());
        existFixedAsset.setOccupiedDate(occupyDate);
        existFixedAsset.setOccupiedDepartmentId(occupyDeptId);
        existFixedAsset.setOccupiedMemberDetailId(occupyMemberDetailId);
        existFixedAsset.setOccupiedMemberName(occupyMemberName);
        existFixedAsset.setOperatorName(operatorName);
        return this.dbProvider.execute(transactionStatus -> {
            FixedAsset beforeUpdate = this.fixedAssetProvider.getFixedAssetDetail(cmd.getId(), namespaceId, ownerType, cmd.getOwnerId());
            this.fixedAssetProvider.updateFixedAsset(existFixedAsset);
            this.fixedAssetProvider.createFixedAssetOperationLog(this.buildOperationLog(existFixedAsset.getId(), beforeUpdate, existFixedAsset, FixedAssetOperationType.UPDATE));
            return existFixedAsset.getId();
        });
    }

    private void checkFixedAssetItemNumIsDuplicated(CheckFixedAssetItemNoExistRequest request) {
        boolean isItemNumExist = this.fixedAssetProvider.isFixedAssetItemNoExist(request);
        if (isItemNumExist) {
            throw RuntimeErrorException
                    .errorWith(
                            FixedAssetErrorCode.SCOPE,
                            FixedAssetErrorCode.FIXED_ASSET_ITEM_NUM_DUPLICATE_ERROR,
                            localeStringService.getLocalizedString(
                                    String.valueOf(FixedAssetErrorCode.SCOPE),
                                    String.valueOf(FixedAssetErrorCode.FIXED_ASSET_ITEM_NUM_DUPLICATE_ERROR),
                                    UserContext.current().getUser().getLocale(),
                                    "Asset item num can not be repeated"));
        }
    }

    @Override
    public FixedAssetDTO getFixedAssetDetail(GetFixedAssetCommand cmd) {
        String ownerType = StringUtils.hasText(cmd.getOwnerType()) ? cmd.getOwnerType() : EntityType.ORGANIZATIONS.getCode();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        FixedAsset fixedAsset = this.fixedAssetProvider.getFixedAssetDetail(cmd.getFixedAssetId(), namespaceId, ownerType, cmd.getOwnerId());
        if (fixedAsset != null) {
            return convertFixedAssetDTO(fixedAsset);
        }
        return null;
    }

    private FixedAssetDTO convertFixedAssetDTO(FixedAsset fixedAsset) {
        FixedAssetDTO fixedAssetDTO = ConvertHelper.convert(fixedAsset, FixedAssetDTO.class);
        if (fixedAsset.getBuyDate() != null) {
            fixedAssetDTO.setBuyDate(DateUtil.dateToStr(fixedAsset.getBuyDate(), DateUtil.YMR_SLASH));
        }
        if (fixedAsset.getOccupiedDate() != null) {
            fixedAssetDTO.setOccupiedDate(DateUtil.dateToStr(fixedAsset.getOccupiedDate(), DateUtil.YMR_SLASH));
        }
        if (StringUtils.hasText(fixedAsset.getBarcodeUri())) {
            fixedAssetDTO.setBarcodeUrl(getResourceUrlByUir(fixedAsset.getBarcodeUri(), fixedAsset.getOwnerType(), fixedAsset.getOwnerId()));
        }
        if (StringUtils.hasText(fixedAsset.getImageUri())) {
            fixedAssetDTO.setImageUrl(getResourceUrlByUir(fixedAsset.getImageUri(), fixedAsset.getOwnerType(), fixedAsset.getOwnerId()));
        }
        fixedAssetDTO.setStatusDisplayName(FixedAssetStatus.fromCode(fixedAsset.getStatus()).getName());
        fixedAssetDTO.setAddFromDisplayName(FixedAssetAddFrom.fromCode(fixedAsset.getAddFrom()).getName());
        if (fixedAsset.getOccupiedDepartmentId() != null) {
            Organization organization = this.organizationProvider.findOrganizationById(fixedAsset.getOccupiedDepartmentId());
            if (organization != null) {
                fixedAssetDTO.setOccupied_department_name(organization.getName());
            }
        }
        if (fixedAsset.getOccupiedMemberDetailId() != null) {
            fixedAssetDTO.setOccupied_member_name(this.getUserContactNameByMemberDetailId(fixedAsset.getOccupiedMemberDetailId()));
        }
        FixedAssetCategory fixedAssetCategory = this.fixedAssetProvider.getFixedAssetCategoryById(fixedAsset.getFixedAssetCategoryId());
        if (fixedAssetCategory != null) {
            fixedAssetDTO.setFixedAssetCategoryName(fixedAssetCategory.getName());
        }
        return fixedAssetDTO;
    }

    @Override
    public void deleteFixedAsset(DeleteFixedAssetCommand cmd) {
        String ownerType = StringUtils.hasText(cmd.getOwnerType()) ? cmd.getOwnerType() : EntityType.ORGANIZATIONS.getCode();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        FixedAsset fixedAsset = this.fixedAssetProvider.getFixedAssetDetail(cmd.getFixedAssetId(), namespaceId, ownerType, cmd.getOwnerId());
        if (fixedAsset != null) {
            fixedAsset.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            fixedAsset.setDeleteUid(UserContext.currentUserId());
            fixedAsset.setOperatorName(this.getUserContactNameByUserId(UserContext.currentUserId()));
            this.dbProvider.execute(transactionStatus -> {
                this.fixedAssetProvider.updateFixedAsset(fixedAsset);
                this.fixedAssetProvider.createFixedAssetOperationLog(this.buildOperationLog(fixedAsset.getId(), null, null, FixedAssetOperationType.DELETE));
                return null;
            });
        }
    }

    @Override
    public Integer batchUpdateFixedAsset(BatchUpdateFixedAssetCommand cmd) {
        this.fixedAssetUtils.checkBatchUpdateFixedAssetCommand(cmd);
        String ownerType = StringUtils.hasText(cmd.getOwnerType()) ? cmd.getOwnerType() : EntityType.ORGANIZATIONS.getCode();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<FixedAsset> fixedAssets = listFixedAssetsByIds(namespaceId, ownerType, cmd.getOwnerId(), cmd.getIds());

        if (CollectionUtils.isEmpty(fixedAssets)) {
            return 0;
        }

        this.dbProvider.execute((TransactionStatus status) -> {
            String operatorName = getUserContactNameByUserId(UserContext.currentUserId());
            Date buyDate = null;
            if (StringUtils.hasText(cmd.getBuyDate())) {
                buyDate = Date.valueOf(cmd.getBuyDate().trim());
            }
            Date occupyDate = null;
            if (StringUtils.hasText(cmd.getOccupiedDate())) {
                occupyDate = Date.valueOf(cmd.getOccupiedDate().trim());
            }
            Long occupyMemberDetailId = null;
            String occupyMemberName = null;
            Long occupyDeptId = null;
            if (cmd.getOccupiedMemberDetailId() != null) {
                Organization organization = this.getCurrentOrganizationByDetailId(cmd.getOccupiedMemberDetailId());
                occupyMemberDetailId = cmd.getOccupiedMemberDetailId();
                occupyDeptId = organization == null ? null : organization.getId();
                occupyMemberName = this.getUserContactNameByMemberDetailId(cmd.getOccupiedMemberDetailId());
            } else if (cmd.getOccupiedDepartmentId() != null) {
                Organization organization = organizationProvider.findOrganizationById(cmd.getOccupiedDepartmentId());
                occupyDeptId = organization == null ? null : organization.getId();
            }

            for (FixedAsset fixedAsset : fixedAssets) {
                fixedAsset.setName(cmd.getName());
                fixedAsset.setStatus(cmd.getStatus());
                fixedAsset.setAddFrom(cmd.getAddFrom());
                fixedAsset.setBuyDate(buyDate);
                fixedAsset.setPrice(cmd.getPrice() != null ? cmd.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP) : null);
                fixedAsset.setVendor(cmd.getVendor());
                fixedAsset.setOtherInfo(cmd.getOtherInfo());
                fixedAsset.setRemark(cmd.getRemark());
                fixedAsset.setFixedAssetCategoryId(cmd.getFixedAssetCategoryId());
                fixedAsset.setSpecification(cmd.getSpecification());
                fixedAsset.setLocation(cmd.getLocation());
                fixedAsset.setOccupiedDate(occupyDate);
                fixedAsset.setOccupiedDepartmentId(occupyDeptId);
                fixedAsset.setOccupiedMemberDetailId(occupyMemberDetailId);
                fixedAsset.setOccupiedMemberName(occupyMemberName);
                fixedAsset.setOperatorName(operatorName);
                FixedAsset beforeUpdate = this.fixedAssetProvider.getFixedAssetDetail(fixedAsset.getId(), namespaceId, ownerType, cmd.getOwnerId());
                this.fixedAssetProvider.updateFixedAsset(fixedAsset);
                this.fixedAssetProvider.createFixedAssetOperationLog(this.buildOperationLog(fixedAsset.getId(), beforeUpdate, fixedAsset, FixedAssetOperationType.UPDATE));
            }
            return null;
        });
        return fixedAssets.size();
    }

    private List<FixedAsset> listFixedAssetsByIds(Integer namespaceId, String ownerType, Long ownerId, List<Long> ids) {
        return this.fixedAssetProvider.findFixedAssets(new ListingLocator(), Integer.MAX_VALUE - 1, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_FIXED_ASSETS.NAMESPACE_ID.eq(namespaceId));
                query.addConditions(Tables.EH_FIXED_ASSETS.OWNER_TYPE.eq(ownerType));
                query.addConditions(Tables.EH_FIXED_ASSETS.OWNER_ID.eq(ownerId));
                query.addConditions(Tables.EH_FIXED_ASSETS.ID.in(ids));
                return query;
            }
        });
    }

    @Override
    public Integer batchDeleteFixedAsset(BatchDeleteFixedAssetCommand cmd) {
        String ownerType = StringUtils.hasText(cmd.getOwnerType()) ? cmd.getOwnerType() : EntityType.ORGANIZATIONS.getCode();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<FixedAsset> fixedAssets = listFixedAssetsByIds(namespaceId, ownerType, cmd.getOwnerId(), cmd.getFixedAssetIds());

        if (CollectionUtils.isEmpty(fixedAssets)) {
            return 0;
        }
        this.dbProvider.execute((TransactionStatus status) -> {
            Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
            String operatorName = getUserContactNameByUserId(UserContext.currentUserId());
            for (FixedAsset fixedAsset : fixedAssets) {
                fixedAsset.setDeleteUid(UserContext.currentUserId());
                fixedAsset.setDeleteTime(now);
                fixedAsset.setOperatorName(operatorName);
                this.fixedAssetProvider.updateFixedAsset(fixedAsset);
                this.fixedAssetProvider.createFixedAssetOperationLog(this.buildOperationLog(fixedAsset.getId(), null, null, FixedAssetOperationType.DELETE));
            }
            return null;
        });
        return fixedAssets.size();
    }

    @Override
    public ListFixedAssetResponse listFixedAssets(ListFixedAssetCommand cmd) {
        String ownerType = StringUtils.hasText(cmd.getOwnerType()) ? cmd.getOwnerType() : EntityType.ORGANIZATIONS.getCode();
        Integer namespaceId = cmd.getNamespaceId() != null ? cmd.getNamespaceId() : UserContext.getCurrentNamespaceId();
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        List<Integer> categoryIdsIncludeSubId = this.getCategoryIdsIncludeSubId(cmd.getFixedAssetCategoryId());
        List<FixedAsset> results = this.fixedAssetProvider.findFixedAssets(locator, pageSize, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_FIXED_ASSETS.NAMESPACE_ID.eq(namespaceId));
                query.addConditions(Tables.EH_FIXED_ASSETS.OWNER_TYPE.eq(ownerType));
                query.addConditions(Tables.EH_FIXED_ASSETS.OWNER_ID.eq(cmd.getOwnerId()));
                if (!CollectionUtils.isEmpty(categoryIdsIncludeSubId)) {
                    query.addConditions(Tables.EH_FIXED_ASSETS.FIXED_ASSET_CATEGORY_ID.in(categoryIdsIncludeSubId));
                }
                if (!CollectionUtils.isEmpty(cmd.getStatusList())) {
                    query.addConditions(Tables.EH_FIXED_ASSETS.STATUS.in(cmd.getStatusList()));
                }
                if (StringUtils.hasText(cmd.getKeyWord())) {
                    query.addConditions(Tables.EH_FIXED_ASSETS.ITEM_NO.like("%" + cmd.getKeyWord() + "%").or(Tables.EH_FIXED_ASSETS.NAME.like("%" + cmd.getKeyWord() + "%")));
                }
                if (StringUtils.hasText(cmd.getBuyDateStart())) {
                    query.addConditions(Tables.EH_FIXED_ASSETS.BUY_DATE.ge(Date.valueOf(cmd.getBuyDateStart())));
                }
                if (StringUtils.hasText(cmd.getBuyDateEnd())) {
                    query.addConditions(Tables.EH_FIXED_ASSETS.BUY_DATE.le(Date.valueOf(cmd.getBuyDateEnd())));
                }
                if (StringUtils.hasText(cmd.getOccupyDateStart())) {
                    query.addConditions(Tables.EH_FIXED_ASSETS.OCCUPIED_DATE.ge(Date.valueOf(cmd.getOccupyDateStart())));
                }
                if (StringUtils.hasText(cmd.getOccupyDateEnd())) {
                    query.addConditions(Tables.EH_FIXED_ASSETS.OCCUPIED_DATE.le(Date.valueOf(cmd.getOccupyDateEnd())));
                }
                if (!CollectionUtils.isEmpty(cmd.getOccupiedDepartmentIds())) {
                    query.addConditions(Tables.EH_FIXED_ASSETS.OCCUPIED_DEPARTMENT_ID.in(cmd.getOccupiedDepartmentIds()));
                }
                if (!CollectionUtils.isEmpty(cmd.getOccupiedMemberDetailIds())) {
                    query.addConditions(Tables.EH_FIXED_ASSETS.OCCUPIED_MEMBER_DETAIL_ID.in(cmd.getOccupiedMemberDetailIds()));
                }
                return query;
            }
        });

        ListFixedAssetResponse response = new ListFixedAssetResponse();

        if (CollectionUtils.isEmpty(results)) {
            return response;
        }
        List<FixedAssetDTO> dtos = new ArrayList<>(results.size());
        for (FixedAsset fixedAsset : results) {
            dtos.add(convertFixedAssetDTO(fixedAsset));
        }

        response.setDtos(dtos);
        response.setNextPageAnchor(locator.getAnchor());
        return response;
    }

    @Override
    public FixedAssetStatisticsDTO getFixedAssetsStatistic(ListFixedAssetCommand cmd) {
        String ownerType = StringUtils.hasText(cmd.getOwnerType()) ? cmd.getOwnerType() : EntityType.ORGANIZATIONS.getCode();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<Integer> categoryIdsIncludeSubId = this.getCategoryIdsIncludeSubId(cmd.getFixedAssetCategoryId());
        Condition condition = Tables.EH_FIXED_ASSETS.NAMESPACE_ID.eq(namespaceId);
        condition = condition.and(Tables.EH_FIXED_ASSETS.OWNER_TYPE.eq(ownerType));
        condition = condition.and(Tables.EH_FIXED_ASSETS.OWNER_ID.eq(cmd.getOwnerId()));
        if (!CollectionUtils.isEmpty(categoryIdsIncludeSubId)) {
            condition = condition.and(Tables.EH_FIXED_ASSETS.FIXED_ASSET_CATEGORY_ID.in(categoryIdsIncludeSubId));
        }
        if (!CollectionUtils.isEmpty(cmd.getStatusList())) {
            condition = condition.and(Tables.EH_FIXED_ASSETS.STATUS.in(cmd.getStatusList()));
        }
        if (StringUtils.hasText(cmd.getKeyWord())) {
            condition = condition.and(Tables.EH_FIXED_ASSETS.ITEM_NO.like("%" + cmd.getKeyWord() + "%").or(Tables.EH_FIXED_ASSETS.NAME.like("%" + cmd.getKeyWord() + "%")));
        }
        if (StringUtils.hasText(cmd.getBuyDateStart())) {
            condition = condition.and(Tables.EH_FIXED_ASSETS.BUY_DATE.ge(Date.valueOf(cmd.getBuyDateStart())));
        }
        if (StringUtils.hasText(cmd.getBuyDateEnd())) {
            condition = condition.and(Tables.EH_FIXED_ASSETS.BUY_DATE.le(Date.valueOf(cmd.getBuyDateEnd())));
        }
        if (StringUtils.hasText(cmd.getOccupyDateStart())) {
            condition = condition.and(Tables.EH_FIXED_ASSETS.OCCUPIED_DATE.ge(Date.valueOf(cmd.getOccupyDateStart())));
        }
        if (StringUtils.hasText(cmd.getOccupyDateEnd())) {
            condition = condition.and(Tables.EH_FIXED_ASSETS.OCCUPIED_DATE.le(Date.valueOf(cmd.getOccupyDateEnd())));
        }
        if (!CollectionUtils.isEmpty(cmd.getOccupiedDepartmentIds())) {
            condition = condition.and(Tables.EH_FIXED_ASSETS.OCCUPIED_DEPARTMENT_ID.in(cmd.getOccupiedDepartmentIds()));
        }
        if (!CollectionUtils.isEmpty(cmd.getOccupiedMemberDetailIds())) {
            condition = condition.and(Tables.EH_FIXED_ASSETS.OCCUPIED_MEMBER_DETAIL_ID.in(cmd.getOccupiedMemberDetailIds()));
        }
        return this.fixedAssetProvider.getFixedAssetsStatistic(condition);
    }

    @Override
    public GetFixedAssetOperationLogsResponse findFixedAssetOperationLogs(GetFixedAssetOperationLogsCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        List<FixedAssetOperationLog> results = this.fixedAssetProvider.getFixedAssetOperationLogs(locator, pageSize, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_FIXED_ASSET_OPERATION_LOGS.NAMESPACE_ID.eq(namespaceId));
                query.addConditions(Tables.EH_FIXED_ASSET_OPERATION_LOGS.FIXED_ASSET_ID.eq(cmd.getFixedAssetId()));
                return query;
            }
        });

        GetFixedAssetOperationLogsResponse response = new GetFixedAssetOperationLogsResponse();

        if (CollectionUtils.isEmpty(results)) {
            return response;
        }
        List<FixedAssetOperationDTO> dtos = new ArrayList<>(results.size());
        for (FixedAssetOperationLog operationLog : results) {
            FixedAssetOperationDTO fixedAssetOperationDTO = ConvertHelper.convert(operationLog, FixedAssetOperationDTO.class);
            fixedAssetOperationDTO.setChangeItems(JSON.parseArray(operationLog.getOperationInfo(), FixedAssetOperationItemDTO.class));
            dtos.add(fixedAssetOperationDTO);
        }

        response.setOperationLogs(dtos);
        response.setNextPageAnchor(locator.getAnchor());
        return response;
    }

    @Override
    public HttpServletResponse exportFixedAssets(ListFixedAssetCommand cmd, HttpServletResponse response) {
        Map<String, Object> params = new HashMap();
        cmd.setPageSize(Integer.MAX_VALUE - 1);
        cmd.setPageAnchor(null);
        params.put("listFixedAssetCommand", JSON.toJSONString(cmd));
        String fileName = String.format("资产列表_%s.xlsx", DateUtil.dateToStr(new java.util.Date(), DateUtil.NO_SLASH));

        taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), FixedAssetExportTaskHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new java.util.Date());
        return response;
    }

    @Override
    public ImportFileTaskDTO importFixedAssets(ImportFixedAssetsCommand cmd, MultipartFile file) {
        String ownerType = StringUtils.hasText(cmd.getOwnerType()) ? cmd.getOwnerType() : EntityType.ORGANIZATIONS.getCode();
        cmd.setOwnerType(ownerType);
        ListFixedAssetCategoryCommand listFixedAssetCategoryCommand = new ListFixedAssetCategoryCommand(cmd.getOwnerType(), cmd.getOwnerId());
        final List<FixedAssetCategoryDTO> parentCategories = this.findFixedAssetCategories(listFixedAssetCategoryCommand);

        checkCategoryIsEmpty(parentCategories);

        ImportFileTask task = new ImportFileTask();
        List resultList = processorExcel(file);
        task.setOwnerType(FixedAssetErrorCode.FIXED_ASSET_OWNER_TYPE);
        task.setOwnerId(cmd.getOwnerId());
        task.setType(ImportFileTaskType.FIXED_ASSET.getCode());
        task.setCreatorUid(UserContext.currentUserId());

        //  调用导入方法
        importFileService.executeTask(new ExecuteImportTaskCallback() {
            @Override
            public ImportFileResponse importFile() {
                if (resultList.size() > 0) {
                    //  校验标题，若不合格直接返回错误
                    ImportFileResponse response = new ImportFileResponse();
                    String fileLog = "";
                    RowResult title = (RowResult) resultList.get(1);
                    Map<String, String> titleMap = title.getCells();
                    fileLog = checkImportFixedAssetsTitle(titleMap);
                    if (!StringUtils.isEmpty(fileLog)) {
                        response.setFileLog(fileLog);
                        return response;
                    }
                }

                ListAllTreeOrganizationsCommand listAllTreeOrganizationsCommand = new ListAllTreeOrganizationsCommand();
                listAllTreeOrganizationsCommand.setOrganizationId(cmd.getOwnerId());
                final OrganizationTreeDTO topOrganization = organizationService.listAllTreeOrganizations(listAllTreeOrganizationsCommand);

                return batchCreateOrUpdateFixedAssets(resultList, cmd, parentCategories, topOrganization);
            }

        }, task);
        return ConvertHelper.convert(task, ImportFileTaskDTO.class);
    }

    @Override
    public ImportFileResponse getImportFixedAssetsResult(GetImportFixedAssetResultCommand cmd) {
        return importFileService.getImportFileResult(cmd.getTaskId());
    }

    @Override
    public void exportImportFileFailResults(GetImportFixedAssetResultCommand cmd, HttpServletResponse httpResponse) {
        importFileService.exportImportFileFailResultXls(httpResponse, cmd.getTaskId());
    }

    private List processorExcel(MultipartFile file) {
        try {
            List resultList = PropMrgOwnerHandler.processorExcel(file.getInputStream());
            if (resultList.isEmpty()) {
                LOGGER.error("File content is empty");
                throw RuntimeErrorException.errorWith(FixedAssetErrorCode.SCOPE, FixedAssetErrorCode.FIXED_ASSET_IMPORT_EMPTY,
                        "File content is empty");
            }
            return resultList;
        } catch (IOException e) {
            LOGGER.error("file process excel error ", e);
            e.printStackTrace();
        }

        return null;
    }

    private String checkImportFixedAssetsTitle(Map<String, String> map) {
        List<String> titles = FixedAssetConstants.FixedAssetExcelShowColumn.getTitleList();
        //  存储字段来进行校验
        List<String> temp = new ArrayList<String>(map.values());

        for (int i = 0; i < titles.size(); i++) {
            if (titles.get(i).equals(temp.get(i).trim()))
                continue;
            else {
                return ImportFileErrorType.TITLE_ERROR.getCode();
            }
        }
        return null;
    }

    private ImportFileResponse batchCreateOrUpdateFixedAssets(List list, ImportFixedAssetsCommand importFixedAssetsCommand, List<FixedAssetCategoryDTO> topCategories, OrganizationTreeDTO topOrganization) {
        ImportFileResponse response = new ImportFileResponse();

        long count = 0;
        long cover = 0;
        List<ImportFileResultLog<Map<String, String>>> errorLogs = new ArrayList<>();
        RowResult title = (RowResult) list.get(1);
        Map<String, String> titleMap = title.getCells();
        response.setTitle(titleMap);
        response.setLogs(new ArrayList<>());
        for (int i = 2; i < list.size(); i++) {
            count++;
            RowResult r = (RowResult) list.get(i);
            ImportFileResultLog<Map<String, String>> log = new ImportFileResultLog<>(FixedAssetErrorCode.SCOPE);

            Map<String, String> data = new HashMap();
            for (Map.Entry<String, String> entry : titleMap.entrySet()) {
                data.put(entry.getKey(), (r.getCells().get(entry.getKey()) == null) ? "" : r.getCells().get(entry.getKey()));
            }
            log.setData(data);

            CreateOrUpdateFixedAssetCommand createOrUpdateFixedAssetCommand = this.checkDataAndBuildCreateCommand(r, data, importFixedAssetsCommand, topCategories, topOrganization, log);
            try {
                if (createOrUpdateFixedAssetCommand != null) {
                    FixedAsset fixedAsset = this.fixedAssetProvider.getFixedAssetDetailByItemNum(createOrUpdateFixedAssetCommand.getItemNo(), UserContext.getCurrentNamespaceId(), importFixedAssetsCommand.getOwnerType(), importFixedAssetsCommand.getOwnerId());
                    if (fixedAsset != null) {
                        createOrUpdateFixedAssetCommand.setId(fixedAsset.getId());
                        cover++;
                    }
                    this.createOrUpdateFixedAsset(createOrUpdateFixedAssetCommand);
                } else {
                    errorLogs.add(log);
                }
            } catch (Exception e) {
                LOGGER.error("保存时出错,资产编号={}", createOrUpdateFixedAssetCommand.getItemNo(), e);
                log.setData(data);
                log.setErrorLog("保存时出错：" + e.getMessage());
                log.setCode(FixedAssetErrorCode.FIXED_ASSET_SAVE_ERROR);
                log.setErrorDescription(log.getErrorLog());
                errorLogs.add(log);
            }
        }
        response.setTotalCount(count);
        response.setCoverCount(cover);
        response.setLogs(errorLogs);
        response.setFailCount((long) errorLogs.size());
        return response;
    }

    public String getUserContactNameByUserId(Long userId) {
        List<OrganizationMember> members = organizationProvider.listOrganizationMembersByUId(userId);
        if (members != null && members.size() > 0)
            return members.get(0).getContactName();
        return "";
    }

    private String getUserContactNameByMemberDetailId(Long detailId) {
        List<String> groupTypeList = new ArrayList<>();
        groupTypeList.add(OrganizationGroupType.ENTERPRISE.getCode());
        List<OrganizationMember> members = organizationProvider.listOrganizationMembersByDetailId(detailId, groupTypeList);
        if (members != null && members.size() > 0)
            return members.get(0).getContactName();
        return "";
    }

    private Organization getCurrentOrganizationByDetailId(Long memberDetailId) {
        Long currentOrganizationId = organizationService.getDepartmentByDetailId(memberDetailId);
        if (currentOrganizationId != null && currentOrganizationId > 0) {
            return organizationProvider.findOrganizationById(currentOrganizationId);
        }
        return null;
    }

    private String getResourceUrlByUir(String uri, String ownerType, Long ownerId) {
        String url = null;
        if (null != uri && uri.length() > 0) {
            try {
                url = contentServerService.parserUri(uri, ownerType, ownerId);
            } catch (Exception e) {
                LOGGER.error("Failed to parse uri, uri={}, ownerType={}, ownerId={}", uri, ownerType, ownerId, e);
            }
        }

        return url;
    }

    private List<Integer> getCategoryIdsIncludeSubId(Integer parentId) {
        if (parentId == null) {
            return Collections.emptyList();
        }
        List<FixedAssetCategory> results = new ArrayList<>();
        FixedAssetCategory parent = this.fixedAssetProvider.getFixedAssetCategoryById(parentId);
        if (parent != null) {
            results.add(parent);
            results.addAll(this.fixedAssetProvider.getAllSubFixedAssetCategories(parent.getPath()));
        }
        if (CollectionUtils.isEmpty(results)) {
            return Collections.emptyList();
        }
        List<Integer> ids = new ArrayList<>(results.size());
        for (FixedAssetCategory fixedAssetCategory : results) {
            ids.add(fixedAssetCategory.getId());
        }
        return ids;
    }

    private FixedAssetOperationLog buildOperationLog(Long fixedAssetId, FixedAsset beforeUpdate, FixedAsset afterUpdate, FixedAssetOperationType type) {
        FixedAssetOperationLog fixedAssetOperationLog = new FixedAssetOperationLog();
        fixedAssetOperationLog.setFixedAssetId(fixedAssetId);
        fixedAssetOperationLog.setNamespaceId(UserContext.getCurrentNamespaceId());
        fixedAssetOperationLog.setOperationType(type.getCode());
        fixedAssetOperationLog.setOperatorName(this.getUserContactNameByUserId(UserContext.currentUserId()));
        fixedAssetOperationLog.setOperationInfo(JSON.toJSONString(Collections.emptyList()));

        if (FixedAssetOperationType.DELETE == type) {
            return fixedAssetOperationLog;
        }

        FixedAssetDTO afterUpdateDTO = convertFixedAssetDTO(afterUpdate);
        List<FixedAssetOperationItemDTO> changeItems = new ArrayList<>();
        boolean isCreate = FixedAssetOperationType.CREATE == type;
        if (isCreate || valueChanged(beforeUpdate.getItemNo(), afterUpdate.getItemNo())) {
            changeItems.add(new FixedAssetOperationItemDTO(FixedAssetConstants.FixedAssetOperationLogShowColumn.编号.name(), afterUpdateDTO.getItemNo()));
        }
        if (isCreate || valueChanged(beforeUpdate.getFixedAssetCategoryId(), afterUpdate.getFixedAssetCategoryId())) {
            changeItems.add(new FixedAssetOperationItemDTO(FixedAssetConstants.FixedAssetOperationLogShowColumn.类型.name(), afterUpdateDTO.getFixedAssetCategoryName()));
        }
        if (isCreate || valueChanged(beforeUpdate.getName(), afterUpdate.getName())) {
            changeItems.add(new FixedAssetOperationItemDTO(FixedAssetConstants.FixedAssetOperationLogShowColumn.名称.name(), afterUpdateDTO.getName()));
        }
        if (isCreate || valueChanged(beforeUpdate.getSpecification(), afterUpdate.getSpecification())) {
            changeItems.add(new FixedAssetOperationItemDTO(FixedAssetConstants.FixedAssetOperationLogShowColumn.规格.name(), afterUpdateDTO.getSpecification()));
        }
        if (isCreate || valueChanged(beforeUpdate.getPrice(), afterUpdate.getPrice())) {
            changeItems.add(new FixedAssetOperationItemDTO(FixedAssetConstants.FixedAssetOperationLogShowColumn.单价.name(), afterUpdateDTO.getPrice() != null ? afterUpdateDTO.getPrice().toString() : null));
        }
        if (isCreate || valueChanged(beforeUpdate.getBuyDate(), afterUpdate.getBuyDate())) {
            changeItems.add(new FixedAssetOperationItemDTO(FixedAssetConstants.FixedAssetOperationLogShowColumn.购买时间.name(), afterUpdateDTO.getBuyDate()));
        }
        if (isCreate || valueChanged(beforeUpdate.getVendor(), afterUpdate.getVendor())) {
            changeItems.add(new FixedAssetOperationItemDTO(FixedAssetConstants.FixedAssetOperationLogShowColumn.所属供应商.name(), afterUpdateDTO.getVendor()));
        }
        if (isCreate || valueChanged(beforeUpdate.getAddFrom(), afterUpdate.getAddFrom())) {
            changeItems.add(new FixedAssetOperationItemDTO(FixedAssetConstants.FixedAssetOperationLogShowColumn.来源.name(), afterUpdateDTO.getAddFromDisplayName()));
        }
        if (isCreate || valueChanged(beforeUpdate.getImageUri(), afterUpdate.getImageUri())) {
            changeItems.add(new FixedAssetOperationItemDTO(FixedAssetConstants.FixedAssetOperationLogShowColumn.图片.name(), afterUpdateDTO.getImageUrl()));
        }
        if (isCreate || valueChanged(beforeUpdate.getBarcodeUri(), afterUpdate.getBarcodeUri())) {
            changeItems.add(new FixedAssetOperationItemDTO(FixedAssetConstants.FixedAssetOperationLogShowColumn.条形码.name(), afterUpdateDTO.getBarcodeUrl()));
        }
        if (isCreate || valueChanged(beforeUpdate.getOtherInfo(), afterUpdate.getOtherInfo())) {
            changeItems.add(new FixedAssetOperationItemDTO(FixedAssetConstants.FixedAssetOperationLogShowColumn.其他.name(), afterUpdateDTO.getOtherInfo()));
        }
        if (isCreate || valueChanged(beforeUpdate.getRemark(), afterUpdate.getRemark())) {
            changeItems.add(new FixedAssetOperationItemDTO(FixedAssetConstants.FixedAssetOperationLogShowColumn.备注.name(), afterUpdateDTO.getRemark()));
        }
        if (isCreate || valueChanged(beforeUpdate.getStatus(), afterUpdate.getStatus())) {
            changeItems.add(new FixedAssetOperationItemDTO(FixedAssetConstants.FixedAssetOperationLogShowColumn.状态.name(), afterUpdateDTO.getStatusDisplayName()));
        }
        if (isCreate || valueChanged(beforeUpdate.getLocation(), afterUpdate.getLocation())) {
            changeItems.add(new FixedAssetOperationItemDTO(FixedAssetConstants.FixedAssetOperationLogShowColumn.存放地点.name(), afterUpdateDTO.getLocation()));
        }
        if (isCreate || valueChanged(beforeUpdate.getOccupiedDate(), afterUpdate.getOccupiedDate())) {
            changeItems.add(new FixedAssetOperationItemDTO(FixedAssetConstants.FixedAssetOperationLogShowColumn.领用时间.name(), afterUpdateDTO.getOccupiedDate()));
        }
        if (isCreate || valueChanged(beforeUpdate.getOccupiedDepartmentId(), afterUpdate.getOccupiedDepartmentId())) {
            changeItems.add(new FixedAssetOperationItemDTO(FixedAssetConstants.FixedAssetOperationLogShowColumn.使用部门.name(), afterUpdateDTO.getOccupied_department_name()));
        }
        if (isCreate || valueChanged(beforeUpdate.getOccupiedMemberDetailId(), afterUpdate.getOccupiedMemberDetailId())) {
            changeItems.add(new FixedAssetOperationItemDTO(FixedAssetConstants.FixedAssetOperationLogShowColumn.使用人.name(), afterUpdateDTO.getOccupied_member_name()));
        }
        fixedAssetOperationLog.setOperationInfo(JSON.toJSONString(changeItems));
        return fixedAssetOperationLog;
    }

    private boolean valueChanged(Object oldValue, Object newValue) {
        if (oldValue == null && newValue == null) {
            return false;
        }
        if (oldValue != null && newValue != null) {
            return !oldValue.toString().equals(newValue.toString());
        }
        return true;
    }

    public <T> CreateOrUpdateFixedAssetCommand checkDataAndBuildCreateCommand(RowResult r, T data, ImportFixedAssetsCommand importFixedAssetsCommand, List<FixedAssetCategoryDTO> topCategories, OrganizationTreeDTO topOrganization, ImportFileResultLog<T> log) {
        String itemNo = r.getA(); // 编号
        String categoryName = r.getB(); // 分类名称
        String name = r.getC(); // 资产名称
        String specification = r.getD(); // 规格
        String price = r.getE(); // 单价
        String buyDate = r.getF(); // 购买时间
        String vendor = r.getG(); // 所属供应商
        String addFromName = r.getH(); // 来源
        String otherInfo = r.getI(); // 其它信息
        String statusName = r.getJ(); // 状态
        String departmentName = r.getK(); // 使用部门
        String memberName = r.getL(); // 使用人
        String occupyDate = r.getM(); // 领用时间
        String location = r.getN(); // 存放地点
        String remark = r.getO(); // 备注

        if (!fixedAssetUtils.checkFixedAssetItemNo(log, data, itemNo)) {
            return null;
        }

        Integer fixedAssetCategoryIdFromName = FixedAssetUtils.findFixedAssetCategoryByTreeName(categoryName, topCategories);
        if (!fixedAssetUtils.checkFixedAssetCategory(log, data, fixedAssetCategoryIdFromName, categoryName)) {
            return null;
        }
        if (!fixedAssetUtils.checkFixedAssetName(log, data, name)) {
            return null;
        }
        if (!fixedAssetUtils.checkFixedAssetSpecification(log, data, specification)) {
            return null;
        }
        if (!fixedAssetUtils.checkFixedAssetPrice(log, data, price)) {
            return null;
        }
        if (!fixedAssetUtils.checkFixedAssetBuyDate(log, data, buyDate)) {
            return null;
        }
        if (!fixedAssetUtils.checkFixedAssetVendor(log, data, vendor)) {
            return null;
        }
        if (!fixedAssetUtils.checkFixedAssetAddFrom(log, data, addFromName)) {
            return null;
        }
        if (!fixedAssetUtils.checkFixedAssetOtherInfo(log, data, otherInfo)) {
            return null;
        }
        if (!fixedAssetUtils.checkFixedAssetStatus(log, data, statusName, occupyDate, departmentName)) {
            return null;
        }
        if (!fixedAssetUtils.checkFixedAssetOccupyDate(log, data, occupyDate, departmentName)) {
            return null;
        }

        Long departmentId = FixedAssetUtils.findOrganizationIdByTreeName(departmentName, topOrganization);
        if (!fixedAssetUtils.checkOccupyDepartment(log, data, departmentId, departmentName, occupyDate)) {
            return null;
        }

        OrganizationMemberDTO organizationMemberDTO = null;
        if (StringUtils.hasText(memberName)) {
            Long organizationId = departmentId != null ? departmentId : importFixedAssetsCommand.getOwnerId();
            organizationMemberDTO = matchMembersByDepartmentId(memberName, organizationId);
        }
        if (!fixedAssetUtils.checkOccupyMember(log, data, organizationMemberDTO, memberName, departmentName, occupyDate)) {
            return null;
        }
        if (!fixedAssetUtils.checkFixedAssetLocation(log, data, location)) {
            return null;
        }
        if (!fixedAssetUtils.checkFixedAssetRemark(log, data, remark)) {
            return null;
        }

        Integer fixedAssetCategoryId = !StringUtils.hasText(categoryName) ? importFixedAssetsCommand.getFixedAssetCategoryId() : fixedAssetCategoryIdFromName;
        Byte addFrom = StringUtils.isEmpty(addFromName) ? FixedAssetAddFrom.BUY.getCode() : FixedAssetAddFrom.fromName(addFromName).getCode();
        CreateOrUpdateFixedAssetCommand cmd = new CreateOrUpdateFixedAssetCommand();
        cmd.setOwnerType(importFixedAssetsCommand.getOwnerType());
        cmd.setOwnerId(importFixedAssetsCommand.getOwnerId());
        cmd.setItemNo(itemNo.trim());
        cmd.setFixedAssetCategoryId(fixedAssetCategoryId);
        cmd.setName(name.trim());
        cmd.setSpecification(StringUtils.hasText(specification) ? specification.trim() : null);
        if (StringUtils.hasText(price)) {
            cmd.setPrice(new BigDecimal(price.trim()));
        }
        cmd.setBuyDate(StringUtils.hasText(buyDate) ? buyDate.trim() : null);
        cmd.setVendor(StringUtils.hasText(vendor) ? vendor.trim() : null);
        cmd.setAddFrom(addFrom);
        cmd.setOtherInfo(StringUtils.hasText(otherInfo) ? otherInfo.trim() : null);
        cmd.setStatus(FixedAssetStatus.fromName(statusName.trim()).getCode());
        cmd.setOccupiedDepartmentId(departmentId);
        if (organizationMemberDTO != null) {
            cmd.setOccupiedMemberDetailId(organizationMemberDTO.getDetailId());
        }
        cmd.setOccupiedDate(StringUtils.hasText(occupyDate) ? occupyDate.trim() : null);
        cmd.setLocation(StringUtils.hasText(location) ? location.trim() : null);
        cmd.setRemark(StringUtils.hasText(remark) ? remark.trim() : null);
        return cmd;
    }

    private OrganizationMemberDTO matchMembersByDepartmentId(String memberName, Long organizationId) {
        OrganizationDTO organizationDTO = organizationService.getOrganizationById(organizationId);
        if (organizationDTO == null) {
            return null;
        }
        ListOrganizationContactCommand orgCommand = new ListOrganizationContactCommand();
        orgCommand.setOrganizationId(organizationId);
        orgCommand.setPageSize(Integer.MAX_VALUE - 1);
        orgCommand.setFilterScopeTypes(Collections.singletonList(FilterOrganizationContactScopeType.CURRENT.getCode()));
        orgCommand.setKeywords(memberName);
        orgCommand.setVisibleFlag(VisibleFlag.ALL.getCode());
        ListOrganizationMemberCommandResponse members = organizationService.listOrganizationPersonnelsWithDownStream(orgCommand);
        if (members == null || CollectionUtils.isEmpty(members.getMembers())) {
            return null;
        }
        for (OrganizationMemberDTO organizationMemberDTO : members.getMembers()) {
            if (!organizationMemberDTO.getContactName().equals(memberName)) {
                continue;
            }
            if (CollectionUtils.isEmpty(organizationMemberDTO.getDepartments())) {
                return organizationMemberDTO;
            }
            for (OrganizationDTO department : organizationMemberDTO.getDepartments()) {
                if (department.getName().equals(organizationDTO.getName())) {
                    return organizationMemberDTO;
                }
            }
        }
        return null;
    }

    private void checkCategoryIsEmpty(List<FixedAssetCategoryDTO> categories) {
        if (CollectionUtils.isEmpty(categories)) {
            throw RuntimeErrorException
                    .errorWith(
                            FixedAssetErrorCode.SCOPE,
                            FixedAssetErrorCode.FIXED_ASSET_CATEGORY_EMPTY_ERROR,
                            localeStringService.getLocalizedString(
                                    String.valueOf(FixedAssetErrorCode.SCOPE),
                                    String.valueOf(FixedAssetErrorCode.FIXED_ASSET_CATEGORY_EMPTY_ERROR),
                                    UserContext.current().getUser().getLocale(),
                                    "Categories are empty"));
        }
    }
}
