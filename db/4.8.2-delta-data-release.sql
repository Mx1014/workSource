
-- merge 4.8.1 迁移过来且还未上线的脚本  add by sfyan 20170821

-- 初始化range字段 by st.zheng
update `eh_service_alliances` set `range`=owner_id where owner_type='community';

update `eh_service_alliances` set `range`='all' where owner_type='orgnization';

-- 将原来的审批置为删除 by.st.zheng
update `eh_service_alliance_jump_module` set `signal`=0 where module_name = '审批';

set @eh_service_alliance_jump_id = (select max(id) from eh_service_alliance_jump_module);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `module_name`,`signal`, `module_url`, `parent_id`)
VALUES (@eh_service_alliance_jump_id + 1, '审批', 2,'zl://approval/create?approvalId={}&sourceId={}', '0');

-- 更改菜单 by.st.zheng
UPDATE `eh_web_menus` SET `data_type`='react:/approval-management/approval-list/40500/EhOrganizations' WHERE `id`='40541' and name='审批列表';

--
-- 用户输入内容变量 add by xq.tian  2017/08/05
--
SELECT MAX(id) FROM `eh_flow_variables` INTO @flow_variables_id;
INSERT INTO `eh_flow_variables` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ((@flow_variables_id := @flow_variables_id + 1), 0, 0, '', 0, '', 'text_button_msg_input_content', '文本备注内容', 'text_button_msg', 'bean_id', 'flow-variable-text-button-msg-user-input-content', 1);




-- merge from msg-2.1
-- 二维码页面  add by yanjun 20170727
SET @id = (SELECT MAX(id) FROM eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES( @id:= @id + 1,'group.scanJoin.url','/mobile/static/message/src/addGroup.html','group.scanJoin.url','0',NULL);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES( @id:= @id + 1,'group.scanDownload.url','/mobile/static/qq_down/index.html','group.scanDownload.url','0',NULL);

-- 群聊默认名称   add by yanjun 20170801
SET @id = (SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id + 1),'group.notification','50','zh_CN','你邀请用户加入群','你邀请${userNameList}加入了群聊','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id + 1),'group.notification','51','zh_CN','其他用户邀请用户加入群','${inviterName}邀请${userNameList}加入了群聊','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id + 1),'group.notification','52','zh_CN','被邀请入群','${inviterName}邀请你加入了群聊','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id + 1),'group.notification','53','zh_CN','退出群聊','${userName}已退出群聊','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id + 1),'group.notification','54','zh_CN','退出群聊','群聊名称已修改为“${groupName}”','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id + 1),'group.notification','55','zh_CN','删除成员','你将${userNameList}移出了群聊','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id + 1),'group.notification','56','zh_CN','删除成员','你被${userName}移出了群聊','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id + 1),'group.notification','57','zh_CN','解散群','你加入的群聊“${groupName}”已解散','0');

SET @id = (SELECT MAX(id) FROM eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES((@id := @id + 1),'group','20001','zh_CN','群聊');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES((@id := @id + 1),'group','20002','zh_CN','你已成为新群主');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES((@id := @id + 1),'group','20003','zh_CN','你通过扫描二维码加入了群聊');

-- 活动分享页app信息栏-volgo更新为“工作中的生活态度”  add by yanjun 20170815
UPDATE eh_app_urls set description = '工作中的生活态度' where namespace_id = 1;

-- 更新老数据，以前如果一个地址有人加入后新建group时取的是0域空间。现在要跟新过来，使用实际的域空间  add by yanjun 20170816
update eh_groups set namespace_id = 999994 where id =   179181 and namespace_id =   0 ;
update eh_groups set namespace_id = 999994 where id =   179184 and namespace_id =   0 ;
update eh_groups set namespace_id = 999994 where id =   179185 and namespace_id =   0 ;
update eh_groups set namespace_id = 999994 where id =   179187 and namespace_id =   0 ;
update eh_groups set namespace_id = 999994 where id =   179218 and namespace_id =   0 ;
update eh_groups set namespace_id = 999988 where id =  1004270 and namespace_id =   0 ;
update eh_groups set namespace_id = 999992 where id =  1007151 and namespace_id =   0 ;
update eh_groups set namespace_id = 999992 where id =  1009351 and namespace_id =   0 ;
update eh_groups set namespace_id = 999992 where id =  1010196 and namespace_id =   0 ;
update eh_groups set namespace_id = 999992 where id =  1020489 and namespace_id =   0 ;
update eh_groups set namespace_id = 999992 where id =  1021332 and namespace_id =   0 ;
update eh_groups set namespace_id = 999992 where id =  1041837 and namespace_id =   0 ;
update eh_groups set namespace_id = 999975 where id =  1041838 and namespace_id =   0 ;
update eh_groups set namespace_id = 999992 where id =  1041989 and namespace_id =   0 ;

-- 更新数据，老数据将企业群设置为公有圈，现将其为私有。 add by yanjun 20170817
UPDATE  eh_groups set private_flag = 1 where discriminator = 'enterprise';



-- 黑名单 add by sfyan 20170817
delete from `eh_acl_privileges` where id in (10079, 10090);
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values('30600','黑名单管理','30000','/30000/30600','1','2','2','0',now());
set @service_module_scope_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
insert into `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `apply_policy`) values((@service_module_scope_id := @service_module_scope_id + 1), 999983, 30600, 2);
insert into `eh_acl_privileges` (`id`, `app_id`, `name`, `description`) values(10090,'0','black.list.super','黑名单超级权限');
set @module_privilege_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
insert into `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) values((@module_privilege_id := @module_privilege_id + 1),'30600','1','10090','黑名单管理 管理员','0',now());


-- 以上是4.8.1的脚本


-- merge portal_admin的脚本 add by sfyan 20170821

insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('4000000','域空间管理','0','icon-cube_zuolin','scope-management','1','2','/4000000','zuolin','30','0','1','namespace','classify');
insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('4100000','域空间配置','4000000',NULL,'scope-config','1','2','/4000000/4100000','zuolin','1','0','2','namespace','classify');
insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('4110000','业务应用配置','4100000',NULL,'business-app-config','1','2','/4000000/4100000/4110000','zuolin','1','70100','3','namespace','module');
insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('4120000','app配置','4100000',NULL,'app-config','1','2','/4000000/4100000/4120000','zuolin','2','70200','3','namespace','module');
insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('4111000','业务应用配置','4110000',NULL,'business-app-config','0','2','/4000000/4100000/4110000/4111000','zuolin','1','70100','4','namespace','page');
insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('4121000','APP配置','4120000',NULL,'app-config','0','2','/4000000/4100000/4120000/4121000','zuolin','1','70200','4','namespace','page');


insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('5000000','运营业务模块管理','0','icon-report_zuolin','operational-business-module-management','1','2','/5000000','zuolin','10','0','1','system','classify');
insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('5100000','第三方服务模块','5000000',NULL,'other-service-modules','1','2','/5000000/5100000','zuolin','1','0','2','system','module');
insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('5110000','第三方服务模块','5100000',NULL,'other-service-modules','0','2','/5000000/5100000/5110000','zuolin','1',NULL,'3','namespace','page');


insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('1000000','系统管理','0','icon-setting_zuolin','system-management','1','2','/1000000','zuolin','100','0','1','system','classify');
insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('1100000','管理员管理','1000000',NULL,'admin-management','1','2','/1000000/1100000','zuolin','1','60100','2','system','module');
insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('1110000','管理员列表','1100000',NULL,'administrator-list','0','2','/1000000/1100000/1110000','zuolin','1','60100','3','system','page');
insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('1120000','管理员角色与权限','1100000',NULL,'administrator-roles-permissions','0','2','/1000000/1100000/1120000','zuolin','2','60100','3','system','page');


insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('2000000','物业服务','0','icon-wrench_zuolin','property-service','1','2','/2000000','zuolin','50','0','1','project','classify');
insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('2100000','物业报修','2000000',NULL,'property-repairs','1','2','/2000000/2100000','zuolin','1','20100','2','project','module');
insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('2110000','物业报修','2100000',NULL,'property-repairs','1','2','/2000000/2100000/2110000','zuolin','1','210000','3','project','page');

-- insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('211100','任务列表','211000',NULL,'task-list','0','2','/200000/210000/211000/211100','zuolin','0','210000','4','project','page');
-- insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('211200','信息统计','211000',NULL,'task-statistics','0','2','/200000/210000/211000/211200','zuolin','0','210000','4','project','page');
-- insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('211300','工作流设置','211000',NULL,'workflow-setting','0','2','/200000/210000/211000/211300','zuolin','0','210000','4','project','page');

insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('2200000','能耗管理','2000000',NULL,'energy-management','1','2','/2000000/2200000','zuolin','2','49100','2','project','module');
insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('2210000','能耗管理','2200000',NULL,'energy-management','1','2','/2000000/2200000/2210000','zuolin','2','49100','3','project','page');


insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('6000000','日志管理','0','icon-cube_zuolin','logs','1','2','/6000000','zuolin','70','0','1','system','module');
insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('6100000','日志管理','6000000',NULL,'log-parse','1','2','/6000000/6100000','zuolin','5','0','2','system','page');

DELETE FROM eh_service_modules WHERE status = 2;

insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('10000','基础运营服务','0','/10000','1','1','2','0','2016-12-06 11:40:50',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('10100','论坛/公告','10000','/10000/10100','1','2','2','0','2016-12-06 11:40:50',NULL,NULL,'0','0',NULL,NULL,'1');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('10600','活动','10000','/10000/10600','1','2','2','0','2016-12-06 11:40:50','{\"categoryId\":1,\"publishPrivilege\":1,\"livePrivilege\":2,\"listStyle\":2,\"scope\":2,\"style\":4,\"title\": \"白领活动\"}',NULL,'0','0',NULL,'61','1');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('10700','路演直播','10000','/10000/10700','1','2','0','0','2016-12-06 11:40:50',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('10750','俱乐部','10000','/10000/10750','1','2','2','0','2016-12-06 11:43:04',NULL,NULL,'0','0',NULL,'42','0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('11000','一键推送','10000','/10000/11000','1','2','2','0','2016-12-06 11:40:50',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('12200','短信推送','10000','/10000/12200','1','2','2','0','2016-12-06 11:43:45',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('41000','门禁','10000','/10000/41000','1','2','2','0','2016-12-06 11:40:51','{\"isSupportQR\":1,\"isSupportSmart\":0}',NULL,'0','0',NULL,'40','0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('51000','举报管理','10000','/10000/51000','1','2','2','0','2017-05-10 06:05:34',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('13000','任务管理','10000','/10000/13000','1','2','2','0','2017-04-10 10:54:08',NULL,NULL,'0','0',NULL,NULL,'0');




insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('20000','物业管控服务','0','/20000','1','1','2','0','2016-12-06 11:40:50',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('20400','物业缴费','20000','/20000/20400','1','2','2','0','2016-12-06 11:40:50','{\"url\":\"http://janson.lab.everhomes.com/energy-management/index.html?hideNavigationBar=1#/address_choose#sign_suffix\"}',NULL,'0','0',NULL,'13','0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('20600','品质核查','20000','/20000/20600','1','2','2','0','2016-12-06 11:40:51','{\"realm\":\"quality\",\"entryUrl\":\"http://janson.lab.everhomes.com/nar/quality/index.html?hideNavigationBar=1#/task_list#sign_suffix\"}',NULL,'0','0',NULL,'44','0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('20800','设备巡检','20000','/20000/20800','1','2','2','0','2016-12-06 11:40:51','{\"url\":\"http://janson.lab.everhomes.com/energy-management/index.html?hideNavigationBar=1#/address_choose#sign_suffix\"}',NULL,'0','0',NULL,'13','0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('21000','仓库管理','20000','/20000/21000','1','2','2','0','2017-05-27 10:16:28',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('30500','资产管理','20000','/20000/30500','1','2','2','0','2016-12-06 11:40:51',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('32500','合同管理','20000','/20000/32500','1','2','2','0','2016-12-06 11:43:19',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('33000','企业管理','20000','/20000/33000','1','2','2','0','2016-12-06 11:40:51',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('37000','客户资料','20000','/20000/37000','1','2','2','0','2017-05-11 20:08:38',NULL,NULL,'0','0',NULL,NULL,NULL);
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('49100','能耗管理','20000','/20000/49100','1','2','2','0','2016-12-06 11:46:41','{\"url\":\"http://janson.lab.everhomes.com/energy-management/index.html?hideNavigationBar=1#/address_choose#sign_suffix\"}',NULL,'0','0',NULL,'13','0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('30600','黑名单管理','20000','/20000/30600','1','2','2','0','2016-12-06 11:46:41',NULL,NULL,'0','0',NULL,NULL,'0');


insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('40000','园区运营服务','0','/40000','1','1','2','0','2016-12-06 11:40:51',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('20100','物业报修','40000','/40000/20100','1','2','2','0','2016-12-06 11:40:50','{\"url\":\"zl://propertyrepair/create?type=user&taskCategoryId=0&displayName=物业报修\"}',NULL,'0','0',NULL,'60','1');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('35000','用户认证','40000','/40000/35000','1','2','2','0','2016-12-06 11:40:51',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('34000','用户管理','40000','/40000/34000','1','2','2','0','2016-12-06 11:40:51',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('40050','位置预订','40000','/40000/40050','1','2','0','0','2017-03-07 11:27:15',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('40100','园区入驻','40000','/40000/40100','1','2','2','0','2016-12-06 11:40:51','',NULL,'0','0',NULL,'28','0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('10400','banner管理','40000','/40000/10400','1','2','2','0','2016-12-06 11:40:50',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('40200','工位预订','40000','/40000/40200','1','2','2','0','2016-12-06 11:40:51','{\"url\":\"http://janson.lab.everhomes.com/energy-management/index.html?hideNavigationBar=1#/address_choose#sign_suffix\"}',NULL,'0','0',NULL,'13','0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('40300','服务热线','40000','/40000/40300','1','2','2','0','2016-12-06 11:40:51','',NULL,'0','0',NULL,'45','0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('40400','资源预约','40000','/40000/40400','1','2','2','0','2016-12-06 11:40:51','{\"resourceTypeId\":8,\"pageType\":0}',NULL,'0','0',NULL,'49','1');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('40500','服务联盟','40000','/40000/40500','1','2','2','0','2016-12-06 11:40:51','{\"type\":2,\"parentId\":100001}',NULL,'0','0',NULL,'33','1');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('40600','创客空间','40000','/40000/40600','1','2','2','0','2016-12-06 11:40:51','{\"type\":1,\"forumId\":177000,\"categoryId\":1003,\"parentId\":110001,\"tag\":\"创客\"}',NULL,'0','0',NULL,'32','0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('40700','快递服务','40000','/40000/40700','1','2','2','0','2017-07-18 03:51:18','{\"url\":\"http://janson.lab.everhomes.com/energy-management/index.html?hideNavigationBar=1#/address_choose#sign_suffix\"}','2017-07-18 03:51:18','0','0',NULL,'13','0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('40750','运营统计','40000','/40000/40750','1','2','0','0','2016-12-06 11:40:51',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('40800','停车缴费','40000','/40000/40800','1','2','2','0','2016-12-06 11:40:51','',NULL,'0','0',NULL,'30','0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('40900','车辆管理','40000','/40000/40900','1','2','2','0','2016-12-06 11:40:51',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('41100','一键上网','40000','/40000/41100','1','2','2','0','2016-12-06 11:40:51','',NULL,'0','0',NULL,'47','0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('41200','一卡通','40000','/40000/43500','1','2','2','0','2016-12-06 11:40:51','{\"url\":\"http://janson.lab.everhomes.com/energy-management/index.html?hideNavigationBar=1#/address_choose#sign_suffix\"}',NULL,'0','0',NULL,'13','0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('41300','统计分析','40000','/40000/41300','1','2','2','0','2016-12-28 10:45:07',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('41500','文件管理','40000','/40000/41500','1','2','2','0','2017-05-11 08:51:49',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('41400','云打印','40000','/40000/41400','1','2','2','0','2017-05-11 08:51:49',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('41600','园区审批','40000','/40000/41600','1','2','2','0','2017-05-11 08:51:49',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('41700','问卷调查','40000','/40000/41700','1','2','2','0','2017-05-11 08:51:49',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('10200','园区介绍','40000','/40000/10200','1','2','2','0','2016-12-06 11:40:50',NULL,NULL,'0','0',NULL,'13','0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('10800','园区快讯','40000','/40000/10800','1','2','2','0','2016-12-06 11:40:50','{\"categoryId\":2,\"timeWidgetStyle\":\"date\"}',NULL,'0','0',NULL,'48','1');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('10850','园区报','40000','/40000/10850','1','2','2','0','2016-12-06 11:40:50','{\"url\":\"http://janson.lab.everhomes.com/energy-management/index.html?hideNavigationBar=1#/address_choose#sign_suffix\"}',NULL,'0','0',NULL,'13','0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('20900','车辆放行','40000','/40000/20900','1','2','2','0','2017-01-19 12:12:53','',NULL,'0','0',NULL,'57','0');


insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('50000','企业OA服务','0','/50000','1','1','2','0','2016-12-06 11:40:51',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('50100','组织架构','50000','/50000/50100','1','2','2','0','2016-12-06 11:40:51',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('50200','岗位管理','50000','/50000/50200','1','2','2','0','2016-12-06 11:40:51',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('50300','职级管理','50000','/50000/50300','1','2','2','0','2016-12-06 11:40:51',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('50400','人员档案','50000','/50000/50400','1','2','2','0','2016-12-06 11:40:51',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('50500','员工认证','50000','/50000/50500','1','2','2','0','2016-12-06 11:40:51',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('50600','打卡','50000','/50000/50600','1','2','2','0','2016-12-06 11:40:51','',NULL,'0','0',NULL,'23','0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('50700','视频会议','50000','/50000/50700','1','2','2','0','2016-12-06 11:40:51','',NULL,'0','0',NULL,'27','0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('52000','审批','50000','/50000/52000','1','2','2','0','2017-01-19 11:50:20',NULL,NULL,'0','0',NULL,NULL,'0');



insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('60000','系统管理','0','/60000','1','1','2','0','2016-12-06 11:40:51',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('60100','管理员管理','60000','/60000/60100','1','2','2','0','2016-12-06 11:40:51',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('60200','责任部门配置','60000','/60000/60200','1','2','2','0','2016-12-06 11:40:51',NULL,NULL,'0','0',NULL,NULL,'0');



insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('70000','域空间管理','0','/70000','1','1','2','0','2016-12-28 10:47:42',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('70100','业务应用配置','70000','/70000/70100','1','2','2','0','2017-04-10 10:54:08',NULL,NULL,'0','0',NULL,NULL,'0');
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('70200','App配置','70000','/70000/70200','1','2','2','0','2017-04-10 10:54:08',NULL,NULL,'0','0',NULL,NULL,'0');


insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `update_time`, `operator_uid`, `creator_uid`, `description`, `action_type`, `multiple_flag`) values('90000','第三方服务模块','0','/90000','3','1','2','0','2017-07-04 15:55:50',NULL,NULL,'0','0',NULL,NULL,0);


update `eh_web_menus` set module_id = 10600 where id in (10600, 10610, 10620, 10700);
update `eh_web_menus` set module_id = 10800 where id in (10900);
update `eh_web_menus` set module_id = 30500 where id in (30500, 31000, 32000);
update `eh_web_menus` set module_id = 41000 where path like '/50000/50800%';
update `eh_web_menus` set module_id = 13000 where id in (70100, 70200);
update `eh_web_menus` set module_id = 40500 where path like '/80000/80100%';


delete from `eh_acl_privileges` where id in (10079, 10090);
set @module_privilege_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
insert into `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) values((@module_privilege_id := @module_privilege_id + 1),'70100','1','10079','业务应用配置 管理员','0',now());
insert into `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) values((@module_privilege_id := @module_privilege_id + 1),'70200','1','10080','App配置 管理员','0',now());
insert into `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) values((@module_privilege_id := @module_privilege_id + 1),'13000','1','10078','任务管理 管理员','0',now());
insert into `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) values((@module_privilege_id := @module_privilege_id + 1),'30600','1','10090','黑名单管理 管理员','0',now());
insert into `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) values((@module_privilege_id := @module_privilege_id + 1),'41700','1','10081','问卷调查 管理员','0',now());

insert into `eh_acl_privileges` (`id`, `app_id`, `name`, `description`) values(10080,'0','app.conf.super','app配置超级权限');
insert into `eh_acl_privileges` (`id`, `app_id`, `name`, `description`) values(10079,'0','task.manage.super','任务管理超级权限');
insert into `eh_acl_privileges` (`id`, `app_id`, `name`, `description`) values(10090,'0','black.list.super','黑名单超级权限');
insert into `eh_acl_privileges` (`id`, `app_id`, `name`, `description`) values(10081,'0','question.investigation.super','问卷调查超级权限');

set @domain_id = IFNULL((SELECT MAX(id) FROM `eh_domains`), 1);
insert into `eh_domains` (`id`, `namespace_id`, `portal_type`, `portal_id`, `domain`, `create_uid`, `create_time`) values((@domain_id := @domain_id + 1),'0','EhZuolinAdmins',0, 'test9.lab.everhomes.com', 0, now());


update eh_user_launch_pad_items eulpi set item_name = (select item_name from `eh_launch_pad_items` where id = eulpi.item_id);
update `eh_item_service_categries` set label = name;
update `eh_item_service_categries` set name = id;
update `eh_item_service_categries` set `item_location` = '/home';
update `eh_item_service_categries` set `item_group` = 'Bizs';
update `eh_item_service_categries` set `scope_code` = 5, `scope_id` = 0 where `scene_type` = 'pm_admin'; 
update `eh_item_service_categries` set `scope_code` = 1, `scope_id` = 0 where `scene_type` = 'park_tourist'; 
update `eh_item_service_categries` set `scope_code` = 6, `scope_id` = 0 where `scene_type` = 'default'; 
update `eh_launch_pad_items` elpi set categry_name = (select name from eh_item_service_categries where id = elpi.service_categry_id);







-- merge from profile-1.2 started by R
SET @id = (SELECT MAX(id) FROM eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'organization', '900024', 'zh_CN', '性别仅支持"男""女"');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'organization', '900025', 'zh_CN', '姓名长度需小于20个字');
-- merge from profile-1.2 ended by R

-- merge from barcode-2.0 start  yanjun
-- 电商一维码查询uri add by yanjun 20170816
SET @eh_configurations_id = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES((@eh_configurations_id := @eh_configurations_id + 1),'biz.zuolin.checkBarcode','/zl-ec/rest/openapi/commodity/barcodeByCommodityUrl','电商一维码查询uri','0',NULL);
-- merge from barcode-1.2 end  yanjun

-- 打印接口调用减少 dengs.2017.08.22
-- # 二维码扫描增加到20秒
update eh_configurations SET `value` = '20000' WHERE `name` = 'print.logon.scan.timout' AND namespace_id = '0';
-- # 二维码有效时间改成六分钟
update eh_configurations SET `value` = '6' WHERE `name` = 'print.siyin.timeout' AND namespace_id = '0';
