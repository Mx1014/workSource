ALTER TABLE `eh_acl_privileges` ADD COLUMN `tag` VARCHAR(32);
ALTER TABLE `eh_acl_privileges` ADD INDEX `u_eh_acl_priv_tag`(`tag`);

ALTER TABLE `eh_acl_roles` ADD COLUMN `tag` VARCHAR(32);
ALTER TABLE `eh_acl_roles` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_acl_roles` ADD COLUMN `owner_type` VARCHAR(32);
ALTER TABLE `eh_acl_roles` ADD COLUMN `owner_id` BIGINT;
ALTER TABLE `eh_acl_roles` DROP INDEX `u_eh_acl_role_app_id_name`;
ALTER TABLE `eh_acl_roles` ADD UNIQUE `u_eh_acl_role_name`(`namespace_id`, `app_id`, `name`, `owner_type`, `owner_id`);
ALTER TABLE `eh_acl_roles` ADD INDEX `u_eh_acl_role_tag`(`tag`);
ALTER TABLE `eh_acl_roles` ADD INDEX `i_eh_ach_role_owner`(`namespace_id`, `app_id`, `owner_type`, `owner_id`);

ALTER TABLE `eh_addresses` DROP INDEX `u_eh_uuid`;
ALTER TABLE `eh_addresses` DROP INDEX `i_eh_addr_zipcode`;
ALTER TABLE `eh_addresses` DROP INDEX `i_eh_addr_address`;
ALTER TABLE `eh_addresses` DROP INDEX `i_eh_addr_geohash`;
ALTER TABLE `eh_addresses` DROP INDEX `i_eh_addr_create_time`;
ALTER TABLE `eh_addresses` DROP INDEX `i_eh_addr_delete_time`;
ALTER TABLE `eh_addresses` DROP INDEX `i_eh_addr_itag1`;
ALTER TABLE `eh_addresses` DROP INDEX `i_eh_addr_itag2`;
ALTER TABLE `eh_addresses` DROP INDEX `i_eh_addr_stag1`;
ALTER TABLE `eh_addresses` DROP INDEX `i_eh_addr_stag2`;
ALTER TABLE `eh_addresses` ADD INDEX `i_eh_addr_address` (`address`);

ALTER TABLE `eh_banners` DROP COLUMN `site_uri`;
ALTER TABLE `eh_banners` ADD COLUMN `scene_type` varchar(64) NOT NULL DEFAULT 'default';

ALTER TABLE `eh_businesses` ADD COLUMN `visible_distance` double DEFAULT '5000';

ALTER TABLE `eh_communities` DROP INDEX `i_eh_community_address`;
ALTER TABLE `eh_communities` MODIFY COLUMN `address` varchar(512);
UPDATE `eh_communities` SET `apt_count`=0 WHERE `apt_count` is null;
ALTER TABLE `eh_communities` MODIFY COLUMN `apt_count` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_communities` ADD COLUMN `area_size` double DEFAULT NULL COMMENT 'area size';

ALTER TABLE `eh_conf_account_histories` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_conf_accounts` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_conf_invoices` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_conf_order_account_map` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_conf_order_account_map` ADD COLUMN `conf_account_namespace_id` int(11) NOT NULL DEFAULT '0';

ALTER TABLE `eh_conf_orders` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_conf_orders` ADD COLUMN `buyer_name` varchar(128) DEFAULT NULL;
ALTER TABLE `eh_conf_orders` ADD COLUMN `buyer_contact` varchar(128) DEFAULT NULL;
ALTER TABLE `eh_conf_orders` ADD COLUMN `vendor_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'vendor type 0: none, 1: Alipay, 2: Wechat';
ALTER TABLE `eh_conf_orders` ADD COLUMN `email` varchar(128) DEFAULT NULL;

ALTER TABLE `eh_conf_reservations` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_conf_source_accounts` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';

ALTER TABLE `eh_configurations` DROP INDEX `u_eh_conf_namespace_name`;

ALTER TABLE `eh_enterprise_addresses` MODIFY COLUMN `address_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_addresses';
ALTER TABLE `eh_enterprise_addresses` MODIFY COLUMN `update_time` datetime DEFAULT NULL;
ALTER TABLE `eh_enterprise_contacts` MODIFY COLUMN `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0: inactive, 1: waitingForApproval, 2: waitingForAcceptance 3: active';

ALTER TABLE `eh_enterprise_op_requests` MODIFY COLUMN `enterprise_name` varchar(128) DEFAULT NULL COMMENT 'enterprise name';
ALTER TABLE `eh_forum_attachments` MODIFY COLUMN `orignial_path` varchar(1024) DEFAULT NULL COMMENT 'attachment file path in 2.8 version, keep it for migration';
ALTER TABLE `eh_forum_attachments` DROP COLUMN `target_id`;

ALTER TABLE `eh_forum_posts` MODIFY COLUMN `embedded_json` longtext;
ALTER TABLE `eh_forum_posts` ADD COLUMN `start_time` datetime DEFAULT NULL COMMENT 'publish start time';
ALTER TABLE `eh_forum_posts` ADD COLUMN `end_time` datetime DEFAULT NULL COMMENT 'publish end time';

ALTER TABLE `eh_group_members` MODIFY COLUMN `member_nick_name` varchar(128) DEFAULT NULL COMMENT 'member nick name within the group';

ALTER TABLE `eh_launch_pad_items` DROP COLUMN `site_uri`;
ALTER TABLE `eh_launch_pad_items` ADD COLUMN `scene_type` varchar(64) NOT NULL DEFAULT 'default';
ALTER TABLE `eh_launch_pad_layouts` DROP COLUMN `site_uri`;
ALTER TABLE `eh_launch_pad_layouts` ADD COLUMN `scene_type` varchar(64) NOT NULL DEFAULT 'default';

ALTER TABLE `eh_locale_templates` DROP INDEX `u_eh_lstr_identifier`;
ALTER TABLE `eh_locale_templates` ADD UNIQUE `u_eh_template_identifier` (`namespace_id`,`scope`,`code`,`locale`);

ALTER TABLE `eh_organization_members` ADD COLUMN `group_id` bigint(20) DEFAULT '0' COMMENT 'refer to the organization id';
ALTER TABLE `eh_organization_members` ADD COLUMN `employee_no` varchar(128) DEFAULT NULL;
ALTER TABLE `eh_organization_members` ADD COLUMN `avatar` varchar(128) DEFAULT NULL;
ALTER TABLE `eh_organization_members` ADD COLUMN `group_path` varchar(128) DEFAULT NULL COMMENT 'refer to the organization path';
ALTER TABLE `eh_organization_members` ADD COLUMN `gender` tinyint(4) DEFAULT '0' COMMENT '0: undisclosured, 1: male, 2: female';
ALTER TABLE `eh_organization_members` ADD COLUMN `update_time` datetime DEFAULT NULL;
ALTER TABLE `eh_organization_members` ADD COLUMN `create_time` datetime DEFAULT NULL;
ALTER TABLE `eh_organization_members` ADD COLUMN `integral_tag1` bigint(20) DEFAULT NULL;
ALTER TABLE `eh_organization_members` ADD COLUMN `integral_tag2` bigint(20) DEFAULT NULL;
ALTER TABLE `eh_organization_members` ADD COLUMN `integral_tag3` bigint(20) DEFAULT NULL;
ALTER TABLE `eh_organization_members` ADD COLUMN `integral_tag4` bigint(20) DEFAULT NULL;
ALTER TABLE `eh_organization_members` ADD COLUMN `integral_tag5` bigint(20) DEFAULT NULL;
ALTER TABLE `eh_organization_members` ADD COLUMN `string_tag1` varchar(128) DEFAULT NULL;
ALTER TABLE `eh_organization_members` ADD COLUMN `string_tag2` varchar(128) DEFAULT NULL;
ALTER TABLE `eh_organization_members` ADD COLUMN `string_tag3` varchar(128) DEFAULT NULL;
ALTER TABLE `eh_organization_members` ADD COLUMN `string_tag4` varchar(128) DEFAULT NULL;
ALTER TABLE `eh_organization_members` ADD COLUMN `string_tag5` varchar(128) DEFAULT NULL;
ALTER TABLE `eh_organization_members` ADD COLUMN `namespace_id` int(11) DEFAULT '0';

ALTER TABLE `eh_organization_owners` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_organization_owners` ADD COLUMN `community_id` bigint(20) NOT NULL DEFAULT '0';

ALTER TABLE `eh_organization_tasks` ADD COLUMN `task_category` varchar(128) DEFAULT NULL COMMENT '1:PUBLIC_AREA 2:PRIVATE_OWNER';
ALTER TABLE `eh_organization_tasks` ADD COLUMN `visible_region_type` tinyint(4) DEFAULT NULL COMMENT 'define the visible region type';
ALTER TABLE `eh_organization_tasks` ADD COLUMN `visible_region_id` bigint(20) DEFAULT NULL COMMENT 'visible region id';

ALTER TABLE `eh_organizations` ADD COLUMN `group_type` varchar(64) DEFAULT NULL COMMENT 'enterprise, department, service_group';
ALTER TABLE `eh_organizations` ADD COLUMN `create_time` datetime DEFAULT NULL;
ALTER TABLE `eh_organizations` ADD COLUMN `update_time` datetime DEFAULT NULL;
ALTER TABLE `eh_organizations` ADD COLUMN `directly_enterprise_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'directly under the company';
ALTER TABLE `eh_organizations` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_organizations` ADD COLUMN `group_id` bigint(20) DEFAULT NULL COMMENT 'eh_group id';
ALTER TABLE `eh_organizations` ADD COLUMN `integral_tag1` bigint(20) DEFAULT NULL;
ALTER TABLE `eh_organizations` ADD COLUMN `integral_tag2` bigint(20) DEFAULT NULL;
ALTER TABLE `eh_organizations` ADD COLUMN `integral_tag3` bigint(20) DEFAULT NULL;
ALTER TABLE `eh_organizations` ADD COLUMN `integral_tag4` bigint(20) DEFAULT NULL;
ALTER TABLE `eh_organizations` ADD COLUMN `integral_tag5` bigint(20) DEFAULT NULL;
ALTER TABLE `eh_organizations` ADD COLUMN `string_tag1` varchar(128) DEFAULT NULL;
ALTER TABLE `eh_organizations` ADD COLUMN `string_tag2` varchar(128) DEFAULT NULL;
ALTER TABLE `eh_organizations` ADD COLUMN `string_tag3` varchar(128) DEFAULT NULL;
ALTER TABLE `eh_organizations` ADD COLUMN `string_tag4` varchar(128) DEFAULT NULL;
ALTER TABLE `eh_organizations` ADD COLUMN `string_tag5` varchar(128) DEFAULT NULL;
ALTER TABLE `eh_organizations` ADD COLUMN `show_flag` tinyint(4) DEFAULT '1';
ALTER TABLE `eh_organizations` DROP INDEX `u_eh_org_name`;

ALTER TABLE `eh_park_apply_card` ADD COLUMN `community_id` bigint(20) NOT NULL DEFAULT '0';
ALTER TABLE `eh_park_apply_card` ADD COLUMN `company_name` varchar(256) NOT NULL DEFAULT '';

ALTER TABLE `eh_park_charge` ADD COLUMN `card_type` varchar(128) DEFAULT NULL;
ALTER TABLE `eh_park_charge` DROP COLUMN `rental_type`;
ALTER TABLE `eh_park_charge` DROP COLUMN `cancel_time`;
ALTER TABLE `eh_park_charge` DROP COLUMN `overtime_time`;

ALTER TABLE `eh_punch_day_logs` MODIFY COLUMN `view_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT 'is view(0) not view(1)';
UPDATE `eh_punch_exception_approvals` SET `approval_status`=1 WHERE `approval_status` IS NULL;
ALTER TABLE `eh_punch_exception_approvals` MODIFY COLUMN `approval_status` tinyint(4) DEFAULT '1' COMMENT 'NORMAL(0)BELATE(1)LEAVEEARLY(2)UNPUNCH(3)BLANDLE(4)ABSENCE(5)SICK(6)EXCHANGE(7)';
UPDATE `eh_punch_exception_approvals` SET `view_flag`=1 WHERE `view_flag` IS NULL;
ALTER TABLE `eh_punch_exception_approvals` MODIFY COLUMN `view_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT 'is view(0) not view(1)';

ALTER TABLE `eh_punch_exception_requests` MODIFY COLUMN `request_type` tinyint(4) DEFAULT NULL COMMENT '0:request ;  1:approval';
ALTER TABLE `eh_punch_exception_requests` MODIFY COLUMN `description` varchar(256) DEFAULT NULL;
ALTER TABLE `eh_punch_exception_requests` MODIFY COLUMN `status` tinyint(4) DEFAULT '1' COMMENT '0: inactive, 1: waitingForApproval, 2:active';
ALTER TABLE `eh_punch_exception_requests` MODIFY COLUMN `process_details` text;
UPDATE `eh_punch_exception_requests` SET `view_flag`=1 WHERE `view_flag` IS NULL;
ALTER TABLE `eh_punch_exception_requests` MODIFY COLUMN `view_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT 'is view(0) not view(1)';

ALTER TABLE `eh_punch_geopoints` MODIFY COLUMN `enterprise_id` bigint(20) DEFAULT NULL;
ALTER TABLE `eh_punch_rules` MODIFY COLUMN `enterprise_id` bigint(20) NOT NULL COMMENT 'rule company id';

ALTER TABLE `eh_qrcodes` MODIFY COLUMN `create_time` datetime DEFAULT NULL;

ALTER TABLE `eh_recharge_info` ADD COLUMN `card_type` varchar(128) DEFAULT NULL;

ALTER TABLE `eh_regions` DROP INDEX `u_eh_region_name`;
ALTER TABLE `eh_regions` ADD UNIQUE `u_eh_region_name` (`namespace_id`,`parent_id`,`name`);

ALTER TABLE `eh_rental_bill_attachments` CHANGE `community_id` `owner_id` bigint(20) NOT NULL COMMENT '    community id or organization id ';
ALTER TABLE `eh_rental_bill_attachments` ADD COLUMN `owner_type` varchar(255) DEFAULT NULL COMMENT 'owner type : community ; organization';

ALTER TABLE `eh_rental_bill_paybill_map` CHANGE `community_id` `owner_id` bigint(20) NOT NULL COMMENT '    community id or organization id ';
ALTER TABLE `eh_rental_bill_paybill_map` ADD COLUMN `owner_type` varchar(255) DEFAULT NULL COMMENT 'owner type : community ; organization';

ALTER TABLE `eh_rental_bills` CHANGE `community_id` `owner_id` bigint(20) NOT NULL COMMENT '    community id or organization id ';
ALTER TABLE `eh_rental_bills` MODIFY COLUMN `pay_total_money` decimal(10,2) DEFAULT NULL;
ALTER TABLE `eh_rental_bills` MODIFY COLUMN `site_total_money` decimal(10,2) DEFAULT NULL;
ALTER TABLE `eh_rental_bills` MODIFY COLUMN `reserve_money` decimal(10,2) DEFAULT NULL;
ALTER TABLE `eh_rental_bills` MODIFY COLUMN `paid_money` decimal(10,2) DEFAULT NULL;
ALTER TABLE `eh_rental_bills` ADD COLUMN `owner_type` varchar(255) DEFAULT NULL COMMENT 'owner type : community ; organization';

UPDATE `eh_rental_items_bills` SET `community_id`=0 WHERE `community_id` IS NULL;
ALTER TABLE `eh_rental_items_bills` MODIFY COLUMN `community_id` bigint(20) NOT NULL COMMENT ' enterprise  community id';
ALTER TABLE `eh_rental_items_bills` MODIFY COLUMN `total_money` decimal(10,2) DEFAULT NULL;

UPDATE `eh_rental_rules` SET `community_id`=0 WHERE `community_id` IS NULL;
ALTER TABLE `eh_rental_rules` CHANGE `community_id` `owner_id` bigint(20) NOT NULL COMMENT '    community id or organization id ';
ALTER TABLE `eh_rental_rules` ADD COLUMN `owner_type` varchar(255) DEFAULT NULL COMMENT 'owner type : community ; organization';


ALTER TABLE `eh_rental_site_items` MODIFY COLUMN `price` decimal(10,2) DEFAULT NULL;

UPDATE `eh_rental_site_rules` SET `community_id`=0 WHERE `community_id` IS NULL;
ALTER TABLE `eh_rental_site_rules` CHANGE `community_id` `owner_id` bigint(20) NOT NULL COMMENT '    community id or organization id ';
ALTER TABLE `eh_rental_site_rules` CHANGE `rentalStep` `rental_step` int(11) DEFAULT '1' COMMENT 'how many time_step must be rental every time ';
ALTER TABLE `eh_rental_site_rules` MODIFY COLUMN `price` decimal(10,2) DEFAULT NULL;
ALTER TABLE `eh_rental_site_rules` ADD COLUMN `owner_type` varchar(255) DEFAULT NULL COMMENT 'owner type : community ; organization';
ALTER TABLE `eh_rental_site_rules` ADD COLUMN `time_step` double DEFAULT NULL;

UPDATE `eh_rental_sites` SET `community_id`=0 WHERE `community_id` IS NULL;
ALTER TABLE `eh_rental_sites` CHANGE `community_id` `owner_id` bigint(20) NOT NULL COMMENT '    community id or organization id ';
ALTER TABLE `eh_rental_sites` ADD COLUMN `owner_type` varchar(255) DEFAULT NULL COMMENT 'owner type : community ; organization';
ALTER TABLE `eh_rental_sites` ADD COLUMN `introduction` text;
ALTER TABLE `eh_rental_sites` ADD COLUMN `notice` text;

UPDATE `eh_rental_sites_bills` SET `community_id`=0 WHERE `community_id` IS NULL;
ALTER TABLE `eh_rental_sites_bills` CHANGE `community_id` `owner_id` bigint(20) NOT NULL COMMENT '    community id or organization id ';
ALTER TABLE `eh_rental_sites_bills` MODIFY COLUMN `total_money` decimal(10,2) DEFAULT NULL;
ALTER TABLE `eh_rental_sites_bills` ADD COLUMN `owner_type` varchar(255) DEFAULT NULL COMMENT 'owner type : community ; organization';

ALTER TABLE `eh_user_service_addresses` ADD COLUMN `notice` text;
ALTER TABLE `eh_user_service_addresses` ADD COLUMN `contact_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: mobile, 1: email';
ALTER TABLE `eh_user_service_addresses` ADD COLUMN `contact_token` varchar(128) NOT NULL DEFAULT '' COMMENT 'phone number or email address';
ALTER TABLE `eh_user_service_addresses` ADD COLUMN `contact_name` varchar(64) DEFAULT NULL;
ALTER TABLE `eh_user_service_addresses` ADD COLUMN `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 1: waitingForConfirmation, 2: active';
ALTER TABLE `eh_user_service_addresses` ADD COLUMN `creator_uid` bigint(20) NOT NULL;
ALTER TABLE `eh_user_service_addresses` ADD COLUMN `update_time` datetime DEFAULT NULL;
ALTER TABLE `eh_user_service_addresses` ADD COLUMN `deleter_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'deleter id';
ALTER TABLE `eh_user_service_addresses` ADD COLUMN `delete_time` datetime DEFAULT NULL;

ALTER TABLE `eh_users` DROP COLUMN `site_uri`;
ALTER TABLE `eh_users` DROP COLUMN `site_user_token`;
ALTER TABLE `eh_users` DROP COLUMN `namespace_user_id`;

ALTER TABLE `eh_version_realm` DROP INDEX `u_eh_ver_realm`;
ALTER TABLE `eh_version_realm` ADD UNIQUE `u_eh_ver_realm` (`realm`,`namespace_id`);

ALTER TABLE `eh_warning_contacts` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';


DROP TABLE IF EXISTS `eh_banner_profiles`;
DROP TABLE IF EXISTS `eh_biz_coupon`;
DROP TABLE IF EXISTS `eh_biz_coupon_groups`;
DROP TABLE IF EXISTS `eh_business`;
DROP TABLE IF EXISTS `eh_business_profiles`;
DROP TABLE IF EXISTS `eh_china_workdate`;
DROP TABLE IF EXISTS `eh_community_address_mappings`;
DROP TABLE IF EXISTS `eh_community_address_owners`;
DROP TABLE IF EXISTS `eh_community_pm_bill_items`;
DROP TABLE IF EXISTS `eh_community_pm_bills`;
DROP TABLE IF EXISTS `eh_community_pm_contacts`;
DROP TABLE IF EXISTS `eh_community_pm_members`;
DROP TABLE IF EXISTS `eh_community_pm_owners`;
DROP TABLE IF EXISTS `eh_community_pm_tasks`;
DROP TABLE IF EXISTS `eh_company_phone_list`;
DROP TABLE IF EXISTS `eh_department_communities`;
DROP TABLE IF EXISTS `eh_department_members`;
DROP TABLE IF EXISTS `eh_departments`;
DROP TABLE IF EXISTS `eh_enterprise_videoconf_account`;
DROP TABLE IF EXISTS `eh_group_contacts`;
DROP TABLE IF EXISTS `eh_request_tojoin_organization`;
DROP TABLE IF EXISTS `eh_user_feedbacks`;
DROP TABLE IF EXISTS `eh_videoconf_account`;
DROP TABLE IF EXISTS `eh_videoconf_account_rule`;
DROP TABLE IF EXISTS `eh_videoconf_enterprise`;
DROP TABLE IF EXISTS `eh_videoconf_order`;
DROP TABLE IF EXISTS `eh_account_vedioconf`;

CREATE TABLE `eh_door_access` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `uuid` varchar(64) NOT NULL,
  `door_type` tinyint(4) NOT NULL COMMENT '0: Zuolin aclink with wifi, 1: Zuolink aclink without wifi',
  `hardware_id` varchar(64) NOT NULL COMMENT 'mac address of aclink',
  `name` varchar(128) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `avatar` varchar(128) DEFAULT NULL,
  `address` varchar(128) DEFAULT NULL,
  `active_user_id` bigint(20) NOT NULL,
  `creator_user_id` bigint(20) NOT NULL,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `geohash` varchar(64) DEFAULT NULL,
  `aes_iv` varchar(64) NOT NULL,
  `link_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: linked, 1: failed',
  `owner_type` tinyint(4) NOT NULL COMMENT '0:community, 1:enterprise, 2: family',
  `owner_id` bigint(20) NOT NULL,
  `role` tinyint(4) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `status` tinyint(4) NOT NULL COMMENT '0:activing, 1: active',
  `acking_secret_version` int(11) NOT NULL DEFAULT '1',
  `expect_secret_key` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_door_access_uuid` (`uuid`),
  KEY `i_eh_door_access_name` (`name`),
  KEY `i_eh_door_hardware_id` (`hardware_id`),
  KEY `i_eh_door_access_owner` (`owner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_door_auth` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `door_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `approve_user_id` bigint(20) NOT NULL,
  `auth_type` tinyint(4) NOT NULL COMMENT '0: forever, 1: temperate',
  `valid_from_ms` bigint(20) NOT NULL DEFAULT '0',
  `valid_end_ms` bigint(20) NOT NULL DEFAULT '0',
  `owner_type` tinyint(4) NOT NULL COMMENT '0:community, 1:enterprise, 2: family, 3: user',
  `owner_id` bigint(20) NOT NULL,
  `organization` varchar(128) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `nickname` varchar(64) DEFAULT NULL,
  `phone` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `status` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_eh_door_auth_door_id` (`door_id`),
  CONSTRAINT `eh_door_auth_ibfk_1` FOREIGN KEY (`door_id`) REFERENCES `eh_door_access` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_door_command` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `door_id` bigint(20) NOT NULL,
  `cmd_id` tinyint(4) NOT NULL COMMENT 'cmd id for aclink',
  `cmd_type` tinyint(4) NOT NULL COMMENT 'cmd type for aclink',
  `cmd_body` text COMMENT 'json type of cmd body',
  `cmd_resp` text COMMENT 'json resp of cmd resp body',
  `server_key_ver` bigint(20) NOT NULL COMMENT 'cmd of server key',
  `aclink_key_ver` tinyint(4) NOT NULL COMMENT 'cmd of aclink key',
  `status` tinyint(4) NOT NULL COMMENT '0: creating, 1: sending, 2: response, 3: process, 4: invalid',
  `user_id` bigint(20) DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `owner_type` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_eh_door_command_id` (`door_id`),
  CONSTRAINT `eh_door_command_ibfk_1` FOREIGN KEY (`door_id`) REFERENCES `eh_door_access` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_aclink_firmware` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `firmware_type` tinyint(4) NOT NULL COMMENT 'firmware type',
  `major` int(11) NOT NULL DEFAULT '0',
  `minor` int(11) NOT NULL DEFAULT '0',
  `revision` int(11) NOT NULL DEFAULT '0',
  `checksum` bigint(20) NOT NULL,
  `md5sum` varchar(64) DEFAULT NULL,
  `download_url` varchar(128) DEFAULT NULL,
  `info_url` varchar(128) DEFAULT NULL,
  `status` tinyint(4) NOT NULL COMMENT '0: invalid, 1: valid',
  `create_time` datetime DEFAULT NULL,
  `creator_id` bigint(20) DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `owner_type` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_aclink_undo_key` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `key_id` int(11) NOT NULL COMMENT 'cancel a key, must notify all users for this key_id to update',
  `door_id` bigint(20) NOT NULL,
  `status` tinyint(4) NOT NULL COMMENT '0: invalid, 1: requesting, 2: confirm',
  `expire_time_ms` bigint(20) NOT NULL,
  `create_time_ms` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_eh_aclink_undo_key_door_id` (`door_id`),
  CONSTRAINT `eh_aclink_undo_key_ibfk_1` FOREIGN KEY (`door_id`) REFERENCES `eh_door_access` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_aclinks` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `door_id` bigint(20) NOT NULL,
  `device_name` varchar(32) NOT NULL,
  `manufacturer` varchar(32) NOT NULL,
  `firware_ver` varchar(32) NOT NULL,
  `driver` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'identify the hardware driver of aclink, not used now',
  `create_time` datetime DEFAULT NULL,
  `status` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_eh_aclink_door_id` (`door_id`),
  CONSTRAINT `eh_aclinks_ibfk_1` FOREIGN KEY (`door_id`) REFERENCES `eh_door_access` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_aes_server_key` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record, also as secret_ver',
  `door_id` bigint(20) NOT NULL,
  `device_ver` tinyint(4) NOT NULL COMMENT 'ver of aclink: 0x0 or 0x1',
  `secret_ver` bigint(20) NOT NULL COMMENT 'ignore it',
  `secret` varchar(64) NOT NULL COMMENT 'The base64 secret 16B',
  `create_time_ms` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_eh_aes_server_key_door_id` (`door_id`),
  CONSTRAINT `eh_aes_server_key_ibfk_1` FOREIGN KEY (`door_id`) REFERENCES `eh_door_access` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_aes_user_key` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `key_id` int(11) NOT NULL COMMENT 'lazy load for aes_user_key',
  `key_type` tinyint(4) NOT NULL COMMENT '0: aclink normal key',
  `door_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `auth_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'auth id of user key',
  `expire_time_ms` bigint(20) NOT NULL,
  `create_time_ms` bigint(20) NOT NULL,
  `creator_uid` bigint(20) NOT NULL,
  `secret` varchar(64) NOT NULL,
  `status` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_eh_aes_user_key_door_id` (`door_id`),
  CONSTRAINT `eh_aes_user_key_ibfk_1` FOREIGN KEY (`door_id`) REFERENCES `eh_door_access` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_building_attachments`;
CREATE TABLE `eh_building_attachments` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `building_id` bigint(20) NOT NULL DEFAULT '0',
  `content_type` varchar(32) DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` varchar(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_business_assigned_namespaces` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_id` bigint(20) NOT NULL COMMENT 'owner business id',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `visible_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT 'business can see in namespace or not.0-hide,1-visible',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_business_namespace_id` (`owner_id`,`namespace_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_conf_account_categories`;
CREATE TABLE `eh_conf_account_categories` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `multiple_account_threshold` int(11) NOT NULL DEFAULT '0' COMMENT 'the limit value of mutiple buy channel',
  `conf_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: 25方仅视频, 1: 25方支持电话, 2: 100方仅视频, 3: 100方支持电话, 4: 6方仅视频, 5: 50方仅视频, 6: 50方支持电话',
  `min_period` int(11) NOT NULL DEFAULT '1' COMMENT 'the minimum count of months',
  `single_account_price` decimal(10,2) DEFAULT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `display_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'display when online or offline, 0: all, 1: online, 2: offline',
  `multiple_account_price` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_conf_conferences`;
CREATE TABLE `eh_conf_conferences` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `meeting_no` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the meeting no from 3rd conference provider',
  `subject` varchar(128) DEFAULT NULL COMMENT 'the conference subject from 3rd conference provider',
  `description` text,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `expect_duration` int(11) NOT NULL DEFAULT '0' COMMENT 'how long the conference expected to last, unit: minute',
  `real_duration` int(11) NOT NULL DEFAULT '0' COMMENT 'how long the conference really lasted, unit: minute',
  `conf_host_id` varchar(128) NOT NULL DEFAULT '0' COMMENT 'the conf host id from 3rd conference provider',
  `conf_host_name` varchar(256) NOT NULL DEFAULT '0' COMMENT 'the conf host name of the conference',
  `max_count` int(11) NOT NULL DEFAULT '0' COMMENT 'the max amount of allowed attendees',
  `conf_host_key` varchar(128) DEFAULT NULL COMMENT 'the password of the conference, set by the creator',
  `join_policy` int(11) NOT NULL DEFAULT '1' COMMENT '0: free join, 1: conf host first',
  `source_account_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to eh_source_accounts',
  `conf_account_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to eh_conf_accounts',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the user id who create the account',
  `join_url` varchar(256) DEFAULT NULL COMMENT 'user use the url to join the meeting',
  `start_url` varchar(256) DEFAULT NULL COMMENT 'user who start the meeting use this url',
  `create_time` datetime DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '0: close, 1: on progress, 2: failed',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `conference_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the conference id from 3rd conference provider',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



DROP TABLE IF EXISTS `eh_lease_promotions`;
CREATE TABLE `eh_lease_promotions` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `community_id` bigint(20) NOT NULL DEFAULT '0',
  `rent_type` varchar(128) NOT NULL DEFAULT '' COMMENT 'For rent',
  `poster_uri` varchar(128) DEFAULT NULL,
  `subject` varchar(512) DEFAULT NULL,
  `rent_areas` varchar(100) DEFAULT NULL,
  `description` text,
  `create_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: active',
  `building_id` bigint(20) NOT NULL DEFAULT '0',
  `rent_position` varchar(128) DEFAULT NULL COMMENT 'rent position',
  `contacts` varchar(128) DEFAULT NULL,
  `contact_phone` varchar(128) DEFAULT NULL,
  `enter_time` datetime DEFAULT NULL COMMENT 'enter time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_message_boxs`;
DROP TABLE IF EXISTS `eh_messages`;

CREATE TABLE `eh_messages` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `app_id` bigint(20) NOT NULL DEFAULT '1' COMMENT 'default to messaging app itself',
  `message_seq` bigint(20) NOT NULL COMMENT 'message sequence id generated at server side',
  `sender_uid` bigint(20) NOT NULL,
  `context_type` varchar(32) DEFAULT NULL,
  `context_token` varchar(32) DEFAULT NULL,
  `channel_type` varchar(32) DEFAULT NULL,
  `channel_token` varchar(32) DEFAULT NULL,
  `message_text` mediumtext COMMENT 'message content',
  `meta_app_id` bigint(20) DEFAULT NULL COMMENT 'app that is in charge of message content and meta intepretation',
  `message_meta` mediumtext COMMENT 'JSON encoded message meta info, in format of string to string map',
  `encode_version` int(11) NOT NULL DEFAULT '1' COMMENT 'message meta encode version',
  `sender_tag` varchar(32) DEFAULT NULL COMMENT 'sender generated tag',
  `create_time` datetime NOT NULL COMMENT 'message creation time',
  PRIMARY KEY (`id`),
  KEY `i_eh_msgs_namespace` (`namespace_id`),
  KEY `i_eh_msgs_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_message_boxs` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `box_key` varchar(128) DEFAULT NULL,
  `message_id` bigint(20) NOT NULL COMMENT 'foreign key to message record',
  `message_seq` bigint(20) NOT NULL COMMENT 'message sequence id that identifies the message',
  `box_seq` bigint(20) NOT NULL COMMENT 'sequence of the message inside the box',
  `create_time` datetime NOT NULL COMMENT 'time that message goes into the box, taken from create time of the message',
  PRIMARY KEY (`id`),
  KEY `fk_eh_mbx_msg_id` (`message_id`),
  KEY `i_eh_mbx_namespace` (`namespace_id`),
  KEY `i_eh_mbx_msg_seq` (`message_seq`),
  KEY `i_eh_mbx_box_seq` (`box_seq`),
  KEY `i_eh_mbx_create_time` (`create_time`),
  CONSTRAINT `eh_message_boxs_ibfk_1` FOREIGN KEY (`message_id`) REFERENCES `eh_messages` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_organization_addresses` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `organization_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_groups',
  `address_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_addresses',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` bigint(20) DEFAULT NULL COMMENT 'record creator user id',
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL COMMENT 'redundant auditing info',
  `process_code` tinyint(4) DEFAULT NULL,
  `process_details` text,
  `proof_resource_uri` varchar(1024) DEFAULT NULL,
  `approve_time` datetime DEFAULT NULL COMMENT 'redundant auditing info',
  `update_time` datetime DEFAULT NULL,
  `building_id` bigint(20) NOT NULL DEFAULT '0',
  `building_name` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_organization_attachments` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `organization_id` bigint(20) NOT NULL DEFAULT '0',
  `content_type` varchar(32) DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` varchar(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
  `creator_uid` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_organization_community_requests` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `community_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_enterprise_communities',
  `member_type` varchar(32) NOT NULL COMMENT 'enterprise',
  `member_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'enterprise_id etc',
  `member_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waitingForApproval, 2: waitingForAcceptance 3: active',
  `creator_uid` bigint(20) DEFAULT NULL COMMENT 'record creator user id',
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL COMMENT 'redundant auditing info',
  `process_code` tinyint(4) DEFAULT NULL,
  `process_details` text,
  `proof_resource_uri` varchar(1024) DEFAULT NULL,
  `approve_time` datetime DEFAULT NULL COMMENT 'redundant auditing info',
  `requestor_comment` text,
  `operation_type` tinyint(4) DEFAULT NULL COMMENT '1: request to join, 2: invite to join',
  `inviter_uid` bigint(20) DEFAULT NULL COMMENT 'record inviter user id',
  `invite_time` datetime DEFAULT NULL COMMENT 'the time the member is invited',
  `update_time` datetime NOT NULL,
  `integral_tag1` bigint(20) DEFAULT NULL,
  `integral_tag2` bigint(20) DEFAULT NULL,
  `integral_tag3` bigint(20) DEFAULT NULL,
  `integral_tag4` bigint(20) DEFAULT NULL,
  `integral_tag5` bigint(20) DEFAULT NULL,
  `string_tag1` varchar(128) DEFAULT NULL,
  `string_tag2` varchar(128) DEFAULT NULL,
  `string_tag3` varchar(128) DEFAULT NULL,
  `string_tag4` varchar(128) DEFAULT NULL,
  `string_tag5` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_organization_details` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `organization_id` bigint(20) NOT NULL COMMENT 'group id',
  `description` text COMMENT 'description',
  `contact` varchar(128) DEFAULT NULL COMMENT 'the phone number',
  `address` varchar(256) DEFAULT NULL COMMENT 'address str',
  `create_time` datetime DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `geohash` varchar(32) DEFAULT NULL,
  `display_name` varchar(64) DEFAULT NULL,
  `contactor` varchar(64) DEFAULT NULL,
  `member_count` bigint(20) DEFAULT '0',
  `checkin_date` datetime DEFAULT NULL COMMENT 'checkin date',
  `avatar` varchar(128) DEFAULT NULL,
  `post_uri` varchar(128) DEFAULT NULL,
  `integral_tag1` bigint(20) DEFAULT NULL,
  `integral_tag2` bigint(20) DEFAULT NULL,
  `integral_tag3` bigint(20) DEFAULT NULL,
  `integral_tag4` bigint(20) DEFAULT NULL,
  `integral_tag5` bigint(20) DEFAULT NULL,
  `string_tag1` varchar(128) DEFAULT NULL,
  `string_tag2` varchar(128) DEFAULT NULL,
  `string_tag3` varchar(128) DEFAULT NULL,
  `string_tag4` varchar(128) DEFAULT NULL,
  `string_tag5` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_organization_role_map` (
  `id` bigint(20) NOT NULL,
  `owner_type` varchar(64) DEFAULT NULL,
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `role_id` bigint(20) NOT NULL DEFAULT '0',
  `private_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: public, 1: private',
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 2: active',
  `create_time` datetime DEFAULT NULL,
  `role_name` varchar(128) DEFAULT NULL COMMENT 'role name',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_organization_task_targets` (
  `id` bigint(20) NOT NULL,
  `owner_type` varchar(64) NOT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `target_type` varchar(64) NOT NULL COMMENT 'target object(user/group) type',
  `target_id` bigint(20) DEFAULT NULL COMMENT 'target object(user/group) id',
  `task_type` varchar(64) NOT NULL,
  `message_type` varchar(64) DEFAULT NULL COMMENT 'PUSH COMMENT SMS ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_owner_doors` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_type` tinyint(4) NOT NULL COMMENT '0:community, 1:enterprise, 2: family',
  `owner_id` bigint(20) NOT NULL,
  `door_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_uq_door_id_owner_id` (`door_id`,`owner_id`,`owner_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_parking_activities` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `parking_lot_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `start_time` datetime DEFAULT NULL COMMENT 'start time',
  `end_time` datetime DEFAULT NULL COMMENT 'end time',
  `top_count` int(11) NOT NULL DEFAULT '0' COMMENT 'Top N user can join the activity',
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_parking_card_requests` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `parking_lot_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `requestor_enterprise_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the id of organization where the requestor is in',
  `requestor_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'requestor id',
  `plate_number` varchar(64) DEFAULT NULL,
  `plate_owner_entperise_name` varchar(64) DEFAULT NULL COMMENT 'the enterprise name of plate owner',
  `plate_owner_name` varchar(64) DEFAULT NULL COMMENT 'the name of plate owner',
  `plate_owner_phone` varchar(64) DEFAULT NULL COMMENT 'the phone of plate owner',
  `status` tinyint(4) DEFAULT NULL COMMENT '0: inactive, 1: queueing, 2: notified, 3: issued',
  `issue_flag` tinyint(4) DEFAULT NULL COMMENT 'whether the applier fetch the card or not, 0: unissued, 1: issued',
  `issue_time` datetime DEFAULT NULL,
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_parking_lots` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT 'used to display',
  `vendor_name` varchar(64) NOT NULL DEFAULT '' COMMENT 'reference to name of eh_parking_vendors',
  `vendor_lot_token` varchar(128) DEFAULT NULL COMMENT 'parking lot id from vendor',
  `card_reserve_days` int(11) NOT NULL DEFAULT '0' COMMENT 'how may days the parking card is reserved for the applicant',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_parking_recharge_orders` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `order_no` bigint(20) DEFAULT NULL,
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `parking_lot_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `plate_number` varchar(64) DEFAULT NULL,
  `plate_owner_name` varchar(64) DEFAULT NULL COMMENT 'the name of plate owner',
  `plate_owner_phone` varchar(64) DEFAULT NULL COMMENT 'the phone of plate owner',
  `payer_enterprise_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the id of organization where the payer is in',
  `payer_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the user id of payer',
  `payer_phone` varchar(64) DEFAULT NULL COMMENT 'the phone of payer',
  `paid_time` datetime DEFAULT NULL COMMENT 'the pay time',
  `vendor_name` varchar(64) NOT NULL DEFAULT '' COMMENT 'reference to name of eh_parking_vendors',
  `card_number` varchar(128) NOT NULL DEFAULT '' COMMENT 'it may be number of virtual card or location number',
  `rate_token` varchar(128) NOT NULL DEFAULT '' COMMENT 'it may be from eh_parking_recharge_rates or 3rd system, according to vendor_name',
  `rate_name` varchar(128) NOT NULL DEFAULT '' COMMENT 'rate name for recharging the parking card',
  `month_count` decimal(10,2) DEFAULT NULL COMMENT 'how many months in the rate for recharging the parking card',
  `price` decimal(10,2) DEFAULT NULL COMMENT 'the total price in the item for recharging the parking card',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT 'the status of the order, 0: inactive, 1: unpaid, 2: paid',
  `recharge_status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0: none, 1: unrecharged 1: recharged',
  `recharge_time` datetime DEFAULT NULL,
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `old_expired_time` datetime DEFAULT NULL,
  `new_expired_time` datetime DEFAULT NULL,
  `paid_type` varchar(32) DEFAULT NULL COMMENT 'the type of payer',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'the order is delete, 0 : is not deleted, 1: deleted',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_parking_recharge_rates` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `parking_lot_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `rate_name` varchar(128) NOT NULL DEFAULT '' COMMENT 'rate name for recharging the parking card',
  `month_count` decimal(10,2) DEFAULT NULL COMMENT 'how many months in the rate for recharging the parking card',
  `price` decimal(10,2) DEFAULT NULL COMMENT 'the total price for recharging the parking card',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_parking_vendors` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT 'the identifier name of the vendor',
  `display_name` varchar(512) NOT NULL DEFAULT '' COMMENT 'the name used to display in desk',
  `description` varchar(2048) DEFAULT NULL COMMENT 'the description of the vendor',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_vender_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_pmsy_communities` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `community_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'community id',
  `community_token` varchar(128) NOT NULL DEFAULT '' COMMENT 'the id of community according the third system, siyuan',
  `contact` varchar(64) NOT NULL DEFAULT '' COMMENT 'the contact of user fill in',
  `bill_tip` varchar(256) NOT NULL DEFAULT '' COMMENT 'the bill_tip of user fill in',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_pmsy_order_items` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `order_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'eh_pmsy_orders id',
  `bill_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'the id of bill according the third system, siyuan',
  `item_name` varchar(128) NOT NULL DEFAULT '' COMMENT 'the name of item according the third system, siyuan',
  `bill_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT 'the money of bill',
  `resource_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'the id of resource,door according the third system, siyuan',
  `customer_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'the id of customer according the third system, siyuan',
  `create_time` datetime DEFAULT NULL,
  `bill_date` datetime DEFAULT NULL COMMENT 'the date of bill',
  `status` tinyint(4) DEFAULT NULL COMMENT 'the status of the order, 0: fail, 1: success',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_pmsy_orders` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `user_name` varchar(64) DEFAULT NULL COMMENT 'the name of address resource',
  `user_contact` varchar(64) DEFAULT NULL COMMENT 'the phone of address resource',
  `order_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT 'the amount of money paid',
  `paid_time` datetime DEFAULT NULL COMMENT 'the pay time',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT 'the status of the order, 0: inactive, 1: unpaid, 2: paid',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `paid_type` varchar(32) DEFAULT NULL COMMENT 'the type of paid 10001:zhifubao 10002: weixin',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `eh_pmsy_payers` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `user_name` varchar(64) NOT NULL DEFAULT '' COMMENT 'the name of user fill in',
  `user_contact` varchar(64) NOT NULL DEFAULT '' COMMENT 'the contact of user fill in',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT 'the status of the payer, 0: inactive, 1: wating, 2: active',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_preferential_rules` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_type` varchar(128) DEFAULT NULL COMMENT 'community',
  `owner_id` bigint(20) NOT NULL,
  `start_time` datetime DEFAULT NULL COMMENT 'start time',
  `end_time` datetime DEFAULT NULL COMMENT 'end time',
  `type` varchar(256) DEFAULT NULL COMMENT 'PARKING',
  `before_nember` bigint(20) DEFAULT NULL,
  `params_json` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_quality_inspection_categories` (
  `id` bigint(20) NOT NULL,
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, enterprise, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `parent_id` bigint(20) NOT NULL DEFAULT '0',
  `name` varchar(64) NOT NULL,
  `path` varchar(128) DEFAULT NULL,
  `default_order` int(11) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_quality_inspection_evaluation_factors` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, enterprise, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `category_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_categories',
  `group_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_organizations',
  `weight` double NOT NULL DEFAULT '0',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_quality_inspection_evaluations` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, enterprise, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `date_str` varchar(32) NOT NULL DEFAULT '',
  `group_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_organizations',
  `group_name` varchar(64) DEFAULT NULL,
  `score` double NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_quality_inspection_standard_group_map` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `group_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: executive group, 2: review group',
  `standard_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_quality_inspection_standards',
  `group_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_organizations',
  `inspector_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_organizations',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_quality_inspection_standards` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, enterprise, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `standard_number` varchar(128) DEFAULT NULL,
  `name` varchar(1024) DEFAULT NULL,
  `category_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_categories',
  `repeat_setting_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_repeat_settings',
  `description` text COMMENT 'content data',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waiting for approval, 2: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL COMMENT 'operator uid of last operation',
  `update_time` datetime DEFAULT NULL,
  `deleter_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'deleter id',
  `delete_time` datetime DEFAULT NULL COMMENT 'mark-deletion policy. historic data may be useful',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_quality_inspection_task_attachments` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `record_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_quality_inspection_tasks',
  `content_type` varchar(32) DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` varchar(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_quality_inspection_task_records` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `task_id` bigint(20) NOT NULL DEFAULT '0',
  `operator_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who operates the task, organization, etc',
  `operator_id` bigint(20) NOT NULL DEFAULT '0',
  `target_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who is the target of processing the task, organization, etc',
  `target_id` bigint(20) NOT NULL DEFAULT '0',
  `process_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: inspect, 2: retify, 3: review, 4: assgin, 5: forward',
  `process_end_time` datetime DEFAULT NULL,
  `process_result` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: inspect ok, 2: inspect close, 3: rectified ok, 4: rectify closed, 5: inspect delay, 6: rectify delay, 11: rectified ok(waiting approval), 12: rectify closed(waiting approval)',
  `process_message` text,
  `process_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_quality_inspection_tasks` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, organization, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `standard_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_quality_inspection_standards',
  `task_number` varchar(128) DEFAULT NULL,
  `task_name` varchar(1024) DEFAULT NULL,
  `task_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: verify task, 2: rectify task',
  `parent_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '0: parent task, others childrean-task',
  `child_count` bigint(20) NOT NULL DEFAULT '0',
  `executive_group_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_organizations',
  `executive_start_time` datetime DEFAULT NULL,
  `executive_expire_time` datetime DEFAULT NULL,
  `executive_time` datetime DEFAULT NULL,
  `executor_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who executes the task, organization, etc',
  `executor_id` bigint(20) NOT NULL DEFAULT '0',
  `operator_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who executes the task, organization, etc',
  `operator_id` bigint(20) NOT NULL DEFAULT '0',
  `process_expire_time` datetime DEFAULT NULL,
  `process_result` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0:none, 11: rectified ok(waiting approval), 12: rectify closed(waiting approval)',
  `process_time` datetime DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: waiting for executing, 2: rectifing, 3: rectified and waiting approval, 4: rectify closed and waiting approval, 5: closed',
  `result` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: inspect ok, 2: inspect close, 3: rectified ok, 4: rectify closed, 5: inspect delay, 6: rectify delay',
  `reviewer_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who review the task, organization, etc',
  `reviewer_id` bigint(20) NOT NULL DEFAULT '0',
  `review_result` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0:none, 1: qualified, 2: unqualified',
  `review_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_repeat_settings` (
  `id` bigint(20) NOT NULL,
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the setting, QA, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `forever_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: false, 1: true',
  `repeat_count` int(11) NOT NULL DEFAULT '0' COMMENT 'how many times of repeating before the end, 0:inactive, others as times',
  `start_date` date DEFAULT NULL COMMENT 'the whole span of the event, including repeat time',
  `end_date` date DEFAULT NULL COMMENT 'ineffective if forever_flag is set true, forever_flag/repeat_count/end_date are exclusive, only one is used',
  `time_ranges` varchar(2048) DEFAULT NULL COMMENT 'multiple time ranges in a day, json format, {"ranges":[{"startTime":"08:00:00","endTime":"09:30:00","duration":"3m"},{"startTime":"18:30:00","endTime":"19:30:00","duration":"2d"}]}',
  `repeat_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: no repeat, 1: by day, 2: by week, 3: by month, 4: by year',
  `repeat_interval` int(11) NOT NULL DEFAULT '1' COMMENT 'every N day/week/month/year',
  `every_workday_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: false, 1: true, only effective if repeat_type is by-day',
  `expression` varchar(2048) DEFAULT NULL COMMENT 'the expression for the repeat details, json format, should be parsed with repeat_type and repeat_interval',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waiting for approval, 2: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_scene_types` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(64) NOT NULL COMMENT 'the identifier of the scene type',
  `display_name` varchar(128) NOT NULL DEFAULT '' COMMENT 'the name used to display',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_ns_scene` (`namespace_id`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_web_menu_privileges` (
  `id` bigint(20) NOT NULL,
  `privilege_id` bigint(20) NOT NULL,
  `menu_id` bigint(20) NOT NULL,
  `name` varchar(64) DEFAULT NULL,
  `show_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0: Not related menu display, 1: Associated menu display',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0: inactive, 1: active',
  `discription` varchar(128) DEFAULT NULL,
  `sort_num` int(11) DEFAULT NULL COMMENT 'sort number',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_web_menus` (
  `id` bigint(20) NOT NULL,
  `name` varchar(64) DEFAULT NULL,
  `parent_id` bigint(20) NOT NULL DEFAULT '0',
  `icon_url` varchar(64) DEFAULT NULL,
  `data_type` varchar(64) DEFAULT NULL,
  `leaf_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'Whether leaf nodes, non leaf nodes can be folded 0: false, 1: true',
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 2: active',
  `path` varchar(64) DEFAULT NULL,
  `type` varchar(64) NOT NULL DEFAULT 'zuolin' COMMENT 'zuolin, park',
  `sort_num` int(11) DEFAULT NULL COMMENT 'sort number',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_web_menu_scopes`( 
`id` BIGINT NOT NULL, 
`menu_id` BIGINT NULL,
`menu_name` VARCHAR(64) NULL,
`owner_type` VARCHAR(64) NOT NULL,
`owner_id` BIGINT DEFAULT NULL,
`apply_policy` TINYINT NOT NULL DEFAULT 0 COMMENT '0: delete , 1: override, 2: revert',
PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; 








