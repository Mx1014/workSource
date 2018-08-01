-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 黄鹏宇 20180731
-- REMARK:客户字段无法满足并导入，新增字段见详情
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `buy_or_lease_item_id` TINYINT null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `biz_address` varchar(1024) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `biz_life` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `customer_intention_level` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `enter_dev_goal` text null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `controller_name` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `controller_sun_birth` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `controller_lunar_birth` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `financing_demand_item_id` TINYINT null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag1` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag2` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag3` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag4` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag5` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag6` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag7` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag8` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag9` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag10` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag11` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag12` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag13` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag14` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag15` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `string_tag16` varchar(32) null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `drop_box1` TINYINT null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `drop_box2` TINYINT null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `drop_box3` TINYINT null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `drop_box4` TINYINT null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `drop_box5` TINYINT null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `drop_box6` TINYINT null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `drop_box7` TINYINT null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `drop_box8` TINYINT null default null;
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `drop_box9` TINYINT null default null;

-- end
-- --------------------- SECTION END ---------------------------------------------------------

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