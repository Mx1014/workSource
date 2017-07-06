-- 企业人才消息推送者， add by tt, 20170705
-- DROP TABLE IF EXISTS `eh_talent_message_senders`;
CREATE TABLE `eh_talent_message_senders` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT,
  `organization_member_id` BIGINT NOT NULL,
  `user_id` BIGINT,
  `status` TINYINT NOT NULL COMMENT '0: inactive, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 企业人才申请记录表， add by tt, 20170705
-- DROP TABLE IF EXISTS `eh_talent_requests`;
CREATE TABLE `eh_talent_requests` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT,
  `requestor` VARCHAR(64),
  `phone` VARCHAR(64),
  `organization_name` ,
  `talent_id` BIGINT,
  `form_origin_id` BIGINT,
  `status` TINYINT NOT NULL COMMENT '0: inactive, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;