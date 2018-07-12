CREATE TABLE `eh_pm_task_configs` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int NOT NULL DEFAULT '0',
  `owner_type` varchar(32) DEFAULT NULL COMMENT 'attachment object owner type',
  `owner_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'owner id',
  `payment_flag` tinyint(4) DEFAULT '0' COMMENT '0: inactive, 1: active',
  `payment_account` bigint(20) DEFAULT NULL COMMENT '收款方账号',
  `payment_account_type` tinyint(4) DEFAULT NULL COMMENT '收款方类型',
  `content_hint` varchar(64) DEFAULT '请描述服务具体内容' COMMENT '服务内容提示文本',
  `creator_id` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `updater_id` bigint(20) NOT NULL DEFAULT '0',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '物业报修通用配置表';

CREATE TABLE `eh_pm_task_orders` (
  `id`  bigint(20) NOT NULL ,
  `namespace_id` int NULL ,
  `task_id` bigint(20) NOT NULL COMMENT '报修单Id',
  `biz_order_num`  varchar(64) NULL COMMENT '处理过的资源预订订单号' ,
  `pay_order_id`  bigint(20) NULL COMMENT '支付系统订单号' ,
  `payment_order_type`  tinyint(8) NULL COMMENT '订单类型 1续费订单 2欠费订单 3支付订单 4退款订单' ,
  `status`  tinyint(8) NULL COMMENT '订单状态0未支付 1已支付' ,
  `amount` decimal(16,2) NULL COMMENT '订单金额' ,
  `service_fee` decimal(16) NULL COMMENT '服务费' ,
  `product_fee` decimal(16) NULL COMMENT '产品费' ,
  `account_id`  bigint(20) NULL COMMENT '收款方账号' ,
  `order_commit_url` varchar(1024) NULL,
  `order_commit_token` varchar(1024) NULL,
  `order_commit_nonce` varchar(128) NULL,
  `order_commit_timestamp`  bigint(20) NULL  ,
  `pay_info` text NULL,
  `create_time`  datetime  ,
  `update_time`  datetime  ,
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '物业报修订单表';

-- 订单明细
CREATE TABLE `eh_pm_task_order_details` (
  `id`  bigint(20) NOT NULL ,
  `namespace_id`  int NULL ,
  `owner_id` bigint(20),
  `owner_type` varchar(32),
  `task_id` bigint(20) NOT NULL COMMENT '报修单Id',
  `order_id`  bigint(20) NOT NULL COMMENT '资源预订订单id' ,
  `product_name` varchar(60) NULL COMMENT '产品名称',
  `product_amount` int NULL COMMENT '产品数量',
  `product_price` decimal(16) NULL COMMENT '产品单价' ,
  `create_time`  datetime  ,
  `update_time`  datetime  ,
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '物业报修订单明细表';

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
	`user_name` VARCHAR(64) NOT NULL DEFAULT '""' COMMENT 'organization_members contact name',
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
-- ADD BY 唐岑 2018年7月3日17:14:41
-- issue-29532
-- 资产管理V2.8
CREATE TABLE `eh_pm_resoucre_reservations` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`enterprise_customer_id` BIGINT COMMENT 'primary id of eh_enterprise_customer',
	`address_id` BIGINT COMMENT 'id of eh_addresses',
	`start_time` TIMESTAMP NOT NULL COMMENT 'start time of this reservation',
	`end_time` TIMESTAMP NOT NULL COMMENT 'end time of this reservation',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '1. inactive; 2: active; 3: deleted;',
	`previous_living_status` TINYINT DEFAULT NULL COMMENT 'previous living status in eh_organization_address_mapping',
	`namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `community_id` BIGINT DEFAULT NULL COMMENT 'id of community',
  `entry_id` BIGINT DEFAULT NULL COMMENT '其他入口的备用字段',
	`creator_uid` BIGINT DEFAULT NULL COMMENT '创建者id，可以为空',
	`create_time` DATETIME NOT NULL DEFAULT NOW() COMMENT '事件发生时间',
	`update_time` DATETIME COMMENT '事件update时间',
	`update_uid` BIGINT DEFAULT NULL ,
	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '访客管理预约编码表';
-- END BY 唐岑

-- 通用脚本
-- ADD BY 严军  2018年7月3日16:01:33
-- 服务广场V2.8  #26705  域空间配置V1.6 #18061

-- 用户端访问权限，即广场上的icon能否点击进去  add by yanjun 20180619
ALTER TABLE `eh_service_modules` ADD COLUMN `access_control_type`  tinyint(4) NULL DEFAULT 1 COMMENT '0-all, 1-logon, 2-auth';

ALTER TABLE `eh_service_module_apps` ADD COLUMN `access_control_type`  tinyint(4) NULL DEFAULT 1 COMMENT '0-all, 1-logon, 2-auth';

ALTER TABLE `eh_launch_pad_items` ADD COLUMN `access_control_type`  tinyint(4) NULL DEFAULT 1 COMMENT '0-all, 1-logon, 2-auth';

-- END BY 严军

-- 通用脚本
-- ADD BY dengs  2018年7月3日16:01:33
-- 停车缴费6.5.5  #31856 
ALTER TABLE eh_parking_recharge_orders ADD COLUMN `pay_source` VARCHAR(16) DEFAULT 'app' COMMENT 'app:APP支付, qrcode:扫码支付';
ALTER TABLE eh_parking_lots ADD COLUMN `id_hash` VARCHAR(128) COMMENT 'id_hash';

-- END BY dengs

-- 通用脚本
-- ADD BY 黄良铭  2018年7月3日
-- issue-30602  iOS推送流程升级 #30602

-- 创建 开发者账号信息表
CREATE TABLE `eh_developer_account_info` (
  `id` INT(11)  NOT NULL COMMENT '主键',
  `bundle_ids` varchar(200)  COMMENT '关联应用',
  `team_id` VARCHAR(100)  COMMENT '关联开发者帐号',
  `authkey_id` VARCHAR(100)  COMMENT 'authkey_id',
  `authkey`  BLOB COMMENT  'authkey',
  `create_time` DATETIME    COMMENT '创建时间',
  `create_name` VARCHAR(50)   COMMENT '创建者',

  PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '开发者账号信息表';


-- 设备注册信息表新增3列（字段 ）
ALTER  TABLE eh_devices  ADD  pusher_service_type  VARCHAR(40)   COMMENT '推送服务类型：develop或productiom';
ALTER  TABLE eh_devices  ADD  bundle_id  VARCHAR(100)    COMMENT '关联应用';

-- 创建 bundleId 映射信息表
CREATE TABLE `eh_bundleid_mapper` (
  `id` INT(11)  NOT NULL COMMENT '主键',
  `namespace_id` INT(11)  COMMENT '域空间ID',
  `identify` VARCHAR(40)  COMMENT 'pusherIdentify中截取的标志字符串',
  `bundle_id` VARCHAR(100)  COMMENT '关联应用',
  PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'bundleId 映射信息表';
-- END

-- Designer: liuyilin
-- Description:   issue-31813门禁2.9.7客户端支持自定义门禁授权方式 20180615
ALTER TABLE `eh_door_access` ADD `max_duration` INTEGER COMMENT '有效时间最大值(天)';
ALTER TABLE `eh_door_access` ADD `max_count` INTEGER COMMENT '按次授权最大次数';
ALTER TABLE `eh_door_access` ADD `defualt_invalid_duration` INTEGER COMMENT '按次授权默认有效期(天)';
ALTER TABLE `eh_door_access` ADD `enable_duration` TINYINT DEFAULT '1' COMMENT '是否支持按有效期开门';
-- issue-31813 END

-- 通用脚本
-- ADD BY shiheng.ma
-- issue-26467 物业报修 增加企业Id字段
ALTER TABLE `eh_pm_tasks` ADD COLUMN `enterprise_id`  bigint(20) NOT NULL DEFAULT 0 COMMENT '需求人公司Id';
-- END

-- 通用脚本
-- ADD BY 唐岑 2018年7月3日22:04:25
-- issue-28195
-- 个人客户管理V2.1
ALTER TABLE `eh_organization_owners` ADD COLUMN `contact_extra_tels` VARCHAR(1024) DEFAULT NULL COMMENT '客户多手机号，以jsonarray方式存储';
-- END BY 唐岑