
-- AUTHOR: 黄良铭
-- REMARK:　添加工作流记录处理表
CREATE TABLE `eh_flow_statistics_handle_log` (
  `id` BIGINT(32) NOT NULL COMMENT '主键',
  `log_id`   BIGINT(32)  COMMENT 'log主键',
  `namespace_id` INT(11) DEFAULT NULL COMMENT '域空间ＩＤ',
  `flow_main_id` BIGINT(32) DEFAULT NULL,
  `flow_version` INT(11) DEFAULT NULL COMMENT '版本号',
  `flow_node_id` BIGINT(32) DEFAULT NULL COMMENT '节点ＩＤ',
  `flow_node_name` VARCHAR(64) DEFAULT NULL COMMENT '节点名',
  `flow_lanes_id` BIGINT(32) DEFAULT NULL COMMENT '泳道ＩＤ',
  `flow_lanes_name` VARCHAR(64) DEFAULT NULL COMMENT '泳道名',
  `start_time` DATETIME DEFAULT NULL COMMENT '起始时间',
  `end_time` DATETIME DEFAULT NULL COMMENT '结束时间',
  `cycle` BIGINT(32) DEFAULT NULL COMMENT '周期时长（单位为秒 s）',
  `create_time` DATETIME DEFAULT NULL COMMENT '记录创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE eh_flow_cases ADD COLUMN origin_app_id BIGINT COMMENT '应用 id';

-- AUTHOR:  胡琪
-- REMARK:　添加‘泳道周期时长’字段
ALTER TABLE `eh_flow_statistics_handle_log` ADD COLUMN flow_lanes_cycle BIGINT(32) DEFAULT 0 COMMENT '泳道周期时长（单位为秒 s）';

-- REMARK:  添加 flow_case_id 字段
ALTER TABLE `eh_flow_statistics_handle_log` ADD COLUMN flow_case_id BIGINT(20);

