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
-- REMARK issue- 	26688  企业信息V1.0
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


-- 通用脚本
-- AUTHOR jiarui  20180717
-- REMARK issue-27396	 服务联盟 活动企业数据同步
CREATE TABLE `eh_customer_configutations` (
  `id` bigint(20) NOT NULL,
  `scope_type` VARCHAR(64)  DEFAULT NULL COMMENT 'service_alliance or activity',
  `scope_id` bigint(20) NOT NULL COMMENT 'code',
  `value`  tinyint(4) NOT NULL DEFAULT '0' ,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: invalid, 2: valid',
  `namespace_id` int(11) not NULL,
  `create_time` datetime DEFAULT NULL COMMENT 'record create time',
  `creator_uid` BIGINT(20) DEFAULT NULL COMMENT 'creatorUid',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_customer_potential_datas` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL  DEFAULT 0,
  `name` text COMMENT 'potential customer name',
  `source_id` bigint(20) DEFAULT NULL COMMENT 'refer to service allance activity categoryId',
  `source_type` varchar(1024) DEFAULT NULL COMMENT 'service_alliance or activity',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: invalid, 2: valid',
  `operate_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ,
  `create_time` datetime NOT NULL ,
  `delete_time` datetime  NULL ,
  `delete_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER  TABLE `eh_enterprise_customers` ADD COLUMN `source_id` BIGINT(20)  NULL ;
ALTER  TABLE `eh_enterprise_customers` ADD COLUMN `source_type` VARCHAR(64)  NULL;
ALTER TABLE `eh_customer_talents` ADD COLUMN `talent_source_item_id`  BIGINT(20) NULL COMMENT 'categoryId' AFTER `age`;
ALTER TABLE `eh_customer_talents` ADD COLUMN `origin_source_id` bigint(20) NULL COMMENT 'origin potential data primary key' AFTER `age`;
ALTER TABLE `eh_customer_talents` ADD COLUMN `origin_source_type`  VARCHAR(64) NULL COMMENT 'service_alliance or activity' AFTER `age`;
ALTER TABLE `eh_customer_talents` ADD COLUMN `register_status`  TINYINT(4) NOT NULL  DEFAULT  0 AFTER `age`;

-- end

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 杨崇鑫
-- REMARK:物业缴费6.2 增加减免费项
CREATE TABLE `eh_payment_subtraction_items` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '多入口应用id，对应应用的origin_id',
  `owner_id` BIGINT,
  `owner_type` VARCHAR(64),
  `bill_id` BIGINT NOT NULL DEFAULT 0,
  `bill_group_id` BIGINT,
  `subtraction_type` VARCHAR(255) COMMENT '减免费项类型，eh_payment_bill_items：费项（如：物业费），eh_payment_late_fine：减免滞纳金（如：物业费滞纳金）',
  `charging_item_id` BIGINT COMMENT '减免费项的id，存的都是charging_item_id，因为滞纳金是跟着费项走，所以可以通过subtraction_type类型，判断是否减免费项滞纳金',
  `charging_item_name` VARCHAR(255) COMMENT '减免费项名称',
  `creator_uid` BIGINT COMMENT '创建者ID',
  `create_time` DATETIME,
  `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='减免费项配置表';

-- AUTHOR: 杨崇鑫
-- REMARK: 取消滞纳金表字段非空限制
ALTER TABLE eh_payment_late_fine MODIFY COLUMN customer_id BIGINT COMMENT 'allows searching taking advantage of it';
-- --------------------- SECTION END ---------------------------------------------------------

-- 通用脚本
-- ADD BY jun.yan
-- ISSUE-32697 模块对应的菜单是否需要授权, add by yanjun 20180517
ALTER TABLE `eh_service_modules` ADD COLUMN `menu_auth_flag`  tinyint(4) NOT NULL DEFAULT 1 COMMENT 'if its menu need auth' ;

ALTER TABLE `eh_service_modules` ADD COLUMN `category`  varchar(255) NULL COMMENT 'classify, module, subModule';
-- end

-- 通用脚本
-- add by liangyanlong 20180710
-- 第三方应用链接白名单.
CREATE TABLE `eh_app_white_list` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `link` VARCHAR(128) NOT NULL COMMENT '第三方应用链接',
  `name` VARCHAR(128) NOT NULL COMMENT '第三方应用名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='第三方应用白名单';
-- end

-- 通用脚本  
-- AUTHOR: 黄良铭
-- REMARK: 一键推送消息(短信)记录表
CREATE TABLE `eh_push_message_log` (

  `id` INT(11)  NOT NULL COMMENT '主键',
  `namespace_id` INT(11) NOT NULL  COMMENT '域空间ID',
  `content` TEXT  COMMENT '推送内容',
  `push_type` INT(2)  COMMENT '推送方式（1表示应用消息推送，2表示短信推送）',
  `receiver_type` INT(2)  COMMENT '推送对象的类型（0表示所有人，1表示按项目，2表示按手机号）',
  `operator_id` INT(11)  COMMENT '操作者',
  `create_time` DATETIME  COMMENT '推送创建时间',
  `push_status` INT(2)  COMMENT '推送状态(1表示等待推送，2表示推送中，3表示推送完成)',
  `receivers` VARCHAR(600)  COMMENT '推送对象(与推送对象类型对应，所有人为空，按项目为项目ID，按手机号为手机号)',

  PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '一键推送消息(短信)记录表';

--end

-- 通用脚本
-- AUTHOR liuyilin  20180712
-- REMARK issue-32260 人脸识别V1.6 - 管理控制台 增加字段长度 删除唯一约束
ALTER TABLE `eh_aclink_servers` MODIFY COLUMN `uuid` VARCHAR(64) NOT NULL;
ALTER TABLE `eh_aclink_servers` DROP INDEX `uuid`;
-- end
