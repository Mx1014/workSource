-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 黄鹏宇 2018-8-13
-- REMARK: 增加导入错误日志表


CREATE TABLE `eh_sync_data_errors` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `module_id` BIGINT,
  `sync_type` VARCHAR(64) NOT NULL COMMENT '同步类型，对应同步的任务类型，如sync_contract',
  `owner_id` BIGINT NOT NULL COMMENT '错误对应的id，如：contractId，对应同步的任务类型',
  `owner_type` VARCHAR(64),
  `error_message` VARCHAR(512) NOT NULL COMMENT '发生错误的信息',
  `task_id` BIGINT NOT NULL COMMENT '同步版本';

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- END
-- --------------------- SECTION END ---------------------------------------------------------