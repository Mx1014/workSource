-- domain 增加name, icon
ALTER TABLE `eh_domains` ADD COLUMN `icon_uri`  varchar(255) NULL AFTER `create_time`;
ALTER TABLE `eh_domains` ADD COLUMN `name`  varchar(255) NULL AFTER `namespace_id`;

ALTER TABLE `eh_web_menu_scopes` ADD COLUMN `app_id`  bigint(20) NULL COMMENT 'eh_service_module_app id';
ALTER TABLE `eh_web_menu_scopes` DROP INDEX `u_menu_scope_owner` , ADD UNIQUE INDEX `u_menu_scope_owner` (`menu_id`, `owner_type`, `owner_id`, `app_id`) USING BTREE ;

-- 账单item关联滞纳金 by wentian
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `late_fine_standard_id` BIGINT DEFAULT NULL COMMENT '滞纳金标准id';
-- 滞纳金表 by wentian
DROP TABLE IF EXISTS `eh_payment_late_fine`;
CREATE TABLE `eh_payment_late_fine`(
  `id` BIGINT NOT NULL COMMENT 'primary key',
  `name` VARCHAR(20) COMMENT '滞纳金名称',
  `amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT 'amount of overdue payment',
  `bill_id` BIGINT NOT NULL COMMENT 'the id of the corresponding bill, one to one',
  `bill_item_id` BIGINT NOT NULL COMMENT 'the id of the corresponding bill item id, one to one',
  `create_time` DATETIME DEFAULT NOW(),
  `upate_time` DATETIME DEFAULT NOW(),
  `update_uid` BIGINT DEFAULT NULL,
  `namespace_id` INTEGER DEFAULT NULL COMMENT 'location info, for possible statistics later',
  `community_id` BIGINT DEFAULT NULL,
  `customer_id` BIGINT NOT NULL COMMENT 'allows searching taking advantage of it',
  `customer_type` VARCHAR(20) NOT NULL COMMENT 'break of user info benefits',
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4;


--  标准数据增加周期类型及关系表状态start by jiarui 20180105
UPDATE eh_equipment_inspection_standards
SET repeat_type = (SELECT repeat_type FROM eh_repeat_settings WHERE id = eh_equipment_inspection_standards.repeat_setting_id)
WHERE STATUS =2  AND repeat_type =0;

-- 上版未置状态数据修改
UPDATE eh_equipment_inspection_equipment_standard_map
SET `status` = 0
WHERE review_status IN (0, 3 ,4) OR review_result = 2;
-- 上版bug数据修改

--  标准数据增加周期类型 及关系表状态 end by jiarui 20180105
-- 巡检任务状态统一 start by jiarui 20180105
UPDATE eh_equipment_inspection_tasks
SET `status` = 6
WHERE `status` = 4 AND review_result = 2;

UPDATE eh_equipment_inspection_tasks
SET `status` = 7
WHERE `status` = 4 AND review_result = 4;

-- 巡检任务状态统一 end by jiarui 20180105