-- 供应商
DROP TABLE IF EXISTS `eh_warehouse_suppliers`;
CREATE TABLE `eh_warehouse_suppliers`(
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) DEFAULT NULL,
  `owner_id` BIGINT DEFAULT NULL ,
  `namespace_id` INTEGER DEFAULT NULL ,
  `identity` VARCHAR(128) DEFAULT NULL COMMENT '供应商编号',
  `name` VARCHAR(128) NOT NULL COMMENT '供应商名称',
  `biz_license` VARCHAR(32) DEFAULT NULL COMMENT '企业营业执照注册号',
  `legal_representative` VARCHAR(32) DEFAULT NULL COMMENT '法人',
  `contacts` VARCHAR(1028) DEFAULT NULL COMMENT '联系人$电话,联系人$,$电话,用逗号连接',
  `register_address` VARCHAR(256) DEFAULT NULL COMMENT '注册地址',
  `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
  `corp_address` VARCHAR(256) DEFAULT NULL COMMENT '公司地址',
  `corp_intro_info` TEXT DEFAULT NULL COMMENT '公司简介',
  `industry` VARCHAR(128) DEFAULT NULL COMMENT '所属行业',
  `bank_name` VARCHAR(128) DEFAULT NULL COMMENT '开户行',
  `bank_card_number` VARCHAR(64) DEFAULT NULL COMMENT '银行账号',
  `category_of_evaluation` TINYINT DEFAULT NULL COMMENT '评定类别， 1：合格；2：临时',
  `evaluation_leval` TINYINT DEFAULT NULL COMMENT '供应商等级 1：A类；2：B类；3：C类',
  `main_biz_scope` VARCHAR(1024) DEFAULT NULL COMMENT '主要经营范围',
  `attachment_url` VARCHAR(256) DEFAULT NULL COMMENT '附件地址',
  `create_time` DATETIME DEFAULT NOW(),
  `create_uid` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT now(),
  `update_uid` DATETIME DEFAULT NULL,
  `default_order` INTEGER DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 采购管理schemas
-- 采购单
DROP TABLE IF EXISTS `eh_warehouse_purchase_orders`;
CREATE TABLE `eh_warehouse_purchase_orders`(
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) DEFAULT NULL,
  `owner_id` BIGINT DEFAULT NULL ,
  `namespace_id` INTEGER DEFAULT NULL ,
  `applicant_id` BIGINT DEFAULT NULL COMMENT '申请人id',
  `applicant_time` DATETIME DEFAULT NOW() COMMENT '申请时间',
  `submission_status` TINYINT DEFAULT NULL COMMENT '审核状态',
  `total_amount` DECIMAL(20,2) DEFAULT 0.00 COMMENT '总金额',
  `warehouse_status` TINYINT DEFAULT NULL COMMENT '库存状态',
  `supplier_id` BIGINT DEFAULT NULL COMMENT '供应商id',
  `delivery_date` DATETIME DEFAULT NULL COMMENT '交付日期',
  `remark` VARCHAR(2048) DEFAULT NULL COMMENT '备注',
  `approval_sheet_id` BIGINT DEFAULT NULL COMMENT '关联的审批单的id',
  `create_time` DATETIME DEFAULT NOW(),
  `create_uid` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT now(),
  `update_uid` DATETIME DEFAULT NULL,
  `default_order` INTEGER DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 采购单物品表
DROP TABLE IF EXISTS `eh_warehouse_purchase_items`;
CREATE TABLE `eh_warehouse_purchase_items`(
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) DEFAULT NULL,
  `owner_id` BIGINT DEFAULT NULL ,
  `namespace_id` INTEGER DEFAULT NULL ,
  `material_id` BIGINT DEFAULT NULL COMMENT '采购物品id',
  `purchase_quantity` BIGINT DEFAULT 0 COMMENT '采购数量',
  `unit_price` DECIMAL(20,2) DEFAULT 0.00 COMMENT '单价',
  `purchase_request_id` BIGINT NOT NULL COMMENT '所属的采购单id',
  `create_time` DATETIME DEFAULT NOW(),
  `create_uid` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT now(),
  `update_uid` DATETIME DEFAULT NULL,
  `default_order` INTEGER DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 为物品增加供应商字段
ALTER TABLE `eh_warehouse_materials` ADD COLUMN `supplier_id` BIGINT DEFAULT NULL COMMENT '物品的供应商的主键id';

DROP TABLE IF EXISTS `eh_warehouse_orders`;
-- 出入库单 模型
CREATE TABLE `eh_warehouse_orders`(
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) DEFAULT NULL,
  `owner_id` BIGINT DEFAULT NULL ,
  `namespace_id` INTEGER DEFAULT NULL ,
  `identity` VARCHAR(128) NOT NULL COMMENT '出入库单号',
  `executor_id` BIGINT DEFAULT NULL COMMENT '执行人id',
  `executor_time` DATETIME DEFAULT now() COMMENT '执行时间',
  `service_type` TINYINT DEFAULT NULL COMMENT '服务类型，1. 普通入库,2.领用出库，3.采购入库',
  `create_time` DATETIME DEFAULT NOW(),
  `create_uid` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NOW(),
  `update_uid` DATETIME DEFAULT NULL,
  `default_order` INTEGER DEFAULT 0,
  KEY `i_service_type` (`service_type`) COMMENT '出入库状态得索引，用于搜索',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 增加出入库记录关联出入库单的字段
ALTER TABLE `eh_warehouse_stock_logs` ADD COLUMN `warehouse_sheet_id` BIGINT DEFAULT NULL COMMENT '关联的出入库单的id';

-- 请示单
DROP TABLE IF EXISTS `eh_requisitions`;
CREATE TABLE `eh_requisitions`(
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) DEFAULT NULL,
  `owner_id` BIGINT DEFAULT NULL ,
  `namespace_id` INTEGER DEFAULT NULL ,
  `identity` VARCHAR(32) NOT NULL COMMENT '请示单号',
  `theme` VARCHAR(32) NOT NULL COMMENT '请示主题',
  `type` BIGINT DEFAULT NULL COMMENT '请示类型,参考eh_requisition_type表',
  `applicant_name` BIGINT NOT NULL COMMENT '请示人id',
  `applicant_department` BIGINT DEFAULT NULL COMMENT '请示人部门',
  `amount` DECIMAL (20,2) DEFAULT 0.00 COMMENT '申请金额',
  `description` TEXT DEFAULT NULL COMMENT '申请说明',
  `attachment_url` VARCHAR(256) DEFAULT NULL COMMENT '附件地址',
  `create_time` DATETIME DEFAULT NOW(),
  `create_uid` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NOW(),
  `update_uid` DATETIME DEFAULT NULL,
  `default_order` INTEGER DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 请示单类型
DROP TABLE IF EXISTS `eh_requisition_types`;
CREATE TABLE `eh_requisition_types`(
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) DEFAULT NULL,
  `owner_id` BIGINT DEFAULT NULL ,
  `namespace_id` INTEGER DEFAULT NULL ,
  `name` VARCHAR(32) NOT NULL COMMENT '类型名字',
  `create_time` DATETIME DEFAULT NOW(),
  `create_uid` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NOW(),
  `update_uid` DATETIME DEFAULT NULL,
  `default_order` INTEGER DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
