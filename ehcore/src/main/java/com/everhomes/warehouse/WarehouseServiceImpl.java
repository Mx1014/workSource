package com.everhomes.warehouse;

import com.everhomes.rest.warehouse.*;
import com.everhomes.search.WarehouseMaterialCategorySearcher;
import com.everhomes.search.WarehouseMaterialSearcher;
import com.everhomes.search.WarehouseSearcher;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ying.xiong on 2017/5/12.
 */
@Component
public class WarehouseServiceImpl implements WarehouseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseServiceImpl.class);

    @Autowired
    private WarehouseProvider warehouseProvider;

    @Autowired
    private WarehouseSearcher warehouseSearcher;

    @Autowired
    private WarehouseMaterialCategorySearcher warehouseMaterialCategorySearcher;

    @Autowired
    private WarehouseMaterialSearcher warehouseMaterialSearcher;

    @Override
    public WarehouseDTO updateWarehouse(UpdateWarehouseCommand cmd) {
        Warehouses warehouse = ConvertHelper.convert(cmd, Warehouses.class);
        checkWarehouseNumber(warehouse.getId(), warehouse.getWarehouseNumber(),warehouse.getOwnerType(),warehouse.getOwnerId());
        if(cmd.getId() == null) {
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
                        "warehouseNumber already exist");
            }
        } else {
            if(warehouse != null && !warehouse.getId().equals(warehouseId)) {
                LOGGER.error("warehouseNumber already exist, warehouseNumber = " + warehouseNumber
                        + ", ownerType = " + ownerType + ", ownerId = " + ownerId);
                throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_NUMBER_ALREADY_EXIST,
                        "warehouseNumber already exist");
            }
        }
    }

    private Warehouses verifyWarehouses(Long warehouseId, String ownerType, Long ownerId) {
        Warehouses warehouse = warehouseProvider.findWarehouse(warehouseId, ownerType, ownerId);
        if(warehouse == null) {
            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE,
                    WarehouseServiceErrorCode.ERROR_WAREHOUSE_NOT_EXIST,
                    "仓库不存在");
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
            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE,
                    WarehouseServiceErrorCode.ERROR_WAREHOUSE_STOCK_NOT_NULL,
                    "仓库库存不为0");
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
        WarehouseMaterialCategories category = ConvertHelper.convert(cmd, WarehouseMaterialCategories.class);
        if(cmd.getId() == null) {
            category.setNamespaceId(UserContext.getCurrentNamespaceId());
            category.setCreatorUid(UserContext.current().getUser().getId());
            WarehouseMaterialCategories parent = warehouseProvider.findWarehouseMaterialCategories(category.getParentId(), category.getOwnerType(), category.getOwnerId());
            if(parent != null) {
                category.setPath(parent.getPath());
            }

            if(category.getCategoryNumber() == null) {
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
            checkCategoryNumber(category.getId(), category.getCategoryNumber(),category.getOwnerType(),category.getOwnerId());
            WarehouseMaterialCategories parent = warehouseProvider.findWarehouseMaterialCategories(category.getParentId(), category.getOwnerType(), category.getOwnerId());
            if(parent != null) {
                category.setPath(parent.getPath() + "/" + category.getId());
            }
            warehouseProvider.updateWarehouseMaterialCategories(category);
        }

        warehouseMaterialCategorySearcher.feedDoc(category);
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
                        "categoryNumber already exist");
            }
        } else {
            if(category != null && !category.getId().equals(categoryId)) {
                LOGGER.error("categoryNumber already exist, categoryNumber = " + categoryNumber
                        + ", ownerType = " + ownerType + ", ownerId = " + ownerId);
                throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_NUMBER_ALREADY_EXIST,
                        "categoryNumber already exist");
            }
        }
    }

    private WarehouseMaterialCategories verifyWarehouseMaterialCategories(Long categoryId, String ownerType, Long ownerId) {
        WarehouseMaterialCategories category = warehouseProvider.findWarehouseMaterialCategories(categoryId, ownerType, ownerId);
        if(category == null) {
            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE,
                    WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_NOT_EXIST,
                    "物品分类不存在");
        }

        return category;
    }

    @Override
    public void deleteWarehouseMaterialCategory(DeleteWarehouseMaterialCategoryCommand cmd) {
        WarehouseMaterialCategories category = verifyWarehouseMaterialCategories(cmd.getCategoryId(), cmd.getOwnerType(), cmd.getOwnerId());

        //该分类下有关联任何物品时，该分类不可删除
        List<WarehouseMaterials> materials = warehouseProvider.listWarehouseMaterialsByCategory(cmd.getCategoryId(), cmd.getOwnerType(), cmd.getOwnerId());
        if(materials != null && materials.size() > 0) {
            LOGGER.error("the category has attach to active material, categoryId = " + cmd.getCategoryId()
                    + ", ownerType = " + cmd.getOwnerType() + ", ownerId = " + cmd.getOwnerId());
            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE,
                    WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_CATEGORY_IN_USE,
                    "物品分类有关联物品");
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
        } else {
            category = verifyWarehouseMaterialCategories(cmd.getCategoryId(), cmd.getOwnerType(), cmd.getOwnerId());
        }
        WarehouseMaterialCategoryDTO dto = ConvertHelper.convert(category, WarehouseMaterialCategoryDTO.class);

        List<WarehouseMaterialCategories> children = warehouseProvider.listAllChildWarehouseMaterialCategories(category.getPath() + "/%");
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
        WarehouseMaterialCategoryDTO allTreeDTO = ConvertHelper.convert(dto, WarehouseMaterialCategoryDTO.class);
        trees.add(allTreeDTO);
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
        checkMaterialNumber(material.getId(), material.getMaterialNumber(),material.getOwnerType(),material.getOwnerId());
        if(cmd.getId() == null) {
            material.setNamespaceId(UserContext.getCurrentNamespaceId());
            material.setCreatorUid(UserContext.current().getUser().getId());
            WarehouseMaterialCategories category = warehouseProvider.findWarehouseMaterialCategories(material.getCategoryId(), material.getOwnerType(), material.getOwnerId());
            if(category != null) {
                material.setCategoryPath(category.getPath());
            }
            warehouseProvider.creatWarehouseMaterials(material);
        } else {
            WarehouseMaterials exist = verifyWarehouseMaterials(material.getId(), material.getOwnerType(), material.getOwnerId());
            material.setNamespaceId(exist.getNamespaceId());
            material.setCreatorUid(exist.getCreatorUid());
            material.setCreateTime(exist.getCreateTime());
            WarehouseMaterialCategories category = warehouseProvider.findWarehouseMaterialCategories(material.getCategoryId(), material.getOwnerType(), material.getOwnerId());
            if(category != null) {
                material.setCategoryPath(category.getPath());
            }
            warehouseProvider.updateWarehouseMaterials(material);
        }

        warehouseMaterialSearcher.feedDoc(material);
        WarehouseMaterialDTO dto = ConvertHelper.convert(material, WarehouseMaterialDTO.class);
        return dto;
    }

    private void checkMaterialNumber(Long materialId, String materialNumber, String ownerType, Long ownerId) {
        WarehouseMaterials material = warehouseProvider.findWarehouseMaterialsByNumber(materialNumber, ownerType, ownerId);
        if(materialId == null) {
            if(material != null) {
                LOGGER.error("materialNumber already exist, materialNumber = " + materialNumber
                        + ", ownerType = " + ownerType + ", ownerId = " + ownerId);
                throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_NUMBER_ALREADY_EXIST,
                        "materialNumber already exist");
            }
        } else {
            if(material != null && !material.getId().equals(materialId)) {
                LOGGER.error("materialNumber already exist, materialNumber = " + materialNumber
                        + ", ownerType = " + ownerType + ", ownerId = " + ownerId);
                throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE, WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_NUMBER_ALREADY_EXIST,
                        "materialNumber already exist");
            }
        }
    }

    private WarehouseMaterials verifyWarehouseMaterials(Long matetial, String ownerType, Long ownerId) {
        WarehouseMaterials material = warehouseProvider.findWarehouseMaterials(matetial, ownerType, ownerId);
        if(material == null) {
            throw RuntimeErrorException.errorWith(WarehouseServiceErrorCode.SCOPE,
                    WarehouseServiceErrorCode.ERROR_WAREHOUSE_MATERIAL_NOT_EXIST,
                    "物品不存在");
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
                    "物品有库存引用");
        }
        material.setStatus(Status.INACTIVE.getCode());
        material.setDeleteUid(UserContext.current().getUser().getId());
        material.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        warehouseProvider.updateWarehouseMaterials(material);
        warehouseMaterialSearcher.deleteById((material.getId()));
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

    }

    @Override
    public List<WarehouseMaterialUnitDTO> listWarehouseMaterialUnits(ListWarehouseMaterialUnitsCommand cmd) {
        List<WarehouseMaterialUnitDTO> dtos = warehouseProvider.listWarehouseMaterialUnits(cmd.getOwnerType(), cmd.getOwnerId()).stream().map(unit -> {
            WarehouseMaterialUnitDTO dto = ConvertHelper.convert(unit, WarehouseMaterialUnitDTO.class);
            return dto;
        }).collect(Collectors.toList());
        return dtos;
    }
}
