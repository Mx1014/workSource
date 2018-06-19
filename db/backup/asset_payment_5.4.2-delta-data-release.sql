-- 添加 上传了缴费凭证的账单tab 功能,function_id为100
INSERT INTO eh_service_module_functions (id,module_id,privilege_id,`explain`) VALUES (100,20400,0,"上传了缴费凭证的账单tab");

-- 将 “修改缴费状态” 改成 “修改缴费状态与审核”
UPDATE `eh_acl_privileges` SET `name`='修改缴费状态与审核', `description`='账单管理 修改缴费状态与审核' WHERE (`id`='204001004');
UPDATE `eh_service_module_privileges` SET `remark`='账单管理 修改缴费状态与审核' WHERE (`id`='220');

-- 设置eh_service_module_exclude_functions表的主键id
SET @id = IFNULL((SELECT MAX(`id`) FROM `eh_service_module_exclude_functions`),0);

-- 设置 上传了缴费凭证的账单tab 对应的黑名单（除了天企汇（中天）这个域空间，其他域空间全部加入该tab的黑名单）
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999973', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999955', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999986', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999995', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999998', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '1', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999953', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999981', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999970', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999990', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999980', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999972', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999965', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999964', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999958', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999952', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999985', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999976', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999959', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999974', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999968', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999948', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999967', NULL, '20400', '100');
-- 这一句是 屏蔽 上传了缴费凭证的账单tab 在 天企汇（中天） 中显示的sql，因为测试不在 天企汇（中天） 中屏蔽 上传了缴费凭证的账单tab
-- INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999944', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999991', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '2', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999997', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '11', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '1000001', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999978', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999971', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999956', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999949', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999954', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999969', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999961', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999947', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999950', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999945', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999957', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999993', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999992', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999987', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '1000000', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999982', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999943', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999988', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999962', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999983', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999977', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999946', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999975', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999941', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999999', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999942', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999963', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999996', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999989', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999951', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999994', NULL, '20400', '100');

set @id = ifnull((select max(`id`) from eh_payment_app_views), 0);
INSERT INTO `eh_payment_app_views` (`id`, `namespace_id`, `community_id`, `has_view`, `view_item`, `remark1_type`, `remark1_identifier`, `remark2_type`, `remark2_identifier`, `remark3_type`, `remark3_identifier`) VALUES (@id:=@id+1, '999944', NULL, '1', 'CERTIFICATE', NULL, NULL, NULL, NULL, NULL, NULL);

