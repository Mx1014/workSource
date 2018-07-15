-- 通用脚本  
-- ADD BY 唐岑
-- # 资产管理V3.2 房源拆分/合并
CREATE TABLE `eh_address_arrangement` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `address_id` bigint(20) DEFAULT NULL COMMENT '要执行拆分/合并计划的房源id',
  `original_id` varchar(2048) DEFAULT NULL COMMENT '被拆分的房源id或者被合并的房源id（以json数组方式存储）',
  `target_id` varchar(2048) DEFAULT NULL COMMENT '拆分后产生的房源id或者合并后产生的房源id（以json数组方式存储）',
  `operation_type` tinyint(4) DEFAULT NULL COMMENT '操作类型：拆分（0），合并（1）',
  `date_begin` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '拆分合并计划的生效日期',
  `operation_flag` tinyint(4) DEFAULT NULL COMMENT '计划是否执行标志（0：否，1：是）',
  `status` tinyint(255) DEFAULT NULL COMMENT '计划状态',
  `namespace_id` int(11) NOT NULL DEFAULT '0' COMMENT '域空间id',
  `creator_uid` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_uid` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房源拆分/合并计划表';

ALTER TABLE `eh_addresses` ADD COLUMN `free_area` double NULL DEFAULT NULL COMMENT '可招租面积';
ALTER TABLE `eh_buildings` ADD COLUMN `free_area` double NULL DEFAULT NULL COMMENT '可招租面积';
ALTER TABLE `eh_communities` ADD COLUMN `free_area` double NULL DEFAULT NULL COMMENT '可招租面积';
ALTER TABLE `eh_addresses` ADD COLUMN `is_future_apartment` tinyint NULL DEFAULT 0 COMMENT '未来房源标记（0：否，1：是）';
-- END by 唐岑