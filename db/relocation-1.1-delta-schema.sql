-- 物品放行 1.1 新增配置表
-- by shiheng.ma
CREATE TABLE `eh_relocation_configs` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) DEFAULT NULL COMMENT 'attachment object owner type',
  `owner_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'owner id',
	`agreement_flag` tinyint(4) DEFAULT 0 COMMENT '0: inactive, 1: active',
	`agreement_content` text COMMENT '协议内容',
	`tips_flag` tinyint(4) DEFAULT 0 COMMENT '0: inactive, 1: active',
	`tips_content` varchar(100) DEFAULT NULL COMMENT '提示内容',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
	`operator_uid` bigint(20) NOT NULL DEFAULT '0',
  `operate_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 物品放行 1.1 增加新字段 用于小区场景
-- by shiheng.ma 20180620
alter table eh_relocation_requests add column org_owner_type_id BIGINT(20) DEFAULT NULL COMMENT '客户类型（小区场景）';