ALTER TABLE `eh_questionnaires` ADD COLUMN `organization_scope` TEXT COMMENT 'targetType是organization的时候，发布公司的列表' AFTER `user_scope`;

-- 用户管理1.4 add by yanjun 201711071007
ALTER TABLE `eh_user_organizations` ADD COLUMN `executive_tag`  tinyint(4) NULL, ADD COLUMN `position_tag`  varchar(128) NULL;

-- flow 加校验状态字段   add by xq.tian  2017/10/31
ALTER TABLE ehcore.eh_flows ADD COLUMN `validation_status` TINYINT NOT NULL DEFAULT 2 COMMENT 'flow validation status';