
-- 设置应用类型 add by yanjun 201804081646
UPDATE eh_service_modules set app_type = 0 where id=50000 OR parent_id = 50000 OR path LIKE '%/50000/%';
UPDATE eh_service_modules set app_type = 1 WHERE app_type is NULL;
UPDATE eh_service_module_apps a set a.app_type = IFNULL((SELECT b.app_type from eh_service_modules b where b.id = a.module_id), 0);

-- add by yuanlei
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'organization', '900031', 'zh_CN', '无法注销企业。当前企业仍存在需要管理的项目。请转移项目管理权至其它公司后再试');
insert into eh_locale_strings(scope,code,locale,`text`)values('building',10004,'zh_CN','该楼栋名称已经存在，请更换其他名称');
insert into eh_locale_strings(scope,code,locale,`text`)values('organization',900032,'zh_CN','姓名为空');
insert into eh_locale_strings(scope,code,locale,`text`)values('organization',900033,'zh_CN','办公地点名称为空');
insert into eh_locale_strings(scope,code,locale,`text`)values('organization',900034,'zh_CN','办公地点所属项目为空');
insert into eh_locale_strings(scope,code,locale,`text`)values('organization',900035,'zh_CN','是否属于管理公司标志为空');
insert into eh_locale_strings(scope,code,locale,`text`)values('organization',900036,'zh_CN','是否属于服务商标志为空');
insert into eh_locale_strings(scope,code,locale,`text`)values('organization',900037,'zh_CN','是否启用工作台标志为空');