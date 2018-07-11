-- 通用脚本
-- ADD BY 唐岑  2018-6-27 20:02:00
-- 合同管理V2.9 #30013
CREATE TABLE `eh_contract_events` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `contract_id` bigint(20) NOT NULL COMMENT '该日志事件对应的合同id',
  `operator_uid` bigint(20) DEFAULT NULL COMMENT '修改人员id',
  `opearte_time` datetime NOT NULL COMMENT '修改时间',
  `opearte_type` tinyint(4) NOT NULL COMMENT '操作类型（1：增加，2：删除，3：修改）',
  `content` text COMMENT '修改内容描述'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '合同日志记录表';
-- END BY 唐岑


