-- 20180522-huangliangming-配置项管理-#30016
-- 创建配置项信息变更记录表
CREATE TABLE `eh_configurations_record_change` (
  `id` INT(11)  NOT NULL COMMENT '主键',
  `namespace_id` INT(11) NOT NULL COMMENT '域空间ID',
  `conf_pre_json` VARCHAR(1024)  COMMENT '变动前信息JSON字符串',
  `conf_aft_json` VARCHAR(1024)  COMMENT '变动后信息JSON字符串',
  `record_change_type` INT(3) COMMENT '变动类型。0，新增；1，修改；3，删除',
  `operator_uid` BIGINT(20)   COMMENT '操作人userId',
  `operator_time` DATETIME    COMMENT '操作时间',
  `operator_ip` VARCHAR(50)   COMMENT '操作者的IP地址',

  PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '配置项信息变更记录表';


-- 配置项信息表新增一列（字段 ） is_readyonly
ALTER  TABLE eh_configurations  ADD  is_readonly  INT(3)  COMMENT '是否只读：1，是 ；null 或其他值为 否';

-- 20180522-huangliangming