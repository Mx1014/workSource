-- 通用脚本  
-- AUTHOR: 黄良铭
-- REMARK: 一键推送消息(短信)记录表
CREATE TABLE `eh_push_message_log` (

  `id` INT(11)  NOT NULL COMMENT '主键',
  `namespace_id` INT(11) NOT NULL  COMMENT '域空间ID',
  `content` TEXT  COMMENT '推送内容',
  `push_type` INT(2)  COMMENT '推送方式（1表示应用消息推送，2表示短信推送）',
  `receiver_type` INT(2)  COMMENT '推送对象的类型（0表示所有人，1表示按项目，2表示按手机号）',
  `operator_id` INT(11)  COMMENT '操作者',
  `create_time` DATE  COMMENT '推送创建时间',
  `push_status` INT(2)  COMMENT '推送状态(1表示等待推送，2表示推送中，3表示推送完成)',
  `receivers` VARCHAR(600)  COMMENT '推送对象(与推送对象类型对应，所有人为空，按项目为项目ID，按手机号为手机号)',

  PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '一键推送消息(短信)记录表';

--end