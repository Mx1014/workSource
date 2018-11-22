                                                                                                                         -- AUTHOR: 胡琪
-- REMARK:　修改工作流记录处理表的表结构，添加‘泳道周期时长’字段
ALTER TABLE `eh_flow_statistics_handle_log` ADD COLUMN flow_lanes_cycle BIGINT(32) DEFAULT 0 COMMENT '泳道周期时长（单位为秒 s）';
