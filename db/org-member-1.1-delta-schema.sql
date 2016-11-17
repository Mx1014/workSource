--
-- 用户认证记录表:记录用户加入/退出企业的log
--

CREATE TABLE `eh_organization_user_logs` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `organization_id` BIGINT(20) NOT NULL, 
  `user_id` BIGINT(20) NOT NULL COMMENT 'organization member target id (type user)',
  `operation_type` TINYINT(4) DEFAULT '0' COMMENT '0-退出企业 1-加入企业',
  `operate_time` DATETIME DEFAULT NULL , 
  `operator_uid` BIGINT(20) NOT NULL,
  `namespace_id` INT(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=2119064 DEFAULT CHARSET=utf8mb4


-- 增加字段  
ALTER TABLE `eh_organization_members` ADD COLUMN `executive_flag` TINYINT(4) DEFAULT '0' COMMENT '0-不是高管 1-是高管',