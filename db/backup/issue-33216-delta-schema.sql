-- 通用脚本  
-- AUTHOR: 黄良铭
-- REMARK: 服务协议信息表
CREATE TABLE `eh_service_agreement` (

  `id` INT(11)  NOT NULL COMMENT '主键',
  `namespace_id` INT(11) NOT NULL  COMMENT '域空间ID',
  `agreement_content` mediumtext  COMMENT '协议内容',

  PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '服务协议信息表';

--end