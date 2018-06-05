-- 钖叕2.2 begin
-- added by wh :钖叕宸ヨ祫鏉″彂鏀炬秷鎭�
SET @template_id = (SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'salary.notification', 1, 'zh_CN', '鍚庡彴鍙戝伐璧勬潯', '${salaryDate} 宸ヨ祫宸插彂鏀俱��', 0);

-- added by wh 钖叕鐨勬ā鍧楀搴攁ction_type 鏀逛负74(宸ヨ祫鏉�)
UPDATE eh_service_modules SET action_type = 74 WHERE id = 51400;
UPDATE eh_service_module_apps SET action_type = 74 WHERE module_id = 51400;
-- 钖叕2.2 end


-- 瀹㈡埛绠＄悊绗笁鏂规暟鎹窡杩涗汉  by jiarui
UPDATE  eh_enterprise_customers set tracking_uid = NULL  WHERE  tracking_uid = -1;
update eh_enterprise_customer_admins  set namespace_id  = (select namespace_id from eh_enterprise_customers where id = customer_id);

update eh_enterprise_customer_admins t2  set contact_type  = (select target_type from eh_organization_members t1 where t1.contact_token = t2.contact_token and t1.namespace_id = t2.namespace_id limit 1);




-- 妯″潡Id 41900
-- 鏂板妯″潡 eh_service_modules
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `update_time`, `operator_uid`, `creator_uid`, `multiple_flag`, `module_control_type`) VALUES ('41900', '鏀垮姟鏈嶅姟', '40000', '/40000/41900', '1', '2', '2', '0', UTC_TIMESTAMP(), UTC_TIMESTAMP(), '0', '0', '0', 'community_control');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('41910', '鏀跨瓥绠＄悊', '41900', '/40000/41900/41910', '1', '3', '2', '0', UTC_TIMESTAMP(), '', NULL, UTC_TIMESTAMP(), '0', '0', '', '0', 'community_control');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('41920', '鏌ヨ璁板綍', '41900', '/40000/41900/41920', '1', '3', '2', '0', UTC_TIMESTAMP(), '', NULL, UTC_TIMESTAMP(), '0', '0', '', '0', 'community_control');
-- 鏂板妯″潡鑿滃崟 eh_web_menus
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `data_type`, `leaf_flag`, `path`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) VALUES ('16032200', '鏀垮姟鏈嶅姟', '16030000', 'policy-service', '1', '/16000000/16030000/16032200', '22', '41900', '3', 'system', 'module');
-- 鏉冮檺
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`) VALUES ('4190041910', '0', '鏀跨瓥绠＄悊', '鏀跨瓥绠＄悊 鍏ㄩ儴鏉冮檺');
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`) VALUES ('4190041920', '0', '鏌ヨ璁板綍', '鏌ヨ璁板綍 鍏ㄩ儴鏉冮檺');
-- 鏉冮檺涓棿琛� 璁剧疆Id
SET @pri_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@pri_id := @pri_id + 1, '41910', '0', '4190041910', '鍏ㄩ儴鏉冮檺', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@pri_id := @pri_id + 1, '41920', '0', '4190041920', '鍏ㄩ儴鏉冮檺', '0', UTC_TIMESTAMP());

-- 鍚堝悓鐨勫紑濮嬬粨鏉熸椂闂存敼涓猴紝寮�濮嬪湪00锛�00锛�00锛� 缁撴潫鍦�23锛�59锛�59 by wentian
update eh_contracts set contract_start_date = date_format(contract_start_date,'%Y-%m-%d 00:00:00');
update eh_contracts set contract_end_date = date_format(contract_end_date,'%Y-%m-%d 23:59:59');

-- ------------------------------
-- 鏈嶅姟鑱旂洘V3.3锛堟柊澧為渶姹傛彁鍗曞姛鑳斤級     
-- 浜у搧鍔熻兘 #26469 add by huangmingbo  2018/05/29
-- ------------------------------
-- 娣诲姞閿欒淇℃伅鎻愮ず
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '11000', 'zh_CN', '鏂颁簨浠剁敵璇风敤鎴蜂笉瀛樺湪');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '11001', 'zh_CN', '鏈壘鍒板伐浣滄祦淇℃伅');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '11002', 'zh_CN', '鏈嶅姟鍟嗗姛鑳藉苟鏈紑鍚�');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '11003', 'zh_CN', '璇ユ湇鍔″晢涓嶅瓨鍦�');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '11004', 'zh_CN', '涓婁紶鐨勬枃浠跺湴鍧�涓虹┖');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '11005', 'zh_CN', '閭欢闄勪欢鑾峰彇澶辫触');

-- 鍒犻櫎鏍峰紡绠＄悊锛屾ā鍧楋紝鏉冮檺锛屾ā鍧楁潈闄愯〃锛屾洿鏂癿odule鐨勯『搴�  #26469 add by huangmingbo  2018/05/29
delete from eh_service_modules where parent_id = 40500 and id = 40510;
update eh_service_modules m set m.default_order = 0 where m.parent_id = 40500 and m.id = 40520;
update eh_service_modules m set m.default_order = 1 where m.parent_id = 40500 and m.id = 40540;
update eh_service_modules m set m.default_order = 2 where m.parent_id = 40500 and m.id = 40530;
delete from eh_service_module_privileges where module_id = 40510;
delete from  eh_acl_privileges  where id =  4050040510;

SET @ns_id = 999954;
SET @module_id = 40500;

-- 灏嗘湇鍔¤仈鐩熼兘鍙樻垚community_control  #26469 add by huangmingbo  2018/05/29
UPDATE `eh_service_modules` SET `module_control_type`='community_control' WHERE  `id`=@module_id;
update eh_service_module_apps set  module_control_type='community_control' where module_id = @module_id and  module_control_type = 'unlimit_control';

-- 鏂板缓浜嬩欢  #26469 add by huangmingbo  2018/05/29
SET @flow_predefined_params_id = IFNULL((SELECT MAX(id) FROM `eh_flow_predefined_params`), 1);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
  VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1), @ns_id, 0, '', @module_id, 'any-module', 'flow_button', '鏂板缓浜嬩欢', 'new event', '{"nodeType":"NEW_EVENT"}', 2, NULL, NULL, NULL, NULL);  

-- 鈥滃仠杞︾即璐光�濄�佲�滅墿涓氭姤淇�濄�佲�滅墿鍝佹斁琛屸�濄�佲�滃洯鍖哄叆椹烩��  #26469 add by huangmingbo  2018/05/29
SET @jump_id = IFNULL((SELECT MAX(id) FROM `eh_service_alliance_jump_module`), 1);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_id`, `module_url`, `instance_config`,`parent_id`, `signal`) VALUES ((@jump_id := @jump_id + 1), @ns_id, '鍋滆溅缂磋垂', 40800, 'zl://parking/query?displayName=鍋滆溅缂磋垂', NULL, 0, 1);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_id`, `module_url`, `instance_config`,`parent_id`, `signal`) VALUES ((@jump_id := @jump_id + 1), @ns_id, '鐗╀笟鎶ヤ慨', 20100, NULL, '{"taskCategoryId":6,"prefix":"/property-repair-web/build/index.html","skipRoute":"zl://browser/i?url="}', 0, 1);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_id`, `module_url`, `instance_config`,`parent_id`, `signal`) VALUES ((@jump_id := @jump_id + 1), @ns_id, '鐗╁搧鏀捐', 49200, NULL, '{"prefix":"/goods-move/build/index.html","skipRoute":"zl://browser/i?url="}', 0, 1);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_id`, `module_url`, `instance_config`,`parent_id`, `signal`) VALUES ((@jump_id := @jump_id + 1), @ns_id, '鍥尯鍏ラ┗', 40100, NULL, '{"skipRoute":"zl://park-service/settle"}', 0, 1);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_id`, `module_url`, `instance_config`,`parent_id`, `signal`) VALUES ((@jump_id := @jump_id + 1), @ns_id, '鎶曡瘔寤鸿', 20100, NULL, '{"taskCategoryId":9,"prefix":"/property-repair-web/build/index.html","skipRoute":"zl://browser/i?url="}', 0, 1);



-- 鍚堝悓鍔ㄦ�佽〃鍗曚慨鏀�  by jiarui
UPDATE  eh_var_field_groups set title = '鏀舵鍚堝悓' WHERE  module_name ='contract' and title ='鍚堝悓淇℃伅' AND  parent_id =0;
UPDATE `eh_var_fields`  SET `field_param`='{\"fieldParamType\": \"text\", \"length\": 32}' WHERE `module_name`='contract' AND `name`='contractType';
UPDATE `eh_var_field_scopes`  SET `field_param`='{\"fieldParamType\": \"text\", \"length\": 32}' WHERE  field_id IN (SELECT id FROM eh_var_fields   WHERE `module_name`='contract' AND `name`='contractType');
UPDATE `eh_var_fields`  SET `field_param`='{\"fieldParamType\": \"text\", \"length\": 32}' WHERE `module_name`='contract' AND `name`='status';
UPDATE `eh_var_field_scopes`  SET `field_param`='{\"fieldParamType\": \"text\", \"length\": 32}' WHERE  field_id IN (SELECT id FROM eh_var_fields   WHERE `module_name`='contract' AND `name`='status');
-- 琛ㄥ崟select 鑷畾涔�
UPDATE  eh_var_fields  SET field_param = REPLACE(field_param,'select','customizationSelect') WHERE module_name = 'enterprise_customer' AND `name` <> 'levelItemId';
UPDATE  eh_var_fields  SET field_param = REPLACE(field_param,'select','customizationSelect') WHERE module_name = 'contract' AND `name` NOT  IN ('contractType','status');

UPDATE  eh_var_field_scopes SET field_param = (SELECT field_param FROM eh_var_fields WHERE id = field_id);
-- 琛ㄥ崟default  by jiarui

SET  @id = (SELECT MAX(id) FROM  eh_var_field_group_scopes);
INSERT INTO `eh_var_field_group_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '1', '瀹㈡埛淇℃伅', '1', '2', '1', NOW(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_group_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '10', '鍩烘湰淇℃伅', '1', '2', '1', NOW(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_group_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '11', '浼佷笟鎯呭喌', '3', '2', '1',NOW(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_group_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '12', '鍛樺伐鎯呭喌', '4', '2', '1', NOW(), NULL, NULL, NULL, NULL);

SET  @id = (SELECT MAX(id) FROM  eh_var_field_scopes);
INSERT INTO `eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '10', '2', '{\"fieldParamType\": \"text\", \"length\": 32}', '瀹㈡埛鍚嶇О', '1', '1', '2', '1', now(), '1', NULL , NULL, '/1/10/');
INSERT INTO `eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '10', '3', '{\"fieldParamType\": \"text\", \"length\": 32}', '绠�绉�', '0', '3', '2', '1', now(), NULL, NULL, NULL, '/1/10/');
INSERT INTO `eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '10', '4', '{\"fieldParamType\": \"select\", \"length\": 32}', '瀹㈡埛绫诲瀷', '0', '3', '2', '1', now(), NULL, NULL, NULL, '/1/10/');
INSERT INTO `eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '10', '5', '{\"fieldParamType\": \"select\", \"length\": 32}', '瀹㈡埛绾у埆', '1', '4', '2', '1', now(), '1', NULL , NULL, '/1/10/');
INSERT INTO `eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '10', '7', '{\"fieldParamType\": \"text\", \"length\": 32}', '鑱旂郴浜�', '1', '5', '2', '1', now(), '1', NULL , NULL, '/1/10/');
INSERT INTO `eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '10', '10', '{\"fieldParamType\": \"text\", \"length\": 32}', '鑱旂郴鐢佃瘽', '1', '6', '2', '1',now(), '1', NULL , NULL , '/1/10/');
INSERT INTO `eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '10', '211', '{\"fieldParamType\": \"text\", \"length\": 32}', '璺熻繘浜�', '0', '8', '2', '1', now(), '1', NULL , NULL, '/1/10/');

INSERT INTO `eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '11', '15', '{\"fieldParamType\": \"text\", \"length\": 32}', '鍦板潃', '1', '8', '2', '1', now(), NULL, NULL, NULL, '/1/11/');
INSERT INTO `eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '11', '17', '{\"fieldParamType\": \"text\", \"length\": 32}', '浼佷笟缃戝潃', '0', '11', '2', '1', now(), NULL, NULL, NULL, '/1/11/');
INSERT INTO `eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '11', '24', '{\"fieldParamType\": \"select\", \"length\": 32}', '琛屼笟绫诲瀷', '0', '13', '2', '1',now(), NULL, NULL, NULL, '/1/11/');
INSERT INTO `eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '11', '36', '{\"fieldParamType\": \"image\", \"length\": 1}', '浼佷笟LOGO', '0', '46', '2', '1', now(), '1', NULL , NULL , '/1/11//');
INSERT INTO `eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '11', '37', '{\"fieldParamType\": \"multiText\", \"length\": 2048}', '浼佷笟绠�浠�', '0', '26', '2', '1', now(), '1', NULL , NULL , '/1/11/');

SET  @id = (SELECT MAX(id) FROM  eh_var_field_item_scopes);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '5', '3', '鏅�氬鎴�', '3', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '5', '4', '閲嶈瀹㈡埛', '4', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '5', '5', '鎰忓悜瀹㈡埛', '5', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '5', '6', '宸叉垚浜ゅ鎴�', '6', '2', '1',now(), NULL, NULL, NULL, NULL);

INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '24', '202', '闆嗘垚鐢佃矾', '1', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '24', '203', '杞欢', '1', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '24', '204', '閫氫俊鎶�鏈�', '1', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '24', '205', '鐢熺墿鍖昏嵂', '1', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '24', '206', '鍖荤枟鍣ㄦ', '1', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '24', '207', '鍏夋満鐢�', '1', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '24', '208', '閲戣瀺鏈嶅姟', '1', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '24', '209', '鏂拌兘婧愪笌鐜繚', '1', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '24', '210', '鏂囧寲鍒涙剰', '1', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '24', '211', '鍟嗕笟-椁愰ギ', '1', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '24', '212', '鍟嗕笟-瓒呭競', '1', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '24', '213', '鍟嗕笟-椋熷爞', '1', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '24', '214', '鍟嗕笟-鍏朵粬', '1', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '24', '215', '鍏朵粬', '1', '2', '1', now(), NULL, NULL, NULL, NULL);


INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '4', '197', '涓氫富', '1', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '4', '198', '鍔炲叕', '2', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '4', '199', '鍟嗕笟', '3', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '4', '200', '瀛靛寲鍣�', '4', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '4', '201', '鐗╀笟鍏徃', '5', '2', '1', now(), NULL, NULL, NULL, NULL);

-- 鑳借�楁洿鏂扮绾垮寘鐗堟湰   by jiarui
update eh_version_urls set download_url = replace(download_url,'1-0-1','1-0-3'),
  info_url = replace(info_url,'1-0-1','1-0-3'),
  target_version = '1.0.3' where realm_id = (select id from eh_version_realm where realm = 'energyManagement');

-- 鍚堝悓绠＄悊2.7锛� by dingjianmin
SET @id = IFNULL((SELECT MAX(`id`) FROM `eh_service_module_exclude_functions`),0);
-- 鍚屾鍚堝悓 98
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999953', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999952', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999959', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999948', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '2', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '11', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999956', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999949', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999950', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999945', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999943', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999946', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999963', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999951', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999941', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999942', NULL, '21200', '98');
-- 鍚屾瀹㈡埛 99                                                                                                                            
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999953', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999952', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999959', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999948', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '2', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '11', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999956', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999949', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999950', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999945', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999943', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999946', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999963', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999951', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999941', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999942', NULL, '21200', '99');
-- 浼佷笟瀹㈡埛绠＄悊锛屽幓鎺夌浉搴旂殑鏉冮檺锛堟柊澧烇紝瀵煎叆瀹㈡埛锛屼笅杞芥ā鏉匡級
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999983', NULL, '21100', '21101');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999983', NULL, '21100', '21103');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '1000000', NULL, '21100', '21101');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '1000000', NULL, '21100', '21103');
-- 浼佷笟瀹㈡埛绠＄悊锛屽幓鎺夌浉搴旂殑鏉冮檺锛堝悓姝ュ鎴�99锛�
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999953', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999952', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999959', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999948', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '2', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '11', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999956', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999949', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999950', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999945', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999943', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999946', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999963', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999951', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999941', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999942', NULL, '21100', '99');

-- 鍚堝悓鍩虹鍙傛暟閰嶇疆 宸ヤ綔娴侀厤缃� 鏉冮檺 by dingjianmin
SELECT id from eh_service_modules WHERE path IN ('/110000/21200/21230','/110000/21200/21220');
SELECT id from EH_SERVICE_MODULE_PRIVILEGES  WHERE module_id='21210' AND privilege_id=21213 AND privilege_type=0;
-- 瀵规帴绗笁鏂� 鏉冮檺
SELECT id from EH_SERVICE_MODULE_EXCLUDE_FUNCTIONS  WHERE namespace_id IN(999971,1000000) AND module_id IN (21100,21200)AND function_id IN(98,99);
-- 鍏嶇鏈熷瓧娈靛垹闄� 鏉冮檺
SELECT id from  EH_VAR_FIELDS WHERE module_name='contract' AND group_id=15 AND group_path='/13/15/' AND `name`='freeDays';
-- 瀹㈡埛绠＄悊 鍚屾瀹㈡埛鏉冮檺
SELECT id from  EH_SERVICE_MODULE_PRIVILEGES WHERE module_id=21110 and privilege_id=21104;

-- 鍚堝悓鍩虹鍙傛暟閰嶇疆 宸ヤ綔娴侀厤缃� 鏉冮檺 by dingjianmin
DELETE FROM EH_SERVICE_MODULES WHERE id IN(SELECT id FROM (SELECT id from eh_service_modules WHERE path IN ('/110000/21200/21230','/110000/21200/21220')) sm);
DELETE FROM EH_SERVICE_MODULE_PRIVILEGES WHERE id IN(SELECT id FROM (SELECT id from EH_SERVICE_MODULE_PRIVILEGES  WHERE module_id='21210' AND privilege_id=21213 AND privilege_type=0) smp);
-- 瀵规帴绗笁鏂� 鏉冮檺
DELETE FROM EH_SERVICE_MODULE_EXCLUDE_FUNCTIONS WHERE id IN(SELECT id FROM (SELECT id from EH_SERVICE_MODULE_EXCLUDE_FUNCTIONS  WHERE namespace_id IN(999971,1000000) AND module_id IN (21100,21200)AND function_id IN(98,99))smef);
-- 鍏嶇鏈熷瓧娈靛垹闄� 鏉冮檺
DELETE FROM EH_VAR_FIELDS WHERE id IN(SELECT id FROM (SELECT id from  EH_VAR_FIELDS WHERE module_name='contract' AND group_id=15 AND group_path='/13/15/' AND `name`='freeDays')vf);
-- 瀹㈡埛绠＄悊 鍚屾瀹㈡埛鏉冮檺
DELETE FROM EH_SERVICE_MODULE_PRIVILEGES WHERE id IN(SELECT id FROM (SELECT id from  EH_SERVICE_MODULE_PRIVILEGES WHERE module_id=21110 and privilege_id=21104)smp);

-- 鏇存柊鍚堝悓鍒楄〃涓烘敹娆惧悎鍚�
UPDATE EH_SERVICE_MODULES SET `name`='鏀舵鍚堝悓' WHERE `level`=3 and parent_id=21200 and path='/110000/21200/21210';

-- 鏇存柊鍚堝悓鍒楄〃涓轰粯娆惧悎鍚� 涓嬬殑鐩稿叧鏉冮檺 by dingjianmin
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '绛剧害銆佷慨鏀�' ,default_order=1 WHERE module_id = 21210 AND privilege_id = 21201;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '鍙戣捣瀹℃壒' ,default_order=2 WHERE module_id = 21210 AND privilege_id = 21202;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '鍒犻櫎' ,default_order=3 WHERE module_id = 21210 AND privilege_id = 21204;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '浣滃簾' ,default_order=4 WHERE module_id = 21210 AND privilege_id = 21205;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '鍏ュ満' ,default_order=5 WHERE module_id = 21210 AND privilege_id = 21206;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '鏌ョ湅' ,default_order=0 WHERE module_id = 21210 AND privilege_id = 21207;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '缁害' ,default_order=6 WHERE module_id = 21210 AND privilege_id = 21208;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '鍙樻洿' ,default_order=7 WHERE module_id = 21210 AND privilege_id = 21209;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '閫�绾�' ,default_order=8 WHERE module_id = 21210 AND privilege_id = 21214;
-- 鏇存柊浠樻鍚堝悓涓嬬殑鐩稿叧鏉冮檺鏄剧ず
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '鏂板' ,default_order=5 WHERE module_id = 21215 AND privilege_id = 21215;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '绛剧害銆佸彂璧峰鎵�',default_order=1 WHERE module_id = 21215 AND privilege_id = 21216;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '淇敼' ,default_order=2 WHERE module_id = 21215 AND privilege_id = 21217;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '鍒犻櫎' ,default_order=3 WHERE module_id = 21215 AND privilege_id = 21218;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '浣滃簾' ,default_order=4 WHERE module_id = 21215 AND privilege_id = 21219;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '鏌ョ湅' ,default_order=0 WHERE module_id = 21215 AND privilege_id = 21220;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '缁害' ,default_order=6 WHERE module_id = 21215 AND privilege_id = 21221;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '鍙樻洿' ,default_order=7 WHERE module_id = 21215 AND privilege_id = 21222;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '閫�绾�' ,default_order=8 WHERE module_id = 21215 AND privilege_id = 21223;

-- issue-30235 鏂板妯″潡 "鎴戠殑閽ュ寵" by liuyilin 20180524
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `instance_config`, `action_type`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('42000', '鎴戠殑閽ュ寵', '40000', '/10000/42000', '1', '2', '2', '10', '{\"isSupportQR\":1,\"isSupportSmart\":0}', 76, '0', '0', '0', '0', 'community_control');

-- issue-30573 璺闂ㄧ鍒囨崲鎴恴uolin_v2 by liuyilin 20180521
SET @var_id = (SELECT MAX(`id`) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@var_id:=@var_id+1, 'aclink.qr_driver_zuolin_inner', 'zuolin_v2', 'use version2 of zuolin driver', '999963', NULL);

-- 鍔ㄦ�佽〃鍗曟暟鎹笉涓�鑷翠慨澶�  by jiarui 20180531
update  eh_var_fields set mandatory_flag =1 where id = 10;

update eh_var_field_scopes set field_param = (SELECT t1.field_param from eh_var_fields t1 where id = field_id);

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`) 
VALUES ( 'asset.dingfenghui.appshowpay', '2', '2浠ｈ〃APP灞曠ず涓哄叏閮ㄧ即璐�', '999951');