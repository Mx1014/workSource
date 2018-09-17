 
-- AUTHOR: 吴寒
-- REMARK: 打卡考勤V8.2 - 支持人脸识别关联考勤；支持自动打卡
ALTER TABLE `eh_punch_logs` ADD COLUMN `create_type` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '创建类型 : 0-正常打卡创建 1-自动打卡创建 2-人脸识别打卡创建 4-其他第三方接口创建(通过第三方接口打卡没有带创建类型)' ;
