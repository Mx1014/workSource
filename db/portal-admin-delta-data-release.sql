
update `eh_service_modules` set action_type = '23' where id = 50600;
update `eh_service_modules` set action_type = '27' where id = 50700;
update `eh_service_modules` set action_type = '30' where id = 40800;
update `eh_service_modules` set action_type = '44', instance_config = '{"realm":"quality","entryUrl":"https://core.zuolin.com/nar/quality/index.html?hideNavigationBar=1#/task_list#sign_suffix"}' where id = 20600;
update `eh_service_modules` set action_type = '45' where id = 40300;
update `eh_service_modules` set action_type = '46' where id = 50400;
update `eh_service_modules` set action_type = '47' where id = 41100;
update `eh_service_modules` set action_type = '48', instance_config = '{"categoryId":2,"timeWidgetStyle":"date"}', multiple_flag = 1 where id = 10900;
update `eh_service_modules` set action_type = '49', instance_config = '{"resourceTypeId":8,"pageType":0}', multiple_flag = 1  where id = 40400;
update `eh_service_modules` set action_type = '57' where id = 20900;
update `eh_service_modules` set action_type = '61', instance_config = '{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":2,"scope":2,"style":4,"title": "白领活动"}', multiple_flag = 1 where id = 10600;
update `eh_service_modules` set action_type = '13', instance_config = '{"url":"https://core.zuolin.com/equipment-inspection/dist/index.html?hideNavigationBar=1#sign_suffix"}' where id = 20800;
update `eh_service_modules` set action_type = '13', instance_config = '{"url":"http://alpha.lab.everhomes.com/station-booking/index.html?hideNavigationBar=1#/station_booking#sign_suffix"}' where id = 40200;
update `eh_service_modules` set action_type = '62', instance_config = '{"tag":"创客"}', multiple_flag = 1 where id = 10100;


set @module_privilege_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`,`instance_config`,`update_time`,`operator_uid`,`creator_uid`,`description`,`action_type`,`multiple_flag`) VALUES ('40700', '快递管理', '40000', '/40000/40700', '1', '2', '2', '0', UTC_TIMESTAMP(),'{"url":"http://alpha.lab.everhomes.com/deliver/dist/index.html?hideNavigationBar=1#/home_page#sign_suffixl"}',UTC_TIMESTAMP(),0,0,null,13,0);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@module_privilege_id := @module_privilege_id + 1), '40700', '1', '30085', NULL, '0', UTC_TIMESTAMP());

insert  into `eh_service_modules`(`id`,`name`,`parent_id`,`path`,`type`,`level`,`status`,`default_order`,`create_time`,`instance_config`,`update_time`,`operator_uid`,`creator_uid`,`description`) values (90000,'第三方服务模块',0,'/90000',3,1,2,0,now(),NULL,NULL,0,0,NULL);

insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('4000000','域空间管理','0',NULL,'','1','2','/4000000','zuolin','0','0','1','namespace','classify');
insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('4100000','域空间配置','4000000',NULL,'','1','2','/4000000/4100000','zuolin','0','0','2','namespace','classify');
insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('4110000','业务应用配置','4100000',NULL,'','0','2','/4000000/4100000/4110000','zuolin','0','0','3','namespace','module');
insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('4120000','app配置','4100000',NULL,'','1','2','/4000000/4100000/4120000','zuolin','0','0','3','namespace','module');
insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('4121000','门户配置','4120000',NULL,'','0','2','/4000000/4100000/4120000/4121000','zuolin','0','0','4','namespace','page');
insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('4122000','导航栏配置','4120000',NULL,'','0','2','/4000000/4100000/4120000/4122000','zuolin','0','0','4','namespace','page');

insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('5000000','运营业务模块管理','0',NULL,'','1','2','/5000000','zuolin','0','0','1','system','classify');
insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) values('5100000','第三方服务模块','5000000',NULL,'','1','2','/5000000/5100000','zuolin','0','0','2','system','module');