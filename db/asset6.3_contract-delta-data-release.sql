-- AUTHOR: 丁建民
-- REMARK: 合同退约提示信息(来自合同2.8)
SET @id = IFNULL((SELECT MAX(`id`) FROM `eh_locale_strings`),0);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'contract', '10012', 'zh_CN', '合同关联的账单不存在');
