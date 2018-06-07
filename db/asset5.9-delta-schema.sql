-- 为账单组管理增加“收款方账户”字段
ALTER TABLE `eh_payment_bill_groups` ADD COLUMN `biz_payee_type` VARCHAR(128) COMMENT '收款方账户类型：EhUsers/EhOrganizations';
ALTER TABLE `eh_payment_bill_groups` ADD COLUMN `biz_payee_id` BIGINT COMMENT '收款方账户id';



-- st.zheng
CREATE TABLE `eh_rentalv2_order_records` (
`id`  bigint(20) NOT NULL ,
`namespace_id`  int NULL ,
`order_id`  bigint(20) NULL COMMENT '资源预订订单id' ,
`order_no`  bigint(20) NULL COMMENT '资源预订订单号' ,
`pay_order_id`  bigint(20) NULL COMMENT '支付系统订单号' ,
`payment_order_type`  tinyint(8) NULL COMMENT '订单类型 1续费订单 2欠费订单 3支付订单 4退款订单' ,
`status`  tinyint(8) NULL COMMENT '订单状态0未支付 1已支付' ,
`amount` decimal(16) NULL COMMENT '订单金额' ,
`create_time`  datetime  ,
`update_time`  datetime  ,
PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_rentalv2_pay_accounts` (
`id`  bigint(20) NOT NULL ,
`namespace_id`  int NULL ,
`community_id`  bigint(20) NULL ,
`resource_type`  varchar(20) NULL,
`source_type`  varchar(20) NULL COMMENT 'default_rule:默认规则 resource_rule:资源规则' ,
`source_id`  bigint(20) NULL ,
`account_id`  bigint(20) NULL ,
`create_time`  datetime  ,
PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- yanlong.liang 发起活动的企业与收款方账户映射表
CREATE TABLE `eh_activity_biz_payee` (
`id`  bigint(20) NOT NULL ,
`biz_payee_id` bigint(20) NOT NULL COMMENT '收款方账户ID',
`organization_id` bigint(20) NOT NULL COMMENT '企业ID',
PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;





