-- AUTHOR: 丁建民  20180728
-- REMARK: 仓库管理V2.3（库存导入导出功能） issue-34335
SET @id = (SELECT MAX(id) from eh_locale_strings);
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'warehouse', '10027', 'zh_CN', '物品编号不能为空');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'warehouse', '10028', 'zh_CN', '库存不能为空');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'warehouse', '10029', 'zh_CN', '所属仓库不能为空');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'warehouse', '10030', 'zh_CN', '库存请输入大于的0整数');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'warehouse', '10031', 'zh_CN', '物品编号和名称不能匹配');
-- end

-- 更新 warehouse_material.sh warehouse.sh warehouse_stock_log.sh warehouse_stock.sh es的结构
-- 同步  /warehouse/syncWarehouseIndex
-- 同步/warehouse/syncWarehouseMaterialCategoryIndex
-- 同步/warehouse/syncWarehouseMaterialsIndex
-- 同步/warehouse/syncWarehouseRequestMaterialIndex
-- 同步/warehouse/syncWarehouseStockIndex
-- 同步/warehouse/syncWarehouseStockLogIndex