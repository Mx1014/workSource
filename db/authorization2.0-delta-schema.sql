-- by dengs,你大爷的，搞成这样子。
-- DROP TABLE IF EXISTS `eh_user_authorization`;
CREATE TABLE `eh_user_authorization` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `namespace_id` INTEGER,
  `type` VARCHAR(128) COMMENT '1, personal authorization, 2, organization authorization',
  `phone` VARCHAR(128) COMMENT 'phone',
  `name` VARCHAR(128) COMMENT 'user name',
  `certificateType` VARCHAR(128)  COMMENT '1.id card',
  `certificateNo` VARCHAR(128)  COMMENT 'id card number',
  `organizationCode` VARCHAR(128)  COMMENT 'organization Code',
  `organizationContact` VARCHAR(128)  COMMENT 'organization Contact',
  `organizationPhone` VARCHAR(128)  COMMENT 'organization Phone',
  `errorCode` TINYINT COMMENT '200 success,201 invaild param,202 unrent',
  `address_id` BIGINT  COMMENT 'authorization success, and save address id',
  `full_address` VARCHAR(256)  COMMENT 'authorization success, and save full_address',
  `user_count` INTEGER  COMMENT 'authorization success, and save user_count',
  `result_json` TEXT,
  `creator_uid` BIGINT,
  `create_time` TimeStamp,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
