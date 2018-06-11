-- organization owner  by jiarui 
SET @id = (SELECT MAX(id) from eh_locale_strings);

INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'pm', '18002', 'zh_CN', '必填项未填写');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'pm', '18003', 'zh_CN', '楼栋门牌不存在');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'pm', '18004', 'zh_CN', '时间格式不正确');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'pm', '18005', 'zh_CN', '入驻状态格式错误');

