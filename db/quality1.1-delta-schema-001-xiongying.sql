-- category表增加description字段记录规范内容；增加分数字段记录规范所占分值。
alter table `eh_quality_inspection_categories` add column `score` double NOT NULL DEFAULT '0';
alter table `eh_quality_inspection_categories` add column `description` text DEFAULT NULL COMMENT 'content data';
 
-- task表新增category_id字段记录任务所属类型；category_path字段记录类型路径；增加字段记录创建任务的人的id，0为根据标准自动创建的任务；增加manual_flag字段标识是否是自动生成的任务
alter table `eh_quality_inspection_tasks` add column `category_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_categories';
alter table `eh_quality_inspection_tasks` add column `category_path` varchar(128) DEFAULT NULL COMMENT 'refernece to the path of eh_categories';
alter table `eh_quality_inspection_tasks` add column `create_uid` bigint(20) NOT NULL DEFAULT '0';
alter table `eh_quality_inspection_tasks` add column `manual_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: auto 1:manual';
 
-- 新增表记录修改的记录
CREATE TABLE `eh_quality_inspection_logs` (
`id` bigint(20) NOT NULL COMMENT 'id',
`owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the log, enterprise, etc',
`owner_id` bigint(20) NOT NULL DEFAULT '0',
`target_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'standard, etc',
`target_id` bigint(20) NOT NULL DEFAULT '0',
`process_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: insert, 2: update, 3: delete',
`operator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'record operator user id',
`create_time` datetime DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;