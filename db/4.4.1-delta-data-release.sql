-- 增加一个“活动时间：”中文字符串 2017-03-31 19:17 add by yanjun
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('activity','11','zh_CN','活动时间：');

-- 更新报修与工作流状态不同步 add by sw 20170206
UPDATE eh_pm_tasks join (SELECT refer_id from eh_flow_cases where id in (SELECT flow_case_id from eh_pm_tasks where flow_case_id != 0) and `status` = 4) t on t.refer_id = id set `status` = 4;

-- 科技园报修 add by sw 20170406
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'sms.default.yzx', 34, 'zh_CN', '物业任务分配人员', '40775');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'flow:20100', 34, 'zh_CN', '物业任务分配人员', '40775');

-- 更新华润 园区入驻 add by sw 20170406
DELETE from eh_lease_configs where id =3;
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `rent_amount_flag`, `issuing_lease_flag`, `issuer_manage_flag`, `park_indroduce_flag`, `renew_flag`) 
	VALUES ('3', '999985', '1', '1', '1', '0', '0');