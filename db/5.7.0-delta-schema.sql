-- 通用脚本
-- ADD BY xq.tian
-- ISSUE-32697 运营统计重构
ALTER TABLE eh_terminal_hour_statistics ADD COLUMN cumulative_active_user_number BIGINT NOT NULL DEFAULT 0;

ALTER TABLE eh_terminal_day_statistics ADD COLUMN average_active_user_number BIGINT NOT NULL DEFAULT 0;
ALTER TABLE eh_terminal_day_statistics ADD COLUMN average_active_user_change_rate DECIMAL(10, 2) NOT NULL DEFAULT 0;

-- ISSUE-32697 END

-- 通用脚本  
-- AUTHOR: 黄良铭
-- REMARK: #Issue-33216 服务协议信息表
CREATE TABLE `eh_service_agreement` (

  `id` INT(11)  NOT NULL COMMENT '主键',
  `namespace_id` INT(11) NOT NULL  COMMENT '域空间ID',
  `agreement_content` MEDIUMTEXT  COMMENT '协议内容',

  PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '服务协议信息表';

-- #Issue-33216  end


-- 通用脚本
-- AUTHOR jiarui  20180625
-- REMARK 客户管理附件 企业信息V1.0
CREATE TABLE `eh_customer_attachments` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `name` VARCHAR(1024) DEFAULT  NULL ,
  `namespace_id` INT(11) NOT NULL COMMENT 'namespaceId',
  `customer_id` BIGINT(20) NOT NULL DEFAULT '0',
  `content_type` VARCHAR(32) DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
  `status` TINYINT(4) NOT NULL COMMENT '0:inactive 2:active',
  `creator_uid` BIGINT(20) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- end


-- 通用脚本 所有环境
-- AUTHOR wuhan 2018-7-12
-- REMARK issues-32611 社保bug:月份为空导致错误 先删除数据再修改表
DELETE FROM eh_social_security_payments WHERE pay_month IS NULL;
ALTER TABLE eh_social_security_payments CHANGE `pay_month` `pay_month` VARCHAR(8) NOT NULL COMMENT 'yyyymm'; 
-- end


