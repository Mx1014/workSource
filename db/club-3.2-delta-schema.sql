-- 行业协会类型
CREATE TABLE `eh_industry_types` (
  `id` bigint(20) NOT NULL,
  `uuid` varchar(128) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `name` varchar(32) NOT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_guild_applies` (
  `id` bigint(20) NOT NULL,
  `uuid` varchar(128) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `group_id` bigint(22) NOT NULL,
  `applicant_uid` bigint(22) NOT NULL,
  `group_member_id` bigint(22) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(18) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `organization_name` varchar(255) DEFAULT NULL,
  `registered_capital` varchar(255) DEFAULT NULL,
  `industry_type` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_uid` bigint(22) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 俱乐部类型，普通俱乐部、行业协会
ALTER TABLE `eh_group_settings` ADD COLUMN `club_type`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '0-normal club, 1-guild club' ;
-- 未加入俱乐部成员在俱乐部论坛的权限 0-不可见，1-可见，2-可交互
ALTER TABLE `eh_groups` ADD COLUMN `tourist_post_policy`  tinyint(4) NULL DEFAULT 2 COMMENT '0-hide, 1-see only, 2-interact';
-- 俱乐部类型，普通俱乐部、行业协会
ALTER TABLE `eh_groups` ADD COLUMN `club_type`  tinyint(4) NULL DEFAULT 0 COMMENT '0-normal club, 1-guild club' ;

ALTER TABLE `eh_groups` ADD COLUMN `phone_number`  varchar(18) NULL ;

