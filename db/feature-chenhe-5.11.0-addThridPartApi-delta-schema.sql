-- AUTHOR: chenhe 2018-11-20
-- REMARK: 圳智慧TICKET表
CREATE TABLE `eh_tickets` (
  `id` BIGINT NOT NULL,
  `user_id` BIGINT COMMENT 'token所属用户id',
  `ticket` VARCHAR(128) NOT NULL COMMENT 'token',
  `redirect_code` VARCHAR(16) COMMENT '指定跳转页面的代码',
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '圳智慧TICKET表';