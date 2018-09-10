
-- 通用脚本
-- ADD BY 梁燕龙
-- issue-26754 敏感词过滤功能
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
  VALUES ('sensitiveword.url', '', '敏感词文档url', '0');

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
  VALUES ('sensitiveword.fileName', '', '敏感词文档名称', '0');

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
  VALUES ('sensitiveword.filePath', 'C:/Users/Administrator/Desktop/', '下载敏感词文档路径', '0');

INSERT INTO eh_web_menus(id,NAME,parent_id,icon_url,data_type ,leaf_flag,STATUS,path,TYPE,sort_num,module_id,LEVEL,condition_type,category,config_type)
VALUES(20030000 ,'敏感词日志',20000000,NULL,'sensitive-word',1,2,'/11000000/20000000/20030000','zuolin',40,NULL,2,'system','module',NULL);
-- END