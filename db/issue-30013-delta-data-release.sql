-- 通用脚本
-- ADD BY 黄良铭
-- issue-30013 初始化短信白名单配置项
-- 初始化配置项表“是否只读”字段为“是”，值为1
UPDATE eh_configurations s SET s.is_readonly = 1 ;

INSERT INTO eh_web_menus(id,NAME,parent_id,icon_url,data_type ,leaf_flag,STATUS,path,TYPE,sort_num,module_id,LEVEL,condition_type,category,config_type)
VALUES(30000000 ,'后台配置项',11000000,NULL,'server-configuration',1,2,'/11000000/30000000','zuolin',50,60100,3,'system','module',NULL);
-- END BY 黄良铭