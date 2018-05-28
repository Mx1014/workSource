-- 为账单组管理增加“收款方账户”字段
ALTER TABLE `eh_payment_bill_groups` ADD COLUMN `biz_payee_account` VARCHAR(1024) COMMENT '收款方账户';
ALTER TABLE `eh_payment_bill_groups` ADD COLUMN `biz_payee_type` VARCHAR(1024) COMMENT '收款方账户类型：EhUsers/EhOrganizations';
ALTER TABLE `eh_payment_bill_groups` ADD COLUMN `biz_payee_id` VARCHAR(1024) COMMENT '收款方账户id';


-- 陈毅峰
DROP TABLE IF EXISTS `eh_namespace_pay_mappings`;
CREATE TABLE `eh_namespace_pay_mappings` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `namespace_name` varchar(64) DEFAULT NULL,
  `app_key` varchar(64) NOT NULL COMMENT 'app key in pay server for the namespace',
  `secret_key` varchar(1024) DEFAULT NULL COMMENT 'app key in pay server for the namespace',
  `create_time` DATETIME,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_namespace_pay_mapping_namespace_id`(`namespace_id`),
  KEY `i_eh_namespace_pay_mapping_app_key`(`app_key`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
