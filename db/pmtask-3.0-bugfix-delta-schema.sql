-- 增加字段判断是否使用费用清单 by st.zheng
ALTER TABLE `eh_pm_tasks`
ADD COLUMN `if_use_feelist` TINYINT(4) NULL DEFAULT '0' COMMENT '是否使用费用清单 0不使用 1 使用' AFTER `organization_name`;