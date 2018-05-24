-- 文档管理1.2 start by ryan.
ALTER TABLE `eh_file_management_catalog_scopes` ADD COLUMN `source_type` VARCHAR(64) COMMENT'the type of the source' AFTER `source_id`;
UPDATE `eh_file_management_catalog_scopes` SET source_type = 'MEMBERDETAIL';
-- 文档管理1.2 end by ryan.


-- issue-26471 服务热线V1.5（客服聊天记录保存和导出) by tianxiaoqiang 2018.5.8
CREATE TABLE `eh_message_records` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INT(11) DEFAULT '0',
  `dst_channel_token` VARCHAR(32) DEFAULT NULL,
  `dst_channel_type` VARCHAR(32) DEFAULT NULL,
  `status` VARCHAR(32) COMMENT 'message status',
  `app_id` BIGINT(20) DEFAULT '1' COMMENT 'default to messaging app itself',
  `message_seq` BIGINT(20) DEFAULT NULL COMMENT 'message sequence id generated at server side',
  `sender_uid` BIGINT(20) DEFAULT NULL,
  `sender_tag` VARCHAR(32) DEFAULT NULL COMMENT 'sender generated tag',
  `channels_info` VARCHAR(2048) DEFAULT NULL,
  `body_type`VARCHAR(32),
  `body` VARCHAR(2048),
  `delivery_option` INT(2) DEFAULT '0',
  `create_time` DATETIME NOT NULL COMMENT 'message creation time',
  `session_token` VARCHAR(128),
  `device_id` VARCHAR(2048),
  `index_id` BIGINT(20),
  PRIMARY KEY (`id`),
  INDEX `i_eh_index_id_index` (`index_id`) USING BTREE,
  INDEX `i_eh_sender_uid_index` (`sender_uid`) USING BTREE,
  INDEX `i_eh_dst_channel_token_index` (`dst_channel_token`) USING BTREE,
  INDEX `i_en_namespace_id_index` (`namespace_id`) USING BTREE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- issue-26471 服务热线V1.5（客服聊天记录保存和导出) by huangmingbo 2018.5.8
ALTER TABLE `eh_service_hotlines` ADD COLUMN `status` TINYINT NOT NULL DEFAULT '1' COMMENT '0-deleted 1-active';

-- by zheng
ALTER TABLE `eh_configurations` MODIFY `value` VARCHAR(1024);

-- add by yanjun 增加字段长度 201805091806
 
ALTER TABLE `eh_service_module_apps` MODIFY COLUMN `custom_tag`  VARCHAR(256)  NULL DEFAULT '';
-- issue-23470 by liuyilin
ALTER TABLE `eh_door_access`
ADD `enable_amount` TINYINT COMMENT '是否支持按次数开门的授权';

ALTER TABLE `eh_door_auth`
ADD `auth_rule_type` TINYINT COMMENT '授权规则的种类,0 按时间,1 按次数';
ALTER TABLE `eh_door_auth`
ADD `total_auth_amount` INT COMMENT '授权的总开门次数';
ALTER TABLE `eh_door_auth`
ADD `valid_auth_amount` INT COMMENT '剩余的开门次数';
-- End by liuyilin 


-- added by wh 
-- 增加应打卡时间给punch_log
ALTER TABLE `eh_punch_logs` ADD should_punch_time BIGINT COMMENT '应该打卡时间(用以计算早退迟到时长)';


-- asset_5.1_wentian by wentian
ALTER TABLE `eh_payment_notice_config` ADD COLUMN `notice_day_after` INTEGER DEFAULT NULL COMMENT '欠费日期后多少天';
ALTER TABLE `eh_payment_notice_config` ADD COLUMN `notice_day_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1:欠费前；2：欠费后';
ALTER TABLE `eh_payment_notice_config` ADD COLUMN `notice_objs` VARCHAR(3064) DEFAULT NULL COMMENT '催缴对象,格式为{type+id,type+id,...}';
ALTER TABLE `eh_payment_notice_config` ADD COLUMN `notice_app_id` BIGINT DEFAULT NULL COMMENT '催缴app信息模板的id';
ALTER TABLE `eh_payment_notice_config` ADD COLUMN `notice_msg_id` BIGINT DEFAULT NULL COMMENT '催缴sms信息模板的id';
ALTER TABLE `eh_payment_notice_config` ADD COLUMN `create_time` DATETIME DEFAULT NULL COMMENT '创建时间';
ALTER TABLE `eh_payment_notice_config` ADD COLUMN `create_uid` BIGINT DEFAULT NULL COMMENT '创建账号id';

-- 缴费app端支付按钮和合同查看隐藏
CREATE TABLE `eh_payment_app_views`(
  `id` BIGINT COMMENT 'primary key',
  `namespace_id` INTEGER DEFAULT NULL ,
  `community_id` BIGINT DEFAULT NULL ,
  `has_view` TINYINT NOT NULL ,
  `view_item` VARCHAR(16) NOT NULL ,
  `remark1_type` VARCHAR(16) DEFAULT NULL ,
  `remark1_identifier` VARCHAR(128) DEFAULT NULL ,
  `remark2_type` VARCHAR(16) DEFAULT NULL ,
  `remark2_identifier` VARCHAR(128) DEFAULT NULL,
  `remark3_type` VARCHAR(16) DEFAULT NULL ,
  `remark3_identifier` VARCHAR(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '缴费app端支付按钮和合同查看隐藏';

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

