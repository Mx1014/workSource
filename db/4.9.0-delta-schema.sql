-- 园区入驻 add by sw 20170904
ALTER TABLE eh_buildings DROP COLUMN lift_description;
ALTER TABLE eh_buildings DROP COLUMN pm_description;
ALTER TABLE eh_buildings DROP COLUMN parking_lot_description;
ALTER TABLE eh_buildings DROP COLUMN environmental_description;
ALTER TABLE eh_buildings DROP COLUMN power_description;
ALTER TABLE eh_buildings DROP COLUMN telecommunication_description;
ALTER TABLE eh_buildings DROP COLUMN air_condition_description;
ALTER TABLE eh_buildings DROP COLUMN security_description;
ALTER TABLE eh_buildings DROP COLUMN fire_control_description;
ALTER TABLE eh_buildings DROP COLUMN general_form_id;
ALTER TABLE eh_buildings DROP COLUMN custom_form_flag;

ALTER TABLE eh_enterprise_op_requests DROP COLUMN building_id;

ALTER TABLE `eh_lease_promotion_attachments` ADD COLUMN `owner_type` VARCHAR(128) NOT NULL AFTER `id`;
ALTER TABLE `eh_lease_promotion_attachments` CHANGE COLUMN `lease_id` `owner_id` BIGINT NOT NULL AFTER `id`;

ALTER TABLE `eh_lease_promotions` DROP COLUMN community_id;
ALTER TABLE `eh_lease_promotions` ADD COLUMN `building_name` VARCHAR(512) DEFAULT NULL AFTER `building_id`;
ALTER TABLE `eh_lease_promotions` DROP COLUMN `subject`;
ALTER TABLE `eh_lease_promotions` DROP COLUMN `rent_position`;
ALTER TABLE `eh_lease_promotions` ADD COLUMN `update_uid` bigint(20) DEFAULT NULL AFTER `update_time`;
ALTER TABLE `eh_lease_promotions` ADD COLUMN `apartment_name` varchar(128) DEFAULT NULL AFTER `address_id`;


CREATE TABLE `eh_lease_promotion_communities` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `lease_promotion_id` bigint(20) NOT NULL COMMENT 'lease promotion id',
  `community_id` bigint(20) NOT NULL COMMENT 'community id',
  `creator_uid` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_lease_buildings` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `community_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refering to eh_communities',
  `building_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refering to eh_buildings',

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
  `delete_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0: forbidden 1: support delete',

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_community_id_name` (`community_id`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
