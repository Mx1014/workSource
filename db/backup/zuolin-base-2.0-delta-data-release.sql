
-- add by yuanlei
SET @id = (SELECT MAX(id) + INTERVAL(500000000, MAX(id)) * 500000000 from eh_launch_pad_layouts);
INSERT INTO `eh_locale_strings`(`id`,`scope`, `code`,`locale`, `text`) VALUES(@id := @id+1,'organization', '900031', 'zh_CN', '无法注销企业。当前企业仍存在需要管理的项目。请转移项目管理权至其它公司后再试');
insert into eh_locale_strings(`id`,scope,code,locale,`text`)values(@id := @id+1,'building',10004,'zh_CN','该楼栋名称已经存在，请更换其他名称');
insert into eh_locale_strings(`id`,scope,code,locale,`text`)values(@id := @id+1,'organization',900032,'zh_CN','姓名为空');
insert into eh_locale_strings(`id`,scope,code,locale,`text`)values(@id := @id+1,'organization',900033,'zh_CN','办公地点名称为空');
insert into eh_locale_strings(`id`,scope,code,locale,`text`)values(@id := @id+1,'organization',900034,'zh_CN','办公地点所属项目为空');
insert into eh_locale_strings(`id`,scope,code,locale,`text`)values(@id := @id+1,'organization',900035,'zh_CN','是否属于管理公司标志为空');
insert into eh_locale_strings(`id`,scope,code,locale,`text`)values(@id := @id+1,'organization',900036,'zh_CN','是否属于服务商标志为空');
insert into eh_locale_strings(`id`,scope,code,locale,`text`)values(@id := @id+1,'organization',900037,'zh_CN','是否启用工作台标志为空');
insert into eh_locale_strings(`id`,scope,code,locale,`text`)values(@id := @id+1,'organization',900038,'zh_CN','公司名称不能超过50字');
insert into eh_locale_strings(`id`,scope,code,locale,`text`)values(@id := @id+1,'community',10013,'zh_CN','楼栋名称不能超过20个汉字');
insert into eh_locale_strings(`id`,scope,code,locale,`text`)values(@id := @id+1,'community',10014,'zh_CN','楼栋名称重复了');
insert into eh_locale_strings(`id`,scope,code,locale,`text`)values(@id := @id+1,'address',20011,'zh_CN','门牌地址超过了20个汉字');
insert into eh_locale_strings(`id`,scope,code,locale,`text`)values(@id := @id+1,'building',10005,'zh_CN','该项目下不存在该楼栋');
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
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('3', '50100', '组织架构', '组织架构', '2', '4', '1', '4', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('4', '50100', '组织架构', '通讯录', '2', '4', '2', '2', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('7', '50300', '职级管理', '职级管理', '2', '4', '1', '4', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('11', '50500', '员工认证', '员工认证', '2', '4', '1', '4', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('15', '50400', '人事档案', '人事档案', '2', '4', '1', '4', '4', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('18', '52000', '流程审批', '审批', '1', '1', '2', '1', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('19', '52000', '流程审批', '审批管理', '2', '4', '1', '3', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('20', '52000', '流程审批', '审批', '2', '4', '2', '1', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('22', '50600', '打卡考勤', '打卡考勤', '1', '1', '2', '2', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('23', '50600', '打卡考勤', '考勤管理', '2', '4', '1', '4', '5', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('24', '50600', '打卡考勤', '打卡考勤', '2', '4', '2', '2', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('26', '54000', '工作汇报', '工作汇报', '1', '1', '2', '1', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('27', '54000', '工作汇报', '汇报管理', '2', '4', '1', '3', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('28', '54000', '工作汇报', '工作汇报', '2', '4', '2', '1', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('35', '51300', '社保管理', '社保管理', '2', '4', '1', '4', '8', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('38', '51400', '薪酬管理', '工资条', '1', '1', '2', '2', '4', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('39', '51400', '薪酬管理', '薪酬管理', '2', '4', '1', '4', '9', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('40', '51400', '薪酬管理', '工资条', '2', '4', '2', '2', '5', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('42', '55000', '文档', '文档', '1', '1', '2', '1', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('43', '55000', '文档', '文档管理', '2', '4', '1', '3', '4', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('44', '55000', '文档', '文档', '2', '4', '2', '1', '4', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('50', '57000', '企业公告', '企业公告', '1', '1', '2', '1', '5', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('51', '57000', '企业公告', '公告管理', '2', '4', '1', '3', '6', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('52', '57000', '企业公告', '企业公告', '2', '4', '2', '1', '6', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('55', '60100', '管理员管理', '管理员管理', '2', '4', '1', '0', '0', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('59', '60210', '责任部门配置', '责任部门配置', '2', '4', '1', '0', '0', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('62', '20830', '任务管理', '我的任务', '1', '1', '2', '1', '0', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('63', '20830', '任务管理', '任务管理', '2', '4', '1', '3', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('64', '20830', '任务管理', '我的任务', '2', '4', '2', '1', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('67', '41300', '应用活跃统计', '应用活跃统计', '2', '4', '1', '15', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('71', '41330', '用户行为统计', '用户行为统计', '2', '4', '1', '15', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('75', '41020', '企业门禁', '企业门禁管理', '2', '4', '1', '4', '6', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('79', '41010', '公共门禁', '公共门禁管理', '2', '4', '1', '11', '4', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('82', '10300', '园区公告', '园区公告', '1', '2', '2', '6', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('83', '10300', '园区公告', '园区公告', '2', '4', '1', '6', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('84', '10300', '园区公告', '园区公告', '2', '5', '2', '6', '0', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('87', '10900', '启动广告', '启动广告', '2', '4', '1', '6', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('90', '10400', '园区广告', '园区广告', '1', '2', '2', '6', '0', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('91', '10400', '园区广告', '园区广告', '2', '4', '1', '6', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('92', '10400', '园区广告', '园区广告', '2', '5', '2', '6', '0', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('94', '10800', '园区快讯', '园区快讯', '1', '2', '2', '6', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('95', '10800', '园区快讯', '园区快讯', '2', '4', '1', '6', '4', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('96', '10800', '园区快讯', '园区快讯', '2', '5', '2', '6', '0', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('98', '10500', '园区电子报', '园区电子报', '1', '2', '2', '6', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('99', '10500', '园区电子报', '园区电子报', '2', '4', '1', '6', '5', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('103', '11000', '一键推送', '一键推送', '2', '4', '1', '6', '7', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('107', '12200', '短信推送（定制）', '短信推送', '2', '4', '1', '6', '8', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('111', '34000', '用户管理', '用户管理', '2', '4', '1', '7', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('115', '35000', '用户认证', '用户认证', '2', '4', '1', '7', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('119', '47000', '积分管理', '积分管理', '2', '4', '1', '7', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('121', '10600', '园区活动', '园区活动管理', '1', '1', '1', '7', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('122', '10600', '园区活动', '园区活动', '1', '2', '2', '7', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('123', '10600', '园区活动', '园区活动管理', '2', '4', '1', '7', '4', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('124', '10600', '园区活动', '园区活动', '2', '5', '2', '7', '0', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('125', '10100', '园区论坛', '园区论坛管理', '1', '1', '1', '7', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('126', '10100', '园区论坛', '园区论坛', '1', '2', '2', '7', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('127', '10100', '园区论坛', '园区论坛管理', '2', '4', '1', '7', '5', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('129', '10750', '俱乐部', '俱乐部管理', '1', '1', '1', '7', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('130', '10750', '俱乐部', '俱乐部', '1', '2', '2', '7', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('131', '10750', '俱乐部', '俱乐部管理', '2', '4', '1', '7', '6', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('133', '10760', '行业协会', '行业协会管理', '1', '1', '1', '7', '4', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('134', '10760', '行业协会', '行业协会', '1', '2', '2', '7', '4', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('135', '10760', '行业协会', '行业协会管理', '2', '4', '1', '7', '7', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('138', '41700', '问卷调查', '问卷调查', '1', '2', '2', '7', '5', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('139', '41700', '问卷调查', '问卷调查管理', '2', '4', '1', '7', '8', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('143', '30600', '黑名单管理', '黑名单管理', '2', '4', '1', '7', '9', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('147', '51000', '举报管理', '举报管理', '2', '4', '1', '7', '10', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('150', '40100', '园区入驻', '园区入驻', '1', '2', '2', '9', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('151', '40100', '园区入驻', '园区入驻', '2', '4', '1', '10', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('152', '40100', '园区入驻', '园区入驻', '2', '5', '2', '9', '0', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('155', '36000', '孵化器入驻（定制）', '孵化器入驻', '2', '4', '1', '10', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('158', '40200', '工位预订', '工位预订', '1', '2', '2', '9', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('159', '40200', '工位预订', '工位预订', '2', '4', '1', '10', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('160', '40200', '工位预订', '工位预订', '2', '5', '2', '9', '0', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('162', '22000', '装修办理', '装修办理', '1', '2', '2', '9', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('163', '22000', '装修办理', '装修申请管理', '2', '4', '1', '11', '12', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('167', '22000', '楼宇资产管理', '楼宇资产管理', '2', '4', '1', '8', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('170', '33000', '企业管理', '园区企业', '1', '2', '2', '12', '15', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('171', '33000', '企业管理', '企业管理', '2', '4', '1', '8', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('173', '21100', '企业客户管理', '移动招商', '1', '1', '1', '8', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('175', '21100', '企业客户管理', '企业客户管理', '2', '4', '1', '8', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('183', '37000', '个人客户管理', '个人客户管理', '2', '4', '1', '8', '4', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('187', '21200', '合同管理', '合同管理', '2', '4', '1', '10', '6', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('191', '32500', '合同管理（定制）', '合同管理', '2', '4', '1', '10', '7', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('194', '20400', '物业缴费', '物业缴费', '1', '2', '2', '9', '4', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('195', '20400', '物业缴费', '物业缴费管理', '2', '4', '1', '14', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('199', '21300', '付款管理', '付款管理', '2', '4', '1', '14', '11', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('205', '40400', '资源预约', '资源预约管理', '1', '1', '1', '11', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('206', '40400', '资源预约', '资源预约', '1', '2', '2', '12', '7', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('207', '40400', '资源预约', '资源预约管理', '2', '4', '1', '11', '6', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('208', '40400', '资源预约', '资源预约', '2', '5', '2', '12', '0', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('210', '40800', '停车缴费', '停车缴费', '1', '2', '2', '12', '8', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('211', '40800', '停车缴费', '停车缴费', '2', '4', '1', '11', '7', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('213', '20900', '车辆放行', '车辆放行', '1', '1', '1', '11', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('215', '20900', '车辆放行', '车辆放行', '2', '4', '1', '11', '9', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('217', '49200', '物品搬迁', '物品放行', '1', '1', '1', '11', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('218', '49200', '物品搬迁', '物品搬迁', '1', '2', '2', '12', '9', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('219', '49200', '物品搬迁', '物品搬迁', '2', '4', '1', '11', '10', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('222', '40300', '服务热线', '服务热线', '1', '2', '2', '12', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('223', '40300', '服务热线', '服务热线', '2', '4', '1', '11', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('224', '40300', '服务热线', '服务热线', '2', '5', '2', '12', '0', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('225', '41400', '企业云打印', '云打印管理', '1', '1', '1', '3', '4', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('226', '41400', '企业云打印', '云打印', '1', '1', '2', '1', '10', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('227', '41400', '企业云打印', '云打印管理', '2', '4', '1', '3', '11', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('228', '41400', '企业云打印', '云打印', '2', '5', '2', '1', '11', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('230', '40700', '快递服务', '快递服务', '1', '2', '2', '12', '10', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('231', '40700', '快递服务', '快递服务', '2', '4', '1', '11', '13', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('234', '41500', '文件管理', '文件管理', '1', '2', '2', '6', '4', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('235', '41500', '文件管理', '文件管理', '2', '4', '1', '6', '6', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('238', '41100', '一键上网', '一键上网', '1', '2', '2', '12', '11', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('239', '41100', '一键上网', '一键上网', '2', '4', '1', '11', '14', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('242', '40070', '园区地图（定制）', '园区地图', '1', '2', '2', '12', '12', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('243', '40070', '园区地图（定制）', '园区地图', '2', '4', '1', '11', '15', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('246', '40730', '企业人才（定制）', '企业人才', '1', '2', '2', '12', '13', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('247', '40730', '企业人才（定制）', '企业人才', '2', '4', '1', '11', '16', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('250', '41600', '园区审批（定制）', '园区审批', '1', '2', '2', '12', '14', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('251', '41600', '园区审批（定制）', '园区审批', '2', '4', '1', '11', '17', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('254', '41200', '电子钱包（定制）', '钱包', '1', '2', '2', '12', '16', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('255', '41200', '电子钱包（定制）', '一卡通', '2', '4', '1', '11', '18', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('258', '40500', '服务联盟', '服务联盟', '1', '2', '2', '13', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('259', '40500', '服务联盟', '服务联盟', '2', '4', '1', '13', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('260', '40500', '服务联盟', '服务联盟', '2', '5', '2', '13', '0', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('262', '20100', '物业报修', '物业报修', '1', '2', '2', '12', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('263', '20100', '物业报修', '报修管理', '2', '4', '1', '14', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('265', '20800', '物业巡检', '物业巡检', '1', '1', '1', '14', '1', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('267', '20800', '物业巡检', '物业巡检', '2', '4', '1', '14', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('269', '20600', '品质核查', '品质核查', '1', '1', '1', '14', '2', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('271', '20600', '品质核查', '品质核查', '2', '4', '1', '14', '4', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('273', '49100', '能耗管理', '能耗管理', '1', '1', '1', '14', '3', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('275', '49100', '能耗管理', '能耗管理', '2', '4', '1', '14', '5', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('279', '21000', '仓库管理', '仓库管理', '2', '4', '1', '14', '7', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('283', '26000', '采购管理', '采购管理', '2', '4', '1', '14', '8', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('287', '27000', '供应商管理', '供应商管理', '2', '4', '1', '14', '9', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('291', '25000', '请示单管理（定制）', '请示单管理', '2', '4', '1', '14', '10', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('306', '50700', '视频会议', '视频会议', '1', '1', '2', '1', '11', NULL);
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`, `uri`) VALUES ('312', '41000', '门禁（临时）', '门禁（临时）', '1', '2', '2', '12', '10', NULL);


-- 增加应用、项目和公司菜单
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15025000', '应用入口', '15000000', NULL, 'servicemodule-entry', '1', '2', '/15000000/15025000', 'zuolin', '30', NULL, '2', 'system', 'module', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15030000', '应用管理', '15000000', NULL, 'application-management', '1', '2', '/15000000/15030000', 'zuolin', '40', NULL, '2', 'system', 'module', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15040000', '项目管理', '15000000', NULL, 'project-management', '1', '2', '/15000000/15040000', 'zuolin', '50', NULL, '2', 'system', 'module', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15050000', '企业管理', '15000000', NULL, 'business-admin', '1', '2', '/15000000/15050000', 'zuolin', '60', NULL, '2', 'system', 'module', NULL);
DELETE FROM `eh_web_menus` WHERE `id`='15030100';
DELETE FROM `eh_web_menus` WHERE `id`='15030200';
DELETE FROM `eh_web_menus` WHERE `id`='15030300';

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

-- 增加电商模块，用于运营板块 add by yanjun 201807041725
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `app_type`, `client_handler_type`, `system_app_flag`) VALUES ('92000', '电商', '0', '/92000', '1', '1', '2', '0', '2018-07-04 17:22:11', NULL, NULL, '2018-07-04 17:22:20', '0', '0', '0', '0', NULL, '1', '0', NULL);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `app_type`, `client_handler_type`, `system_app_flag`) VALUES ('92100', '微商城', '92000', '/92000/92100', '1', '2', '2', '0', '2018-07-04 17:23:28', '{\"url\":\"${stat.biz.server.url}zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=${stat.biz.server.url}nar/biz/web/app/user/index.html?clientrecommend=1#/recommend?_k=zlbiz#sign_suffix\"}', NULL, '2018-07-04 17:23:33', '0', '0', '0', '0', NULL, '1', '2', NULL);

-- 更新企业客户管理模块
UPDATE eh_service_modules set client_handler_type = 2 where id = 21100;

-- 人事档案2.6 数据同步(基线已经执行过,可重复执行)start by ryan
UPDATE eh_organization_member_details AS t SET check_in_time_index = (CONCAT(SUBSTRING(t.check_in_time,6,2),SUBSTRING(t.check_in_time,9,2)));
UPDATE eh_organization_member_details AS t SET birthday_index = (CONCAT(SUBSTRING(t.birthday_index,6,2),SUBSTRING(t.birthday_index,9,2)));
-- end

-- 人事2.7 数据同步 (基线已经执行过,不不不不可重复执行)start by ryan.

-- 执行 /archives/syncArchivesConfigAndLogs 接口

-- 人事2.7 数据同步 end by ryan.

-- #issue-31731【标准版V2.0】【园区快讯】【Android&Ios】客户端内容详情显示失败：404 not found
update eh_configurations set  value = '/html/news_text_review.html'  where name = 'news.content.url';
