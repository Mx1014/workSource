-- 工作流日志加会签标志字段 add by xq.tian 2017/12/25
ALTER TABLE eh_flow_event_logs ADD enter_log_complete_flag TINYINT NOT NULL DEFAULT 0;


-- 意见反馈增加一个字段“业务自定义参数”  add by yanjun 20171220
ALTER TABLE `eh_feedbacks` ADD COLUMN `target_param`  text NULL AFTER `target_id`;

-- add by st.zheng 增加起步价
ALTER TABLE `eh_rentalv2_price_rules`
ADD COLUMN `price_type` TINYINT(4) NULL AFTER `rental_type`,
ADD COLUMN `initiate_price` DECIMAL(10,2) NULL AFTER `weekend_price`,
ADD COLUMN `org_member_initiate_price` DECIMAL(10,2) NULL AFTER `org_member_weekend_price`,
ADD COLUMN `approving_user_initiate_price` DECIMAL(10,2) NULL AFTER `approving_user_weekend_price`;

ALTER TABLE `eh_rentalv2_price_packages`
ADD COLUMN `price_type` TINYINT(4) NULL AFTER `rental_type`,
ADD COLUMN `initiate_price` DECIMAL(10,2) NULL AFTER `price`,
ADD COLUMN `org_member_initiate_price` DECIMAL(10,2) NULL AFTER `org_member_price`,
ADD COLUMN `approving_user_initiate_price` DECIMAL(10,2) NULL AFTER `approving_user_price`;

ALTER TABLE `eh_rentalv2_cells`
ADD COLUMN `price_type` TINYINT(4) NULL AFTER `rental_type`,
ADD COLUMN `initiate_price` DECIMAL(10,2) NULL AFTER `price`,
ADD COLUMN `org_member_initiate_price` DECIMAL(10,2) NULL AFTER `org_member_price`,
ADD COLUMN `approving_user_initiate_price` DECIMAL(10,2) NULL AFTER `approving_user_price`;

-- 增加字段判断是否使用费用清单 by st.zheng
ALTER TABLE `eh_pm_tasks`
ADD COLUMN `if_use_feelist` TINYINT(4) NULL DEFAULT '0' COMMENT '是否使用费用清单 0不使用 1 使用' AFTER `organization_name`;


-- merge from forum-2.2 by yanjun 20171225 start
-- 是否支持投票
ALTER TABLE `eh_polls` ADD COLUMN `wechat_poll` TINYINT(4) DEFAULT '0' NULL COMMENT 'is support wechat poll 0:no, 1:yes';
-- merge from forum-2.2 by yanjun 20171225 end


-- merge from incubator-1.2 by yanjun 201712252013 start
-- 增加入孵申请类型  add by yanjun 20171215
ALTER TABLE `eh_incubator_applies` ADD COLUMN `apply_type`  tinyint(4) NOT NULL DEFAULT 0 ;
ALTER TABLE `eh_incubator_applies` ADD COLUMN `parent_id`  bigint(22) NULL AFTER `uuid`;
ALTER TABLE `eh_incubator_applies` ADD COLUMN `root_id`  bigint(22) NULL COMMENT 'tree root id' AFTER `parent_id`;
ALTER TABLE `eh_incubator_applies` ADD COLUMN `organization_id`  bigint(22) NULL;
ALTER TABLE `eh_incubator_applies` MODIFY COLUMN `approve_opinion`  varchar(1024) ;
ALTER TABLE `eh_incubator_applies` MODIFY COLUMN `re_apply_id`  bigint(22) NULL DEFAULT NULL COMMENT '重新申请的Id，现已弃用该字段';
-- 文件下载任务 add by yanjun 20171207
CREATE TABLE `eh_tasks` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) DEFAULT NULL,
  `community_id` bigint(20) DEFAULT NULL,
  `org_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL COMMENT 'owner',
  `name` varchar(255) DEFAULT NULL,
  `type` tinyint(4) DEFAULT NULL COMMENT '0-default,1-filedownload',
  `class_name` varchar(255) DEFAULT NULL,
  `params` text,
  `repeat_flag` tinyint(4) DEFAULT NULL,
  `process` int(11) DEFAULT NULL COMMENT 'rate of progress',
  `result_string1` varchar(255) DEFAULT NULL,
  `result_string2` varchar(255) DEFAULT NULL,
  `result_long1` bigint(20) DEFAULT NULL,
  `result_long2` bigint(20) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `error_description` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `finish_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- merge from incubator-1.2 by yanjun 201712252013 end



-- auth分支开始
-- 仓库权限 by wentian
ALTER TABLE `eh_warehouse_materials` ADD COLUMN `community_id` BIGINT DEFAULT 0 COMMENT '园区id';
ALTER TABLE `eh_warehouses` ADD COLUMN `community_id` BIGINT DEFAULT 0 COMMENT '园区id';
ALTER TABLE `eh_warehouse_stocks` ADD COLUMN `community_id` BIGINT DEFAULT 0 COMMENT '园区id';
ALTER TABLE `eh_warehouse_stock_logs` ADD COLUMN `community_id` BIGINT DEFAULT 0 COMMENT '园区id';
ALTER TABLE `eh_warehouse_request_materials` ADD COLUMN `community_id` BIGINT DEFAULT 0 COMMENT '园区id';
ALTER TABLE `eh_warehouse_requests` ADD COLUMN `community_id` BIGINT DEFAULT 0 COMMENT '园区id';
-- endl

-- 物业巡检权限细化 start  by jiarui
CREATE TABLE `eh_equipment_model_community_map` (
  `id`          BIGINT(20) NOT NULL,
  `model_id` BIGINT(20) DEFAULT 0 NOT NULL,
  `model_type`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '0:standard 1:template',
  `target_type` VARCHAR(255) DEFAULT NULL,
  `target_id`   BIGINT(20)   DEFAULT NULL COMMENT 'community id ',
  `create_time` DATETIME     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

ALTER TABLE `eh_equipment_inspection_standards`
  ADD COLUMN `refer_id` BIGINT(20) NULL;

ALTER TABLE `eh_equipment_inspection_templates`
  ADD COLUMN `refer_id`  bigint(20) NULL;

ALTER TABLE `eh_equipment_inspection_templates`
  ADD COLUMN `target_type` VARCHAR(32) NOT NULL DEFAULT '',
  ADD COLUMN `target_id` BIGINT(20) NOT NULL DEFAULT 0 ;
-- 物业巡检权限细化 end  by  jiarui

-- 品质核查权限细化start by jiarui  20171215
CREATE TABLE `eh_quality_inspection_model_community_map` (
  `id`          BIGINT(20) NOT NULL,
  `model_id` BIGINT(20) DEFAULT 0 NOT NULL,
  `category_id` BIGINT(20) DEFAULT 0 ,
  `model_type`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '0:standard',
  `target_type` VARCHAR(255) DEFAULT NULL,
  `target_id`   BIGINT(20)   DEFAULT NULL COMMENT 'community id ',
  `create_time` DATETIME     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

ALTER TABLE `eh_quality_inspection_standards`
  ADD COLUMN `refer_id` BIGINT(20) NULL;

-- 品质核查权限细化end  by jiarui  20171215


-- customerAuth by xiongying
CREATE TABLE `eh_energy_meter_category_map`(
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'enterprise id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `community_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'category id',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_energy_meter_fomular_map`(
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'enterprise id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `community_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `fomular_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'fomular id',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE eh_energy_meter_setting_logs ADD COLUMN `community_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id' AFTER `namespace_id`;
-- customerAuth by xiongying end


-- auth分支结束
