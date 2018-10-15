-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END OPERATION------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境





-- 标注版zuolin-base-2.1之前的数据



-- add by yuanlei
SET @id = (SELECT MAX(id)  from eh_locale_strings);
INSERT INTO `eh_locale_strings`(`id`,`scope`, `code`,`locale`, `text`) VALUES(@id := @id+1,'organization', '900031', 'zh_CN', '无法注销企业。当前企业仍存在需要管理的项目。请转移项目管理权至其它公司后再试');
insert into eh_locale_strings(`id`,scope,code,locale,`text`)values(@id := @id+1,'building',10004,'zh_CN','该楼栋名称已经存在，请更换其他名称');
insert into eh_locale_strings(`id`,scope,code,locale,`text`)values(@id := @id+1,'organization',900032,'zh_CN','姓名为空');
insert into eh_locale_strings(`id`,scope,code,locale,`text`)values(@id := @id+1,'organization',900033,'zh_CN','办公地点名称为空');
insert into eh_locale_strings(`id`,scope,code,locale,`text`)values(@id := @id+1,'organization',900034,'zh_CN','办公地点所属项目为空');
insert into eh_locale_strings(`id`,scope,code,locale,`text`)values(@id := @id+1,'organization',900035,'zh_CN','是否属于管理公司标志为空');
insert into eh_locale_strings(`id`,scope,code,locale,`text`)values(@id := @id+1,'organization',900036,'zh_CN','是否属于服务商标志为空');
insert into eh_locale_strings(`id`,scope,code,locale,`text`)values(@id := @id+1,'organization',900037,'zh_CN','是否启用工作台标志为空');
insert into eh_locale_strings(`id`,scope,code,locale,`text`)values(@id := @id+1,'organization',900038,'zh_CN','公司名称不能超过50字');

-- 现网已有，导致冲突了。start1
-- insert into eh_locale_strings(`id`,scope,code,locale,`text`)values(@id := @id+1,'community',10013,'zh_CN','楼栋名称不能超过20个汉字');
-- insert into eh_locale_strings(`id`,scope,code,locale,`text`)values(@id := @id+1,'community',10014,'zh_CN','楼栋名称重复了');
-- insert into eh_locale_strings(`id`,scope,code,locale,`text`)values(@id := @id+1,'address',20011,'zh_CN','门牌地址超过了20个汉字');

-- 现网已有，导致冲突了。start2

insert into eh_locale_strings(`id`,scope,code,locale,`text`)values(@id := @id+1,'building',10005,'zh_CN','该项目下不存在该楼栋');

SET @id = (SELECT MAX(id) from eh_locale_templates);

INSERT INTO eh_locale_templates(`id`,scope,code,locale,description,`text`,namespace_id)VALUES(@id := @id+1,'workbench',1,'zh_CN','开启工作台','"${organizationName}"开启工作台' , 2);
INSERT INTO eh_locale_templates(`id`,scope,code,locale,description,`text`,namespace_id)VALUES(@id := @id+1,'workbench',2,'zh_CN','关闭工作台','"${organizationName}"关闭工作台' , 2);
-- add by yuanlei
update eh_locale_templates set `text` = '${userName}（${contactToken}）已成为${organizationName}企业超级管理员。' where code = 20 and scope = 'organization.notification' and namespace_id = 0;


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
UPDATE eh_service_modules set app_type = 0 WHERE id in (50100,  50300, 50500, 50400, 52000, 50600, 54000, 51300, 51400, 55000, 57000, 60100, 60200, 60210, 13000, 20650, 20830, 41020, 50700, 41410, 41420, 41430, 41400);
-- 更新应用信息
UPDATE eh_service_module_apps a set a.app_type = IFNULL((SELECT b.app_type from eh_service_modules b where b.id = a.module_id), 1);


-- 应用二级分类
INSERT INTO `eh_second_app_types` (`id`, `name`, `app_type`) VALUES ('1', '协同办公', '0');
INSERT INTO `eh_second_app_types` (`id`, `name`, `app_type`) VALUES ('2', '人力资源', '0');
INSERT INTO `eh_second_app_types` (`id`, `name`, `app_type`) VALUES ('3', 'OA管理', '0');
INSERT INTO `eh_second_app_types` (`id`, `name`, `app_type`) VALUES ('4', 'HR管理', '0');
INSERT INTO `eh_second_app_types` (`id`, `name`, `app_type`) VALUES ('5', 'ERP', '0');
INSERT INTO `eh_second_app_types` (`id`, `name`, `app_type`) VALUES ('6', '信息发布', '1');
INSERT INTO `eh_second_app_types` (`id`, `name`, `app_type`) VALUES ('7', '社群运营', '1');
INSERT INTO `eh_second_app_types` (`id`, `name`, `app_type`) VALUES ('8', '基础数据管理', '1');
INSERT INTO `eh_second_app_types` (`id`, `name`, `app_type`) VALUES ('9', '招商租赁', '1');
INSERT INTO `eh_second_app_types` (`id`, `name`, `app_type`) VALUES ('10', '招商与租赁管理', '1');
INSERT INTO `eh_second_app_types` (`id`, `name`, `app_type`) VALUES ('11', '客服管理', '1');
INSERT INTO `eh_second_app_types` (`id`, `name`, `app_type`) VALUES ('12', '园区服务', '1');
INSERT INTO `eh_second_app_types` (`id`, `name`, `app_type`) VALUES ('13', '服务联盟', '1');
INSERT INTO `eh_second_app_types` (`id`, `name`, `app_type`) VALUES ('14', '物业管理', '1');
INSERT INTO `eh_second_app_types` (`id`, `name`, `app_type`) VALUES ('15', '运营统计', '1');

-- 应用入口信息
-- INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('2', '50100', '组织架构', '通讯录', '1', '1', '2', '2', '0');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('3', '50100', '组织架构', '组织架构', '2', '4', '1', '4', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('4', '50100', '组织架构', '通讯录', '2', '4', '2', '2', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('7', '50300', '职级管理', '职级管理', '2', '4', '1', '4', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('11', '50500', '员工认证', '员工认证', '2', '4', '1', '4', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('15', '50400', '人事档案', '人事档案', '2', '4', '1', '4', '4', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('18', '52000', '流程审批', '审批', '1', '1', '2', '1', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('19', '52000', '流程审批', '审批管理', '2', '4', '1', '3', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('20', '52000', '流程审批', '审批', '2', '4', '2', '1', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('22', '50600', '打卡考勤', '打卡考勤', '1', '1', '2', '2', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('23', '50600', '打卡考勤', '考勤管理', '2', '4', '1', '4', '5', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('24', '50600', '打卡考勤', '打卡考勤', '2', '4', '2', '2', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('26', '54000', '工作汇报', '工作汇报', '1', '1', '2', '1', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('27', '54000', '工作汇报', '汇报管理', '2', '4', '1', '3', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('28', '54000', '工作汇报', '工作汇报', '2', '4', '2', '1', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('35', '51300', '社保管理', '社保管理', '2', '4', '1', '4', '8', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('38', '51400', '薪酬管理', '工资条', '1', '1', '2', '2', '4', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('39', '51400', '薪酬管理', '薪酬管理', '2', '4', '1', '4', '9', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('40', '51400', '薪酬管理', '工资条', '2', '4', '2', '2', '5', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('42', '55000', '文档', '文档', '1', '1', '2', '1', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('43', '55000', '文档', '文档管理', '2', '4', '1', '3', '4', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('44', '55000', '文档', '文档', '2', '4', '2', '1', '4', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('50', '57000', '企业公告', '企业公告', '1', '1', '2', '1', '5', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('51', '57000', '企业公告', '公告管理', '2', '4', '1', '3', '6', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('52', '57000', '企业公告', '企业公告', '2', '4', '2', '1', '6', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('55', '60100', '管理员管理', '管理员管理', '2', '4', '1', '0', '0', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('59', '60210', '责任部门配置', '责任部门配置', '2', '4', '1', '0', '0', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('62', '20830', '任务管理', '我的任务', '1', '1', '2', '1', '0', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('63', '20830', '任务管理', '任务管理', '2', '4', '1', '3', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('64', '20830', '任务管理', '我的任务', '2', '4', '2', '1', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('67', '41300', '应用活跃统计', '应用活跃统计', '2', '4', '1', '15', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('71', '41330', '用户行为统计', '用户行为统计', '2', '4', '1', '15', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('75', '41020', '企业门禁', '企业门禁管理', '2', '4', '1', '4', '6', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('79', '41010', '公共门禁', '公共门禁管理', '2', '4', '1', '11', '4', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('82', '10300', '园区公告', '园区公告', '1', '2', '2', '6', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('83', '10300', '园区公告', '园区公告', '2', '4', '1', '6', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('84', '10300', '园区公告', '园区公告', '2', '5', '2', '6', '0', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('87', '10900', '启动广告', '启动广告', '2', '4', '1', '6', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('90', '10400', '园区广告', '园区广告', '1', '2', '2', '6', '0', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('91', '10400', '园区广告', '园区广告', '2', '4', '1', '6', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('92', '10400', '园区广告', '园区广告', '2', '5', '2', '6', '0', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('94', '10800', '园区快讯', '园区快讯', '1', '2', '2', '6', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('95', '10800', '园区快讯', '园区快讯', '2', '4', '1', '6', '4', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('96', '10800', '园区快讯', '园区快讯', '2', '5', '2', '6', '0', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('98', '10500', '园区电子报', '园区电子报', '1', '2', '2', '6', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('99', '10500', '园区电子报', '园区电子报', '2', '4', '1', '6', '5', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('103', '11000', '一键推送', '一键推送', '2', '4', '1', '6', '7', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('107', '12200', '短信推送（定制）', '短信推送', '2', '4', '1', '6', '8', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('111', '34000', '用户管理', '用户管理', '2', '4', '1', '7', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('115', '35000', '用户认证', '用户认证', '2', '4', '1', '7', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('119', '47000', '积分管理', '积分管理', '2', '4', '1', '7', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('121', '10600', '园区活动', '园区活动管理', '1', '1', '1', '7', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('122', '10600', '园区活动', '园区活动', '1', '2', '2', '7', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('123', '10600', '园区活动', '园区活动管理', '2', '4', '1', '7', '4', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('124', '10600', '园区活动', '园区活动', '2', '5', '2', '7', '0', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('125', '10100', '园区论坛', '园区论坛管理', '1', '1', '1', '7', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('126', '10100', '园区论坛', '园区论坛', '1', '2', '2', '7', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('127', '10100', '园区论坛', '园区论坛管理', '2', '4', '1', '7', '5', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('129', '10750', '俱乐部', '俱乐部管理', '1', '1', '1', '7', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('130', '10750', '俱乐部', '俱乐部', '1', '2', '2', '7', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('131', '10750', '俱乐部', '俱乐部管理', '2', '4', '1', '7', '6', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('133', '10760', '行业协会', '行业协会管理', '1', '1', '1', '7', '4', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('134', '10760', '行业协会', '行业协会', '1', '2', '2', '7', '4', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('135', '10760', '行业协会', '行业协会管理', '2', '4', '1', '7', '7', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('138', '41700', '问卷调查', '问卷调查', '1', '2', '2', '7', '5', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('139', '41700', '问卷调查', '问卷调查管理', '2', '4', '1', '7', '8', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('143', '30600', '黑名单管理', '黑名单管理', '2', '4', '1', '7', '9', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('147', '51000', '举报管理', '举报管理', '2', '4', '1', '7', '10', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('150', '40100', '园区入驻', '园区入驻', '1', '2', '2', '9', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('151', '40100', '园区入驻', '园区入驻', '2', '4', '1', '10', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('152', '40100', '园区入驻', '园区入驻', '2', '5', '2', '9', '0', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('155', '36000', '孵化器入驻（定制）', '孵化器入驻', '2', '4', '1', '10', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('158', '40200', '工位预订', '工位预订', '1', '2', '2', '9', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('159', '40200', '工位预订', '工位预订', '2', '4', '1', '10', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('160', '40200', '工位预订', '工位预订', '2', '5', '2', '9', '0', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('162', '22000', '装修办理', '装修办理', '1', '2', '2', '9', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('163', '22000', '装修办理', '装修申请管理', '2', '4', '1', '11', '12', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('167', '22000', '楼宇资产管理', '楼宇资产管理', '2', '4', '1', '8', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('170', '33000', '企业管理', '园区企业', '1', '2', '2', '12', '15', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('171', '33000', '企业管理', '企业管理', '2', '4', '1', '8', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('173', '21100', '企业客户管理', '移动招商', '1', '1', '1', '8', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('175', '21100', '企业客户管理', '企业客户管理', '2', '4', '1', '8', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('183', '37000', '个人客户管理', '个人客户管理', '2', '4', '1', '8', '4', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('187', '21200', '合同管理', '合同管理', '2', '4', '1', '10', '6', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('191', '32500', '合同管理（定制）', '合同管理', '2', '4', '1', '10', '7', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('194', '20400', '物业缴费', '物业缴费', '1', '2', '2', '9', '4', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('195', '20400', '物业缴费', '物业缴费管理', '2', '4', '1', '14', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('199', '21300', '付款管理', '付款管理', '2', '4', '1', '14', '11', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('205', '40400', '资源预约', '资源预约管理', '1', '1', '1', '11', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('206', '40400', '资源预约', '资源预约', '1', '2', '2', '12', '7', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('207', '40400', '资源预约', '资源预约管理', '2', '4', '1', '11', '6', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('208', '40400', '资源预约', '资源预约', '2', '5', '2', '12', '0', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('210', '40800', '停车缴费', '停车缴费', '1', '2', '2', '12', '8', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('211', '40800', '停车缴费', '停车缴费', '2', '4', '1', '11', '7', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('213', '20900', '车辆放行', '车辆放行', '1', '1', '1', '11', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('215', '20900', '车辆放行', '车辆放行', '2', '4', '1', '11', '9', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('217', '49200', '物品搬迁', '物品放行', '1', '1', '1', '11', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('218', '49200', '物品搬迁', '物品搬迁', '1', '2', '2', '12', '9', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('219', '49200', '物品搬迁', '物品搬迁', '2', '4', '1', '11', '10', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('222', '40300', '服务热线', '服务热线', '1', '2', '2', '12', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('223', '40300', '服务热线', '服务热线', '2', '4', '1', '11', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('224', '40300', '服务热线', '服务热线', '2', '5', '2', '12', '0', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('225', '41400', '企业云打印', '云打印管理', '1', '1', '1', '3', '4', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('226', '41400', '企业云打印', '云打印', '1', '1', '2', '1', '10', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('227', '41400', '企业云打印', '云打印管理', '2', '4', '1', '3', '11', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('228', '41400', '企业云打印', '云打印', '2', '5', '2', '1', '11', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('230', '40700', '快递服务', '快递服务', '1', '2', '2', '12', '10', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('231', '40700', '快递服务', '快递服务', '2', '4', '1', '11', '13', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('234', '41500', '文件管理', '文件管理', '1', '2', '2', '6', '4', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('235', '41500', '文件管理', '文件管理', '2', '4', '1', '6', '6', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('238', '41100', '一键上网', '一键上网', '1', '2', '2', '12', '11', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('239', '41100', '一键上网', '一键上网', '2', '4', '1', '11', '14', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('242', '40070', '园区地图（定制）', '园区地图', '1', '2', '2', '12', '12', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('243', '40070', '园区地图（定制）', '园区地图', '2', '4', '1', '11', '15', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('246', '40730', '企业人才（定制）', '企业人才', '1', '2', '2', '12', '13', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('247', '40730', '企业人才（定制）', '企业人才', '2', '4', '1', '11', '16', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('250', '41600', '园区审批（定制）', '园区审批', '1', '2', '2', '12', '14', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('251', '41600', '园区审批（定制）', '园区审批', '2', '4', '1', '11', '17', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('254', '41200', '电子钱包（定制）', '钱包', '1', '2', '2', '12', '16', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('255', '41200', '电子钱包（定制）', '一卡通', '2', '4', '1', '11', '18', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('258', '40500', '服务联盟', '服务联盟', '1', '2', '2', '13', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('259', '40500', '服务联盟', '服务联盟', '2', '4', '1', '13', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('260', '40500', '服务联盟', '服务联盟', '2', '5', '2', '13', '0', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('262', '20100', '物业报修', '物业报修', '1', '2', '2', '12', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('263', '20100', '物业报修', '报修管理', '2', '4', '1', '14', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('265', '20800', '物业巡检', '物业巡检', '1', '1', '1', '14', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('267', '20800', '物业巡检', '物业巡检', '2', '4', '1', '14', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('269', '20600', '品质核查', '品质核查', '1', '1', '1', '14', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('271', '20600', '品质核查', '品质核查', '2', '4', '1', '14', '4', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('273', '49100', '能耗管理', '能耗管理', '1', '1', '1', '14', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('275', '49100', '能耗管理', '能耗管理', '2', '4', '1', '14', '5', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('279', '21000', '仓库管理', '仓库管理', '2', '4', '1', '14', '7', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('283', '26000', '采购管理', '采购管理', '2', '4', '1', '14', '8', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('287', '27000', '供应商管理', '供应商管理', '2', '4', '1', '14', '9', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('291', '25000', '请示单管理（定制）', '请示单管理', '2', '4', '1', '14', '10', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('306', '50700', '视频会议', '视频会议', '1', '1', '2', '1', '11', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `app_category_id`, `default_order`, `icon_uri`) VALUES ('312', '41000', '门禁（临时）', '门禁（临时）', '1', '2', '2', '12', '10', NULL);



-- 标准版的菜单，基线不用。start1.5

-- 增加应用、项目和公司菜单
-- INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15025000', '应用入口', '15000000', NULL, 'servicemodule-entry', '1', '2', '/15000000/15025000', 'zuolin', '30', NULL, '2', 'system', 'module', NULL);
-- INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15030000', '应用管理', '15000000', NULL, 'application-management', '1', '2', '/15000000/15030000', 'zuolin', '40', NULL, '2', 'system', 'module', NULL);
-- INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15040000', '项目管理', '15000000', NULL, 'project-management', '1', '2', '/15000000/15040000', 'zuolin', '50', NULL, '2', 'system', 'module', NULL);
-- INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15050000', '企业管理', '15000000', NULL, 'business-admin', '1', '2', '/15000000/15050000', 'zuolin', '60', NULL, '2', 'system', 'module', NULL);
-- DELETE FROM `eh_web_menus` WHERE `id`='15030100';
-- DELETE FROM `eh_web_menus` WHERE `id`='15030200';
-- DELETE FROM `eh_web_menus` WHERE `id`='15030300';

-- 标准版的菜单，基线不用。end1.5

-- 公司门禁 员工认证 职级管理 任务管理 设置为org控制  add by yanjun 20180614
UPDATE eh_service_modules SET module_control_type = 'org_control' WHERE id in (41020, 50500, 50300, 13000);
UPDATE eh_service_module_apps SET module_control_type = 'org_control' WHERE module_id in (41020, 50500, 50300, 13000);

-- janson 物品搬迁
UPDATE `eh_service_modules` SET `instance_config`='{"url":"${home.url}/goods-move/build/index.html?ns=2&hideNavigationBar=1&ehnavigatorstyle=0#/home#sign_suffix"}' WHERE `id`='49200';
UPDATE `eh_service_module_apps` SET `instance_config`='{"url":"${home.url}/goods-move/build/index.html?ns=2&hideNavigationBar=1&ehnavigatorstyle=0#/home#sign_suffix"}' WHERE `module_id`='49200';
UPDATE `eh_service_modules` SET `client_handler_type`='2' WHERE `id`='49200';
-- end

-- #issue-26479更新园区快讯WEB跳转地址
update eh_launch_pad_items set action_data = replace(action_data,'#/newsList#sign_suffix',concat('&ns=',namespace_id,'#/newsList#sign_suffix')) where action_type = 13 and action_data like '%park-news-web%';

-- #issue-26479 迁移原新闻从organization属性至community属性
update  eh_news nu ,
	(select news.id as news_id,  com.community_id as community_id from eh_news news
	left join 	(select n.namespace_id as namespace_id, n.resource_id as community_id
					from eh_namespace_resources n
					where n.resource_type  = 'COMMUNITY'
					group by n.namespace_id
					order by n.namespace_id,n.default_order,n.resource_id)
					as com
	on com.namespace_id = news.namespace_id
	where news.owner_type = 'organization' and news.owner_id > 0)
	as n_c
set nu.owner_type = 'EhCommunities', nu.owner_id = n_c.community_id
where nu.id = n_c.news_id;

-- 20180528-huangliangming-配置项管理-#30016
-- 初始化配置项表“是否只读”字段为“是”，值为1
UPDATE eh_configurations s SET s.is_readonly = 1 ;

-- 现网已有，导致冲突了。start2
-- -- 添加菜单
-- INSERT INTO eh_web_menus(id,NAME,parent_id,icon_url,data_type ,leaf_flag,STATUS,path,TYPE,sort_num,module_id,LEVEL,condition_type,category,config_type)
-- VALUES(30000000 ,'后台配置项',11000000,NULL,'server-configuration',1,2,'/11000000/30000000','zuolin',50,60100,3,'system','module',NULL);
-- -- 20180522-huangliangming



-- -- janson 电商
-- INSERT INTO `eh_payment_accounts` (`id`, `name`, `account_id`, `system_id`, `app_key`, `secret_key`, `create_time`) VALUES ('1', 'zuolinAccount', '0', '0', 'd7c0c950-d57d-4290-9318-f927dcca92c2', '1k0ty3aZPC8bjMm8V9+pFmsU5B7cImfQXB4GUm4ACSFPP1IhZI5basNbUBXe7p6gJ7OC8J03DW1U8fvvtpim6Q==', NOW());
-- end janson

--
-- -- 增加电商模块，用于运营板块 add by yanjun 201807041725
-- INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `app_type`, `client_handler_type`, `system_app_flag`) VALUES ('92000', '电商', '0', '/92000', '1', '1', '2', '0', '2018-07-04 17:22:11', NULL, NULL, '2018-07-04 17:22:20', '0', '0', '0', '0', NULL, '1', '0', NULL);
-- INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `app_type`, `client_handler_type`, `system_app_flag`) VALUES ('92100', '微商城', '92000', '/92000/92100', '1', '2', '2', '0', '2018-07-04 17:23:28', '{\"url\":\"${stat.biz.server.url}zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=${stat.biz.server.url}nar/biz/web/app/user/index.html?clientrecommend=1#/recommend?_k=zlbiz#sign_suffix\"}', NULL, '2018-07-04 17:23:33', '0', '0', '0', '0', NULL, '1', '2', NULL);

-- 现网已有，导致冲突了。end2
-- -----------------------------------------------------  以上为 5.6.3 以前的脚本 ----------------------------------------

-- -----------------------------------------------------  以下为 5.6.3 新增的脚本 ----------------------------------------


-- 更新企业客户管理模块
UPDATE eh_service_modules set client_handler_type = 2 where id = 21100;

-- #issue-31731【标准版V2.0】【园区快讯】【Android&Ios】客户端内容详情显示失败：404 not found
update eh_configurations set  value = '/html/news_text_review.html'  where name = 'news.content.url';

-- 园区入驻从“敬请期待”改为“上线”
UPDATE eh_service_modules set client_handler_type = 0, instance_config = '{}' WHERE id = 40100;
UPDATE eh_service_module_apps set instance_config = '{}' WHERE module_id = 40100;

-- janson
delete from eh_rentalv2_orders where  rental_resource_id not in (select id from eh_rentalv2_resources);
update eh_organizations set organization_type = 'ENTERPRISE' where organization_type is null;
-- end janson

-- ------------------------------------------------- 以下为zuolin-base-2.1(5.8.2)新增的数据脚本   start ---------------------------------


-- 因为 5.7.0对模块和菜单重构了，这里要重新执行模块了菜单的sql。

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
UPDATE eh_service_modules set app_type = 0 WHERE id in (50100,  50300, 50500, 50400, 52000, 50600, 54000, 51300, 51400, 55000, 57000, 60100, 60200, 60210, 13000, 20650, 20830, 41020, 50700, 41410, 41420, 41430, 41400);
-- 更新应用信息
UPDATE eh_service_module_apps a set a.app_type = IFNULL((SELECT b.app_type from eh_service_modules b where b.id = a.module_id), 1);


-- 注释掉，原因如下文描述  start3
-- 增加应用、项目和公司菜单  (仅在标准版执行)
-- INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15010000', '左邻标准版管理', '15000000', NULL, NULL, '1', '2', '/15000000/15010000', 'zuolin', '20', NULL, '2', 'system', 'classify', NULL);
-- INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15040000', '项目管理', '15010000', NULL, 'project-management', '1', '2', '/15000000/15010000/15040000', 'zuolin', '10', NULL, '3', 'system', 'module', NULL);
-- INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15030000', '应用管理', '15010000', NULL, 'application-management', '1', '2', '/15000000/15010000/15030000', 'zuolin', '20', NULL, '3', 'system', 'module', NULL);
-- INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15025000', '应用入口', '15010000', NULL, 'servicemodule-entry', '1', '2', '/15000000/15010000/15025000', 'zuolin', '30', NULL, '3', 'system', 'module', NULL);
-- INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15050000', '企业管理', '15010000', NULL, 'business-admin', '1', '2', '/15000000/15010000/15050000', 'zuolin', '40', NULL, '3', 'system', 'module', NULL);
-- DELETE FROM `eh_web_menus` WHERE `id`='15030100';
-- DELETE FROM `eh_web_menus` WHERE `id`='15030200';
-- DELETE FROM `eh_web_menus` WHERE `id`='15030300';

-- 注释掉，原因如下文描述  end3

-- 公司门禁 员工认证 职级管理 任务管理 设置为org控制  add by yanjun 20180614
UPDATE eh_service_modules SET module_control_type = 'org_control' WHERE id in (41020, 50500, 50300, 13000);
UPDATE eh_service_module_apps SET module_control_type = 'org_control' WHERE module_id in (41020, 50500, 50300, 13000);

-- janson 物品搬迁
UPDATE `eh_service_modules` SET `instance_config`='{"url":"${home.url}/goods-move/build/index.html?ns=2&hideNavigationBar=1&ehnavigatorstyle=0#/home#sign_suffix"}' WHERE `id`='49200';
UPDATE `eh_service_module_apps` SET `instance_config`='{"url":"${home.url}/goods-move/build/index.html?ns=2&hideNavigationBar=1&ehnavigatorstyle=0#/home#sign_suffix"}' WHERE `module_id`='49200';
UPDATE `eh_service_modules` SET `client_handler_type`='2' WHERE `id`='49200';
-- end

-- 现网已有，导致冲突了。start4
-- -- 增加电商模块，用于运营板块 add by yanjun 201807041725
-- INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `app_type`, `client_handler_type`, `system_app_flag`) VALUES ('92000', '电商', '0', '/92000', '1', '1', '2', '0', '2018-07-04 17:22:11', NULL, NULL, '2018-07-04 17:22:20', '0', '0', '0', '0', NULL, '1', '0', NULL);
-- INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `app_type`, `client_handler_type`, `system_app_flag`) VALUES ('92100', '微商城', '92000', '/92000/92100', '1', '2', '2', '0', '2018-07-04 17:23:28', '{\"url\":\"${stat.biz.server.url}zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=${stat.biz.server.url}nar/biz/web/app/user/index.html?clientrecommend=1#/recommend?_k=zlbiz#sign_suffix\"}', NULL, '2018-07-04 17:23:33', '0', '0', '0', '0', NULL, '1', '2', NULL);
-- 现网已有，导致冲突了。end4

-- 更新企业客户管理模块
UPDATE eh_service_modules set client_handler_type = 2 where id = 21100;


-- 园区入驻从“敬请期待”改为“上线”
UPDATE eh_service_modules set client_handler_type = 0, instance_config = '{}' WHERE id = 40100;
UPDATE eh_service_module_apps set instance_config = '{}' WHERE module_id = 40100;


-- PC工作台入口分类基础数据
UPDATE eh_service_module_entries SET app_category_id = 0 where location_type not in(2, 4);
UPDATE eh_service_module_entries SET app_category_id = 16 WHERE location_type = 2 and app_category_id = 6;
UPDATE eh_service_module_entries SET app_category_id = 17 WHERE location_type = 2 and app_category_id = 7;
UPDATE eh_service_module_entries SET app_category_id = 18 WHERE location_type = 2 and app_category_id = 13;

INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('1', '协同办公', '104', '4', NULL, '1', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('2', '人力资源', '104', '4', NULL, '2', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('3', 'OA管理', '104', '4', NULL, '3', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('4', 'HR管理', '104', '4', NULL, '4', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('5', 'ERP', '104', '4', NULL, '5', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('6', '信息发布', '102', '4', NULL, '1', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('7', '社群运营', '102', '4', NULL, '2', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('8', '基础数据管理', '100', '4', NULL, '1', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('9', '招商租赁', '0', '2', NULL, '3', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('10', '招商与租赁管理', '100', '4', NULL, '2', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('11', '客服管理', '102', '4', NULL, '3', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('12', '园区服务', '0', '2', NULL, '4', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('13', '服务联盟', '103', '4', NULL, '1', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('14', '物业管理', '101', '4', NULL, '1', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('15', '运营统计', '102', '4', NULL, '4', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('16', '信息发布', '0', '2', NULL, '1', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('17', '社群运营', '0', '2', NULL, '2', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('18', '服务联盟', '0', '2', NULL, '5', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('100', '资产管理', '0', '4', NULL, '2', '0');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('101', '物业服务', '0', '4', NULL, '3', '0');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('102', '园区运营', '0', '4', NULL, '4', '0');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('103', '服务联盟', '0', '4', NULL, '5', '0');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('104', '企业办公', '0', '4', NULL, '1', '0');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('500000002', '分类123', '0', '1', NULL, '1', '1');


INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('58000000', '协同办公', '40000010', NULL, NULL, '1', '2', '/40000010/58000000', 'park', '4', NULL, '2', 'system', 'classify', '2', '1');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('59000000', '人力资源', '40000010', NULL, NULL, '1', '2', '/40000010/59000000', 'park', '7', NULL, '2', 'system', 'classify', '2', '1');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('58010000', '审批', '58000000', NULL, 'approval-work', '0', '2', '/40000010/58000000/58010000', 'park', '10', '52000', '3', 'system', 'module', '2', '2');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('58020000', '工作汇报', '58000000', NULL, 'working-conference', '0', '2', '/40000010/58000000/58020000', 'park', '20', '54000', '3', 'system', 'module', '2', '2');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('58030000', '文档', '58000000', NULL, 'document-work', '0', '2', '/40000010/58000000/58030000', 'park', '30', '55000', '3', 'system', 'module', '2', '2');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('58040000', '企业公告', '58000000', NULL, 'notice-work', '0', '2', '/40000010/58000000/58040000', 'park', '40', '57000', '3', 'system', 'module', '2', '2');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('58050000', '日程提醒', '58000000', NULL, 'time-schedule', '0', '2', '/40000010/58000000/58050000', 'park', '50', '59100', '3', 'system', 'module', '2', '2');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('58060000', '会议预订', '58000000', NULL, 'meeting-work', '0', '2', '/40000010/58000000/58060000', 'park', '60', '53000', '3', 'system', 'module', '2', '2');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('59010000', '通讯录', '59000000', NULL, 'address-book', '0', '2', '/40000010/59000000/59010000', 'park', '10', '50100', '3', 'system', 'module', '2', '2');



-- 微商城设置为内部链接
UPDATE eh_service_modules SET client_handler_type = 2 WHERE id = 92100;


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: xq.tian  20180725
-- REMARK: 工作流 2.7

-- 脚本重复了  先注释  edit by yanjun

-- SET @eh_locale_templates_id = (SELECT MAX(id) FROM eh_locale_templates);
-- INSERT INTO eh_locale_templates (id, scope, code, locale, description, text, namespace_id)
--   VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'flow', 20008, 'zh_CN', '子业务流程进行中', '${serviceName} 进行中', 0);
-- INSERT INTO eh_locale_templates (id, scope, code, locale, description, text, namespace_id)
--   VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'flow', 20009, 'zh_CN', '子流程创建成功，点击此处查看父流程详情。', '子流程创建成功，点击此处查看父流程详情', 0);
-- INSERT INTO eh_locale_templates (id, scope, code, locale, description, text, namespace_id)
--   VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'flow', 20010, 'zh_CN', '${serviceName} 已完成', '${serviceName} 已完成', 0);
-- INSERT INTO eh_locale_templates (id, scope, code, locale, description, text, namespace_id)
--   VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'flow', 20011, 'zh_CN', '${serviceName} 已终止', '${serviceName} 已终止', 0);
-- INSERT INTO eh_locale_templates (id, scope, code, locale, description, text, namespace_id)
--   VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'flow', 20012, 'zh_CN', '子流程循环层级过多，流程已终止，详情请联系管理员', '子流程循环层级过多，流程已终止，详情请联系管理员', 0);
--
-- SET @eh_locale_strings_id = (SELECT MAX(id) FROM eh_locale_strings);
-- INSERT INTO eh_locale_strings (id, scope, code, locale, text)
--   VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'flow', '100025', 'zh_CN', '子流程异常，请检查设置');
-- INSERT INTO eh_locale_strings (id, scope, code, locale, text)
--   VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'flow', '100026', 'zh_CN', '请先发布新版本后再启用');
-- INSERT INTO eh_locale_strings (id, scope, code, locale, text)
--   VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'flow', '100027', 'zh_CN', '当前工作流未被修改，请修改后发布新版本')

-- AUTHOR: shiheng.ma  20180824
-- REMARK: 工位预订 应用配置迁移
UPDATE `eh_service_modules` SET `instance_config`='{\"url\":\"${home.url}/station-booking-web/build/index.html#/home#sign_suffix\",\"currentProjectOnly\":0}' WHERE `id`='40200';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"url\":\"${home.url}/station-booking-web/build/index.html#/home#sign_suffix\",\"currentProjectOnly\":0}' WHERE `module_id`='40200';
-- --------------------- SECTION END ---------------------------------------------------------



-- 更新 layout
SET @versionCode = '201809170100';

SET @bizAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 92100 AND `namespace_id` = 2);
SET @activityAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 10600 AND `namespace_id` = 2);
SET @forumAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 10100 AND `namespace_id` = 2);
SET @communityBulletinsAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 10300 AND `namespace_id` = 2);
SET @enterpriseBulletinsAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 57000 AND `namespace_id` = 2);

UPDATE eh_launch_pad_layouts set version_code = @versionCode, layout_json  = CONCAT('{"versionCode":"',@versionCode,'","layoutName":\"ServiceMarketLayout\",\"displayName\":\"工作台\",\"groups\":[{\"defaultOrder\":3,\"groupName\":\"公告\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"rowCount\":1.0,\"style\":2.0,\"shadow\":1.0,\"moduleId\":57000.0,\"appId\":', @enterpriseBulletinsAppId ,'},\"separatorFlag\":0,\"separatorHeight\":0,\"widget\":\"Bulletins\"},{\"columnCount\":1,\"defaultOrder\":2,\"groupName\":\"园区运营\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"paddingTop\":16.0,\"paddingLeft\":16.0,\"paddingBottom\":16.0,\"paddingRight\":16.0,\"lineSpacing\":0.0,\"columnSpacing\":0.0,\"backgroundColor\":\"#ffffff\",\"appType\":1.0},\"separatorFlag\":0,\"separatorHeight\":0,\"style\":\"Default\",\"widget\":\"Card\"},{\"columnCount\":1,\"defaultOrder\":4,\"groupName\":\"企业办公\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"paddingTop\":16.0,\"paddingLeft\":16.0,\"paddingBottom\":16.0,\"paddingRight\":16.0,\"lineSpacing\":0.0,\"columnSpacing\":0.0,\"backgroundColor\":\"#ffffff\",\"appType\":0.0},\"separatorFlag\":0,\"separatorHeight\":0,\"style\":\"Default\",\"widget\":\"Card\"}]}') WHERE type = 4 AND namespace_id = 2;
UPDATE eh_launch_pad_layouts set version_code = @versionCode, layout_json  = CONCAT('{"versionCode":"',@versionCode,'","layoutName":\"ServiceMarketLayout\",\"displayName\":\"服务广场\",\"groups\":[{\"defaultOrder\":1,\"groupName\":\"banner图片1\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31056\",\"widthRatio\":16.0,\"heightRatio\":9.0,\"shadowFlag\":1.0,\"paddingFlag\":1.0},\"separatorFlag\":0,\"separatorHeight\":0,\"widget\":\"Banners\"},{\"groupId\":0,\"groupName\":\"容器\",\"columnCount\":4,\"defaultOrder\":2,\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"paddingTop\":0.0,\"paddingLeft\":16.0,\"paddingBottom\":0.0,\"paddingRight\":16.0,\"lineSpacing\":0.0,\"columnSpacing\":0.0,\"cssStyleFlag\":1.0,\"backgroundColor\":\"#ffffff\",\"allAppFlag\":1.0},\"separatorFlag\":0,\"separatorHeight\":0,\"style\":\"Default\",\"widget\":\"Navigator\"},{\"defaultOrder\":3,\"groupName\":\"公告\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31057\",\"rowCount\":1.0,\"style\":2.0,\"shadow\":1.0,\"moduleId\":10300.0,\"appId\":', @communityBulletinsAppId ,'},\"separatorFlag\":0,\"separatorHeight\":0,\"widget\":\"Bulletins\"},{\"groupName\":\"电商入口\",\"widget\":\"NavigatorTemp\"},{\"defaultOrder\":4,\"groupName\":\"商品精选\",\"instanceConfig\":{\"itemGroup\":\"OPPushBiz\",\"moduleId\":92100.0,\"appId\":', @bizAppId, ',\"entityCount\":5.0,\"appConfig\":{}},\"separatorFlag\":1,\"separatorHeight\":24,\"style\":\"HorizontalScrollSquareView\",\"widget\":\"OPPush\"},{\"defaultOrder\":5,\"groupName\":\"活动\",\"instanceConfig\":{\"itemGroup\":\"OPPushActivity\",\"entityCount\":5.0,\"subjectHeight\":0.0,\"descriptionHeight\":0.0,\"newsSize\":5.0,\"moduleId\":10600.0,\"appId\":', @activityAppId, ',\"actionType\":61.0,\"appConfig\":{\"categoryId\":1.0,\"publishPrivilege\":1.0,\"livePrivilege\":0.0,\"listStyle\":2.0,\"scope\":3.0,\"style\":4.0}},\"separatorFlag\":1,\"separatorHeight\":24,\"style\":\"HorizontalScrollWideView\",\"widget\":\"OPPush\"},{\"defaultOrder\":7,\"groupName\":\"论坛\",\"instanceConfig\":{\"moduleId\":10100.0,\"appId\":', @forumAppId, ',\"actionType\":62.0,\"newsSize\":5.0,\"appConfig\":{}},\"separatorFlag\":1,\"separatorHeight\":24,\"style\":\"TextImageWithTagListView\",\"widget\":\"OPPush\"}]}') WHERE type = 5 AND namespace_id = 2 ;



-- 物业巡检，品质核查从coming soon恢复成正式的离线包
UPDATE eh_service_modules set instance_config = '{"realm":"qualityInspection","entryUrl":"https://park-std.zuolin.com/nar/qualityInspection/build/index.html?hideNavigationBar=1#/home#sign_suffix"}' WHERE id = 20600;

UPDATE eh_service_modules set instance_config = '{"realm":"equipmentInspection","entryUrl":"https://park-std.zuolin.com/nar/equipmentInspection/dist/index.html?hideNavigationBar=1#sign_suffix"}' WHERE id = 20800;

UPDATE eh_service_module_apps set instance_config = '{"realm":"qualityInspection","entryUrl":"https://park-std.zuolin.com/nar/qualityInspection/build/index.html?hideNavigationBar=1#/home#sign_suffix"}' WHERE module_id = 20600;

UPDATE eh_service_module_apps set instance_config = '{"realm":"equipmentInspection","entryUrl":"https://park-std.zuolin.com/nar/equipmentInspection/dist/index.html?hideNavigationBar=1#sign_suffix"}' WHERE module_id = 20800;


-- 设置oa模块
UPDATE eh_service_modules set app_type = 0 WHERE id in (59100, 53000);
-- 更新应用信息
UPDATE eh_service_module_apps a set app_type = 0 WHERE module_id in (59100, 53000);


-- ------------------------------------------------- zuolin-base-2.1(5.8.2)新增的数据脚本   end ---------------------------------



-- ------------------------------------------------- 5.8.4.20180925 新增的数据脚本   start ---------------------------------

-- 现网已有，导致冲突了。start 6


-- -- --------------企业OA相关功能提前融合到标准版，在5.9.0全量合并到标准版发布时需要跳过这部分脚本的执行-----------
--
-- -- AUTHOR: 张智伟 20180822
-- -- REMARK: issue-36367 考勤规则新增打卡提醒设置初始化，默认开启
-- UPDATE eh_punch_rules SET punch_remind_flag=1,remind_minutes_on_duty=10 WHERE rule_type=1;
--
-- -- AUTHOR: 张智伟 20180822
-- -- REMARK: issue-36367 假期余额变更消息提醒文案配置
-- SET @max_template_id = IFNULL((SELECT MAX(`id`) FROM `eh_locale_templates`),1);
-- INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
-- VALUE (@max_template_id:=@max_template_id+1, 'approval.tip.info', 10006, 'zh_CN', '消息标题','假期余额变动', 0);
-- INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
-- VALUE (@max_template_id:=@max_template_id+1, 'approval.tip.info', 10007, 'zh_CN', '假期余额变动提醒：管理员调整假期余额','管理员调整了你的假期余额：<#if annualLeaveAdd != 0><#if annualLeaveAdd gt 0>发放<#else>扣除</#if>年假${annualLeaveAddShow}</#if><#if annualLeaveAdd != 0 && overtimeAdd != 0>，</#if><#if overtimeAdd != 0><#if overtimeAdd gt 0>发放<#else>扣除</#if>调休${overtimeAddShow}</#if>。当前余额为：年假${annualLeaveBalance} ，调休${overtimeCompensationBalance} 。', 0);
-- INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
-- VALUE (@max_template_id:=@max_template_id+1, 'approval.tip.info', 10008, 'zh_CN', '假期余额变动提醒：年假/调休的审批被撤销/终止/删除','${categoryName}申请（${requestTime}） 已失效，假期已返还。当前余额为：年假 ${annualLeaveBalance} ，调休${overtimeCompensationBalance} 。', 0);
-- INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
-- VALUE (@max_template_id:=@max_template_id+1, 'approval.tip.info', 10009, 'zh_CN', '假期余额变动提醒：请假审批提交','${categoryName}申请（${requestTime}） 已提交，假期已扣除。当前余额为：年假 ${annualLeaveBalance} ，调休${overtimeCompensationBalance} 。', 0);
--
-- INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
-- VALUE (@max_template_id:=@max_template_id+1, 'punch.remind', 1, 'zh_CN', '消息标题','<#if punchType eq 0>上班打卡<#else>下班打卡</#if>', 0);
-- INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
-- VALUE (@max_template_id:=@max_template_id+1, 'punch.remind', 2, 'zh_CN', '上班打卡提醒','最晚${onDutyTime}上班打卡，立即打卡', 0);
-- INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
-- VALUE (@max_template_id:=@max_template_id+1, 'punch.remind', 3, 'zh_CN', '下班打卡提醒','上班辛苦了，记得打下班卡', 0);
--
-- -- AUTHOR: 张智伟 20180822
-- -- REMARK: issue-36367 打卡记录报表排序
-- UPDATE eh_punch_logs l INNER JOIN eh_organization_member_details d ON d.organization_id=l.enterprise_id AND d.target_id=l.user_id
-- SET l.detail_id=d.id
-- WHERE d.target_id>0 AND l.detail_id IS NULL;
--
-- UPDATE eh_punch_log_files l INNER JOIN eh_organization_member_details d ON d.organization_id=l.enterprise_id AND d.target_id=l.user_id
-- SET l.detail_id=d.id
-- WHERE d.target_id>0 AND l.detail_id IS NULL;
--
-- -- AUTHOR: 吴寒
-- -- REMARK: issue-33943 日程提醒1.2 增加5.9.0之后使用的提醒设置
-- SET @id = (SELECT MAX(id) FROM eh_remind_settings);
-- INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'准时','0',NULL,'1','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',NULL);
-- INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前5分钟','0',NULL,'2','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',300000);
-- INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前15分钟','0',NULL,'3','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',900000);
-- INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前30分钟','0',NULL,'4','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',1800000);
-- INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前1小时','0',NULL,'5','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',3600000);
-- INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前2小时','0',NULL,'6','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',7200000);
-- INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前1天','1',NULL,'7','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',NULL);
-- INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前2天','2',NULL,'8','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',NULL);
-- INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前3天','3',NULL,'9','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',NULL);
-- INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前5天','5',NULL,'10','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',NULL);
-- INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前1周','7',NULL,'11','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',NULL);
--
-- INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES('remind.version.segmen','5.8.4','日程提醒的版本分隔','0',NULL,NULL);
--
-- SET @tem_id = (SELECT MAX(id) FROM eh_locale_templates);
-- INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@tem_id := @tem_id + 1),'remind.msg','1','zh_CN','完成日程发消息','${trackContractName}的日程“${planDescription}” 已完成','0');
-- INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@tem_id := @tem_id + 1),'remind.msg','2','zh_CN','重置未完成日程发消息','${trackContractName}的日程“${planDescription}” 重置为未完成','0');
-- INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@tem_id := @tem_id + 1),'remind.msg','3','zh_CN','修改日程发消息','${trackContractName}修改了日程“${planDescription}”','0');
-- INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@tem_id := @tem_id + 1),'remind.msg','4','zh_CN','删除日程发消息','${trackContractName}删除了日程“${planDescription}”','0');
-- INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@tem_id := @tem_id + 1),'remind.msg','5','zh_CN','取消共享日程发消息','${trackContractName}的日程“${planDescription}” 取消共享了','0');
--
--
-- -- AUTHOR: 吴寒
-- -- REMARK: 会议管理V1.2
-- UPDATE eh_configurations SET VALUE = 5000 WHERE NAME ='meeting.record.word.limit';
--
-- -- AUTHOR: 荣楠
-- -- REMARK: 工作汇报V1.2
-- UPDATE `eh_locale_templates` SET `text`='${applierName}给你提交了${reportName}（${reportTime}）', `code` = 101 WHERE `scope`='work.report.notification' AND `code`='1';
-- UPDATE `eh_locale_templates` SET `text`='${applierName}更新了Ta的${reportName}（${reportTime}）', `code` = 102 WHERE `scope`='work.report.notification' AND `code`='2';
-- UPDATE `eh_locale_templates` SET `text`='${commentatorName}在Ta的${reportName}（${reportTime}）中 回复了你', `code` = 1, `description` = '评论消息类型1' WHERE `scope`='work.report.notification' AND `code`='3';
-- UPDATE `eh_locale_templates` SET `text`='${commentatorName}在Ta的${reportName}（${reportTime}）中 发表了评论', `code` = 2, `description` = '评论消息类型2' WHERE `scope`='work.report.notification' AND `code`='4';
-- UPDATE `eh_locale_templates` SET `text`='${commentatorName}在你的${reportName}（${reportTime}）中 回复了你', `code` = 3, `description` = '评论消息类型3' WHERE `scope`='work.report.notification' AND `code`='5';
-- UPDATE `eh_locale_templates` SET `text`='${commentatorName}在${applierName}的${reportName}（${reportTime}）中 回复了你', `code` = 4, `description` = '评论消息类型4' WHERE `scope`='work.report.notification' AND `code`='6';
-- UPDATE `eh_locale_templates` SET `text`='${commentatorName}在你的${reportName}（${reportTime}）中 发表了评论', `code` = 5, `description` = '评论消息类型5' WHERE `scope`='work.report.notification' AND `code`='7';
-- UPDATE `eh_work_reports` SET `validity_setting` = '{"endTime":"10:00","endType":1,"startTime":"15:00","startType":0}' WHERE `report_type` = 0;
-- UPDATE `eh_work_reports` SET `validity_setting` = '{"endMark":"1","endTime":"10:00","endType":1,"startMark":"5","startTime":"15:00","startType":0}' WHERE `report_type` = 1;
-- UPDATE `eh_work_reports` SET `validity_setting` = '{"endMark":"1","endTime":"10:00","endType":1,"startMark":"31","startTime":"15:00","startType":0}' WHERE `report_type` = 2;
-- UPDATE `eh_work_report_templates` SET `icon_uri`='cs://1/image/aW1hZ2UvTVRwaFlqazJNVFk1WkdabFpEbGhNamc0T1RjNVltWmlOakl3TmpobE1qUXpOUQ' WHERE (`id`='1') LIMIT 1;
-- UPDATE `eh_work_report_templates` SET `icon_uri`='cs://1/image/aW1hZ2UvTVRvMU1tRTVPV0kwWlRjeU16WmlOR05rTnpWbE9HUTFZV1ExTlRZMFpHUm1Odw' WHERE (`id`='2') LIMIT 1;
-- UPDATE `eh_work_report_templates` SET `icon_uri`='cs://1/image/aW1hZ2UvTVRvd1lXRmtObUkwWkRaaFlXRXdPRFV5TmpjMU5qa3hNelk1TVRRNE5XUTRNdw' WHERE (`id`='3') LIMIT 1;
-- SET @config_id = (SELECT MAX(id) FROM eh_configurations);
-- INSERT INTO `ehcore`.`eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES (@config_id := @config_id + 1, 'work.report.icon', 'cs://1/image/aW1hZ2UvTVRveE9UVm1aRGhsTTJRek9HRmpaRFV3WXpKaU5ESTNNV1ppT0RneVpHRTFaQQ', 'the icon of the customize workReport ', '0', NULL, NULL);
-- UPDATE eh_work_reports SET `icon_uri`='cs://1/image/aW1hZ2UvTVRveE9UVm1aRGhsTTJRek9HRmpaRFV3WXpKaU5ESTNNV1ppT0RneVpHRTFaQQ' ;
-- UPDATE eh_work_reports SET `icon_uri`='cs://1/image/aW1hZ2UvTVRwaFlqazJNVFk1WkdabFpEbGhNamc0T1RjNVltWmlOakl3TmpobE1qUXpOUQ' WHERE report_template_id = 1;
-- UPDATE eh_work_reports SET `icon_uri`='cs://1/image/aW1hZ2UvTVRvMU1tRTVPV0kwWlRjeU16WmlOR05rTnpWbE9HUTFZV1ExTlRZMFpHUm1Odw' WHERE report_template_id = 2;
-- UPDATE eh_work_reports SET `icon_uri`='cs://1/image/aW1hZ2UvTVRvd1lXRmtObUkwWkRaaFlXRXdPRFV5TmpjMU5qa3hNelk1TVRRNE5XUTRNdw' WHERE report_template_id = 3;
-- -- AUTHOR: ryan  20180827
-- -- REMARK: 执行 /workReport/syncWorkReportReceiver 接口, 用以同步工作汇报接收人公司信息
--
-- -- AUTHOR: ryan  20180827
-- -- REMARK: 执行 /workReport/updateWorkReportReceiverAvatar 接口, 用以更新工作汇报接收人头像
--
-- -- AUTHOR: ryan  20180926
-- -- REMARK: 执行 /workReport/updateWorkReportValAvatar 接口, 用以更新历史工作汇报值的头像
-- -- --------------企业OA相关功能提前融合到标准版，END 张智伟 -----------

-- 在 5.8.1-delta-data-release.sql 中已存在

-- -- ---------------------个人中心数据初始化sql--------------------------
-- -- AUTHOR: 梁燕龙
-- -- REMARK: 个人中心初始化数据
-- set @id = IFNULL((select MAX(id) FROM eh_personal_center_settings), 1);
-- INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
-- VALUES ((@id := @id + 1),0,'钱包','钱包',1,1,1,1,2,2,'cs://1/image/aW1hZ2UvTVRwaFkyUmhZV1F3WkdRMk56RXpNMlptWkRVek0ySXlNbVkxTVRJNFkyVTRZUQ',1,1,'/app/wallet');
-- INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
-- VALUES ((@id := @id + 1),0,'订单','订单',1,2,1,0,3,2,'cs://1/image/aW1hZ2UvTVRvNFptTTFOalZrWkRrd01tVXdOak5rWmpKa09UY3lOR1E1TlRJeVpUUXpZZw',1,1,'/zl-ec/rest/service/front/logon?sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fmall%2findex.html#sign_suffix');
-- INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
-- VALUES ((@id := @id + 1),0,'卡券','卡券',1,3,1,0,4,2,'cs://1/image/aW1hZ2UvTVRvMU5qUXhZV0prWXpBM01UazVOell4TVdWaU1qZzNPVEZsWXpneE9XSmtaZw',1,1,'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Froll%2F1%3F_k%3Dzlbiz#sign_suffix');
-- INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
-- VALUES ((@id := @id + 1),0,'发票','发票',1,4,0,1,5,2,'cs://1/image/aW1hZ2UvTVRwaE9EYzRORGd5WkRJd01URmpZMlV5T0dSbU5tVXdNemRtTjJGbU5EZG1ZUQ',1,1);
-- INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
-- VALUES ((@id := @id + 1),0,'我的申请','我的申请',2,1,1,1,0,6,2,'cs://1/image/aW1hZ2UvTVRvMlpHUXlaVGxoTWpoa016Sm1OR1U1TXpsbE5ESTNNbVpqWVRFM1ptWTFPQQ',1,1);
-- INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
-- VALUES ((@id := @id + 1),0,'我的地址','我的地址',2,1,2,1,0,7,2,'cs://1/image/aW1hZ2UvTVRvNFlXRmxObVZqWkRVeE5EWTROamRsTWpFNVltSTBNV0poT0dNd01UVTRZZw',1,1);
-- INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
-- VALUES ((@id := @id + 1),0,'我的店铺','我的店铺',2,1,3,1,1,8,2,'cs://1/image/aW1hZ2UvTVRvellUZzJOR0UzT0RSbU5UTmpaalUxWmpjM01XWTBaakptTmpNMk9ETXpaUQ',1,1);
-- INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
-- VALUES ((@id := @id + 1),0,'我的发布','我的发布',2,2,1,1,1,9,2,'cs://1/image/aW1hZ2UvTVRwaE1HUmlOV1l4WXpKaVl6a3dNak01TmpVeU9HVmxPRFl6TVRrME4yRTBZUQ',1,1);
-- INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
-- VALUES ((@id := @id + 1),0,'我的收藏','我的收藏',2,2,2,1,1,10,2,'cs://1/image/aW1hZ2UvTVRvMU16bGhZV0l4TVROaU5XSXdNRE13TVdRMFlURmpNV0l4TlRRM01qWmhZZw',1,1);
-- INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
-- VALUES ((@id := @id + 1),0,'我的报名','我的报名',2,2,3,1,1,11,2,'cs://1/image/aW1hZ2UvTVRwaVlqTXpabVJoTldZM056SXhaR1E1TjJWaE9URTRPVEE0TW1RM1kyUTJZUQ',1,1);
-- INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
-- VALUES ((@id := @id + 1),0,'设置','设置',2,3,1,1,0,12,2,'cs://1/image/aW1hZ2UvTVRwaU5tUXhNR013T1RGaVlUVmtNalF6TmpkaVpqZzVNVGhtWlRoaU1XVTRaQQ',1,1);
-- -- -------------------END-----------------------------------------------

-- -- ------------------------园区公告功能提前融合到标准版 ----------------------
-- -- AUTHOR: 梁燕龙
-- -- REMARK: 活动报名人数不足最低限制人数自动取消活动消息推送
-- INSERT INTO eh_locale_templates(`scope`, `code`,`locale`, `description`, `text`)
-- VALUES( 'announcement.notification', 1, 'zh_CN', '公告消息', '${subject}');
-- INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`) VALUES ('announcement',1,'zh_CN','公告消息');
-- INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`) VALUES ('forum',10007,'zh_CN','来晚啦，公告已不存在');
-- ---------------------------园区公告功能提前融合到标准版 END -------------
--


-- 现网已有，导致冲突了。end 6

-- ------------------------- 物业融合标准版------------------------------------
-- AUTHOR:jiarui

-- 通用脚本
-- AUHOR: jiarui 20180726
-- REMARK：动态表单迁移ownerId
update eh_var_field_group_scopes t1 set owner_id = IFNULL((select id  from eh_organizations  t2 where organization_type = 'PM' and t2.namespace_id = t1.namespace_id and parent_id = 0 LIMIT 1 ),0);
update eh_var_field_group_scopes set owner_type ='EhOrganizations';
update eh_var_field_scopes t1 set owner_id = IFNULL((select id  from eh_organizations  t2 where organization_type = 'PM' and t2.namespace_id = t1.namespace_id and parent_id = 0 LIMIT 1 ),0);
update eh_var_field_scopes set owner_type ='EhOrganizations';
update eh_var_field_item_scopes t1 set owner_id = IFNULL((select id  from eh_organizations  t2 where organization_type = 'PM' and t2.namespace_id = t1.namespace_id and parent_id = 0 LIMIT 1),0);
update eh_var_field_item_scopes set owner_type ='EhOrganizations';
-- 物业巡检 by jiatui 20180730
update eh_equipment_inspection_equipments set owner_type = 'EhOrganizations';
update eh_equipment_inspection_standards set owner_type = 'EhOrganizations';
update eh_equipment_inspection_accessories set owner_type = 'EhOrganizations';
update eh_equipment_inspection_plans set owner_type = 'EhOrganizations';
update eh_equipment_inspection_tasks set owner_type = 'EhOrganizations';
update eh_equipment_inspection_templates set owner_type = 'EhOrganizations';
update eh_pm_notify_configurations t1 set target_id = (select t2.id  from eh_organizations  t2 where t2.organization_type = 'PM' and t2.namespace_id =(select namespace_id from eh_communities t3 where t3.id = scope_id ) and t2.parent_id = 0 LIMIT 1 ) where t1.scope_type = 2;
update eh_pm_notify_configurations t1 set target_id = (select t2.id  from eh_organizations  t2 where t2.organization_type = 'PM' and t2.namespace_id =t1.scope_id and t2.parent_id = 0 LIMIT 1 ) where t1.scope_type = 1;
update eh_equipment_inspection_review_date t1 set target_id = (select t2.id  from eh_organizations  t2 where t2.organization_type = 'PM' and t2.namespace_id =(select namespace_id from eh_communities t3 where t3.id = scope_id ) and t2.parent_id = 0 LIMIT 1 ) where t1.scope_type = 2;
update eh_equipment_inspection_review_date t1 set target_id = (select t2.id  from eh_organizations  t2 where t2.organization_type = 'PM' and t2.namespace_id =t1.scope_id and t2.parent_id = 0 LIMIT 1 ) where t1.scope_type = 1;
update eh_equipment_inspection_review_date set target_type = 'EhOrganizations';
update eh_pm_notify_configurations set target_type = 'EhOrganizations';
-- 品质核查 by jiarui  20180730
update eh_quality_inspection_standards set owner_type ='EhOrganizations';
update eh_quality_inspection_tasks set owner_type ='EhOrganizations';
update eh_quality_inspection_task_templates set owner_type ='EhOrganizations';
update eh_quality_inspection_specifications set owner_type ='EhOrganizations';
update eh_quality_inspection_samples set owner_type ='EhOrganizations';
update eh_quality_inspection_sample_score_stat set owner_type ='EhOrganizations';
update eh_quality_inspection_sample_community_specification_stat set owner_type ='EhOrganizations';
update eh_quality_inspection_logs set owner_type ='EhOrganizations';
update eh_quality_inspection_evaluations set owner_type ='EhOrganizations';
-- 能耗管理  by jiarui 20180731
update eh_energy_meter_categories set owner_type ='EhOrganizations';

-- 合同管理 by jiarui 20180731
update eh_contract_templates t1 set org_id = (select t2.id  from eh_organizations  t2 where t2.organization_type = 'PM' and t2.namespace_id =t1.namespace_id and t2.parent_id = 0 LIMIT 1);
update eh_contract_params t1 set owner_id = (select id  from eh_organizations  t2 where organization_type = 'PM' and t2.namespace_id = t1.namespace_id and parent_id = 0 LIMIT 1 );
update eh_contract_params set owner_type = 'EhOrganizations';


-- 缴费管理  by jiarui 20180806
UPDATE eh_asset_bills set owner_type ='EhOrganizations';
UPDATE eh_asset_bill_template_fields set owner_type ='EhOrganizations';
UPDATE  eh_payment_charging_item_scopes t1 set org_id = (select t2.id  from eh_organizations  t2 where t2.organization_type = 'PM' and t2.namespace_id =t1.namespace_id and t2.parent_id = 0 LIMIT 1);
update eh_asset_bill_notify_records set owner_type = 'EhOrganizations';

-- -------------------------- 物业融合标准版---------------------------------




-- ------------------------更新广场layout ----------------------
-- 更新 layout
SET @versionCode = '201809260800';

SET @bizAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 92100 AND `namespace_id` = 2);
SET @activityAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 10600 AND `namespace_id` = 2);
SET @forumAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 10100 AND `namespace_id` = 2);
SET @communityBulletinsAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 10300 AND `namespace_id` = 2);
SET @enterpriseBulletinsAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 57000 AND `namespace_id` = 2);

UPDATE eh_launch_pad_layouts set version_code = @versionCode, layout_json  = CONCAT('{"versionCode":"',@versionCode,'","layoutName":\"ServiceMarketLayout\",\"displayName\":\"工作台\",\"groups\":[{\"defaultOrder\":3,\"groupName\":\"公告\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"rowCount\":1,\"noticeCount\":1,\"style\":1,\"shadow\":1.0,\"moduleId\":57000.0,\"appId\":', @enterpriseBulletinsAppId ,'},\"separatorFlag\":0,\"separatorHeight\":0,\"widget\":\"Bulletins\"},{\"columnCount\":1,\"defaultOrder\":2,\"groupName\":\"园区运营\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"paddingTop\":16.0,\"paddingLeft\":16.0,\"paddingBottom\":16.0,\"paddingRight\":16.0,\"lineSpacing\":0.0,\"columnSpacing\":0.0,\"backgroundColor\":\"#ffffff\",\"appType\":1.0},\"separatorFlag\":0,\"separatorHeight\":0,\"style\":\"Default\",\"widget\":\"Card\"},{\"columnCount\":1,\"defaultOrder\":4,\"groupName\":\"企业办公\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"paddingTop\":16.0,\"paddingLeft\":16.0,\"paddingBottom\":16.0,\"paddingRight\":16.0,\"lineSpacing\":0.0,\"columnSpacing\":0.0,\"backgroundColor\":\"#ffffff\",\"appType\":0.0},\"separatorFlag\":0,\"separatorHeight\":0,\"style\":\"Default\",\"widget\":\"Card\"}]}') WHERE type = 4 AND namespace_id = 2;
UPDATE eh_launch_pad_layouts set version_code = @versionCode, layout_json  = CONCAT('{"versionCode":"',@versionCode,'","layoutName":\"ServiceMarketLayout\",\"displayName\":\"服务广场\",\"groups\":[{\"defaultOrder\":1,\"groupName\":\"banner图片1\","style":"Shape",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31056\",\"widthRatio\":16.0,\"heightRatio\":9.0,\"shadowFlag\":1.0,\"paddingFlag\":1.0},\"separatorFlag\":0,\"separatorHeight\":0,\"widget\":\"Banners\"},{\"groupId\":0,\"groupName\":\"容器\",\"columnCount\":4,\"defaultOrder\":2,\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"paddingTop\":0.0,\"paddingLeft\":16.0,\"paddingBottom\":0.0,\"paddingRight\":16.0,\"lineSpacing\":0.0,\"columnSpacing\":0.0,\"cssStyleFlag\":1.0,\"backgroundColor\":\"#ffffff\",\"allAppFlag\":1.0},\"separatorFlag\":0,\"separatorHeight\":0,\"style\":\"Default\",\"widget\":\"Navigator\"},{\"defaultOrder\":3,\"groupName\":\"公告\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31057\",\"rowCount\":1,\"noticeCount\":1,\"style\":2.0,\"shadow\":1.0,\"moduleId\":10300.0,\"appId\":', @communityBulletinsAppId ,'},\"separatorFlag\":0,\"separatorHeight\":0,\"widget\":\"Bulletins\"},{\"groupName\":\"电商入口\",\"widget\":\"NavigatorTemp\"},{\"defaultOrder\":4,\"groupName\":\"商品精选\",\"instanceConfig\":{\"itemGroup\":\"OPPushBiz\",\"moduleId\":92100.0,\"appId\":', @bizAppId, ',\"entityCount\":5.0,\"appConfig\":{}},\"separatorFlag\":1,\"separatorHeight\":24,\"style\":\"HorizontalScrollSquareView\",\"widget\":\"OPPush\"},{\"defaultOrder\":5,\"groupName\":\"活动\",\"instanceConfig\":{\"itemGroup\":\"OPPushActivity\",\"entityCount\":5.0,\"subjectHeight\":0.0,\"descriptionHeight\":0.0,\"newsSize\":5.0,\"moduleId\":10600.0,\"appId\":', @activityAppId, ',\"actionType\":61.0,\"appConfig\":{\"categoryId\":1.0,\"publishPrivilege\":1.0,\"livePrivilege\":0.0,\"listStyle\":2.0,\"scope\":3.0,\"style\":4.0}},\"separatorFlag\":1,\"separatorHeight\":24,\"style\":\"HorizontalScrollWideView\",\"widget\":\"OPPush\"},{\"defaultOrder\":7,\"groupName\":\"论坛\",\"instanceConfig\":{\"moduleId\":10100.0,\"appId\":', @forumAppId, ',\"actionType\":62.0,\"newsSize\":5.0,\"appConfig\":{}},\"separatorFlag\":1,\"separatorHeight\":24,\"style\":\"TextImageWithTagListView\",\"widget\":\"OPPush\"}]}') WHERE type = 5 AND namespace_id = 2 ;

-- -------------------END-----------------------------------------------


-- "固定资产管理" 设置为oa模块
UPDATE eh_service_modules set app_type = 0 WHERE id = 59000;
UPDATE eh_service_module_apps a set app_type = 0 WHERE module_id = 59000;

-- ------------------------------------------------- 5.8.4.20180925 新增的数据脚本   end ---------------------------------




-- --------------------- SECTION END ALL -----------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END zuolin-base ---------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: dev
-- DESCRIPTION: 此SECTION只在开发库中执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END dev -----------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zijing
-- DESCRIPTION: 此SECTION只在清华信息港(紫荆)-999984执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END ---------------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guangda
-- DESCRIPTION: 此SECTION只在光大-999979执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END guangda -------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: szbay
-- DESCRIPTION: 此SECTION只在深圳湾-999966执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END szbay ---------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: chuangyechang
-- DESCRIPTION: 此SECTION只在成都创业场-999964执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END chuangyechang -------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: anbang
-- DESCRIPTION: 此SECTION只在安邦物业-999949执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END anbang---------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: nanshanquzhengfu
-- DESCRIPTION: 此SECTION只在南山区政府-999931执行的脚本
-- --------------------- SECTION END nanshanquzhengfu ----------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guanzhouyuekongjian
-- DESCRIPTION: 此SECTION只在广州越空间-999930执行的脚本
-- --------------------- SECTION END guanzhouyuekongjian -------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ruianxintiandi
-- DESCRIPTION: 此SECTION只在上海瑞安新天地-999929执行的脚本
-- --------------------- SECTION END ruianxintiandi ------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: wanzhihui
-- DESCRIPTION: 此SECTION只在万智汇-999953执行的脚本
-- --------------------- SECTION END wanzhihui ------------------------------------------