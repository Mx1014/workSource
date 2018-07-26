-- DROP TABLE IF EXISTS `eh_express_payee_accounts`;
CREATE TABLE `eh_express_payee_accounts` (
  `id` bigint NOT NULL,
  `namespace_id` int NOT NULL,
  `owner_type` varchar(32) NOT NULL COMMENT 'community 园区或者其他类型',
  `owner_id` bigint NOT NULL COMMENT '园区id或者其他id',
  `payee_id` bigint NOT NULL COMMENT '支付帐号id',
  `payee_user_type` varchar(128) NOT NULL COMMENT '帐号类型，1-个人帐号、2-企业帐号',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '0: inactive, 2: active',
  `creator_uid` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '快递收款账户表';

ALTER TABLE `eh_express_orders`	ADD COLUMN `flow_case_id` BIGINT COMMENT '工作流id 国贸快递专用';
ALTER TABLE `eh_express_orders`	ADD COLUMN `express_type` TINYINT COMMENT '0:物品 1:文件 2:其他  国贸快递专用';
ALTER TABLE `eh_express_orders`	ADD COLUMN `express_way` TINYINT COMMENT '0:陆运 1:空运  国贸快递专用';
ALTER TABLE `eh_express_orders`	ADD COLUMN `express_target` TINYINT COMMENT '0:同城 1:外埠  国贸快递专用';
ALTER TABLE `eh_express_orders`	ADD COLUMN `express_remark` TEXT COMMENT '备注 国贸快递专用';


