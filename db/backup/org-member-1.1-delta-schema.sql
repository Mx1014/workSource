--
-- 用户认证记录表:记录用户加入/退出企业的log
--

CREATE TABLE `eh_organization_member_logs` (
  `id` BIGINT(20)  COMMENT 'id of the record',
  `namespace_id` INT(11) DEFAULT '0',
  `organization_id` BIGINT(20) , 
  `user_id` BIGINT(20) COMMENT 'organization member target id (type user)',
  `contact_name` VARCHAR(64) DEFAULT NULL,
  `contact_type` TINYINT(4) DEFAULT '0' COMMENT '0: mobile, 1: email',
  `contact_token` VARCHAR(128) COMMENT 'phone number or email address',
  `operation_type` TINYINT(4) DEFAULT '0' COMMENT '0-退出企业 1-加入企业',
  `request_type` TINYINT(4) DEFAULT '0' COMMENT '0-管理员操作 1-用户操作',
  `operate_time` DATETIME , 
  `operator_uid` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 增加字段  `eh_users`
ALTER TABLE `eh_users` ADD COLUMN `executive_tag` TINYINT(4) DEFAULT '0' COMMENT '0-不是高管 1-是高管';
ALTER TABLE `eh_users` ADD COLUMN `position_tag` VARCHAR(128) DEFAULT NULL COMMENT '职位';
ALTER TABLE `eh_users` ADD COLUMN `identity_number_tag` VARCHAR(20) DEFAULT NULL COMMENT '身份证号';