-- AUTHOR :黄鹏宇 2018年12月20日
-- REMARK :添加物业配置表
CREATE TABLE `eh_property_configurations` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) DEFAULT NULL,
  `community_id` bigint(20) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `module_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment '物业通用配置';