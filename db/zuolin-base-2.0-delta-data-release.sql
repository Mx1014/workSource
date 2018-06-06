
-- add by yuanlei
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'organization', '900031', 'zh_CN', '无法注销企业。当前企业仍存在需要管理的项目。请转移项目管理权至其它公司后再试');
insert into eh_locale_strings(scope,code,locale,`text`)values('building',10004,'zh_CN','该楼栋名称已经存在，请更换其他名称');
insert into eh_locale_strings(scope,code,locale,`text`)values('organization',900032,'zh_CN','姓名为空');
insert into eh_locale_strings(scope,code,locale,`text`)values('organization',900033,'zh_CN','办公地点名称为空');
insert into eh_locale_strings(scope,code,locale,`text`)values('organization',900034,'zh_CN','办公地点所属项目为空');
insert into eh_locale_strings(scope,code,locale,`text`)values('organization',900035,'zh_CN','是否属于管理公司标志为空');
insert into eh_locale_strings(scope,code,locale,`text`)values('organization',900036,'zh_CN','是否属于服务商标志为空');
insert into eh_locale_strings(scope,code,locale,`text`)values('organization',900037,'zh_CN','是否启用工作台标志为空');
insert into eh_locale_strings(scope,code,locale,`text`)values('organization',900038,'zh_CN','公司名称不能超过50字');
insert into eh_locale_strings(scope,code,locale,`text`)values('community',10013,'zh_CN','楼栋名称不能超过20个汉字');
insert into eh_locale_strings(scope,code,locale,`text`)values('community',10014,'zh_CN','楼栋名称重复了');
insert into eh_locale_strings(scope,code,locale,`text`)values('address',20011,'zh_CN','门牌地址超过了20个汉字');
insert into eh_locale_strings(scope,code,locale,`text`)values('building',10005,'zh_CN','该项目下不存在该楼栋');
INSERT INTO EH_LOCALE_TEMPLATES(scope,code,locale,description,`text`,namespace_id)VALUES('workbench',1,'zh_CN','开启工作台','"${organizationName}"开启工作台' , 2);
INSERT INTO EH_LOCALE_TEMPLATES(scope,code,locale,description,`text`,namespace_id)VALUES('workbench',2,'zh_CN','关闭工作台','"${organizationName}"关闭工作台' , 2);


-- 客户端处理方式
-- 默认所有是navtive应用
update eh_service_modules set client_handler_type = 0;
-- 内部链接
update eh_service_modules set client_handler_type = 2 WHERE id in (41400, 20400, 21200, 32500);
-- 离线包
update eh_service_modules set client_handler_type = 3 WHERE id in (49100, 20800, 20600);


-- 设置应用类型
-- 默认为园区模块
UPDATE eh_service_modules set app_type = 1;
-- 设置oa模块
UPDATE eh_service_modules set app_type = 0 WHERE id in (41000, 50100,  50300, 50500, 50400, 52000, 50600, 54000, 51300, 51400, 55000, 57000, 60100, 60200, 60210, 13000, 20650, 20830, 41020, 50700);
-- 更新应用信息
UPDATE eh_service_module_apps a set a.app_type = IFNULL((SELECT b.app_type from eh_service_modules b where b.id = a.module_id), 1);


