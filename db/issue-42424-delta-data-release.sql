-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等

-- --------------------- SECTION END OPERATION------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 杨崇鑫
-- REMARK: 缺陷 #42424 【智谷汇】保证金设置为固定金额，但是实际会以合同签约门牌的数量计价。实际上保证金是按照合同收费，不是按照门牌的数量进行重复计费。 给智谷汇的权限
INSERT INTO `eh_service_module_functions`(`id`, `module_id`, `privilege_id`, `explain`) VALUES (21225, 21200, 21225, '保证金一次性产生一笔费用');
-- AUTHOR: 杨崇鑫
-- REMARK: 缺陷 #42424 只是临时开给智谷汇这个域空间使用
set @id=(select ifnull((SELECT max(id) from eh_service_module_include_functions),1));
INSERT INTO `eh_service_module_include_functions`(`id`, `namespace_id`, `module_id`, `community_id`, `function_id`) VALUES (@id:= @id +1, 999945, 21200, NULL, 21225);


-- --------------------- SECTION END ALL -----------------------------------------------------