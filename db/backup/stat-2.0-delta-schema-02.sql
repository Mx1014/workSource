
-- merge 4.8.1 迁移过来且还未上线的脚本  add by sfyan 20170821

-- 增加可见范围字段 by st.zheng
ALTER TABLE`eh_service_alliances`  ADD COLUMN `range` VARCHAR(512) NULL DEFAULT NULL AFTER `owner_id`;
-- 增加标志位 by st.zheng
ALTER TABLE `eh_service_alliance_jump_module`
  ADD COLUMN `signal` TINYINT(4) NULL DEFAULT '1' COMMENT '标志 0:删除 1:普通 2:审批' AFTER `parent_id`;

-- merge from msg-2.1
-- 更改群聊名称可为空  edit by yanjun 20170724
ALTER TABLE eh_groups MODIFY `name` VARCHAR(128) DEFAULT NULL;


-- 以上是4.8.1的脚本


-- merge portal_admin的脚本 add by sfyan 20170821

ALTER TABLE `eh_service_modules` ADD `instance_config` text;
ALTER TABLE `eh_service_modules` ADD `action_type` tinyint(4) DEFAULT NULL;
ALTER TABLE `eh_service_modules` ADD `update_time` datetime DEFAULT NULL;
ALTER TABLE `eh_service_modules` ADD `operator_uid` bigint(20) NOT NULL;
ALTER TABLE `eh_service_modules` ADD `creator_uid` bigint(20) NOT NULL;
ALTER TABLE `eh_service_modules` ADD `description` varchar(1024);
ALTER TABLE `eh_service_modules` ADD `multiple_flag` tinyint(4) DEFAULT NULL;

ALTER TABLE `eh_item_service_categries` ADD `label` varchar(64) DEFAULT NULL;
ALTER TABLE `eh_item_service_categries` ADD `item_location` varchar(2048) DEFAULT NULL;
ALTER TABLE `eh_item_service_categries` ADD `item_group` varchar(128) NOT NULL DEFAULT '';
ALTER TABLE `eh_item_service_categries` ADD `update_time` datetime DEFAULT NULL;
ALTER TABLE `eh_item_service_categries` ADD `operator_uid` bigint(20) NOT NULL;
ALTER TABLE `eh_item_service_categries` ADD `creator_uid` bigint(20) NOT NULL;
ALTER TABLE `eh_item_service_categries` ADD `description` varchar(1024);
ALTER TABLE `eh_item_service_categries` ADD `scope_code` tinyint(4) NOT NULL DEFAULT '0';
ALTER TABLE `eh_item_service_categries` ADD `scope_id` bigint(20) DEFAULT NULL;

ALTER TABLE `eh_user_launch_pad_items` ADD `item_name` varchar(32) DEFAULT NULL;

ALTER TABLE `eh_launch_pad_items` ADD `categry_name` varchar(64) DEFAULT NULL;


-- 模块应用表  
CREATE TABLE `eh_service_module_apps` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(64) DEFAULT NULL,
  `module_id` bigint(20),
  `instance_config` text COMMENT '应用入口需要的配置参数',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '',
  `action_type` tinyint(4) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) NOT NULL,
  `creator_uid` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 门户导航栏
CREATE TABLE `eh_portal_navigation_bars` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `label` varchar(64) DEFAULT NULL,
  `target_type` varchar(64) NOT NULL,
  `target_id` bigint(20) NOT NULL,
  `icon_uri` varchar(1024) DEFAULT NULL,
  `selected_icon_uri` varchar(1024) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) NOT NULL,
  `creator_uid` bigint(20) NOT NULL,
  `description` varchar(1024),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 门户 layout
CREATE TABLE `eh_portal_layouts` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `label` varchar(64) DEFAULT NULL,
  `location` varchar(2048) DEFAULT NULL COMMENT '用于item_location查询eh_launch_pad_items',
  `name` varchar(64) DEFAULT NULL COMMENT 'layout_${id}，eh_launch_pad_layouts里面的name',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) NOT NULL,
  `creator_uid` bigint(20) NOT NULL,
  `description` varchar(1024),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 门户item group
CREATE TABLE `eh_portal_item_groups` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `layout_id` bigint(20) NOT NULL,
  `label` varchar(64) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL  COMMENT 'item_group_${id}，对应eh_launch_pad_layouts里面的layout_json里面item_group的 groups[x].instanceConfig.itemGroup 和 eh_launch_pad_items里的item_group',
  `separator_flag` tinyint(4) DEFAULT 0,
  `separator_height` decimal(10,2) DEFAULT NULL,
  `widget` varchar(64) DEFAULT NULL,
  `content_type` varchar(64) DEFAULT NULL,
  `style` varchar(64) DEFAULT NULL,
  `instance_config` text COMMENT '参数配置',
  `default_order` int(11) NOT NULL DEFAULT '0',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) NOT NULL,
  `creator_uid` bigint(20) NOT NULL,
  `description` varchar(1024),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 门户item
CREATE TABLE `eh_portal_items` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `item_group_id` bigint(20) NOT NULL,
  `label` varchar(64) DEFAULT NULL,
  `item_location` varchar(2048) DEFAULT NULL COMMENT 'eh_portal_layouts的item_location 对应eh_launch_pad_items里的item_location',
  `group_name` varchar(64) DEFAULT NULL  COMMENT 'eh_portal_item_groups  name 对应eh_launch_pad_layouts里面的layout_json里面item_group的 groups[x].instanceConfig.itemGroup 和 eh_launch_pad_items里的item_group',
  `name` varchar(64) DEFAULT NULL  COMMENT 'item_${id}',
  `icon_uri` varchar(1024) DEFAULT NULL,
  `item_width` int(11) NOT NULL DEFAULT '1',
  `item_height` int(11) NOT NULL DEFAULT '1',
  `bgcolor` int(11) NOT NULL DEFAULT '0',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '',
  `action_type` varchar(64) DEFAULT NULL,
  `action_data` text,
  `default_order` int(11) NOT NULL DEFAULT '0',
  `display_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'default display on the pad, 0: hide, 1:display',
  `selected_icon_uri` varchar(1024) DEFAULT NULL,
  `more_order` int(11) NOT NULL DEFAULT '0',
  `target_type` varchar(32) DEFAULT NULL,
  `target_id` varchar(64) DEFAULT NULL COMMENT 'the entity id linked back to the orginal resource',
  `item_category_id` bigint(20),
  `description` varchar(1024),
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) NOT NULL,
  `creator_uid` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- 门户模板表
CREATE TABLE `eh_portal_layout_templates` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `label` varchar(64) DEFAULT NULL,
  `template_json` text COMMENT '模板json',
  `show_uri` varchar(64),
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) NOT NULL,
  `creator_uid` bigint(20) NOT NULL,
  `description` varchar(1024),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 门户item的分类
CREATE TABLE `eh_portal_item_categories` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) DEFAULT NULL,
  `item_group_id` bigint(20) NOT NULL,
  `name` varchar(64) NOT NULL,
  `label` varchar(64) NOT NULL COMMENT 'item categry label',
  `icon_uri` varchar(1024) DEFAULT NULL COMMENT 'service categry icon uri',
  `default_order` int(11) DEFAULT NULL COMMENT 'order ',
  `align` varchar(64) DEFAULT NULL COMMENT 'left, center',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0: inactive, 1: active',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) NOT NULL,
  `creator_uid` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 门户item分类 范围
CREATE TABLE `eh_portal_content_scopes` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `scope_type` varchar(64) DEFAULT NULL,
  `scope_id` bigint(20) NOT NULL,
  `content_type` varchar(64) DEFAULT NULL,
  `content_id` bigint(20) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) NOT NULL,
  `creator_uid` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 门户发布记录
CREATE TABLE `eh_portal_publish_logs` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `content_type` varchar(64) DEFAULT NULL,
  `content_data` text,
  `status` tinyint(4) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) NOT NULL,
  `creator_uid` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 门户管理和服务广场id映射关系表git 
CREATE TABLE `eh_portal_launch_pad_mappings` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `content_type` varchar(64) DEFAULT NULL,
  `portal_content_id` bigint(20) NOT NULL,
  `launch_pad_content_id` bigint(20) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `creator_uid` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
