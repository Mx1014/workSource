-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.6（对接统一账单） 业务应用与缴费的关联关系表
CREATE TABLE `eh_asset_module_app_mapping_configs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `owner_id` BIGINT COMMENT '园区ID',
  `owner_type` VARCHAR(64) COMMENT '园区类型',
  `origin_id` BIGINT COMMENT '各个业务应用的originId（公共平台定义的唯一标识）',
  `asset_category_id` BIGINT COMMENT '缴费的多入口应用ID',
  `bill_group_id` BIGINT COMMENT '缴费的账单组ID',
  `bill_group_name` VARCHAR(64) COMMENT '缴费的账单组名称',
  `charging_item_id` BIGINT COMMENT '缴费的费项ID',
  `charging_item_name` VARCHAR(64) COMMENT '缴费的费项名称',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_uid` BIGINT NOT NULL,
  `update_time` DATETIME,
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='业务应用与缴费的关联关系表';


-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.6（对接统一账单） 账单要增加来源字段
ALTER TABLE `eh_payment_bills` ADD COLUMN `source` VARCHAR(1024) COMMENT '账单来源（如：停车缴费）';
ALTER TABLE `eh_payment_bills` ADD COLUMN `produce_uid` BIGINT COMMENT '企业下面的某个人的ID';