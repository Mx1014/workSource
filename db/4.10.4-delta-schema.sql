
-- 薪酬组加上版本 By lei.lv 
ALTER TABLE eh_uniongroup_configures ADD COLUMN `version_code` INT DEFAULT 0 COMMENT '版本号';
ALTER TABLE eh_uniongroup_member_details ADD COLUMN `version_code` INT DEFAULT 0 COMMENT '版本号';
 -- 更改索引
ALTER TABLE `eh_uniongroup_member_details`  DROP INDEX `uniongroup_member_uniqueIndex`;
ALTER TABLE eh_uniongroup_member_details ADD UNIQUE INDEX `uniongroup_member_uniqueIndex` (`group_type`, `group_id`, `detail_id`, `contact_token`,`version_code`) ;

ALTER TABLE `eh_punch_schedulings` ADD COLUMN `status` TINYINT DEFAULT 2 COMMENT ' 规则状态 1-已删除 2-正常 3-次日更新 4-新规则次日生效';  

-- 打卡加入当前生效版本号
-- ALTER TABLE eh_punch_rules ADD COLUMN `version_code` INT DEFAULT 0 COMMENT '当前生效版本号';

-- 维护uniongroup现在使用的是哪个version
-- drop TABLE `eh_uniongroup_version`;
CREATE TABLE `eh_uniongroup_version` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INT(11) NOT NULL DEFAULT '0',
  `enterprise_id` BIGINT(20) DEFAULT '0',
  `group_type` VARCHAR(32) DEFAULT NULL COMMENT 'SalaryGroup,PunchGroup', 
  `current_version_code` INT(11) DEFAULT '0' COMMENT '当前使用的版本号 从1开始 , 0默认是config版本', 
  `operator_uid` BIGINT(20) DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- by dengs,问卷调查1.1
ALTER TABLE `eh_questionnaires` ADD COLUMN `cut_off_time` DATETIME DEFAULT now() COMMENT '问卷截止日期'  AFTER `publish_time`;
ALTER TABLE `eh_questionnaires` ADD COLUMN `user_scope` TEXT COMMENT '需要填写的问卷调查的用户[userid,nickname|userid,nickname]' AFTER `cut_off_time`;
ALTER TABLE `eh_questionnaires` ADD COLUMN `support_share` TINYINT COMMENT '是否支持分享, 0:不支持分享,2:支持分享' AFTER `cut_off_time`;
ALTER TABLE `eh_questionnaires` ADD COLUMN `support_anonymous` TINYINT COMMENT '是否支持匿名, 0:不支持匿名,2:支持匿名' AFTER `cut_off_time`;
ALTER TABLE `eh_questionnaires` ADD COLUMN `target_type` VARCHAR(32) DEFAULT 'organization' COMMENT '调查对象 organization:企业 user:个人' AFTER `cut_off_time`;
ALTER TABLE `eh_questionnaires` ADD COLUMN `poster_uri` VARCHAR(1024) COMMENT '问卷调查的封面uri' AFTER `cut_off_time`;
ALTER TABLE `eh_questionnaires` ADD COLUMN `target_user_num` INTEGER COMMENT '目标用户收集数量' AFTER `cut_off_time`;
ALTER TABLE `eh_questionnaires` ADD COLUMN `scope_sent_message_users` TEXT COMMENT '已发送消息的用户列表（发布时发送的消息用户）' AFTER `target_user_num`;
ALTER TABLE `eh_questionnaires` ADD COLUMN `scope_resent_message_users` TEXT COMMENT '问卷到期前发送消息的用户列表（问卷到期一天前发送的消息用户）' AFTER `target_user_num`;


ALTER TABLE `eh_questionnaire_answers` ADD COLUMN `target_from` TINYINT COMMENT '用户来源（1:app，2:wx）' AFTER `target_name`;
ALTER TABLE `eh_questionnaire_answers` ADD COLUMN `target_phone` VARCHAR(128) COMMENT '用户电话' AFTER `target_name`;
ALTER TABLE `eh_questionnaire_answers` ADD COLUMN `anonymous_flag` TINYINT DEFAULT 0 COMMENT '是否匿名回答, 0:不是匿名回答,2:是匿名回答' AFTER `target_name`;

-- 问卷调查范围表
-- DROP TABLE IF EXISTS  `eh_questionnaire_ranges`;
CREATE TABLE `eh_questionnaire_ranges` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
	`questionnaire_id` BIGINT NOT NULL COMMENT '关联问卷调查的id',
  `community_id` BIGINT COMMENT '园区id，查询楼栋（range_type=building）下的企业的时候，使用的是楼栋的名称查询，这里必须保存community一起查询才正确。',
  `range_type` VARCHAR(64) COMMENT 'community_all(项目),community_authenticated(项目下已认证的用户),community_unauthorized(未认证),building(楼栋),enterprise(企业),user 范围类型',
  `range` VARCHAR(512) COMMENT '对应项目id,楼栋名称，企业ID，用户id',
	`range_description` VARCHAR(1024) COMMENT '范围描述信息，用于显示在问卷详情页',
	`rid` BIGINT COMMENT '范围为building的时候，存buildingid，给web做逻辑，后端没有必要存储',
  `status` TINYINT NOT NULL COMMENT '0. inactive, 1. draft, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 版本号  add by xq.tian  2017/10/26
ALTER TABLE eh_version_urls ADD COLUMN version_encoded_value BIGINT NOT NULL DEFAULT 0;


-- merge from forum-2.4 add by yanjun 201710311836

CREATE TABLE `eh_forum_categories` (
  `id` bigint(20) NOT NULL,
  `uuid` varchar(128) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `forum_id` bigint(20) NOT NULL COMMENT 'forum id',
  `entry_id` bigint(20) NOT NULL COMMENT 'entry id',
  `name` varchar(255) DEFAULT NULL,
  `activity_entry_id` bigint(20) DEFAULT '0' COMMENT 'activity entry id',
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 是否支持评论功能
CREATE TABLE `eh_interact_settings` (
  `id` bigint(20) NOT NULL,
  `uuid` varchar(128) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `forum_id` bigint(20) NOT NULL,
  `type` varchar(32) NOT NULL COMMENT 'forum, activity, announcement',
  `entry_id` bigint(20) DEFAULT NULL,
  `interact_flag` tinyint(4) NOT NULL COMMENT 'support interact, 0-no, 1-yes',
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_forum_posts` ADD COLUMN `forum_entry_id`  bigint(20) NULL DEFAULT 0 COMMENT 'forum_category  entry_id' ;

ALTER TABLE `eh_forum_posts` ADD COLUMN `interact_flag`  tinyint(4) NOT NULL DEFAULT 1 COMMENT 'support interact, 0-no, 1-yes' ;

ALTER TABLE `eh_forum_posts` ADD COLUMN `stick_time`  datetime NULL;
ALTER TABLE `eh_activities` ADD COLUMN `stick_time`  datetime NULL;

-- merge from forum-2.4 add by yanjun 201710311836

-- 增加缴费的工作id   wentian
ALTER TABLE `eh_payment_bills` ADD COLUMN `next_switch` TINYINT DEFAULT 0 COMMENT '下一次switch的值';
ALTER TABLE `eh_payment_contract_receiver` ADD COLUMN `in_work` TINYINT DEFAULT 0 COMMENT '0:工作完成；1：正在生成';
ALTER TABLE `eh_payment_contract_receiver` ADD COLUMN `is_recorder` TINYINT DEFAULT 1 COMMENT '0：合同状态记录者，不保存计价数据；1：不是合同状态记录者';


-- fix customer相关导入数据的状态 add by xiongying20171102
ALTER TABLE eh_customer_apply_projects MODIFY COLUMN status TINYINT NOT NULL DEFAULT 2;
ALTER TABLE eh_customer_certificates MODIFY COLUMN status TINYINT NOT NULL DEFAULT 2;
ALTER TABLE eh_customer_commercials MODIFY COLUMN status TINYINT NOT NULL DEFAULT 2;
ALTER TABLE eh_customer_economic_indicators MODIFY COLUMN status TINYINT NOT NULL DEFAULT 2;
ALTER TABLE eh_customer_events MODIFY COLUMN status TINYINT NOT NULL DEFAULT 2;
ALTER TABLE eh_customer_investments MODIFY COLUMN status TINYINT NOT NULL DEFAULT 2;
ALTER TABLE eh_customer_patents MODIFY COLUMN status TINYINT NOT NULL DEFAULT 2;
ALTER TABLE eh_customer_talents MODIFY COLUMN status TINYINT NOT NULL DEFAULT 2;
ALTER TABLE eh_customer_tracking_plans MODIFY COLUMN status TINYINT NOT NULL DEFAULT 2;
ALTER TABLE eh_customer_trackings MODIFY COLUMN status TINYINT NOT NULL DEFAULT 2;
ALTER TABLE eh_customer_trademarks MODIFY COLUMN status TINYINT NOT NULL DEFAULT 2;
ALTER TABLE eh_enterprise_customers MODIFY COLUMN status TINYINT NOT NULL DEFAULT 2;
