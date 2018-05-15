SET @id = ifnull((SELECT max(`id`) from `eh_locale_templates`),0);
set @code_id = ifnull((SELECT max(`code`) from `eh_locale_templates`),0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'user.notification.app.asset', @code_id:=@code_id+1, 'zh_CN', '物业账单通知用户模板，欠费前', '尊敬的租户${targetName}先生/小姐：您好，您至今未缴：${dateStr}月服务费共计：${amount}元。请及时缴纳，谢谢您的配合！', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'user.notification.app.asset', @code_id:=@code_id+1, 'zh_CN', '物业账单通知用户模板，欠费后', '尊敬的租户${targetName}先生/小姐：您好，请尽快缴纳${dateStr}月服务费：${amount}元。如您明日仍未缴纳，将视为逾期，我司将停止相关物业服务，并计收相应滞纳金，引起一切后果由租户自行承担。谢谢！', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'user.notification.app.asset', @code_id:=@code_id+1, 'zh_CN', '物业账单通知用户模板，欠费很久后', '尊敬的租户${targetName}先生/小姐：您好，您已拖欠${dateStr}月服务费：${amount}元，我司至今未收到您的欠款。物业管理公司将按《物业管理条例》的规定，在欠费后次日对贵租户暂停各项服务，出现任何后果，责任自负。我公司并保留通过法律途径追缴的权利。', 0);

-- 短信模板 申请成功过的，物业缴费模块 by wentian @2018/5/10
-- 模板： 尊敬的租户xx先生/小姐：您好，您至今未缴：{2018-5}月服务费共计：xx元。请及时缴纳，谢谢您的配合！
SET @id = ifnull((SELECT max(`id`) from `eh_locale_templates`),0);
set @code_id = ifnull((SELECT max(`code`) from `eh_locale_templates`),0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'sms.default', @code_id:=@code_id+1, 'zh_CN', '短信通知模板，用于欠费前', '尊敬的租户${targetName}先生/小姐：您好，您至今未缴：${dateStr}月服务费共计：${amount}元。请及时缴纳，谢谢您的配合！', 0);
-- 用来查找模板用的
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'asset.sms', @code_id, 'zh_CN', '短信通知模板，用于欠费前', '尊敬的租户${targetName}先生/小姐：您好，您至今未缴：${dateStr}月服务费共计：${amount}元。请及时缴纳，谢谢您的配合！', 0);

-- 999958，999983，999966，999954不缴费，999955不看合同不缴费 by wentian 2018/5/15
set @id = IFNULL((select max(`id`) from `eh_payment_app_views`),0);
INSERT INTO `eh_payment_app_views` (`id`, `namespace_id`, `community_id`, `has_view`, `view_item`, `remark1_type`, `remark1_identifier`, `remark2_type`, `remark2_identifier`, `remark3_type`, `remark3_identifier`) VALUES (@id:=@id+1, 999983, NULL, 0, 'PAY', NULL , NULL , NULL, NULL, NULL, NULL);
INSERT INTO `eh_payment_app_views` (`id`, `namespace_id`, `community_id`, `has_view`, `view_item`, `remark1_type`, `remark1_identifier`, `remark2_type`, `remark2_identifier`, `remark3_type`, `remark3_identifier`) VALUES (@id:=@id+1, 999958, NULL, 0, 'PAY', NULL , NULL , NULL, NULL, NULL, NULL);
INSERT INTO `eh_payment_app_views` (`id`, `namespace_id`, `community_id`, `has_view`, `view_item`, `remark1_type`, `remark1_identifier`, `remark2_type`, `remark2_identifier`, `remark3_type`, `remark3_identifier`) VALUES (@id:=@id+1, 999966, NULL, 0, 'PAY', NULL , NULL , NULL, NULL, NULL, NULL);
INSERT INTO `eh_payment_app_views` (`id`, `namespace_id`, `community_id`, `has_view`, `view_item`, `remark1_type`, `remark1_identifier`, `remark2_type`, `remark2_identifier`, `remark3_type`, `remark3_identifier`) VALUES (@id:=@id+1, 999954, NULL, 0, 'PAY', NULL , NULL , NULL, NULL, NULL, NULL);
INSERT INTO `eh_payment_app_views` (`id`, `namespace_id`, `community_id`, `has_view`, `view_item`, `remark1_type`, `remark1_identifier`, `remark2_type`, `remark2_identifier`, `remark3_type`, `remark3_identifier`) VALUES (@id:=@id+1, 999955, NULL, 0, 'PAY', NULL , NULL , NULL, NULL, NULL, NULL);
INSERT INTO `eh_payment_app_views` (`id`, `namespace_id`, `community_id`, `has_view`, `view_item`, `remark1_type`, `remark1_identifier`, `remark2_type`, `remark2_identifier`, `remark3_type`, `remark3_identifier`) VALUES (@id:=@id+1, 999955, NULL, 0, 'CONTRACT', NULL , NULL , NULL, NULL, NULL, NULL);
INSERT INTO `eh_payment_app_views` (`id`, `namespace_id`, `community_id`, `has_view`, `view_item`, `remark1_type`, `remark1_identifier`, `remark2_type`, `remark2_identifier`, `remark3_type`, `remark3_identifier`) VALUES (@id:=@id+1, 999950, NULL, 0, 'PAY', NULL , NULL , NULL, NULL, NULL, NULL);





