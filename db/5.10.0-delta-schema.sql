

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
	INDEX `u_eh_prefix` (`type`, `project_id`)
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
