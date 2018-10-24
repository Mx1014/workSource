-- AUTHOR: 马世亨
-- REMARK: 物业报修3.8 对接国贸用户映射表 20181001
DROP TABLE IF EXISTS `eh_pm_task_archibus_user_mapping`;
CREATE TABLE `eh_pm_task_archibus_user_mapping` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT NOT NULL DEFAULT 0,

  `archibus_uid` VARCHAR(64) DEFAULT NULL COMMENT '国贸用户Id',
  `user_id` bigint DEFAULT 0 COMMENT '用户Id',
  `identifier_token` varchar(128) DEFAULT NULL COMMENT '用户手机号',
  `create_time` datetime DEFAULT NOW(),
  `creator_uid` BIGINT,
  `update_time` DATETIME DEFAULT NOW(),
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物业报修国贸对接用户映射表';

-- AUTHOR: 马世亨
-- REMARK: 物业报修3.8 支持多应用服务类型 20181001
CREATE TABLE `eh_pm_task_categories`(
  `id` BIGINT NOT NULL,
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `link_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'point to the linked category (similar to soft link in file system)',
  `name` VARCHAR(64) NOT NULL,
  `path` VARCHAR(128),
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `create_time` DATETIME,
  `delete_time` DATETIME COMMENT 'mark-deletion policy. It is much more safer to do so if an allocated category is broadly used',
  `logo_uri` VARCHAR(1024) COMMENT 'the logo uri of the category',
  `description` TEXT,
  `app_id`  bigint(20) NULL DEFAULT NULL COMMENT '多应用标识',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32),
  `owner_id` BIGINT DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `i_eh_category_path` (`path`),
  KEY `i_eh_category_order` (`default_order`),
  KEY `i_eh_category_delete_time` (`delete_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='物业报修服务类型表';


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
