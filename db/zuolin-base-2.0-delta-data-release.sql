
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
UPDATE eh_service_modules set app_type = 0 WHERE id in (50100,  50300, 50500, 50400, 52000, 50600, 54000, 51300, 51400, 55000, 57000, 60100, 60200, 60210, 13000, 20650, 20830, 41020, 50700);
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
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('2', '50100', '组织架构', '通讯录', '1', '1', '2', '2', '0');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('3', '50100', '组织架构', '组织架构', '2', '4', '1', '4', '1');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('4', '50100', '组织架构', '通讯录', '2', '4', '2', '2', '1');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('7', '50300', '职级管理', '职级管理', '2', '4', '1', '4', '2');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('11', '50500', '员工认证', '员工认证', '2', '4', '1', '4', '3');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('15', '50400', '人事档案', '人事档案', '2', '4', '1', '4', '4');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('18', '52000', '流程审批', '审批', '1', '1', '2', '1', '1');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('19', '52000', '流程审批', '审批管理', '2', '4', '1', '3', '2');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('20', '52000', '流程审批', '审批', '2', '4', '2', '1', '2');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('22', '50600', '打卡考勤', '打卡考勤', '1', '1', '2', '2', '1');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('23', '50600', '打卡考勤', '考勤管理', '2', '4', '1', '4', '5');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('24', '50600', '打卡考勤', '打卡考勤', '2', '4', '2', '2', '2');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('26', '54000', '工作汇报', '工作汇报', '1', '1', '2', '1', '2');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('27', '54000', '工作汇报', '汇报管理', '2', '4', '1', '3', '3');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('28', '54000', '工作汇报', '工作汇报', '2', '4', '2', '1', '3');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('30', '0', '会议预订', '会议预订', '1', '1', '2', '1', '8');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('31', '0', '会议预订', '会议室管理', '2', '4', '1', '3', '8');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('32', '0', '会议预订', '会议预订', '2', '4', '2', '1', '9');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('35', '51300', '社保管理', '社保管理', '2', '4', '1', '4', '8');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('38', '51400', '薪酬管理', '工资条', '1', '1', '2', '2', '4');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('39', '51400', '薪酬管理', '薪酬管理', '2', '4', '1', '4', '9');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('40', '51400', '薪酬管理', '工资条', '2', '4', '2', '2', '5');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('42', '55000', '文档', '文档', '1', '1', '2', '1', '3');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('43', '55000', '文档', '文档管理', '2', '4', '1', '3', '4');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('44', '55000', '文档', '文档', '2', '4', '2', '1', '4');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('46', '0', '日程提醒', '日程提醒', '1', '1', '2', '1', '7');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('48', '0', '日程提醒', '日程提醒', '2', '4', '2', '1', '8');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('50', '57000', '企业公告', '企业公告', '1', '1', '2', '1', '5');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('51', '57000', '企业公告', '公告管理', '2', '4', '1', '3', '6');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('52', '57000', '企业公告', '企业公告', '2', '4', '2', '1', '6');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('55', '60100', '管理员管理', '管理员管理', '2', '4', '1', '0', '0');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('59', '60210', '责任部门配置', '责任部门配置', '2', '4', '1', '0', '0');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('62', '20830', '任务管理', '我的任务', '1', '1', '2', '1', '0');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('63', '20830', '任务管理', '任务管理', '2', '4', '1', '3', '1');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('64', '20830', '任务管理', '我的任务', '2', '4', '2', '1', '1');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('67', '41300', '应用活跃统计', '应用活跃统计', '2', '4', '1', '15', '1');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('71', '41330', '用户行为统计', '用户行为统计', '2', '4', '1', '15', '2');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('74', '0', '企业门禁', '企业门禁', '1', '1', '2', '2', '2');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('75', '41020', '企业门禁', '企业门禁管理', '2', '4', '1', '4', '6');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('76', '0', '企业门禁', '企业门禁', '2', '4', '2', '2', '3');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('78', '0', '公共门禁', '公共门禁', '1', '2', '2', '12', '4');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('79', '41010', '公共门禁', '公共门禁管理', '2', '4', '1', '11', '4');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('82', '10300', '园区公告', '园区公告', '1', '2', '2', '6', '1');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('83', '10300', '园区公告', '园区公告', '2', '4', '1', '6', '1');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('84', '10300', '园区公告', '园区公告', '2', '5', '2', '6', '0');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('87', '10900', '启动广告', '启动广告', '2', '4', '1', '6', '3');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('90', '10400', '园区广告', '园区广告', '1', '2', '2', '6', '0');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('91', '10400', '园区广告', '园区广告', '2', '4', '1', '6', '2');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('92', '10400', '园区广告', '园区广告', '2', '5', '2', '6', '0');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('94', '10800', '园区快讯', '园区快讯', '1', '2', '2', '6', '2');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('95', '10800', '园区快讯', '园区快讯', '2', '4', '1', '6', '4');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('96', '10800', '园区快讯', '园区快讯', '2', '5', '2', '6', '0');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('98', '10500', '园区电子报', '园区电子报', '1', '2', '2', '6', '3');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('99', '10500', '园区电子报', '园区电子报', '2', '4', '1', '6', '5');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('103', '11000', '一键推送', '一键推送', '2', '4', '1', '6', '7');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('107', '12200', '短信推送（定制）', '短信推送', '2', '4', '1', '6', '8');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('111', '34000', '用户管理', '用户管理', '2', '4', '1', '7', '1');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('115', '35000', '用户认证', '用户认证', '2', '4', '1', '7', '2');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('119', '47000', '积分管理', '积分管理', '2', '4', '1', '7', '3');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('121', '10600', '园区活动', '园区活动管理', '1', '1', '1', '7', '1');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('122', '10600', '园区活动', '园区活动', '1', '2', '2', '7', '1');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('123', '10600', '园区活动', '园区活动管理', '2', '4', '1', '7', '4');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('124', '10600', '园区活动', '园区活动', '2', '5', '2', '7', '0');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('125', '10100', '园区论坛', '园区论坛管理', '1', '1', '1', '7', '2');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('126', '10100', '园区论坛', '园区论坛', '1', '2', '2', '7', '2');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('127', '10100', '园区论坛', '园区论坛管理', '2', '4', '1', '7', '5');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('129', '10750', '俱乐部', '俱乐部管理', '1', '1', '1', '7', '3');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('130', '10750', '俱乐部', '俱乐部', '1', '2', '2', '7', '3');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('131', '10750', '俱乐部', '俱乐部管理', '2', '4', '1', '7', '6');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('133', '10760', '行业协会', '行业协会管理', '1', '1', '1', '7', '4');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('134', '10760', '行业协会', '行业协会', '1', '2', '2', '7', '4');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('135', '10760', '行业协会', '行业协会管理', '2', '4', '1', '7', '7');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('138', '41700', '问卷调查', '问卷调查', '1', '2', '2', '7', '5');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('139', '41700', '问卷调查', '问卷调查管理', '2', '4', '1', '7', '8');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('143', '30600', '黑名单管理', '黑名单管理', '2', '4', '1', '7', '9');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('147', '51000', '举报管理', '举报管理', '2', '4', '1', '7', '10');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('150', '40100', '园区入驻', '园区入驻', '1', '2', '2', '9', '1');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('151', '40100', '园区入驻', '园区入驻', '2', '4', '1', '10', '1');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('152', '40100', '园区入驻', '园区入驻', '2', '5', '2', '9', '0');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('155', '36000', '孵化器入驻（定制）', '孵化器入驻', '2', '4', '1', '10', '2');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('158', '40200', '工位预订', '工位预订', '1', '2', '2', '9', '2');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('159', '40200', '工位预订', '工位预订', '2', '4', '1', '10', '3');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('160', '40200', '工位预订', '工位预订', '2', '5', '2', '9', '0');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('162', '22000', '装修办理', '装修办理', '1', '2', '2', '9', '3');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('163', '22000', '装修办理', '装修申请管理', '2', '4', '1', '11', '12');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('167', '22000', '楼宇资产管理', '楼宇资产管理', '2', '4', '1', '8', '1');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('170', '33000', '企业管理', '园区企业', '1', '2', '2', '12', '15');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('171', '33000', '企业管理', '企业管理', '2', '4', '1', '8', '2');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('173', '21100', '企业客户管理', '移动招商', '1', '1', '1', '8', '1');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('175', '21100', '企业客户管理', '企业客户管理', '2', '4', '1', '8', '3');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('179', '0', '企业信息', '企业信息', '2', '4', '1', '3', '10');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('183', '37000', '个人客户管理', '个人客户管理', '2', '4', '1', '8', '4');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('187', '21200', '合同管理', '合同管理', '2', '4', '1', '10', '6');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('191', '32500', '合同管理（定制）', '合同管理', '2', '4', '1', '10', '7');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('194', '20400', '物业缴费', '物业缴费', '1', '2', '2', '9', '4');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('195', '20400', '物业缴费', '物业缴费管理', '2', '4', '1', '14', '3');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('199', '21300', '付款管理', '付款管理', '2', '4', '1', '14', '11');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('203', '0', '车辆管理（定制）', '车辆管理', '2', '4', '1', '11', '8');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('205', '40400', '资源预约', '资源预约管理', '1', '1', '1', '11', '1');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('206', '40400', '资源预约', '资源预约', '1', '2', '2', '12', '7');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('207', '40400', '资源预约', '资源预约管理', '2', '4', '1', '11', '6');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('208', '40400', '资源预约', '资源预约', '2', '5', '2', '12', '0');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('210', '40800', '停车缴费', '停车缴费', '1', '2', '2', '12', '8');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('211', '40800', '停车缴费', '停车缴费', '2', '4', '1', '11', '7');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('213', '20900', '车辆放行', '车辆放行', '1', '1', '1', '11', '2');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('215', '20900', '车辆放行', '车辆放行', '2', '4', '1', '11', '9');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('217', '49200', '物品搬迁', '物品放行', '1', '1', '1', '11', '3');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('218', '49200', '物品搬迁', '物品搬迁', '1', '2', '2', '12', '9');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('219', '49200', '物品搬迁', '物品搬迁', '2', '4', '1', '11', '10');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('222', '40300', '服务热线', '服务热线', '1', '2', '2', '12', '1');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('223', '40300', '服务热线', '服务热线', '2', '4', '1', '11', '1');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('224', '40300', '服务热线', '服务热线', '2', '5', '2', '12', '0');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('225', '41400', '园区云打印', '云打印管理', '1', '1', '1', '11', '4');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('226', '41400', '园区云打印', '云打印', '1', '2', '2', '12', '10');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('227', '41400', '园区云打印', '云打印管理', '2', '4', '1', '11', '11');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('230', '40700', '快递服务', '快递服务', '1', '2', '2', '12', '10');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('231', '40700', '快递服务', '快递服务', '2', '4', '1', '11', '13');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('234', '41500', '文件管理', '文件管理', '1', '2', '2', '6', '4');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('235', '41500', '文件管理', '文件管理', '2', '4', '1', '6', '6');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('238', '41100', '一键上网', '一键上网', '1', '2', '2', '12', '11');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('239', '41100', '一键上网', '一键上网', '2', '4', '1', '11', '14');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('242', '40070', '园区地图（定制）', '园区地图', '1', '2', '2', '12', '12');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('243', '40070', '园区地图（定制）', '园区地图', '2', '4', '1', '11', '15');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('246', '40730', '企业人才（定制）', '企业人才', '1', '2', '2', '12', '13');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('247', '40730', '企业人才（定制）', '企业人才', '2', '4', '1', '11', '16');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('250', '41600', '园区审批（定制）', '园区审批', '1', '2', '2', '12', '14');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('251', '41600', '园区审批（定制）', '园区审批', '2', '4', '1', '11', '17');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('254', '41200', '电子钱包（定制）', '钱包', '1', '2', '2', '12', '16');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('255', '41200', '电子钱包（定制）', '一卡通', '2', '4', '1', '11', '18');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('258', '40500', '服务联盟', '服务联盟', '1', '2', '2', '13', '1');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('259', '40500', '服务联盟', '服务联盟', '2', '4', '1', '13', '1');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('260', '40500', '服务联盟', '服务联盟', '2', '5', '2', '13', '0');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('262', '20100', '物业报修', '物业报修', '1', '2', '2', '12', '2');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('263', '20100', '物业报修', '报修管理', '2', '4', '1', '14', '2');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('265', '20800', '物业巡检', '物业巡检', '1', '1', '1', '14', '1');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('267', '20800', '物业巡检', '物业巡检', '2', '4', '1', '14', '3');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('269', '20600', '品质核查', '品质核查', '1', '1', '1', '14', '2');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('271', '20600', '品质核查', '品质核查', '2', '4', '1', '14', '4');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('273', '49100', '能耗管理', '能耗管理', '1', '1', '1', '14', '3');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('275', '49100', '能耗管理', '能耗管理', '2', '4', '1', '14', '5');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('279', '21000', '仓库管理', '仓库管理', '2', '4', '1', '14', '7');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('283', '26000', '采购管理', '采购管理', '2', '4', '1', '14', '8');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('287', '27000', '供应商管理', '供应商管理', '2', '4', '1', '14', '9');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('291', '25000', '请示单管理（定制）', '请示单管理', '2', '4', '1', '14', '10');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('294', '0', '我的钥匙（定制）', '我的钥匙', '1', '2', '2', '12', '6');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('298', '0', '园区访客', '访客预约', '1', '2', '2', '12', '5');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('299', '0', '园区访客', '园区访客', '2', '4', '1', '11', '5');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('302', '0', '企业访客', '访客预约', '1', '1', '2', '1', '9');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('303', '0', '企业访客', '企业访客', '2', '4', '1', '3', '9');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('306', '50700', '视频会议', '视频会议', '1', '1', '2', '1', '11');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('311', '0', '企业账户', '企业账户', '2', '4', '1', '3', '12');
INSERT INTO `eh_service_module_entries` (`id`, `module_id`, `module_name`, `entry_name`, `terminal_type`, `location_type`, `scene_type`, `second_app_type`, `default_order`) VALUES ('312', '41000', '门禁（临时）', '门禁（临时）', '1', '2', '2', '12', '10');





-- 更新左邻标准版的layout
DELETE  from eh_launch_pad_layouts WHERE namespace_id = 2;

SET @layoutId = (SELECT MAX(id) + INTERVAL(500000000, MAX(id)) * 500000000 from eh_launch_pad_layouts);
-- 工作台
SET @layoutIdW = @layoutId + 1;
-- 广场
SET @layoutIdC = @layoutId + 2;

INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`, `bg_image_uri`, `preview_portal_version_id`, `type`, `bg_color`) VALUES ((@layoutId + 1), '2', 'ServiceMarketLayout', '{\"versionCode\":\"201806080103\",\"layoutName\":\"ServiceMarketLayout\",\"displayName\":\"工作台\",\"groups\":[{\"groupName\":\"公告\",\"widget\":\"Bulletins\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31057\",\"rowCount\":1},\"defaultOrder\":1,\"separatorFlag\":1,\"separatorHeight\":24},{\"groupName\":\"应用管理\",\"title\":\"应用管理\",\"widget\":\"Card\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"paddingTop\":16,\"paddingLeft\":16,\"paddingBottom\":16,\"paddingRight\":16,\"lineSpacing\":20,\"columnSpacing\":20,\"backgroundColor\":\"#ffffff\",\"appType\":1},\"style\":\"Default\",\"defaultOrder\":2,\"separatorFlag\":1,\"separatorHeight\":32,\"columnCount\":1},{\"groupName\":\"OA应用\",\"title\":\"OA应用\",\"widget\":\"Card\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"paddingTop\":16,\"paddingLeft\":16,\"paddingBottom\":16,\"paddingRight\":16,\"lineSpacing\":20,\"columnSpacing\":20,\"backgroundColor\":\"#ffffff\",\"appType\":0},\"style\":\"Default\",\"defaultOrder\":4 ,\"separatorFlag\":1,\"separatorHeight\":32,\"columnCount\":1}]}', '201806080103', '0', '2', NULL, 'default', '0', '0', '0', NULL, NULL, '4', NULL);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`, `bg_image_uri`, `preview_portal_version_id`, `type`, `bg_color`) VALUES ((@layoutId + 2), '2', 'ServiceMarketLayout', '{\"versionCode\":\"201806110101\",\"layoutName\":\"ServiceMarketLayout\",\"displayName\":\"服务广场\",\"groups\":[{\"groupName\":\"banner图片1\",\"widget\":\"Banners\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31056\",\"wideRatio\":16,\"hightRatio\":9,\"shadowFlag\":1,\"paddingFlag\":1},\"defaultOrder\":1,\"separatorFlag\":0,\"separatorHeight\":0},{\"groupName\":\"应用\",\"widget\":\"Navigator\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"paddingTop\":0,\"paddingLeft\":16,\"paddingBottom\":0,\"paddingRight\":16,\"lineSpacing\":0,\"columnSpacing\":0,\"cssStyleFlag\":1,\"backgroundColor\":\"#ffffff\"},\"style\":\"Default\",\"defaultOrder\":2,\"separatorFlag\":1,\"separatorHeight\":24,\"columnCount\":4},{\"groupName\":\"公告\",\"widget\":\"Bulletins\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31057\",\"rowCount\":1,\"style\":2},\"defaultOrder\":3,\"separatorFlag\":1,\"separatorHeight\":24},{\"groupName\":\"商品精选\",\"widget\":\"OPPush\",\"title\":\"商品精选\",\"instanceConfig\":{\"newsSize\":5},\"style\":\"HorizontalScrollSquareView\",\"defaultOrder\":4,\"separatorFlag\":1,\"separatorHeight\":24},{\"groupName\":\"最新活动\",\"widget\":\"OPPush\",\"title\":\"最新活动\",\"instanceConfig\":{\"itemGroup\":\"OPPushActivity\",\"entityCount\":5,\"subjectHeight\":0,\"descriptionHeight\":0,\"newsSize\":5,\"moduleId\":10600,\"appId\":115269,\"actionType\":62,\"serviceModuleAppInstanceConfig\":{\"categoryId\":1,\"publishPrivilege\":1,\"livePrivilege\":0,\"listStyle\":2,\"scope\":3,\"style\":4}},\"style\":\"HorizontalScrollWideView\",\"defaultOrder\":5,\"separatorFlag\":1,\"separatorHeight\":24},{\"groupName\":\"最新帖子\",\"widget\":\"OPPush\",\"title\":\"最新帖子\",\"instanceConfig\":{\"newsSize\":5},\"style\":\"TextImageWithTagListView\",\"defaultOrder\":7,\"separatorFlag\":1,\"separatorHeight\":24}]}', '201806110101', '0', '2', NULL, 'pm_admin', '0', '0', '0', NULL, NULL, '5', NULL);

-- 添加标准版主页签
INSERT INTO `eh_launch_pad_indexs` (`id`, `namespace_id`, `type`, `name`, `config_json`, `icon_uri`, `selected_icon_uri`, `status`, `create_time`, `update_time`, `operator_uid`, `creator_uid`, `description`, `default_order`) VALUES ('1', '2', '1', '工作台', CONCAT('{\"layoutId\":',@layoutId + 1,', \"layoutType\":4}'), 'cs://1/image/aW1hZ2UvTVRvMll6Y3hNemMwWXpWaVl6WTVOVFpoTlRneFlUUTVaalU0WlRSaFpUQXlOUQ', 'cs://1/image/aW1hZ2UvTVRveU9USmpNamRtTWprNU1qRmpObUl5T1RNd05USXhNRE5tTnpGa05tSmhNZw', '2', '2018-04-20 10:12:37', '2018-04-20 10:12:40', '1', '1', '1', '0');
INSERT INTO `eh_launch_pad_indexs` (`id`, `namespace_id`, `type`, `name`, `config_json`, `icon_uri`, `selected_icon_uri`, `status`, `create_time`, `update_time`, `operator_uid`, `creator_uid`, `description`, `default_order`) VALUES ('2', '2', '1', '广场', CONCAT('{\"layoutId\":',@layoutId + 2,', \"layoutType\":5}'), 'cs://1/image/aW1hZ2UvTVRwbE1HUm1Nekk0WTJFM1lUYzVZV1psWW1NMU5HUmpZV0V3TldOaU56UmxNZw', 'cs://1/image/aW1hZ2UvTVRwaFpqWmhOek0wWmpBM05URTNaRFprWTJVM01ERTJOMkUwTldNd00ySm1ZUQ', '2', '2018-04-20 10:12:37', '2018-04-20 10:12:40', '1', '1', '1', '2');
INSERT INTO `eh_launch_pad_indexs` (`id`, `namespace_id`, `type`, `name`, `config_json`, `icon_uri`, `selected_icon_uri`, `status`, `create_time`, `update_time`, `operator_uid`, `creator_uid`, `description`, `default_order`) VALUES ('3', '2', '3', '消息', '{}', 'cs://1/image/aW1hZ2UvTVRvNFltTmhNREU0TXpFd01UZGtZV1k0TTJGbVlXWTNPR1k0T1RZd1ltRTFNZw', 'cs://1/image/aW1hZ2UvTVRwbU5tSTNNV1EzTWpOa016WmtaRE15TTJOaVpHSTFZbUprWVRSak1qaGhaZw', '2', '2018-04-20 10:12:37', '2018-04-20 10:12:40', '1', '1', '1', '3');
INSERT INTO `eh_launch_pad_indexs` (`id`, `namespace_id`, `type`, `name`, `config_json`, `icon_uri`, `selected_icon_uri`, `status`, `create_time`, `update_time`, `operator_uid`, `creator_uid`, `description`, `default_order`) VALUES ('4', '2', '4', '我的', '{}', 'cs://1/image/aW1hZ2UvTVRvMU1HUmxZMkUxWXpnMU5HRTBZelkzT1dNM01EUXlOekpsWkRVeFpEY3pZZw', 'cs://1/image/aW1hZ2UvTVRvd016Wm1NekZoTnpSbU16WTFZV1E1WTJVeFkyUTJNamxtTURabVkyTmpPQQ', '2', '2018-04-20 10:12:37', '2018-04-20 10:12:40', '1', '1', '1', '4');

-- 增加应用、项目和公司菜单
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15030000', '应用管理', '15000000', NULL, 'application-management', '1', '2', '/15000000/15030000', 'zuolin', '3', NULL, '2', 'system', 'module', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15040000', '项目管理', '15000000', NULL, 'project-management', '1', '2', '/15000000/15040000', 'zuolin', '4', NULL, '2', 'system', 'module', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15050000', '企业管理', '15000000', NULL, 'business-admin', '1', '2', '/15000000/15050000', 'zuolin', '5', NULL, '2', 'system', 'module', NULL);
DELETE FROM `ehcore`.`eh_web_menus` WHERE `id`='15030100';
DELETE FROM `ehcore`.`eh_web_menus` WHERE `id`='15030200';
DELETE FROM `ehcore`.`eh_web_menus` WHERE `id`='15030300';

