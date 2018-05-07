-- 客户管理增加企业管理字段 hotline  post_uri refer to  organization_detail    unified_social_credit_code  refer to organization
ALTER  TABLE  eh_enterprise_customers  ADD  COLUMN  `hotline` varchar(256) DEFAULT NULL;
ALTER  TABLE  eh_enterprise_customers  ADD  COLUMN  `post_uri` varchar(128) DEFAULT NULL;
ALTER  TABLE  eh_enterprise_customers  ADD  COLUMN  `unified_social_credit_code` varchar(256) DEFAULT NULL;
ALTER  TABLE  eh_enterprise_customers  ADD  COLUMN  `admin_flag` TINYINT(4) NOT NULL DEFAULT 0;



-- 客户增加附件表  by jiarui
CREATE TABLE `eh_enterprise_customer_attachments` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `customer_id` bigint(20) NOT NULL DEFAULT '0',
  `content_type` varchar(32) DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` varchar(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
  `creator_uid` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 客户增加企业管理员记录表   by jiarui
CREATE TABLE `eh_enterprise_customer_admins` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `customer_id` bigint(20) NOT NULL DEFAULT '0',
  `contact_name` varchar(256) DEFAULT NULL,
  `contact_token` varchar(256) DEFAULT NULL,
  `contact_type` varchar(256) DEFAULT NULL,
  `creator_uid` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

