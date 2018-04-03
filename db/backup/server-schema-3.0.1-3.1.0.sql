# It is mainly for enterprise communities, which is a little different from communities;
# so the enterprise related tables are added below.

use ehcore;

SET foreign_key_checks = 0;

# member of eh_groups partition
# the relationship between eh_enterprises and eh_enterprise_communities
#
-- DROP TABLE IF EXISTS `eh_enterprise_addresses`;
CREATE TABLE `eh_enterprise_addresses` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `enterprise_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_groups',
    `address_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_addresses',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waitingForApproval, 2: active',
    `creator_uid` BIGINT COMMENT 'record creator user id',
    `create_time` DATETIME,
    `operator_uid` BIGINT COMMENT 'redundant auditing info',
    `process_code` TINYINT,
    `process_details` TEXT,
    `proof_resource_uri` VARCHAR(1024),
    `approve_time` DATETIME COMMENT 'redundant auditing info',
    `update_time` DATETIME,

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_groups partition
# TODO for index field
#
-- DROP TABLE IF EXISTS `eh_enterprise_contacts`;
CREATE TABLE `eh_enterprise_contacts` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `enterprise_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'enterprise id',
    `name` VARCHAR(256) COMMENT 'real name',
    `nick_name` VARCHAR(256) COMMENT 'display name',
    `avatar` VARCHAR(128) COMMENT 'avatar uri',
    `user_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'user id reference to eh_users, it determine the contact authenticated or not',
    `role` BIGINT NOT NULL DEFAULT 7 COMMENT 'The role in company',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive, 1: waitingForApproval, 2: waitingForAcceptance 3: active',
    `creator_uid` BIGINT COMMENT 'record creator user id',
    `create_time` DATETIME,
    `integral_tag1` BIGINT,
    `integral_tag2` BIGINT,
    `integral_tag3` BIGINT,
    `integral_tag4` BIGINT,
    `integral_tag5` BIGINT,
    `string_tag1` VARCHAR(128),
    `string_tag2` VARCHAR(128),
    `string_tag3` VARCHAR(128),
    `string_tag4` VARCHAR(128),
    `string_tag5` VARCHAR(128),
	
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_enterprises partition
# entry info of eh_enterprise_contacts
#
-- DROP TABLE IF EXISTS `eh_enterprise_contact_entries`;
CREATE TABLE `eh_enterprise_contact_entries` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `enterprise_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'enterprise id',
    `contact_id` BIGINT NOT NULL COMMENT 'contact id',
    `entry_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: mobile, 1: email',
    `entry_value` VARCHAR(128),
    `creator_uid` BIGINT COMMENT 'record creator user id',
    `create_time` DATETIME,

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_enterprises partition
# internal group in enterprise
#
-- DROP TABLE IF EXISTS `eh_enterprise_contact_groups`;
CREATE TABLE `eh_enterprise_contact_groups` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `enterprise_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'enterprise id',
    `role` BIGINT NOT NULL DEFAULT 7 COMMENT 'The role in company',
    `name` VARCHAR(256),
    `parent_id` BIGINT NOT NULL DEFAULT 0,
	`path` VARCHAR(128) COMMENT 'path from the root',
    `creator_uid` BIGINT COMMENT 'record creator user id',
    `create_time` DATETIME,
	
    `integral_tag1` BIGINT,
    `integral_tag2` BIGINT,
    `integral_tag3` BIGINT,
    `integral_tag4` BIGINT,
    `integral_tag5` BIGINT,
    `string_tag1` VARCHAR(128),
    `string_tag2` VARCHAR(128),
    `string_tag3` VARCHAR(128),
    `string_tag4` VARCHAR(128),
    `string_tag5` VARCHAR(128),

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_enterprises partition
# role of member inside the group (internal)
#
-- DROP TABLE IF EXISTS `eh_enterprise_contact_group_members`;
CREATE TABLE `eh_enterprise_contact_group_members` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `enterprise_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'enterprise id',
    `contact_group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_enterprise_contact_groups',
    `contact_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_enterprise_contacts',
    `role` BIGINT NOT NULL DEFAULT 7 COMMENT 'The role in company',
    `contact_avatar` VARCHAR(128) COMMENT 'contact avatar image identifier in storage sub-system',
    `contact_nick_name` VARCHAR(128) COMMENT 'contact nick name within the group',
    `contact_status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForApproval, 2: active',
    `creator_uid` BIGINT COMMENT 'record creator user id',
    `create_time` DATETIME,
	
    `integral_tag1` BIGINT,
    `integral_tag2` BIGINT,
    `integral_tag3` BIGINT,
    `integral_tag4` BIGINT,
    `integral_tag5` BIGINT,
    `string_tag1` VARCHAR(128),
    `string_tag2` VARCHAR(128),
    `string_tag3` VARCHAR(128),
    `string_tag4` VARCHAR(128),
    `string_tag5` VARCHAR(128),

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_communities partition
# the relationship between eh_enterprises and eh_enterprise_communities
#
-- DROP TABLE IF EXISTS `eh_enterprise_community_map`;
CREATE TABLE `eh_enterprise_community_map` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `community_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_enterprise_communities',
    `member_type` VARCHAR(32) NOT NULL COMMENT 'enterprise',
    `member_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'enterprise_id etc',
    `member_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waitingForApproval, 2: waitingForAcceptance 3: active',
    `creator_uid` BIGINT COMMENT 'record creator user id',
    `create_time` DATETIME,
    `operator_uid` BIGINT COMMENT 'redundant auditing info',
    `process_code` TINYINT,
    `process_details` TEXT,
    `proof_resource_uri` VARCHAR(1024),
    `approve_time` DATETIME COMMENT 'redundant auditing info',
    `requestor_comment` TEXT,
    `operation_type` TINYINT COMMENT '1: request to join, 2: invite to join',
    `inviter_uid` BIGINT COMMENT 'record inviter user id',
    `invite_time` DATETIME COMMENT 'the time the member is invited',
    `update_time` DATETIME NOT NULL,

    `integral_tag1` BIGINT,
    `integral_tag2` BIGINT,
    `integral_tag3` BIGINT,
    `integral_tag4` BIGINT,
    `integral_tag5` BIGINT,
    `string_tag1` VARCHAR(128),
    `string_tag2` VARCHAR(128),
    `string_tag3` VARCHAR(128),
    `string_tag4` VARCHAR(128),
    `string_tag5` VARCHAR(128),

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_communities partition
#
-- DROP TABLE IF EXISTS `eh_buildings`;
CREATE TABLE `eh_buildings` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `community_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refering to eh_communities',
    `name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'building name',
	`alias_name` VARCHAR(128),
	`manager_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'the manager of the building',
	`contact` VARCHAR(128) COMMENT 'the phone number',
    `address` VARCHAR(1024),
    `area_size` DOUBLE,
    `longitude` DOUBLE,
    `latitude` DOUBLE,
    `geohash` VARCHAR(32),
    `description` TEXT,
    `poster_uri` VARCHAR(128),
    `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: confirming, 2: active',
	`operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'uid of the user who process the address',
    `operate_time` DATETIME,
    `creator_uid` BIGINT COMMENT 'uid of the user who has suggested address, NULL if it is system created',
    `create_time` DATETIME,
    `delete_time` DATETIME COMMENT 'mark-deletion policy, historic data may be valuable',
    
    `integral_tag1` BIGINT,
    `integral_tag2` BIGINT,
    `integral_tag3` BIGINT,
    `integral_tag4` BIGINT,
    `integral_tag5` BIGINT,
    `string_tag1` VARCHAR(128),
    `string_tag2` VARCHAR(128),
    `string_tag3` VARCHAR(128),
    `string_tag4` VARCHAR(128),
    `string_tag5` VARCHAR(128),
    
	UNIQUE `u_eh_community_id_name`(`community_id`, `name`),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_communities sharding group
#
-- DROP TABLE IF EXISTS `eh_building_attachments`;
CREATE TABLE `eh_building_attachments` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `building_id` BIGINT NOT NULL DEFAULT 0,
    `content_type` VARCHAR(32) COMMENT 'attachment object content type',
    `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
    `creator_uid` BIGINT NOT NULL DEFAULT 0,
    `create_time` DATETIME,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_groups sharding group
#
-- DROP TABLE IF EXISTS `eh_enterprise_attachments`;
CREATE TABLE `eh_enterprise_attachments` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `enterprise_id` bigint(20) NOT NULL DEFAULT '0',
  `content_type` varchar(32) DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` varchar(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
  `creator_uid` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global parition
# shared among namespaces, no application module specific information
#
-- DROP TABLE IF EXISTS `eh_organization_assigned_scopes`;
CREATE TABLE `eh_organization_assigned_scopes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `organization_id` bigint(20) NOT NULL,
  `scope_code` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: building',
  `scope_id` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

# 
# member of eh_users partition
# Used for duplicated recording of group membership that user is involved in order to store 
# it in the same shard as of its owner user
#
-- DROP TABLE IF EXISTS `eh_user_communities`;
CREATE TABLE `eh_user_communities` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_uid` BIGINT NOT NULL COMMENT 'owner user id',
    `community_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'redendant info for quickly distinguishing associated community', 
    `community_id` BIGINT NOT NULL DEFAULT 0,
    `join_policy` TINYINT NOT NULL DEFAULT 1 COMMENT '1: register, 2: request to join',
    `create_time` DATETIME,
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_usr_community`(`owner_uid`, `community_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global partition
#
-- DROP TABLE IF EXISTS `eh_namespace_resources`;
CREATE TABLE `eh_namespace_resources` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `namespace_id` INTEGER NOT NULL DEFAULT 0,
    `resource_type` VARCHAR(128) COMMENT 'COMMUNITY',
    `resource_id` BIGINT NOT NULL DEFAULT 0,
    `create_time` DATETIME,
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_namespace_resource_id`(`namespace_id`, `resource_type`, `resource_id`),
    INDEX `i_eh_resource_id`(`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- reuse eh_communities for eh_enterprise_communities
ALTER TABLE `eh_communities` ADD COLUMN `community_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: residential, 1: commercial';
ALTER TABLE `eh_communities` ADD COLUMN `default_forum_id` BIGINT NOT NULL DEFAULT 1 COMMENT 'the default forum for the community, forum-1 is system default forum';
ALTER TABLE `eh_communities` ADD COLUMN `feedback_forum_id` BIGINT NOT NULL DEFAULT 2 COMMENT 'the default forum for the community, forum-2 is system feedback forum';
ALTER TABLE `eh_communities` ADD COLUMN `update_time` DATETIME;
ALTER TABLE `eh_groups` ADD COLUMN `visible_region_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'the type of region where the group belong to';
ALTER TABLE `eh_groups` ADD COLUMN `visible_region_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the id of region where the group belong to';
ALTER TABLE `eh_user_profiles` MODIFY COLUMN `item_name` VARCHAR(128) ;
ALTER TABLE `eh_addresses` ADD COLUMN `area_size` DOUBLE COMMENT 'the area size of the room according to the address';

-- ALTER TABLE `eh_users` DROP COLUMN `site_uri`;
-- ALTER TABLE `eh_users` DROP COLUMN `site_user_token`;
-- ALTER TABLE `eh_launch_pad_layouts` DROP COLUMN `site_uri`;
-- ALTER TABLE `eh_launch_pad_items` DROP COLUMN `site_uri`;
-- ALTER TABLE `eh_banners` DROP COLUMN `site_uri`;

-- ALTER TABLE `eh_users` ADD COLUMN `site_uri` VARCHAR(2048) NOT NULL DEFAULT '' COMMENT 'the site uri of third-part system';
-- ALTER TABLE `eh_users` ADD COLUMN `site_user_token` VARCHAR(2048) NOT NULL DEFAULT '' COMMENT 'the site user token of third-part system';
-- ALTER TABLE `eh_launch_pad_layouts` ADD COLUMN `site_uri` VARCHAR(2048) NOT NULL DEFAULT '' COMMENT 'the site uri of third-part system';
-- ALTER TABLE `eh_launch_pad_items` ADD COLUMN `site_uri` VARCHAR(2048) NOT NULL DEFAULT '' COMMENT 'the site uri of third-part system';
-- ALTER TABLE `eh_banners` ADD COLUMN `site_uri` VARCHAR(2048) NOT NULL DEFAULT '' COMMENT 'the site uri of third-part system';
ALTER TABLE `eh_users` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_users` ADD COLUMN `namespace_user_token` VARCHAR(2048) NOT NULL DEFAULT '';
ALTER TABLE `eh_user_identifiers` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_version_realm` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_version_upgrade_rules` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_version_urls` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_versioned_content` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_categories` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_categories` DROP INDEX `u_eh_category_name`;

ALTER TABLE `eh_scoped_configurations` MODIFY COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_launch_pad_layouts` MODIFY COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_launch_pad_items` MODIFY COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
UPDATE `eh_groups` SET namespace_id=0 WHERE namespace_id IS NULL;
ALTER TABLE `eh_groups` MODIFY COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_forums` MODIFY COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_banners` MODIFY COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_binary_resources` MODIFY COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_rtxt_resources` MODIFY COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_events` MODIFY COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_polls` MODIFY COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
-- ALTER TABLE `eh_devices` MODIFY COLUMN `device_id` VARCHAR(2048) NOT NULL DEFAULT '';

-- update at 20151210
-- ALTER TABLE eh_park_charge ADD COLUMN `card_type`  VARCHAR(128);
-- ALTER TABLE eh_recharge_info ADD COLUMN  `card_type`  VARCHAR(128);
-- ALTER TABLE `eh_configurations` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
-- ALTER TABLE `eh_configurations` ADD COLUMN `display_name` VARCHAR(128);
-- ALTER TABLE `eh_configurations` DROP INDEX `u_eh_conf_name`;
UPDATE `eh_park_charge` SET card_type = '普通月卡';
UPDATE `eh_launch_pad_items` SET action_data = '{"cardDescription":"目前仅开通金融基地停车场
其他停车场线上充值功能正在建设中"}' WHERE action_type = 30 ;
update `eh_launch_pad_items` set `action_data` = '{"privateFlag":0,"keywords":"不传"}' , `action_type` = 36 where `id` = 813;


-- ALTER TABLE `eh_categories` DROP INDEX `u_eh_category_name`;

INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'enterprise.notification', 1, 'zh_CN', '用户加入企业，用户自己的消息', '您已加入公司“${enterpriseName}”。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'enterprise.notification', 2, 'zh_CN', '发给企业其它所有成员', '${userName}已加入公司“${enterpriseName}”。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'enterprise.notification', 3, 'zh_CN', '拒绝加入公司', '您被拒绝加入公司“${enterpriseName}”。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'enterprise.notification', 4, 'zh_CN', '发给企业其它所有成员', '您已离开公司“${enterpriseName}”。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'enterprise.notification', 5, 'zh_CN', '发给企业其它所有成员', '${userName}已离开公司“${enterpriseName}”。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'enterprise.notification', 6, 'zh_CN', '发给申请加入企业的请求者', '${userName}申请加入公司“${enterpriseName}”。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'enterprise.notification', 7, 'zh_CN', '有人申请加入企业，发给企业管理员', '${userName}正申请加入公司“${enterpriseName}”，您同意此申请吗？');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'park.notification', 1, 'zh_CN', '有新的月卡发放通知排队用户申请月卡成功', '月卡申报成功，请于“${deadline}” 18:00前去领取，否则自动失效。');

INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (1000000, 'app.agreements.url', '/mobile/static/app_agreements/techpark_agreements.html', 'the relative path for techpark app agreements');
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (999999, 'app.agreements.url', '/mobile/static/app_agreements/xunmei_agreements.html', 'the relative path for techpark app agreements');
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `display_name`, `value`, `description`) VALUES (1000000, 'yzx.vcode.templateid', '科技园注册码短信模板', '18077', 'vcode sms template id(yxz)');
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `display_name`, `value`, `description`) VALUES (999999, 'yzx.vcode.templateid', '讯美注册码短信模板', '18086', 'vcode sms template id(xunmei)');
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `display_name`, `value`, `description`) VALUES (999995, 'yzx.vcode.templateid', '金隅嘉业注册码短信模板', '18091', 'vcode sms template id(jinyujiaye)');

INSERT INTO `eh_configurations` (`namespace_id`, `name`, `display_name`, `value`, `description`) VALUES (0, 'user.avatar.undisclosured.url', '用户头像(性别保密)', 'cs://1/image/aW1hZ2UvTVRvME1qVTBZalpqT1dGa05USm1aVEE1WVRnMU9EWmhOVE0zTm1Nd1pXSTVZUQ', '性别保密用户的默认头像');

INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'enterprise', '10001', 'zh_CN', '公司不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'parking', '10001', 'zh_CN', '车牌号位数错误');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'parking', '10002', 'zh_CN', '该车牌已有月卡');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'parking', '10003', 'zh_CN', '该车牌已申请月卡');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'parking', '10004', 'zh_CN', '服务器忙');

INSERT INTO `eh_namespaces`(`id`, `name`) VALUES(1000000, '科技园版');
INSERT INTO `eh_namespaces`(`id`, `name`) VALUES(999999, '讯美园区版');
INSERT INTO `eh_namespaces`(`id`, `name`) VALUES(999998, '华为园区版');
INSERT INTO `eh_namespaces`(`id`, `name`) VALUES(999997, '左邻服务版');
INSERT INTO `eh_namespaces`(`id`, `name`) VALUES(999996, '上海联通版');
INSERT INTO `eh_namespaces`(`id`, `name`) VALUES(999995, '金隅嘉业版');
INSERT INTO `eh_namespaces`(`id`, `name`) VALUES(999994, '龙岗智慧社区');
INSERT INTO `eh_namespaces`(`id`, `name`) VALUES(999993, '海岸物业');
INSERT INTO `eh_namespaces`(`id`, `name`) VALUES(999992, '深业物业');
INSERT INTO `eh_namespaces`(`id`, `name`) VALUES(999991, '金地威新');
INSERT INTO `eh_namespaces`(`id`, `name`) VALUES(999990, '储能大厦');

-- INSERT INTO `eh_acl_privileges` (`app_id`,`name`,`description`) VALUES(32, '设置组的管理员，普通成员的增删改Group_member_mgt', '设置组的管理员，普通成员的增删改');
-- INSERT INTO `eh_acl_privileges` (`app_id`,`name`,`description`) VALUES(32, 'Group_mgt', '组的增删改');
-- INSERT INTO `eh_acl_privileges` (`app_id`,`name`,`description`) VALUES(32, 'Post_mgt', '帖子的增删改，置顶等');
-- INSERT INTO `eh_acl_privileges` (`app_id`,`name`,`description`) VALUES(32, 'Task_mgt', '分配任务、修改任务状态、关闭任务、评论任务');
-- INSERT INTO `eh_acl_privileges` (`app_id`,`name`,`description`) VALUES(32, 'Accounting', '1.物业账单的上传、缴费状态的修改等;2.电商收入管理');
-- INSERT INTO `eh_acl_privileges` (`app_id`,`name`,`description`) VALUES(32, 'Property_mgt', '1.楼栋的增删改查;2.招商、入驻申请处理（现在没有该功能）');
-- INSERT INTO `eh_acl_privileges` (`app_id`,`name`,`description`) VALUES(32, 'Sevice_operator', '会议室预定、停车位预定、电子屏预定、停车充值');
-- INSERT INTO `eh_acl_privileges` (`app_id`,`name`,`description`) VALUES(32, 'Banner_input', '创建banner，只能创建，不能审核和发布');
-- INSERT INTO `eh_acl_privileges` (`app_id`,`name`,`description`) VALUES(32, 'Banner_publish', '审核和发布banner');
-- INSERT INTO `eh_acl_privileges` (`app_id`,`name`,`description`) VALUES(32, 'Market_mgt', '服务广场配置哪些服务');

-- INSERT INTO `eh_acl_privileges` (`app_id`,`name`,`description`) VALUES(10001, 32, '机构所有权限', '机构所有权限');

INSERT INTO `eh_acl_roles` (`id`, `app_id`,`name`,`description`) VALUES(1001, 32, '所有权限', '所有权限（All rights）');
INSERT INTO `eh_acl_roles` (`id`, `app_id`,`name`,`description`) VALUES(1002, 32, '任务管理', '任务管理（Task_mgt）');
INSERT INTO `eh_acl_roles` (`id`, `app_id`,`name`,`description`) VALUES(1003, 32, '财务管理', '财务管理（Accounting）');
INSERT INTO `eh_acl_roles` (`id`, `app_id`,`name`,`description`) VALUES(1004, 32, '招商管理', '招商管理（Property_mgt）');
INSERT INTO `eh_acl_roles` (`id`, `app_id`,`name`,`description`) VALUES(1005, 32, '组人员管理', '组人员管理（Group_member_mgt）');
INSERT INTO `eh_acl_roles` (`id`, `app_id`,`name`,`description`) VALUES(1006, 32, '服务管理', '服务管理（Sevice_operator）');
INSERT INTO `eh_acl_roles` (`id`, `app_id`,`name`,`description`) VALUES(1007, 32, '客服管理', '客服管理（ke fu）');

-- INSERT INTO `eh_acls` (`id`, `owner_type`,`grant_type`,`privilege_id`,`role_id`,`creator_uid`,`create_time`) VALUES(10001, 'EhOrganizations', 1, 10001, 1001, 1, '2015-11-14 19:16:27');
-- INSERT INTO `eh_acls` (`id`, `owner_type`,`grant_type`,`privilege_id`,`role_id`,`creator_uid`,`create_time`) VALUES(10002, 'EhOrganizations', 1, 10001, 1002, 1, '2015-11-14 19:16:27');
-- INSERT INTO `eh_acls` (`id`, `owner_type`,`grant_type`,`privilege_id`,`role_id`,`creator_uid`,`create_time`) VALUES(10003, 'EhOrganizations', 1, 10001, 1003, 1, '2015-11-14 19:16:27');
-- INSERT INTO `eh_acls` (`id`, `owner_type`,`grant_type`,`privilege_id`,`role_id`,`creator_uid`,`create_time`) VALUES(10004, 'EhOrganizations', 1, 10001, 1004, 1, '2015-11-14 19:16:27');
-- INSERT INTO `eh_acls` (`id`, `owner_type`,`grant_type`,`privilege_id`,`role_id`,`creator_uid`,`create_time`) VALUES(10005, 'EhOrganizations', 1, 10001, 1005, 1, '2015-11-14 19:16:27');
-- INSERT INTO `eh_acls` (`id`, `owner_type`,`grant_type`,`privilege_id`,`role_id`,`creator_uid`,`create_time`) VALUES(10006, 'EhOrganizations', 1, 10001, 1006, 1, '2015-11-14 19:16:27');

alter table eh_version_realm drop index `u_eh_ver_realm`;
alter table eh_version_realm add unique key `u_eh_ver_realm` (`realm`,`namespace_id`);

insert into `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values('3','Android_Techpark',NULL,'2015-11-26 16:10:58');
insert into `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values('4','iOS_Techpark',NULL,'2015-11-26 16:10:59');
insert into `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values('5','Android_Xunmei',NULL,'2015-11-26 16:15:29');
insert into `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values('6','iOS_Xunmei',NULL,'2015-11-26 16:15:29');
insert into `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values('7','Android_Hwpark',NULL,'2015-11-26 16:15:29');
insert into `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values('8','iOS_Hwpark',NULL,'2015-11-26 16:15:29');
insert into `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values('9','Android_IService',NULL,'2015-11-26 16:15:29');
insert into `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values('10','iOS_IService',NULL,'2015-11-26 16:15:29');
insert into `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values('11','Android_ShUnicom',NULL,'2015-11-26 16:15:29');
insert into `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values('12','iOS_ShUnicom',NULL,'2015-11-26 16:15:29');
insert into `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values('13','Android_JYJY',NULL,'2015-11-26 16:15:29');
insert into `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values('14','iOS_JYJY',NULL,'2015-11-26 16:15:29');

insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) values(5,3,'-0.1','1048576','0','1.0.0','0','2015-11-26 16:10:59');
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) values(6,4,'-0.1','1048576','0','1.0.0','0','2015-11-26 16:10:59');
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) values(7,5,'-0.1','3145728','0','3.0.0','0','2015-11-26 16:15:29');
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) values(8,6,'-0.1','3145728','0','3.0.0','0','2015-11-26 16:15:29');
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) values(9,7,'-0.1','3145728','0','3.0.0','0','2015-11-26 16:15:29');
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) values(10,8,'-0.1','3145728','0','3.0.0','0','2015-11-26 16:15:29');
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) values(11,9,'-0.1','3145728','0','3.0.0','0','2015-11-26 16:15:29');
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) values(12,10,'-0.1','3145728','0','3.0.0','0','2015-11-26 16:15:29');
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) values(13,11,'-0.1','3145728','0','3.0.0','0','2015-11-26 16:15:29');
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) values(14,12,'-0.1','3145728','0','3.0.0','0','2015-11-26 16:15:29');
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) values(15,13,'-0.1','3145728','0','3.0.0','0','2015-11-26 16:15:29');
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) values(16,14,'-0.1','3145728','0','3.0.0','0','2015-11-26 16:15:29');



# 
# member of eh_groups partition
#
CREATE TABLE `eh_enterprise_details` ( 
	`id` BIGINT NOT NULL COMMENT 'id of the record', 
	`enterprise_id` BIGINT NOT NULL COMMENT 'group id', 
	`description` text COMMENT 'description', 
	`contact` VARCHAR(128) COMMENT 'the phone number',
	`address` VARCHAR(256) COMMENT 'address str', 
    `longitude` DOUBLE,
    `latitude` DOUBLE,
    `geohash` VARCHAR(32),
    `create_time` DATETIME,
	PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; 

# 
# member of global partition
#
CREATE TABLE `eh_enterprise_op_requests` ( 
	`id` BIGINT NOT NULL COMMENT 'id of the record', 
    `namespace_id` INTEGER NOT NULL DEFAULT 0,
    `community_id` BIGINT NOT NULL DEFAULT 0,
    `source_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'enterprise, marker zone',
    `source_id` BIGINT NOT NULL DEFAULT 0,
	`enterprise_name` VARCHAR(128) NOT NULL COMMENT 'enterprise name', 
    `enterprise_id` BIGINT NOT NULL DEFAULT 0,
	`apply_contact` VARCHAR(128) COMMENT 'contact phone', 
	`apply_user_id` BIGINT COMMENT 'user id', 
	`apply_user_name` VARCHAR(128) COMMENT 'apply user name', 
	`apply_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'apply type 1:apply 2:The expansion of rent 3:Renew', 
	`description` TEXT COMMENT 'description', 
	`size_unit` TINYINT COMMENT '1:singleton 2:square meters', 
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: requesting, 2: accepted',
	`area_size` DOUBLE, 
	`create_time` DATETIME, 
    `operator_uid` BIGINT,
    `process_message` TEXT,
    `process_time` DATETIME,
	PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global partition
#

-- DROP TABLE `eh_lease_promotions` ;

CREATE TABLE `eh_lease_promotions` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT NOT NULL DEFAULT 0,
  `rent_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'For rent',
  `poster_uri` VARCHAR(128),
  `subject` VARCHAR(512),
  `rent_areas` VARCHAR(1024),
  `description` text,
  `create_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: active',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


#
# member of global partition
#
CREATE TABLE `eh_lease_promotion_attachments` ( 
	`id` BIGINT NOT NULL COMMENT 'id of the record', 
	`lease_id` BIGINT NOT NULL DEFAULT '0', 
	`content_type` VARCHAR(32) COMMENT 'attachment object content type', 
	`content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage', 
	`creator_uid` BIGINT, 
	`create_time` DATETIME, 
	PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


ALTER TABLE `eh_activities` ADD COLUMN `guest` VARCHAR(2048) ;

-- 20151130
ALTER TABLE `eh_enterprise_addresses` ADD COLUMN `building_id` BIGINT NOT NULL DEFAULT 0; 
ALTER TABLE `eh_enterprise_addresses` ADD COLUMN `building_name` VARCHAR(128);

-- 20151202
ALTER TABLE `eh_lease_promotions` ADD COLUMN `building_id` BIGINT NOT NULL DEFAULT 0; 
ALTER TABLE `eh_lease_promotions` ADD COLUMN `rent_position` VARCHAR(128) COMMENT 'rent position'; 
ALTER TABLE `eh_lease_promotions` ADD COLUMN `contacts` VARCHAR(128); 
ALTER TABLE `eh_lease_promotions` ADD COLUMN `contact_phone` VARCHAR(128); 
ALTER TABLE `eh_lease_promotions` ADD COLUMN `enter_time` DATETIME COMMENT 'enter time'; 

-- 20151211 by xiongying
-- ALTER TABLE `eh_park_apply_card` ADD COLUMN `company_name` VARCHAR(256) NOT NULL DEFAULT '';


ALTER TABLE `eh_enterprise_op_requests` MODIFY COLUMN apply_user_name VARCHAR(128) COMMENT 'apply user name';

ALTER TABLE `eh_version_realm` DROP COLUMN `namespace_id`;
ALTER TABLE `eh_version_upgrade_rules` DROP COLUMN `namespace_id`;

ALTER TABLE `eh_forum_posts` ADD COLUMN `tag` VARCHAR(32);

ALTER TABLE `eh_locale_templates` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;

INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(0, 'sms.default', 1, 'zh_CN', '验证码', '您的验证码为${vcode}，10分钟内有效，感谢您的使用。');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(0, 'sms.default.yzx', 1, 'zh_CN', '验证码-左邻', '9547');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(1000000, 'sms.default.yzx', 1, 'zh_CN', '验证码-科技园', '18077');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999999, 'sms.default.yzx', 1, 'zh_CN', '验证码-讯美', '18086');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999995, 'sms.default.yzx', 1, 'zh_CN', '验证码-金隅嘉业', '18091');


-- 给物业维修人员发短信 （没用到）
-- INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(0, 'sms.default', 2, 'zh_CN', '给物业维修人员发短信-左邻', '管理员{1}给您分配了{2}帖的任务，管理员电话{3}，请尽快与管理员联系处理问题。');
-- INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(0, 'sms.default.yzx', 2, 'zh_CN', '给物业维修人员发短信-左邻', '10832');
-- INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(1000000, 'sms.default.yzx', 2, 'zh_CN', '给物业维修人员发短信-科技园', '18529');
-- INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999999, 'sms.default.yzx', 2, 'zh_CN', '给物业维修人员发短信-讯美', '18530');
-- INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999995, 'sms.default.yzx', 2, 'zh_CN', '给物业维修人员发短信-金隅嘉业', '18532');

-- 账单信息
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(0, 'sms.default', 3, 'zh_CN', '账单信息', '您${year}年${month}月物业账单为:本月金额￥${dueAmount}，往期欠款￥${oweAmount}，本月实付金额￥${payAmount}，应付金额￥${balance}。${description}请尽快使用左邻缴纳物业费。');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(0, 'sms.default.yzx',3, 'zh_CN', '账单信息', '');

-- 给被分配人员发短信
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(0, 'sms.default', 4, 'zh_CN', '给被分配任务人员发短信-左邻', '业主${phone}发布了新的${topicType}帖，您已被分配处理该业主的需求，请尽快联系该业主。');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(0, 'sms.default.yzx', 4, 'zh_CN', '给被分配任务人员发短信-左邻', '10832');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(1000000, 'sms.default.yzx', 4, 'zh_CN', '给被分配任务人员发短信-科技园', '18529');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999999, 'sms.default.yzx', 4, 'zh_CN', '给被分配任务人员发短信-讯美', '18530');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999995, 'sms.default.yzx', 4, 'zh_CN', '给被分配任务人员发短信-金隅嘉业', '18532');

-- 物业一键推送消息
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(0, 'sms.default', 5, 'zh_CN', '物业一键推送消息', '左邻温馨提醒您：${msg}祝您生活愉快。');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(0, 'sms.default.yzx', 5, 'zh_CN', '物业一键推送消息', '');

INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (999999, 'sms.itemName.type', 'YZX', 'sms itemName');

ALTER TABLE `eh_organizations` ADD COLUMN `department_type` VARCHAR(64);

SET foreign_key_checks = 1;