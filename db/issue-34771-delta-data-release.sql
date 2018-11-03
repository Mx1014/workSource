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

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等

-- AUTHOR:xq.tian 20181016
-- REMARK:网关及注册中心部署.
-- DESCRIPTION：http://s.a.com/docs/faq/baseline-21539677844

-- AUTHOR:梁燕龙 20181016
-- REMARK:统一用户上线操作.
-- DESCRIPTION：http://s.a.com/docs/faq/baseline-21539678631

-- AUTHOR:杨崇鑫 20181018
-- REMARK:解决缺陷 #39352: 【全量标准版】【物业缴费】新增收费标准前端提示成功，但实际未新增成功，无相关数据，后台提示“应用开小差”
-- REMARK：备份eh_payment_variables表
-- select * from eh_payment_variables;

-- AUTHOR: 唐岑 2018年10月8日19:56:37
-- REMARK: 在瑞安CM部署时，需执行该任务（issue-38706）同步资产数据。详细步骤咨询 唐岑


-- AUTHOR: 黄明波 2018年10月27日17:31:00
-- REMARK: 备份eh_service_alliances, eh_flows表
-- REMARK: 以下接口参数ownerId 填 1802，需将返回字符串发给我
-- REMARK: /yellowPage/transferApprovalToForm 执行完后，需观察eh_general_form_vals表是否有记录持续新增（后台会持续更新10分钟左右，更新期间可以继续做后面接口,sql操作)。
-- REMARK: /yellowPage/transferMainAllianceOwnerType
-- REMARK: /yellowPage/transferAllianceModuleUrl
-- REMARK: /yellowPage/transferApprovalFlowCases
-- REMARK: /yellowPage/transferPadItems

-- --------------------- SECTION END OPERATION------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境


-- AUTHOR:黄明波
-- REMARK:服务联盟数据迁移 迁移1~迁移8
-- 迁移 start
-- 迁移1.调整ca表的ownerType和ownerId
update eh_service_alliance_categories ca, eh_service_alliances sa
set ca.owner_type = sa.owner_type, ca.owner_id = sa.owner_id, ca.`type` = ca.id
,ca.enable_provider = ifnull(sa.integral_tag3, 0) , ca.enable_comment = ifnull(sa.enable_comment, 0), ca.description = sa.description
where ca.parent_id = 0 and sa.`type` = ca.id and sa.owner_type = 'organaization';


-- 迁移2.调整ca表子类的ownerType ownerId, type
update eh_service_alliance_categories  cag1,  eh_service_alliance_categories  cag2
set cag1.owner_type = cag2.owner_type, cag1.owner_id = cag2.owner_id, cag1.`type` = cag2.`type`
where cag1.parent_id = cag2.id;


-- 迁移3.更新ca表skip_rule
update eh_service_alliance_categories ca, eh_service_alliance_skip_rule sr
set ca.skip_type = 1, ca.delete_uid = -100 where ca.id = sr.service_alliance_category_id and sr.id is not null and ca.namespace_id = sr.namespace_id;


-- 迁移4.tag表填充ownerType ownerId
update eh_alliance_tag tag, eh_service_alliances sa
set tag.owner_type = sa.owner_type, tag.owner_id = sa.owner_id
where tag.type = sa.type and sa.parent_id = 0 and tag.type <> 0 ;

-- 迁移5.jumpType应用跳转时，设置为3
update eh_service_alliances
set  integral_tag1 = 3
where module_url not like 'zl://approva%' and  module_url not like 'zl://form%' and  module_url is not null and integral_tag1 = 2;

-- 迁移5.1 迁移首页图片
update eh_service_alliance_attachments st, eh_service_alliances sa set st.owner_type = 'EhServiceAllianceCategories', st.owner_id = sa.`type`
where sa.parent_id = 0 and sa.owner_id > 0 and sa.`type` <> 0 and st.owner_type = 'EhServiceAlliances' and st.owner_id = sa.id;

-- 迁移6.工作流
update eh_flows fl, eh_general_approvals ap
set fl.owner_id = ap.owner_id, fl.owner_type = 'SERVICE_ALLIANCE', fl.string_tag5 = ap.id
where fl.owner_type = 'GENERAL_APPROVAL' and fl.module_id = 40500 and fl.owner_id = ap.id and fl.owner_type <> 'SERVICE_ALLIANCE' ;


-- 迁移7.添加基础数据
DELIMITER $$  -- 开始符

CREATE PROCEDURE alliance_transfer_add_base_ca(

) -- 声明存储过程

READS SQL DATA
SQL SECURITY INVOKER

BEGIN

DECLARE  no_more_record INT DEFAULT 0;
DECLARE  pName varchar(64);
DECLARE pNamespaceId INT;
DECLARE pType BIGINT(20);

DECLARE  cur_record CURSOR FOR   SELECT  name,  namespace_id, `type` from eh_service_alliance_categories where parent_id = 0;  -- 首先这里对游标进行定义
 DECLARE  CONTINUE HANDLER FOR NOT FOUND  SET  no_more_record = 1; -- 这个是个条件处理,针对NOT FOUND的条件,当没有记录时赋值为1

 OPEN  cur_record; -- 接着使用OPEN打开游标
 FETCH  cur_record INTO pName, pNamespaceId, pType; -- 把第一行数据写入变量中,游标也随之指向了记录的第一行


 SET @max_id = (select max(id) from eh_service_alliance_categories);

 WHILE no_more_record != 1 DO
 INSERT  INTO eh_service_alliance_categories(id, name, namespace_id, parent_id, owner_type, owner_id,creator_uid,`status`, `type`)
 VALUES  (@max_id:=@max_id+1, pName, pNamespaceId, 0, 'organaization', -1, 3, 2, pType );
 FETCH  cur_record INTO pName, pNamespaceId, pType;

 END WHILE;
 CLOSE  cur_record;  -- 用完后记得用CLOSE把资源释放掉

END

$$

DELIMITER ; -- 结束符

call alliance_transfer_add_base_ca();

DROP PROCEDURE IF EXISTS alliance_transfer_add_base_ca;


-- 迁移8.添加服务与类型的关联到match表
DELIMITER $$  -- 开始符

CREATE PROCEDURE alliance_transfer_add_match(

) -- 声明存储过程

READS SQL DATA
SQL SECURITY INVOKER

BEGIN

DECLARE  no_more_record INT DEFAULT 0;
DECLARE  pServiceId BIGINT(20);
DECLARE  pCategoryId BIGINT(20);
DECLARE  pNamespaceId BIGINT(20);
DECLARE  pOwnerType VARCHAR(50);
DECLARE  pOwnerId BIGINT(20);
DECLARE  pType BIGINT(20);
DECLARE  pCategoryName VARCHAR(64);

-- 首先这里对游标进行定义
DECLARE  cur_record CURSOR FOR
SELECT  sa.id, sa.category_id, ca.name, ca.namespace_id,  ca.owner_type, ca.owner_id, ca.`type`
from eh_service_alliances sa, eh_service_alliance_categories ca
where sa.category_id = ca.id and sa.category_id is not null and sa.parent_id <> 0;

-- 这个是个条件处理,针对NOT FOUND的条件,当没有记录时赋值为1
DECLARE  CONTINUE HANDLER FOR NOT FOUND  SET  no_more_record = 1;

 OPEN  cur_record; -- 接着使用OPEN打开游标
 FETCH  cur_record INTO pServiceId, pCategoryId, pCategoryName,  pNamespaceId, pOwnerType, pOwnerId, pType; -- 把第一行数据写入变量中,游标也随之指向了记录的第一行

 SET @max_id = (select ifnull(max(id),0) from eh_alliance_service_category_match);

 WHILE no_more_record != 1 DO

 INSERT  INTO eh_alliance_service_category_match(id, namespace_id, owner_type, owner_id, `type`, service_id, category_id, category_name,create_time, create_uid)
 VALUES  (@max_id:=@max_id+1, pNamespaceId, pOwnerType, pOwnerId, pType, pServiceId, pCategoryId, pCategoryName, now(), 3 );
 FETCH  cur_record INTO pServiceId, pCategoryId, pCategoryName,  pNamespaceId, pOwnerType, pOwnerId, pType;

 END WHILE;
 CLOSE  cur_record;  -- 用完后记得用CLOSE把资源释放掉

END

$$

DELIMITER ; -- 结束符

call alliance_transfer_add_match(); -- 执行

DROP PROCEDURE IF EXISTS alliance_transfer_add_match;  -- 删除该存储过程
-- 迁移 end


-- AUTHOR:黄明波
-- REMARK:云打印账号迁移
update eh_siyin_print_business_payee_accounts ac set ac.merchant_id = ac.payee_id ;
update eh_service_modules set client_handler_type = 2 where id = 40500;
update eh_service_modules set client_handler_type = 2 where id = 10800;



-- AUTHOR:杨崇鑫 20181018
-- REMARK:解决缺陷 #39352: 【全量标准版】【物业缴费】新增收费标准前端提示成功，但实际未新增成功，无相关数据，后台提示“应用开小差”
delete from eh_payment_variables;
INSERT INTO `eh_payment_variables`(`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (1, NULL, NULL, '单价', 0, '2017-11-02 12:51:43', NULL, '2017-11-02 12:51:43', 'dj');
INSERT INTO `eh_payment_variables`(`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (2, NULL, 1, '面积', 0, '2017-11-02 12:51:43', NULL, '2017-11-02 12:51:43', 'mj');
INSERT INTO `eh_payment_variables`(`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (3, NULL, 6, '固定金额', 0, '2017-11-02 12:51:43', NULL, '2017-11-02 12:51:43', 'gdje');
INSERT INTO `eh_payment_variables`(`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (4, NULL, 5, '用量', 0, '2017-11-02 12:51:43', NULL, '2017-11-02 12:51:43', 'yl');
INSERT INTO `eh_payment_variables`(`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (5, NULL, 6, '欠费', 0, '2017-10-16 09:31:00', NULL, '2017-10-16 09:31:00', 'qf');
INSERT INTO `eh_payment_variables`(`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (7, NULL, NULL, '比例系数', 0, '2018-05-04 21:34:48', NULL, '2018-05-04 21:34:48', 'blxs');
INSERT INTO `eh_payment_variables`(`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (8, NULL, NULL, '折扣', 0, '2018-05-23 02:09:38', NULL, '2018-05-23 02:09:38', 'zk');


-- AUTHOR:杨崇鑫 20181015
-- REMARK:补充缴费模块“应用开小差”的错误码
SET @locale_string_id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
	VALUES ((@locale_string_id := @locale_string_id + 1), 'assetv2', '10012', 'zh_CN', '第三方授权异常');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
	VALUES ((@locale_string_id := @locale_string_id + 1), 'assetv2', '10013', 'zh_CN', '收费项标准公式不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
	VALUES ((@locale_string_id := @locale_string_id + 1), 'assetv2', '10014', 'zh_CN', '收费项标准类型错误');

-- 更新 layout
SET @versionCode = '201810110200';

SET @bizAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 92100 AND `namespace_id` = 2);
SET @activityAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 10600 AND `namespace_id` = 2);
SET @forumAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 10100 AND `namespace_id` = 2);
SET @newsAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 10800 AND `namespace_id` = 2);
SET @communityBulletinsAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 10300 AND `namespace_id` = 2);
SET @enterpriseBulletinsAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 57000 AND `namespace_id` = 2);

UPDATE eh_launch_pad_layouts set version_code = @versionCode, layout_json  = CONCAT('{"versionCode":"',@versionCode,'","layoutName":\"ServiceMarketLayout\",\"displayName\":\"工作台\",\"groups\":[{\"defaultOrder\":3,\"groupName\":\"公告\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"rowCount\":1.0,\"style\":2.0,\"shadow\":1.0,\"moduleId\":57000.0,\"appId\":', @enterpriseBulletinsAppId ,'},\"separatorFlag\":0,\"separatorHeight\":0,\"widget\":\"Bulletins\"},{\"columnCount\":1,\"defaultOrder\":2,\"groupName\":\"园区运营\","title":"园区运营","titleFlag":1,\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"paddingTop\":16.0,\"paddingLeft\":16.0,\"paddingBottom\":16.0,\"paddingRight\":16.0,\"lineSpacing\":0.0,\"columnSpacing\":0.0,\"backgroundColor\":\"#ffffff\",\"appType\":1.0},\"separatorFlag\":0,\"separatorHeight\":0,\"style\":\"Default\",\"widget\":\"Card\"},{\"columnCount\":1,\"defaultOrder\":4,\"groupName\":\"企业办公\","title":"企业办公","titleFlag":1,\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"paddingTop\":16.0,\"paddingLeft\":16.0,\"paddingBottom\":16.0,\"paddingRight\":16.0,\"lineSpacing\":0.0,\"columnSpacing\":0.0,\"backgroundColor\":\"#ffffff\",\"appType\":0.0},\"separatorFlag\":0,\"separatorHeight\":0,\"style\":\"Default\",\"widget\":\"Card\"}]}') WHERE type = 4 AND namespace_id = 2;
UPDATE eh_launch_pad_layouts set version_code = @versionCode, layout_json  = CONCAT('{"versionCode":"',@versionCode,'","layoutName":\"ServiceMarketLayout\",\"displayName\":\"服务广场\",\"groups\":[{\"defaultOrder\":1,\"groupName\":\"banner图片1\",\"style\":\"Shape\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31056\",\"widthRatio\":16.0,\"heightRatio\":9.0,\"shadowFlag\":1.0,\"paddingFlag\":1.0},\"separatorFlag\":0,\"separatorHeight\":0,\"widget\":\"Banners\"},{\"groupId\":0,\"groupName\":\"容器\","title":"容器","titleFlag":0,"titleStyle":101,"titleSize":2,"titleMoreFlag":0,\"columnCount\":4,\"defaultOrder\":2,\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"paddingTop\":0.0,\"paddingLeft\":16.0,\"paddingBottom\":0.0,\"paddingRight\":16.0,\"lineSpacing\":0.0,\"columnSpacing\":0.0,\"cssStyleFlag\":1.0,\"backgroundColor\":\"#ffffff\",\"allAppFlag\":1.0},\"separatorFlag\":0,\"separatorHeight\":0,\"style\":\"Default\",\"widget\":\"Navigator\"},{\"defaultOrder\":3,\"groupName\":\"公告\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31057\",\"rowCount\":1.0,\"style\":2.0,\"shadow\":1.0,\"moduleId\":10300.0,\"appId\":', @communityBulletinsAppId ,'},\"separatorFlag\":0,\"separatorHeight\":0,\"widget\":\"Bulletins\"},{\"groupName\":\"电商入口\",\"widget\":\"NavigatorTemp\"},{\"defaultOrder\":4,\"groupName\":\"商品精选\","title":"商品精选","titleFlag":1,"titleStyle":101,"titleSize":2,"titleMoreFlag":1,\"instanceConfig\":{\"itemGroup\":\"OPPushBiz\",\"moduleId\":92100.0,\"appId\":', @bizAppId, ',\"entityCount\":5.0,\"appConfig\":{}},\"separatorFlag\":1,\"separatorHeight\":0,\"style\":\"HorizontalScrollSquareView\",\"widget\":\"OPPush\"},{\"defaultOrder\":5,\"groupName\":\"活动\","title":"活动","titleFlag":1,"titleStyle":101,"titleSize":2,"titleMoreFlag":1,\"instanceConfig\":{\"itemGroup\":\"OPPushActivity\",\"entityCount\":5.0,\"subjectHeight\":0.0,\"descriptionHeight\":0.0,\"newsSize\":5.0,\"moduleId\":10600.0,\"appId\":', @activityAppId, ',\"actionType\":61.0,\"appConfig\":{\"categoryId\":1.0,\"publishPrivilege\":1.0,\"livePrivilege\":0.0,\"listStyle\":2.0,\"scope\":3.0,\"style\":4.0}},\"separatorFlag\":1,\"separatorHeight\":0,\"style\":\"HorizontalScrollWideView\",\"widget\":\"OPPush\"},{\"defaultOrder\":7,\"groupName\":\"论坛\","title":"论坛","titleFlag":1,"titleStyle":101,"titleSize":2,"titleMoreFlag":1,\"instanceConfig\":{\"moduleId\":10100.0,\"appId\":', @forumAppId, ',\"actionType\":62.0,\"newsSize\":5.0,\"appConfig\":{}},\"separatorFlag\":1,\"separatorHeight\":0,\"style\":\"TextImageWithTagListView\",\"widget\":\"OPPush\"},{\"defaultOrder\":8,\"groupName\":\"园区快讯\","title":"园区快讯","titleFlag":1,"titleStyle":101,"titleSize":2,"titleMoreFlag":1,\"instanceConfig\":{\"moduleId\":10800,\"appId\":', @newsAppId, ',\"actionType\":48,\"newsSize\":5.0,\"appConfig\":{}},\"separatorFlag\":1,\"separatorHeight\":0,\"style\":\"NewsListView\",\"widget\":\"OPPush\"}]}') WHERE type = 5 AND namespace_id = 2 ;




-- 企业访客 设置oa模块
UPDATE eh_service_modules set app_type = 0 WHERE id in (52100, 52200);
-- 更新应用信息
UPDATE eh_service_module_apps a set app_type = 0 WHERE module_id in (52100, 52200);

-- 默认的微信消息模板Id
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('wx.default.template.id', 'JnTt-ce69Wlie-o8nv4Jhl3CKA0pXaageIsr4aJiWCk', '默认的微信消息模板Id', '0', NULL, '1');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('wx.default.template.url', 'http://www.zuolin.com/', '默认的微信消息模板url', '0', NULL, '1');


-- AUTHOR: 荣楠
-- REMARK: 组织架构4.6
SET @locale_id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@locale_id := @locale_id + 1, 'archives', '100015', 'zh_CN', '账号重复');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@locale_id := @locale_id + 1, 'archives', '100016', 'zh_CN', '账号长度不对或格式错误');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@locale_id := @locale_id + 1, 'archives', '100017', 'zh_CN', '账号一经设定，无法修改');


-- AUTHOR: 严军
-- REMARK: 客户端处理方式
update eh_service_modules set client_handler_type = 2 WHERE id in (41700, 20100,40730,41200);
UPDATE eh_service_modules SET client_handler_type = 1 WHERE id in (90100,  180000);


-- AUTHOR: 严军
-- REMARK: 云打印设置为园区应用
UPDATE eh_service_modules set app_type = 1 WHERE id = 41400;
UPDATE eh_service_module_apps a set app_type = 1 WHERE module_id = 41400;

UPDATE eh_service_modules set instance_config = '{"url":"${home.url}/cloud-print/build/index.html#/home#sign_suffix"}' WHERE id = 41400;
UPDATE eh_service_module_apps set instance_config = '{"url":"${home.url}/cloud-print/build/index.html#/home#sign_suffix"}' WHERE module_id = 41400;

-- AUTHOR: 严军
-- REMARK: 工位预定客户端处理方式设置为内部链接
update eh_service_modules set client_handler_type = 2 WHERE id in (40200);


-- AUTHOR: 严军
-- REMARK: 开放“应用入口”菜单
DELETE FROM eh_web_menus WHERE id = 15010000;
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15010000', '基础数据', '15000000', NULL, NULL, '1', '2', '/15000000/15010000', 'zuolin', '20', NULL, '2', 'system', 'classify', NULL);
DELETE FROM eh_web_menus WHERE id = 15025000;
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15025000', '应用入口', '15010000', NULL, 'servicemodule-entry', '1', '2', '/15000000/15010000/15025000', 'zuolin', '30', NULL, '3', 'system', 'module', NULL);

-- AUTHOR: 严军
-- REMARK: 设置默认的应用分类
UPDATE eh_service_modules set app_type = 1 WHERE app_type is NULL;
UPDATE eh_service_module_apps a set a.app_type = IFNULL((SELECT b.app_type from eh_service_modules b where b.id = a.module_id), 1);

update eh_service_modules set client_handler_type = 2 WHERE id = 43000;

-- AUTHOR: xq.tian
-- REMARK: 用户名或密码错误提示 add by xq.tian  2018/10/11
SET @eh_locale_strings_id = (SELECT MAX(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
	VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'user', '100020', 'zh_CN', '用户名或密码错误');

-- AUTHOR: 缪洲 20181008
-- REMARK: issue-38650 增加error消息模板
INSERT INTO `eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('parking', '10034', 'zh_CN', '接口参数缺失');
INSERT INTO `eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('parking', '10035', 'zh_CN', '接口参数异常');
INSERT INTO `eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('parking', '10036', 'zh_CN', '订单状态异常');
INSERT INTO `eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('parking', '10037', 'zh_CN', '文件导出失败');
INSERT INTO `eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('parking', '10038', 'zh_CN', '工作流未开启');
INSERT INTO `eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('parking', '10039', 'zh_CN', '对象不存在');

-- AUTHOR: 马世亨 20181008
-- REMARK: issue-38650 增加error消息模板
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask','10020','zh_CN','同步搜索引擎失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask','10021','zh_CN','查询失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask','10022','zh_CN','文件导出失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask','10023','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask','10024','zh_CN','第三方返回失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask','10025','zh_CN','接口参数异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1414','zh_CN','同步搜索引擎失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1415','zh_CN','接口参数异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1416','zh_CN','接口参数缺失');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1417','zh_CN','二维码下载失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1418','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1419','zh_CN','文件导出失败');

-- by st.zheng
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'relocation', '506', 'zh_CN', '非法参数', '非法参数', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'rental', '506', 'zh_CN', '非法参数', '非法参数', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'rental', '507', 'zh_CN', '参数缺失', '参数缺失', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'rental', '508', 'zh_CN', '资源或资源规则缺失', '资源或资源规则缺失', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'rental', '509', 'zh_CN', '找不到订单或订单状态错误', '找不到订单或订单状态错误', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'rental', '510', 'zh_CN', '下单失败', '下单失败', '0');

update eh_rentalv2_pay_accounts set merchant_id = account_id;


-- AUTHOR: 黄明波 20181008
-- REMARK: issue-38650 增加error消息模板
-- yellowPage
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '10012', 'zh_CN', '评论不存在或已被删除');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '10013', 'zh_CN', '文件导出失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '10014', 'zh_CN', '跳转链接格式错误');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '10015', 'zh_CN', '接口参数异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '10016', 'zh_CN', '获取电商模块失败');


-- express
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10003', 'zh_CN', 'URL加密失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10004', 'zh_CN', '请求失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10005', 'zh_CN', '接口参数缺失');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10006', 'zh_CN', '接口参数异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10007', 'zh_CN', '订单不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10008', 'zh_CN', '获取公司失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10009', 'zh_CN', '用户鉴权失败，请重新登录');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10010', 'zh_CN', '订单异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10011', 'zh_CN', '第三方返回失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10012', 'zh_CN', '支付鉴权失败');

-- news
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10017', 'zh_CN', '评论不存在或已被删除');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10018', 'zh_CN', '接口参数异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10019', 'zh_CN', '无效的快讯类型id');

-- print

INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', '10000', 'zh_CN', '订单不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', '10001', 'zh_CN', '订单异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', '10002', 'zh_CN', '邮箱地址格式错误');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', '10003', 'zh_CN', '接口参数缺失');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', '10004', 'zh_CN', '接口参数异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', '10005', 'zh_CN', '获取打印任务失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', '10006', 'zh_CN', '订单不存在或已支付');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', '10007', 'zh_CN', '打印机解锁失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', '10008', 'zh_CN', '第三方返回失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', '10009', 'zh_CN', '扫码失败，请重试');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', '10010', 'zh_CN', '有未支付订单，请支付后重试');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', '10011', 'zh_CN', '订单已支付');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', '10012', 'zh_CN', '锁定订单失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', '10013', 'zh_CN', '文件导出失败');


-- 马世亨 2018-10-10
-- 访客管理1.3 合并访客应用
update eh_service_modules set instance_config = '{"url":"${home.url}/visitor-management/build/index.html?ns=%s&appId=%s&ownerType=community#/home#sign_suffix"}' where id = 41800;
update eh_service_modules set instance_config = '{"url":"${home.url}/visitor-appointment/build/index.html?ns=%s&appId=%s&ownerType=enterprise#/home#sign_suffix"}' where id = 52100;
delete from eh_service_modules where id in (42100,52200);
-- end

-- 马世亨 2018-10-10
-- 访客管理1.3 企业访客权限
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`)
VALUES ('52110', '预约管理', '52100', '/100/50000/52100/52110', '1', '4', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '', '1', '1', NULL);

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`)
VALUES ('52120', '访客管理', '52100', '/100/50000/52100/52120', '1', '4', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '', '1', '1', NULL);

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`)
VALUES ('52130', '设备管理', '52100', '/100/50000/52100/52130', '1', '4', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '', '1', '1', NULL);

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`)
VALUES ('52140', '移动端管理', '52100', '/100/50000/52100/52140', '1', '4', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '', '1', '1', NULL);


set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@privilege_id:=@privilege_id+1, '52100', '0', '5210052100', '全部权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (5210052110, '0', '企业访客 预约管理权限', '企业访客 预约管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@privilege_id:=@privilege_id+1, '52110', '0', 5210052110, '预约管理权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (5210052120, '0', '企业访客 访客管理权限', '企业访客 访客管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@privilege_id:=@privilege_id+1, '52120', '0', 5210052120, '访客管理权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (5210052130, '0', '企业访客 设备管理权限', '企业访客 设备管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@privilege_id:=@privilege_id+1, '52130', '0', 5210052130, '设备管理权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (5210052140, '0', '企业访客 移动端管理权限', '企业访客 移动端管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@privilege_id:=@privilege_id+1, '52140', '0', 5210052140, '移动端管理权限', '0', now());


-- AUTHOR: 唐岑2018年10月17日20:32:09
-- REMARK: issue-38650 增加error消息模板
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','101','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','102','zh_CN','接口参数异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','103','zh_CN','接口参数缺失');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','104','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','105','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','106','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','107','zh_CN','消息内容为空');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','108','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','109','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','110','zh_CN','上传文件为空');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','111','zh_CN','解析文件失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','112','zh_CN','账单数据重复');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','113','zh_CN','服务器内部错误');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','114','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','115','zh_CN','该用户未欠费，不能向其发送催缴短信');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','116','zh_CN','支付方式不支持');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','117','zh_CN','订单不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','118','zh_CN','账单无效');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','119','zh_CN','用户权限不足');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','120','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','121','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','122','zh_CN','excel数据格式不正确');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','123','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','124','zh_CN','创建预约计划失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','125','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','126','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','127','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','128','zh_CN','导出文件失败');

-- end

-- AUTHOR: xq.tian 2018-10-19
-- REMARK: 驳回按钮的默认跟踪
UPDATE eh_locale_strings SET text='任务已被 ${text_tracker_curr_operator_name} 驳回' WHERE scope='flow' AND code='20005';


-- AUTHOR: 严军 2018-10-21
-- REMARK: issue-38924 修改菜单
-- 一级菜单
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('25000000', '资产管理系统', '0', NULL, NULL, '1', '2', '/25000000', 'zuolin', '23', NULL, '1', 'system', 'classify', NULL, '1');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('26000000', '物业服务系统', '0', NULL, NULL, '1', '2', '/26000000', 'zuolin', '26', NULL, '1', 'system', 'classify', NULL, '1');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('27000000', '统计分析', '0', NULL, NULL, '1', '2', '/27000000', 'zuolin', '60', NULL, '1', 'system', 'classify', NULL, '1');
UPDATE eh_web_menus set `name` = '园区运营系统' WHERE id = 16000000;
UPDATE eh_web_menus set `name` = '企业办公系统' WHERE id = 23000000;
-- 资产管理系统
UPDATE eh_web_menus set parent_id = 25000000, sort_num = 10 WHERE id = 16010000;
UPDATE eh_web_menus set parent_id = 25000000, sort_num = 20 WHERE id = 16210000;
UPDATE eh_web_menus SET path = replace(path, '/16000000/', '/25000000/') WHERE parent_id in (16010000, 16210000) OR id in (16010000, 16210000);
-- 物业服务系统
UPDATE eh_web_menus set parent_id = 26000000, sort_num = 10, `name` = '物业服务' WHERE id = 16050000;
UPDATE eh_web_menus SET path = replace(path, '/16000000/', '/26000000/') WHERE parent_id = 16050000 or id = 16050000;
UPDATE eh_web_menus SET `status` = 0 WHERE id = 16050400;
-- 园区运营系统
UPDATE eh_web_menus SET `status` = 2, parent_id = 16400000, path = '/16000000/16400000/16020500' WHERE id = 16020500;
UPDATE eh_web_menus SET `name` = '收款账户管理' WHERE id = 16070000;
-- 统计分析
UPDATE eh_web_menus set parent_id = 27000000, sort_num = 10, `name` = '统计分析' WHERE id = 17000000;
UPDATE eh_web_menus SET path = replace(path, '/16000000/', '/27000000/') WHERE parent_id = 17000000 or id = 17000000;
-- 企业办公系统
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('23020000', '协同办公', '23000000', NULL, NULL, '1', '2', '/23000000/23020000', 'zuolin', '10', NULL, '2', 'system', 'classify', NULL, '1');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('23030000', '人力资源', '23000000', NULL, NULL, '1', '0', '/23000000/23030000', 'zuolin', '20', NULL, '2', 'system', 'classify', NULL, '1');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('23040000', '支付管理', '23000000', NULL, NULL, '1', '2', '/23000000/23040000', 'zuolin', '50', NULL, '2', 'system', 'classify', NULL, '1');
UPDATE eh_web_menus SET parent_id = 23040000, path = '/23000000/23040000/78000001' WHERE id = 78000001;
UPDATE eh_web_menus SET parent_id = 23040000, path = '/23000000/23040000/79100000' WHERE id = 79100000;

-- 资产管理系统
UPDATE eh_web_menus set parent_id = 15000000, sort_num = 25 WHERE id = 20000000;
UPDATE eh_web_menus set parent_id = 15000000, sort_num = 5 WHERE id = 21000000;
UPDATE eh_web_menus set parent_id = 15000000, sort_num = 90 WHERE id = 22000000;
UPDATE eh_web_menus SET path = replace(path, '/11000000/', '/15000000/') WHERE parent_id in (20000000, 21000000, 22000000) OR id in (20000000, 21000000, 22000000);
UPDATE eh_web_menus SET `status` = 0 WHERE id = 11000000;
UPDATE eh_web_menus SET `status` = 0 WHERE id = 23020000;
UPDATE eh_web_menus set parent_id = 25000000, sort_num = 30 WHERE id = 16050000;
UPDATE eh_web_menus SET path = replace(path, '/26000000/', '/25000000/') WHERE parent_id = 16050000 OR id = 16050000;
UPDATE eh_web_menus SET `status` = 0 WHERE id = 26000000;
UPDATE eh_web_menus SET `name` = '资管物业业务' WHERE id = 25000000;
UPDATE eh_web_menus set `name` = '园区运营业务' WHERE id = 16000000;
UPDATE eh_web_menus set `name` = '企业办公业务' WHERE id = 23000000;
UPDATE eh_web_menus set `name` = '统计分析业务' WHERE id = 27000000;



-- AUTHOR: 严军 2018-10-21
-- REMARK: issue-null 增加模块路由
update eh_service_modules set host = 'bulletin'  where id = 	10300;
update eh_service_modules set host = 'activity'  where id = 	10600;
update eh_service_modules set host = 'post'  where id = 	10100;
update eh_service_modules set host = 'group'  where id = 	10750;
update eh_service_modules set host = 'group'  where id = 	10760;
update eh_service_modules set host = 'approval'  where id = 	52000;
update eh_service_modules set host = 'work-report'  where id = 	54000;
update eh_service_modules set host = 'file-management'  where id = 	55000;
update eh_service_modules set host = 'remind'  where id = 	59100;
update eh_service_modules set host = 'meeting-reservation'  where id = 	53000;
update eh_service_modules set host = 'video-conference'  where id = 	50700;
update eh_service_modules set host = 'enterprise-bulletin'  where id = 	57000;
update eh_service_modules set host = 'enterprise-contact'  where id = 	50100;
update eh_service_modules set host = 'attendance'  where id = 	50600;
update eh_service_modules set host = 'salary'  where id = 	51400;
update eh_service_modules set host = 'station'  where id = 	40200;
update eh_service_modules set host = 'news-feed'  where id = 	10800;
update eh_service_modules set host = 'questionnaire'  where id = 	41700;
update eh_service_modules set host = 'hot-line'  where id = 	40300;
update eh_service_modules set host = 'property-repair'  where id = 	20100;
update eh_service_modules set host = 'resource-reservation'  where id = 	40400;
update eh_service_modules set host = 'visitor'  where id = 	41800;
update eh_service_modules set host = 'parking'  where id = 	40800;
update eh_service_modules set host = 'vehicle-release'  where id = 	20900;
update eh_service_modules set host = 'cloud-print'  where id = 	41400;
update eh_service_modules set host = 'item-release'  where id = 	49200;
update eh_service_modules set host = 'decoration'  where id = 	22000;
update eh_service_modules set host = 'service-alliance'  where id = 	40500;
update eh_service_modules set host = 'wifi'  where id = 	41100;
update eh_service_modules set host = 'park-enterprises'  where id = 	33000;
update eh_service_modules set host = 'park-settle'  where id = 	40100;
update eh_service_modules set host = 'property-payment'  where id = 	20400;
update eh_service_modules set host = 'property-inspection'  where id = 	20800;
update eh_service_modules set host = 'quality'  where id = 	20600;
update eh_service_modules set host = 'energy-management'  where id = 	49100;
update eh_service_modules set host = 'customer-management'  where id = 	21100;

update eh_service_modules set host = 'access-control'  where id = 	41000;
update eh_service_modules set client_handler_type = 2  where id = 	40700;
update eh_service_modules set client_handler_type = 2  where id = 	10800;

-- AUTHOR: st.zheng
-- REMARK: 增加商户管理模块及菜单
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`) VALUES ('210000', '商户管理', '170000', '/200/170000/210000', '1', '3', '2', '110', '2018-03-19 17:52:57', NULL, NULL, '2018-03-19 17:53:11', '0', '0', '0', '0', 'community_control', '1', '1', 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('79820000', '商户管理', '16400000', NULL, 'business-management', '1', '2', '/16000000/16400000/79820000', 'zuolin', '120', '210000', '3', 'system', 'module', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('79830000', '商户管理', '56000000', NULL, 'business-management', '1', '2', '/40000040/56000000/79830000', 'park', '120', '210000', '3', 'system', 'module', '2');

-- AUTHOR: st.zheng
-- REMARK: 资源预订3.7.1
ALTER TABLE `eh_rentalv2_resources`
MODIFY COLUMN `aclink_id`  text  NULL AFTER `default_order`;
ALTER TABLE `eh_rentalv2_orders`
MODIFY COLUMN `door_auth_id`  text  NULL AFTER `auth_end_time`;
update eh_rentalv2_orders set pay_channel = 'normal' where pay_channel is null;


-- AUTHOR: 缪洲 20180930
-- REMARK: issue-34780 增加企业支付授权页面
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES (79800000, '企业支付授权', 16300000, NULL, 'payment-privileges', 1, 2, '/16000000/16300000/79800000', 'zuolin', 8, 200000, 3, 'system', 'module', NULL, 1);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES (79810000, '企业支付授权', 55000000, NULL, 'payment-privileges', 1, 2, '/40000040/55000000/79810000', 'park', 2, 200000, 3, 'system', 'module', NULL, 1);
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`, `app_type`, `client_handler_type`, `system_app_flag`, `icon_uri`) VALUES (200000, '企业支付授权', 140000, '/200/140000', 1, 3, 2, 10, '2018-09-26 16:51:46', '{}', 13, '2018-09-26 16:51:46', 0, 0, '0', NULL, 'community_control', 1, 1, 'module', NULL, 0, NULL, NULL);

-- AUTHOR: liangqishi
-- REMARK: 把原来统一订单的配置项前缀gorder改为prmt 20181006
UPDATE `eh_configurations` SET `value`=REPLACE(`value`, 'gorder', 'prmt') WHERE `name`='gorder.server.connect_url';
UPDATE `eh_configurations` SET `name`='prmt.server.connect_url' WHERE `name`='gorder.server.connect_url';
UPDATE `eh_configurations` SET `name`='prmt.server.app_key' WHERE `name`='gorder.server.app_key';
UPDATE `eh_configurations` SET `name`='prmt.server.app_secret' WHERE `name`='gorder.server.app_secret';
UPDATE `eh_configurations` SET `name`='prmt.default.personal_bind_phone' WHERE `name`='gorder.default.personal_bind_phone';
UPDATE `eh_configurations` SET `name`='prmt.system_id' WHERE `name`='gorder.system_id';

-- AUTHOR: 缪洲 20180930
-- REMARK: issue-34780 增加未支付推送与短信模板
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (3421, 'sms.default', 83, 'zh_CN', '未支付短信', '您有一笔云打印的订单未支付，请到云打印-打印记录中进行支付。', 0);

-- AUTHOR: 缪洲 201801011
-- REMARK: issue-34780 删除打印设置规则
DELETE FROM `eh_service_modules` WHERE parent_id = 41400 AND id = 41430;
DELETE FROM `eh_acl_privileges` WHERE id = 4140041430;
DELETE FROM `eh_service_module_privileges` WHERE privilege_id = 4140041430;

-- AUTHOR: 梁燕龙 20181026
-- REMARK: 行业协会路由修改
UPDATE eh_service_modules SET instance_config = '{"isGuild":1}' WHERE id = 10760;
UPDATE eh_service_module_apps SET instance_config = '{"isGuild":1}' WHERE module_id = 10760;
-- --------------------- SECTION END ALL -----------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本

-- AUTHOR: xq.tian
-- REMARK: 把基线的 2 域空间删掉，标准版不执行这个 sql
DELETE FROM eh_namespaces WHERE id=2;

-- AUTHOR: 黄明波
-- REMARK: 更新打印机名称
update eh_siyin_print_printers set printer_name = 'FX-ApeosPort-VI C3370' where reader_name = 'TC101154727022';
update eh_siyin_print_printers set printer_name = 'FX-AP-VI C3370-BJ' where reader_name = 'TC101154727294';
update eh_siyin_print_printers set printer_name = 'FX_AP_VIC3370' where reader_name = 'TC101154727497';
update eh_siyin_print_printers set printer_name = 'Zuolin' where reader_name = 'TC101157736913';
update eh_siyin_print_printers set printer_name = 'APV3373' where reader_name = 'TC100887870538';


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

-- AUTHOR: xq.tian
-- REMARK: 越空间独立部署的 root 用户的密码修改为: eh#1802
UPDATE eh_users SET password_hash='4eaded9b566765a1e70e2e0dc45204c14c4b9df41507a6b72c7cc7fe91d85341', salt='3023538e14053565b98fdfb2050c7709'
WHERE account_name='root' AND namespace_id=0;

-- --------------------- SECTION END guanzhouyuekongjian -------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ruianxintiandi
-- DESCRIPTION: 此SECTION只在上海瑞安新天地-999929执行的脚本
-- AUTHOR:梁燕龙  20181022
-- REMARK: 瑞安个人中心跳转URL
INSERT INTO eh_configurations (name, value, description, namespace_id, display_name)
VALUES ('ruian.point.url','https://m.mallcoo.cn/a/user/10764/Point/List','瑞安积分跳转URL',999929, '瑞安积分跳转URL');
INSERT INTO eh_configurations (name, value, description, namespace_id, display_name)
VALUES ('ruian.vip.url','https://m.mallcoo.cn/a/custom/10764/xtd/Rights','瑞安会员跳转URL',999929, '瑞安会员跳转URL');
INSERT INTO eh_configurations (name, value, description, namespace_id, display_name)
VALUES ('ruian.order.url','/zl-ec/rest/service/front/logon?sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fmall%2findex.html#sign_suffix','瑞安订单跳转URL',999929, '瑞安订单跳转URL');
INSERT INTO eh_configurations (name, value, description)
VALUES ('ruian.coupon.url','https://inno.xintiandi.com/promotion/app-coupon?systemId=16#/','瑞安新天地卡券链接');


-- AUTHOR:黄良铭  20181025
-- REMARK: 瑞安活动对接配置项
-- 默认
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.publickey', 'd2NP2Z','publickey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ( 'mall.ruian.privatekey', 'a6cfff2c4aa370f8','privatekey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.appid', '5b5046c988ce7e5ad49c9b10','appid','999929','');

-- 上海新天地
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.publickey.1', 'd2NP2Z','上海新天地publickey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.privatekey.1', 'a6cfff2c4aa370f8','上海新天地privatekey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.appid.1', '5b5046c988ce7e5ad49c9b10','上海新天地appid','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'ruian.mall.id.1', '10764','上海新天地mallid','999929','');


-- 重庆天地
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ( 'mall.ruian.publickey.2', 'o7Oep_','重庆天地publickey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.privatekey.2', '7d57f43738f546e2','重庆天地privatekey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.appid.2', '5b505c8688ce7e238c3c3a2a','重庆天地appid','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'ruian.mall.id.2', '10782','重庆天地mallid','999929','');

-- 岭南天地
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.publickey.3', 'bOT1fy','岭南天地publickey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.privatekey.3', 'ef94c7e11445aebd','岭南天地privatekey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.appid.3', '5b505b8e3ae74e465c93447b','岭南天地appid','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'ruian.mall.id.3', '10778','岭南天地mallid','999929','');


-- 虹桥天地
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ( 'mall.ruian.publickey.4', '6pqMSA','虹桥天地publickey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.privatekey.4', '761604f49636c418','虹桥天地privatekey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.appid.4', '5b505ac188ce7e238c3c3a28','虹桥天地appid','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'ruian.mall.id.4', '10743','虹桥天地mallid','999929','');

-- 创智天地
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.publickey.5', 'XWLzKN','创智天地publickey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.privatekey.5', 'cfeb935979f50825','创智天地privatekey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.appid.5', '5b5048333ae74e58743209f7','创智天地appid','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'ruian.mall.id.5', '10776','创智天地mallid','999929','');

-- 瑞虹天地
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.publickey.6', 'ydVQ7f','瑞虹天地publickey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.privatekey.6', '24f36ef07865a906','瑞虹天地privatekey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.appid.6', '5b5047103ae74e58743209f3','瑞虹天地appid','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'ruian.mall.id.6', '10775','瑞虹天地mallid','999929','');


INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'activity.butt.url.getcategorylist', 'https://openapi10.mallcoo.cn/Event/Activity/V1/GetCategoryList/','获取活动分类','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'activity.butt.url.getactivitylist', 'https://openapi10.mallcoo.cn/Event/Activity/V1/GetList/','获取活动列表','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ( 'activity.butt.url.getactivity', 'https://openapi10.mallcoo.cn/Event/Activity/V1/GetDetail/','获取活动详情','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.url.activity', 'https://m.mallcoo.cn/a/custom/10764/xtd/activitylist','瑞安活动列表面URL','999929','');

-- AUTHOR: 唐岑
-- REMARK: 修改楼宇资产管理web menu的module id
UPDATE eh_web_menus SET module_id=38000 WHERE id=16010100;

-- AUTHOR: 唐岑
-- REMARK: 创建新的园区
-- 添加园区
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`, `area_size`, `shared_area`, `charge_area`, `build_area`, `rent_area`, `namespace_community_type`, `namespace_community_token`, `community_number`, `free_area`) VALUES ('240111044332063579', 'ca94e3a2-f36e-4033-9cbc-869881b643a4', '21977', '南京市', '21978', '玄武', 'SOP Office', NULL, '南京市玄武区珠江路未来城', NULL, 'TPQ项目', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '1', NULL, '2', '2018-08-27 14:20:21', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '196430', '196431', NULL, '999929', NULL, NULL, NULL, NULL, NULL, 'ruian_cm', '299', NULL, NULL);
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`, `area_size`, `shared_area`, `charge_area`, `build_area`, `rent_area`, `namespace_community_type`, `namespace_community_token`, `community_number`, `free_area`) VALUES ('240111044332063580', '8c5043ee-846f-4927-bb17-3ef65b999f0d', '21977', '南京市', '21978', '玄武', 'Inno Office', NULL, '南京市玄武区珠江路未来城', NULL, 'INNO创智', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '1', NULL, '2', '2018-08-27 16:03:02', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '196430', '196431', NULL, '999929', NULL, NULL, NULL, NULL, NULL, 'ruian_cm', '441', NULL, NULL);
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`, `area_size`, `shared_area`, `charge_area`, `build_area`, `rent_area`, `namespace_community_type`, `namespace_community_token`, `community_number`, `free_area`) VALUES ('240111044332063581', '43877d13-8995-46ed-9bcb-4f8a307c41f2', '21977', '南京市', '21978', '玄武', 'Inno Work', NULL, '南京市玄武区珠江路未来城', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '1', NULL, '2', '2018-08-27 16:18:54', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '196430', '196431', NULL, '999929', NULL, NULL, NULL, NULL, NULL, 'ruian_cm', '442', NULL, NULL);
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`, `area_size`, `shared_area`, `charge_area`, `build_area`, `rent_area`, `namespace_community_type`, `namespace_community_token`, `community_number`, `free_area`) VALUES ('240111044332063586', '6b61ab1c-efeb-4624-b160-543f2bb6363f', '21977', '南京市', '21978', '玄武', 'INNO创智A栋 Office', NULL, '南京市玄武区珠江路未来城', NULL, 'INNO创智', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '1', NULL, '2', '2018-08-27 16:29:51', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '196430', '196431', NULL, '999929', NULL, NULL, NULL, NULL, NULL, 'ruian_cm', '443', NULL, NULL);
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`, `area_size`, `shared_area`, `charge_area`, `build_area`, `rent_area`, `namespace_community_type`, `namespace_community_token`, `community_number`, `free_area`) VALUES ('240111044332063583', '239e0eac-c818-41d7-887d-651a037acc09', '21977', '南京市', '21978', '玄武', 'INNO创智B栋 Office', NULL, '南京市玄武区珠江路未来城', NULL, 'INNO创智', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '1', NULL, '2', '2018-08-27 16:36:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '196430', '196431', NULL, '999929', NULL, NULL, NULL, NULL, NULL, 'ruian_cm', '444', NULL, NULL);
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`, `area_size`, `shared_area`, `charge_area`, `build_area`, `rent_area`, `namespace_community_type`, `namespace_community_token`, `community_number`, `free_area`) VALUES ('240111044332063584', '88d29ccf-8cf4-4ff9-8dfc-896e8159af99', '21977', '南京市', '21978', '玄武', 'INNO创智A栋 Retail', NULL, '南京市玄武区珠江路未来城', NULL, 'INNO创智', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '1', NULL, '2', '2018-08-27 16:38:27', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '196430', '196431', NULL, '999929', NULL, NULL, NULL, NULL, NULL, 'ruian_cm', '445', NULL, NULL);
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`, `area_size`, `shared_area`, `charge_area`, `build_area`, `rent_area`, `namespace_community_type`, `namespace_community_token`, `community_number`, `free_area`) VALUES ('240111044332063585', '59e51ce5-5f1d-4150-9bd6-78741c6afd83', '21977', '南京市', '21978', '玄武', 'INNO创智B栋 Retail', NULL, '南京市玄武区珠江路未来城', NULL, 'INNO创智', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '1', NULL, '2', '2018-08-27 16:43:52', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '196430', '196431', NULL, '999929', NULL, NULL, NULL, NULL, NULL, 'ruian_cm', '446', NULL, NULL);

-- 园区和域空间关联
set @eh_namespace_resources_id = IFNULL((select MAX(id) from eh_namespace_resources),1);
INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`, `default_order`) VALUES (@eh_namespace_resources_id := @eh_namespace_resources_id + 1, '999929', 'COMMUNITY', '240111044332063579', '2018-08-27 14:20:21', NULL);
INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`, `default_order`) VALUES (@eh_namespace_resources_id := @eh_namespace_resources_id + 1, '999929', 'COMMUNITY', '240111044332063580', '2018-08-27 16:03:02', NULL);
INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`, `default_order`) VALUES (@eh_namespace_resources_id := @eh_namespace_resources_id + 1, '999929', 'COMMUNITY', '240111044332063581', '2018-08-27 16:18:54', NULL);
INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`, `default_order`) VALUES (@eh_namespace_resources_id := @eh_namespace_resources_id + 1, '999929', 'COMMUNITY', '240111044332063586', '2018-08-27 16:29:52', NULL);
INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`, `default_order`) VALUES (@eh_namespace_resources_id := @eh_namespace_resources_id + 1, '999929', 'COMMUNITY', '240111044332063583', '2018-08-27 16:36:00', NULL);
INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`, `default_order`) VALUES (@eh_namespace_resources_id := @eh_namespace_resources_id + 1, '999929', 'COMMUNITY', '240111044332063584', '2018-08-27 16:38:27', NULL);
INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`, `default_order`) VALUES (@eh_namespace_resources_id := @eh_namespace_resources_id + 1, '999929', 'COMMUNITY', '240111044332063585', '2018-08-27 16:43:52', NULL);

-- 创建经纬度数据
set @eh_community_geopoints_id = IFNULL((select MAX(id) from eh_community_geopoints),1);
INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) VALUES (@eh_community_geopoints_id := @eh_community_geopoints_id + 1, '240111044332063579', NULL, '118.804035', '32.054084', 'wtsqr7q2wdm5');
INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) VALUES (@eh_community_geopoints_id := @eh_community_geopoints_id + 1, '240111044332063580', NULL, '118.804035', '32.054084', 'wtsqr7q2wdm5');
INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) VALUES (@eh_community_geopoints_id := @eh_community_geopoints_id + 1, '240111044332063581', NULL, '118.804035', '32.054084', 'wtsqr7q2wdm5');
INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) VALUES (@eh_community_geopoints_id := @eh_community_geopoints_id + 1, '240111044332063586', NULL, '118.804035', '32.054084', 'wtsqr7q2wdm5');
INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) VALUES (@eh_community_geopoints_id := @eh_community_geopoints_id + 1, '240111044332063583', NULL, '118.804035', '32.054084', 'wtsqr7q2wdm5');
INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) VALUES (@eh_community_geopoints_id := @eh_community_geopoints_id + 1, '240111044332063584', NULL, '118.804035', '32.054084', 'wtsqr7q2wdm5');
INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) VALUES (@eh_community_geopoints_id := @eh_community_geopoints_id + 1, '240111044332063585', NULL, '118.804035', '32.054084', 'wtsqr7q2wdm5');

-- 添加企业可见园区,管理公司可以看到添加的园区
set @eh_organization_communities_id = IFNULL((select MAX(id) from eh_organization_communities),1);
INSERT INTO `eh_organization_communities` (`id`, `organization_id`, `community_id`) VALUES (@eh_organization_communities_id := @eh_organization_communities_id + 1, '1051547', '240111044332063579');
INSERT INTO `eh_organization_communities` (`id`, `organization_id`, `community_id`) VALUES (@eh_organization_communities_id := @eh_organization_communities_id + 1, '1051547', '240111044332063580');
INSERT INTO `eh_organization_communities` (`id`, `organization_id`, `community_id`) VALUES (@eh_organization_communities_id := @eh_organization_communities_id + 1, '1051547', '240111044332063581');
INSERT INTO `eh_organization_communities` (`id`, `organization_id`, `community_id`) VALUES (@eh_organization_communities_id := @eh_organization_communities_id + 1, '1051547', '240111044332063586');
INSERT INTO `eh_organization_communities` (`id`, `organization_id`, `community_id`) VALUES (@eh_organization_communities_id := @eh_organization_communities_id + 1, '1051547', '240111044332063583');
INSERT INTO `eh_organization_communities` (`id`, `organization_id`, `community_id`) VALUES (@eh_organization_communities_id := @eh_organization_communities_id + 1, '1051547', '240111044332063584');
INSERT INTO `eh_organization_communities` (`id`, `organization_id`, `community_id`) VALUES (@eh_organization_communities_id := @eh_organization_communities_id + 1, '1051547', '240111044332063585');

-- AUTHOR: 杨崇鑫
-- REMARK: 配置客户V4.1瑞安CM对接的访问地址
SET @id = ifnull((SELECT MAX(id) FROM `eh_configurations`),0);
INSERT INTO `eh_configurations`(`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`)
	VALUES (@id := @id + 1, 'RuiAnCM.sync.url', 'http://10.50.12.39/cm/WebService/OfficeApp-CM/OfficeApp_CMService.asmx', '瑞安新天地对接的第三方地址', 0, NULL, 1);
INSERT INTO `eh_configurations`(`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`)
	VALUES (@id:=@id+1, 'contractService', '999929', NULL, 999929, NULL, 1);

-- AUTHOR: 杨崇鑫
-- REMARK: 初始化瑞安CM对接的默认账单组，由于该账单组是默认账单组，所以不允许删除
set @id = 1000000;
INSERT INTO `eh_payment_bill_groups`(`id`, `namespace_id`, `owner_id`, `owner_type`, `name`, `balance_date_type`, `bills_day`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`, `due_day`, `due_day_type`, `brother_group_id`, `bills_day_type`, `category_id`, `biz_payee_type`, `biz_payee_id`, `is_default`)
	select @id:=@id+1, 999929, id, 'community', '缴费', 2, 5, 67663, UTC_TIMESTAMP(), NULL, UTC_TIMESTAMP(), 1, 5, 1, NULL, 4, 3, NULL, NULL, 1
		from eh_communities;
-- REMARK:瑞安CM对接 账单区分数据来源
SET @eh_locale_strings_id = (SELECT MAX(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
	VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'asset.source', '10005', 'zh_CN', '瑞安CM产生');
-- REMARK: 瑞安CM对接 APP只支持查费，隐藏掉缴费
SET @id = ifnull((SELECT MAX(id) FROM `eh_payment_app_views`),0);
INSERT INTO `eh_payment_app_views`(`id`, `namespace_id`, `community_id`, `has_view`, `view_item`, `remark1_type`, `remark1_identifier`, `remark2_type`, `remark2_identifier`, `remark3_type`, `remark3_identifier`)
VALUES (@id := @id + 1, 999929, NULL, 0, 'PAY', NULL, NULL, NULL, NULL, NULL, NULL);

-- AUTHOR: 黄明波
-- REMARK: 填写瑞安打印机名称
update eh_siyin_print_printers set printer_name = 'Sys_NJ_INNO_2F02' where reader_name = 'TC101152723470';
update eh_siyin_print_printers set printer_name = 'Sys_NJ_INNO_3F01' where reader_name = 'TC101152723540';
update eh_siyin_print_printers set printer_name = 'Sys_NJ_INNO_2F01' where reader_name = 'TC101152723478';

-- AUTHOR: 刘一麟
-- REMARK: 访客二维码短信模板
UPDATE eh_locale_templates set `text` = '${username}已授权给你${doorname}门禁二维码，请点击以下链接使用：${link}/aclink/v?id=${id}（24小时有效）' where `code` = 8 and `scope` in ('sms.default','sms.default.yzx') and `description` like '%门禁%';

-- AUTHOR: 缪洲
-- REMARK: 停车缴费收款账号迁移
update eh_parking_business_payee_accounts ac set ac.merchant_id = ac.payee_id ;

-- --------------------- SECTION END ruianxintiandi ------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: wanzhihui
-- DESCRIPTION: 此SECTION只在万智汇-999953执行的脚本
-- --------------------- SECTION END wanzhihui ------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等

-- AUTHOR:xq.tian 20181016
-- REMARK:网关及注册中心部署.
-- DESCRIPTION：http://s.a.com/docs/faq/baseline-21539677844

-- AUTHOR:梁燕龙 20181016
-- REMARK:统一用户上线操作.
-- DESCRIPTION：http://s.a.com/docs/faq/baseline-21539678631

-- AUTHOR:杨崇鑫 20181018
-- REMARK:解决缺陷 #39352: 【全量标准版】【物业缴费】新增收费标准前端提示成功，但实际未新增成功，无相关数据，后台提示“应用开小差”
-- REMARK：备份eh_payment_variables表
-- select * from eh_payment_variables;

-- AUTHOR:杨崇鑫 20181027
-- REMARK:解决缺陷 #39571:
-- 第一步请执行在es上执行db/search/energy_task.sh
-- 第二步执行同步接口/energy/syncEnergyTaskIndex

-- AUTHOR:梁家声 20181030
-- REMARK: 对照营销的core_key，在eh_apps中插入营销core_key对

-- AUTHOR:黄鹏宇 20181103
-- REMARK:解决缺陷 #38583:
-- 第一步请执行在es上执行db/search/pmowner.sh
-- 第二步执行同步接口/pm/syncOwnerIndex



-- --------------------- SECTION END OPERATION------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境

-- AUTHOR:唐岑2018年10月23日14:29:41
-- REMARK:添加资产报表定时配置
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('schedule.property.task.time', '0 30 2 * * ?', '资产报表定时任务', '0', NULL, '1');

-- AUTHOR: 杨崇鑫 20181023
-- REMARK: 新增“资产报表中心”模块和菜单
set @module_id=230000; -- 模块Id（运维分配的）
set @data_type='property-statistic';-- 前端发给你的页面跳转链接
-- 新增模块 eh_service_modules
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `app_type`, `client_handler_type`, `system_app_flag`, `icon_uri`, `access_control_type`, `menu_auth_flag`, `category`)
VALUES (230000, '资产报表中心', 130000, '/200/130000/230000', 1, 3, 2, 10, UTC_TIMESTAMP(), NULL, NULL, UTC_TIMESTAMP(), 0, 0, '0', 0, 'unlimit_control', 1, 0, NULL, NULL, 1, 1, 'module');
-- 新增模块菜单 eh_web_menus
-- 左邻后台:zuolin、园区：park
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`)
	VALUES (79850000, '资产报表中心', 17000000, NULL, @data_type, 1, 2, '/27000000/17000000/79850000', 'zuolin', 10, 230000, 3, 'system', 'module', NULL, 1);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`)
	VALUES (79860000, '资产报表中心', 49000000, NULL, @data_type, 1, 2, '/40000040/49000000/79860000', 'park', 10, 230000, 3, 'system', 'module', 2, 1);













-- AUTHOR:黄鹏宇 2018年10月22日
-- REMARK:将计划任务中的拜访时间改为计划时间
update eh_var_fields set display_name = '计划时间' where display_name='拜访时间' and group_id = 20;
update eh_var_field_scopes set field_display_name = '计划时间' where field_display_name='拜访时间' and group_id = 20;


-- AUTHOR:黄鹏宇 2018年10月22日
-- REMARK:更改module表中的client_handler_type类型为外部链接
update eh_service_modules set client_handler_type = 2 where id = 25000;
update eh_service_modules set client_handler_type = 2 where id = 150020;







UPDATE eh_service_modules SET client_handler_type = 1 WHERE id in (90100,  180000);

-- AUTHOR:严军 201801030
-- REMARK: issue-null 设置路由相关参数
UPDATE eh_service_modules SET client_handler_type = 2 WHERE id = 40500;
UPDATE eh_service_modules SET `host` = 'workflow' WHERE id = 13000;
UPDATE eh_service_modules SET `host` = 'community-map' WHERE id = 40070;

-- AUTHOR:严军 201801103
-- REMARK: issue-null 增加内部链接，并刷新数据
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`, `app_type`, `client_handler_type`, `system_app_flag`, `icon_uri`, `host`, `enable_enterprise_pay_flag`) VALUES ('90200', '内部链接', '90000', '/400/90000/90200', '1', '3', '2', '15', NULL, NULL, '13', NULL, '0', '0', '0', '1', '', '1', '1', 'module', '1', '2', NULL, NULL, NULL, NULL);
UPDATE eh_service_module_apps SET module_id = 90200, action_type =13 WHERE module_id = 90100 AND instance_config like '%zuolin.com%';

-- AUTHOR: 吴寒
-- REMARK: 打卡考勤V8.2 - 支持人脸识别关联考勤；支持自动打卡
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES('punch.create','1','zh_CN','打卡发送消息','${createType}: ${punchTime}','0');

INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('punch.create.type','1','zh_CN','自动打卡');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('punch.create.type','2','zh_CN','人脸识别打卡');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('punch.create.type','3','zh_CN','门禁打卡');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('punch.create.type','4','zh_CN','其他第三方打卡');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('oa.unit','1','zh_CN','天');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('oa.unit','2','zh_CN','次');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('remind.msg','1','zh_CN','日程提醒');

INSERT INTO eh_locale_strings(scope,CODE,locale,TEXT) VALUE( 'PunchStatusStatisticsItemName', 11, 'zh_CN', '外出');

-- AUTHOR: 吴寒
-- REMARK: 日程提醒给共享人发消息
SET @tem_id = (SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@tem_id := @tem_id + 1),'remind.msg','6','zh_CN','创建给被共享人发消息','${trackContractName}共享了日程“${planDescription}” ','0');

-- AUTHOR: 马世亨
-- REMARK: 邀请者查看邀请详情路由修改
UPDATE `eh_configurations` SET `value`='%s/visitor-appointment/build/index.html?detailId=%s&ns=%s#/appointment-detail#sign_suffix' WHERE `name`='visitorsys.inviter.route';

-- AUTHER：李清岩 20181019
-- REMARK: 新增公共门禁权限子模块
INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( 41011, '门禁授权', '40000', '/200/40000/41010/41011', '1', '4', '2', 0, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( 41012, '门禁日志', '40000', '/200/40000/41010/41012', '1', '4', '2', 0, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( 41013, '数据统计', '40000', '/200/40000/41010/41013', '1', '4', '2', 0, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( 41014, '移动端管理', '40000', '/200/40000/41010/41014', '1', '4', '2', 0, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );
-- AUTHER：李清岩 20181019
-- REMARK: 新增企业门禁权限子模块
INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( 41021, '门禁授权', '310000', '/100/310000/41020/4102', '1', '4', '2', 0, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( 41022, '门禁日志', '310000', '/100/310000/41020/41022', '1', '4', '2', 0, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( 41023, '数据统计', '310000', '/100/310000/41020/41023', '1', '4', '2', 0, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( 41024, '移动端管理', '310000', '/100/310000/41020/41024', '1', '4', '2', 0, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );
-- AUTHER：李清岩 20181019
-- 新增门禁权限项
INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4101041010, 0, '公共门禁 全部权限', '公共门禁 全部权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4101041011, 0, '公共门禁 门禁授权', '公共门禁 门禁授权权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4101041012, 0, '公共门禁 门禁日志', '公共门禁 门禁日志权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4101041013, 0, '公共门禁 数据统计', '公共门禁 数据统计权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4101041014, 0, '公共门禁 移动端管理', '公共门禁 移动端管理权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4102041020, 0, '企业门禁 全部权限', '企业门禁 全部权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4102041021, 0, '企业门禁 门禁授权', '企业门禁 门禁授权权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4102041022, 0, '企业门禁 门禁日志', '企业门禁 门禁日志权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4102041023, 0, '企业门禁 数据统计', '企业门禁 数据统计权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4102041024, 0, '企业门禁 移动端管理', '企业门禁 移动端管理权限', NULL );
-- AUTHER：李清岩 20181019
-- 模块权限关联
SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41010', '0', 4101041010, '全部权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41011', '0', 4101041011, '门禁授权权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41012', '0', 4101041012, '门禁日志权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41013', '0', 4101041013, '数据统计权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41014', '0', 4101041014, '移动端管理权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41020', '0', 4102041020, '全部权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41021', '0', 4102041021, '门禁授权权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41022', '0', 4102041022, '门禁日志权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41023', '0', 4102041023, '数据统计权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41024', '0', 4102041024, '移动端管理权限', '0', NOW());
-- AUTHER：李清岩 20181019
-- 新增两个门禁模块：公共门禁beta，企业门禁beta
INSERT INTO `eh_web_menus` ( `id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type` ) VALUES ( 16030610, '公共门禁beta', 16030000, NULL, 'public-access', 1, 2, '/16000000/16030000/16030610', 'zuolin', 40, 41110, 3, 'system', 'module', NULL );

INSERT INTO `eh_web_menus` ( `id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type` ) VALUES ( 16030620, '公共门禁beta', 45000000, NULL, 'public-access', 1, 2, '/40000040/45000000/16030620', 'park', 30, 41110, 3, 'system', 'module', NULL );

INSERT INTO `eh_web_menus` ( `id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type` ) VALUES ( 48140010, '企业门禁beta', 16040000, NULL, 'company-access', 1, 2, '/23000000/16040000/48140010', 'zuolin', 60, 41120, 3, 'system', 'module', NULL );

INSERT INTO `eh_web_menus` ( `id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type` ) VALUES ( 48140020, '企业门禁beta', 48000000, NULL, 'company-access', 1, 2, '/40000010/48000000/48140020', 'park', 80, 41120, 3, 'system', 'module', NULL );
-- AUTHER：李清岩 20181019
-- 新增公共门禁beta子模块
INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( 41110, '公共门禁beta', '40000', '/200/40000/41110', '1', '3', '2', 10, NOW(), '{\"isSupportQR\":1,\"isSupportSmart\":0}', '78', NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'module' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( '41111', '门禁授权', '40000', '/200/40000/41110/41111', '1', '4', '2', '0', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( '41112', '门禁日志', '40000', '/200/40000/41110/41112', '1', '4', '2', '0', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( '41113', '数据统计', '40000', '/200/40000/41110/41113', '1', '4', '2', '0', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( '41114', '移动端管理', '40000', '/200/40000/41110/41114', '1', '4', '2', '0', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );
-- AUTHER：李清岩 20181019
-- 新增企业门禁beta子模块
INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( '41120', '企业门禁beta', '310000', '/100/310000/41120', '1', '3', '2', '100', NOW(), '{\"isSupportQR\":1,\"isSupportSmart\":0}', '79', NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'module' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( '41121', '门禁授权', '310000', '/100/310000/41120/41121', '1', '4', '2', '0', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( '41122', '门禁日志', '310000', '/100/310000/41120/41122', '1', '4', '2', '0', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( '41123', '数据统计', '310000', '/100/310000/41120/41123', '1', '4', '2', '0', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( '41124', '移动端管理', '310000', '/100/310000/41120/41124', '1', '4', '2', '0', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );
-- AUTHER：李清岩 20181019
-- 新增公共门禁beta权限
INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4111041110, 0, '公共门禁beta 全部权限', '公共门禁beta 全部权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4111041111, 0, '公共门禁beta 门禁授权', '公共门禁beta 门禁授权权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4111041112, 0, '公共门禁beta 门禁日志', '公共门禁beta 门禁日志权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4111041113, 0, '公共门禁beta 数据统计', '公共门禁beta 数据统计权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4111041114, 0, '公共门禁beta 移动端管理', '公共门禁beta 移动端管理权限', NULL );
-- AUTHER：李清岩 20181019
-- 新增企业门禁beta权限
INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4112041120, 0, '企业门禁beta 全部权限', '企业门禁beta 全部权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4112041121, 0, '企业门禁beta 门禁授权', '企业门禁beta 门禁授权权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4112041122, 0, '企业门禁beta 门禁日志', '企业门禁beta 门禁日志权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4112041123, 0, '企业门禁beta 数据统计', '企业门禁beta 数据统计权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4112041124, 0, '企业门禁beta 移动端管理', '企业门禁beta 移动端管理权限', NULL );
-- AUTHER：李清岩 20181019
-- 模块权限关联
SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41110', '0', 4111041110, '全部权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41111', '0', 4111041111, '门禁授权权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41112', '0', 4111041112, '门禁日志权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41113', '0', 4111041113, '数据统计权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41114', '0', 4111041114, '移动端管理权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41120', '0', 4112041120, '全部权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41121', '0', 4112041121, '门禁授权权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41122', '0', 4112041122, '门禁日志权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41123', '0', 4112041123, '数据统计权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41124', '0', 4112041124, '移动端管理权限', '0', NOW());
-- AUTHER：李清岩 20181019
-- 新增左邻后台门禁管理模块
INSERT INTO `eh_web_menus` ( `id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type` ) VALUES ( 79300000, '智能硬件', 15000000, NULL, NULL, 1, 2, '/15000000/79300000', 'zuolin', 60, NULL, 2, 'system', 'classify', NULL );

INSERT INTO `eh_web_menus` ( `id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type` ) VALUES ( 79410000, '门禁管理', 79300000, NULL, 'access-management', 1, 2, '/15000000/79300000/79410000', 'zuolin', 1, 70400, 3, 'system', 'module', NULL );
-- AUTHER：李清岩 20181019
-- 更新门禁设备所属城市id
UPDATE eh_door_access dc LEFT JOIN eh_communities c ON c.id = dc.owner_id SET dc.city_id = c.city_id WHERE dc.owner_type = 0;

UPDATE eh_door_access dc LEFT JOIN ( SELECT DISTINCT r.member_id organization_id, c.city_id city_id FROM eh_organization_community_requests r LEFT JOIN eh_communities c ON c.id = r.community_id ) t ON t.organization_id = dc.owner_id SET dc.city_id = t.city_id WHERE dc.owner_type = 1;
-- AUTHER：李清岩 20181019
-- 更新门禁设备所属域空间id
UPDATE eh_door_access dc LEFT JOIN eh_communities c ON c.id = dc.owner_id SET dc.namespace_id = c.namespace_id WHERE dc.owner_type = 0;

UPDATE eh_door_access dc LEFT JOIN eh_organizations t ON dc.owner_id = t.id SET dc.namespace_id = t.namespace_id WHERE dc.owner_type = 1;

-- AUTHOR: 张智伟 20180912
-- REMARK: issue-37602 审批单支持编辑
SET @flow_predefined_params_id = IFNULL((SELECT MAX(id) FROM `eh_flow_predefined_params`), 1);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`)
  VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1),0, 0, '', 52000, 'any-module', 'flow_button', '终止并重新提交', '终止并重新提交', '{"nodeType":"APPROVAL_CANCEL_AND_RESUMBIT"}', 2);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`)
  VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1),0, 0, '', 52000, 'any-module', 'flow_button', '复制审批单', '复制审批单', '{"nodeType":"APPROVAL_COPY_FORM_VALUES"}', 2);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`)
  VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1),0, 0, '', 52000, 'any-module', 'flow_button', '显示撤销规则', '显示撤销规则', '{"nodeType":"APPROVAL_SHOW_CANCEL_INFO"}', 2);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`)
  VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1),0, 0, '', 52000, 'any-module', 'flow_button', '编辑当前节点表单', '编辑当前节点表单', '{"nodeType":"APPROVAL_EDIT_CURRENT_FORM"}', 2);

-- AUTHOR: 张智伟 20180912
-- REMARK: issue-37602 审批单支持编辑
SET @string_id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval.error', '30002', 'zh_CN', '关联表单需要填写才能进入下一步');

-- AUTHOR: 张智伟 20181031
-- REMARK: issue-40880 离职申请执行下一步，提示“No rights to remove the admin.”无法执行成功。
SET @string_id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id +1, 'archives', '200002', 'zh_CN', '无法删除管理员，请先解除其管理权限再重试');


-- AUTHOR: 黄良铭
-- REMARK: 修改积分系统状态
UPDATE  eh_point_systems SET STATUS='2' ,point_exchange_flag='1' WHERE id = 1;

-- AUTHOR: 马世亨 20181031
-- REMARK: 访客1.3合并访客与访客管理后清除app
-- REMARK: 访客1.3园区访客地址修改
delete from eh_service_module_apps where module_id in (42100,52200);
update eh_service_modules set instance_config = '{"url":"${home.url}/visitor-appointment/build/index.html?ns=%s&appId=%s&ownerType=community&sceneType=1#/home#sign_suffix"}' where id = 41800;

-- AUTHOR: 黄明波
-- REMARK: 修改默认新闻为 NewsFlash
update eh_service_modules set  instance_config = replace(instance_config, '}', ',"widget":"NewsFlash"}') , action_type = 55, client_handler_type =  0  where id = 10800 and instance_config not like '%"widget"%';

update eh_service_module_apps set  instance_config = replace(instance_config, '}', ',"widget":"NewsFlash"}') , action_type = 55  where module_id = 10800 and instance_config not like '%"widget"%' ;

update eh_service_modules set client_handler_type = 2 where id = 10500;
update eh_service_modules set client_handler_type = 2 where id = 40500;
update eh_service_modules set client_handler_type = 2 where id = 10800;



-- AUTHOR: 黄鹏宇 2018年11月1日
-- REMARK: 更改楼宇房源
update eh_var_fields set display_name = '楼宇' where id = 10965;
update eh_var_fields set display_name = '房源' where id = 10966;
update eh_var_field_scopes set field_display_name = '楼宇' where field_id = 10965 and field_display_name = '楼栋';
update eh_var_field_scopes set field_display_name = '房源' where field_id = 10966 and field_display_name = '门牌名称';

-- AUTHOR: 马世亨
-- REMARK: 物业报修3.8 对接国贸报错信息 20181022
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask', '10026', 'zh_CN', '用户不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask', '10027', 'zh_CN', '初始化失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask', '10028', 'zh_CN', '获取数据失败');

-- AUTHOR: 缪洲 2018年11月1日
-- REMARK: 把资源预约，停车缴费，云打印加入企业支付授权
UPDATE eh_service_module_apps SET enable_enterprise_pay_flag = 1 WHERE module_id in (40800,41400,40400);

-- AUTHOR: 缪洲
-- REMARK: 增加用户自定义上传资料与默认车牌的默认值
UPDATE eh_parking_lots SET default_data = 'identity,driver,driving';
UPDATE eh_parking_lots SET default_plate = '粤,B';
-- AUTHOR: 马世亨
-- REMARK: 物业报修3.8 支持多应用服务类型 20181025
INSERT INTO `eh_pm_task_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`, `owner_type`, `owner_id`) VALUES ('6', '0', '0', '物业报修', '物业报修', '0', '2', '2015-09-28 06:09:03', NULL, NULL, NULL, '0', NULL, '0');
INSERT INTO `eh_pm_task_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`, `owner_type`, `owner_id`) VALUES ('9', '0', '0', '投诉建议', '投诉建议', '0', '2', '2017-12-04 13:09:45', NULL, NULL, NULL, '0', NULL, '0');

-- AUTHOR: 黄鹏宇 2018年11月1日
-- REMARK: 更改楼宇房源
update eh_general_forms set template_text = replace(template_text,'楼栋门牌','楼宇房源') where module_id = 25000;
update eh_general_form_templates set template_text = '[{
	"dynamicFlag": 0,
	"fieldDesc": "客户名称",
	"fieldDisplayName": "客户名称",
	"fieldExtra": "{}",
	"fieldName": "客户名称",
	"fieldType": "SINGLE_LINE_TEXT",
	"renderType": "DEFAULT",
	"remark": "系统自动获取客户管理中该项目下所有客户信息供用户选择；",
	"disabled": 1,
	"requiredFlag": 1,
	"validatorType": "TEXT_LIMIT",
	"visibleType": "EDITABLE",
	"filterFlag": 1
},
{
	"dynamicFlag": 0,
	"fieldDesc": "楼宇房源",
	"fieldDisplayName": "楼宇房源",
	"fieldExtra": "{}",
	"fieldName": "楼宇房源",
	"fieldType": "SINGLE_LINE_TEXT",
	"remark": "系统自动获取资产管理中该项目下所有待租门牌供用户选择；",
	"disabled": 1,
	"renderType": "DEFAULT",
	"requiredFlag": 1,
	"validatorType": "TEXT_LIMIT",
	"visibleType": "EDITABLE",
	"filterFlag": 1
},
{
	"dynamicFlag": 0,
	"fieldDesc": "审批状态",
	"fieldDisplayName": "审批状态",
	"fieldExtra": "{}",
	"fieldName": "审批状态",
	"fieldType": "SINGLE_LINE_TEXT",
	"renderType": "DEFAULT",
	"requiredFlag": 1,
	"remark": "系统自动根据不同的触发不同的操作；",
	"disabled": 1,
	"validatorType": "TEXT_LIMIT",
	"visibleType": "EDITABLE",
	"filterFlag": 1
}]' where module_id = 25000;

-- AUTHOR: 马世亨
-- REMARK: 物业报修3.8 国贸对接项目标识 20181031
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('pmtask.handler-999948', 'archibus', 'archibus handler', '0', NULL, '1');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('pmtask.archibus.areaid-999948', '000000201807140AU8ME', '物业报修国贸区域ID', '0', NULL, NULL);

-- AUTHOR: 缪洲
-- REMARK: 科兴科学园发票类型字段
UPDATE eh_parking_lots SET config_json = '{"tempfeeFlag":0,"rateFlag":0,"lockCarFlag":0,"searchCarFlag":1,"currentInfoType":2,"contact":"18718523489","invoiceFlag":1,"businessLicenseFlag":0,"vipParkingFlag":0,"monthRechargeFlag":1,"identityCardFlag":1,"monthCardFlag":1,"noticeFlag":0,"flowMode":3,"invoiceTypeFlag":1}' WHERE id = 10006;


-- AUTHOR: 吴寒z
-- REMARK: 会议室预定发邮件的内容修改
UPDATE  eh_locale_templates SET TEXT = '主题：${meetingSubject}|时间：${meetingBeginTime}|地点：${meetingRoomName}|发起人：${meetingSponsorName}|参会人：${meetingUserList}||${content}' WHERE  CODE =1000005 AND scope = 'meetingMessage';


-- AUTHOR: 梁燕龙 20181026
-- REMARK: 广告管理修改为多应用
UPDATE eh_service_modules SET multiple_flag = 1 WHERE id = 10400;
-- AUTHOR: 梁燕龙 20181026
-- REMARK: 广告管理修改为多应用
-- 刷app值
UPDATE eh_service_module_apps SET instance_config = '{"categoryId":0}' WHERE module_id = 10400;
-- AUTHOR: 梁燕龙 20181026
-- REMARK: 广告管理修改为多应用
-- 刷广告数据入口
UPDATE eh_banners SET category_id = 0;

-- AUTHOR: 梁燕龙
-- REMARK: issue-36940 用户认证，邮箱认证提示文案
SET @max_id = IFNULL((SELECT MAX(`id`) FROM `eh_locale_strings`),1);
INSERT INTO eh_locale_strings (id, scope, code, locale, text)
VALUES (@max_id:=@max_id+1,'organization', 900039, 'zh_CN', '该邮箱已被认证');

-- AUTHOR: 唐岑
-- REMARK: 更改module表中的client_handler_type类型为内部应用
update eh_service_modules set client_handler_type = 2 where id = 150010;

-- AUTHOR: 唐岑
-- REMARK: 更新房源招商的字段模板
UPDATE eh_general_form_templates SET template_text='[{\r\n	\"dynamicFlag\": 0,\r\n	\"fieldDesc\": \"用户姓名\",\r\n	\"fieldDisplayName\": \"用户姓名\",\r\n	\"fieldExtra\": \"{}\",\r\n	\"fieldName\": \"USER_NAME\",\r\n	\"fieldType\": \"SINGLE_LINE_TEXT\",\r\n	\"remark\": \"用户的姓名；\",\r\n	\"disabled\": 1,\r\n	\"renderType\": \"DEFAULT\",\r\n	\"requiredFlag\": 1,\r\n	\"validatorType\": \"TEXT_LIMIT\",\r\n	\"visibleType\": \"EDITABLE\",\r\n	\"filterFlag\": 1\r\n},\r\n{\r\n	\"dynamicFlag\": 0,\r\n	\"fieldDesc\": \"手机号码\",\r\n	\"fieldDisplayName\": \"手机号码\",\r\n	\"fieldExtra\": \"{}\",\r\n	\"fieldName\": \"USER_PHONE\",\r\n	\"fieldType\": \"NUMBER_TEXT\",\r\n	\"remark\": \"用户的手机号码；\",\r\n	\"disabled\": 1,\r\n	\"renderType\": \"DEFAULT\",\r\n	\"requiredFlag\": 1,\r\n	\"validatorType\": \"TEXT_LIMIT\",\r\n	\"visibleType\": \"EDITABLE\",\r\n	\"filterFlag\": 1\r\n},\r\n{\r\n	\"dynamicFlag\": 0,\r\n	\"fieldDesc\": \"承租方\",\r\n	\"fieldDisplayName\": \"承租方\",\r\n	\"fieldExtra\": \"{}\",\r\n	\"fieldName\": \"ENTERPRISE_NAME\",\r\n	\"fieldType\": \"SINGLE_LINE_TEXT\",\r\n	\"remark\": \"允许用户手动输入；\",\r\n	\"disabled\": 1,\r\n	\"renderType\": \"DEFAULT\",\r\n	\"requiredFlag\": 1,\r\n	\"validatorType\": \"TEXT_LIMIT\",\r\n	\"visibleType\": \"EDITABLE\",\r\n	\"filterFlag\": 1\r\n},\r\n{\r\n	\"dynamicFlag\": 0,\r\n	\"fieldDesc\": \"意向房源\",\r\n	\"fieldDisplayName\": \"意向房源\",\r\n	\"fieldExtra\": \"{}\",\r\n	\"fieldName\": \"APARTMENT\",\r\n	\"fieldType\": \"SINGLE_LINE_TEXT\",\r\n	\"remark\": \"允许用户手动选择；\",\r\n	\"disabled\": 1,\r\n	\"renderType\": \"DEFAULT\",\r\n	\"requiredFlag\": 1,\r\n	\"validatorType\": \"TEXT_LIMIT\",\r\n	\"visibleType\": \"EDITABLE\",\r\n	\"filterFlag\": 1\r\n}]' WHERE module_id=150010;

-- AUTHOR: 唐岑
-- REMARK: 楼宇导入出错提示
SET @max_id = IFNULL((SELECT MAX(`id`) FROM `eh_locale_strings`),1);
INSERT INTO eh_locale_strings (id, scope, code, locale, text)
VALUES (@max_id:=@max_id+1,'community', 10213, 'zh_CN', '楼栋名称不能超过20个汉字');
INSERT INTO eh_locale_strings (id, scope, code, locale, text)
VALUES (@max_id:=@max_id+1,'community', 10214, 'zh_CN', '楼栋名称不能重复');

-- AUTHOR: 丁建民 20181031
-- REMARK: 缴费对接门禁。企业或者个人欠费将禁用该企业或个人门禁 定时器执行时间
SET @id = (SELECT MAX(id) from eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ((@id:=@id+1), 'asset.dooraccess.cronexpression', '0 0 3,23 * * ?', '欠费禁用门禁的定时任务执行时间', '0', NULL, '1');


-- AUTHOR: 马世亨
-- REMARK: 物业报修V3.8 多应用 任务迁移
-- REMARK: 执行前备份eh_pm_tasks表！！！
update eh_pm_tasks t1,eh_categories t2 set t1.app_id = t2.parent_id where t2.id = t1.task_category_id;
update eh_pm_tasks t1,eh_service_module_apps t2 set t1.app_id = t2.origin_id where t2.module_id = 20100 and t1.namespace_id = t2.namespace_id and t1.app_id = t2.custom_tag;
-- AUTHOR: 马世亨
-- REMARK: 物业报修V3.8 多应用 通用配置迁移
update eh_pm_task_configs t1,eh_service_module_apps t2 set t1.app_id = t2.origin_id where t2.module_id = 20100 and t1.namespace_id = t2.namespace_id and t1.task_category_id = t2.custom_tag;
-- AUTHOR: 马世亨
-- REMARK: 物业报修V3.8 多应用 物业报修类型迁移
insert into eh_pm_task_categories(id,parent_id,link_id,name,path,default_order,status,create_time,delete_time,logo_uri,description,namespace_id,owner_type,owner_id)
select t1.id,t1.parent_id,t1.link_id,t1.name,t1.path,t1.default_order,t1.status,t1.create_time,t1.delete_time,t1.logo_uri,t1.description,t1.namespace_id,t1.owner_type,t1.owner_id
from eh_categories t1 where t1.parent_id = 6;

insert into eh_pm_task_categories(id,parent_id,link_id,name,path,default_order,status,create_time,delete_time,logo_uri,description,namespace_id,owner_type,owner_id)
select t1.id,t1.parent_id,t1.link_id,t1.name,t1.path,t1.default_order,t1.status,t1.create_time,t1.delete_time,t1.logo_uri,t1.description,t1.namespace_id,t1.owner_type,t1.owner_id
from eh_categories t1 where t1.parent_id in (select t2.id from eh_categories t2 where t2.parent_id = 6);

insert into eh_pm_task_categories(id,parent_id,link_id,name,path,default_order,status,create_time,delete_time,logo_uri,description,namespace_id,owner_type,owner_id)
select t1.id,t1.parent_id,t1.link_id,t1.name,t1.path,t1.default_order,t1.status,t1.create_time,t1.delete_time,t1.logo_uri,t1.description,t1.namespace_id,t1.owner_type,t1.owner_id
from eh_categories t1 where t1.parent_id in (select t2.id from eh_categories t2 where t2.parent_id in (select t3.id from eh_categories t3 where t3.parent_id = 6));

update eh_pm_task_categories t1,eh_service_module_apps t2 set t1.app_id = t2.origin_id where t2.module_id = 20100 and t2.custom_tag = 6 and t1.namespace_id = t2.namespace_id;
-- 物业报修V3.8 多应用 物业报修类型迁移 END

-- AUTHOR: 马世亨
-- REMARK: 物业报修V3.8 多应用 投诉建议类型迁移
insert into eh_pm_task_categories(id,parent_id,link_id,name,path,default_order,status,create_time,delete_time,logo_uri,description,namespace_id,owner_type,owner_id)
select t1.id,t1.parent_id,t1.link_id,t1.name,t1.path,t1.default_order,t1.status,t1.create_time,t1.delete_time,t1.logo_uri,t1.description,t1.namespace_id,t1.owner_type,t1.owner_id
from eh_categories t1 where t1.parent_id = 9;

insert into eh_pm_task_categories(id,parent_id,link_id,name,path,default_order,status,create_time,delete_time,logo_uri,description,namespace_id,owner_type,owner_id)
select t1.id,t1.parent_id,t1.link_id,t1.name,t1.path,t1.default_order,t1.status,t1.create_time,t1.delete_time,t1.logo_uri,t1.description,t1.namespace_id,t1.owner_type,t1.owner_id
from eh_categories t1 where t1.parent_id in (select t2.id from eh_categories t2 where t2.parent_id = 9);

insert into eh_pm_task_categories(id,parent_id,link_id,name,path,default_order,status,create_time,delete_time,logo_uri,description,namespace_id,owner_type,owner_id)
select t1.id,t1.parent_id,t1.link_id,t1.name,t1.path,t1.default_order,t1.status,t1.create_time,t1.delete_time,t1.logo_uri,t1.description,t1.namespace_id,t1.owner_type,t1.owner_id
from eh_categories t1 where t1.parent_id in (select t2.id from eh_categories t2 where t2.parent_id in (select t3.id from eh_categories t3 where t3.parent_id = 9));

update eh_pm_task_categories t1,eh_service_module_apps t2 set t1.app_id = t2.origin_id where t2.module_id = 20100 and t2.custom_tag = 9 and t1.namespace_id = t2.namespace_id;
-- 物业报修V3.8 多应用 投诉建议类型迁移 END

-- AUTHOR: 马世亨
-- REMARK: 物业报修V3.8 多应用 父类型迁移
update eh_pm_task_categories set parent_id = 0 where parent_id in (6,9);
INSERT INTO `eh_pm_task_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `app_id`, `namespace_id`, `owner_type`, `owner_id`) VALUES ('0', '0', '0', '物业报修', '物业报修', '0', '2', '2015-09-28 06:09:03', NULL, NULL, NULL, '190', '0', NULL, '0');

-- AUTHOR: 黄鹏宇
-- REMARK: 更新跟进信息
update eh_customer_trackings set customer_source = 0 where namespace_id = 999954;
update eh_customer_trackings set tracking_type = 5 where namespace_id = 999954 and tracking_type=3;
update eh_customer_trackings set tracking_type = 4 where namespace_id = 999954 and tracking_type=4;
update eh_customer_trackings set tracking_type = 4 where namespace_id = 999954 and tracking_type=2;
update eh_customer_trackings set tracking_type = 3 where namespace_id = 999954 and tracking_type=13439;

-- REMARK: 所有含有organization的客户都进入租客
update eh_enterprise_customers set customer_source = 1, level_item_id  = 6 where organization_id is not null and status = 2;


-- --------------------- SECTION END ALL -----------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本

-- AUTHOR: xq.tian
-- REMARK: 把基线的 2 域空间删掉，标准版不执行这个 sql
DELETE FROM eh_namespaces WHERE id=2;

update eh_launch_pad_items set action_type=55, action_data = replace(action_data, '"News"', '"NewsFlash"')
where namespace_id=999938 and action_type in (48, 55) and action_data like '%"widget"%';

update eh_launch_pad_items set action_type=55, action_data = replace(action_data, '}', ',"widget":"NewsFlash"}')
where namespace_id=999938 and  action_type in (48, 55) and action_data not like '%"widget"%';


-- --------------------- SECTION END zuolin-base ---------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-standard
-- DESCRIPTION: 此SECTION只在左邻标准版执行的脚本

-- AUTHOR: xq.tian
-- REMARK: 标准版数据库的标识
INSERT INTO eh_configurations (name, value, description, namespace_id, display_name)
  VALUES ('server.standard.flag','true','标准版 server 标识',2, '标准版 server 标识');

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
-- AUTHOR: 黄良铭
-- REMARK: 新增ＩＤ为1的积分系统
INSERT INTO `eh_point_systems` (`id`, `namespace_id`, `display_name`, `point_name`, `point_exchange_flag`, `exchange_point`, `exchange_cash`, `user_agreement`, `status`, `create_time`, `creator_uid`, `update_time`, `update_uid`)
VALUES('1','0','固定数据','固定数据　','1','1','2','','2','2018-10-27 10:48:04.621','2',NULL,NULL);

-- --------------------- SECTION END nanshanquzhengfu ----------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guanzhouyuekongjian
-- DESCRIPTION: 此SECTION只在广州越空间-999930执行的脚本

-- AUTHOR: xq.tian
-- REMARK: 越空间独立部署的 root 用户的密码修改为: eh#1802

-- --------------------- SECTION END guanzhouyuekongjian -------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ruianxintiandi
-- DESCRIPTION: 此SECTION只在上海瑞安新天地-999929执行的脚本
-- AUTHOR:梁燕龙  20181022
-- REMARK: 瑞安个人中心跳转URL

-- --------------------- SECTION END ruianxintiandi ------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: wanzhihui
-- DESCRIPTION: 此SECTION只在万智汇-999953执行的脚本
-- --------------------- SECTION END wanzhihui ------------------------------------------
