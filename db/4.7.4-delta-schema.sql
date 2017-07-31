-- 增加详情字段 by st.zheng
ALTER TABLE `eh_rentalv2_items` ADD COLUMN `description` VARCHAR(1024) NULL DEFAULT NULL AFTER `item_type`;
--创建 审批表 by st.zheng
CREATE TABLE `eh_community_approve` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `organization_id` bigint(20) NOT NULL,
  `owner_id` bigint(20) NOT NULL,
  `owner_type` varchar(64) NOT NULL,
  `module_id` bigint(20) DEFAULT NULL,
  `module_type` varchar(64) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT '0',
  `project_type` varchar(64) DEFAULT NULL,
  `approve_name` varchar(64) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1',
  `form_origin_id` bigint(20) DEFAULT NULL,
  `form_version` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--创建申请表 by st.zheng
CREATE TABLE `eh_community_approve_vals` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `organization_id` bigint(20) NOT NULL DEFAULT '0',
  `owner_id` bigint(20) NOT NULL,
  `owner_type` varchar(64) NOT NULL,
  `module_id` bigint(20) DEFAULT NULL,
  `module_type` varchar(64) DEFAULT NULL,
  `flow_case_id` bigint(20) DEFAULT '0',
  `form_origin_id` bigint(20) DEFAULT NULL,
  `form_version` bigint(20) DEFAULT NULL,
  `approve_id` bigint(20) DEFAULT '0',
  `approve_name` varchar(64) DEFAULT NULL,
  `requestor_name` varchar(64) DEFAULT NULL,
  `requestor_phone` varchar(64) DEFAULT NULL,
  `requestor_company` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




