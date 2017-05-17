-- added by wh  视频会议增加判断公司可否申请试用账号
 ALTER TABLE `eh_conf_enterprises` ADD COLUMN `Trial_Flag` TINYINT DEFAULT 2 COMMENT '0:reject request trial  account 2:can request trial account'; 