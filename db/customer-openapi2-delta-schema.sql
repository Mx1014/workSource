
ALTER TABLE `eh_news` ADD COLUMN `create_type` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '0-后台创建 1-第三方调用接口' ;
