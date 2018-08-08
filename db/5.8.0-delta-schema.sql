-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.1（展示能耗数据）
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `energy_consume` VARCHAR(1024) COMMENT '能耗用量';

-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.1 新增账单上传附件
CREATE TABLE `eh_payment_bill_attachments` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '多入口应用id，对应应用的origin_id',
  `owner_id` BIGINT,
  `owner_type` VARCHAR(64),
  `bill_id` BIGINT NOT NULL DEFAULT 0,
  `bill_group_id` BIGINT,
  `filename` VARCHAR(1024) COMMENT '附件名称',
  `content_type` VARCHAR(64) COMMENT '附件类型：word/pdf/png...',
  `content_uri` VARCHAR(1024) COMMENT '附件uri',
  `content_url` VARCHAR(1024) COMMENT '附件url',
  `creator_uid` BIGINT COMMENT '创建者ID',
  `create_time` DATETIME,
  `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 通用脚本
-- add by yanlong.liang 20180713
-- 帖子和活动表增加最低限制人数
ALTER TABLE `eh_forum_posts` ADD COLUMN `min_quantity` INT(11) COMMENT '最低限制人数';
ALTER TABLE `eh_activities` ADD COLUMN `min_quantity` INT(11) COMMENT '最低限制人数';
-- END BY yanlong.liang

-- 通用脚本
-- add by yanlong.liang 20180719
-- 导出中心增加阅读状态和下载次数
ALTER TABLE `eh_tasks` ADD COLUMN `read_status` TINYINT(4) COMMENT '阅读状态';
ALTER TABLE `eh_tasks` ADD COLUMN `download_times` INT(11) COMMENT '下载次数';
-- end

-- 通用脚本
-- AUTHOR: dengs
-- REMARK: 访客管理 管理者消息接受表 , add by dengs, 20180425
CREATE TABLE `eh_visitor_sys_message_receivers` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community or organization',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ownerType为community时候，为园区id;ownerType为organization时候，为公司id',
  `creator_uid` BIGINT COMMENT '创建者/访客管理者id',
  `create_time` DATETIME COMMENT '事件发生时间',
  `operator_uid` BIGINT,
  `operate_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '访客管理 管理者消息接受表';

ALTER TABLE eh_visitor_sys_visitors ADD COLUMN source TINYINT DEFAULT '0' COMMENT '访客来源，0:内部录入 1:外部对接';
ALTER TABLE eh_visitor_sys_visitors ADD COLUMN notify_third_success_flag TINYINT DEFAULT '0' COMMENT '访客来源为外部对接，0：未回调到第三方 1：回调失败 2:回调成功';
-- end
-- --------------------- SECTION END ---------------------------------------------------------