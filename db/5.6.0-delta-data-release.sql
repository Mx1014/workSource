-- issue-31813门禁2.9.7客户端支持自定义门禁授权方式 by liuyilin 20180615
ALTER TABLE `eh_door_access` ADD `max_duration` INTEGER COMMENT '有效时间最大值(天)';
ALTER TABLE `eh_door_access` ADD `max_count` INTEGER COMMENT '按次授权最大次数';
ALTER TABLE `eh_door_access` ADD `defualt_invalid_duration` INTEGER COMMENT '按次授权默认有效期(天)';
ALTER TABLE `eh_door_access` ADD `enable_duration` TINYINT DEFAULT '1' COMMENT '是否支持按有效期开门';
-- issue-31813 END