-- by dengs,第三方认证记录表
-- DROP TABLE IF EXISTS `eh_authorization_thirdparty_records`;
CREATE TABLE `eh_authorization_third_party_records` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'Ehnamespace',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `type` VARCHAR(128) COMMENT '1, personal authorization, 2, organization authorization',
  `phone` VARCHAR(128) COMMENT 'phone',
  `name` VARCHAR(128) COMMENT 'user name',
  `certificateType` VARCHAR(128)  COMMENT '1.id card',
  `certificateNo` VARCHAR(128)  COMMENT 'id card number',
  `organizationCode` VARCHAR(128)  COMMENT 'organization Code',
  `organizationContact` VARCHAR(128)  COMMENT 'organization Contact',
  `organizationPhone` VARCHAR(128)  COMMENT 'organization Phone',
  `errorCode` INTEGER COMMENT '200 success,201 invaild param,202 unrent',
  `address_id` BIGINT  COMMENT 'authorization success, and save address id',
  `full_address` VARCHAR(256)  COMMENT 'authorization success, and save full_address',
  `user_count` INTEGER  COMMENT 'authorization success, and save user_count',
  `result_json` TEXT,
  `creator_uid` BIGINT,
  `create_time` TimeStamp,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- by dengs,表单与第三方namespace关联表
-- DROP TABLE IF EXISTS `eh_authorization_third_party_forms`;
CREATE TABLE `eh_authorization_third_party_forms` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'Ehnamespace',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `sourceType` VARCHAR(32) COMMENT 'zj_personal_auth zj_organization_auth,form ownertype',
  `sourceId` BIGINT COMMENT 'form owner id',
  `authorization_url` VARCHAR(512) COMMENT 'third party authorization url',
  `app_key` VARCHAR(128)  COMMENT 'app key',
  `secret_key` VARCHAR(512)  COMMENT 'secret_key',
  `creator_uid` BIGINT,
  `create_time` TimeStamp,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
