
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