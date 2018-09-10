-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境

-- AUTHOR: 黄良铭
-- REMARK: #31347 #33785  保存用户当前所在场景
CREATE TABLE `eh_user_current_scene` (
  `id` BIGINT(32) NOT NULL COMMENT '主键',
  `uid` BIGINT(32) NOT NULL COMMENT '用户ID',
  `namespace_id` INT(11) DEFAULT NULL COMMENT '域空间ID',
  `community_id` BIGINT(32) DEFAULT NULL COMMENT '园区ID',
  `community_type` TINYINT(4) DEFAULT NULL COMMENT '园区类型',
  `create_time` DATETIME DEFAULT NULL ,
  `update_time` DATETIME DEFAULT NULL ,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- end


-- --------------------- SECTION END ---------------------------------------------------------