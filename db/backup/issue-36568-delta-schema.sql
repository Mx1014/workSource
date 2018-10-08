-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境



-- 通用脚本

-- AUTHOR:黄良铭
-- REMARK:  20180903-huangliangming-用户对接脚本方案-#36568
CREATE TABLE `eh_butt_script_config` (
  `id`  bigint(20)  NOT NULL COMMENT '主键',
  `info_type` varchar(64)  COMMENT '分类',
  `info_describe` varchar(128) COMMENT '描述',
  `namespace_id` int(11)  COMMENT '域空间ID',
  `module_id`  bigint(20) COMMENT  '这个没啥意思,自己定义,因为建库入参需要,应该是作区分用',
  `module_type` varchar(64)    COMMENT '这个没啥意思,自己定义,因为建库入参需要,应该是作区分用 ',
  `owner_id`  bigint(20)   ,
  `owner_type`  varchar(64)   ,
  `remark`  varchar(240)   COMMENT '备注',
  `status`  tinyint(4)    COMMENT '状态;0失效,1生效',

  PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '存储创建GOGS仓库时所需的指定相关表';

CREATE TABLE `eh_butt_script_publish_info` (
  `id`  bigint(20)  NOT NULL COMMENT '主键',
  `info_type` varchar(64)  COMMENT '分类 ,对应 eh_butt_script_config 表',
  `namespace_id` int(11)  COMMENT '域空间ID',
  `commit_version`  varchar(64)  COMMENT  '版本号',
  `publish_time` datetime     COMMENT '版本发布 时间',

  PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '版本信息及发布信息表';

CREATE TABLE `eh_butt_info_type_event_mapping` (
  `id`  bigint(20)  NOT NULL COMMENT '主键',
  `info_type` varchar(64)  COMMENT '分类 ,对应 eh_butt_script_config 表',
  `namespace_id` int(11)  COMMENT '域空间ID',
  `event_name`  varchar(128)  COMMENT  '触发该脚本的事件',
  `sync_flag`  tinyint(4)  COMMENT  '0 同步;1异步  同步执行还是异执行',
  `describe`  varchar(256)  COMMENT  '描述',

  PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '脚本与事件映射表';

CREATE TABLE `eh_butt_script_last_commit` (
  `id`  BIGINT(20)  NOT NULL COMMENT '主键',
  `info_type` VARCHAR(64)  COMMENT '分类 ,对应 eh_butt_script_config 表',
  `namespace_id` INT(11)  COMMENT '域空间ID',
  `last_commit`  VARCHAR(128)  COMMENT  '最后一次提交版本号',
  `commit_msg`  VARCHAR(256)  COMMENT  '提交相关信息',
  `commit_time`  DATETIME    COMMENT  '提交时间',

  PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'last_commit 存储表';
-- END

-- --------------------- SECTION END ---------------------------------------------------------