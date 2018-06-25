-- 客户管理附件 企业信息V1.0
-- by jiarui  20180625
CREATE TABLE `eh_customer_attachments` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `name` VARCHAR(1024) DEFAULT  NULL ,
  `namespace_id` INT(11) NOT NULL COMMENT 'namespaceId',
  `customer_id` BIGINT(20) NOT NULL DEFAULT '0',
  `content_type` VARCHAR(32) DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
  `status` TINYINT(4) NOT NULL COMMENT '0:inactive 2:active',
  `creator_uid` BIGINT(20) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
