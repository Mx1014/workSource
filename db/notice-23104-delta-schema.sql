-- 公告管理 add by zhiwei.zhang

-- 企业公告表
-- DROP TABLE IF EXISTS `eh_enterprise_notices`;
CREATE TABLE `eh_enterprise_notices` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64) NOT NULL COMMENT '默认EhOrganizations',
  `owner_id` BIGINT NOT NULL,
  `title` VARCHAR(256) NOT NULL COMMENT '企业公告标题',
  `summary` VARCHAR(512) COMMENT '摘要',
  `content_type` VARCHAR(32),
  `content` TEXT COMMENT '公告正文',
  `publisher` VARCHAR(256) COMMENT '公告发布者',
  `secret_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '状态 : 0-(PUBLIC)公开, 1-(PRIVATE)保密',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态 : 0-(DELETED)已删除, 1-(DRAFT)草稿, 2-(ACTIVE)已发送, 3-(INACTIVE)已撤销',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  `operator_name` VARCHAR(128) COMMENT 'the name of the operator',
  `delete_uid` BIGINT,
  `delete_time` DATETIME,

  PRIMARY KEY (`id`),
  KEY `i_notices_namespace_id`(`namespace_id`),
  KEY `i_notices_create_time`(`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

-- 企业公告附件表
-- DROP TABLE IF EXISTS `eh_enterprise_notice_attachments`;
CREATE TABLE `eh_enterprise_notice_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `notice_id` BIGINT NOT NULL COMMENT 'key of the table eh_enterprise_notices',
  `content_name` VARCHAR(256) NOT NULL COMMENT 'the name of the content',
  `content_suffix` VARCHAR(64) COMMENT 'the suffix of the file',
  `size` INT NOT NULL DEFAULT 0 COMMENT 'the size of the content',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-invalid, 1-valid',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`),
  KEY `i_notice_attachment_notice_id`(`notice_id`)
) ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;


-- 企业公告发送信息表
-- DROP TABLE IF EXISTS `eh_enterprise_notice_receivers`;
CREATE TABLE `eh_enterprise_notice_receivers` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `notice_id` BIGINT NOT NULL COMMENT 'key of table the eh_enterprise_notices',
  `receiver_type` VARCHAR(64) NOT NULL COMMENT 'DEPARTMENT OR MEMBER',
  `receiver_id` BIGINT NOT NULL,
  `name` VARCHAR(128),
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-invalid, 1-valid',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`),
  KEY `i_notice_receivers_notice_id`(`notice_id`),
  KEY `i_notice_receivers_receiver_id`(`receiver_type`,`receiver_id`)
) ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

-- end by zhiwei.zhang