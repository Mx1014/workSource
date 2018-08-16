-- 国贸快递配置
SET @parent_express_company_id = 4;
set @son_express_company_id = 10005;
set @express_company_businesses_id = 8;
set @ns = 1000000; -- todo 需要修改为国贸的域空间id
set @community_id=240111044331048623; -- todo 需要修改为国贸的园区id
INSERT INTO `eh_express_companies` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `logo`, `description`, `logistics_url`, `order_url`, `app_key`, `app_secret`, `authorization`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES (@parent_express_company_id, @ns, 'EhNamespaces', @ns, '0', '国贸物业酒店管理有限公司', '', '国贸项目，国贸快递业务对应的公司', '', '', '', '', '', '2', '0', now(), now(), '0');
INSERT INTO `eh_express_companies` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `logo`, `description`, `logistics_url`, `order_url`, `app_key`, `app_secret`, `authorization`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES (@son_express_company_id, @ns, 'community', @community_id, @parent_express_company_id, '国贸物业酒店管理有限公司', '', '国贸项目，国贸快递业务对应的公司', NULL, NULL, NULL, NULL, NULL, '1', '0', now(), now(), '0');

INSERT INTO `eh_express_company_businesses` (`id`, `namespace_id`, `owner_type`, `owner_id`, `express_company_id`, `send_type`, `send_type_name`, `package_types`, `insured_documents`, `order_status_collections`, `pay_type`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES (@express_company_businesses_id, @ns, 'EhNamespaces', @ns, @parent_express_company_id, '10', '国贸快递', '[]', NULL, '[{\"status\": 6},{\"status\": 5},{\"status\": 4}]', '3', '2', '0', now(), now(), '0');

-- 删除快递的参数配置权限
DELETE from eh_acl_privileges WHERE id = '4070040710';
DELETE from eh_service_module_privileges WHERE module_id = '40710';
