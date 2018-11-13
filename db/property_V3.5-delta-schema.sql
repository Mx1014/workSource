CREATE TABLE `eh_address_events` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `address_id` bigint(20) NOT NULL COMMENT '房源id',
  `operator_uid` bigint(20) COMMENT '操作人id',
  `operate_time` datetime ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  `operate_type` tinyint(4) COMMENT '操作类型（1：增加，2：删除，3：修改）',
  `content` text COMMENT '日志内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房源日志表';