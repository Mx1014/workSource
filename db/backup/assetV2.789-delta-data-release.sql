
-- 通用脚本  
-- ADD BY 丁建民 
-- #30490 资产管理V2.7（产品功能）
SET @id = (SELECT MAX(id) from eh_locale_strings);
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'community', '10017', 'zh_CN', '省份所属填写错误');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'community', '10018', 'zh_CN', '城市所属填写错误');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'community', '10019', 'zh_CN', '区县所属填写错误');
-- END BY 丁建民