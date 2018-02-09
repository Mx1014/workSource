-- 供应商
DROP TABLE IF EXISTS `eh_warehouse_suppliers`;
CREATE TABLE `eh_warehouse_suppliers`(
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER DEFAULT NULL ,
  `owner_type` VARCHAR(32) DEFAULT NULL,
  `owner_id` BIGINT DEFAULT NULL ,
  `identity` VARCHAR(128) DEFAULT NULL COMMENT '供应商编号',
  `name` VARCHAR(128) NOT NULL COMMENT '供应商名称',
  `enterprise_business_licence` VARCHAR(32) DEFAULT NULL COMMENT '企业营业执照注册号',
  `legal_person_name` VARCHAR(32) DEFAULT NULL COMMENT '法人的名字',
  `contact_name` VARCHAR(32) DEFAULT NULL COMMENT '联系人',
  `contact_tel` VARCHAR(64) DEFAULT NULL COMMENT '联系电话',
  `enterprise_register_address` VARCHAR(256) DEFAULT NULL COMMENT '注册地址',
  `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
  `corp_address` VARCHAR(256) DEFAULT NULL COMMENT '公司地址',
  `corp_intro_info` TEXT DEFAULT NULL COMMENT '公司简介',
  `industry` VARCHAR(128) DEFAULT NULL COMMENT '所属行业',
  `bank_name` VARCHAR(128) DEFAULT NULL COMMENT '开户行',
  `bank_card_number` VARCHAR(64) DEFAULT NULL COMMENT '银行账号',
  `evaluation_category` TINYINT DEFAULT NULL COMMENT '评定类别， 1：合格；2：临时',
  `evaluation_levle` TINYINT DEFAULT NULL COMMENT '供应商等级 1：A类；2：B类；3：C类',
  `main_biz_scope` VARCHAR(1024) DEFAULT NULL COMMENT '主要经营范围',
  `attachment_url` VARCHAR(2048) DEFAULT NULL COMMENT '附件地址',
  `create_time` DATETIME DEFAULT NOW(),
  `create_uid` BIGINT DEFAULT NULL,
  `update_time` DATETIME DEFAULT now(),
  `update_uid` BIGINT DEFAULT NULL,
  `default_order` INTEGER DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 采购管理schemas
-- 采购单
DROP TABLE IF EXISTS `eh_warehouse_purchase_orders`;
CREATE TABLE `eh_warehouse_purchase_orders`(
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER DEFAULT NULL ,
  `owner_type` VARCHAR(32) DEFAULT NULL,
  `owner_id` BIGINT DEFAULT NULL ,
  `applicant_id` BIGINT DEFAULT NULL COMMENT '申请人id',
  `applicant_time` DATETIME DEFAULT NOW() COMMENT '申请时间',
  `supplier_id` BIGINT DEFAULT NULL COMMENT '供应商id',
  `submission_status` TINYINT DEFAULT NULL COMMENT '审核状态',
  `total_amount` DECIMAL(20,2) DEFAULT 0.00 COMMENT '总金额',
  `warehouse_status` TINYINT DEFAULT NULL COMMENT '库存状态',
  `delivery_date` DATETIME DEFAULT NULL COMMENT '交付日期',
  `community_id` BIGINT DEFAULT NULL,
  `applicant_name` VARCHAR(128) DEFAULT NULL,
  `contact_tel` VARCHAR(128) DEFAULT NULL,
  `contact_name` BIGINT(128) DEFAULT NULL,
  `remark` VARCHAR(2048) DEFAULT NULL COMMENT '备注',
  `approval_order_id` BIGINT DEFAULT NULL COMMENT '关联的审批单的id',
  `create_time` DATETIME DEFAULT NOW(),
  `create_uid` BIGINT DEFAULT NULL,
  `update_time` DATETIME DEFAULT now(),
  `update_uid` BIGINT DEFAULT NULL,
  `default_order` INTEGER DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 采购单物品表
DROP TABLE IF EXISTS `eh_warehouse_purchase_items`;
CREATE TABLE `eh_warehouse_purchase_items`(
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER DEFAULT NULL ,
  `owner_type` VARCHAR(32) DEFAULT NULL,
  `owner_id` BIGINT DEFAULT NULL ,
  `purchase_request_id` BIGINT NOT NULL COMMENT '所属的采购单id',
  `material_id` BIGINT DEFAULT NULL COMMENT '采购物品id',
  `purchase_quantity` BIGINT DEFAULT 0 COMMENT '采购数量',
  `unit_price` DECIMAL(20,2) DEFAULT 0.00 COMMENT '单价',
  `create_time` DATETIME DEFAULT NOW(),
  `create_uid` BIGINT DEFAULT NULL,
  `update_time` DATETIME DEFAULT now(),
  `update_uid` BIGINT DEFAULT NULL,
  `default_order` INTEGER DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 为物品增加供应商字段
ALTER TABLE `eh_warehouse_materials` ADD COLUMN `supplier_id` BIGINT DEFAULT NULL COMMENT '物品的供应商的主键id';
ALTER TABLE `eh_warehouse_materials` ADD COLUMN `supplier_name` VARCHAR(128) DEFAULT NULL COMMENT '物品的供应商的名字';

DROP TABLE IF EXISTS `eh_warehouse_orders`;
-- 出入库单 模型
CREATE TABLE `eh_warehouse_orders`(
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER DEFAULT NULL ,
  `owner_type` VARCHAR(32) DEFAULT NULL,
  `owner_id` BIGINT DEFAULT NULL ,
  `identity` VARCHAR(128) NOT NULL COMMENT '出入库单号',
  `executor_id` BIGINT DEFAULT NULL COMMENT '执行人id',
  `executor_name` VARCHAR(128) DEFAULT NULL COMMENT '执行人姓名',
  `executor_time` DATETIME DEFAULT now() COMMENT '执行时间',
  `service_type` TINYINT DEFAULT NULL COMMENT '服务类型，1. 普通入库,2.领用出库，3.采购入库',
  `create_time` DATETIME DEFAULT NOW(),
  `create_uid` BIGINT DEFAULT NULL,
  `update_time` DATETIME DEFAULT NOW(),
  `update_uid` BIGINT DEFAULT NULL,
  `default_order` INTEGER DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `i_service_type` (`service_type`) COMMENT '出入库状态得索引，用于搜索'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 增加出入库记录关联出入库单的字段
ALTER TABLE `eh_warehouse_stock_logs` ADD COLUMN `warehouse_order_id` BIGINT DEFAULT NULL COMMENT '关联的出入库单的id';
ALTER TABLE `eh_warehouse_requests` ADD COLUMN `requisition_id` BIGINT DEFAULT NULL COMMENT '关联的请示单的id';

-- 请示单
DROP TABLE IF EXISTS `eh_requisitions`;
CREATE TABLE `eh_requisitions`(
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER DEFAULT NULL ,
  `owner_type` VARCHAR(32) DEFAULT NULL,
  `owner_id` BIGINT DEFAULT NULL ,
  `identity` VARCHAR(32) NOT NULL COMMENT '请示单号',
  `theme` VARCHAR(32) NOT NULL COMMENT '请示主题',
  `requisition_type_id` BIGINT DEFAULT NULL COMMENT '请示类型,参考eh_requisition_type表',
  `applicant_name` VARCHAR(128) NOT NULL COMMENT '请示人id',
  `applicant_department` VARCHAR(256) DEFAULT NULL COMMENT '请示人部门',
  `amount` DECIMAL (20,2) DEFAULT 0.00 COMMENT '申请金额',
  `description` TEXT DEFAULT NULL COMMENT '申请说明',
  `attachment_url` VARCHAR(2048) DEFAULT NULL COMMENT '附件地址',
  `status` TINYINT DEFAULT 1 NOT NULL COMMENT '审批状态，1:处理中；2:已完成; 3:已取消;',
  `create_time` DATETIME DEFAULT NOW(),
  `create_uid` BIGINT DEFAULT NULL,
  `update_time` DATETIME DEFAULT NOW(),
  `update_uid` BIGINT DEFAULT NULL,
  `default_order` INTEGER DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 请示单类型
DROP TABLE IF EXISTS `eh_requisition_types`;
CREATE TABLE `eh_requisition_types`(
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER DEFAULT NULL ,
  `owner_type` VARCHAR(32) DEFAULT NULL,
  `owner_id` BIGINT DEFAULT NULL ,
  `name` VARCHAR(32) NOT NULL COMMENT '类型名字',
  `create_time` DATETIME DEFAULT NOW(),
  `create_uid` BIGINT DEFAULT NULL,
  `update_time` DATETIME DEFAULT NOW(),
  `update_uid` BIGINT DEFAULT NULL,
  `default_order` INTEGER DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

