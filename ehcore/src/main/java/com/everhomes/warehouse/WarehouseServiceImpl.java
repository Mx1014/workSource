package com.everhomes.warehouse;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.ExecuteImportTaskCallback;
import com.everhomes.organization.ImportFileService;
import com.everhomes.organization.ImportFileTask;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.portal.PortalService;
import com.everhomes.requisition.RequisitionService;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.acl.PrivilegeServiceErrorCode;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.community.CommunityServiceErrorCode;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.order.OwnerType;
import com.everhomes.rest.organization.ImportFileResultLog;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.organization.ImportFileTaskType;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationServiceErrorCode;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.warehouse.CreateOrUpdateWarehouseEntryOrderCommand;
import com.everhomes.rest.warehouse.CreateRequestCommand;
import com.everhomes.rest.warehouse.CreateWarehouseEntryOrderDTO;
import com.everhomes.rest.warehouse.DeleteRequestCommand;
import com.everhomes.rest.warehouse.DeleteWarehouseCommand;
import com.everhomes.rest.warehouse.DeleteWarehouseMaterialCategoryCommand;
import com.everhomes.rest.warehouse.DeleteWarehouseMaterialCommand;
import com.everhomes.rest.warehouse.DeleteWarehouseMaterialUnitCommand;
import com.everhomes.rest.warehouse.DeliveryFlag;
import com.everhomes.rest.warehouse.FindRequestCommand;
import com.everhomes.rest.warehouse.ImportOwnerCommand;
import com.everhomes.rest.warehouse.ImportStocksCommand;
import com.everhomes.rest.warehouse.ImportStocksDataDTO;
import com.everhomes.rest.warehouse.ImportWarehouseMaterialCategoryDataDTO;
import com.everhomes.rest.warehouse.ImportWarehouseMaterialDataDTO;
import com.everhomes.rest.warehouse.ListMaterialLogsBySupplierCommand;
import com.everhomes.rest.warehouse.ListMaterialLogsBySupplierResponse;
import com.everhomes.rest.warehouse.ListWarehouseMaterialUnitsCommand;
import com.everhomes.rest.warehouse.ListWarehouseStockOrdersCommand;
import com.everhomes.rest.warehouse.ListWarehouseStockOrdersResponse;
import com.everhomes.rest.warehouse.QueryRequestCommand;
import com.everhomes.rest.warehouse.ReviewResult;
import com.everhomes.rest.warehouse.SearchOneselfRequestsCommand;
import com.everhomes.rest.warehouse.SearchRequestsCommand;
import com.everhomes.rest.warehouse.SearchRequestsResponse;
import com.everhomes.rest.warehouse.SearchWarehouseMaterialsCommand;
import com.everhomes.rest.warehouse.SearchWarehouseMaterialsResponse;
import com.everhomes.rest.warehouse.SearchWarehouseStockLogsCommand;
import com.everhomes.rest.warehouse.SearchWarehouseStockLogsResponse;
import com.everhomes.rest.warehouse.SearchWarehouseStocksCommand;
import com.everhomes.rest.warehouse.SearchWarehouseStocksResponse;
import com.everhomes.rest.warehouse.SearchWarehousesCommand;
import com.everhomes.rest.warehouse.SearchWarehousesResponse;
import com.everhomes.rest.warehouse.Status;
import com.everhomes.rest.warehouse.UpdateWarehouseCommand;
import com.everhomes.rest.warehouse.UpdateWarehouseMaterialCategoryCommand;
import com.everhomes.rest.warehouse.UpdateWarehouseMaterialCommand;
import com.everhomes.rest.warehouse.UpdateWarehouseMaterialUnitCommand;
import com.everhomes.rest.warehouse.UpdateWarehouseStockCommand;
import com.everhomes.rest.warehouse.WarehouseDTO;
import com.everhomes.rest.warehouse.WarehouseLogDTO;
import com.everhomes.rest.warehouse.WarehouseMaterialCategoryDTO;
import com.everhomes.rest.warehouse.WarehouseMaterialDTO;
import com.everhomes.rest.warehouse.WarehouseMaterialStock;
import com.everhomes.rest.warehouse.WarehouseMaterialUnitDTO;
import com.everhomes.rest.warehouse.WarehouseRequestDetailsDTO;
import com.everhomes.rest.warehouse.WarehouseRequestMaterialDTO;
import com.everhomes.rest.warehouse.WarehouseRequestMaterialDetailDTO;
import com.everhomes.rest.warehouse.WarehouseServiceErrorCode;
import com.everhomes.rest.warehouse.WarehouseStatus;
import com.everhomes.rest.warehouse.WarehouseStockDTO;
import com.everhomes.rest.warehouse.WarehouseStockExportDetailDTO;
import com.everhomes.rest.warehouse.WarehouseStockLogDTO;
import com.everhomes.rest.warehouse.WarehouseStockOrderDTO;
import com.everhomes.rest.warehouse.WarehouseStockRequestSource;
import com.everhomes.rest.warehouse.WarehouseStockRequestType;
import com.everhomes.search.WarehouseMaterialCategorySearcher;
import com.everhomes.search.WarehouseMaterialSearcher;
import com.everhomes.search.WarehouseRequestMaterialSearcher;
import com.everhomes.search.WarehouseSearcher;
import com.everhomes.search.WarehouseStockLogSearcher;
import com.everhomes.search.WarehouseStockSearcher;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.EhWarehouseOrders;
import com.everhomes.server.schema.tables.pojos.EhWarehouseStockLogs;
import com.everhomes.server.schema.tables.pojos.EhWarehouseStocks;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.supplier.SupplierHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.DateUtils;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.excel.ExcelUtils;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.everhomes.util.RuntimeErrorException.errorWith;

/**
 * Created by ying.xiong on 2017/5/12.
 */
@Component
public class WarehouseServiceImpl implements WarehouseService {
    final String downloadDir = "\\download\\";
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseServiceImpl.class);

    private static DateTimeFormatter dfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private WarehouseProvider warehouseProvider;

    @Autowired
    private WarehouseSearcher warehouseSearcher;

    @Autowired
    private WarehouseMaterialCategorySearcher warehouseMaterialCategorySearcher;

    @Autowired
    private WarehouseMaterialSearcher warehouseMaterialSearcher;

    @Autowired
    private WarehouseStockSearcher warehouseStockSearcher;

    @Autowired
    private WarehouseStockLogSearcher warehouseStockLogSearcher;

    @Autowired
    private WarehouseRequestMaterialSearcher warehouseRequestMaterialSearcher;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private FlowService flowService;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private ConfigurationProvider configProvider;

    @Autowired
    private ImportFileService importFileService;

    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private LocaleStringService localeStringService;

    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;

    @Autowired
    private PortalService portalService;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private RequisitionService requisitionService;

    @Autowired
    private FlowCaseProvider flowCaseProvider;

    @Autowired
	private CommunityProvider communityProvider;

    @Autowired
	private OrganizationService organizationService;

    @Autowired
    private WarehouseService warehouseService;

    @Override
    public WarehouseDTO updateWarehouse(UpdateWarehouseCommand cmd) {
        checkAssetPriviledgeForPropertyOrg(cmd.getCommunityId(), PrivilegeConstants.WAREHOUSE_REPO_OPERATION, cmd.getOwnerId());
        Warehouses warehouse = ConvertHelper.convert(cmd, Warehouses.class);
        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_WAREHOUSE.getCode().substring(
                CoordinationLocks.UPDATE_WAREHOUSE.getCode().lastIndexOf(".") + 1
        )
                + cmd.getOwnerType() + cmd.getOwnerId() + cmd.getCommunityId()).enter(() -> {
            checkWarehouseNumber(warehouse.getId(), warehouse.getWarehouseNumber(), null,null, warehouse.getCommunityId());
            if (cmd.getId() == null) {
                warehouse.setNamespaceId(cmd.getNamespaceId());
                warehouse.setCreatorUid(UserContext.current().getUser().getId());
                warehouseProvider.creatWarehouse(warehouse);
            } else {
                Warehouses exist = verifyWarehouses(warehouse.getId(),null,null, warehouse.getCommunityId());
                warehouse.setNamespaceId(exist.getNamespaceId());
                warehouse.setCreatorUid(exist.getCreatorUid());
                warehouse.setCreateTime(exist.getCreateTime());
                warehouseProvider.updateWarehouse(warehouse);
            }

            warehouseSearcher.feedDoc(warehouse);
            return null;
        });
        return ConvertHelper.convert(warehouse, WarehouseDTO.class);
    }

    private void checkWarehouseNumber(Long warehouseId, String warehouseNumber, String ownerType, Long ownerId, Long communityId) {
        Warehouses warehouse = warehouseProvider.findWarehouseByNumber(warehouseNumber, null, null, communityId);
        if (warehouseId == null) {
            if (warehouse != null) {
                LOGGER.error("warehouseNumber already exist, warehouseNumber = " + warehouseNumber
                        + ", ownerType = " + ownerType + ", ownerId = " + ownerId);
                throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_NUMBER_ALREADY_EXIST,
                        localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_NUMBER_ALREADY_EXIST),
                                UserContext.current().getUser().getLocale(), "warehouseNumber already exist"));

            }
        } else {
            if (warehouse != null && !warehouse.getId().equals(warehouseId)) {
                LOGGER.error("warehouseNumber already exist, warehouseNumber = " + warehouseNumber
                        + ", ownerType = " + ownerType + ", ownerId = " + ownerId);
                throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_NUMBER_ALREADY_EXIST,
                        localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_NUMBER_ALREADY_EXIST),
                                UserContext.current().getUser().getLocale(), "warehouseNumber already exist"));
            }
        }
    }

    private Warehouses verifyWarehouses(Long warehouseId, String ownerType, Long ownerId, Long communityId) {
        Warehouses warehouse = warehouseProvider.findWarehouse(warehouseId, null,null,communityId);
        if (warehouse == null) {
            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE,
                    WarehouseServiceErrorCode.ERROR_WAREHOUSE_NOT_EXIST,
                    localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                            String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_NOT_EXIST),
                            UserContext.current().getUser().getLocale(), "仓库不存在"));
        }

        return warehouse;
    }

    @Override
    public void deleteWarehouse(DeleteWarehouseCommand cmd) {
        checkAssetPriviledgeForPropertyOrg(cmd.getCommunityId(), PrivilegeConstants.WAREHOUSE_REPO_OPERATION, cmd.getOwnerId());
        Warehouses warehouse = verifyWarehouses(cmd.getWarehouseId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getCommunityId());

        //库存不为0时不能删除 @deprecated
//        Long amount = warehouseProvider.getWarehouseStockAmount(cmd.getWarehouseId(), cmd.getOwnerType(), cmd.getOwnerId());
//        if (amount != null && amount > 0) {
//            LOGGER.error("warehouse stock is not null, warehouseId = " + cmd.getWarehouseId()
//                    + ", ownerType = " + cmd.getOwnerType() + ", ownerId = " + cmd.getOwnerId()
//                    + ", count stocks = " + amount);
//            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_STOCK_NOT_NULL,
//                    localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
//                            String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_STOCK_NOT_NULL),
//                            UserContext.current().getUser().getLocale(), "仓库库存不为0"));
//
//        }
        // 仓库关闭时可以删除
        if(warehouse.getStatus().byteValue() == WarehouseStatus.DISABLE.getCode()){
            warehouse.setStatus(WarehouseStatus.INACTIVE.getCode());
            warehouse.setDeleteUid(UserContext.current().getUser().getId());
            warehouse.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.dbProvider.execute((status) ->{
                warehouseProvider.updateWarehouse(warehouse);
                warehouseSearcher.deleteById((warehouse.getId()));
                //仓库删除时，把仓库里的库存也删除掉  by wentian 2018/3/21   from redmine #26047
                warehouseProvider.deleteWarehouseStocks(warehouse.getId());
                return null;
            });
        }else{
            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE
                    ,WarehouseServiceErrorCode.ERROR_WAREHOUSE_IS_RUNNING
                    ,"warehouse cannot be deleted when it's enabled");
        }

    }

    @Override
    public WarehouseDTO findWarehouse(DeleteWarehouseCommand cmd) {
        Warehouses warehouse = verifyWarehouses(cmd.getWarehouseId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getCommunityId());
        WarehouseDTO dto = ConvertHelper.convert(warehouse, WarehouseDTO.class);
        return dto;

    }

    private String generateCategoryNumber() {

        String num = "category_" + RandomUtils.nextInt(100000000);

        return num;
    }

    @Override
    public WarehouseMaterialCategoryDTO updateWarehouseMaterialCategory(UpdateWarehouseMaterialCategoryCommand cmd) {
        //没有项目指定，无法再非全部项目授权下正常使用
//        checkAssetPriviledgeForPropertyOrg(null, PrivilegeConstants.WAREHOUSE_MATERIAL_CATEGORY_ALL,cmd.getOwnerId());
        if (StringUtils.isBlank(cmd.getName())) {
            LOGGER.error("warehouse material category name is null, data = {}", cmd);
            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_NAME_IS_NULL,
                    localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                            String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_NAME_IS_NULL),
                            UserContext.current().getUser().getLocale(), "warehouse material category name is null"));
        }
        WarehouseMaterialCategories category = ConvertHelper.convert(cmd, WarehouseMaterialCategories.class);
        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_WAREHOUSE_CATEGORY.getCode()
                .substring(CoordinationLocks.UPDATE_WAREHOUSE_CATEGORY.getCode().lastIndexOf(".") + 1)
                + cmd.getOwnerType() + cmd.getOwnerId()).enter(() -> {
            if (cmd.getId() == null) {
                category.setNamespaceId(cmd.getNamespaceId());
                category.setCreatorUid(UserContext.current().getUser().getId());
                category.setPath("");
                WarehouseMaterialCategories parent = warehouseProvider.findWarehouseMaterialCategories(category.getParentId(), category.getOwnerType(), category.getOwnerId());
                if (parent != null) {
                    category.setPath(parent.getPath());
                }

                if (category.getCategoryNumber() == null) {
                    category.setCategoryNumber(generateCategoryNumber());
                } else {
                    checkCategoryNumber(category.getId(), category.getCategoryNumber(), category.getOwnerType(), category.getOwnerId());
                }

                warehouseProvider.creatWarehouseMaterialCategories(category);
            } else {
                WarehouseMaterialCategories exist = verifyWarehouseMaterialCategories(category.getId(), category.getOwnerType(), category.getOwnerId());
                category.setNamespaceId(exist.getNamespaceId());
                category.setCreatorUid(exist.getCreatorUid());
                category.setCreateTime(exist.getCreateTime());
                checkCategoryNumber(category.getId(), category.getCategoryNumber(), category.getOwnerType(), category.getOwnerId());
                WarehouseMaterialCategories parent = warehouseProvider.findWarehouseMaterialCategories(category.getParentId(), category.getOwnerType(), category.getOwnerId());
                if (parent != null) {
                    category.setPath(parent.getPath() + "/" + category.getId());
                } else {
                    category.setPath("/" + category.getId());
                }
                warehouseProvider.updateWarehouseMaterialCategories(category);
            }

            warehouseMaterialCategorySearcher.feedDoc(category);
            return null;
        });
        WarehouseMaterialCategoryDTO dto = ConvertHelper.convert(category, WarehouseMaterialCategoryDTO.class);
        return dto;
    }

    private void checkCategoryNumber(Long categoryId, String categoryNumber, String ownerType, Long ownerId) {
        WarehouseMaterialCategories category = warehouseProvider.findWarehouseMaterialCategoriesByNumber(categoryNumber, null, null);
        if (categoryId == null) {
            if (category != null) {
                LOGGER.error("categoryNumber already exist, categoryNumber = " + categoryNumber
                        + ", ownerType = " + ownerType + ", ownerId = " + ownerId);
                throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_NUMBER_ALREADY_EXIST,
                        localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_NUMBER_ALREADY_EXIST),
                                UserContext.current().getUser().getLocale(), "categoryNumber already exist"));
            }
        } else {
            if (category != null && !category.getId().equals(categoryId)) {
                LOGGER.error("categoryNumber already exist, categoryNumber = " + categoryNumber
                        + ", ownerType = " + ownerType + ", ownerId = " + ownerId);
                throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_NUMBER_ALREADY_EXIST,
                        localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_NUMBER_ALREADY_EXIST),
                                UserContext.current().getUser().getLocale(), "categoryNumber already exist"));
            }
        }
    }

    private WarehouseMaterialCategories verifyWarehouseMaterialCategories(Long categoryId, String ownerType, Long ownerId) {
        WarehouseMaterialCategories category = warehouseProvider.findWarehouseMaterialCategories(categoryId, null, null);
        if (category == null) {
            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE,
                    WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_NOT_EXIST,
                    localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                            String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_NOT_EXIST),
                            UserContext.current().getUser().getLocale(), "物品分类不存在"));
        }

        return category;
    }

    @Override
    public void deleteWarehouseMaterialCategory(DeleteWarehouseMaterialCategoryCommand cmd) {
        //不支持指定项目
//        checkAssetPriviledgeForPropertyOrg(null, PrivilegeConstants.WAREHOUSE_MATERIAL_CATEGORY_ALL,cmd.getOwnerId());
        WarehouseMaterialCategories category = verifyWarehouseMaterialCategories(cmd.getCategoryId(), cmd.getOwnerType(), cmd.getOwnerId());

        //该分类下有关联任何物品时，该分类不可删除
        List<WarehouseMaterials> materials = warehouseProvider.listWarehouseMaterialsByCategory(category.getPath(), category.getOwnerType(), category.getOwnerId());
        if (materials != null && materials.size() > 0) {
            LOGGER.error("the category has attach to active material, categoryId = " + cmd.getCategoryId()
                    + ", ownerType = " + cmd.getOwnerType() + ", ownerId = " + cmd.getOwnerId());
            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE,
                    WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_IN_USE,
                    localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                            String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_IN_USE),
                            UserContext.current().getUser().getLocale(), "物品分类有关联物品"));
        }
        category.setStatus(Status.INACTIVE.getCode());
        category.setDeleteUid(UserContext.current().getUser().getId());
        category.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        warehouseProvider.updateWarehouseMaterialCategories(category);
        warehouseMaterialCategorySearcher.deleteById((category.getId()));
    }

    @Override
    public WarehouseMaterialCategoryDTO findWarehouseMaterialCategory(DeleteWarehouseMaterialCategoryCommand cmd) {
        WarehouseMaterialCategories category = verifyWarehouseMaterialCategories(cmd.getCategoryId(), cmd.getOwnerType(), cmd.getOwnerId());
        WarehouseMaterialCategoryDTO dto = ConvertHelper.convert(category, WarehouseMaterialCategoryDTO.class);
        if (dto.getParentId() != null) {
            WarehouseMaterialCategories parent = warehouseProvider.findWarehouseMaterialCategories(dto.getParentId(), dto.getOwnerType(), dto.getOwnerId());
            if (parent != null) {
                dto.setParentCategoryNumber(parent.getCategoryNumber());
                dto.setParentCategoryName(parent.getName());
            }
        }
        return dto;
    }

    @Override
    public WarehouseMaterialCategoryDTO listWarehouseMaterialCategory(DeleteWarehouseMaterialCategoryCommand cmd) {
        WarehouseMaterialCategories category = new WarehouseMaterialCategories();
        if (cmd.getCategoryId() == null || cmd.getCategoryId() == 0L) {
            category.setId(0L);
            category.setName("全部");
            category.setPath("");
        } else {
            category = verifyWarehouseMaterialCategories(cmd.getCategoryId(), cmd.getOwnerType(), cmd.getOwnerId());
        }
        WarehouseMaterialCategoryDTO dto = ConvertHelper.convert(category, WarehouseMaterialCategoryDTO.class);

        List<WarehouseMaterialCategories> children = warehouseProvider.listAllChildWarehouseMaterialCategories(category.getPath() + "/%", null, null);
        if (children != null && children.size() > 0) {
            List<WarehouseMaterialCategoryDTO> childrenDto = children.stream().map(child -> ConvertHelper.convert(child, WarehouseMaterialCategoryDTO.class)).collect(Collectors.toList());
            dto = this.processWarehouseMaterialCategoryTree(childrenDto, dto);
        }
        return dto;
    }

    private WarehouseMaterialCategoryDTO processWarehouseMaterialCategoryTree(List<WarehouseMaterialCategoryDTO> dtos, WarehouseMaterialCategoryDTO dto) {

        List<WarehouseMaterialCategoryDTO> trees = new ArrayList<WarehouseMaterialCategoryDTO>();
        for (WarehouseMaterialCategoryDTO treeDTO : dtos) {
            if (treeDTO.getParentId().equals(dto.getId())) {
                WarehouseMaterialCategoryDTO categoryTreeDTO = processWarehouseMaterialCategoryTree(dtos, treeDTO);
                trees.add(categoryTreeDTO);
            }
        }

        dto.setChildrens(trees);
        return dto;
    }

    @Override
    public WarehouseMaterialDTO updateWarehouseMaterial(UpdateWarehouseMaterialCommand cmd) {
        checkAssetPriviledgeForPropertyOrg(cmd.getCommunityId(), PrivilegeConstants.WAREHOUSE_MATERIAL_INFO_ALL, cmd.getOwnerId());
        WarehouseMaterials material = ConvertHelper.convert(cmd, WarehouseMaterials.class);

        this.coordinationProvider.getNamedLock(
                CoordinationLocks.UPDATE_WAREHOUSE_MATERIAL.getCode().substring(CoordinationLocks.UPDATE_WAREHOUSE_MATERIAL.getCode().lastIndexOf(".")+1)
                + cmd.getOwnerType() + cmd.getOwnerId() + cmd.getCommunityId()).enter(() -> {
            //检查此设备的编号，增加园区维度进行查询,允许一个企业同一个设备在不同园区增加实例
            checkMaterialNumber(material.getId(), material.getMaterialNumber(), material.getOwnerType(), material.getOwnerId(), cmd.getCommunityId());
            if (cmd.getId() == null) {
                //没有，则给定分类并新增
                material.setNamespaceId(cmd.getNamespaceId());
                material.setCreatorUid(UserContext.current().getUser().getId());
                WarehouseMaterialCategories category = warehouseProvider.findWarehouseMaterialCategories(material.getCategoryId(), material.getOwnerType(), material.getOwnerId());
                if (category != null) {
                    material.setCategoryPath(category.getPath());
                }
                //增加供应商
                material.setSupplierId(cmd.getSupplierId());
                material.setSupplierName(cmd.getSupplierName());
                warehouseProvider.creatWarehouseMaterials(material);
            } else {
                //有，则进行修改该
                WarehouseMaterials exist = verifyWarehouseMaterials(material.getId(), material.getOwnerType(), material.getOwnerId(), material.getCommunityId());
                material.setNamespaceId(exist.getNamespaceId());
                material.setCreatorUid(exist.getCreatorUid());
                material.setCreateTime(exist.getCreateTime());
                WarehouseMaterialCategories category = warehouseProvider.findWarehouseMaterialCategories(material.getCategoryId(), material.getOwnerType(), material.getOwnerId());
                if (category != null) {
                    material.setCategoryPath(category.getPath());
                }
                //修改供应商
                material.setSupplierId(cmd.getSupplierId());
                material.setSupplierName(cmd.getSupplierName());
                warehouseProvider.updateWarehouseMaterials(material);
            }
            //更新到es上
            warehouseMaterialSearcher.feedDoc(material);
            return null;
        });
        //返回返回新增数据
        WarehouseMaterialDTO dto = ConvertHelper.convert(material, WarehouseMaterialDTO.class);
        WarehouseUnits unit = warehouseProvider.findWarehouseUnits(dto.getUnitId(), dto.getOwnerType(), dto.getOwnerId());
        if (unit != null) {
            dto.setUnitName(unit.getName());
        }
        WarehouseMaterialCategories category = warehouseProvider.findWarehouseMaterialCategories(dto.getCategoryId(), dto.getOwnerType(), dto.getOwnerId());
        if (category != null) {
            dto.setCategoryName(category.getName());
        }

        return dto;
    }

    private void checkMaterialNumber(Long materialId, String materialNumber, String ownerType, Long ownerId, Long communityId) {
        WarehouseMaterials material = warehouseProvider.findWarehouseMaterialsByNumber(materialNumber, null, null, communityId);
        if (materialId == null) {
            if (material != null) {
                LOGGER.error("materialNumber already exist, materialNumber = " + materialNumber
                        + ", ownerType = " + ownerType + ", ownerId = " + ownerId);
                throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_NUMBER_ALREADY_EXIST,
                        localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_NUMBER_ALREADY_EXIST),
                                UserContext.current().getUser().getLocale(), "materialNumber already exist"));
            }
        } else {
            if (material != null && !material.getId().equals(materialId)) {
                LOGGER.error("materialNumber already exist, materialNumber = " + materialNumber
                        + ", ownerType = " + ownerType + ", ownerId = " + ownerId);
                throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_NUMBER_ALREADY_EXIST,
                        localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_NUMBER_ALREADY_EXIST),
                                UserContext.current().getUser().getLocale(), "materialNumber already exist"));
            }
        }
    }

    private WarehouseMaterials verifyWarehouseMaterials(Long matetial, String ownerType, Long ownerId, Long communityId) {
        WarehouseMaterials material = warehouseProvider.findWarehouseMaterials(matetial, null, null, communityId);
        if (material == null) {
            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE,
                    WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_NOT_EXIST,
                    localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                            String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_NOT_EXIST),
                            UserContext.current().getUser().getLocale(), "物品不存在"));
        }

        return material;
    }

    @Override
    public void deleteWarehouseMaterial(DeleteWarehouseMaterialCommand cmd) {
        checkAssetPriviledgeForPropertyOrg(cmd.getCommunityId(), PrivilegeConstants.WAREHOUSE_MATERIAL_INFO_ALL, cmd.getOwnerId());
        WarehouseMaterials material = verifyWarehouseMaterials(cmd.getMaterialId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getCommunityId());

        //物品有库存引用时，该物品不可删除
        Long amount = warehouseProvider.getWarehouseStockAmountByMaterialId(cmd.getMaterialId(), cmd.getOwnerType(), cmd.getOwnerId());
        if (amount != null && amount > 0) {
            LOGGER.error("the material is related to warehouse, materialId = " + cmd.getMaterialId()
                    + ", ownerType = " + cmd.getOwnerType() + ", ownerId = " + cmd.getOwnerId()
                    + ", amount = " + amount);
            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE,
                    WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_RELATED_TO_WAREHOUSE,
                    localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                            String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_RELATED_TO_WAREHOUSE),
                            UserContext.current().getUser().getLocale(), "物品有库存引用"));
        }
        material.setStatus(Status.INACTIVE.getCode());
        material.setDeleteUid(UserContext.current().getUser().getId());
        material.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        warehouseProvider.updateWarehouseMaterials(material);
        warehouseMaterialSearcher.deleteById(material.getId());

        List<WarehouseStocks> stocks = warehouseProvider.listMaterialStocks(material.getId(), cmd.getOwnerType(), cmd.getOwnerId());
        if (stocks != null && stocks.size() > 0) {
            for (WarehouseStocks stock : stocks) {
                stock.setStatus(Status.INACTIVE.getCode());
                warehouseProvider.updateWarehouseStock(stock);
                warehouseStockSearcher.feedDoc(stock);
            }
        }
    }

    @Override
    public WarehouseMaterialDTO findWarehouseMaterial(DeleteWarehouseMaterialCommand cmd) {
        WarehouseMaterials material = verifyWarehouseMaterials(cmd.getMaterialId(), null, null, cmd.getCommunityId());
        WarehouseMaterialDTO dto = ConvertHelper.convert(material, WarehouseMaterialDTO.class);

        WarehouseMaterialCategories category = warehouseProvider.findWarehouseMaterialCategories(dto.getCategoryId(), null, null);
        if (category != null) {
            dto.setCategoryName(category.getName());
        }

        WarehouseUnits unit = warehouseProvider.findWarehouseUnits(dto.getUnitId(), null,null);
        if (unit != null) {
            dto.setUnitName(unit.getName());
        }
        return dto;
    }

    @Override
    public void updateWarehouseStock(UpdateWarehouseStockCommand cmd) {
		// 增加必要的参数校验
    	if (cmd.getStocks() == null) {
    		LOGGER.error("Amount cannot be empty.");
			throw errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_NOT_EXIST,
					"WarehouseMaterial cannot be empty.");
		} else {
			for (WarehouseMaterialStock stock : cmd.getStocks()) {
				if (stock.getAmount() == null) {
					LOGGER.error("Amount cannot be empty.");
					throw errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_AMOUNT_EMPTY,
							"Amount cannot be empty.");
				}
				if (stock.getWarehouseId() == null) {
					LOGGER.error("WarehouseName cannot be empty.");
					throw errorWith(WarehouseServiceErrorCode.SCOPE,
							WarehouseServiceErrorCode.ERROR_WAREHOUSENAME_EMPTY, "WarehouseName cannot be empty.");
				}
			}
		}
        if (cmd.getRequestType().byteValue() == WarehouseStockRequestType.STOCK_IN.getCode()) {
            checkAssetPriviledgeForPropertyOrg(cmd.getCommunityId(), PrivilegeConstants.WAREHOUSE_REPO_MAINTAIN_INSTOCK, cmd.getOwnerId()); } else if (cmd.getRequestType().byteValue() == WarehouseStockRequestType.STOCK_OUT.getCode()) {
            checkAssetPriviledgeForPropertyOrg(cmd.getCommunityId(), PrivilegeConstants.WAREHOUSE_REPO_MAINTAIN_OUTSTOCK, cmd.getOwnerId());
        }
        WarehouseOrder order = null;
        if(cmd.getRequestType().byteValue() == WarehouseStockRequestType.STOCK_IN.getCode() || cmd.getRequestType().byteValue() == WarehouseStockRequestType.STOCK_OUT.getCode()){
             //增加普通入库单
            order = ConvertHelper.convert(cmd, WarehouseOrder.class);
            long warehouseOrderId = this.sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhWarehouseOrders.class));
            order.setId(warehouseOrderId);
            order.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            order.setCreateUid(UserContext.currentUserId());
            order.setOwnerType(cmd.getOwnerType());
            order.setOwnerId(cmd.getOwnerId());
            order.setNamespaceId(cmd.getNamespaceId());
            //增加communityId
            order.setCommunityId(cmd.getCommunityId());
            order.setIdentity(SupplierHelper.getIdentity());
            order.setExecutorId(UserContext.currentUserId());
            order.setExecutorName(UserContext.current().getUser().getNickName());
            order.setExecutorTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            order.setServiceType(cmd.getServiceType());
            warehouseProvider.insertWarehouseOrder(order);
        }
        if(cmd.getStocks() == null || cmd.getStocks().size() < 1){
            //改为从request中获取stocks by wentian 因为不同情况的新增和更新都走了此接口，有新的情况添加时此方法会不太明朗 2018/4/10
            List<WarehouseRequestMaterials> warehouseRequestMaterials = warehouseProvider.findAllWarehouseRequestMaterials(cmd.getRequestId());
            if(warehouseRequestMaterials.size() > 0){
                List<WarehouseMaterialStock> stocks = new ArrayList<>();
                for(WarehouseRequestMaterials material : warehouseRequestMaterials){
                    WarehouseMaterialStock stock = new WarehouseMaterialStock();
                    stock.setAmount(material.getAmount());
                    stock.setMaterialId(material.getMaterialId());
                    stock.setWarehouseId(material.getWarehouseId());
                    stocks.add(stock);
                }
                cmd.setStocks(stocks);
            }
        }

        if (cmd.getStocks() != null && cmd.getStocks().size() > 0) {
            cmd.getStocks().forEach(stock -> {
                if (stock.getAmount() <= 0) {
                    LOGGER.error("warehouse stock change amount should larger than 0, stock: {} ", stock);
                    throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_STOCK_SHORTAGE,
                            localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                    String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_REQUEST_MATERIAL_SHOULD_LARGER_THAN_ZERO),
                                    UserContext.current().getUser().getLocale(), "warehouse stock change amount should larger than 0"));
                }

                Warehouses warehouse = warehouseProvider.findWarehouse(stock.getWarehouseId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getCommunityId());
                if (warehouse == null) {
                    LOGGER.error("warehouse is not exit, warehouseid: {} ", stock.getWarehouseId());
                    throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_STOCK_SHORTAGE,
                            localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                    String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_NOT_EXIST),
                                    UserContext.current().getUser().getLocale(), "warehouse is not exit"));

                } else if (warehouse != null && !Status.ACTIVE.equals(Status.fromCode(warehouse.getStatus()))) {
                    LOGGER.error("warehouse is not active, warehouseid: {} ", stock.getWarehouseId());
                    throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_STOCK_SHORTAGE,
                            localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                    String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_IS_NOT_ACTIVE),
                                    UserContext.current().getUser().getLocale(), "warehouse is not active"));

                }
            });
            Long uid = UserContext.current().getUser().getId();
            Timestamp current = new Timestamp(DateHelper.currentGMTTime().getTime());
            WarehouseOrder finalOrder = order;
            cmd.getStocks().forEach(stock -> {
                //增加园区维度 by wentian
                WarehouseStocks materialStock = warehouseProvider.findWarehouseStocksByWarehouseAndMaterial(
                        stock.getWarehouseId(), stock.getMaterialId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getCommunityId());

                if (materialStock != null) {
                    if (WarehouseStockRequestType.STOCK_IN.equals(WarehouseStockRequestType.fromCode(cmd.getRequestType()))) {
                        materialStock.setAmount(materialStock.getAmount() + stock.getAmount());
                        materialStock.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                        warehouseProvider.updateWarehouseStock(materialStock);
                        //更新
                        warehouseStockSearcher.feedDoc(materialStock);

                        WarehouseStockLogs log = ConvertHelper.convert(materialStock, WarehouseStockLogs.class);
                        log.setRequestType(cmd.getRequestType());
                        log.setDeliveryAmount(stock.getAmount());
                        log.setStockAmount(materialStock.getAmount());
                        log.setRequestSource(WarehouseStockRequestSource.MANUAL_INPUT.getCode());
                        log.setDeliveryUid(uid);
                        log.setWarehouseOrderId(finalOrder.getId());
                        warehouseProvider.creatWarehouseStockLogs(log);
                        //更新入库log，增加园区id到es中
                        warehouseStockLogSearcher.feedDoc(log);
                    } else if (WarehouseStockRequestType.STOCK_OUT.equals(WarehouseStockRequestType.fromCode(cmd.getRequestType()))) {
                        if (materialStock.getAmount().compareTo(stock.getAmount()) < 0) {
                            LOGGER.error("warehouse stock is not enough, warehouseId = " + stock.getWarehouseId()
                                    + ", materialId = " + stock.getMaterialId());
                            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_STOCK_SHORTAGE,
                                    localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                            String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_STOCK_SHORTAGE),
                                            UserContext.current().getUser().getLocale(), "warehouse stock is not enough"));
                        }

                        WarehouseRequestMaterials requestMaterial = warehouseProvider.findWarehouseRequestMaterials(cmd.getRequestId(), stock.getWarehouseId(), stock.getMaterialId());
                        if (requestMaterial == null) {
                            LOGGER.error("WarehouseRequestMaterials is not exist, requestId = " + cmd.getRequestId() + ", warehouseId = " + stock.getWarehouseId()
                                    + ", materialId = " + stock.getMaterialId());
                            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_REQUEST_MATERIAL_IS_NOT_EXIST,
                                    localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                            String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_REQUEST_MATERIAL_IS_NOT_EXIST),
                                            UserContext.current().getUser().getLocale(), "WarehouseRequestMaterials is not exist"));
                        }

                        if (!ReviewResult.QUALIFIED.equals(ReviewResult.fromStatus(requestMaterial.getReviewResult()))) {
                            LOGGER.error("WarehouseRequestMaterials is not qualified, requestId = " + cmd.getRequestId() + ", warehouseId = " + stock.getWarehouseId()
                                    + ", materialId = " + stock.getMaterialId());
                            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_REQUEST_MATERIAL_IS_NOT_QUALIFIED,
                                    localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                            String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_REQUEST_MATERIAL_IS_NOT_QUALIFIED),
                                            UserContext.current().getUser().getLocale(), "WarehouseRequestMaterials is not qualified"));
                        }

                        if (DeliveryFlag.YES.equals(DeliveryFlag.fromStatus(requestMaterial.getDeliveryFlag()))) {
                            LOGGER.error("WarehouseRequestMaterials is already delivery, requestId = " + cmd.getRequestId() + ", warehouseId = " + stock.getWarehouseId()
                                    + ", materialId = " + stock.getMaterialId());
                            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_REQUEST_MATERIAL_IS_ALREADY_DELIVERY,
                                    localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                            String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_REQUEST_MATERIAL_IS_ALREADY_DELIVERY),
                                            UserContext.current().getUser().getLocale(), "WarehouseRequestMaterials is already delivery"));
                        }

                        requestMaterial.setDeliveryFlag(DeliveryFlag.YES.getCode());
                        requestMaterial.setDeliveryUid(uid);
                        requestMaterial.setDeliveryTime(current);
                        requestMaterial.setReviewTime(current);
                        requestMaterial.setReviewResult(ReviewResult.QUALIFIED.getCode());
                        warehouseProvider.updateWarehouseRequestMaterial(requestMaterial);
                        //更新
                        warehouseRequestMaterialSearcher.feedDoc(requestMaterial);

                        materialStock.setAmount(materialStock.getAmount() - stock.getAmount());
                        warehouseProvider.updateWarehouseStock(materialStock);
                        warehouseStockSearcher.feedDoc(materialStock);

                        WarehouseStockLogs log = ConvertHelper.convert(materialStock, WarehouseStockLogs.class);
                        log.setRequestType(cmd.getRequestType());
                        log.setDeliveryAmount(stock.getAmount());
                        log.setStockAmount(materialStock.getAmount());
                        log.setRequestSource(WarehouseStockRequestSource.MANUAL_INPUT.getCode());
                        log.setDeliveryUid(uid);
                        log.setWarehouseOrderId(finalOrder.getId());
                        warehouseProvider.creatWarehouseStockLogs(log);
                        warehouseStockLogSearcher.feedDoc(log);
                    }
                } else {
                    if (WarehouseStockRequestType.STOCK_OUT.equals(WarehouseStockRequestType.fromCode(cmd.getRequestType()))) {
                        LOGGER.error("warehouse stock is not enough, warehouseId = " + stock.getWarehouseId()
                                + ", materialId = " + stock.getMaterialId());
                        throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_STOCK_SHORTAGE,
                                localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                        String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_STOCK_SHORTAGE),
                                        UserContext.current().getUser().getLocale(), "warehouse stock is not enough"));
                    }
                    materialStock = new WarehouseStocks();
                    materialStock.setNamespaceId(cmd.getNamespaceId());
                    materialStock.setOwnerId(cmd.getOwnerId());
                    materialStock.setOwnerType(cmd.getOwnerType());
                    //新增园区id字段
                    materialStock.setCommunityId(cmd.getCommunityId());
                    materialStock.setWarehouseId(stock.getWarehouseId());
                    materialStock.setMaterialId(stock.getMaterialId());
                    materialStock.setAmount(stock.getAmount());
                    materialStock.setCreatorUid(uid);
                    materialStock.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    warehouseProvider.creatWarehouseStock(materialStock);
                    warehouseStockSearcher.feedDoc(materialStock);

                    WarehouseStockLogs log = ConvertHelper.convert(materialStock, WarehouseStockLogs.class);
                    log.setRequestType(cmd.getRequestType());
                    log.setDeliveryAmount(stock.getAmount());
                    log.setStockAmount(materialStock.getAmount());
                    log.setRequestSource(WarehouseStockRequestSource.MANUAL_INPUT.getCode());
                    log.setDeliveryUid(uid);
                    log.setWarehouseOrderId(finalOrder.getId());
                    warehouseProvider.creatWarehouseStockLogs(log);
                    warehouseStockLogSearcher.feedDoc(log);
                }
            });
            if(cmd.getRequestType().byteValue() == WarehouseStockRequestType.STOCK_OUT.getCode()){
                //申请下面的都出库了则申请也出库
                List<WarehouseRequestMaterials> requestMaterials = warehouseProvider.listWarehouseRequestMaterials(cmd.getRequestId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getCommunityId());
                if (requestMaterials != null || requestMaterials.size() > 0) {
                    WarehouseRequests request = warehouseProvider.findWarehouseRequests(cmd.getRequestId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getCommunityId());
                    if (request != null) {
                        request.setDeliveryFlag(DeliveryFlag.YES.getCode());
                        request.setUpdateTime(current);
                        request.setReviewResult(ReviewResult.QUALIFIED.getCode());
                        warehouseProvider.updateWarehouseRequest(request);
                    }
                }
            }
        }
    }

    @Override
    public void updateWarehouseMaterialUnit(UpdateWarehouseMaterialUnitCommand cmd) {
        //没有规定项目，如果没有指定全部权限，就不能通过
//        checkAssetPriviledgeForPropertyOrg(null,PrivilegeConstants.WAREHOUSE_PARAMETER_CONFIG,cmd.getOwnerId());
        Long uid = UserContext.current().getUser().getId();
        Integer namespaceId = cmd.getNamespaceId();

        //不带id的create，其他的看unit表中的id在不在cmd里面 不在的删掉
        List<Long> updateUnitIds = new ArrayList<Long>();
        if (cmd.getUnits() != null && cmd.getUnits().size() > 0) {
            cmd.getUnits().forEach(dto -> {
                if (dto.getId() == null) {
                    WarehouseUnits unit = ConvertHelper.convert(dto, WarehouseUnits.class);
                    unit.setCreatorUid(uid);
                    unit.setNamespaceId(namespaceId);
                    warehouseProvider.creatWarehouseUnit(unit);
                    updateUnitIds.add(unit.getId());
                } else {
                    WarehouseUnits unit = warehouseProvider.findWarehouseUnits(dto.getId(), dto.getOwnerType(), dto.getOwnerId());
                    if (unit != null) {
                        updateUnitIds.add(unit.getId());
                        if (!unit.getName().equals(dto.getName())) {
                            unit.setName(dto.getName());
                            warehouseProvider.updateWarehouseUnit(unit);
                        }
                    }
                }
            });
        }

        List<WarehouseUnits> units = warehouseProvider.listWarehouseMaterialUnits(cmd.getOwnerType(), cmd.getOwnerId());
        for (WarehouseUnits unit : units) {
            if (!updateUnitIds.contains(unit.getId())) {
                unit.setStatus(Status.INACTIVE.getCode());
                unit.setDeletorUid(uid);
                unit.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                warehouseProvider.updateWarehouseUnit(unit);
            }
        }
    }

    @Override
    public WarehouseMaterialUnitDTO findWarehouseMaterialUnit(DeleteWarehouseMaterialUnitCommand cmd) {
        WarehouseUnits unit = warehouseProvider.findWarehouseUnits(cmd.getId(), cmd.getOwnerType(), cmd.getOwnerId());
        WarehouseMaterialUnitDTO dto = ConvertHelper.convert(unit, WarehouseMaterialUnitDTO.class);
        return dto;
    }

    @Override
    public List<WarehouseMaterialUnitDTO> listWarehouseMaterialUnits(ListWarehouseMaterialUnitsCommand cmd) {
        List<WarehouseMaterialUnitDTO> dtos = warehouseProvider.listWarehouseMaterialUnits(cmd.getOwnerType(), cmd.getOwnerId()).stream().map(unit -> {
            WarehouseMaterialUnitDTO dto = ConvertHelper.convert(unit, WarehouseMaterialUnitDTO.class);
            return dto;
        }).collect(Collectors.toList());
        return dtos;
    }

    @Override
    public HttpServletResponse exportWarehouseStockLogs(SearchWarehouseStockLogsCommand cmd, HttpServletResponse response) {
        checkAssetPriviledgeForPropertyOrg(cmd.getCommunityId(), PrivilegeConstants.WAREHOUSE_REPO_MAINTAIN_LOG_EXPORT, cmd.getOwnerId());
        Integer pageSize = Integer.MAX_VALUE;
        cmd.setPageSize(pageSize);

        SearchWarehouseStockLogsResponse logs = warehouseStockLogSearcher.query(cmd);
        List<WarehouseStockLogDTO> dtos = logs.getStockLogDTOs();

        URL rootPath = WarehouseServiceImpl.class.getResource("/");
        String filePath = rootPath.getPath() + this.downloadDir;
        File file = new File(filePath);
        if (!file.exists())
            file.mkdirs();
        filePath = filePath + "WarehouseStockLogs" + System.currentTimeMillis() + ".xlsx";
        //新建了一个文件
        this.createWarehouseStockLogsBook(filePath, dtos);

        return download(filePath, response);
    }

    public void createWarehouseStockLogsBook(String path, List<WarehouseStockLogDTO> dtos) {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("WarehouseStockLogs");

        this.createWarehouseStockLogsBookSheetHead(sheet);
        for (WarehouseStockLogDTO dto : dtos) {
            this.setNewWarehouseStockLogsBookRow(sheet, dto);
        }

        try {
            FileOutputStream out = new FileOutputStream(path);

            wb.write(out);
            wb.close();
            out.close();

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE,
                    WarehouseServiceErrorCode.ERROR_CREATE_EXCEL,
                    e.getLocalizedMessage());
        }

    }

    private void createWarehouseStockLogsBookSheetHead(Sheet sheet) {

        Row row = sheet.createRow(sheet.getLastRowNum());
        int i = -1;
        row.createCell(++i).setCellValue("所属仓库");
        row.createCell(++i).setCellValue("类型");
        row.createCell(++i).setCellValue("物品编码");
        row.createCell(++i).setCellValue("物品名称");
        row.createCell(++i).setCellValue("数量");
        row.createCell(++i).setCellValue("单位");
        row.createCell(++i).setCellValue("申请人");
        row.createCell(++i).setCellValue("操作人");
        row.createCell(++i).setCellValue("操作时间");
    }

    private void setNewWarehouseStockLogsBookRow(Sheet sheet, WarehouseStockLogDTO dto) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        int i = -1;
        row.createCell(++i).setCellValue(dto.getWarehouseName());
        if (WarehouseStockRequestType.fromCode(dto.getRequestType()) != null) {
            row.createCell(++i).setCellValue(WarehouseStockRequestType.fromCode(dto.getRequestType()).getName());
        } else {
            row.createCell(++i).setCellValue("");
        }

        row.createCell(++i).setCellValue(dto.getMaterialNumber());
        row.createCell(++i).setCellValue(dto.getMaterialName());
        if (WarehouseStockRequestType.STOCK_IN.equals(WarehouseStockRequestType.fromCode(dto.getRequestType()))) {
            row.createCell(++i).setCellValue("+" + dto.getDeliveryAmount());
        } else if (WarehouseStockRequestType.STOCK_OUT.equals(WarehouseStockRequestType.fromCode(dto.getRequestType()))) {
            row.createCell(++i).setCellValue("-" + dto.getDeliveryAmount());
        } else {
            row.createCell(++i).setCellValue("");
        }

        row.createCell(++i).setCellValue(dto.getUnitName());
        row.createCell(++i).setCellValue(dto.getRequestUserName());
        row.createCell(++i).setCellValue(dto.getDeliveryUserName());
        LocalDateTime localDateTime = LocalDateTime.ofInstant(dto.getCreateTime().toInstant(), ZoneId.systemDefault());
        String format = localDateTime.format(dfDate);
        row.createCell(++i).setCellValue(format);

    }

    public HttpServletResponse download(String path, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();

            // 读取完成删除文件
            if (file.isFile() && file.exists()) {
                file.delete();
            }
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE,
                    WarehouseServiceErrorCode.ERROR_DOWNLOAD_EXCEL,
                    ex.getLocalizedMessage());

        }
        return response;
    }

//    @Override
//    public ImportDataResponse importWarehouseMaterialCategories(ImportOwnerCommand cmd, MultipartFile mfile, Long userId) {
//        ImportDataResponse importDataResponse = importData(cmd, mfile, userId, ImportDataType.WAREHOUSE_MATERIAL_CATEGORIES.getCode());
//        return importDataResponse;
//    }
//
//    @Override
//    public ImportDataResponse importWarehouseMaterials(ImportOwnerCommand cmd, MultipartFile mfile, Long userId) {
//        ImportDataResponse importDataResponse = importData(cmd, mfile, userId, ImportDataType.WAREHOUSE_MATERIALS.getCode());
//        return importDataResponse;
//    }

    @Override
    public ImportFileTaskDTO importWarehouseMaterialCategories(ImportOwnerCommand cmd, MultipartFile mfile, Long userId) {
        //不到community
//        checkAssetPriviledgeForPropertyOrg(null, PrivilegeConstants.WAREHOUSE_MATERIAL_CATEGORY_ALL,cmd.getOwnerId());
        ImportFileTask task = new ImportFileTask();
        try {
            //解析excel
            List resultList = PropMrgOwnerHandler.processorExcel(mfile.getInputStream());

            if (null == resultList || resultList.isEmpty()) {
                LOGGER.error("File content is empty。userId=" + userId);
                throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_FILE_CONTEXT_ISNULL,
                        localeStringService.getLocalizedString(String.valueOf(UserServiceErrorCode.SCOPE),
                                String.valueOf(UserServiceErrorCode.ERROR_FILE_CONTEXT_ISNULL),
                                UserContext.current().getUser().getLocale(), "File content is empty"));
            }
            task.setOwnerType(EntityType.ORGANIZATIONS.getCode());
            task.setOwnerId(cmd.getOwnerId());
            task.setType(ImportFileTaskType.WAREHOUSE_MATERIAL_CATEGORY.getCode());
            task.setCreatorUid(userId);
            task = importFileService.executeTask(new ExecuteImportTaskCallback() {
                @Override
                public ImportFileResponse importFile() {
                    ImportFileResponse response = new ImportFileResponse();
                    List<ImportWarehouseMaterialCategoryDataDTO> datas = handleImportWarehouseMaterialCategoriesData(resultList);
                    if (datas.size() > 0) {
                        //设置导出报错的结果excel的标题
                        response.setTitle(datas.get(0));
                        datas.remove(0);
                    }

                    List<ImportFileResultLog<ImportWarehouseMaterialCategoryDataDTO>> results = importWarehouseMaterialCategoriesData(cmd, datas, userId);
                    response.setTotalCount((long) datas.size());
                    response.setFailCount((long) results.size());
                    response.setLogs(results);
                    return response;
                }
            }, task);

        } catch (IOException e) {
            LOGGER.error("File can not be resolved...");
            e.printStackTrace();
        }
        return ConvertHelper.convert(task, ImportFileTaskDTO.class);
    }

    @Override
    public ImportFileTaskDTO importWarehouseMaterials(ImportOwnerCommand cmd, MultipartFile mfile, Long userId) {
        checkAssetPriviledgeForPropertyOrg(cmd.getCommunityId(), PrivilegeConstants.WAREHOUSE_MATERIAL_INFO_ALL, cmd.getOwnerId());
        ImportFileTask task = new ImportFileTask();
        try {
            //解析excel
            List resultList = PropMrgOwnerHandler.processorExcel(mfile.getInputStream());

            if (null == resultList || resultList.isEmpty()) {
                LOGGER.error("File content is empty。userId=" + userId);
                throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_FILE_CONTEXT_ISNULL,
                        localeStringService.getLocalizedString(String.valueOf(UserServiceErrorCode.SCOPE),
                                String.valueOf(UserServiceErrorCode.ERROR_FILE_CONTEXT_ISNULL),
                                UserContext.current().getUser().getLocale(), "File content is empty"));
            }
            task.setOwnerType(EntityType.ORGANIZATIONS.getCode());
            task.setOwnerId(cmd.getOwnerId());
            task.setType(ImportFileTaskType.WAREHOUSE_MATERIAL.getCode());
            task.setCreatorUid(userId);
            task = importFileService.executeTask(new ExecuteImportTaskCallback() {
                @Override
                public ImportFileResponse importFile() {
                    ImportFileResponse response = new ImportFileResponse();
                    List<ImportWarehouseMaterialDataDTO> datas = handleImportWarehouseMaterialsData(resultList);
                    if (datas.size() > 0) {
                        //设置导出报错的结果excel的标题
                        response.setTitle(datas.get(0));
                        datas.remove(0);
                    }
                    List<ImportFileResultLog<ImportWarehouseMaterialDataDTO>> results = importWarehouseMaterialsData(cmd, datas, userId);
                    response.setTotalCount((long) datas.size());
                    response.setFailCount((long) results.size());
                    response.setLogs(results);
                    return response;
                }
            }, task);

        } catch (IOException e) {
            LOGGER.error("File can not be resolved...");
            e.printStackTrace();
        }
        return ConvertHelper.convert(task, ImportFileTaskDTO.class);
    }

//    private ImportDataResponse importData(ImportOwnerCommand cmd, MultipartFile mfile,
//                                          Long userId, String dataType) {
//        ImportDataResponse importDataResponse = new ImportDataResponse();
//        try {
//            //解析excel
//            List resultList = PropMrgOwnerHandler.processorExcel(mfile.getInputStream());
//
//            if(null == resultList || resultList.isEmpty()){
//                LOGGER.error("File content is empty。userId="+userId);
//                throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_FILE_CONTEXT_ISNULL,
//                        "File content is empty");
//            }
//            LOGGER.debug("Start import data...,total:" + resultList.size());
//
//            List<String> errorDataLogs = null;
//            //导入数据，返回导入错误的日志数据集
//            if(StringUtils.equals(dataType, ImportDataType.WAREHOUSE_MATERIALS.getCode())) {
//                errorDataLogs = importWarehouseMaterialsData(cmd, handleImportWarehouseMaterialsData(resultList), userId);
//            }
//
//            if(StringUtils.equals(dataType, ImportDataType.WAREHOUSE_MATERIAL_CATEGORIES.getCode())) {
//                errorDataLogs = importWarehouseMaterialCategoriesData(cmd, handleImportWarehouseMaterialCategoriesData(resultList), userId);
//            }
//
//            LOGGER.debug("End import data...,fail:" + errorDataLogs.size());
//            if(null == errorDataLogs || errorDataLogs.isEmpty()){
//                LOGGER.debug("Data import all success...");
//            }else{
//                //记录导入错误日志
//                for (String log : errorDataLogs) {
//                    LOGGER.error(log);
//                }
//            }
//
//            importDataResponse.setTotalCount((long)resultList.size()-1);
//            importDataResponse.setFailCount((long)errorDataLogs.size());
//            importDataResponse.setLogs(errorDataLogs);
//        } catch (IOException e) {
//            LOGGER.error("File can not be resolved...");
//            e.printStackTrace();
//        }
//        return importDataResponse;
//    }

    private List<ImportFileResultLog<ImportWarehouseMaterialDataDTO>> importWarehouseMaterialsData(ImportOwnerCommand cmd, List<ImportWarehouseMaterialDataDTO> list, Long userId) {
        List<ImportFileResultLog<ImportWarehouseMaterialDataDTO>> errorDataLogs = new ArrayList<>();
//        Integer namespaceId = UserContext.getCurrentNamespaceId();
        //在左邻新运营后台，此接口传递了namespaceId，usercontext中的namespaceId为错误的。 2018/3/8 by vin. wang
        Integer namespaceId = cmd.getNamespaceId();

        for (ImportWarehouseMaterialDataDTO str : list) {
            ImportFileResultLog<ImportWarehouseMaterialDataDTO> log = new ImportFileResultLog<>(WarehouseServiceErrorCode.SCOPE);
            WarehouseMaterials material = new WarehouseMaterials();

            if (StringUtils.isBlank(str.getName())) {
                LOGGER.error("warehouse material name is null, data = {}", str);
                log.setData(str);
                log.setErrorLog("warehouse material name is null");
                log.setCode(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_NAME_IS_NULL);
                errorDataLogs.add(log);
                continue;
            }
            material.setName(str.getName());

            if (StringUtils.isBlank(str.getMaterialNumber())) {
                LOGGER.error("warehouse material number is null, data = {}", str);
                log.setData(str);
                log.setErrorLog("warehouse material number is null");
                log.setCode(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_NUMBER_IS_NULL);
                errorDataLogs.add(log);
                continue;
            }

            WarehouseMaterials exist = warehouseProvider.findWarehouseMaterialsByNumber(str.getMaterialNumber(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getCommunityId());
            if (exist != null) {
                LOGGER.error("materialNumber already exist, data = {}, cmd = {}", str, cmd);
                log.setData(str);
                log.setErrorLog("materialNumber already exist");
                log.setCode(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_NUMBER_ALREADY_EXIST);
                errorDataLogs.add(log);
                continue;
            }
            material.setMaterialNumber(str.getMaterialNumber());

            if (StringUtils.isBlank(str.getCategoryNumber())) {
                LOGGER.error("warehouse material category number is null, data = {}", str);
                log.setData(str);
                log.setErrorLog("warehouse material category number is null");
                log.setCode(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_NUMBER_IS_NULL);
                errorDataLogs.add(log);
                continue;
            }
            WarehouseMaterialCategories category = warehouseProvider.findWarehouseMaterialCategoriesByNumber(str.getCategoryNumber(), cmd.getOwnerType(), cmd.getOwnerId());
            if (category == null) {
                LOGGER.error("warehouse material category number cannot find category, data = {}, cmd = {}", str, cmd);
                log.setData(str);
                log.setErrorLog("warehouse material category number cannot find category");
                log.setCode(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_NUMBER);
                errorDataLogs.add(log);
                continue;
            }
            material.setCategoryPath(category.getPath());
            material.setCategoryId(category.getId());

            material.setBrand(str.getBrand());
            material.setItemNo(str.getItemNo());
            if (!StringUtils.isBlank(str.getReferencePrice())) {
                if (!isNumber(str.getReferencePrice())) {
                    LOGGER.error("warehouse material reference price is wrong, data = {}", str);
                    log.setData(str);
                    log.setErrorLog("warehouse material reference price is wrong");
                    log.setCode(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_PRICE);
                    errorDataLogs.add(log);
                    continue;
                }
                material.setReferencePrice(new BigDecimal(str.getReferencePrice()));
            }

            if (StringUtils.isBlank(str.getUnitName())) {
                LOGGER.error("warehouse material unit is null, data = {}", str);
                log.setData(str);
                log.setErrorLog("warehouse material unit is null");
                log.setCode(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_UNIT_IS_NULL);
                errorDataLogs.add(log);
                continue;
            }
            WarehouseUnits unit = warehouseProvider.findWarehouseUnitByName(str.getUnitName(), cmd.getOwnerType(), cmd.getOwnerId());
            if (unit == null) {
                LOGGER.error("warehouse material unit is null, data = {}", str);
                log.setData(str);
                log.setErrorLog("warehouse material unit is null");
                log.setCode(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_UNIT_IS_NULL);
                errorDataLogs.add(log);
                continue;
            }
            material.setUnitId(unit.getId());
            material.setSpecificationInformation(str.getSpecificationInformation());

            material.setOwnerType(cmd.getOwnerType());
            material.setOwnerId(cmd.getOwnerId());
            material.setCommunityId(cmd.getCommunityId());
            material.setNamespaceId(namespaceId);
            material.setCreatorUid(userId);
            warehouseProvider.creatWarehouseMaterials(material);
            warehouseMaterialSearcher.feedDoc(material);
        }
        return errorDataLogs;
    }

    //金额验证
    public static boolean isNumber(String str) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        java.util.regex.Matcher match = pattern.matcher(str);
        if (match.matches() == false) {
            return false;
        } else {
            return true;
        }
    }

    private List<ImportFileResultLog<ImportWarehouseMaterialCategoryDataDTO>> importWarehouseMaterialCategoriesData(ImportOwnerCommand cmd, List<ImportWarehouseMaterialCategoryDataDTO> list, Long userId) {
        List<ImportFileResultLog<ImportWarehouseMaterialCategoryDataDTO>> errorDataLogs = new ArrayList<>();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        list.forEach(str -> {
            ImportFileResultLog<ImportWarehouseMaterialCategoryDataDTO> log = new ImportFileResultLog<>(WarehouseServiceErrorCode.SCOPE);
            WarehouseMaterialCategories category = new WarehouseMaterialCategories();

            if (StringUtils.isBlank(str.getName())) {
                LOGGER.error("warehouse material category name is null, data = {}", str);
                log.setData(str);
                log.setErrorLog("warehouse material category name is null");
                log.setCode(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_NAME_IS_NULL);
                errorDataLogs.add(log);
                return;
            }
            category.setName(str.getName());

            if (StringUtils.isBlank(str.getCategoryNumber())) {
                LOGGER.error("warehouse material category number is null, data = {}", str);
                log.setData(str);
                log.setErrorLog("warehouse material category number is null");
                log.setCode(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_NUMBER_IS_NULL);
                errorDataLogs.add(log);
                return;
            }

            WarehouseMaterialCategories exist = warehouseProvider.findWarehouseMaterialCategoriesByNumber(str.getCategoryNumber(), cmd.getOwnerType(), cmd.getOwnerId());
            if (exist != null) {
                LOGGER.error("material categoty number already exist, data = {}, cmd = {}", str, cmd);
                log.setData(str);
                log.setErrorLog("material categoty number already exist");
                log.setCode(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_NUMBER_ALREADY_EXIST);
                errorDataLogs.add(log);
                return;
            }
            category.setCategoryNumber(str.getCategoryNumber());
            category.setPath("");
            if (!StringUtils.isBlank(str.getParentCategoryNumber())) {
                WarehouseMaterialCategories parent = warehouseProvider.findWarehouseMaterialCategoriesByNumber(str.getParentCategoryNumber(), cmd.getOwnerType(), cmd.getOwnerId());
                if (parent == null) {
                    LOGGER.error("material categoty parent number is not exist, data = {}, cmd = {}", str, cmd);
                    log.setData(str);
                    log.setErrorLog("material categoty parent number is not exist");
                    log.setCode(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_NOT_EXIST);
                    errorDataLogs.add(log);
                    return;
                }
                category.setParentId(parent.getId());
                category.setPath(parent.getPath());
            }


            category.setOwnerType(cmd.getOwnerType());
            category.setOwnerId(cmd.getOwnerId());
            category.setNamespaceId(namespaceId);
            category.setCreatorUid(userId);
            warehouseProvider.creatWarehouseMaterialCategories(category);
            warehouseMaterialCategorySearcher.feedDoc(category);
        });
        return errorDataLogs;
    }

    private List<ImportWarehouseMaterialDataDTO> handleImportWarehouseMaterialsData(List list) {
        List<ImportWarehouseMaterialDataDTO> result = new ArrayList<>();
        int row = 1;
        int i = 1;
        for (Object o : list) {
            if (row < 2) {
                row++;
                continue;
            }

            if (i > 9 && result.size() < 2) {
                break;
            }
            i++;

            RowResult r = (RowResult) o;
            ImportWarehouseMaterialDataDTO data = null;
            if (StringUtils.isNotBlank(r.getA())) {
                if (data == null) {
                    data = new ImportWarehouseMaterialDataDTO();
                }
                data.setName(r.getA().trim());
            }

            if (StringUtils.isNotBlank(r.getB())) {
                if (data == null) {
                    data = new ImportWarehouseMaterialDataDTO();
                }
                data.setMaterialNumber(r.getB().trim());
            }

            if (StringUtils.isNotBlank(r.getC())) {
                if (data == null) {
                    data = new ImportWarehouseMaterialDataDTO();
                }
                data.setCategoryName(r.getC().trim());
            }

            if (StringUtils.isNotBlank(r.getD())) {
                if (data == null) {
                    data = new ImportWarehouseMaterialDataDTO();
                }
                data.setCategoryNumber(r.getD().trim());
            }

            if (StringUtils.isNotBlank(r.getE())) {
                if (data == null) {
                    data = new ImportWarehouseMaterialDataDTO();
                }
                data.setBrand(r.getE().trim());
            }

            if (StringUtils.isNotBlank(r.getF())) {
                if (data == null) {
                    data = new ImportWarehouseMaterialDataDTO();
                }
                data.setItemNo(r.getF().trim());
            }

            if (StringUtils.isNotBlank(r.getG())) {
                if (data == null) {
                    data = new ImportWarehouseMaterialDataDTO();
                }
                data.setReferencePrice(r.getG().trim());
            }

            if (StringUtils.isNotBlank(r.getH())) {
                if (data == null) {
                    data = new ImportWarehouseMaterialDataDTO();
                }
                data.setUnitName(r.getH().trim());
            }

            if (StringUtils.isNotBlank(r.getI())) {
                if (data == null) {
                    data = new ImportWarehouseMaterialDataDTO();
                }
                data.setSpecificationInformation(r.getI().trim());
            }

            if (data != null) {
                result.add(data);
            }
        }
        LOGGER.info("result size : " + result.size());
        return result;
    }

    private List<ImportWarehouseMaterialCategoryDataDTO> handleImportWarehouseMaterialCategoriesData(List list) {
        List<ImportWarehouseMaterialCategoryDataDTO> result = new ArrayList<>();
        int row = 1;
        int i = 1;
        for (Object o : list) {
            if (row < 2) {
                row++;
                continue;
            }
            if (i > 10 && result.size() <= 1) {
                break;
            }
            i++;
            RowResult r = (RowResult) o;
            ImportWarehouseMaterialCategoryDataDTO data = new ImportWarehouseMaterialCategoryDataDTO();
            if (StringUtils.isNotBlank(r.getA())) {
                data.setName(r.getA().trim());
            }

            if (StringUtils.isNotBlank(r.getB())) {
                data.setCategoryNumber(r.getB().trim());
            }

            if (StringUtils.isNotBlank(r.getC())) {
                data.setParentCategoryName(r.getC().trim());
            }

            if (StringUtils.isNotBlank(r.getD())) {
                data.setParentCategoryNumber(r.getD().trim());
            }

            result.add(data);
        }
        return result;
    }

    @Override
    public void createRequest(CreateRequestCommand cmd) {
        checkAssetPriviledgeForPropertyOrg(cmd.getCommunityId(), PrivilegeConstants.WAREHOUSE_CLAIM_MANAGEMENT_APPLICATION, cmd.getOwnerId());
        //根据requestId先进行删除,request和requestMaterials,然后根据startFlow是否为1来决定直接发起审批否，给与request正确审核状态
        warehouseProvider.deleteWarehouseRequest(cmd.getRequestId());
        if (WarehouseStockRequestType.STOCK_OUT.equals(WarehouseStockRequestType.fromCode(cmd.getRequestType()))) {
            if (cmd.getStocks() != null && cmd.getStocks().size() > 0) {
                cmd.getStocks().forEach(stock -> {
                    WarehouseStocks warehouseStocks = warehouseProvider.findWarehouseStocksByWarehouseAndMaterial(stock.getWarehouseId(), stock.getMaterialId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getCommunityId());
                    if (warehouseStocks == null || warehouseStocks.getAmount().compareTo(stock.getAmount()) < 0) {
                        LOGGER.error("warehouse stock is not enough, warehouseId = " + stock.getWarehouseId()
                                + ", materialId = " + stock.getMaterialId());
                        throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_STOCK_SHORTAGE,
                                localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                        String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_STOCK_SHORTAGE),
                                        UserContext.current().getUser().getLocale(), "warehouse stock is not enough"));
                    }
                });
            }
        }
        if (cmd.getStocks() != null && cmd.getStocks().size() > 0) {
            cmd.getStocks().forEach(stock -> {
                Warehouses warehouse = warehouseProvider.findWarehouse(stock.getWarehouseId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getCommunityId());
                if (warehouse == null) {
                    LOGGER.error("warehouse is not exit, warehouseid: {} ", stock.getWarehouseId());
                    throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_STOCK_SHORTAGE,
                            localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                    String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_NOT_EXIST),
                                    UserContext.current().getUser().getLocale(), "warehouse is not exit"));

                } else if (warehouse != null && !Status.ACTIVE.equals(Status.fromCode(warehouse.getStatus()))) {
                    LOGGER.error("warehouse is not active, warehouseid: {} ", stock.getWarehouseId());
                    throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_STOCK_SHORTAGE,
                            localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                    String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_IS_NOT_ACTIVE),
                                    UserContext.current().getUser().getLocale(), "warehouse is not active"));

                }
            });
        }

        Long uid = UserContext.current().getUser().getId();
        Integer namespaceId = cmd.getNamespaceId();
        Long communityId = cmd.getCommunityId();
        dbProvider.execute((TransactionStatus status) -> {
            WarehouseRequests request = ConvertHelper.convert(cmd, WarehouseRequests.class);
            request.setNamespaceId(namespaceId);
            request.setCommunityId(communityId);
            request.setRequestUid(uid);
            request.setCreatorUid(uid);
            if(cmd.getStartFlow().byteValue() == (byte)1){
                request.setReviewResult(ReviewResult.NONE.getCode());
            }else{
                request.setReviewResult(ReviewResult.UNINITIALIZED.getCode());
            }
            request.setDeliveryFlag(DeliveryFlag.NO.getCode());
            warehouseProvider.creatWarehouseRequest(request);
            Long requestId = request.getId();
            if (cmd.getStocks() != null && cmd.getStocks().size() > 0) {
                cmd.getStocks().forEach(stock -> {
                    WarehouseRequestMaterials material = ConvertHelper.convert(stock, WarehouseRequestMaterials.class);
                    material.setNamespaceId(request.getNamespaceId());
                    material.setOwnerId(request.getOwnerId());
                    material.setOwnerType(request.getOwnerType());
                    material.setCommunityId(communityId);
                    material.setRequestId(requestId);
                    material.setRequestType(request.getRequestType());
                    material.setRequestSource(WarehouseStockRequestSource.REQUEST.getCode());
                    if(cmd.getStartFlow().byteValue() == (byte)1){
                        material.setReviewResult(ReviewResult.NONE.getCode());
                    }else{
                        material.setReviewResult(ReviewResult.UNINITIALIZED.getCode());
                    }
                    material.setDeliveryFlag(DeliveryFlag.NO.getCode());
                    warehouseProvider.creatWarehouseRequestMaterial(material);
                    warehouseRequestMaterialSearcher.feedDoc(material);
                });
            }
            if(cmd.getStartFlow().byteValue() == (byte)1){

                //新建flowcase
//                Flow flow = flowService.getEnabledFlow(namespaceId, FlowConstants.WAREHOUSE_REQUEST,
//                        FlowModuleType.NO_MODULE.getCode(), cmd.getOwnerId(), FlowOwnerType.WAREHOUSE_REQUEST.getCode());
                Flow flow = flowService.getEnabledFlow(namespaceId,EntityType.COMMUNITY.getCode(),request.getCommunityId(), FlowConstants.WAREHOUSE_REQUEST,
                        FlowModuleType.NO_MODULE.getCode(), null, null);
                if (null == flow) {
                    LOGGER.error("Enable request flow not found, moduleId={}", FlowConstants.WAREHOUSE_REQUEST);
                    throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_ENABLE_FLOW,
                            localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                    String.valueOf(WarehouseServiceErrorCode.ERROR_ENABLE_FLOW),
                                    UserContext.current().getUser().getLocale(), "Enable request flow not found."));
                }
                CreateFlowCaseCommand createFlowCaseCommand = new CreateFlowCaseCommand();
                createFlowCaseCommand.setCurrentOrganizationId(request.getOwnerId());
                createFlowCaseCommand.setTitle("领用申请");
                createFlowCaseCommand.setApplyUserId(request.getCreatorUid());
                createFlowCaseCommand.setFlowMainId(flow.getFlowMainId());
                createFlowCaseCommand.setFlowVersion(flow.getFlowVersion());
                createFlowCaseCommand.setReferId(request.getId());
                createFlowCaseCommand.setReferType(EntityType.WAREHOUSE_REQUEST.getCode());
                createFlowCaseCommand.setContent(request.getRemark());
                createFlowCaseCommand.setServiceType("领用申请");
                //这里先注释掉试试
//            createFlowCaseCommand.setProjectId(request.getOwnerId());
//            createFlowCaseCommand.setProjectType(request.getOwnerType());
                //增加园区
                createFlowCaseCommand.setProjectId(request.getCommunityId());
                createFlowCaseCommand.setProjectType("EhCommunities");

                flowService.createFlowCase(createFlowCaseCommand);
            }
            return null;
        });
    }

    @Override
    public WarehouseRequestDetailsDTO findRequest(FindRequestCommand cmd) {
        WarehouseRequestDetailsDTO dto = new WarehouseRequestDetailsDTO();
        Long requestId = cmd.getRequestId();
        if(cmd.getRequestId() == null){
            Long flowCaseId = cmd.getFlowCaseId();
            FlowCase flowCase = flowCaseProvider.getFlowCaseById(flowCaseId);
            requestId = flowCase.getReferId();
        }
        WarehouseRequests request = warehouseProvider.findWarehouseRequests(requestId, cmd.getOwnerType(), cmd.getOwnerId(), cmd.getCommunityId());
        if (request != null) {
            dto = ConvertHelper.convert(request, WarehouseRequestDetailsDTO.class);
            if (dto.getRequestUid() != null) {
                List<OrganizationMember> members = organizationProvider.listOrganizationMembers(dto.getRequestUid());
                if (members != null && members.size() > 0) {
                    dto.setRequestUserName(members.get(0).getContactName());
                    dto.setRequestUserContact(members.get(0).getContactToken());
                }
            }
            //公司的id存在了ownerId中
            if(dto.getOwnerType().equals(OwnerType.ORGANIZATION.getCode())){
                dto.setRequestOrganizationId(dto.getOwnerId());
            }

            Organization organization = organizationProvider.findOrganizationById(dto.getRequestOrganizationId());
            if (organization != null) {
                dto.setRequestOrganizationName(organization.getName());
            }

            List<WarehouseRequestMaterials> materials = warehouseProvider.listWarehouseRequestMaterials(cmd.getRequestId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getCommunityId());
            if (materials != null && materials.size() > 0) {
                String requestUserName = dto.getRequestUserName();
                List<WarehouseRequestMaterialDetailDTO> materialDetailDTOs = materials.stream().map(material -> {
                    WarehouseRequestMaterialDetailDTO materialDetailDTO = convertToDetail(material);
                    materialDetailDTO.setRequestUid(request.getRequestUid());
                    materialDetailDTO.setRequestUserName(requestUserName);
                    String supplierName = warehouseProvider.findMaterialSupplierNameByMaterialId(material.getMaterialId());
                    materialDetailDTO.setSupplierName(supplierName==null?"":supplierName);
                    return materialDetailDTO;
                }).collect(Collectors.toList());
                dto.setMaterialDetailDTOs(materialDetailDTOs);
            }
        }
        //通过请示单id获得请示单名称
        String requisitionName = requisitionService.getRequisitionNameById(dto.getRequisitionId());
        dto.setRequisitionName(requisitionName);
        return dto;
    }

    private WarehouseRequestMaterialDetailDTO convertToDetail(WarehouseRequestMaterials material) {
        WarehouseRequestMaterialDetailDTO materialDetailDTO = ConvertHelper.convert(material, WarehouseRequestMaterialDetailDTO.class);
        WarehouseMaterials warehouseMaterial = warehouseProvider.findWarehouseMaterials(material.getMaterialId(), material.getOwnerType(), material.getOwnerId(), material.getCommunityId());
        materialDetailDTO.setDeliveryAmount(material.getAmount());
        if (warehouseMaterial != null) {
            materialDetailDTO.setMaterialName(warehouseMaterial.getName());
            materialDetailDTO.setMaterialNumber(warehouseMaterial.getMaterialNumber());
            materialDetailDTO.setBrand(warehouseMaterial.getBrand());
            materialDetailDTO.setItemNo(warehouseMaterial.getItemNo());
            WarehouseMaterialCategories category = warehouseProvider.findWarehouseMaterialCategories(warehouseMaterial.getCategoryId(), material.getOwnerType(), material.getOwnerId());
            if (category != null) {
                materialDetailDTO.setCategoryName(category.getName());
            }

            Warehouses warehouse = warehouseProvider.findWarehouse(material.getWarehouseId(), material.getOwnerType(), material.getOwnerId(), material.getCommunityId());
            if (warehouse != null) {
                materialDetailDTO.setWarehouseName(warehouse.getName());
            }

            WarehouseStocks stock = warehouseProvider.findWarehouseStocksByWarehouseAndMaterial(material.getWarehouseId(), material.getMaterialId(), material.getOwnerType(), material.getOwnerId(), material.getCommunityId());
            if (stock != null) {
                materialDetailDTO.setStockAmount(stock.getAmount());
            }
        }

        return materialDetailDTO;
    }

    @Override
    public SearchRequestsResponse searchOneselfRequests(SearchOneselfRequestsCommand cmd) {
        QueryRequestCommand command = ConvertHelper.convert(cmd, QueryRequestCommand.class);
        command.setRequestUid(UserContext.current().getUser().getId());
        List<Long> ids = warehouseRequestMaterialSearcher.query(command);
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0l;
        if (cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }
        SearchRequestsResponse response = getWarehouseRequestMaterials(ids, cmd.getOwnerType(), cmd.getOwnerId(), pageSize, anchor, cmd.getCommunityId());
        return response;
    }

    @Override
    public SearchRequestsResponse searchRequests(SearchRequestsCommand cmd) {
        checkAssetPriviledgeForPropertyOrg(cmd.getCommunityId(), PrivilegeConstants.WAREHOUSE_CLAIM_MANAGEMENT_SEARCH, cmd.getOwnerId());
        QueryRequestCommand command = ConvertHelper.convert(cmd, QueryRequestCommand.class);
        List<Long> ids = warehouseRequestMaterialSearcher.query(command);
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0l;
        if (cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }
//        SearchRequestsResponse response = getWarehouseRequestMaterials(ids, cmd.getOwnerType(), cmd.getOwnerId(), pageSize, anchor, cmd.getCommunityId());
        //改成找一个request,同样的request去重
        SearchRequestsResponse response = getWarehouseRequests(ids, null, null, pageSize, anchor, cmd.getCommunityId());

        return response;
    }

    private SearchRequestsResponse getWarehouseRequests(List<Long> ids, String ownerType, Long ownerId, int pageSize, Long anchor, Long communityId) {
        SearchRequestsResponse response = new SearchRequestsResponse();
        List<WarehouseRequestMaterialDTO> requestDTOs = new ArrayList<>();
        if (ids.size() > pageSize) {
            response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
        } else {
            response.setNextPageAnchor(null);
        }
        List<WarehouseRequestMaterials> requestMaterials = warehouseProvider.listWarehouseRequestMaterials(ids, ownerType, ownerId, communityId);
        HashSet<Long> repeadFilter = new HashSet<>();
        for(WarehouseRequestMaterials requestMaterial : requestMaterials){
            if(repeadFilter.contains(requestMaterial.getRequestId())){
                continue;
            }
            WarehouseRequestMaterialDTO dto = ConvertHelper.convert(requestMaterial, WarehouseRequestMaterialDTO.class);
            //申请数量
            dto.setRequestAmount(requestMaterial.getAmount());
            //增加flowCaseId
            //flow case id get
            FlowCase flowcase = flowCaseProvider.findFlowCaseByReferId(requestMaterial.getRequestId()
                    , EntityType.WAREHOUSE_REQUEST.getCode(), PrivilegeConstants.WAREHOUSE_MODULE_ID);
            if(flowcase!=null){
                dto.setFlowCaseId(flowcase.getId());
            }
            // 找到物品
            WarehouseMaterials warehouseMaterial = warehouseProvider.findWarehouseMaterials(requestMaterial.getMaterialId(), requestMaterial.getOwnerType(), requestMaterial.getOwnerId(), requestMaterial.getCommunityId());
            if (warehouseMaterial != null) {
                dto.setMaterialName(warehouseMaterial.getName());
                dto.setMaterialNumber(warehouseMaterial.getMaterialNumber());
            }
            // 找到仓库
            Warehouses warehouse = warehouseProvider.findWarehouse(requestMaterial.getWarehouseId(), requestMaterial.getOwnerType(), requestMaterial.getOwnerId(), requestMaterial.getCommunityId());
            if (warehouse != null) {
                dto.setWarehouseName(warehouse.getName());
            }
            //找到库存
            WarehouseStocks stock = warehouseProvider.findWarehouseStocksByWarehouseAndMaterial(requestMaterial.getWarehouseId(), requestMaterial.getMaterialId(), requestMaterial.getOwnerType(), requestMaterial.getOwnerId(), requestMaterial.getCommunityId());
            if (stock != null) {
                dto.setStockAmount(stock.getAmount());
            }
            // 找到领用
            WarehouseRequests request = warehouseProvider.findWarehouseRequests(requestMaterial.getRequestId(), requestMaterial.getOwnerType(), requestMaterial.getOwnerId(), requestMaterial.getCommunityId());
            if (request != null) {
                dto.setRequestId(request.getId());
                dto.setRequestUid(request.getRequestUid());
                dto.setCreateTime(request.getCreateTime());
                if(request.getDeliveryFlag().byteValue() == DeliveryFlag.YES.getCode()){
                    dto.setDeliveryTime(request.getUpdateTime());
                }
                if (dto.getRequestUid() != null) {
                    List<OrganizationMember> members = organizationProvider.listOrganizationMembers(dto.getRequestUid());
                    if (members != null && members.size() > 0) {
                        dto.setRequestUserName(members.get(0).getContactName());
                    }
                }
                //领用人
                User user = userProvider.findUserById(request.getRequestUid());
    			if(user != null) {
    				dto.setRequestUserName(user.getNickName());
    			}
            }
            //返回requisitionId
            dto.setRequisitionId(warehouseProvider.findRequisitionId(requestMaterial.getRequestId()));
            repeadFilter.add(requestMaterial.getRequestId());
            requestDTOs.add(dto);
        }
        response.setRequestDTOs(requestDTOs);
        return response;
    }

    @Override
    public void createOrUpdateWarehouseEntryOrder(CreateOrUpdateWarehouseEntryOrderCommand cmd) {
        Long id = cmd.getId();
        WarehouseOrder order = null;
        boolean insert = true;
        if (id == null) {
            //新增
            order = new WarehouseOrder();
            long nextSequence = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWarehouseOrders.class));
            order.setId(nextSequence);
            order.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            //此时就是执行时间吗

            order.setCreateUid(UserContext.currentUserId());
            order.setOwnerType(cmd.getOwnerType());
            order.setOwnerId(cmd.getOwnerId());
            order.setNamespaceId(cmd.getNamespaceId());
            order.setIdentity(SupplierHelper.getIdentity());
            order.setExecutorId(UserContext.currentUserId());
            order.setServiceType(cmd.getServiceType());
            order.setCommunityId(cmd.getCommunityId());
            User userById = userProvider.findUserById(UserContext.currentUserId());
            order.setExecutorName(userById.getNickName());
            order.setExecutorTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        } else {
            insert = false;
            //更新
            order = warehouseProvider.findWarehouseOrderById(id);
            order.setUpdateUid(UserContext.currentUserId());
            order.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        }
        if (insert) {
            warehouseProvider.insertWarehouseOrder(order);
        } else {
            warehouseProvider.updateWarehouseOrder(order);
        }
        //干掉所有的物品，重新添加 vs 行修改/添加； first strategy is now applied for convenience
        warehouseProvider.deleteWarehouseStockLogs(order.getId());
        List<EhWarehouseStockLogs> list = new ArrayList<>();
        for (CreateWarehouseEntryOrderDTO dto : cmd.getDtos()) {
            WarehouseStockLogs stockLog = new WarehouseStockLogs();
            stockLog.setWarehouseOrderId(order.getId());
            stockLog.setNamespaceId(cmd.getNamespaceId());
            stockLog.setOwnerId(cmd.getOwnerId());
            stockLog.setOwnerType(cmd.getOwnerType());
            stockLog.setRequestUid(UserContext.currentUserId());
            stockLog.setCommunityId(cmd.getOwnerId());
            stockLog.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            stockLog.setDeliveryAmount(dto.getQuantity());
//            stockLog.setDeliveryUid(UserContext.currentUserId());
            stockLog.setId(this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWarehouseStockLogs.class)));
            stockLog.setMaterialId(dto.getMaterialId());
            stockLog.setWarehouseId(dto.getWarehouseId());
            stockLog.setRequestUid(UserContext.currentUserId());
            list.add(stockLog);

            warehouseProvider.insertWarehouseStockLogs(list);
            //更改物品的库存
            //寻找库存
            boolean stockExists = warehouseProvider.checkStockExists(dto.getWarehouseId(),dto.getMaterialId());
            WarehouseStocks stock = null;
            if(stockExists){
                //如果库存已经有了，则增加库存
                warehouseProvider.updateWarehouseStockByPurchase(dto.getWarehouseId(),dto.getMaterialId(),dto.getQuantity());
            }else{
                //如果没有，则新增库存
                stock = new WarehouseStocks();
                stock.setAmount(dto.getQuantity());
                stock.setCommunityId(order.getCommunityId());
                stock.setCreateTime(DateUtils.currentTimestamp());
                stock.setCreatorUid(order.getCreateUid());
                stock.setId(this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(
                        EhWarehouseStocks.class
                )));
                stock.setMaterialId(dto.getMaterialId());
                stock.setNamespaceId(order.getNamespaceId());
                stock.setOwnerId(order.getOwnerId());
                stock.setOwnerType(order.getOwnerType());
                stock.setStatus(Status.ACTIVE.getCode());
                stock.setWarehouseId(dto.getWarehouseId());
                warehouseProvider.insertWarehouseStock(stock);
            }
        }

        // end


    }

    @Override
        public ListWarehouseStockOrdersResponse listWarehouseStockOrders(ListWarehouseStockOrdersCommand cmd) {
            ListWarehouseStockOrdersResponse response = new ListWarehouseStockOrdersResponse();
            Long pageAnchor = cmd.getPageAnchor();
            Integer pageSize = cmd.getPageSize();
            if (pageAnchor == null) pageAnchor = 0l;
            if (pageSize == null) pageSize = 20;
            List<WarehouseStockOrderDTO> dtos = warehouseProvider.listWarehouseStockOrders(cmd.getExecutor(), cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getServiceType(), pageAnchor, pageSize + 1,cmd.getCommunityId() );
            if (dtos.size() > pageSize) {
                dtos.remove(dtos.size() - 1);
            response.setNextPageAnchor(pageAnchor + pageSize);
        }
        response.setDtos(dtos);
        return response;
    }

    @Override
    public void deleteWarehouseStockOrder(Long id) {
         warehouseProvider.deleteWarehouseOrderById(id);
    }

    @Override
    public void deleteRequest(DeleteRequestCommand cmd) {
        warehouseProvider.deleteWarehouseRequest(cmd.getRequestId());
    }

    @Override
    public ListMaterialLogsBySupplierResponse listMaterialLogsBySupplier(ListMaterialLogsBySupplierCommand cmd) {
        ListMaterialLogsBySupplierResponse response = new ListMaterialLogsBySupplierResponse();
        Long pageAnchor = cmd.getPageAnchor();
        Integer pageSize = cmd.getPageSize();
        if(pageAnchor == null) pageAnchor = 0l;
        if(pageSize == null) pageSize = 20;
        List<WarehouseLogDTO> dtos = warehouseProvider.listMaterialLogsBySupplier(cmd.getSupplierId(),pageAnchor,++ pageSize);
        if(dtos.size() > pageSize){
            response.setNextPageAnchor(pageAnchor + pageSize);
            dtos.remove(dtos.size() - 1);
        }
        response.setDtos(dtos);
        return response;
    }

    private SearchRequestsResponse getWarehouseRequestMaterials(List<Long> ids, String ownerType, Long ownerId, Integer pageSize, Long anchor, Long communityId) {
        SearchRequestsResponse response = new SearchRequestsResponse();
        if (ids.size() > pageSize) {
            response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
        } else {
            response.setNextPageAnchor(null);
        }
        List<WarehouseRequestMaterials> requestMaterials = warehouseProvider.listWarehouseRequestMaterials(ids, ownerType, ownerId, communityId);
        if (requestMaterials != null && requestMaterials.size() > 0) {
            HashSet<Long> repeatedFilter = new HashSet<>();
            List<WarehouseRequestMaterialDTO> requestDTOs = new ArrayList<>();
            requestMaterials.stream().forEach(requestMaterial -> {
                if(repeatedFilter.contains(requestMaterial.getRequestId())){
                    // move to next iteration, return from lambda expression
                    return;
                }else{
                    repeatedFilter.add(requestMaterial.getRequestId());
                }
                WarehouseRequestMaterialDTO dto = ConvertHelper.convert(requestMaterial, WarehouseRequestMaterialDTO.class);
                dto.setRequestAmount(requestMaterial.getAmount());
                //增加flowCaseId
                //flow case id get
                FlowCase flowcase = flowCaseProvider.findFlowCaseByReferId(requestMaterial.getRequestId()
                        , EntityType.WAREHOUSE_REQUEST.getCode(), PrivilegeConstants.WAREHOUSE_MODULE_ID);
                if(flowcase!=null){
                    dto.setFlowCaseId(flowcase.getId());
                }
                //返回requisitionId
                dto.setRequisitionId(warehouseProvider.findRequisitionId(requestMaterial.getRequestId()));
                // 找到物品
                WarehouseMaterials warehouseMaterial = warehouseProvider.findWarehouseMaterials(requestMaterial.getMaterialId(), requestMaterial.getOwnerType(), requestMaterial.getOwnerId(), requestMaterial.getCommunityId());
                if (warehouseMaterial != null) {
                    dto.setMaterialName(warehouseMaterial.getName());
                    dto.setMaterialNumber(warehouseMaterial.getMaterialNumber());
                }
                // 找到仓库
                Warehouses warehouse = warehouseProvider.findWarehouse(requestMaterial.getWarehouseId(), requestMaterial.getOwnerType(), requestMaterial.getOwnerId(), requestMaterial.getCommunityId());
                if (warehouse != null) {
                    dto.setWarehouseName(warehouse.getName());
                }
                //找到库存
                WarehouseStocks stock = warehouseProvider.findWarehouseStocksByWarehouseAndMaterial(requestMaterial.getWarehouseId(), requestMaterial.getMaterialId(), requestMaterial.getOwnerType(), requestMaterial.getOwnerId(), requestMaterial.getCommunityId());
                if (stock != null) {
                    dto.setStockAmount(stock.getAmount());
                }
                // 找到领用
                WarehouseRequests request = warehouseProvider.findWarehouseRequests(requestMaterial.getRequestId(), requestMaterial.getOwnerType(), requestMaterial.getOwnerId(), requestMaterial.getCommunityId());
                if (request != null) {
                    dto.setRequestUid(request.getRequestUid());
                    dto.setCreateTime(request.getCreateTime());
                    if(request.getDeliveryFlag().byteValue() == DeliveryFlag.YES.getCode()){
                        dto.setDeliveryTime(request.getUpdateTime());
                    }
                    if (dto.getRequestUid() != null) {
                        List<OrganizationMember> members = organizationProvider.listOrganizationMembers(dto.getRequestUid());
                        if (members != null && members.size() > 0) {
                            dto.setRequestUserName(members.get(0).getContactName());
                        }
                    }
                    //领用人
                    User user = userProvider.findUserById(request.getRequestUid());
        			if(user != null) {
        				dto.setRequestUserName(user.getNickName());
        			}
                }
                requestDTOs.add(dto);
            });
            response.setRequestDTOs(requestDTOs);
        }

        return response;

    }

    private void checkAssetPriviledgeForPropertyOrg(Long communityId, Long priviledgeId, Long OrganizationId) {
        ListServiceModuleAppsCommand cmd1 = new ListServiceModuleAppsCommand();
        cmd1.setActionType((byte) 13);
        cmd1.setModuleId(PrivilegeConstants.WAREHOUSE_MODULE_ID);
        cmd1.setNamespaceId(UserContext.getCurrentNamespaceId());
        ListServiceModuleAppsResponse res = portalService.listServiceModuleAppsWithConditon(cmd1);
        Long appId = res.getServiceModuleApps().get(0).getOriginId();
        if (!userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), EntityType.ORGANIZATIONS.getCode(), OrganizationId, OrganizationId, priviledgeId, appId, null, communityId)) {
            throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_CHECK_APP_PRIVILEGE,
                    "check app privilege error");
        }
    }

	@Override
	public void exportStocksByCommunityId(SearchWarehouseStocksCommand cmd, HttpServletResponse response) {
		cmd.setPageSize(10000);
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
        if (community == null) {
            LOGGER.error("Community is not exist.");
            throw errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST,
                    "Community is not exist.");
        }

        SearchWarehouseStocksResponse stocks = warehouseStockSearcher.query(cmd);
        List<WarehouseStockDTO> stockDTOs = stocks.getStockDTOs();

        if (stockDTOs != null && stockDTOs.size() > 0) {
			String fileName = String.format("库存信息_%s", community.getName(), com.everhomes.sms.DateUtil.dateToStr(new Date(), com.everhomes.sms.DateUtil.NO_SLASH));
			ExcelUtils excelUtils = new ExcelUtils(response, fileName, "库存信息");
			List<WarehouseStockExportDetailDTO> data = stockDTOs.stream().map(this::convertToExportDetail).collect(Collectors.toList());
			/*excelUtils.setNeedTitleRemark(true).setTitleRemark("填写注意事项：（未按照如下要求填写，会导致数据不能正常导入）\n" +
                    "1、请不要修改此表格的格式，包括插入删除行和列、合并拆分单元格等。需要填写的单元格有字段规则校验，请按照要求输入。\n" +
                    "2、请在表格里面逐行录入数据，若连续10行内容为空，录入数据不再被识别。建议一次最多导入400条信息。\n" +
                    "3、请不要随意复制单元格，这样会破坏字段规则校验。\n" +
                    "4、带有星号（*）的红色字段为必填项。\n" +
                    "5、请注意：\n" +
                    "		1）填写的物品信息与物品分类需要匹配，若不匹配会导致导入不成功。\n" +
                    "		2）一次导入物品编码不可重复，如果导入时一个编码导入多条，数量则会累加。\n" +
                    "		3）必填信息如果不导入，则该条数据导入失败。\n" +
                    "		4）物品编号与名称、分类编码、所属仓库等需要为系统中存在的，如果在系统不存在，则该条信息导入失败。\n" +
                    "		5）导入操作为刷新物品库存而非增加，导入后会新增一条入库记录。（限制导入的库存数必须大于等于现有库存，否则导入不成功）\n" +
                    "\n", (short) 13, (short) 2500).setNeedSequenceColumn(false).setIsCellStylePureString(true);*/
			String[] propertyNames = {"materialNumber", "materialName", "categoryName", "amount", "unitName", "supplierName", "warehouseName", "updateTime"};
			String[] titleNames = {"物品编号", "物品名称", "物品分类", "库存", "单位", "供应商", "仓库名称", "更新时间"};
			int[] titleSizes = {20, 20, 20, 20, 20, 20, 20, 20};
			excelUtils.writeExcel(propertyNames, titleNames, titleSizes, data);
		} else {
			throw errorWith(WarehouseServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_NO_DATA,
					"no data");
		}
	}

	private WarehouseStockExportDetailDTO convertToExportDetail(WarehouseStockDTO dto) {
		WarehouseStockExportDetailDTO exportDetailDTO = ConvertHelper.convert(dto, WarehouseStockExportDetailDTO.class);
		return exportDetailDTO;
	}

	@Override
	public ImportFileTaskDTO importStocksData(ImportStocksCommand cmd, MultipartFile file) {
		Long userId = UserContext.current().getUser().getId();
		ImportFileTask task = new ImportFileTask();
		try {
			//解析excel
			List resultList = PropMrgOwnerHandler.processorExcel(file.getInputStream());
			if(null == resultList || resultList.isEmpty()){
				LOGGER.error("File content is empty。userId="+userId);
				throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_FILE_IS_EMPTY,
						"File content is empty");
			}
			task.setOwnerType(EntityType.COMMUNITY.getCode());
			task.setOwnerId(cmd.getCommunityId());
			task.setType(ImportFileTaskType.WAREHOUSESTOCK.getCode());
			task.setCreatorUid(userId);
			task = importFileService.executeTask(() -> {
					ImportFileResponse response = new ImportFileResponse();
					List<ImportStocksDataDTO> datas = handleImportStocksData(resultList);
					if(datas.size() > 0){
						//设置导出报错的结果excel的标题
						response.setTitle(datas.get(0));
						datas.remove(0);
					}
					List<ImportFileResultLog<ImportStocksDataDTO>> results = importStocksData(datas, userId, cmd);
					response.setTotalCount((long)datas.size());
					response.setFailCount((long)results.size());
					response.setLogs(results);
					return response;
			}, task);

		} catch (IOException e) {
			LOGGER.error("File can not be resolved...");
			e.printStackTrace();
		}
		return ConvertHelper.convert(task, ImportFileTaskDTO.class);
	}

	private List<ImportFileResultLog<ImportStocksDataDTO>> importStocksData(List<ImportStocksDataDTO> datas,
			Long userId, ImportStocksCommand cmd) {
		OrganizationDTO org = this.organizationService.getUserCurrentOrganization();
		List<OrganizationMember> orgMem = this.organizationProvider.listOrganizationMembersByOrgId(org.getId());
		Map<String, OrganizationMember> ct = new HashMap<String, OrganizationMember>();
		if(orgMem != null) {
			orgMem.stream().map(r -> {
				ct.put(r.getContactToken(), r);
				return null;
			});
		}
		List<ImportFileResultLog<ImportStocksDataDTO>> list = new ArrayList<>();
		for (ImportStocksDataDTO data : datas) {
			ImportFileResultLog<ImportStocksDataDTO> log = checkData(data, cmd);
			if (log != null) {
				list.add(log);
				continue;
			}
			try {
				UpdateWarehouseStockCommand updateWarehouseStock = ConvertHelper.convert(cmd, UpdateWarehouseStockCommand.class);
				WarehouseMaterialStock stocks = new WarehouseMaterialStock();
				stocks.setWarehouseId(data.getWarehouse().getId());
				stocks.setMaterialId(data.getMaterial().getId());
				stocks.setAmount(Long.valueOf(data.getAmount()));
				List<WarehouseMaterialStock> stocksList = new ArrayList<WarehouseMaterialStock>();
				stocksList.add(stocks);
				updateWarehouseStock.setStocks(stocksList);
				updateWarehouseStock.setRequestType((byte) 0);
				updateWarehouseStock.setServiceType((byte) 4);

				warehouseService.updateWarehouseStock(updateWarehouseStock);
			} catch (NumberFormatException e) {
				LOGGER.error("ImportStocks error...");
				e.printStackTrace();
			}
		}
		return list;
	}

	private ImportFileResultLog<ImportStocksDataDTO> checkData(ImportStocksDataDTO data, ImportStocksCommand cmd) {
		ImportFileResultLog<ImportStocksDataDTO> log = new ImportFileResultLog<>(WarehouseServiceErrorCode.SCOPE);

		if (StringUtils.isEmpty(data.getMaterialNumber())) {
			log.setCode(WarehouseServiceErrorCode.ERROR_MATERIALNUMBE_EMPTY);
			log.setData(data);
			log.setErrorLog("MaterialNumbe cannot be empty");
			return log;
		}

		if (StringUtils.isEmpty(data.getAmount())) {
			log.setCode(WarehouseServiceErrorCode.ERROR_AMOUNT_EMPTY);
			log.setData(data);
			log.setErrorLog("Amount cannot be empty");
			return log;
		}

		if (StringUtils.isEmpty(data.getWarehouseName())) {
			log.setCode(WarehouseServiceErrorCode.ERROR_WAREHOUSENAME_EMPTY);
			log.setData(data);
			log.setErrorLog("WarehouseName cannot be empty");
			return log;
		}
		//正则校验数字
		if (StringUtils.isNotEmpty(data.getAmount())) {
			String reg = "^\\+?[1-9][0-9]*$";
			if(!Pattern.compile(reg).matcher(data.getAmount()).find()){
				log.setCode(WarehouseServiceErrorCode.ERROR_AMOUNT_FORMAT);
				log.setData(data);
				log.setErrorLog("Amount format is error");
				return log;
			}
		}
		//校验物品信息是否存在
		if (StringUtils.isNotEmpty(data.getMaterialNumber())) {
			//查询物品
			SearchWarehouseMaterialsCommand searchWarehouseMaterialsDTO = ConvertHelper.convert(cmd, SearchWarehouseMaterialsCommand.class);
			searchWarehouseMaterialsDTO.setMaterialNumber(data.getMaterialNumber());
			searchWarehouseMaterialsDTO.setNamespaceId((long)cmd.getNamespaceId());
			SearchWarehouseMaterialsResponse materialsResponse = warehouseMaterialSearcher.query(searchWarehouseMaterialsDTO);
			List<WarehouseMaterialDTO> materials = materialsResponse.getMaterialDTOs();
			if (materials.size()<1) {
				log.setCode(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_NOT_EXIST);
				log.setData(data);
				log.setErrorLog("Material not exist");
				return log;
			}
			if (StringUtils.isNotEmpty(data.getMaterialName())) {
				if (!data.getMaterialName().equals(materials.get(0).getName())) {
					log.setCode(WarehouseServiceErrorCode.ERROR_WAREHOUSE_NUMBERANDNAME_NOT_MATCH);
					log.setData(data);
					log.setErrorLog("Number and Name not match");
					return log;
				}
			}
			if (StringUtils.isNotEmpty(data.getCategoryName())) {
				if (!data.getCategoryName().equals(materials.get(0).getCategoryName())) {
					log.setCode(WarehouseServiceErrorCode.ERROR_WAREHOUSE_CATEGORYNAME_NOT_MATCH);
					log.setData(data);
					log.setErrorLog("CategoryName and Name not match");
					return log;
				}
			}

			data.setMaterial(materials.get(0));
		}
		//校验仓库是否存在
		if (StringUtils.isNotEmpty(data.getWarehouseName())) {
			//查询仓库
			SearchWarehousesCommand searchWarehousesDTO = ConvertHelper.convert(cmd, SearchWarehousesCommand.class);
			searchWarehousesDTO.setName(data.getWarehouseName());
			SearchWarehousesResponse warehousesResponse = warehouseSearcher.query(searchWarehousesDTO);
			List<WarehouseDTO> warehouses = warehousesResponse.getWarehouseDTOs();
			if (warehouses.size()<1) {
				data.setMaterial(null);
				log.setCode(WarehouseServiceErrorCode.ERROR_WAREHOUSE_NOT_EXIST);
				log.setData(data);
				log.setErrorLog("WarehouseName not exist");
				return log;
			}
			data.setWarehouse(warehouses.get(0));
		}

		return null;
	}

	private List<ImportStocksDataDTO> handleImportStocksData(List resultList) {
		List<ImportStocksDataDTO> list = new ArrayList<>();
		for(int i = 1; i < resultList.size(); i++) {
			RowResult r = (RowResult) resultList.get(i);
			if (StringUtils.isNotBlank(r.getA()) || StringUtils.isNotBlank(r.getB()) || StringUtils.isNotBlank(r.getC()) || StringUtils.isNotBlank(r.getD()) ||
					StringUtils.isNotBlank(r.getE())) {
				ImportStocksDataDTO data = new ImportStocksDataDTO();
				data.setMaterialNumber(trim(r.getA()));
				data.setMaterialName(trim(r.getB()));
				data.setCategoryName(trim(r.getC()));
				data.setAmount(trim(r.getD()));
				data.setWarehouseName(trim(r.getE()));
				//data.setSupplierName(trim(r.getF()));
				list.add(data);
			}
		}
		return list;
	}

	private String trim(String string) {
		if (string != null) {
			return string.trim();
		}
		return "";
	}
}
