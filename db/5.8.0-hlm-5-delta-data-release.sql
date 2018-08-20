-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等
-- AUTHOR: 杨崇鑫  20180720
-- REMARK: content图片程序升级，① 从本版中的content二进制更新到正式环境中  ② 把allowOriginToGet = * 加到 config.ini 配置文件中的 system 区域下

-- AUTHOR: ryan  20180807
-- REMARK: 执行 /archives/cleanRedundantArchivesDetails 接口(可能速度有点慢，但可重复执行)

-- AUTHOR: jiarui  20180807
-- REMARK: 执行search 下脚本 enter_meter.sh
-- 执行 /energy/syncEnergyMeterIndex 接口(可能速度有点慢，但可重复执行)

-- AUTHOR: 唐岑 2018年8月17日15:13:14
-- REMARK: 1、执行接口/community/caculateAllCommunityArea（该接口计算时间非常长）
--         2、执行接口/community/caculateAllBuildingArea（该接口计算时间非常长）


-- AUTHOR: 丁建民 2018年8月17日15:13:14 (仓库管理) 更新一下es结构，并调用相关接口同步
-- REMARK: 1、更新 warehouse_material.sh warehouse.sh warehouse_stock_log.sh warehouse_stock.sh es的结构
--         2、 同步  /warehouse/syncWarehouseIndex
--         3、同步/warehouse/syncWarehouseMaterialCategoryIndex
--         4、同步/warehouse/syncWarehouseMaterialsIndex
--         5、同步/warehouse/syncWarehouseRequestMaterialIndex
--         6、同步/warehouse/syncWarehouseStockIndex
--         7、同步/warehouse/syncWarehouseStockLogIndex

-- AUTHOR: 丁建民 2018年8月17日15:13:14 (能耗管理) 更新一下es结构，并调用相关接口同步
-- REMARK: 1、更新 energy_meter.sh es上的结构
--         2、同步 /energy/syncEnergyMeterIndex

-- AUTHOR: dengs 2018年8月17日
-- REMARK: 1、备份表eh_parking_lots
--         2、调用接口/parking/initFuncLists

-- AUTHOR: 严军 2018年8月17日
-- REMARK: 1、备份表eh_service_modules 和 eh_portal_items


-- --------------------- SECTION END ---------------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 黄良铭
-- REMARK: 超级管理员维护时的消息模板
UPDATE  eh_locale_templates s SET s.text='${userName}（${contactToken}）的${organizationName}企业管理员身份已被移除。' 
WHERE s.scope='organization.notification' AND s.code=20;

SET @b_id = (SELECT IFNULL(MAX(id),1) FROM eh_locale_templates);
INSERT INTO eh_locale_templates(id ,scope ,CODE ,locale ,description ,TEXT,namespace_id)
VALUES(@b_id:= @b_id +1 , 'organization.notification',23,'zh_CN','添加超级管理员给当前超级管理员发送的消息模板' ,  '您已成为${organizationName}的超级管理员',0);

INSERT INTO eh_locale_templates(id ,scope ,CODE ,locale ,description ,TEXT,namespace_id)
VALUES(@b_id:= @b_id +1 , 'organization.notification',24,'zh_CN','删除超级管理员给其他超级管理员发送的消息模板' ,  '${userName}（${contactToken}）的${organizationName}超级管理员身份已被移除',0);

INSERT INTO eh_locale_templates(id ,scope ,CODE ,locale ,description ,TEXT,namespace_id)
VALUES(@b_id:= @b_id +1 , 'organization.notification',25,'zh_CN','添加超级管理员给其他管理员发送的消息模板' ,  '${userName}（${contactToken}）已被添加为${organizationName}的超级管理员',0);

-- --------------------- SECTION END ---------------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本


-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: dev
-- DESCRIPTION: 此SECTION只在开发库中执行的脚本
-- AUTHOR:
-- REMARK:

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zijing
-- DESCRIPTION: 此SECTION只在清华信息港(紫荆)-999984执行的脚本

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guangda
-- DESCRIPTION: 此SECTION只在光大-999979执行的脚本

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: szbay
-- DESCRIPTION: 此SECTION只在深圳湾-999966执行的脚本

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: anbang
-- DESCRIPTION: 此SECTION只在安邦物业-999949执行的脚本
-- AUTHOR: 杨崇鑫
-- REMARK: 需根据域空间配置是否显示，初始化时配置好，默认不展示

-- --------------------- SECTION END ---------------------------------------------------------


