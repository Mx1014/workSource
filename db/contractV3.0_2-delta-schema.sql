-- 通用脚本  
-- ADD BY 丁建民 
-- # 合同管理 合同模板及打印

CREATE TABLE `eh_contract_templates` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0' COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `category_id` bigint(20) DEFAULT NULL COMMENT 'contract category id 用于多入口',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `name` varchar(64) NOT NULL COMMENT '合同模板名称',
  `contract_template_type` tinyint(2) DEFAULT '0' COMMENT '0 收款合同模板 1付款合同模板',
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 1: confirming, 2: active', 
  `contents` text  COMMENT '模板内容',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '复制于哪个合同模板',
  `version` int(11) DEFAULT '0' COMMENT '版本记录',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_uid` bigint(20) DEFAULT '0' COMMENT 'record update user id',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `delete_uid` bigint(20) DEFAULT '0' COMMENT 'record deleter user id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- END by 丁建民