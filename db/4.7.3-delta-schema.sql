-- by dengs,第三方认证记录表
-- DROP TABLE IF EXISTS `eh_authorization_third_party_records`;
CREATE TABLE `eh_authorization_third_party_records` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'Ehnamespace',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `flow_case_id` BIGINT,
  `type` VARCHAR(128) COMMENT '1, personal authorization, 2, organization authorization',
  `phone` VARCHAR(128) COMMENT 'phone',
  `name` VARCHAR(128) COMMENT 'user name',
  `certificate_type` VARCHAR(128)  COMMENT '1.id card',
  `certificate_no` VARCHAR(128)  COMMENT 'id card number',
  `organization_code` VARCHAR(128)  COMMENT 'organization Code',
  `organization_contact` VARCHAR(128)  COMMENT 'organization Contact',
  `organization_phone` VARCHAR(128)  COMMENT 'organization Phone',
  `error_code` INTEGER COMMENT '200 success,201 invaild param,202 unrent',
  `address_id` BIGINT  COMMENT 'authorization success, and save address id',
  `full_address` VARCHAR(256)  COMMENT 'authorization success, and save full_address',
  `user_count` INTEGER  COMMENT 'authorization success, and save user_count',
  `result_json` TEXT,
  `status` TINYINT,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- by dengs,表单与第三方namespace关联表
-- DROP TABLE IF EXISTS `eh_authorization_third_party_forms`;
CREATE TABLE `eh_authorization_third_party_forms` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'Ehnamespace',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `source_type` VARCHAR(32) COMMENT 'zj_personal_auth zj_organization_auth,form ownertype',
  `source_id` BIGINT COMMENT 'form owner id',
  `authorization_url` VARCHAR(512) COMMENT 'third party authorization url',
  `app_key` VARCHAR(128)  COMMENT 'app key',
  `secret_key` VARCHAR(512)  COMMENT 'secret_key',
  `title` VARCHAR(512)  COMMENT 'form title',
  `detail` VARCHAR(512)  COMMENT 'form detail',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- by dengs,家庭按钮状态表
-- DROP TABLE IF EXISTS `eh_authorization_third_party_buttons`;
CREATE TABLE `eh_authorization_third_party_buttons` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'Ehnamespace',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `title` VARCHAR(128) COMMENT 'ren cai gong yu',
  `modify_flag` TINYINT COMMENT '0,hidden,1,show',
  `families_flag` TINYINT COMMENT '0,hidden,1,show',
  `qrcode_flag` TINYINT COMMENT '0,hidden,1,show',
  `delete_flag` TINYINT COMMENT '0,hidden,1,show',
  `blank_detail` VARCHAR(128) COMMENT 'ni hai mei jia ru jia ting',
  `button_detail` VARCHAR(128) COMMENT 'shen qing ren zheng ',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- by dengs,车辆放行
ALTER TABLE eh_parking_clearance_logs ADD COLUMN `log_token` VARCHAR(1024) DEFAULT NULL COMMENT 'The info from third';
ALTER TABLE eh_parking_clearance_logs ADD COLUMN `log_json` TEXT DEFAULT NULL COMMENT 'The info from third';

--by xiongying
ALTER TABLE eh_equipment_inspection_standards ADD COLUMN `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, etc';
ALTER TABLE eh_equipment_inspection_standards ADD COLUMN `target_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to the id of who own the standard';