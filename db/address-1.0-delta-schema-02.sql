
-- 增加可见范围字段 by st.zheng
ALTER TABLE`eh_service_alliances`  ADD COLUMN `range` VARCHAR(512) NULL DEFAULT NULL AFTER `owner_id`;
-- 增加标志位 by st.zheng
ALTER TABLE `eh_service_alliance_jump_module`
ADD COLUMN `signal` TINYINT(4) NULL DEFAULT '1' COMMENT '标志 0:删除 1:普通 2:审批' AFTER `parent_id`;

-- merge from msg-2.1
-- 更改群聊名称可为空  edit by yanjun 20170724
ALTER TABLE eh_groups MODIFY `name` VARCHAR(128) DEFAULT NULL;