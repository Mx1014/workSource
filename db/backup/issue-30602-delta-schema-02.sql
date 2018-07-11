-- 通用脚本  
-- ADD BY 杨崇鑫
-- 为账单组管理增加“收款方账户”字段 
ALTER TABLE `eh_payment_bill_groups` ADD COLUMN `biz_payee_type` VARCHAR(128) COMMENT '收款方账户类型：EhUsers/EhOrganizations';
ALTER TABLE `eh_payment_bill_groups` ADD COLUMN `biz_payee_id` BIGINT COMMENT '收款方账户id';
-- END BY 杨崇鑫


-- st.zheng
CREATE TABLE `eh_rentalv2_order_records` (
`id`  bigint(20) NOT NULL ,
`namespace_id`  int NULL ,
`order_id`  bigint(20) NULL COMMENT '资源预订订单id' ,
`order_no`  bigint(20) NULL COMMENT '资源预订订单号' ,
`biz_order_num`  varchar(64) NULL COMMENT '处理过的资源预订订单号' ,
`pay_order_id`  bigint(20) NULL COMMENT '支付系统订单号' ,
`payment_order_type`  tinyint(8) NULL COMMENT '订单类型 1续费订单 2欠费订单 3支付订单 4退款订单' ,
`status`  tinyint(8) NULL COMMENT '订单状态0未支付 1已支付' ,
`amount` decimal(16) NULL COMMENT '订单金额' ,
`account_id`  bigint(20) NULL COMMENT '收款方账号' ,
`order_commit_url` varchar(1024) NULL,
`order_commit_token` varchar(1024) NULL,
`order_commit_nonce` varchar(128) NULL,
`order_commit_timestamp`  bigint(20) NULL  ,
`pay_info` text NULL,
`create_time`  datetime  ,
`update_time`  datetime  ,
PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_rentalv2_pay_accounts` (
`id`  bigint(20) NOT NULL ,
`namespace_id`  int NULL ,
`community_id`  bigint(20) NULL ,
`resource_type`  varchar(20) NULL,
`resource_type_id`  bigint(20) NULL ,
`source_type`  varchar(20) NULL COMMENT 'default_rule:默认规则 resource_rule:资源规则' ,
`source_id`  bigint(20) NULL ,
`resource_name`  varchar(20) NULL,
`account_id`  bigint(20) NULL ,
`create_time`  datetime  ,
PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- yanlong.liang 发起活动的企业与收款方账户映射表
CREATE TABLE `eh_activity_biz_payee` (
`id`  bigint(20) NOT NULL ,
`namespace_id` int not null,
`owner_id` bigint not null COMMENT '应用类型id',
`biz_payee_id` bigint(20) NOT NULL COMMENT '收款方账户ID',
`biz_payee_type` VARCHAR(128) COMMENT '收款方账户类型：EhUsers/EhOrganizations',
PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- 活动表增加企业ID，用于查询收款方.
ALTER TABLE `eh_activities` ADD COLUMN `organization_id` BIGINT COMMENT '企业ID';
-- 活动报名表增加支付系统订单ID，关联支付订单.
ALTER TABLE `eh_activity_roster` ADD COLUMN `pay_order_id` BIGINT COMMENT '支付系统订单ID';
ALTER TABLE `eh_activity_roster` ADD COLUMN `refund_pay_order_id` BIGINT COMMENT '支付系统退款订单ID';
-- yanlong.liang END



-- DROP TABLE IF EXISTS `eh_parking_business_payee_accounts`;
CREATE TABLE `eh_parking_business_payee_accounts` (
  `id` bigint NOT NULL,
  `namespace_id` int NOT NULL,
  `owner_type` varchar(32) NOT NULL COMMENT 'community 园区或者其他类型',
  `owner_id` bigint NOT NULL COMMENT '园区id或者其他id',
  `parking_lot_id` bigint NOT NULL COMMENT '停车场id',
  `parking_lot_name` varchar(512) NOT NULL COMMENT '停车场名称',
  `business_type` varchar(32) NOT NULL COMMENT '业务 tempfee:临时车缴费 vipParking:vip车位预约 monthRecharge:月卡充值',
  `payee_id` bigint NOT NULL COMMENT '支付帐号id',
  `payee_user_type` varchar(128) NOT NULL COMMENT '帐号类型，1-个人帐号、2-企业帐号',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '0: inactive, 2: active',
  `creator_uid` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '停车充值收款账户表';

-- DROP TABLE IF EXISTS `eh_siyin_print_business_payee_accounts`;
CREATE TABLE `eh_siyin_print_business_payee_accounts` (
  `id` bigint NOT NULL,
  `namespace_id` int NOT NULL,
  `owner_type` varchar(32) NOT NULL COMMENT 'community 园区',
  `owner_id` bigint NOT NULL COMMENT '园区id',
  `payee_id` bigint NOT NULL COMMENT '支付帐号id',
  `payee_user_type` varchar(128) NOT NULL COMMENT '帐号类型，1-个人帐号、2-企业帐号',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '0: inactive, 2: active',
  `creator_uid` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '云打印收款账户表';

ALTER TABLE `eh_parking_recharge_orders` ADD COLUMN `pay_order_no`  VARCHAR(64) COMMENT '支付系统单号';

ALTER TABLE `eh_siyin_print_orders` ADD COLUMN `pay_order_no`  VARCHAR(64)  COMMENT '支付系统单号';

-- 添加停车订单字段 bydengs
-- 通用脚本
-- 32033	左邻	任务	停车支持发票系统接口 (未处理)
ALTER TABLE `eh_parking_recharge_orders` ADD COLUMN `payee_id` BIGINT COMMENT '收款方id';
ALTER TABLE `eh_parking_recharge_orders` ADD COLUMN `invoice_status` TINYINT COMMENT '0 =发票未开，2发票已开';
ALTER TABLE `eh_parking_recharge_orders` ADD COLUMN `invoice_create_time` DATETIME COMMENT '发票开票时间';
-- dengs

-- 通用脚本
-- ADD BY 张智伟
-- issue-32748
ALTER TABLE eh_meeting_rooms DROP INDEX u_eh_namespace_owner_name;
ALTER TABLE eh_meeting_rooms ADD INDEX `i_eh_namespace_owner_name` (`namespace_id` , `organization_id`, `owner_type` , `owner_id` , `name`);
-- END

-- 通用脚本
-- ADD BY 唐岑  2018年7月3日14:27:33
-- 合同管理V2.9 #30013
CREATE TABLE `eh_contract_events` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `contract_id` bigint(20) NOT NULL COMMENT '该日志事件对应的合同id',
  `operator_uid` bigint(20) DEFAULT NULL COMMENT '修改人员id',
  `opearte_time` datetime NOT NULL COMMENT '修改时间',
  `opearte_type` tinyint(4) NOT NULL COMMENT '操作类型（1：增加，2：删除，3：修改）',
  `content` text COMMENT '修改内容描述'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '合同日志记录表';
-- END BY 唐岑

-- 通用脚本
-- ADD BY 黄明波
-- issue-29989
-- 服务联盟v3.4 服务新增客服会话表 add by huangmingbo 2018.07.03
CREATE TABLE `eh_alliance_online_service` (
	`id` BIGINT(20) NOT NULL,
	`owner_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'id of eh_service_alliances',
	`user_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'id of eh_users',
	`user_name` VARCHAR(64) NOT NULL DEFAULT '""' COMMENT 'organization_members\'s contact name',
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`status` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '0-inactive 1-active  currently not used',
	`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'last update time',
	PRIMARY KEY (`id`),
	UNIQUE INDEX `u_eh_owner_user` (`owner_id`, `user_id`)
)
COMMENT='服务联盟客服表，新增服务时会指派客服专员。这个表保存服务添加过的客服信息。'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB;

ALTER TABLE `eh_service_alliance_attachments` CHANGE COLUMN `attachment_type` `attachment_type` TINYINT(4) NULL DEFAULT '0' COMMENT '0: banner; 1: file attachment; 2: cover' ;
ALTER TABLE `eh_service_alliance_attachments` ADD COLUMN `default_order` TINYINT NOT NULL DEFAULT '0' COMMENT 'the order of image; the smaller the toper;0,1,2,3,...' ;
ALTER TABLE `eh_service_alliance_attachments` ADD COLUMN `skip_url` VARCHAR(1024) NULL DEFAULT NULL COMMENT 'the url to skip' ;

ALTER TABLE `eh_service_alliances` ADD COLUMN `start_time` DATETIME NULL DEFAULT NULL COMMENT 'for policydeclare ; start time of the policy' ;
ALTER TABLE `eh_service_alliances` ADD COLUMN `end_time` DATETIME NULL DEFAULT NULL COMMENT 'for policydeclare ; end time of the policy' ;
ALTER TABLE `eh_service_alliances` ADD INDEX `i_eh_default_order` (`default_order`);

CREATE TABLE `eh_alliance_tag` (
	`id` BIGINT(20) NOT NULL,
	`namespace_id` INT(11) NOT NULL DEFAULT '0',
	`parent_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '0-parent node , others-child node',
	`value` VARCHAR(32) NOT NULL DEFAULT '""' COMMENT 'tag name',
	`type` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'type of service alliances ',
	`is_default` TINYINT(8) NOT NULL DEFAULT '0' COMMENT 'default chosen',
	`default_order` TINYINT(4) NOT NULL DEFAULT '0' COMMENT 'show order; the smaller the toper;like 0,1,2',
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`create_uid` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'user_id of creater',
	`delete_flag` TINYINT(8) NOT NULL DEFAULT '0' COMMENT '0-active 1-deleted',
	PRIMARY KEY (`id`)
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB;

CREATE TABLE `eh_alliance_tag_vals` (
	`id` BIGINT(20) NOT NULL,
	`owner_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'id of service alliance',
	`tag_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'id of eh_alliance_tag',
	`tag_parent_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'parent id of eh_alliance_tag',
	PRIMARY KEY (`id`)
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB;
-- END



-- 通用脚本
-- ADD BY 严军  2018年7月3日16:01:33
-- 服务广场V2.8  #26705  域空间配置V1.6 #18061

-- 用户端访问权限，即广场上的icon能否点击进去  add by yanjun 20180619
ALTER TABLE `eh_service_modules` ADD COLUMN `access_control_type`  tinyint(4) NULL DEFAULT 1 COMMENT '0-all, 1-logon, 2-auth';

ALTER TABLE `eh_service_module_apps` ADD COLUMN `access_control_type`  tinyint(4) NULL DEFAULT 1 COMMENT '0-all, 1-logon, 2-auth';

ALTER TABLE `eh_launch_pad_items` ADD COLUMN `access_control_type`  tinyint(4) NULL DEFAULT 1 COMMENT '0-all, 1-logon, 2-auth';

-- END BY 严军

