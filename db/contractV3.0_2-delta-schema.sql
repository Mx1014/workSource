
CREATE TABLE `eh_contract_template` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0' COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `community_id` bigint(20) DEFAULT NULL COMMENT '园区id',
  `contract_template_type` tinyint(2) DEFAULT '0' COMMENT '0 收款合同模板 1付款合同模板',
  `category_id` bigint(20) DEFAULT NULL COMMENT 'contract category id',
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 1: confirming, 2: active', 
  `template_contents` text  COMMENT '模板内容',
  `template_owner` bigint(20) NOT NULL DEFAULT '0' COMMENT '0: 通用配置, community_id ：表示归属的园区', 
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_uid` bigint(20) DEFAULT '0' COMMENT 'record update user id',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `delete_uid` bigint(20) DEFAULT '0' COMMENT 'record deleter user id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



