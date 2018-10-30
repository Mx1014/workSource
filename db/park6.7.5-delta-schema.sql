-- AUTHOR: 缪洲
-- REMARK: 增加用户自定义上传资料与默认车牌字段
ALTER TABLE `eh_parking_lots` ADD COLUMN `default_data` TEXT NULL COMMENT '自定义上传资料';
ALTER TABLE `eh_parking_lots` ADD COLUMN `default_plate` VARCHAR(16) NULL COMMENT '默认车牌';