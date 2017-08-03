ALTER TABLE eh_buildings DROP COLUMN lift_description;
ALTER TABLE eh_buildings DROP COLUMN pm_description;
ALTER TABLE eh_buildings DROP COLUMN parking_lot_description;
ALTER TABLE eh_buildings DROP COLUMN environmental_description;
ALTER TABLE eh_buildings DROP COLUMN power_description;
ALTER TABLE eh_buildings DROP COLUMN telecommunication_description;
ALTER TABLE eh_buildings DROP COLUMN air_condition_description;
ALTER TABLE eh_buildings DROP COLUMN security_description;
ALTER TABLE eh_buildings DROP COLUMN fire_control_description;

ALTER TABLE `eh_lease_promotion_attachments` ADD COLUMN `owner_type` VARCHAR(128) NOT NULL AFTER `id`;
ALTER TABLE `eh_lease_promotion_attachments` CHANGE COLUMN `lease_id` `owner_id` BIGINT NOT NULL AFTER `id`;


CREATE TABLE `eh_lease_buildings` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `community_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refering to eh_communities',
  `name` varchar(128) NOT NULL DEFAULT '' COMMENT 'building name',
  `alias_name` varchar(128) DEFAULT NULL,
  `manager_name` varchar(128) DEFAULT NULL,
  `manager_contact` varchar(128) DEFAULT NULL COMMENT 'the phone number',
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `address` varchar(1024) DEFAULT NULL,
  `area_size` double DEFAULT NULL,

  `description` text,
  `poster_uri` varchar(128) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 1: confirming, 2: active',
  `traffic_description` text,
  `general_form_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'id of eh_general_form',
  `custom_form_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: not add custom field, 1: add custom field',
  `default_order` bigint(20) NOT NULL,
  
  `creator_uid` bigint(20) DEFAULT NULL COMMENT 'uid of the user who has suggested address, NULL if it is system created',
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'uid of the user who process the address',
  `operate_time` datetime DEFAULT NULL,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_community_id_name` (`community_id`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 增加详情字段 by st.zheng
ALTER TABLE `eh_rentalv2_items` ADD COLUMN `description` VARCHAR(1024) NULL DEFAULT NULL AFTER `item_type`;
-- 创建 审批表 by st.zheng
CREATE TABLE `eh_community_approve` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `organization_id` bigint(20) NOT NULL,
  `owner_id` bigint(20) NOT NULL,
  `owner_type` varchar(64) NOT NULL,
  `module_id` bigint(20) DEFAULT NULL,
  `module_type` varchar(64) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT '0',
  `project_type` varchar(64) DEFAULT NULL,
  `approve_name` varchar(64) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1',
  `form_origin_id` bigint(20) DEFAULT NULL,
  `form_version` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建申请表 by st.zheng
CREATE TABLE `eh_community_approve_requests` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `organization_id` bigint(20) NOT NULL DEFAULT '0',
  `owner_id` bigint(20) NOT NULL,
  `owner_type` varchar(64) NOT NULL,
  `module_id` bigint(20) DEFAULT NULL,
  `module_type` varchar(64) DEFAULT NULL,
  `flow_case_id` bigint(20) DEFAULT '0',
  `form_origin_id` bigint(20) DEFAULT NULL,
  `form_version` bigint(20) DEFAULT NULL,
  `approve_id` bigint(20) DEFAULT '0',
  `approve_name` varchar(64) DEFAULT NULL,
  `requestor_name` varchar(64) DEFAULT NULL,
  `requestor_phone` varchar(64) DEFAULT NULL,
  `requestor_company` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_rentalv2_orders`  ADD COLUMN `requestor_organization_id` BIGINT DEFAULT NULL COMMENT 'id of the requestor organization';



