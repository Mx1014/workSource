-- 新增字段 第三方KeyU字段
ALTER TABLE `eh_door_auth`
ADD COLUMN `key_u` VARCHAR(16) NULL DEFAULT NULL COMMENT '第三方用户秘钥' AFTER `right_remote`;

-- 新增字段 授权楼层
ALTER TABLE `eh_door_access`
ADD COLUMN `floor_id` VARCHAR(2000) NULL DEFAULT NULL COMMENT '授权楼层' AFTER `groupId`;