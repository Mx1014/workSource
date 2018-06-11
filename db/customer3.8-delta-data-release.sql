
-- 企业客户管理ERROR code  by jiarui
SET @id = (SELECT MAX(id) from eh_locale_strings);

INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'customer', '10022', 'zh_CN', '为必填项');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'customer', '10023', 'zh_CN', '需按照给定类型填写');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'customer', '10024', 'zh_CN', '格式错误');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'customer', '10025', 'zh_CN', '未知格式错误');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'customer', '10026', 'zh_CN', '格式错误');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'customer', '10027', 'zh_CN', '格式错误');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'customer', '10028', 'zh_CN', '不存在');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'customer', '10029', 'zh_CN', '数字格式错误');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'customer', '10030', 'zh_CN', '日期格式错误');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'customer', '10031', 'zh_CN', '手机号系统中不存在');
