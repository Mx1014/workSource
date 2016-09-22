--
-- 给eh_organization_owners增加一些客户资料管理增加的字段       by xq.tian
--
ALTER TABLE `eh_organization_owners` ADD COLUMN `registered_residence` VARCHAR(128) COMMENT 'registered residence';
ALTER TABLE `eh_organization_owners` ADD COLUMN `org_owner_type_id` BIGINT COMMENT '业主类型id';
ALTER TABLE `eh_organization_owners` ADD COLUMN `gender` TINYINT COMMENT '男,女';
ALTER TABLE `eh_organization_owners` ADD COLUMN `birthday` DATE COMMENT 'birthday';
ALTER TABLE `eh_organization_owners` ADD COLUMN `marital_status` VARCHAR(10);
ALTER TABLE `eh_organization_owners` ADD COLUMN `job` VARCHAR(10) COMMENT 'job';
ALTER TABLE `eh_organization_owners` ADD COLUMN `company` VARCHAR(100) COMMENT 'company';
ALTER TABLE `eh_organization_owners` ADD COLUMN `id_card_number` VARCHAR(18) COMMENT 'id card number';
ALTER TABLE `eh_organization_owners` ADD COLUMN `avatar` VARCHAR(128) COMMENT 'avatar';
ALTER TABLE `eh_organization_owners` ADD COLUMN `status` TINYINT COMMENT 'delete: 0, normal: 1';
ALTER TABLE `eh_organization_owners`  MODIFY COLUMN `address_id`  bigint(20) NULL COMMENT 'address id';

--
-- 创建eh_organization_owner_cars表,汽车管理的汽车表    by xq.tian
--
CREATE TABLE `eh_organization_owner_cars` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT,
  `brand` VARCHAR(20) COMMENT '汽车品牌',
  `parking_space` VARCHAR(20) COMMENT '停车位',
  `parking_type` TINYINT COMMENT '停车类型',
  `plate_number` VARCHAR(20) COMMENT '车牌号',
  `contacts` VARCHAR(20) COMMENT '联系人姓名',
  `contact_number` VARCHAR(20) COMMENT '联系电话',
  `content_uri` VARCHAR(20) COMMENT '照片uri',
  `color` VARCHAR(20) COMMENT '汽车颜色',
  `status` TINYINT COMMENT 'delete: 0, normal: 1',
  `create_time` DATETIME,
  `update_time` DATETIME,
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 创建eh_organization_owner与eh_address的多对多表    by xq.tian
--
CREATE TABLE `eh_organization_owner_address` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_owner_id` BIGINT,
  `address_id` BIGINT,
  `living_status` TINYINT,
  `auth_type` TINYINT COMMENT 'Auth type',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 创建eh_organization_owner_owner_car与eh_organization_owner_cars的多对多表    by xq.tian
--
CREATE TABLE `eh_organization_owner_owner_car` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_owner_id` BIGINT,
  `car_id` BIGINT,
  `primary_flag` TINYINT COMMENT 'primary flag, yes: 1, no: 0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 客户资料管理中的附件上传记录表    by xq.tian
--
CREATE TABLE `eh_organization_owner_attachments` (
  `id` BIGINT(20) NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT COMMENT 'organization owner id',
  `attachment_name` VARCHAR(100) COMMENT 'attachment name',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(256) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 车辆管理中的附件上传记录表    by xq.tian
--
CREATE TABLE `eh_organization_owner_car_attachments` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT COMMENT 'car id',
  `attachment_name` VARCHAR(100) COMMENT 'attachment name',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(256) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 客户的活动记录表   by xq.tian
--
CREATE TABLE `eh_organization_owner_behaviors` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT COMMENT 'organization owner id',
  `address_id` BIGINT COMMENT 'address id',
  `behavior_type` VARCHAR(20) COMMENT '迁入, 迁出..',
  `status` TINYINT COMMENT 'delete: 0, normal: 1',
  `behavior_time` DATETIME,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 客户类型表    by xq.tian
--
CREATE TABLE `eh_organization_owner_type` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `name` VARCHAR(20) COMMENT 'owner, tenant, relative, friend',
  `display_name` VARCHAR(20) COMMENT '业主, 租户, 亲戚, 朋友',
  `status` TINYINT COMMENT 'delete: 0, normal: 1',
  `create_time` DATETIME,
  `update_time` DATETIME,
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;