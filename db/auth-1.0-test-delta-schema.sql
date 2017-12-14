-- 物业巡检权限细化 start  by jiarui
CREATE TABLE `eh_equipment_modle_community_map` (
  `id`          BIGINT(20) NOT NULL,
  `standard_id` BIGINT(20) DEFAULT 0 NOT NULL,
  `template_id` BIGINT(20) DEFAULT 0 NOT NULL,
  `model_type`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '0:standard 1:template',
  `target_type` VARCHAR(255) DEFAULT NULL,
  `target_id`   BIGINT(20)   DEFAULT NULL COMMENT 'community id ',
  `create_time` DATETIME     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

ALTER TABLE `eh_equipment_inspection_standards`
  ADD COLUMN `refer_id` BIGINT(20) NULL;

ALTER TABLE `eh_equipment_inspection_templates`
  ADD COLUMN `refer_id`  bigint(20) NULL;

UPDATE eh_service_module_privileges
SET module_id = 20810
WHERE module_id = 20811;


UPDATE eh_service_modules
SET STATUS =2
WHERE id = 20840;
UPDATE eh_service_modules
SET LEVEL =4
WHERE id = 20841;

UPDATE eh_service_modules
SET STATUS =2
WHERE id = 20841;
-- 物业巡检权限细化 start  by jiarui


ALTER TABLE `eh_equipment_inspection_templates`
  ADD COLUMN `target_type` VARCHAR(32) NOT NULL DEFAULT '',
  ADD COLUMN `target_id` BIGINT(20) NOT NULL DEFAULT 0 ;
-- 物业巡检权限细化 end  by  jiarui

-- merge from forum2.6 by yanjun 201712121010 start

-- added by janson
ALTER TABLE `eh_organization_address_mappings`
  ADD COLUMN `building_id` BIGINT(20) NOT NULL DEFAULT 0 AFTER `namespace_type`,
  ADD COLUMN `building_name` VARCHAR(128) NULL AFTER `building_id`;


-- 在帖子表里增加模块类型，以及类型入口  add by yanjun 20171205
ALTER TABLE `eh_forum_posts` ADD COLUMN `module_type`  tinyint(4) NULL, ADD COLUMN `module_category_id`  bigint(20);

-- 评论功能使用域空间、module、categoryid唯一定位  add by yanjun 20171205
DELETE from eh_interact_settings;
ALTER TABLE `eh_interact_settings` DROP COLUMN `forum_id`, CHANGE COLUMN `type` `module_type`  tinyint(4) NOT NULL COMMENT 'forum, activity, announcement' AFTER `namespace_id`, CHANGE COLUMN `entry_id` `category_id`  bigint(20) NULL DEFAULT NULL AFTER `module_type`;

-- merge from forum2.6 by yanjun 201712121010 end

-- start 权限细化
ALTER TABLE `eh_service_modules` ADD COLUMN `module_control_type`  varchar(64)  DEFAULT '' COMMENT 'community_control;org_control;unlimit' ;
ALTER TABLE `eh_service_module_apps` ADD COLUMN `module_control_type`  varchar(64)  DEFAULT '' COMMENT 'community_control;org_control;unlimit' ;


ALTER TABLE `eh_authorizations` ADD COLUMN `module_app_id`  bigint(20) COMMENT 'eh_service_module_apps id';
ALTER TABLE `eh_authorizations` ADD COLUMN `module_control_type`  varchar(64)  DEFAULT '' COMMENT 'community_control;org_control;unlimit' ;
ALTER TABLE `eh_authorizations` ADD COLUMN `all_control_flag` tinyint(4) DEFAULT 0 COMMENT '0 not all, 1 all' ;
ALTER TABLE `eh_authorizations` ADD COLUMN `control_id` bigint(20) ;
ALTER TABLE `eh_authorizations` ADD COLUMN `control_option` tinyint(4);
-- eh_authorization_control_configs
-- DROP TABLE IF EXISTS `eh_authorization_control_configs`;
CREATE TABLE `eh_authorization_control_configs` (
  `id` bigint(20) NOT NULL,
  `control_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `target_type` varchar(32) NOT NULL,
  `target_id` bigint(20) NOT NULL,
  `include_child_flag` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 应用表改造 by lei.lv
ALTER TABLE `eh_service_module_apps` ADD COLUMN `custom_tag` varchar(64)  DEFAULT '';
ALTER TABLE `eh_service_module_apps` ADD COLUMN `custom_path` varchar(128) DEFAULT '';

-- ----------------------------
-- Table structure for eh_reflection_service_module_apps
-- ----------------------------
-- DROP TABLE IF EXISTS `eh_reflection_service_module_apps`;
CREATE TABLE `eh_reflection_service_module_apps` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `active_app_id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(64) DEFAULT NULL,
  `module_id` bigint(20) DEFAULT NULL,
  `instance_config` text COMMENT '应用入口需要的配置参数',
  `status` tinyint(4) NOT NULL DEFAULT '0',
  `action_type` tinyint(4) DEFAULT NULL,
  `action_data` text COMMENT 'the parameters depend on item_type, json format',
  `update_time` datetime DEFAULT NULL,
  `module_control_type` varchar(64) DEFAULT '' COMMENT 'community_control;org_control;unlimit',
  `multiple_flag` tinyint(4) DEFAULT NULL,
  `custom_tag` varchar(64) DEFAULT '',
  `custom_path` varchar(128) DEFAULT '',
  `menu_id` bigint(20) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5007 DEFAULT CHARSET=utf8mb4;


-- 权限细化改造 by lei.lv
ALTER TABLE `eh_authorization_relations` ADD COLUMN `app_id` bigint(20);

-- end
-- merge from customer1129 by xiongying20171212
ALTER TABLE `eh_customer_commercials` ADD COLUMN `main_business` VARCHAR(256) COMMENT '主营业务';
ALTER TABLE `eh_customer_commercials` ADD COLUMN `branch_company_name` VARCHAR(256) COMMENT '分公司名称';
ALTER TABLE `eh_customer_commercials` ADD COLUMN `branch_registered_date` DATETIME COMMENT '分公司登记日期';
ALTER TABLE `eh_customer_commercials` ADD COLUMN `legal_representative_name` VARCHAR(256) COMMENT '法人代表名称';
ALTER TABLE `eh_customer_commercials` ADD COLUMN `legal_representative_contact` VARCHAR(256) COMMENT '法人联系方式';
ALTER TABLE `eh_customer_commercials` ADD COLUMN `shareholder_name` VARCHAR(256) COMMENT '股东姓名';
ALTER TABLE `eh_customer_commercials` ADD COLUMN `actual_capital_injection_situation` VARCHAR(256) COMMENT '实际注资情况';
ALTER TABLE `eh_customer_commercials` ADD COLUMN `shareholding_situation` VARCHAR(256) COMMENT '股权占比情况';

CREATE TABLE `eh_service_module_functions` (
  `id` BIGINT NOT NULL,
  `module_id` BIGINT NOT NULL DEFAULT 0,
  `privilege_id` BIGINT NOT NULL DEFAULT 0,
  `explain` VARCHAR(64) NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_service_module_exclude_functions` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT,
  `module_id` BIGINT NOT NULL DEFAULT 0,
  `function_id` BIGINT NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `eh_customer_accounts` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `customer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: organization; 1: individual',
  `customer_id` BIGINT,
  `customer_name` VARCHAR(64),
  `bank_name` VARCHAR(128) COMMENT '开户行名称',
  `branch_name` VARCHAR(128) COMMENT '开户网点',
  `account_holder` VARCHAR(128) COMMENT '开户人',
  `account_number`  VARCHAR(128) COMMENT '账号',
  `account_number_type` VARCHAR(128) COMMENT '账号类型',
  `branch_province` VARCHAR(128) COMMENT '开户行所在省',
  `branch_city` VARCHAR(128) COMMENT '开户行所在市',
  `account_type_id` BIGINT COMMENT '账户类型 refer to the id of eh_var_field_items',
  `memo` VARCHAR(128) COMMENT '备注',
  `status` TINYINT NOT NULL DEFAULT 2,
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `eh_customer_taxes` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `customer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: organization; 1: individual',
  `customer_id` BIGINT,
  `customer_name` VARCHAR(64),
  `tax_name` VARCHAR(128) COMMENT '报税人',
  `tax_no` VARCHAR(128) COMMENT '报税人税号',
  `tax_address` VARCHAR(128) COMMENT '地址',
  `tax_phone` VARCHAR(128) COMMENT '联系电话',
  `tax_bank` VARCHAR(128) COMMENT '开户行名称',
  `tax_bank_no` VARCHAR(128) COMMENT '开户行账号',
  `tax_payer_type_id` BIGINT COMMENT '报税人类型 refer to the id of eh_var_field_items',
  `status` TINYINT NOT NULL DEFAULT 2,
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- merge from customer1129 by xiongying20171212 end

-- 物品搬迁 add by sw 20171212
CREATE TABLE `eh_relocation_requests` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` varchar(32) DEFAULT NULL COMMENT 'attachment object owner type',
  `owner_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'owner id',
  `request_no` varchar(128) NOT NULL,
  `requestor_enterprise_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the id of organization where the requestor is in',
  `requestor_enterprise_name` varchar(64) DEFAULT NULL COMMENT 'the enterprise name of requestor',
  `requestor_enterprise_address` varchar(256) DEFAULT NULL COMMENT 'the enterprise address of requestor',
  `requestor_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'requestor id',
  `requestor_name` varchar(64) DEFAULT NULL COMMENT 'the name of requestor',
  `contact_phone` varchar(64) DEFAULT NULL COMMENT 'the phone of requestor',
  `relocation_date` datetime NOT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '0: inactive, 1: processing, 2: completed',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `flow_case_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'flow case id',
  `cancel_time` datetime DEFAULT NULL,
  `cancel_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'cancel user id',
  `qr_code_url` varchar(256) DEFAULT NULL COMMENT 'url of the qr record',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_relocation_request_items` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` varchar(32) DEFAULT NULL COMMENT 'attachment object owner type',
  `owner_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'owner id',
  `request_id` bigint(20) NOT NULL COMMENT 'id of the relocation request record',
  `item_name` varchar(64) DEFAULT NULL COMMENT 'the name of item',
  `item_quantity` int(11) DEFAULT 0 COMMENT 'the quantity of item',
  `status` tinyint(4) DEFAULT NULL COMMENT '0: inactive, 1: , 2: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_relocation_request_attachments` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_type` varchar(32) DEFAULT NULL COMMENT 'attachment object owner type',
  `owner_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'owner id',
  `content_type` varchar(32) DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` varchar(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
  `status` tinyint(4) DEFAULT NULL COMMENT '0: inactive, 1: , 2: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 园区入驻多入口 add by sw 20171212
ALTER TABLE eh_enterprise_op_requests ADD COLUMN `category_id` bigint(20) DEFAULT NULL;
ALTER TABLE eh_lease_promotions ADD COLUMN `category_id` bigint(20) DEFAULT NULL;
ALTER TABLE eh_lease_projects ADD COLUMN `category_id` bigint(20) DEFAULT NULL;
ALTER TABLE eh_lease_project_communities ADD COLUMN `category_id` bigint(20) DEFAULT NULL;
ALTER TABLE eh_lease_issuers ADD COLUMN `category_id` bigint(20) DEFAULT NULL;
ALTER TABLE eh_lease_form_requests ADD COLUMN `category_id` bigint(20) DEFAULT NULL;
ALTER TABLE eh_lease_configs ADD COLUMN `category_id` bigint(20) DEFAULT NULL;
ALTER TABLE eh_lease_buildings ADD COLUMN `category_id` bigint(20) DEFAULT NULL;

-- 用户打印机映射表，by dengs,2017/11/12
-- DROP TABLE IF EXISTS `eh_siyin_user_printer_mappings`;
CREATE TABLE `eh_siyin_user_printer_mappings` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `user_id` BIGINT,
  `reader_name` VARCHAR(128) COMMENT 'printer reader name',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0:INACTIVE,2:ACTIVE',
  `unlock_times` BIGINT,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

