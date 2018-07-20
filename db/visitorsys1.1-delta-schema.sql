-- 访客管理 管理者消息接受表 , add by dengs, 20180425
-- DROP TABLE IF EXISTS `eh_visitor_sys_message_receivers`;
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
