--
-- 添加域空间字段  add by xq.tian  2017/08/24
--
-- 第一步
ALTER TABLE eh_terminal_statistics_tasks ADD COLUMN namespace_id INTEGER NOT NULL DEFAULT 0;
ALTER TABLE eh_terminal_statistics_tasks DROP INDEX `task_no`;
ALTER TABLE eh_terminal_statistics_tasks ALGORITHM=inplace, LOCK=NONE, ADD UNIQUE INDEX `u_eh_task_no_namespace_id`(`task_no`, `namespace_id`);

-- 第二步
ALTER TABLE eh_user_activities ALGORITHM=inplace, LOCK=NONE, DROP INDEX i_eh_imei_number;
ALTER TABLE eh_user_activities ALGORITHM=inplace, LOCK=NONE, DROP INDEX i_eh_create_time;

-- 第三步
ALTER TABLE eh_user_activities ALGORITHM=inplace, LOCK=NONE, ADD INDEX `i_eh_namespace_id`(`namespace_id`) USING HASH;
ALTER TABLE eh_user_activities ALGORITHM=inplace, LOCK=NONE, ADD INDEX `i_eh_imei_number`(`imei_number`) USING HASH;
ALTER TABLE eh_user_activities ALGORITHM=inplace, LOCK=NONE, ADD INDEX `i_eh_app_version_name`(`app_version_name`);
ALTER TABLE eh_user_activities ALGORITHM=inplace, LOCK=NONE, ADD INDEX `i_eh_create_time`(`create_time`);

-- 第四步
OPTIMIZE TABLE eh_user_activities;