-- AUTHOR: 丁建民
-- REMARK: 合同退约提示信息(来自合同2.8)
SET @id = IFNULL((SELECT MAX(`id`) FROM `eh_locale_strings`),0);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'contract', '10012', 'zh_CN', '合同关联的账单不存在');

-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.3 
SET @eh_locale_strings_id = (SELECT MAX(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'assetv2', '10020', 'zh_CN', '此账单组中已存在该费项');
