-- merge from organization-delta-data-release.sql by lqs 20161128
-- by sfyan 获取电商平台的店铺信息接口
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (0, 'get.businesses.info.api', 'zl-ec/rest/openapi/shop/listByCondition', '获取店铺信息');

INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
VALUES ('300', 'organization', '600001', 'zh_CN', '通用岗位已存在');

-- merge from activityentry-delta-data-release.sql by xiongying20161128
update eh_activities a set forum_id = (select forum_id from eh_forum_posts where id = a.post_id);
update eh_activities a set creator_tag = (select creator_tag from eh_forum_posts where id = a.post_id);
update eh_activities a set target_tag = (select target_tag from eh_forum_posts where id = a.post_id);
update eh_activities a set visible_region_type = (select visible_region_type from eh_forum_posts where id = a.post_id);
update eh_activities a set visible_region_id = (select visible_region_id from eh_forum_posts where id = a.post_id);

-- merge from sa1.7-delta-data-release.sql by xiongying20161128
-- 新增预约看楼模板 add by xiongying 20161116
INSERT INTO `eh_request_templates` (`id`, `template_type`, `name`, `button_title`, `email_flag`, `msg_flag`, `fields_json`, `status`, `creator_uid`, `create_time`)
    VALUES ('9', 'Apartment', '预约看楼', '预约看楼', '1', '1', ' {"fields":[{"fieldName":"name","fieldDisplayName":"姓名","fieldType":"string","fieldContentType":"text","fieldDesc":"userName","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"mobile","fieldDisplayName":"手机号","fieldType":"string","fieldContentType":"text","fieldDesc":"mobile","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"organizationName","fieldDisplayName":"企业名称","fieldType":"string","fieldContentType":"text","fieldDesc":"organizationName","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"areaSize","fieldDisplayName":"面积需求","fieldType":"number","fieldContentType":"text","fieldDesc":"请输入面积需求","requiredFlag":"1","dynamicFlag":"0"},{"fieldName":"remarks","fieldDisplayName":"备注","fieldType":"string","fieldContentType":"text","fieldDesc":"（选填）其他说明","requiredFlag":"0","dynamicFlag":"0"}]}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             ', '1', '1', UTC_TIMESTAMP());
INSERT INTO `eh_request_templates_namespace_mapping` (`id`, `namespace_id`, `template_id`) VALUES (10, '999985', '9');


-- 组织架构 add by sw 20161128
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('10000', '信息发布', '0', '/10000', '0', '1', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('10100', '论坛/公告', '10000', '/10000/10100', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('10200', '园区简介', '40000', '/40000/10200', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('10400', '广告管理', '10000', '/10000/10400', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('10600', '活动管理', '10000', '/10000/10600', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('10700', '路演直播', '10000', '/10000/10700', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('10800', '新闻管理', '10000', '/10000/10800', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('10850', '园区报', '10000', '/10000/10850', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('10900', '行业动态', '10000', '/10000/10900', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('11000', '一键推送', '10000', '/10000/11000', '0', '2', '2', '0', UTC_TIMESTAMP());

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('20000', '物业服务', '0', '/20000', '0', '1', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('20100', '物业报修', '20000', '/20000/20100', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('20400', '物业缴费', '20000', '/20000/20400', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('20600', '品质核查', '20000', '/20000/20600', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('20800', '设备巡检', '20000', '/20000/20800', '0', '2', '2', '0', UTC_TIMESTAMP());

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('30000', '项目管理', '0', '/30000', '2', '1', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('30500', '项目信息', '30000', '/30000/30500', '2', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('31000', '楼栋管理', '30000', '/30000/31000', '2', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('32000', '门牌管理', '30000', '/30000/32000', '2', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('33000', '企业管理', '30000', '/30000/33000', '2', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('34000', '用户管理', '30000', '/30000/34000', '2', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('35000', '用户认证', '30000', '/30000/35000', '2', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('37000', '客户资料', '30000', '/30000/37000', '2', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('38000', '业主管理', '30000', '/30000/38000', '2', '2', '2', '0', UTC_TIMESTAMP());

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('40000', '运营服务', '0', '/40000', '0', '1', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('40100', '招租管理', '40000', '/40000/40100', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('40200', '工位预订', '40000', '/40000/40200', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('40300', '服务热线', '40000', '/40000/40300', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('40400', '资源预订', '40000', '/40000/40400', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('40500', '服务联盟', '40000', '/40000/40500', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('40600', '创客空间', '40000', '/40000/40600', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('40700', '结算管理', '40000', '/40000/40700', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('40750', '运营统计', '40000', '/40000/40750', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('40800', '停车缴费', '40000', '/40000/40800', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('40900', '车辆管理', '40000', '/40000/40900', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('41000', '大堂门禁', '40000', '/40000/41000', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('41100', 'Wifi热点', '40000', '/40000/41100', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('41200', '一卡通', '40000', '/40000/43500', '0', '2', '2', '0', UTC_TIMESTAMP());

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('41300', '场所预订', '40000', '/40000/41300', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('41400', '服务预约', '40000', '/40000/41400', '0', '2', '2', '0', UTC_TIMESTAMP());

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('50000', '内部管理', '0', '/50000', '1', '1', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('50100', '组织架构', '50000', '/50000/50100', '1', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('50200', '岗位管理', '50000', '/50000/50200', '1', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('50300', '职级管理', '50000', '/50000/50300', '1', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('50400', '人员管理', '50000', '/50000/50400', '1', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('50500', '认证管理', '50000', '/50000/50500', '1', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('50600', '考勤管理', '50000', '/50000/50600', '1', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('50700', '视频会议', '50000', '/50000/50700', '1', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('50800', '公司门禁', '50000', '/50000/50800', '1', '2', '2', '0', UTC_TIMESTAMP());

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('60000', '系统管理', '0', '/60000', '2', '1', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('60100', '管理员管理', '60000', '/60000/60100', '2', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('60200', '业务授权', '60000', '/60000/60200', '2', '2', '2', '0', UTC_TIMESTAMP());


INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('1', '10000', '1', '10001', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('2', '10100', '1', '10002', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('3', '10400', '1', '10003', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('4', '10600', '1', '10004', NULL, '0', UTC_TIMESTAMP());


INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('5', '10800', '1', '10005', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('6', '11000', '1', '10006', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('7', '20000', '1', '10007', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('8', '20100', '1', '10008', NULL, '0', UTC_TIMESTAMP());


INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('9', '20400', '1', '10009', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('10', '20600', '1', '10010', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('11', '20800', '1', '10011', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('12', '30000', '1', '10012', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('13', '30500', '1', '10013', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('14', '31000', '1', '10014', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('15', '32000', '1', '10015', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('16', '34000', '1', '10016', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('17', '35000', '1', '10017', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('18', '37000', '1', '10018', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('19', '40000', '1', '10019', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('20', '40100', '1', '10020', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('21', '40200', '1', '10021', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('22', '40300', '1', '10022', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('23', '40400', '1', '10023', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('24', '40500', '1', '10024', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('25', '40600', '1', '10025', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('26', '40700', '1', '10026', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('27', '40750', '1', '10027', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('28', '40800', '1', '10028', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('29', '40900', '1', '10029', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('30', '41000', '1', '10030', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('31', '41100', '1', '10031', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('32', '41200', '1', '10032', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('33', '50000', '1', '10033', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('34', '50100', '1', '10034', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('35', '50200', '1', '10035', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('36', '50300', '1', '10036', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('37', '50400', '1', '10037', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('38', '50500', '1', '10038', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('39', '50600', '1', '10039', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('40', '50700', '1', '10040', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('41', '50800', '1', '10041', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('42', '60000', '1', '10042', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('43', '60100', '1', '10043', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('44', '60200', '1', '10044', NULL, '0', UTC_TIMESTAMP());

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('45', '10700', '1', '10045', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('46', '10900', '1', '10046', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('47', '33000', '1', '10047', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('48', '38000', '1', '10048', NULL, '0', UTC_TIMESTAMP());

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('49', '10850', '1', '10049', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('50', '41300', '1', '10050', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('51', '41400', '1', '10051', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('52', '10200', '1', '10052', NULL, '0', UTC_TIMESTAMP());



INSERT INTO `eh_web_menu_privileges` VALUES ('1008', '10002', '10100', '论坛/公告', '1', '1', '论坛/公告 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1009', '10003', '10400', '广告管理', '1', '1', '广告管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1010', '10004', '10600', '活动管理', '1', '1', '活动管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1011', '10005', '10800', '新闻管理', '1', '1', '新闻管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1012', '10006', '11000', '一键推送', '1', '1', '一键推送 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1013', '10045', '10700', '路演直播', '1', '1', '路演直播 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1014', '10046', '10900', '行业动态', '1', '1', '行业动态 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1015', '10049', '10850', '园区报', '1', '1', '园区报 全部权限', '710');

INSERT INTO `eh_web_menu_privileges` VALUES ('1021', '10008', '20100', '物业报修', '1', '1', '物业报修 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1022', '10009', '20400', '物业缴费', '1', '1', '物业缴费 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1023', '10010', '20600', '品质核查', '1', '1', '品质核查 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1024', '10011', '20800', '设备巡检', '1', '1', '设备巡检 全部权限', '710');

INSERT INTO `eh_web_menu_privileges` VALUES ('1034', '10013', '30500', '项目信息', '1', '1', '项目信息 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1035', '10014', '31000', '楼栋管理', '1', '1', '楼栋管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1036', '10015', '32000', '门牌管理', '1', '1', '门牌管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1037', '10016', '34000', '用户管理', '1', '1', '用户管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1038', '10017', '35000', '用户认证', '1', '1', '用户认证 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1039', '10018', '37000', '客户资料', '1', '1', '客户资料 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1040', '10047', '33000', '企业管理', '1', '1', '企业管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1041', '10048', '38000', '业主管理', '1', '1', '业主管理 全部权限', '710');

INSERT INTO `eh_web_menu_privileges` VALUES ('1059', '10020', '40100', '招租管理', '1', '1', '招租管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1060', '10021', '40200', '工位预订', '1', '1', '工位预订 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1061', '10022', '40300', '服务热线', '1', '1', '服务热线 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1062', '10023', '40400', '资源预订', '1', '1', '资源预订 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1063', '10024', '40500', '服务联盟', '1', '1', '服务联盟 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1064', '10025', '40600', '创客空间', '1', '1', '创客空间 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1065', '10026', '40700', '结算管理', '1', '1', '结算管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1066', '10027', '40750', '运营统计', '1', '1', '运营统计 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1067', '10028', '40800', '停车缴费', '1', '1', '停车缴费 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1068', '10029', '40900', '车辆管理', '1', '1', '车辆管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1069', '10030', '41000', '大堂门禁', '1', '1', '大堂门禁 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1070', '10031', '41100', 'Wifi热点', '1', '1', 'Wifi热点 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1071', '10032', '41200', '一卡通', '1', '1', '一卡通 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1072', '10050', '41300', '场所预订', '1', '1', '场所预订 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1073', '10051', '41400', '服务预约', '1', '1', '服务预约 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1074', '10052', '10200', '园区简介', '1', '1', '园区简介 全部权限', '710');

INSERT INTO `eh_web_menu_privileges` VALUES ('1084', '10034', '50100', '组织架构', '1', '1', '组织架构 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1085', '10035', '50200', '岗位管理', '1', '1', '岗位管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1086', '10036', '50300', '职级管理', '1', '1', '职级管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1087', '10037', '50400', '人员管理', '1', '1', '人员管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1088', '10038', '50500', '认证管理', '1', '1', '认证管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1089', '10039', '50600', '考勤管理', '1', '1', '考勤管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1090', '10040', '50700', '视频会议', '1', '1', '视频会议 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1091', '10041', '50800', '公司门禁', '1', '1', '公司门禁 全部权限', '710');

INSERT INTO `eh_web_menu_privileges` VALUES ('1094', '10043', '60100', '管理员管理', '1', '1', '管理员管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1095', '10044', '60200', '业务授权', '1', '1', '业务授权 全部权限', '710');

delete from eh_web_menus;
INSERT INTO `eh_web_menus` VALUES ('10000', '信息发布', '0', 'fa fa-volume-up', null, '1', '2', '/10000', 'park', '100');
INSERT INTO `eh_web_menus` VALUES ('10100', '论坛/公告', '10000', null, 'forum_notice', '0', '2', '/10000/10100', 'park', '110');
INSERT INTO `eh_web_menus` VALUES ('10200', '园区简介', '10000', null, 'park-intro', '0', '2', '/10000/10200', 'park', '111');
INSERT INTO `eh_web_menus` VALUES ('10400', '广告管理', '10000', null, 'banner_management', '0', '2', '/10000/10400', 'park', '140');
INSERT INTO `eh_web_menus` VALUES ('10600', '活动管理', '10000', null, 'forum_activity', '0', '2', '/10000/10600', 'park', '160');
INSERT INTO `eh_web_menus` VALUES ('10700', '路演直播', '10000', null, 'road_show', '0', '2', '/10000/10700', 'park', '170');
INSERT INTO `eh_web_menus` VALUES ('10800', '新闻管理', '10000', null, 'news_management', '0', '2', '/10000/10800', 'park', '180');

INSERT INTO `eh_web_menus` VALUES ('10850', '园区报', '10000', null, null, '1', '2', '/10000/10850', 'park', '181');
INSERT INTO `eh_web_menus` VALUES ('10851', '园区报管理', '10850', null, 'park_epaper_management', '0', '2', '/10000/10850/10851', 'park', '182');
INSERT INTO `eh_web_menus` VALUES ('10852', '约稿须知', '10850', null, 'manuscripts_notice', '0', '2', '/10000/10850/10852', 'park', '183');

INSERT INTO `eh_web_menus` VALUES ('10900', '行业动态', '10000', null, 'industry_dynamics', '0', '2', '/10000/10900', 'park', '185');
INSERT INTO `eh_web_menus` VALUES ('11000', '一键推送', '10000', null, 'message_push', '0', '2', '/10000/11000', 'park', '190');

INSERT INTO `eh_web_menus` VALUES ('20000', '物业服务', '0', 'fa fa-coffee', null, '1', '2', '/20000', 'park', '200');
INSERT INTO `eh_web_menus` VALUES ('20100', '物业报修', '20000', null, null, '1', '2', '/20000/20100', 'park', '201');
INSERT INTO `eh_web_menus` VALUES ('20110', '全部任务', '20100', null, 'all_task', '0', '2', '/20000/20100/20110', 'park', '205');
INSERT INTO `eh_web_menus` VALUES ('20120', '我的任务', '20100', null, 'my_task', '0', '2', '/20000/20100/20120', 'park', '210');
INSERT INTO `eh_web_menus` VALUES ('20130', '统计', '20100', null, 'statistics', '0', '2', '/20000/20100/20130', 'park', '215');
INSERT INTO `eh_web_menus` VALUES ('20140', '任务列表', '20100', null, 'task_management_list', '0', '2', '/20000/20100/20140', 'park', '220');
INSERT INTO `eh_web_menus` VALUES ('20150', '服务录入', '20100', null, 'task_management_service_entry', '0', '2', '/20000/20100/20150', 'park', '225');
INSERT INTO `eh_web_menus` VALUES ('20160', '执行人员设置', '20100', null, 'executive_setting', '0', '2', '/20000/20100/20160', 'park', '230');
INSERT INTO `eh_web_menus` VALUES ('20170', '服务类型设置', '20100', null, 'service_type_setting', '0', '2', '/20000/20100/20170', 'park', '235');
INSERT INTO `eh_web_menus` VALUES ('20180', '分类设置', '20100', null, 'classify_setting', '0', '2', '/20000/20100/20180', 'park', '240');
INSERT INTO `eh_web_menus` VALUES ('20190', '统计', '20100', null, 'task_statistics', '0', '2', '/20000/20100/20190', 'park', '245');

INSERT INTO `eh_web_menus` VALUES ('20400', '物业缴费', '20000', null, null, '1', '2', '/20000/20400', 'park', '250');
INSERT INTO `eh_web_menus` VALUES ('20410', '缴费记录', '20400', null, 'property_fee_record', '0', '2', '/20000/20400/20410', 'park', '252');
INSERT INTO `eh_web_menus` VALUES ('20420', '设置', '20400', null, 'property_fee_config', '0', '2', '/20000/20400/20420', 'park', '255');


INSERT INTO `eh_web_menus` VALUES ('20600', '品质核查', '20000', null, null, '1', '2', '/20000/20600', 'park', '260');
INSERT INTO `eh_web_menus` VALUES ('20610', '类型管理', '20600', null, 'react:/type-management/type-list', '1', '2', '/20000/20600/20610', 'park', '261');
INSERT INTO `eh_web_menus` VALUES ('20620', '规范管理', '20600', null, 'react:/specification-management/specification-list', '1', '2', '/20000/20600/20620', 'park', '265');
INSERT INTO `eh_web_menus` VALUES ('20630', '标准管理', '20600', null, 'reference_standard', '0', '2', '/20000/20600/20630', 'park', '266');
INSERT INTO `eh_web_menus` VALUES ('20640', '标准审批', '20600', null, 'react:/standard-check/standard-check-list', '0', '2', '/20000/20600/20640', 'park', '267');
INSERT INTO `eh_web_menus` VALUES ('20650', '任务查询', '20600', null, 'task_list', '0', '2', '/20000/20600/20650', 'park', '268');

INSERT INTO `eh_web_menus` VALUES ('20660', '统计', '20600', null, null, '1', '2', '/20000/20600/20660', 'park', '269');
INSERT INTO `eh_web_menus` VALUES ('20661', '分数统计', '20660', null, 'react:/statistics-management/fractional-report', '0', '2', '/20000/20600/20660/20661', 'park', '270');
INSERT INTO `eh_web_menus` VALUES ('20662', '任务数统计', '20660', null, 'react:/statistics-management/task-report', '0', '2', '/20000/20600/20660/20662', 'park', '271');

INSERT INTO `eh_web_menus` VALUES ('20670', '修改记录', '20600', null, 'edit_record', '0', '2', '/20000/20600/20670', 'park', '272');

INSERT INTO `eh_web_menus` VALUES ('20800', '设备巡检', '20000', null, null, '1', '2', '/20000/20800', 'park', '280');
INSERT INTO `eh_web_menus` VALUES ('20810', '参考标准', '20800', null, null, '1', '2', '/20000/20800/20810', 'park', '281');
INSERT INTO `eh_web_menus` VALUES ('20811', '标准列表', '20810', null, 'equipment_inspection_standard_list', '0', '2', '/20000/20800/20810/20811', 'park', '282');
INSERT INTO `eh_web_menus` VALUES ('20812', '设备关联审批', '20810', null, 'equipment_inspection_check_attachment', '0', '2', '/20000/20800/20810/20812', 'park', '283');
INSERT INTO `eh_web_menus` VALUES ('20820', '设备台帐', '20800', null, null, '1', '2', '/20000/20800/20820', 'park', '284');
INSERT INTO `eh_web_menus` VALUES ('20821', '设备列表', '20820', null, 'equipment_inspection_equipment_list', '0', '2', '/20000/20800/20820/20821', 'park', '285');
INSERT INTO `eh_web_menus` VALUES ('20822', '备品备件', '20820', null, 'equipment_inspection_sparepart_list', '0', '2', '/20000/20800/20820/20822', 'park', '286');
INSERT INTO `eh_web_menus` VALUES ('20830', '任务列表', '20800', null, null, '1', '2', '/20000/20800/20830', 'park', '287');
INSERT INTO `eh_web_menus` VALUES ('20831', '任务列表', '20830', null, 'equipment_inspection_task_list', '0', '2', '/20000/20800/20830/20831', 'park', '288');
INSERT INTO `eh_web_menus` VALUES ('20840', '巡检项资料库管理', '20800', null, null, '1', '2', '/20000/20800/20840', 'park', '289');
INSERT INTO `eh_web_menus` VALUES ('20841', '巡检项设置', '20840', null, 'equipment_inspection_inspection_item_list', '0', '2', '/20000/20800/20840/20841', 'park', '290');


INSERT INTO `eh_web_menus` VALUES ('30000', '项目管理', '0', 'fa fa-building', null, '1', '2', '/30000', 'park', '300');
INSERT INTO `eh_web_menus` VALUES ('30500', '项目信息', '30000', null, 'react:/project-classification/projects', '0', '2', '/30000/30500', 'park', '305');
INSERT INTO `eh_web_menus` VALUES ('31000', '楼栋管理', '30000', null, 'building_management', '0', '2', '/30000/31000', 'park', '310');
INSERT INTO `eh_web_menus` VALUES ('32000', '门牌管理', '30000', null, 'apartment_statistics', '0', '2', '/30000/32000', 'park', '320');
INSERT INTO `eh_web_menus` VALUES ('33000', '企业管理', '30000', null, 'enterprise_management', '0', '2', '/30000/33000', 'park', '330');
INSERT INTO `eh_web_menus` VALUES ('34000', '用户管理', '30000', null, 'user_management', '0', '2', '/30000/34000', 'park', '340');
INSERT INTO `eh_web_menus` VALUES ('35000', '用户认证', '30000', null, 'user_identify', '0', '2', '/30000/35000', 'park', '350');
INSERT INTO `eh_web_menus` VALUES ('37000', '客户资料', '30000', null, 'customer_management', '0', '2', '/30000/37000', 'park', '370');
INSERT INTO `eh_web_menus` VALUES ('38000', '业主管理', '30000', null, 'apartment_info', '0', '2', '/30000/38000', 'park', '360');


INSERT INTO `eh_web_menus` VALUES ('40000', '运营服务', '0', 'fa fa-comment', null, '1', '2', '/40000', 'park', '400');

INSERT INTO `eh_web_menus` VALUES ('40100', '招租管理', '40000', null, null, '1', '2', '/40000/40100', 'park', '410');
INSERT INTO `eh_web_menus` VALUES ('40110', '招租管理', '40100', null, 'rent_manage', '0', '2', '/40000/40100/40110', 'park', '412');
INSERT INTO `eh_web_menus` VALUES ('40120', '入住申请', '40100', null, 'enter_apply', '0', '2', '/40000/40100/40120', 'park', '414');

INSERT INTO `eh_web_menus` VALUES ('40200', '工位预订', '40000', null, null, '1', '2', '/40000/40200', 'park', '420');
INSERT INTO `eh_web_menus` VALUES ('40210', '空间管理', '40200', null, 'project_management', '0', '2', '/40000/40200/40210', 'park', '424');
INSERT INTO `eh_web_menus` VALUES ('40220', '预订详情', '40200', null, 'project_detail', '0', '2', '/40000/40200/40220', 'park', '426');

INSERT INTO `eh_web_menus` VALUES ('40300', '服务热线', '40000', null, 'service_hotline', '0', '2', '/40000/40300', 'park', '430');

INSERT INTO `eh_web_menus` VALUES ('40400', '资源预订', '40000', null, null, '1', '2', '/40000/40400', 'park', '440');
INSERT INTO `eh_web_menus` VALUES ('40410', '默认参数', '40400', null, 'default_parameters', '0', '2', '/40000/40400/40410', 'park', '441');
INSERT INTO `eh_web_menus` VALUES ('40420', '资源发布', '40400', null, 'resource_publish', '0', '2', '/40000/40400/40420', 'park', '444');
INSERT INTO `eh_web_menus` VALUES ('40430', '预订详情', '40400', null, 'rental_info', '0', '2', '/40000/40400/40430', 'park', '446');
INSERT INTO `eh_web_menus` VALUES ('40440', '退款处理', '40400', null, 'refund_management', '0', '2', '/40000/40400/40440', 'park', '448');

INSERT INTO `eh_web_menus` VALUES ('40500', '服务联盟', '40000', null, null, '1', '2', '/40000/40500', 'park', '450');
INSERT INTO `eh_web_menus` VALUES ('40510', '类型管理', '40500', null, 'service_type_management', '0', '2', '/40000/40500/40510', 'park', '451');
INSERT INTO `eh_web_menus` VALUES ('40520', '机构管理', '40500', null, 'service_alliance', '0', '2', '/40000/40500/40520', 'park', '453');
INSERT INTO `eh_web_menus` VALUES ('40530', '消息推送设置', '40500', null, 'message_push_setting', '0', '2', '/40000/40500/40530', 'park', '455');
INSERT INTO `eh_web_menus` VALUES ('40540', '申请记录', '40500', null, 'apply_record', '0', '2', '/40000/40500/40540', 'park', '457');

INSERT INTO `eh_web_menus` VALUES ('40600', '创客空间', '40000', null, 'market_zone', '0', '2', '/40000/40600', 'park', '460');

INSERT INTO `eh_web_menus` VALUES ('40700', '结算管理', '40000', null, 'settlement_management', '1', '2', '/40000/40700', 'park', '462');

INSERT INTO `eh_web_menus` VALUES ('40750', '运营统计', '40000', null, 'dailyActive--dailyActive', '0', '2', '/40000/40750', 'park', '464');

INSERT INTO `eh_web_menus` VALUES ('40800', '停车缴费', '40000', null, null, '1', '2', '/40000/40800', 'park', '470');
INSERT INTO `eh_web_menus` VALUES ('40810', '充值项管理', '40800', null, 'park_setting', '0', '2', '/40000/40800/40810', 'park', '471');
INSERT INTO `eh_web_menus` VALUES ('40820', '活动规则', '40800', null, 'park_rules', '0', '2', '/40000/40800/40820', 'park', '472');
INSERT INTO `eh_web_menus` VALUES ('40830', '月卡申请', '40800', null, 'park_card', '0', '2', '/40000/40800/40830', 'park', '473');
INSERT INTO `eh_web_menus` VALUES ('40840', '缴费记录', '40800', null, 'park_recharge', '0', '2', '/40000/40800/40840', 'park', '474');

INSERT INTO `eh_web_menus` VALUES ('40900', '车辆管理', '40000', null, 'car_management', '0', '2', '/40000/40900', 'park', '476');

INSERT INTO `eh_web_menus` VALUES ('41000', '大堂门禁', '40000', null, null, '1', '2', '/40000/41000', 'park', '478');
INSERT INTO `eh_web_menus` VALUES ('41010', '门禁管理', '41000', null, 'access_manage', '0', '2', '/40000/41000/41010', 'park', '479');
INSERT INTO `eh_web_menus` VALUES ('41020', '版本管理', '41000', null, 'version_manage', '0', '2', '/40000/41000/41020', 'park', '480');
INSERT INTO `eh_web_menus` VALUES ('41030', '门禁分组', '41000', null, 'access_group', '0', '2', '/40000/41000/41030', 'park', '481');
INSERT INTO `eh_web_menus` VALUES ('41040', '用户授权', '41000', null, 'user_auth', '0', '2', '/40000/41000/41040', 'park', '482');
INSERT INTO `eh_web_menus` VALUES ('41050', '访客授权', '41000', null, 'visitor_auth', '0', '2', '/40000/41000/41050', 'park', '483');
INSERT INTO `eh_web_menus` VALUES ('41060', '门禁日志', '41000', null, 'access_log', '0', '2', '/40000/41000/41060', 'park', '484');

INSERT INTO `eh_web_menus` VALUES ('41100', 'Wifi热点', '40000', null, 'wifi_hotspot', '0', '2', '/40000/41100', 'park', '485');

INSERT INTO `eh_web_menus` VALUES ('41200', '一卡通', '40000', null, null, '1', '2', '/40000/43500', 'park', '486');
INSERT INTO `eh_web_menus` VALUES ('41210', '开卡用户', '41200', null, 'card_user', '0', '2', '/40000/41200/41210', 'park', '487');
INSERT INTO `eh_web_menus` VALUES ('41220', '充值记录', '41200', null, 'card_recharge_record', '0', '2', '/40000/41200/41220', 'park', '488');
INSERT INTO `eh_web_menus` VALUES ('41230', '消费记录', '41200', null, 'card_purchase_record', '0', '2', '/40000/41200/41230', 'park', '489');

INSERT INTO `eh_web_menus` VALUES ('41300', '场所预订', '40000', null, null, '1', '2', '/40000/41300', 'park', '490');
INSERT INTO `eh_web_menus` VALUES ('41310', '通用设置', '41300', null, 'rental_setting', '0', '2', '/40000/41300/41310', 'park', '491');
INSERT INTO `eh_web_menus` VALUES ('41320', '场所发布', '41300', null, 'rental_publish', '0', '2', '/40000/41300/41320', 'park', '492');
INSERT INTO `eh_web_menus` VALUES ('41330', '预订详情', '41300', null, 'rental_detail', '0', '2', '/40000/41300/41330', 'park', '493');

INSERT INTO `eh_web_menus` VALUES ('41400', '服务预约', '40000', null, null, '1', '2', '/40000/41400', 'park', '494');
INSERT INTO `eh_web_menus` VALUES ('41410', '通用设置', '41400', null, 'service_setting', '0', '2', '/40000/41400/41410', 'park', '495');
INSERT INTO `eh_web_menus` VALUES ('41420', '服务类型', '41400', null, 'service_type', '0', '2', '/40000/41400/41420', 'park', '496');
INSERT INTO `eh_web_menus` VALUES ('41430', '预约详情', '41400', null, 'service_detail', '0', '2', '/40000/41400/41430', 'park', '497');
INSERT INTO `eh_web_menus` VALUES ('41440', '数据统计', '41400', null, 'service_statistics', '0', '2', '/40000/41400/41440', 'park', '498');

INSERT INTO `eh_web_menus` VALUES ('50000', '内部管理', '0', 'fa fa-group', null, '1', '2', '/50000', 'park', '505');

INSERT INTO `eh_web_menus` VALUES ('50100', '组织架构', '50000', null, null, '1', '2', '/50000/50100', 'park', '510');
INSERT INTO `eh_web_menus` VALUES ('50110', '组织架构', '50100', null, 'react:/system-architect/architect-list', '0', '2', '/50000/50100/50110', 'park', '511');

INSERT INTO `eh_web_menus` VALUES ('50200', '岗位管理', '50000', null, null, '0', '2', '/50000/50200', 'park', '521');
INSERT INTO `eh_web_menus` VALUES ('50210', '通用岗位', '50200', null, 'react:/rank-management/general', '0', '2', '/50000/50200/50210', 'park', '522');
INSERT INTO `eh_web_menus` VALUES ('50220', '岗位管理', '50200', null, 'react:/rank-management/rank-list', '0', '2', '/50000/50200/50220', 'park', '523');

INSERT INTO `eh_web_menus` VALUES ('50300', '职级管理', '50000', null, 'react:/level-management/level-list', '0', '2', '/50000/50300', 'park', '530');

INSERT INTO `eh_web_menus` VALUES ('50400', '人员管理', '50000', null, 'react:/employee-management/employee-list', '0', '2', '/50000/50400', 'park', '540');

INSERT INTO `eh_web_menus` VALUES ('50500', '认证管理', '50000', null, 'user_check', '0', '2', '/50000/50500', 'park', '541');

INSERT INTO `eh_web_menus` VALUES ('50600', '考勤管理', '50000', null, null, '1', '2', '/50000/50600', 'park', '543');
INSERT INTO `eh_web_menus` VALUES ('50630', '考勤规则', '50600', null, null, '1', '2', '/50000/50600/50630', 'park', '554');
INSERT INTO `eh_web_menus` VALUES ('50631', '通用规则设置', '50630', null, 'punch--generalSetting', '0', '2', '/50000/50600/50630/50631', 'park', '555');
INSERT INTO `eh_web_menus` VALUES ('50632', '特殊个人设置', '50630', null, 'punch--personalSetting', '0', '2', '/50000/50600/50630/50632', 'park', '556');
INSERT INTO `eh_web_menus` VALUES ('50633', '请假类型设置', '50630', null, 'leave_setting', '0', '2', '/50000/50600/50630/50633', 'park', '556');
INSERT INTO `eh_web_menus` VALUES ('50640', '打卡详情', '50600', null, 'punch_detail', '1', '2', '/50000/50600/50640', 'park', '557');
INSERT INTO `eh_web_menus` VALUES ('50650', '申请处理', '50600', null, null, '1', '2', '/50000/50600/50650', 'park', '559');
INSERT INTO `eh_web_menus` VALUES ('50651', '异常申请', '50650', null, 'abnormal_apply', '0', '2', '/50000/50600/50650/50651', 'park', '560');
INSERT INTO `eh_web_menus` VALUES ('50652', '请假申请', '50650', null, 'leave_apply', '0', '2', '/50000/50600/50650/50652', 'park', '561');
INSERT INTO `eh_web_menus` VALUES ('50653', '加班申请', '50650', null, 'punch--overTimeApply', '0', '2', '/50000/50600/50650/50653', 'park', '561');
INSERT INTO `eh_web_menus` VALUES ('50660', '考勤统计', '50600', null, 'punch_statistics', '1', '2', '/50000/50600/50660', 'park', '562');

INSERT INTO `eh_web_menus` VALUES ('50700', '视频会议', '50000', null, null, '1', '2', '/50000/50700', 'park', '570');
INSERT INTO `eh_web_menus` VALUES ('50710', '账号管理', '50700', null, 'account_manage', '0', '2', '/50000/50700/50710', 'park', '571');
INSERT INTO `eh_web_menus` VALUES ('50720', '我的订单', '50700', null, 'video_detail', '0', '2', '/50000/50700/50720', 'park', '572');
INSERT INTO `eh_web_menus` VALUES ('50730', '会议官网', '50700', null, 'url:http://meeting.zuolin.com', '0', '2', '/50000/50700/50730', 'park', '573');

INSERT INTO `eh_web_menus` VALUES ('50800', '公司门禁', '50000', null, null, '1', '2', '/50000/50800', 'park', '580');
INSERT INTO `eh_web_menus` VALUES ('50810', '门禁管理', '50800', null, 'access_manage_inside', '0', '2', '/50000/50800/50810', 'park', '581');
INSERT INTO `eh_web_menus` VALUES ('50820', '版本管理', '50800', null, 'version_manage_inside', '0', '2', '/50000/50800/50820', 'park', '582');
INSERT INTO `eh_web_menus` VALUES ('50830', '门禁分组', '50800', null, 'access_group_inside', '0', '2', '/50000/50800/50830', 'park', '583');
INSERT INTO `eh_web_menus` VALUES ('50840', '用户授权', '50800', null, 'user_auth_inside', '0', '2', '/50000/50800/50840', 'park', '584');
INSERT INTO `eh_web_menus` VALUES ('50850', '访客授权', '50800', null, 'visitor_auth_inside', '0', '2', '/50000/50800/50850', 'park', '585');
INSERT INTO `eh_web_menus` VALUES ('50860', '门禁日志', '50800', null, 'access_log_inside', '0', '2', '/50000/50800/50860', 'park', '586');

INSERT INTO `eh_web_menus` VALUES ('60000', '系统管理', '0', 'fa fa-group', null, '1', '2', '/60000', 'park', '600');

INSERT INTO `eh_web_menus` VALUES ('60100', '管理员管理', '60000', null, 'react:/admin-management/administrator', '0', '2', '/60000/60100', 'park', '610');
INSERT INTO `eh_web_menus` VALUES ('60200', '业务授权', '60000', null, 'react:/bussiness-authorization/department', '0', '2', '/60000/60200', 'park', '620');


INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10001, '0', '信息发布 管理员', '信息发布 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10002, '0', '论坛/公告 管理员', '论坛/公告 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10003, '0', '广告管理 管理员', '广告管理 业务模块权限', NULL);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10004, '0', '活动管理 管理员', '活动管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10005, '0', '新闻管理 管理员', '新闻管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10006, '0', '一键推送 管理员', '一键推送 业务模块权限', NULL);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10007, '0', '物业服务 管理员', '物业服务 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10008, '0', '物业报修 管理员', '物业报修 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10009, '0', '物业缴费 管理员', '物业缴费 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10010, '0', '品质核查 管理员', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10011, '0', '设备巡检 管理员', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10012, '0', '项目管理 管理员', '项目管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10013, '0', '项目信息 管理员', '项目信息 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10014, '0', '楼栋管理 管理员', '楼栋管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10015, '0', '门牌管理 管理员', '门牌管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10016, '0', '用户管理 管理员', '用户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10017, '0', '用户认证 管理员', '用户认证 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10018, '0', '客户资料 管理员', '客户资料 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10019, '0', '运营服务 管理员', '运营服务 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10020, '0', '招租管理 管理员', '招租管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10021, '0', '工位预订 管理员', '工位预订 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10022, '0', '服务热线 管理员', '服务热线 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10023, '0', '资源预订 管理员', '资源预订 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10024, '0', '服务联盟 管理员', '服务联盟 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10025, '0', '创客空间 管理员', '创客空间 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10026, '0', '结算管理 管理员', '结算管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10027, '0', '运营统计 管理员', '运营统计 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10028, '0', '停车缴费 管理员', '停车缴费 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10029, '0', '车辆管理 管理员', '车辆管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10030, '0', '大堂门禁 管理员', '大堂门禁 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10031, '0', 'Wifi热点 管理员', 'Wifi热点 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10032, '0', '一卡通 管理员', '一卡通 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10033, '0', '内部管理 管理员', '内部管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10034, '0', '组织架构 管理员', '组织架构 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10035, '0', '岗位管理 管理员', '岗位管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10036, '0', '职级管理 管理员', '职级管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10037, '0', '人员管理 管理员', '人员管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10038, '0', '认证管理 管理员', '认证管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10039, '0', '考勤管理 管理员', '考勤管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10040, '0', '视频会议 管理员', '视频会议 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10041, '0', '公司门禁 管理员', '公司门禁 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10042, '0', '系统管理 管理员', '系统管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10043, '0', '管理员管理 管理员', '管理员管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10044, '0', '业务授权 管理员', '业务授权 业务模块权限', NULL);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10045, '0', '路演直播 管理员', '路演直播 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10046, '0', '行业动态 管理员', '行业动态 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10047, '0', '企业管理 管理员', '企业管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10048, '0', '业主管理 管理员', '业主管理 业务模块权限', NULL);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10049, '0', '园区报 管理员', '园区报 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10050, '0', '场所预订 管理员', '场所预订 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10051, '0', '服务预约 管理员', '服务预约 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10052, '0', '园区简介 管理员', '园区简介 业务模块权限', NULL);

delete from eh_web_menu_privileges;
INSERT INTO `eh_web_menu_privileges` VALUES ('1', '10001', '10000', '发帖', '1', '1', '发公告和任务贴', '10');

INSERT INTO `eh_web_menu_privileges` VALUES ('2', '10006', '11000', '推送消息', '1', '1', '推送消息', '20');

INSERT INTO `eh_web_menu_privileges` VALUES ('3', '340', '10900', '行业动态', '1', '1', '行业动态  全部权限', '16');
INSERT INTO `eh_web_menu_privileges` VALUES ('4', '315', '10700', '路演直播', '1', '1', '路演直播  全部权限', '16');

-- 任务管理：
INSERT INTO `eh_web_menu_privileges` VALUES ('5', '306', '20110', '接受，拒绝任务', '0', '1', '接受，拒绝任务', '30');
INSERT INTO `eh_web_menu_privileges` VALUES ('6', '307', '20110', '任务处理', '0', '1', '修改任务状态', '40');
INSERT INTO `eh_web_menu_privileges` VALUES ('7', '303', '20110', '查询报修任务帖子', '1', '1', '查询报修任务帖子', '50');
INSERT INTO `eh_web_menu_privileges` VALUES ('8', '304', '20110', '查看求助任务帖子', '1', '1', '查看求助任务帖子', '60');
INSERT INTO `eh_web_menu_privileges` VALUES ('9', '305', '20110', '查看所有任务帖子', '1', '1', '查看所有任务帖子', '70');

INSERT INTO `eh_web_menu_privileges` VALUES ('10', '306', '20120', '接受，拒绝任务', '0', '1', '接受，拒绝任务', '79');
INSERT INTO `eh_web_menu_privileges` VALUES ('11', '303', '20120', '查询报修任务帖子', '1', '1', '查询报修任务帖子', '81');
INSERT INTO `eh_web_menu_privileges` VALUES ('12', '305', '20120', '查看所有任务帖子', '1', '1', '查看所有任务帖子', '83');
INSERT INTO `eh_web_menu_privileges` VALUES ('13', '304', '20120', '查看求助任务帖子', '1', '1', '查看求助任务帖子', '82');
INSERT INTO `eh_web_menu_privileges` VALUES ('14', '307', '20120', '任务处理', '0', '1', '修改任务状态', '80');

INSERT INTO `eh_web_menu_privileges` VALUES ('15', '309', '20130', '任务统计', '1', '1', '任务统计', '88');

INSERT INTO `eh_web_menu_privileges` VALUES ('16', '904', '20140', '查看所有任务', '1', '1', '任务管理 查看所有任务', '603');
INSERT INTO `eh_web_menu_privileges` VALUES ('17', '805', '20140', '查看我的任务', '1', '1', '任务管理 查看我的任务', '603');
INSERT INTO `eh_web_menu_privileges` VALUES ('18', '331', '20140', '分派任务', '0', '1', '分派任务 全部权限', '604');
INSERT INTO `eh_web_menu_privileges` VALUES ('19', '332', '20140', '完成任务', '0', '1', '完成任务 全部权限', '605');
INSERT INTO `eh_web_menu_privileges` VALUES ('20', '333', '20140', '关闭任务', '0', '1', '关闭任务 全部权限', '606');
INSERT INTO `eh_web_menu_privileges` VALUES ('21', '920', '20140', '完成回访', '0', '1', '完成回访 全部权限', '606');
INSERT INTO `eh_web_menu_privileges` VALUES ('22', '936', '20150', '服务录入', '1', '1', '任务管理 服务录入', '606');
INSERT INTO `eh_web_menu_privileges` VALUES ('23', '937', '20160', '执行人员设置', '1', '1', '任务管理 执行人员设置', '606');
INSERT INTO `eh_web_menu_privileges` VALUES ('24', '905', '20170', '服务类型设置', '1', '1', '服务类型设置 全部权限', '604');
INSERT INTO `eh_web_menu_privileges` VALUES ('25', '906', '20180', '分类设置', '1', '1', '分类设置 全部权限', '605');
INSERT INTO `eh_web_menu_privileges` VALUES ('26', '907', '20190', '统计', '1', '1', '统计 全部权限', '606');

-- 门派管理：
INSERT INTO `eh_web_menu_privileges` VALUES ('27', '408', '32000', '门牌管理', '1', '1', '门牌管理操作', '100');
-- 企业管理：
INSERT INTO `eh_web_menu_privileges` VALUES ('28', '401', '33000', '企业列表', '1', '1', '查询企业列表', '110');
INSERT INTO `eh_web_menu_privileges` VALUES ('29', '402', '33000', '增加企业', '0', '1', '增加企业', '120');
INSERT INTO `eh_web_menu_privileges` VALUES ('30', '403', '33000', '删除企业', '0', '1', '删除企业', '130');
INSERT INTO `eh_web_menu_privileges` VALUES ('31', '404', '33000', '修改企业', '0', '1', '修改企业', '140');
INSERT INTO `eh_web_menu_privileges` VALUES ('32', '405', '33000', '开通企业管理员', '0', '1', '开通企业管理员', '150');
-- 用户管理：
INSERT INTO `eh_web_menu_privileges` VALUES ('33', '406', '34000', '用户列表', '1', '1', '查询用户列表', '160');
-- 停车：
INSERT INTO `eh_web_menu_privileges` VALUES ('34', '556', '40810', '充值项管理列表', '1', '1', '查询充值项管理列表', '170');
INSERT INTO `eh_web_menu_privileges` VALUES ('35', '512', '40810', '增加停车充值项', '0', '1', '增加停车充值项', '180');
INSERT INTO `eh_web_menu_privileges` VALUES ('36', '513', '40810', '删除停车充值项', '0', '1', '删除停车充值项', '190');
INSERT INTO `eh_web_menu_privileges` VALUES ('37', '557', '40820', '设置活动规则', '1', '1', '设置活动规则', '200');
INSERT INTO `eh_web_menu_privileges` VALUES ('38', '515', '40840', '缴费记录', '1', '1', '查询缴费记录', '210');
INSERT INTO `eh_web_menu_privileges` VALUES ('39', '516', '40830', '月卡申请记录', '1', '1', '查询月卡申请记录', '220');
INSERT INTO `eh_web_menu_privileges` VALUES ('41', '514', '40830', '设置月卡申请参数', '0', '1', '设置月卡申请参数', '230');
INSERT INTO `eh_web_menu_privileges` VALUES ('42', '517', '40830', '发放月卡', '0', '1', '发放月卡', '240');
INSERT INTO `eh_web_menu_privileges` VALUES ('43', '518', '40830', '领取月卡', '0', '1', '领取月卡', '250');
-- 场所预订：
INSERT INTO `eh_web_menu_privileges` VALUES ('44', '526', '41310', '设置通用设置', '1', '1', '设置通用设置', '260');
INSERT INTO `eh_web_menu_privileges` VALUES ('45', '527', '41320', '查询场所', '1', '1', '查询场所', '270');
INSERT INTO `eh_web_menu_privileges` VALUES ('46', '528', '41320', '添加/修改具体场所', '0', '1', '添加/修改具体场所', '280');
INSERT INTO `eh_web_menu_privileges` VALUES ('47', '530', '41320', '添加具体场所预订规则', '0', '1', '添加具体场所预订规则', '290');
INSERT INTO `eh_web_menu_privileges` VALUES ('48', '531', '41320', '删除场所规则', '0', '1', '删除场所规则', '300');
INSERT INTO `eh_web_menu_privileges` VALUES ('49', '532', '41320', '查询具体场所商品信息', '0', '1', '查询具体场所商品信息', '310');
INSERT INTO `eh_web_menu_privileges` VALUES ('50', '533', '41320', '添加具体场所商品信息', '0', '1', '添加具体场所商品信息', '320');
INSERT INTO `eh_web_menu_privileges` VALUES ('51', '534', '41320', '删除具体场所商品信息', '0', '1', '删除具体场所商品信息', '330');
INSERT INTO `eh_web_menu_privileges` VALUES ('52', '535', '41320', '删除具体场所', '0', '1', '删除具体场所', '340');
INSERT INTO `eh_web_menu_privileges` VALUES ('53', '529', '41330', '查询预定详情', '1', '1', '查询预定详情', '260');
-- 招租管理：
INSERT INTO `eh_web_menu_privileges` VALUES ('54', '536', '40110', '招租信息列表', '1', '1', '查看招租信息列表', '260');
INSERT INTO `eh_web_menu_privileges` VALUES ('55', '539', '40110', '增删改招租信息', '0', '1', '增删改招租信息', '340');
INSERT INTO `eh_web_menu_privileges` VALUES ('56', '537', '40120', '查看入驻申请', '1', '1', '查看入驻申请', '260');
INSERT INTO `eh_web_menu_privileges` VALUES ('57', '538', '40120', '删除入驻申请', '0', '1', '删除入驻申请', '340');
-- 服务热线：
INSERT INTO `eh_web_menu_privileges` VALUES ('58', '554', '40300', '查看服务热线', '1', '1', '查看服务热线信息', '350');
INSERT INTO `eh_web_menu_privileges` VALUES ('59', '555', '40300', '增加删除服务热线', '0', '1', '增加删除服务热线', '360');
-- 服务联盟:
INSERT INTO `eh_web_menu_privileges` VALUES ('60', '540', '40500', '查看服务联盟', '1', '1', '查看服务联盟信息', '370');
INSERT INTO `eh_web_menu_privileges` VALUES ('61', '541', '40500', '增删改服务联盟信息', '0', '1', '增删改服务联盟信息', '380');
INSERT INTO `eh_web_menu_privileges` VALUES ('62', '540', '40510', '类型管理', '1', '1', '备品备件 全部权限', '603');
INSERT INTO `eh_web_menu_privileges` VALUES ('63', '541', '40520', '机构管理', '1', '1', '任务列表 全部权限', '604');
INSERT INTO `eh_web_menu_privileges` VALUES ('64', '450', '40530', '消息推送设置', '1', '1', '消息推送设置  全部权限', '606');
INSERT INTO `eh_web_menu_privileges` VALUES ('65', '451', '40540', '申请记录', '1', '1', '申请记录  全部权限', '607');
-- 创客空间：
INSERT INTO `eh_web_menu_privileges` VALUES ('66', '542', '40600', '查看创客空间', '1', '1', '查看创客空间信息', '390');
INSERT INTO `eh_web_menu_privileges` VALUES ('67', '543', '40600', '删除修改创客空间', '0', '1', '删除修改创客空间', '400');
-- 组织架构：
INSERT INTO `eh_web_menu_privileges` VALUES ('68', '600', '50110', '组织架构列表', '1', '1', '查看组织架构列表', '410');
INSERT INTO `eh_web_menu_privileges` VALUES ('69', '601', '50110', '增删改组织架构', '0', '1', '增删改组织架构', '420');
-- 管理员管理：
INSERT INTO `eh_web_menu_privileges` VALUES ('70', '604', '60100', '查看管理员信息', '1', '1', '查看管理员信息', '430');
INSERT INTO `eh_web_menu_privileges` VALUES ('71', '605', '60100', '管理员的增删改', '0', '1', '管理员的增删改', '440');

-- INSERT INTO `eh_web_menu_privileges` VALUES ('50', '606', '52200', '查看角色与权限', '1', '1', '角色与权限查看', '450');
-- INSERT INTO `eh_web_menu_privileges` VALUES ('51', '607', '52200', '角色与权限的增删改', '0', '1', '角色与权限的增删改', '460');
-- 人员管理：
INSERT INTO `eh_web_menu_privileges` VALUES ('72', '608', '50400', '成员列表', '1', '1', '成员列表查看', '470');
INSERT INTO `eh_web_menu_privileges` VALUES ('73', '609', '50400', '成员的增删改', '0', '1', '成员的增删改', '480');
INSERT INTO `eh_web_menu_privileges` VALUES ('74', '610', '50400', '设置角色', '0', '1', '设置角色', '490');
-- 视频会议：
INSERT INTO `eh_web_menu_privileges` VALUES ('75', '548', '50710', '查看账号', '1', '1', '查看账号', '610');
INSERT INTO `eh_web_menu_privileges` VALUES ('76', '549', '50710', '分配账号', '0', '1', '分配账号', '620');
INSERT INTO `eh_web_menu_privileges` VALUES ('77', '550', '50710', '更换用户', '0', '1', '更换用户', '630');
INSERT INTO `eh_web_menu_privileges` VALUES ('78', '551', '50720', '我的订单', '1', '1', '查看我的订单', '640');
INSERT INTO `eh_web_menu_privileges` VALUES ('79', '552', '50720', '查看账号信息', '0', '1', '查看账号信息', '650');
INSERT INTO `eh_web_menu_privileges` VALUES ('80', '553', '50720', '查看开票信息', '0', '1', '查看开票信息', '660');
INSERT INTO `eh_web_menu_privileges` VALUES ('81', '560', '50730', '会议官网', '1', '1', '查看会议官网', '641');

-- 楼栋管理：
INSERT INTO `eh_web_menu_privileges` VALUES ('82', '391', '31000', '楼栋的增删改', '0', '1', '楼栋的增删改', '91');
INSERT INTO `eh_web_menu_privileges` VALUES ('83', '390', '31000', '楼栋列表', '1', '1', '楼栋列表', '90');

-- INSERT INTO `eh_web_menu_privileges` VALUES ('87', '616', '53100', '管辖范围管理', '1', '1', '管辖范围管理，包括管辖的小区楼栋查询，添加，删除', '545');
-- 认证管理：
INSERT INTO `eh_web_menu_privileges` VALUES ('84', '621', '50500', '审批认证', '0', '1', '审批同意，拒绝', '770');
INSERT INTO `eh_web_menu_privileges` VALUES ('85', '620', '50500', '审批列表', '1', '1', '绩效统计 全部权限', '760');
-- 项目信息：
INSERT INTO `eh_web_menu_privileges` VALUES ('86', '380', '30500', '查看区信息', '1', '1', '查看区信息', '88');
INSERT INTO `eh_web_menu_privileges` VALUES ('87', '381', '30500', '修改区信息', '0', '1', '修改区信息', '89');
-- 客户资料：
INSERT INTO `eh_web_menu_privileges` VALUES ('88', '420', '37000', '客户资料', '1', '1', '客户资料  全部权限', '200');
-- 车辆管理：
INSERT INTO `eh_web_menu_privileges` VALUES ('89', '421', '40900', '车辆管理', '1', '1', '车辆管理  全部权限', '201');
-- 用户认证：
INSERT INTO `eh_web_menu_privileges` VALUES ('90', '409', '35000', '用户认证', '1', '1', '用户认证', '165');
INSERT INTO `eh_web_menu_privileges` VALUES ('91', '410', '35000', '认证审核', '0', '1', '拒绝，同意', '166');
-- 业主管理：
INSERT INTO `eh_web_menu_privileges` VALUES ('92', '411', '40900', '业主管理', '1', '1', '业主管理 全部权限', '167');
-- 大堂门禁：
INSERT INTO `eh_web_menu_privileges` VALUES ('93', '720', '41010', '门禁管理', '1', '1', '门禁管理', '345');
INSERT INTO `eh_web_menu_privileges` VALUES ('94', '721', '41040', '用户授权', '1', '1', '用户授权', '346');
INSERT INTO `eh_web_menu_privileges` VALUES ('95', '722', '41050', '访客授权', '1', '1', '访客授权', '347');
INSERT INTO `eh_web_menu_privileges` VALUES ('96', '723', '41020', '版本管理', '1', '1', '版本管理', '346');
INSERT INTO `eh_web_menu_privileges` VALUES ('97', '724', '41030', '门禁分组', '1', '1', '门禁分组', '346');
INSERT INTO `eh_web_menu_privileges` VALUES ('98', '731', '41060', '门禁日志', '1', '1', '公共 门禁日志', '348');

-- 服务预约：
INSERT INTO `eh_web_menu_privileges` VALUES ('99', '519', '41410', '查询通用设置', '1', '1', '查询通用设置', '348');
INSERT INTO `eh_web_menu_privileges` VALUES ('100', '520', '41410', '通用设置', '0', '1', '通用设置', '348');
INSERT INTO `eh_web_menu_privileges` VALUES ('101', '521', '41420', '查询服务类型', '1', '1', '查询服务类型', '349');
INSERT INTO `eh_web_menu_privileges` VALUES ('102', '522', '41420', '服务类型的增删改', '0', '1', '服务类型的增删改', '349');
INSERT INTO `eh_web_menu_privileges` VALUES ('103', '523', '41430', '查询预约详情', '1', '1', '查询预约详情', '350');
INSERT INTO `eh_web_menu_privileges` VALUES ('104', '525', '41440', '查询数据统计', '1', '1', '数据统计', '350');

-- 公司门禁：
INSERT INTO `eh_web_menu_privileges` VALUES ('105', '725', '50810', '门禁管理', '1', '1', '门禁管理', '780');
INSERT INTO `eh_web_menu_privileges` VALUES ('106', '726', '50840', '用户授权', '1', '1', '用户授权', '810');
INSERT INTO `eh_web_menu_privileges` VALUES ('107', '727', '50850', '访客授权', '1', '1', '访客授权', '820');
INSERT INTO `eh_web_menu_privileges` VALUES ('108', '728', '50820', '版本管理', '1', '1', '版本管理', '790');
INSERT INTO `eh_web_menu_privileges` VALUES ('109', '729', '50830', '门禁分组', '1', '1', '门禁分组', '800');
INSERT INTO `eh_web_menu_privileges` VALUES ('110', '730', '50860', '门禁日志', '1', '1', '门禁日志', '801');

-- wifi热点：
INSERT INTO `eh_web_menu_privileges` VALUES ('111', '715', '41100', 'Wifi热点', '1', '1', 'Wifi热点', '401');
-- 物业缴费：
INSERT INTO `eh_web_menu_privileges` VALUES ('112', '713', '20410', '缴费记录', '1', '1', '缴费记录', '341');
INSERT INTO `eh_web_menu_privileges` VALUES ('113', '714', '20420', '设置', '1', '1', '设置', '342');
-- 活动管理：
INSERT INTO `eh_web_menu_privileges` VALUES ('114', '200', '10600', '活动管理', '1', '1', '查询活动贴以及发布活动', '15');
INSERT INTO `eh_web_menu_privileges` VALUES ('115', '310', '10600', '官方活动', '0', '1', '官方活动', '16');
-- 新闻管理：
INSERT INTO `eh_web_menu_privileges` VALUES ('116', '220', '10800', '新闻管理', '1', '1', '新闻管理  全部权限', '16');
-- 园区简介：
INSERT INTO `eh_web_menu_privileges` VALUES ('117', '570', '10200', '园区简介', '1', '1', '园区简介', '350');

-- 一卡通：
INSERT INTO `eh_web_menu_privileges` VALUES ('118', '760', '41210', '开卡用户', '1', '1', '开卡用户 全部权限', '345');
INSERT INTO `eh_web_menu_privileges` VALUES ('119', '761', '41220', '充值记录', '1', '1', '充值记录 全部权限', '346');
INSERT INTO `eh_web_menu_privileges` VALUES ('120', '762', '41230', '消费记录', '1', '1', '消费记录 全部权限', '347');
-- 运营统计：
INSERT INTO `eh_web_menu_privileges` VALUES ('121', '716', '40750', '日活统计', '1', '1', '日活统计  全部权限', '16');

-- 工位预定
INSERT INTO `eh_web_menu_privileges` VALUES ('122', '558', '40210', '空间管理', '1', '1', '空间管理 全部权限', '341');
INSERT INTO `eh_web_menu_privileges` VALUES ('145', '559', '40220', '预订详情', '1', '1', '预订详情 全部权限', '342');
-- 资源预订：
INSERT INTO `eh_web_menu_privileges` VALUES ('123', '565', '40410', '默认参数', '1', '1', '默认参数  全部权限', '343');
INSERT INTO `eh_web_menu_privileges` VALUES ('124', '566', '40420', '资源发布', '1', '1', '资源发布 全部权限', '344');
INSERT INTO `eh_web_menu_privileges` VALUES ('125', '567', '40430', '预定详情', '1', '1', '预定详情  全部权限', '345');
INSERT INTO `eh_web_menu_privileges` VALUES ('126', '568', '40440', '退款处理', '1', '1', '退款处理 全部权限', '346');
-- 结算管理：
INSERT INTO `eh_web_menu_privileges` VALUES ('127', '770', '40700', '结算管理', '1', '1', '结算管理', '349');
-- 园区报
INSERT INTO `eh_web_menu_privileges` VALUES ('128', '780', '41510', '园区报管理', '1', '1', '园区报管理  全部权限', '346');
INSERT INTO `eh_web_menu_privileges` VALUES ('129', '781', '41520', '约稿须知', '1', '1', '约稿须知 全部权限', '347');
-- 广告管理：
INSERT INTO `eh_web_menu_privileges` VALUES ('130', '830', '10400', '广告管理', '1', '1', '广告管理  全部权限', '577');

-- 设备巡检：
INSERT INTO `eh_web_menu_privileges` VALUES ('131', '800', '20811', '标准列表', '1', '1', '标准列表  全部权限', '600');
INSERT INTO `eh_web_menu_privileges` VALUES ('132', '801', '20812', '设备关联审批', '1', '1', '设备关联审批 全部权限', '601');

INSERT INTO `eh_web_menu_privileges` VALUES ('133', '802', '20821', '设备列表', '1', '1', '设备列表 全部权限', '602');
INSERT INTO `eh_web_menu_privileges` VALUES ('134', '803', '20822', '备品备件', '1', '1', '备品备件 全部权限', '603');

INSERT INTO `eh_web_menu_privileges` VALUES ('135', '804', '20831', '任务列表', '1', '1', '任务列表 全部权限', '604');
INSERT INTO `eh_web_menu_privileges` VALUES ('136', '950', '20840', '巡检项资料库管理', '1', '1', '巡检项资料库管理', '801');
INSERT INTO `eh_web_menu_privileges` VALUES ('137', '951', '20841', '巡检项设置', '1', '1', '巡检项设置', '641');
-- 品质核查：
INSERT INTO `eh_web_menu_privileges` VALUES ('138', '751', '20611', '行为规范', '1', '1', '行为规范 全部权限', '700');
INSERT INTO `eh_web_menu_privileges` VALUES ('139', '752', '20612', '绩效统计', '1', '1', '绩效统计 全部权限', '710');

INSERT INTO `eh_web_menu_privileges` VALUES ('140', '753', '20621', '参考标准', '1', '1', '参考标准 全部权限', '720');
INSERT INTO `eh_web_menu_privileges` VALUES ('141', '754', '20622', '类型管理', '1', '1', '类型管理 全部权限', '730');

INSERT INTO `eh_web_menu_privileges` VALUES ('142', '755', '20631', '任务列表', '1', '1', '任务列表 全部权限', '740');
INSERT INTO `eh_web_menu_privileges` VALUES ('143', '756', '20632', '任务审阅', '1', '1', '任务审阅 全部权限', '750');

INSERT INTO `eh_web_menu_privileges` VALUES ('144', '757', '20640', '修改记录', '1', '1', '修改记录 全部权限', '760');

-- 考勤管理：打卡 菜单改变
delete from eh_acl_privileges where id >= 790 and id <= 794;
delete from eh_acl_privileges where id >= 822 and id <= 823;
-- INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (790, '0', '加班申请', '加班申请 全部权限', NULL);

-- INSERT INTO `eh_web_menu_privileges` VALUES ('154', '795', '50630', '考勤规则', '1', '1', '考勤规则 全部权限', '572'); 
-- INSERT INTO `eh_web_menu_privileges` VALUES ('155', '795', '50631', '通用规则设置', '1', '1', '通用规则设置 全部权限', '310');
-- INSERT INTO `eh_web_menu_privileges` VALUES ('156', '796', '50632', '特殊个人设置', '1', '1', '特殊个人设置 全部权限', '320');
-- INSERT INTO `eh_web_menu_privileges` VALUES ('153', '821', '50633', '请假类型设置', '1', '1', '请假类型设置 全部权限', '320');

-- INSERT INTO `eh_web_menu_privileges` VALUES ('157', '797', '50640', '打卡详情', '1', '1', '打卡详情 全部权限', '573'); 

-- INSERT INTO `eh_web_menu_privileges` VALUES ('159', '798', '50650', '申请处理', '1', '1', '申请处理 全部权限', '574'); 
-- INSERT INTO `eh_web_menu_privileges` VALUES ('160', '798', '50651', '异常申请', '1', '1', '异常申请 全部权限', '510');
-- INSERT INTO `eh_web_menu_privileges` VALUES ('161', '799', '50652', '请假申请', '1', '1', '请假申请 全部权限', '520');
-- INSERT INTO `eh_web_menu_privileges` VALUES ('158', '790', '50653', '加班申请', '1', '1', '加班申请 全部权限', '521');

-- INSERT INTO `eh_web_menu_privileges` VALUES ('162', '820', '50660', '考勤统计', '1', '1', '考勤统计 全部权限', '575'); 

-- INSERT INTO `eh_web_menu_privileges` VALUES ('163', '10053', '50210', '通用岗位', '1', '1', '通用岗位 全部权限', '520');
-- INSERT INTO `eh_web_menu_privileges` VALUES ('164', '10054', '50220', '岗位管理', '1', '1', '岗位管理 全部权限', '521');
-- 新增通用岗位权限
-- INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10053, '0', '通用岗位', '通用岗位 全部权限', NULL);
-- INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10054, '0', '岗位管理', '岗位管理 全部权限', NULL);


select * from eh_acl_privileges where id >= 10001 and id <= 10052;
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `id`, 1001,0,1,now() FROM `eh_acl_privileges` WHERE id >= 10001 and id <= 10052;

INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_service_module_privileges` WHERE module_id in (select id from eh_service_modules where type = 1);

-- SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
-- INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`) 
--	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '790', '1001', '0', '1', '2016-10-25 10:18:59');
-- INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`) 
--	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '10053', '1001', '0', '1', '2016-10-25 10:18:59');
-- INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`) 
--	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '790', '1005', '0', '1', '2016-10-25 10:18:59');
-- INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`) 
--	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '10053', '1005', '0', '1', '2016-10-25 10:18:59');


-- 添加 业务权限下面的子菜单
SET @web_menu_privilegel_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
-- INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) 
-- SELECT ((@web_menu_privilegel_id := @web_menu_privilegel_id + 1), '10008', `id` , '物业报修', '1', '1', '物业报修 全部权限', '710') FROM `eh_web_menus` WHERE `path` like '20100/%';
	
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) 
SELECT (@web_menu_privilegel_id := @web_menu_privilegel_id + 1), privilege_id, mm.id , tm.name, '1', '1', tm.discription, tm.sort_num FROM
(select t.* FROM eh_web_menus m
JOIN (SELECT * from eh_web_menu_privileges where privilege_id >= 10001 and privilege_id <= 10052 and privilege_id not in (10001, 10007, 10012, 10019, 10033, 10042)) t
ON m.id = t.menu_id
) tm join eh_web_menus mm on tm.menu_id = SUBSTRING_INDEX(SUBSTRING_INDEX(mm.path,'/',3), '/', -1) where mm.id not in (SELECT menu_id from eh_web_menu_privileges where privilege_id>=10000);


-- 新增俱乐部菜单
INSERT INTO `eh_web_menus` VALUES ('10750', '俱乐部', '10000', null, 'groups', '0', '2', '/10000/10750', 'park', '180');
INSERT INTO `eh_web_menus` VALUES ('10751', '俱乐部管理', '10750', null, 'groups_management', '0', '2', '/10000/10750/10751', 'park', '181');
INSERT INTO `eh_web_menus` VALUES ('10752', '审核俱乐部', '10750', null, 'audit_groups', '0', '2', '/10000/10750/10752', 'park', '182');

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('10750', '俱乐部', '10000', '/10000/10750', '0', '2', '2', '0', UTC_TIMESTAMP());

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('53', '10750', '1', '10055', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('54', '20100', '0', '904', '新的物业报修权限', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('55', '20100', '0', '805', '新的物业报修权限', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('56', '20100', '0', '331', '新的物业报修权限', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('57', '20100', '0', '332', '新的物业报修权限', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('58', '20100', '0', '333', '新的物业报修权限', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('59', '20100', '0', '920', '新的物业报修权限', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('60', '20100', '0', '303', '老的物业报修权限', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('61', '20100', '0', '304', '老的物业报修权限', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('62', '20100', '0', '305', '老的物业报修权限', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('63', '20100', '0', '306', '老的物业报修权限', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('64', '20100', '0', '307', '老的物业报修权限', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('65', '10600', '0', '310', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('66', '10100', '0', '200', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('67', '41000', '0', '720', NULL, '0', UTC_TIMESTAMP());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10055, '0', '俱乐部 管理员', '俱乐部 业务模块权限', NULL);
-- INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10056, '0', '俱乐部管理', '俱乐部管理 全部权限', NULL);
-- INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10057, '0', '审核俱乐部', '审核俱乐部 全部权限', NULL);

INSERT INTO `eh_web_menu_privileges` VALUES ('166', '10055', '10750', '俱乐部管理', '1', '1', '俱乐部管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('167', '10055', '10751', '俱乐部管理', '1', '1', '俱乐部管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('168', '10055', '10752', '审核俱乐部', '1', '1', '审核俱乐部 全部权限', '710');


SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`) 
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '10055', '1001', '0', '1', UTC_TIMESTAMP());
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`) 
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '10055', '1005', '0', '1', UTC_TIMESTAMP());
	
DELETE FROM `eh_acls` WHERE `privilege_id` in (604, 605) AND `role_id` = 1005;


SET @service_module_scopes_id = 0;
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) 
SELECT (@service_module_scopes_id := @service_module_scopes_id + 1), 999992,id,null,null,null,0,2 FROM `eh_service_modules` WHERE `type` = 0 AND `parent_id` <> 0;


