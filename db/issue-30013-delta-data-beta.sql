-- 通用脚本
-- ADD BY 梁燕龙
-- issue-30013 初始化短信白名单配置项
INSERT INTO `eh_configurations`(`name`,`value`,`description`,`namespace_id`)
      VALUES('sms.whitelist.test','true','短信白名单',0);
-- END BY 梁燕龙