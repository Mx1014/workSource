-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等
-- AUTHOR: 杨崇鑫  20180720
-- REMARK: content图片程序升级，① 从本版中的content二进制更新到正式环境中  ② 把allowOriginToGet = * 加到 config.ini 配置文件中的 system 区域下

-- AUTHOR: ryan  20180807
-- REMARK: 执行 /archives/cleanRedundantArchivesDetails 接口(可能速度有点慢，但可重复执行)

-- AUTHOR: jiarui  20180807
-- REMARK: 执行search 下脚本 enter_meter.sh
-- 执行 /energy/syncEnergyMeterIndex 接口(可能速度有点慢，但可重复执行)

-- AUTHOR: 唐岑 2018年8月17日15:13:14
-- REMARK: 1、执行接口/community/caculateAllCommunityArea（该接口计算时间非常长）
--         2、执行接口/community/caculateAllBuildingArea（该接口计算时间非常长）


-- AUTHOR: 丁建民 2018年8月17日15:13:14 (仓库管理) 更新一下es结构，并调用相关接口同步
-- REMARK: 1、更新 warehouse_material.sh warehouse.sh warehouse_stock_log.sh warehouse_stock.sh es的结构
--         2、 同步  /warehouse/syncWarehouseIndex
--         3、同步/warehouse/syncWarehouseMaterialCategoryIndex
--         4、同步/warehouse/syncWarehouseMaterialsIndex
--         5、同步/warehouse/syncWarehouseRequestMaterialIndex
--         6、同步/warehouse/syncWarehouseStockIndex
--         7、同步/warehouse/syncWarehouseStockLogIndex

-- AUTHOR: 丁建民 2018年8月17日15:13:14 (能耗管理) 更新一下es结构，并调用相关接口同步
-- REMARK: 1、更新 energy_meter.sh es上的结构
--         2、同步 /energy/syncEnergyMeterIndex

-- AUTHOR: dengs 2018年8月17日
-- REMARK: 1、备份表eh_parking_lots
--         2、调用接口/parking/initFuncLists

-- AUTHOR: 严军 2018年8月17日
-- REMARK: 1、备份表eh_service_modules 和 eh_portal_items


-- --------------------- SECTION END ---------------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 杨崇鑫
-- REMARK: 根据域空间判断是否展示能耗数据，测试环境初始化鼎峰汇支持展示能耗数据
INSERT INTO `eh_service_module_functions`(`id`, `module_id`, `privilege_id`, `explain`) VALUES (101, 20400, 0, '账单显示用量');

-- AUTHOR: jiarui
-- REMARK: 物业巡检的早期菜单数据导致权限项目显示不全
UPDATE  eh_service_modules SET `status` = 0 WHERE path LIKE '/200/20000/20800/%' AND `level` = 5;

-- AUTHOR: 梁燕龙
-- REMARK: 活动报名人数不足最低限制人数自动取消活动消息推送
INSERT INTO eh_locale_templates(`scope`, `code`,`locale`, `description`, `text`)
VALUES( 'activity.notification', 18, 'zh_CN', '活动报名人数不足最低限制人数，活动取消', '您报名的活动「${subject}」由于未达到最低人数，已被取消。');
INSERT INTO eh_locale_templates(`scope`, `code`,`locale`, `description`, `text`)
VALUES( 'activity.notification', 19, 'zh_CN', '活动报名人数不足最低限制人数，活动取消', '您报名的活动「${subject}」由于未达到最低人数，已被取消，报名费用将在三个工作日内退回您的账户上。');
INSERT INTO eh_locale_templates(`scope`, `code`,`locale`, `description`, `text`)
VALUES( 'activity.notification', 20, 'zh_CN', '活动报名人数不足最低限制人数，活动取消', '您发起的活动「${subject}」由于未达到最低人数，已被自动取消。');
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`) VALUES ('activity',28,'zh_CN','活动取消通知');
UPDATE eh_locale_strings SET text = '来晚啦，活动已删除' WHERE scope = 'forum' and code = 10006;
-- AUTHOR: 梁燕龙
-- REMARK: 活动消息推送文案修改。
UPDATE eh_locale_templates SET text = '${userName}报名了您发起的活动「${postName}」' WHERE scope = 'activity.notification' and code = 1;
UPDATE eh_locale_templates SET text = '${userName}取消报名了您发起的活动「${postName}」' WHERE scope = 'activity.notification' and code = 2;
UPDATE eh_locale_templates SET text = '您报名的活动「${postName}」已被管理员通过' WHERE scope = 'activity.notification' and code = 3;
UPDATE eh_locale_templates SET text = '很抱歉通知您：您报名的活动「${title}」因故取消。
更多活动敬请继续关注。' WHERE scope = 'activity.notification' and code = 5;
UPDATE eh_locale_templates SET text = '您报名的活动 「${title}」 还有 ${time}就要开始了。' WHERE scope = 'activity.notification' and code = 6;
UPDATE eh_locale_templates SET text = '${userName}报名了活动「${postName}」，请尽快确认。' WHERE scope = 'activity.notification' and code = 8;
UPDATE eh_locale_templates SET text = '您参加的活动「${postName}」的主题已被发起方改成「${newPostName}」。' WHERE scope = 'activity.notification' and code = 11;
UPDATE eh_locale_templates SET text = '您参加的活动「${postName}」的时间已被发起方改成「${startTime}~${endTime}」。' WHERE scope = 'activity.notification' and code = 12;
UPDATE eh_locale_templates SET text = '您参加的活动「${postName}」的地点已被发起方改成「${address}」。' WHERE scope = 'activity.notification' and code = 13;
UPDATE eh_locale_templates SET text = '您参加的活动「${postName}」被发起方修改，详情如下：主题被改成「${newPostName}」、时间被改成「${startTime}~${endTime}」。' WHERE scope = 'activity.notification' and code = 14;
UPDATE eh_locale_templates SET text = '您参加的活动「${postName}」被发起方修改，详情如下：主题被改成「${newPostName}」、地点被改成「${address}」。' WHERE scope = 'activity.notification' and code = 15;
UPDATE eh_locale_templates SET text = '您参加的活动「${postName}」被发起方修改，详情如下：时间被改成「${startTime}~${endTime}」、地点被改成「${address}」。' WHERE scope = 'activity.notification' and code = 16;
UPDATE eh_locale_templates SET text = '您参加的活动「${postName}」被发起方修改，详情如下：主题被改成「${newPostName}」、时间被改成「${startTime}~${endTime}」、地点被改成「${address}」。' WHERE scope = 'activity.notification' and code = 17;

-- AUTHOR: jiarui
-- REMARK: 物业巡检离线包版本更新
UPDATE eh_version_urls SET download_url = replace(download_url,'1-0-1','1-0-2') WHERE app_name = '物业巡检';
UPDATE eh_version_urls SET info_url = replace(info_url,'1-0-1','1-0-2') where app_name = '物业巡检';
UPDATE eh_version_urls SET target_version = '1.0.2' WHERE app_name = '物业巡检';

-- AUTHOR: dengs
-- REMARK: 访客管理1.1 移动端管理权限
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('41850', '移动端管理', '41800', '/200/20000/41800/41850', '1', '4', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
update eh_service_modules SET  path='/200/20000/41800/41810',level=4 WHERE id = 41810;
update eh_service_modules SET  path='/200/20000/41800/41840',level=4 WHERE id = 41840;

set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4180041850, '0', '园区访客 移动端管理权限', '园区访客 移动端管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '41850', '0', 4180041850, '移动端管理权限', '0', now());

SET @homeurl = (select `value` from eh_configurations WHERE `name`='home.url' AND namespace_id = 0 limit 1);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`,`access_control_type`, `menu_auth_flag`, `category`) VALUES ('52200', '企业访客管理', '50000', '/100/50000/52200', '1', '3', '2', '220', now(), CONCAT('{"url":"',@homeurl,'/visitor-management/build/index.html?ns=%s&appId=%s&ownerType=enterprise#/home#sign_suffix"}'), '13', now(), '0', '0', '0', '0', 'community_control','1', '1', 'module');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`,`access_control_type`, `menu_auth_flag`, `category`) VALUES ('42100', '园区访客管理', '20000', '/200/20000/42100', '1', '3', '2', '210', now(), CONCAT('{"url":"',@homeurl,'/visitor-management/build/index.html?ns=%s&appId=%s&ownerType=community#/home#sign_suffix"}'), '13', now(), '0', '0', '0', '0', 'community_control','1', '1', 'module');

update eh_service_modules SET instance_config = REPLACE(instance_config,' ','') WHERE id = 52100;
update eh_service_module_apps SET instance_config = REPLACE(instance_config,' ','') WHERE module_id = 52100;

-- AUTHOR: xq.tian  20180725
-- REMARK: 工作流 2.7
SET @eh_locale_templates_id = (SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO eh_locale_templates (id, scope, code, locale, description, text, namespace_id)
  VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'flow', 20008, 'zh_CN', '子业务流程进行中', '${serviceName} 进行中', 0);
INSERT INTO eh_locale_templates (id, scope, code, locale, description, text, namespace_id)
  VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'flow', 20009, 'zh_CN', '子流程创建成功，点击此处查看父流程详情。', '子流程创建成功，点击此处查看父流程详情', 0);
INSERT INTO eh_locale_templates (id, scope, code, locale, description, text, namespace_id)
  VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'flow', 20010, 'zh_CN', '${serviceName} 已完成', '${serviceName} 已完成', 0);
INSERT INTO eh_locale_templates (id, scope, code, locale, description, text, namespace_id)
  VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'flow', 20011, 'zh_CN', '${serviceName} 已终止', '${serviceName} 已终止', 0);
INSERT INTO eh_locale_templates (id, scope, code, locale, description, text, namespace_id)
  VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'flow', 20012, 'zh_CN', '子流程循环层级过多，流程已终止，详情请联系管理员', '子流程循环层级过多，流程已终止，详情请联系管理员', 0);

SET @eh_locale_strings_id = (SELECT MAX(id) FROM eh_locale_strings);
INSERT INTO eh_locale_strings (id, scope, code, locale, text)
  VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'flow', '100025', 'zh_CN', '子流程异常，请检查设置');
INSERT INTO eh_locale_strings (id, scope, code, locale, text)
  VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'flow', '100026', 'zh_CN', '请先发布新版本后再启用');
INSERT INTO eh_locale_strings (id, scope, code, locale, text)
  VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'flow', '100027', 'zh_CN', '当前工作流未被修改，请修改后发布新版本');


-- AUTHOR: 黄良铭  20180815
-- REMARK: 超级管理员删除提示
SET @b_id = (SELECT IFNULL(MAX(id),1) FROM eh_locale_templates);
INSERT INTO eh_locale_templates(id ,scope ,CODE ,locale ,description ,TEXT,namespace_id)
VALUES(@b_id:= @b_id +1 , 'organization.notification',22,'zh_CN','删除超级管理员给当前超级管理员发送的消息模板' ,  '你在${organizationName}的超级管理员身份已被移除',0);


-- AUTHOR: jun.yan  20180813
-- REMARK: 修改菜单目录
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('16300000', '支付结算管理', '16000000', NULL, NULL, '1', '2', '/16000000/16300000', 'zuolin', '90', NULL, '2', 'system', 'classify', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('16400000', '营销管理', '16000000', NULL, NULL, '1', '2', '/16000000/16400000', 'zuolin', '100', NULL, '2', 'system', 'classify', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('55000000', '支付结算管理', '40000040', NULL, NULL, '1', '2', '/40000040/55000000', 'park', '70', NULL, '2', 'system', 'classify', '2');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('56000000', '营销管理', '40000040', NULL, NULL, '1', '2', '/40000040/56000000', 'park', '80', NULL, '2', 'system', 'classify', '2');

UPDATE eh_web_menus SET parent_id = 16300000, path = '/16000000/16300000/16070000', `name` = '收款账户'  WHERE id = 16070000;
UPDATE eh_web_menus SET parent_id = 55000000, path = '/40000040/55000000/52000000', `name` = '收款账户'  WHERE id = 52000000;

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`) VALUES ('140000', '支付结算管理', '200', '/200/140000', '1', '2', '2', '100', '2018-08-14 14:01:29', NULL, NULL, '2018-08-14 14:01:39', '0', '0', '0', '0', '', '1', '1', 'classify');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`) VALUES ('170000', '营销管理', '200', '/200/170000', '1', '2', '2', '110', '2018-08-14 14:01:33', NULL, NULL, '2018-08-14 14:01:42', '0', '0', '0', '0', '', '1', '1', 'classify');

UPDATE eh_service_modules SET `name` = '收款账户', parent_id = '140000', path = '/200/140000' WHERE id = 58000;
UPDATE eh_service_module_apps SET `name` = '收款账户' WHERE module_id = 58000;


-- AUTHOR: 杨崇鑫
-- REMARK: 新增企业账单菜单
set @module_id=20500; -- 模块Id 41900（家声分配的）
set @data_type='public-transfer';-- 前端发给你的页面跳转链接
set @module_parent_parent_id=100;-- select * from eh_service_modules where name='企业访客'
set @module_parent_id=50000; -- select * from eh_service_modules where name='物业缴费'
set @path=CONCAT("/", @module_parent_parent_id,"/",@module_parent_id,"/",@module_id); -- 如：/100/50000/52100
set @bill_module_id=(select max(id) + 1 from eh_service_modules);-- 账单管理标签页的id
set @bill_path=CONCAT(@path,"/",@bill_module_id); -- 如：/100/50000/52100/101
set @order_module_id=(select max(id) + 2 from eh_service_modules);-- 交易明细标签页的id
set @order_path=CONCAT(@path,"/",@order_module_id); -- 如：/100/50000/52100/102
-- 左邻后台菜单路径（三层）
set @zuolin_menu_id=(select max(id) + 1 from eh_web_menus);-- 如：16032200
set @zuolin_menu_parent_parent_id=23000000;-- select * from eh_web_menus where name='企业办公业务'
set @zuolin_menu_parent_id=23010000; -- select * from eh_web_menus where name='OA管理' and type='zuolin'
set @zuolin_menu_path=CONCAT("/",@zuolin_menu_parent_parent_id,"/",@zuolin_menu_parent_id,"/",@zuolin_menu_id);-- 如：/23000000/23010000/XXXX
-- 园区后台菜单路径（三层）
-- set @park_menu_id=(select max(id) + 2 from eh_web_menus);-- 如：16032200
-- set @park_menu_parent_parent_id=40000010;-- select * from eh_web_menus where name='企业办公' and type='park'
-- set @park_menu_parent_id=53000000; -- select * from eh_web_menus where name='OA管理' and type='park'
-- set @park_menu_path=CONCAT("/",@park_menu_parent_parent_id,"/",@park_menu_parent_id,"/",@park_menu_id);-- 如：/40000010/53000000/XXX
-- 普通公司后台菜单路径（三层）
set @organization_menu_id=(select max(id) + 3 from eh_web_menus);-- 如：16032200
set @organization_menu_parent_parent_id=70000010;-- select * from eh_web_menus where name='企业办公' and type='organization'
set @organization_menu_parent_id=77000000; -- select * from eh_web_menus where name='OA管理' and type='organization'
set @organization_menu_path=CONCAT("/",@organization_menu_parent_parent_id,"/",@organization_menu_parent_id,"/",@organization_menu_id);-- 如：/40000010/53000000/XXX
-- 新增模块 eh_service_modules
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `update_time`, `operator_uid`, `creator_uid`, `multiple_flag`, `module_control_type`,`category`) 
VALUES (@module_id, '企业账单', @module_parent_id, @path, '1', '3', '2', '0', UTC_TIMESTAMP(), UTC_TIMESTAMP(), '0', '0', '0', 'community_control', 'module');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `category`) 
VALUES (@bill_module_id, '账单管理', @module_id, @bill_path, '1', '4', '2', '0', UTC_TIMESTAMP(), '', NULL, UTC_TIMESTAMP(), '0', '0', '', '0', 'community_control','subModule');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `category`) 
VALUES (@order_module_id, '交易明细', @module_id, @order_path, '1', '4', '2', '0', UTC_TIMESTAMP(), '', NULL, UTC_TIMESTAMP(), '0', '0', '', '0', 'community_control','subModule');
-- 新增模块菜单 eh_web_menus
-- 左邻后台（levle=3)
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) 
VALUES (@zuolin_menu_id, '企业账单', @zuolin_menu_parent_id, NULL, @data_type, 1, 2, @zuolin_menu_path, 'zuolin', 220, @module_id, 3, 'system', 'module', NULL);
-- 园区（levle=3)
-- INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) 
-- VALUES (@park_menu_id, '企业账单', @park_menu_parent_id, NULL, @data_type, 1, 2, @park_menu_path, 'park', 220, @module_id, 3, 'system', 'module', NULL);
-- 普通公司（levle=3)
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) 
VALUES (@organization_menu_id, '企业账单', @organization_menu_parent_id, NULL, @data_type, 1, 2, @organization_menu_path, 'organization', 220, @module_id, 3, 'system', 'module', NULL);

-- AUTHOR: 张智伟
-- REMARK: 企业公告分享提示文案
SET @locale_string_id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_string_id := @locale_string_id + 1), 'enterprise.notice', '10000', 'zh_CN', '未设置公告分享链接');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_string_id := @locale_string_id + 1), 'enterprise.notice', '10001', 'zh_CN', '该公告设置了保密');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_string_id := @locale_string_id + 1), 'enterprise.notice', '10002', 'zh_CN', '该公告已被删除');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_string_id := @locale_string_id + 1), 'enterprise.notice', '10003', 'zh_CN', '分享链接已失效');

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('enterprise.notice.share.url', '/announcement/build/index.html?ns=%s&noticeToken=%s', '企业公告分享uri', '0', NULL, '1');

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-29760: 考勤5.0 - 请假规则 将旧版本无用的数据置为删除状态
UPDATE eh_approval_categories SET status=0 WHERE namespace_id<>0;

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-29760: 考勤5.0 - 请假规则 新增请假单位等字段
UPDATE eh_approval_categories SET owner_type='organization',time_unit='HOUR',time_step=0.5,status=3,remainder_flag=0,default_order=1 WHERE approval_type=1 AND category_name='事假' and namespace_id=0;
UPDATE eh_approval_categories SET owner_type='organization', time_unit='HOUR',time_step=0.5,status=3,remainder_flag=0,default_order=2 WHERE approval_type=1 AND category_name='病假' and namespace_id=0;
UPDATE eh_approval_categories SET owner_type='organization', time_unit='DAY',time_step=0.5,hander_type='ANNUAL_LEAVE',remainder_flag=2,default_order=3 WHERE approval_type=1 AND category_name='年假' and namespace_id=0;
UPDATE eh_approval_categories SET owner_type='organization', time_unit='DAY',time_step=0.5,hander_type='WORKING_DAY_OFF',remainder_flag=2,default_order=4 WHERE approval_type=1 AND category_name='调休' and namespace_id=0;
UPDATE eh_approval_categories SET owner_type='organization', time_unit='DAY',time_step=0.5,remainder_flag=0,default_order=5 WHERE approval_type=1 AND category_name='婚假' and namespace_id=0;
UPDATE eh_approval_categories SET owner_type='organization', time_unit='DAY',time_step=0.5,remainder_flag=0,default_order=6 WHERE approval_type=1 AND category_name='产假' and namespace_id=0;
UPDATE eh_approval_categories SET owner_type='organization', time_unit='DAY',time_step=0.5,remainder_flag=0,default_order=7 WHERE approval_type=1 AND category_name='陪产假' and namespace_id=0;
UPDATE eh_approval_categories SET owner_type='organization', time_unit='DAY',time_step=0.5,remainder_flag=0,default_order=8 WHERE approval_type=1 AND category_name='丧假' and namespace_id=0;
UPDATE eh_approval_categories SET owner_type='organization', time_unit='DAY',time_step=0.5,remainder_flag=0,default_order=9 WHERE approval_type=1 AND category_name='工伤假' and namespace_id=0;
UPDATE eh_approval_categories SET owner_type='organization', time_unit='DAY',time_step=0.5,remainder_flag=0,default_order=10 WHERE approval_type=1 AND category_name='路途假' and namespace_id=0;

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-29760: 考勤5.0 - 请假规则 新增四种请假类型
SET @lastest_id = IFNULL((SELECT MAX(id) FROM `eh_approval_categories`), 1);
INSERT INTO eh_approval_categories(id,namespace_id,owner_type,owner_id,approval_type,category_name,time_unit,time_step,remainder_flag,status,default_order,creator_uid,create_time)
VALUE ((@lastest_id := @lastest_id + 1),0,'organization',0,1,'哺乳假','DAY',0.5,0,1,11,0,NOW());
INSERT INTO eh_approval_categories(id,namespace_id,owner_type,owner_id,approval_type,category_name,time_unit,time_step,remainder_flag,status,default_order,creator_uid,create_time)
VALUE ((@lastest_id := @lastest_id + 1),0,'organization',0,1,'探亲假','DAY',0.5,0,1,12,0,NOW());
INSERT INTO eh_approval_categories(id,namespace_id,owner_type,owner_id,approval_type,category_name,time_unit,time_step,remainder_flag,status,default_order,creator_uid,create_time)
VALUE ((@lastest_id := @lastest_id + 1),0,'organization',0,1,'看护假','DAY',0.5,0,1,13,0,NOW());
INSERT INTO eh_approval_categories(id,namespace_id,owner_type,owner_id,approval_type,category_name,time_unit,time_step,remainder_flag,status,default_order,creator_uid,create_time)
VALUE ((@lastest_id := @lastest_id + 1),0,'organization',0,1,'产检假','DAY',0.5,0,1,14,0,NOW());

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-29760: 考勤5.0 - 请假规则 请假类型相关操作提示文案信息
INSERT INTO eh_locale_strings(scope,code,locale,text)
SELECT r.scope,r.code,r.locale,r.text FROM(
SELECT 'approval' AS scope,10030 AS code,'zh_CN' AS locale,'该假期类型不存在' AS text UNION ALL
SELECT 'approval' AS scope,10031 AS code,'zh_CN' AS locale,'该假期类型不可禁用' AS text UNION ALL
SELECT 'approval' AS scope,10032 AS code,'zh_CN' AS locale,'假期类型不支持删除操作' AS text UNION ALL
SELECT 'approval' AS scope,10033 AS code,'zh_CN' AS locale,'请假表单数据不完整' AS text UNION ALL
SELECT 'approval' AS scope,10034 AS code,'zh_CN' AS locale,'该请假类型未开启，请联系管理员' AS text UNION ALL
SELECT 'approval' AS scope,10035 AS code,'zh_CN' AS locale,'余额不足' AS text UNION ALL
SELECT 'approval.tip.info' AS scope,10003 AS code,'zh_CN' AS locale,'请假申请已取消' AS text UNION ALL
SELECT 'approval.tip.info' AS scope,10004 AS code,'zh_CN' AS locale,'请假申请被驳回' AS text UNION ALL
SELECT 'approval.tip.info' AS scope,10005 AS code,'zh_CN' AS locale,'员工发起请假申请' AS text
)r LEFT JOIN eh_locale_strings s ON r.scope=s.scope AND r.code=s.code AND r.locale=s.locale
WHERE s.id IS NULL;

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-29760: 考勤5.0 - 请假规则 请假类型余额提示文案信息
SET @max_template_id = IFNULL((SELECT MAX(`id`) FROM `eh_locale_templates`),1);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUE (@max_template_id:=@max_template_id+1, 'approval.tip.info', 10000, 'zh_CN', '$假期类型名称$：最小请假0.5$请假单位$','${categoryName}：最小请假${timeStep}${timeUnit}。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUE (@max_template_id:=@max_template_id+1, 'approval.tip.info', 10001, 'zh_CN', '$假期类型名称$：最小请假0.5$请假单位$，剩余 $余额$','${categoryName}：最小请假${timeStep}${timeUnit}，剩余${remainCountDisplay}。', 0);

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-29760: 考勤5.0 - 请假规则 初始化假期余额列表已请年假总计和已请调休总计
UPDATE eh_punch_vacation_balances b INNER JOIN
(
SELECT d.namespace_id,r.enterprise_id,r.user_id,d.id AS detail_id,
    SUM((CASE ac.category_name
        WHEN '年假' THEN r.duration_day
        ELSE 0
    END)) AS annual_leave_history_count,
    SUM((CASE ac.category_name
        WHEN '调休' THEN r.duration_day
        ELSE 0
    END)) AS overtime_compensation_history_count
FROM eh_punch_exception_requests r
INNER JOIN eh_approval_categories ac ON r.category_id = ac.id
INNER JOIN eh_organization_member_details d ON r.user_id = d.target_id AND r.enterprise_id = d.organization_id
WHERE r.status = 1 AND ac.namespace_id = 0 AND ac.owner_id = 0 AND ac.category_name IN ('年假' , '调休')
GROUP BY r.enterprise_id , r.user_id
)t ON t.namespace_id=b.namespace_id AND t.enterprise_id=b.owner_id AND t.user_id=b.user_id
SET b.annual_leave_history_count=t.annual_leave_history_count,b.overtime_compensation_history_count=t.overtime_compensation_history_count;

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-29760: 考勤5.0 - 请假规则 初始化假期余额列表已请年假总计和已请调休总计
SET @lastest_id = IFNULL((SELECT MAX(id) FROM `eh_punch_vacation_balances`), 1);
INSERT INTO eh_punch_vacation_balances(id,namespace_id,owner_id,owner_type,user_id,detail_id,annual_leave_balance,overtime_compensation_balance,annual_leave_history_count,overtime_compensation_history_count,creator_uid,create_time,operator_uid,update_time)
SELECT t.id,t.namespace_id,t.enterprise_id,'organization' as owner_type,t.user_id,t.detail_id,0 AS annual_leave_balance,0 AS overtime_compensation_balance,t.annual_leave_history_count,t.overtime_compensation_history_count,0 AS creator_uid,NOW() AS create_time,0 AS operator_uid,NOW() update_time FROM
(
SELECT (@lastest_id := @lastest_id + 1) AS id,d.namespace_id,r.enterprise_id,r.user_id,d.id AS detail_id,
    SUM((CASE ac.category_name
        WHEN '年假' THEN r.duration_day
        ELSE 0
    END)) AS annual_leave_history_count,
    SUM((CASE ac.category_name
        WHEN '调休' THEN r.duration_day
        ELSE 0
    END)) AS overtime_compensation_history_count
FROM eh_punch_exception_requests r
INNER JOIN eh_approval_categories ac ON r.category_id = ac.id
INNER JOIN eh_organization_member_details d ON r.user_id = d.target_id AND r.enterprise_id = d.organization_id
WHERE r.status = 1 AND ac.namespace_id = 0 AND ac.owner_id = 0 AND ac.category_name IN ('年假' , '调休')
GROUP BY r.enterprise_id , r.user_id
)t LEFT JOIN eh_punch_vacation_balances b ON t.namespace_id=b.namespace_id AND t.enterprise_id=b.owner_id AND t.user_id=b.user_id
WHERE b.id IS NULL;


-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-29760: 考勤5.0 - 报表相关 初始化旷工天数
UPDATE eh_punch_statistics SET absence_count=(LENGTH(status_list) - LENGTH( REPLACE(status_list,'旷工','')))/6
WHERE status_list IS NOT NULL;

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-32471: 考勤6.0 - 加班设置 考勤加班规则不同加班类型的提示文案初始化
SET @max_locale_id = IFNULL((SELECT MAX(`id`) FROM `eh_locale_strings`),1);
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'overtime.rule.tip.info', -1, 'zh_CN', '未设置打卡规则');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'overtime.rule.tip.info', 0, 'zh_CN', '未开启');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'overtime.rule.tip.info', 1, 'zh_CN', '需要申请和打卡，时长按打卡时间计算，但不能超过申请的时间');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'overtime.rule.tip.info', 2, 'zh_CN', '需要申请，时长按申请单时间计算');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'overtime.rule.tip.info', 3, 'zh_CN', '不需要申请，时长按打卡时间计算');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'overtime.rule.tip.info', 4, 'zh_CN', '工作日加班：');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'overtime.rule.tip.info', 5, 'zh_CN', '休息日/节假日加班：');

-- AUTHOR: 吴寒 20180813
-- REMARK: ISSUE-32471: 考勤6.0 - 加班设置 初始化2018剩余节假日
UPDATE eh_punch_holidays SET legal_flag = 1 WHERE rule_date IN ('2018-01-01','2018-02-16','2018-02-17','2018-02-18','2018-04-05','2018-05-01','2018-06-18','2018-09-24','2018-10-01','2018-10-02','2018-10-03');

-- AUTHOR: 吴寒 20180813
-- REMARK: ISSUE-33645: 考勤7.0 - 统计相关 申请统计标题和详情的template
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES('punch.exception.statistic',1,'zh_CN','申请统计项列表-请假-内容','${beginYear}年${beginMonth}月${beginDate}日 ${beginTime} 至 ${endYear}年${endMonth}月${endDate}日 ${endTime}','0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES('punch.exception.statistic',2,'zh_CN','申请统计项列表-外出、出差、加班-标题','${day}天${hour}小时 ','0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES('punch.exception.statistic',3,'zh_CN','申请统计项列表-外出、出差、加班-内容','${beginYear}年${beginMonth}月${beginDate}日 ${beginTime} 至 ${endYear}年${endMonth}月${endDate}日 ${endTime}','0');

-- AUTHOR: 吴寒 20180813
-- REMARK: ISSUE-33645: 考勤7.0 - 统计相关 审批类型的的一些locale_string
SET @max_locale_id = IFNULL((SELECT MAX(`id`) FROM `eh_locale_strings`),1);
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'general.approval.attribute', 'ASK_FOR_LEAVE', 'zh_CN', '请假');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'general.approval.attribute', 'BUSINESS_TRIP', 'zh_CN', '出差');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'general.approval.attribute', 'OVERTIME', 'zh_CN', '加班');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'general.approval.attribute', 'GO_OUT', 'zh_CN', '外出');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'time.unit', 'date', 'zh_CN', '日');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'punch.punchType', '0', 'zh_CN', '上班');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'punch.punchType', '1', 'zh_CN', '下班');

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-33645: 考勤7.0 - 统计相关 考勤状态统计项名称定义
INSERT INTO eh_locale_strings(scope,code,locale,text)
SELECT r.scope,r.code,r.locale,r.text FROM(
SELECT 'punch.status' AS scope,18 AS code,'zh_CN' AS locale,'迟到且缺卡' AS text UNION ALL
SELECT 'punch.status' AS scope,19 AS code,'zh_CN' AS locale,'缺卡' AS text UNION ALL
SELECT 'punch.status' AS scope,-1 AS code,'zh_CN' AS locale,'未设置规则' AS text UNION ALL
SELECT 'punch.status' AS scope,-2 AS code,'zh_CN' AS locale,'未安排班次' AS text UNION ALL
SELECT 'punch.status' AS scope,-3 AS code,'zh_CN' AS locale,'未打卡' AS text UNION ALL
SELECT 'punch' AS scope,10300 AS code,'zh_CN' AS locale,'月报数据还没有生成' AS text UNION ALL

SELECT 'PunchStatusStatisticsItemName' AS scope,1 AS code,'zh_CN' AS locale,'未到' AS text UNION ALL
SELECT 'PunchStatusStatisticsItemName' AS scope,2 AS code,'zh_CN' AS locale,'迟到' AS text UNION ALL
SELECT 'PunchStatusStatisticsItemName' AS scope,3 AS code,'zh_CN' AS locale,'早退' AS text UNION ALL
SELECT 'PunchStatusStatisticsItemName' AS scope,4 AS code,'zh_CN' AS locale,'正常' AS text UNION ALL
SELECT 'PunchStatusStatisticsItemName' AS scope,5 AS code,'zh_CN' AS locale,'休息' AS text UNION ALL
SELECT 'PunchStatusStatisticsItemName' AS scope,6 AS code,'zh_CN' AS locale,'旷工' AS text UNION ALL
SELECT 'PunchStatusStatisticsItemName' AS scope,7 AS code,'zh_CN' AS locale,'缺卡' AS text UNION ALL
SELECT 'PunchStatusStatisticsItemName' AS scope,8 AS code,'zh_CN' AS locale,'核算中' AS text UNION ALL
SELECT 'PunchStatusStatisticsItemName' AS scope,9 AS code,'zh_CN' AS locale,'应到' AS text UNION ALL
SELECT 'PunchStatusStatisticsItemName' AS scope,10 AS code,'zh_CN' AS locale,'已到' AS text UNION ALL

SELECT 'PunchExceptionRequestStatisticsItemName' AS scope,1 AS code,'zh_CN' AS locale,'请假' AS text UNION ALL
SELECT 'PunchExceptionRequestStatisticsItemName' AS scope,2 AS code,'zh_CN' AS locale,'外出' AS text UNION ALL
SELECT 'PunchExceptionRequestStatisticsItemName' AS scope,3 AS code,'zh_CN' AS locale,'出差' AS text UNION ALL
SELECT 'PunchExceptionRequestStatisticsItemName' AS scope,4 AS code,'zh_CN' AS locale,'加班' AS text UNION ALL
SELECT 'PunchExceptionRequestStatisticsItemName' AS scope,5 AS code,'zh_CN' AS locale,'打卡异常' AS text
)r LEFT JOIN eh_locale_strings s ON r.scope=s.scope AND r.code=s.code AND r.locale=s.locale
WHERE s.id IS NULL;

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-33645: 考勤7.0 - 统计相关 对新增的detail_id进行旧数据初始化
UPDATE eh_punch_statistics l INNER JOIN eh_organization_member_details d ON d.organization_id=l.owner_id AND d.target_id=l.user_id
SET l.detail_id=d.id
WHERE d.target_id>0 AND l.detail_id IS NULL;

UPDATE eh_punch_day_logs l INNER JOIN eh_organization_member_details d ON d.organization_id=l.enterprise_id AND d.target_id=l.user_id
SET l.detail_id=d.id
WHERE d.target_id>0 AND l.detail_id IS NULL;

UPDATE eh_punch_day_log_files l INNER JOIN eh_organization_member_details d ON d.organization_id=l.enterprise_id AND d.target_id=l.user_id
SET l.detail_id=d.id
WHERE d.target_id>0 AND l.detail_id IS NULL;

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-33645: 考勤7.0 - 统计相关 从每日统计eh_punch_day_logs表中追溯当日的考勤规则，更新到eh_punch_logs表中
UPDATE eh_punch_logs l INNER JOIN eh_punch_day_logs pdl ON l.user_id=pdl.user_id AND l.enterprise_id=pdl.enterprise_id AND l.punch_date=pdl.punch_date
SET l.punch_organization_id=pdl.punch_organization_id
WHERE l.punch_organization_id IS NULL;

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-33645: 考勤7.0 - 统计相关 将非工作日变更为休息
UPDATE eh_locale_strings SET text='休息' WHERE scope='punch.status' AND code=17;
UPDATE eh_punch_statistics SET status_list=REPLACE(status_list,'非工作日','休息')  WHERE status_list LIKE '%非工作日%';

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-33645: 考勤7.0 - 统计相关 初始化考勤月报的异常打卡申请次数
UPDATE eh_punch_statistics s INNER JOIN
(
SELECT enterprise_id,user_id,REPLACE(LEFT(punch_date,7),'-','') AS punch_month,COUNT(1) AS exception_request_counts
FROM eh_punch_exception_requests WHERE approval_attribute='ABNORMAL_PUNCH' AND status IN(0,1) GROUP BY enterprise_id,user_id,LEFT(punch_date,7)
) e
ON s.owner_id=e.enterprise_id AND s.user_id=e.user_id AND s.punch_month=e.punch_month
SET s.exception_request_counts=e.exception_request_counts,s.punch_exception_request_count=e.exception_request_counts
WHERE s.user_id>0;

-- AUTHOR: 吴寒 20180816
-- REMARK: ISSUE-33645: 考勤7.0 - 给日/月报表旧数据初始化dept_id
-- REMARK: 先找有没有部门,如果有就取第一个部门
UPDATE eh_punch_day_logs a SET  a.dept_id =
  (SELECT
    b.`organization_id`
  FROM
    eh_organization_members b
  WHERE a.`detail_id` = b.`detail_id`
    AND b.group_path LIKE CONCAT("/", a.`enterprise_id`, "%")
    AND b.group_type = 'DEPARTMENT'
    AND b.`status` = 3 LIMIT 1)
WHERE a.`dept_id` IS NULL ;

-- REMARK: 没部门再找公司
UPDATE eh_punch_day_logs a SET  a.dept_id =
   (SELECT
    b.`organization_id`
  FROM
    eh_organization_members b
  WHERE a.`detail_id` = b.`detail_id`
    AND b.group_path LIKE CONCAT("/", a.`enterprise_id`, "%")
    AND b.group_type = 'ENTERPRISE'
    AND b.`status` = 3  LIMIT 1)
WHERE a.`dept_id` IS NULL ;

-- REMARK: 先找有没有部门,如果有就取第一个部门
UPDATE eh_punch_statistics a SET  a.dept_id =
  (SELECT
    b.`organization_id`
  FROM
    eh_organization_members b
  WHERE a.`detail_id` = b.`detail_id`
    AND b.group_path LIKE CONCAT("/", a.`owner_id`, "%")
    AND b.group_type = 'DEPARTMENT'
    AND b.`status` = 3 LIMIT 1)
WHERE a.`dept_id` IS NULL ;

-- REMARK: 没部门再找公司
UPDATE eh_punch_statistics a SET  a.dept_id =
  (SELECT
    b.`organization_id`
  FROM
    eh_organization_members b
  WHERE a.`detail_id` = b.`detail_id`
    AND b.group_path LIKE CONCAT("/", a.`owner_id`, "%")
    AND b.group_type = 'ENTERPRISE'
    AND b.`status` = 3  LIMIT 1)
WHERE a.`dept_id` IS NULL ;
 -- AUTHOR: 梁燕龙  20180816
-- REMARK: ISSUE-34179: 用户认证v3.6
-- 权限
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (42007, null, '查看审核列表', '用户认证查看审核列表', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (42008, null, '审核权限', '用户认证审核权限', NULL);

set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@mp_id:=@mp_id+1, '35000', '0', 42007, '查看审核列表', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@mp_id:=@mp_id+1, '35000', '0', 42008, '审核权限', '0', NOW());
-- 将用户认证移动到跟项目相关
UPDATE eh_service_modules SET module_control_type = 'community_control' WHERE id =35000;
UPDATE eh_service_module_apps SET module_control_type = 'community_control' WHERE module_id = 35000;
UPDATE eh_authorizations SET owner_type = 'EhAll' WHERE auth_id = 35000;
UPDATE eh_authorizations SET module_control_type = 'community_control' WHERE auth_id = 35000;


-- AUTHOR: 杨崇鑫  20180724
-- REMARK: 物业缴费V6.5初始化收费项税率为0
update eh_payment_charging_item_scopes set tax_rate=0;




-- AUTHOR: 唐岑 2018年8月17日10:31:31
-- REMARK: 导入房源信息时，添加提示信息
-- REMARK: ISSUE-31926: 资产V3.1，资产管理重构
SET @id = IFNULL((SELECT MAX(`id`) FROM `eh_locale_strings`),0);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'community', '20007', 'zh_CN', '楼层数填写格式错误，楼层数只能为数字');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'address', '20016', 'zh_CN', '楼层数填写格式错误，楼层数只能为数字');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'community', '10201', 'zh_CN', '楼宇名称已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'address', '30001', 'zh_CN', '建筑面积不能为负数');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'address', '30002', 'zh_CN', '收费面积不能为负数');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'address', '30003', 'zh_CN', '在租面积不能为负数');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'address', '30004', 'zh_CN', '可招租面积不能为负数');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'address', '30005', 'zh_CN', '楼层不在该楼宇楼层范围内');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'community', '10012', 'zh_CN', '楼宇编号已存在');

-- AUTHOR: 唐岑 2018年8月17日10:31:31
-- REMARK: 房源执行拆分合并计划时，添加提示信息
-- REMARK: ISSUE-30697: 资产V3.2，新增拆分合并功能
SET @id = IFNULL((SELECT MAX(`id`) FROM `eh_locale_templates`),0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'address.arrangement', '1', 'zh_CN', '房源拆分计划', '${originalIds}被拆分成${targetIds}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'address.arrangement', '2', 'zh_CN', '房源合并计划', '${originalIds}被合并成${targetIds}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'address.arrangement', '3', 'zh_CN', '新（中文）', '新', '0');

SET @id = IFNULL((SELECT MAX(`id`) FROM `eh_locale_strings`),0);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'address', '20014', 'zh_CN', '可招租面积只能为数字');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'address', '20015', 'zh_CN', '拆分/合并计划产生的未来房源已关联合同，删除失败');


-- AUTHOR: 丁建民
-- REMARK: 合同退约提示信息(来自合同2.8)
SET @id = IFNULL((SELECT MAX(`id`) FROM `eh_locale_strings`),0);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'contract', '10012', 'zh_CN', '合同关联的账单不存在');

-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.3 
SET @eh_locale_strings_id = (SELECT MAX(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'assetv2', '10020', 'zh_CN', '此账单组中已存在该费项');


-- AUTHOR: 丁建民
-- REMARK: 仓库管理V2.3（库存导入导出功能） issue-34335
SET @id = (SELECT MAX(id) from eh_locale_strings);
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'warehouse', '10027', 'zh_CN', '物品编号不能为空');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'warehouse', '10028', 'zh_CN', '库存不能为空');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'warehouse', '10029', 'zh_CN', '所属仓库不能为空');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'warehouse', '10030', 'zh_CN', '库存请输入大于的0整数');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'warehouse', '10031', 'zh_CN', '物品编号和名称不能匹配');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'warehouse', '10032', 'zh_CN', '填写分类与物品分类不一致');


-- AUTHOR: huangmingbo 2018.08.03
-- REMARK: 服务联盟v3.5 埋点统计
SET @event_display_name = '服务联盟埋点统计';
SET @event_name = 'service_alliance_click';
SET @eh_stat_events_id = (SELECT MAX(id) FROM eh_stat_events);
INSERT INTO eh_stat_events (id, namespace_id, event_scope, event_type, event_name, event_version, event_display_name, status, creator_uid, update_uid, create_time, update_time)
VALUES ((@eh_stat_events_id := @eh_stat_events_id + 1), 0, 1, 1, @event_name, '1.0', @event_display_name, 2, null, null, null, null);


SET @eh_stat_event_params_id = (SELECT MAX(id) FROM eh_stat_event_params);
INSERT INTO eh_stat_event_params (id, namespace_id, event_scope, event_type, event_version, multiple, event_name, param_type, param_key, param_name, status, creator_uid, update_uid, create_time, update_time)
VALUES ((@eh_stat_event_params_id := @eh_stat_event_params_id + 1), 0, 1, 1, '1.0', 1, 'service_alliance_click', 1, 'service_id', 'service_id', 2, null, null, null, null);
INSERT INTO eh_stat_event_params (id, namespace_id, event_scope, event_type, event_version, multiple, event_name, param_type, param_key, param_name, status, creator_uid, update_uid, create_time, update_time)
VALUES ((@eh_stat_event_params_id := @eh_stat_event_params_id + 1), 0, 1, 1, '1.0', 1, 'service_alliance_click', 1, 'category_id', 'category_id', 2, null, null, null, null);
INSERT INTO eh_stat_event_params (id, namespace_id, event_scope, event_type, event_version, multiple, event_name, param_type, param_key, param_name, status, creator_uid, update_uid, create_time, update_time)
VALUES ((@eh_stat_event_params_id := @eh_stat_event_params_id + 1), 0, 1, 1, '1.0', 1, 'service_alliance_click', 1, 'user_id', 'user_id', 2, null, null, null, null);
INSERT INTO eh_stat_event_params (id, namespace_id, event_scope, event_type, event_version, multiple, event_name, param_type, param_key, param_name, status, creator_uid, update_uid, create_time, update_time)
VALUES ((@eh_stat_event_params_id := @eh_stat_event_params_id + 1), 0, 1, 1, '1.0', 1, 'service_alliance_click', 1, 'click_type', 'click_type', 2, null, null, null, null);
INSERT INTO eh_stat_event_params (id, namespace_id, event_scope, event_type, event_version, multiple, event_name, param_type, param_key, param_name, status, creator_uid, update_uid, create_time, update_time)
VALUES ((@eh_stat_event_params_id := @eh_stat_event_params_id + 1), 0, 1, 1, '1.0', 1, 'service_alliance_click', 1, 'community_id', 'community_id', 2, null, null, null, null);

-- 错误信息
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '11200', 'zh_CN', '获取统计信息时type为null');

-- 权限
SET @eh_service_module_privileges_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id+1), 40550, 0, 4050040550, '用户行为统计', 0, '2018-03-31 22:18:44');
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4050040550, 0, '服务联盟 用户行为统计', '服务联盟 用户行为统计', NULL);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`) VALUES (40550, '用户行为统计权限', 40500, '/200/120000/40500/40550', 1, 4, 2, 3, '2018-03-31 22:18:44', NULL, NULL, '2018-03-31 22:18:44', 0, 1, '1', NULL, '', 1, 1, 'subModule');


-- AUTHOR: 黄鹏宇 2018年8月17日16:26:43
-- REMARK: 请示单默认表单以及企业客户是否为资质客户字段

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'aptitudeFlagItemId', '资质客户', 'Long', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');
  INSERT INTO `eh_var_field_items`(`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES (@eeciId, 'enterprise_customer', @eecid, '是', 1, 2, 1, sysdate(), NULL, NULL, 1);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_field_items`(`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES (@eeciId, 'enterprise_customer', @eecid, '否', 2, 2, 1, sysdate(), NULL, NULL, 0);



set @id=(select max(id)+1 from `eh_general_form_templates`) ;
INSERT INTO `eh_general_form_templates`(`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`,  `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `create_time` ) VALUES (@id, 11, 0, 0, 'EhOrganizations', 25000, 'requisition', '请示单',  0, 'DEFAULT_JSON', '[{
	"dynamicFlag": 0,
	"fieldDesc": "客户名称",
	"fieldDisplayName": "客户名称",
	"fieldExtra": "{}",
	"fieldName": "客户名称",
	"fieldType": "SINGLE_LINE_TEXT",
	"renderType": "DEFAULT",
	"requiredFlag": 1,
	"validatorType": "TEXT_LIMIT",
	"visibleType": "EDITABLE",
	"filterFlag": 1
},
{
	"dynamicFlag": 0,
	"fieldDesc": "楼栋门牌",
	"fieldDisplayName": "楼栋门牌",
	"fieldExtra": "{}",
	"fieldName": "楼栋门牌",
	"fieldType": "SINGLE_LINE_TEXT",
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
	"validatorType": "TEXT_LIMIT",
	"visibleType": "EDITABLE",
	"filterFlag": 1
}]',1, 1,SYSDATE()) ;

-- AUTHOR: 郑思挺 2018年8月17日
-- REMARK: 资源预约3.6
INSERT INTO `eh_locale_strings` ( `scope`, `code`, `locale`, `text`) VALUES ( 'rental.flow', 'scene', 'zh_CN', '用户类型');

update `eh_rentalv2_cells` set `cell_id` = `id` where `cell_id`is null;

UPDATE eh_rentalv2_resource_orders
SET begin_time = resource_rental_date,end_time = date_add(resource_rental_date,INTERVAL 1 MONTH) WHERE rental_type = 4 AND begin_time IS NULL AND end_time IS NULL AND resource_rental_date IS NOT NULL;
UPDATE eh_rentalv2_cells
SET begin_time = resource_rental_date,end_time = date_add(resource_rental_date,INTERVAL 1 MONTH) WHERE rental_type = 4 AND begin_time IS NULL AND end_time IS NULL AND resource_rental_date IS NOT NULL;

UPDATE eh_rentalv2_resource_orders
SET begin_time = resource_rental_date,end_time = date_add(resource_rental_date,INTERVAL 1 week) WHERE rental_type = 5 AND begin_time IS NULL AND end_time IS NULL AND resource_rental_date IS NOT NULL;
UPDATE eh_rentalv2_cells
SET begin_time = resource_rental_date,end_time = date_add(resource_rental_date,INTERVAL 1 week) WHERE rental_type = 5 AND begin_time IS NULL AND end_time IS NULL AND resource_rental_date IS NOT NULL;

UPDATE eh_rentalv2_resource_orders
SET begin_time = resource_rental_date,end_time = date_add(resource_rental_date,INTERVAL 1 day) WHERE rental_type = 2 AND begin_time IS NULL AND end_time IS NULL AND resource_rental_date IS NOT NULL;
UPDATE eh_rentalv2_cells
SET begin_time = resource_rental_date,end_time = date_add(resource_rental_date,INTERVAL 1 day) WHERE rental_type = 2 AND begin_time IS NULL AND end_time IS NULL AND resource_rental_date IS NOT NULL;

UPDATE eh_rentalv2_resource_orders
SET begin_time = date_add(resource_rental_date,INTERVAL 8 hour),end_time = date_add(resource_rental_date,INTERVAL 12 hour) WHERE (rental_type = 1 or rental_type = 3) and amorpm = 0 AND begin_time IS NULL AND end_time IS NULL AND resource_rental_date IS NOT NULL;
UPDATE eh_rentalv2_cells
SET begin_time = date_add(resource_rental_date,INTERVAL 8 hour),end_time = date_add(resource_rental_date,INTERVAL 12 hour) WHERE (rental_type = 1 or rental_type = 3) and amorpm = 0 AND begin_time IS NULL AND end_time IS NULL AND resource_rental_date IS NOT NULL;

UPDATE eh_rentalv2_resource_orders
SET begin_time = date_add(resource_rental_date,INTERVAL 14 hour),end_time = date_add(resource_rental_date,INTERVAL 18 hour) WHERE (rental_type = 1 or rental_type = 3) and amorpm = 1 AND begin_time IS NULL AND end_time IS NULL AND resource_rental_date IS NOT NULL;
UPDATE eh_rentalv2_cells
SET begin_time = date_add(resource_rental_date,INTERVAL 14 hour),end_time = date_add(resource_rental_date,INTERVAL 18 hour) WHERE (rental_type = 1 or rental_type = 3) and amorpm = 1 AND begin_time IS NULL AND end_time IS NULL AND resource_rental_date IS NOT NULL;

UPDATE eh_rentalv2_resource_orders
SET begin_time = date_add(resource_rental_date,INTERVAL 18 hour),end_time = date_add(resource_rental_date,INTERVAL 22 hour) WHERE  rental_type = 3 and amorpm = 2 AND begin_time IS NULL AND end_time IS NULL AND resource_rental_date IS NOT NULL;
UPDATE eh_rentalv2_cells
SET begin_time = date_add(resource_rental_date,INTERVAL 18 hour),end_time = date_add(resource_rental_date,INTERVAL 22 hour) WHERE  rental_type = 3 and amorpm = 2 AND begin_time IS NULL AND end_time IS NULL AND resource_rental_date IS NOT NULL;


-- AUTHOR: 吴寒 2018年8月17日
-- REMARK: 锁掌柜门禁对接
-- 会议室1
SET @door_id = (SELECT MAX(id) FROM `eh_door_access`);
INSERT INTO `eh_door_access` (`id`, `namespace_id`, `uuid`, `door_type`, `hardware_id`, `name`, `display_name`, `description`, `avatar`, `address`, `active_user_id`, `creator_user_id`, `longitude`, `latitude`, `geohash`, `aes_iv`, `link_status`, `owner_type`, `owner_id`, `role`, `create_time`, `status`, `acking_secret_version`, `expect_secret_key`, `groupId`, `floor_id`, `mac_copy`, `enable_amount`, `local_server_id`, `has_qr`) VALUES((@door_id := @door_id + 1),'999972',UUID(),'18','DE:85:D2:31:3E:C0','会议室1','会议室1','会议室1',NULL,NULL,'505361','505361',NULL,NULL,NULL,'','0','0','240111044331050361','0','2018-06-01 06:01:11','1','1','1','0',NULL,NULL,NULL,NULL,'1');
SET @aclink_id = (SELECT MAX(id) FROM `eh_aclinks`);
INSERT INTO `eh_aclinks` (`id`, `namespace_id`, `door_id`, `device_name`, `manufacturer`, `firware_ver`, `create_time`, `status`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `driver`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`) VALUES((@aclink_id := @aclink_id+1),'999972',@door_id,'会议室1','uclbrt','1',NULL,'1','{"sid":"3a56074d26aebd50027c23a3679b5fa5","token":"768446d02dca22504658ff46cca019","communityNo":"1316881497","buildNo":"001","floorNo":"001","roomNo":"A1"}',NULL,NULL,NULL,NULL,'zuolin',NULL,NULL,NULL,NULL,NULL);

-- 会议室2
SET @door_id = (SELECT MAX(id) FROM `eh_door_access`);
INSERT INTO `eh_door_access` (`id`, `namespace_id`, `uuid`, `door_type`, `hardware_id`, `name`, `display_name`, `description`, `avatar`, `address`, `active_user_id`, `creator_user_id`, `longitude`, `latitude`, `geohash`, `aes_iv`, `link_status`, `owner_type`, `owner_id`, `role`, `create_time`, `status`, `acking_secret_version`, `expect_secret_key`, `groupId`, `floor_id`, `mac_copy`, `enable_amount`, `local_server_id`, `has_qr`) VALUES((@door_id := @door_id + 1),'999972',UUID(),'18','DE:85:D2:31:3E:C0','会议室2','会议室2','会议室2',NULL,NULL,'505361','505361',NULL,NULL,NULL,'','0','0','240111044331050361','0','2018-06-01 06:01:11','1','1','1','0',NULL,NULL,NULL,NULL,'1');
SET @aclink_id = (SELECT MAX(id) FROM `eh_aclinks`);
INSERT INTO `eh_aclinks` (`id`, `namespace_id`, `door_id`, `device_name`, `manufacturer`, `firware_ver`, `create_time`, `status`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `driver`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`) VALUES((@aclink_id := @aclink_id+1),'999972',@door_id,'会议室2','uclbrt','1',NULL,'1','{"sid":"3a56074d26aebd50027c23a3679b5fa5","token":"768446d02dca22504658ff46cca019","communityNo":"1316881497","buildNo":"001","floorNo":"001","roomNo":"A2"}',NULL,NULL,NULL,NULL,'zuolin',NULL,NULL,NULL,NULL,NULL);

-- 会议室3
SET @door_id = (SELECT MAX(id) FROM `eh_door_access`);
INSERT INTO `eh_door_access` (`id`, `namespace_id`, `uuid`, `door_type`, `hardware_id`, `name`, `display_name`, `description`, `avatar`, `address`, `active_user_id`, `creator_user_id`, `longitude`, `latitude`, `geohash`, `aes_iv`, `link_status`, `owner_type`, `owner_id`, `role`, `create_time`, `status`, `acking_secret_version`, `expect_secret_key`, `groupId`, `floor_id`, `mac_copy`, `enable_amount`, `local_server_id`, `has_qr`) VALUES((@door_id := @door_id + 1),'999972',UUID(),'18','DE:85:D2:31:3E:C0','会议室3','会议室3','会议室3',NULL,NULL,'505361','505361',NULL,NULL,NULL,'','0','0','240111044331050361','0','2018-06-01 06:01:11','1','1','1','0',NULL,NULL,NULL,NULL,'1');
SET @aclink_id = (SELECT MAX(id) FROM `eh_aclinks`);
INSERT INTO `eh_aclinks` (`id`, `namespace_id`, `door_id`, `device_name`, `manufacturer`, `firware_ver`, `create_time`, `status`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `driver`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`) VALUES((@aclink_id := @aclink_id+1),'999972',@door_id,'会议室3','uclbrt','1',NULL,'1','{"sid":"3a56074d26aebd50027c23a3679b5fa5","token":"768446d02dca22504658ff46cca019","communityNo":"1316881497","buildNo":"001","floorNo":"001","roomNo":"A3"}',NULL,NULL,NULL,NULL,'zuolin',NULL,NULL,NULL,NULL,NULL);

-- 会议室4
SET @door_id = (SELECT MAX(id) FROM `eh_door_access`);
INSERT INTO `eh_door_access` (`id`, `namespace_id`, `uuid`, `door_type`, `hardware_id`, `name`, `display_name`, `description`, `avatar`, `address`, `active_user_id`, `creator_user_id`, `longitude`, `latitude`, `geohash`, `aes_iv`, `link_status`, `owner_type`, `owner_id`, `role`, `create_time`, `status`, `acking_secret_version`, `expect_secret_key`, `groupId`, `floor_id`, `mac_copy`, `enable_amount`, `local_server_id`, `has_qr`) VALUES((@door_id := @door_id + 1),'999972',UUID(),'18','DE:85:D2:31:3E:C0','会议室4','会议室4','会议室4',NULL,NULL,'505361','505361',NULL,NULL,NULL,'','0','0','240111044331050361','0','2018-06-01 06:01:11','1','1','1','0',NULL,NULL,NULL,NULL,'1');
SET @aclink_id = (SELECT MAX(id) FROM `eh_aclinks`);
INSERT INTO `eh_aclinks` (`id`, `namespace_id`, `door_id`, `device_name`, `manufacturer`, `firware_ver`, `create_time`, `status`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `driver`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`) VALUES((@aclink_id := @aclink_id+1),'999972',@door_id,'会议室4','uclbrt','1',NULL,'1','{"sid":"3a56074d26aebd50027c23a3679b5fa5","token":"768446d02dca22504658ff46cca019","communityNo":"1316881497","buildNo":"001","floorNo":"001","roomNo":"A4"}',NULL,NULL,NULL,NULL,'zuolin',NULL,NULL,NULL,NULL,NULL);

-- 会议室5
SET @door_id = (SELECT MAX(id) FROM `eh_door_access`);
INSERT INTO `eh_door_access` (`id`, `namespace_id`, `uuid`, `door_type`, `hardware_id`, `name`, `display_name`, `description`, `avatar`, `address`, `active_user_id`, `creator_user_id`, `longitude`, `latitude`, `geohash`, `aes_iv`, `link_status`, `owner_type`, `owner_id`, `role`, `create_time`, `status`, `acking_secret_version`, `expect_secret_key`, `groupId`, `floor_id`, `mac_copy`, `enable_amount`, `local_server_id`, `has_qr`) VALUES((@door_id := @door_id + 1),'999972',UUID(),'18','DE:85:D2:31:3E:C0','会议室5','会议室5','会议室5',NULL,NULL,'505361','505361',NULL,NULL,NULL,'','0','0','240111044331050361','0','2018-06-01 06:01:11','1','1','1','0',NULL,NULL,NULL,NULL,'1');
SET @aclink_id = (SELECT MAX(id) FROM `eh_aclinks`);
INSERT INTO `eh_aclinks` (`id`, `namespace_id`, `door_id`, `device_name`, `manufacturer`, `firware_ver`, `create_time`, `status`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `driver`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`) VALUES((@aclink_id := @aclink_id+1),'999972',@door_id,'会议室5','uclbrt','1',NULL,'1','{"sid":"3a56074d26aebd50027c23a3679b5fa5","token":"768446d02dca22504658ff46cca019","communityNo":"1316881497","buildNo":"001","floorNo":"001","roomNo":"A5"}',NULL,NULL,NULL,NULL,'zuolin',NULL,NULL,NULL,NULL,NULL);

-- 会议室6
SET @door_id = (SELECT MAX(id) FROM `eh_door_access`);
INSERT INTO `eh_door_access` (`id`, `namespace_id`, `uuid`, `door_type`, `hardware_id`, `name`, `display_name`, `description`, `avatar`, `address`, `active_user_id`, `creator_user_id`, `longitude`, `latitude`, `geohash`, `aes_iv`, `link_status`, `owner_type`, `owner_id`, `role`, `create_time`, `status`, `acking_secret_version`, `expect_secret_key`, `groupId`, `floor_id`, `mac_copy`, `enable_amount`, `local_server_id`, `has_qr`) VALUES((@door_id := @door_id + 1),'999972',UUID(),'18','DE:85:D2:31:3E:C0','会议室6','会议室6','会议室6',NULL,NULL,'505361','505361',NULL,NULL,NULL,'','0','0','240111044331050361','0','2018-06-01 06:01:11','1','1','1','0',NULL,NULL,NULL,NULL,'1');
SET @aclink_id = (SELECT MAX(id) FROM `eh_aclinks`);
INSERT INTO `eh_aclinks` (`id`, `namespace_id`, `door_id`, `device_name`, `manufacturer`, `firware_ver`, `create_time`, `status`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `driver`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`) VALUES((@aclink_id := @aclink_id+1),'999972',@door_id,'会议室6','uclbrt','1',NULL,'1','{"sid":"3a56074d26aebd50027c23a3679b5fa5","token":"768446d02dca22504658ff46cca019","communityNo":"1316881497","buildNo":"001","floorNo":"001","roomNo":"A6"}',NULL,NULL,NULL,NULL,'zuolin',NULL,NULL,NULL,NULL,NULL);

-- 会议室7-1
SET @door_id = (SELECT MAX(id) FROM `eh_door_access`);
INSERT INTO `eh_door_access` (`id`, `namespace_id`, `uuid`, `door_type`, `hardware_id`, `name`, `display_name`, `description`, `avatar`, `address`, `active_user_id`, `creator_user_id`, `longitude`, `latitude`, `geohash`, `aes_iv`, `link_status`, `owner_type`, `owner_id`, `role`, `create_time`, `status`, `acking_secret_version`, `expect_secret_key`, `groupId`, `floor_id`, `mac_copy`, `enable_amount`, `local_server_id`, `has_qr`) VALUES((@door_id := @door_id + 1),'999972',UUID(),'18','DE:85:D2:31:3E:C0','会议室7-1','会议室7-1','会议室7-1',NULL,NULL,'505361','505361',NULL,NULL,NULL,'','0','0','240111044331050361','0','2018-06-01 06:01:11','1','1','1','0',NULL,NULL,NULL,NULL,'1');
SET @aclink_id = (SELECT MAX(id) FROM `eh_aclinks`);
INSERT INTO `eh_aclinks` (`id`, `namespace_id`, `door_id`, `device_name`, `manufacturer`, `firware_ver`, `create_time`, `status`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `driver`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`) VALUES((@aclink_id := @aclink_id+1),'999972',@door_id,'会议室7-1','uclbrt','1',NULL,'1','{"sid":"3a56074d26aebd50027c23a3679b5fa5","token":"768446d02dca22504658ff46cca019","communityNo":"1316881497","buildNo":"001","floorNo":"001","roomNo":"A71"}',NULL,NULL,NULL,NULL,'zuolin',NULL,NULL,NULL,NULL,NULL);

-- 会议室7-2
SET @door_id = (SELECT MAX(id) FROM `eh_door_access`);
INSERT INTO `eh_door_access` (`id`, `namespace_id`, `uuid`, `door_type`, `hardware_id`, `name`, `display_name`, `description`, `avatar`, `address`, `active_user_id`, `creator_user_id`, `longitude`, `latitude`, `geohash`, `aes_iv`, `link_status`, `owner_type`, `owner_id`, `role`, `create_time`, `status`, `acking_secret_version`, `expect_secret_key`, `groupId`, `floor_id`, `mac_copy`, `enable_amount`, `local_server_id`, `has_qr`) VALUES((@door_id := @door_id + 1),'999972',UUID(),'18','DE:85:D2:31:3E:C0','会议室7-2','会议室7-2','会议室7-2',NULL,NULL,'505361','505361',NULL,NULL,NULL,'','0','0','240111044331050361','0','2018-06-01 06:01:11','1','1','1','0',NULL,NULL,NULL,NULL,'1');
SET @aclink_id = (SELECT MAX(id) FROM `eh_aclinks`);
INSERT INTO `eh_aclinks` (`id`, `namespace_id`, `door_id`, `device_name`, `manufacturer`, `firware_ver`, `create_time`, `status`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `driver`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`) VALUES((@aclink_id := @aclink_id+1),'999972',@door_id,'会议室7-2','uclbrt','1',NULL,'1','{"sid":"3a56074d26aebd50027c23a3679b5fa5","token":"768446d02dca22504658ff46cca019","communityNo":"1316881497","buildNo":"001","floorNo":"001","roomNo":"A72"}',NULL,NULL,NULL,NULL,'zuolin',NULL,NULL,NULL,NULL,NULL);

-- 会议室8
SET @door_id = (SELECT MAX(id) FROM `eh_door_access`);
INSERT INTO `eh_door_access` (`id`, `namespace_id`, `uuid`, `door_type`, `hardware_id`, `name`, `display_name`, `description`, `avatar`, `address`, `active_user_id`, `creator_user_id`, `longitude`, `latitude`, `geohash`, `aes_iv`, `link_status`, `owner_type`, `owner_id`, `role`, `create_time`, `status`, `acking_secret_version`, `expect_secret_key`, `groupId`, `floor_id`, `mac_copy`, `enable_amount`, `local_server_id`, `has_qr`) VALUES((@door_id := @door_id + 1),'999972',UUID(),'18','DE:85:D2:31:3E:C0','会议室8','会议室8','会议室8',NULL,NULL,'505361','505361',NULL,NULL,NULL,'','0','0','240111044331050361','0','2018-06-01 06:01:11','1','1','1','0',NULL,NULL,NULL,NULL,'1');
SET @aclink_id = (SELECT MAX(id) FROM `eh_aclinks`);
INSERT INTO `eh_aclinks` (`id`, `namespace_id`, `door_id`, `device_name`, `manufacturer`, `firware_ver`, `create_time`, `status`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `driver`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`) VALUES((@aclink_id := @aclink_id+1),'999972',@door_id,'会议室8','uclbrt','1',NULL,'1','{"sid":"3a56074d26aebd50027c23a3679b5fa5","token":"768446d02dca22504658ff46cca019","communityNo":"1316881497","buildNo":"001","floorNo":"001","roomNo":"A8"}',NULL,NULL,NULL,NULL,'zuolin',NULL,NULL,NULL,NULL,NULL);

-- 会议室10
SET @door_id = (SELECT MAX(id) FROM `eh_door_access`);
INSERT INTO `eh_door_access` (`id`, `namespace_id`, `uuid`, `door_type`, `hardware_id`, `name`, `display_name`, `description`, `avatar`, `address`, `active_user_id`, `creator_user_id`, `longitude`, `latitude`, `geohash`, `aes_iv`, `link_status`, `owner_type`, `owner_id`, `role`, `create_time`, `status`, `acking_secret_version`, `expect_secret_key`, `groupId`, `floor_id`, `mac_copy`, `enable_amount`, `local_server_id`, `has_qr`) VALUES((@door_id := @door_id + 1),'999972',UUID(),'18','DE:85:D2:31:3E:C0','会议室10','会议室10','会议室10',NULL,NULL,'505361','505361',NULL,NULL,NULL,'','0','0','240111044331050361','0','2018-06-01 06:01:11','1','1','1','0',NULL,NULL,NULL,NULL,'1');
SET @aclink_id = (SELECT MAX(id) FROM `eh_aclinks`);
INSERT INTO `eh_aclinks` (`id`, `namespace_id`, `door_id`, `device_name`, `manufacturer`, `firware_ver`, `create_time`, `status`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `driver`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`) VALUES((@aclink_id := @aclink_id+1),'999972',@door_id,'会议室10','uclbrt','1',NULL,'1','{"sid":"3a56074d26aebd50027c23a3679b5fa5","token":"768446d02dca22504658ff46cca019","communityNo":"1316881497","buildNo":"001","floorNo":"002","roomNo":"A10"}',NULL,NULL,NULL,NULL,'zuolin',NULL,NULL,NULL,NULL,NULL);

-- AUTHOR: dengs 2018年8月17日
-- REMARK: issue-26616 停车缴费V6.6（UE优化）
DELETE from eh_service_modules WHERE id in (40810,40820,40830,40840);
DELETE from eh_acl_privileges WHERE id in (4080040810,4080040820,4080040830,4080040840);
DELETE from eh_service_module_privileges WHERE module_id in (40810,40820,40830,40840);

SELECT * from eh_service_modules WHERE id in (40800,40810,40820,40830,40840);
SELECT * from eh_acl_privileges WHERE id in (4080040800,4080040810,4080040820,4080040830,4080040840);
SELECT * from eh_service_module_privileges WHERE module_id in (40800,40810,40820,40830,40840);


INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40810', '申请管理', '40800', '/200/40000/40800/40810', '1', '4', '2', '61', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40820', '订单记录', '40800', '/200/40000/40800/40820', '1', '4', '2', '62', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');

set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4080040810, '0', '停车缴费 申请管理', '停车缴费 申请管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40810', '0', 4080040810, '申请管理权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4080040820, '0', '停车缴费 订单记录', '停车缴费 订单记录权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40820', '0', 4080040820, '订单记录权限', '0', now());

-- AUTHOR: 郑思挺 2018年8月17日
-- REMARK: 装修1.0
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`) VALUES ('43000', '装修办理', '40000', '/200/40000/43000', '1', '3', '2', '10', now(), '{}', '13', now(), '0', '0', '0', NULL, 'community_control', '1', '1', 'module');

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('16032300', '装修办理', '16030000', NULL, 'decoration-management', '1', '2', '/16000000/16030000/16032300', 'zuolin', '8', '43000', '3', 'system', 'module', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('45150000', '装修办理', '45000000', NULL, 'decoration-management', '1', '2', '/40000040/45000000/45150000', 'park', '2', '43000', '3', 'system', 'module', NULL);

set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4300043010, '0', '装修办理 装修办理权限', '装修办理 装修办理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '43000', '0', 4300043010, '全部权限', '0', now());

set @eh_locale_templates_id = (select max(id) from eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@eh_locale_templates_id := @eh_locale_templates_id+1, 'sms.default', '70', 'zh_CN', '装修申请通过', '尊敬的${decoratorName}，用户（${applyName}：${applyPhone}）提交的装修申请（${applyCompamy}）已审核通过，需尽快提交相关装修资料，请前往APP查看详情，您可以点击以下链接下载APP，并使用本机号码进行注册：${url} 。', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@eh_locale_templates_id := @eh_locale_templates_id+1, 'sms.default', '71', 'zh_CN', '资料审核成功', '尊敬的${decoratorName}，关于${applyCompamy}的装修资料已审核通过，请等待管理公司上传费用清单，您可前往APP查看详情。', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@eh_locale_templates_id := @eh_locale_templates_id+1, 'sms.default', '72', 'zh_CN', '资料审核失败', '尊敬的${decoratorName}，关于${applyCompamy}的装修资料审核不通过，装修办理流程已中止，您可前往APP查看详情。', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@eh_locale_templates_id := @eh_locale_templates_id+1, 'sms.default', '73', 'zh_CN', '装修费用清单生成', '尊敬的${name}，关于${applyCompamy}的装修费用清单已上传，需尽快缴费，您可前往APP查看详情。', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@eh_locale_templates_id := @eh_locale_templates_id+1, 'sms.default', '74', 'zh_CN', '缴费完成', '尊敬的${name}，关于${applyCompamy}的装修费用已缴纳，施工人员可凭证进场，请遵守施工相关规定，感谢您的配合。', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@eh_locale_templates_id := @eh_locale_templates_id+1, 'sms.default', '75', 'zh_CN', '竣工验收工作流正常结束', '尊敬的${decoratorName}，关于${applyCompamy}的装修已通过竣工验收，请等待管理公司确认押金退费信息，您可前往APP查看详情。', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@eh_locale_templates_id := @eh_locale_templates_id+1, 'sms.default', '76', 'zh_CN', '竣工验收工作流异常结束', '尊敬的${decoratorName}，关于${applyCompamy}的装修竣工验收未通过，装修办理流程已中止，您可前往APP查看详情。', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@eh_locale_templates_id := @eh_locale_templates_id+1, 'sms.default', '77', 'zh_CN', '押金退回信息生成', '尊敬的${name}，关于${applyCompamy}的装修押金退费信息已上传，您可前往APP查看详情。', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@eh_locale_templates_id := @eh_locale_templates_id+1, 'sms.default', '78', 'zh_CN', '押金退回成功', '尊敬的${name}，关于${applyCompamy}的装修押金已退回，装修办理完成，感谢您的使用。', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@eh_locale_templates_id := @eh_locale_templates_id+1, 'sms.default', '79', 'zh_CN', '装修公司负责人登记施工人员成功', '尊敬的${workerName}，用户（${decoratorName}：${decoratorPhone}）已登记您为此次装修工程（${applyCompamy}）的施工人员，请前往APP查看详情，您可以点击以下链接下载APP，并使用本机号码进行注册：${url} 。', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@eh_locale_templates_id := @eh_locale_templates_id+1, 'sms.default', '80', 'zh_CN', '装修流程被管理员在后台手动中止', '尊敬的${name}，关于${applyCompamy}的装修流程已被管理公司（${operatorName}；${operatorPhone}）中止，您可前往APP查看详情。', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@eh_locale_templates_id := @eh_locale_templates_id+1, 'sms.default', '81', 'zh_CN', '管理员修改装修费用的时候', '尊敬的${name}，关于${applyCompamy}的装修费用清单有更新，请前往APP查看详情。', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@eh_locale_templates_id := @eh_locale_templates_id+1, 'sms.default', '82', 'zh_CN', '管理员修改退费的时候', '尊敬的${name}，关于${applyCompamy}的装修押金退费信息有更新，请前往APP查看详情。', '0');



-- AUTHOR: 严军 2018年08月17日19:22:25
-- REMARK: issue-31049 域空间配置V1.4

-- 重建服务广场模板
DELETE from eh_portal_layout_templates;
INSERT INTO `eh_portal_layout_templates` (`id`, `namespace_id`, `label`, `template_json`, `show_uri`, `status`, `create_time`, `update_time`, `operator_uid`, `creator_uid`, `description`, `type`) VALUES ('1', '0', '首页', '{\"layoutName\":\"ServiceMarketLayout\",\"location\":\"/home\",\"groups\":[{\"label\":\"海报\", \"separatorFlag\":\"0\", \"separatorHeight\":\"0\",\"widget\":\"Banners\",\"style\":\"Default\",\"defaultOrder\":1},{\"label\":\"公告\", \"separatorFlag\":\"0\", \"separatorHeight\":\"0\",\"widget\":\"Bulletins\",\"style\":\"Default\",\"defaultOrder\":2,\"description\":\"\"},{\"label\":\"容器\", \"separatorFlag\":\"0\", \"separatorHeight\":\"0\",\"widget\":\"Navigator\",\"style\":\"Gallery\",\"instanceConfig\":{\"margin\":20,\"padding\":16,\"backgroundColor\":\"#ffffff\",\"titleFlag\":0,\"title\":\"标题\",\"titleUri\":\"\"},\"defaultOrder\":3,\"description\":\"\"}]}', NULL, '2', '2017-09-15 18:53:16', '2017-09-15 18:53:16', '1', '1', NULL, '1');
INSERT INTO `eh_portal_layout_templates` (`id`, `namespace_id`, `label`, `template_json`, `show_uri`, `status`, `create_time`, `update_time`, `operator_uid`, `creator_uid`, `description`, `type`) VALUES ('2', '0', '自定义门户', '{\"groups\":[{\"label\":\"容器\", \"separatorFlag\":\"0\",\"separatorHeight\":\"0\",\"widget\":\"Navigator\",\"style\":\"Gallery\",\"instanceConfig\":{\"margin\":20,\"padding\":16,\"backgroundColor\":\"#ffffff\",\"titleFlag\":0,\"title\":\"标题\",\"titleUri\":\"\"},\"defaultOrder\":1,\"description\":\"\"}]}', NULL, '2', '2017-09-15 18:53:16', '2017-09-15 18:53:16', '1', '1', NULL, '2');
INSERT INTO `eh_portal_layout_templates` (`id`, `namespace_id`, `label`, `template_json`, `show_uri`, `status`, `create_time`, `update_time`, `operator_uid`, `creator_uid`, `description`, `type`) VALUES ('3', '0', '分页签门户', '{\"groups\":[{\"label\":\"分页签\", \"separatorFlag\":\"0\", \"separatorHeight\":\"0\",\"widget\":\"Tab\",\"style\":\"1\",\"defaultOrder\":1}]}', NULL, '2', '2017-09-15 18:53:16', '2017-09-15 18:53:16', '1', '1', NULL, '3');

-- 首页（激活）
UPDATE eh_portal_layouts SET type = 1, index_flag = 1 WHERE location = '/home' AND `name` = 'ServiceMarketLayout';
-- 自定义门户（激活）
UPDATE eh_portal_layouts SET type = 2, index_flag = 1 WHERE location = '/secondhome' AND `name` = 'SecondServiceMarketLayout';
-- 分页签门户（激活）
UPDATE eh_portal_layouts SET type = 3, index_flag = 1 WHERE location = '/association' AND `name` = 'AssociationLayout';

-- 分页签门户（未激活）
UPDATE eh_portal_layouts a SET type = 3, index_flag = 0 WHERE type IS NULL AND EXISTS ( SELECT * from  eh_portal_item_groups b WHERE a.id = b.layout_id and b.widget = 'Tab');

-- 自定义门户（未激活）
UPDATE eh_portal_layouts SET type = 2, index_flag = 0 WHERE type IS NULL;

--
DELETE FROM eh_service_modules  WHERE  id in (400, 90000, 90100, 92000, 92100, 92200);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`) VALUES ('400', '其他', '0', '/400', '1', '1', '2', '40', '2018-07-31 11:44:40', NULL, NULL, '2018-07-31 11:44:44', '0', '0', '0', '0', '', '1', '1', 'classify');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`) VALUES ('90000', '第三方服务模块', '400', '/400/90000', '1', '2', '2', '20', '2017-07-04 15:55:50', NULL, NULL, '2017-09-08 18:59:10', '0', '0', '0', '0', '', '1', '1', 'classify');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`) VALUES ('90100', '第三方服务模块', '90000', '/400/90000/90100', '1', '3', '2', '10', '2018-07-31 12:10:57', NULL, '14', NULL, '0', '0', '0', '1', '', '1', '1', 'module');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`) VALUES ('92000', '电商', '400', '/400/92000', '1', '2', '2', '10', '2018-07-04 17:22:11', NULL, NULL, '2018-07-04 17:22:20', '0', '0', '0', '0', NULL, '1', '1', 'classify');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`) VALUES ('92100', '微商城', '92000', '/400/92000/92100', '1', '3', '2', '10', '2018-07-04 17:23:28', '{\"url\":\"${stat.biz.server.url}zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=${stat.biz.server.url}nar/biz/web/app/user/index.html?clientrecommend=1#/recommend?_k=zlbiz#sign_suffix\"}', '13', '2018-07-04 17:23:33', '0', '0', '0', '1', NULL, '1', '1', 'module');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`) VALUES ('92200', '食堂', '92000', '/400/92000/92200', '1', '3', '2', '20', '2018-07-31 12:09:15', NULL, '13', '2018-07-31 12:09:25', '0', '0', '0', '1', '', '1', '1', 'module');

-- 将原有的外部链接和电商改成新的模块  add by yanjun  20180811
DROP PROCEDURE IF EXISTS update_url_module_function;
DELIMITER //
CREATE PROCEDURE `update_url_module_function` ()
BEGIN
  DECLARE aid LONG;
  DECLARE ans INTEGER;
  DECLARE aversionid LONG;
	DECLARE aname VARCHAR(200);
	DECLARE adata VARCHAR(10240);
	DECLARE atype INTEGER;
  DECLARE amoduleid LONG;
  DECLARE done INT DEFAULT FALSE;
  DECLARE cur CURSOR FOR SELECT id, namespace_id, version_id, label, action_data, IF(action_type = 'ZuoLinUrl', 13 , 14),  IF(action_type = 'ZuoLinUrl', 92100 , 90100) from eh_portal_items WHERE action_type = 'ThirdUrl' OR action_type = 'ZuoLinUrl';
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
  OPEN cur;
  read_loop: LOOP

        FETCH cur INTO aid, ans, aversionid, aname, adata, atype, amoduleid;
				IF done THEN
					LEAVE read_loop;
				END IF;

				SET @appId = (SELECT MAX(id) + 1 from eh_service_module_apps);

				INSERT INTO `eh_service_module_apps` (`id`, `namespace_id`, `version_id`, `origin_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `create_time`, `update_time`, `operator_uid`, `creator_uid`, `module_control_type`, `custom_tag`, `custom_path`, `access_control_type`) VALUES (@appId, ans, aversionid, @appId, aname, amoduleid, adata, '2', atype, NOW(), NOW(), '1', '1', 'unlimit_control', NULL, NULL, '0');
				UPDATE eh_portal_items SET action_type = 'ModuleApp', action_data = CONCAT('{"moduleAppId":', @appId, '}') WHERE id = aid;

  END LOOP;
  CLOSE cur;
END
//
DELIMITER ;
CALL update_url_module_function;
DROP PROCEDURE IF EXISTS update_url_module_function;



-- 当前版本已被他人抢先发布
set @id = (select MAX(id) FROM eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id := @id + 1), 'portal', '100021', 'zh_CN', '当前版本已被他人抢先发布，请刷新页面后继续操作！');

-- 黄鹏宇 2018年8月20日
-- 科兴同步按钮

INSERT INTO `eh_service_module_functions`(`id`, `module_id`, `privilege_id`, `explain`) VALUES (43960, 21200, 43960, '企业客户管理 全量同步权限');
INSERT INTO `eh_service_module_functions`(`id`, `module_id`, `privilege_id`, `explain`) VALUES (43970, 21100, 43970, '合同管理 全量同步权限');


-- AUTHOR: 黄良铭
-- REMARK: 超级管理员维护时的消息模板
UPDATE  eh_locale_templates s SET s.text='${userName}（${contactToken}）的${organizationName}企业管理员身份已被移除。' 
WHERE s.scope='organization.notification' AND s.code=20;

SET @b_id = (SELECT IFNULL(MAX(id),1) FROM eh_locale_templates);
INSERT INTO eh_locale_templates(id ,scope ,CODE ,locale ,description ,TEXT,namespace_id)
VALUES(@b_id:= @b_id +1 , 'organization.notification',23,'zh_CN','添加超级管理员给当前超级管理员发送的消息模板' ,  '您已成为${organizationName}的超级管理员',0);

INSERT INTO eh_locale_templates(id ,scope ,CODE ,locale ,description ,TEXT,namespace_id)
VALUES(@b_id:= @b_id +1 , 'organization.notification',24,'zh_CN','删除超级管理员给其他超级管理员发送的消息模板' ,  '${userName}（${contactToken}）的${organizationName}超级管理员身份已被移除',0);

INSERT INTO eh_locale_templates(id ,scope ,CODE ,locale ,description ,TEXT,namespace_id)
VALUES(@b_id:= @b_id +1 , 'organization.notification',25,'zh_CN','添加超级管理员给其他管理员发送的消息模板' ,  '${userName}（${contactToken}）已被添加为${organizationName}的超级管理员',0);


-- 黄鹏宇 2018年8月20日
-- 本地化导出标题

set @id = (select max(id)+1 from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id, 'enterpriseCustomer.export', '1', 'zh_CN', '企业客户数据导出');

set @id = (select max(id)+1 from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id, 'contract.export', '1', 'zh_CN', '合同异常数据导出');


-- 黄鹏宇 2018年8月20日
-- 替换
UPDATE `eh_general_form_templates` SET `namespace_id` = 0 WHERE `form_name` = '请示单';

-- AUTHOR: 严军 2018年08月21日10:22:25
-- REMARK: 启用“工作汇报”的普通公司菜单
UPDATE eh_web_menus set `status` = 2 WHERE id = 72070000;

-- AUTHOR: 吴寒 2018年8月21日
-- REMARK: 增加中文
set @id = (select max(id)+1 from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id, 'punch.time', 'nextDay', 'zh_CN', '次日');

-- AUTHOR: 杨崇鑫  20180822
-- REMARK: 物业缴费V6.5 数据迁移账单组
update eh_contract_charging_items as aaa inner join (
select t2.ccid, d.namespace_id, d.charging_item_id,d.charging_standards_id, d.ownerId,d.bill_group_id  from (
select t.ccid, t.namespace_id, t.charging_item_id, t.charging_standard_id, t.community_id, t.category_id, c.asset_category_id from (
select a.id as ccid, a.namespace_id, a.charging_item_id,a.charging_standard_id, b.community_id, b.category_id from eh_contract_charging_items a left join eh_contracts b on a.contract_id=b.id ) as t
left join eh_asset_module_app_mappings c on t.category_id=c.contract_category_id ) as t2
left join eh_payment_bill_groups_rules d on t2.charging_item_id=d.charging_item_id and t2.namespace_id=d.namespace_id and t2.community_id=d.ownerId
) as bbb on aaa.id=bbb.ccid set aaa.bill_group_id=bbb.bill_group_id;


-- AUTHOR: 黄良铭
-- REMARK: 系统管理员维护时的消息模板
SET @b_id = (SELECT IFNULL(MAX(id),1) FROM eh_locale_templates);
INSERT INTO eh_locale_templates(id ,scope ,CODE ,locale ,description ,TEXT,namespace_id)
VALUES(@b_id:= @b_id +1 , 'organization.notification',26,'zh_CN','添加系统管理员给当前系统管理员发送的消息模板' ,  '您在${organizationName}的系统管理员身份已被移除',0);
INSERT INTO eh_locale_templates(id ,scope ,CODE ,locale ,description ,TEXT,namespace_id)
VALUES(@b_id:= @b_id +1 , 'organization.notification',27,'zh_CN','删除系统管理员给其他系统管理员发送的消息模板' ,  '您已成为${organizationName}的系统管理员',0);
INSERT INTO eh_locale_templates(id ,scope ,CODE ,locale ,description ,TEXT,namespace_id)
VALUES(@b_id:= @b_id +1 , 'organization.notification',28,'zh_CN','添加系统管理员给其他管理员发送的消息模板' ,  '${userName}（${contactToken}）的${organizationName}系统管理员身份已被移除',0);
INSERT INTO eh_locale_templates(id ,scope ,CODE ,locale ,description ,TEXT,namespace_id)
VALUES(@b_id:= @b_id +1 , 'organization.notification',29,'zh_CN','添加系统管理员给其他管理员发送的消息模板' ,  '${userName}（${contactToken}）已被添加为${organizationName}的系统管理员',0);
-- --------------------- SECTION END ---------------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本
-- AUTHOR: 杨崇鑫
-- REMARK: 需根据域空间配置是否显示，初始化时配置好，默认不展示
SET @id = ifnull((SELECT MAX(id) FROM `eh_service_module_exclude_functions`),0);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 1000000 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999999 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999996 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999994 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999993 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999992 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999990 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999991 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999989 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999988 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999986 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999985 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999983 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999982 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999981 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999980 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999978 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999977 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 1 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999976 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999975 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999974 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999973 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999972 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999971 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999970 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999969 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999968 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999967 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999965 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999963 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999962 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999961 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999960 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999959 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999958 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999957 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999956 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999955 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999954 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999953 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999952 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999951 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999950 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999948 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999947 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999946 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999945 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999944 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999943 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999942 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999941 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999940 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999939 , NULL, 20400, 101);


-- AUTHOR: dengs
-- REMARK: ISSUE-33347: 新增国贸快递类型，对接新支付
SET @parent_express_company_id = 4;
set @son_express_company_id = 10007;
set @express_company_businesses_id = 8;
set @ns = 999948; -- 需要修改为国贸的域空间id
set @community_id=240111044332060225; -- todo 需要修改为国贸的园区id
INSERT INTO `eh_express_companies` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `logo`, `description`, `logistics_url`, `order_url`, `app_key`, `app_secret`, `authorization`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES (@parent_express_company_id, @ns, 'EhNamespaces', @ns, '0', '国贸物业酒店管理有限公司', '', '国贸项目，国贸快递业务对应的公司', '', '', '', '', '', '2', '0', now(), now(), '0');
INSERT INTO `eh_express_companies` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `logo`, `description`, `logistics_url`, `order_url`, `app_key`, `app_secret`, `authorization`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES (@son_express_company_id, @ns, 'community', @community_id, @parent_express_company_id, '国贸物业酒店管理有限公司', '', '国贸项目，国贸快递业务对应的公司', NULL, NULL, NULL, NULL, NULL, '1', '0', now(), now(), '0');

INSERT INTO `eh_express_company_businesses` (`id`, `namespace_id`, `owner_type`, `owner_id`, `express_company_id`, `send_type`, `send_type_name`, `package_types`, `insured_documents`, `order_status_collections`, `pay_type`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES (@express_company_businesses_id, @ns, 'EhNamespaces', @ns, @parent_express_company_id, '10', '国贸快递', '[]', NULL, '[{\"status\": 6},{\"status\": 5},{\"status\": 4}]', '3', '2', '0', now(), now(), '0');

-- 删除快递的参数配置权限
DELETE from eh_acl_privileges WHERE id = '4070040710';
DELETE from eh_service_module_privileges WHERE module_id = '40710';

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: dev
-- DESCRIPTION: 此SECTION只在开发库中执行的脚本
-- AUTHOR:
-- REMARK:

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zijing
-- DESCRIPTION: 此SECTION只在清华信息港(紫荆)-999984执行的脚本
-- AUTHOR: 杨崇鑫
-- REMARK: 需根据域空间配置是否显示，初始化时配置好，默认不展示
SET @id = ifnull((SELECT MAX(id) FROM `eh_service_module_exclude_functions`),0);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999984 , NULL, 20400, 101);

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guangda
-- DESCRIPTION: 此SECTION只在光大-999979执行的脚本
-- AUTHOR: 杨崇鑫
-- REMARK: 需根据域空间配置是否显示，初始化时配置好，默认不展示
SET @id = ifnull((SELECT MAX(id) FROM `eh_service_module_exclude_functions`),0);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999979 , NULL, 20400, 101);

-- AUTHOR: Ryan
-- REMARK: 由纲哥需求在所有域空间增加 “公文收发” 审批模板，此sql需在5.6.0以上版本的代码执行
INSERT INTO `eh_enterprise_approval_groups` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `status`, `group_attribute`, `approval_icon`, `operator_uid`, `operator_time`) VALUES ('10', '0', NULL, '0', '公文收发', '1', 'CUSTOMIZE', 'cs://1/image/aW1hZ2UvTVRwak1UazVOekZpT0dZNU1UVTRZMlpoTjJKak56WXpOMlUyWVRobFpqa3hZZw', '1', '2018-07-31 09:00:00');
UPDATE eh_enterprise_approval_groups SET id = 20 WHERE id = 1;
UPDATE eh_enterprise_approval_groups SET id = 30 WHERE id = 2;
UPDATE eh_enterprise_approval_groups SET id = 40 WHERE id = 3;
UPDATE eh_enterprise_approval_groups SET id = 50 WHERE id = 4;
UPDATE eh_enterprise_approval_groups SET id = 60 WHERE id = 5;

UPDATE eh_enterprise_approval_templates SET group_id = 20 WHERE group_id = 1;
UPDATE eh_enterprise_approval_templates SET group_id = 30 WHERE group_id = 2;
UPDATE eh_enterprise_approval_templates SET group_id = 40 WHERE group_id = 3;
UPDATE eh_enterprise_approval_templates SET group_id = 50 WHERE group_id = 4;
UPDATE eh_enterprise_approval_templates SET group_id = 60 WHERE group_id = 5;

UPDATE eh_general_approvals SET integral_tag1 = 20 WHERE integral_tag1 = 1 AND module_id = 52000;
UPDATE eh_general_approvals SET integral_tag1 = 30 WHERE integral_tag1 = 2 AND module_id = 52000;
UPDATE eh_general_approvals SET integral_tag1 = 40 WHERE integral_tag1 = 3 AND module_id = 52000;
UPDATE eh_general_approvals SET integral_tag1 = 50 WHERE integral_tag1 = 4 AND module_id = 52000;
UPDATE eh_general_approvals SET integral_tag1 = 60 WHERE integral_tag1 = 5 AND module_id = 52000;

INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('15', '0', '0', 'EhOrganizaions', '0', '52000', 'any-module', '0', NULL, '15', '0', '发文（红头文件）', NULL, '10', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRwallUUTBNR05rTkdZMFptVXpaVEJoT1RZNU9EZ3dOMkpsTkRjNU9XTTROZw', '2018-07-31 14:49:45', '2018-07-31 14:49:45');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('16', '0', '0', 'EhOrganizaions', '0', '52000', 'any-module', '0', NULL, '16', '0', '发文（非红头文件）', NULL, '10', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRwallUUTBNR05rTkdZMFptVXpaVEJoT1RZNU9EZ3dOMkpsTkRjNU9XTTROZw', '2018-07-31 14:49:45', '2018-07-31 14:49:45');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('17', '0', '0', 'EhOrganizaions', '0', '52000', 'any-module', '0', NULL, '17', '0', '行政收文（紧急）', NULL, '10', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRvM00yTXlOek0yTnpnNU0yVXhOR1JtWW1Gak9Ea3dZemxpWkdFMU9EVXpaQQ', '2018-07-31 14:49:45', '2018-07-31 14:49:45');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('18', '0', '0', 'EhOrganizaions', '0', '52000', 'any-module', '0', NULL, '18', '0', '行政收文', NULL, '10', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRvM00yTXlOek0yTnpnNU0yVXhOR1JtWW1Gak9Ea3dZemxpWkdFMU9EVXpaQQ', '2018-07-31 14:49:45', '2018-07-31 14:49:45');

INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('15', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '红头文件发文申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"文件标题\",\"fieldExtra\":\"{}\",\"fieldName\":\"文件标题\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"发文号\",\"fieldExtra\":\"{}\",\"fieldName\":\"发文号\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"主送单位\",\"fieldExtra\":\"{}\",\"fieldName\":\"主送单位\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"抄送单位\",\"fieldExtra\":\"{}\",\"fieldName\":\"抄送单位\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"文件上传\",\"fieldExtra\":\"{\\\"limitCount\\\":3,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"文件上传\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"文件份数\",\"fieldExtra\":\"{\\\"defaultValue\\\":\\\"\\\"}\",\"fieldName\":\"文件份数\",\"fieldType\":\"NUMBER_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"NUM_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-07-31 14:57:08', '2018-07-31 14:57:08');
INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('16', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '非红头文件发文申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"文件标题\",\"fieldExtra\":\"{}\",\"fieldName\":\"文件标题\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"文件上传\",\"fieldExtra\":\"{\\\"limitCount\\\":3,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"文件上传\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请选择\",\"fieldDisplayName\":\"是否盖章\",\"fieldExtra\":\"{\\\"selectValue\\\":[\\\"是\\\",\\\"否\\\"]}\",\"fieldName\":\"是否盖章\",\"fieldType\":\"DROP_BOX\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"备注信息\",\"fieldExtra\":\"{}\",\"fieldName\":\"备注信息\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-07-31 14:57:08', '2018-07-31 14:57:08');
INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('17', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '行政收文（紧急）申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"发文单位\",\"fieldExtra\":\"{}\",\"fieldName\":\"发文单位\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请选择\",\"fieldDisplayName\":\"收文日期\",\"fieldExtra\":\"{\\\"type\\\":\\\"DATE\\\"}\",\"fieldName\":\"收文日期\",\"fieldType\":\"DATE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"收文编号\",\"fieldExtra\":\"{}\",\"fieldName\":\"收文编号\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"收文标题\",\"fieldExtra\":\"{}\",\"fieldName\":\"收文标题\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"文件上传\",\"fieldExtra\":\"{\\\"limitCount\\\":3,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"文件上传\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"备注信息\",\"fieldExtra\":\"{}\",\"fieldName\":\"备注信息\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-07-31 14:57:08', '2018-07-31 14:57:08');
INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('18', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '行政收文申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"发文单位\",\"fieldExtra\":\"{}\",\"fieldName\":\"发文单位\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请选择\",\"fieldDisplayName\":\"收文日期\",\"fieldExtra\":\"{\\\"type\\\":\\\"DATE\\\"}\",\"fieldName\":\"收文日期\",\"fieldType\":\"DATE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"收文编号\",\"fieldExtra\":\"{}\",\"fieldName\":\"收文编号\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"收文标题\",\"fieldExtra\":\"{}\",\"fieldName\":\"收文标题\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"文件上传\",\"fieldExtra\":\"{\\\"limitCount\\\":3,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"文件上传\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"备注信息\",\"fieldExtra\":\"{}\",\"fieldName\":\"备注信息\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-07-31 14:57:08', '2018-07-31 14:57:08');

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: szbay
-- DESCRIPTION: 此SECTION只在深圳湾-999966执行的脚本
-- AUTHOR: 杨崇鑫
-- REMARK: 需根据域空间配置是否显示，初始化时配置好，默认不展示
SET @id = ifnull((SELECT MAX(id) FROM `eh_service_module_exclude_functions`),0);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999966 , NULL, 20400, 101);
-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: chuangyechang
-- DESCRIPTION: 此SECTION只在成都创业场-999964执行的脚本
-- AUTHOR: 杨崇鑫
-- REMARK: 需根据域空间配置是否显示，初始化时配置好，默认不展示
SET @id = ifnull((SELECT MAX(id) FROM `eh_service_module_exclude_functions`),0);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999964 , NULL, 20400, 101);

-- AUTHOR: Ryan
-- REMARK: 由纲哥需求在所有域空间增加 “公文收发” 审批模板，此sql需在5.6.0以上版本的代码执行
INSERT INTO `eh_enterprise_approval_groups` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `status`, `group_attribute`, `approval_icon`, `operator_uid`, `operator_time`) VALUES ('10', '0', NULL, '0', '公文收发', '1', 'CUSTOMIZE', 'cs://1/image/aW1hZ2UvTVRwak1UazVOekZpT0dZNU1UVTRZMlpoTjJKak56WXpOMlUyWVRobFpqa3hZZw', '1', '2018-07-31 09:00:00');
UPDATE eh_enterprise_approval_groups SET id = 20 WHERE id = 1;
UPDATE eh_enterprise_approval_groups SET id = 30 WHERE id = 2;
UPDATE eh_enterprise_approval_groups SET id = 40 WHERE id = 3;
UPDATE eh_enterprise_approval_groups SET id = 50 WHERE id = 4;
UPDATE eh_enterprise_approval_groups SET id = 60 WHERE id = 5;

UPDATE eh_enterprise_approval_templates SET group_id = 20 WHERE group_id = 1;
UPDATE eh_enterprise_approval_templates SET group_id = 30 WHERE group_id = 2;
UPDATE eh_enterprise_approval_templates SET group_id = 40 WHERE group_id = 3;
UPDATE eh_enterprise_approval_templates SET group_id = 50 WHERE group_id = 4;
UPDATE eh_enterprise_approval_templates SET group_id = 60 WHERE group_id = 5;

UPDATE eh_general_approvals SET integral_tag1 = 20 WHERE integral_tag1 = 1 AND module_id = 52000;
UPDATE eh_general_approvals SET integral_tag1 = 30 WHERE integral_tag1 = 2 AND module_id = 52000;
UPDATE eh_general_approvals SET integral_tag1 = 40 WHERE integral_tag1 = 3 AND module_id = 52000;
UPDATE eh_general_approvals SET integral_tag1 = 50 WHERE integral_tag1 = 4 AND module_id = 52000;
UPDATE eh_general_approvals SET integral_tag1 = 60 WHERE integral_tag1 = 5 AND module_id = 52000;

INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('15', '0', '0', 'EhOrganizaions', '0', '52000', 'any-module', '0', NULL, '15', '0', '发文（红头文件）', NULL, '10', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRwallUUTBNR05rTkdZMFptVXpaVEJoT1RZNU9EZ3dOMkpsTkRjNU9XTTROZw', '2018-07-31 14:49:45', '2018-07-31 14:49:45');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('16', '0', '0', 'EhOrganizaions', '0', '52000', 'any-module', '0', NULL, '16', '0', '发文（非红头文件）', NULL, '10', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRwallUUTBNR05rTkdZMFptVXpaVEJoT1RZNU9EZ3dOMkpsTkRjNU9XTTROZw', '2018-07-31 14:49:45', '2018-07-31 14:49:45');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('17', '0', '0', 'EhOrganizaions', '0', '52000', 'any-module', '0', NULL, '17', '0', '行政收文（紧急）', NULL, '10', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRvM00yTXlOek0yTnpnNU0yVXhOR1JtWW1Gak9Ea3dZemxpWkdFMU9EVXpaQQ', '2018-07-31 14:49:45', '2018-07-31 14:49:45');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('18', '0', '0', 'EhOrganizaions', '0', '52000', 'any-module', '0', NULL, '18', '0', '行政收文', NULL, '10', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRvM00yTXlOek0yTnpnNU0yVXhOR1JtWW1Gak9Ea3dZemxpWkdFMU9EVXpaQQ', '2018-07-31 14:49:45', '2018-07-31 14:49:45');

INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('15', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '红头文件发文申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"文件标题\",\"fieldExtra\":\"{}\",\"fieldName\":\"文件标题\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"发文号\",\"fieldExtra\":\"{}\",\"fieldName\":\"发文号\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"主送单位\",\"fieldExtra\":\"{}\",\"fieldName\":\"主送单位\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"抄送单位\",\"fieldExtra\":\"{}\",\"fieldName\":\"抄送单位\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"文件上传\",\"fieldExtra\":\"{\\\"limitCount\\\":3,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"文件上传\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"文件份数\",\"fieldExtra\":\"{\\\"defaultValue\\\":\\\"\\\"}\",\"fieldName\":\"文件份数\",\"fieldType\":\"NUMBER_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"NUM_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-07-31 14:57:08', '2018-07-31 14:57:08');
INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('16', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '非红头文件发文申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"文件标题\",\"fieldExtra\":\"{}\",\"fieldName\":\"文件标题\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"文件上传\",\"fieldExtra\":\"{\\\"limitCount\\\":3,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"文件上传\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请选择\",\"fieldDisplayName\":\"是否盖章\",\"fieldExtra\":\"{\\\"selectValue\\\":[\\\"是\\\",\\\"否\\\"]}\",\"fieldName\":\"是否盖章\",\"fieldType\":\"DROP_BOX\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"备注信息\",\"fieldExtra\":\"{}\",\"fieldName\":\"备注信息\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-07-31 14:57:08', '2018-07-31 14:57:08');
INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('17', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '行政收文（紧急）申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"发文单位\",\"fieldExtra\":\"{}\",\"fieldName\":\"发文单位\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请选择\",\"fieldDisplayName\":\"收文日期\",\"fieldExtra\":\"{\\\"type\\\":\\\"DATE\\\"}\",\"fieldName\":\"收文日期\",\"fieldType\":\"DATE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"收文编号\",\"fieldExtra\":\"{}\",\"fieldName\":\"收文编号\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"收文标题\",\"fieldExtra\":\"{}\",\"fieldName\":\"收文标题\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"文件上传\",\"fieldExtra\":\"{\\\"limitCount\\\":3,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"文件上传\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"备注信息\",\"fieldExtra\":\"{}\",\"fieldName\":\"备注信息\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-07-31 14:57:08', '2018-07-31 14:57:08');
INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('18', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '行政收文申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"发文单位\",\"fieldExtra\":\"{}\",\"fieldName\":\"发文单位\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请选择\",\"fieldDisplayName\":\"收文日期\",\"fieldExtra\":\"{\\\"type\\\":\\\"DATE\\\"}\",\"fieldName\":\"收文日期\",\"fieldType\":\"DATE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"收文编号\",\"fieldExtra\":\"{}\",\"fieldName\":\"收文编号\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"收文标题\",\"fieldExtra\":\"{}\",\"fieldName\":\"收文标题\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"文件上传\",\"fieldExtra\":\"{\\\"limitCount\\\":3,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"文件上传\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"备注信息\",\"fieldExtra\":\"{}\",\"fieldName\":\"备注信息\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-07-31 14:57:08', '2018-07-31 14:57:08');
-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: anbang
-- DESCRIPTION: 此SECTION只在安邦物业-999949执行的脚本
-- AUTHOR: 杨崇鑫
-- REMARK: 需根据域空间配置是否显示，初始化时配置好，默认不展示
SET @id = ifnull((SELECT MAX(id) FROM `eh_service_module_exclude_functions`),0);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999949 , NULL, 20400, 101);

-- AUTHOR: Ryan
-- REMARK: 由纲哥需求在所有域空间增加 “公文收发” 审批模板，此sql需在5.6.0以上版本的代码执行
INSERT INTO `eh_enterprise_approval_groups` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `status`, `group_attribute`, `approval_icon`, `operator_uid`, `operator_time`) VALUES ('10', '0', NULL, '0', '公文收发', '1', 'CUSTOMIZE', 'cs://1/image/aW1hZ2UvTVRwak1UazVOekZpT0dZNU1UVTRZMlpoTjJKak56WXpOMlUyWVRobFpqa3hZZw', '1', '2018-07-31 09:00:00');
UPDATE eh_enterprise_approval_groups SET id = 20 WHERE id = 1;
UPDATE eh_enterprise_approval_groups SET id = 30 WHERE id = 2;
UPDATE eh_enterprise_approval_groups SET id = 40 WHERE id = 3;
UPDATE eh_enterprise_approval_groups SET id = 50 WHERE id = 4;
UPDATE eh_enterprise_approval_groups SET id = 60 WHERE id = 5;

UPDATE eh_enterprise_approval_templates SET group_id = 20 WHERE group_id = 1;
UPDATE eh_enterprise_approval_templates SET group_id = 30 WHERE group_id = 2;
UPDATE eh_enterprise_approval_templates SET group_id = 40 WHERE group_id = 3;
UPDATE eh_enterprise_approval_templates SET group_id = 50 WHERE group_id = 4;
UPDATE eh_enterprise_approval_templates SET group_id = 60 WHERE group_id = 5;

UPDATE eh_general_approvals SET integral_tag1 = 20 WHERE integral_tag1 = 1 AND module_id = 52000;
UPDATE eh_general_approvals SET integral_tag1 = 30 WHERE integral_tag1 = 2 AND module_id = 52000;
UPDATE eh_general_approvals SET integral_tag1 = 40 WHERE integral_tag1 = 3 AND module_id = 52000;
UPDATE eh_general_approvals SET integral_tag1 = 50 WHERE integral_tag1 = 4 AND module_id = 52000;
UPDATE eh_general_approvals SET integral_tag1 = 60 WHERE integral_tag1 = 5 AND module_id = 52000;

INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('15', '0', '0', 'EhOrganizaions', '0', '52000', 'any-module', '0', NULL, '15', '0', '发文（红头文件）', NULL, '10', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRwallUUTBNR05rTkdZMFptVXpaVEJoT1RZNU9EZ3dOMkpsTkRjNU9XTTROZw', '2018-07-31 14:49:45', '2018-07-31 14:49:45');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('16', '0', '0', 'EhOrganizaions', '0', '52000', 'any-module', '0', NULL, '16', '0', '发文（非红头文件）', NULL, '10', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRwallUUTBNR05rTkdZMFptVXpaVEJoT1RZNU9EZ3dOMkpsTkRjNU9XTTROZw', '2018-07-31 14:49:45', '2018-07-31 14:49:45');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('17', '0', '0', 'EhOrganizaions', '0', '52000', 'any-module', '0', NULL, '17', '0', '行政收文（紧急）', NULL, '10', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRvM00yTXlOek0yTnpnNU0yVXhOR1JtWW1Gak9Ea3dZemxpWkdFMU9EVXpaQQ', '2018-07-31 14:49:45', '2018-07-31 14:49:45');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('18', '0', '0', 'EhOrganizaions', '0', '52000', 'any-module', '0', NULL, '18', '0', '行政收文', NULL, '10', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRvM00yTXlOek0yTnpnNU0yVXhOR1JtWW1Gak9Ea3dZemxpWkdFMU9EVXpaQQ', '2018-07-31 14:49:45', '2018-07-31 14:49:45');

INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('15', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '红头文件发文申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"文件标题\",\"fieldExtra\":\"{}\",\"fieldName\":\"文件标题\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"发文号\",\"fieldExtra\":\"{}\",\"fieldName\":\"发文号\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"主送单位\",\"fieldExtra\":\"{}\",\"fieldName\":\"主送单位\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"抄送单位\",\"fieldExtra\":\"{}\",\"fieldName\":\"抄送单位\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"文件上传\",\"fieldExtra\":\"{\\\"limitCount\\\":3,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"文件上传\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"文件份数\",\"fieldExtra\":\"{\\\"defaultValue\\\":\\\"\\\"}\",\"fieldName\":\"文件份数\",\"fieldType\":\"NUMBER_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"NUM_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-07-31 14:57:08', '2018-07-31 14:57:08');
INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('16', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '非红头文件发文申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"文件标题\",\"fieldExtra\":\"{}\",\"fieldName\":\"文件标题\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"文件上传\",\"fieldExtra\":\"{\\\"limitCount\\\":3,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"文件上传\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请选择\",\"fieldDisplayName\":\"是否盖章\",\"fieldExtra\":\"{\\\"selectValue\\\":[\\\"是\\\",\\\"否\\\"]}\",\"fieldName\":\"是否盖章\",\"fieldType\":\"DROP_BOX\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"备注信息\",\"fieldExtra\":\"{}\",\"fieldName\":\"备注信息\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-07-31 14:57:08', '2018-07-31 14:57:08');
INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('17', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '行政收文（紧急）申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"发文单位\",\"fieldExtra\":\"{}\",\"fieldName\":\"发文单位\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请选择\",\"fieldDisplayName\":\"收文日期\",\"fieldExtra\":\"{\\\"type\\\":\\\"DATE\\\"}\",\"fieldName\":\"收文日期\",\"fieldType\":\"DATE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"收文编号\",\"fieldExtra\":\"{}\",\"fieldName\":\"收文编号\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"收文标题\",\"fieldExtra\":\"{}\",\"fieldName\":\"收文标题\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"文件上传\",\"fieldExtra\":\"{\\\"limitCount\\\":3,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"文件上传\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"备注信息\",\"fieldExtra\":\"{}\",\"fieldName\":\"备注信息\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-07-31 14:57:08', '2018-07-31 14:57:08');
INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('18', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '行政收文申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"发文单位\",\"fieldExtra\":\"{}\",\"fieldName\":\"发文单位\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请选择\",\"fieldDisplayName\":\"收文日期\",\"fieldExtra\":\"{\\\"type\\\":\\\"DATE\\\"}\",\"fieldName\":\"收文日期\",\"fieldType\":\"DATE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"收文编号\",\"fieldExtra\":\"{}\",\"fieldName\":\"收文编号\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"收文标题\",\"fieldExtra\":\"{}\",\"fieldName\":\"收文标题\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"文件上传\",\"fieldExtra\":\"{\\\"limitCount\\\":3,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"文件上传\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"备注信息\",\"fieldExtra\":\"{}\",\"fieldName\":\"备注信息\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-07-31 14:57:08', '2018-07-31 14:57:08');


-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.3 
SET @eh_locale_strings_id = (SELECT MAX(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'assetv2', '10021', 'zh_CN', '删除失败，该收费项标准已经关联合同，或者产生了账单');



-- --------------------- SECTION END ---------------------------------------------------------


