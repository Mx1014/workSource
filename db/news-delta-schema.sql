
ALTER TABLE `eh_rentalv2_orders` ADD COLUMN `rental_type` tinyint(4) DEFAULT NULL;

ALTER TABLE eh_rentalv2_resources DROP COLUMN `cell_begin_id`;
ALTER TABLE eh_rentalv2_resources DROP COLUMN `cell_end_id`;



-- 增加咨询电话 by st.zheng
ALTER TABLE `eh_news` ADD COLUMN `phone` BIGINT(20) NULL DEFAULT '0' AFTER `source_url`;

-- 新建标签表 by st.zheng
CREATE TABLE `eh_news_tag` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT '0',
  `parent_id` bigint(20)  DEFAULT '0',
  `value` varchar(32) DEFAULT NULL,
  `is_search` tinyint(8)  DEFAULT '0' COMMENT '是否开启筛选',
  `is_default` tinyint(8)  DEFAULT '0' COMMENT '是否是默认选项',
  `delete_flag` tinyint(8) NOT NULL DEFAULT '0',
  `default_order` bigint(20)  DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_news_tag_vals` (
  `id` BIGINT(20) NOT NULL,
  `news_id` BIGINT(20) NOT NULL DEFAULT '0',
  `news_tag_id` BIGINT(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`));

-- 园区地图add by sw 20171009
CREATE TABLE `eh_community_map_shops` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT 0,
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the type, community, enterprise, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT 0,
  `shop_name` varchar(128) DEFAULT NULL,
  `shop_type` varchar(128) DEFAULT NULL,
  `business_hours` varchar(512) DEFAULT NULL,
  `contact_name` varchar(128) DEFAULT NULL,
  `contact_phone` varchar(128) DEFAULT NULL,
  `building_id` bigint(20) NOT NULL,
  `address_id` bigint(20) NOT NULL,
  `description` text,
  `shop_Avatar_uri` varchar(1024) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT 0,
  `create_time` datetime DEFAULT NULL,
  `update_uid` bigint(20) NOT NULL DEFAULT 0,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- merge from activity-3.4.0 by yanjun 20171009 start
-- 在帖子表中增加一个字段，标识这个帖子是否是克隆帖子   add by yanjun 20170805
ALTER TABLE `eh_forum_posts` ADD COLUMN `clone_flag`  tinyint(4) NULL COMMENT 'clone_flag post 0-real post, 1-clone post';
ALTER TABLE `eh_forum_posts` ADD COLUMN `real_post_id`  bigint(20) NULL COMMENT 'if this is clone post, then it should have a real post id';

ALTER TABLE `eh_activities` ADD COLUMN `clone_flag`  tinyint(4) NULL COMMENT 'clone_flag post 0-real post, 1-clone post';

-- -- 活动报名导入错误信息  add by yanjun 20170828
-- CREATE TABLE `eh_activity_roster_error` (
--   `id` bigint(20) NOT NULL COMMENT 'id',
--   `uuid` varchar(36) DEFAULT NULL COMMENT 'uuid',
--   `job_id` bigint(20) DEFAULT NULL COMMENT 'jobId, one job may has several error',
--   `row_num` int(11) DEFAULT NULL COMMENT 'row_num',
--   `description` varchar(255) DEFAULT NULL COMMENT 'description',
--   `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
--   `create_uid` bigint(20) DEFAULT NULL,
--   PRIMARY KEY (`id`),
--   KEY `eh_activity_roster_error_jobId` (`job_id`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_warning_settings` ADD COLUMN `category_id`  bigint(22) NULL COMMENT '入口id';

ALTER TABLE `eh_roster_order_settings` ADD COLUMN `category_id`  bigint(22) NULL COMMENT 'category_id';

ALTER TABLE `eh_hot_tags` ADD COLUMN `category_id`  bigint(22) NULL ;

-- merge from activity-3.4.0 by yanjun 20171009 end

-- merge form archives-1.4 by R 20171010 start
ALTER TABLE eh_organization_member_details CHANGE dimission_time dismiss_time DATE;
ALTER TABLE eh_organization_member_details ADD COLUMN region_code VARCHAR(64) COMMENT '手机区号';
-- merge form archives-1.4 by R 20171010 end

-- bydengs,20171011,添加一个属性，存跳转的路由
ALTER TABLE `eh_service_alliances` ADD COLUMN `jump_service_alliance_routing` VARCHAR(2048) COMMENT 'jump to other service alliance routing';