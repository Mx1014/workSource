-- merge from pmtaskzj xiongying 20170810
ALTER TABLE eh_pm_tasks ADD COLUMN `remark_source` VARCHAR(32);
ALTER TABLE eh_pm_tasks ADD COLUMN `remark` VARCHAR(1024);

-- 添加字段默认值  add by xq.tian 2017/08/10
ALTER TABLE `eh_group_member_logs` MODIFY COLUMN `community_id` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_group_member_logs` MODIFY COLUMN `address_id` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_group_member_logs` MODIFY COLUMN `group_id` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_group_member_logs` MODIFY COLUMN `update_time` DATETIME;
ALTER TABLE `eh_group_member_logs` MODIFY COLUMN `member_type` VARCHAR(32) NOT NULL DEFAULT '';

-- 增加可见范围字段 by st.zheng
ALTER TABLE`eh_service_alliances`  ADD COLUMN `range` VARCHAR(512) NULL DEFAULT NULL AFTER `owner_id`;
-- 增加标志位 by st.zheng
ALTER TABLE `eh_service_alliance_jump_module`
ADD COLUMN `signal` TINYINT(4) NULL DEFAULT '1' COMMENT '标志 0:删除 1:普通 2:审批' AFTER `parent_id`;

-- merge from msg-2.1
-- 更改群聊名称可为空  edit by yanjun 20170724
ALTER TABLE eh_groups MODIFY `name` VARCHAR(128) DEFAULT NULL;