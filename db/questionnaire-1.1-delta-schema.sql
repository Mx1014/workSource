-- by dengs,问卷调查1.1
ALTER TABLE `eh_questionnaires` ADD COLUMN `cut_off_time` DATETIME DEFAULT now() COMMENT '问卷截止日期'  AFTER `publish_time`;
ALTER TABLE `eh_questionnaires` ADD COLUMN `user_scope` TEXT COMMENT '需要填写的问卷调查的用户[userid,nickname|userid,nickname]' AFTER `cut_off_time`;
ALTER TABLE `eh_questionnaires` ADD COLUMN `support_share` TINYINT COMMENT '是否支持分享, 0:不支持分享,2:支持分享' AFTER `cut_off_time`;
ALTER TABLE `eh_questionnaires` ADD COLUMN `support_anonymous` TINYINT COMMENT '是否支持匿名, 0:不支持匿名,2:支持匿名' AFTER `cut_off_time`;
ALTER TABLE `eh_questionnaires` ADD COLUMN `target_type` VARCHAR(32) DEFAULT 'organization' COMMENT '调查对象 organization:企业 user:个人' AFTER `cut_off_time`;
ALTER TABLE `eh_questionnaires` ADD COLUMN `poster_uri` VARCHAR(1024) COMMENT '问卷调查的封面uri' AFTER `cut_off_time`;
ALTER TABLE `eh_questionnaires` ADD COLUMN `target_user_num` INTEGER COMMENT '目标用户收集数量' AFTER `cut_off_time`;

ALTER TABLE `eh_questionnaire_answers` ADD COLUMN `target_from` TINYINT COMMENT '用户来源（1:app，2:wx）' AFTER `target_name`;
ALTER TABLE `eh_questionnaire_answers` ADD COLUMN `target_phone` VARCHAR(128) COMMENT '用户电话' AFTER `target_name`;
ALTER TABLE `eh_questionnaire_answers` ADD COLUMN `anonymous_flag` TINYINT DEFAULT 0 COMMENT '是否匿名回答, 0:不是匿名回答,2:是匿名回答' AFTER `target_name`;

-- 问卷调查范围表
-- DROP TABLE IF EXISTS  `eh_questionnaire_ranges`;
CREATE TABLE `eh_questionnaire_ranges` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
	`questionnaire_id` BIGINT NOT NULL COMMENT '关联问卷调查的id',
  `community_id` BIGINT NOT NULL COMMENT '园区id，查询楼栋（range_type=building）下的企业的时候，使用的是楼栋的名称查询，这里必须保存community一起查询才正确。',
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
