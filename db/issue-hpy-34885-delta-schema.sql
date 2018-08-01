-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 黄鹏宇 20180801
-- REMARK:修复客户下拉框字段类型
ALTER TABLE `eh_enterprise_customers` MODIFY COLUMN `buy_or_lease_item_id` LONG;
ALTER TABLE `eh_enterprise_customers` MODIFY COLUMN `financing_demand_item_id` LONG;
ALTER TABLE `eh_enterprise_customers` MODIFY COLUMN `financing_demand_item_id` LONG;
ALTER TABLE `eh_enterprise_customers` MODIFY COLUMN `drop_box1` LONG;
ALTER TABLE `eh_enterprise_customers` MODIFY COLUMN `drop_box2` LONG;
ALTER TABLE `eh_enterprise_customers` MODIFY COLUMN `drop_box3` LONG;
ALTER TABLE `eh_enterprise_customers` MODIFY COLUMN `drop_box4` LONG;
ALTER TABLE `eh_enterprise_customers` MODIFY COLUMN `drop_box5` LONG;
ALTER TABLE `eh_enterprise_customers` MODIFY COLUMN `drop_box6` LONG;
ALTER TABLE `eh_enterprise_customers` MODIFY COLUMN `drop_box7` LONG;
ALTER TABLE `eh_enterprise_customers` MODIFY COLUMN `drop_box8` LONG;
ALTER TABLE `eh_enterprise_customers` MODIFY COLUMN `drop_box9` LONG;
-- end
-- --------------------- SECTION END ---------------------------------------------------------