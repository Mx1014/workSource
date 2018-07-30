-- add by liangyanlong 20180710
CREATE TABLE `eh_app_white_list` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `link` VARCHAR(128) NOT NULL COMMENT '第三方应用链接',
  `name` VARCHAR(128) NOT NULL COMMENT '第三方应用名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='第三方应用白名单';