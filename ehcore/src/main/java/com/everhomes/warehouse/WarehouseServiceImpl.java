package com.everhomes.warehouse;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.*;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.organization.*;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.organization.ImportFileResultLog;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.organization.ImportFileTaskStatus;
import com.everhomes.rest.organization.ImportFileTaskType;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.user.admin.ImportDataResponse;
import com.everhomes.rest.warehouse.*;
import com.everhomes.search.*;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
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
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ying.xiong on 2017/5/12.
 */
@Component
public class WarehouseServiceImpl implements WarehouseService {
    final String downloadDir ="\\download\\";
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

    @Override
    public WarehouseDTO updateWarehouse(UpdateWarehouseCommand cmd) {
        Warehouses warehouse = ConvertHelper.convert(cmd, Warehouses.class);
        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_WAREHOUSE.getCode()
                +cmd.getOwnerType()+cmd.getOwnerId()).enter(()-> {
            checkWarehouseNumber(warehouse.getId(), warehouse.getWarehouseNumber(), warehouse.getOwnerType(), warehouse.getOwnerId());
            if (cmd.getId() == null) {
                warehouse.setNamespaceId(UserContext.getCurrentNamespaceId());
                warehouse.setCreatorUid(UserContext.current().getUser().getId());
                warehouseProvider.creatWarehouse(warehouse);
            } else {
                Warehouses exist = verifyWarehouses(warehouse.getId(), warehouse.getOwnerType(), warehouse.getOwnerId());
                warehouse.setNamespaceId(exist.getNamespaceId());
                warehouse.setCreatorUid(exist.getCreatorUid());
                warehouse.setCreateTime(exist.getCreateTime());
                warehouseProvider.updateWarehouse(warehouse);
            }

            warehouseSearcher.feedDoc(warehouse);
            return null;
        });
        WarehouseDTO dto = ConvertHelper.convert(warehouse, WarehouseDTO.class);
        return dto;
    }

    private void checkWarehouseNumber(Long warehouseId, String warehouseNumber, String ownerType, Long ownerId) {
        Warehouses warehouse = warehouseProvider.findWarehouseByNumber(warehouseNumber, ownerType, ownerId);
        if(warehouseId == null) {
            if(warehouse != null) {
                LOGGER.error("warehouseNumber already exist, warehouseNumber = " + warehouseNumber
                        + ", ownerType = " + ownerType + ", ownerId = " + ownerId);
                throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_NUMBER_ALREADY_EXIST,
                        localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_NUMBER_ALREADY_EXIST),
                                UserContext.current().getUser().getLocale(),"warehouseNumber already exist"));

            }
        } else {
            if(warehouse != null && !warehouse.getId().equals(warehouseId)) {
                LOGGER.error("warehouseNumber already exist, warehouseNumber = " + warehouseNumber
                        + ", ownerType = " + ownerType + ", ownerId = " + ownerId);
                throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_NUMBER_ALREADY_EXIST,
                        localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_NUMBER_ALREADY_EXIST),
                                UserContext.current().getUser().getLocale(),"warehouseNumber already exist"));
            }
        }
    }

    private Warehouses verifyWarehouses(Long warehouseId, String ownerType, Long ownerId) {
        Warehouses warehouse = warehouseProvider.findWarehouse(warehouseId, ownerType, ownerId);
        if(warehouse == null) {
            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE,
                    WarehouseServiceErrorCode.ERROR_WAREHOUSE_NOT_EXIST,
                    localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                            String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_NOT_EXIST),
                            UserContext.current().getUser().getLocale(),"仓库不存在"));
        }

        return warehouse;
    }

    @Override
    public void deleteWarehouse(DeleteWarehouseCommand cmd) {
        Warehouses warehouse = verifyWarehouses(cmd.getWarehouseId(), cmd.getOwnerType(), cmd.getOwnerId());

        //库存不为0时不能删除
        Long amount = warehouseProvider.getWarehouseStockAmount(cmd.getWarehouseId(), cmd.getOwnerType(), cmd.getOwnerId());
        if(amount != null && amount > 0) {
            LOGGER.error("warehouse stock is not null, warehouseId = " + cmd.getWarehouseId()
                    + ", ownerType = " + cmd.getOwnerType() + ", ownerId = " + cmd.getOwnerId()
                    + ", count stocks = " + amount);
            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_STOCK_NOT_NULL,
                    localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                            String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_STOCK_NOT_NULL),
                            UserContext.current().getUser().getLocale(),"仓库库存不为0"));

        }
        warehouse.setStatus(WarehouseStatus.INACTIVE.getCode());
        warehouse.setDeleteUid(UserContext.current().getUser().getId());
        warehouse.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        warehouseProvider.updateWarehouse(warehouse);
        warehouseSearcher.deleteById((warehouse.getId()));

    }

    @Override
    public WarehouseDTO findWarehouse(DeleteWarehouseCommand cmd) {
        Warehouses warehouse = verifyWarehouses(cmd.getWarehouseId(), cmd.getOwnerType(), cmd.getOwnerId());
        WarehouseDTO dto = ConvertHelper.convert(warehouse, WarehouseDTO.class);
        return dto;

    }

    private String generateCategoryNumber() {

        String num = "category_" + RandomUtils.nextInt(100000000);

        return num;
    }

    @Override
    public WarehouseMaterialCategoryDTO updateWarehouseMaterialCategory(UpdateWarehouseMaterialCategoryCommand cmd) {
        if(StringUtils.isBlank(cmd.getName())){
            LOGGER.error("warehouse material category name is null, data = {}", cmd);
            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_NAME_IS_NULL,
                    localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                            String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_NAME_IS_NULL),
                            UserContext.current().getUser().getLocale(),"warehouse material category name is null"));
        }
        WarehouseMaterialCategories category = ConvertHelper.convert(cmd, WarehouseMaterialCategories.class);
        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_WAREHOUSE_CATEGORY.getCode()
                +cmd.getOwnerType()+cmd.getOwnerId()).enter(()-> {
            if (cmd.getId() == null) {
                category.setNamespaceId(UserContext.getCurrentNamespaceId());
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
        WarehouseMaterialCategories category = warehouseProvider.findWarehouseMaterialCategoriesByNumber(categoryNumber, ownerType, ownerId);
        if(categoryId == null) {
            if(category != null) {
                LOGGER.error("categoryNumber already exist, categoryNumber = " + categoryNumber
                        + ", ownerType = " + ownerType + ", ownerId = " + ownerId);
                throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_NUMBER_ALREADY_EXIST,
                        localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_NUMBER_ALREADY_EXIST),
                                UserContext.current().getUser().getLocale(),"categoryNumber already exist"));
            }
        } else {
            if(category != null && !category.getId().equals(categoryId)) {
                LOGGER.error("categoryNumber already exist, categoryNumber = " + categoryNumber
                        + ", ownerType = " + ownerType + ", ownerId = " + ownerId);
                throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_NUMBER_ALREADY_EXIST,
                        localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_NUMBER_ALREADY_EXIST),
                                UserContext.current().getUser().getLocale(),"categoryNumber already exist"));
            }
        }
    }

    private WarehouseMaterialCategories verifyWarehouseMaterialCategories(Long categoryId, String ownerType, Long ownerId) {
        WarehouseMaterialCategories category = warehouseProvider.findWarehouseMaterialCategories(categoryId, ownerType, ownerId);
        if(category == null) {
            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE,
                    WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_NOT_EXIST,
                    localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                            String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_NOT_EXIST),
                            UserContext.current().getUser().getLocale(),"物品分类不存在"));
        }

        return category;
    }

    @Override
    public void deleteWarehouseMaterialCategory(DeleteWarehouseMaterialCategoryCommand cmd) {
        WarehouseMaterialCategories category = verifyWarehouseMaterialCategories(cmd.getCategoryId(), cmd.getOwnerType(), cmd.getOwnerId());

        //该分类下有关联任何物品时，该分类不可删除
        List<WarehouseMaterials> materials = warehouseProvider.listWarehouseMaterialsByCategory(category.getPath(), category.getOwnerType(), category.getOwnerId());
        if(materials != null && materials.size() > 0) {
            LOGGER.error("the category has attach to active material, categoryId = " + cmd.getCategoryId()
                    + ", ownerType = " + cmd.getOwnerType() + ", ownerId = " + cmd.getOwnerId());
            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE,
                    WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_IN_USE,
                    localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                            String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_IN_USE),
                            UserContext.current().getUser().getLocale(),"物品分类有关联物品"));
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
        if(dto.getParentId() != null) {
            WarehouseMaterialCategories parent = warehouseProvider.findWarehouseMaterialCategories(dto.getParentId(), dto.getOwnerType(), dto.getOwnerId());
            if(parent != null) {
                dto.setParentCategoryNumber(parent.getCategoryNumber());
                dto.setParentCategoryName(parent.getName());
            }
        }
        return dto;
    }

    @Override
    public WarehouseMaterialCategoryDTO listWarehouseMaterialCategory(DeleteWarehouseMaterialCategoryCommand cmd) {
        WarehouseMaterialCategories category = new WarehouseMaterialCategories();
        if(cmd.getCategoryId() == null || cmd.getCategoryId() == 0L) {
            category.setId(0L);
            category.setName("全部");
            category.setPath("");
        } else {
            category = verifyWarehouseMaterialCategories(cmd.getCategoryId(), cmd.getOwnerType(), cmd.getOwnerId());
        }
        WarehouseMaterialCategoryDTO dto = ConvertHelper.convert(category, WarehouseMaterialCategoryDTO.class);

        List<WarehouseMaterialCategories> children = warehouseProvider.listAllChildWarehouseMaterialCategories(category.getPath() + "/%", cmd.getOwnerType(), cmd.getOwnerId());
        if(children != null && children.size() > 0) {
            List<WarehouseMaterialCategoryDTO> childrenDto = children.stream().map(child -> {
                WarehouseMaterialCategoryDTO childDto = ConvertHelper.convert(child, WarehouseMaterialCategoryDTO.class);
                return childDto;
            }).collect(Collectors.toList());
            dto = this.processWarehouseMaterialCategoryTree(childrenDto, dto);
        }
        return dto;
    }

    private WarehouseMaterialCategoryDTO processWarehouseMaterialCategoryTree(List<WarehouseMaterialCategoryDTO> dtos, WarehouseMaterialCategoryDTO dto){

        List<WarehouseMaterialCategoryDTO> trees = new ArrayList<WarehouseMaterialCategoryDTO>();
        for (WarehouseMaterialCategoryDTO treeDTO : dtos) {
            if(treeDTO.getParentId().equals(dto.getId())){
                WarehouseMaterialCategoryDTO categoryTreeDTO= processWarehouseMaterialCategoryTree(dtos, treeDTO);
                trees.add(categoryTreeDTO);
            }
        }

        dto.setChildrens(trees);
        return dto;
    }

    @Override
    public WarehouseMaterialDTO updateWarehouseMaterial(UpdateWarehouseMaterialCommand cmd) {
        WarehouseMaterials material = ConvertHelper.convert(cmd, WarehouseMaterials.class);

        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_WAREHOUSE_MATERIAL.getCode()
                +cmd.getOwnerType()+cmd.getOwnerId()).enter(()-> {
            checkMaterialNumber(material.getId(), material.getMaterialNumber(), material.getOwnerType(), material.getOwnerId());
            if (cmd.getId() == null) {
                material.setNamespaceId(UserContext.getCurrentNamespaceId());
                material.setCreatorUid(UserContext.current().getUser().getId());
                WarehouseMaterialCategories category = warehouseProvider.findWarehouseMaterialCategories(material.getCategoryId(), material.getOwnerType(), material.getOwnerId());
                if (category != null) {
                    material.setCategoryPath(category.getPath());
                }
                warehouseProvider.creatWarehouseMaterials(material);
            } else {
                WarehouseMaterials exist = verifyWarehouseMaterials(material.getId(), material.getOwnerType(), material.getOwnerId());
                material.setNamespaceId(exist.getNamespaceId());
                material.setCreatorUid(exist.getCreatorUid());
                material.setCreateTime(exist.getCreateTime());
                WarehouseMaterialCategories category = warehouseProvider.findWarehouseMaterialCategories(material.getCategoryId(), material.getOwnerType(), material.getOwnerId());
                if (category != null) {
                    material.setCategoryPath(category.getPath());
                }
                warehouseProvider.updateWarehouseMaterials(material);
            }

            warehouseMaterialSearcher.feedDoc(material);
            return null;
        });
        WarehouseMaterialDTO dto = ConvertHelper.convert(material, WarehouseMaterialDTO.class);
        WarehouseUnits unit = warehouseProvider.findWarehouseUnits(dto.getUnitId(), dto.getOwnerType(), dto.getOwnerId());
        if(unit != null) {
            dto.setUnitName(unit.getName());
        }
        WarehouseMaterialCategories category = warehouseProvider.findWarehouseMaterialCategories(dto.getCategoryId(), dto.getOwnerType(), dto.getOwnerId());
        if(category != null) {
            dto.setCategoryName(category.getName());
        }

        return dto;
    }

    private void checkMaterialNumber(Long materialId, String materialNumber, String ownerType, Long ownerId) {
        WarehouseMaterials material = warehouseProvider.findWarehouseMaterialsByNumber(materialNumber, ownerType, ownerId);
        if(materialId == null) {
            if(material != null) {
                LOGGER.error("materialNumber already exist, materialNumber = " + materialNumber
                        + ", ownerType = " + ownerType + ", ownerId = " + ownerId);
                throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_NUMBER_ALREADY_EXIST,
                        localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_NUMBER_ALREADY_EXIST),
                                UserContext.current().getUser().getLocale(),"materialNumber already exist"));
            }
        } else {
            if(material != null && !material.getId().equals(materialId)) {
                LOGGER.error("materialNumber already exist, materialNumber = " + materialNumber
                        + ", ownerType = " + ownerType + ", ownerId = " + ownerId);
                throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_NUMBER_ALREADY_EXIST,
                        localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_NUMBER_ALREADY_EXIST),
                                UserContext.current().getUser().getLocale(),"materialNumber already exist"));
            }
        }
    }

    private WarehouseMaterials verifyWarehouseMaterials(Long matetial, String ownerType, Long ownerId) {
        WarehouseMaterials material = warehouseProvider.findWarehouseMaterials(matetial, ownerType, ownerId);
        if(material == null) {
            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE,
                    WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_NOT_EXIST,
                    localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                            String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_NOT_EXIST),
                            UserContext.current().getUser().getLocale(),"物品不存在"));
        }

        return material;
    }

    @Override
    public void deleteWarehouseMaterial(DeleteWarehouseMaterialCommand cmd) {
        WarehouseMaterials material = verifyWarehouseMaterials(cmd.getMaterialId(), cmd.getOwnerType(), cmd.getOwnerId());

        //物品有库存引用时，该物品不可删除
        Long amount = warehouseProvider.getWarehouseStockAmountByMaterialId(cmd.getMaterialId(), cmd.getOwnerType(), cmd.getOwnerId());
        if(amount != null && amount > 0) {
            LOGGER.error("the material is related to warehouse, materialId = " + cmd.getMaterialId()
                    + ", ownerType = " + cmd.getOwnerType() + ", ownerId = " + cmd.getOwnerId()
                    + ", amount = " + amount);
            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE,
                    WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_RELATED_TO_WAREHOUSE,
                    localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                            String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_RELATED_TO_WAREHOUSE),
                            UserContext.current().getUser().getLocale(),"物品有库存引用"));
        }
        material.setStatus(Status.INACTIVE.getCode());
        material.setDeleteUid(UserContext.current().getUser().getId());
        material.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        warehouseProvider.updateWarehouseMaterials(material);
        warehouseMaterialSearcher.deleteById(material.getId());

        List<WarehouseStocks> stocks = warehouseProvider.listMaterialStocks(material.getId(), cmd.getOwnerType(), cmd.getOwnerId());
        if(stocks != null && stocks.size() > 0) {
            for (WarehouseStocks stock : stocks) {
                stock.setStatus(Status.INACTIVE.getCode());
                warehouseProvider.updateWarehouseStock(stock);
                warehouseStockSearcher.feedDoc(stock);
            }
        }
    }

    @Override
    public WarehouseMaterialDTO findWarehouseMaterial(DeleteWarehouseMaterialCommand cmd) {
        WarehouseMaterials material = verifyWarehouseMaterials(cmd.getMaterialId(), cmd.getOwnerType(), cmd.getOwnerId());
        WarehouseMaterialDTO dto = ConvertHelper.convert(material, WarehouseMaterialDTO.class);

        WarehouseMaterialCategories category = warehouseProvider.findWarehouseMaterialCategories(dto.getCategoryId(), dto.getOwnerType(), dto.getOwnerId());
        if(category != null) {
            dto.setCategoryName(category.getName());
        }

        WarehouseUnits unit = warehouseProvider.findWarehouseUnits(dto.getUnitId(), dto.getOwnerType(), dto.getOwnerId());
        if(unit != null) {
            dto.setUnitName(unit.getName());
        }
        return dto;
    }

    @Override
    public void updateWarehouseStock(UpdateWarehouseStockCommand cmd) {
        if(cmd.getStocks() != null && cmd.getStocks().size() > 0) {
            cmd.getStocks().forEach(stock -> {
                if(stock.getAmount() <= 0) {
                    LOGGER.error("warehouse stock change amount should larger than 0, stock: {} ", stock);
                    throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_STOCK_SHORTAGE,
                            localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                    String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_REQUEST_MATERIAL_SHOULD_LARGER_THAN_ZERO),
                                    UserContext.current().getUser().getLocale(),"warehouse stock change amount should larger than 0"));
                }

                Warehouses warehouse = warehouseProvider.findWarehouse(stock.getWarehouseId(), cmd.getOwnerType(), cmd.getOwnerId());
                if(warehouse == null) {
                    LOGGER.error("warehouse is not exit, warehouseid: {} ", stock.getWarehouseId());
                    throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_STOCK_SHORTAGE,
                            localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                    String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_NOT_EXIST),
                                    UserContext.current().getUser().getLocale(),"warehouse is not exit"));

                }
                else if(warehouse != null && !Status.ACTIVE.equals(Status.fromCode(warehouse.getStatus()))) {
                    LOGGER.error("warehouse is not active, warehouseid: {} ", stock.getWarehouseId());
                    throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_STOCK_SHORTAGE,
                            localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                    String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_IS_NOT_ACTIVE),
                                    UserContext.current().getUser().getLocale(),"warehouse is not active"));

                }
            });
            Long uid = UserContext.current().getUser().getId();
            Timestamp current = new Timestamp(DateHelper.currentGMTTime().getTime());
            cmd.getStocks().forEach(stock -> {
                WarehouseStocks materialStock = warehouseProvider.findWarehouseStocksByWarehouseAndMaterial(
                        stock.getWarehouseId(), stock.getMaterialId(), cmd.getOwnerType(), cmd.getOwnerId());

                if(materialStock != null) {
                    if(WarehouseStockRequestType.STOCK_IN.equals(WarehouseStockRequestType.fromCode(cmd.getRequestType()))) {
                        materialStock.setAmount(materialStock.getAmount() + stock.getAmount());
                        warehouseProvider.updateWarehouseStock(materialStock);
                        warehouseStockSearcher.feedDoc(materialStock);

                        WarehouseStockLogs log = ConvertHelper.convert(materialStock, WarehouseStockLogs.class);
                        log.setRequestType(cmd.getRequestType());
                        log.setDeliveryAmount(stock.getAmount());
                        log.setStockAmount(materialStock.getAmount());
                        log.setRequestSource(WarehouseStockRequestSource.MANUAL_INPUT.getCode());
                        log.setDeliveryUid(uid);
                        warehouseProvider.creatWarehouseStockLogs(log);
                        warehouseStockLogSearcher.feedDoc(log);
                    } else if(WarehouseStockRequestType.STOCK_OUT.equals(WarehouseStockRequestType.fromCode(cmd.getRequestType()))) {
                        if(materialStock.getAmount().compareTo(stock.getAmount()) < 0) {
                            LOGGER.error("warehouse stock is not enough, warehouseId = " + stock.getWarehouseId()
                                    + ", materialId = " + stock.getMaterialId());
                            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_STOCK_SHORTAGE,
                                    localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                            String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_STOCK_SHORTAGE),
                                            UserContext.current().getUser().getLocale(),"warehouse stock is not enough"));
                        }

                        WarehouseRequestMaterials requestMaterial = warehouseProvider.findWarehouseRequestMaterials(cmd.getRequestId(), stock.getWarehouseId(), stock.getMaterialId());
                        if(requestMaterial == null) {
                            LOGGER.error("WarehouseRequestMaterials is not exist, requestId = " + cmd.getRequestId() +", warehouseId = " + stock.getWarehouseId()
                                    + ", materialId = " + stock.getMaterialId());
                            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_REQUEST_MATERIAL_IS_NOT_EXIST,
                                    localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                            String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_REQUEST_MATERIAL_IS_NOT_EXIST),
                                            UserContext.current().getUser().getLocale(),"WarehouseRequestMaterials is not exist"));
                        }

                        if(!ReviewResult.QUALIFIED.equals(ReviewResult.fromStatus(requestMaterial.getReviewResult()))) {
                            LOGGER.error("WarehouseRequestMaterials is not qualified, requestId = " + cmd.getRequestId() +", warehouseId = " + stock.getWarehouseId()
                                    + ", materialId = " + stock.getMaterialId());
                            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_REQUEST_MATERIAL_IS_NOT_QUALIFIED,
                                    localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                            String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_REQUEST_MATERIAL_IS_NOT_QUALIFIED),
                                            UserContext.current().getUser().getLocale(),"WarehouseRequestMaterials is not qualified"));
                        }

                        if(DeliveryFlag.YES.equals(DeliveryFlag.fromStatus(requestMaterial.getDeliveryFlag()))) {
                            LOGGER.error("WarehouseRequestMaterials is already delivery, requestId = " + cmd.getRequestId() +", warehouseId = " + stock.getWarehouseId()
                                    + ", materialId = " + stock.getMaterialId());
                            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_REQUEST_MATERIAL_IS_ALREADY_DELIVERY,
                                    localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                            String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_REQUEST_MATERIAL_IS_ALREADY_DELIVERY),
                                            UserContext.current().getUser().getLocale(),"WarehouseRequestMaterials is already delivery"));
                        }

                        requestMaterial.setDeliveryFlag(DeliveryFlag.YES.getCode());
                        requestMaterial.setDeliveryUid(uid);
                        requestMaterial.setDeliveryTime(current);
                        warehouseProvider.updateWarehouseRequestMaterial(requestMaterial);
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
                        warehouseProvider.creatWarehouseStockLogs(log);
                        warehouseStockLogSearcher.feedDoc(log);
                    }
                } else {
                    if(WarehouseStockRequestType.STOCK_OUT.equals(WarehouseStockRequestType.fromCode(cmd.getRequestType()))) {
                        LOGGER.error("warehouse stock is not enough, warehouseId = " + stock.getWarehouseId()
                                + ", materialId = " + stock.getMaterialId());
                        throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_STOCK_SHORTAGE,
                                localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                        String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_STOCK_SHORTAGE),
                                        UserContext.current().getUser().getLocale(),"warehouse stock is not enough"));
                    }
                    materialStock = new WarehouseStocks();
                    materialStock.setNamespaceId(UserContext.getCurrentNamespaceId());
                    materialStock.setOwnerId(cmd.getOwnerId());
                    materialStock.setOwnerType(cmd.getOwnerType());
                    materialStock.setWarehouseId(stock.getWarehouseId());
                    materialStock.setMaterialId(stock.getMaterialId());
                    materialStock.setAmount(stock.getAmount());
                    materialStock.setCreatorUid(uid);
                    warehouseProvider.creatWarehouseStock(materialStock);
                    warehouseStockSearcher.feedDoc(materialStock);

                    WarehouseStockLogs log = ConvertHelper.convert(materialStock, WarehouseStockLogs.class);
                    log.setRequestType(cmd.getRequestType());
                    log.setDeliveryAmount(stock.getAmount());
                    log.setStockAmount(materialStock.getAmount());
                    log.setRequestSource(WarehouseStockRequestSource.MANUAL_INPUT.getCode());
                    log.setDeliveryUid(uid);
                    warehouseProvider.creatWarehouseStockLogs(log);
                    warehouseStockLogSearcher.feedDoc(log);
                }
            });

            //申请下面的都出库了则申请也出库
            List<WarehouseRequestMaterials> requestMaterials = warehouseProvider.listWarehouseRequestMaterials(cmd.getRequestId(), cmd.getOwnerType(), cmd.getOwnerId());
            if(requestMaterials != null || requestMaterials.size() > 0) {
                WarehouseRequests request = warehouseProvider.findWarehouseRequests(cmd.getRequestId(), cmd.getOwnerType(), cmd.getOwnerId());
                if(request != null) {
                    request.setDeliveryFlag(DeliveryFlag.YES.getCode());
                    request.setUpdateTime(current);
                    warehouseProvider.updateWarehouseRequest(request);
                }
            }
        }
    }

    @Override
    public void updateWarehouseMaterialUnit(UpdateWarehouseMaterialUnitCommand cmd) {
        Long uid = UserContext.current().getUser().getId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        //不带id的create，其他的看unit表中的id在不在cmd里面 不在的删掉
        List<Long> updateUnitIds = new ArrayList<Long>();
        if(cmd.getUnits() != null && cmd.getUnits().size() > 0) {
            cmd.getUnits().forEach(dto -> {
                if(dto.getId() == null) {
                    WarehouseUnits unit = ConvertHelper.convert(dto, WarehouseUnits.class);
                    unit.setCreatorUid(uid);
                    unit.setNamespaceId(namespaceId);
                    warehouseProvider.creatWarehouseUnit(unit);
                    updateUnitIds.add(unit.getId());
                } else {
                    WarehouseUnits unit = warehouseProvider.findWarehouseUnits(dto.getId(), dto.getOwnerType(), dto.getOwnerId());
                    if(unit != null) {
                        updateUnitIds.add(unit.getId());
                        if(!unit.getName().equals(dto.getName())) {
                            unit.setName(dto.getName());
                            warehouseProvider.updateWarehouseUnit(unit);
                        }
                    }
                }
            });
        }

        List<WarehouseUnits> units = warehouseProvider.listWarehouseMaterialUnits(cmd.getOwnerType(), cmd.getOwnerId());
        for(WarehouseUnits unit : units) {
            if(!updateUnitIds.contains(unit.getId())) {
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
        Integer pageSize = Integer.MAX_VALUE;
        cmd.setPageSize(pageSize);

        SearchWarehouseStockLogsResponse logs = warehouseStockLogSearcher.query(cmd);
        List<WarehouseStockLogDTO> dtos = logs.getStockLogDTOs();

        URL rootPath = WarehouseServiceImpl.class.getResource("/");
        String filePath =rootPath.getPath() + this.downloadDir ;
        File file = new File(filePath);
        if(!file.exists())
            file.mkdirs();
        filePath = filePath + "WarehouseStockLogs"+System.currentTimeMillis()+".xlsx";
        //新建了一个文件
        this.createWarehouseStockLogsBook(filePath, dtos);

        return download(filePath,response);
    }

    public void createWarehouseStockLogsBook(String path,List<WarehouseStockLogDTO> dtos) {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("WarehouseStockLogs");

        this.createWarehouseStockLogsBookSheetHead(sheet);
        for (WarehouseStockLogDTO dto : dtos ) {
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

    private void createWarehouseStockLogsBookSheetHead(Sheet sheet){

        Row row = sheet.createRow(sheet.getLastRowNum());
        int i =-1 ;
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

    private void setNewWarehouseStockLogsBookRow(Sheet sheet ,WarehouseStockLogDTO dto){
        Row row = sheet.createRow(sheet.getLastRowNum()+1);
        int i = -1;
        row.createCell(++i).setCellValue(dto.getWarehouseName());
        if(WarehouseStockRequestType.fromCode(dto.getRequestType()) != null) {
            row.createCell(++i).setCellValue(WarehouseStockRequestType.fromCode(dto.getRequestType()).getName());
        } else {
            row.createCell(++i).setCellValue("");
        }

        row.createCell(++i).setCellValue(dto.getMaterialNumber());
        row.createCell(++i).setCellValue(dto.getMaterialName());
        if(WarehouseStockRequestType.STOCK_IN.equals(WarehouseStockRequestType.fromCode(dto.getRequestType()))) {
            row.createCell(++i).setCellValue("+" + dto.getDeliveryAmount());
        } else if(WarehouseStockRequestType.STOCK_OUT.equals(WarehouseStockRequestType.fromCode(dto.getRequestType()))) {
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
        ImportFileTask task = new ImportFileTask();
        try {
            //解析excel
            List resultList = PropMrgOwnerHandler.processorExcel(mfile.getInputStream());

            if(null == resultList || resultList.isEmpty()){
                LOGGER.error("File content is empty。userId="+userId);
                throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_FILE_CONTEXT_ISNULL,
                        localeStringService.getLocalizedString(String.valueOf(UserServiceErrorCode.SCOPE),
                                String.valueOf(UserServiceErrorCode.ERROR_FILE_CONTEXT_ISNULL),
                                UserContext.current().getUser().getLocale(),"File content is empty"));
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
                    if(datas.size() > 0){
                        //设置导出报错的结果excel的标题
                        response.setTitle(datas.get(0));
                        datas.remove(0);
                    }

                    List<ImportFileResultLog<ImportWarehouseMaterialCategoryDataDTO>> results = importWarehouseMaterialCategoriesData(cmd, datas, userId);
                    response.setTotalCount((long)datas.size());
                    response.setFailCount((long)results.size());
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
        ImportFileTask task = new ImportFileTask();
        try {
            //解析excel
            List resultList = PropMrgOwnerHandler.processorExcel(mfile.getInputStream());

            if(null == resultList || resultList.isEmpty()){
                LOGGER.error("File content is empty。userId="+userId);
                throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_FILE_CONTEXT_ISNULL,
                        localeStringService.getLocalizedString(String.valueOf(UserServiceErrorCode.SCOPE),
                                String.valueOf(UserServiceErrorCode.ERROR_FILE_CONTEXT_ISNULL),
                                UserContext.current().getUser().getLocale(),"File content is empty"));
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
                    if(datas.size() > 0){
                        //设置导出报错的结果excel的标题
                        response.setTitle(datas.get(0));
                        datas.remove(0);
                    }
                    List<ImportFileResultLog<ImportWarehouseMaterialDataDTO>> results = importWarehouseMaterialsData(cmd, datas, userId);
                    response.setTotalCount((long)datas.size());
                    response.setFailCount((long)results.size());
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

    private List<ImportFileResultLog<ImportWarehouseMaterialDataDTO>> importWarehouseMaterialsData(ImportOwnerCommand cmd, List<ImportWarehouseMaterialDataDTO> list, Long userId){
        List<ImportFileResultLog<ImportWarehouseMaterialDataDTO>> errorDataLogs = new ArrayList<>();
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        for (ImportWarehouseMaterialDataDTO str : list) {
            ImportFileResultLog<ImportWarehouseMaterialDataDTO> log = new ImportFileResultLog<>(WarehouseServiceErrorCode.SCOPE);
            WarehouseMaterials material = new WarehouseMaterials();

            if(StringUtils.isBlank(str.getName())){
                LOGGER.error("warehouse material name is null, data = {}", str);
                log.setData(str);
                log.setErrorLog("warehouse material name is null");
                log.setCode(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_NAME_IS_NULL);
                errorDataLogs.add(log);
                continue;
            }
            material.setName(str.getName());

            if(StringUtils.isBlank(str.getMaterialNumber())){
                LOGGER.error("warehouse material number is null, data = {}", str);
                log.setData(str);
                log.setErrorLog("warehouse material number is null");
                log.setCode(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_NUMBER_IS_NULL);
                errorDataLogs.add(log);
                continue;
            }

            WarehouseMaterials exist = warehouseProvider.findWarehouseMaterialsByNumber(str.getMaterialNumber(), cmd.getOwnerType(), cmd.getOwnerId());
            if(exist != null) {
                LOGGER.error("materialNumber already exist, data = {}, cmd = {}" , str, cmd);
                log.setData(str);
                log.setErrorLog("materialNumber already exist");
                log.setCode(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_NUMBER_ALREADY_EXIST);
                errorDataLogs.add(log);
                continue;
            }
            material.setMaterialNumber(str.getMaterialNumber());

            if(StringUtils.isBlank(str.getCategoryNumber())){
                LOGGER.error("warehouse material category number is null, data = {}", str);
                log.setData(str);
                log.setErrorLog("warehouse material category number is null");
                log.setCode(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_NUMBER_IS_NULL);
                errorDataLogs.add(log);
                continue;
            }
            WarehouseMaterialCategories category = warehouseProvider.findWarehouseMaterialCategoriesByNumber(str.getCategoryNumber(), cmd.getOwnerType(), cmd.getOwnerId());
            if(category == null) {
                LOGGER.error("warehouse material category number cannot find category, data = {}, cmd = {}" , str, cmd);
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
            if(!StringUtils.isBlank(str.getReferencePrice())) {
                if(!isNumber(str.getReferencePrice())) {
                    LOGGER.error("warehouse material reference price is wrong, data = {}", str);
                    log.setData(str);
                    log.setErrorLog("warehouse material reference price is wrong");
                    log.setCode(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_PRICE);
                    errorDataLogs.add(log);
                    continue;
                }
                material.setReferencePrice(new BigDecimal(str.getReferencePrice()));
            }

            if(StringUtils.isBlank(str.getUnitName())){
                LOGGER.error("warehouse material unit is null, data = {}", str);
                log.setData(str);
                log.setErrorLog("warehouse material unit is null");
                log.setCode(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_UNIT_IS_NULL);
                errorDataLogs.add(log);
                continue;
            }
            WarehouseUnits unit = warehouseProvider.findWarehouseUnitByName(str.getUnitName(), cmd.getOwnerType(), cmd.getOwnerId());
            if(unit == null) {
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
            material.setNamespaceId(namespaceId);
            material.setCreatorUid(userId);
            warehouseProvider.creatWarehouseMaterials(material);
            warehouseMaterialSearcher.feedDoc(material);
        }
        return errorDataLogs;
    }

    //金额验证
    public static boolean isNumber(String str)
    {
        java.util.regex.Pattern pattern=java.util.regex.Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        java.util.regex.Matcher match=pattern.matcher(str);
        if(match.matches()==false)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    private List<ImportFileResultLog<ImportWarehouseMaterialCategoryDataDTO>> importWarehouseMaterialCategoriesData(ImportOwnerCommand cmd, List<ImportWarehouseMaterialCategoryDataDTO> list, Long userId){
        List<ImportFileResultLog<ImportWarehouseMaterialCategoryDataDTO>> errorDataLogs = new ArrayList<>();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        list.forEach(str -> {
            ImportFileResultLog<ImportWarehouseMaterialCategoryDataDTO> log = new ImportFileResultLog<>(WarehouseServiceErrorCode.SCOPE);
            WarehouseMaterialCategories category = new WarehouseMaterialCategories();

            if(StringUtils.isBlank(str.getName())){
                LOGGER.error("warehouse material category name is null, data = {}", str);
                log.setData(str);
                log.setErrorLog("warehouse material category name is null");
                log.setCode(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_NAME_IS_NULL);
                errorDataLogs.add(log);
                return;
            }
            category.setName(str.getName());

            if(StringUtils.isBlank(str.getCategoryNumber())){
                LOGGER.error("warehouse material category number is null, data = {}", str);
                log.setData(str);
                log.setErrorLog("warehouse material category number is null");
                log.setCode(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_NUMBER_IS_NULL);
                errorDataLogs.add(log);
                return;
            }

            WarehouseMaterialCategories exist = warehouseProvider.findWarehouseMaterialCategoriesByNumber(str.getCategoryNumber(), cmd.getOwnerType(), cmd.getOwnerId());
            if(exist != null) {
                LOGGER.error("material categoty number already exist, data = {}, cmd = {}" , str, cmd);
                log.setData(str);
                log.setErrorLog("material categoty number already exist");
                log.setCode(WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_NUMBER_ALREADY_EXIST);
                errorDataLogs.add(log);
                return;
            }
            category.setCategoryNumber(str.getCategoryNumber());
            category.setPath("");
            if(!StringUtils.isBlank(str.getParentCategoryNumber())) {
                WarehouseMaterialCategories parent = warehouseProvider.findWarehouseMaterialCategoriesByNumber(str.getParentCategoryNumber(), cmd.getOwnerType(), cmd.getOwnerId());
                if(parent == null) {
                    LOGGER.error("material categoty parent number is not exist, data = {}, cmd = {}" , str, cmd);
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

    private List<ImportWarehouseMaterialDataDTO> handleImportWarehouseMaterialsData(List list){
        List<ImportWarehouseMaterialDataDTO> result = new ArrayList<>();
        int row = 1;
        int i = 1;
        for (Object o : list) {
            if(row < 2){
                row ++;
                continue;
            }

            if(i > 9 && result.size() < 2) {
                break;
            }
            i++;

            RowResult r = (RowResult)o;
            ImportWarehouseMaterialDataDTO data = null;
            if(StringUtils.isNotBlank(r.getA())) {
                if(data == null) {
                    data = new ImportWarehouseMaterialDataDTO();
                }
                data.setName(r.getA().trim());
            }

            if(StringUtils.isNotBlank(r.getB())) {
                if(data == null) {
                    data = new ImportWarehouseMaterialDataDTO();
                }
                data.setMaterialNumber(r.getB().trim());
            }

            if(StringUtils.isNotBlank(r.getC())) {
                if(data == null) {
                    data = new ImportWarehouseMaterialDataDTO();
                }
                data.setCategoryName(r.getC().trim());
            }

            if(StringUtils.isNotBlank(r.getD())) {
                if(data == null) {
                    data = new ImportWarehouseMaterialDataDTO();
                }
                data.setCategoryNumber(r.getD().trim());
            }

            if(StringUtils.isNotBlank(r.getE())) {
                if(data == null) {
                    data = new ImportWarehouseMaterialDataDTO();
                }
                data.setBrand(r.getE().trim());
            }

            if(StringUtils.isNotBlank(r.getF())) {
                if(data == null) {
                    data = new ImportWarehouseMaterialDataDTO();
                }
                data.setItemNo(r.getF().trim());
            }

            if(StringUtils.isNotBlank(r.getG())) {
                if(data == null) {
                    data = new ImportWarehouseMaterialDataDTO();
                }
                data.setReferencePrice(r.getG().trim());
            }

            if(StringUtils.isNotBlank(r.getH())) {
                if(data == null) {
                    data = new ImportWarehouseMaterialDataDTO();
                }
                data.setUnitName(r.getH().trim());
            }

            if(StringUtils.isNotBlank(r.getI())) {
                if(data == null) {
                    data = new ImportWarehouseMaterialDataDTO();
                }
                data.setSpecificationInformation(r.getI().trim());
            }

            if(data != null) {
                result.add(data);
            }
        }
        LOGGER.info("result size : " + result.size());
        return result;
    }

    private List<ImportWarehouseMaterialCategoryDataDTO> handleImportWarehouseMaterialCategoriesData(List list){
        List<ImportWarehouseMaterialCategoryDataDTO> result = new ArrayList<>();
        int row = 1;
        int i = 1;
        for (Object o : list) {
            if(row < 2){
                row ++;
                continue;
            }
            if(i > 10 && result.size() <= 1) {
                break;
            }
            i++;
            RowResult r = (RowResult)o;
            ImportWarehouseMaterialCategoryDataDTO data = new ImportWarehouseMaterialCategoryDataDTO();
            if(StringUtils.isNotBlank(r.getA())) {
                data.setName(r.getA().trim());
            }

            if(StringUtils.isNotBlank(r.getB())) {
                data.setCategoryNumber(r.getB().trim());
            }

            if(StringUtils.isNotBlank(r.getC())) {
                data.setParentCategoryName(r.getC().trim());
            }

            if(StringUtils.isNotBlank(r.getD())) {
                data.setParentCategoryNumber(r.getD().trim());
            }
            
            result.add(data);
        }
        return result;
    }

    @Override
    public void createRequest(CreateRequestCommand cmd) {
        if(WarehouseStockRequestType.STOCK_OUT.equals(WarehouseStockRequestType.fromCode(cmd.getRequestType()))) {
            if(cmd.getStocks() != null && cmd.getStocks().size() > 0) {
                cmd.getStocks().forEach(stock -> {
                    WarehouseStocks warehouseStocks = warehouseProvider.findWarehouseStocksByWarehouseAndMaterial(stock.getWarehouseId(), stock.getMaterialId(), cmd.getOwnerType(), cmd.getOwnerId());
                    if (warehouseStocks == null || warehouseStocks.getAmount().compareTo(stock.getAmount()) < 0) {
                        LOGGER.error("warehouse stock is not enough, warehouseId = " + stock.getWarehouseId()
                                + ", materialId = " + stock.getMaterialId());
                        throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_STOCK_SHORTAGE,
                                localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                        String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_STOCK_SHORTAGE),
                                        UserContext.current().getUser().getLocale(),"warehouse stock is not enough"));
                    }
                });
            }
        }
        if(cmd.getStocks() != null && cmd.getStocks().size() > 0) {
            cmd.getStocks().forEach(stock -> {
                Warehouses warehouse = warehouseProvider.findWarehouse(stock.getWarehouseId(), cmd.getOwnerType(), cmd.getOwnerId());
                if(warehouse == null) {
                    LOGGER.error("warehouse is not exit, warehouseid: {} ", stock.getWarehouseId());
                    throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_STOCK_SHORTAGE,
                            localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                    String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_NOT_EXIST),
                                    UserContext.current().getUser().getLocale(),"warehouse is not exit"));

                }
                else if(warehouse != null && !Status.ACTIVE.equals(Status.fromCode(warehouse.getStatus()))) {
                    LOGGER.error("warehouse is not active, warehouseid: {} ", stock.getWarehouseId());
                    throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_STOCK_SHORTAGE,
                            localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                    String.valueOf(WarehouseServiceErrorCode.ERROR_WAREHOUSE_IS_NOT_ACTIVE),
                                    UserContext.current().getUser().getLocale(),"warehouse is not active"));

                }
            });
        }

        Long uid = UserContext.current().getUser().getId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        dbProvider.execute((TransactionStatus status) -> {
            WarehouseRequests request = ConvertHelper.convert(cmd, WarehouseRequests.class);
            request.setNamespaceId(namespaceId);
            request.setRequestUid(uid);
            request.setCreatorUid(uid);
            request.setReviewResult(ReviewResult.NONE.getCode());
            request.setDeliveryFlag(DeliveryFlag.NO.getCode());
            warehouseProvider.creatWarehouseRequest(request);
            Long requestId = request.getId();
            if(cmd.getStocks() != null && cmd.getStocks().size() > 0) {
                cmd.getStocks().forEach(stock -> {
                    WarehouseRequestMaterials material = ConvertHelper.convert(stock, WarehouseRequestMaterials.class);
                    material.setNamespaceId(request.getNamespaceId());
                    material.setOwnerId(request.getOwnerId());
                    material.setOwnerType(request.getOwnerType());
                    material.setRequestId(requestId);
                    material.setRequestType(request.getRequestType());
                    material.setRequestSource(WarehouseStockRequestSource.REQUEST.getCode());
                    material.setReviewResult(ReviewResult.NONE.getCode());
                    material.setDeliveryFlag(DeliveryFlag.NO.getCode());
                    warehouseProvider.creatWarehouseRequestMaterial(material);
                    warehouseRequestMaterialSearcher.feedDoc(material);
                });
            }
            //新建flowcase
            Flow flow = flowService.getEnabledFlow(namespaceId, FlowConstants.WAREHOUSE_REQUEST,
                    FlowModuleType.NO_MODULE.getCode(), cmd.getOwnerId(), FlowOwnerType.WAREHOUSE_REQUEST.getCode());
            if(null == flow) {
                LOGGER.error("Enable request flow not found, moduleId={}", FlowConstants.WAREHOUSE_REQUEST);
                throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_ENABLE_FLOW,
                        localeStringService.getLocalizedString(String.valueOf(WarehouseServiceErrorCode.SCOPE),
                                String.valueOf(WarehouseServiceErrorCode.ERROR_ENABLE_FLOW),
                                UserContext.current().getUser().getLocale(),"Enable request flow not found."));
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

            createFlowCaseCommand.setProjectId(request.getOwnerId());
            createFlowCaseCommand.setProjectType(request.getOwnerType());

            flowService.createFlowCase(createFlowCaseCommand);
            return null;
        });
    }

    @Override
    public WarehouseRequestDetailsDTO findRequest(FindRequestCommand cmd) {
        WarehouseRequestDetailsDTO dto = new WarehouseRequestDetailsDTO();
        WarehouseRequests request = warehouseProvider.findWarehouseRequests(cmd.getRequestId(), cmd.getOwnerType(), cmd.getOwnerId());
        if(request != null) {
            dto = ConvertHelper.convert(request, WarehouseRequestDetailsDTO.class);
            if(dto.getRequestUid() != null) {
                List<OrganizationMember> members = organizationProvider.listOrganizationMembers(dto.getRequestUid());
                if(members != null && members.size() > 0) {
                    dto.setRequestUserName(members.get(0).getContactName());
                    dto.setRequestUserContact(members.get(0).getContactToken());
                }
            }


            Organization organization = organizationProvider.findOrganizationById(dto.getRequestOrganizationId());
            if(organization != null) {
                dto.setRequestOrganizationName(organization.getName());
            }

            List<WarehouseRequestMaterials> materials = warehouseProvider.listWarehouseRequestMaterials(cmd.getRequestId(), cmd.getOwnerType(), cmd.getOwnerId());
            if(materials != null && materials.size() > 0) {
                String requestUserName = dto.getRequestUserName();
                List<WarehouseRequestMaterialDetailDTO> materialDetailDTOs = materials.stream().map(material -> {
                    WarehouseRequestMaterialDetailDTO materialDetailDTO = convertToDetail(material);
                    materialDetailDTO.setRequestUid(request.getRequestUid());
                    materialDetailDTO.setRequestUserName(requestUserName);
                    return materialDetailDTO;
                }).collect(Collectors.toList());
                dto.setMaterialDetailDTOs(materialDetailDTOs);
            }
        }

        return dto;
    }

    private WarehouseRequestMaterialDetailDTO convertToDetail(WarehouseRequestMaterials material) {
        WarehouseRequestMaterialDetailDTO materialDetailDTO = ConvertHelper.convert(material, WarehouseRequestMaterialDetailDTO.class);
        WarehouseMaterials warehouseMaterial = warehouseProvider.findWarehouseMaterials(material.getMaterialId(), material.getOwnerType(), material.getOwnerId());
        materialDetailDTO.setDeliveryAmount(material.getAmount());
        if(warehouseMaterial != null) {
            materialDetailDTO.setMaterialName(warehouseMaterial.getName());
            materialDetailDTO.setMaterialNumber(warehouseMaterial.getMaterialNumber());
            materialDetailDTO.setBrand(warehouseMaterial.getBrand());
            materialDetailDTO.setItemNo(warehouseMaterial.getItemNo());
            WarehouseMaterialCategories category = warehouseProvider.findWarehouseMaterialCategories(warehouseMaterial.getCategoryId(),  material.getOwnerType(), material.getOwnerId());
            if(category != null) {
                materialDetailDTO.setCategoryName(category.getName());
            }

            Warehouses warehouse = warehouseProvider.findWarehouse(material.getWarehouseId(), material.getOwnerType(), material.getOwnerId());
            if(warehouse != null) {
                materialDetailDTO.setWarehouseName(warehouse.getName());
            }

            WarehouseStocks stock = warehouseProvider.findWarehouseStocksByWarehouseAndMaterial(material.getWarehouseId(), material.getMaterialId(), material.getOwnerType(), material.getOwnerId());
            if(stock != null) {
                materialDetailDTO.setStockAmount(stock.getAmount());
            }
        }

        return materialDetailDTO;
    }

    @Override
    public SearchRequestsResponse searchOneselfRequests(SearchOneselfRequestsCommand cmd) {
        QueryRequestCommand command = ConvertHelper.convert(cmd , QueryRequestCommand.class);
        command.setRequestUid(UserContext.current().getUser().getId());
        List<Long> ids = warehouseRequestMaterialSearcher.query(command);
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0l;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }
        SearchRequestsResponse response = getWarehouseRequestMaterials(ids, cmd.getOwnerType(), cmd.getOwnerId(), pageSize, anchor);
        return response;
    }

    @Override
    public SearchRequestsResponse searchRequests(SearchRequestsCommand cmd) {
        QueryRequestCommand command = ConvertHelper.convert(cmd , QueryRequestCommand.class);
        List<Long> ids = warehouseRequestMaterialSearcher.query(command);
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0l;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }
        SearchRequestsResponse response = getWarehouseRequestMaterials(ids, cmd.getOwnerType(), cmd.getOwnerId(), pageSize, anchor);
        return response;
    }

    private SearchRequestsResponse getWarehouseRequestMaterials(List<Long> ids, String ownerType, Long ownerId, Integer pageSize, Long anchor) {
        SearchRequestsResponse response = new SearchRequestsResponse();
        if(ids.size() > pageSize) {
            response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
        } else {
            response.setNextPageAnchor(null);
        }
        List<WarehouseRequestMaterials> requestMaterials = warehouseProvider.listWarehouseRequestMaterials(ids, ownerType, ownerId);
        if(requestMaterials != null && requestMaterials.size() > 0) {
            List<WarehouseRequestMaterialDTO> requestDTOs = requestMaterials.stream().map(requestMaterial -> {
                WarehouseRequestMaterialDTO dto = ConvertHelper.convert(requestMaterial, WarehouseRequestMaterialDTO.class);
                dto.setRequestAmount(requestMaterial.getAmount());

                WarehouseMaterials warehouseMaterial = warehouseProvider.findWarehouseMaterials(requestMaterial.getMaterialId(), requestMaterial.getOwnerType(), requestMaterial.getOwnerId());
                if(warehouseMaterial != null) {
                    dto.setMaterialName(warehouseMaterial.getName());
                    dto.setMaterialNumber(warehouseMaterial.getMaterialNumber());
                }
                Warehouses warehouse = warehouseProvider.findWarehouse(requestMaterial.getWarehouseId(), requestMaterial.getOwnerType(), requestMaterial.getOwnerId());
                if(warehouse != null) {
                    dto.setWarehouseName(warehouse.getName());
                }

                WarehouseStocks stock = warehouseProvider.findWarehouseStocksByWarehouseAndMaterial(requestMaterial.getWarehouseId(), requestMaterial.getMaterialId(), requestMaterial.getOwnerType(), requestMaterial.getOwnerId());
                if(stock != null) {
                    dto.setStockAmount(stock.getAmount());
                }

                WarehouseRequests request = warehouseProvider.findWarehouseRequests(requestMaterial.getRequestId(), requestMaterial.getOwnerType(), requestMaterial.getOwnerId());
                if(request != null) {
                    dto.setRequestUid(request.getRequestUid());
                    dto.setCreateTime(request.getCreateTime());
                    if(dto.getRequestUid() != null) {
                        List<OrganizationMember> members = organizationProvider.listOrganizationMembers(dto.getRequestUid());
                        if(members != null && members.size() > 0) {
                            dto.setRequestUserName(members.get(0).getContactName());
                        }
                    }
                }
                return dto;
            }).collect(Collectors.toList());
            response.setRequestDTOs(requestDTOs);
        }

        return response;

    }

}
