-- Designer: yang cx
-- Description: 物业缴费6.2
CREATE TABLE `eh_payment_subtraction_items` (
  `id` BIGINT NOT NULL,
  `bill_id` BIGINT NOT NULL DEFAULT 0,
  `subtraction_type` VARCHAR(255) COMMENT '减免费项类型，eh_payment_bill_items：费项（如：物业费），eh_payment_late_fine：减免滞纳金（如：物业费滞纳金）',
  `subtraction_id` BIGINT COMMENT '减免费项的id，存的都是charging_item_id，因为滞纳金是跟着费项走，所以可以通过subtraction_type类型，判断是否减免费项滞纳金',
  `bill_group_id` BIGINT,
  `target_type` VARCHAR(255),
  `target_id` BIGINT,
  `targetName` VARCHAR(255) COMMENT '客户名称，客户没有在系统中时填写',
  `creator_uid` BIGINT COMMENT '创建者ID',
  `create_time` DATETIME,
  `operator_uid` BIGINT COMMENT '修改者ID',
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='减免费项配置表';

-- End by: yang cx
