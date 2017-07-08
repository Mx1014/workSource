
ALTER TABLE `eh_service_modules` ADD `instance_config` text;
ALTER TABLE `eh_service_modules` ADD `update_time` datetime DEFAULT NULL;
ALTER TABLE `eh_service_modules` ADD `operator_uid` bigint(20) NOT NULL;
ALTER TABLE `eh_service_modules` ADD `creator_uid` bigint(20) NOT NULL;
ALTER TABLE `eh_service_modules` ADD `description` varchar(1024);

ALTER TABLE `eh_item_service_categries` ADD `update_time` datetime DEFAULT NULL;
ALTER TABLE `eh_item_service_categries` ADD `operator_uid` bigint(20) NOT NULL;
ALTER TABLE `eh_item_service_categries` ADD `creator_uid` bigint(20) NOT NULL;
ALTER TABLE `eh_item_service_categries` ADD `description` varchar(1024);

-- 模块应用表  
CREATE TABLE `eh_service_module_apps` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(64) DEFAULT NULL,
  `module_id` bigint(20) NOT NULL,
  `instance_config` text COMMENT '应用入口需要的配置参数',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '',
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
  `item_location` varchar(2048) DEFAULT NULL COMMENT '/eh_portal_item_groups  name 对应eh_launch_pad_items里的item_location',
  `group_name` varchar(64) DEFAULT NULL  COMMENT 'eh_portal_item_groups  name 对应eh_launch_pad_layouts里面的layout_json里面item_group的 groups[x].instanceConfig.itemGroup 和 eh_launch_pad_items里的item_group',
  `name` varchar(64) DEFAULT NULL  COMMENT 'item_${id}',
  `icon_uri` varchar(64) DEFAULT NULL,
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
  `item_category_id` bigint(20) NOT NULL,
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
  `name` varchar(64) NOT NULL COMMENT 'item categry name',
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
