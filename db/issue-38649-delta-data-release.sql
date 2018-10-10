-- AUTHOR: 梁燕龙
-- REMARK: 协议条款菜单
INSERT INTO eh_service_modules(id ,NAME , parent_id,path ,TYPE ,LEVEL ,STATUS ,default_order ,menu_auth_flag,category,operator_uid,creator_uid)
VALUES(60300,'协议条款管理',60000,'/100/60000/60300',1,3,2,30,1,'module',1,1);
INSERT INTO eh_web_menus(id,NAME,parent_id,icon_url,data_type ,leaf_flag,STATUS,path,TYPE,sort_num,module_id,LEVEL,condition_type,category,config_type)
VALUES(79000000 ,'协议条款管理',21000000,NULL,'script-management',1,2,'/11000000/21000000/79000000','zuolin',30,60300,3,'system','module',NULL);