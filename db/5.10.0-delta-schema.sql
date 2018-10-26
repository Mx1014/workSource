

-- AUTHOR: 严军
-- REMARK: 组件表增加标题栏信息  20181001
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `title_flag`  tinyint(4) NULL COMMENT '0-none,1-left,2-center，reference  TitleFlag.java';
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `title`  varchar(255) NULL ;
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `title_uri`  varchar(1024) NULL ;
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `title_style`  int(11) NULL COMMENT 'title style, reference TitleStyle.java' ;
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `sub_title`  varchar(255) NULL ;
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `title_size`  tinyint(4) NULL COMMENT '0-small, 1-medium, 2-large    TitleSize.java' ;
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `title_more_flag`  tinyint(4) NULL COMMENT '0-no, 1-yes. reference trueOrFalseFlag.java' ;

-- AUTHOR: 严军
-- REMARK: 公司头像字段太短 128 -> 512
ALTER TABLE `eh_organization_details` MODIFY COLUMN `avatar`  varchar(512)  DEFAULT NULL ;

-- AUTHOR: djm
-- REMARK: 合同添加押金状态字段
ALTER TABLE eh_contracts ADD COLUMN `deposit_status`  tinyint(4) NULL COMMENT '押金状态, 0-未缴, 2-已缴' AFTER deposit;

-- AUTHOR: 荣楠
-- REMARK: 组织架构4.6 增加了唯一标识账号给通讯录表
ALTER TABLE `eh_organization_member_details` ADD COLUMN `account` VARCHAR(32) COMMENT 'the unique symbol of the member' AFTER `target_id`;

-- AUTHOR: 梁燕龙
-- REMARK: 用户增加会员等级信息。
ALTER TABLE eh_users ADD COLUMN `vip_level_text` VARCHAR(128) COMMENT '会员等级文本';

-- AUTHOR: 马世亨
-- REMARK: 访客办公地点表  20181001
ALTER TABLE `eh_visitor_sys_office_locations` ADD COLUMN `refer_type` varchar(64) NULL COMMENT '关联数据类型';
ALTER TABLE `eh_visitor_sys_office_locations` ADD COLUMN `refer_id` bigint(20) NULL COMMENT '关联数据id';
-- end

-- AUTHOR: 黄明波
-- REMARK: 服务联盟通用配置修复
CREATE TABLE `eh_alliance_config_state` (
	`id` BIGINT(20) NOT NULL,
	`namespace_id` INT(11) NOT NULL,
	`type` BIGINT(20) NOT NULL,
	`project_id` BIGINT(20) NOT NULL COMMENT 'community为项目id， organaization为公司id',
	`status` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '0-取默认配置 1-取自定义配置。当owner_type为organization时，该值必定为1。',
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ,
	`create_uid` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'user_id of creater' ,
	PRIMARY KEY (`id`),
	UNIQUE INDEX `u_eh_prefix` (`type`, `project_id`)
)
COMMENT='储存应用不同项目下的配置情况。'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

CREATE TABLE `eh_alliance_service_category_match` (
	`id` BIGINT(20) NOT NULL,
	`namespace_id` INT(11) NOT NULL,
        `type` BIGINT(20) NOT NULL,
	`owner_type` VARCHAR(20) NOT NULL,
	`owner_id` BIGINT(20) NOT NULL,
	`service_id` BIGINT(20) NOT NULL COMMENT '服务id',
	`category_id` BIGINT(20) NOT NULL COMMENT '服务类型id',
	`category_name` VARCHAR(64) NOT NULL COMMENT '服务类型名称',
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ,
	`create_uid` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'user_id of creater' ,
	PRIMARY KEY (`id`),
	UNIQUE INDEX `u_eh_service_category` (`service_id`, `category_id`)
)
COMMENT='服务与服务类型的匹配表，生成/删除项目配置后需要新增/删除服务与服务类型的匹配关系。这样客户端才能获取到以前对应的服务。'
ENGINE=InnoDB
;

ALTER TABLE `eh_service_alliance_categories` ADD COLUMN `enable_provider` TINYINT NOT NULL DEFAULT '0' COMMENT '0-关闭服务商功能 1-开启' ;
ALTER TABLE `eh_service_alliance_categories` ADD COLUMN `enable_comment` TINYINT NOT NULL DEFAULT '0' COMMENT '0-关闭评论功能 1-开启评论功能' ;
ALTER TABLE `eh_service_alliance_categories` ADD COLUMN `description` MEDIUMTEXT NULL COMMENT '首页样式描述文字';



-- AUTHOR: 梁燕龙
-- REMARK: 用户认证审核权限配置表
CREATE TABLE `eh_user_authentication_organizations`(
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL  COMMENT '域空间ID',
  `organization_id` BIGINT NOT NULL COMMENT '企业ID',
  `creator_uid` BIGINT NOT NULL COMMENT 'assignment creator uid',
  `create_time` DATETIME COMMENT 'record create time',

  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '用户认证审核权限配置表';


-- AUTHOR: 黄良铭
-- REMARK: 场景记录表添加字段
ALTER TABLE eh_user_current_scene ADD COLUMN  sign_token VARCHAR(2048);


-- AUTHOR: 严军
-- REMARK: 授权表加索引
ALTER TABLE `eh_service_module_app_authorizations` ADD INDEX `organization_id_index` (`organization_id`) ;
ALTER TABLE `eh_service_module_app_authorizations` ADD INDEX `project_id_index` (`project_id`) ;
ALTER TABLE `eh_service_module_app_authorizations` ADD INDEX `owner_id_imdex` (`owner_id`) ;

-- 模块增加模块路由host
ALTER TABLE `eh_service_modules` ADD COLUMN `host`  varchar(255) NULL;

-- AUTHOR: 杨崇鑫
-- REMARK: 瑞安CM对接 为每个域空间初始化一个默认账单组，因此加上一个标识是否是默认账单组的字段
ALTER TABLE `eh_payment_bill_groups` ADD COLUMN `is_default` TINYINT DEFAULT 0 COMMENT '标识是否是默认账单组的字段：1：默认；0：非默认';
-- REMARK: 瑞安CM对接 账单、费项表增加是否是只读字段
ALTER TABLE `eh_payment_bills` ADD COLUMN `is_readonly` TINYINT DEFAULT 0 COMMENT '只读状态：0：非只读；1：只读';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `is_readonly` TINYINT DEFAULT 0 COMMENT '只读状态：0：非只读；1：只读';
-- AUTHOR: djm
alter table eh_contracts modify column sponsor_uid varchar(50);

-- AUTHOR: 荣楠
-- REMARK：OA增加域账号
-- ALTER TABLE `eh_organization_member_details` ADD COLUMN `account` VARCHAR(32) COMMENT 'the unique symbol of the member' AFTER `target_id`;

-- AUTHOR: 缪洲 20180930
-- REMARK: issue-34780 企业支付授权应用列表
ALTER TABLE `eh_siyin_print_orders` ADD COLUMN `general_bill_id` VARCHAR(64) NULL DEFAULT NULL COMMENT '统一账单id' ;

ALTER TABLE `eh_service_modules` ADD COLUMN `enable_enterprise_pay_flag`  tinyint(4) NULL COMMENT '企业支付标志，0-否，1-是';
ALTER TABLE `eh_service_module_apps` ADD COLUMN `enable_enterprise_pay_flag`  tinyint(4) NULL COMMENT '企业支付标志，0-否，1-是';

-- AUTHOR: 缪洲 20180930
-- REMARK: issue-34780 企业支付授权表
CREATE TABLE `eh_enterprise_payment_auths` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间',
  `enterprise_id` BIGINT NOT NULL COMMENT '公司id',
  `app_id` BIGINT NOT NULL COMMENT '授权应用id',
  `app_name` VARCHAR(32) NOT NULL  COMMENT '授权应用名称',
  `source_id` BIGINT NOT NULL COMMENT '授权用户id',
  `source_name` VARCHAR(32) COMMENT '授权用户名称',
  `source_type` VARCHAR(32) NOT NULL COMMENT '用户类型',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `update_time` DATETIME NULL COMMENT '记录创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT='企业支付授权表';



-- AUTHOR: 杨崇鑫 20180930
-- REMARK: 物业缴费V7.1（企业记账流程打通）
-- REMARK: 删除上个版本遗留的弃用字段
ALTER TABLE `eh_asset_module_app_mappings` DROP COLUMN `energy_flag`;
ALTER TABLE `eh_asset_module_app_mappings` DROP COLUMN `contract_originId`;
ALTER TABLE `eh_asset_module_app_mappings` DROP COLUMN `contract_changeFlag`;

-- REMARK：物业缴费V7.1（企业记账流程打通）：统一订单定义的唯一标识
ALTER TABLE `eh_payment_bills` ADD COLUMN `merchant_order_id` VARCHAR(128) COMMENT '统一账单加入的：统一订单定义的唯一标识';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `merchant_order_id` VARCHAR(128) COMMENT '统一账单加入的：统一订单定义的唯一标识';

-- REMARK：  物业缴费V7.1（企业记账流程打通）:增加业务对应的相关信息
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_serve_type` VARCHAR(1024) COMMENT '商品-服务类别';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_namespace` VARCHAR(1024) COMMENT '商品-域空间';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_tag1` VARCHAR(1024) COMMENT '商品-服务提供方标识1';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_tag2` VARCHAR(1024) COMMENT '商品-服务提供方标识2';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_tag3` VARCHAR(1024) COMMENT '商品-服务提供方标识3';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_tag4` VARCHAR(1024) COMMENT '商品-服务提供方标识4';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_tag5` VARCHAR(1024) COMMENT '商品-服务提供方标识5';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_serve_apply_name` VARCHAR(1024) COMMENT '商品-服务提供方名称';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_tag` VARCHAR(1024) COMMENT '商品标识，如：活动ID、商品ID';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_name` VARCHAR(1024) COMMENT '商品名称';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_description` VARCHAR(1024) COMMENT '商品说明';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_counts` INTEGER COMMENT '商品数量';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_price` DECIMAL(10,2) COMMENT '商品单价';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_totalPrice` DECIMAL(10,2) COMMENT '商品总金额';

ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `goods_serve_type` VARCHAR(1024) COMMENT '商品-服务类别';
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `goods_namespace` VARCHAR(1024) COMMENT '商品-域空间';
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `goods_tag1` VARCHAR(1024) COMMENT '商品-服务提供方标识1';
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `goods_tag2` VARCHAR(1024) COMMENT '商品-服务提供方标识2';
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `goods_tag3` VARCHAR(1024) COMMENT '商品-服务提供方标识3';
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `goods_tag4` VARCHAR(1024) COMMENT '商品-服务提供方标识4';
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `goods_tag5` VARCHAR(1024) COMMENT '商品-服务提供方标识5';
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `goods_serve_apply_name` VARCHAR(1024) COMMENT '商品-服务提供方名称';

-- REMARK： 物业缴费V7.1（企业记账流程打通）: 增加记账人名称
ALTER TABLE `eh_payment_bills` ADD COLUMN `consume_user_name` VARCHAR(128) COMMENT '记账人名称' after `consume_user_id`;
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `consume_user_name` VARCHAR(128) COMMENT '记账人名称' after `consume_user_id`;

-- REMARK： 物业缴费V7.1（企业记账流程打通）: 修改consume_user_id注释为“记账人ID”
ALTER TABLE `eh_payment_bills` modify COLUMN `consume_user_id` BIGINT COMMENT '记账人ID';
ALTER TABLE `eh_payment_bill_items` modify COLUMN `consume_user_id` BIGINT COMMENT '记账人ID';


-- AUTHOR: 黄明波 20181007
-- REMARK： 云打印 添加发票标识
ALTER TABLE `eh_siyin_print_orders` ADD COLUMN `is_invoiced` TINYINT(4) NULL DEFAULT '0' COMMENT '是否开具发票 0-未开发票 1-已发票';
ALTER TABLE `eh_siyin_print_printers` ADD COLUMN `printer_name` VARCHAR(128) NOT NULL COMMENT 'printer name' ;
ALTER TABLE `eh_siyin_print_records` ADD COLUMN `serial_number` VARCHAR(128) NULL DEFAULT NULL COMMENT 'reader_name' ;
ALTER TABLE `eh_siyin_print_orders` ADD COLUMN `printer_name` VARCHAR(128) NULL DEFAULT NULL COMMENT '打印机名称';
ALTER TABLE `eh_siyin_print_business_payee_accounts` ADD COLUMN `merchant_id` bigint(20) NULL  DEFAULT '0' COMMENT '商户ID';



-- AUTHOR: 缪洲 20181010
-- REMARK： 云打印 添加支付方式字段
ALTER TABLE `eh_siyin_print_orders` ADD COLUMN `pay_mode` TINYINT(4) COMMENT '支付方式';

-- AUTHOR: 郑思挺 20181011
-- REMARK： 资源预约3.7.1
ALTER TABLE `eh_rentalv2_orders` ADD COLUMN `pay_channel`  VARCHAR(128) NULL  COMMENT '支付类型 ' ;
ALTER TABLE `eh_rentalv2_order_records` ADD COLUMN `pay_url`  varchar(1024) NULL AFTER `pay_info`;
ALTER TABLE `eh_rentalv2_order_records` ADD COLUMN `merchant_id`  bigint(20) NULL AFTER `pay_url`;
ALTER TABLE `eh_rentalv2_order_records` ADD COLUMN `merchant_order_id`  bigint(20) NULL AFTER `merchant_id`;
ALTER TABLE `eh_rentalv2_pay_accounts` ADD COLUMN `merchant_id`  bigint(20) NULL AFTER `account_id`;

-- AUTHOR: 缪洲 20181011
-- REMARK： 停车6.7.2 添加支付方式与支付类型字段
ALTER TABLE `eh_parking_recharge_orders` ADD COLUMN `pay_mode` TINYINT(4) COMMENT '0:个人支付，1：已记账，2：已支付，支付类型';
ALTER TABLE `eh_parking_recharge_orders` ADD COLUMN `general_order_id` varchar(64) COMMENT '统一订单ID';
ALTER TABLE `eh_parking_business_payee_accounts` ADD COLUMN `merchant_id` bigint(20) NULL COMMENT '商户ID';


 
-- AUTHOR: 吴寒
-- REMARK: 打卡考勤V8.2 - 支持人脸识别关联考勤；支持自动打卡
ALTER TABLE `eh_punch_logs` ADD COLUMN `create_type` TINYINT NOT NULL DEFAULT 0 COMMENT '创建类型 : 0-正常打卡创建 1-自动打卡创建 2-人脸识别打卡创建 4-其他第三方接口创建(通过第三方接口打卡没有带创建类型)' ;

-- 外出打卡表
CREATE TABLE `eh_punch_go_out_logs` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `user_id` BIGINT DEFAULT NULL COMMENT 'user''s id',
  `detail_id` BIGINT DEFAULT NULL COMMENT 'eh_organization_member_details id',
  `organization_id` BIGINT DEFAULT NULL COMMENT 'organization id',
  `namespace_id` INT DEFAULT NULL COMMENT 'NAMESPACE id',
  `longitude` DOUBLE DEFAULT NULL,
  `latitude` DOUBLE DEFAULT NULL,
  `location_info` VARCHAR(1024) DEFAULT NULL COMMENT '打卡用到的地址定位',
  `wifi_info` VARCHAR(1024) DEFAULT NULL COMMENT '打卡用到的WiFi信息(暂时无用)',
  `img_uri` VARCHAR(2048) DEFAULT NULL COMMENT '打卡上传图片uri地址',
  `description` VARCHAR(1024) DEFAULT NULL COMMENT '备注',
  `punch_date` DATE DEFAULT NULL COMMENT 'user punch date',
  `punch_time` DATETIME DEFAULT NULL COMMENT '打卡时间',
  `identification` VARCHAR(255) DEFAULT NULL COMMENT 'unique identification for a phone',
  `status` TINYINT(4) DEFAULT NULL COMMENT '打卡状态 0-正常 1-迟到 2-早退 3-缺勤 14-缺卡',
  `update_date` DATE DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_eh_user_id` (`user_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_punch_day_logs` ADD COLUMN `go_out_punch_flag` TINYINT DEFAULT 0 COMMENT '是否外出打卡，1：是 0：否' AFTER `normal_flag`;
ALTER TABLE `eh_punch_statistics` ADD COLUMN `go_out_punch_day_count` INT DEFAULT 0 COMMENT '当月外出打卡天数' AFTER `rest_day_count`;


-- AUTHOR: 张智伟 20180914
-- REMARK: ISSUE-37602 表单关联工作流节点
ALTER TABLE eh_general_approval_vals ADD COLUMN flow_node_id BIGINT COMMENT '表单绑定的工作流节点ID' AFTER flow_case_id;
ALTER TABLE eh_general_approval_vals ADD COLUMN creator_uid BIGINT COMMENT '创建人uid' AFTER create_time;
ALTER TABLE eh_general_approval_vals ADD COLUMN operator_uid BIGINT COMMENT '操作人Uid' AFTER creator_uid;
ALTER TABLE eh_general_approval_vals ADD COLUMN operate_time DATETIME COMMENT '编辑时间' AFTER operator_uid;

ALTER TABLE eh_general_approval_vals ADD INDEX i_eh_flow_case_id(`flow_case_id`);

